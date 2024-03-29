package com.github.hugh.http.builder;

import com.github.hugh.http.OkHttpUpdateFileTest;
import com.github.hugh.http.constant.MediaTypes;
import com.github.hugh.http.constant.OkHttpCode;
import com.github.hugh.http.exception.ToolboxHttpException;
import com.github.hugh.http.model.FileFrom;
import com.github.hugh.http.model.JsonPlaceholderResult;
import com.github.hugh.http.model.ResponseData;
import com.github.hugh.json.gson.Jsons;
import okhttp3.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Okhttp builder test
 *
 * @author AS
 * Date 2023/3/9 9:51
 */
class OkHttpsTest {
    // -------------本地测试文件-----------
    // 定义了几个本地路径常量，分别用于测试图片上传功能
    private static final String path1 = "/file/image/84630805_p0.png"; // 图片大小在10M
    private static final String path3 = "/file/image/69956256_p1.jpg";
    private static final String path4 = "/file/image/20200718234953_grmzy.jpeg";
    private static final String path5 = "/file/image/heif/share_a4b448c4f972858f42640e36ffc3a8e6.png";
    private static final String path6 = "/file/image/webp/share_572031b53d646c2c8a8191bdd93a95b2.png";
    private static final String path7 = "/file/image/vant_log_150x150.png";
    // 定义了两个测试用的 HTTP URL 常量，分别用于测试 GET 和 POST 请求功能
    private static final String http_bin_get_url = "https://httpbin.org/get";
    private static final String http_bin_post_url = "https://httpbin.org/post";

    // get请求
    private static final String http_json_placeholder_get_url = "http://jsonplaceholder.typicode.com/posts";


    @Test
    void testGet() throws IOException {
        final ToolboxHttpException toolboxHttpException = Assertions.assertThrowsExactly(ToolboxHttpException.class, () -> {
            new OkHttps().doGet();
        });
        assertEquals("url is null", toolboxHttpException.getMessage());
        Map<String, Object> map = new HashMap<>();
        String params1 = "bar1";
        map.put("foo1", params1);
        map.put("foo2", "bar3");
        final String s2 = new OkHttps().setUrl(http_bin_get_url).setBody(map).doGet().getMessage();
        assertNotNull(s2);
        final ResponseData postman = new OkHttps().setUrl(http_bin_get_url).setBody(map).doGet().fromJson(ResponseData.class);
        assertEquals(params1, postman.getArgs().getFoo1());
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
        final ResponseData responseData1 = new OkHttps().setUrl(http_bin_post_url).setBody(map).doPostForm().fromJson(ResponseData.class);
        assertEquals(params1, responseData1.getForm().getFoo1());
        assertEquals("application/x-www-form-urlencoded; charset=utf-8", responseData1.getHeaders().getContentType());
    }

    // 测试post 请求，参数json格式
    @Test
    void testPostJson() throws Exception {
        Map<String, Object> map = new HashMap<>();
        String params1 = "bar1";
        map.put("foo1", params1);
        map.put("foo2", "bar3");
        final Jsons jsons = new OkHttps().setUrl(http_bin_post_url).setBody(map).doPostJson().toJsons();
        assert MediaTypes.APPLICATION_JSON_UTF8 != null;
        assertEquals(MediaTypes.APPLICATION_JSON_UTF8.toString(), jsons.getThis("headers").getString("Content-Type"));
        final ResponseData response1 = new OkHttps().setUrl(http_bin_post_url).doPostJson().fromJson(ResponseData.class);
        assertEquals("null", response1.getJson() + "");
//        final ResponseData responseData1 = new OkHttps().setUrl(http_bin_post_url).setBody(map).doPostForm().fromJson(ResponseData.class);
//        assertEquals(params1, responseData1.getForm().getFoo1());
    }

