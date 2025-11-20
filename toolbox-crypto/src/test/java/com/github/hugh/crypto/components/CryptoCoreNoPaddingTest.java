package com.github.hugh.crypto.components;

import com.github.hugh.exception.ToolboxException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.IllegalBlockSizeException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 针对 CryptoCore 类的 getAesInstanceWithNoPadding 方法的单元测试。
 */
class CryptoCoreNoPaddingTest {

    // 定义一个符合 AES 要求的 16 字节密钥
    private static final String AES_KEY_16_BYTES = "ThisIsA16ByteKey"; // 16个字符 -> 16字节

    @DisplayName("NoPadding 模式：当数据长度为 16 字节的倍数时，应能成功加密和解密")
    void testEncryptAndDecrypt_SuccessWithValidBlockSize() {
        // 准备：待加密数据，长度正好是 16 字节
        String originalText = "1234567890abcdef"; // 16 bytes
        byte[] originalData = originalText.getBytes(StandardCharsets.UTF_8);
        assertDoesNotThrow(() -> {
            // 操作：获取 NoPadding 实例并执行加解密
            CryptoCore cryptoCore = CryptoCore.getAesNoPadding(AES_KEY_16_BYTES);
            // 1. 加密
            byte[] encryptedData = cryptoCore.encrypt(originalData);
            assertNotNull(encryptedData);
            // 对于 NoPadding，加密后的长度应与原始数据长度相同
            assertEquals(originalData.length, encryptedData.length, "加密后的数据长度应与原始数据长度相同");
            // 2. 解密
            byte[] decryptedData = cryptoCore.decrypt(encryptedData);
            // 验证：解密后的数据与原始数据完全一致
            assertArrayEquals(originalData, decryptedData, "解密后的数据应与原始数据匹配");
            assertEquals(originalText, new String(decryptedData, StandardCharsets.UTF_8), "解密后的字符串应与原始字符串匹配");
        }, "使用有效块大小的数据进行加解密时不应抛出异常");
    }

    @Test
    @DisplayName("NoPadding 模式：当加密数据长度不是 16 字节的倍数时，应抛出 ToolboxException")
    void testEncrypt_ThrowsExceptionWithInvalidBlockSize() {
        // 准备：待加密数据，长度不是 16 的倍数
        String originalText = "Invalid length"; // 14 bytes
        byte[] originalData = originalText.getBytes(StandardCharsets.UTF_8);
        // 操作和验证：
        CryptoCore cryptoCore = CryptoCore.getAesNoPadding(AES_KEY_16_BYTES);
        // 1. 断言抛出的是我们自定义的 ToolboxException
        ToolboxException thrown = assertThrows(ToolboxException.class, () -> {
            cryptoCore.encrypt(originalData);
        }, "当数据块大小无效时，加密操作应抛出 ToolboxException");

        // 2. (非常推荐) 断言根本原因 (cause) 是 IllegalBlockSizeException
        // 这能确保我们是因为正确的原因而收到了正确的异常
        assertNotNull(thrown.getCause(), "ToolboxException 应该包含根本原因");
        assertInstanceOf(IllegalBlockSizeException.class, thrown.getCause(), "异常的根本原因应该是 IllegalBlockSizeException");

        // 3. (可选) 仍然可以验证异常消息
        assertTrue(thrown.getMessage().contains("Input length not multiple of 16 bytes"), "异常消息应指明输入长度问题");
    }

    @Test
    @DisplayName("NoPadding 模式：当解密数据长度不是 16 字节的倍数时，应抛出 ToolboxException")
    void testDecrypt_ThrowsExceptionWithInvalidBlockSize() {
        // 准备：一个长度无效的字节数组
        byte[] invalidEncryptedData = "Invalid length".getBytes(StandardCharsets.UTF_8); // 14 bytes

        // 操作和验证：
        CryptoCore cryptoCore = CryptoCore.getAesNoPadding(AES_KEY_16_BYTES);

        // 1. 断言抛出的是 ToolboxException
        ToolboxException thrown = assertThrows(ToolboxException.class, () -> {
            cryptoCore.decrypt(invalidEncryptedData);
        }, "当数据块大小无效时，解密操作应抛出 ToolboxException");

        // 2. 断言根本原因
        assertNotNull(thrown.getCause(), "ToolboxException 应该包含根本原因");
        assertInstanceOf(IllegalBlockSizeException.class, thrown.getCause(), "异常的根本原因应该是 IllegalBlockSizeException");
    }
}
