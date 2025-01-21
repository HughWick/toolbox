package com.github.hugh.util.file;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 文件后缀测试类
 * User: Lenovo
 * Date: 2021/3/3 14:19
 */
class SuffixTest {

    @Test
    void testSuffix() {
        String str = "jpg";
        assertTrue(SuffixUtils.isImage(str));
        String str2 = "docx";
        assertFalse(SuffixUtils.isWord(""));
        assertTrue(SuffixUtils.isWord(str2));
        String str3 = "xlsx";
        assertTrue(SuffixUtils.isExcel(str3));
        String str4 = "rar";
        assertTrue(SuffixUtils.isCompress(str4));
        String str5 = "pdF";
        assertFalse(SuffixUtils.isPdf(""));
        assertTrue(SuffixUtils.isPdf(str5));
        String str6 = "mp4";
        assertFalse(SuffixUtils.isMp4(""));
        assertTrue(SuffixUtils.isMp4(str6));
    }
    // 测试合法的图片后缀
    @Test
    public void testValidImageSuffixes() {
        assertTrue(SuffixUtils.isImage("PNG"), "Test failed for PNG");
        assertTrue(SuffixUtils.isImage("JPG"), "Test failed for JPG");
        assertTrue(SuffixUtils.isImage("JPEG"), "Test failed for JPEG");
        assertTrue(SuffixUtils.isImage("BMP"), "Test failed for BMP");
        assertTrue(SuffixUtils.isImage("GIF"), "Test failed for GIF");
        assertTrue(SuffixUtils.isImage("SVG"), "Test failed for SVG");
        assertTrue(SuffixUtils.isImage("WEBP"), "Test failed for WEBP");
    }

    // 测试非法的图片后缀
    @Test
    public void testInvalidImageSuffixes() {
        assertFalse(SuffixUtils.isImage("TXT"), "Test failed for TXT");
        assertFalse(SuffixUtils.isImage("PDF"), "Test failed for PDF");
        assertFalse(SuffixUtils.isImage("DOCX"), "Test failed for DOCX");
        assertFalse(SuffixUtils.isImage("HTML"), "Test failed for HTML");
        assertFalse(SuffixUtils.isImage("EXE"), "Test failed for EXE");
    }

    // 测试空字符串
    @Test
    public void testEmptyString() {
        assertFalse(SuffixUtils.isImage(""), "Test failed for empty string");
    }

    // 测试 null 值
    @Test
    public void testNullValue() {
        assertFalse(SuffixUtils.isImage(null), "Test failed for null value");
    }

    // 测试大小写不敏感的比较
    @Test
    public void testCaseInsensitiveComparison() {
        assertTrue(SuffixUtils.isImage("pNg"), "Test failed for case-insensitive comparison of PNG");
        assertTrue(SuffixUtils.isImage("JpG"), "Test failed for case-insensitive comparison of JPG");
    }

    // 测试空格字符
    @Test
    public void testWhitespace() {
        assertFalse(SuffixUtils.isImage(" "), "Test failed for whitespace string");
    }

    // 测试包含数字的后缀
    @Test
    public void testSuffixWithNumbers() {
        assertFalse(SuffixUtils.isImage("JPG123"), "Test failed for suffix with numbers");
    }

    // 测试混合大小写的后缀
    @Test
    public void testMixedCaseSuffix() {
        assertTrue(SuffixUtils.isImage("JpG"), "Test failed for mixed-case JPG");
    }

    // 测试合法的压缩后缀
    @Test
    void testValidCompressionSuffixes() {
        assertTrue(SuffixUtils.isCompress("ZIP"), "Test failed for ZIP");
        assertTrue(SuffixUtils.isCompress("RAR"), "Test failed for RAR");
        assertTrue(SuffixUtils.isCompress("GZ"), "Test failed for GZ");
        assertTrue(SuffixUtils.isCompress("7Z"), "Test failed for 7Z");
    }

