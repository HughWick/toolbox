package com.github.hugh.util.heatshrink;

import com.github.hugh.util.compress.heatshrink.HeatshrinkUtils;
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
        byte[] nullResult = HeatshrinkUtils.compress((byte[]) null);
        Assertions.assertEquals(0, nullResult.length);
        // 测试空数组
        byte[] emptyResult = HeatshrinkUtils.compress(new byte[0]);
        Assertions.assertEquals(0, emptyResult.length);
        // 测试解压空数据
        byte[] emptyDecompress = HeatshrinkUtils.decompress(new byte[0]);
        Assertions.assertEquals(0, emptyDecompress.length);
        // 测试 compressString
        Assertions.assertArrayEquals(new byte[0], HeatshrinkUtils.compress((byte[]) null));
        Assertions.assertArrayEquals(new byte[0], HeatshrinkUtils.compress(""));

        // 测试 decompressToString
        Assertions.assertEquals("", HeatshrinkUtils.decompressToString(null)); // 注意：原代码decompress处理了null，这里要确保decompressToString也兼容
        Assertions.assertEquals("", HeatshrinkUtils.decompressToString(new byte[0]));

        // 测试 compressToEncodedString
        Assertions.assertEquals("", HeatshrinkUtils.compressToEncodedString(null));
        Assertions.assertEquals("", HeatshrinkUtils.compressToEncodedString(""));

        // 测试 decompressFromEncodedString
        Assertions.assertEquals("", HeatshrinkUtils.decompressFromEncodedString(null));
        Assertions.assertEquals("", HeatshrinkUtils.decompressFromEncodedString(""));
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

    // ================================================================
    // 测试场景 1: 字符串 -> 字节数组 -> 字符串 (Round Trip)
    // ================================================================

    @Test
    @DisplayName("测试基础字符串压缩与还原")
    void testStringRoundTrip() {
        String originalText = "Hello Heatshrink! This is a test string.";

        // 1. 压缩
        byte[] compressedBytes = HeatshrinkUtils.compress(originalText);
        Assertions.assertNotNull(compressedBytes);
        Assertions.assertTrue(compressedBytes.length > 0);
        System.out.println("场景1 - 原始长度: " + originalText.length());
        System.out.println("场景1 - 压缩后字节数: " + compressedBytes.length);
        // 2. 解压
        String restoredText = HeatshrinkUtils.decompressToString(compressedBytes);
        // 3. 验证
        Assertions.assertEquals(originalText, restoredText, "解压后的字符串应与原文一致");
    }

    // ================================================================
    // 测试场景 2: 字符串 -> Base64字符串 -> 字符串 (传输场景)
    // ================================================================

    @Test
    @DisplayName("测试 Base64 编码的压缩字符串 (JSON传输场景)")
    void testEncodedStringRoundTrip() {
        // 模拟一个较长的 JSON 数据，重复内容多，压缩效果好
        String jsonContent = "{\"device_id\":\"GD32F450\",\"status\":\"online\",\"config\":{\"wifi\":\"OFF\",\"ble\":\"ON\",\"wifi\":\"OFF\",\"ble\":\"ON\"}}";
        // 1. 压缩并转 Base64
        String base64Result = HeatshrinkUtils.compressToEncodedString(jsonContent);
        Assertions.assertNotNull(base64Result);
        // Base64 只包含可见字符，不应包含换行或非法字符
        Assertions.assertTrue(base64Result.matches("^[a-zA-Z0-9+/=]+$"), "输出应为合法的 Base64 字符串");
        System.out.println("场景2 - 原始字符串: " + jsonContent);
        System.out.println("场景2 - Base64结果: " + base64Result);
        // 2. 还原
        String restoredJson = HeatshrinkUtils.decompressFromEncodedString(base64Result);
        // 3. 验证
        Assertions.assertEquals(jsonContent, restoredJson, "经过Base64编解码后内容应保持一致");
    }

    // ================================================================
    // 测试场景 3: 中文与特殊字符 (UTF-8 兼容性)
    // ================================================================

    @Test
    @DisplayName("测试中文、Emoji等特殊字符支持")
    void testUnicodeSupport() {
        String original = "你好，世界！Heatshrink 🚀 测试中...";
        // 1. 压缩
        byte[] compressed = HeatshrinkUtils.compress(original);
        // 2. 解压
        String restored = HeatshrinkUtils.decompressToString(compressed);
        // 3. 验证
        Assertions.assertEquals(original, restored, "中文和Emoji应能正确还原，不乱码");
    }

    // ================================================================
    // 测试场景 4: 边界条件 (Null 和 空串)
    // ================================================================


    // ================================================================
    // 测试场景 5: 异常处理 (非法 Base64)
    // ================================================================

    @Test
    @DisplayName("测试非法 Base64 输入时的异常抛出")
    void testInvalidBase64Input() {
        String invalidBase64 = "这显然不是一个Base64字符串!!!";
        // 期待抛出 IllegalStateException (根据你代码中的实现)
        Exception exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            HeatshrinkUtils.decompressFromEncodedString(invalidBase64);
        });
        // 验证错误信息包含关键词
        Assertions.assertTrue(exception.getMessage().contains("not a valid Base64"));
    }

    // ================================================================
    // 测试场景 6: 压缩率验证 (确保真的压缩了)
    // ================================================================

    @Test
    @DisplayName("验证长文本的压缩效果")
    void testCompressionRatio() {
        // 构造一个高度重复的长字符串
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("RepeatString_");
        }
        String longStr = sb.toString();
        byte[] originalBytes = longStr.getBytes(StandardCharsets.UTF_8);
        byte[] compressedBytes = HeatshrinkUtils.compress(longStr);
        System.out.println("场景6 - 原始大小: " + originalBytes.length + " bytes");
        System.out.println("场景6 - 压缩大小: " + compressedBytes.length + " bytes");
        // 对于重复内容，Heatshrink 应该能显著减少体积
        Assertions.assertTrue(compressedBytes.length < originalBytes.length, "压缩后的数据应当比原始数据小");
        // 确保数据没坏
        Assertions.assertEquals(longStr, HeatshrinkUtils.decompressToString(compressedBytes));
    }
}
