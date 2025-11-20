package com.github.hugh.crypto.components;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
/**
 * AesGcmCipher 类的单元测试。
 * 覆盖了 AES/GCM 模式下的各种加密解密场景。
 */
class AesGcmCipherTest {
    // --- 测试常量 ---
    private final String KEY_16_BYTES  = "ThisIsA16ByteKey";               // 128位
    private final String KEY_24_BYTES  = "ThisIsAValid24ByteAesKey";       // 192位
    private final String KEY_32_BYTES  = "ThisIsDefinitelyAValid32ByteKey!"; // 256位
    private final String invalidAesKey = "short";
    private final String originalText = "Hello, this is a secret message for AES/GCM!";
    private static final String PLAINTEXT = "This is a secret message for testing AES/GCM!";
    @Test
    void testAesGcmEncryptDecrypt_Success_128BitKey() {
        // === 准备数据 ===
        String key = KEY_16_BYTES; // "ThisIsA16ByteKey"
        String originalText = "Hello, this is a secret message for AES/GCM!";
        System.out.println("--- Java 端加密参数 ---");
        System.out.println("Key (String): " + key);
        System.out.println("Plaintext: " + originalText);
        // === 执行加密 ===
        AesGcmCipher gcmCrypto = AesGcmCipher.getInstance(key);
        AesGcmCipher.GcmEncryptedPacket packet = gcmCrypto.encrypt(originalText);
        // === 打印用于在线工具验证的结果 ===
        System.out.println("\n--- 用于在线工具验证 ---");
        System.out.println("IV (Base64): " + packet.getIvAsBase64());
        System.out.println("Ciphertext (Base64): " + packet.getCiphertextAsBase64());
        System.out.println("------------------------\n");
        // === Java 端本地验证 (依然保留) ===
        String decryptedText = gcmCrypto.decryptToString(packet.getCiphertextAsBase64(), packet.getIvAsBase64());
        assertEquals(originalText, decryptedText, "Java端解密结果与原文不匹配！");
        System.out.println("Java 端本地验证成功！");
    }

    @Nested
    @DisplayName("Instance Creation Tests")
    class InstanceCreationTests {

        @ParameterizedTest
        @ValueSource(strings = {KEY_16_BYTES, KEY_24_BYTES, KEY_32_BYTES})
        @DisplayName("应该成功创建实例当密钥长度有效时")
        void shouldCreateInstance_WithValidKeyLengths(String validKey) {
            assertDoesNotThrow(() -> AesGcmCipher.getInstance(validKey), "Valid key should not throw exception");
        }

        @Test
        @DisplayName("应该抛出异常当密钥为 null 或空")
        void shouldThrowException_WhenKeyIsNullOrEmpty() {
            // 测试 null 密钥
            AesGcmCipher.ToolboxException nullException = assertThrows(AesGcmCipher.ToolboxException.class,
                    () -> AesGcmCipher.getInstance(null));
            assertTrue(nullException.getCause() instanceof IllegalArgumentException);
            // 测试空密钥
            AesGcmCipher.ToolboxException emptyException = assertThrows(AesGcmCipher.ToolboxException.class,
                    () -> AesGcmCipher.getInstance(""));
            assertTrue(emptyException.getCause() instanceof IllegalArgumentException);
        }

        @ParameterizedTest
        @ValueSource(strings = {"12345", "123456789012345", "12345678901234567"})
        @DisplayName("应该抛出异常当密钥长度无效时")
        void shouldThrowException_WhenKeyLengthIsInvalid(String invalidKey) {
            assertThrows(AesGcmCipher.ToolboxException.class, () -> AesGcmCipher.getInstance(invalidKey),
                    "Invalid key length should throw exception");
        }
    }

    @Nested
    @DisplayName("End-to-End Encryption & Decryption Flow")
    class EndToEndTests {

        @ParameterizedTest
        @ValueSource(strings = {KEY_16_BYTES, KEY_24_BYTES, KEY_32_BYTES})
        @DisplayName("应该能正确加密和解密字符串 - 不同密钥长度")
        void shouldEncryptAndDecryptString_WithDifferentKeyLengths(String key) {
            AesGcmCipher crypto = AesGcmCipher.getInstance(key);
            AesGcmCipher.GcmEncryptedPacket packet = crypto.encrypt(PLAINTEXT);

            assertNotNull(packet);
            String decryptedText = crypto.decryptToString(packet.getCiphertextAsBase64(), packet.getIvAsBase64());
            assertEquals(PLAINTEXT, decryptedText);
        }

