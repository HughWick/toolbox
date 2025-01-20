package com.github.hugh;

import com.github.hugh.util.GzipUtils;
import com.github.hugh.util.base.BaseConvertUtils;
import com.lingmoyun.minilzo.MiniLZO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * gzip 压缩
 *
 * @author AS
 * @date 2020/9/3 11:19
 */
class GzipUtilsTest {

    @Test
    void testEmpty() throws IOException {
        String str1 = "";
        String compress = GzipUtils.compress(str1);
        assertEquals(str1, compress);
        String uncompress = GzipUtils.uncompress(str1);
        assertEquals(str1, uncompress);
    }

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
    void testCompress() throws IOException {
        String str1 = "添加了方法 uncompress 的文档注释，说明了该方法的功能和参数。";
        String compress = GzipUtils.compress(str1.getBytes());
        String uncompress = GzipUtils.uncompress(compress);
        assertEquals(uncompress, str1);


    }

    @Test
    void testGzip() throws IOException {
        String str1 = "{\"action\":\"W\",\"command\":{\"00100004\":[\"00050001\", \"00060001\", \"00050006\", \"00010001\", \"0007000c\", \"00070007\", \"00070009\", \"06010005\", \"0601000b\", \"07000005\", \"07000006\", \"07000001\", \"0700000b\", \"0f030001\", \"06000001\", \"06010001\", \"06020001\",\"06000002\", \"06000003\", \"06000006\", \"06010006\", \"06020006\", \"06010002\", \"06010003\", \"06000005\", \"0600000b\", \"0602000b\", \"06020002\", \"06020003\", \"00020001\", \"00020002\", \"00020003\", \"00020004\", \"06020005\", \"0600000c\", \"0007000d\", \"0007000e\", \"0601000c\",\"0602000c\", \"07000002\", \"07000003\", \"0007000a\"]}}";
        String compress = GzipUtils.compress(str1);
        assertEquals(540, str1.length());
        assertEquals(180, compress.length());
    }

    @Test
    void testUncompressToByteArray() throws IOException {
        // Arrange
        String originalString = "Hello, world!";
        byte[] compressedBytes = GzipUtils.compressToByteArray(originalString.getBytes());
        // Act
        byte[] uncompressedBytes = GzipUtils.uncompressToByteArray(compressedBytes);
        // Assert
        assertArrayEquals(originalString.getBytes(), uncompressedBytes);
    }

    @Test
    void testUncompressToByteArrayWithString() throws IOException {
        // Arrange
        String originalString = "测试用例示例，测试 uncompressToByteArray 方法是否正确地将字符串解压缩为字节数组：";
        String compress = GzipUtils.compress(originalString);
        // Act
        byte[] uncompressedBytes = GzipUtils.uncompressToByteArray(compress);
        // Assert
        assertArrayEquals(originalString.getBytes(), uncompressedBytes);
    }


    // 嵌入式压缩方式
    @Test
    void testMinilzo() {
        String str1 = "{\"action\":\"W\",\"command\":{\"00100004\":[\"00050001\", \"00060001\", \"00050006\", \"00010001\", \"0007000c\", \"00070007\", \"00070009\", \"06010005\", \"0601000b\", \"07000005\", \"07000006\", \"07000001\", \"0700000b\", \"0f030001\", \"06000001\", \"06010001\", \"06020001\",\"06000002\", \"06000003\", \"06000006\", \"06010006\", \"06020006\", \"06010002\", \"06010003\", \"06000005\", \"0600000b\", \"0602000b\", \"06020002\", \"06020003\", \"00020001\", \"00020002\", \"00020003\", \"00020004\", \"06020005\", \"0600000c\", \"0007000d\", \"0007000e\", \"0601000c\",\"0602000c\", \"07000002\", \"07000003\", \"0007000a\"]}}";
        byte[] origin = str1.getBytes(Charset.forName("gb2312"));
        byte[] compressed = MiniLZO.compress(origin);
        String result1 = "[0, 36, 123, 34, 97, 99, 116, 105, 111, 110, 34, 58, 34, 87, 34, 44, 34, 99, 111, 109, 109, 97, 110, 100, 34, 58, 123, 34, 48, 48, 49, 48, 48, 48, 48, 52, 34, 58, 91, 34, 48, 48, 48, 53, 48, 48, 48, 49, 34, 44, 32, 34, 48, 48, 48, 54, 41, 45, 0, 53, 124, 1, -36, 2, 97, 6, 49, -52, 1, 2, 55, 48, 48, 48, 99, 41, 45, 0, 55, 41, 45, 0, 57, -115, 1, 54, -127, 12, 53, 41, 45, 0, 98, -116, 1, 102, 7, 48, 48, -68, 2, -83, 1, 54, 41, 92, 0, -68, 11, -68, 2, -65, 5, 102, 48, 51, 39, 61, 2, 54, 104, 22, -36, 20, -68, 11, -65, 5, 54, 48, 50, -83, 4, 34, -109, 23, 48, 48, 50, -120, 10, -71, 5, 51, 41, 44, 0, -88, 13, -88, 7, -20, 1, 104, 7, -20, 1, 104, 28, -20, 7, 108, 1, 42, -20, 0, -88, 22, -84, 10, -88, 19, -72, 14, 42, 44, 0, -20, 7, 108, 10, -83, 7, 48, -24, 19, -120, 43, 124, 2, -84, 4, -68, 2, 42, -115, 0, 52, -36, 20, 108, 4, 42, -84, 1, 42, -103, 5, 100, -114, 4, 48, 48, 97, 36, 101, -116, 1, -81, 25, 99, 34, 44, 104, 31, 104, 7, -72, 5, 96, 4, -8, 32, -84, 1, 14, 51, 34, 44, 32, 34, 48, 48, 48, 55, 48, 48, 48, 97, 34, 93, 125, 125, 17, 0, 0]";
        System.out.println("origin.length = " + origin.length); // origin.length = 540
        System.out.println("compressed.length = " + compressed.length); // compressed.length = 256
        Assertions.assertEquals(result1, Arrays.toString(compressed));
    }


