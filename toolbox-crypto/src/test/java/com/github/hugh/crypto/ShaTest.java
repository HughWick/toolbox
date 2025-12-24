package com.github.hugh.crypto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

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
        Assertions.assertEquals("", ShaUtils.hmacSha256(DATA_HELLO, (String) null));

        // 2. byte[], String 重载 - 代码逻辑是抛出 IllegalArgumentException
        IllegalArgumentException exception1 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ShaUtils.hmacSha256(new byte[0], (String) null);
        });
        Assertions.assertEquals("Secret key cannot be null", exception1.getMessage());

        // 3. String, byte[] 重载 - 代码逻辑是返回 ""
//        Assertions.assertEquals("", ShaUtils.hmacSha256((byte[]) null, new byte[0]));

// 测试 Key 为 null
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ShaUtils.hmacSha256(new byte[0], (byte[]) null);
        }, "Key为null时应抛出 IllegalArgumentException");

// 测试 Data 为 null
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ShaUtils.hmacSha256((byte[]) null, new byte[0]);
        }, "Data为null时应抛出 IllegalArgumentException");
    }

    /**
     * 测试 SHA-256: 验证 String 入参和 byte[] 入参结果是否一致
     */
    @Test
    void testLowerCase256_Consistency() {
        String content = "Hello, World! 你好世界 123";
        // 1. 获取字符串直接加密的结果
        String resultStr = ShaUtils.lowerCase256(content);
        // 2. 获取字节数组加密的结果 (使用 UTF-8 保持一致)
        String resultBytes = ShaUtils.lowerCase256(content.getBytes(StandardCharsets.UTF_8));
        System.out.println("SHA-256 (String): " + resultStr);
        System.out.println("SHA-256 (Bytes) : " + resultBytes);
        // 断言：两者必须相等
        assertEquals(resultStr, resultBytes, "SHA-256 String和Byte[]加密结果不一致");
    }

    /**
     * 测试 SHA-512: 验证 String 入参和 byte[] 入参结果是否一致
     */
    @Test
    void testLowerCase512_Consistency() {
        String content = "Test Content For SHA-512";
        String resultStr = ShaUtils.lowerCase512(content);
        String resultBytes = ShaUtils.lowerCase512(content.getBytes(StandardCharsets.UTF_8));
        System.out.println("SHA-512 (String): " + resultStr);
        System.out.println("SHA-512 (Bytes) : " + resultBytes);
        assertEquals(resultStr, resultBytes, "SHA-512 String和Byte[]加密结果不一致");
    }

    /**
     * 标准值验证：使用已知标准哈希值验证算法准确性
     * 测试用例：字符串 "hello"
     */
    @Test
    void testStandardVectors() {
        String input = "hello";
        byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
        // 标准 SHA-256 ("hello") 的十六进制值
        String expected256 = "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824";
        // 标准 SHA-512 ("hello") 的十六进制值
        String expected512 = "9b71d224bd62f3785d96d46ad3ea3d73319bfbc2890caadae2dff72519673ca72323c3d99ba5c11d7c7acc6e14b8c5da0c4663475c2e5c3adef46f73bcdec043";
        // 验证 256
        assertEquals(expected256, ShaUtils.lowerCase256(inputBytes), "SHA-256 计算错误");
        // 验证 512
        assertEquals(expected512, ShaUtils.lowerCase512(inputBytes), "SHA-512 计算错误");
    }

    /**
     * 二进制数据测试：模拟图片或文件的 byte 流（非纯文本）
     */
    @Test
    void testBinaryData() {
        // 构造一个包含特殊字节的数组（例如 0xFF, 0x00 等非打印字符）
        byte[] binaryData = new byte[]{(byte) 0xFF, (byte) 0x00, (byte) 0xAB, (byte) 0x12};
        String sha256 = ShaUtils.lowerCase256(binaryData);
        String sha512 = ShaUtils.lowerCase512(binaryData);
        System.out.println("Binary SHA-256: " + sha256);
        System.out.println("Binary SHA-512: " + sha512);
        // 验证结果不为空
        assertNotNull(sha256);
        assertNotNull(sha512);
        // 验证长度 (SHA-256 Hex 长度应为 64字符, SHA-512 Hex 长度应为 128字符)
        assertEquals(64, sha256.length());
        assertEquals(128, sha512.length());
        // 验证是否只包含十六进制字符
        assertTrue(sha256.matches("^[0-9a-f]+$"));
    }

    /**
     * 边界测试：空数组
     */
    @Test
    void testEmptyBytes() {
        byte[] empty = new byte[0];
        // 根据你的 Md5Utils 实现，如果 byte 长度为 0，可能返回 null 或者 ""，或者是空串的 hash
        // 假设之前代码逻辑是: if (data == null || data.length == 0) return null;
        String result = ShaUtils.lowerCase256(empty);
        // 如果你的业务逻辑是返回 null
        // Assert.assertNull(result);

        // 如果你的业务逻辑是计算空串的 hash (e3b0c442...)，请根据实际 Md5Utils 修改断言
        // 注意：之前的 Md5Utils 代码主要逻辑是 EmptyUtils.isEmpty 判断，通常返回原值或 null
        System.out.println("Empty input result: " + result);
    }
}
