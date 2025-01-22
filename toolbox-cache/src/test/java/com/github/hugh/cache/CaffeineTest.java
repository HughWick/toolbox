package com.github.hugh.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.hugh.cache.caffeine.CaffeineCache;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 本地缓存测试工具类
 *
 * @author Hugh
 **/
class CaffeineTest {

    // 根据最后访问时间移除
    static Cache<String, Object> afterAccessCache = Caffeine.newBuilder()
//            .expireAfterWrite(3, TimeUnit.SECONDS)
            // 设置缓存策略在1天未写入过期缓存
            .expireAfterAccess(2, TimeUnit.SECONDS)
            .maximumSize(10)
            .recordStats()
            .build();


    @Test
    void test01() throws InterruptedException {
        String key1 = "test01";
        String value1 = key1 + "_value";
        //手动加载
        Object value = afterAccessCache.get(key1, (Function<String, String>) k -> k + "_value");
        assertEquals(value1, value.toString());
        Object ifPresentBefore = afterAccessCache.getIfPresent(key1);
        assertNotNull(ifPresentBefore);
        Thread.sleep(1000);
        Object ifPresentAfter2 = afterAccessCache.getIfPresent(key1);
        assertNotNull(ifPresentBefore);
        //移除一个key
        afterAccessCache.invalidate(key1);
        //判断是否存在如果不存返回null
        Object ifPresentAfter = afterAccessCache.getIfPresent(key1);
        assertNull(ifPresentAfter);
//        System.out.println(cache.stats().toString());
//        caffeineTest.manulOperator("test02");
//        Thread.sleep(1000 * 2);
//        caffeineTest.manulOperator("test03");
    }

    // lambda
    @Test
    void testLambda() {
        String keys = "KEY_01";
        LoadingCache<String, Integer> loadingCache = CaffeineCache.create(key -> -1);
        //只查询缓存，没有命中，即返回null。 miss++
        assertNull(loadingCache.getIfPresent(keys));
        // 查询缓存，未命中，调用load方法，返回-1. miss++
        assertEquals(-1, loadingCache.get(keys));
        //移除一个key
//        cache.invalidate(keys);
//        System.out.println("--3---->>" + BooleanCaffeineCache.isNotExists(keys));
//        BooleanCaffeineCache.LOADING_CACHE.put(keys, false);
//        System.out.println("--4---->>" + BooleanCaffeineCache.isNotExists(keys));
    }

    // 测试 最后一次写入后经过固定时间过期
    @Test
    void expireAfterWrite() throws InterruptedException {
        String keys = "KEY_01";
        String value1 = "abc";
        Cache<String, String> expireAfterWrite = CaffeineCache.createExpireAfterWrite(3);
//        Integer ifPresent = expireAfterWrite.getIfPresent(keys);
        assertNull(expireAfterWrite.getIfPresent(keys));
        assertEquals(value1, expireAfterWrite.get(keys, k -> value1));
        // 根据key查询一个缓存，如果没有返回NULL
        assertEquals(value1, expireAfterWrite.getIfPresent(keys));
        Thread.sleep(1000);
        assertNotNull(expireAfterWrite.getIfPresent(keys));
        assertEquals(value1, expireAfterWrite.getIfPresent(keys));
        Thread.sleep(3000);
        assertNull(expireAfterWrite.getIfPresent(keys));
//        ConcurrentMap<String, String> stringStringConcurrentMap = expireAfterWrite.asMap();
//        System.out.println("=====>>"+expireAfterWrite.stats().toString());
//        for (Map.Entry<String, String> entry : stringStringConcurrentMap.entrySet()) {
//            System.out.println(entry.getKey()+"===="+entry.getValue());
//        }
//        assertEquals(0, expireAfterWrite.asMap().size());
    }

