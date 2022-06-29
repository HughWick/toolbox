package com.github.hugh;

import com.github.hugh.bean.dto.Ip2regionDTO;
import com.github.hugh.util.ip.Ip2regionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

/**
 * @author AS
 * @date 2021/2/23 14:23
 */
public class Ip2regeinTest {

    @Test
    void parseIp2Test(){
        StopWatch stopWatch =new StopWatch("解析IP");
        stopWatch.start();
        Ip2regionDTO parse = Ip2regionUtils.parse("222.244.144.131");
        stopWatch.stop();
        System.out.println(parse);
        stopWatch.start("2");
        System.out.println(Ip2regionUtils.parse("175.10.128.89"));
        stopWatch.stop();
        stopWatch.start("3");
        System.out.println(Ip2regionUtils.parse("120.132.126.126"));
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }
}
