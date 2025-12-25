package com.github.hugh.cache.redis;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EasyRedis Key 操作相关测试
 * <p>
 * 覆盖 getAllKeys (scan/keys) 和 exists 方法
 * </p>
 */
class EasyRedisKeysTest {

    private static RedisServer redisServer;
    private static JedisPool jedisPool;
    private static EasyRedis easyRedis;

    // =================== 环境初始化 ===================

    @BeforeAll
    static void startRedis() throws IOException {
        redisServer = new RedisServer(6379);
        try {
            redisServer.start();
        } catch (Exception e) {
            System.err.println("Redis启动警告: " + e.getMessage());
        }
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        jedisPool = new JedisPool(config, "localhost", 6379);
        easyRedis = new EasyRedis(jedisPool);
    }

    @AfterAll
    static void stopRedis() throws IOException {
        if (jedisPool != null) jedisPool.close();
        if (redisServer != null) redisServer.stop();
    }

    @BeforeEach
    void cleanDb() {
        try (var jedis = jedisPool.getResource()) {
            jedis.flushAll(); // 保证每个测试方法开始时环境是干净的
        }
    }

    // =================== 改进后的测试用例 ===================

    /**
     * 测试模糊查询所有 Keys
     * 原方法：getAllKeys
     */
    @Test
    void testGetAllKeys_PatternAndIsolation() {
        // 1. 验证空库查询
        Set<String> emptyKeys = easyRedis.getAllKeys("*");
        // 兼容 null 或 empty set，视你的 ListUtils 实现而定，标准 Jedis 返回空集合
        assertTrue(emptyKeys == null || emptyKeys.isEmpty(), "初始状态应该是空的");

        // 2. 准备数据：写入不同模式的 Key
        easyRedis.set("user:1001", "Tom");
        easyRedis.set("user:1002", "Jerry");
        easyRedis.set("order:2001", "OrderData");

        // 3. 验证全量查询 (*)
        Set<String> allKeys = easyRedis.getAllKeys("*");
        assertNotNull(allKeys);
        assertEquals(3, allKeys.size(), "应该查到所有 3 个 keys");
        assertTrue(allKeys.contains("user:1001"));

        // 4. 验证模糊匹配 (user:*)
        Set<String> userKeys = easyRedis.getAllKeys("user:*");
        assertEquals(2, userKeys.size(), "应该只查到 2 个 user keys");
        assertTrue(userKeys.contains("user:1001"));
        assertTrue(userKeys.contains("user:1002"));
        assertFalse(userKeys.contains("order:2001"));
        // 5. 验证 DB 隔离性
        int otherDb = 14; // 原测试用例中的 index
        // 在 DB 14 写入数据
        easyRedis.set(otherDb, "user:db14", "data");

        // 在默认 DB 查，不应该查到 DB 14 的数据
        Set<String> defaultDbKeys = easyRedis.getAllKeys("user:*");
        assertEquals(2, defaultDbKeys.size());

        // 在 DB 14 查，应该查到 1 个
        Set<String> db14Keys = easyRedis.getAllKeys(otherDb, "*");
        assertEquals(1, db14Keys.size());
        assertTrue(db14Keys.contains("user:db14"));
    }

    /**
     * 测试 Key 是否存在
     * 原方法：existsTest
     */
    @Test
    void testExists_BasicAndCrossDb() {
        String key = "exists_test_key";
        // 1. 初始验证：不存在
        assertFalse(easyRedis.exists(key), "初始化时 key 不应存在");
        // 2. 写入后验证：存在
        easyRedis.set(key, "abc");
        assertTrue(easyRedis.exists(key), "写入后 key 应当存在");

        // 3. 删除后验证：不存在
        assertEquals(1, easyRedis.del(key));
        assertFalse(easyRedis.exists(key), "删除后 key 不应存在");

        // ================= 跨库测试 (补充完善点) =================
        int targetDb = 5;
        String dbKey = "db_unique_key";
        // 在 DB 5 写入
        easyRedis.set(targetDb, dbKey, "val");
        // 验证：DB 5 存在
        assertTrue(easyRedis.exists(targetDb, dbKey), "DB 5 中应当存在该 key (如果不提供exists(int, String)重载，需确认是否有select逻辑)");

        // 验证：默认 DB (0) 不存在 (确保没有查错库)
        // 注意：如果你的 EasyRedis 没有 exists(int dbIndex, String key) 方法，
        // 这里验证默认 DB 的 exists(key) 返回 false 即可证明隔离性。
        assertFalse(easyRedis.exists(dbKey), "DB 0 中不应当存在 DB 5 的 key");
    }
}
