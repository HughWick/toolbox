package com.github.hugh;

import com.github.hugh.util.ip.IpUtils;
import com.github.hugh.util.regex.RegexUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
//        System.out.println("起始IP：" + startIp + "终止IP：" + endIp);
        assertEquals("255.255.240.0", IpUtils.getNetmask(mask));

        //获得起始IP和终止IP的方法（不包含网络地址和广播地址）
        String subStart = startIp.split("\\.")[0] + "." + startIp.split("\\.")[1] + "." + startIp.split("\\.")[2] + ".";
        String subEnd = endIp.split("\\.")[0] + "." + endIp.split("\\.")[1] + "." + endIp.split("\\.")[2] + ".";
        startIp = subStart + (Integer.parseInt(startIp.split("\\.")[3]) + 1);
        endIp = subEnd + (Integer.parseInt(endIp.split("\\.")[3]) - 1);
        assertEquals("43.112.128.1", startIp);
        assertEquals("43.112.143.254", endIp);
//        System.out.println("起始IP：" + startIp + "终止IP：" + endIp);
//        System.out.println("--子网掩码->>" + IpUtils.getNetmask(mask));
        //判断一个IP是否属于某个网段
        boolean flag = IpUtils.isInRange("10.2.0.0", "10.3.0.0/17");
        assertFalse(flag);
//        System.out.println(flag);

        //根据位数查询IP数量
        int ipCount = IpUtils.getIpCount("8");
        assertEquals(16777216, ipCount);
//        System.out.println(ipCount);

        //判断是否是一个IP
//        System.out.println(IpUtils.isIP("192.168.1.0"));

        //把ip转换为数字(mysql中inet_aton()的实现)
//        System.out.println(IpUtils.ipToDouble("192.168.1.1"));

        //打印IP段所有IP（IP过多会内存溢出）
//      List<String> list = IpUtils.parseIpMaskRange(ip, mask);
//      for (String s : list){
//          System.out.println(s);
//      }
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
}
