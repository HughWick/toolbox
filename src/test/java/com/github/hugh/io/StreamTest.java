package com.github.hugh.io;

import com.github.hugh.util.io.StreamUtils;
import org.junit.Test;

import java.io.InputStream;

/**
 * @author AS
 * @date 2020/10/26 15:11
 */
public class StreamTest {
    private static final String DB_PATH = "/ip2region/ip2region.db";

    @Test
    public void test01() {
        InputStream inputStream = StreamUtils.getInputStream(DB_PATH);
        StreamUtils.toFile(inputStream, "D:\\new_db.db");
        System.out.println("==END===");
    }
}
