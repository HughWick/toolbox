package com.github.hugh.util.heatshrink;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
     * 解压 (GD32发来的数据 -> 还原)
     */
    public static byte[] decompress(byte[] compressedData) {
        if (compressedData == null || compressedData.length == 0) {
            return new byte[0];
        }
        try (ByteArrayInputStream bais = new ByteArrayInputStream(compressedData);
             HsInputStream hsIn = new HsInputStream(bais, WINDOW_BITS, LOOKAHEAD_BITS);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = hsIn.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            // 在工具类中，建议捕获异常并转为 RuntimeException 或者打印日志
            throw new java.io.UncheckedIOException("Heatshrink decompression failed", e);
        }
    }

    /**
     * 压缩 (发送给GD32的数据 -> 压缩)
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
}