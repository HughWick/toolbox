package com.github.hugh;

import com.github.hugh.bean.dto.Ip2regionDTO;
import com.github.hugh.util.ip.Ip2regionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * IP 解析测试工具
 *
 * @author AS
 * @date 2021/2/23 14:23
 */
class Ip2regeinTest {

    @Test
    void parseIp2Test() {
        StopWatch stopWatch = new StopWatch("解析IP");
        stopWatch.start();
        Ip2regionDTO parse = Ip2regionUtils.parse("222.244.144.131");
        stopWatch.stop();
        assertNotNull(parse);
        assertEquals("长沙市", parse.getCity());
        assertEquals("湖南省", parse.getProvince());
        stopWatch.start("2");
        String str2 = "13.215.99.95";
        final Ip2regionDTO parse2 = Ip2regionUtils.parse(str2);
        assertNotNull(parse2);
        assertEquals("亚马逊", parse2.getIsp());
//        System.out.println(Ip2regionUtils.parse("175.10.128.89"));
        stopWatch.stop();
        stopWatch.start("3");
        System.out.println(Ip2regionUtils.parse("120.132.126.126"));
        stopWatch.stop();
        stopWatch.start("4");
        System.out.println(Ip2regionUtils.parse("175.178.82.15"));
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }
}
