package com.github.hugh.cache.redis;

import com.github.hugh.cache.redis.base.BaseRedisTest;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EasyRedis String/Byte 基本操作测试
 * <p>
 * 针对 set/get 方法的深度测试，包含多库、字节数组、过期时间验证
 * </p>
 */
class EasyRedisStringTest extends BaseRedisTest {

    /**
     * 测试基础 Set/Get 以及过期时间
     * 对应原：setTest 的前半部分
     */
    @Test
    void testSetStringBasicAndExpire() {
        String key = "set_test_01";
        String value = "sdjfhkj";
        // 1. 基础 Set 测试
        String set = easyRedis.set(key, value);
        assertEquals("OK", set);
        assertEquals(value, easyRedis.get(key));
        // 2. 测试带过期时间的 Set
        String expireKey = "set_test_02";
        int expireSeconds = 1000;
        easyRedis.set(expireKey, value, expireSeconds);
        // 验证值是否存入
        assertEquals(value, easyRedis.get(expireKey));
        // 验证过期时间是否设置成功 (允许1-2秒的误差)
        long ttl = easyRedis.ttl(expireKey);
        assertTrue(ttl > 990 && ttl <= 1000, "TTL 应该接近 1000 秒");
    }

    /**
     * 测试指定 DB 的操作
     * 对应原：setTest 中 dbIndex 部分
     */
    @Test
    void testSetInSpecificDb() {
        int targetDb = 13;
        String key = "set_test_03";
        String value = "sdjfhkj";
        // 在 DB 13 写入
        easyRedis.set(targetDb, key, value, 1000);
        // 验证 DB 13 能取到
        assertEquals(value, easyRedis.get(targetDb, key));
        // 验证默认 DB (0) 取不到该值，确保库隔离生效
        assertNull(easyRedis.get(key), "DB 0 不应该包含 DB 13 的数据");
        // 验证 DB 13 的 TTL
        assertTrue(easyRedis.ttl(targetDb, key) > 0);
    }

    /**
     * 测试 Byte 数组操作
     * 对应原：getTest 中的 byte 操作
     */
    @Test
    void testByteOperations() {
        // 准备数据
        String rawKey = "byte_test_01";
        String rawValue = "sdjfhkj";
        byte[] byteKey = rawKey.getBytes(StandardCharsets.UTF_8);
        byte[] byteValue = rawValue.getBytes(StandardCharsets.UTF_8);
        // 初始状态：Key 不存在
        // 原代码用 Arrays.toString() 判空，不够优雅，直接用 assertNull
        assertNull(easyRedis.get(byteKey), "初始状态 get(byte[]) 应该返回 null");
        // 写入 Byte 数据
        String setBytesResult = easyRedis.set(byteKey, byteValue);
        assertEquals("OK", setBytesResult);
        // 读取并验证
        byte[] resultBytes = easyRedis.get(byteKey); // 默认 DB
        assertNotNull(resultBytes);
        assertArrayEquals(byteValue, resultBytes, "取出的字节数组应与存入的一致");
        // 验证转回 String 是否正确
        assertEquals(rawValue, new String(resultBytes, StandardCharsets.UTF_8));
        // 测试指定 DB 的 Byte 操作
        int dbIndex = 2;
        easyRedis.set(dbIndex, byteKey, byteValue);
        // 验证：原代码中手动构造的 byte[] {115, 100...} 其实就是 "sdjfhkj" 的 ASCII 码
        // 这里为了代码可读性，我们直接对比 byteValue，效果是一样的
        assertArrayEquals(byteValue, easyRedis.get(dbIndex, byteKey));
        // 5. 删除测试
        Long delCount = easyRedis.del(dbIndex, byteKey);
        assertEquals(1L, delCount);
        assertNull(easyRedis.get(dbIndex, byteKey), "删除后应该返回 null");
    }

    /**
     * 测试 Key 不存在的场景
     * 对应原：getTest 的前半部分
     */
    @Test
    void testGetNonExistent() {
        String key = "not_exist_key";
        // 1. 默认 DB
        assertNull(easyRedis.get(key));
        // 2. 指定 DB
        assertNull(easyRedis.get(1, key));
        // 3. Byte 类型 Key
        assertNull(easyRedis.get(key.getBytes(StandardCharsets.UTF_8)));
    }
}
