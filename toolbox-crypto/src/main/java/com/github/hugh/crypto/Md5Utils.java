package com.github.hugh.crypto;

import com.github.hugh.constant.EncryptCode;
import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.EmptyUtils;
import com.github.hugh.util.base.BaseConvertUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5 加密工具类
 *
 * @author hugh
 * @since 2.0.1
 */
public class Md5Utils {

    /**
     * 获取字符串的 md5 值 小写
     *
     * @param string 字符串
     * @return String  加密后的小写字符串
     */
    public static String lowerCase(final String string) {
        return encrypt(string, true, EncryptCode.MD5);
    }

    /**
     * 获取字符串的 md5 值 大写
     *
     * @param string 字符串
     * @return String  加密后的大写字符串
     */
    public static String upperCase(final String string) {
        return encrypt(string, false, EncryptCode.MD5);
    }

    /**
     * 获取字符串的 md5 值
     *
     * @param string      字符串
     * @param lowerCase   大小写标识：{@code true}小写
     * @param encryptType 加密类型
     * @return String 加密后字符串
     */
    public static String encrypt(final String string, boolean lowerCase, String encryptType) {
        if (EmptyUtils.isEmpty(string)) {
            return string;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(encryptType);
            byte[] output = messageDigest.digest(string.getBytes());
            String result = BaseConvertUtils.hexBytesToString(output);
            if (lowerCase) {
                return result.toLowerCase();
            }
            return result;
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new ToolboxException(noSuchAlgorithmException);
        }
    }

    /**
     * 对给定的字节数组进行MD5加密，并返回加密后的十六进制字符串表示。
     *
     * @param inputBytes 要加密的字节数组
     * @return 加密后的十六进制字符串
     * @since 2.7.10
     */
    public static String encryptBytes(byte[] inputBytes) {
        try {
            MessageDigest md5 = MessageDigest.getInstance(EncryptCode.MD5);
            // 对字节数组进行MD5加密
            byte[] hash = md5.digest(inputBytes);
            // 将加密后的字节数组转换为字符串表示
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : hash) {
                stringBuilder.append(String.format("%02x", b));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new ToolboxException(noSuchAlgorithmException);
        }
    }
}
