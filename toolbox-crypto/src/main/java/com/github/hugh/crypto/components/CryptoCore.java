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
     * 获取 CryptoCore 实例（不带初始化向量 IV，通常用于 ECB 模式）。
     * <p>
     * 这是一个便捷方法，内部会调用 {@link #getInstance(byte[], byte[], String)} 并将 IV 置为 null。
     * 适用于不需要 IV 的加密工作模式（例如 ECB 模式）。
     *
     * @param keyBytes  密钥字节数组
     * @param algorithm 加密算法名称或完整的算法转换名称（如 "AES", "DES", "SM4"）
     * @return 初始化完成的 CryptoCore 实例
     * @throws IllegalArgumentException 当密钥为空或算法为空时抛出
     * @throws ToolboxException         当底层密码学组件初始化失败时抛出
     */
    public static CryptoCore getInstance(byte[] keyBytes, String algorithm) {
        return getInstance(keyBytes, null, algorithm);
    }

    /**
     * 获取 CryptoCore 实例（支持字符串密钥和字节数组 IV）。
     * <p>
     * 该方法适合密钥以纯文本字符串形式存在的场景。内部会自动将字符串密钥按照 <b>UTF-8</b> 编码
     * 转换为字节数组，然后调用底层核心初始化方法。
     *
     * @param key       字符串形式的密钥（不能为 null 或空字符串）
     * @param iv        初始化向量（IV）或 Nonce 字节数组，对于不需要 IV 的模式（如 ECB）可传 null
     * @param algorithm 加密算法名称（如 "AES", "DES", "SM4" 或带有模式/填充的完整名称）
     * @return 初始化完成的 CryptoCore 实例
     * @throws IllegalArgumentException 当密钥/算法为空，或者特定模式缺少 IV 时抛出
     * @throws ToolboxException         当底层密码学组件初始化失败时抛出
     */
    public static CryptoCore getInstance(String key, byte[] iv, String algorithm) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("密钥 (key) 不能为空。");
        }
        if (algorithm == null || algorithm.isEmpty()) {
            throw new IllegalArgumentException("算法 (algorithm) 不能为空。");
        }
        return getInstance(key.getBytes(StandardCharsets.UTF_8), iv, algorithm);
    }

    /**
     * 获取 CryptoCore 实例的核心方法（使用字节数组密钥和字节数组 IV）。
     * <p>
     * 该方法负责解析基础算法类型（AES/DES/SM4），补全默认的工作模式和填充方式（若未指定，默认
     * 使用 ECB/PKCS5Padding），生成对应的加密密钥（SecretKey），并根据算法和工作模式（如 CBC、GCM）
     * 自动装配所需的参数规格（AlgorithmParameterSpec）。最后，同时初始化好用于加密和解密的 Cipher 实例。
     *
     * @param keyBytes  密钥字节数组（不能为 null 或空）
     * @param iv        初始化向量（IV）或 Nonce 字节数组，工作模式需要时必填，不需要时（如 ECB）可传 null
     * @param algorithm 加密算法名称或完整的转换格式（例如 "AES"、"AES/CBC/PKCS5Padding"、"AES/GCM/NoPadding" 等）
     * @return 初始化完成的 CryptoCore 实例，包含已配置好的加密和解密 Cipher
     * @throws IllegalArgumentException 当密钥为空、算法为空、是不支持的基础算法，或者特定工作模式下未提供 IV 时抛出
     * @throws ToolboxException         当底层 Java 加密架构（JCA）报出算法不存在、密钥规格错误、填充错误或参数无效等异常时抛出
     */
    public static CryptoCore getInstance(byte[] keyBytes, byte[] iv, String algorithm) {
        if (keyBytes == null || keyBytes.length == 0) {
            throw new IllegalArgumentException("密钥 (keyBytes) 不能为空。");
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
                    throw new IllegalArgumentException("未知异常分支");
            }
            // 初始化 Cipher
            instance.encryptCipher = Cipher.getInstance(cipherAlgorithmName);
            instance.decryptCipher = Cipher.getInstance(cipherAlgorithmName);
            AlgorithmParameterSpec paramSpec = getAlgorithmParameterSpec(iv, cipherAlgorithmName);
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
     * 根据算法转换名称和 IV 字节数组，解析并构建对应的算法参数规格对象（AlgorithmParameterSpec）。
     * <p>
     * <b>解析规则：</b>
     * <ul>
     * <li>如果算法转换名称中包含 <b>"/CBC/"</b>，则校验并将其封装为 {@link IvParameterSpec}</li>
     * <li>如果算法转换名称中包含 <b>"/GCM/"</b>，则校验并将其封装为 {@link GCMParameterSpec}，其中认证标签（Authentication Tag）长度固定为 128 位（16 字节）</li>
     * <li>对于其他模式（如 ECB），由于不需要额外参数，直接返回 null</li>
     * </ul>
     *
     * @param iv                  初始化向量（IV）或 Nonce 字节数组
     * @param cipherAlgorithmName 完整的加密算法转换名称（例如 "AES/CBC/PKCS5Padding" 或 "AES/GCM/NoPadding"）
     * @return 对应的 {@link AlgorithmParameterSpec} 实例；如果是不需要参数的模式则返回 null
     * @throws IllegalArgumentException 当算法属于 CBC 或 GCM 模式，但未提供所需的 iv 参数时抛出
     */
    private static AlgorithmParameterSpec getAlgorithmParameterSpec(byte[] iv, String cipherAlgorithmName) {
        AlgorithmParameterSpec paramSpec = null;
        if (cipherAlgorithmName.contains("/CBC/")) {
            if (iv == null) throw new IllegalArgumentException("CBC模式必须提供 IV 参数");
            paramSpec = new IvParameterSpec(iv);
        } else if (cipherAlgorithmName.contains("/GCM/")) {
            if (iv == null) throw new IllegalArgumentException("GCM模式必须提供 IV(Nonce) 参数");
            // GCM 需要 GCMParameterSpec，128 表示 Authentication Tag 的长度为 128 bit (16 byte)
            paramSpec = new GCMParameterSpec(128, iv);
        }
        return paramSpec;
    }

    /**
     * 获取配置了 <b>AES 算法 (AES/ECB/PKCS5Padding)</b> 的 CryptoCore 实例。
     * <p>
     * 此方法为获取 AES 实例的便捷入口，内部默认使用 ECB 模式和 PKCS5Padding 填充。
     * </p>
     *
     * @param key 用于 AES 加密的密钥字符串。
     *            请确保由 {@code key.getBytes()} 得到的字节数组长度符合 AES 密钥要求（16、24 或 32 字节）。
     *            <b>安全提示：</b>直接使用字符串的默认字节编码作为密钥存在风险，生产环境应使用安全的密钥生成或派生方式（KDF）。
     * @return 配置完毕的 CryptoCore 实例
     * @throws IllegalArgumentException 如果密钥参数无效
     * @throws ToolboxException         如果实例初始化失败（如底层算法不支持）
     */
    public static CryptoCore getAesInstance(String key) {
        return getInstance(key, ALGORITHM_AES);
    }

    /**
     * 获取配置了 <b>AES 算法 (AES/ECB/PKCS5Padding)</b> 的 CryptoCore 实例（原生二进制密钥）。
     * <p>
     * 此方法为 {@link #getAesInstance(String)} 的重载，直接传入字节数组可避免字符集编码带来的不确定性。
     * </p>
     *
     * @param keyBytes 用于 AES 加密的原生字节数组密钥（要求 16、24 或 32 字节）。
     * @return 配置完毕的 CryptoCore 实例
     * @throws IllegalArgumentException 如果密钥参数无效
     * @throws ToolboxException         如果实例初始化失败
     * @since 3.0.23
     */
    public static CryptoCore getAesInstance(byte[] keyBytes) {
        return getInstance(keyBytes, ALGORITHM_AES);
    }

    /**
     * 获取配置了 <b>AES 算法 (AES/ECB/NoPadding)</b> 的 CryptoCore 实例。
     * <p>
     * <b>注意：</b>使用 NoPadding 模式时，待加密的数据长度<b>必须是 16 字节的整数倍</b>，否则底层会抛出 {@code IllegalBlockSizeException}。
     * </p>
     *
     * @param key 用于 AES 加密的密钥字符串。
     *            请确保由 {@code key.getBytes()} 得到的字节数组长度符合 AES 密钥要求（16、24 或 32 字节）。
     * @return 配置完毕的 CryptoCore 实例
     * @throws IllegalArgumentException 如果密钥参数无效
     * @throws ToolboxException         如果实例初始化失败
     */
    public static CryptoCore getAesNoPadding(String key) {
        return getInstance(key, ALGORITHM_AES_ECB_NO_PADDING);
    }

    /**
     * 获取配置了 <b>AES 算法 (AES/ECB/NoPadding)</b> 的 CryptoCore 实例（原生二进制密钥）。
     * <p>
     * 此方法为 {@link #getAesNoPadding(String)} 的重载。使用 NoPadding 模式时，数据长度必须为 16 字节的整数倍。
     * </p>
     *
     * @param keyBytes 用于 AES 加密的原生字节数组密钥（要求 16、24 或 32 字节）。
     * @return 配置完毕的 CryptoCore 实例
     * @throws IllegalArgumentException 如果密钥参数无效
     * @throws ToolboxException         如果实例初始化失败
     * @since 3.0.23
     */
    public static CryptoCore getAesNoPadding(byte[] keyBytes) {
        return getInstance(keyBytes, ALGORITHM_AES_ECB_NO_PADDING);
    }

    /**
     * 获取配置了 <b>DES 算法</b> 的 CryptoCore 实例。
     * <p>
     * 此方法为获取 DES 实例的便捷入口，内部默认调用 {@code ALGORITHM_DES}。
     * </p>
     *
     * @param key 用于 DES 加密的密钥字符串。
     *            DES 密钥长度通常要求为 8 字节（64位）。
     *            <b>安全提示：</b>直接使用字符串的默认字节编码作为密钥存在风险，建议使用安全的密钥派生方式。
     * @return 配置完毕的 CryptoCore 实例
     * @throws IllegalArgumentException 如果密钥参数无效
     * @throws ToolboxException         如果实例初始化失败
     */
    public static CryptoCore getDesInstance(String key) {
        return getInstance(key, ALGORITHM_DES);
    }

    /**
     * 获取配置了 <b>DES 算法</b> 的 CryptoCore 实例（原生二进制密钥）。
     * <p>
     * 此方法为 {@link #getDesInstance(String)} 的重载，直接传入字节数组可避免字符集编码问题。
     * </p>
     *
     * @param keyBytes 用于 DES 加密的原生字节数组密钥（通常为 8 字节）。
     * @return 配置完毕的 CryptoCore 实例
     * @throws IllegalArgumentException 如果密钥参数无效
     * @throws ToolboxException         如果实例初始化失败
     * @since 3.0.23
     */
    public static CryptoCore getDesInstance(byte[] keyBytes) {
        return getInstance(keyBytes, ALGORITHM_DES);
    }

    /**
     * 获取配置了 <b>AES 算法 (AES/CBC/PKCS5Padding)</b> 的 CryptoCore 实例。
     * <p>
     * CBC 模式需要配合初始化向量 (IV) 使用，能有效提高安全性。内部默认使用 PKCS5Padding 填充。
     * </p>
     *
     * @param key 密钥字符串。底层转换为字节数组后要求为 16、24 或 32 字节。
     * @param iv  初始化向量 (Initialization Vector)，通常要求与分组长度一致（AES 为 16 字节）。
     * @return 配置完毕的 CryptoCore 实例
     * @throws IllegalArgumentException 如果密钥或 IV 参数无效
     * @throws ToolboxException         如果实例初始化失败
     * @since 3.0.22
     */
    public static CryptoCore getAesCbcInstance(String key, byte[] iv) {
        return getInstance(key, iv, ALGORITHM_AES_CBC_PKCS5PADDING);
    }

    /**
     * 获取配置了 <b>AES 算法 (AES/CBC/PKCS5Padding)</b> 的 CryptoCore 实例（原生二进制密钥）。
     * <p>
     * 此方法为 {@link #getAesCbcInstance(String, byte[])} 的重载。
     * </p>
     *
     * @param keyBytes 原生字节数组密钥（要求 16、24 或 32 字节）。
     * @param iv       初始化向量 (Initialization Vector)。
     * @return 配置完毕的 CryptoCore 实例
     * @throws IllegalArgumentException 如果密钥或 IV 参数无效
     * @throws ToolboxException         如果实例初始化失败
     * @since 3.0.23
     */
    public static CryptoCore getAesCbcInstance(byte[] keyBytes, byte[] iv) {
        return getInstance(keyBytes, iv, ALGORITHM_AES_CBC_PKCS5PADDING);
    }

    /**
     * 获取配置了 <b>AES 算法 (AES/GCM/NoPadding)</b> 的 CryptoCore 实例。
     * <p>
     * GCM (Galois/Counter Mode) 是一种提供认证加密的模式。内部默认无填充 (NoPadding)。
     * </p>
     *
     * @param key 密钥字符串。底层转换为字节数组后要求为 16、24 或 32 字节。
     * @param iv  初始化向量 (Initialization Vector)，在 GCM 模式中通常称为 Nonce，建议长度为 12 字节 (96位)。
     * @return 配置完毕的 CryptoCore 实例
     * @throws IllegalArgumentException 如果密钥或 IV 参数无效
     * @throws ToolboxException         如果实例初始化失败
     * @since 3.0.22
     */
    public static CryptoCore getAesGcmInstance(String key, byte[] iv) {
        return getInstance(key, iv, ALGORITHM_AES_GCM_NO_PADDING);
    }

    /**
     * 获取配置了 <b>AES 算法 (AES/GCM/NoPadding)</b> 的 CryptoCore 实例（原生二进制密钥）。
     * <p>
     * 此方法为 {@link #getAesGcmInstance(String, byte[])} 的重载。
     * </p>
     *
     * @param keyBytes 原生字节数组密钥（要求 16、24 或 32 字节）。
     * @param iv       初始化向量 / Nonce。
     * @return 配置完毕的 CryptoCore 实例
     * @throws IllegalArgumentException 如果密钥或 IV 参数无效
     * @throws ToolboxException         如果实例初始化失败
     * @since 3.0.23
     */
    public static CryptoCore getAesGcmInstance(byte[] keyBytes, byte[] iv) {
        return getInstance(keyBytes, iv, ALGORITHM_AES_GCM_NO_PADDING);
    }

    /**
     * 获取配置了 <b>国密 SM4 算法 (默认 ECB 模式)</b> 的 CryptoCore 实例。
     * <p>
     * ECB 模式为基础加密模式，不需要初始化向量 (IV)。
     * </p>
     *
     * @param key 密钥字符串。SM4 密钥长度固定要求为 16 字节（128位）。
     * @return 配置完毕的 CryptoCore 实例
     * @throws IllegalArgumentException 如果密钥参数无效
     * @throws ToolboxException         如果实例初始化失败
     * @since 3.0.22
     */
    public static CryptoCore getSm4Instance(String key) {
        return getInstance(key, null, ALGORITHM_SM4);
    }

    /**
     * 获取配置了 <b>国密 SM4 算法 (默认 ECB 模式)</b> 的 CryptoCore 实例（原生二进制密钥）。
     * <p>
     * 此方法为 {@link #getSm4Instance(String)} 的重载。
     * </p>
     *
     * @param keyBytes 原生字节数组密钥（要求 16 字节）。
     * @return 配置完毕的 CryptoCore 实例
     * @throws IllegalArgumentException 如果密钥参数无效
     * @throws ToolboxException         如果实例初始化失败
     * @since 3.0.23
     */
    public static CryptoCore getSm4Instance(byte[] keyBytes) {
        return getInstance(keyBytes, null, ALGORITHM_SM4);
    }

    /**
     * 获取配置了 <b>国密 SM4 算法 (SM4/CBC/PKCS5Padding)</b> 的 CryptoCore 实例。
     * <p>
     * SM4 的 CBC 模式，需要配合初始化向量 (IV) 使用。内部默认使用 PKCS5Padding 填充方式。
     * </p>
     *
     * @param key 密钥字符串。SM4 密钥长度固定要求为 16 字节。
     * @param iv  初始化向量 (Initialization Vector)，固定要求为 16 字节。
     * @return 配置完毕的 CryptoCore 实例
     * @throws IllegalArgumentException 如果密钥或 IV 参数无效
     * @throws ToolboxException         如果实例初始化失败
     * @since 3.0.22
     */
    public static CryptoCore getSm4CbcInstance(String key, byte[] iv) {
        return getInstance(key, iv, ALGORITHM_SM4_CBC_PKCS5PADDING);
    }

    /**
     * 获取配置了 <b>国密 SM4 算法 (SM4/CBC/PKCS5Padding)</b> 的 CryptoCore 实例（原生二进制密钥）。
     * <p>
     * 此方法为 {@link #getSm4CbcInstance(String, byte[])} 的重载。
     * </p>
     *
     * @param keyBytes 原生字节数组密钥（要求 16 字节）。
     * @param iv       初始化向量 (Initialization Vector)。
     * @return 配置完毕的 CryptoCore 实例
     * @throws IllegalArgumentException 如果密钥或 IV 参数无效
     * @throws ToolboxException         如果实例初始化失败
     * @since 3.0.23
     */
    public static CryptoCore getSm4CbcInstance(byte[] keyBytes, byte[] iv) {
        return getInstance(keyBytes, iv, ALGORITHM_SM4_CBC_PKCS5PADDING);
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
