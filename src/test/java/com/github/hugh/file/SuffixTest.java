package com.github.hugh.file;

import com.github.hugh.util.file.SuffixUtils;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 2021/3/3 14:19
 * Description: No Description
 */
public class SuffixTest {
    @Test
    public void test01() {
        String str = "jpg";
        String str2 = "docx";
        String str3 = "xlsx";
        String str4 = "rar";
        String str5 = "pdf";
        String str6 = "mp4";
        System.out.println("--1->>"+ SuffixUtils.isImage(str));
        System.out.println("--2->>"+ SuffixUtils.isWord(str2));
        System.out.println("--3->>"+ SuffixUtils.isExcel(str3));
        System.out.println("--4->>"+ SuffixUtils.isCompress(str4));
        System.out.println("--5->>"+ SuffixUtils.isPdf(str5));
        System.out.println("--6->>"+ SuffixUtils.isMp4(str6));

    }
}
