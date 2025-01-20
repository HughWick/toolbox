package com.github.hugh.util.net;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

/**
 * java 网络工具测试类
 *
 * @author Hugh
 **/
@Slf4j
class NetworkTest {

    @Test
    void testPing() {
        try {
            assertTrue(NetworkUtils.ping("www.baidu.com"));
        } catch (Exception e) {
            System.out.println("Error pinging www.baidu.com: " + e.getMessage());
        }
        assertFalse(NetworkUtils.ping("www.google321.com", 500));
        assertTrue(NetworkUtils.ping("223.5.5.5" , 400));
    }
//
//    @Test
//    void testPingFall(){
//        assertFalse(NetworkUtils.pingFail("223.5.5.5" , 400));
//        assertTrue(NetworkUtils.pingFail("114.114.114.132" , 40));
//        assertTrue(NetworkUtils.pingFail("114.114.114.142"  ));
//    }

    @Test
    void testPingValidIP() {
        // 使用一个有效的 IP 地址，假设 8.8.8.8 是可达的
        assertTrue(NetworkUtils.ping("8.8.8.8", 200));
    }

    @Test
    void testPingInvalidIP() {
        // 使用一个无效的 IP 地址，假设该 IP 不存在
        assertFalse(NetworkUtils.ping("999.999.999.999", 200));
    }

    @Test
    void testPingValidDomain() {
        // 使用一个有效的域名，假设 www.baidu.com 是可达的
        assertTrue(NetworkUtils.ping("www.baidu.com", 200));
    }

    @Test
    void testPingInvalidDomain() {
        // 使用一个不存在的域名，假设 www.invalid-domain.com 不存在
        assertFalse(NetworkUtils.ping("www.invalid-domain.com", 200));
    }

    @Test
    void testPingWithTimeout() {
        // 使用有效的 IP 地址，但设置非常短的超时（1 毫秒），应该返回 false
        assertFalse(NetworkUtils.ping("8.8.8.8", 1));
    }

    @Test
    void testPingLocalhost() {
        // 使用回环地址（127.0.0.1），应该返回 true
        assertTrue(NetworkUtils.ping("127.0.0.1", 200));
    }

    @Test
    void testPingEmptyIP() {
        // 使用空字符串，应该返回 false
        assertFalse(NetworkUtils.ping("", 200));
    }

    @Test
    void testPingNullIP() {
        // 使用 null，应该返回 false
        assertFalse(NetworkUtils.ping(null, 200));
    }

    @Test
    void testPingWithLongTimeout() {
        // 使用有效的 IP 地址，但设置较长的超时（5000 毫秒），应该返回 true
        assertTrue(NetworkUtils.ping("8.8.8.8", 5000));
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


    @Test
    public void testPingFailDefaultTimeout() {
        // 测试一个无法到达的IP地址，应该返回true（失败）
        String unreachableIp = "192.0.2.0"; // 这是一个保留的无法到达的IP地址
        boolean result = NetworkUtils.pingFail(unreachableIp);
        assertTrue(result, "Expected ping to fail for unreachable IP address with default timeout");

        // 测试一个可以到达的IP地址，应该返回false（成功）
        String reachableIp = "8.8.8.8"; // Google的公共DNS地址
        result = NetworkUtils.pingFail(reachableIp);
        assertFalse(result, "Expected ping to succeed for reachable IP address with default timeout");
    }
    @Test
    public void testPingFailWithCustomTimeout() {
        // 使用较短的超时测试无法到达的IP地址
        String unreachableIp = "192.0.2.0";
        int shortTimeout = 100; // 超时100毫秒
        boolean result = NetworkUtils.pingFail(unreachableIp, shortTimeout);
        assertTrue(result, "Expected ping to fail for unreachable IP address with short timeout");

        // 使用较长的超时测试可以到达的IP地址
        String reachableIp = "8.8.8.8";
        int longTimeout = 1000; // 超时1000毫秒
        result = NetworkUtils.pingFail(reachableIp, longTimeout);
        assertFalse(result, "Expected ping to succeed for reachable IP address with long timeout");
    }


    @Test
    public void testPingFailWithUnreachableIp() {
        // 测试一个永远不可到达的IP地址
        String unreachableIp = "192.0.2.0"; // 这是一个保留的不可到达的IP
        boolean result = NetworkUtils.pingFail(unreachableIp, 500);
        assertTrue(result, "Expected ping to fail for unreachable IP address");
    }

    @Test
    public void testPingFailWithReachableIp() {
        // 测试Google DNS服务器（通常可达）
        String reachableIp = "8.8.8.8"; // Google的公共DNS地址
        boolean result = NetworkUtils.pingFail(reachableIp, 500);
        assertFalse(result, "Expected ping to succeed for reachable IP address");
    }

    @Test
    public void testConnect() throws IOException {
        // 测试连接到一个开放的端口（例如，Google的HTTP端口）
        String hostname = "baidu.com";
        int port = 80;
        try (Socket socket = NetworkUtils.connect(hostname, port)) {
            assertNotNull(socket, "Expected a valid socket connection");
            assertTrue(socket.isConnected(), "Expected socket to be connected");
        }
    }

    @Test
    public void testConnectFail() {
        // 测试连接到一个不存在的端口
        String hostname = "google.com";
        int port = 9999; // 可能无法连接
        assertThrows(IOException.class, () -> NetworkUtils.connect(hostname, port), "Expected IOException for connection failure");
    }
}
