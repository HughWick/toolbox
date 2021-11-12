package com.github.hugh.guava;

import com.github.hugh.IdSequence;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author AS
 * @date 2020/9/22 11:07
 */
public class Test01 {
    //加载缓存
    private static CacheLoader<String, Boolean> cacheLoader = CacheLoader
            .from(key -> {
                return true;// 当唯一ID不存在时返回true
            });

    /**
     * Guava 缓存策略
     */
    private static LoadingCache<String, Boolean> SINGLETON_CACHE = CacheBuilder
            .newBuilder()
//            .initialCapacity(10)  //设置缓存容器的初始容量为10
//            .concurrencyLevel(8) //设置并发级别为8，并发级别是指可以同时写缓存的线程数
//            .maximumSize(100) //设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
//            .recordStats()  //是否需要统计缓存情况,该操作消耗一定的性能,生产环境应该去除
//            .expireAfterWrite(30, TimeUnit.SECONDS)  //设置写缓存后n秒钟过期
            .expireAfterAccess(4, TimeUnit.SECONDS)  //设置读写缓存后n秒钟过期,实际很少用到,类似于expireAfterWrite
            //.refreshAfterWrite(13, TimeUnit.SECONDS) //只阻塞当前数据加载线程，其他线程返回旧值
//            .concurrencyLevel(Runtime.getRuntime().availableProcessors()) // 设置并发级别为cpu核心数
            //设置缓存的移除通知
//            .removalListener(notification -> {
//                System.out.println(notification.getKey() + " " + notification.getValue() + " 被移除,原因:" + notification.getCause());
//            })
            .build(cacheLoader); //build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String serialNo = IdSequence.snowflake();
        Boolean notExist = SINGLETON_CACHE.get(serialNo);
        if (notExist) {//缓存中不存在
            System.out.println("======one====" + notExist);
            SINGLETON_CACHE.put(serialNo, false);
        }
        System.out.println("=two==>>" + SINGLETON_CACHE.get(serialNo));
        SINGLETON_CACHE.put("a", false);
        SINGLETON_CACHE.put("b", false);
        Thread.sleep(3000);
        SINGLETON_CACHE.put("a", false);
        Boolean b1 = SINGLETON_CACHE.get("b");
        Thread.sleep(3000);
        Boolean a1 = SINGLETON_CACHE.get("a");
        System.out.println("-a1-->>" + a1);
        System.out.println("-b1-->>" + b1);
        System.out.println("======END====");
    }
}
