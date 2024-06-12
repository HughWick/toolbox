package com.github.hugh.net;

import com.github.hugh.util.net.NetworkUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.net.InetAddress;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * java 网络工具测试类
 *
 * @author Hugh
 **/
@Slf4j
class NetworkTest {

    //
    @Test
    void testPing() {
        assertTrue(NetworkUtils.ping("www.baidu.com"));
        assertFalse(NetworkUtils.ping("www.google.com", 500));
//        System.out.println("=1==>>" + NetworkUtils.ping("www.baidu.com"));
//        System.out.println("==2=>>" + NetworkUtils.ping("www.google.com", 500));
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
//                System.out.println("=====开始=======");
                InetAddress byName = InetAddress.getByName(ip);
                boolean reachable = byName.isReachable(timeout);
//                System.out.println("==结果=>>>" + reachable);
                if (reachable) {
                    return;
                }
                count++;
            }
            stopWatch.stop();
            log.info(stopWatch.prettyPrint());
//            System.out.println(stopWatch.prettyPrint());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Test
    void testIsConnect() {
        boolean connect = NetworkUtils.isConnect("222.244.144.131", 10010);
        assertTrue(connect);
        boolean connect2 = NetworkUtils.isConnect("192.168.1.86", 9567);
        assertFalse(connect2);
        boolean connect3 = NetworkUtils.isConnect("www.baidu.com", 80, 1000);
        assertTrue(connect3);
    }

    @Test
    void testIsNotConnect() {
        boolean connect = NetworkUtils.isNotConnect("www.google.com", 80,2000);
        assertTrue(connect);
        boolean connect2 = NetworkUtils.isNotConnect("172.17.0.6", 80);
        assertTrue(connect2);
//        boolean connect3 = NetworkUtils.isConnect("www.baidu.com", 80, 1000);
//        assertTrue(connect3);
    }

    @Test
    void testPort() {
        boolean localsPortUsing = NetworkUtils.isLocalsPortUsing(8080);
//        System.out.println(localsPortUsing);
//        assertTrue(localsPortUsing);
        assertFalse(localsPortUsing);
    }
}
