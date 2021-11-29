package com.github.hugh.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.hugh.cache.caffeine.CaffeineCache;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author Hugh
 * @since
 **/
public class CaffeineTest {

    static Cache<String, Object> cache = Caffeine.newBuilder()
//            .expireAfterWrite(3, TimeUnit.SECONDS)
            // 设置缓存策略在1天未写入过期缓存
            .expireAfterAccess(2, TimeUnit.SECONDS)
            .maximumSize(10)
            .recordStats()
            .build();

    /**
     * 手动加载
     *
     * @param key
     * @return
     */
    public Object manulOperator(String key) {
        //如果一个key不存在，那么会进入指定的函数生成value
        Object value = cache.get(key, (Function<String, String>) k -> getValue(k));
        //判断是否存在如果不存返回null
        Object ifPresent = cache.getIfPresent(key);
//        cache.put(key, ifPresent);
        System.out.println(ifPresent);
        //移除一个key
//        cache.invalidate(key);
//        System.out.println(cache.stats().toString());
        return value;
    }

    // 缓存中找不到，则会进入这个方法。一般是从数据库获取内容
    private static String getValue(String k) {
        System.out.println("==生产==");
        return k + ":value";
    }
//    public Function<String, Object> setValue(String key) {
//        System.out.println("==生产==");
//        return t -> key + "_value";
//    }

    @Test
    void test01() throws InterruptedException {
        CaffeineTest caffeineTest = new CaffeineTest();
        caffeineTest.manulOperator("test01");
        Thread.sleep(1000);
        caffeineTest.manulOperator("test01");
        Thread.sleep(1000 * 2);
        caffeineTest.manulOperator("test01");
    }

    @Test
    void testo2() {
        String keys = "KEY_01";
//        CacheLoader<String, Integer> cacheLoader = key -> -1;
        LoadingCache<String, Integer> loadingCache = CaffeineCache.create(key -> -1);
        //只查询缓存，没有命中，即返回null。 miss++
        Integer ifPresent = loadingCache.getIfPresent(keys);
        System.out.println("==>>" + ifPresent);
        // 查询缓存，未命中，调用load方法，返回-1. miss++
        System.out.println("----2---->>" + loadingCache.get(keys));
        //移除一个key
//        cache.invalidate(key);
//        System.out.println("--3---->>" + BooleanCaffeineCache.isNotExists(keys));
//        BooleanCaffeineCache.LOADING_CACHE.put(keys, false);
//        System.out.println("--4---->>" + BooleanCaffeineCache.isNotExists(keys));
    }

    @Test
    void expireAfterWrite() throws InterruptedException {
        String keys = "KEY_01";
        Cache<String, String> expireAfterWrite = CaffeineCache.createExpireAfterWrite(1);
//        Integer ifPresent = expireAfterWrite.getIfPresent(keys);
        System.out.println(expireAfterWrite.getIfPresent(keys));
        System.out.println("---1->>" + expireAfterWrite.get(keys, k -> "abc"));
        System.out.println("---2->>" + expireAfterWrite.getIfPresent(keys));
        Thread.sleep(1000);
        System.out.println("---3->>" + expireAfterWrite.getIfPresent(keys));
    }

    @Test
    void expireAfterAccess() throws InterruptedException {
        String keys = "KEY_01";
        Cache<Object, Object> expireAfterAccess = CaffeineCache.createExpireAfterAccess(1);
        System.out.println(expireAfterAccess.getIfPresent(keys));
        System.out.println("---1->>" + expireAfterAccess.get(keys, k -> "abc"));
        System.out.println("---2->>" + expireAfterAccess.getIfPresent(keys));
        Thread.sleep(1000);
        System.out.println("---3->>" + expireAfterAccess.getIfPresent(keys));
        Cache<Object, Object> expireAfterAccess2 = CaffeineCache.createExpireAfterAccess(1, (k) -> "scds");
        System.out.println("---expireAfterAccess2->>" + expireAfterAccess2.get(keys, k -> "abc"));

    }


    @Test
    void testo4() throws InterruptedException {
        String keys = "KEY_01";
        Cache<String, String> expireAfterWrite = CaffeineCache.createExpireAfterWrite(1, k -> "abc");
        System.out.println(expireAfterWrite.getIfPresent(keys));
    }
}
