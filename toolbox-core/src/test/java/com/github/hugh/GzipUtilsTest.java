package com.github.hugh;

import com.github.hugh.util.GzipUtils;
import com.lingmoyun.minilzo.MiniLZO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

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

    @Test
    void testGzip() throws IOException {
        String str1 = "{\"action\":\"W\",\"command\":{\"00100004\":[\"00050001\", \"00060001\", \"00050006\", \"00010001\", \"0007000c\", \"00070007\", \"00070009\", \"06010005\", \"0601000b\", \"07000005\", \"07000006\", \"07000001\", \"0700000b\", \"0f030001\", \"06000001\", \"06010001\", \"06020001\",\"06000002\", \"06000003\", \"06000006\", \"06010006\", \"06020006\", \"06010002\", \"06010003\", \"06000005\", \"0600000b\", \"0602000b\", \"06020002\", \"06020003\", \"00020001\", \"00020002\", \"00020003\", \"00020004\", \"06020005\", \"0600000c\", \"0007000d\", \"0007000e\", \"0601000c\",\"0602000c\", \"07000002\", \"07000003\", \"0007000a\"]}}";
        String compress = GzipUtils.compress(str1);
        System.out.println("---源->"+str1.length());
        System.out.println("---压手->"+compress.length()); // 180
    }


    @Test
    void testMinilzo() {
        String str1 = "{\"action\":\"W\",\"command\":{\"00100004\":[\"00050001\", \"00060001\", \"00050006\", \"00010001\", \"0007000c\", \"00070007\", \"00070009\", \"06010005\", \"0601000b\", \"07000005\", \"07000006\", \"07000001\", \"0700000b\", \"0f030001\", \"06000001\", \"06010001\", \"06020001\",\"06000002\", \"06000003\", \"06000006\", \"06010006\", \"06020006\", \"06010002\", \"06010003\", \"06000005\", \"0600000b\", \"0602000b\", \"06020002\", \"06020003\", \"00020001\", \"00020002\", \"00020003\", \"00020004\", \"06020005\", \"0600000c\", \"0007000d\", \"0007000e\", \"0601000c\",\"0602000c\", \"07000002\", \"07000003\", \"0007000a\"]}}";
        byte[] origin = str1.getBytes(Charset.forName("gb2312"));
        byte[] compressed = MiniLZO.compress(origin);
        String result1="[0, 36, 123, 34, 97, 99, 116, 105, 111, 110, 34, 58, 34, 87, 34, 44, 34, 99, 111, 109, 109, 97, 110, 100, 34, 58, 123, 34, 48, 48, 49, 48, 48, 48, 48, 52, 34, 58, 91, 34, 48, 48, 48, 53, 48, 48, 48, 49, 34, 44, 32, 34, 48, 48, 48, 54, 41, 45, 0, 53, 124, 1, -36, 2, 97, 6, 49, -52, 1, 2, 55, 48, 48, 48, 99, 41, 45, 0, 55, 41, 45, 0, 57, -115, 1, 54, -127, 12, 53, 41, 45, 0, 98, -116, 1, 102, 7, 48, 48, -68, 2, -83, 1, 54, 41, 92, 0, -68, 11, -68, 2, -65, 5, 102, 48, 51, 39, 61, 2, 54, 104, 22, -36, 20, -68, 11, -65, 5, 54, 48, 50, -83, 4, 34, -109, 23, 48, 48, 50, -120, 10, -71, 5, 51, 41, 44, 0, -88, 13, -88, 7, -20, 1, 104, 7, -20, 1, 104, 28, -20, 7, 108, 1, 42, -20, 0, -88, 22, -84, 10, -88, 19, -72, 14, 42, 44, 0, -20, 7, 108, 10, -83, 7, 48, -24, 19, -120, 43, 124, 2, -84, 4, -68, 2, 42, -115, 0, 52, -36, 20, 108, 4, 42, -84, 1, 42, -103, 5, 100, -114, 4, 48, 48, 97, 36, 101, -116, 1, -81, 25, 99, 34, 44, 104, 31, 104, 7, -72, 5, 96, 4, -8, 32, -84, 1, 14, 51, 34, 44, 32, 34, 48, 48, 48, 55, 48, 48, 48, 97, 34, 93, 125, 125, 17, 0, 0]";
        System.out.println("origin.length = " + origin.length); // origin.length = 540
        System.out.println("compressed.length = " + compressed.length); // compressed.length = 256
        Assertions.assertEquals(result1 , Arrays.toString(compressed));
    }
}
