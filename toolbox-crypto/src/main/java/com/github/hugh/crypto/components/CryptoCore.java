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
 * <ul>
 * <li>直接使用字符串的 {@code getBytes()} 作为密钥通常不安全，且对于 AES，字节长度可能不匹配算法要求。
 * 生产环境应使用 {@link javax.crypto.KeyGenerator} 生成安全密钥，或使用 {@link javax.crypto.SecretKeyFactory}
 * 从密码派生密钥 (如 PBKDF2)。</li>
 * <li>加密模式 (Mode) 和填充方式 (Padding) 对安全性至关重要。本示例中 {@code Cipher.getInstance(algorithmName)}
 * 使用了 JVM 默认设置，可能不是最安全的选择 (如 ECB 模式)。生产环境强烈建议明确指定模式和填充，
 * 并在使用如 CBC 模式时妥善处理 IV (Initialization Vector)。</li>
 * </ul>
 * </p>
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
    public static final String ALGORITHM_AES = "AES";

    /**
     * 获取 CryptoCore 实例，支持 DES 和 AES 加密/解密。
     * <p>
     * 该方法是工厂方法，每次调用都会返回一个新的 DesEncDecUtils 实例，
     * 该实例已使用指定的密钥和算法初始化好加密和解密所需的 Cipher 对象。
     * 请注意：同一个 DesEncDecUtils 实例只能用于创建时指定的密钥和算法。
     * </p>
     *
     * @param key       密钥字符串。
     *                  对于 DES，密钥通常是 8 字节。
     *                  对于 AES，由 {@code key.getBytes()} 得到的字节数组长度需要符合 AES 密钥长度要求 (16, 24 或 32 字节，对应 128, 192, 256 位)。
     *                  **注意：直接使用字符串的字节数组作为密钥可能不安全，且长度不匹配常用算法要求。生产环境应使用安全的密钥生成或派生方式。**
     * @param algorithm 指定的加密算法，目前支持 "DES" 或 "AES" (不区分大小写)。
     * @return 根据指定密钥和算法配置好的 DesEncDecUtils 实例。
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
            String cipherAlgorithmName; // 用于 Cipher.getInstance() 的算法名称，可以包含模式和填充
            // 根据指定的算法进行密钥和 Cipher 的初始化
            switch (algorithm.toUpperCase()) { // 转换为大写以支持不区分大小写输入
                case ALGORITHM_DES:
                    // DES 密钥需要通过 DESKeySpec 和 SecretKeyFactory 生成
                    // 注意：原始代码没有指定模式和填充，这里沿用，但生产环境建议明确指定，如 "DES/ECB/PKCS5Padding"
                    cipherAlgorithmName = ALGORITHM_DES; // 或者 "DES/ECB/PKCS5Padding" 等
                    DESKeySpec dks = new DESKeySpec(key.getBytes());
                    SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM_DES);
                    secretKey = skf.generateSecret(dks);
                    break;
                case ALGORITHM_AES:
                    // AES 密钥直接通过密钥字节数组和算法名称创建 SecretKeySpec
                    // WARNING: key.getBytes() 得到的字节数组长度不一定符合 AES 密钥长度 (16, 24, 32 字节)
                    // 如果长度不正确，可能会抛出 InvalidKeyException 或导致加密解密失败
                    // 生产环境应使用 KeyGenerator 生成安全密钥，或从密码派生符合长度的密钥
                    cipherAlgorithmName = ALGORITHM_AES; // 或者 "AES/CBC/PKCS5Padding" 等
                    byte[] keyBytes = key.getBytes();
                    secretKey = new SecretKeySpec(keyBytes, ALGORITHM_AES);
                    // 可选：简单检查密钥长度是否符合常见的 AES 长度
                    // int keyLength = keyBytes.length;
                    // if (keyLength != 16 && keyLength != 24 && keyLength != 32) {
                    //     System.err.println("警告：由字符串衍生的 AES 密钥长度为 " + keyLength + " 字节。常见的 AES 密钥长度为 16, 24 或 32 字节。这可能会导致问题或不安全。");
                    //     // 如果严格要求密钥长度，可以在此处抛出异常：
                    //     // throw new InvalidKeyException("AES 密钥长度无效：" + keyLength + " 字节。");
                    // }
                    break;
                default:
                    // 如果指定了不支持的算法，抛出异常
                    throw new IllegalArgumentException("不支持的加密算法：" + algorithm + "。目前支持：" + ALGORITHM_DES + ", " + ALGORITHM_AES);
            }
            // 实例化和初始化加密和解密 Cipher
            // 注意：生产环境强烈建议明确指定模式和填充 (例如 "AES/CBC/PKCS5Padding")
            // 并为如 CBC 模式处理 IV (Initialization Vector)，这对安全性至关重要。
            // 仅使用算法名称 "DES" 或 "AES" 依赖于 JVM 默认设置，可能不安全或不一致。
            instance.encryptCipher = Cipher.getInstance(cipherAlgorithmName);
            instance.decryptCipher = Cipher.getInstance(cipherAlgorithmName);
            instance.encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey);
            instance.decryptCipher.init(Cipher.DECRYPT_MODE, secretKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException |
                 InvalidKeyException exception) {
            // 捕获密码学相关的异常，并包装成 ToolboxException 抛出
            throw new ToolboxException("初始化加密/解密器失败，算法：" + algorithm + "，原因：" + exception.getMessage(), exception); // 传递原始异常
        }
        // 返回配置好的实例
        return instance;
    }

    /**
     * 获取一个配置了 **AES 算法** 和指定密钥的 CryptoCore 实例。
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
     * 该字符串应是由加密后的字节数组进行 Base64 编码得到的。
     * @return 解密后的原始字符串（使用 UTF-8 编码），如果输入为 null 则返回 null。
     * @throws IllegalArgumentException 如果输入的字符串不是有效的 Base64 编码格式。
     * @throws ToolboxException 如果 Base64 解码后的字节数组在解密过程中发生错误（例如，密钥不匹配、数据损坏或填充错误）。
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
}