    // 测试标准版压缩
    @Test
    void testStandardMinilzo() {
        String str1 = "{\"action\":\"time\",\"00050001\":\"26\",\"00060001\":\"$GNGGA,012039.00,2813.37500,N,11256.49092,E,1,26,0.63,65.7,M,,M,,*6E\\r\\n\\r\\nOK\\r\\n\",\"00060002\":\"$GNRMC,012041.00,A,2813.37487,N,11256.49093,E,0.134,,010724,,,A,V*15\\r\\n\\r\\nOK\\r\\n\",\"00050006\":\"FDD LTE\",\"00010001\":\"12.41\",\"0007000c\":\"1\",\"00070007\":\"192.168.1.45\",\"00070009\":\"01\",\"07000001\":\"1\",\"06000001\":\"1\",\"06010001\":\"1\",\"06020001\":\"1\",\"06000002\":\"12.34\",\"06000003\":\"0.800\",\"06010002\":\"12.38\",\"06010003\":\"0.176\",\"06020002\":\"12.39\",\"06020003\":\"0.075\",\"00020001\":\"33.0\",\"00020002\":\"26.0\",\"00020003\":\"1\",\"00020004\":\"0\",\"0007000d\":[0,1,0,0],\"0007000e\":[0,0,0,0],\"07000002\":\"228.57\",\"0007000a\":\"17\",\"06030001\":\"1\",\"06030002\":\"12.36\",\"06030003\":\"0.000\",\"00060001\":\"$GNGGA,012039.00,2813.37500,N,11256.49092,E,1,26,0.63,65.7,M,,M,,*6E\\r\\n\\r\\nOK\\r\\n\",\"00060002\":\"$GNRMC,012041.00,A,2813.37487,N,11256.49093,E,0.134,,010724,,,A,V*15\\r\\n\\r\\nOK\\r\\n\",\"03010001\":\"228.57\",\"03010002\":\"0.23\",\"03010003\":\"25.47\",\"03010006\":\"100035.89\",\"07010001\":\"1\",\"07010002\":\"228.57\",\"07020001\":\"1\",\"07020002\":\"228.57\",\"07030001\":\"1\",\"07030002\":\"228.57\",\"00030004\":\"湖南省/长沙市/岳麓区/银盆岭街道\",\"00030003\":\"桐梓坡路96号中联科技园\",\"0007000f\":\"192.168.1.4\",\"00070010\":\"0\",\"01000003\":\"NULL\",\"02000003\":\"NULL\",\"00030009\":\"D02020240202001\"}";
        byte[] origin = str1.getBytes(Charset.forName("gb2312"));
        byte[] compressed = MiniLZO.compress(origin);
        System.out.println("origin.length = " + origin.length); // origin.length = 540
        System.out.println("compressed.length = " + compressed.length); // compressed.length = 256
    }


    @Test
    void testStandardHeartbeatMinilzo() {
        String str1 = "{\"action\":\"200\",\"00030009\":\"D00020240000001\",\"00050001\":\"26\"}";
        byte[] origin = str1.getBytes(Charset.forName("gb2312"));
        byte[] compressed = MiniLZO.compress(origin);
        System.out.println("origin.length = " + origin.length); // origin.length = 540
        System.out.println("compressed.length = " + compressed.length); // compressed.length = 256
    }


    @Test
    void testHex() throws IOException {
        String jsonStr2 = "{\n" +
                "  \"args\": {},\n" +
                "  \"data\": \"{\\\"foo1\\\":\\\"bar1\\\",\\\"foo2\\\":\\\"bar3\\\"}\",\n" +
                "  \"files\": {},\n" +
                "  \"form\": {},\n" +
                "  \"headers\": {\n" +
                "    \"Accept-Encoding\": \"gzip\",\n" +
                "    \"Content-Length\": \"29中文sfdsa大师金克拉后期\",\n" +
                "    \"Content-Type\": \"application/json;charset=UTF-8\",\n" +
                "    \"Host\": \"httpbin.org\",\n" +
                "    \"User-Agent\": \"Custom User Agent\",\n" +
                "    \"X-Amzn-Trace-Id\": \"Root=1-663046e8-00aceca639b1ac3753a69a83\"\n" +
                "  },\n" +
                "  \"json\": \"{\\\"foo1\\\":\\\"bar1\\\",\\\"foo2\\\":\\\"bar3\\\"}\",\n" +
                "  \"origin\": \"222.244.144.131\",\n" +
                "  \"url\": \"https://httpbin.org/post\"\n" +
                "}";
//        System.out.println("===ori=>" + jsonStr2.length());
        byte[] bytes = GzipUtils.compressToByteArray(jsonStr2);
//        System.out.println("==压缩后>>>>>" + Arrays.toString(bytes));
//        System.out.println("===压缩后长度>>>" + bytes.length);
        String stringBuffer = BaseConvertUtils.byteToHexStr(bytes);
        assertEquals(688, stringBuffer.length());
        String uncompress = GzipUtils.uncompress(BaseConvertUtils.hexToBytes(stringBuffer));
        assertEquals(jsonStr2, uncompress);
        String encode = URLEncoder.encode(jsonStr2, StandardCharsets.UTF_8);
//        System.out.println("===URL>>" + encode);
//        System.out.println("====>>" + encode.length());
    }

}
