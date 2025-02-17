package com.github.hugh.cache.redis;

import com.github.hugh.util.ListUtils;
import com.github.hugh.util.RandomUtils;
import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * redis 工具类测试
 * User: AS
 * Date: 2021/11/11 10:44
 */
@Configuration
class EasyRedisTest {

    public static final String IP_ADDR = "141.147.176.125";
    //    public static final String IP_ADDR = "192.168.10.245";
    public static final String PASSWORD = "password123";
    public static final int PORT = 7779;
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
//        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "192.168.1.45", PORT, 10000, "jugg#8225586");
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, IP_ADDR, PORT, 10000, PASSWORD);
//        log.info("初始化redis pool。end...");
        System.out.println("初始化redis pool。end...");
        return jedisPool;
    }


    @BeforeEach
    public void init() {
        System.out.println("init...");

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
//    Supplier<EasyRedis> supplier2 = Suppliers.memoizeWithExpiration(easyRedisSupplier::get, 5, TimeUnit.SECONDS);

    /**
     * 线程安全的int
     */
//    static AtomicInteger hashcode = new AtomicInteger(0);
    static AtomicInteger hashcode2 = new AtomicInteger(0);

    static ThreadPoolExecutor fixedThreadPool1 = new ThreadPoolExecutor(10, 10, 20, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    static ThreadPoolExecutor fixedThreadPool2 = new ThreadPoolExecutor(10, 10, 20, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    @Test
    void testSingleInstance() {
        List<Integer> integerList = new ArrayList<>();
        // 创建 JedisPool 对象
        JedisPool jedisPool = redisPoolFactory();
        // 开启固定数量的线程池
        fixedThreadPool1.execute(() -> {
            // 获取 EasyRedis 实例, 并将其 hashCode 添加到 integerList 中
            EasyRedis easyRedis = EasyRedis.getInstance(jedisPool, 1);
            integerList.add(easyRedis.hashCode());
        });
        fixedThreadPool1.execute(() -> {
            // 获取 EasyRedis 实例, 并将其 hashCode 添加到 integerList 中
            EasyRedis easyRedis = EasyRedis.getInstance(jedisPool, 2);
            integerList.add(easyRedis.hashCode());
        });
        fixedThreadPool1.execute(() -> {
            // 获取 EasyRedis 实例, 并将其 hashCode 添加到 integerList 中
            EasyRedis easyRedis = EasyRedis.getInstance(jedisPool, 3);
            integerList.add(easyRedis.hashCode());
        });
        fixedThreadPool1.execute(() -> {
            // 获取 EasyRedis 实例, 并将其 hashCode 添加到 integerList 中
            EasyRedis easyRedis = EasyRedis.getInstance(jedisPool, 4);
            integerList.add(easyRedis.hashCode());
        });
        // 关闭线程池
        if (!fixedThreadPool1.isShutdown()) {
            fixedThreadPool1.shutdown();
        }
        try {
            // 等待所有线程执行完成
            fixedThreadPool1.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }
//        for (int hashCode : integerList) {
//            if (hashcode.get() == 0) {
//                hashcode.set(hashCode);
//            }
//            assertNotEquals(hashcode.get(), hashCode);
//        }
        // 刷新单例
        fixedThreadPool2.execute(() -> {
            // 获取 EasyRedis 实例, 并将其 hashCode 添加到 hashcode2 中
            EasyRedis easyRedis = EasyRedis.getInstance(jedisPool, 2, true);
            hashcode2.set(easyRedis.hashCode());
        });
        // 关闭线程池
        if (!fixedThreadPool2.isShutdown()) {
            fixedThreadPool2.shutdown();
        }
        try {
            // 等待所有线程执行完成
            fixedThreadPool2.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
        }
        // 判断 integerList 中的 hashCode 是否与 hashcode2 相等
        for (int hashCode : integerList) {
            assertNotEquals(hashcode2.get(), hashCode);
        }
    }

//    @Test
//    void test01() {
//        JedisPool jedisPool = redisPoolFactory();
//        System.out.println("---->>" + redisPoolFactory());
//        System.out.println(new EasyRedis(jedisPool));
//        EasyRedis instance = EasyRedis.getInstance(redisPoolFactory());
//        System.out.println("===>>" + instance);
//        System.out.println("===>>" + EasyRedis.getInstance(redisPoolFactory()));
//    }

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
        instance.set(dbIndex, byteKey, value.getBytes(StandardCharsets.UTF_8));
        final byte[] bytes = new byte[]{115, 100, 106, 102, 104, 107, 106};
        assertArrayEquals(bytes, instance.get(dbIndex, byteKey));
    }

    @Test
    void mgetTest() {
        int dbIndex = 0;
        String key1 = "mget_test_01";
        String key2 = "mget_test_02";
        EasyRedis instance = EasyRedis.getInstance(redisPoolFactory());
        assertEquals(Lists.newArrayList(null, null), instance.mget(dbIndex, key1, key2));
        String str1 = RandomUtils.randomString(5);
        String set = instance.set(key1, str1);
        assertEquals("OK", set);
        List<String> mget1 = instance.mget(0, key1);
        assertEquals(Lists.newArrayList(str1), mget1);
        instance.del(dbIndex, key1);
    }

    @Test
    void getAllKeys() {
        EasyRedis easyRedis = EasyRedis.getInstance(redisPoolFactory());
        final Set<String> allKeys = easyRedis.getAllKeys(GET_TEST_INDEX, "*");
        assertTrue(ListUtils.isEmpty(allKeys));
        final Set<String> allKeys2 = easyRedis.getAllKeys("*");
        assertTrue(ListUtils.isEmpty(allKeys2));
    }

    @Test
    void delTest() {
        EasyRedis instance = supplier.get();
        String key = "del_test_01";
        instance.set(key, RandomUtils.randomString(5));
        assertEquals(1, instance.del(key));
        instance.set(key.getBytes(StandardCharsets.UTF_8), RandomUtils.randomString(5).getBytes(StandardCharsets.UTF_8));
        assertEquals(1, instance.del(key.getBytes(StandardCharsets.UTF_8)));
//        assertEquals(0, instance.del(1, "set_test_03"));
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
        Long ttl1 = easyRedis.incr(key);
        assertEquals(1, ttl1.intValue());
        Long ttl2 = easyRedis.incr(key);
        assertEquals(2, ttl2.intValue());
        final long del = easyRedis.del(key);
        assertEquals(1, del);
    }

    @Test
    void hsetTest() {
        String url1 = "www.baidu.com";
        String cacheKey1 = "hset_test_01";
        EasyRedis easyRedis = supplier.get();
        String field1 = "json";
        String field2 = "url";
        String field3 = "account";
        Long hset = easyRedis.hset(cacheKey1, field1, "www.google.com");
        assertEquals(1, hset);
        easyRedis.hset(cacheKey1, field2, url1);
        assertEquals(url1, easyRedis.hget(cacheKey1, field2));
        easyRedis.hset(cacheKey1, field3, "hugh");
        assertTrue(easyRedis.hexists(cacheKey1, field2));
        assertFalse(easyRedis.hIsNotExists(cacheKey1, field2));
        assertTrue(easyRedis.hIsNotExists(15, cacheKey1, field2));
        assertFalse(easyRedis.hIsNotExists(1, cacheKey1, field1));
        Long account = easyRedis.hdel(cacheKey1, field3);
        assertEquals(1, account);
        Long hdel2 = easyRedis.hdel(cacheKey1, field1, field2);
        assertEquals(2, hdel2);
    }


    @Test
    void hgetAllTest() {
        EasyRedis easyRedis = supplier.get();
        String key1 = "ab";
        String field1 = "json";
        Map<String, String> stringStringMap = easyRedis.hgetAll(key1);
        assertTrue(stringStringMap.isEmpty());
        Long hset = easyRedis.hset(key1, field1, "www.google.com");
        assertEquals(1, hset);
        Long hdel = easyRedis.hdel(key1, field1);
        assertEquals(1, hdel);
    }


    @Test
    void dbSizeTest() {
        EasyRedis easyRedis = supplier.get();
        assertEquals(0, easyRedis.dbSize(4));
        assertEquals(4, easyRedis.dbSize());
    }
}
