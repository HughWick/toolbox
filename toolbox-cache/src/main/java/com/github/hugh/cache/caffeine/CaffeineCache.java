package com.github.hugh.cache.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

/**
 * Caffeine 高性能本地缓存
 * <p>它还提供了以下方法进行参数配置</p>
 * <ul>
 * <li>{@link Caffeine#newBuilder()}有以下参数可进行配置</li>
 * <li>{@link Caffeine#initialCapacity(int)}缓存容器的初始容量</li>
 * <li>{@link Caffeine#maximumSize(long)}设置缓存最大容量，超过之后就会按照LRU最近虽少使用算法来移除缓存项</li>
 * <li>{@link Caffeine#expireAfterWrite(long, TimeUnit)} 在最后一次写入缓存后开始计时，在指定的时间后过期</li>
 * <li>{@link Caffeine#expireAfterAccess(long, TimeUnit)} 在最后一次访问或者写入后开始计时，在指定的时间后过期。假如一直有请求访问该key，那么这个缓存将一直不会过期。</li>
 * <li>{@link Caffeine#initialCapacity(int)}缓存容器的初始容量}</li>
 * </ul>
 *
 * @author Hugh
 * @since 2.1.5
 **/
public class CaffeineCache {

    public CaffeineCache() {
    }

    /**
     * 创建本地缓存
     *
     * @param <K>         key
     * @param <V>         value
     * @param cacheLoader build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
     * @return LoadingCache
     */
    public static <K, V> LoadingCache<K, V> create(CacheLoader<K, V> cacheLoader) {
        return Caffeine.newBuilder().build(cacheLoader);
    }

    /**
     * 创建本地缓存,手动加载的缓存
     * <p>
     * 在每次get key的时候指定一个同步的函数，如果key不存在就调用这个函数生成一个值。
     * </p>
     *
     * @param expireAfterAccess 设置缓存n秒后没有读写就会被回收、单位：秒
     * @param <K>               key
     * @param <V>               value
     * @return Cache
     * @since 2.1.5
     */
    public static <K, V> Cache<K, V> createExpireAfterAccess(int expireAfterAccess) {
        return createExpireAfterAccess(expireAfterAccess, TimeUnit.SECONDS);
    }

    /**
     * 创建本地缓存,手动加载的缓存
     * <p>
     * 在每次get key的时候指定一个同步的函数，如果key不存在就调用这个函数生成一个值。
     * </p>
     *
     * @param expireAfterAccess 设置缓存n秒后没有读写就会被回收、单位：秒
     * @param timeUnit          时间单位 {@link TimeUnit}
     * @param <K>               key
     * @param <V>               value
     * @return Cache
     * @since 2.1.6
     */
    public static <K, V> Cache<K, V> createExpireAfterAccess(int expireAfterAccess, TimeUnit timeUnit) {
        return Caffeine.newBuilder().expireAfterAccess(expireAfterAccess, timeUnit).build();
    }

    /**
     * 创建本地缓存，同步加载：在创建缓存时加载缓存
     * <p>
     * 构造Cache时候，build方法传入一个CacheLoader实现类。实现load方法，通过key加载value。
     * </p>
     * <p>在最后一次访问或者写入后开始计时，在指定的时间后过期。假如一直有请求访问该key，那么这个缓存将一直不会过期</p>
     *
     * @param <K>               key
     * @param <V>               value
     * @param expireAfterAccess 设置缓存n秒后没有读写就会被回收、单位：秒
     * @param cacheLoader       build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
     * @return LoadingCache
     */
    public static <K, V> LoadingCache<K, V> createExpireAfterAccess(int expireAfterAccess, CacheLoader<K, V> cacheLoader) {
        return createExpireAfterAccess(expireAfterAccess, TimeUnit.SECONDS, cacheLoader);
    }

    /**
     * 创建本地缓存、自定义设置超时时间单位
     * <p>在最后一次访问或者写入后开始计时，在指定的时间后过期。假如一直有请求访问该key，那么这个缓存将一直不会过期</p>
     *
     * @param expireAfterAccess 设置缓存n秒后没有读写就会被回收
     * @param timeUnit          时间单位 {@link TimeUnit}
     * @param cacheLoader       build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
     * @param <K>               key
     * @param <V>               value
     * @return LoadingCache
     */
    public static <K, V> LoadingCache<K, V> createExpireAfterAccess(int expireAfterAccess, TimeUnit timeUnit, CacheLoader<K, V> cacheLoader) {
        return Caffeine.newBuilder().expireAfterAccess(expireAfterAccess, timeUnit).build(cacheLoader);
    }

    /**
     * 创建本地缓存、自定义设置超时时间
     * <p>在每次get key的时候指定一个同步的函数，如果key不存在就调用这个函数生成一个值。</p>
     * <p>在最后一次写入缓存后开始计时，在指定的时间后过期</p>
     *
     * @param expireAfterWrite 设置缓存n秒后没有创建/覆盖时会被回收、第一参数:时间长度
     * @param <K>              key
     * @param <V>              value
     * @return Cache
     * @since 2.1.5
     */
    public static <K, V> Cache<K, V> createExpireAfterWrite(int expireAfterWrite) {
        return createExpireAfterWrite(expireAfterWrite, TimeUnit.SECONDS);
    }

    /**
     * 创建本地缓存、自定义设置超时时间
     * <p>在每次get key的时候指定一个同步的函数，如果key不存在就调用这个函数生成一个值。</p>
     * <p>在最后一次写入缓存后开始计时，在指定的时间后过期</p>
     *
     * @param expireAfterAccess 设置缓存n秒后没有读写就会被回收、单位：秒
     * @param timeUnit          时间单位 {@link TimeUnit}
     * @param <K>               key
     * @param <V>               value
     * @return Cache
     * @since 2.1.6
     */
    public static <K, V> Cache<K, V> createExpireAfterWrite(int expireAfterAccess, TimeUnit timeUnit) {
        return Caffeine.newBuilder().expireAfterWrite(expireAfterAccess, timeUnit).build();
    }


    /**
     * 创建本地缓存、自定义设置超时时间单位
     * <p>在最后一次访问或者写入后开始计时，在指定的时间后过期。假如一直有请求访问该key，那么这个缓存将一直不会过期</p>
     * <p>在最后一次写入缓存后开始计时，在指定的时间后过期</p>
     *
     * @param expireAfterWrite 设置缓存n秒后没有创建/覆盖时会被回收
     * @param cacheLoader      build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
     * @param <K>              key
     * @param <V>              value
     * @return Cache
     * @since 2.1.5
     */
    public static <K, V> LoadingCache<K, V> createExpireAfterWrite(int expireAfterWrite, CacheLoader<K, V> cacheLoader) {
        return createExpireAfterWrite(expireAfterWrite, TimeUnit.SECONDS, cacheLoader);
    }

    /**
     * 创建本地缓存、自定义设置超时时间单位
     * <p>在最后一次写入缓存后开始计时，在指定的时间后过期</p>
     *
     * @param expireAfterWrite 设置缓存n秒后没有创建/覆盖时会被回收
     * @param timeUnit         时间单位 {@link TimeUnit}
     * @param cacheLoader      build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
     * @param <K>              key
     * @param <V>              value
     * @return Cache
     * @since 2.1.5
     */
    public static <K, V> LoadingCache<K, V> createExpireAfterWrite(int expireAfterWrite, TimeUnit timeUnit, CacheLoader<K, V> cacheLoader) {
        return Caffeine.newBuilder().expireAfterWrite(expireAfterWrite, timeUnit).build(cacheLoader);
    }
}