        @Test
        @DisplayName("应该能正确处理空字符串的加解密")
        void shouldHandleEmptyString() {
            AesGcmCipher crypto = AesGcmCipher.getInstance(KEY_16_BYTES);
            AesGcmCipher.GcmEncryptedPacket packet = crypto.encrypt("");
            assertNotNull(packet);
            String decryptedText = crypto.decryptToString(packet);
            assertEquals("", decryptedText);
        }

        @Test
        @DisplayName("应该能正确处理包含特殊字符的字符串")
        void shouldHandleSpecialCharacters() {
            AesGcmCipher crypto = AesGcmCipher.getInstance(KEY_32_BYTES);
            String specialText = "你好, world! @#$%^&*()_+|}{[]:?><,./;'`~";
            AesGcmCipher.GcmEncryptedPacket packet = crypto.encrypt(specialText);
            assertNotNull(packet);
            String decryptedText = crypto.decryptToString(packet);
            assertEquals(specialText, decryptedText);
        }
    }

    @Nested
    @DisplayName("Input/Output Format Compatibility Tests")
    class FormatCompatibilityTests {
        private final AesGcmCipher crypto = AesGcmCipher.getInstance(KEY_16_BYTES);
        private final byte[] plainBytes = PLAINTEXT.getBytes();

        @Test
        @DisplayName("应该能通过 [byte[]] -> [GcmEncryptedPacket] -> [byte[]] 流程")
        void testByteToPacketToByteFlow() {
            AesGcmCipher.GcmEncryptedPacket packet = crypto.encrypt(plainBytes);
            byte[] decryptedBytes = crypto.decryptToBytes(packet);
            assertArrayEquals(plainBytes, decryptedBytes);
        }

        @Test
        @DisplayName("应该能通过 [Combined Bytes] 格式进行解密")
        void testCombinedBytesFormat() {
            byte[] combined = crypto.encrypt(PLAINTEXT).toCombinedBytes();
            assertEquals(PLAINTEXT, crypto.decryptToString(combined));
            assertArrayEquals(plainBytes, crypto.decryptToBytes(combined));
        }

        @Test
        @DisplayName("应该能通过 [Combined Base64] 格式进行解密")
        void testCombinedBase64Format() {
            String combinedBase64 = crypto.encrypt(PLAINTEXT).toCombinedBase64();
            assertEquals(PLAINTEXT, crypto.decryptToString(combinedBase64));
            assertArrayEquals(plainBytes, crypto.decryptToBytes(combinedBase64));
        }

        @Test
        @DisplayName("应该能通过 [Separated Bytes] 格式进行解密")
        void testSeparatedBytesFormat() {
            AesGcmCipher.GcmEncryptedPacket packet = crypto.encrypt(plainBytes);
            byte[] decryptedBytes = crypto.decryptToBytes(packet.getCiphertext(), packet.getIv());
            assertArrayEquals(plainBytes, decryptedBytes);
        }
    }

    @Nested
    @DisplayName("Security and Robustness Tests")
    class SecurityAndRobustnessTests {
        private final AesGcmCipher crypto = AesGcmCipher.getInstance(KEY_16_BYTES);

        @Test
        @DisplayName("对相同明文的两次加密应产生不同的密文（因为 IV 不同）")
        void shouldProduceDifferentCiphertext_ForSamePlaintext() {
            AesGcmCipher.GcmEncryptedPacket packet1 = crypto.encrypt(PLAINTEXT);
            AesGcmCipher.GcmEncryptedPacket packet2 = crypto.encrypt(PLAINTEXT);

            assertNotNull(packet1);
            assertNotNull(packet2);

            // IV 应该是随机且不同的
            assertNotEquals(packet1.getIvAsBase64(), packet2.getIvAsBase64());
            // 因此，密文也应该是不同的
            assertNotEquals(packet1.getCiphertextAsBase64(), packet2.getCiphertextAsBase64());
        }

