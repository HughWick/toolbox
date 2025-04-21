package com.github.hugh;

import com.github.hugh.util.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.powermock.api.mockito.PowerMockito.when;

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
        assertEquals("{userId=9001, page=1, size=20, content={hostSerialNumber=202010260288}}", params.toString());
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
// ---------------------------- getParamsDeleteLimit 测试用例 ----------------------------

    @Test
    void getParamsDeleteLimit_withPageAndSize_removesKeys() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.put("name", new String[]{"John"});
        parameterMap.put("page", new String[]{"1"});
        parameterMap.put("size", new String[]{"10"});
        parameterMap.put("email", new String[]{"john@example.com"});

        when(request.getParameterNames()).thenReturn(Collections.enumeration(parameterMap.keySet()));
        when(request.getParameter("name")).thenReturn("John");
        when(request.getParameter("page")).thenReturn("1");
        when(request.getParameter("size")).thenReturn("10");
        when(request.getParameter("email")).thenReturn("john@example.com");

        Map<String, String> params = ServletUtils.getParamsDeleteLimit(request); // 替换 RequestUtil

        assertNotNull(params);
        assertEquals(2, params.size());
        assertEquals("John", params.get("name"));
        assertEquals("john@example.com", params.get("email"));
        assertNull(params.get("page")); // 应该被移除
        assertNull(params.get("size")); // 应该被移除
    }

    @Test
    void getParamsDeleteLimit_withoutPageAndSize_noRemoval() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.put("name", new String[]{"Jane"});
        parameterMap.put("email", new String[]{"jane@example.com"});

        when(request.getParameterNames()).thenReturn(Collections.enumeration(parameterMap.keySet()));
        when(request.getParameter("name")).thenReturn("Jane");
        when(request.getParameter("email")).thenReturn("jane@example.com");

        Map<String, String> params = ServletUtils.getParamsDeleteLimit(request); // 替换 RequestUtil

        assertNotNull(params);
        assertEquals(2, params.size());
        assertEquals("Jane", params.get("name"));
        assertEquals("jane@example.com", params.get("email"));
        assertNull(params.get("page")); // 虽然没有，但移除操作不应影响其他
        assertNull(params.get("size")); // 虽然没有，但移除操作不应影响其他
    }

    @Test
    void getParamsDeleteLimit_emptyRequest_returnsEmptyMap() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getParameterNames()).thenReturn(Collections.emptyEnumeration());

        Map<String, String> params = ServletUtils.getParamsDeleteLimit(request); // 替换 RequestUtil

        assertNotNull(params);
        assertTrue(params.isEmpty());
    }

    @Test
    void getParamsDeleteLimit_nullRequest_handlesNullGracefully() {
        // 假设 getParams(HttpServletRequest request) 方法能处理 null request
        // 如果不能，你需要修改你的 getParamsDeleteLimit 方法来处理 null request
        // 这里假设 getParams(request) 返回空 Map 或抛出异常，你需要根据实际情况调整测试
        HttpServletRequest request = null;
        // 注意：这里直接调用，如果你的 getParams 方法不能处理 null request，需要调整测试或方法
        // 更好的做法是在 getParamsDeleteLimit 方法中加入 null request 检查
        Map<String, String> params = ServletUtils.getParamsDeleteLimit(request); // 替换 RequestUtil

        assertNotNull(params); // 假设 getParams(null) 返回空 Map
        assertTrue(params.isEmpty());
    }


    // ---------------------------- getParams(HttpServletRequest request, String... keys) 测试用例 ----------------------------

    @Test
    void getParams_removeSpecificKeys_removesCorrectly() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.put("param1", new String[]{"value1"});
        parameterMap.put("param2", new String[]{"value2"});
        parameterMap.put("param3", new String[]{"value3"});

        when(request.getParameterNames()).thenReturn(Collections.enumeration(parameterMap.keySet()));
        when(request.getParameter("param1")).thenReturn("value1");
        when(request.getParameter("param2")).thenReturn("value2");
        when(request.getParameter("param3")).thenReturn("value3");

        Map<String, String> params = ServletUtils.getParams(request, "param1", "param3"); // 替换 RequestUtil

        assertNotNull(params);
        assertEquals(1, params.size());
        assertEquals("value2", params.get("param2"));
        assertNull(params.get("param1")); // 应该被移除
        assertNull(params.get("param3")); // 应该被移除
    }

    @Test
    void getParams_removeNoKeys_returnsAllParams() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.put("key1", new String[]{"val1"});
        parameterMap.put("key2", new String[]{"val2"});

        when(request.getParameterNames()).thenReturn(Collections.enumeration(parameterMap.keySet()));
        when(request.getParameter("key1")).thenReturn("val1");
        when(request.getParameter("key2")).thenReturn("val2");

        Map<String, String> params = ServletUtils.getParams(request); // 调用不带 keys 的 getParams (假设存在并返回所有参数)

        assertNotNull(params);
        assertEquals(2, params.size());
        assertEquals("val1", params.get("key1"));
        assertEquals("val2", params.get("key2"));
    }

    @Test
    void getParams_removeNullKeysArray_returnsAllParams() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.put("keyA", new String[]{"valueA"});
        parameterMap.put("keyB", new String[]{"valueB"});

        when(request.getParameterNames()).thenReturn(Collections.enumeration(parameterMap.keySet()));
        when(request.getParameter("keyA")).thenReturn("valueA");
        when(request.getParameter("keyB")).thenReturn("valueB");

        Map<String, String> params = ServletUtils.getParams(request, (String[]) null); // 替换 RequestUtil，传递 null keys

        assertNotNull(params);
        assertEquals(2, params.size());
        assertEquals("valueA", params.get("keyA"));
        assertEquals("valueB", params.get("keyB"));
    }

    @Test
    void getParams_removeEmptyKeysArray_returnsAllParams() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.put("item1", new String[]{"data1"});
        parameterMap.put("item2", new String[]{"data2"});

        when(request.getParameterNames()).thenReturn(Collections.enumeration(parameterMap.keySet()));
        when(request.getParameter("item1")).thenReturn("data1");
        when(request.getParameter("item2")).thenReturn("data2");

        Map<String, String> params = ServletUtils.getParams(request, new String[]{}); // 替换 RequestUtil，传递空 keys 数组

        assertNotNull(params);
        assertEquals(2, params.size());
        assertEquals("data1", params.get("item1"));
        assertEquals("data2", params.get("item2"));
    }

    @Test
    void getParams_removeKeysNotExist_noEffect() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.put("existKey", new String[]{"existValue"});

        when(request.getParameterNames()).thenReturn(Collections.enumeration(parameterMap.keySet()));
        when(request.getParameter("existKey")).thenReturn("existValue");

        Map<String, String> params = ServletUtils.getParams(request, "nonExistKey1", "nonExistKey2"); // 替换 RequestUtil，移除不存在的 key

        assertNotNull(params);
        assertEquals(1, params.size());
        assertEquals("existValue", params.get("existKey"));
    }

    @Test
    void getParams_emptyRequest_returnsEmptyMap_withKeysToRemove() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getParameterNames()).thenReturn(Collections.emptyEnumeration());

        Map<String, String> params = ServletUtils.getParams(request, "anyKeyToRemove"); // 替换 RequestUtil，空请求但尝试移除 key

        assertNotNull(params);
        assertTrue(params.isEmpty());
    }

    @Test
    void getParams_nullRequest_handlesNullGracefully_withKeysToRemove() {
        // 假设 getParams(HttpServletRequest request) 方法能处理 null request
        HttpServletRequest request = null;

        Map<String, String> params = ServletUtils.getParams(request, "keyToRemove"); // 替换 RequestUtil，null 请求但尝试移除 key

        assertNotNull(params); // 假设 getParams(null) 返回空 Map
        assertTrue(params.isEmpty());
    }
}
