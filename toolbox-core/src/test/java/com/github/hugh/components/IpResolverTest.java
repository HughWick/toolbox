package com.github.hugh.components;

import com.github.hugh.util.io.StreamUtils;
import com.github.hugh.util.ip.Ip2RegeinTest;
import com.github.hugh.util.ip.Ip2regionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * IP 解析测试工具
 *
 * @author AS
 * @date 2021/2/23 14:23
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Ip2regionUtils.class})
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

    @Test
    public void completeTest() {
        String ip1 = "192.168.1.191";
        final String str1 = IpResolver.on(ip1).getComplete();
        assertEquals("内网IP", str1);
        String ip2 = "175.8.167.6";
        final String str2 = IpResolver.on(ip2, easyRedisSupplier.get()).getComplete();
        assertEquals("湖南省长沙市", str2);
        String ip3 = "154.18.161.64";
        final String str3 = IpResolver.on(ip3, easyRedisSupplier.get()).getComplete();
        assertNull(str3);
//        String ip4 = "";
//        final String str4 = IpResolver.on("", easyRedisSupplier.get());
//        assertEquals(ip4 , str4);
    }

    @Test
    public void completeSpareTest() {
        String ip1 = "192.168.1.191";
        final String str1 = IpResolver.on(ip1).getComplete();
        assertEquals("内网IP", str1);
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
}
