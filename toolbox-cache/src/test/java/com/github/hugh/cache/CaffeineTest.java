package com.github.hugh.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author Hugh
 * @since
 **/
public class CaffeineTest {

    Cache<String, Object> cache = Caffeine.newBuilder()
            .expireAfterWrite(2, TimeUnit.SECONDS)
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
        Object value = cache.get(key, t -> setValue(key).apply(key));
        cache.put("hello", value);
        //判断是否存在如果不存返回null
        Object ifPresent = cache.getIfPresent(key);
        System.out.println(ifPresent);
        //移除一个key
        cache.invalidate(key);
        System.out.println(cache.stats().toString());
        return value;
    }

    public Function<String, Object> setValue(String key) {
        System.out.println("==生产==");
        return t -> key + "_value";
    }

    @Test
    void test01() throws InterruptedException {
        manulOperator("test01");
        Thread.sleep(1000);
        manulOperator("test01");
    }

}
