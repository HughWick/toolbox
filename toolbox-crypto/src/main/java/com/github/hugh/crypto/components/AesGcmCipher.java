package com.github.hugh.crypto.components;

import lombok.Getter;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 专用于 AES/GCM/NoPadding 模式的加密解密核心类。
 * <p>
 * AES/GCM 是一种带有关联数据的认证加密 (AEAD) 模式，它能同时提供机密性、完整性和真实性。
 * 特点：
 * 1.  **安全性高**: GCM 模式被广泛认为是目前最安全的对称加密模式之一。
 * 2.  **无需 Padding**: GCM 是流加密模式，不需要对明文进行填充。
 * 3.  **需要唯一的 IV**: 每次加密【绝不能】使用相同的密钥和 IV 组合，否则会严重破坏安全性。本类库在每次加密时都会生成一个安全的随机 IV。
 * </p>
 *
 * @since 3.0.15
 */
public class AesGcmCipher {

    // --- 算法常量 ---
    private static final String ALGORITHM_AES = "AES";
    private static final String TRANSFORMATION_AES_GCM = "AES/GCM/NoPadding";
    // --- GCM 模式参数常量 ---
    private static final int GCM_IV_LENGTH_BYTES = 12;    // 96 bits
    private static final int GCM_TAG_LENGTH_BITS = 128;   // 128 bits
    private final SecretKey secretKey;

