package com.github.hugh.util.ip;

import com.github.hugh.util.regex.RegexUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * IP工具测试类
 *
 * @author AS
 * @date 2020/9/11 16:42
 */
class IpTest {

    // 测试验证IP
    @Test
    void testVerify() {
        String str1 = "113.218.2.1223";
        assertTrue(RegexUtils.isNotIp(str1));
        String str2 = "113.218.2.122";
        assertTrue(RegexUtils.isIp(str2));
        assertTrue(RegexUtils.isIp(IpUtils.random()));
        String str3 = "43.113.33.124";
        assertTrue(RegexUtils.isIp(str3));
    }

    // 测试计算IP地址长度
    @Test
    void test002() {
        String ip = "43.112.134.143";//ip
        String mask = "20";//位数，如果只知道子网掩码不知道位数的话在参考getMaskMap()方法

        //获得起始IP和终止IP的方法（包含网络地址和广播地址）
        String startIp = IpUtils.calcBeginIp(ip, mask);
        String endIp = IpUtils.calcEndIp(ip, mask);
        // 起始IP
        assertEquals("43.112.128.0", startIp);
        assertEquals("43.112.143.255", endIp);
        assertEquals("255.255.240.0", IpUtils.getNetmask(mask));

        //获得起始IP和终止IP的方法（不包含网络地址和广播地址）
        String subStart = startIp.split("\\.")[0] + "." + startIp.split("\\.")[1] + "." + startIp.split("\\.")[2] + ".";
        String subEnd = endIp.split("\\.")[0] + "." + endIp.split("\\.")[1] + "." + endIp.split("\\.")[2] + ".";
        startIp = subStart + (Integer.parseInt(startIp.split("\\.")[3]) + 1);
        endIp = subEnd + (Integer.parseInt(endIp.split("\\.")[3]) - 1);
        assertEquals("43.112.128.1", startIp);
        assertEquals("43.112.143.254", endIp);
        //判断一个IP是否属于某个网段
        boolean flag = IpUtils.isInRange("10.2.0.0", "10.3.0.0/17");
        assertFalse(flag);

        //根据位数查询IP数量
        int ipCount = IpUtils.getIpCount("8");
        assertEquals(16777216, ipCount);
    }

    // 测试IP是否归属统一个网段内
    @Test
    void testIsSameNetworkSegment() {
        String ip1 = "43.115.39.203";
        String ip2 = "43.115.36.1";
        String mask1 = "255.255.252.0";
        assertFalse(IpUtils.isSameNetworkSegment("555.168.0.1", ip2, mask1));
        assertFalse(IpUtils.isSameNetworkSegment(ip1, "43.113.36.1", mask1));
        assertTrue(IpUtils.isSameNetworkSegment(ip1, ip2, mask1));
        assertTrue(IpUtils.isSameNetworkSegment("43.115.36.113", ip2, mask1));
        assertTrue(IpUtils.isSameNetworkSegment("43.115.37.113", ip2, mask1));
        assertTrue(IpUtils.isSameNetworkSegment("43.115.38.113", ip2, mask1));
        assertTrue(IpUtils.isSameNetworkSegment("43.115.39.113", ip2, mask1));
        assertTrue(IpUtils.isSameNetworkSegment("43.115.88.2", "43.115.88.1", mask1));
        assertTrue(IpUtils.isNotSameNetworkSegment("43.115.87.2", "43.115.88.1", mask1));
        assertTrue(IpUtils.isSameNetworkSegment("43.115.88.252", "43.115.88.1", mask1));
        assertFalse(IpUtils.isSameNetworkSegment("115.91.255.132", "43.115.88.1", mask1));

        String mask2 = "255.255.255.0";
        assertTrue(IpUtils.isSameNetworkSegment("43.115.33.1", "43.115.33.155", mask2));
        assertFalse(IpUtils.isSameNetworkSegment("192.168.1.213", "192.168.2.2", mask2));
        assertFalse(IpUtils.isSameNetworkSegment("115.91.255.132", "43.115.88.1", mask1));
        assertFalse(IpUtils.isSameNetworkSegment("192.168.10.220", "192.168.11.1", mask2));
        assertTrue(IpUtils.isSameNetworkSegment("192.168.10.220", "192.168.10.1", mask2));
    }


    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private IpUtils ipUtils;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void get_ValidXForwardedFor_ReturnsFirstIp() {
        when(request.getHeader("x-forwarded-for")).thenReturn("192.168.1.110, 192.168.1.120");
        String ip = IpUtils.get(request);
        assertEquals("192.168.1.110", ip);
    }

    @Test
    public void get_XForwardedForNull_ReturnsProxyClientIp() {
        when(request.getHeader("x-forwarded-for")).thenReturn(null);
        when(request.getHeader("Proxy-Client-IP")).thenReturn("192.168.1.120");
        String ip = IpUtils.get(request);
        assertEquals("192.168.1.120", ip);
    }

