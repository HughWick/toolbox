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

    /**
     * 测试：getInstance(jedisPool, dbIndex)
     * 验证：多参数工厂方法的单例性及刷新机制
     */
    @Test
    void testSingletonConcurrencyAndRefresh() throws InterruptedException {
        // ... (保持你原有的代码不变) ...
        // 1. 准备高并发环境
        int threadCount = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        Set<Integer> hashCodeSet = Collections.newSetFromMap(new ConcurrentHashMap<>());
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch endGate = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int dbIndex = (i % 4) + 1;
            executorService.submit(() -> {
                try {
                    startGate.await();
                    // 测试带 dbIndex 的方法
                    EasyRedis instance = EasyRedis.getInstance(jedisPool, dbIndex);
                    hashCodeSet.add(instance.hashCode());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endGate.countDown();
                }
            });
        }
        startGate.countDown();
        endGate.await();
        executorService.shutdown();
        // 验证单例
        // 注意：如果你 EasyRedis 的实现是每个 dbIndex 一个单例，那么这里 size 应该是 4 而不是 1
        // 但根据你原代码的注释 "假设 EasyRedis 是全局单例设计"，这里保留 assertEquals(1, ...)
        // 如果实际是 "dbIndex 不同则实例不同"，请改为 assertEquals(4, hashCodeSet.size());
        // 这里的断言取决于 getInstance(pool, dbIndex) 的具体实现逻辑。
        // 假设原测试逻辑是你验证过的，这里暂不修改。
        System.out.println("多参方法并发实例 HashCode 集合: " + hashCodeSet);
        assertEquals(1, hashCodeSet.size(), "getInstance(pool, index) 应该返回同一个单例");
        // 验证刷新
        int oldHashCode = hashCodeSet.iterator().next();
        EasyRedis refreshedInstance = EasyRedis.getInstance(jedisPool, 1, true);
        int newHashCode = refreshedInstance.hashCode();
        assertNotEquals(oldHashCode, newHashCode);
        EasyRedis currentInstance = EasyRedis.getInstance(jedisPool, 1);
        assertEquals(newHashCode, currentInstance.hashCode());
    }

    /**
     * 测试：getInstance(JedisPool jedisPool)
     * 覆盖方法：public static synchronized EasyRedis getInstance(JedisPool jedisPool)
     * 验证点：
     * 1. 基于 Suppliers.memoize 的懒加载单例是否生效
     * 2. synchronized 是否保证了并发安全
     */
    @Test
    void testSingleton_DefaultInstance() throws InterruptedException {
        // 1. 基础验证：连续调用两次，应当是同一个对象
        EasyRedis instance1 = EasyRedis.getInstance(jedisPool);
        EasyRedis instance2 = EasyRedis.getInstance(jedisPool);
        assertEquals(instance1.hashCode(), instance2.hashCode(), "串行调用 getInstance(pool) 应返回相同实例");
        // 2. 高并发验证
        int threadCount = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        // 用于存储并发获取到的对象 HashCode
        Set<Integer> hashCodeSet = Collections.newSetFromMap(new ConcurrentHashMap<>());
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch endGate = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    // 等待发令枪
                    startGate.await();

                    // --- 调用待测方法 ---
                    EasyRedis instance = EasyRedis.getInstance(jedisPool);
                    hashCodeSet.add(instance.hashCode());
                    // ------------------

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endGate.countDown();
                }
            });
        }
        // 3. 开始并发测试
        startGate.countDown(); // 发令
        endGate.await();       // 等待结束
        executorService.shutdown();
        // 4. 验证结果
        System.out.println("getInstance(pool) 并发获取到的实例 HashCode: " + hashCodeSet);
        // 断言：Set 中只能有 1 个 HashCode，证明 Suppliers.memoize 生效且并发安全
        assertEquals(1, hashCodeSet.size(), "Suppliers.memoize 应该保证全局单例");
        // 5. 验证一致性：并发获取到的应该和最开始串行获取的是同一个
        assertEquals(instance1.hashCode(), hashCodeSet.iterator().next(), "并发获取的实例应与串行获取的一致");
    }
}
