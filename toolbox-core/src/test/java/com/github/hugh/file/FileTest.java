package com.github.hugh.file;

import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.file.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 文件测试
 *
 * @author AS
 * @date 2020/9/10 11:06
 */
class FileTest {

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
        String path1 = "D:\\java测试目录";
        File fileDir = new File(path1);
        if (!fileDir.exists()) {
            assertTrue(fileDir.mkdir());
        }
        String fileName = path1 + "\\file.txt";
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
        String path1 = "D:\\java测试目录";
        File fileDir = new File(path1);
        if (!fileDir.exists()) {
            assertTrue(fileDir.mkdir());
        }
        assertTrue(new File(path1).exists());
        FileUtils.delEmptyDir(path1);
        assertFalse(new File(path1).exists());
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
        String ip2DbPath = FileTest.class.getResource("/ip2region.xdb").getFile();
        byte[] bytes = FileUtils.toByteArray(ip2DbPath);
        assertEquals(58597, bytes.length);
        ToolboxException toolboxException = assertThrowsExactly(ToolboxException.class, () -> {
            FileUtils.toByteArray(ip2DbPath + "2");
        });
        assertEquals("file not exists !", toolboxException.getMessage());
    }

    @Test
    void testSizeCalc() {
        String ip2DbPath = FileTest.class.getResource("/ip2region.xdb").getFile();
//        String head = "C:\\Users\\Hugh\\Desktop\\";
//        String path = head + "FIGqfQdakAQeRiG.jpg";
//        File directory = new File(ip2DbPath);//设定为当前文件

//        System.out.println("--B->>" + FileUtils.formatFileSize(new File(head + "updateFile.http").length()));
        String temp1 = "/file/img.gitconfig";
        final String path = FileTest.class.getResource("/").getPath();
//        System.out.println(path);
        final File kbFile = new File(path + temp1);
        //B
        assertEquals("213.00B", FileUtils.formatFileSize(kbFile.length()));
        assertEquals("57.22KB", FileUtils.formatFileSize(new File(ip2DbPath).length()));

//        System.out.println("==b=>" + FileUtils.formatFileSize(new File(ip2DbPath).length()));
//        System.out.println("--MB->>" + FileUtils.formatFileSize(directory.length()));
//        System.out.println("--path->>" + FileUtils.formatFileSize(head + "updateFile.http"));
//        System.out.println("--file->>" + FileUtils.formatFileSize(new File(head + "FO2ufhraAAA-hpu.jpg")));

    }
}
