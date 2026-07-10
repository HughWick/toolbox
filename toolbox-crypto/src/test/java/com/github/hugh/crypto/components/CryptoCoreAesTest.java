package com.github.hugh.crypto.components;

import com.github.hugh.exception.ToolboxException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CryptoCore AES 功能测试") // JUnit 5 的 DisplayName，用于在测试报告中显示更友好的名称
class CryptoCoreAesTest {
    // 定义不同长度的 AES 测试密钥字符串
    // 重要：请确保这些字符串的 UTF-8 字节长度符合 AES 要求 (16, 24, 32 字节)
    // 使用 ASCII 字符通常每个字符是 1 字节，所以简单地使用对应长度的 ASCII 字符串即可
    private static final String DES_KEY_64BIT_STR = "84729105"; // 16 bytes
    private static final String AES_KEY_128BIT_STR = "ThisIsA16ByteKey"; // 16 bytes
    private static final String AES_KEY_192BIT_STR = "ThisIsA24ByteKeyForAES"; // 24 bytes
    private static final String AES_KEY_256BIT_STR = "ThisIsA32ByteKeyForAES_256Bits_1"; // 32 bytes

    private static final String INVALID_AES_KEY_STR = "ShortKey"; // Invalid length, e.g., 8 bytes
    // 如果您还没有定义这些常量，请在测试类中补充：
//    private static final String AES_KEY_128BIT_STR = "1234567890123456"; // 16字节 AES 密钥
    private static final String SM4_KEY_128BIT_STR = "6543210987654321"; // 16字节 SM4 密钥

    // 16字节 IV (适用于 AES-CBC, SM4-CBC)
    private static final byte[] IV_16_BYTES = "abcdefghijklmnop".getBytes(StandardCharsets.UTF_8);
    // 12字节 IV (适用于 AES-GCM 推荐长度)
    private static final byte[] IV_12_BYTES = "abcdefghijkl".getBytes(StandardCharsets.UTF_8);

    // 定义测试数据
    private static final String ORIGINAL_STRING = "Hello, World! This is a test string for AES encryption.";
    private static final byte[] ORIGINAL_BYTES = ORIGINAL_STRING.getBytes(StandardCharsets.UTF_8);

    @DisplayName("Java 端加密参数 (用于在线工具验证)")
    @Test
    void testForOnlineToolVerification_AesEcbNoPadding() {
        final String plaintext = "ThisIs32ByteTestMessageForECB!";
        System.out.println("--- Java 端加密参数 (用于在线工具验证) ---");
        System.out.println("加密模式 (Mode): ECB");
        System.out.println("填充方式 (Padding): PKCS5Padding");
        System.out.println("密钥 (Key - String): " + AES_KEY_128BIT_STR);
        System.out.println("明文 (Plaintext - String): " + plaintext);
        System.out.println("-------------------------------------------------");
        // --- 2. 使用 Java 进行加密 ---
        // 注意：这里需要调用我们新增的、能指定完整 transformation 的方法
        CryptoCore aesEcbCrypto = CryptoCore.getAesInstance(AES_KEY_128BIT_STR);
        // 加密并获取 Base64 编码的密文
        System.out.println(">>> 准备加密的明文实际字节长度: " + plaintext.getBytes(StandardCharsets.UTF_8).length);
        String ciphertextBase64 = aesEcbCrypto.encrypt(plaintext);
        System.out.println("--- Java 端生成的密文 (复制到在线工具进行比对) ---");
        System.out.println("Ciphertext (Base64): " + ciphertextBase64);
        System.out.println("-------------------------------------------------");
        // --- 3. (可选) 在 Java 端进行本地验证 ---
        String decryptedText = aesEcbCrypto.decrypt(ciphertextBase64);
        assertEquals(plaintext, decryptedText, "Java端本地解密失败！");
        System.out.println("Java 端本地验证成功: 解密结果与原文一致。");
        // --- 4. (可选) 预期的在线工具结果 (硬编码) ---
        // 您可以先运行一次，得到 ciphertextBase64 的值，然后填入这里，
        // 这样测试用例就可以自动化地检查结果是否稳定。
        // 例如，对于上面的 key 和 plaintext，加密结果是固定的。
        String expectedCiphertextBase64 = "LOvOLhxOYnRnHV+gwEZwKKrC0CZxsazgaEMPfL5L6gk=";
        assertEquals(expectedCiphertextBase64, ciphertextBase64, "加密结果与预期的固定值不符！");
    }

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

