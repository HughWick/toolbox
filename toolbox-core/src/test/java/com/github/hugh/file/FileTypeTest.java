package com.github.hugh.file;

import com.github.hugh.constant.SuffixCode;
import com.github.hugh.util.file.FileType;
import com.github.hugh.util.file.FileTypeUtils;
import com.github.hugh.util.file.ImageUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;


/**
 * 文件类型判断工具类
 *
 * @author AS
 * @date 2023/8/3 10:00
 */
class FileTypeTest {

    // 验证文件类型
    @Test
    void testVerifyType() throws FileNotFoundException {
        String image1 = "/file/image/webp/share_572031b53d646c2c8a8191bdd93a95b2.png";
        String path1 = getPath(image1);
        final String picTyp1 = FileTypeUtils.getType(path1);
        assertEquals(SuffixCode.WEBP_LOWER_CASE, picTyp1);

        String image2 = "/file/image/20200718234953_grmzy.jpeg";
        String path2 = getPath(image2);
        final String picTyp2 = FileTypeUtils.getType(new File(path2));
        assertEquals(SuffixCode.JPG_LOWER_CASE, picTyp2);
        String jpg1 = "/file/image/69956256_p1.jpg";
//        String path2 = ImageTest.class.getResource(image2).getPath();
        final String jpgTyp1 = FileTypeUtils.getType(getPath(jpg1));
        assertEquals(SuffixCode.JPG_LOWER_CASE, jpgTyp1);
        String image3 = "/file/image/Teresa.png";
        final String picTyp3 = FileTypeUtils.getType(new File(getPath(image3)));
        assertEquals(SuffixCode.PNG_LOWER_CASE, picTyp3);
        String image4 = "/file/image/tom.gif";
        final String picTyp4 = FileTypeUtils.getType(getPath(image4));
        assertEquals(SuffixCode.GIF_LOWER_CASE, picTyp4);
        String image5 = "/file/image/BMP.bmp";
        final String picTyp5 = FileTypeUtils.getType(getPath(image5));
        assertEquals(SuffixCode.BMP_LOWER_CASE, picTyp5);
        String image7 = "/file/image/tiff.tif";
        final String picTyp7 = FileTypeUtils.getType(getPath(image7));
        assertEquals(SuffixCode.TIF_LOWER_CASE, picTyp7);
        String imagePng = "/file/image/webp/share_875d7016c30cc485a2d35f7aad804aaa.png";
        final String pngType = FileTypeUtils.getType(getPath(imagePng));
        assertEquals(SuffixCode.WEBP_LOWER_CASE, pngType);
        String mp4Path = "/file/mp4/1cbf9f6b76484cbb96515f8ecef16efd.mp4";
        final String mp4Type = FileTypeUtils.getType(getPath(mp4Path));
        assertEquals(SuffixCode.MP4.toLowerCase(), mp4Type);
        String imageHeif = "/file/image/heif/share_a4b448c4f972858f42640e36ffc3a8e6.png";
        final String picTypHeif = FileTypeUtils.getType(getPath(imageHeif));
        assertEquals(SuffixCode.HEIF_LOWER_CASE, picTypHeif);
    }

    // 测试压缩包
    @Test
    void testCompressed() throws FileNotFoundException {
        // zip 内部文件如果是excel时，会出现十六进制头前缀一致问题
//        String zipPath1 = "/file/zip/test.zip";
//        final String zipType1 = FileUtils.getFileType(getPath(zipPath1));
//        assertEquals(SuffixCode.ZIP.toLowerCase(), zipType1);
        String zipPath2 = "/file/zip/test_2.zip";
        final String zipType2 = FileTypeUtils.getType(getPath(zipPath2));
        assertEquals(SuffixCode.ZIP.toLowerCase(), zipType2);
        String rarPath = "/file/rar/files.rar";
        final String rarType = FileTypeUtils.getType(getPath(rarPath));
        assertEquals(SuffixCode.RAR.toLowerCase(), rarType);
    }

