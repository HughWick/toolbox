package com.github.hugh.io;

import com.github.hugh.util.file.FileUtils;
import com.github.hugh.util.io.StreamUtils;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * stream 流处理工具类
 *
 * @author AS
 * @date 2020/10/26 15:11
 */
class StreamTest {

    @Test
    void testToFile() throws IOException {
        String image1 = "/file/image/69956256_p1.jpg";
        String path = StreamTest.class.getResource(image1).getPath();
        String path2 = StreamTest.class.getResource("/").getPath();
        InputStream inputStream = StreamUtils.getInputStream(path);
        String outFilePath = path2 + "test.jpg";
        StreamUtils.toFile(inputStream, outFilePath);
        assertTrue(new File(outFilePath).exists());
        FileUtils.delFile(outFilePath);
        assertFalse(new File(outFilePath).exists());
    }

    //  测试文件转字节
    @Test
    void testToByteArray() throws IOException {
//        String path = "D:\\private\\toolbox-2.4.X\\toolbox-core\\src\\test\\resources\\file\\image\\Teresa.png";
        String image1 = "/file/image/Teresa.png";
        String path = StreamTest.class.getResource(image1).getPath();
        File file = new File(path);
        ByteSource byteSource = Files.asByteSource(file);
        byte[] read = byteSource.read();
        byte[] bytes = Files.toByteArray(file);
        assertEquals(read.length, bytes.length);
        byte[] bytes1 = StreamUtils.resourceToByteArray(path);
        assertEquals(bytes.length, bytes1.length);
        assertArrayEquals(bytes, bytes1);
    }


    @Test
    void testCloneInputStream() throws IOException {
        String image1 = "/file/image/Teresa.png";
        String path = StreamTest.class.getResource(image1).getPath();
//        File file = new File(path);
        InputStream inputStream = StreamUtils.getInputStream(path);
        InputStream inputStream2 = StreamUtils.getInputStream(path);
        assertArrayEquals(inputStream.readAllBytes(), inputStream2.readAllBytes());
        final InputStream cloneInputStream1 = StreamUtils.cloneInputStream(inputStream);
        assertNotEquals(inputStream2.hashCode(), inputStream.hashCode());
//        assertEquals(inputStream2.hashCode(), inputStream3.hashCode());
        assertArrayEquals(inputStream.readAllBytes(), cloneInputStream1.readAllBytes());
    }

}
