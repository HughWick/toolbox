package com.github.hugh.crypto.components;

import com.github.hugh.exception.ToolboxException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * 通用的加密解密核心工具类。
 * <p>
 * 该类提供 DES 和 AES 算法的加密和解密功能。
 * 它采用工厂模式，通过静态方法 {@link #getInstance(String, String)}、{@link #getAesInstance(String)}
 * 或 {@link #getDesInstance(String)} 创建不同配置（特定密钥和算法）的实例。
 * 每个实例都包含了用于加解密的 {@link Cipher} 对象，并绑定了创建时指定的密钥和算法。
 * </p>
 *
 * <p>
 * **重要安全提示：**
 * </p>
 * <ul>
 * <li>直接使用字符串的 {@code getBytes()} 作为密钥通常不安全，且对于 AES，字节长度可能不匹配算法要求。
 * 生产环境应使用 {@link javax.crypto.KeyGenerator} 生成安全密钥，或使用 {@link javax.crypto.SecretKeyFactory}
 * 从密码派生密钥 (如 PBKDF2)。</li>
 * <li>加密模式 (Mode) 和填充方式 (Padding) 对安全性至关重要。本示例中 {@code Cipher.getInstance(algorithmName)}
 * 使用了 JVM 默认设置，可能不是最安全的选择 (如 ECB 模式)。生产环境强烈建议明确指定模式和填充，
 * 并在使用如 CBC 模式时妥善处理 IV (Initialization Vector)。</li>
 * </ul>
 *
 * @version 3.0.3
 */
public class CryptoCore {

    // 私有构造方法，防止外部直接实例化
    private CryptoCore() {
    }

    // 类的实例不再是单例，每个实例持有自己的 Cipher 对象
    private Cipher encryptCipher;
    private Cipher decryptCipher;

    // 定义支持的算法常量
    public static final String ALGORITHM_DES = "DES";
    public static final String ALGORITHM_AES = "AES";// 代表默认使用 "AES/ECB/PKCS5Padding"
    public static final String ALGORITHM_AES_ECB_PKCS5PADDING = "AES/ECB/PKCS5Padding";
    /**
     * AES 算法，使用 ECB 模式，不进行填充。
     * <b>注意：</b>使用此模式时，待加密的数据字节数组长度必须是 16 的整数倍。
     */
    public static final String ALGORITHM_AES_ECB_NO_PADDING = "AES/ECB/NoPadding";
    /**
     * 获取 CryptoCore 实例，支持 DES 和 AES 加密/解密。
     * <p>
     * 该方法是工厂方法，每次调用都会返回一个新的 CryptoCore 实例，
     * 该实例已使用指定的密钥和算法初始化好加密和解密所需的 Cipher 对象。
     * 请注意：同一个 CryptoCore 实例只能用于创建时指定的密钥和算法。
     * </p>
     *
     * @param key       密钥字符串。
     *                  对于 DES，密钥通常是 8 字节。
     *                  对于 AES，由 {@code key.getBytes()} 得到的字节数组长度需要符合 AES 密钥长度要求 (16, 24 或 32 字节)。
     *                  **注意：直接使用字符串的字节数组作为密钥可能不安全，且长度不匹配常用算法要求。生产环境应使用安全的密钥生成或派生方式。**
     * @param algorithm 指定的加密算法。
     *                  - 支持简写： "DES", "AES" (默认使用 "AES/ECB/PKCS5Padding")。
     *                  - 支持完整名称： "AES/ECB/PKCS5Padding", "AES/ECB/NoPadding"。
     * @return 根据指定密钥和算法配置好的 CryptoCore 实例。
     * @throws ToolboxException         如果密钥无效、算法不支持、初始化失败等密码学相关异常。
     * @throws IllegalArgumentException 如果传入的密钥或算法参数无效。
     */
    public static CryptoCore getInstance(String key, String algorithm) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("密钥 (key) 不能为空。");
        }
        if (algorithm == null || algorithm.isEmpty()) {
            throw new IllegalArgumentException("算法 (algorithm) 不能为空。");
        }
        CryptoCore instance = new CryptoCore();
        try {
            SecretKey secretKey;
            String cipherAlgorithmName = algorithm; // 用于 Cipher.getInstance() 的完整算法名称
            String baseAlgorithm; // 用于密钥生成的算法名称 (如 "AES", "DES")
            // 根据指定的算法进行密钥和 Cipher 的初始化
            String upperCaseAlgorithm = algorithm.toUpperCase();
            // 解析基础算法，用于密钥生成
            if (upperCaseAlgorithm.startsWith(ALGORITHM_AES)) {
                baseAlgorithm = ALGORITHM_AES;
            } else if (upperCaseAlgorithm.startsWith(ALGORITHM_DES)) {
                baseAlgorithm = ALGORITHM_DES;
            } else {
                throw new IllegalArgumentException("不支持的基础加密算法：" + algorithm);
            }
            // 处理算法简写，并确定最终用于 Cipher 的完整名称
            switch (baseAlgorithm) {
                case ALGORITHM_DES:
                    // DES 密钥需要通过 DESKeySpec 和 SecretKeyFactory 生成
                    if (upperCaseAlgorithm.equals(ALGORITHM_DES)) {
                        cipherAlgorithmName = "DES/ECB/PKCS5Padding";
                    }
                    DESKeySpec dks = new DESKeySpec(key.getBytes());
                    SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM_DES);
                    secretKey = skf.generateSecret(dks);
                    break;
                case ALGORITHM_AES:
                    // AES 密钥直接通过密钥字节数组和算法名称创建 SecretKeySpec
                    if (upperCaseAlgorithm.equals(ALGORITHM_AES)) {
                        // 为了向后兼容，如果只传入 "AES"，则默认使用 PKCS5Padding
                        cipherAlgorithmName = ALGORITHM_AES_ECB_PKCS5PADDING;
                    }
                    byte[] keyBytes = key.getBytes();
                    secretKey = new SecretKeySpec(keyBytes, ALGORITHM_AES); // SecretKeySpec 需要基础算法 "AES"
                    break;
                default:
                    // 这个分支实际上在上面的检查中已经处理了，但为了代码完整性保留
                    throw new IllegalArgumentException("不支持的加密算法：" + algorithm);
            }
            instance.encryptCipher = Cipher.getInstance(cipherAlgorithmName);
            instance.decryptCipher = Cipher.getInstance(cipherAlgorithmName);
            instance.encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey);
            instance.decryptCipher.init(Cipher.DECRYPT_MODE, secretKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException |
                 InvalidKeyException exception) {
            // 捕获密码学相关的异常，并包装成 ToolboxException 抛出
            throw new ToolboxException("初始化加密/解密器失败，算法：" + algorithm + "，原因：" + exception.getMessage(), exception);
        }
        return instance;
    }

    /**
     * 获取一个配置了 **AES 算法 (AES/ECB/PKCS5Padding)** 和指定密钥的 CryptoCore 实例。
     * <p>
     * 此方法是获取 AES 实例的便捷方法，内部调用 {@code getInstance(key, ALGORITHM_AES)}。
     * </p>
     *
     * @param key 用于 AES 加密的密钥字符串。
     *            请确保由 {@code key.getBytes()} 得到的字节数组长度符合 AES 密钥要求 (16, 24 或 32 字节)。
     *            **重要：直接使用字符串字节数组作为密钥不安全，生产环境应使用安全的密钥生成或派生方式。**
     * @return 配置了 AES 算法和指定密钥的 CryptoCore 实例。
     * @throws IllegalArgumentException 如果密钥参数无效。
     * @throws ToolboxException         如果 CryptoCore 实例初始化失败 (如密钥无效、算法问题等)。
     */
    public static CryptoCore getAesInstance(String key) {
        return getInstance(key, ALGORITHM_AES);
    }

    /**
     * 获取一个配置了 **AES 算法 (AES/ECB/NoPadding)** 和指定密钥的 CryptoCore 实例。
     * <p>
     * 使用此实例进行加密时，<b>待加密的数据长度必须是 16 字节的整数倍</b>，否则会抛出 {@code IllegalBlockSizeException}。
     * </p>
     *
     * @param key 用于 AES 加密的密钥字符串。
     *            请确保由 {@code key.getBytes()} 得到的字节数组长度符合 AES 密钥要求 (16, 24 或 32 字节)。
     * @return 配置了 AES NoPadding 算法和指定密钥的 CryptoCore 实例。
     * @throws IllegalArgumentException 如果密钥参数无效。
     * @throws ToolboxException         如果 CryptoCore 实例初始化失败。
     */
    public static CryptoCore getAesNoPadding(String key) {
        return getInstance(key, ALGORITHM_AES_ECB_NO_PADDING);
    }


    /**
     * 获取一个配置了 **DES 算法** 和指定密钥的 CryptoCore 实例。
     * <p>
     * 此方法是获取 DES 实例的便捷方法，内部调用 {@code getInstance(key, ALGORITHM_DES)}。
     * </p>
     *
     * @param key 用于 DES 加密的密钥字符串。
     *            通常需要是 8 字节（64位）。
     *            **重要：直接使用字符串字节数组作为密钥不安全，生产环境应使用安全的密钥生成或派生方式。**
     * @return 配置了 DES 算法和指定密钥的 CryptoCore 实例。
     * @throws IllegalArgumentException 如果密钥参数无效。
     * @throws ToolboxException         如果 CryptoCore 实例初始化失败 (如密钥无效、算法问题等)。
     */
    public static CryptoCore getDesInstance(String key) {
        return getInstance(key, ALGORITHM_DES);
    }

    /**
     * 使用当前实例配置的 Cipher 对象进行加密。
     *
     * @param data 待加密的原始数据字节数组。
     * @return 加密后的数据字节数组。
     * @throws ToolboxException 如果加密过程中发生错误。
     */
    public byte[] encrypt(byte[] data) {
        if (this.encryptCipher == null) {
            throw new ToolboxException("加密器未初始化。请先调用 getInstance 方法获取实例。");
        }
        if (data == null) {
            return null; // 或者抛出 IllegalArgumentException
        }
        try {
            return this.encryptCipher.doFinal(data);
        } catch (Exception exception) { // 捕获 doFinal 可能抛出的多种异常，如 IllegalBlockSizeException, BadPaddingException
            throw new ToolboxException("加密失败：" + exception.getMessage(), exception);
        }
    }

    /**
     * 使用当前实例配置的 Cipher 对象进行加密。
     * <p>
     * 将输入的字符串按照 UTF-8 编码转换为字节数组后进行加密。
     * </p>
     *
     * @param data 待加密的原始字符串。如果为 null，则返回 null。
     * @return 加密后的数据字节数组。
     * @throws ToolboxException 如果加密过程中发生错误。
     */
    public String encrypt(String data) {
        if (data == null) {
            return null;
        }
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        byte[] encrypt = this.encrypt(dataBytes);
        return java.util.Base64.getEncoder().encodeToString(encrypt);
    }

    /**
     * 接收字节，返回 Base64 字符串
     * 这个方法专门用于将二进制数据加密后，转换为便于传输的字符串格式。
     *
     * @param dataBytes 待加密的原始字节数组（例如 Protobuf 数据）。
     * @return 加密后并经过 Base64 编码的字符串。
     * @throws ToolboxException 如果加密过程中发生错误。
     * @since 3.0.14
     */
    public String encryptToBase64(byte[] dataBytes) {
        // 调用核心的字节加密方法
        byte[] encryptedBytes = encrypt(dataBytes);
        // 将加密后的二进制结果编码为 Base64 字符串
        if (encryptedBytes == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * 使用当前实例配置的 Cipher 对象进行解密。
     *
     * @param data 待解密的字节数组。
     * @return 解密后的原始数据字节数组。
     * @throws ToolboxException 如果解密过程中发生错误，例如数据格式不正确或密钥不匹配。
     */
    public byte[] decrypt(byte[] data) {
        if (this.decryptCipher == null) {
            throw new ToolboxException("解密器未初始化。请先调用 getInstance 方法获取实例。");
        }
        if (data == null) {
            return null; // 或者抛出 IllegalArgumentException
        }
        try {
            return this.decryptCipher.doFinal(data);
        } catch (Exception e) { // 捕获 doFinal 可能抛出的多种异常，如 IllegalBlockSizeException, BadPaddingException
            throw new ToolboxException("解密失败：" + e.getMessage(), e);
        }
    }

    /**
     * 使用当前实例配置的 Cipher 对象解密一个 Base64 编码的字符串。
     * <p>
     * 该方法首先将输入的 Base64 字符串解码为字节数组，然后对解码后的字节数组进行解密。
     * 最后，将解密得到的字节数组按照 UTF-8 编码转换为字符串返回。
     * </p>
     *
     * @param data 待解密的 Base64 编码字符串。如果为 null，则返回 null。
     *             该字符串应是由加密后的字节数组进行 Base64 编码得到的。
     * @return 解密后的原始字符串（使用 UTF-8 编码），如果输入为 null 则返回 null。
     * @throws IllegalArgumentException 如果输入的字符串不是有效的 Base64 编码格式。
     * @throws ToolboxException         如果 Base64 解码后的字节数组在解密过程中发生错误（例如，密钥不匹配、数据损坏或填充错误）。
     */
    public String decrypt(String data) {
        if (data == null) {
            return null;
        }
        // 如果 data 不是有效的 Base64 格式，Base64.getDecoder().decode() 会抛出 IllegalArgumentException
        byte[] dataBytes = Base64.getDecoder().decode(data);
        byte[] decrypt = decrypt(dataBytes); // 这里会调用 public byte[] decrypt(byte[] data) 方法
        return new String(decrypt, StandardCharsets.UTF_8);
    }

    /**
     * Base64 字符串解密
     *
     * @param encryptedBase64 经过 Base64 编码的密文字符串。
     * @return 解密后的原始数据字节数组。
     * @throws ToolboxException 如果解码或解密过程中发生错误。
     * @since 3.0.14
     */
    public byte[] decryptFromBase64(String encryptedBase64) {
        if (encryptedBase64 == null) {
            return null;
        }
        try {
            // 先将 Base64 字符串解码回原始的加密后字节数组
            byte[] encryptedData = Base64.getDecoder().decode(encryptedBase64);
            // 调用核心的字节解密方法
            return this.decrypt(encryptedData);
        } catch (IllegalArgumentException e) {
            // 捕获 Base64 解码失败的异常
            throw new ToolboxException("Base64 解码失败：输入的字符串不是有效的 Base64 格式。", e);
        } catch (ToolboxException toolboxException) {
            // 重新抛出 decrypt 方法可能抛出的异常
            throw toolboxException;
        }
    }

    /**
     * 使用当前实例配置的 Cipher 对象解密一个字节数组，并返回 UTF-8 编码的字符串。
     * <p>
     * 该方法直接对输入的加密字节数组进行解密，然后将结果转换为字符串。
     * </p>
     *
     * @param encryptedData 待解密的原始加密字节数组。如果为 null，则返回 null。
     * @return 解密后的原始字符串（使用 UTF-8 编码），如果输入为 null 则返回 null。
     * @throws ToolboxException 如果在解密过程中发生错误（例如，密钥不匹配、数据损坏或填充错误）。
     * @since 3.0.14
     */
    public String decryptToString(byte[] encryptedData) {
        if (encryptedData == null) {
            return null;
        }
        // 调用核心的字节解密方法，得到解密后的字节数组
        byte[] decryptedBytes = decrypt(encryptedData);
        // 将解密后的字节数组按照 UTF-8 编码转换为字符串
        if (decryptedBytes == null) {
            return null;
        }
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