    @Test
    public void get_AllHeadersUnknown_ReturnsRemoteAddr() {
        when(request.getHeader("x-forwarded-for")).thenReturn("unknown");
        when(request.getHeader("Proxy-Client-IP")).thenReturn("unknown");
        when(request.getHeader("WL-Proxy-Client-IP")).thenReturn("unknown");
        when(request.getHeader("HTTP_CLIENT_IP")).thenReturn("unknown");
        when(request.getHeader("HTTP_X_FORWARDED_FOR")).thenReturn("unknown");
        when(request.getRemoteAddr()).thenReturn("192.168.1.100");
        String ip = IpUtils.get(request);
        assertEquals("192.168.1.100", ip);
    }

    @Test
    public void get_ValidHttpXForwardedFor_ReturnsIp() {
        when(request.getHeader("HTTP_X_FORWARDED_FOR")).thenReturn("192.168.1.130");
        String ip = IpUtils.get(request);
        assertEquals("192.168.1.130", ip);
    }

    @Test
    public void getNetmask_ValidMaskBit_ReturnsCorrectNetmask() {
        Assertions.assertEquals("128.0.0.0", IpUtils.getNetmask("1"));
        Assertions.assertEquals("192.0.0.0", IpUtils.getNetmask("2"));
        Assertions.assertEquals("224.0.0.0", IpUtils.getNetmask("3"));
        Assertions.assertEquals("240.0.0.0", IpUtils.getNetmask("4"));
        Assertions.assertEquals("248.0.0.0", IpUtils.getNetmask("5"));
        Assertions.assertEquals("252.0.0.0", IpUtils.getNetmask("6"));
        Assertions.assertEquals("254.0.0.0", IpUtils.getNetmask("7"));
        Assertions.assertEquals("255.0.0.0", IpUtils.getNetmask("8"));
        Assertions.assertEquals("255.128.0.0", IpUtils.getNetmask("9"));
        Assertions.assertEquals("255.192.0.0", IpUtils.getNetmask("10"));
        Assertions.assertEquals("255.224.0.0", IpUtils.getNetmask("11"));
        Assertions.assertEquals("255.240.0.0", IpUtils.getNetmask("12"));
        Assertions.assertEquals("255.248.0.0", IpUtils.getNetmask("13"));
        Assertions.assertEquals("255.252.0.0", IpUtils.getNetmask("14"));
        Assertions.assertEquals("255.254.0.0", IpUtils.getNetmask("15"));
        Assertions.assertEquals("255.255.0.0", IpUtils.getNetmask("16"));
        Assertions.assertEquals("255.255.128.0", IpUtils.getNetmask("17"));
        Assertions.assertEquals("255.255.192.0", IpUtils.getNetmask("18"));
        Assertions.assertEquals("255.255.224.0", IpUtils.getNetmask("19"));
        Assertions.assertEquals("255.255.240.0", IpUtils.getNetmask("20"));
        Assertions.assertEquals("255.255.248.0", IpUtils.getNetmask("21"));
        Assertions.assertEquals("255.255.252.0", IpUtils.getNetmask("22"));
        Assertions.assertEquals("255.255.254.0", IpUtils.getNetmask("23"));
        Assertions.assertEquals("255.255.255.0", IpUtils.getNetmask("24"));
        Assertions.assertEquals("255.255.255.128", IpUtils.getNetmask("25"));
        Assertions.assertEquals("255.255.255.192", IpUtils.getNetmask("26"));
        Assertions.assertEquals("255.255.255.224", IpUtils.getNetmask("27"));
        Assertions.assertEquals("255.255.255.240", IpUtils.getNetmask("28"));
        Assertions.assertEquals("255.255.255.248", IpUtils.getNetmask("29"));
        Assertions.assertEquals("255.255.255.252", IpUtils.getNetmask("30"));
        Assertions.assertEquals("255.255.255.254", IpUtils.getNetmask("31"));
        Assertions.assertEquals("255.255.255.255", IpUtils.getNetmask("32"));
    }

    // 测试：无效的子网掩码位，应该返回 "-1"
    @Test
    void getNetmask_InvalidMaskBit_ReturnsMinusOne() {
        // 传入无效的子网掩码位，应该返回 "-1"
        Assertions.assertEquals("-1", IpUtils.getNetmask("0"));
        Assertions.assertEquals("-1", IpUtils.getNetmask("33"));
        Assertions.assertEquals("-1", IpUtils.getNetmask("a"));
        Assertions.assertEquals("-1", IpUtils.getNetmask("100"));
    }

    // 测试：子网掩码为 32，应该返回单个 IP 地址
    @Test
    void parseIpMaskRange_Mask32_ReturnsSingleIp() {
        // 子网掩码为 32，应该返回单个 IP 地址
        List<String> ipList = IpUtils.parseIpMaskRange("192.168.1.1", "32");
        Assertions.assertEquals(1, ipList.size());  // 返回一个 IP 地址
        Assertions.assertTrue(ipList.contains("192.168.1.1"));  // 确保包含该 IP 地址
    }

