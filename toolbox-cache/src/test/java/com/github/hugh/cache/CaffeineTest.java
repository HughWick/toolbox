package com.github.hugh.cache;

import com.github.benmanes.caffeine.cache.Cache;
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
        Cache<String, String> expireAfterWrite = CaffeineCache.createExpireAfterWrite(1);
//        Integer ifPresent = expireAfterWrite.getIfPresent(keys);
        assertNull(expireAfterWrite.getIfPresent(keys));
        assertEquals("abc", expireAfterWrite.get(keys, k -> "abc"));
        // 根据key查询一个缓存，如果没有返回NULL
        assertEquals("abc", expireAfterWrite.getIfPresent(keys));
        Thread.sleep(1000);
        assertNull(expireAfterWrite.getIfPresent(keys));
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





//    @Test
//    void testo4() throws InterruptedException {
//        String keys = "KEY_01";
////        Cache<String, String> expireAfterWrite = CaffeineCache.createExpireAfterWrite(2, k -> "abc");
//        Cache<Object, Object> expireAfterWrite = CaffeineCache.createExpireAfterWrite(2);
//        System.out.println("---1->>" + expireAfterWrite.get(keys, k -> tem01()));
//        System.out.println(expireAfterWrite.getIfPresent(keys));
//        new Thread(() -> {
//            try {
//                System.out.println(expireAfterWrite.get(keys, k -> tem01()));
//                Thread.sleep(1000);
//            } catch (InterruptedException interruptedException) {
//                interruptedException.printStackTrace();
//            }
//        }).start();
//        Thread.sleep(3000);
//        System.out.println("===>" + expireAfterWrite.getIfPresent(keys));
//    }
}
