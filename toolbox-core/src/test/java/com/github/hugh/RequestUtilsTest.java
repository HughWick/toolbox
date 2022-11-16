package com.github.hugh;

import com.github.hugh.util.RequestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

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
        Assertions.assertEquals("Chrome 8/86.0.4240.198" , browserName);
//        System.out.println(browserName);
        MockHttpServletRequest request2 = new MockHttpServletRequest();
        request2.addHeader("User-Agent", "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.5060.66 Safari/537.36 Edg/103.0.1264.44");
        Assertions.assertEquals("Chrome 10/103.0.5060.66" , RequestUtils.getBrowserName(request2));
        Assertions.assertEquals("Windows 10" , RequestUtils.getOsName(request2));
//        System.out.println(RequestUtils.getBrowserName(request2));
//        System.out.println(RequestUtils.getOsName(request2));
    }
}
