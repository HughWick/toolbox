package com.github.hugh.cache.redis.base;

import com.github.hugh.cache.redis.EasyRedis;
import org.junit.jupiter.api.BeforeEach;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.embedded.RedisServer;

import java.io.IOException;

/**
 * Redis 测试基类
 * 负责：
 * 1. 全局只启动一次 RedisServer (单例模式)
 * 2. 提供统一的 easyRedis 实例
 * 3. 每个 @Test 前自动清空 DB
 */
public abstract class BaseRedisTest {

    private static RedisServer redisServer;
    protected static EasyRedis easyRedis;
    protected static JedisPool jedisPool;

    // 使用静态标志位或检查 server 是否存在，确保只初始化一次
    static {
        try {
            // 1. 启动 Redis (固定端口 6379)
            // 检查端口逻辑略，假设环境纯净
            // 如果你想更稳健，可以加个判断：如果端口被占就不启动了(复用已有的)
            System.out.println(">>> 正在检查/启动 全局 Embedded Redis...");

            // 注意：这里简单的静态块可能在并发下有问题，
            // 但配合 JUnit 5 的生命周期，通常第一次加载类时执行
            if (redisServer == null) {
                redisServer = new RedisServer(6379);
                redisServer.start();

                // 添加 JVM 关闭钩子，确保所有测试跑完后关闭 Redis
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    System.out.println(">>> 测试结束，关闭全局 Redis...");
                    if (redisServer != null) {
                        try {
                            redisServer.stop();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }));
            }

            // 2. 初始化连接池 (全局一份)
            if (jedisPool == null) {
                JedisPoolConfig config = new JedisPoolConfig();
                config.setMaxTotal(50);
                config.setMaxIdle(10);
                jedisPool = new JedisPool(config, "localhost", 6379);
            }

            // 3. 初始化 EasyRedis
            if (easyRedis == null) {
                easyRedis = new EasyRedis(jedisPool);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("全局 Redis 启动失败", e);
        }
    }

    /**
     * 每个测试方法执行前，清空当前 DB，确保隔离性
     */
    @BeforeEach
    public void cleanDb() {
        try (var jedis = jedisPool.getResource()) {
            jedis.flushAll();
        }
    }
}