    // 私有构造方法，通过工厂方法创建
    private AesGcmCipher(String key) throws InvalidKeyException {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty.");
        }
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length != 16 && keyBytes.length != 24 && keyBytes.length != 32) {
            throw new InvalidKeyException("Invalid AES key length: " + keyBytes.length + " bytes. Must be 16, 24, or 32 bytes.");
        }
        this.secretKey = new SecretKeySpec(keyBytes, ALGORITHM_AES);
    }

    /**
     * 工厂方法，获取一个 GcmCryptoCore 实例。
     *
     * @param key 用于 AES 加密的密钥字符串 (UTF-8 编码, 长度必须是 16, 24, 或 32)。
     * @return 配置好的 GcmCryptoCore 实例。
     * @throws ToolboxException 如果密钥无效。
     */
    public static AesGcmCipher getInstance(String key) {
        try {
            return new AesGcmCipher(key);
        } catch (InvalidKeyException | IllegalArgumentException e) {
            throw new ToolboxException("初始化 GCM 实例失败: " + e.getMessage(), e);
        }
    }

    /**
     * 加密一个明文字符串。
     *
     * @param plaintext 要加密的 UTF-8 编码的明文字符串。
     * @return 包含 IV 和密文的 GcmEncryptedPacket 对象。
     */
    public GcmEncryptedPacket encrypt(String plaintext) {
        if (plaintext == null) return null;
        return encrypt(plaintext.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 加密一个明文字节数组。这是核心的加密方法。
     * 每次调用都会生成一个全新的、密码学安全的随机 IV。
     *
     * @param plainBytes 要加密的明文字节数组。
     * @return 包含 IV 和密文的 GcmEncryptedPacket 对象。
     */
    public GcmEncryptedPacket encrypt(byte[] plainBytes) {
        if (plainBytes == null) return null;
        try {
            // GCM 模式强烈要求每次加密使用不同的 IV
            byte[] iv = new byte[GCM_IV_LENGTH_BYTES];
            new SecureRandom().nextBytes(iv);
            Cipher gcmCipher = Cipher.getInstance(TRANSFORMATION_AES_GCM);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv);
            gcmCipher.init(Cipher.ENCRYPT_MODE, this.secretKey, gcmSpec);
            byte[] ciphertext = gcmCipher.doFinal(plainBytes);
            return new GcmEncryptedPacket(iv, ciphertext);
        } catch (Exception e) {
            throw new ToolboxException("AES/GCM encryption failed: " + e.getMessage(), e);
        }
    }

    /**
     * 解密并返回原始字节数组。这是核心的解密方法。
     *
     * @param ciphertext 密文字节数组 (包含了 GCM 的认证标签)。
     * @param iv         用于加密的 IV 字节数组。
     * @return 解密后的明文字节数组。
     * @throws ToolboxException 如果解密失败 (例如，数据被篡改、密钥或IV不匹配)。
     */
    public byte[] decryptToBytes(byte[] ciphertext, byte[] iv) {
        if (ciphertext == null || iv == null) return null;
        try {
            Cipher gcmCipher = Cipher.getInstance(TRANSFORMATION_AES_GCM);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv);
            gcmCipher.init(Cipher.DECRYPT_MODE, this.secretKey, gcmSpec);
            return gcmCipher.doFinal(ciphertext);
        } catch (Exception e) {
            throw new ToolboxException("AES/GCM decryption failed (data may be tampered or key/IV is incorrect): " + e.getMessage(), e);
        }
    }

    /**
     * 从 Base64 编码的密文和 IV 解密，返回字节数组。
     */
    public byte[] decryptToBytes(String ciphertextBase64, String ivBase64) {
        if (ciphertextBase64 == null || ivBase64 == null) return null;
        byte[] iv = Base64.getDecoder().decode(ivBase64);
        byte[] ciphertext = Base64.getDecoder().decode(ciphertextBase64);
        return decryptToBytes(ciphertext, iv);
    }

    /**
     * 从一个合并了 [IV] + [Ciphertext] 的字节数组中解密，返回字节数组。
     */
    public byte[] decryptToBytes(byte[] combinedBytes) {
        if (combinedBytes == null) return null;
        if (combinedBytes.length <= GCM_IV_LENGTH_BYTES) {
            throw new IllegalArgumentException("Invalid GCM encrypted packet length.");
        }
        ByteBuffer bb = ByteBuffer.wrap(combinedBytes);
        byte[] iv = new byte[GCM_IV_LENGTH_BYTES];
        bb.get(iv);
        byte[] ciphertext = new byte[bb.remaining()];
        bb.get(ciphertext);
        return decryptToBytes(ciphertext, iv);
    }

    /**
     * 从一个合并了 IV 和密文的 Base64 字符串中解密，返回字节数组。
     */
    public byte[] decryptToBytes(String combinedBase64) {
        if (combinedBase64 == null) return null;
        return decryptToBytes(Base64.getDecoder().decode(combinedBase64));
    }

    /**
     * 从 GcmEncryptedPacket 对象中解密，返回字节数组。
     */
    public byte[] decryptToBytes(GcmEncryptedPacket packet) {
        if (packet == null) return null;
        return decryptToBytes(packet.getCiphertext(), packet.getIv());
    }

    /**
     * 解密并返回 UTF-8 编码的字符串。
     * @see #decryptToBytes(byte[], byte[])
     */
    public String decryptToString(byte[] ciphertext, byte[] iv) {
        byte[] decryptedBytes = decryptToBytes(ciphertext, iv);
        return decryptedBytes != null ? new String(decryptedBytes, StandardCharsets.UTF_8) : null;
    }

    /**
     * 解密并返回 UTF-8 编码的字符串。
     * @see #decryptToBytes(String, String)
     */
    public String decryptToString(String ciphertextBase64, String ivBase64) {
        byte[] decryptedBytes = decryptToBytes(ciphertextBase64, ivBase64);
        return decryptedBytes != null ? new String(decryptedBytes, StandardCharsets.UTF_8) : null;
    }

    /**
     * 解密并返回 UTF-8 编码的字符串。
     * @see #decryptToBytes(byte[])
     */
    public String decryptToString(byte[] combinedBytes) {
        byte[] decryptedBytes = decryptToBytes(combinedBytes);
        return decryptedBytes != null ? new String(decryptedBytes, StandardCharsets.UTF_8) : null;
    }

    /**
     * 解密并返回 UTF-8 编码的字符串。
     * @see #decryptToBytes(String)
     */
    public String decryptToString(String combinedBase64) {
        byte[] decryptedBytes = decryptToBytes(combinedBase64);
        return decryptedBytes != null ? new String(decryptedBytes, StandardCharsets.UTF_8) : null;
    }

    /**
     * 解密并返回 UTF-8 编码的字符串。
     * @see #decryptToBytes(GcmEncryptedPacket)
     */
    public String decryptToString(GcmEncryptedPacket packet) {
        byte[] decryptedBytes = decryptToBytes(packet);
        return decryptedBytes != null ? new String(decryptedBytes, StandardCharsets.UTF_8) : null;
    }

    /**
     * 内部数据包类
     * 封装 AES/GCM 加密结果的数据包。
     * 这是一个不可变对象，以确保数据完整性。
     */
    @Getter
    public static final class GcmEncryptedPacket {
        private final byte[] iv;
        private final byte[] ciphertext;

        private GcmEncryptedPacket(byte[] iv, byte[] ciphertext) {
            this.iv = iv;
            this.ciphertext = ciphertext;
        }

        public static GcmEncryptedPacket fromCombinedBytes(byte[] combined) {
            if (combined == null || combined.length <= GCM_IV_LENGTH_BYTES) {
                throw new IllegalArgumentException("Invalid combined byte array.");
            }
            ByteBuffer bb = ByteBuffer.wrap(combined);
            byte[] iv = new byte[GCM_IV_LENGTH_BYTES];
            bb.get(iv);
            byte[] ciphertext = new byte[bb.remaining()];
            bb.get(ciphertext);
            return new GcmEncryptedPacket(iv, ciphertext);
        }

        public static GcmEncryptedPacket fromCombinedBase64(String combinedBase64) {
            return fromCombinedBytes(Base64.getDecoder().decode(combinedBase64));
        }
        public String getIvAsBase64() {
            return Base64.getEncoder().encodeToString(iv);
        }

        public String getCiphertextAsBase64() {
            return Base64.getEncoder().encodeToString(ciphertext);
        }
        public byte[] toCombinedBytes() {
            return ByteBuffer.allocate(iv.length + ciphertext.length)
                    .put(iv)
                    .put(ciphertext)
                    .array();
        }
        public String toCombinedBase64() {
            return Base64.getEncoder().encodeToString(toCombinedBytes());
        }
    }

    /**
     * 自定义运行时异常，用于封装所有加密相关的受检异常。
     */
    public static class ToolboxException extends RuntimeException {
        public ToolboxException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}