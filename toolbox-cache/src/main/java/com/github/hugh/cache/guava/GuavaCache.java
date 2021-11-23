package com.github.hugh.cache.guava;

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
 * <li>{@link CacheBuilder#expireAfterWrite(long, TimeUnit)} 设置缓存n秒后没有创建/覆盖时会被回收、第一参数:时间长度,第二参数：时间单位}</li>
 * <li>{@link CacheBuilder#expireAfterAccess(long, TimeUnit)} (long, TimeUnit)} 设置缓存n秒后没有读写就会被回收、第一参数:时间长度,第二参数：时间单位}</li>
 * <li>{@link CacheBuilder#initialCapacity(int)}缓存容器的初始容量}</li>
 * </ul>
 *
 * @author hugh
 * @since 2.1.3
 */
public class GuavaCache {

    /**
     * 创建本地谷歌缓存
     *
     * @param <K>         key
     * @param <V>         value
     * @param cacheLoader build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
     * @return LoadingCache
     */
    public static <K, V> LoadingCache<K, V> create(CacheLoader<K, V> cacheLoader) {
        return CacheBuilder.newBuilder().build(cacheLoader);
    }

    /**
     * 创建本地谷歌缓存
     *
     * @param <K>              key
     * @param <V>              value
     * @param initialCapacity  缓存容器的初始容量
     * @param maximumSize      设置缓存最大容量，超过之后就会按照LRU最近虽少使用算法来移除缓存项
     * @param expireAfterWrite 设置缓存n秒后没有创建/覆盖时会被回收、单位：秒
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
     * @param <K>               key
     * @param <V>               value
     * @param expireAfterAccess 设置缓存n秒后没有读写就会被回收、单位：秒
     * @param cacheLoader       build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
     * @return LoadingCache
     */
    public static <K, V> LoadingCache<K, V> create(int expireAfterAccess, CacheLoader<K, V> cacheLoader) {
        return create(expireAfterAccess, TimeUnit.SECONDS, cacheLoader);
    }

    /**
     * 创建本地谷歌缓存、自定义设置超时时间单位
     *
     * @param expireAfterAccess 设置缓存n秒后没有读写就会被回收、单位：秒
     * @param timeUnit          时间单位 {@link TimeUnit}
     * @param cacheLoader       build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
     * @param <K>               key
     * @param <V>               value
     * @return LoadingCache
     * @since 1.5.10
     */
    public static <K, V> LoadingCache<K, V> create(int expireAfterAccess, TimeUnit timeUnit, CacheLoader<K, V> cacheLoader) {
        return CacheBuilder.newBuilder().expireAfterAccess(expireAfterAccess, timeUnit)
                .build(cacheLoader);
    }
}
