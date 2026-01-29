package com.github.hugh.util.compress;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.charset.Charset;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class Lz4UtilsTest {

    @Test
    @DisplayName("基本压缩与解压测试 - 字节数组")
    void testBasicCompressAndDecompress() {
        String originalStr = "Hello LZ4, this is a test string for compression! " +
                "Repeat: Hello LZ4, this is a test string for compression!";
        byte[] originalBytes = originalStr.getBytes();

        byte[] compressed = Lz4Utils.compress(originalBytes);
        assertNotNull(compressed);
        assertTrue(compressed.length > 0);

        byte[] decompressed = Lz4Utils.decompress(compressed, originalBytes.length);
        assertArrayEquals(originalBytes, decompressed);
    }

    @Test
    @DisplayName("字符串压缩测试 - 指定编码 GBK")
    void testGbkStringCompression() {
        String content = "你好，这是中文GBK编码测试内容";
        String charsetGbk = "GBK";

        // 获取原始 GBK 字节长度，用于解压
        int originalLength = content.getBytes(Charset.forName(charsetGbk)).length;

        byte[] compressed = Lz4Utils.compress(content, charsetGbk);
        String decompressed = Lz4Utils.decompressToString(compressed, originalLength, Charset.forName(charsetGbk));

        assertEquals(content, decompressed);
    }

    @Test
    @DisplayName("字符串压缩测试 - 指定编码 GB2312")
    void testGb2312StringCompression() {
        String content = "春眠不觉晓，处处闻啼鸟。";
        String charset = "GB2312";

        int originalLength = content.getBytes(Charset.forName(charset)).length;

        byte[] compressed = Lz4Utils.compress(content, charset);
        String result = Lz4Utils.decompressToString(compressed, originalLength, Charset.forName(charset));

        assertEquals(content, result);
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 100, 1000, 5000})
    @DisplayName("大规模随机数据压缩测试")
    void testLargeData(int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(UUID.randomUUID().toString());
        }
        String largeData = sb.toString();
        int originalSize = largeData.getBytes().length;

        byte[] compressed = Lz4Utils.compress(largeData);
        String result = Lz4Utils.decompressToString(compressed, originalSize);

        assertEquals(largeData, result);
        System.out.println("Size: " + originalSize + " -> Compressed: " + compressed.length);
    }

    @Test
    @DisplayName("空值与异常边界测试")
    void testEdgeCases() {
        // 测试空输入
        assertArrayEquals(new byte[0], Lz4Utils.compress((byte[]) null));
        assertArrayEquals(new byte[0], Lz4Utils.compress(""));

        // 测试空解压
        assertArrayEquals(new byte[0], Lz4Utils.decompress(null, 0));
        assertEquals("", Lz4Utils.decompressToString(new byte[0], 0));

        // 测试解压长度错误 (过小会抛出异常或解压不全)
        byte[] compressed = Lz4Utils.compress("Some data");
        assertThrows(RuntimeException.class, () -> {
            Lz4Utils.decompress(compressed, 2); // 长度给小了
        });
    }
}