    // 测试非法的压缩后缀
    @Test
    void testInvalidCompressionSuffixes() {
        assertFalse(SuffixUtils.isCompress("TXT"), "Test failed for TXT");
        assertFalse(SuffixUtils.isCompress("PDF"), "Test failed for PDF");
        assertFalse(SuffixUtils.isCompress("DOCX"), "Test failed for DOCX");
        assertFalse(SuffixUtils.isCompress("HTML"), "Test failed for HTML");
        assertFalse(SuffixUtils.isCompress("EXE"), "Test failed for EXE");
    }

    // 测试空字符串
    @Test
    void testEmptyStringCompress() {
        assertFalse(SuffixUtils.isCompress(""), "Test failed for empty string");
    }

    // 测试 null 值
    @Test
    void testNullValueCompress() {
        assertFalse(SuffixUtils.isCompress(null), "Test failed for null value");
    }

    // 测试大小写不敏感的比较
    @Test
    void testCaseInsensitiveComparison2() {
        assertTrue(SuffixUtils.isCompress("zIp"), "Test failed for case-insensitive comparison of ZIP");
        assertTrue(SuffixUtils.isCompress("RaR"), "Test failed for case-insensitive comparison of RAR");
        assertTrue(SuffixUtils.isCompress("gz"), "Test failed for case-insensitive comparison of GZ");
        assertTrue(SuffixUtils.isCompress("7z"), "Test failed for case-insensitive comparison of 7Z");
    }

    // 测试空格字符
    @Test
    void testWhitespaceCompress() {
        assertFalse(SuffixUtils.isCompress(" "), "Test failed for whitespace string");
    }

    // 测试包含数字的后缀
    @Test
    void testSuffixWithNumbersCompress() {
        assertFalse(SuffixUtils.isCompress("ZIP123"), "Test failed for suffix with numbers");
    }

    // 测试混合大小写的后缀
    @Test
    void testMixedCaseSuffixCompress() {
        assertTrue(SuffixUtils.isCompress("ZiP"), "Test failed for mixed-case ZIP");
    }

    // 测试合法的 Excel 后缀
    @Test
    void testValidExcelSuffixes() {
        assertTrue(SuffixUtils.isExcel("XLS"), "Test failed for XLS");
        assertTrue(SuffixUtils.isExcel("XLSX"), "Test failed for XLSX");
    }

    // 测试非法的 Excel 后缀
    @Test
    void testInvalidExcelSuffixes() {
        assertFalse(SuffixUtils.isExcel("CSV"), "Test failed for CSV");
        assertFalse(SuffixUtils.isExcel("TXT"), "Test failed for TXT");
        assertFalse(SuffixUtils.isExcel("PDF"), "Test failed for PDF");
        assertFalse(SuffixUtils.isExcel("DOCX"), "Test failed for DOCX");
        assertFalse(SuffixUtils.isExcel("HTML"), "Test failed for HTML");
    }

    // 测试空字符串
    @Test
    void testEmptyStringIsExcel() {
        assertFalse(SuffixUtils.isExcel(""), "Test failed for empty string");
    }

    // 测试 null 值
    @Test
    void testNullValueIsExcel() {
        assertFalse(SuffixUtils.isExcel(null), "Test failed for null value");
    }

    // 测试大小写不敏感的比较
    @Test
    void testCaseInsensitiveComparisonIsExcel() {
        assertTrue(SuffixUtils.isExcel("xls"), "Test failed for case-insensitive comparison of XLS");
        assertTrue(SuffixUtils.isExcel("xlsx"), "Test failed for case-insensitive comparison of XLSX");
    }

    // 测试空格字符
    @Test
    void testWhitespaceIsExcel() {
        assertFalse(SuffixUtils.isExcel(" "), "Test failed for whitespace string");
    }

    // 测试包含数字的后缀
    @Test
    void testSuffixWithNumbersIsExcel() {
        assertFalse(SuffixUtils.isExcel("XLS123"), "Test failed for suffix with numbers");
    }

    // 测试混合大小写的后缀
    @Test
    void testMixedCaseSuffixIsExcel() {
        assertTrue(SuffixUtils.isExcel("XlS"), "Test failed for mixed-case XLS");
//        assertTrue(SuffixUtils.isExcel("XlXs"), "Test failed for mixed-case XLSX");
    }



}
