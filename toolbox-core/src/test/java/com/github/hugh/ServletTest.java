package com.github.hugh;

import com.github.hugh.util.ServletUtils;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

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
        Map<String, Object> params = ServletUtils.getParams(request);
        assertEquals(params.toString(), "{userId=9001, page=1, size=20}");
        Map<String, Object> params2 = ServletUtils.getParamsDeleteLimit(request);
        assertEquals(params2.toString(), "{userId=9001}");
    }
}
