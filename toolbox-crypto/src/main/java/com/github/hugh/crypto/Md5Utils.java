package com.github.hugh.crypto;

import com.github.hugh.constant.EncryptCode;
import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.EmptyUtils;
import com.github.hugh.util.base.BaseConvertUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
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
            String result = BaseConvertUtils.bytesToHexString(output);
            if (lowerCase) {
                return result.toLowerCase();
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            throw new ToolboxException(e);
        }
    }

    /**
     * 获取文件的md5值
     *
     * @param file 文件
     * @return String 小写的字符串
     * @since 2.3.4
     */
    public static String encryptFile(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance(EncryptCode.MD5);
            byte[] buffer = new byte[1024 * 1024 * 10];
            int len;
            while ((len = fileInputStream.read(buffer)) > 0) {
                digest.update(buffer, 0, len);
            }
            String md5 = new BigInteger(1, digest.digest()).toString(16);
            int length = 32 - md5.length();
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    md5 = "0" + md5;
                }
            }
            return md5;
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new ToolboxException(e);
        }
    }
}
