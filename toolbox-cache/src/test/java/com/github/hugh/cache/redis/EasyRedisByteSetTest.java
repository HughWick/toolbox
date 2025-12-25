package com.github.hugh.cache.redis;

import com.github.hugh.cache.redis.base.BaseRedisTest;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EasyRedis Byte[] 数组 Set 操作测试
 * <p>
 * 覆盖：
 * 1. set(byte[], byte[]) - 基础存储
 * 2. set(byte[], byte[], int) - 带过期时间
 * 3. set(int, byte[], byte[], int) - 指定库 + 过期时间
 * </p>
 */
class EasyRedisByteSetTest extends BaseRedisTest {

    /**
     * 测试基础 Byte Set
     * 对应：easyRedis.set(key.getBytes(), value.getBytes())
     */
    @Test
    void testSetBytes_Basic() {
        byte[] key = "byteKey_01".getBytes(StandardCharsets.UTF_8);
        byte[] value = "value_data".getBytes(StandardCharsets.UTF_8);

        // 1. 执行 Set
        String result = easyRedis.set(key, value);
        assertEquals("OK", result);

        // 2. 验证 Get (确保数据正确存入)
        byte[] resultBytes = easyRedis.get(key);
        assertNotNull(resultBytes);
        assertArrayEquals(value, resultBytes, "取出的字节数组应与存入的一致");

        // 3. 验证默认无过期时间 (TTL = -1)
        // 注意：如果你没有 ttl(byte[]) 方法，可以用 String 版本的 ttl 验证
        String keyStr = new String(key, StandardCharsets.UTF_8);
        assertEquals(-1, (long) easyRedis.ttl(keyStr));
    }

    /**
     * 测试带过期时间的 Byte Set
     * 对应：easyRedis.set(key2.getBytes(), value.getBytes(), 300)
     */
    @Test
    void testSetBytes_WithExpire() {
        byte[] key = "byteKey_time".getBytes(StandardCharsets.UTF_8);
        byte[] value = "value_time".getBytes(StandardCharsets.UTF_8);
        int expireSeconds = 300;

        // 1. 执行带时间的 Set
        String result = easyRedis.set(key, value, expireSeconds);
        assertEquals("OK", result);

        // 2. 验证数据存在
        assertArrayEquals(value, easyRedis.get(key));

        // 3. 验证过期时间是否生效
        String keyStr = new String(key, StandardCharsets.UTF_8);
        long ttl = easyRedis.ttl(keyStr);
        // 允许少许误差，但必须在合理范围内
        assertTrue(ttl > 290 && ttl <= 300, "TTL 应当接近 300 秒");
    }

    /**
     * 测试指定 DB 的 Byte Set + 过期时间
     * 对应：easyRedis.set(0, key2.getBytes(), value.getBytes(), 300)
     * (此处修改为使用 DB 3 进行测试，以验证隔离性)
     */
    @Test
    void testSetBytes_SpecificDbAndExpire() {
        int targetDb = 3; // 使用 DB 3
        byte[] key = "byteKey_db3".getBytes(StandardCharsets.UTF_8);
        byte[] value = "value_db3".getBytes(StandardCharsets.UTF_8);
        int expireSeconds = 300;

        // 1. 在 DB 3 执行 Set
        String result = easyRedis.set(targetDb, key, value, expireSeconds);
        assertEquals("OK", result);

        // 2. 验证 DB 3 中能取到数据
        byte[] db3Value = easyRedis.get(targetDb, key);
        assertArrayEquals(value, db3Value);

        // 3. 验证默认 DB (0) 中取不到数据 (隔离性验证)
        byte[] db0Value = easyRedis.get(key); // 默认查 DB 0
        assertNull(db0Value, "DB 0 不应包含 DB 3 的数据");

        // 4. 验证 DB 3 中的 TTL
        String keyStr = new String(key, StandardCharsets.UTF_8);
        long ttl = easyRedis.ttl(targetDb, keyStr); // 假设有 ttl(int db, String key)
        assertTrue(ttl > 290 && ttl <= 300);
    }
}
