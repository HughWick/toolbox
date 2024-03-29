package com.github.hugh;

import com.github.hugh.util.RequestUtils;
import eu.bitwalker.useragentutils.Version;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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
        MockHttpServletRequest request2 = new MockHttpServletRequest();
        String userAgent2 = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.5060.66 Safari/537.36 Edg/103.0.1264.44";
        request2.addHeader("user-agent", userAgent2);
        assertEquals("Chrome 10/103.0.5060.66", RequestUtils.getBrowserName(request2));
        assertEquals("Windows 10", RequestUtils.getOsName(request2));
        assertEquals(userAgent2, RequestUtils.getUserAgent(request2));
//        MapUtil.<String, Object>builder()
//                .put("query", ServletUtil.getParamMap(request))
//                .put("body", ServletUtil.getBody(request)).build();
    }

    // 获取浏览器的版本号
    @Test
    void testVerifyVersion() {
        String userAgent1 = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("User-Agent", userAgent1);
        final Version browserVersion = RequestUtils.getBrowserVersion(request);
        assertEquals("95.0.4638.69", browserVersion.toString());
        assertEquals("95.0.4638.69", browserVersion.getVersion());
        assertEquals("95", browserVersion.getMajorVersion());
    }

    // 验证请求头中操作系统类型
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

    @Test
    void getHeaders() {
        // 准备数据，构建一个 MockHttpServletRequest 对象
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Content-T", "application/json");
        request.addHeader("Accept", "application/xml");

        // 调用被测试方法
        Map<String, String> headers = RequestUtils.getHeaders(request);

        // 验证返回结果
        assertNotNull(headers);
        assertEquals(2, headers.size());
        assertEquals("application/json", headers.get("Content-T"));
        assertEquals("application/xml", headers.get("Accept"));
    }
}
