package com.github.hugh.cache.redis;

import com.github.hugh.exception.ToolboxException;
import com.google.common.base.Suppliers;
import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 简化版本的redis操作工具类
 *
 * @author Hugh
 * @since 2.1.3
 **/
public class EasyRedis {

    /**
     * 内存单例工厂
     */
    private static Supplier<EasyRedis> defaultEasyRedisSupp;

    /**
     * 设置指定数据库索引的单例工厂
     */
    private static Supplier<EasyRedis> easyRedisSupp;

    /**
     * redis库索引
     */
    private final int dbIndex;

    /**
     * redis连接池
     */
    private final JedisPool jedisPool;

    public EasyRedis(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
        this.dbIndex = 0;
    }

    public EasyRedis(JedisPool jedisPool, int dbIndex) {
        this.jedisPool = jedisPool;
        this.dbIndex = dbIndex;
    }

    /**
     * 获取单例类
     *
     * @param jedisPool jedis连接池
     * @return EasyRedis
     */
    public static synchronized EasyRedis getInstance(JedisPool jedisPool) {
        if (defaultEasyRedisSupp == null) {
            Supplier<EasyRedis> easyRedisSupplier = () -> new EasyRedis(jedisPool);
            defaultEasyRedisSupp = Suppliers.memoize(easyRedisSupplier::get);
        }
        return defaultEasyRedisSupp.get();
    }

    /**
     * 获取单例类
     *
     * @param jedisPool jedis连接池
     * @param dbIndex   库索引
     * @return EasyRedis
     */
    public static synchronized EasyRedis getInstance(JedisPool jedisPool, int dbIndex) {
        return getInstance(jedisPool, dbIndex, false);
    }

    /**
     * 获取单例类
     *
     * @param jedisPool jedis连接池
     * @param dbIndex   库索引
     * @param refresh   刷新标识
     * @return EasyRedis
     */
    public static synchronized EasyRedis getInstance(JedisPool jedisPool, int dbIndex, boolean refresh) {
        if (easyRedisSupp == null || refresh) {
            Supplier<EasyRedis> easyRedisSupplier = () -> new EasyRedis(jedisPool, dbIndex);
            easyRedisSupp = Suppliers.memoize(easyRedisSupplier::get);
        }
        return easyRedisSupp.get();
    }

    /**
     * set
     * <p>有效时间为-1（永久）</p>
     *
     * @param key   键
     * @param value 值
     * @return String
     */
    public String set(String key, String value) {
        return set(key, value, -1);
    }

    /**
     * 根据key将value放入redis缓存，并且设置过期时间
     *
     * @param key     唯一键
     * @param value   值
     * @param seconds 时间：单位秒
     * @return String
     */
    public String set(String key, String value, int seconds) {
        return set(dbIndex, key, value, seconds);
    }