    @Test
    @DisplayName("AES 128位密钥加密字节数组并返回Base64字符串")
    void testAesEncryptBytesToBase64AndDecrypt() {
        // --- 1. 获取 AES 128 位密钥的 CryptoCore 实例 ---
        // (与上一个测试用例相同，假设 ORIGINAL_BYTES 已定义)
        CryptoCore aesCore = CryptoCore.getAesInstance(AES_KEY_128BIT_STR);
        System.out.println("\n--- 开始测试 encryptToBase64 ---");
        System.out.println("原始数据字节长度: " + ORIGINAL_BYTES.length);
        // --- 2. 调用新方法，加密字节数组并获取 Base64 字符串 ---
        String encryptedBase64 = aesCore.encryptToBase64(ORIGINAL_BYTES);
        // 断言：验证返回的 Base64 字符串
        assertNotNull(encryptedBase64, "加密后的 Base64 字符串不应为 null");
        assertFalse(encryptedBase64.isEmpty(), "加密后的 Base64 字符串不应为空");
        System.out.println("加密后的 Base64 字符串: " + encryptedBase64);
        // 我们可以通过尝试解码来简单验证它是一个有效的 Base64 字符串
        assertDoesNotThrow(() -> {
            Base64.getDecoder().decode(encryptedBase64);
        }, "返回的字符串应为有效的 Base64 格式");
        // --- 3. 模拟接收方：解码 Base64 并解密 ---
        // 3a. 将 Base64 字符串解码回原始的加密后字节数组
        byte[] encryptedDataFromBase64 = Base64.getDecoder().decode(encryptedBase64);
        System.out.println("Base64 解码后的密文字节长度: " + encryptedDataFromBase64.length);
        // 3b. 调用 decrypt 方法解密
        byte[] decryptedData = aesCore.decrypt(encryptedDataFromBase64);
        // 断言：验证解密后的数据
        assertNotNull(decryptedData, "解密结果不应为 null");
        assertTrue(decryptedData.length > 0, "解密结果字节数组长度应大于 0");
        // --- 4. 最终验证：解密后的数据是否与原始数据完全一致 ---
        String decryptedString = new String(decryptedData, StandardCharsets.UTF_8);
        // 断言字符串内容一致
        assertEquals(ORIGINAL_STRING, decryptedString, "解密后的字符串应与原始字符串一致");
        // 【最重要】断言原始字节数组完全一致
        assertArrayEquals(ORIGINAL_BYTES, decryptedData, "解密后的字节数组应与原始字节数组一致");
        System.out.println("测试通过！encryptToBase64 方法工作正常，整个流程闭环验证成功。");
    }

