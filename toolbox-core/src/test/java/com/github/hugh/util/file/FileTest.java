package com.github.hugh.util.file;

import com.github.hugh.exception.ToolboxException;
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

    @Test
    void testCreateDir() {
        FileUtils.createDir(TEMP_PATH);
        assertTrue(new File(TEMP_PATH).exists());
        FileUtils.deleteDir(TEMP_PATH);
        assertFalse(new File(TEMP_PATH).exists());
    }

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


    private static String getPath(String fileName) {
        return ImageTest.class.getResource(fileName).getPath();
    }

    @Test
    void testFormat() throws IOException {
        String image9 = "/file/image/heif/share_a4b448c4f972858f42640e36ffc3a8e6.png";
        final File kbFile = new File(getPath(image9));
        // 无法正确读取文件格式
//        final String picTyp9 = FileUtils.getFileType(getPath(image9));
//        assertNull(picTyp9);
//        assertEquals(SuffixCode.HEIF_LOWER_CASE, picTyp9);
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

    @Test
    void testImageStreamToBase64Str() throws FileNotFoundException {
        String imageBase64 = "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";
        String imageJpg = "/file/image/avatar.jpg";
        final String path1 = getPath(imageJpg);
        String s1 = FileUtils.imageToBase64Str(new FileInputStream(path1));
        assertEquals(imageBase64, s1);
    }
}