    @Test
    void testCookie() throws Exception {
        String url = "http://httpbin.org/cookies/set?username=admin&password=123456";
        final String responseData = new OkHttps().setUrl(url)
                .isSendCookie(true)
                .doGet()
                .getMessage();
        URI uri = URI.create(url);
        List<Cookie> cookies = OkHttpCode.COOKIE_STORE.get(uri.getHost());
        assertEquals("[username=admin; path=/, password=123456; path=/]", cookies.toString());
    }

    // 测试上传单张图片
    @Test
    void testUploadSingletonFile() throws IOException {
        // 附加参数
        var params = new HashMap<>();
        params.put("type", "0");
        final ToolboxHttpException toolboxHttpException = Assertions.assertThrowsExactly(ToolboxHttpException.class, () -> {
            new OkHttps().setUrl(http_bin_post_url).setBody(params).uploadFile().getMessage();
        });
        assertEquals("file is null", toolboxHttpException.getMessage());
        // 测试上传文件对应后台key为空
        final ToolboxHttpException toolboxHttpException2 = Assertions.assertThrowsExactly(ToolboxHttpException.class, () -> {
            // 测试单个
            List<FileFrom> mediaList1 = Collections.singletonList(
                    new FileFrom("", "84630805_p0.jpg", getPath(path1), null, MediaTypes.IMAGE_JPEG)
            );
            new OkHttps().setUrl(http_bin_post_url).setFileFrom(mediaList1).uploadFile().getMessage();
        });
        assertEquals("upload file key is null", toolboxHttpException2.getMessage());
        // 测试上传文件路径为空
        final ToolboxHttpException toolboxHttpException3 = Assertions.assertThrowsExactly(ToolboxHttpException.class, () -> {
            // 测试单个
            List<FileFrom> mediaList1 = Collections.singletonList(
                    new FileFrom("file", "84630805_p0.jpg", null, null, MediaTypes.IMAGE_JPEG)
            );
            new OkHttps().setUrl(http_bin_post_url).setUrl(http_bin_post_url).setFileFrom(mediaList1).uploadFile().getMessage();
        });
        assertEquals("file path is null", toolboxHttpException3.getMessage());
        // 测试单个
        FileFrom media1 = new FileFrom("file", "vant_log_150x150.png", getPath(path7), null, MediaTypes.IMAGE_PNG);
        // 测试图片
        final ResponseData responseData2 = new OkHttps().setUrl(http_bin_post_url)
                .setBody(params)
                .setFileFrom(media1)
                .uploadFile()
                .fromJson(ResponseData.class);
        // 验证数据长度
        assertEquals("4366", responseData2.getHeaders().getContentLength());
    }

    // 测试多张图片
    @Test
    void testUploadFile() throws Exception {
        // 创建多个文件媒体信息对象
        List<FileFrom> mediaList = Arrays.asList(
                new FileFrom("file", "69956256_p1.jpg", getPath(path3), null, MediaTypes.TEXT_PLAIN),
                new FileFrom("file", "20200718234953_grmzy.jpeg", null, new File(getPath(path4)), MediaTypes.IMAGE_JPEG)
        );
        final var okHttpsResponse = new OkHttps().setUrl(http_bin_post_url).setFileFrom(mediaList)
                .uploadFile();
        // 测试图片
        final ResponseData responseData3 = okHttpsResponse.fromJson(ResponseData.class);
        // 验证数据长度
        assertEquals("610771", responseData3.getHeaders().getContentLength());
    }

    private static String getPath(String fileName) {
        return OkHttpUpdateFileTest.class.getResource(fileName).getPath();
    }