    /**
     * 测试使用 AES 128 位密钥加密字节数组到Base64，再从Base64解密的完整流程。
     */
    @Test
    @DisplayName("AES 128位密钥 加密字节到Base64 -> 从Base64解密 完整闭环")
    void testAesEncryptBytesToBase64AndDecryptFromBase64() {
        // --- 1. 获取实例和原始数据 ---
        CryptoCore aesCore = CryptoCore.getAesInstance(AES_KEY_128BIT_STR);
        System.out.println("\n--- 开始测试 encryptToBase64 -> decryptFromBase64 完整流程 ---");
        // --- 2. 调用加密方法，得到 Base64 字符串 ---
        String encryptedBase64 = aesCore.encryptToBase64(ORIGINAL_BYTES);
        System.out.println("加密后的 Base64 字符串: " + encryptedBase64);
        assertNotNull(encryptedBase64, "加密后的 Base64 字符串不应为 null");
        // --- 3. 【修改点】直接调用新的解密方法 ---
        // 无需再手动进行 Base64 解码，代码变得更简洁！
        byte[] decryptedData = aesCore.decryptFromBase64(encryptedBase64);
        System.out.println("通过 decryptFromBase64 直接解密成功。");
        // 断言：验证解密后的数据
        assertNotNull(decryptedData, "解密结果不应为 null");
        // --- 4. 最终验证：解密后的数据是否与原始数据完全一致 ---
        String decryptedString = new String(decryptedData, StandardCharsets.UTF_8);
        assertEquals(ORIGINAL_STRING, decryptedString, "解密后的字符串应与原始字符串一致");
        assertArrayEquals(ORIGINAL_BYTES, decryptedData, "解密后的字节数组应与原始字节数组一致");
        System.out.println("测试通过！encryptToBase64 和 decryptFromBase64 方法配对工作正常！");
    }

    /**
     * 【新增】测试无效 Base64 输入的边界情况
     */
    @Test
    @DisplayName("decryptFromBase64 应能处理无效的 Base64 输入")
    void testDecryptFromInvalidBase64() {
        CryptoCore aesCore = CryptoCore.getAesInstance(AES_KEY_128BIT_STR);
        String invalidBase64String = "这是一个无效的Base64字符串!!!";
        // 断言：当调用 decryptFromBase64 并传入无效字符串时，应该抛出我们定义的 ToolboxException
        ToolboxException thrown = assertThrows(
                ToolboxException.class,
                () -> aesCore.decryptFromBase64(invalidBase64String),
                "对于无效的 Base64 输入，应该抛出 ToolboxException"
        );
        // （可选）进一步断言异常消息中是否包含了我们期望的信息
        assertTrue(thrown.getMessage().contains("Base64 解码失败"), "异常消息应指明是 Base64 解码问题");
        System.out.println("\n测试通过！decryptFromBase64 对无效输入的处理符合预期。");
    }

    /**
     * 【新增的测试用例】
     * 测试使用 AES 128 位密钥，加密一个字符串得到密文字节数组，
     * 然后使用 decryptToString(byte[]) 方法直接解密回原始字符串。
     */
    @Test
    @DisplayName("AES 128位密钥 密文字节数组 -> 明文字符串 解密测试")
    void testAesDecryptBytesToString() {
        // --- 1. 获取实例和原始数据 ---
        CryptoCore aesCore = CryptoCore.getAesInstance(AES_KEY_128BIT_STR);
        String originalComplexString = "测试场景：密文 byte[] -> 明文 String";
        byte[] originalBytes = originalComplexString.getBytes(StandardCharsets.UTF_8);
        System.out.println("\n--- 开始测试 decryptToString(byte[]) ---");
        System.out.println("原始字符串: " + originalComplexString);
        // --- 2. 获取加密后的字节数组 (密文) ---
        // 我们需要先得到一个加密后的 byte[] 作为新方法的输入
        byte[] encryptedData = aesCore.encrypt(originalBytes);
        assertNotNull(encryptedData, "加密后的字节数组不应为 null");
        System.out.println("加密后的密文字节长度: " + encryptedData.length);
        // --- 3. 【核心】调用我们新的解密方法 ---
        // 输入是 byte[] (密文)，输出是 String (明文)
        String decryptedString = aesCore.decryptToString(encryptedData);
        assertNotNull(decryptedString, "解密后的字符串不应为 null");
        System.out.println("调用 decryptToString(byte[]) 解密后的字符串: " + decryptedString);
        // --- 4. 最终验证：解密后的字符串是否与原始字符串完全一致 ---
        assertEquals(originalComplexString, decryptedString, "解密后的字符串应与原始字符串完全一致");
        System.out.println("测试通过！decryptToString(byte[]) 方法工作正常！");
    }

