package com.github.hugh;

import com.github.hugh.util.ServletUtils;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Map;

/**
 * @author Hugh
 * @sine
 **/
public class ServletTest {

    @Test
    void testServlet(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "9001");
//        Map<String, String> params = ServletUtils.getParams(request);
//        System.out.println(params);
        request.addParameter("page", "1");
        request.addParameter("size", "20");
        Map<String, String> params = ServletUtils.getParams(request);
        Map<String, String> params2 = ServletUtils.getParamsDeleteLimit(request);
        System.out.println(params);
        System.out.println(params2);
    }
}
