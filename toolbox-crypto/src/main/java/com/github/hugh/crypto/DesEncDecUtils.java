package com.github.hugh.crypto;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
            e.printStackTrace();
        }
        return ourInstance;
    }

    /**
     * 加密
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
     * @throws BadPaddingException       当输入数据需要特定的填充机制但数据没有正确填充时，抛出此异常
     * @throws IllegalBlockSizeException 使用填充密码解密时，输入长度必须是 16 的倍数
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
     * @throws BadPaddingException       当输入数据需要特定的填充机制但数据没有正确填充时，抛出此异常
     * @throws IllegalBlockSizeException 使用填充密码解密时，输入长度必须是 16 的倍数
     */
    public static boolean check(String key, String value, String sign) throws BadPaddingException, IllegalBlockSizeException {
        String core = genCore(key, value);
        return core.equals(sign);
    }
}
