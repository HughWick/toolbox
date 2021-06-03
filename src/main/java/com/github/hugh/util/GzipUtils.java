package com.github.hugh.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
     * 压缩
     *
     * @param str：正常的字符串
     * @return String 压缩字符串 结果：³)°K,NIc i£_`Çe#  c¦%ÂXHòjyIÅÖ`
     * @throws IOException IO异常
     */
    public static String compress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        return out.toString(StandardCharsets.ISO_8859_1.toString());
    }

    /**
     * 解压
     *
     * @param str 已压缩后的字符串
     * @return String
     * @throws IOException IO异常
     */
    public static String uncompress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes(StandardCharsets.ISO_8859_1));
        GZIPInputStream gunzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = gunzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        return out.toString(StandardCharsets.UTF_8.toString());
    }
}
