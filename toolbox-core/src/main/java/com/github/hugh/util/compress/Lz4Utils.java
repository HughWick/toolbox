package com.github.hugh.util.compress;

import lombok.extern.slf4j.Slf4j;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4SafeDecompressor;

@Slf4j
public class Lz4Utils {

    private Lz4Utils() {
    }

    // 获取 LZ4 工厂实例
    // fastestInstance() 会尝试加载 JNI (C代码加速)，如果失败则回退到纯 Java 实现
    private static final LZ4Factory factory = LZ4Factory.fastestInstance();

    /**
     * 压缩数据 (Block 模式)
     * 适用于服务端 -> 设备
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
     * 解压数据 (Block 模式)
     * 适用于 设备 -> 服务端 (如果有日志上报需求)
     *
     * @param compressedData 压缩后的数据
     * @param originalSize   原始数据大小 (LZ4 Block解压必须知道原始大小！)
     */
    public static byte[] decompress(byte[] compressedData, int originalSize) {
        if (compressedData == null || compressedData.length == 0) {
            return new byte[0];
        }
        LZ4SafeDecompressor decompressor = factory.safeDecompressor();
        byte[] restored = new byte[originalSize];
        // 解压到 dest 数组
        decompressor.decompress(compressedData, restored);
        return restored;
    }
}
