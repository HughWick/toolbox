package com.github.hugh.support.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

/**
 * 提供Goole Guava 缓存 {@link LoadingCache} 实例
 * <p>它还提供了以下方法进行参数配置</p>
 * <ul>
 * <li>{@link CacheBuilder#newBuilder()}有以下参数可进行配置</li>
 * <li>{@link CacheBuilder#initialCapacity(int)}缓存容器的初始容量}</li>
 * <li>{@link CacheBuilder#maximumSize(long)}设置缓存最大容量，超过之后就会按照LRU最近虽少使用算法来移除缓存项}</li>
 * <li>{@link CacheBuilder#expireAfterWrite(long, TimeUnit)}第一参数:时间长度,第二参数：时间单位}</li>
 * <li>{@link CacheBuilder#initialCapacity(int)}缓存容器的初始容量}</li>
 * </ul>
 *
 * @author hugh
 * @since 1.3.2
 */
public class GuavaCache {

    /**
     * 创建本地谷歌缓存
     *
     * @param cacheLoader build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
     * @return LoadingCache
     */
    public static <K, V> LoadingCache<K, V> create(CacheLoader<K, V> cacheLoader) {
        return CacheBuilder.newBuilder().build(cacheLoader);
    }

    /**
     * 创建本地谷歌缓存
     *
     * @param initialCapacity  缓存容器的初始容量
     * @param maximumSize      设置缓存最大容量，超过之后就会按照LRU最近虽少使用算法来移除缓存项
     * @param expireAfterWrite 设置写缓存后n秒钟过期
     * @param cacheLoader      build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
     * @return LoadingCache
     */
    public static <K, V> LoadingCache<K, V> create(int initialCapacity, int maximumSize, int expireAfterWrite, CacheLoader<K, V> cacheLoader) {
        return CacheBuilder.newBuilder()
                .initialCapacity(initialCapacity)
                .maximumSize(maximumSize)
                .expireAfterWrite(expireAfterWrite, TimeUnit.SECONDS)
                .build(cacheLoader);
    }

    /**
     * 创建本地谷歌缓存
     *
     * @param expireAfterWrite 设置写缓存后n秒钟过期
     * @param cacheLoader      build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
     * @return LoadingCache
     */
    public static <K, V> LoadingCache<K, V> create(int expireAfterWrite, CacheLoader<K, V> cacheLoader) {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(expireAfterWrite, TimeUnit.SECONDS)
                .build(cacheLoader);
    }
}
