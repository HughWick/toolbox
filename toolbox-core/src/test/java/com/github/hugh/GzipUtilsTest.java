package com.github.hugh;

import com.github.hugh.util.GzipUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * gzip 压缩
 *
 * @author AS
 * @date 2020/9/3 11:19
 */
class GzipUtilsTest {

    @Test
    void testLength() throws IOException {
        String str =
                "《中文编码测试》间22lastUpdateTime%22%3A%222011-10-28+9%3A39%3A41%22%2C%22smsList%22%3A%5B%7B%22liveState%22%3A%221";
        assertEquals(106, str.length());
        String compress = GzipUtils.compress(str);
        assertEquals(124, compress.length());
        assertEquals(str, GzipUtils.uncompress(compress));
    }
}
