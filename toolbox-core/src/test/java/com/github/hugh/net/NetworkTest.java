package com.github.hugh.net;

import com.github.hugh.util.net.NetworkUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.net.InetAddress;

/**
 * @author Hugh
 * @since
 **/
public class NetworkTest {

    @Test
    void test01() {
        System.out.println("=1==>>" + NetworkUtils.ping("www.baidu.com"));
        System.out.println("==2=>>" + NetworkUtils.ping("www.google.com", 500));
    }

    @Test
    void test02() {
        try {
            int count = 0;
            int timeout = 4000;
            String ip = "192.168.1.32";
            StopWatch stopWatch = new StopWatch();
            stopWatch.start("开始");
            while (count < 3) {
                System.out.println("=====开始=======");
                InetAddress byName = InetAddress.getByName(ip);
                boolean reachable = byName.isReachable(timeout);
                System.out.println("==结果=>>>" + reachable);
                if (reachable) {
                    return;
                }
                count++;
            }
            stopWatch.stop();
            System.out.println(stopWatch.prettyPrint());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
