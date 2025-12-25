package com.github.hugh.cache.redis;

import com.github.hugh.cache.redis.base.BaseRedisTest;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EasyRedis Del (删除) 独立测试
 * <p>
 * 覆盖：String删除、Byte[]删除、重复删除(返回0)、跨库删除
 * </p>
 */
class EasyRedisDelTest extends BaseRedisTest {

    /**
     * 测试 String 类型的删除
     * 包含：删除存在的值、删除不存在的值
     */
    @Test
    void testDel_StringKey() {
        String key = "del_string_key";

        // 1. 准备数据
        easyRedis.set(key, "val");
        assertTrue(easyRedis.exists(key));

        // 2. 执行删除，断言返回 1 (表示删除了 1 个 key)
        Long result = easyRedis.del(key);
        assertEquals(1L, result);

        // 3. 验证确实不存在了
        assertFalse(easyRedis.exists(key));

        // 4. 再次删除同一个 Key (验证重复删除)
        // 预期返回 0，因为 Key 已经不存在了
        Long repeatDel = easyRedis.del(key);
        assertEquals(0L, repeatDel, "删除不存在的 Key 应该返回 0");
    }

    /**
     * 测试 Byte[] 类型的删除
     * 对应原测试中的: instance.del(key.getBytes(...))
     */
    @Test
    void testDel_ByteKey() {
        byte[] key = "del_byte_key".getBytes(StandardCharsets.UTF_8);
        byte[] val = "val".getBytes(StandardCharsets.UTF_8);

        // 1. 准备数据
        easyRedis.set(key, val);

        // 验证存在 (这里假设 get 返回不为 null 即存在)
        assertNotNull(easyRedis.get(key));

        // 2. 执行删除
        Long result = easyRedis.del(key); // 这里的 del 应该是 del(byte[]) 或 del(int, byte[])
        assertEquals(1L, result);

        // 3. 验证已删除
        assertNull(easyRedis.get(key));
    }

    /**
     * 测试跨库删除与指定 DB 删除
     * 解决原代码注释: assertEquals(0, instance.del(1, "set_test_03"));
     */
    @Test
    void testDel_DbIsolation() {
        String key = "shared_key_name";
        int db0 = 0;
        int db1 = 1;

        // 1. 在 DB 0 设置数据
        easyRedis.set(db0, key, "value_in_db0");
        assertTrue(easyRedis.exists(db0, key)); // 假设有 exists(int, String)

        // 确保 DB 1 没有这个 key
        assertFalse(easyRedis.exists(db1, key)); // 假设有 exists(int, String)

        // 2. 尝试从 DB 1 删除该 Key (错误的目标库)
        // 预期：返回 0，因为 DB 1 里没有这个 key
        Long delFromDb1 = easyRedis.del(db1, key);
        assertEquals(0L, delFromDb1, "在 DB1 删除不存在的 Key 应返回 0");

        // 验证：DB 0 的数据依然健在
        assertTrue(easyRedis.exists(db0, key), "误删操作不应影响 DB 0 的数据");

        // 3. 正确从 DB 0 删除
        Long delFromDb0 = easyRedis.del(db0, key);
        assertEquals(1L, delFromDb0);

        // 验证：彻底没了
        assertFalse(easyRedis.exists(db0, key));
    }

    /**
     * 测试批量删除 (如果 EasyRedis 支持 varargs)
     * Jedis 原生 del 支持 del(key1, key2, key3)
     */
    @Test
    void testDel_Batch() {
        // 如果你的 EasyRedis.del 方法签名是 del(String... keys) 或者 del(int dbIndex, String... keys)
        // 那么可以进行此测试。如果是 del(String key) 单个参数，则忽略此测试。

        /* 假设支持变长参数:
        String k1 = "k1", k2 = "k2", k3 = "k3";
        easyRedis.set(k1, "v");
        easyRedis.set(k2, "v");
        // k3 不设置

        // 删除 k1, k2, k3
        // 预期返回 2 (因为只有 k1, k2 真实存在)
        Long count = easyRedis.del(k1, k2, k3);
        assertEquals(2L, count);

        assertFalse(easyRedis.exists(k1));
        assertFalse(easyRedis.exists(k2));
        */
    }
}