    @Test
    @DisplayName("AES-CBC 128位密钥带IV 密文字节数组 -> 明文字符串 解密测试")
    void testAesCbcDecryptBytesToString() {
        // --- 1. 获取实例和原始数据 ---
        CryptoCore aesCbcCore = CryptoCore.getAesCbcInstance(AES_KEY_128BIT_STR, IV_16_BYTES);
        String originalComplexString = "测试场景：AES-CBC 密文 byte[] -> 明文 String（包含特殊字符！@#￥%）";
        byte[] originalBytes = originalComplexString.getBytes(StandardCharsets.UTF_8);
        System.out.println("\n--- 开始测试 AES-CBC decryptToString(byte[]) ---");
        System.out.println("原始字符串: " + originalComplexString);

        // --- 2. 获取加密后的字节数组 (密文) ---
        byte[] encryptedData = aesCbcCore.encrypt(originalBytes);
        assertNotNull(encryptedData, "加密后的字节数组不应为 null");
        System.out.println("加密后的密文字节长度: " + encryptedData.length);
        System.out.println("加密后的密文字: " + aesCbcCore.encryptToBase64(originalBytes));

        // --- 3. 【核心】调用解密方法 ---
        String decryptedString = aesCbcCore.decryptToString(encryptedData);
        assertNotNull(decryptedString, "解密后的字符串不应为 null");
        System.out.println("解密后的字符串: " + decryptedString);

        // --- 4. 最终验证 ---
        assertEquals(originalComplexString, decryptedString, "解密后的字符串应与原始字符串完全一致");
        System.out.println("测试通过！AES-CBC decryptToString(byte[]) 方法工作正常！");
    }

    @Test
    @DisplayName("AES-GCM 128位密钥带12字节IV 密文字节数组 -> 明文字符串 解密测试")
    void testAesGcmDecryptBytesToString() {
        // --- 1. 获取实例和原始数据 ---
        // GCM 模式推荐使用 12 字节的 IV (Nonce)
        CryptoCore aesGcmCore = CryptoCore.getAesGcmInstance(AES_KEY_128BIT_STR, IV_12_BYTES);
        String originalComplexString = "测试场景：AES-GCM AEAD模式 密文 byte[] -> 明文 String";
        byte[] originalBytes = originalComplexString.getBytes(StandardCharsets.UTF_8);
        System.out.println("\n--- 开始测试 AES-GCM decryptToString(byte[]) ---");
        System.out.println("原始字符串: " + originalComplexString);

        // --- 2. 获取加密后的字节数组 (密文) ---
        // 注意：GCM模式加密后的密文长度会比明文多出 16字节（认证标签 Auth Tag）
        byte[] encryptedData = aesGcmCore.encrypt(originalBytes);
        assertNotNull(encryptedData, "加密后的字节数组不应为 null");
        System.out.println("加密后的密文字节长度: " + encryptedData.length);

        // --- 3. 【核心】调用解密方法 ---
        String decryptedString = aesGcmCore.decryptToString(encryptedData);
        assertNotNull(decryptedString, "解密后的字符串不应为 null");
        System.out.println("解密后的字符串: " + decryptedString);

        // --- 4. 最终验证 ---
        assertEquals(originalComplexString, decryptedString, "解密后的字符串应与原始字符串完全一致");
        System.out.println("测试通过！AES-GCM decryptToString(byte[]) 方法工作正常！");
    }

