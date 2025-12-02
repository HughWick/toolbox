package com.github.hugh.util.heatshrink;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Heatshrink 压缩算法工具类
 * <p>
 * 该类主要用于解压来自嵌入式设备（如 GD32）传输的 Heatshrink 压缩数据。
 * Heatshrink 是一种基于 LZSS 的数据压缩算法，特别适用于内存极小的嵌入式系统。
 * </p>
 *
 * <p><b>注意事项：</b></p>
 * <p>
 * 此类的配置参数（{@link #WINDOW_BITS} 和 {@link #LOOKAHEAD_BITS}）必须与
 * 嵌入式 C 语言端（{@code heatshrink_config.h}）中的宏定义完全保持一致，
 * 否则会导致解压失败或数据乱码。
 * </p>
 *
 * @author HughWick
 * @see <a href="https://github.com/atomicobject/heatshrink">Heatshrink C Library</a>
 * @since 3.0.16
 */
public class HeatshrinkUtils {

    private HeatshrinkUtils() {
    }

    // ==========================================
    // 配置区：必须与嵌入式(GD32)的 heatshrink_config.h 完全一致
    // ==========================================
    // C语言端: #define HEATSHRINK_STATIC_WINDOW_BITS 8
    private static final int WINDOW_BITS = 8;

    // C语言端: #define HEATSHRINK_STATIC_LOOKAHEAD_BITS 4
    private static final int LOOKAHEAD_BITS = 4;

    /**
     * 解压 (将接收到的压缩数据还原)
     */
    public static byte[] decompress(byte[] compressedData) {
        if (compressedData == null || compressedData.length == 0) {
            return new byte[0];
        }
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressedData);
             HsInputStream hsIn = new HsInputStream(byteArrayInputStream, WINDOW_BITS, LOOKAHEAD_BITS);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = hsIn.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            // 在工具类中，建议捕获异常并转为 RuntimeException 或者打印日志
            throw new java.io.UncheckedIOException("Heatshrink decompression failed", e);
        }
    }

    /**
     * 压缩 (将待发送的数据压缩)
     */
    public static byte[] compress(byte[] rawData) {
        if (rawData == null || rawData.length == 0) {
            return new byte[0];
        }
        // 1. 把 baos 定义在外面
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 2. 在 try 中只负责操作压缩流
        try (HsOutputStream hsOut = new HsOutputStream(baos, WINDOW_BITS, LOOKAHEAD_BITS)) {
            hsOut.write(rawData);
            // try 块结束时，会自动调用 hsOut.close()
            // hsOut.close() 会触发 flushOutputBuffer(true)，从而把数据写入 baos
        } catch (IOException e) {
            throw new java.io.UncheckedIOException("Heatshrink compress failed", e);
        }
        // 3. 此时流已经关闭且刷新，baos 里才有数据
        return baos.toByteArray();
    }

    /**
     * 将字符串压缩为字节数组
     *
     * @param content 原始字符串 (默认使用 UTF-8 编码)
     * @return Heatshrink 压缩后的二进制数据
     */
    public static byte[] compress(String content) {
        if (content == null || content.isEmpty()) {
            return new byte[0];
        }
        return compress(content.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 将压缩的字节数组还原为字符串
     *
     * @param compressedData 压缩后的二进制数据
     * @return 还原后的字符串 (默认使用 UTF-8 解码)
     */
    public static String decompressToString(byte[] compressedData) {
        byte[] decompressedBytes = decompress(compressedData);
        if (decompressedBytes.length == 0) {
            return "";
        }
        return new String(decompressedBytes, StandardCharsets.UTF_8);
    }

    /**
     * 压缩并编码: 原始字符串转换为 Base64 字符串
     * <p>
     * 流程: String 转 UTF-8 bytes 转 Heatshrink 压缩 转 Base64 编码 转 String
     * </p>
     *
     * @param content 原始字符串
     * @return 压缩并 Base64 编码后的字符串 (可见字符，适合 JSON 传输)
     */
    public static String compressToEncodedString(String content) {
        byte[] compressedBytes = compress(content);
        if (compressedBytes.length == 0) {
            return "";
        }
        return Base64.getEncoder().encodeToString(compressedBytes);
    }

    /**
     * 解码并解压: 将 Base64 字符串还原为原始字符串
     * <p>
     * 流程: Base64 String 转 解码为二进制 转 Heatshrink 解压 转 UTF-8 String
     * </p>
     *
     * @param base64Content 经过 Base64 编码的压缩字符串
     * @return 还原后的原始字符串
     */
    public static String decompressFromEncodedString(String base64Content) {
        if (base64Content == null || base64Content.isEmpty()) {
            return "";
        }
        try {
            byte[] compressedBytes = Base64.getDecoder().decode(base64Content);
            return decompressToString(compressedBytes);
        } catch (IllegalArgumentException e) {
            // 处理 Base64 格式错误
            throw new IllegalStateException("Input is not a valid Base64 string", e);
        }
    }
}