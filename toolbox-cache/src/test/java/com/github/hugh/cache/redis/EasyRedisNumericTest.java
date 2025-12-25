package com.github.hugh.cache.redis;

import com.github.hugh.cache.redis.base.BaseRedisTest;
import com.github.hugh.exception.ToolboxException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EasyRedis 数值计算与过期时间测试
 * <p>
 * 覆盖 TTL 逻辑 (-1/-2/正数) 和 Incr 自增逻辑 (正常/多库/异常)
 * </p>
 */
class EasyRedisNumericTest extends BaseRedisTest {

    /**
     * TTL (Time To Live) 深度测试
     * 原方法：tllTest
     * <p>
     * 补充点：
     * 1. 验证 -2 (不存在)
     * 2. 验证 -1 (永不过期)
     * 3. 验证 > 0 (设置了过期时间)
     * 4. 验证 expire 方法能否成功修改 TTL
     */
    @Test
    void testTtl_Lifecycle() {
        String key = "ttl_test_key";
        // 1. Key 不存在时，TTL 应返回 -2
        assertEquals(-2, (long) easyRedis.ttl(key), "Key不存在应返回 -2");
        // 2. Set 一个永不过期的 Key
        easyRedis.set(key, "value");
        assertEquals(-1, (long) easyRedis.ttl(key), "无过期时间的 Key 应返回 -1");
        // 3. 手动设置过期时间 (expire)
        Long expireResult = easyRedis.expire(key, 100);
        assertEquals(1, expireResult, "设置过期时间成功应返回 1");
        // 4. 验证 TTL 变成了正数 (接近 100)
        long ttl = easyRedis.ttl(key);
        assertTrue(ttl > 0 && ttl <= 100, "TTL 应该在 0 到 100 之间");
        // 5. Set 一个自带过期时间的 Key
        String exKey = "ex_key";
        easyRedis.set(exKey, "val", 50);
        long exTtl = easyRedis.ttl(exKey);
        assertTrue(exTtl > 0 && exTtl <= 50);
        // 6. 验证跨库 TTL (DB 1)
        int otherDb = 1;
        assertEquals(-2, (long) easyRedis.ttl(otherDb, key), "DB 1 中该 Key 不存在，应返回 -2");
        easyRedis.set(otherDb, key, "db1_val", 200);
        long db1Ttl = easyRedis.ttl(otherDb, key);
        assertTrue(db1Ttl > 190 && db1Ttl <= 200);
    }

    /**
     * Incr (自增) 深度测试
     * 原方法：incrTest
     * <p>
     * 补充点：
     * 1. 基础自增 1 -> 2
     * 2. 多库隔离 (DB 0 的自增不影响 DB 1)
     * 3. 异常测试 (对非数字字符串自增应报错)
     */
    @Test
    void testIncr_LogicAndIsolation() {
        String key = "counter_key";
        // 1. 针对新 Key 自增，Redis 默认初始化为 0 并加 1 -> 结果 1
        Long val1 = easyRedis.incr(key);
        assertEquals(1, val1.intValue());
        // 2. 再次自增 -> 结果 2
        Long val2 = easyRedis.incr(key);
        assertEquals(2, val2.intValue());
        // 3. 验证多库隔离
        int otherDb = 2;
        // 在 DB 2 对同名 Key 自增，应该从 1 开始，不受 DB 0 影响
        Long db2Val = easyRedis.incr(otherDb, key); // 假设你有 incr(int dbIndex, String key)
        assertEquals(1, db2Val.intValue(), "DB 2 的计数器应该独立从 1 开始");
        // 验证 DB 0 还是 2
        String db0ValStr = easyRedis.get(key);
        assertEquals("2", db0ValStr);
        // 4. 清理
        easyRedis.del(key);
        easyRedis.del(otherDb, key);
    }

    /**
     * Incr 的异常边界测试
     * 如果对一个 "abc" 字符串进行自增，Redis 会报错
     */
    @Test
    void testIncr_Exception() {
        String key = "str_key";
        easyRedis.set(key, "abc"); // 设置一个非数字的值
        // 验证是否抛出异常 (ToolboxException 或 JedisDataException)
        assertThrows(ToolboxException.class, () -> {
            easyRedis.incr(key);
        }, "对非数字字符串进行 incr 操作应该抛出异常");
    }
}