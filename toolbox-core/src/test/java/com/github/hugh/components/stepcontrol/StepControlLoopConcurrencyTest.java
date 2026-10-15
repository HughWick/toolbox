package com.github.hugh.components.stepcontrol;

import com.github.hugh.components.stepcontrol.domain.StepConfig;
import com.github.hugh.components.stepcontrol.domain.StepResult;
import com.github.hugh.components.stepcontrol.domain.StepState;
import com.github.hugh.components.stepcontrol.enums.ActionDirection;
import com.github.hugh.components.stepcontrol.enums.StepReason;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StepControlLoop 并发安全性与性能压力验证")
public class StepControlLoopConcurrencyTest {

    private StepConfig standardConfig;

    @BeforeEach
    void setUp() {
        // REVERSE: 加热模式 (SP > PV 需加大输出)
        standardConfig = StepConfig.builder()
                .direction(ActionDirection.REVERSE)
                .setPoint(50.0)
                .deadBand(0.5)
                .cooldownMs(1000L)      // 冷却时间 1 秒
                .gain(1.0)             // 固定增益 1.0
                .maxStep(10.0)
                .outputMin(0.0)
                .outputMax(100.0)
                .coldStartOutput(0.0)
                .build();
    }

    /**
     * 测试 1：验证单设备极端并发下，互斥锁能否保证“绝对冷却”，杜绝连续动作
     * 修正点：使用 result.isNeedAction() 判定是否触发了动作
     */
    @Test
    @DisplayName("用例 1：同一设备高并发击打，验证临界区排他与冷却锁生效")
    void testSingleDeviceConcurrentCooldownSafety() throws InterruptedException {
        String deviceId = "DEV_CONCURRENT_001";
        StepControlLoop.evictState(deviceId);

        int threadCount = 32;
        int requestsPerThread = 50;
        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);

        List<StepResult> executedResults = Collections.synchronizedList(new ArrayList<>());
        AtomicInteger cooldownSkipCount = new AtomicInteger();
        long frozenTimeMs = 100_000L; // 模拟同一时间戳并发涌入

