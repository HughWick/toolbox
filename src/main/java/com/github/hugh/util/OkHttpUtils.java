package com.github.hugh.util;

import com.github.hugh.exception.ToolboxException;
import com.github.hugh.support.instance.Instance;
import com.github.hugh.util.common.AssertUtils;
import com.github.hugh.util.gson.JsonObjectUtils;
import com.github.hugh.util.lang.UrlUtils;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp工具类
 *
 * @author hugh
 * @since 1.0.2
 */
@Slf4j
public class OkHttpUtils {
    private OkHttpUtils() {

    }

    /**
     * 链接超时为10秒
     */
    private static final int CONNECT_TIMEOUT = 10;

    /**
     * 设置读超时10秒
     */
    private static final int READ_TIMEOUT = 10;


    /**
     * 构建OkHttpClient对象
     * <p>链接超时为10秒</p>
     * <p>设置读超时10秒</p>
     *
     * @return OkHttpClient
     * @since 1.3.3
     */
    public static OkHttpClient buildClient() {
        return buildClient(CONNECT_TIMEOUT, READ_TIMEOUT);
    }

    /**
     * 构建OkHttpClient对象
     * <p> 版本从：1.4.6开始，改用单例模式创建{@link OkHttpClient}</p>
     *
     * @param connectTimeout 设置连接超时
     * @param readTimeout    设置读超时
     * @return OkHttpClient
     * @since 1.3.3
     */
    public static OkHttpClient buildClient(int connectTimeout, int readTimeout) {
        OkHttpClient client = Instance.getInstance().singleton(OkHttpClient.class);
        client.newBuilder().connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        return client;
    }

    /**
     * 参数以表单形式提交类型
     */
    private static final String APPLICATION_FORM_URLENCODED_VALUE = "application/x-www-form-urlencoded";

    /**
     * 参数以json形式提交类型
     */
    private static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";

    /**
     * 参数以form表单提交类型
     */
    private static final String MULTIPART_FORM_DATA = "multipart/form-data";

    /**
     * 本地cookie存储
     */
    private static final ConcurrentHashMap<String, List<Cookie>> COOKIE_STORE = new ConcurrentHashMap<>();

    /**
     * 表单参数请求类型
     */
    private static final MediaType FORM_TYPE = MediaType.parse(APPLICATION_FORM_URLENCODED_VALUE);

    /**
     * json参数请求类型
     */
    private static final MediaType JSON_TYPE = MediaType.get(APPLICATION_JSON_UTF8_VALUE);