    @Test
    void testTimeout() throws IOException {
        MockWebServer server = new MockWebServer();
        // 模拟服务器延迟5秒钟响应
        server.enqueue(new MockResponse()
                .setBody("response"));
//        server.enqueue(new MockResponse()
//                .setBody("response")
//                .throttleBody(1024, 2, TimeUnit.SECONDS));
        // 启动 MockWebServer
        server.start();
        // 创建 OkHttpClient 实例
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS) // 设置连接超时时间为3秒
                .addInterceptor(chain -> {
                    // 模拟请求超时
                    try {
                        Thread.sleep(4000); // 5 秒钟的休眠，模拟超时
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    throw new SocketTimeoutException("Simulated timeout"); // 抛出超时异常
                })
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

    @Test
    void testGet02() throws Exception {
        String msg = "{\"code\":\"BAD_API_KEY_OR_SECRET\",\"message\":\"Check api_key/api_secret\"}";
        String account = "ec19c00317131f08";
        String password = "Y8i7KZwlDF9BzlxOJbB849Axj2mxqBQ7u9B2i9C6fPdwX4P";
        String credential = Credentials.basic(account, password);
        Map<String, String> headerContent = new HashMap<>();
        headerContent.put("Authorization", credential);
        String url = "http://175.6.24.171:10121/api/v5/subscriptions";
        Map<String, Object> map = new HashMap<>();
        map.put("page", 3);
        String message = new OkHttps().setUrl(url).setBody(map).setHeader(headerContent).doGet().getMessage();
        assertEquals(msg, message);
//        System.out.println("--->" + message);
//        Map<String, Object> map = new HashMap<>();
//        map.put("page", 3);
        String message2 = new OkHttps().setUrl(url + "?page=3").setHeader(headerContent).doGet().getMessage();
//        System.out.println("--22--->" + message2);
        assertEquals(msg, message2);
    }

    @Test
    void testClient() throws Exception {
        ConnectionPool connectionPool = new ConnectionPool(10, 1, TimeUnit.MINUTES);
        final OkHttpClient build = new OkHttpClient.Builder()
                .connectionPool(connectionPool)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        final String s2 = new OkHttps().setUrl(http_bin_get_url).setOkHttpClient(build).doGet().getMessage();
        assertNotNull(s2);
    }

    // 测试客户端连接超时
    @Test
    void testClientTimeout() throws Exception {
        final String s2 = new OkHttps().setUrl(http_bin_get_url)
                .setConnectTimeout(20)
                .setReadTimeout(3)
                .setWriteTimeout(76)
                .doGet().getMessage();
        assertNotNull(s2);
    }

    @Test
    void testClientConnectionPool() throws IOException {
        ConnectionPool defaultConnectionPool = new ConnectionPool(5, 1234, TimeUnit.SECONDS);
        final OkHttps okHttps = new OkHttps();

        final String message = okHttps.setUrl(http_bin_get_url)
                .setConnectionPool(defaultConnectionPool)
                .doGet().getMessage();
        assertNotNull(message);
        final String message2 = OkHttps.builder().url(http_bin_get_url)
                .build().doGet().getMessage();
        assertNotNull(message2);
    }

    //    -------------------------------以下都为静态测试工具类-------------------------------
    @Test
    void testStaticGet() throws IOException {
        Map<String, Object> map = new HashMap<>();
        String params1 = "bar1";
        map.put("foo1", params1);
        map.put("foo2", "bar3");
        final ResponseData postman = OkHttps.url(http_bin_get_url).setBody(map).doGet().fromJson(ResponseData.class);
        assertEquals(params1, postman.getArgs().getFoo1());
        assertEquals("okhttp/4.9.3", postman.getHeaders().getUserAgent());

        final String message = OkHttps.url(http_json_placeholder_get_url).doGet().getMessage();
        assertNotNull(message);
        final List<JsonPlaceholderResult> postman2 = OkHttps.url(http_json_placeholder_get_url).doGet().toList(JsonPlaceholderResult.class);
        assertEquals(100, postman2.size());
//        final List<Jsons> postman3 = OkHttps.url(http_json_placeholder_get_url).doGet().toList();
//        System.out.println("====>>" + postman3);
//        assertEquals(1, postman3.get(0).getInt("id"));
    }

    @Test
    void testHttps() throws IOException {
        String url = "http://factory.hnlot.com.cn/v2/host/queryList";
//        final Jsons jsons = OkHttps.url(url).doGet().toJsons();
        System.out.println(OkHttps.url(url).doGet().getMessage());
    }
}
