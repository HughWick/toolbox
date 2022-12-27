package com.github.hugh;

import com.github.hugh.util.RequestUtils;
import eu.bitwalker.useragentutils.Version;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Request 请求测试类
 * User: AS
 * Date: 2022/7/5 11:20
 */
class RequestUtilsTest {

    //获取请求头中浏览器版本号
    @Test
    void testGetName() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");
        String browserName = RequestUtils.getBrowserName(request);
        assertEquals("Chrome 8/86.0.4240.198", browserName);
        final Version browserVersion = RequestUtils.getBrowserVersion(request);
        assertEquals("86.0.4240.198", browserVersion.toString());
        assertEquals("86", browserVersion.getMajorVersion());
        assertEquals("86.0.4240.198", browserVersion.getVersion());
        MockHttpServletRequest request2 = new MockHttpServletRequest();
        request2.addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.5060.66 Safari/537.36 Edg/103.0.1264.44");
        assertEquals("Chrome 10/103.0.5060.66", RequestUtils.getBrowserName(request2));
        assertEquals("Windows 10", RequestUtils.getOsName(request2));
//        System.out.println(RequestUtils.getBrowserName(request2));
//        System.out.println(RequestUtils.getOsName(request2));
    }

    @Test
    void testRequestSystem() {
        String str = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36 Edg/108.0.1462.54";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("User-Agent", str);
        assertTrue(RequestUtils.isPc(request));
        String str2 = "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1";
        MockHttpServletRequest request2 = new MockHttpServletRequest();
        request2.addHeader("user-agent", str2);
        assertTrue(RequestUtils.isIos(request2));
        String str3 = "Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Mobile Safari/537.36";
        MockHttpServletRequest request3 = new MockHttpServletRequest();
        request3.addHeader("user-agent", str3);
        assertTrue(RequestUtils.isAndroid(request3));
    }
}
