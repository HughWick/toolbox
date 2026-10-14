package com.github.hugh.components.stepcontrol;

import com.github.hugh.components.stepcontrol.domain.StepConfig;
import com.github.hugh.components.stepcontrol.domain.StepResult;
import com.github.hugh.components.stepcontrol.enums.ActionDirection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 【IoT 核心回路专用压测】StepControlLoop 50万容量极限与内存安全防爆基准测试
 *
 * <p>【运行建议 VM 参数】：
 * -Xms2048m -Xmx2048m -XX:+UseG1GC
 * （固定堆内存以消除 JVM 动态扩容对内存统计与执行耗时的干扰）
 */
@Tag("stress")
class StepControlLoopStressTest {

    private static final DecimalFormat DF = new DecimalFormat("#,###.00");
    private static final DecimalFormat INT_FMT = new DecimalFormat("#,###");

    // =========================================================================
    // 压测一：50万设备满载内存基准线评估 (防 OOM 核心指标)
    // =========================================================================

    @Test
    @DisplayName("【压测 01·内存基线】50万满载设备实例内存占用与单设备成本评估")
    void test500kDevices_MemoryFootprintAndSafety() {
        clearStateCache(); // 压测前清理缓存

        final int targetCapacity = 500_000;
        final StepConfig config = StepConfig.of(500.0, 15.0, ActionDirection.REVERSE);
        config.setCooldownMs(0L); // 压测关闭冷却，模拟真实瞬时并发写入

        System.out.println("\n=========================================================================================");
        System.out.printf("       [容量压测 01] 正在灌入 %s 台虚拟设备，评估 Caffeine 缓存内存基线与对象开销...\n", INT_FMT.format(targetCapacity));
        System.out.println("=========================================================================================");

        // 1. 深度 GC 获取洁净初始堆内存
        triggerDeepGC();
        long memBeforeBytes = getUsedHeapMemory();
        long startTime = System.currentTimeMillis();

        // 2. 灌入 50万 真实业务设备（使用典型 IoT 设备 ID 格式）
        for (int i = 1; i <= targetCapacity; i++) {
            // 典型设备ID命名（如：dev_gw_room_001_sensor_999999）
            String deviceId = "dev_sensor_iot_node_" + i;
            double simulatedLux = 100.0 + (i % 400); // 随机变化照度

            // 触发真实算法计算并沉淀缓存
            StepControlLoop.compute(deviceId, simulatedLux, config);

            // 每 10 万台打点观测一次
            if (i % 100_000 == 0) {
                System.out.printf(" -> 已灌入 %s 台设备状态 (当前进度: %d%%)...\n",
                        INT_FMT.format(i), (i * 100 / targetCapacity));
            }
        }

        long elapsedMs = System.currentTimeMillis() - startTime;

        // 3. 统计注入后真实内存占用 (GC 清理掉计算过程中的短命临时对象，保留常驻缓存)
        triggerDeepGC();
        long memAfterBytes = getUsedHeapMemory();

        long netMemoryUsedBytes = Math.max(0, memAfterBytes - memBeforeBytes);
        double netMemoryMB = netMemoryUsedBytes / (1024.0 * 1024.0);
        double perDeviceBytes = (double) netMemoryUsedBytes / targetCapacity;
        double throughputTPS = (targetCapacity * 1000.0) / elapsedMs;

        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.printf(" [压测结果报告 - 50万设备常驻缓存]:\n");
        System.out.printf(" - 写入总耗时       : %d ms (平均 TPS: %s ops/s)\n", elapsedMs, INT_FMT.format(throughputTPS));
        System.out.printf(" - 灌入前堆内存占用 : %s MB\n", DF.format(memBeforeBytes / 1024.0 / 1024.0));
        System.out.printf(" - 灌入后堆内存占用 : %s MB\n", DF.format(memAfterBytes / 1024.0 / 1024.0));
        System.out.printf(" - 50万设备净增内存 : %s MB\n", DF.format(netMemoryMB));
        System.out.printf(" - 单设备平均开销   : %s 字节 / 台 (含 Caffeine 内部 Node + Key 字符串 + State 实例)\n", DF.format(perDeviceBytes));
        System.out.println("=========================================================================================\n");

        // 4. 生产核心红线断言:
        // [防爆断言 1]: 单台设备状态占用必须在 600 字节以内（正常一般在 150B ~ 300B 之间）
        assertTrue(perDeviceBytes < 600.0,
                String.format("单设备状态对象过重！平均占用: %.1f 字节，超出安全上限 600B！", perDeviceBytes));

        // [防爆断言 2]: 50万设备常驻堆内存总占用必须严格控制在 300MB 以内（通常在 80MB ~ 150MB 之间）
        assertTrue(netMemoryMB < 300.0,
                String.format("50万设备缓存内存暴涨！总占用: %.2f MB，已达线上预警线 (300MB)！", netMemoryMB));

        // [性能断言 3]: 基础吞吐吞吐能力不低于 50,000 TPS
        assertTrue(throughputTPS > 50_000.0,
                String.format("写入吞吐量偏低: %.1f TPS，请排查状态机是否存在严重耗时！", throughputTPS));
    }

