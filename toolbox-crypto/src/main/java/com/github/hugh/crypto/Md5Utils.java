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

    private Md5Utils(){}

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
     * 获取字节数组的 md5 值 小写
     *
     * @param bytes 字节数组
     * @return String 加密后的小写字符串
     * @since 3.0.18
     */
    public static String lowerCase(final byte[] bytes) {
        return encrypt(bytes, true, EncryptCode.MD5);
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
     * 接收 String 的通用加密方法
     * 原逻辑保留，将核心实现委托给 byte[] 版本
     */
    public static String encrypt(final String string, boolean lowerCase, String encryptType) {
        if (EmptyUtils.isEmpty(string)) {
            return string;
        }
        // 使用默认编码获取字节，保持与原逻辑一致
        return encrypt(string.getBytes(), lowerCase, encryptType);
    }

    /**
     * 核心通用加密方法，接收 byte[]
     *
     * @param data        要加密的数据
     * @param lowerCase   大小写标识：{@code true}小写
     * @param encryptType 加密类型 (MD5, SHA-256, SHA-512 等)
     * @return String 加密后字符串
     */
    public static String encrypt(final byte[] data, boolean lowerCase, String encryptType) {
        if (data == null || data.length == 0) {
            return null; // 或者返回 ""，根据你的业务需求定
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(encryptType);
            byte[] output = messageDigest.digest(data);
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
     * 现有的针对 byte[] 的 MD5 加密 (保留或标记过时)
     * 建议：既然有了通用的 encrypt(byte[]...), 这个方法可以保留以兼容旧代码，
     * 或者重构为调用 encrypt(inputBytes, true, EncryptCode.MD5)
     */
    public static String encryptBytes(byte[] inputBytes) {
        return lowerCase(inputBytes);
    }
}
