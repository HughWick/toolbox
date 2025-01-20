package com.github.hugh.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * pako 字符串压缩
 *
 * @author hugh
 * @since 1.0.4
 */
public class GzipUtils {

    private GzipUtils() {
    }

    /**
     * 压缩指定字符串使用 ISO_8859_1 字符集。
     *
     * @param str 待压缩的字符串
     * @return 压缩后的字符串
     * @throws IOException 如果发生 I/O 错误
     */
    public static String compress(String str) throws IOException {
        return compress(str, StandardCharsets.ISO_8859_1);
    }

    /**
     * 压缩指定字节数组使用 ISO_8859_1 字符集。
     *
     * @param bytes 待压缩的字节数组
     * @return 压缩后的字符串
     * @throws IOException 如果发生 I/O 错误
     * @since 2.7.9
     */
    public static String compress(byte[] bytes) throws IOException {
        return compress(bytes, StandardCharsets.ISO_8859_1);
    }

    /**
     * 压缩指定字符串使用指定的字符集。
     *
     * @param str     待压缩的字符串
     * @param charset 字符集
     * @return 压缩后的字符串
     * @throws IOException 如果发生 I/O 错误
     * @since 2.7.9
     */
    public static String compress(String str, Charset charset) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        return compress(str.getBytes(), charset);
    }

    /**
     * 压缩指定字节数组使用指定的字符集。
     *
     * @param bytes   待压缩的字节数组
     * @param charset 字符集
     * @return 压缩后的字符串
     * @throws IOException 如果发生 I/O 错误
     * @since 2.7.9
     */
    public static String compress(byte[] bytes, Charset charset) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        compressToStream(bytes, out);
        return out.toString(charset);
    }

    /**
     * 将字符串进行压缩，并将压缩后的结果存储到字节数组中。
     *
     * @param str 要压缩的字符串
     * @return 压缩后的字节数组
     * @throws IOException 如果压缩过程中发生 I/O 错误
     * @since 2.7.9
     */
    public static byte[] compressToByteArray(String str) throws IOException {
        return compressToByteArray(str.getBytes());
    }

    /**
     * 将指定字节数组压缩为字节数组。
     *
     * @param bytes 待压缩的字节数组
     * @return 压缩后的字节数组
     * @throws IOException 如果发生 I/O 错误
     * @since 2.7.9
     */
    public static byte[] compressToByteArray(byte[] bytes) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            compressToStream(bytes, out);
            return out.toByteArray();
        }
    }

    /**
     * 将字符串进行压缩，并将压缩后的结果写入到指定的输出流中。
     *
     * @param str 要压缩的字符串
     * @param out 输出流，用于存储压缩后的结果
     * @throws IOException 如果压缩过程中发生 I/O 错误
     * @since 2.7.9
     */
    private static void compressToStream(byte[] str, ByteArrayOutputStream out) throws IOException {
        try (GZIPOutputStream gzip = new GZIPOutputStream(out);) {
            gzip.write(str);
        }
    }

    /**
     * 解压缩使用 ISO_8859_1 字符集编码的字符串。
     *
     * @param str 待解压缩的字符串
     * @return 解压缩后的字符串
     * @throws IOException 如果发生 I/O 错误
     */
    public static String uncompress(String str) throws IOException {
        return uncompress(str, StandardCharsets.ISO_8859_1);
    }

    /**
     * 解压缩使用指定字符集编码的字符串。
     *
     * @param str        待解压缩的字符串
     * @param strCharset 字符串使用的字符集
     * @return 解压缩后的字符串
     * @throws IOException 如果发生 I/O 错误
     * @since 2.7.9
     */
    public static String uncompress(String str, Charset strCharset) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        return uncompress(str.getBytes(strCharset));
    }

    /**
     * 将字节数组进行解压缩，并将解压缩后的字符串返回。
     *
     * @param bytes 要解压缩的字节数组
     * @return 解压缩后的字符串
     * @throws IOException 如果解压缩过程中发生 I/O 错误
     * @since 2.7.9
     */
    public static String uncompress(byte[] bytes) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        uncompressToStream(bytes, out);
        return out.toString(StandardCharsets.UTF_8);
    }


    /**
     * 将使用 ISO_8859_1 字符集编码的字符串解压缩为字节数组。
     *
     * @param str 待解压缩的字符串
     * @return 解压缩后的字节数组
     * @throws IOException 如果发生 I/O 错误
     * @since 2.7.9
     */
    public static byte[] uncompressToByteArray(String str) throws IOException {
        return uncompressToByteArray(str, StandardCharsets.ISO_8859_1);
    }

    /**
     * 将使用指定字符集编码的字符串解压缩为字节数组。
     *
     * @param str        待解压缩的字符串
     * @param strCharset 字符串使用的字符集
     * @return 解压缩后的字节数组
     * @throws IOException 如果发生 I/O 错误
     * @since 2.7.9
     */
    public static byte[] uncompressToByteArray(String str, Charset strCharset) throws IOException {
        return uncompressToByteArray(str.getBytes(strCharset));
    }


    /**
     * 将字节数组进行解压缩，并将解压缩后的结果存储到字节数组中。
     *
     * @param bytes 要解压缩的字节数组
     * @return 解压缩后的字节数组
     * @throws IOException 如果解压缩过程中发生 I/O 错误
     * @since 2.7.9
     */
    public static byte[] uncompressToByteArray(byte[] bytes) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        uncompressToStream(bytes, out);
        return out.toByteArray();
    }

    /**
     * 将字节数组进行解压缩，并将解压缩后的结果写入到指定的输出流中。
     *
     * @param bytes 要解压缩的字节数组
     * @param out   解压缩后的结果将写入的输出流
     * @throws IOException 如果解压缩过程中发生 I/O 错误
     * @since 2.7.9
     */
    private static void uncompressToStream(byte[] bytes, ByteArrayOutputStream out) throws IOException {
        try (ByteArrayInputStream in = new ByteArrayInputStream(bytes);
             GZIPInputStream gunzip = new GZIPInputStream(in)) {
            byte[] buffer = new byte[256];
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        }
    }
}
