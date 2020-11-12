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
     * hex 数组
     */
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    /**
     * 将字节数组转换成十六进制，并以字符串的形式返回
     * 128位是指二进制位。二进制太长，所以一般都改写成16进制，
     * 每一位16进制数可以代替4位二进制数，所以128位二进制数写成16进制就变成了128/4=32位。
     *
     * @param bytes 字节流
     * @return 字符串
     */
    public static String toBase64String(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(byteToHex(b));
        }
        return sb.toString();
    }

    /**
     * 将一个字节转换成十六进制，并以字符串的形式返回
     *
     * @param b 比特
     */
    private static String byteToHex(byte b) {
        int n = b;
        if (n < 0) {
            n = n + 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        final String charOne = String.valueOf(HEX_ARRAY[d1]);
        final String charTwo = String.valueOf(HEX_ARRAY[d2]);
        return charOne + charTwo;
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