        @Test
        @DisplayName("当密文被篡改时解密应失败")
        void shouldFailDecryption_WhenCiphertextIsTampered() {
            AesGcmCipher.GcmEncryptedPacket packet = crypto.encrypt(PLAINTEXT);
            byte[] tamperedCiphertext = packet.getCiphertext();

            // 篡改最后一个字节
            tamperedCiphertext[tamperedCiphertext.length - 1] ^= (byte) 0x01;

            assertThrows(AesGcmCipher.ToolboxException.class,
                    () -> crypto.decryptToBytes(tamperedCiphertext, packet.getIv()),
                    "Decryption should fail for tampered data due to tag mismatch.");
        }

        @Test
        @DisplayName("使用错误的密钥解密应失败")
        void shouldFailDecryption_WhenKeyIsIncorrect() {
            AesGcmCipher crypto1 = AesGcmCipher.getInstance(KEY_16_BYTES);
            AesGcmCipher crypto2 = AesGcmCipher.getInstance("DIFFERENT_KEY_16"); // 不同的密钥

            AesGcmCipher.GcmEncryptedPacket packet = crypto1.encrypt(PLAINTEXT);

            assertThrows(AesGcmCipher.ToolboxException.class,
                    () -> crypto2.decryptToString(packet),
                    "Decryption should fail when using the wrong key.");
        }

        @Test
        @DisplayName("使用错误的 IV 解密应失败")
        void shouldFailDecryption_WhenIvIsIncorrect() {
            AesGcmCipher.GcmEncryptedPacket packet = crypto.encrypt(PLAINTEXT);
            byte[] wrongIv = packet.getIv().clone();
            wrongIv[0] ^= (byte) 0x01; // 篡改 IV

            assertThrows(AesGcmCipher.ToolboxException.class,
                    () -> crypto.decryptToBytes(packet.getCiphertext(), wrongIv));
        }

        @Test
        @DisplayName("当输入为 null 时应返回 null")
        void shouldReturnNull_ForNullInputs() {
            assertNull(crypto.encrypt((String) null));
            assertNull(crypto.encrypt((byte[]) null));
            assertNull(crypto.decryptToString((String) null, null));
            assertNull(crypto.decryptToBytes((byte[]) null));
        }

        @Test
        @DisplayName("当组合字节数组太短时应抛出异常")
        void shouldThrowException_WhenCombinedBytesAreTooShort() {
            byte[] shortBytes = new byte[12]; // IV 长度是 12，所以这个长度是无效的
            assertThrows(IllegalArgumentException.class, () -> crypto.decryptToString(shortBytes));
        }
    }

    @Nested
    @DisplayName("GcmEncryptedPacket Helper Method Tests")
    class PacketHelperTests {

        @Test
        @DisplayName("fromCombinedBytes 和 toCombinedBytes 应该可逆")
        void testFromAndToCombinedBytes() {
            AesGcmCipher crypto = AesGcmCipher.getInstance(KEY_16_BYTES);
            AesGcmCipher.GcmEncryptedPacket originalPacket = crypto.encrypt(PLAINTEXT);

            byte[] combined = originalPacket.toCombinedBytes();
            AesGcmCipher.GcmEncryptedPacket restoredPacket = AesGcmCipher.GcmEncryptedPacket.fromCombinedBytes(combined);

            assertArrayEquals(originalPacket.getIv(), restoredPacket.getIv());
            assertArrayEquals(originalPacket.getCiphertext(), restoredPacket.getCiphertext());
        }

        @Test
        @DisplayName("fromCombinedBase64 和 toCombinedBase64 应该可逆")
        void testFromAndToCombinedBase64() {
            AesGcmCipher crypto = AesGcmCipher.getInstance(KEY_16_BYTES);
            AesGcmCipher.GcmEncryptedPacket originalPacket = crypto.encrypt(PLAINTEXT);

            String combinedBase64 = originalPacket.toCombinedBase64();
            AesGcmCipher.GcmEncryptedPacket restoredPacket = AesGcmCipher.GcmEncryptedPacket.fromCombinedBase64(combinedBase64);

            assertEquals(originalPacket.getIvAsBase64(), restoredPacket.getIvAsBase64());
            assertEquals(originalPacket.getCiphertextAsBase64(), restoredPacket.getCiphertextAsBase64());
        }
    }
}