    @Test
    @DisplayName("SM4-ECB (国密) 128位密钥 密文字节数组 -> 明文字符串 解密测试")
    void testSm4EcbDecryptBytesToString() {
        // --- 1. 获取实例和原始数据 ---
        // SM4 使用 128 bit (16字节) 密钥
        CryptoCore sm4Core = CryptoCore.getSm4Instance(SM4_KEY_128BIT_STR);
        String originalComplexString = "测试场景：SM4-ECB 国密算法 密文 byte[] -> 明文 String";
        byte[] originalBytes = originalComplexString.getBytes(StandardCharsets.UTF_8);
        System.out.println("\n--- 开始测试 SM4-ECB decryptToString(byte[]) ---");
        System.out.println("原始字符串: " + originalComplexString);

        // --- 2. 获取加密后的字节数组 (密文) ---
        byte[] encryptedData = sm4Core.encrypt(originalBytes);
        assertNotNull(encryptedData, "加密后的字节数组不应为 null");
        System.out.println("加密后的密文字节长度: " + encryptedData.length);
        System.out.println("加密后的密文字: " + sm4Core.encryptToBase64(originalBytes));
        // --- 3. 【核心】调用解密方法 ---
        String decryptedString = sm4Core.decryptToString(encryptedData);
        assertNotNull(decryptedString, "解密后的字符串不应为 null");
        System.out.println("解密后的字符串: " + decryptedString);

        // --- 4. 最终验证 ---
        assertEquals(originalComplexString, decryptedString, "解密后的字符串应与原始字符串完全一致");
        System.out.println("测试通过！SM4-ECB decryptToString(byte[]) 方法工作正常！");
    }

    @Test
    @DisplayName("SM4-CBC (国密) 128位密钥带IV 密文字节数组 -> 明文字符串 解密测试")
    void testSm4CbcDecryptBytesToString() {
        // --- 1. 获取实例和原始数据 ---
        // SM4-CBC 需要 16 字节的 IV
        CryptoCore sm4CbcCore = CryptoCore.getSm4CbcInstance(SM4_KEY_128BIT_STR, IV_16_BYTES);
        String originalComplexString = "测试场景：SM4-CBC 增强安全国密 密文 byte[] -> 明文 String";
        byte[] originalBytes = originalComplexString.getBytes(StandardCharsets.UTF_8);
        System.out.println("\n--- 开始测试 SM4-CBC decryptToString(byte[]) ---");
        System.out.println("原始字符串: " + originalComplexString);

        // --- 2. 获取加密后的字节数组 (密文) ---
        byte[] encryptedData = sm4CbcCore.encrypt(originalBytes);
        assertNotNull(encryptedData, "加密后的字节数组不应为 null");
        System.out.println("加密后的密文字节长度: " + encryptedData.length);
        System.out.println("加密后的密文字: " + sm4CbcCore.encryptToBase64(originalBytes));
        // --- 3. 【核心】调用解密方法 ---
        String decryptedString = sm4CbcCore.decryptToString(encryptedData);
        assertNotNull(decryptedString, "解密后的字符串不应为 null");
        System.out.println("解密后的字符串: " + decryptedString);

        // --- 4. 最终验证 ---
        assertEquals(originalComplexString, decryptedString, "解密后的字符串应与原始字符串完全一致");
        System.out.println("测试通过！SM4-CBC decryptToString(byte[]) 方法工作正常！");
    }

    @Test
    @DisplayName("AES-ECB 128位原生字节数组密钥 密文 byte[] -> 明文 String 解密测试")
    void testAesByteKeyDecryptBytesToString() {
        // --- 1. 获取实例和原始数据 ---
        byte[] keyBytes = AES_KEY_128BIT_STR.getBytes(StandardCharsets.UTF_8);
        CryptoCore aesCore = CryptoCore.getAesInstance(keyBytes);

        String originalString = "测试场景：AES-ECB 默认模式 (原生 byte[] 密钥)";
        byte[] originalBytes = originalString.getBytes(StandardCharsets.UTF_8);
        System.out.println("\n--- 开始测试 AES-ECB (原生Byte密钥) decryptToString(byte[]) ---");
        System.out.println("原始字符串: " + originalString);

        // --- 2. 获取加密后的密文 ---
        byte[] encryptedData = aesCore.encrypt(originalBytes);
        assertNotNull(encryptedData, "加密后的字节数组不应为 null");
        System.out.println("加密后的密文字节长度: " + encryptedData.length);
        System.out.println("加密后的密文 (Base64展示): " + aesCore.encryptToBase64(originalBytes));

        // --- 3. 调用解密方法 ---
        String decryptedString = aesCore.decryptToString(encryptedData);
        assertNotNull(decryptedString, "解密后的字符串不应为 null");
        System.out.println("解密后的字符串: " + decryptedString);

        // --- 4. 最终验证 ---
        assertEquals(originalString, decryptedString, "解密后的字符串应与原始字符串完全一致");
        System.out.println("测试通过！AES-ECB 原生 byte[] 密钥解密工作正常！");
    }