    // 测试：子网掩码为 31，应该返回两个 IP 地址
    @Test
    void parseIpMaskRange_Mask31_ReturnsTwoIps() {
        // 子网掩码为 31，应该返回两个 IP 地址
        List<String> ipList = IpUtils.parseIpMaskRange("192.168.1.1", "31");
        Assertions.assertEquals(2, ipList.size());  // 返回两个 IP 地址
        Assertions.assertTrue(ipList.contains("192.168.1.0"));  // 确保包含 192.168.1.0
        Assertions.assertTrue(ipList.contains("192.168.1.1"));  // 确保包含 192.168.1.1
    }

    // 测试：子网掩码为 24，应该返回正确的范围（256个 IP 地址）
    @Test
    void parseIpMaskRange_Mask24_ReturnsCorrectRange() {
        // 子网掩码为 24，应该返回 256 个 IP 地址
        List<String> ipList = IpUtils.parseIpMaskRange("192.168.1.1", "24");
        Assertions.assertEquals(256, ipList.size());  // 返回 256 个 IP 地址
        Assertions.assertTrue(ipList.contains("192.168.1.0"));  // 确保包含 192.168.1.0
        Assertions.assertTrue(ipList.contains("192.168.1.255"));  // 确保包含 192.168.1.255
    }

    // 测试：子网掩码为 1，应该返回正确的范围，并且限制返回的 IP 地址数量为 500000
    @Test
    void parseIpMaskRange_Mask1_ReturnsCorrectRange() {
        // 子网掩码为 1，应该返回 500000 个 IP 地址
        int limit = 500000;  // 限制返回的 IP 地址数量为 500000
        List<String> ipList = IpUtils.parseIpMaskRange("192.168.1.1", "1", limit);
        Assertions.assertEquals(limit, ipList.size());  // 返回的 IP 地址数量应该是 500000
        Assertions.assertTrue(ipList.contains("128.0.0.0"));  // 确保包含 128.0.0.0
        String lastElement = ipList.get(ipList.size() - 1);
        System.out.println(lastElement);  // 打印最后一个 IP 地址
        Assertions.assertTrue(ipList.contains("128.7.161.31"));  // 确保包含 128.7.161.31
    }

    // 测试：子网掩码为 32，使用不同的 IP 地址，应该返回单个 IP 地址
    @Test
    void parseIpMaskRange_Mask32_ReturnsSingleIpDifferentIp() {
        // 使用不同的 IP 地址（"10.0.0.1"），子网掩码为 32，应该返回单个 IP 地址
        List<String> ipList = IpUtils.parseIpMaskRange("10.0.0.1", "32");
        Assertions.assertEquals(1, ipList.size());  // 返回一个 IP 地址
        Assertions.assertTrue(ipList.contains("10.0.0.1"));  // 确保包含该 IP 地址
    }

    // 测试用例 1：简单的 IP 范围
    @Test
    void testSimpleRange() {
        List<String> result = IpUtils.parseIpRange("192.168.0.1", "192.168.0.3");
        assertEquals(3, result.size());
        assertTrue(result.contains("192.168.0.1"));
        assertTrue(result.contains("192.168.0.2"));
        assertTrue(result.contains("192.168.0.3"));
    }

    // 测试用例 2：跨越不同的子网段
    @Test
    void testRangeAcrossSubnets() {
        List<String> result = IpUtils.parseIpRange("192.168.0.1", "192.168.1.2");
        assertTrue(result.size() > 0);
        assertTrue(result.contains("192.168.0.1"));
        assertTrue(result.contains("192.168.1.2"));
        assertTrue(result.contains("192.168.0.100"));  // 中间的IP地址应该也在范围内
    }

    // 测试用例 3：仅在同一个子网段内
    @Test
    void testSameSubnetRange() {
        List<String> result = IpUtils.parseIpRange("192.168.1.5", "192.168.1.7");
        assertEquals(3, result.size());
        assertTrue(result.contains("192.168.1.5"));
        assertTrue(result.contains("192.168.1.6"));
        assertTrue(result.contains("192.168.1.7"));
    }

    // 测试用例 4：包含完整的子网范围
    @Test
    void testFullSubnetRange() {
        List<String> result = IpUtils.parseIpRange("192.168.0.0", "192.168.0.255");
        assertEquals(256, result.size());  // 应该包含 192.168.0.0 到 192.168.0.255 共 256 个IP地址
        assertTrue(result.contains("192.168.0.0"));
        assertTrue(result.contains("192.168.0.255"));
    }

    // 测试用例 5：从一个子网到另一个子网
    @Test
    void testRangeFromOneSubnetToAnother() {
        List<String> result = IpUtils.parseIpRange("10.0.0.0", "10.0.0.3");
        assertEquals(4, result.size());
        assertTrue(result.contains("10.0.0.0"));
        assertTrue(result.contains("10.0.0.3"));
    }
}
