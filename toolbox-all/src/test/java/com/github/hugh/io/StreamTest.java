package com.github.hugh.io;

import com.github.hugh.util.io.StreamUtils;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author AS
 * @date 2020/10/26 15:11
 */
public class StreamTest {
    private static final String DB_PATH =   "/ip2region/ip2region.db";
    private static final String del_path = "D:\\battery-report.html";

    @Test
    public void test01() {
        InputStream inputStream = StreamUtils.getInputStream(DB_PATH);
        StreamUtils.toFile(inputStream, "D:\\battery-report.html");
        System.out.println("==END===");
    }

    @Test
    public void test02() throws IOException {
        File file = new File(del_path);
        ByteSource byteSource = Files.asByteSource(file);
        byte[] read = byteSource.read();
        System.out.println(read.length);
        byte[] bytes = Files.toByteArray(file);
        System.out.println(bytes.length);
        byte[] bytes1 = StreamUtils.resourceToByteArray(DB_PATH);
        System.out.println("---->>" + bytes1.length);
    }

}
