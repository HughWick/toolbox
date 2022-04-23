package com.github.hugh.cache;

import com.github.hugh.cache.redis.EasyRedis;
import com.google.common.base.Suppliers;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * User: AS
 * Date: 2021/11/11 10:44
 */
@Configuration
public class EasyRedisTest {

    /**
     * 初始化redis连接池
     */
    @Bean
    public JedisPool redisPoolFactory() {
//        log.info("初始化redis pool。start...");
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最小基础数
        jedisPoolConfig.setMinIdle(10);
        // 最大空闲数
        jedisPoolConfig.setMaxIdle(500);
        // 连接池的最大数据库连接数
        jedisPoolConfig.setMaxTotal(1000);
        // 最大建立连接等待时间
        jedisPoolConfig.setMaxWaitMillis(10000);
        // 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
        jedisPoolConfig.setMinEvictableIdleTimeMillis(60000);
        // 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
        jedisPoolConfig.setNumTestsPerEvictionRun(10);
        // 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(30000);
        // 是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个
        jedisPoolConfig.setTestOnBorrow(true);
        // 在空闲时检查有效性, 默认false
        jedisPoolConfig.setTestWhileIdle(true);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "43.128.14.188", 9991, 10000, "password123@");
//        log.info("初始化redis pool。end...");
        System.out.println("初始化redis pool。end...");
        return jedisPool;
    }

    /**
     * 单例工厂
     */
    Supplier<EasyRedis> easyRedisSupplier = () -> new EasyRedis(redisPoolFactory(), 1);

    /**
     * 放入内存中
     */
    Supplier<EasyRedis> supplier = Suppliers.memoize(easyRedisSupplier::get);

    /**
     * 放入过期时间
     */
    Supplier<EasyRedis> supplier2 = Suppliers.memoizeWithExpiration(easyRedisSupplier::get, 5, TimeUnit.SECONDS);

    @Test
    void testRedis() {
        JedisPool jedisPool = redisPoolFactory();
//        new Thread(() -> {
//            EasyRedis easyRedis = EasyRedis.getInstance(redisPoolFactory());
//            System.out.println("---1->>" + easyRedis);
//        }).start();
//        new Thread(() -> {
//            EasyRedis easyRedis = EasyRedis.getInstance(redisPoolFactory());
//            System.out.println("---2->>" + easyRedis);
//        }).start();
//        new Thread(() -> {
//            EasyRedis easyRedis = EasyRedis.getInstance(redisPoolFactory());
//            System.out.println("---3->>" + easyRedis);
//        }).start();
        new Thread(() -> {
            System.out.println("---4---");
            EasyRedis easyRedis = EasyRedis.getInstance(jedisPool, 1);
            System.out.println("---4->>" + easyRedis);
        }).start();
        new Thread(() -> {
            System.out.println("---5---");
            EasyRedis easyRedis = EasyRedis.getInstance(jedisPool, 2, true);
            System.out.println("---5->>" + easyRedis);
        }).start();
        new Thread(() -> {
            System.out.println("---6---");
            EasyRedis easyRedis = EasyRedis.getInstance(jedisPool, 2);
            System.out.println("---6->>" + easyRedis);
        }).start();
        new Thread(() -> {
            System.out.println("---7---");
            EasyRedis easyRedis = EasyRedis.getInstance(jedisPool, 2);
            System.out.println("---7->>" + easyRedis);
        }).start();
//        System.out.println("===1>>" + supplier.get());
//        System.out.println("=2==>>" + supplier.get());
//        EasyRedis instance = EasyRedis.getInstance(redisPoolFactory());
//        System.out.println(instance);
//        System.out.println(EasyRedis.getInstance(redisPoolFactory()));
//        System.out.println(redisPoolFactory());
//        EasyRedis easyRedis = new EasyRedis(redisPoolFactory());
//        System.out.println(easyRedis);
//        System.out.println(new EasyRedis(redisPoolFactory()));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }
    }

    @Test
    void test01() {
//        JedisPool jedisPool = redisPoolFactory();
//        System.out.println("---->>" + redisPoolFactory());
//        System.out.println(new EasyRedis(jedisPool));
//        EasyRedis instance = EasyRedis.getInstance(redisPoolFactory());
//        System.out.println("===>>" + instance);
//        System.out.println("===>>" + EasyRedis.getInstance(redisPoolFactory()));
    }

    @Test
    void setTest() {
        EasyRedis instance = supplier.get();
        int dbIndex = 2;
        String key = "set_test_01";
        String value = "sdjfhkj";
        String set = instance.set(key, value);
        System.out.println("=====1==>>" + set);
        instance.set("set_test_02", value, 1000);
        String key3 = "set_test_03";
        instance.set(dbIndex, key3, value, 1000);
        System.out.println("==-getkey3--->>" + instance.get(dbIndex, key3));
        String setBytes = instance.set("byte_test_01".getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8));
        System.out.println("====2>>>>>" + setBytes);
        instance.set(1, "byte_test_01".getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void getTest() {
        EasyRedis instance = EasyRedis.getInstance(redisPoolFactory());
        int dbIndex = 2;
        String key = "set_test_01";
        byte[] byteKey = "byte_test_01".getBytes(StandardCharsets.UTF_8);
        String value = "sdjfhkj";
        String get = instance.get(key);
        System.out.println("----<>>>><>>" + get);
        System.out.println("11111--->>" + instance.get(1, "set_test_03"));
        System.out.println("222--->>" + Arrays.toString(instance.get(byteKey)));
        System.out.println("33--->>" + Arrays.toString(instance.get(1, byteKey)));
        System.out.println("444--->>" + (instance.mget(0, key, "set_test_02")));
        System.out.println("555===>>" + instance.getAllKeys("*"));
    }

    @Test
    void delTest() {
        EasyRedis instance = supplier.get();
        String key = "set_test_01";
        System.out.println("---1-<>>>><>>" + instance.del(key));
        System.out.println("--2--<>>>><>>" + instance.del("byte_test_01".getBytes(StandardCharsets.UTF_8)));
        System.out.println("---3-<>>>><>>" + instance.del(1, "set_test_03"));
    }

    @Test
    void existsTest() {
        EasyRedis instance = supplier.get();
        String key = "set_test_01";
        System.out.println("---1-<>>>><>>" + instance.exists(key));
    }

    @Test
    void expireTest() {
        EasyRedis easyRedis = supplier.get();
        String key = "expire_test_01";
        String value = "sdjfhkj";
        easyRedis.set(key, value, 100);
        Long expire = easyRedis.expire(key, 1999);
        System.out.println(expire);
    }

    @Test
    void tllTest() {
        EasyRedis easyRedis = supplier.get();
        String key = "expire_test_01";
        Long ttl = easyRedis.ttl(key);
        System.out.println("tll=1==>>" + ttl);
        System.out.println("tll===2>>" + easyRedis.ttl(1, key));
    }

    @Test
    void incrTest() {
        EasyRedis easyRedis = supplier.get();
        String key = "incr_test_01";
        Long ttl = easyRedis.incr(key);
        System.out.println(ttl);
    }

    @Test
    void hsetTest() {
        EasyRedis easyRedis = supplier.get();
        String key = "test02";
        Long hset = easyRedis.hset(key, "json", "www.google.com");
        easyRedis.hset(key, "url", "www.baidu.com");
        easyRedis.hset(key, "account", "hugh");
        System.out.println(hset);
        System.out.println(easyRedis.hget(key, "url"));
        System.out.println(easyRedis.hgetAll(key));
        Long account = easyRedis.hdel(key, "account");
        System.out.println("===del=>" + account);
    }
}
