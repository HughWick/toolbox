package com.github.hugh.cache.redis;

import com.github.hugh.cache.redis.base.BaseRedisTest;
import com.github.hugh.exception.ToolboxException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EasyRedis Hash 相关测试 (基于 Embedded Redis)
 */
class EasyRedisHashTest extends BaseRedisTest {

    @Test
    void hsetTest() {
        String url1 = "www.baidu.com";
        String cacheKey1 = "hset_test_01";
        // 1. 基础 hset 测试
        String field1 = "json";
        String field2 = "url";
        String field3 = "account";
        // 写入数据
        Long hset = easyRedis.hset(cacheKey1, field1, "www.google.com");
        assertEquals(1, hset, "首次写入应该返回 1");
        easyRedis.hset(cacheKey1, field2, url1);
        // 验证 hget
        assertEquals(url1, easyRedis.hget(cacheKey1, field2));
        easyRedis.hset(cacheKey1, field3, "hugh");
        // 2. 验证 hexists
        assertTrue(easyRedis.hexists(cacheKey1, field2));
        // 3. 验证 hIsNotExists (双重否定即肯定)
        // 当前 easyRedis 默认连接的是 DB 0
        assertFalse(easyRedis.hIsNotExists(cacheKey1, field2), "DB0 中 key 存在，IsNotExists 应该返回 false");
        // 验证跨库查询 (DB 15 应该是空的)
        assertTrue(easyRedis.hIsNotExists(15, cacheKey1, field2), "DB15 中 key 不存在，IsNotExists 应该返回 true");
        // 原用例是 assertFalse(easyRedis.hIsNotExists(1, ...))
        // 但因为我们现在只往 DB 0 写了数据，所以 DB 1 里应该是不存在的。
        // 如果要维持原意“验证跨库存在性”，我们需要显式往 DB 1 写一点数据：
        easyRedis.hset(1, cacheKey1, field1, "data_in_db_1", -1);
        assertFalse(easyRedis.hIsNotExists(1, cacheKey1, field1), "写入了DB1，所以这里应该返回 false");

        // 4. 验证 hdel
        Long account = easyRedis.hdel(cacheKey1, field3);
        assertEquals(1, account);

        Long hdel2 = easyRedis.hdel(cacheKey1, field1, field2);
        assertEquals(2, hdel2);
    }

    @Test
    void batchHsetMapToHash_success() throws ToolboxException {
        int dbIndex = 2;
        String hashKey = "testHashKey";
        String field1 = "field1";
        String value1 = "value1";

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put(field1, value1);
        dataMap.put("field2", "value2");

        // 测试指定 DB (dbIndex = 2)
        easyRedis.batchHashSet(dbIndex, hashKey, dataMap);
        assertEquals(2, easyRedis.hlen(dbIndex, hashKey));
        assertEquals(-1, easyRedis.ttl(dbIndex, hashKey)); // 验证没有过期时间
        assertEquals(value1, easyRedis.hget(dbIndex, hashKey, field1));
        assertEquals(1, easyRedis.del(dbIndex, hashKey)); // 清理

        // 测试默认 DB (dbIndex = 0)
        easyRedis.batchHashSet(hashKey, dataMap);
        assertEquals(value1, easyRedis.hget(hashKey, field1));
        assertEquals(2, easyRedis.hlen(hashKey));
        assertEquals(1, easyRedis.del(hashKey));
    }

    @Test
    void hgetAllTest() {
        String key1 = "ab";
        String field1 = "json";

        // 初始为空
        Map<String, String> stringStringMap = easyRedis.hgetAll(key1);
        assertTrue(stringStringMap.isEmpty());

        // 写入数据后查询
        Long hset = easyRedis.hset(key1, field1, "www.google.com");
        assertEquals(1, hset);

        // 再次查询不为空
        assertFalse(easyRedis.hgetAll(key1).isEmpty());

        Long hdel = easyRedis.hdel(key1, field1);
        assertEquals(1, hdel);
    }

    @Test
    void dbSizeTest() {
        // 1. 验证 DB 4 是空的 (我们在 @BeforeEach 里 flushAll 了，或者是新启动的)
        assertEquals(0, easyRedis.dbSize(4));
        // 2. 验证默认 DB (DB 0)
        // 此时应该是空的 (因为 @BeforeEach 执行了 flushAll)
        assertEquals(0, easyRedis.dbSize());
        // 3. 写入 3 个 key
        easyRedis.set("k1", "v1");
        easyRedis.set("k2", "v2");
        easyRedis.hset("k3", "f1", "v3");
        // 4. 断言现在是 3
        assertEquals(3, easyRedis.dbSize());
    }
}
