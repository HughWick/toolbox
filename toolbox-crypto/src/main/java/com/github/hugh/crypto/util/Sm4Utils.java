package com.github.hugh.crypto.util;

import com.github.hugh.crypto.emus.Sm4Enum;
import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.base.BaseConvertUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;

/**
 * SM4算法
 *
 * @author hugh
 * @since 2.6.1
 */

public class Sm4Utils {

    private Sm4Utils() {
    }

    static {
        //加入BouncyCastleProvider的支持 BouncyCastle->开源密码包，扩充密码算法支持
        Security.addProvider(new BouncyCastleProvider());
    }

    //密钥长度
    public static final int DEFAULT_KEY_SIZE = 128;

    /**
     * 获取密钥
     *
     * @return 密钥
     */
    public static byte[] generateKey() {
        try {
            return generateKey(DEFAULT_KEY_SIZE);
        } catch (Exception exception) {
            throw new ToolboxException(exception.getMessage());
        }
    }

    /**
     * 获取指定长度密钥
     *
     * @param keySize 密钥的长度
     * @return 密钥
     */
    public static byte[] generateKey(int keySize) {
        try {
            KeyGenerator kg = KeyGenerator.getInstance(Sm4Enum.ALGORITHM_NAME.getCode(), BouncyCastleProvider.PROVIDER_NAME);
            kg.init(keySize, new SecureRandom());
            return kg.generateKey().getEncoded();
        } catch (Exception exception) {
            throw new ToolboxException(exception.getMessage());
        }
    }

    /**
     * ECB P5填充加密
     * <p>
     * 优点:简单，利于并行计算，误差不会被传递
     * </p>
     * <p>
     * 缺点：加密模式易被确定
     * </p>
     *
     * @param hexKey 十六进制密钥，用于加密。
     * @param data   明文数据
     * @return 加密结果
     */
    public static byte[] encryptEcbPadding(String hexKey, String data) {
        try {
            Cipher cipher = generateEcbCipher(Cipher.ENCRYPT_MODE, BaseConvertUtils.hexToBytes(hexKey));
            return cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception exception) {
            throw new ToolboxException(exception.getMessage());
        }
    }

    /**
     * 使用ECB模式进行填充加密
     *
     * @param bytes 加密密钥的字节数组
     * @param data  需要加密的数据
     * @return 加密后的结果字节数组
     * @throws ToolboxException 如果发生加密过程中的异常，将抛出自定义的ToolboxException异常
     */
    public static byte[] encryptEcbPadding(byte[] bytes, String data) {
        try {
            Cipher cipher = generateEcbCipher(Cipher.ENCRYPT_MODE, bytes);
            return cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception exception) {
            throw new ToolboxException(exception);
        }
    }

    /**
     * 使用 ECB 模式进行填充的加密，并将结果转换为十六进制字符串形式。
     *
     * @param hexKey 加密所需的十六进制密钥
     * @param data   要加密的数据
     * @return 加密后的数据以十六进制字符串形式返回
     */
    public static String encryptEcbPaddingToHex(String hexKey, String data) {
        return BaseConvertUtils.hexBytesToString(encryptEcbPadding(hexKey, data));
    }

    /**
     * ECB P5填充解密
     *
     * @param hexKey     十六进制密钥，用于解密。
     * @param cipherText 加密后的数据
     * @return 解密结果
     */
    public static String decryptEcbPadding(String hexKey, String cipherText) {
        return decryptEcbPadding(BaseConvertUtils.hexToBytes(hexKey), BaseConvertUtils.hexToBytes(cipherText));
    }