    // 验证错误文件类型
    @Test
    void testErrorFileType() throws FileNotFoundException {
        // svg 暂时无法获取
        String image6 = "/file/image/svg.svg";
        final String picTyp6 = FileTypeUtils.getType(getPath(image6));
        assertNull(picTyp6);
    }

    // 验证office
    @Test
    void testOffice() throws FileNotFoundException {
        String path1 = "/file/microsoft/2007.xlsx";
        String excelPath1 = getPath(path1);
        FileType on1 = FileType.on(excelPath1);
        assertTrue(FileType.on(excelPath1).isOffice2007());
        assertTrue(FileType.on(excelPath1).isXlsx());
        on1.closeStream();
        String path2 = "/file/microsoft/251.xls";
        String excelPath2 = getPath(path2);
        FileType on2 = FileType.on(excelPath2);
        assertTrue(on2.isOffice2003());
//        final String excel2 = FileTypeUtils.getType(getPath(path2));
//        assertEquals("xls_doc", excel2);
        String path3 = "/file/microsoft/word/2007.docx";
        // 文档类型
        String wordPath3 = getPath(path3);
        FileType on3 = FileType.on(wordPath3);
        assertTrue(on3.isDocx());
//        File file3 = new File(getPath(path3));
//        final String word3 = FileTypeUtils.getType(file3);
//        assertEquals(SuffixCode.DOCX.toLowerCase(), word3);
        String path4 = "/file/microsoft/word/2003.doc";
        // 流
        FileInputStream fileInputStream = new FileInputStream(getPath(path4));
        final String word4 = FileTypeUtils.getType(fileInputStream);
        assertEquals("xls_doc", word4);
        String image9 = "/file/image/heif/share_a4b448c4f972858f42640e36ffc3a8e6.png";
        // 无法正确读取文件格式
        final String picTyp9 = FileTypeUtils.getType(getPath(image9));
//        assertNull(picTyp9);
        assertEquals(SuffixCode.HEIF_LOWER_CASE, picTyp9);
        assertFalse(ImageUtils.isImage(getPath(image9)));
    }

    // 验证图片
    @Test
    void testIsJpg() throws FileNotFoundException {
        String jpg1 = "/file/image/69956256_p1.jpg";
        FileType on1 = FileType.on(getPath(jpg1));
        assertTrue(on1.isJpg());
        String jpeg1 = "/file/image/20200718234953_grmzy.jpeg";
        FileType on2 = FileType.on(new File(getPath(jpeg1)));
        assertTrue(on2.isJpg());
    }

    // 验证音视频
    @Test
    void testIsMp3() throws FileNotFoundException {
        String mp3_1 = "/file/mp3/MainMenu.mp3";
        FileType on1 = FileType.on(getPath(mp3_1));
        assertTrue(on1.isMp3());
    }

    @Test
    void testIsMp4() throws FileNotFoundException {
        String mp4_1 = "/file/mp4/1cbf9f6b76484cbb96515f8ecef16efd.mp4";
        String path1 = getPath(mp4_1);
        FileType on1 = FileType.on(path1);
//        assertTrue(FileTypeUtils.isMp4(new File(path1)));
        assertTrue(on1.isMp4());
    }

    @Test
    void testIsWebp() throws FileNotFoundException {
        String image1 = "/file/image/webp/share_572031b53d646c2c8a8191bdd93a95b2.png";
        String path1 = getPath(image1);
        FileType on1 = FileType.on(path1);
        assertFalse(on1.isPng());
        assertTrue(FileType.on(path1).isWebp());
        on1.closeStream();
    }

    @Test
    void testIsPdf() throws FileNotFoundException {
        String pdf1 = "/file/pdf/work.pdf";
        String path1 = getPath(pdf1);
        assertTrue(FileType.on(path1).isPdf());
    }

    private static String getPath(String fileName) {
        return FileTypeTest.class.getResource(fileName).getPath();
    }
}
