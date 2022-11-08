package com.github.hugh.io;

import com.github.hugh.file.ImageTest;
import com.github.hugh.util.file.FileUtils;
import com.github.hugh.util.io.StreamUtils;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * stream 流处理工具类
 *
 * @author AS
 * @date 2020/10/26 15:11
 */
class StreamTest {
    //    private static final String DB_PATH =   "/ip2region/ip2region.db";
    private static final String del_path = "D:\\battery-report.html";

    @Test
    void testToFile() throws IOException {
        String image1 = "/file/image/69956256_p1.jpg";
        String path = StreamTest.class.getResource(image1).getPath();
        String path2 = StreamTest.class.getResource("/").getPath();
        InputStream inputStream = StreamUtils.getInputStream(path);
        String outFilePath = path2 + "test.jpg";
        StreamUtils.toFile(inputStream,outFilePath );
        assertTrue(new File(outFilePath).exists());
        FileUtils.delFile(outFilePath);
        assertFalse(new File(outFilePath).exists());
    }

    @Test
    void testToByteArray() throws IOException {
        File file = new File(del_path);
        ByteSource byteSource = Files.asByteSource(file);
        byte[] read = byteSource.read();
//        System.out.println(read.length);
        byte[] bytes = Files.toByteArray(file);
//        System.out.println(bytes.length);
        assertEquals(read.length, bytes.length);
        byte[] bytes1 = StreamUtils.resourceToByteArray(del_path);
        assertEquals(bytes.length, bytes1.length);
        assertArrayEquals(bytes, bytes1);
    }

}
