package com.github.hugh.util.secrect;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * DES加密工具
 *
 * @author hugh
 * @since 1.2.0
 */
public class DesEncDecUtils {
    private DesEncDecUtils() {

    }

    private static DesEncDecUtils ourInstance = new DesEncDecUtils();
    private static Cipher ENCRYPT_CIPHER;
    private static Cipher DECRYPT_CIPHER;
    private static final String CHARSET = "utf-8";
    private static final String DES = "DES";

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
            SecretKeyFactory skf = SecretKeyFactory.getInstance(DES);
            SecretKey desKey = skf.generateSecret(dks);
            ENCRYPT_CIPHER = Cipher.getInstance(DES);
            DECRYPT_CIPHER = Cipher.getInstance(DES);
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
     * @throws Exception
     */
    public String encrypt(String str) throws Exception {
        // Encode the string into bytes using utf-8
        byte[] utf8 = str.getBytes(CHARSET);
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
     * @throws Exception
     */
    public String decrypt(String str) throws Exception {
        // Decode base64 to get bytes
        byte[] dec = java.util.Base64.getDecoder().decode(str);
        byte[] utf8 = DECRYPT_CIPHER.doFinal(dec);
        // Decode using utf-8
        return new String(utf8, CHARSET);
    }

    /**
     * 生成签名
     *
     * @param key   键
     * @param value 值
     * @return String 加密后的字符串
     * @throws Exception
     */
    public static String genCore(String key, String value) throws Exception {
        DesEncDecUtils des = DesEncDecUtils.getInstance(key);
        return des.encrypt(value);
    }

    /**
     * 校验签名是否一致
     *
     * @param key   键
     * @param value 值
     * @param sign  签名
     * @return boolean
     */
    public static boolean check(String key, String value, String sign) throws Exception {
        String core = genCore(key, value);
        return core.equals(sign);
    }
}
