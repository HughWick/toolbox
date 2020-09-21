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

    /**
     * Guava 缓存策略
     */
    private static LoadingCache<String, Object> SINGLETON_CACHE = CacheBuilder
            .newBuilder()
            .build(new CacheLoader<String, Object>() {
                @Override
                public Object load(String key) {
                    try {
                        return Class.forName(key).newInstance();//根据 key(包名)创建实体
                    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            });


    /**
     * 根据Class 创建实体
     *
     * @param tClass class
     * @param <T>    泛型
     * @return T 实体类型
     */
    @SuppressWarnings("unchecked")
    public <T> T singleton(Class<T> tClass) {
        AssertUtils.notNull(tClass, " class is null");
        String fullClassName = tClass.getName();
        try {
            return (T) SINGLETON_CACHE.get(fullClassName);
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
