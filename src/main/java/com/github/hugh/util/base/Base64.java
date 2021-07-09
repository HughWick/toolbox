package com.github.hugh.util.base;

import com.github.hugh.util.EmptyUtils;

/**
 * base64处理工具
 *
 * @author hugh
 * @version java 1.8
 * @since 1.0.0
 */
public class Base64 {
    private Base64() {
    }

    /**
     * 将字符串转换成base64 编码
     * <p>将字符串转Bytes后,调用{@link #encode(byte[])}</p>
     *
     * @param text 文本
     * @return String 结果
     */
    public static String encode(final String text) {
        if (EmptyUtils.isEmpty(text)) {
            return text;
        }
        return new String(encode(text.getBytes()));
    }

    /**
     * 将原始数据转换为base64的数组
     * <p>使用JDK8自带的{@link java.util.Base64}</p>
     *
     * @param bytes 数据信息
     * @return 结果
     */
    public static byte[] encode(byte[] bytes) {
        return java.util.Base64.getEncoder().encode(bytes);
    }

    /**
     * 将base64编码的数组解码成原始数据
     * <p>使用JDK8自带的{@link java.util.Base64}</p>
     *
     * @param bytes 数据
     * @return byte[] 结果
     */
    public static byte[] decode(byte[] bytes) {
        return java.util.Base64.getDecoder().decode(bytes);
    }

    /**
     * 将原base64的字符串解码
     *
     * @param text 文本
     * @return String 结果
     */
    public static String decode(final String text) {
        if (EmptyUtils.isEmpty(text)) {
            return text;
        }
        return new String(decode(text.getBytes()));
    }
}
