package com.github.hugh.cache.redis;

import com.github.hugh.cache.redis.base.BaseRedisTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EasyRedis mget (Multi-Get) 独立测试
 * <p>
 * 重点验证：
 * 1. 结果顺序性（必须与 Key 顺序一致）
 * 2. 混合存在性（部分存在，部分为 null）
 * 3. 多库隔离性
 * </p>
 */
class EasyRedisMgetTest extends BaseRedisTest {

    /**
     * 测试默认 DB 的重载方法
     * 覆盖方法：public List<String> mget(String... keys)
     * 验证点：
     * 1. 是否使用了实例默认的 dbIndex (通常为 0)
     * 2. 验证多参数传递是否正常
     */
    @Test
    void testMget_DefaultMethod() {
        String key1 = "default_k1";
        String key2 = "default_k2";
        String val1 = "val_1";

        // 1. 在默认 DB (0) 预设数据
        // 注意：这里显式调用 set(String, String) 也是用默认 DB，正好形成闭环验证
        easyRedis.set(key1, val1);

        // 2. 为了验证它没有去查别的库，我们在 DB 1 设置一个干扰项
        easyRedis.set(1, key1, "WRONG_VALUE_IN_DB1");

        // 3. 调用待测方法 (不传 dbIndex)
        List<String> results = easyRedis.mget(key1, key2);

        // 4. 验证
        assertNotNull(results);
        assertEquals(2, results.size());

        // 验证取到的是 DB 0 的值，而不是 DB 1 的值
        assertEquals(val1, results.get(0), "应该获取默认 DB 中的值");
        assertNotEquals("WRONG_VALUE_IN_DB1", results.get(0), "不应获取到其他 DB 的值");

        // 验证不存在的 key 返回 null
        assertNull(results.get(1));
    }

    /**
     * 测试基础场景：所有 Key 都不存在
     * 预期：返回包含 null 的 List，且长度与 Key 数量一致
     */
    @Test
    void testMget_AllMissing() {
        int dbIndex = 0;
        String key1 = "mget_miss_1";
        String key2 = "mget_miss_2";

        // 调用 mget
        List<String> results = easyRedis.mget(dbIndex, key1, key2);

        // 验证
        assertNotNull(results);
        assertEquals(2, results.size());
        // 验证列表内元素均为 null
        assertNull(results.get(0));
        assertNull(results.get(1));
    }

    /**
     * 测试核心场景：混合情况（有的存在，有的不存在）
     * 重点验证：顺序必须一致
     */
    @Test
    void testMget_MixedAndOrder() {
        // 准备数据
        String key1 = "k1"; // 存在
        String key2 = "k2"; // 不存在
        String key3 = "k3"; // 存在

        String val1 = "value_1";
        String val3 = "value_3";

        easyRedis.set(key1, val1);
        easyRedis.set(key3, val3);

        // 调用 mget，注意传入顺序：k1, k2, k3
        // 默认使用 DB 0
        List<String> results = easyRedis.mget(0, key1, key2, key3);

        // 验证
        assertEquals(3, results.size());
        assertEquals(val1, results.get(0), "第1个应该是 val1");
        assertNull(results.get(1), "第2个应该是 null (因为k2不存在)");
        assertEquals(val3, results.get(2), "第3个应该是 val3");

        // 验证颠倒 Key 传入顺序，结果也应该颠倒
        List<String> reverseResults = easyRedis.mget(0, key3, key1);
        assertEquals(val3, reverseResults.get(0));
        assertEquals(val1, reverseResults.get(1));
    }

    /**
     * 测试多库隔离
     * DB 0 和 DB 2 有同名 Key，mget 应能正确区分
     */
    @Test
    void testMget_DbIsolation() {
        int dbA = 0;
        int dbB = 2;
        String key = "same_key";
        // DB 0 设置 "val_A"
        easyRedis.set(dbA, key, "val_A");
        // DB 2 设置 "val_B"
        easyRedis.set(dbB, key, "val_B");
        // 验证 DB 0
        List<String> resA = easyRedis.mget(dbA, key);
        assertEquals("val_A", resA.get(0));
        // 验证 DB 2
        List<String> resB = easyRedis.mget(dbB, key);
        assertEquals("val_B", resB.get(0));
    }

    /**
     * 边界测试：传入空 Key 列表
     * 验证是否能安全处理单个或少量 Key
     */
    @Test
    void testMget_SingleInput() {
        easyRedis.set("single_k", "v");
        // 测试 mget(String... keys) 传入单个参数
        List<String> res = easyRedis.mget("single_k");
        assertEquals(1, res.size());
        assertEquals("v", res.get(0));
    }

    /**
     * 对应原测试逻辑的直接迁移与增强
     */
    @Test
    void testMget_OriginalLogicRefactored() {
        int dbIndex = 0;
        String key1 = "mget_test_01";
        String key2 = "mget_test_02";

        // 1. 初始全空
        List<String> initialRes = easyRedis.mget(dbIndex, key1, key2);
        // 使用 Arrays.asList 替代 Lists.newArrayList
        assertEquals(Arrays.asList(null, null), initialRes);

        // 2. 写入一个
        String str1 = "random_val_123";
        String setRes = easyRedis.set(key1, str1);
        assertEquals("OK", setRes);

        // 3. 查单条
        List<String> singleRes = easyRedis.mget(0, key1);
        assertEquals(Collections.singletonList(str1), singleRes);

        // 4. 再次查两条 (验证混合)
        List<String> mixedRes = easyRedis.mget(dbIndex, key1, key2);
        assertEquals(Arrays.asList(str1, null), mixedRes, "应返回 [值, null]");

        // 5. 清理
        Long delCount = easyRedis.del(dbIndex, key1);
        assertEquals(1, delCount);
    }
}
