package com.github.hugh.crypto.util;

import com.github.hugh.crypto.components.CryptoCore;
import com.github.hugh.exception.ToolboxException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("CryptoCore AES 功能测试") // JUnit 5 的 DisplayName，用于在测试报告中显示更友好的名称
public class CryptoCoreAesTest {
    // 定义不同长度的 AES 测试密钥字符串
    // 重要：请确保这些字符串的 UTF-8 字节长度符合 AES 要求 (16, 24, 32 字节)
    // 使用 ASCII 字符通常每个字符是 1 字节，所以简单地使用对应长度的 ASCII 字符串即可
    private static final String AES_KEY_128BIT_STR = "ThisIsA16ByteKey"; // 16 bytes
    private static final String AES_KEY_192BIT_STR = "ThisIsA24ByteKeyForAES"; // 24 bytes
    private static final String AES_KEY_256BIT_STR = "ThisIsA32ByteKeyForAES_256Bits_1"; // 32 bytes

    private static final String INVALID_AES_KEY_STR = "ShortKey"; // Invalid length, e.g., 8 bytes

    // 定义测试数据
    private static final String ORIGINAL_STRING = "Hello, World! This is a test string for AES encryption.";
    private static final byte[] ORIGINAL_BYTES = ORIGINAL_STRING.getBytes(StandardCharsets.UTF_8);

    @Test
    @DisplayName("AES 128位密钥加密解密字符串")
    void testAesEncryptDecryptString128BitKey() {
        // 1. 获取 AES 128 位密钥的 CryptoCore 实例
        CryptoCore aesCore = CryptoCore.getAesInstance(AES_KEY_128BIT_STR);
        // 2. 加密字符串
        byte[] encryptedData = aesCore.encrypt(ORIGINAL_BYTES);
        assertNotNull(encryptedData, "加密结果不应为 null");
        assertTrue(encryptedData.length > 0, "加密结果字节数组长度应大于 0");
        // 注意：对于 AES，加密后的长度与模式和填充有关，通常会大于等于原始数据长度

        // 3. 解密字节数组
        byte[] decryptedData = aesCore.decrypt(encryptedData);
        assertNotNull(decryptedData, "解密结果不应为 null");
        assertTrue(decryptedData.length > 0, "解密结果字节数组长度应大于 0");

        // 4. 验证解密后的数据是否与原始数据一致
        String decryptedString = new String(decryptedData, StandardCharsets.UTF_8);
        assertEquals(ORIGINAL_STRING, decryptedString, "解密后的字符串应与原始字符串一致");
        assertArrayEquals(ORIGINAL_BYTES, decryptedData, "解密后的字节数组应与原始字节数组一致");
    }

    @Test
    @DisplayName("AES 256位密钥加密解密字符串")
    void testAesEncryptDecryptString256BitKey() {
        // 1. 获取 AES 256 位密钥的 CryptoCore 实例
        CryptoCore aesCore = CryptoCore.getAesInstance(AES_KEY_256BIT_STR);
        // 2. 加密字符串
        byte[] encryptedData = aesCore.encrypt(ORIGINAL_BYTES);
        assertNotNull(encryptedData, "加密结果不应为 null");
        assertTrue(encryptedData.length > 0, "加密结果字节数组长度应大于 0");

        // 3. 解密字节数组
        byte[] decryptedData = aesCore.decrypt(encryptedData);
        assertNotNull(decryptedData, "解密结果不应为 null");
        assertTrue(decryptedData.length > 0, "解密结果字节数组长度应大于 0");

        // 4. 验证解密后的数据是否与原始数据一致
        String decryptedString = new String(decryptedData, StandardCharsets.UTF_8);
        assertEquals(ORIGINAL_STRING, decryptedString, "解密后的字符串应与原始字符串一致");
        assertArrayEquals(ORIGINAL_BYTES, decryptedData, "解密后的字节数组应与原始字节数组一致");
    }

    @Test
    @DisplayName("解密无效的 AES 数据")
    void testAesDecryptInvalidData() {
        // 1. 获取一个 AES CryptoCore 实例
        CryptoCore aesCore = CryptoCore.getAesInstance(AES_KEY_128BIT_STR);
        // 2. 尝试解密一个明显无效的字节数组（不是加密后的格式）
        byte[] invalidData = "This is not encrypted data".getBytes(StandardCharsets.UTF_8);
        // 3. 尝试解密，应抛出 ToolboxException
        assertThrows(ToolboxException.class, () -> {
            aesCore.decrypt(invalidData);
        }, "解密无效数据应抛出 ToolboxException");

        // 同样，如果想进一步验证底层异常，可以获取异常原因
    }
    @Test
    @DisplayName("加密 null 字节数组")
    void testAesEncryptNullBytes() {
        CryptoCore aesCore = CryptoCore.getAesInstance(AES_KEY_128BIT_STR);
        byte[] encryptedData = aesCore.encrypt((byte[]) null); // 注意强制转换为 byte[]
        assertNull(encryptedData, "加密 null 字节数组应返回 null");
    }

    @Test
    @DisplayName("加密 null 字符串")
    void testAesEncryptNullString() {
        CryptoCore aesCore = CryptoCore.getAesInstance(AES_KEY_128BIT_STR);
        String string = null;
        String encryptedData = aesCore.encrypt(string); // 注意强制转换为 String
        assertNull(encryptedData, "加密 null 字符串应返回 null");
    }

    @Test
    @DisplayName("解密 null 字节数组")
    void testAesDecryptNullBytes() {
        CryptoCore aesCore = CryptoCore.getAesInstance(AES_KEY_128BIT_STR);
        byte[] decryptedData = aesCore.decrypt((byte[]) null);
        assertNull(decryptedData, "解密 null 字节数组应返回 null");
    }

    @Test
    @DisplayName("使用无效长度密钥获取 AES 实例")
    void testAesGetAesInstanceInvalidKeyLength() {
        // 尝试使用长度不符合 AES 要求的密钥获取实例，应抛出 IllegalArgumentException 或 ToolboxException
        // 根据 CryptoCore.getInstance 中的逻辑，这里会先进行长度检查（如果保留了警告代码），
        // 并在 SecretKeySpec 构造时抛出 InvalidKeyException，最终包装为 ToolboxException。
        assertThrows(ToolboxException.class, () -> {
            CryptoCore.getAesInstance(INVALID_AES_KEY_STR);
        }, "使用无效长度密钥获取 AES 实例应抛出 ToolboxException");
        // 如果想验证底层异常是 InvalidKeyException
        // ToolboxException exception = assertThrows(ToolboxException.class, () -> {
        //      CryptoCore.getAesInstance(INVALID_AES_KEY_STR);
        // });
        // assertTrue(exception.getCause() instanceof InvalidKeyException, "底层异常应是 InvalidKeyException");
    }
}
