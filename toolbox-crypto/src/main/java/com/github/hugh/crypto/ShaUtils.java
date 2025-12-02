package com.github.hugh.crypto;

import com.github.hugh.constant.EncryptCode;
import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.base.BaseConvertUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * sha 加密工具类
 *
 * @author hugh
 * @since 2.0.1
 */
public class ShaUtils {
    private ShaUtils() {
    }

    /**
     * 传入文本内容，返回小写的SHA-256串
     *
     * @param text 内容
     * @return String
     */
    public static String lowerCase256(final String text) {
        return Md5Utils.encrypt(text, true, EncryptCode.SHA_256);
    }

    /**
     * 传入文本内容，返回小写的SHA-512串
     *
     * @param text 内容
     * @return String
     */
    public static String lowerCase512(final String text) {
        return Md5Utils.encrypt(text, true, EncryptCode.SHA_512);
    }

    /**
     * 底层核心方法：基于 byte[] 数据和 byte[] 密钥计算 HMAC-SHA256，返回原始字节数组。
     * <p>
     * 此方法适用于需要获取原始二进制签名结果的场景（例如后续需要进行 Base64 编码，
     * 或者作为其他加密算法的输入）。
     *
     * @param data 要签名的原始字节数组
     * @param key  密钥字节数组
     * @return 签名的原始字节数组 (byte[])
     * @throws ToolboxException 如果计算过程中出现算法不支持等异常
     * @since 3.1.0
     */
    public static byte[] hmacSha256Bytes(byte[] data, byte[] key) {
        // 1. 快速失败检查
        if (data == null || key == null) {
            throw new IllegalArgumentException("Data and Key must not be null for HMAC calculation");
        }
        try {
            String algorithm = "HmacSHA256";
            Mac mac = Mac.getInstance(algorithm);
            // 2. 创建密钥规范
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, algorithm);
            mac.init(secretKeySpec);
            // 3. 执行计算并直接返回字节数组
            return mac.doFinal(data);
        } catch (Exception e) {
            throw new ToolboxException("HMAC-SHA256 calculation failed", e);
        }
    }

    /**
     * 核心方法：基于 byte[] 数据和 byte[] 密钥计算 HMAC-SHA256
     * (兼容 String 和 byte[] 的所有组合调用此基础方法)
     *
     * @param data 要签名的原始字节数组
     * @param key  密钥字节数组
     * @return 小写的 Hex 字符串
     * @since 3.0.16
     */
    public static String hmacSha256(byte[] data, byte[] key) {
        // 直接调用返回 byte[] 的新方法，然后统一转 Hex
        byte[] rawHmac = hmacSha256Bytes(data, key);
        return BaseConvertUtils.hexBytesToString(rawHmac).toLowerCase();
    }

    /**
     * 计算 HMAC-SHA256 签名 (字符串内容 + 字符串密钥)。
     * <p>
     * 这是最常用的重载方法，适用于常规 Web API 签名或文本消息验证。
     * 内部强制使用 UTF-8 编码将字符串转换为字节进行计算。
     *
     * @param text   要签名的文本内容 (StringToSign)
     * @param secret 密钥字符串 (DeviceSecret)
     * @return 加密后的十六进制字符串 (小写)；为了防止空指针异常，如果 text 或 secret 为 null，将返回空字符串 ""
     * @since 3.0.16
     */
    public static String hmacSha256(String text, String secret) {
        if (text == null || secret == null) {
            return ""; // 或者抛出异常，视业务逻辑而定
        }
        return hmacSha256(text.getBytes(StandardCharsets.UTF_8),
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * 计算 HMAC-SHA256 签名 (字节数组内容 + 字符串密钥)。
     * <p>
     * 适用于对二进制数据进行签名的场景，例如：
     * <ul>
     *     <li>Protobuf 序列化后的字节流</li>
     *     <li>文件内容的校验</li>
     *     <li>非文本格式的网络包</li>
     * </ul>
     * 密钥仍然作为普通字符串处理 (UTF-8)。
     *
     * @param contentBytes 要签名的原始内容字节数组
     * @param secret       密钥字符串 (内部转为 UTF-8 字节)
     * @return 加密后的十六进制字符串 (小写)
     * @throws IllegalArgumentException 如果 secret 为 null (鉴于安全要求，密钥通常不允许为空)
     * @since 3.0.16
     */
    public static String hmacSha256(byte[] contentBytes, String secret) {
        if (secret == null) {
            throw new IllegalArgumentException("Secret key cannot be null");
        }
        return hmacSha256(contentBytes, secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 计算 HMAC-SHA256 签名 (字符串内容 + 字节数组密钥)。
     * <p>
     * 适用于密钥本身是二进制数据的场景。例如：密钥存储在数据库中是 Hex 格式或 Base64 格式，
     * 解码成 byte[] 后直接传入此方法，避免二次编码导致密钥不一致。
     *
     * @param text        要签名的文本内容 (内部转为 UTF-8 字节)
     * @param secretBytes 密钥的原始字节数组
     * @return 加密后的十六进制字符串 (小写)；如果 text 为 null，将返回空字符串 ""
     * @since 3.0.16
     */
    public static String hmacSha256(String text, byte[] secretBytes) {
        if (text == null) {
            return "";
        }
        return hmacSha256(text.getBytes(StandardCharsets.UTF_8), secretBytes);
    }
}
