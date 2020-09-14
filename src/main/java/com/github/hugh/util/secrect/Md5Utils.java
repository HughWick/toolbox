package com.github.hugh.util.secrect;

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

    private Md5Utils() {
    }

    /**
     * 获取字符串的 md5 值 小写
     *
     * @param string
     * @return String
     */
    public static String lowerCase(final String string) {
        return generate(string, true);
    }

    /**
     * 获取字符串的 md5 值 大写
     *
     * @param string
     * @return String
     */
    public static String upperCase(final String string) {
        return generate(string, false);
    }

    /**
     * 获取字符串的 md5 值
     *
     * @param string    字符串
     * @param lowerCase 大小写表示,true为小写
     * @return String
     */
    private static String generate(final String string, boolean lowerCase) {
        try {
            if (EmptyUtils.isEmpty(string)) {
                return string;
            }
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] output = messageDigest.digest(string.getBytes());
            String result = Base64.toBase64String(output);
            if (lowerCase) {
                return result.toLowerCase();
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
