package com.github.hugh;

import com.github.hugh.util.ServletUtils;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Servlet 测试类
 *
 * @author Hugh
 **/
public class ServletTest {

    @Test
    void testServlet() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "9001");
        request.addParameter("page", "1");
        request.addParameter("size", "20");
        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("hostSerialNumber", "202010260288");
        request.addParameter("content", contentMap.toString());
        Map<String, Object> params = ServletUtils.getParams(request);
//        assertEquals(params.toString(), "{userId=9001, page=1, size=20}");
//        Map<String, Object> params2 = ServletUtils.getParamsDeleteLimit(request);
//        assertEquals(params2.toString(), "{userId=9001}");
    }

    // 请求头中body内容
    @Test
    void testGetBody() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", "9001");
        MockHttpServletRequest request = new MockHttpServletRequest();
        byte[] bytes = map.toString().getBytes(Charset.defaultCharset());
//        request.setContentType("application/json;charset=UTF-8");
        request.addParameter("page", "1");
        request.addParameter("size", "20");
        request.setMethod("POST");
        request.setContent(bytes);
        final String body = ServletUtils.getBody(request);
        assertEquals("{userId=9001}", body);
        Map<String, Object> params = ServletUtils.getParams(request);
        assertEquals("{page=1, size=20}", params.toString());
    }
}
