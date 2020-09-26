package com.github.hugh.support.instance;

import com.github.hugh.util.common.AssertUtils;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;

/**
 * 实例工具类
 *
 * @author hugh
 * @since 1.2.1
 */
public class Instance {

    private static volatile Instance instance;

    /**
     * 懒汉式获取单例对象
     *
     * @return Instance 实例
     */
    public static Instance getInstance() {
        if (instance == null) {//懒汉式
            synchronized (Instance.class) {
                if (instance == null) {//二次检查
                    instance = new Instance();
                }
            }
        }
        return instance;
    }

    private static CacheLoader<String, Object> cacheLoader() {
        return new CacheLoader<String, Object>() {
            @Override
            public Object load(String key) {
                try {
                    return Class.forName(key).newInstance();//根据 key(包名)创建实体
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    //加载缓存
//    private static CacheLoader<String, Object> cacheLoader = CacheLoader
//            .from(key -> {
//                try {
//                    return Class.forName(key).newInstance();//根据 key(包名)创建实体
//                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            });

//    private static CacheLoader<? super String, Object> cacheLoader;
    /**
     * Guava 缓存策略
     */
    private static LoadingCache<String, Object> SINGLETON_CACHE = CacheBuilder
            .newBuilder()
//            .recordStats() //开启统计信息开关
//            .initialCapacity(10)  //设置缓存容器的初始容量为10
//            .concurrencyLevel(8) //设置并发级别为8，并发级别是指可以同时写缓存的线程数
//            .maximumSize(100) //设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
//            .recordStats()  //是否需要统计缓存情况,该操作消耗一定的性能,生产环境应该去除
//            .expireAfterWrite(60, TimeUnit.SECONDS)  //设置写缓存后n秒钟过期
            //.expireAfterAccess(17, TimeUnit.SECONDS)  //设置读写缓存后n秒钟过期,实际很少用到,类似于expireAfterWrite
            //.refreshAfterWrite(13, TimeUnit.SECONDS) //只阻塞当前数据加载线程，其他线程返回旧值
//            .concurrencyLevel(Runtime.getRuntime().availableProcessors()) // 设置并发级别为cpu核心数
            //设置缓存的移除通知
//            .removalListener(notification -> {
//                System.out.println(notification.getKey() + " " + notification.getValue() + " 被移除,原因:" + notification.getCause());
//            })
            .build(cacheLoader()); //build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存

    /**
     * 根据Class 创建实体
     *
     * @param tClass class
     * @param <T>    泛型
     * @return T 实体类型
     */
    @SuppressWarnings("unchecked")
    public <T> T singleton(Class<T> tClass) {
        AssertUtils.notNull(tClass, "class");
        String fullClassName = tClass.getName();
        try {
            return (T) SINGLETON_CACHE.get(fullClassName);
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