    @Test
    @DisplayName("AES-ECB NoPadding 原生字节数组密钥 密文 byte[] -> 明文 String 解密测试")
    void testAesNoPaddingByteKeyDecryptBytesToString() {
        // --- 1. 获取实例和原始数据 ---
        byte[] keyBytes = AES_KEY_128BIT_STR.getBytes(StandardCharsets.UTF_8);
        CryptoCore aesNoPaddingCore = CryptoCore.getAesNoPadding(keyBytes);

        // 注意：NoPadding 模式要求明文数据长度必须是 16 的整数倍。
        // 这里使用刚好 16 字节长度的 ASCII 字符串进行测试。
        String originalString = "Exact16BytesData";
        byte[] originalBytes = originalString.getBytes(StandardCharsets.UTF_8);
        assertEquals(16, originalBytes.length, "NoPadding 测试数据长度必须是 16 字节");

        System.out.println("\n--- 开始测试 AES-ECB NoPadding (原生Byte密钥) decryptToString(byte[]) ---");
        System.out.println("原始字符串: " + originalString);

        // --- 2. 获取加密后的密文 ---
        byte[] encryptedData = aesNoPaddingCore.encrypt(originalBytes);
        assertNotNull(encryptedData, "加密后的字节数组不应为 null");
        System.out.println("加密后的密文字节长度: " + encryptedData.length);
        System.out.println("加密后的密文 (Base64展示): " + aesNoPaddingCore.encryptToBase64(originalBytes));

        // --- 3. 调用解密方法 ---
        String decryptedString = aesNoPaddingCore.decryptToString(encryptedData);
        assertNotNull(decryptedString, "解密后的字符串不应为 null");
        System.out.println("解密后的字符串: " + decryptedString);

        // --- 4. 最终验证 ---
        assertEquals(originalString, decryptedString, "解密后的字符串应与原始字符串完全一致");
        System.out.println("测试通过！AES-ECB NoPadding 原生 byte[] 密钥解密工作正常！");
    }

    @Test
    @DisplayName("DES 64位原生字节数组密钥 密文 byte[] -> 明文 String 解密测试")
    void testDesByteKeyDecryptBytesToString() {
        // --- 1. 获取实例和原始数据 ---
        // 假设 DES_KEY_64BIT_STR 长度为 8 字节
        byte[] keyBytes = DES_KEY_64BIT_STR.getBytes(StandardCharsets.UTF_8);
        CryptoCore desCore = CryptoCore.getDesInstance(keyBytes);

        String originalString = "测试场景：DES 传统加密 (原生 byte[] 密钥)";
        byte[] originalBytes = originalString.getBytes(StandardCharsets.UTF_8);
        System.out.println("\n--- 开始测试 DES (原生Byte密钥) decryptToString(byte[]) ---");
        System.out.println("原始字符串: " + originalString);

        // --- 2. 获取加密后的密文 ---
        byte[] encryptedData = desCore.encrypt(originalBytes);
        assertNotNull(encryptedData, "加密后的字节数组不应为 null");
        System.out.println("加密后的密文字节长度: " + encryptedData.length);
        System.out.println("加密后的密文 (Base64展示): " + desCore.encryptToBase64(originalBytes));

        // --- 3. 调用解密方法 ---
        String decryptedString = desCore.decryptToString(encryptedData);
        assertNotNull(decryptedString, "解密后的字符串不应为 null");
        System.out.println("解密后的字符串: " + decryptedString);

        // --- 4. 最终验证 ---
        assertEquals(originalString, decryptedString, "解密后的字符串应与原始字符串完全一致");
        System.out.println("测试通过！DES 原生 byte[] 密钥解密工作正常！");
    }

