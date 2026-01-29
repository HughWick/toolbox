package com.github.hugh.util.compress;

import com.github.hugh.exception.ToolboxException;
import lombok.extern.slf4j.Slf4j;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4SafeDecompressor;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * LZ4 压缩工具类
 * <p>
 * 提供基于 LZ4 算法的高速压缩与解压功能。
 * 适用于：
 * 1. 服务端下发大数据包到设备（OTA、配置同步）
 * 2. 设备上报大量日志到服务端
 * </p>
 *
 * @since 3.0.20
 */
@Slf4j
public class Lz4Utils {

    private Lz4Utils() {
    }

    // 获取 LZ4 工厂实例
    // fastestInstance() 会尝试加载 JNI (C代码加速)，如果失败则回退到纯 Java 实现
    private static final LZ4Factory factory = LZ4Factory.fastestInstance();
    /**
     * 默认字符集使用 UTF-8
     */
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * 压缩数据 (Block 模式)
     * 适用于服务端 {@code ->} 设备
     */
    public static byte[] compress(byte[] src) {
        if (src == null || src.length == 0) {
            return new byte[0];
        }
        // 选择压缩器：
        // fastCompressor(): 速度最快，压缩率中等 (适合实时流)
        // highCompressor(): 速度稍慢，压缩率最高 (适合 OTA，因为服务器 CPU 不值钱，带宽值钱)
        LZ4Compressor compressor = factory.highCompressor();
        return compressor.compress(src);
    }

    /**
     * 压缩字符串 (默认 UTF-8)
     *
     * @param src 原始字符串
     * @return 压缩后的字节数组
     */
    public static byte[] compress(String src) {
        return compress(src, DEFAULT_CHARSET);
    }

    /**
     * 压缩字符串，支持指定编码 (如 GBK, GB2312)
     *
     * @param src     原始字符串
     * @param charset 字符集名称 (如 "GBK")
     * @return 压缩后的字节数组
     */
    public static byte[] compress(String src, String charset) {
        return compress(src, Charset.forName(charset));
    }

    /**
     * 压缩字符串核心方法
     */
    public static byte[] compress(String src, Charset charset) {
        if (src == null || src.isEmpty()) {
            return new byte[0];
        }
        return compress(src.getBytes(charset));
    }

    /**
     * 解压数据 (Block 模式)
     *
     * @param compressedData 压缩后的数据
     * @param originalSize   原始数据大小 (LZ4 Block 模式解压必须显式传入原始长度)
     * @return 解压后的原始字节数组
     */
    public static byte[] decompress(byte[] compressedData, int originalSize) {
        if (compressedData == null || compressedData.length == 0 || originalSize <= 0) {
            return new byte[0];
        }
        LZ4SafeDecompressor decompressor = factory.safeDecompressor();
        byte[] restored = new byte[originalSize];
        try {
            decompressor.decompress(compressedData, restored);
        } catch (Exception e) {
            throw new ToolboxException("LZ4 decompression failed", e);
        }
        return restored;
    }

    /**
     * 解压并转回字符串 (默认 UTF-8)
     *
     * @param compressedData 压缩数据
     * @param originalSize   原始大小
     * @return 原始字符串
     */
    public static String decompressToString(byte[] compressedData, int originalSize) {
        return decompressToString(compressedData, originalSize, DEFAULT_CHARSET);
    }

    /**
     * 解压并转回字符串 (支持指定编码 GBK)
     *
     * @param compressedData 压缩数据
     * @param originalSize   原始大小
     * @param charset        字符集
     * @return 原始字符串
     */
    public static String decompressToString(byte[] compressedData, int originalSize, Charset charset) {
        byte[] data = decompress(compressedData, originalSize);
        if (data.length == 0) {
            return "";
        }
        return new String(data, charset);
    }
}
