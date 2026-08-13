package com.github.hugh.components;

import com.github.hugh.bean.dto.Ip2regionDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * IP 解析测试工具
 *
 * @author AS
 * @date 2021/2/23 14:23
 */
class IpResolverTest {

    @Test
    void completeTest() {
        String ip1 = "192.168.1.191";
        final String str1 = IpResolver.on(ip1).getComplete();
        assertEquals("内网IP", str1);
        String ip2 = "175.8.167.6";
        final String str2 = IpResolver.on(ip2).getComplete();
        assertEquals("湖南省长沙市", str2);
        // 香港
        String ip3 = "154.18.161.64";
        final String str3 = IpResolver.on(ip3).getComplete();
        assertNull(str3);
        String complete = IpResolver.on(ip3).useV4().getComplete();
        assertEquals("Singapore", complete);
//        String ip4 = "";
//        final String str4 = IpResolver.on("", easyRedisSupplier.get());
//        assertEquals(ip4 , str4);
    }

    @Test
    void getCompleteTest() {
        String ip1 = "192.168.1.191";
        final Ip2regionDTO str1 = IpResolver.on(ip1).parse();
        assertEquals("内网IP", str1.getRegion());
        String ip2 = "175.8.167.6";
        final Ip2regionDTO str2 = IpResolver.on(ip2).parse();
        assertEquals("0", str2.getRegion());
        Ip2regionDTO parse = IpResolver.on(ip2).useV4().parse();
        assertEquals("CN", parse.getRegion());
        assertEquals("长沙市", parse.getCity());
    }

    @Test
    void completeSpareTest() {
//        String ip1 = "192.168.1.191";
//        final String str1 = IpResolver.on(ip1).getComplete();
//        assertEquals("内网IP", str1);
        String ip2 = "175.8.167.6";
        final String str2 = IpResolver.on(ip2).setSpare("-").getComplete();
        assertEquals("湖南省-长沙市", str2);
        String ip3 = "154.18.161.64";
        final String str3 = IpResolver.on(ip3).getComplete();
        assertNull(str3);
        String ip4 = "47.79.38.215";
        final String str4 = IpResolver.on(ip4).setSpare("|").getComplete();
        assertEquals("加利福尼亚|圣克拉拉", str4);
    }

    @Test
    void cityTest() {
        String ip1 = "192.168.1.191";
        final String str1 = IpResolver.on(ip1).getCity();
        assertEquals("内网IP", str1);
        String ip2 = "175.8.167.6";
        final String str2 = IpResolver.on(ip2).getCity();
        assertEquals("长沙市", str2);
        String ip3 = "154.18.161.64";
        final String str3 = IpResolver.on(ip3).getCity();
        assertNull(str3);
        String ip4 = "103.41.232.82";
        final String str4 = IpResolver.on(ip4).getCity();
        assertEquals("贵阳市", str4);
    }

    @Test
    @DisplayName("Test case: parse() 方法返回 null，应抛出异常")
    void testGetComplete_ParseReturnsNull_ThrowsException() {
//        IpResolver ipResolver = IpResolver.on("8.0.25.");
//        ToolboxException exception = assertThrows(ToolboxException.class, ipResolver::getComplete);
//        assertEquals("failed to create content cached searcher:", exception.getMessage());
//        assertEquals("invalid ip address `8.0.25.`", exception.getCause().getMessage());
    }

    @Test
    void completeV4Test() {
        String ip1 = "192.168.1.191";
        final String str1 = IpResolver.on(ip1)
                .useV4().setSpare("-")
                .getComplete();
        assertEquals("内网IP",str1);
        String ip2 = "175.8.167.6";
        final String str2 = IpResolver.on(ip2).getComplete();
        assertEquals("湖南省长沙市", str2);
        // 实际地址：湖南省岳阳市
        String ip3 = "39.144.192.141";
        final String str3 = IpResolver.on(ip3).getComplete();
        assertNull(str3);
        // 实际地址：湖南省长沙市
        String ip4 = "39.144.190.72";
        final String str4 = IpResolver.on(ip4).getComplete();
        assertNull(str4);
    }
}
