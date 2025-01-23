package com.github.hugh.util.ip;

import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.io.StreamUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IP 解析测试工具
 *
 * @author AS
 * @date 2021/2/23 14:23
 */
// 加入下面的注解，再测试时可以不传入 ip 数据文件
//@RunWith(PowerMockRunner.class)
//@PrepareForTest({Ip2regionUtils.class})
public class Ip2RegeinTest {

    /**
     * ip数据文件目录
     */
    private static final String XDB_PATH = "/ip2region/ip2region.xdb";

    private static final Supplier<byte[]> easyRedisSupplier = () -> {
        String path;
//        if (OsUtils.isWindows()) {
        path = Ip2RegeinTest.class.getResource(XDB_PATH).getPath();
//        } else {
//            path = XDB_PATH;
//        }
//        InputStream resourceAsStream = Ip2RegeinTest.class.getResourceAsStream(path);
        InputStream resourceAsStream = new ByteArrayInputStream(StreamUtils.resourceToByteArray(path));
        try {
            return StreamUtils.toByteArray(resourceAsStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };

    @Test
    public void parseStringTest() {
//        String ip = "222.244.144.131";
//        final String cityInfo = Ip2regionUtils.getCityInfo(ip);
//        assertEquals("中国|0|湖南省|长沙市|电信", cityInfo);
        String ip2 = "223.153.137.189";
        final String cityInfo2 = Ip2regionUtils.getCityInfo(ip2, easyRedisSupplier.get());
        assertEquals("中国|0|湖南省|张家界市|电信", cityInfo2);
        String ip3 = "175.8.167.6";
        final String cityInfo3 = Ip2regionUtils.getCityInfo(ip3, easyRedisSupplier.get());
        assertEquals("中国|0|湖南省|长沙市|电信", cityInfo3);
        String ip4 = "192.168.1.191";
        final String cityInfo4 = Ip2regionUtils.getCityInfo(ip4, easyRedisSupplier.get());
        assertEquals("0|0|0|内网IP|内网IP", cityInfo4);
    }

    @Test
    public void testGetCityInfoWithInvalidIp() {
        // 测试无效 IP 地址时的异常处理
        String ip = "invalid_ip";  // 无效的 IP 地址
        byte[] mockData = new byte[]{ /* 假设这里有加载的字节数据 */};
        // 调用静态方法并验证抛出的异常
        Exception exception = assertThrows(ToolboxException.class, () -> {
            Ip2regionUtils.getCityInfo(ip, mockData);
        });
        assertTrue(exception.getMessage().contains("failed to create content cached searcher"));
    }
}
