package com.github.hugh.http;

import com.github.hugh.http.exception.ToolboxHttpException;
import com.github.hugh.json.gson.JsonObjectUtils;
import com.github.hugh.json.gson.JsonObjects;
import com.github.hugh.json.gson.Jsons;
import com.github.hugh.util.MapUtils;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

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
@Deprecated
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
        OkHttpClient client = new OkHttpClient();
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
                public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
                    COOKIE_STORE.put(httpUrl.host(), list);
                }

                @Override
                public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
                    List<Cookie> cookies = COOKIE_STORE.get(httpUrl.host());
                    return cookies != null ? cookies : new ArrayList<>();
                }
            }).connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS) // 设置连接超时
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)// 设置读超时
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
        return postForm(url, params, buildClient());
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
        String urlParams = UrlUtils.jsonParse(params);
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
        String urlParams = UrlUtils.jsonParse(params);
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
     * @param <P>  参数类型
     * @return String
     * @throws IOException 如果请求失败或响应无法解析为指定的数据类型，则抛出 IOException 异常
     * @since 1.2.4
     */
    public static <P> String postFormCookie(String url, P json) throws IOException {
        return postForm(url, json, COOKIE_CLIENT);
    }

    /**
     * 发送json形式参数的post请求
     * <p>Content-Type:application/json;charset=UTF-8</p>
     *
     * @param url    请求URL
     * @param params 参数
     * @param <T>    请求参数类型
     * @return String
     * @throws IOException 如果请求失败或响应无法解析为指定的数据类型，则抛出 IOException 异常
     */
    public static <T> String postJson(String url, T params) throws IOException {
        if (params instanceof String) {
            return post(url, String.valueOf(params), JSON_TYPE);
        }
        if (params == null) {
            return post(url, "", JSON_TYPE);
        }
        return post(url, new Jsons(params).toJson(), JSON_TYPE);
    }

    /**
     * 发送POST 后返回结果转换为{@link JsonObject}
     * <p>Content-Type:application/x-www-form-urlencoded</p>
     *
     * @param url    请求URL
     * @param params 参数
     * @param <T>    请求参数类型
     * @return {@link JsonObject}
     * @throws IOException 如果请求失败或响应无法解析为指定的数据类型，则抛出 IOException 异常
     * @since 1.3.0
     */
    @Deprecated
    public static <T> JsonObject postFormReJsonObject(String url, T params) throws IOException {
        String result = postForm(url, params);
        try {
            return JsonObjectUtils.parse(result);
        } catch (Exception e) {
            throw new ToolboxHttpException("url:" + url + ",返回结果参数格式错误:" + result);
        }
    }

    /**
     * 发送post json参数类型的请求，并且将结果集转换为 {@link JsonObjects}
     *
     * @param url URL
     * @return JsonObjects
     * @throws IOException 如果请求失败或响应无法解析为指定的数据类型，则抛出 IOException 异常
     * @since 2.0.6
     */
    @Deprecated
    public static JsonObjects postJsonReJsonObjects(String url) throws IOException {
        return postJsonReJsonObjects(url, null);
    }

    /**
     * 发送post json参数类型的请求，并且将结果集转换为 {@link JsonObjects}
     *
     * @param url    URL
     * @param params 参数
     * @param <T>    类型
     * @return JsonObjects
     * @throws IOException 如果请求失败或响应无法解析为指定的数据类型，则抛出 IOException 异常
     * @since 2.0.6
     */
    @Deprecated
    public static <T> JsonObjects postJsonReJsonObjects(String url, T params) throws IOException {
        String result = postJson(url, params);
        try {
            return new JsonObjects(result);
        } catch (Exception e) {
            throw new ToolboxHttpException("url:" + url + ",返回结果参数格式错误:" + result);
        }
    }

    /**
     * 发送无参的post表单参数类型请求，并且将结果集转换为 {@link JsonObjects}
     *
     * @param url URL
     * @return JsonObjects
     * @throws IOException
     * @since 2.0.6
     */
    @Deprecated
    public static JsonObjects postFormReJsonObjects(String url) throws IOException {
        return postFormReJsonObjects(url, null);
    }

    /**
     * 发送post表单参数类型的请求，并且将结果集转换为 {@link JsonObjects}
     *
     * @param url    URL
     * @param params 参数
     * @param <T>    类型
     * @return JsonObjects
     * @throws IOException 如果请求失败或响应无法解析为指定的数据类型，则抛出 IOException 异常
     * @since 2.0.6
     */
    @Deprecated
    public static <T> JsonObjects postFormReJsonObjects(String url, T params) throws IOException {
        String result = postForm(url, params);
        try {
            return new JsonObjects(result);
        } catch (Exception e) {
            throw new ToolboxHttpException("url:" + url + ",返回结果参数格式错误:" + result);
        }
    }

    /**
     * 发送get请求，并且将结果集转换为 {@link JsonObjects}
     *
     * @param url URL
     * @return JsonObjects
     * @since 2.0.6
     */
    @Deprecated
    public static JsonObjects getReJsonObjects(String url) throws IOException {
        return getReJsonObjects(url, null);
    }

    /**
     * 发送get请求，并且将结果集转换为 {@link JsonObjects}
     *
     * @param url    URL
     * @param params 参数
     * @param <T>    类型
     * @return JsonObjects
     * @since 2.0.6
     */
    @Deprecated
    public static <T> JsonObjects getReJsonObjects(String url, T params) throws IOException {
        String result = get(url, params, 10);
        try {
            return new JsonObjects(result);
        } catch (Exception e) {
            throw new ToolboxHttpException("url:" + url + ",返回结果参数格式错误:" + result);
        }
    }

    /**
     * 发送get请求，并且将结果集转换为 {@link JsonObjects}
     *
     * @param url     URL
     * @param params  参数
     * @param timeout 超时时间，单位：秒
     * @param <T>     类型
     * @return JsonObjects
     * @since 2.4.2.
     */
    @Deprecated
    public static <T> JsonObjects getReJsonObjects(String url, T params, int timeout) throws IOException {
        String result = get(url, params, timeout);
        try {
            return new JsonObjects(result);
        } catch (Exception e) {
            throw new ToolboxHttpException("url:" + url + ",返回结果参数格式错误:" + result);
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
        url = UrlUtils.urlParam(url, params);
        return get(url);
    }

    /**
     * 发送get请求
     * <p>将data中的键值对拼接成标准的url访问参数</p>
     *
     * @param url     URL
     * @param params  请求参数
     * @param timeout 超时时间，单位：秒
     * @param <T>     请求参数类型
     * @return String
     * @since 2.4.2
     */
    public static <T> String get(String url, T params, int timeout) throws IOException {
        url = UrlUtils.urlParam(url, params);
        return get(url, timeout);
    }

    /**
     * get请求
     * <p>注：url自行拼接查询条件参数</p>
     *
     * @param url URL
     * @return String
     */
    public static String get(String url) throws IOException {
        return get(url, 10);
    }

    /**
     * get请求
     * <p>注：url自行拼接查询条件参数</p>
     *
     * @param url     URL
     * @param timeout 超时时间，单位：秒
     * @return String
     * @throws IOException IO异常
     * @since 2.4.2
     */
    public static String get(String url, int timeout) throws IOException {
        Request request = new Request.Builder().url(url).build();
        return send(request, buildClient(timeout, timeout));
    }

    /**
     * 发送带cookie 的get请求
     *
     * @param url URL
     * @return JsonObjects
     * @throws IOException
     * @since 2.3.0
     */
    public static JsonObjects getByCookieReJsonObjects(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        String result = send(request, COOKIE_CLIENT);
        try {
            return new JsonObjects(result);
        } catch (Exception e) {
            throw new ToolboxHttpException("url:" + url + ",返回结果参数格式错误:" + result);
        }
    }

    /**
     * 发送Get请求
     *
     * @param url           请求URL
     * @param data          参数
     * @param headerContent header 附加内容
     * @param <D>           数据类型
     * @return String
     * @throws IOException IO流错误
     * @since 1.3.0
     */
    public static <D> String get(String url, D data, Map<String, String> headerContent) throws IOException {
        url = UrlUtils.urlParam(url, data);
        Headers headers = Headers.of(headerContent);
        Request request = new Request.Builder()
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
        RequestBody body = RequestBody.create(mediaType, params);
        Request request = new Request.Builder().url(url).post(body).build();
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
        RequestBody body = RequestBody.create(mediaType, params);
        Request request = new Request.Builder().url(url).post(body).build();
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
    public static String send(Request request, OkHttpClient okHttpClient) throws IOException {
        try (Response response = okHttpClient.newCall(request).execute()) {
            ResponseBody body1 = response.body();
            if (body1 == null) {
                throw new ToolboxHttpException("result params is null ");
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
        MediaType mediaType = MediaType.parse(MULTIPART_FORM_DATA);
        return upload(url, params, fileName, fileMap, mediaType);
    }

    /**
     * 上传文件方法
     *
     * @param url       URL
     * @param params    额外参数
     * @param fileName  文件的name(对应后台接收form-data的名称)
     * @param fileMap   文件map、key-文件名称、value-文件路径
     * @param mediaType 每个文件的Content-type
     * @param <K>       key
     * @param <V>       value
     * @return String
     * @throws IOException IO错误
     * @since 2.3.11
     */
    public static <K, V> String upload(String url, Map<K, V> params, String fileName, Map<K, V> fileMap, MediaType mediaType) throws IOException {
        MultipartBody.Builder requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        if (MapUtils.isNotEmpty(params)) {
            for (Map.Entry<K, V> entry : params.entrySet()) {
                requestBody.addFormDataPart(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
        }
        if (MapUtils.isNotEmpty(fileMap)) {
            for (Map.Entry<K, V> entry : fileMap.entrySet()) {
                String name = String.valueOf(entry.getKey());
                String path = String.valueOf(entry.getValue());
                RequestBody fileBody = RequestBody.create(mediaType, new File(path));
                requestBody.addFormDataPart(fileName, name, fileBody);
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody.build())
                .build();
        return send(request);
    }
}
