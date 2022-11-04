package com.github.hugh.file;

import com.github.hugh.util.file.SuffixUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        assertTrue(SuffixUtils.isWord(str2));
        String str3 = "xlsx";
        assertTrue(SuffixUtils.isExcel(str3));
        String str4 = "rar";
        assertTrue(SuffixUtils.isCompress(str4));
        String str5 = "pdF";
        assertTrue(SuffixUtils.isPdf(str5));
        String str6 = "mp4";
        assertTrue(SuffixUtils.isMp4(str6));
    }
}
