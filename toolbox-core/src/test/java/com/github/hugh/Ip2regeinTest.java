package com.github.hugh;

import com.github.hugh.util.ip.Ip2regionUtils;
import org.junit.jupiter.api.Test;

/**
 * @author AS
 * @date 2021/2/23 14:23
 */
public class Ip2regeinTest {

    @Test
    void parseIp2Test(){
        System.out.println(Ip2regionUtils.parse("222.244.144.131"));
    }
}