    /**
     * 使用 ECB 模式解密填充的密文，并返回解密后的字符串。
     *
     * @param keyBytes   密钥字节数组
     * @param cipherText 填充的密文
     * @return 解密后的字符串
     */
    public static String decryptEcbPadding(byte[] keyBytes, byte[] cipherText) {
        byte[] decryptBytes; // 存储解密后的字节数组
        Cipher cipher; // 加密算法对象
        try {
            // 使用ECB模式生成Cipher对象，指定为解密模式
            cipher = generateEcbCipher(Cipher.DECRYPT_MODE, keyBytes);
            // 解密密文并获得解密后的字节数组
            decryptBytes = cipher.doFinal(cipherText);
        } catch (BadPaddingException e) {
            if ("pad block corrupted".equals(e.getMessage())) {
                // 如果填充块损坏，使用无填充的ECB模式生成Cipher对象，并进行解密
                cipher = generateEcbCipher(Sm4Enum.ALGORITHM_NAME_ECB_NO_PADDING.getCode(), Cipher.DECRYPT_MODE, keyBytes);
                try {
                    decryptBytes = cipher.doFinal(cipherText); // 解密密文并获得解密后的字节数组
                } catch (IllegalBlockSizeException ex) {
                    throw new ToolboxException("解密失败：无效的块大小", ex);
                } catch (BadPaddingException ex) {
                    throw new ToolboxException("解密失败：填充异常", ex);
                }
            } else {
                throw new ToolboxException("解密失败：填充异常", e);
            }
        } catch (IllegalBlockSizeException e) {
            throw new ToolboxException("解密失败：无效的块大小", e);
        } catch (Exception e) {
            throw new ToolboxException("解密过程中出现错误", e);
        }
        return new String(decryptBytes); // 将解密后的字节数组转换为字符串并返回
    }

    /**
     * 使用 CBC 模式进行填充的加密，并将结果转换为十六进制字符串形式。
     *
     * @param hexKey 加密所需的十六进制密钥
     * @param iv     初始化向量（Initialization Vector）, 以十六进制字符串形式表示
     * @param data   要加密的数据
     * @return 加密后的数据以十六进制字符串形式返回
     */
    public static String encryptCbcPadding(String hexKey, String iv, String data) {
        byte[] bytes = encryptCbcPadding(BaseConvertUtils.hexToBytes(hexKey), BaseConvertUtils.hexToBytes(iv), data);
        return BaseConvertUtils.hexBytesToString(bytes);
    }

    /**
     * CBC P5填充加密
     * <p>
     * 优点：安全性高
     * </p>
     * <p>
     * 缺点：不利于并行计算，误差传递，需要初始化向量iv
     * </p>
     *
     * @param bytesKey 十六进制密钥，用于加密。
     * @param bytesIv  偏移量，CBC每轮迭代会和上轮结果进行异或操作，由于首轮没有可进行异或的结果，
     *                 所以需要设置偏移量，一般用密钥做偏移量
     * @param data     明文数据
     * @return 加密结果
     */
    public static byte[] encryptCbcPadding(byte[] bytesKey, byte[] bytesIv, String data) {
        try {
            Cipher cipher = generateCbcCipher(Cipher.ENCRYPT_MODE, bytesKey, bytesIv);
            return cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception exception) {
            throw new ToolboxException(exception.getMessage());
        }
    }

    /**
     * CBC P5填充解密
     *
     * @param hexKey     十六进制密钥，用于解密。
     * @param iv         偏移量，CBC每轮迭代会和上轮结果进行异或操作，由于首轮没有可进行异或的结果，
     *                   所以需要设置偏移量，一般用密钥做偏移量
     * @param cipherText 加密数据
     * @return 解密结果
     */
    public static String decryptCbcPadding(String hexKey, String iv, String cipherText) {
        return decryptCbcPadding(BaseConvertUtils.hexToBytes(hexKey), BaseConvertUtils.hexToBytes(iv), BaseConvertUtils.hexToBytes(cipherText));
    }

    /**
     * 使用CBC模式和Padding进行解密
     *
     * @param keyBytes   密钥字节数组
     * @param iv         初始化向量字节数组
     * @param cipherText 待解密的密文
     * @return 解密后的明文字符串
     * @throws ToolboxException 解密异常时抛出该异常
     */
    public static String decryptCbcPadding(byte[] keyBytes, byte[] iv, byte[] cipherText) {
        try {
            // 生成使用CBC模式的Cipher对象
            Cipher cipher = generateCbcCipher(Cipher.DECRYPT_MODE, keyBytes, iv);
            // 将密文转换为字节数组并执行解密操作
            byte[] decryptBytes = cipher.doFinal(cipherText);
            // 将解密后的字节数组转换为字符串返回
            return new String(decryptBytes);
        } catch (Exception exception) {
            throw new ToolboxException(exception.getMessage());
        }
    }

