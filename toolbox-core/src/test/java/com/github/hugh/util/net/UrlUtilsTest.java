package com.github.hugh.util.net;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class UrlUtilsTest {
    @Test
    void test() {
        String head = "https://minio.dev.hnlot.com.cn/svmp-dev/";
        String str1 = head + "trip/Event/20250430/DC2E97CCD813@1746001629@LeaveSeat.json";
        try {
            String s = UrlUtils.readContent(str1);
            System.out.println(s);
            assertNotNull(s);
            assertEquals(s.length(), 478);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String TEST_IMAGE_URL = "https://minio.dev.hnlot.com.cn/svmp-dev/media/DriverPicture/DC2E97CCD8131745830325.jpg";

    @Test
    void testReadUrlContentAsBytes() {
        System.out.println("正在测试 readUrlContentAsBytes 方法...");
        System.out.println("测试 URL: " + TEST_IMAGE_URL);
        byte[] contentBytes;
        try {
            contentBytes = UrlUtils.readContentAsBytes(TEST_IMAGE_URL);
            // 断言：返回的字节数组不应该为null
            assertNotNull(contentBytes, "读取的内容字节数组不应为 null");
            // 断言：返回的字节数组长度应该大于0 (文件有内容)
            assertTrue(contentBytes.length > 0, "读取的内容字节数组长度应大于 0");
            System.out.println("成功读取 URL 内容。字节数: " + contentBytes.length);
            // 可选的进一步断言：如果知道文件的确切大小，可以断言长度
            // 例如：assertEquals(12345, contentBytes.length, "读取的字节数与预期不符");
            assertEquals(76058, contentBytes.length);
        } catch (IOException e) {
            System.err.println("测试失败: 读取 URL 内容时发生 IOException - " + e.getMessage());
            e.printStackTrace();
            // 如果发生IOException，测试应该失败，可以重新抛出或使用 fail()
            fail("读取 URL 内容时发生异常: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("测试失败: URL 参数异常 - " + e.getMessage());
            fail("URL 参数异常: " + e.getMessage());
        }
        System.out.println("readUrlContentAsBytes 方法测试通过。");
    }

    // 测试url中的文件是否存在
    @Test
    void testUrlFileExist() {
        String url = "https://ym.191ec.com/img/goodsContent/901015857951990381/b632537a5b884ecc8309222fca1d835b_1588148150570.jpg";
        assertTrue(UrlUtils.resourceExists(url));
        assertFalse(UrlUtils.resourceExists(url + "1"));
    }


    @Test
    void testUrlFileNotExist() {
        String url = "https://ym.191ec.com/img/goodsContent/901015857951990381/b632537a5b884ecc8309222fca1d835b_1588148150570.jpg";
        assertFalse(UrlUtils.resourceNotExists(url));
        assertTrue(UrlUtils.resourceNotExists(url + "1"));

        String url2 = "https://minio.dev.hnlot.com.cn/svmp-dev/media/EventMedia/20250428/DC2E97CCD813@DriverChanged@1745808414@2@C43D7BDFADD21B920EE53F2317BB3A6A.jpg";
        assertTrue(UrlUtils.resourceExists(url2));
    }
}