    @Test
    @DisplayName("AES-CBC 128位原生字节数组密钥带IV 密文 byte[] -> 明文 String 解密测试")
    void testAesCbcByteKeyDecryptBytesToString() {
        // --- 1. 获取实例和原始数据 ---
        byte[] keyBytes = AES_KEY_128BIT_STR.getBytes(StandardCharsets.UTF_8);
        CryptoCore aesCbcCore = CryptoCore.getAesCbcInstance(keyBytes, IV_16_BYTES);

        String originalString = "测试场景：AES-CBC 高级加密 (原生 byte[] 密钥)";
        byte[] originalBytes = originalString.getBytes(StandardCharsets.UTF_8);
        System.out.println("\n--- 开始测试 AES-CBC (原生Byte密钥) decryptToString(byte[]) ---");
        System.out.println("原始字符串: " + originalString);

        // --- 2. 获取加密后的密文 ---
        byte[] encryptedData = aesCbcCore.encrypt(originalBytes);
        assertNotNull(encryptedData, "加密后的字节数组不应为 null");
        System.out.println("加密后的密文字节长度: " + encryptedData.length);
        System.out.println("加密后的密文 (Base64展示): " + aesCbcCore.encryptToBase64(originalBytes));

        // --- 3. 调用解密方法 ---
        String decryptedString = aesCbcCore.decryptToString(encryptedData);
        assertNotNull(decryptedString, "解密后的字符串不应为 null");
        System.out.println("解密后的字符串: " + decryptedString);

        // --- 4. 最终验证 ---
        assertEquals(originalString, decryptedString, "解密后的字符串应与原始字符串完全一致");
        System.out.println("测试通过！AES-CBC 原生 byte[] 密钥解密工作正常！");
    }

    @Test
    @DisplayName("AES-GCM 原生字节数组密钥带IV(Nonce) 密文 byte[] -> 明文 String 解密测试")
    void testAesGcmByteKeyDecryptBytesToString() {
        // --- 1. 获取实例和原始数据 ---
        byte[] keyBytes = AES_KEY_128BIT_STR.getBytes(StandardCharsets.UTF_8);
        // GCM 模式推荐使用 12 字节的 IV(Nonce)，假设你有 IV_12_BYTES 常量
        CryptoCore aesGcmCore = CryptoCore.getAesGcmInstance(keyBytes, IV_12_BYTES);

        String originalString = "测试场景：AES-GCM 认证加密 (原生 byte[] 密钥)";
        byte[] originalBytes = originalString.getBytes(StandardCharsets.UTF_8);
        System.out.println("\n--- 开始测试 AES-GCM (原生Byte密钥) decryptToString(byte[]) ---");
        System.out.println("原始字符串: " + originalString);

        // --- 2. 获取加密后的密文 ---
        byte[] encryptedData = aesGcmCore.encrypt(originalBytes);
        assertNotNull(encryptedData, "加密后的字节数组不应为 null");
        System.out.println("加密后的密文字节长度(含认证标签): " + encryptedData.length);
        System.out.println("加密后的密文 (Base64展示): " + Base64.getEncoder().encodeToString(originalBytes));

        // --- 3. 调用解密方法 ---
        String decryptedString = aesGcmCore.decryptToString(encryptedData);
        assertNotNull(decryptedString, "解密后的字符串不应为 null");
        System.out.println("解密后的字符串: " + decryptedString);

        // --- 4. 最终验证 ---
        assertEquals(originalString, decryptedString, "解密后的字符串应与原始字符串完全一致");
        System.out.println("测试通过！AES-GCM 原生 byte[] 密钥解密工作正常！");
    }