    // =========================================================================
    // 压测二：多线程高并发写压测 (验证锁竞争、并发安全性与吞吐极限)
    // =========================================================================

    @Test
    @DisplayName("【压测 02·并发吞吐】多线程并发写入 50万 次 (模拟多网关高频并发报文)")
    void testHighConcurrencyMultiThreadedLoad() throws InterruptedException {
        clearStateCache();

        final int totalRequests = 500_000;
        final int concurrencyThreads = Runtime.getRuntime().availableProcessors() * 2;
        final int requestsPerThread = totalRequests / concurrencyThreads;

        final StepConfig config = StepConfig.of(500.0, 15.0, ActionDirection.REVERSE);
        config.setCooldownMs(0L);

        ExecutorService executor = Executors.newFixedThreadPool(concurrencyThreads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(concurrencyThreads);

        AtomicInteger successCounter = new AtomicInteger(0);
        AtomicLong maxLatencyNanos = new AtomicLong(0);

        System.out.println("\n=========================================================================================");
        System.out.printf("       [并发压测 02] 启动 %d 个并发线程，共同压测 %s 次算法调度...\n",
                concurrencyThreads, INT_FMT.format(totalRequests));
        System.out.println("=========================================================================================");

        long startTime = System.currentTimeMillis();

        for (int t = 0; t < concurrencyThreads; t++) {
            final int threadIndex = t;
            executor.submit(() -> {
                try {
                    startLatch.await(); // 所有线程对齐，瞬时开火
                    for (int i = 0; i < requestsPerThread; i++) {
                        String deviceId = "dev_node_" + threadIndex + "_" + i;
                        long reqStart = System.nanoTime();

                        // 真实并发调用
                        StepResult res = StepControlLoop.compute(deviceId, 300.0, config);

                        long cost = System.nanoTime() - reqStart;
                        maxLatencyNanos.accumulateAndGet(cost, Math::max);

                        if (res != null) {
                            successCounter.incrementAndGet();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    finishLatch.countDown();
                }
            });
        }

        startLatch.countDown(); // 鸣枪起跑
        boolean completed = finishLatch.await(60, TimeUnit.SECONDS); // 最多等待 60 秒
        long elapsedMs = System.currentTimeMillis() - startTime;
        executor.shutdown();

        assertTrue(completed, "多线程并发压测在 60 秒内未完成，可能存在并发死锁或严重锁竞争！");
        assertEquals(totalRequests, successCounter.get(), "并发调用过程中发生丢失或未捕获异常！");

        double tps = (totalRequests * 1000.0) / elapsedMs;
        double maxLatencyMs = maxLatencyNanos.get() / 1_000_000.0;

        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.printf(" [压测结果报告 - 并发性能]:\n");
        System.out.printf(" - 参与并发线程数   : %d 个\n", concurrencyThreads);
        System.out.printf(" - 总处理请求量     : %s 次\n", INT_FMT.format(totalRequests));
        System.out.printf(" - 总耗时           : %d ms\n", elapsedMs);
        System.out.printf(" - 并发综合吞吐 TPS : %s ops/s\n", INT_FMT.format(tps));
        System.out.printf(" - 单次最长尖刺耗时 : %.3f ms\n", maxLatencyMs);
        System.out.println("=========================================================================================\n");

        // 吞吐断言：多线程并行必须跑出 10万 TPS 以上
        assertTrue(tps > 100_000, String.format("高并发吞吐过低: %.1f TPS，请检查内部是否有重量级同步锁！", tps));
    }

    // =========================================================================
    // 压测三：突破 50万 物理上限时的驱逐（Eviction）防爆验证
    // =========================================================================

    @Test
    @DisplayName("【压测 03·防溢出驱逐】写入 55万 设备强制触发容量淘汰，验证内存绝不超限")
    void testCacheEvictionWhenExceeding500k() {
        clearStateCache();

        final int overflowCount = 550_000; // 故意超出 50,000 台
        final StepConfig config = StepConfig.of(500.0, 15.0, ActionDirection.REVERSE);
        config.setCooldownMs(0L);

        System.out.println("\n=========================================================================================");
        System.out.printf("       [防溢出压测 03] 故意灌入 %s 台设备（超出上限 50,000），验证 W-TinyLFU 淘汰保护机制...\n",
                INT_FMT.format(overflowCount));
        System.out.println("=========================================================================================");

        for (int i = 1; i <= overflowCount; i++) {
            StepControlLoop.compute("overflow_device_" + i, 250.0, config);
        }

        // 获取底层真实的 Caffeine Cache 实例，触发 cleanUp() 执行异步驱逐回收维护
        com.github.benmanes.caffeine.cache.Cache<String, ?> cache = getInternalCache();
        assertNotNull(cache, "未能获取到 StepControlLoop 的底层 STATE_CACHE 引用！");
        cache.cleanUp(); // Caffeine 淘汰为异步批处理，强制让其执行完驱逐逻辑

        long currentSize = cache.estimatedSize();
        System.out.printf(" -> 实际灌入量: %s | 经过自动淘汰后实际存活缓存量: %s (配置上限: 500,000)\n",
                INT_FMT.format(overflowCount), INT_FMT.format(currentSize));

        // 断言：缓存大小被牢牢压在 500,000 左右（容许 1% 的 W-TinyLFU 估算窗口公差）
        assertTrue(currentSize <= 505_000,
                String.format("Caffeine maximumSize 约束失效！当前容量: %d，超过了 500,000 上限！", currentSize));

        System.out.println(" -> [OK] 内存淘汰机制生效，多余的 50,000 个冷设备已被安全剔除，未发生无节制蔓延！");
        System.out.println("=========================================================================================\n");
    }

    // =========================================================================
    // 辅助测试支撑工具（内存抓取、GC清理、反射探针）
    // =========================================================================

    /**
     * 强力多次触发 GC，尽量剥离短命垃圾对象，抓取真实常驻内存
     */
    private static void triggerDeepGC() {
        for (int i = 0; i < 3; i++) {
            System.gc();
            System.runFinalization();
            try {
                Thread.sleep(150);
            } catch (InterruptedException ignored) {
            }
        }
    }

    /**
     * 获取当前 JVM 真实的堆内存已用字节数
     */
    private static long getUsedHeapMemory() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        return memoryMXBean.getHeapMemoryUsage().getUsed();
    }

    /**
     * 通过反射清理缓存（保证每次压测不受前后用例干扰）
     */
    @SuppressWarnings("unchecked")
    private static void clearStateCache() {
        try {
            com.github.benmanes.caffeine.cache.Cache<String, ?> cache = getInternalCache();
            if (cache != null) {
                cache.invalidateAll();
                cache.cleanUp();
            }
        } catch (Exception e) {
            System.err.println("重置状态缓存失败: " + e.getMessage());
        }
    }

    /**
     * 反射探测获取 StepControlLoop 中的 STATE_CACHE
     */
    @SuppressWarnings("unchecked")
    private static com.github.benmanes.caffeine.cache.Cache<String, ?> getInternalCache() {
        try {
            Field field = StepControlLoop.class.getDeclaredField("STATE_CACHE");
            field.setAccessible(true);
            return (com.github.benmanes.caffeine.cache.Cache<String, ?>) field.get(null);
        } catch (Exception e) {
            return null;
        }
    }
}
