package com.github.hugh;

import com.github.hugh.bean.dto.PingDTO;
import com.github.hugh.util.PingUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
class PingTest {

    @Test
    void testSend() {
        String ip1 = "223.5.5.5";
        boolean send = PingUtils.send(ip1, 3, 1000);
        assertTrue(send);
    }

    // 测试调用系统命令的ping
    @Test
    void testPing() {
        String ip1 = "223.5.5.5";
        PingDTO ping1 = PingUtils.ping(ip1);
        assertEquals(0, ping1.getStatus());
        String ip2 = "172.17.10.11";
        PingDTO ping2 = PingUtils.ping(ip2, 1, 500);
        assertEquals(-1, ping2.getStatus());
    }

    // 测试正常情况下连接次数
    @Test
    void testGetConnectedCountNormal() throws IOException {
        String ipAddress = "8.8.8.8";  // 你可以使用本地网络中的 IP 或者某个测试用的 IP
        int pingCount = 4;
        int timeOut = 1000;
        int connectedCount = PingUtils.getConnectedCount(ipAddress, pingCount, timeOut);
        // 这里我们假设网络连接正常，返回的连接次数应该等于 pingCount
        assertEquals(pingCount, connectedCount);
    }

    // 测试连接失败，返回-1
    @Test
    void testGetConnectedCountConnectionFailure() throws IOException {
        String ipAddress = "10.255.255.255";  // 不存在或不可达的 IP
        int pingCount = 4;
        int timeOut = 1000;
        int connectedCount = PingUtils.getConnectedCount(ipAddress, pingCount, timeOut);
        // 假设连接失败，方法应返回 -1
        assertEquals(0, connectedCount);
    }

    // 测试没有 ping 响应
    @Test
    void testGetConnectedCountNoResponse() throws IOException {
        String ipAddress = "172.17.1.100";  // 未响应的 IP 地址
        int pingCount = 4;
        int timeOut = 1000;
        int connectedCount = PingUtils.getConnectedCount(ipAddress, pingCount, timeOut);
        // 如果没有收到类似 `=xxms TTL=xx` 的响应，连接次数应该是 0
        assertEquals(0, connectedCount);
    }

    // 测试异常的 pingCount 参数
    @Test
    void testGetConnectedCountInvalidPingCount() throws IOException {
        String ipAddress = "192.168.1.1";  // 正常的 IP 地址
        int pingCount = -1;  // 非法的 pingCount
        int timeOut = 1000;
        // 假设我们的函数能正常处理异常情况，并返回0或-1
        int connectedCount = PingUtils.getConnectedCount(ipAddress, pingCount, timeOut);
        assertEquals(0, connectedCount);  // 如果 pingCount 为负值，应该返回-1
    }

    // 测试异常的 timeOut 参数
    @Test
    void testGetConnectedCountInvalidTimeOut() {
        String ipAddress = "192.168.1.1";  // 正常的 IP 地址
        int pingCount = 4;
        int timeOut = -1;  // 非法的 timeout
        // 假设我们的函数能正常处理异常情况，并返回0或-1
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> PingUtils.getConnectedCount(ipAddress, pingCount, timeOut));
        assertEquals("Timeout must be a positive value.", illegalArgumentException.getMessage());  // 如果 timeOut 为负值，应该返回-1
    }

    // 测试多次 ping 响应
    @Test
    public void testGetConnectedCountMultipleResponses() throws IOException {
        String ipAddress = "192.168.1.1";  // 假设这是一个测试用的 IP 地址
        int pingCount = 5;
        int timeOut = 1000;
        // 假设输出的 ping 响应有 5 次有效的响应
        int connectedCount = PingUtils.getConnectedCount(ipAddress, pingCount, timeOut);
        // 如果每次 ping 都成功，返回的连接次数应该和 pingCount 一样
        assertEquals(0, connectedCount);
    }

    @Test
    void testParseLinuxResult() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        String result = "64 bytes from 8.8.8.8: icmp_seq=1 ttl=116 time=3.14 ms\n" +
                "64 bytes from 8.8.8.8: icmp_seq=2 ttl=116 time=3.50 ms\n" +
                "64 bytes from 8.8.8.8: icmp_seq=3 ttl=116 time=3.27 ms\n" +
                "64 bytes from 8.8.8.8: icmp_seq=4 ttl=116 time=3.12 ms\n" +
                "--- 8.8.8.8 ping statistics ---\n" +
                "4 packets transmitted, 4 received, 0% packet loss, time 3000ms\n" +
                "rtt min/avg/max/mdev = 3.123/3.248/3.502/0.168 ms";

        PingDTO ping = new PingDTO();
        Method parseLinuxResultMethod = PingUtils.class.getDeclaredMethod("parseLinuxResult", String.class, PingDTO.class);
        parseLinuxResultMethod.setAccessible(true);
        parseLinuxResultMethod.invoke(null, result, ping);
        assertEquals(0, ping.getLoss()); // 0% packet loss
        assertEquals(3.248, ping.getDelay(), 0.001); // average delay
        assertEquals(0, ping.getStatus());
