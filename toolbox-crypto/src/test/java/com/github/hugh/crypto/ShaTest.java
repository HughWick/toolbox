package com.github.hugh.crypto;

import com.github.hugh.exception.ToolboxException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * sha 测试
 *
 * @author AS
 * @date 2021/3/1 13:27
 */
class ShaTest {
    // 正确的 HMAC-SHA256 结果
    private static final String DATA_HELLO = "hello";
    private static final String KEY_SECRET = "secret";
    private static final String EXPECTED_HELLO_SECRET = "88aab3ede8d3adf94d26ab90d3bafd4a2083070c3bcce9c014ee04a443847c0b";

    // 2. Data: "" (空字符串), Key: "key"
    private static final String EXPECTED_EMPTY_DATA = "5d5d139563c95b5967b9bd9a8c9b233a9dedb45072794cd232dc1b74832607d0";

    // 3. Data: "测试", Key: "123" (验证中文 UTF-8)
    private static final String EXPECTED_CN_DATA = "685161c14ea864ccb57522605ff6db44a062ace3f9db0cb59b7dc160b81e9cb0";
    @Test
    void testSha() {
        //        final String md5Sign = Md5Utils.lowerCase(appkey + timestamp + masterSecret);
//        System.out.println("-md5Sign-->>" + md5Sign);
        String appkey = "ygjzNnfrEJ9cJ9PPzgI1cA";
        String timestamp = "1669886260352";
        String masterSecret = "migfmCyjVwAQBooo45PUs2";
        String sign = ShaUtils.lowerCase256(appkey + timestamp + masterSecret);
        assertEquals(64, sign.length());
        assertEquals("c19c433a5933f1de7ca9dd8fb30fbcc97c1f03e2f4fb2aa3e5e0c839b0460e17", sign);
        final String str2 = ShaUtils.lowerCase512("123");
        assertEquals(128, str2.length());
        assertEquals("3c9909afec25354d551dae21590bb26e38d53f2173b8d3dc3eee4c047e7ab1c1eb8b85103e3be7ba613b31bb5c9c36214dc9f14a42fd7a2fdb84856bca5c44c2", str2);
    }

    @Test
    @DisplayName("测试最常用的 String + String 重载")
    void testHmacSha256StringString() {
        // 1. 标准情况
        String result = ShaUtils.hmacSha256(DATA_HELLO, KEY_SECRET);
        Assertions.assertEquals(EXPECTED_HELLO_SECRET, result, "标准输入计算结果不匹配");
        // 2. 空字符串测试
        String emptyResult = ShaUtils.hmacSha256("", "key");
        Assertions.assertEquals(EXPECTED_EMPTY_DATA, emptyResult, "空字符串Payload计算错误");
        // 3. 中文测试 (验证 UTF-8 编码是否生效)
        String cnData = "测试";
        String cnKey = "123";
        Assertions.assertEquals(EXPECTED_CN_DATA, ShaUtils.hmacSha256(cnData, cnKey), "中文UTF-8处理错误");
    }

    @Test
    @DisplayName("测试 byte[] + String 重载 (模拟 Protobuf 场景)")
    void testHmacSha256BytesString() {
        byte[] dataBytes = DATA_HELLO.getBytes(StandardCharsets.UTF_8);
        String result = ShaUtils.hmacSha256(dataBytes, KEY_SECRET);
        Assertions.assertEquals(EXPECTED_HELLO_SECRET, result);
    }

    @Test
    @DisplayName("测试 String + byte[] 重载 (模拟二进制密钥)")
    void testHmacSha256StringBytes() {
        byte[] keyBytes = KEY_SECRET.getBytes(StandardCharsets.UTF_8);
        String result = ShaUtils.hmacSha256(DATA_HELLO, keyBytes);
        assertEquals(EXPECTED_HELLO_SECRET, result);
    }

    @Test
    @DisplayName("测试核心方法 byte[] + byte[]")
    void testCoreMethod() {
        byte[] dataBytes = DATA_HELLO.getBytes(StandardCharsets.UTF_8);
        byte[] keyBytes = KEY_SECRET.getBytes(StandardCharsets.UTF_8);
        String result = ShaUtils.hmacSha256(dataBytes, keyBytes);
        Assertions.assertEquals(EXPECTED_HELLO_SECRET, result);
    }

    @Test
    @DisplayName("测试 Null 安全性与异常处理")
    void testNullHandling() {
        // 1. String, String 重载 - 代码逻辑是返回 ""
        Assertions.assertEquals("", ShaUtils.hmacSha256((String) null, KEY_SECRET));
        Assertions.assertEquals("", ShaUtils.hmacSha256(DATA_HELLO, (String)null));

        // 2. byte[], String 重载 - 代码逻辑是抛出 IllegalArgumentException
        IllegalArgumentException exception1 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ShaUtils.hmacSha256(new byte[0], (String)null);
        });
        Assertions.assertEquals("Secret key cannot be null", exception1.getMessage());

        // 3. String, byte[] 重载 - 代码逻辑是返回 ""
//        Assertions.assertEquals("", ShaUtils.hmacSha256((byte[]) null, new byte[0]));

        // 测试 Key 为 null (核心方法)
        // 修正：将 null 强转为 (byte[]) 以明确调用 hmacSha256(byte[], byte[])
        Assertions.assertThrows(ToolboxException.class, () -> {
            ShaUtils.hmacSha256(new byte[0], (byte[]) null);
        }, "Key为null时核心方法应抛出 ToolboxException");

        // 测试 Data 为 null (核心方法)
        // 修正：将 null 强转为 (byte[]) 以明确调用 hmacSha256(byte[], byte[])
        // 否则编译器会混淆 hmacSha256(byte[], byte[]) 和 hmacSha256(String, byte[])
        Assertions.assertThrows(ToolboxException.class, () -> {
            ShaUtils.hmacSha256((byte[]) null, new byte[0]);
        }, "Data为null时核心方法应抛出 ToolboxException");
    }
}
