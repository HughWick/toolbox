package com.github.hugh.components;

import com.github.hugh.bean.dto.Ip2regionDTO;
import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.io.StreamUtils;
import com.github.hugh.util.ip.Ip2RegeinTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IP 解析测试工具
 *
 * @author AS
 * @date 2021/2/23 14:23
 */
public class IpResolverTest {

    /**
     * ip数据文件目录
     */
    private static final String XDB_PATH = "/ip2region/ip2region.xdb";

    private static final Supplier<byte[]> easyRedisSupplier = () -> {
        InputStream resourceAsStream = Ip2RegeinTest.class.getResourceAsStream(XDB_PATH);
        try {
            return StreamUtils.toByteArray(resourceAsStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };

//    @Test
//    public void completeTest() {
//        String ip1 = "192.168.1.191";
//        final String str1 = IpResolver.on(ip1).getComplete();
//        assertEquals("内网IP", str1);
//        String ip2 = "175.8.167.6";
//        final String str2 = IpResolver.on(ip2, easyRedisSupplier.get()).getComplete();
//        assertEquals("湖南省长沙市", str2);
//        String ip3 = "154.18.161.64";
//        final String str3 = IpResolver.on(ip3, easyRedisSupplier.get()).getComplete();
//        assertNull(str3);
//        String ip4 = "";
//        final String str4 = IpResolver.on("", easyRedisSupplier.get());
//        assertEquals(ip4 , str4);
//    }

    @Test
    void getCompleteTest() {
        String ip1 = "192.168.1.191";
        final Ip2regionDTO str1 = IpResolver.on(ip1, easyRedisSupplier.get()).parse();
        assertEquals("0", str1.getRegion());
        String ip2 = "175.8.167.6";
        final Ip2regionDTO str2 = IpResolver.on(ip2, easyRedisSupplier.get()).parse();
        assertEquals("0", str2.getRegion());
    }

    @Test
    public void completeSpareTest() {
//        String ip1 = "192.168.1.191";
//        final String str1 = IpResolver.on(ip1).getComplete();
//        assertEquals("内网IP", str1);
        String ip2 = "175.8.167.6";
        final String str2 = IpResolver.on(ip2, easyRedisSupplier.get()).setSpare("-").getComplete();
        assertEquals("湖南省-长沙市", str2);
        String ip3 = "154.18.161.64";
        final String str3 = IpResolver.on(ip3, easyRedisSupplier.get()).getComplete();
        assertNull(str3);
    }

    @Test
    public void simpleTest() {
        String ip1 = "192.168.1.191";
        final String str1 = IpResolver.on(ip1, easyRedisSupplier.get()).getSimple();
        assertEquals("内网IP", str1);
        String ip2 = "175.8.167.6";
        final String str2 = IpResolver.on(ip2, easyRedisSupplier.get()).getSimple();
        assertEquals("长沙市", str2);
        String ip3 = "154.18.161.64";
        final String str3 = IpResolver.on(ip3, easyRedisSupplier.get()).getSimple();
        assertNull(str3);
    }

    @Test
    @DisplayName("Test case: parse() 方法返回 null，应抛出异常")
    void testGetComplete_ParseReturnsNull_ThrowsException() {
        IpResolver ipResolver = IpResolver.on("8.0.25.", easyRedisSupplier.get());
        ToolboxException exception = assertThrows(ToolboxException.class, ipResolver::getComplete);
        assertEquals("failed to create content cached searcher:", exception.getMessage());
        assertEquals("invalid ip address `8.0.25.`" , exception.getCause().getMessage());
    }
}
