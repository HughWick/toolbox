package com.github.hugh;

import com.github.hugh.util.ServletUtils;
import org.junit.jupiter.api.Assertions;
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
class ServletTest {

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

    @Test
    void test023() {
//        String str1 = "------WebKitFormBoundaryRcvXYFKFONUpAB3Z\n" +
//                "Content-Disposition: form-data; name=\"deviceId\"\n" +
//                "\n" +
//                "D00020230101922\n" +
//                "------WebKitFormBoundaryRcvXYFKFONUpAB3Z\n" +
//                "Content-Disposition: form-data; name=\"sendDataLength\"\n" +
//                "\n" +
//                "700";

        String str2 = "------WebKitFormBoundaryRcvXYFKFONUpAB3Z\n" +
                "Content-Disposition: form-data; name=\"deviceId\"\r\n" +
                "\r\n" +
                "D00020230101922\n" +
                "------WebKitFormBoundaryRcvXYFKFONUpAB3Z\n" +
                "Content-Disposition: form-data; name=\"sendDataLength\"\r\n" +
                "\r\n" +
                "700\n" +
                "------WebKitFormBoundaryRcvXYFKFONUpAB3Z\n" +
                "Content-Disposition: form-data; name=\"sendInterval\"\r\n" +
                "\r\n" +
                "200\n" +
                "------WebKitFormBoundaryRcvXYFKFONUpAB3Z\n" +
                "Content-Disposition: form-data; name=\"updateFlag\"\r\n" +
                "\r\n" +
                "0\n" +
                "------WebKitFormBoundaryRcvXYFKFONUpAB3Z\n" +
                "Content-Disposition: form-data; name=\"environment\"\r\n" +
                "\r\n" +
                "0\n" +
                "------WebKitFormBoundaryRcvXYFKFONUpAB3Z\n" +
                "Content-Disposition: form-data; name=\"fileMd5\"\r\n" +
                "\r\n" +
                "668fb659bd10c90777f89c0c67b8e01a\n" +
                "------WebKitFormBoundaryRcvXYFKFONUpAB3Z";
        String form = "multipart/form-data; boundary=----WebKitFormBoundaryRcvXYFKFONUpAB3Z";
        String deviceId = ServletUtils.getFormData(form, str2, "deviceId");
        Assertions.assertEquals("D00020230101922", deviceId);
    }
}
