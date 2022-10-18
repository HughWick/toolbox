package com.github.hugh;

import com.github.hugh.util.PingUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

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
            System.out.println("--2->>" + PingUtils.getConnectedCount("192.168.1.45", 5, 4000));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Test
    public void test03() {
        System.out.println("---1>>" + PingUtils.ping("192.168.1.25"));
        System.out.println("-2-->>" + PingUtils.ping("192.168.1.236", 2, 1000));
    }

    @Test
    void test04() {
        for (int i = 0; i < 65535; i++) {
            String s = ScannerPortisAlive(i);
            if (s.equals("OPEN")) {
                System.out.println(i + "--->>");
            }
        }
    }

    static public String ScannerPortisAlive(int port) {
        String result = "OPEN";
        Socket socket = null;
        try {
            socket = new Socket();
//            InetAddress ip = InetAddress.getLocalHost();
            SocketAddress address = new InetSocketAddress("222.244.144.131", port);
            socket.connect(address,2);
            socket.close();
//            Socket testPortSocket = new Socket(HostIP, port);
//            testPortSocket.close();
        } catch (IOException e) {
            result = "CLOSE";
        }
        return result;
    }
}
