package com.github.hugh.support.instance;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 单例工厂，使用 CacheLoader 来缓存已经创建的实例，并且保证同一个类在程序中只有一个实例。
 *
 * @author hugh
 * @since 1.2.1
 */
public class Instance {

    /**
     * 使用 AtomicReference 来保证线程安全的 INSTANCE 对象。
     */
    private static final AtomicReference<Instance> INSTANCE = new AtomicReference<>();

    /**
     * 获取单例对象的方法。
     *
     * @return 单例对象
     */
    public static Instance getInstance() {
        Instance instance = INSTANCE.get();
        if (instance == null) {
            // 如果当前实例为 null，则创建新的实例
            instance = new Instance();
            if (!INSTANCE.compareAndSet(null, instance)) {
                // 如果设置失败，则表示其他线程已经设置了值，直接返回即可
                instance = INSTANCE.get();
            }
        }
        return instance;
    }


    /**
     * 缓存已经创建的实例的 CacheLoader 对象，保证同一个类在程序中只有一个实例。
     */
    private static final CacheLoader<String, Object> INSTANCE_CACHE_LOADER = CacheLoader.from(key -> {
        try {
            return Class.forName(key).getDeclaredConstructor().newInstance();//根据 key(包名)创建实体
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    });

    /**
     * Guava 缓存策略
     */
    public static LoadingCache<String, Object> SINGLETON_CACHE = CacheBuilder.newBuilder().build(INSTANCE_CACHE_LOADER);

    /**
     * 根据Class 创建实体
     *
     * @param tClass class
     * @param <T>    泛型
     * @return T 实体类型
     */
    @SuppressWarnings("unchecked")
    public <T> T singleton(Class<T> tClass) {
        String fullClassName = tClass.getName();
        try {
            return (T) SINGLETON_CACHE.get(fullClassName);
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