        for (int i = 0; i < threadCount; i++) {
            pool.submit(() -> {
                try {
                    startLatch.await(); // 所有线程同时起跑
                    for (int j = 0; j < requestsPerThread; j++) {
                        StepResult result = StepControlLoop.compute(deviceId, 20.0, 0.0, standardConfig, frozenTimeMs);

                        // 【修正】：使用 isNeedAction() 代替原 isExecuted()
                        if (result.isNeedAction()) {
                            executedResults.add(result);
                        } else if (result.getReason() == StepReason.COOLDOWN_ACTIVE) {
                            cooldownSkipCount.incrementAndGet();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    endLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        assertTrue(endLatch.await(5, TimeUnit.SECONDS), "并发执行超时");
        pool.shutdown();

        // 验证：在绝对相同的同一个时间戳下，无论多少线程并发涌入，仅允许 1 次动作下发
        assertEquals(1, executedResults.size(), "同一时间戳并发请求，有且仅有 1 个请求能下发动作");
        assertEquals(threadCount * requestsPerThread - 1, cooldownSkipCount.get(), "其余请求必须被精确拦截并进入冷却跳过");
    }

    /**
     * 测试 2：验证 Caffeine 缓存冷启动下的多线程原子性（确保单设备全局只有 1 个 StepState 实例）
     */
    @Test
    @DisplayName("用例 2：多线程冷启动并发访问同一设备，验证 StepState 实例唯一性与无锁逃逸")
    void testColdStartSingleInstanceCreation() throws InterruptedException {
        String deviceId = "DEV_COLD_START_001";
        StepControlLoop.evictState(deviceId);

        int threadCount = 64;
        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        Set<Integer> stateIdentityHashCodes = ConcurrentHashMap.newKeySet();

        for (int i = 0; i < threadCount; i++) {
            pool.submit(() -> {
                try {
                    startLatch.await();
                    StepControlLoop.compute(deviceId, 25.0, standardConfig);
                    StepState state = StepControlLoop.getInternalStateForMonitor(deviceId);
                    if (state != null) {
                        stateIdentityHashCodes.add(System.identityHashCode(state));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    endLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        assertTrue(endLatch.await(5, TimeUnit.SECONDS));
        pool.shutdown();

        // 验证：内存中只存在 1 个唯一的 StepState 引用，没有被重复 new 覆盖
        assertEquals(1, stateIdentityHashCodes.size(), "并发初始化必须保证 State 实例唯一，否则锁失效");
    }

    /**
     * 测试 3：验证多设备（万级并发）各设备独立，不存在全局锁竞争
     * 修正点：使用 res.getNewOutput() 替代 getTargetOutput()
     */
    @Test
    @DisplayName("用例 3：多设备高并发运算，验证各设备锁互不干扰且输出结果独立")
    void testMultiDeviceConcurrencyIsolation() throws InterruptedException {
        int deviceTotal = 500;
        int threads = 16;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(deviceTotal);
        ConcurrentHashMap<String, Double> finalOutputs = new ConcurrentHashMap<>();

        for (int i = 0; i < deviceTotal; i++) {
            final String devId = "DEV_CLUSTER_" + i;
            final double initialPV = 20.0 + (i % 10);
            pool.submit(() -> {
                try {
                    StepResult res = StepControlLoop.compute(devId, initialPV, standardConfig);
                    // 【修正】：取值字段改为 res.getNewOutput()
                    finalOutputs.put(devId, res.getNewOutput());
                } finally {
                    latch.countDown();
                }
            });
        }

        assertTrue(latch.await(10, TimeUnit.SECONDS), "多设备计算未在规定时间内完成");
        pool.shutdown();

        assertEquals(deviceTotal, finalOutputs.size(), "所有设备都应该正确独立完成运算");
        assertNotNull(finalOutputs.get("DEV_CLUSTER_0"));
        assertNotNull(finalOutputs.get("DEV_CLUSTER_1"));
    }

    /**
     * 测试 4：模拟乱序时间戳穿插并发，验证防御机制
     * 修正点：使用 r.isNeedAction() 与 r.isSkipped()
     */
    @Test
    @DisplayName("用例 4：时间戳乱序并发验证，确保时间倒流时系统自愈或防御有效")
    void testTimestampInversionConcurrency() {
        String deviceId = "DEV_TIME_INVERSION";
        StepControlLoop.evictState(deviceId);

        long baseTime = 200_000L;
        // 第一次成功执行
        StepResult r1 = StepControlLoop.compute(deviceId, 10.0, 0.0, standardConfig, baseTime);
        // 【修正】：使用 isNeedAction()
        assertTrue(r1.isNeedAction(), "首次执行应正常触发动作");
        assertEquals(10.0, r1.getDelta(), 0.001);

        // 乱序场景 1：更早的数据延迟到达（baseTime - 500ms）
        StepResult r2 = StepControlLoop.compute(deviceId, 10.0, 0.0, standardConfig, baseTime - 500);
        // 【修正】：直接使用 isSkipped() 快捷方法
        assertTrue(r2.isSkipped(), "时间戳倒流请求应被安全标记为跳过");
        assertEquals(StepReason.COOLDOWN_ACTIVE, r2.getReason());
        assertEquals(0.0, r2.getDelta(), 0.001, "跳过时步长增量必须为 0");

        // 乱序场景 2：冷却期满足后（baseTime + 1000ms）
        StepResult r3 = StepControlLoop.compute(deviceId, 10.0, 0.0, standardConfig, baseTime + 1000);
        assertTrue(r3.isNeedAction(), "冷却周期满足后应正常恢复动作指令");
    }

    /**
     * 测试 5：吞吐量与性能基准压测
     */
    @Test
    @DisplayName("用例 5：性能基准压测（验证无竞争与高吞吐下平均延时与纳秒级性能）")
    void testPerformanceThroughputBenchmark() {
        StepControlLoop loop = new StepControlLoop(standardConfig, 0.0);

        int warmUpCount = 50_000;
        int testCount = 500_000;

        // JIT 预热
        long dummyTime = 1_000_000L;
        for (int i = 0; i < warmUpCount; i++) {
            loop.compute(45.0, dummyTime += 2000);
        }

        // 正式压测计时
        long startNano = System.nanoTime();
        for (int i = 0; i < testCount; i++) {
            StepResult res = loop.compute(45.0, dummyTime += 2000);
            // 确保不会被 JIT 优化为空循环
            if (res.getNewOutput() < 0) {
                System.out.println("unexpected");
            }
        }
        long totalNano = System.nanoTime() - startNano;

        double avgLatencyNs = (double) totalNano / testCount;
        double qps = (testCount / (totalNano / 1_000_000_000.0));

        System.out.printf("[性能压测结果] 连续执行：%d 次, 总耗时: %.2f ms, 单次平均耗时: %.2f 纳秒, 理论 QPS: %,.0f%n",
                testCount, totalNano / 1_000_000.0, avgLatencyNs, qps);

        // 断言：核心计算（含 synchronized 进出锁）平均耗时需小于 5 微秒 (5000 纳秒)
        assertTrue(avgLatencyNs < 5000, "单次核心算法计算耗时过长: " + avgLatencyNs + " ns");
    }
}