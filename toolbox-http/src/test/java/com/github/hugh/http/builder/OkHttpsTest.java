package com.github.hugh.http.builder;

import com.github.hugh.http.exception.ToolboxHttpException;
import com.github.hugh.http.model.ResponseData;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Okhttp builder test
 *
 * @author AS
 * Date 2023/3/9 9:51
 */
class OkHttpsTest {

    private static final String http_bin_get_url = "https://httpbin.org/get";
    private static final String http_bin_post_url = "https://httpbin.org/post";

    @Test
    void testGet() throws IOException {
        final ToolboxHttpException toolboxHttpException = Assertions.assertThrowsExactly(ToolboxHttpException.class, () -> {
            new OkHttps().doGet();
        });
        assertEquals("url is null", toolboxHttpException.getMessage());
//        final String s = new OkHttps().setUrl("https://sudo.191ec.com/silver-web-shop/manual/readInfo2").doGet().getMessage();
//        System.out.println(s);
        Map<String, Object> map = new HashMap<>();
        String params1 = "bar1";
        map.put("foo1", params1);
        map.put("foo2", "bar3");
        final String s2 = new OkHttps().setUrl(http_bin_get_url).setBody(map).doGet().getMessage();
        assertNotNull(s2);
        final ResponseData postman = new OkHttps().setUrl(http_bin_get_url).setBody(map).doGet().fromJson(ResponseData.class);
        assertEquals(params1, postman.getArgs().getFoo1());
        System.out.println(postman.toString());
        assertEquals("okhttp/4.9.3", postman.getHeaders().getUserAgent());
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Custom User Agent");
        final ResponseData responseData = new OkHttps().setUrl(http_bin_get_url).setBody(map).setHeader(headers).doGet().fromJson(ResponseData.class);
        assertEquals("Custom User Agent", responseData.getHeaders().getUserAgent());
    }

    @Test
    void testPostForm() throws Exception {
        Map<String, Object> map = new HashMap<>();
        String params1 = "bar1";
        map.put("foo1", params1);
        map.put("foo2", "bar3");
        System.out.println(new OkHttps().setUrl(http_bin_post_url).setBody(map).doPostForm().getMessage());
        final ResponseData responseData1 = new OkHttps().setUrl(http_bin_post_url).setBody(map).doPostForm().fromJson(ResponseData.class);
        assertEquals(params1, responseData1.getForm().getFoo1());
    }

    @Test
    void testPostJson() throws Exception {
        Map<String, Object> map = new HashMap<>();
        String params1 = "bar1";
        map.put("foo1", params1);
        map.put("foo2", "bar3");
        System.out.println(new OkHttps().setUrl(http_bin_post_url).setBody(map).doPostJson().getMessage());
//        final ResponseData responseData1 = new OkHttps().setUrl(http_bin_post_url).setBody(map).doPostForm().fromJson(ResponseData.class);
//        assertEquals(params1, responseData1.getForm().getFoo1());
    }


    @Test
    void testTimeout() throws IOException {
        MockWebServer server = new MockWebServer();

        // 模拟服务器延迟5秒钟响应
        server.enqueue(new MockResponse().setBody("response").throttleBody(1024, 5, TimeUnit.SECONDS));

        // 创建 OkHttpClient 实例
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.SECONDS) // 设置连接超时时间为3秒
                .readTimeout(1, TimeUnit.SECONDS) // 设置读取超时时间为3秒
                .writeTimeout(1, TimeUnit.SECONDS) // 设置写入超时时间为3秒
                .build();
//        HttpClient httpClient = HttpClient.getInstance();
//        httpClient.setConnectTimeout(2);
//        httpClient.setWriteTimeout(2);
//        httpClient.setReadTimeout(2);
        // 创建请求
        Request request = new Request.Builder()
                .url(server.url("/"))
                .build();
        // 发送请求，此处应该会抛出 java.net.SocketTimeoutException 异常
        try {
//            Response response = httpClient.getOkHttpClient().newCall(request).execute();
            Response response = client.newCall(request).execute();
            fail("Expected a SocketTimeoutException to be thrown");
        } catch (SocketTimeoutException expected) {
            // 预期异常
        }

        // 关闭 MockWebServer
        server.shutdown();
    }
}