    /**
     * ECB P5填充加解密Cipher初始化
     *
     * @param mode 1 加密  2解密
     * @param key  密钥
     * @return Cipher
     */
    private static Cipher generateEcbCipher(int mode, byte[] key) {
        return generateEcbCipher(Sm4Enum.ALGORITHM_NAME_ECB_PKCS5_PADDING.getCode(), mode, key);
    }

    /**
     * 根据给定的转换模式、操作模式和密钥生成使用 ECB 模式的 Cipher 对象。
     *
     * @param transformation 转换算法名称，例如 "SM4/ECB/PKCS5Padding"
     * @param mode           Cipher 的操作模式，如 Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
     * @param key            密钥字节数组
     * @return 使用 ECB 模式的 Cipher 对象
     */
    private static Cipher generateEcbCipher(String transformation, int mode, byte[] key) {
        try {
            Cipher cipher = Cipher.getInstance(transformation, BouncyCastleProvider.PROVIDER_NAME);
            Key sm4Key = new SecretKeySpec(key, Sm4Enum.ALGORITHM_NAME.getCode());
            cipher.init(mode, sm4Key);
            return cipher;
        } catch (Exception exception) {
            throw new ToolboxException(exception.getMessage());
        }
    }

    /**
     * CBC P5填充加解密Cipher初始化
     *
     * @param mode 1 加密  2解密
     * @param key  密钥
     * @param iv   偏移量，CBC每轮迭代会和上轮结果进行异或操作，由于首轮没有可进行异或的结果，
     *             所以需要设置偏移量，一般用密钥做偏移量
     * @return Cipher
     */
    private static Cipher generateCbcCipher(int mode, byte[] key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance(Sm4Enum.ALGORITHM_NAME_CBC_PADDING.getCode(), BouncyCastleProvider.PROVIDER_NAME);
            Key sm4Key = new SecretKeySpec(key, Sm4Enum.ALGORITHM_NAME.getCode());
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(mode, sm4Key, ivParameterSpec);
            return cipher;
        } catch (Exception exception) {
            throw new ToolboxException(exception.getMessage());
        }
    }

//    /**
//     * 验证解密后的ECB填充数据是否与原始明文匹配。
//     *
//     * @param hexKey     十六进制密钥，用于解密。
//     * @param cipherText 加密后的密文。
//     * @param plainText  原始明文。
//     * @return 如果解密后的数据与原始明文匹配，则返回True；否则返回False。
//     */
//    public static boolean verifyEcb(String hexKey, String cipherText, String plainText) {
//        String decryptedText = decryptEcbPadding(hexKey, cipherText);
//        byte[] decryptData = decryptedText.getBytes(StandardCharsets.UTF_8);
//        byte[] srcData = plainText.getBytes(StandardCharsets.UTF_8);
//        return Arrays.equals(decryptData, srcData);
//    }
//
//    /**
//     * 验证解密后的CBC填充数据是否与原始明文匹配。
//     *
//     * @param hexKey     十六进制密钥，用于解密。
//     * @param cipherText 加密的密文。
//     * @param iv         初始化向量（IV）。
//     * @param plainText  与解密数据进行比较的原始明文。
//     * @return 若解密数据与原始明文匹配，则返回True；否则返回False。
//     */
//    public static boolean verifyCbc(String hexKey, String cipherText, String iv, String plainText) {
//        // 使用给定的密钥和IV解密CBC填充的密文
//        String decryptedCbcText = decryptCbcPadding(hexKey, iv, cipherText);
//        // 将解密后的文本转换成字节数组
//        byte[] decryptData = decryptedCbcText.getBytes(StandardCharsets.UTF_8);
//        // 将原始明文转换成字节数组
//        byte[] srcData = plainText.getBytes(StandardCharsets.UTF_8);
//        // 比较解密数据和原始明文的字节数组是否相等
//        return Arrays.equals(decryptData, srcData);
//    }
}
