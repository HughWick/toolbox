package com.github.hugh.file;

import com.github.hugh.constant.SuffixCode;
import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.file.FileUtils;
import com.github.hugh.util.file.ImageUtils;
import com.github.hugh.util.io.StreamUtils;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 文件测试
 *
 * @author AS
 * @date 2020/9/10 11:06
 */
class FileTest {

    private static final String TEMP_PATH = "D:\\\\java测试目录";

//    @Test
//    void testGetFilePath() {
//        String path = FileTest.class.getResource("/69956256_p1.jpg").getFile();
//        System.out.println(path);
//        File directory = new File(path);//设定为当前文件夹
//        String absolutePath = directory.getAbsolutePath();
//        System.out.println("File full path : " + absolutePath);
//    }

    // 删除文件与删除空目录
    @Test
    void testDelFile() throws IOException {
//        String path1 = "D:\\java测试目录";
        File fileDir = new File(TEMP_PATH);
        if (!fileDir.exists()) {
            assertTrue(fileDir.mkdir());
        }
        String fileName = TEMP_PATH + "\\file.txt";
        File file = new File(fileName);
        if (file.createNewFile()) {
            System.out.println("file.txt File Created in Project root directory");
        } else {
            System.out.println("File file.txt already exists in the project root directory");
        }
        FileUtils.delFile(fileName);
        assertFalse(new File(fileName).exists());
    }

    // 测试删除空目录
    @Test
    void testDelEmptyDir() {
        File fileDir = new File(TEMP_PATH);
        if (!fileDir.exists()) {
            assertTrue(fileDir.mkdir());
        }
        assertTrue(new File(TEMP_PATH).exists());
        FileUtils.delEmptyDir(TEMP_PATH);
        assertFalse(new File(TEMP_PATH).exists());
    }

    // 测试删除目录及其下所有文件
    @Test
    void testDelDir() throws IOException {
        File fileDir = new File(TEMP_PATH);
        if (!fileDir.exists()) {
            assertTrue(fileDir.mkdir());
        }
        String name = "文件名";
        for (int i = 0; i < 10; i++) {
            File fileName = new File(TEMP_PATH + "\\" + name + i);
            assertTrue(fileName.createNewFile());
        }
        FileUtils.deleteDir(TEMP_PATH);
        assertFalse(new File(TEMP_PATH).exists());
    }


    // 测试url中的文件是否存在
    @Test
    void testUrlFileExist() {
        String url = "https://ym.191ec.com/img/goodsContent/901015857951990381/b632537a5b884ecc8309222fca1d835b_1588148150570.jpg";
        assertTrue(FileUtils.urlFileExist(url));
        assertFalse(FileUtils.urlFileExist(url + "1"));
        assertFalse(FileUtils.urlNotFileExist(url));
        assertTrue(FileUtils.urlNotFileExist(url + "1"));
    }

