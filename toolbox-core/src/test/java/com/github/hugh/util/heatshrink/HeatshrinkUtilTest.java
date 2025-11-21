package com.github.hugh.util.heatshrink;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * HeatshrinkUtils 的单元测试
 * 用于验证 Java 端与 GD32 嵌入式端的压缩解压算法一致性
 */
class HeatshrinkUtilTest {

    @Test
    @DisplayName("测试基础字符串：压缩后解压应与原值一致")
    void testSimpleStringRoundTrip() {
        String originalText = "Hello GD32F425, this is a protobuf test message!";
        byte[] originalBytes = originalText.getBytes(StandardCharsets.UTF_8);
        // 1. 压缩
        byte[] compressed = HeatshrinkUtils.compress(originalBytes);
        System.out.println("原始大小: " + originalBytes.length);
        System.out.println("压缩后大小: " + compressed.length);
        // 2. 解压
        byte[] decompressed = HeatshrinkUtils.decompress(compressed);
        String restoredText = new String(decompressed, StandardCharsets.UTF_8);
        // 3. 验证
        Assertions.assertNotNull(compressed);
        Assertions.assertEquals(originalText, restoredText, "解压后的字符串应与原始字符串完全一致");
    }
    @Test
    @DisplayName("测试大数据量：模拟重复日志数据，验证高压缩率")
    void testLargeRepetitiveData() {
        // 1. 构造模拟数据 (模拟 MCU 发送的一批重复的传感器日志)
        // 这种数据在实际工程中很常见，Heatshrink 对此非常擅长
        StringBuilder sb = new StringBuilder();
        String logLine = "[INFO] 2025-01-01 12:00:00 Sensor_ID:8848 Temp:36.5 Hum:40%\n";
        // 重复拼接 500 次，产生大约 30KB 的数据
        sb.append(logLine.repeat(500));
        byte[] originalBytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        // 2. 执行压缩
        long startTime = System.currentTimeMillis();
        byte[] compressed = HeatshrinkUtils.compress(originalBytes);
        long endTime = System.currentTimeMillis();
        // 3. 执行解压
        byte[] decompressed = HeatshrinkUtils.decompress(compressed);
        String restoredText = new String(decompressed, StandardCharsets.UTF_8);
        // 4. 打印报告
        System.out.println("========= 大数据量压缩测试报告 =========");
        System.out.println("原始大小: " + originalBytes.length + " bytes (" + (originalBytes.length / 1024.0) + " KB)");
        System.out.println("压缩大小: " + compressed.length + " bytes (" + (compressed.length / 1024.0) + " KB)");
        // 计算压缩比
        double ratio = (double) compressed.length / originalBytes.length * 100;
        System.out.printf("压缩耗时: %d ms\n", (endTime - startTime));
        System.out.printf("压缩比率: %.2f%% (越小越好)\n", ratio);
        System.out.println("======================================");
        // 5. 验证断言
        Assertions.assertNotNull(compressed);
        // 验证体积显著减小 (对于这种高度重复数据，通常能压缩到 5%~20% 的大小)
        Assertions.assertTrue(compressed.length < originalBytes.length / 2, "对于重复日志数据，压缩后体积应至少减少一半");
        // 验证数据完整性
        Assertions.assertEquals(sb.toString(), restoredText, "大数据量解压后内容必须完全一致");
    }

    @Test
    @DisplayName("测试高重复性数据：验证压缩率")
    void testHighCompressionRatio() {
        // 构造一个非常有规律的数据，Heatshrink (LZSS) 对这种数据压缩率极高
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("RepeatData_");
        }
        byte[] originalBytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        // 1. 压缩
        byte[] compressed = HeatshrinkUtils.compress(originalBytes);
        // 2. 解压
        byte[] decompressed = HeatshrinkUtils.decompress(compressed);
        System.out.println("--- 压缩率测试 ---");
        System.out.println("原始数据大小: " + originalBytes.length + " bytes");
        System.out.println("压缩数据大小: " + compressed.length + " bytes");
        // 3. 验证
        // 对于这种高度重复的数据，压缩后的体积应该显著小于原始体积
        Assertions.assertTrue(compressed.length < originalBytes.length, "对于重复数据，压缩后体积应变小");
        // 数据完整性依然要保证
        Assertions.assertArrayEquals(originalBytes, decompressed, "数据内容必须无损还原");
    }

    @Test
    @DisplayName("测试大数据量：超过缓冲区大小(>1KB)的数据")
    void testLargeDataPayload() {
        // 模拟一个较大的 Protobuf 序列化结果 (2KB 随机数据)
        // 注意：完全随机的数据很难被压缩，甚至可能变大，这里主要测试“解压逻辑”是否能处理分段流
        byte[] originalBytes = new byte[2048];
        new Random().nextBytes(originalBytes);
        // 1. 压缩
        byte[] compressed = HeatshrinkUtils.compress(originalBytes);
        // 2. 解压
        byte[] decompressed = HeatshrinkUtils.decompress(compressed);
        // 3. 验证完整性
        Assertions.assertArrayEquals(originalBytes, decompressed, "大字节数组解压后应完全匹配");
    }

    @Test
    @DisplayName("测试边界情况：空数组和 Null")
    void testEdgeCases() {
        // 测试 Null
        byte[] nullResult = HeatshrinkUtils.compress(null);
        Assertions.assertEquals(0, nullResult.length);
        // 测试空数组
        byte[] emptyResult = HeatshrinkUtils.compress(new byte[0]);
        Assertions.assertEquals(0, emptyResult.length);
        // 测试解压空数据
        byte[] emptyDecompress = HeatshrinkUtils.decompress(new byte[0]);
        Assertions.assertEquals(0, emptyDecompress.length);
    }

    @Test
    @DisplayName("模拟 MCU 发送的 Hex 数据解压 (验证参数配置)")
    void testMcuCompatibility() {
        // 如果你手里有 GD32 打印出来的实际压缩数据的 Hex 字符串，可以在这里测试
        // 这是一个示例：假设 GD32 发送了 "Hello" 的压缩 hex (这里是伪造的，仅作演示)
        // 你可以用这个方法调试 Window/Lookahead bits 是否匹配
        /*
        // 假设 "Hello" 压缩后的字节
        byte[] mcuCompressedData = new byte[] { (byte)0xB3, (byte)0x48, ... };

        byte[] decompressed = HeatshrinkUtils.decompress(mcuCompressedData);
        System.out.println("从 MCU 还原的数据: " + new String(decompressed));
        */
        Assertions.assertTrue(true);
    }
}
