package com.github.hugh.util.file;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author AS
 * @date 2023/12/11 11:09
 */
class PdfUtilsTest {

    @Test
    void testPdfToImage(){
        String imageJpg = "/file/pdf/Contract.pdf";
        String imageType = "jpg";
        final String path1 = getPath(imageJpg);
        List<String> strings = PdfUtils.pdfToImage(path1, "D:\\img", "pdf_jpg", imageType);
        strings.forEach(System.out::println);
        Assertions.assertEquals(4,strings.size());
    }

    private static String getPath(String fileName) {
        return PdfUtilsTest.class.getResource(fileName).getPath();
    }
}
