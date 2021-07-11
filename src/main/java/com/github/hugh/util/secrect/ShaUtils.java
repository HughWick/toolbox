package com.github.hugh.util.secrect;

import com.github.hugh.constant.EncryptCode;

/**
 * sha 加密工具类
 *
 * @author hugh
 * @since 1.5.7
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
}