    /**
     * OkHttp管理cookie
     */
    private static final OkHttpClient COOKIE_CLIENT = new OkHttpClient.Builder()
            .cookieJar(new CookieJar() {
                @Override
                public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                    COOKIE_STORE.put(httpUrl.host(), list);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                    List<Cookie> cookies = COOKIE_STORE.get(httpUrl.host());
                    return cookies != null ? cookies : new ArrayList<>();
                }
            }).connectTimeout(10, TimeUnit.SECONDS) // 设置连接超时
            .readTimeout(10, TimeUnit.SECONDS)// 设置读超时
            .build();

    /**
     * 发送post表单请求
     * <p>不带任何参数</p>
     *
     * @param url 请求URL
     * @return String
     * @throws IOException IO流错误
     */
    public static String postForm(String url) throws IOException {
        return postForm(url, null, buildClient());
    }

    /**
     * 发送表单形式参数的POST
     * <p>Content-Type:application/x-www-form-urlencoded</p>
     * <p>使用默认构建的{@link #buildClient()}OkHttpClient{@link #postForm(String, Object, OkHttpClient)}发送post请求</p>
     *
     * @param url    请求URL
     * @param params 请求参数参数
     * @param <T>    请求参数类型
     * @return String
     * @throws IOException IO流错误
     */
    public static <T> String postForm(String url, T params) throws IOException {
        return postForm(url, JSONObject.fromObject(params), buildClient());
    }

    /**
     * 发送表单形式参数的POST
     * <p>Content-Type:application/x-www-form-urlencoded</p>
     *
     * @param url          请求URL
     * @param params       参数
     * @param okHttpClient okHttpClient
     * @param <T>          请求参数类型
     * @return String
     * @throws IOException IO流错误
     */
    public static <T> String postForm(String url, T params, OkHttpClient okHttpClient) throws IOException {
        AssertUtils.notEmpty(url, "url");
        JSONObject jsonObject = JSONObject.fromObject(params);
        String urlParams = UrlUtils.jsonParse(jsonObject);
        return post(url, urlParams, FORM_TYPE, okHttpClient);
    }

    /**
     * 发送表单形式参数的POST
     * <p>Content-Type:application/x-www-form-urlencoded</p>
     *
     * @param url           请求URL
     * @param params        参数
     * @param headerContent header 附加内容
     * @param <T>           请求参数类型
     * @return String
     * @throws IOException IO流错误
     * @since 1.3.0
     */
    public static <T> String postForm(String url, T params, Map<String, String> headerContent) throws IOException {
        return postForm(url, params, headerContent, buildClient());
    }

    /**
     * 发送表单形式参数的POST
     * <p>Content-Type:application/x-www-form-urlencoded</p>
     *
     * @param url           请求URL
     * @param params        参数
     * @param headerContent header 附加内容
     * @param okHttpClient  okHttpClient
     * @param <T>           请求参数类型
     * @return String
     * @throws IOException IO流错误
     */
    public static <T> String postForm(String url, T params, Map<String, String> headerContent, OkHttpClient okHttpClient) throws IOException {
        JSONObject jsonObject = JSONObject.fromObject(params);
        String urlParams = UrlUtils.jsonParse(jsonObject);
        RequestBody body = RequestBody.create(FORM_TYPE, urlParams);
        Headers headers = Headers.of(headerContent);
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .post(body).build();
        return send(request, okHttpClient);
    }

    /**
     * 发送表单形式参数的POST
     * <ul>
     * <li>Content-Type:application/x-www-form-urlencoded</li>
     * <li>该方法为OkHttp管理Cookie,请求获取cookie接口后、将cookie存储至本地Map内存中</li>
     * </ul>
     *
     * @param url  请求URL
     * @param json 参数
     * @return String
     * @throws IOException IO流错误
     * @since 1.2.4
     */
    public static String postFormCookie(String url, JSONObject json) throws IOException {
        String params = UrlUtils.jsonParse(json);
        return post(url, params, FORM_TYPE, COOKIE_CLIENT);
    }

    /**
     * 发送json形式参数的post请求
     * <p>Content-Type:application/json;charset=UTF-8</p>
     *
     * @param url    请求URL
     * @param params 参数
     * @param <T>    请求参数类型
     * @return String
     * @throws IOException IO流错误
     */
    public static <T> String postJson(String url, T params) throws IOException {
        if (params instanceof String) {
            return post(url, String.valueOf(params), JSON_TYPE);
        }
        return post(url, JSONObject.fromObject(params).toString(), JSON_TYPE);
    }

    /**
     * 发送POST 后返回结果转换为{@link JsonObject}
     * <p>Content-Type:application/x-www-form-urlencoded</p>
     *
     * @param url    请求URL
     * @param params 参数
     * @param <T>    请求参数类型
     * @return {@link JsonObject}
     * @throws IOException IO流错误
     * @since 1.3.0
     */
    public static <T> JsonObject postFormReJsonObject(String url, T params) throws IOException {
        String result = postForm(url, params);
        try {
            return JsonObjectUtils.parse(result);
        } catch (Exception e) {
            throw new ToolboxException("url:" + url + ",返回结果参数格式错误:" + result);
        }
    }

    /**
     * 发送get请求
     * <p>将data中的键值对拼接成标准的url访问参数</p>
     *
     * @param url    URL
     * @param params 请求参数
     * @param <T>    请求参数类型
     * @return String
     */
    public static <T> String get(String url, T params) throws IOException {
        AssertUtils.notEmpty(url, "url");
        var jsonObject = JSONObject.fromObject(params);
        url = UrlUtils.urlParam(url, jsonObject);
        return get(url);
    }

    /**
     * get请求
     * <p>注：url自行拼接查询条件参数</p>
     *
     * @param url URL
     * @return String
     */
    public static String get(String url) throws IOException {
        AssertUtils.notEmpty(url, "url");
        var request = new Request.Builder().url(url).build();
        return send(request, buildClient());
    }

    /**
     * 发送Get请求
     *
     * @param url           请求URL
     * @param data          参数
     * @param headerContent header 附加内容
     * @return String
     * @throws IOException IO流错误
     * @since 1.3.0
     */
    public static String get(String url, JSONObject data, Map<String, String> headerContent) throws IOException {
        url = UrlUtils.urlParam(url, data);
        var headers = Headers.of(headerContent);
        var request = new Request.Builder()
                .url(url)
                .headers(headers).build();
        return send(request);
    }

    /**
     * 统一发送post请求方法
     * <p>该方法调用{@link #post(String, String, MediaType, OkHttpClient)},client使用默认的{@link #buildClient()}进行传参</p>
     *
     * @param url       URL
     * @param params    参数
     * @param mediaType 请求类型
     * @return String 请求结果
     * @throws IOException IO异常
     * @since 1.5.9
     */
    private static String post(String url, String params, MediaType mediaType) throws IOException {
        var body = RequestBody.create(mediaType, params);
        var request = new Request.Builder().url(url).post(body).build();
        return send(request, buildClient());
    }

    /**
     * 统一的post请求
     *
     * @param url          URL
     * @param params       参数
     * @param mediaType    请求类型
     * @param okHttpClient 请求客户端
     * @return String 请求结果
     * @throws IOException IO异常
     */
    private static String post(String url, String params, MediaType mediaType, OkHttpClient okHttpClient) throws IOException {
        var body = RequestBody.create(mediaType, params);
        var request = new Request.Builder().url(url).post(body).build();
        return send(request, okHttpClient);
    }

    /**
     * 简化版发送请求
     * <p>该方法调用了{@link #send(Request, OkHttpClient)}，使用默认的{@link #buildClient()}进行创建client</p>
     *
     * @param request {@link Request}
     * @return String 请求返回结果
     * @throws IOException IO流错误
     * @since 1.5.8
     */
    private static String send(Request request) throws IOException {
        return send(request, buildClient());
    }

    /**
     * 统一发送请求
     *
     * @param request      请求内容
     * @param okHttpClient OkHttpClient
     * @return String 返回结果
     * @throws IOException IO异常
     */
    private static String send(Request request, OkHttpClient okHttpClient) throws IOException {
        try (var response = okHttpClient.newCall(request).execute()) {
            ResponseBody body1 = response.body();
            if (body1 == null) {
                throw new ToolboxException("result params is null ");
            }
            return body1.string();
        }
    }

    /**
     * 上传文件方法
     *
     * @param url      URL
     * @param params   额外参数
     * @param fileName 文件的name(对应后台接收form-data的名称)
     * @param fileMap  文件map、key-文件名称、value-文件路径
     * @param <K>      key
     * @param <V>      value
     * @return String
     * @throws IOException IO错误
     * @since 1.5.8
     */
    public static <K, V> String upload(String url, Map<K, V> params, String fileName, Map<K, V> fileMap) throws IOException {
        MultipartBody.Builder requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        if (MapUtils.isNotEmpty(params)) {
            for (Map.Entry<K, V> entry : params.entrySet()) {
                var name = String.valueOf(entry.getKey());
                var value = String.valueOf(entry.getValue());
                requestBody.addFormDataPart(name, value);
            }
        }
        if (MapUtils.isNotEmpty(fileMap)) {
            for (Map.Entry<K, V> entry : fileMap.entrySet()) {
                var name = String.valueOf(entry.getKey());
                var path = String.valueOf(entry.getValue());
                var fileBody = RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), new File(path));
                requestBody.addFormDataPart(fileName, name, fileBody);
            }
        }
        var request = new Request.Builder().url(url).post(requestBody.build()).build();
        return send(request);
    }
}
