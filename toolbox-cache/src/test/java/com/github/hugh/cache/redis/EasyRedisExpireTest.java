package com.github.hugh.cache.redis;

import com.github.hugh.cache.redis.base.BaseRedisTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EasyRedis Expire (过期时间) 独立测试
 * <p>
 * 覆盖：
 * 1. 为无过期时间的 Key 设置过期时间
 * 2. 更新已有 Key 的过期时间
 * 3. 对不存在的 Key 设置过期时间 (应失败)
 * 4. 指定 DB 的过期时间设置
 * </p>
 */
class EasyRedisExpireTest extends BaseRedisTest {

    /**
     * 场景一：基础流程测试
     * set (无过期) -> expire -> 验证 ttl
     */
    @Test
    void testExpire_BasicFlow() {
        String key = "expire_test_basic";
        String value = "val";

        // 1. 设置一个永久 Key
        easyRedis.set(key, value);
        // 验证初始 TTL 为 -1 (永久)
        assertEquals(-1, (long) easyRedis.ttl(key));

        // 2. 设置过期时间为 100 秒
        // 返回 1 表示设置成功
        Long result = easyRedis.expire(key, 100);
        assertEquals(1L, result);

        // 3. 验证 TTL 是否生效
        long ttl = easyRedis.ttl(key);
        // 允许 1-2 秒的执行时间误差
        assertTrue(ttl > 95 && ttl <= 100, "TTL 应当接近 100 秒，实际: " + ttl);
    }

    /**
     * 场景二：更新过期时间
     * set (有过期) -> expire (修改) -> 验证 ttl 变化
     */
    @Test
    void testExpire_UpdateExisting() {
        String key = "expire_test_update";

        // 1. 初始设置 50 秒过期
        easyRedis.set(key, "val", 50);
        long initialTtl = easyRedis.ttl(key);
        assertTrue(initialTtl > 0 && initialTtl <= 50);

        // 2. 修改过期时间为 1999 秒
        Long result = easyRedis.expire(key, 1999);
        assertEquals(1L, result);

        // 3. 验证 TTL 变成了 1999 附近
        long newTtl = easyRedis.ttl(key);
        assertTrue(newTtl > 1990 && newTtl <= 1999, "TTL 应当被延长至 1999 秒，实际: " + newTtl);
    }

    /**
     * 场景三：边界测试 - 对不存在的 Key 设置过期时间
     */
    @Test
    void testExpire_NonExistentKey() {
        String key = "not_exist_key";

        // 1. 确保 Key 不存在
        assertFalse(easyRedis.exists(key));

        // 2. 尝试 expire
        // Redis 规定：如果 Key 不存在，expire 返回 0
        Long result = easyRedis.expire(key, 100);
        assertEquals(0L, result, "对不存在的 Key 设置过期时间应返回 0");
    }

    /**
     * 场景四：多库隔离测试
     * 验证指定 DB 的 expire 操作是否正确
     */
    @Test
    void testExpire_DbIsolation() {
        int targetDb = 5;
        String key = "db5_expire_key";

        // 1. 在 DB 5 设置数据（无过期）
        easyRedis.set(targetDb, key, "val"); // 假设你有 set(int, String, String) 或者 set(int, String, String, -1)

        // 验证初始状态：DB 5 存在且永久，DB 0 不存在
        assertEquals(-1, (long) easyRedis.ttl(targetDb, key));
        assertFalse(easyRedis.exists(key)); // 默认 DB 0

        // 2. 对 DB 5 执行 expire
        // 假设 easyRedis 有 expire(int dbIndex, String key, int seconds) 方法
        Long result = easyRedis.expire(targetDb, key, 600);
        assertEquals(1L, result);

        // 3. 验证 DB 5 的 TTL 变了
        long db5Ttl = easyRedis.ttl(targetDb, key);
        assertTrue(db5Ttl > 590 && db5Ttl <= 600);

        // 4. 再次验证 DB 0 依然不受影响
        assertFalse(easyRedis.exists(key));
    }
}