    // 测试下载文件
    @Test
    void testDownloadFile() {
        String str = "https://vilipix.oss-cn-beijing.aliyuncs.com/release/user/1100171014/1667786977146_share-1667786931.jpg?x-oss-process=image/resize,m_fill,w_1000";
        //图片保存路径
        String filePath = "D:\\";
        String fileName = filePath + new Date().getTime() + ".png";
        try {
            FileUtils.downloadByNio(str, fileName);
            assertTrue(new File(fileName).exists());
            // 下载成功后删除文件
            FileUtils.delFile(fileName);
            assertFalse(new File(fileName).exists());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    // 测试文件转byte数组
    @Test
    void testFileToByteArray() throws IOException {
        String ip2DbPath = FileTest.class.getResource("/ip2region/ip2region.xdb").getFile();
        byte[] bytes = FileUtils.toByteArray(ip2DbPath);
        assertEquals(11065998, bytes.length);
        ToolboxException toolboxException = assertThrowsExactly(ToolboxException.class, () -> {
            FileUtils.toByteArray(ip2DbPath + "2");
        });
        assertEquals("file not exists !", toolboxException.getMessage());
    }

    @Test
    void testSizeCalc() {
        String ip2DbPath = FileTest.class.getResource("/ip2region/ip2region.xdb").getFile();
//        String head = "C:\\Users\\Hugh\\Desktop\\";
//        String path = head + "FIGqfQdakAQeRiG.jpg";
//        File directory = new File(ip2DbPath);//设定为当前文件
//        System.out.println("--B->>" + FileUtils.formatFileSize(new File(head + "updateFile.http").length()));
//        String temp1 = "/file/img.gitconfig";
//        final String path = FileTest.class.getResource("/").getPath();
//        final File kbFile = new File(path + temp1);
//        System.out.println("--->>"+kbFile.length());
        //B
        assertEquals("213.00B", FileUtils.formatFileSize(213));
        assertEquals("10.55MB", FileUtils.formatFileSize(ip2DbPath));
        assertEquals("1.26GB", FileUtils.formatFileSize(1354390941L));
        assertEquals("981.58MB", FileUtils.formatFileSize(1029263971L));
        assertEquals("410.04KB", FileUtils.formatFileSize(419880));
        assertEquals("880.00B", FileUtils.formatFileSize(880));
    }

    // 验证文件类型
    @Test
    void testVerifyType() throws FileNotFoundException {
        String image1 = "/file/image/webp/share_572031b53d646c2c8a8191bdd93a95b2.png";
        String path1 = ImageTest.class.getResource(image1).getPath();
        final String picTyp1 = FileUtils.getFileType(path1);
        assertEquals(SuffixCode.WEBP_LOWER_CASE, picTyp1);

        String image2 = "/file/image/20200718234953_grmzy.jpeg";
        String path2 = ImageTest.class.getResource(image2).getPath();
        final String picTyp2 = FileUtils.getFileType(new File(path2));
        assertEquals(SuffixCode.JPG_LOWER_CASE, picTyp2);
        String jpg1 = "/file/image/69956256_p1.jpg";
//        String path2 = ImageTest.class.getResource(image2).getPath();
        final String jpgTyp1 = FileUtils.getFileType(getPath(jpg1));
        assertEquals(SuffixCode.JPG_LOWER_CASE, jpgTyp1);
        String image3 = "/file/image/Teresa.png";
        final String picTyp3 = FileUtils.getFileType(new File(getPath(image3)));
        assertEquals(SuffixCode.PNG_LOWER_CASE, picTyp3);
        String image4 = "/file/image/tom.gif";
        final String picTyp4 = FileUtils.getFileType(getPath(image4));
        assertEquals(SuffixCode.GIF_LOWER_CASE, picTyp4);
        String image5 = "/file/image/BMP.bmp";
        final String picTyp5 = FileUtils.getFileType(getPath(image5));
        assertEquals(SuffixCode.BMP_LOWER_CASE, picTyp5);
        String image7 = "/file/image/tiff.tif";
        final String picTyp7 = FileUtils.getFileType(getPath(image7));
        assertEquals(SuffixCode.TIF_LOWER_CASE, picTyp7);
        String imagePng = "/file/image/webp/share_875d7016c30cc485a2d35f7aad804aaa.png";
        final String pngType = FileUtils.getFileType(getPath(imagePng));
        assertEquals(SuffixCode.WEBP_LOWER_CASE, pngType);
        String mp4Path = "/file/mp4/1cbf9f6b76484cbb96515f8ecef16efd.mp4";
        final String mp4Type = FileUtils.getFileType(getPath(mp4Path));
        assertEquals(SuffixCode.MP4.toLowerCase(), mp4Type);

        String imageHeif = "/file/image/heif/share_a4b448c4f972858f42640e36ffc3a8e6.png";
        final String picTypHeif = FileUtils.getFileType(getPath(imageHeif));
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
        final String zipType2 = FileUtils.getFileType(getPath(zipPath2));
        assertEquals(SuffixCode.ZIP.toLowerCase(), zipType2);
        String rarPath = "/file/rar/files.rar";
        final String rarType = FileUtils.getFileType(getPath(rarPath));
        assertEquals(SuffixCode.RAR.toLowerCase(), rarType);
    }

    // 验证错误文件类型
    @Test
    void testErrorFileType() throws FileNotFoundException {
        // svg 暂时无法获取
        String image6 = "/file/image/svg.svg";
        final String picTyp6 = FileUtils.getFileType(getPath(image6));
        assertNull(picTyp6);
    }

    // 验证office
    @Test
    void testOffice() throws FileNotFoundException {
        String path1 = "/file/microsoft/2007.xlsx";
        final String excel1 = FileUtils.getFileType(getPath(path1));
        assertEquals(SuffixCode.XLSX.toLowerCase(), excel1);
        String path2 = "/file/microsoft/251.xls";
        final String excel2 = FileUtils.getFileType(getPath(path2));
        assertEquals("xls_doc", excel2);
        String path3 = "/file/microsoft/word/2007.docx";
        // 文件类型
        File file3 = new File(getPath(path3));
        final String word3 = FileUtils.getFileType(file3);
        assertEquals(SuffixCode.DOCX.toLowerCase(), word3);
        String path4 = "/file/microsoft/word/2003.doc";
        // 流
        FileInputStream fileInputStream = new FileInputStream(getPath(path4));
        final String word4 = FileUtils.getFileType(fileInputStream);
        assertEquals("xls_doc", word4);
    }

    private static String getPath(String fileName) {
        return ImageTest.class.getResource(fileName).getPath();
    }

    @Test
    void testFormat() throws IOException {
        String image9 = "/file/image/heif/share_a4b448c4f972858f42640e36ffc3a8e6.png";
        final File kbFile = new File(getPath(image9));
        // 无法正确读取文件格式
        final String picTyp9 = FileUtils.getFileType(getPath(image9));
//        assertNull(picTyp9);
        assertEquals(SuffixCode.HEIF_LOWER_CASE, picTyp9);
        assertFalse(ImageUtils.isImage(getPath(image9)));
        String tempFile = "D:\\temp.jpg";
        InputStream fileInputStream = new FileInputStream(kbFile);
        StreamUtils.toFile(fileInputStream, tempFile);
        final File file = new File(tempFile);
        assertTrue(file.exists());

        assertFalse(ImageUtils.isImage(file));
        assertTrue(file.delete());
    }

    @Test
    void testReadContent() {
        String filePath = "/file/json/data.txt";
        final String path1 = getPath(filePath);
        final File file1 = new File(path1);
        final String s1 = FileUtils.readContent(file1);
//        System.out.println(s.length());
        final String s2 = FileUtils.readContent(path1);
        assertEquals(s1.length(), s2.length());
    }

    @Test
    void testImageToBase64Str() throws IOException {
        String imageJpg = "/file/image/69956256_p1.jpg";
        final String path1 = getPath(imageJpg);
        String s1 = FileUtils.imageToBase64Str(path1);
        String storePath = "D:\\Program Files\\Desktop\\错误图片\\69956256_p1.jpg";
//        System.out.println("--->" + s1);
        FileUtils.base64StrToImage(s1, storePath);
        boolean image = ImageUtils.isImage(storePath);
        assertTrue(image);
        FileUtils.delFile(storePath);
    }
}
