package com.github.hugh;

import com.github.hugh.util.io.StreamUtils;
import com.github.hugh.util.ip.Ip2regionUtils;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * IP 解析测试工具
 *
 * @author AS
 * @date 2021/2/23 14:23
 */
class Ip2regeinTest {
    /**
     * ip数据文件目录
     */
    private static final String XDB_PATH = "/ip2region/ip2region.xdb";

    private static final Supplier<byte[]> easyRedisSupplier = () -> StreamUtils.resourceToByteArray(Ip2regionUtils.class.getResource(XDB_PATH).getPath());

    @Test
    void parseStringTest() {
        String ip = "222.244.144.131";
        final String cityInfo = Ip2regionUtils.getCityInfo(ip, easyRedisSupplier.get());
        assertEquals("中国|0|湖南省|长沙市|电信", cityInfo);
        String ip2 = "223.153.137.189";
        final String cityInfo2 = Ip2regionUtils.getCityInfo(ip2, easyRedisSupplier.get());
        assertEquals("中国|0|湖南省|张家界市|电信", cityInfo2);
    }

    @Test
    void parseIp2Test() {
//        String ip = "222.244.144.131";
//        StopWatch stopWatch = new StopWatch("解析IP");
//        stopWatch.start();
//        Ip2regionDTO parse = Ip2regionUtils.parse(ip);
//        stopWatch.stop();
//        assertNotNull(parse);
//        assertEquals("长沙市", parse.getCity());
//        assertEquals("湖南省", parse.getProvince());
//        stopWatch.start("2");
//        String str2 = "13.215.99.95";
//        final Ip2regionDTO parse2 = Ip2regionUtils.parse(str2);
//        assertNotNull(parse2);
//        assertEquals("亚马逊", parse2.getIsp());
////        System.out.println(Ip2regionUtils.parse("175.10.128.89"));
//        stopWatch.stop();
//        stopWatch.start("3");
//        System.out.println(Ip2regionUtils.parse("120.132.126.126"));
//        stopWatch.stop();
//        stopWatch.start("4");
//        System.out.println(Ip2regionUtils.parse("175.178.82.15"));
//        stopWatch.stop();
////        String ip5 = "43.128.14.188";
//        String ip5 = "141.147.176.125";
//        stopWatch.start("5");
//        System.out.println("--->>" + Ip2regionUtils.parse(ip5));
//        stopWatch.stop();
//        System.out.println(stopWatch.prettyPrint());
    }
}
