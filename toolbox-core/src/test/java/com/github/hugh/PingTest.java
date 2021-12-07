package com.github.hugh;

import com.github.hugh.util.PingUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.net.InetAddress;

/**
 * @author AS
 * @date 2020/10/14 9:42
 */
public class PingTest {

    @Test
    public void test() {
        System.out.println("-1-->" + PingUtils.send("192.168.1.45", 3, 1000));
        System.out.println("-2-->" + PingUtils.batch("192.168.1.45", 4, 1000));
    }

    @Test
    public void test02() {
        System.out.println("--1->>" + PingUtils.ping("192.168.1.45"));
        try {
            System.out.println("--2->>" + PingUtils.getConnectedCount("192.168.1.45",5,4000));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Test
    public void test03() {
        System.out.println("---1>>" + PingUtils.ping("192.168.1.25"));
        System.out.println("-2-->>" + PingUtils.ping("192.168.1.236", 9, 1000));
    }

    public static void main(String[] args) {
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
