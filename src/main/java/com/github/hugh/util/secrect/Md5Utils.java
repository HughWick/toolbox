package com.github.hugh.util.secrect;

import com.github.hugh.constant.EncryptCode;
import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.EmptyUtils;
import com.github.hugh.util.base.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5 加密工具类
 *
 * @author hugh
 * @version 1.0.0
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
            String result = Base64.toBase64String(output);
            if (lowerCase) {
                return result.toLowerCase();
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            throw new ToolboxException(e);
        }
    }
}
