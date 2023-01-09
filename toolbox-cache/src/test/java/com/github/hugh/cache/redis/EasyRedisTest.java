package com.github.hugh.cache.redis;

import com.github.hugh.util.ListUtils;
import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * redis 工具类测试
 * User: AS
 * Date: 2021/11/11 10:44
 */
@Configuration
class EasyRedisTest {

    public static final String IP_ADDR = "141.147.176.125";
    public static final int PORT = 7779;
    public static final String PASSWORD = "password123";
    public static final int GET_TEST_INDEX = 14;

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
//        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "43.128.14.188", 9991, 10000, "password123@");
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, IP_ADDR, PORT, 10000, PASSWORD);
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

    /**
     * 线程安全的int
     */
    static AtomicInteger hashcode = new AtomicInteger(0);
    static AtomicInteger hashcode2 = new AtomicInteger(0);

    static ThreadPoolExecutor fixedThreadPool1 = new ThreadPoolExecutor(10, 10, 20, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    static ThreadPoolExecutor fixedThreadPool2 = new ThreadPoolExecutor(10, 10, 20, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    @Test
    void testSingleInstance() {
        List<Integer> integerList = new ArrayList<>();
        JedisPool jedisPool = redisPoolFactory();
//        new Thread(() -> {
//            EasyRedis easyRedis = EasyRedis.getInstance(redisPoolFactory());
//            if (hashcode2.get() == 0) {
//                hashcode2.set(easyRedis.hashCode());
//            }
//            assertTrue(hashcode2.get() == easyRedis.hashCode());
//        }).start();
//        new Thread(() -> {
//            EasyRedis easyRedis = EasyRedis.getInstance(redisPoolFactory());
//            if (hashcode2.get() == 0) {
//                hashcode2.set(easyRedis.hashCode());
//            }
//            assertTrue(hashcode2.get() == easyRedis.hashCode());
//        }).start();
//        fixedThreadPool.execute(() -> {
//            EasyRedis easyRedis = EasyRedis.getInstance(redisPoolFactory());
//            integerList.add(easyRedis.hashCode());
//        });
        fixedThreadPool1.execute(() -> {
            EasyRedis easyRedis = EasyRedis.getInstance(jedisPool, 1);
            integerList.add(easyRedis.hashCode());
        });
        fixedThreadPool1.execute(() -> {
            EasyRedis easyRedis = EasyRedis.getInstance(jedisPool, 2);
            integerList.add(easyRedis.hashCode());
        });
        fixedThreadPool1.execute(() -> {
            EasyRedis easyRedis = EasyRedis.getInstance(jedisPool, 3);
            integerList.add(easyRedis.hashCode());
        });
        fixedThreadPool1.execute(() -> {
            EasyRedis easyRedis = EasyRedis.getInstance(jedisPool, 4);
            integerList.add(easyRedis.hashCode());
        });
//        System.out.println("===1>>" + supplier.get());
//        System.out.println("=2==>>" + supplier.get());
//        EasyRedis instance = EasyRedis.getInstance(redisPoolFactory());
//        System.out.println(instance);
//        System.out.println(EasyRedis.getInstance(redisPoolFactory()));
//        System.out.println(redisPoolFactory());
//        EasyRedis easyRedis = new EasyRedis(redisPoolFactory());
//        System.out.println(easyRedis);
//        System.out.println(new EasyRedis(redisPoolFactory()));
        if (!fixedThreadPool1.isShutdown()) {
            fixedThreadPool1.shutdown();
        }
        try {
            fixedThreadPool1.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
        }
        for (int hashCode : integerList) {
            if (hashcode.get() == 0) {
                hashcode.set(hashCode);
            }
            assertNotEquals(hashcode.get(), hashCode);
        }
        // 刷新单例
        fixedThreadPool2.execute(() -> {
            EasyRedis easyRedis = EasyRedis.getInstance(jedisPool, 2, true);
            hashcode2.set(easyRedis.hashCode());
        });
        if (!fixedThreadPool2.isShutdown()) {
            fixedThreadPool2.shutdown();
        }
        try {
            fixedThreadPool2.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
        }
        for (int hashCode : integerList) {
            assertNotEquals(hashcode2.get(), hashCode);
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
        int dbIndex = 13;
        String key = "set_test_01";
        String value = "sdjfhkj";
        String set = instance.set(key, value);
        assertEquals("OK", set);
        instance.set("set_test_02", value, 1000);
        String key3 = "set_test_03";
        instance.set(dbIndex, key3, value, 1000);
        assertEquals(value, instance.get(dbIndex, key3));
        String setBytes = instance.set("byte_test_01".getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8));
        assertEquals("OK", setBytes);
    }

    // redis get 测试
    @Test
    void getTest() {
        EasyRedis instance = EasyRedis.getInstance(redisPoolFactory());
        int dbIndex = 2;
        String key = "set_test_01";
        byte[] byteKey = "byte_test_01".getBytes(StandardCharsets.UTF_8);
        String value = "sdjfhkj";
        String get = instance.get(key);
        assertNull(get);
        assertNull(instance.get(1, "set_test_03"));
        assertEquals("null", Arrays.toString(instance.get(byteKey)));
        final byte[] bytes = new byte[]{115, 100, 106, 102, 104, 107, 106};
        assertArrayEquals(bytes, instance.get(1, byteKey));
        assertEquals(Lists.newArrayList(null, null), instance.mget(0, key, "set_test_02"));

        final Set<String> allKeys = instance.getAllKeys(GET_TEST_INDEX, "*");
        assertTrue(ListUtils.isEmpty(allKeys));
    }

    @Test
    void delTest() {
        EasyRedis instance = supplier.get();
        String key = "set_test_01";
        assertEquals(1, instance.del(key));
        assertEquals(1, instance.del("byte_test_01".getBytes(StandardCharsets.UTF_8)));
        assertEquals(0, instance.del(1, "set_test_03"));
//        System.out.println("---1-<>>>><>>" + instance.del(key));
//        System.out.println("--2--<>>>><>>" + instance.del("byte_test_01".getBytes(StandardCharsets.UTF_8)));
//        System.out.println("---3-<>>>><>>" + instance.del(1, "set_test_03"));
    }

    @Test
    void existsTest() {
        String existsKey = "existsKey_01";
        EasyRedis instance = supplier.get();
        assertFalse(instance.exists(existsKey));
        instance.set(existsKey, "abc");
        assertTrue(instance.exists(existsKey));
        assertEquals(1, instance.del(existsKey));
//        System.out.println("---1-<>>>><>>" + instance.exists(key));
    }

    @Test
    void expireTest() {
        EasyRedis easyRedis = supplier.get();
        String key = "expire_test_01";
        String value = "sdjfhkj";
        easyRedis.set(key, value, 100);
        Long expire = easyRedis.expire(key, 1999);
        assertEquals(1, (long) expire);
//        System.out.println(expire);
    }

    @Test
    void tllTest() {
        EasyRedis easyRedis = supplier.get();
        String key = "ttl_test_key_01";
        Long ttl = easyRedis.ttl(key);
        assertEquals(-2, (long) ttl);
        assertEquals(-2, (long) easyRedis.ttl(1, key));
//        System.out.println("tll=1==>>" + ttl);
//        System.out.println("tll===2>>" + easyRedis.ttl(1, key));
    }

    // redis自增测试
    @Test
    void incrTest() {
        String key = "incr_test_01";
        EasyRedis easyRedis = supplier.get();
        final long l = easyRedis.del(key);
        assertEquals(1, l);
        Long ttl = easyRedis.incr(key);
        assertEquals(1, ttl.intValue());
    }

    @Test
    void hsetTest() {
        String url1 = "www.baidu.com";
        EasyRedis easyRedis = supplier.get();
        String key = "test02";
        Long hset = easyRedis.hset(key, "json", "www.google.com");
        assertEquals(0, hset);
//        System.out.println(hset);
        easyRedis.hset(key, "url", url1);
        assertEquals(url1, easyRedis.hget(key, "url"));
        easyRedis.hset(key, "account", "hugh");
//        System.out.println(easyRedis.hget(key, "url"));
        System.out.println(easyRedis.hgetAll(key));
        Long account = easyRedis.hdel(key, "account");
        assertEquals(1, account);
//        System.out.println("===del=>" + account);
    }

    @Test
    void dbsizeTest() {
        EasyRedis easyRedis = supplier.get();
//        System.out.println("-->" + easyRedis.dbSize());
        assertTrue(easyRedis.dbSize(4) == 0);
//        System.out.println("-->" + );
    }
}