    // 测试最后一次写入或访问后经过固定时间过期
    @Test
    void expireAfterAccess() throws InterruptedException {
        String keys = "KEY_01";
        Cache<Object, Object> expireAfterAccess = CaffeineCache.createExpireAfterAccess(1);
        assertNull(expireAfterAccess.getIfPresent(keys));
        assertEquals("123", expireAfterAccess.get(keys, k -> "123"));
        assertEquals("123", expireAfterAccess.getIfPresent(keys));
        Thread.sleep(1000);
        assertNull(expireAfterAccess.getIfPresent(keys));
//        System.out.println("---expireAfterAccess2->>" + expireAfterAccess2.get(keys, k -> "abc"));
        String key2 = "key2";
        Cache<Object, Object> expireAfterAccess2 = CaffeineCache.createExpireAfterAccess(1, (k) -> key2);
        assertEquals("abc", expireAfterAccess2.get(keys, k -> "abc"));
        assertNull(expireAfterAccess2.getIfPresent(key2));
    }

    // 测试方法 createExpireAfterWrite 的基本功能
    @Test
    void testCreateExpireAfterWrite_withExpireAfterWriteInSeconds() throws Exception {
        CacheLoader<String, String> cacheLoader = new CacheLoader<>() {
            @Override
            public String load(String key) {
                return "value_" + key;
            }
        };

        LoadingCache<String, String> cache = CaffeineCache.createExpireAfterWrite(1, cacheLoader);

        // 测试缓存加载功能
        cache.put("key1", "value1");
        assertEquals("value1", cache.get("key1"));

        // 测试过期时间
        TimeUnit.SECONDS.sleep(2);
        assertNull(cache.getIfPresent("key1"), "Cache should have expired after 1 second");
    }

    // 测试方法 createExpireAfterWrite 的自定义时间单位
    @Test
    void testCreateExpireAfterWrite_withCustomTimeUnit() throws Exception {
        CacheLoader<String, String> cacheLoader = new CacheLoader<>() {
            @Override
            public String load(String key) {
                return "value_" + key;
            }
        };

        LoadingCache<String, String> cache = CaffeineCache.createExpireAfterWrite(1, TimeUnit.MILLISECONDS, cacheLoader);

        // 测试缓存加载功能
        cache.put("key1", "value1");
        assertEquals("value1", cache.get("key1"));

        // 测试过期时间
        TimeUnit.MILLISECONDS.sleep(2);
        assertNull(cache.getIfPresent("key1"), "Cache should have expired after 1 millisecond");
    }

    // 测试 CacheLoader 自动加载缓存
    @Test
    void testCacheLoader_autoload() {
        CacheLoader<String, String> cacheLoader = key -> "loaded_value_" + key;

        LoadingCache<String, String> cache = CaffeineCache.createExpireAfterWrite(1, cacheLoader);

        // 测试缓存加载功能
        assertEquals("loaded_value_key1", cache.get("key1"));
        assertEquals("loaded_value_key2", cache.get("key2"));
    }

    // 测试过期时间为 0 的情况
    @Test
    void testCache_withZeroExpireTime() throws Exception {
        CacheLoader<String, String> cacheLoader = key -> "withZeroExpireTime_" + key;

        LoadingCache<String, String> cache = CaffeineCache.createExpireAfterWrite(0, TimeUnit.SECONDS, cacheLoader);

        // 测试缓存加载功能
        cache.put("key1", "value1");
        assertEquals("withZeroExpireTime_key1", cache.get("key1"));

        // 测试立即过期
        TimeUnit.SECONDS.sleep(1);
        assertNull(cache.getIfPresent("key1"), "Cache should have expired immediately");
    }

    // 测试过期时间为负值的情况
    @Test
    void testCache_withNegativeExpireTime() throws Exception {
        CacheLoader<String, String> cacheLoader = key -> "withNegativeExpireTime_" + key;
        // 负值过期时间应该抛出 IllegalArgumentException
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            CaffeineCache.createExpireAfterWrite(-1, TimeUnit.SECONDS, cacheLoader);
        });
        assertEquals("Duration cannot be negative: -1 SECONDS", thrown.getMessage());
    }
}
