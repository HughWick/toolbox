package com.github.hugh.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * pako 字符串压缩
 *
 * @author hugh
 */
public class GzipUtils {

    /**
     * 压缩字符编码
     */
    private final static String CHARSET = "ISO-8859-1";

    /**
     * 压缩
     *
     * @param str：正常的字符串
     * @return 压缩字符串 结果：³)°K,NIc i£_`Çe#  c¦%ÂXHòjyIÅÖ`
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
        return out.toString(CHARSET);
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
        ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes(CHARSET));
        GZIPInputStream gunzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = gunzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        return out.toString("UTF-8");
    }

}
