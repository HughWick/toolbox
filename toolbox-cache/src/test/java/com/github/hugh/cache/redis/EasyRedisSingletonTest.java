package com.github.hugh.cache.redis;

import com.github.hugh.cache.redis.base.BaseRedisTest;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * EasyRedis 单例模式与并发安全性测试
 * <p>
 * 验证目标：
 * 1. 多线程并发调用 getInstance 是否返回同一个对象（单例性）。
 * 2. 传入 refresh=true 时，是否能正确创建新对象。
 * </p>
 */
class EasyRedisSingletonTest extends BaseRedisTest {

    @Test
    void testSingletonConcurrencyAndRefresh() throws InterruptedException {
        // 1. 准备高并发环境
        int threadCount = 20; // 模拟 20 个线程同时访问
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        // 使用线程安全的 Set 来存储获取到的 HashCode
        // 如果是单例，最终这个 Set 的 size 应该为 1
        Set<Integer> hashCodeSet = Collections.newSetFromMap(new ConcurrentHashMap<>());

        // 使用 CountDownLatch 制造“并发起跑线”
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch endGate = new CountDownLatch(threadCount);

        // 2. 提交任务
        for (int i = 0; i < threadCount; i++) {
            // 模拟不同的 DB Index (1-4 循环)，验证 getInstance(pool, index) 是否依然返回同一个单例
            // (假设 EasyRedis 是全局单例设计，而非每个 DB 一个实例)
            final int dbIndex = (i % 4) + 1;

            executorService.submit(() -> {
                try {
                    // 等待发令枪响，所有线程卡在这里
                    startGate.await();

                    // --- 并发执行区开始 ---
                    EasyRedis instance = EasyRedis.getInstance(jedisPool, dbIndex);
                    hashCodeSet.add(instance.hashCode());
                    // --- 并发执行区结束 ---

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endGate.countDown(); // 完成一个减少一个
                }
            });
        }

        // 3. 鸣枪开跑！
        startGate.countDown(); // 所有线程同时开始执行 getInstance
        endGate.await(); // 等待所有线程跑完
        executorService.shutdown();

        // 4. 【验证阶段一】：单例一致性
        System.out.println("并发获取到的实例 HashCode 集合: " + hashCodeSet);
        assertEquals(1, hashCodeSet.size(), "在单例模式下，所有线程获取到的 HashCode 应该完全一致");

        int oldHashCode = hashCodeSet.iterator().next();

        // 5. 【验证阶段二】：强制刷新 (refresh = true)
        // 此时在主线程执行，模拟“刷新单例”操作
        EasyRedis refreshedInstance = EasyRedis.getInstance(jedisPool, 1, true);
        int newHashCode = refreshedInstance.hashCode();

        System.out.println("旧实例 HashCode: " + oldHashCode);
        System.out.println("新实例 HashCode: " + newHashCode);

        assertNotEquals(oldHashCode, newHashCode, "刷新后应该生成一个新的实例对象");

        // 6. 【验证阶段三】：再次获取
        // 再次调用普通 getInstance，应该获取到刚才刷新后的那个新实例
        EasyRedis currentInstance = EasyRedis.getInstance(jedisPool, 1);
        assertEquals(newHashCode, currentInstance.hashCode(), "再次获取应该拿到刷新后的新实例");
    }
}