    /**
     * 放入缓存
     *
     * @param dbIndex 数据库索引
     * @param key     唯一键
     * @param value   值
     * @param seconds 存储时间,单位：秒
     * @return String
     */
    public String set(int dbIndex, String key, String value, long seconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(dbIndex);
            String result;
            if (seconds <= 0) {
                result = jedis.setex(key, 99, value);
                // 永不过期
                jedis.persist(key);
            } else {
                result = jedis.setex(key, seconds, value);
            }
            return result;
        } catch (Exception exception) {
            throw new ToolboxException(exception);
        }
    }

    /**
     * 将键与数据 以数组的形式 set
     *
     * @param key   键
     * @param value 值
     * @return String
     */
    public String set(byte[] key, byte[] value) {
        return set(dbIndex, key, value);
    }

    /**
     * set 字节数组的key与value
     *
     * @param key     键
     * @param value   值
     * @param seconds 时间，单位：秒
     * @return String 成功返回：OK
     * @since 2.3.2
     */
    public String set(byte[] key, byte[] value, int seconds) {
        return set(dbIndex, key, value, seconds);
    }

    /**
     * set 字节数组的key与value
     * <p>默认存储时间为永久</p>
     *
     * @param dbIndex 数据库索引
     * @param key     key字节数组
     * @param value   值数组
     * @return String
     */
    public String set(int dbIndex, byte[] key, byte[] value) {
        return set(dbIndex, key, value, -1);
    }

    /**
     * set 字节数组的key与value
     * <p>-1，存储时间为永久</p>
     *
     * @param dbIndex 数据库索引
     * @param key     key字节数组
     * @param value   值数组
     * @param seconds 时间，单位：秒
     * @return String
     * @since 2.3.2
     */
    public String set(int dbIndex, byte[] key, byte[] value, int seconds) {
        return set(dbIndex, new String(key, StandardCharsets.UTF_8), new String(value, StandardCharsets.UTF_8), seconds);
    }

    /**
     * get
     *
     * @param key 键
     * @return String
     */
    public String get(String key) {
        return get(dbIndex, key);
    }

    /**
     * 获取不同redis库下数据
     *
     * @param dbIndex db索引
     * @param key     唯一键
     * @return String
     */
    public String get(int dbIndex, String key) {
        return executeWithJedis(dbIndex, jedis -> jedis.get(key));
    }

    /**
     * 根据多个kye 获取list结果集
     *
     * @param dbIndex 数据库索引
     * @param keys    多个键
     * @return List
     */
    public List<String> mget(int dbIndex, String... keys) {
        return executeWithJedis(dbIndex, jedis -> jedis.mget(keys));
    }

    /**
     * 根据多个kye 获取list结果集
     *
     * @param keys 多个键
     * @return List
     * @since 2.3.4
     */
    public List<String> mget(String... keys) {
        return mget(this.dbIndex, keys);
    }

    /**
     * 根据字节数组的key 查询数据
     *
     * @param key 键
     * @return byte[]
     */
    public byte[] get(byte[] key) {
        return get(dbIndex, key);
    }

    /**
     * 获取对应数据库索引下的 byte[] 数据
     *
     * @param dbIndex 数据库索引
     * @param key     键
     * @return byte[]
     */
    public byte[] get(int dbIndex, byte[] key) {
        return executeWithJedis(dbIndex, jedis -> jedis.get(key));
    }

    /**
     * 删除对应key
     *
     * @param key KEY
     * @return Long 被删除 key 的数量
     */
    public Long del(String key) {
        return del(dbIndex, key);
    }

    /**
     * 删除多个key
     *
     * @param dbIndex 库索引
     * @param keys    KEY数组
     * @return Long 被删除 key 的数量
     */
    public Long del(int dbIndex, String... keys) {
        return executeWithJedis(dbIndex, jedis -> jedis.del(keys));
    }

    /**
     * 删除对应字节数组的key
     *
     * @param key KEY
     * @return Long 被删除 key 的数量
     */
    public Long del(byte[] key) {
        return del(dbIndex, key);
    }

    /**
     * 删除多个key数组
     *
     * @param dbIndex 库索引
     * @param keys    多个KEY数组
     * @return Long
     */
    public Long del(int dbIndex, byte[]... keys) {
        return executeWithJedis(dbIndex, jedis -> jedis.del(keys));
    }

    /**
     * EXISTS 命令用于检查给定 key 是否存在。
     *
     * @param key KEY
     * @return Boolean
     */
    public Boolean exists(String key) {
        return exists(dbIndex, key);
    }

    /**
     * EXISTS 命令用于检查给定 key 是否存在。
     *
     * @param dbIndex 库索引
     * @param key     KEY
     * @return Boolean 若 key 存在返回 {@code true}}，否则返回{@code false}}
     */
    public Boolean exists(int dbIndex, String key) {
        return executeWithJedis(dbIndex, jedis -> jedis.exists(key));
    }

    /**
     * 设置过期时间
     *
     * @param key     KEY
     * @param seconds 过期时间，单位：秒
     * @return Long 设置成功返回 {@code 1}。当 key不存在或者不能为key设置过期时间时(比如在低于 2.1.3 版本的 Redis 中你尝试更新 key 的过期时间)返回0。
     */
    public Long expire(String key, int seconds) {
        return expire(key.getBytes(StandardCharsets.UTF_8), seconds);
    }

    /**
     * 设置过期时间
     * <p>不支持设置永久(过期时间为：{@code -1})</p>
     *
     * @param key     key 字节数组
     * @param seconds 过期时间，单位：秒
     * @return Long 设置成功返回 {@code 1}。当 key不存在或者不能为key设置过期时间时(比如在低于 2.1.3 版本的 Redis 中你尝试更新 key 的过期时间)返回0。
     */
    public Long expire(byte[] key, long seconds) {
        return expire(dbIndex, key, seconds);
    }

    /**
     * 设置过期时间
     *
     * @param dbIndex 库索引
     * @param key     key 字节数组
     * @param seconds 过期时间，单位：秒
     * @return Long 设置成功返回 {@code 1}。当 key不存在或者不能为key设置过期时间时(比如在低于 2.1.3 版本的 Redis 中你尝试更新 key 的过期时间)返回0。
     */
    public Long expire(int dbIndex, byte[] key, long seconds) {
        return executeWithJedis(dbIndex, jedis -> jedis.expire(key, seconds));
    }

    /**
     * TTL 命令以秒为单位返回 key 的剩余过期时间。
     * <p>
     * 在 Redis 2.8 以前，当 key 不存在，或者 key 没有设置剩余生存时间时，命令都返回 -1
     * </p>
     *
     * @param key KEY
     * @return Long 当key不存在时，返回 {@code -2},当key存在但没有设置剩余生存时间时，返回{@code -1},否则，以秒为单位，返回 key 的剩余生存时间。
     */
    public Long ttl(String key) {
        return ttl(dbIndex, key);
    }

    /**
     * TTL 命令以秒为单位返回 key 的剩余过期时间。
     * <p>
     * 在 Redis 2.8 以前，当 key 不存在，或者 key 没有设置剩余生存时间时，命令都返回 -1
     * </p>
     *
     * @param dbIndex 库索引
     * @param key     KEY
     * @return Long 当key不存在时,返回 {@code -2},当key存在但没有设置剩余生存时间时，返回{@code -1},否则，以秒为单位，返回 key 的剩余生存时间。
     */
    public Long ttl(int dbIndex, String key) {
        return executeWithJedis(dbIndex, jedis -> jedis.ttl(key));
    }

    /**
     * Redis Incr 命令将 key 中储存的数字值增一。
     * <p>
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * 本操作的值限制在 64 位(bit)有符号数字表示之内。
     * </p>
     *
     * @param key KEY
     * @return Long
     */
    public Long incr(String key) {
        return executeWithJedis(dbIndex, jedis -> jedis.incr(key));
    }

    /**
     * Redis Hset 命令
     * <p>默认永不过期</p>
     *
     * @param key   redis key
     * @param field hash表中的键
     * @param value 值
     * @return Long 如果字段是哈希表中的一个新建字段，并且值设置成功，返回{@code 1}。如果哈希表中域字段已经存在且旧值已被新值覆盖，返回 {@code 0}
     */
    public Long hset(String key, String field, String value) {
        return hset(key, field, value, -1);
    }

    /**
     * Redis Hset 命令
     *
     * @param key     redis key
     * @param field   hash表中的键
     * @param value   值
     * @param timeout 过期时间，单位：秒
     * @return Long 如果字段是哈希表中的一个新建字段，并且值设置成功，返回{@code 1}。如果哈希表中域字段已经存在且旧值已被新值覆盖，返回 {@code 0}
     */
    public Long hset(String key, String field, String value, long timeout) {
        return hset(dbIndex, key, field, value, timeout);
    }

    /**
     * Redis Hset 命令
     *
     * @param dbIndex 库索引
     * @param key     redis key
     * @param field   hash表中的键
     * @param value   值
     * @param timeout 过期时间，单位：秒
     * @return Long 如果字段是哈希表中的一个新建字段，并且值设置成功，返回{@code 1}。如果哈希表中域字段已经存在且旧值已被新值覆盖，返回 {@code 0}
     */
    public Long hset(int dbIndex, String key, String field, String value, long timeout) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(dbIndex);
            Long result = jedis.hset(key, field, value);
            if (timeout == -1) {
                // 永不过期
                jedis.persist(key);
            } else {
                jedis.expire(key, timeout);
            }
            return result;
        } catch (Exception exception) {
            throw new ToolboxException(exception);
        }
    }

    /**
     * Redis Hget 命令
     *
     * @param key   redis key
     * @param field hash表中的键
     * @return String 返回给定字段的值。如果给定的字段或 key 不存在时，返回 null 。
     */
    public String hget(String key, String field) {
        return hget(dbIndex, key, field);
    }

    /**
     * Redis Hget 命令
     *
     * @param dbIndex 库索引
     * @param key     redis key
     * @param field   hash表中的键
     * @return String 返回给定字段的值。如果给定的字段或 key 不存在时，返回 null 。
     */
    public String hget(int dbIndex, String key, String field) {
        return executeWithJedis(dbIndex, jedis -> jedis.hget(key, field));
    }

    /**
     * Redis Hget 获取key下所有键值对
     *
     * @param key KEY
     * @return Map
     */
    public Map<String, String> hgetAll(String key) {
        return hgetAll(dbIndex, key);
    }

    /**
     * Redis Hget 获取key下所有键值对
     *
     * @param dbIndex 库索引
     * @param key     KEY
     * @return Map
     */
    public Map<String, String> hgetAll(int dbIndex, String key) {
        return executeWithJedis(dbIndex, jedis -> jedis.hgetAll(key));
    }

    /**
     * Hdel 命令用于删除哈希表 key 中的一个或多个指定字段，不存在的字段将被忽略。
     *
     * @param key    KEY
     * @param fields hash表中的键
     * @return Long 被成功删除字段的数量，不包括被忽略的字段。
     */
    public Long hdel(String key, String... fields) {
        return hdel(dbIndex, key, fields);
    }

    /**
     * Hdel 命令用于删除哈希表 key 中的一个或多个指定字段，不存在的字段将被忽略。
     *
     * @param dbIndex 库索引
     * @param key     KEY
     * @param fields  hash表中的键
     * @return Long 被成功删除字段的数量，不包括被忽略的字段。
     */
    public Long hdel(int dbIndex, String key, String... fields) {
        return executeWithJedis(dbIndex, jedis -> jedis.hdel(key, fields));
    }

    /**
     * 获取整个路径db下所有key
     * <p>
     * 默认对象创建时的db索引
     * </p>
     *
     * @param path 路径
     * @return Set
     */
    public Set<String> getAllKeys(String path) {
        return getAllKeys(this.dbIndex, path);
    }

    /**
     * 获取整个路径db下所有key
     *
     * @param dbIndex 数据库索引标识
     * @param path    路径 {@code  NAME:*}
     * @return Set
     */
    public Set<String> getAllKeys(int dbIndex, String path) {
        return executeWithJedis(dbIndex, jedis -> jedis.keys(path));
    }

    /**
     * Redis Dbsize 命令用于返回当前数据库的 key 的数量。
     *
     * @return long
     * @since 2.3.4
     */
    public long dbSize() {
        return dbSize(this.dbIndex);
    }

    /**
     * Redis Dbsize 命令用于返回当前数据库的 key 的数量。
     *
     * @param dbIndex redis库下标
     * @since 2.3.4
     */
    public long dbSize(int dbIndex) {
        return executeWithJedis(dbIndex, BinaryJedis::dbSize);
    }

    /**
     * 执行 Jedis 操作，并处理连接获取、数据库选择和异常处理。
     *
     * <p>此方法封装了获取 Jedis 连接、选择指定数据库以及执行提供的操作，并在操作过程中发生异常时抛出 ToolboxException。
     * 使用 try-with-resources 确保 Jedis 连接在使用后会被正确关闭。
     *
     * @param dbIndex   要选择的 Redis 数据库索引。
     * @param operation 要执行的 Jedis 操作，它是一个接受 Jedis 对象并返回泛型类型 T 的函数。
     * @param <T>       操作返回的结果类型。
     * @return 执行 Jedis 操作后返回的结果。
     */
    private <T> T executeWithJedis(int dbIndex, Function<Jedis, T> operation) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(dbIndex);
            return operation.apply(jedis);
        } catch (Exception exception) {
            throw new ToolboxException(exception);
        }
    }
}