//        String realStr2 = "PING 223.5.5.5 (223.5.5.5) 56(84) bytes of data.\n" +
//                "64 bytes from 223.5.5.5: icmp_seq=1 ttl=113 time=18.6 ms\n" +
//                "64 bytes from 223.5.5.5: icmp_seq=2 ttl=113 time=18.5 ms\n" +
//                "64 bytes from 223.5.5.5: icmp_seq=3 ttl=113 time=29.0 ms\n" +
//                "64 bytes from 223.5.5.5: icmp_seq=4 ttl=113 time=27.3 ms\n" +
//                "--- 223.5.5.5 ping statistics ---\n" +
//                "4 packets transmitted, 4 received, 0% packet loss, time 3003ms\n" +
//                "rtt min/avg/max/mdev = 18.539/23.413/29.042/4.832 ms";


        String realStr3 = "PING 144.144.114.114 (144.144.114.114) 56(84) bytes of data.\n" +
                "--- 144.144.114.114 ping statistics ---\n" +
                "5 packets transmitted, 0 received, 100% packet loss, time 3999ms";
        PingDTO ping3 = new PingDTO();
        parseLinuxResultMethod.invoke(null, realStr3, ping3);
        assertEquals(0, ping3.getLoss()); // 0% packet loss
        assertEquals(-1, ping3.getStatus());
    }

    @Test
    void testParseLinuxResultDelay() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String lossStr1 = "PING 141.147.177.90 (141.147.177.90) 56(84) bytes of data.\n" +
                "64 bytes from 141.147.177.90: icmp_seq=1 ttl=49 time=170 ms\n" +
                "64 bytes from 141.147.177.90: icmp_seq=2 ttl=49 time=166 ms\n" +
                "64 bytes from 141.147.177.90: icmp_seq=7 ttl=49 time=170 ms\n" +
                "64 bytes from 141.147.177.90: icmp_seq=8 ttl=49 time=167 ms\n" +
                "--- 141.147.177.90 ping statistics ---\n" +
                "8 packets transmitted, 4 received, 50% packet loss, time 7003ms\n" +
                "rtt min/avg/max/mdev = 166.320/168.759/170.518/1.722 ms\n";
        PingDTO ping = new PingDTO();
        Method parseLinuxResultMethod = PingUtils.class.getDeclaredMethod("parseLinuxResult", String.class, PingDTO.class);
        parseLinuxResultMethod.setAccessible(true);
        parseLinuxResultMethod.invoke(null, lossStr1, ping);
        assertEquals(168.759, ping.getDelay(), 0.001); // average delay

        String lossStr2 = "PING 114.114.114.114 (114.114.114.114) 56(84) bytes of data.\n" +
                "--- 114.114.114.114 ping statistics ---\n" +
                "6 packets transmitted, 0 received, 100% packet loss, time 4999ms\n";
        PingDTO ping2 = new PingDTO();
        parseLinuxResultMethod.invoke(null, lossStr2, ping2);
        assertEquals(-1.0, ping2.getDelay());
        assertEquals(-1, ping2.getStatus());
    }


    @Test
    void batch_SuccessfulExecution_ReturnsNonEmptyList() {
        // Arrange
        String ipAddress = "8.8.8.8"; // Example IP address
        int pingCount = 4; // Example ping count
        int timeOut = 3000; // Example timeout
        // Act
        List<String> result = PingUtils.batch(ipAddress, pingCount, timeOut);
        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

//    @Test
//    void batch_InvalidIpAddress_ThrowsException() {
//        // Arrange
//        String invalidIpAddress = "114.114.114.114";
//        int pingCount = 4; // Example ping count
//        int timeOut = 3000; // Example timeout
//        // Act & Assert
//        assertThrows(ToolboxException.class, () -> PingUtils.batch(invalidIpAddress, pingCount, timeOut));
//    }


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