    @Test
    @DisplayName("SM4-ECB (国密) 原生字节数组密钥 密文 byte[] -> 明文 String 解密测试")
    void testSm4ByteKeyDecryptBytesToString() {
        // --- 1. 获取实例和原始数据 ---
        byte[] keyBytes = SM4_KEY_128BIT_STR.getBytes(StandardCharsets.UTF_8);
        CryptoCore sm4Core = CryptoCore.getSm4Instance(keyBytes);

        String originalString = "测试场景：SM4-ECB 基础国密 (原生 byte[] 密钥)";
        byte[] originalBytes = originalString.getBytes(StandardCharsets.UTF_8);
        System.out.println("\n--- 开始测试 SM4-ECB (原生Byte密钥) decryptToString(byte[]) ---");
        System.out.println("原始字符串: " + originalString);

        // --- 2. 获取加密后的密文 ---
        byte[] encryptedData = sm4Core.encrypt(originalBytes);
        assertNotNull(encryptedData, "加密后的字节数组不应为 null");
        System.out.println("加密后的密文字节长度: " + encryptedData.length);
        System.out.println("加密后的密文 (Base64展示): " + sm4Core.encryptToBase64(originalBytes));

        // --- 3. 调用解密方法 ---
        String decryptedString = sm4Core.decryptToString(encryptedData);
        assertNotNull(decryptedString, "解密后的字符串不应为 null");
        System.out.println("解密后的字符串: " + decryptedString);

        // --- 4. 最终验证 ---
        assertEquals(originalString, decryptedString, "解密后的字符串应与原始字符串完全一致");
        System.out.println("测试通过！SM4-ECB 原生 byte[] 密钥解密工作正常！");
    }
    @Test
    @DisplayName("SM4-CBC 原生字节数组密钥带IV 密文 byte[] -> 明文 String 解密测试")
    void testSm4CbcByteKeyDecryptBytesToString() {
        // --- 1. 获取实例和原始数据 ---
        // SM4 密钥长度固定为 16 字节 (128位)，假设你有对应的 SM4_KEY_128BIT_STR 常量
        byte[] keyBytes = SM4_KEY_128BIT_STR.getBytes(StandardCharsets.UTF_8);
        // SM4-CBC 模式必须使用 16 字节的 IV，假设你有 IV_16_BYTES 常量
        CryptoCore sm4CbcCore = CryptoCore.getSm4CbcInstance(keyBytes, IV_16_BYTES);

        String originalString = "测试场景：SM4-CBC 加密解密 (原生 byte[] 密钥)";
        byte[] originalBytes = originalString.getBytes(StandardCharsets.UTF_8);
        System.out.println("\n--- 开始测试 SM4-CBC (原生Byte密钥) decryptToString(byte[]) ---");
        System.out.println("原始字符串: " + originalString);

        // --- 2. 获取加密后的密文 ---
        byte[] encryptedData = sm4CbcCore.encrypt(originalBytes);
        assertNotNull(encryptedData, "加密后的字节数组不应为 null");
        System.out.println("加密后的密文字节长度: " + encryptedData.length);
        // 💡 顺带修正了原 AES 测试用例中的一个小打印 Bug：这里应该传入 encryptedData 而不是 originalBytes
        System.out.println("加密后的密文 (Base64展示): " + Base64.getEncoder().encodeToString(encryptedData));

        // --- 3. 调用解密方法 ---
        String decryptedString = sm4CbcCore.decryptToString(encryptedData);
        assertNotNull(decryptedString, "解密后的字符串不应为 null");
        System.out.println("解密后的字符串: " + decryptedString);

        // --- 4. 最终验证 ---
        assertEquals(originalString, decryptedString, "解密后的字符串应与原始字符串完全一致");
        System.out.println("测试通过！SM4-CBC 原生 byte[] 密钥解密工作正常！");
    }
}
