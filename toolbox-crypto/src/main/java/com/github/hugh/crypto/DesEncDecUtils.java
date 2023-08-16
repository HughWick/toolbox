package com.github.hugh.crypto;

import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.base.BaseConvertUtils;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * DES加密工具
 *
 * @author hugh
 * @since 2.0.1
 */
public class DesEncDecUtils {
    private DesEncDecUtils() {

    }

    private static final DesEncDecUtils ourInstance = new DesEncDecUtils();
    private static Cipher ENCRYPT_CIPHER;
    private static Cipher DECRYPT_CIPHER;

    /**
     * 加密方式：des
     */
    private static final String ENCRYPTION_METHOD_DES = "DES";

    /**
     * 创建实体
     *
     * @param key 加密解密Key
     * @return DesEncDecUtils
     */
    public static DesEncDecUtils getInstance(String key) {
        DESKeySpec dks;
        try {
            // Constants.EncryptDecryptKEY是我一个常量类中的字符串而已，它就是加密解密的密钥。请自行替换。
            dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory skf = SecretKeyFactory.getInstance(ENCRYPTION_METHOD_DES);
            SecretKey desKey = skf.generateSecret(dks);
            ENCRYPT_CIPHER = Cipher.getInstance(ENCRYPTION_METHOD_DES);
            DECRYPT_CIPHER = Cipher.getInstance(ENCRYPTION_METHOD_DES);
            ENCRYPT_CIPHER.init(Cipher.ENCRYPT_MODE, desKey);
            DECRYPT_CIPHER.init(Cipher.DECRYPT_MODE, desKey);
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException e) {
            throw new ToolboxException(e.getMessage());
        }
        return ourInstance;
    }

    /**
     * 采用Base64 加密
     *
     * @param str 字符串
     * @return String 加密后的字符串
     * @throws BadPaddingException       当输入数据需要特定的填充机制但数据没有正确填充时，抛出此异常
     * @throws IllegalBlockSizeException 使用填充密码解密时，输入长度必须是 16 的倍数
     */
    public String encrypt(String str) throws BadPaddingException, IllegalBlockSizeException {
        // Encode the string into bytes using utf-8
        byte[] utf8 = str.getBytes(StandardCharsets.UTF_8);
        // Encrypt
        byte[] enc = ENCRYPT_CIPHER.doFinal(utf8);
        // Encode bytes to base64 to get a string
        return java.util.Base64.getEncoder().encodeToString(enc);
    }

    /**
     * 解密
     *
     * @param str 加密后的字符串
     * @return String  源字符串
     * @throws BadPaddingException       当输入数据需要特定的填充机制但数据没有正确填充时，抛出此异常
     * @throws IllegalBlockSizeException 使用填充密码解密时，输入长度必须是 16 的倍数
     */
    public String decrypt(String str) throws BadPaddingException, IllegalBlockSizeException {
        // Decode base64 to get bytes
        byte[] dec = java.util.Base64.getDecoder().decode(str);
        byte[] utf8 = DECRYPT_CIPHER.doFinal(dec);
        // Decode using utf-8
        return new String(utf8, StandardCharsets.UTF_8);
    }

    /**
     * 生成签名
     *
     * @param key   键
     * @param value 值
     * @return String 加密后的字符串
     */
    public static String genCore(String key, String value) throws BadPaddingException, IllegalBlockSizeException {
        DesEncDecUtils des = DesEncDecUtils.getInstance(key);
        return des.encrypt(value);
    }

    /**
     * 校验签名是否一致
     *
     * @param key   密钥键
     * @param value 未加密的值
     * @param sign  加密后的值
     * @return boolean
     */
    public static boolean check(String key, String value, String sign) throws BadPaddingException, IllegalBlockSizeException {
        String core = genCore(key, value);
        return core.equals(sign);
    }

    /**
     * 用DES对数据进行加密
     * <p>
     * 先加密得到10进制字节数组，再将10进制字节数组转成16进制字节数组，最后将16进制字节数组转成16进制字符串
     * </p>
     *
     * @param data 待加密数据
     * @return String 加密后的数据（十六进制）
     * @since 2.3.9
     */
    public static String encrypt(String data, String cryptKey) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        // 加密后十进制数组
        byte[] encrypt = encrypt(data.getBytes(), cryptKey.getBytes());
        // 十进制数组转十六进制数组
        byte[] hexBytes = BaseConvertUtils.decToHexBytes(encrypt);
        return new String(hexBytes);
    }

    /**
     * 用指定的key对数据进行DES加密.
     *
     * @param data 待加密的数据
     * @param key  DES加密的key
     * @return byte 返回DES加密后的数据
     * @since 2.3.9
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        return operate(data, key, Cipher.ENCRYPT_MODE);
    }

    /**
     * 用DES对数据进行解密
     * <p>
     * 先将16进制字符串对应的字节数组转成10进制字节数组，再将10进制字节数组进行解密，最后将解密得到的字节数组转成10进制字符串
     * </p>
     *
     * @param data 加密数据（十六进制字符串）
     * @return String 解密后的数据
     * @since 2.3.9
     */
    public static String decrypt(String data, String cryptKey) throws Exception {
        // 十六进制字符串转十进制数组
        byte[] bytes = BaseConvertUtils.hexToDec(data.getBytes());
        // 解密
        byte[] decrypt = decrypt(bytes, cryptKey.getBytes());
        return new String(decrypt);
    }

    /**
     * 用指定的key对数据进行DES解密
     *
     * @param data 待解密的数据（十进制数组）
     * @param key  DES解密的key
     * @return byte 解密后的数据
     * @since 2.3.9
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        return operate(data, key, Cipher.DECRYPT_MODE);
    }

    /**
     * 加密/解密操作
     *
     * @param data   待加密/解密数据
     * @param key    加密/解密的key
     * @param opMode 操作模式，{@link Cipher#ENCRYPT_MODE}加密、{@link Cipher#DECRYPT_MODE }解密
     * @return byte 加密/解密的结果
     * @since 2.3.9
     */
    private static byte[] operate(byte[] data, byte[] key, int opMode) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ENCRYPTION_METHOD_DES);
        SecretKey secretKey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成加密/解密操作
        Cipher cipher = Cipher.getInstance(ENCRYPTION_METHOD_DES);
        // 用密钥初始化Cipher对象
        cipher.init(opMode, secretKey, sr);
        // 获取数据并加密/解密
        return cipher.doFinal(data);
    }
}
