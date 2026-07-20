package com.github.hugh.crypto.components;

import com.github.hugh.exception.ToolboxException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
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

    // === 静态代码块：注册国密/拓展算法提供者 ===
    static {
        // 注册 BouncyCastle 以支持 SM4 等国密算法
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    // 私有构造方法，防止外部直接实例化
    private CryptoCore() {
    }

    // 类的实例不再是单例，每个实例持有自己的 Cipher 对象
    private Cipher encryptCipher;
    private Cipher decryptCipher;

    /**
     * DES（Data Encryption Standard）数据加密标准算法标识。
     * <p>
     * <b>默认变换模式：</b>若仅指定此算法标识，默认解析为 {@code "DES/ECB/PKCS5Padding"}。<br>
     * <b>规格说明：</b>
     * <ul>
     *   <li>分组大小（Block Size）：64 位（8 字节）。</li>
     *   <li>密钥长度：固定为 8 字节（64 位，其中包含 56 位有效密钥与 8 位奇偶校验位）。</li>
     *   <li>填充方式：标准的 PKCS#5 填充（填充 1~8 字节）。</li>
     * </ul>
     * <b>注意：</b>DES 密钥强度较低，已被证明不够安全，除兼容老旧系统外不建议在新系统中使用。
     */
    public static final String ALGORITHM_DES = "DES";

    /**
     * AES（Advanced Encryption Standard）高级加密标准算法标识。
     * <p>
     * <b>默认变换模式：</b>若仅指定此算法标识，默认解析为 {@code "AES/ECB/PKCS5Padding"}。<br>
     * <b>规格说明：</b>
     * <ul>
     *   <li>分组大小（Block Size）：128 位（16 字节）。</li>
     *   <li>密钥长度：支持 16、24 或 32 字节（对应 128、192 或 256 位）。</li>
     *   <li>填充方式：Java 声明为 {@code PKCS5Padding}，但因 AES 块大小为 16 字节，底层实际执行的是 <b>PKCS7Padding</b>（填充 1~16 字节）。</li>
     * </ul>
     */
    public static final String ALGORITHM_AES = "AES";

    /**
     * 国密 SM4 对称加密算法标识。
     * <p>
     * <b>默认变换模式：</b>若仅指定此算法标识，默认解析为 {@code "SM4/ECB/PKCS5Padding"}。<br>
     * <b>规格说明：</b>
     * <ul>
     *   <li>分组大小（Block Size）：固定为 128 位（16 字节）。</li>
     *   <li>密钥长度：固定为 16 字节（128 位）。</li>
     *   <li>填充方式：声明为 {@code PKCS5Padding} 时，在 Bouncy Castle 等 Provider 中底层实际调用的同样是 <b>PKCS7Padding</b>（填充 1~16 字节）。</li>
     *   <li>环境依赖：需要系统中注册了支持国密算法的 Security Provider（如 Bouncy Castle）。</li>
     * </ul>
     */
    public static final String ALGORITHM_SM4 = "SM4";

    /**
     * AES/ECB 模式 + PKCS5Padding 填充。
     * <p>
     * <b>注意：</b>ECB 模式不具备隐蔽明文结构的能力，相同的明文块会生成相同的密文块，存在安全性风险，不建议在安全要求较高的场景中使用。
     */
    public static final String ALGORITHM_AES_ECB_PKCS5PADDING = "AES/ECB/PKCS5Padding";

    /**
     * AES/ECB 模式 + 无填充。
     * <p>
     * 要求待加密的明文数据长度必须是 AES 分组大小（16 字节 / 128 位）的整数倍，否则在加密时会抛出异常。
     */
    public static final String ALGORITHM_AES_ECB_NO_PADDING = "AES/ECB/NoPadding";

    /**
     * AES/CBC 模式 + PKCS5Padding 填充。
     * <p>
     * 采用密码块链接模式（CBC），加密时需要搭配随机生成的初始向量（IV），安全性高于 ECB 模式。
     */
    public static final String ALGORITHM_AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5Padding";

    /**
     * AES/GCM 模式 + 无填充。
     * <p>
     * 伽罗瓦/计数器模式（GCM），属于认证加密（AEAD）算法，同时提供机密性与数据完整性校验，是目前推荐使用的 AES 加密模式。
     */
    public static final String ALGORITHM_AES_GCM_NO_PADDING = "AES/GCM/NoPadding";

    /**
     * 国密 SM4/ECB 模式 + PKCS5Padding 填充。
     * <p>
     * <b>说明：</b>SM4 的分组大小为 128 位（16 字节）。在 Java 密码扩展（如 Bouncy Castle）中，
     * 声明为 {@code PKCS5Padding} 时，实际执行的是标准的 <b>PKCS7Padding</b> 机制（填充字节数为 1~16 字节）。
     */
    public static final String ALGORITHM_SM4_ECB_PKCS5PADDING = "SM4/ECB/PKCS5Padding";

    /**
     * 国密 SM4/CBC 模式 + PKCS5Padding 填充。
     * <p>
     * <b>说明：</b>SM4 的分组大小为 128 位（16 字节）。在 Java 密码扩展（如 Bouncy Castle）中，
     * 声明为 {@code PKCS5Padding} 时，实际执行的是标准的 <b>PKCS7Padding</b> 机制（填充字节数为 1~16 字节）。
     */
    public static final String ALGORITHM_SM4_CBC_PKCS5PADDING = "SM4/CBC/PKCS5Padding";

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
        return getInstance(key, null, algorithm);
    }

    /**
     * 【核心工厂方法】带有 IV 支持的 getInstance，支持所有高级模式 (CBC/GCM)
     *
     * @param key       密钥字符串 (建议长度匹配对应算法)
     * @param iv        初始化向量，ECB模式传 null，CBC/GCM模式必须传非空数组
     * @param algorithm 完整算法名称或简写
     */
    public static CryptoCore getInstance(String key, byte[] iv, String algorithm) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("密钥 (key) 不能为空。");
        }
        if (algorithm == null || algorithm.isEmpty()) {
            throw new IllegalArgumentException("算法 (algorithm) 不能为空。");
        }
        CryptoCore instance = new CryptoCore();
        try {
            SecretKey secretKey;
            String cipherAlgorithmName = algorithm;
            String baseAlgorithm;
            String upperCaseAlgorithm = algorithm.toUpperCase();
            // 解析基础算法 (用于 SecretKeySpec)
            if (upperCaseAlgorithm.startsWith(ALGORITHM_AES)) {
                baseAlgorithm = ALGORITHM_AES;
            } else if (upperCaseAlgorithm.startsWith(ALGORITHM_DES)) {
                baseAlgorithm = ALGORITHM_DES;
            } else if (upperCaseAlgorithm.startsWith(ALGORITHM_SM4)) {
                baseAlgorithm = ALGORITHM_SM4;
            } else {
                throw new IllegalArgumentException("不支持的基础加密算法：" + algorithm);
            }
            // 统一生成密钥字节 (推荐显式指定 UTF_8，避免系统默认编码差异)
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            // 根据算法生成 SecretKey 和 确定真实 cipherName
            switch (baseAlgorithm) {
                case ALGORITHM_DES:
                    if (upperCaseAlgorithm.equals(ALGORITHM_DES)) cipherAlgorithmName = "DES/ECB/PKCS5Padding";
                    DESKeySpec dks = new DESKeySpec(keyBytes);
                    SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM_DES);
                    secretKey = skf.generateSecret(dks);
                    break;
                case ALGORITHM_AES:
                    if (upperCaseAlgorithm.equals(ALGORITHM_AES)) cipherAlgorithmName = ALGORITHM_AES_ECB_PKCS5PADDING;
                    secretKey = new SecretKeySpec(keyBytes, ALGORITHM_AES);
                    break;
                case ALGORITHM_SM4:
                    if (upperCaseAlgorithm.equals(ALGORITHM_SM4)) cipherAlgorithmName = ALGORITHM_SM4_ECB_PKCS5PADDING;
                    // SM4 的密钥处理和 AES 一样，直接封装为 SecretKeySpec 即可 (需要 BouncyCastle)
                    secretKey = new SecretKeySpec(keyBytes, ALGORITHM_SM4);
                    break;
                default:
                    throw new IllegalArgumentException("不支持的加密算法：" + algorithm);
            }
            // 初始化 Cipher
            // 如果算法中带有 BouncyCastle 的提供者 (SM4 需要)，Cipher.getInstance 可以自动从 BC 中寻找
            instance.encryptCipher = Cipher.getInstance(cipherAlgorithmName);
            instance.decryptCipher = Cipher.getInstance(cipherAlgorithmName);
            // 5. 根据模式解析并注入 IV 规格参数 (ParameterSpec)
            AlgorithmParameterSpec paramSpec = null;
            if (cipherAlgorithmName.contains("/CBC/")) {
                if (iv == null) throw new IllegalArgumentException("CBC模式必须提供 IV 参数");
                paramSpec = new IvParameterSpec(iv);
            } else if (cipherAlgorithmName.contains("/GCM/")) {
                if (iv == null) throw new IllegalArgumentException("GCM模式必须提供 IV(Nonce) 参数");
                // GCM 需要 GCMParameterSpec，128 表示 Authentication Tag 的长度为 128 bit (16 byte)
                paramSpec = new GCMParameterSpec(128, iv);
            }
            // 执行 Init (分带参数和不带参数)
            if (paramSpec != null) {
                instance.encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
                instance.decryptCipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            } else {
                instance.encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey);
                instance.decryptCipher.init(Cipher.DECRYPT_MODE, secretKey);
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException |
                 InvalidKeyException | InvalidAlgorithmParameterException exception) {
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
     * 获取 AES-CBC 模式的加密核心实例。
     * 内部默认使用 PKCS5Padding 填充方式。
     *
     * @param key 密钥字符串
     * @param iv  初始化向量 (Initialization Vector)
     * @return CryptoCore 实例
     * @since 3.0.22
     */
    public static CryptoCore getAesCbcInstance(String key, byte[] iv) {
        return getInstance(key, iv, ALGORITHM_AES_CBC_PKCS5PADDING);
    }

    /**
     * 获取 AES-GCM 模式的加密核心实例。
     * 内部默认使用 NoPadding 无填充方式，GCM 模式自带认证功能。
     *
     * @param key 密钥字符串
     * @param iv  初始化向量 (Initialization Vector)，在 GCM 模式中通常也称为 Nonce
     * @return CryptoCore 实例
     * @since 3.0.22
     */
    public static CryptoCore getAesGcmInstance(String key, byte[] iv) {
        return getInstance(key, iv, ALGORITHM_AES_GCM_NO_PADDING);
    }

    /**
     * 获取国密 SM4 默认模式的加密核心实例。
     * 默认使用 ECB 模式，该模式不需要初始化向量 (IV)。
     *
     * @param key 密钥字符串
     * @return CryptoCore 实例
     * @since 3.0.22
     */
    public static CryptoCore getSm4Instance(String key) {
        return getInstance(key, null, ALGORITHM_SM4);
    }

    /**
     * 获取国密 SM4-CBC 模式的加密核心实例。
     * 内部默认使用 PKCS5Padding 填充方式。
     *
     * @param key 密钥字符串
     * @param iv  初始化向量 (Initialization Vector)
     * @return CryptoCore 实例
     * @since 3.0.22
     */
    public static CryptoCore getSm4CbcInstance(String key, byte[] iv) {
        return getInstance(key, iv, ALGORITHM_SM4_CBC_PKCS5PADDING);
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
