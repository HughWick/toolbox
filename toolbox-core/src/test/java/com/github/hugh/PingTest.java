package com.github.hugh;

import com.github.hugh.bean.dto.PingDTO;
import com.github.hugh.util.PingUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ping 工具测试类
 *
 * @author AS
 * @date 2020/10/14 9:42
 */
public class PingTest {

    @Test
    void testSend() {
        String ip1 = "223.5.5.5";
        boolean send = PingUtils.send(ip1, 3, 1000);
        assertTrue(send);
//        List<String> batch = PingUtils.batch(ip1, 4, 1000);
//        batch.forEach(System.out::printf);
//        assertEquals(4, batch.size());
//        System.out.println("-2-->" +);
    }

    // 测试调用系统命令的ping
    @Test
    void testPing() {
        String ip1 = "223.5.5.5";
        PingDTO ping2 = PingUtils.ping(ip1);
        assertEquals(0, ping2.getStatus());
        String ip2 = "192.168.10.11";
        PingDTO ping1 = PingUtils.ping(ip2,3,500);
        assertEquals(-1, ping1.getStatus());
        try {
            String ip3 = "192.168.1.45";
            int connectedCount = PingUtils.getConnectedCount(ip3, 5, 1000);
            assertEquals(0, connectedCount);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Test
    void test04() {
//        for (int i = 0; i < 65535; i++) {
//            String s = ScannerPortisAlive(i);
//            if (s.equals("OPEN")) {
//                System.out.println(i + "--->>");
//            }
//        }
    }

    static public String ScannerPortisAlive(int port) {
        String result = "OPEN";
        Socket socket = null;
        try {
            socket = new Socket();
//            InetAddress ip = InetAddress.getLocalHost();
            SocketAddress address = new InetSocketAddress("222.244.144.131", port);
            socket.connect(address, 2);
            socket.close();
//            Socket testPortSocket = new Socket(HostIP, port);
//            testPortSocket.close();
        } catch (IOException e) {
            result = "CLOSE";
        }
        return result;
    }
}
