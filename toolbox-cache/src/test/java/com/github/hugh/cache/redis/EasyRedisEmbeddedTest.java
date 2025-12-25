package com.github.hugh.cache.redis;

import com.github.hugh.cache.model.Student;
import com.github.hugh.json.gson.GsonUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class EasyRedisEmbeddedTest {

    private static RedisServer redisServer;
    private static EasyRedis easyRedis;
    private static JedisPool jedisPool;

    @BeforeAll
    static void setUp() throws IOException {
        // 1. 启动内存 Redis (端口 6379)
        redisServer = new RedisServer(6379);
        try {
            redisServer.start();
        } catch (Exception e) {
            System.err.println("Redis启动失败，可能是端口占用，请检查: " + e.getMessage());
        }

        // 2. 初始化 JedisPool
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(10);
        // 注意：Embedded Redis 默认无密码，这里IP设为localhost
        jedisPool = new JedisPool(config, "localhost", 6379);

        // 3. 初始化待测工具类
        easyRedis = new EasyRedis(jedisPool);
    }

    @AfterAll
    static void tearDown() throws IOException {
        if (jedisPool != null) {
            jedisPool.close();
        }
        if (redisServer != null) {
            redisServer.stop();
        }
    }

    /**
     * 辅助方法：构造一个属性齐全的 Student 对象
     */
    private Student createFullStudent() {
        Student student = new Student();
        student.setId(10086L);
        student.setAge(25);
        student.setName("Hugh Wick");
        student.setAmount(9999.99);
        student.setBalance(50.5);
        student.setBirthday(new Date()); // 注意：JSON转换由于精度问题，毫秒级可能会有差异，比较时需注意
        student.setCreate(new Date());
        student.setCity("Chengdu");
        student.setSex("Male");
        student.setSystem(1L);
        student.setAccount("hugh_acc");
        student.setAccountName("Hugh Account");
        student.setAccountType("VIP");
        student.setPassword("123456");
        student.setPhone("13800138000");
        student.setPhoneType("Mobile");
        student.setIp("127.0.0.1");
        student.setRole("ADMIN");
        student.setAuthorization("ALL");
        return student;
    }

    /**
     * 测试场景：
     * 1. 构造 Java 对象
     * 2. 转 JSON 字符串
     * 3. set(String, String) 存入
     * 4. get(String, Class) 取出并自动转回对象
     */
    @Test
    void testSetAndGetStudent_StringMode() {
        // 1. 准备数据
        String key = "student:10086";
        Student originalStudent = createFullStudent();
        // 模拟业务层：先转成 JSON 字符串
        String jsonValue = GsonUtils.toJson(originalStudent);
        // 2. 调用 set 方法 (String版本)
        String setRet = easyRedis.set(key, jsonValue);
        assertEquals("OK", setRet, "Redis set 应该返回 OK");
        // 3. 调用 get 方法 (泛型版本)
        Student retrievedStudent = easyRedis.get(key, Student.class);
        // 4. 验证
        assertNotNull(retrievedStudent);
        assertEquals(originalStudent.getId(), retrievedStudent.getId());
        assertEquals(originalStudent.getName(), retrievedStudent.getName());
        assertEquals(originalStudent.getAmount(), retrievedStudent.getAmount());
        // 打印结果查看
        System.out.println("存入 JSON: " + jsonValue);
        System.out.println("取出 Object: " + retrievedStudent);
    }

    /**
     * 测试场景：
     * 验证 byte[] 版本的 set 方法
     * 注意：你的源码中 set(byte[], byte[]) 内部强转为了 new String(bytes)，
     * 所以这里测试 JSON 的字节数组是没问题的。
     */
    @Test
    void testSetAndGetStudent_ByteMode() {
        // 1. 准备数据
        String keyStr = "student:byte:10086";
        byte[] keyBytes = keyStr.getBytes(StandardCharsets.UTF_8);

        Student originalStudent = createFullStudent();
        originalStudent.setName("Byte Mode Tester"); // 修改一下名字区分

        String jsonValue = GsonUtils.toJson(originalStudent);
        byte[] valueBytes = jsonValue.getBytes(StandardCharsets.UTF_8);
        // 2. 调用 set 方法 (byte[] 版本)
        String setRet = easyRedis.set(keyBytes, valueBytes);
        assertEquals("OK", setRet);
        // 3. 调用 get 方法 (注意：get(byte[]) 返回的是 byte[]，还是 get(String, Class) ?)
        // 这里演示用 get(String, Class) 混合读取，验证兼容性
        Student retrievedStudent = easyRedis.get(keyStr, Student.class);
        // 4. 验证
        assertNotNull(retrievedStudent);
        assertEquals("Byte Mode Tester", retrievedStudent.getName());
        // 验证底层是否真的是存的字符串
        String rawValue = easyRedis.get(keyStr);
        assertEquals(jsonValue, rawValue, "底层存储的应该是 JSON 字符串");
    }

    /**
     * 测试空值处理
     */
    @Test
    void testGetNonExistentKey() {
        String key = "student:not:exists";
        Student student = easyRedis.get(key, Student.class);
        assertNull(student, "不存在的 Key 应该返回 null");
    }
}
