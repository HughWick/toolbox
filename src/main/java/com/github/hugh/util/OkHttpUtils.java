package com.github.hugh.util;

import com.github.hugh.constant.CharsetCode;
import com.github.hugh.exception.ToolboxException;
import com.github.hugh.support.instance.Instance;
import com.github.hugh.util.gson.JsonObjectUtils;
import com.google.gson.JsonObject;
import net.sf.json.JSONObject;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
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
public class OkHttpUtils {
    private OkHttpUtils() {

    }

    /**
     * 构建OkHttpClient对象
     * <p>链接超时为10秒</p>
     * <p>设置读超时10秒</p>
     *
     * @return OkHttpClient
     * @since 1.3.3
     */
    public static OkHttpClient buildClient() {
        return buildClient(10, 10);
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
    private final static ConcurrentHashMap<String, List<Cookie>> COOKIE_STORE = new ConcurrentHashMap<>();

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
    private static OkHttpClient COOKIE_CLIENT = new OkHttpClient.Builder()
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
     * 发送表单形式参数的POST
     * <p>Content-Type:application/x-www-form-urlencoded</p>
     * <p>该方法调用{@link #postForm(String, JSONObject)}发送post请求</p>
     *
     * @param url 请求URL
     * @param map Map参数
     * @return String
     * @throws IOException IO流错误
     * @since 1.3.8
     */
    public static String postForm(String url, Map map) throws IOException {
        return postForm(url, JSONObject.fromObject(map));
    }

    /**
     * 发送表单形式参数的POST
     * <p>Content-Type:application/x-www-form-urlencoded</p>
     *
     * @param url  请求URL
     * @param json 参数
     * @return String
     * @throws IOException IO流错误
     */
    public static String postForm(String url, JSONObject json) throws IOException {
        String params = jsonParse(json);
        return post(url, params, FORM_TYPE, buildClient());
    }

    /**
     * 发送表单形式参数的POST
     * <p>Content-Type:application/x-www-form-urlencoded</p>
     *
     * @param url           请求URL
     * @param json          参数
     * @param headerContent header 附加内容
     * @return String
     * @throws IOException IO流错误
     * @since 1.3.0
     */
    public static String postForm(String url, JSONObject json, Map<String, String> headerContent) throws IOException {
        String params = jsonParse(json);
        RequestBody body = RequestBody.create(FORM_TYPE, params);
        Headers headers = Headers.of(headerContent);
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .post(body).build();
        return send(request, buildClient());
    }

    /**
     * 发送json形式参数的post请求
     * <p>Content-Type:application/x-www-form-urlencoded</p>
     * <p>该方法调用{@link #postJson(String, JSONObject) }进行post请求</p>
     *
     * @param url 请求URL
     * @param map 参数
     * @return String
     * @throws IOException IO流错误
     * @since 1.3.8
     */
    public static String postJson(String url, Map map) throws IOException {
        return postJson(url, JSONObject.fromObject(map));
    }

    /**
     * 发送json形式参数的post请求
     * <p>Content-Type:application/x-www-form-urlencoded</p>
     *
     * @param url  请求URL
     * @param json 参数
     * @return String
     * @throws IOException IO流错误
     */
    public static String postJson(String url, JSONObject json) throws IOException {
        RequestBody body = RequestBody.create(JSON_TYPE, json.toString());
        Request request = new Request.Builder().url(url).post(body).build();
        return send(request);
    }

    /**
     * 发送POST 后返回结果转换为JSON
     * <p>Content-Type:application/x-www-form-urlencoded</p>
     * <ul>
     * <li>由于JSONObject底层将字符串转成JSONObject时效率过低,在版本1.3.0开始废弃</li>
     * <li>建议使用Gson版本的转换方法{@link #postFormReJsonObject(String, JSONObject)}</li>
     * </ul>
     *
     * @param url  请求URL
     * @param json 参数
     * @return JSONObject
     * @throws IOException IO流错误
     */
    @Deprecated
    public static JSONObject postFormReJSON(String url, JSONObject json) throws IOException {
        String result = postForm(url, json);
        try {
            return JSONObject.fromObject(result);
        } catch (Exception e) {
            throw new ToolboxException("request 返回结果格式错误:" + result);
        }
    }

    /**
     * 发送POST 后返回结果转换为{@link JsonObject}
     * <p>Content-Type:application/x-www-form-urlencoded</p>
     * <p>该方法调用{@link #postFormReJsonObject(String, JSONObject) }进行post请求</p>
     *
     * @param url    请求URL
     * @param params 参数
     * @return {@link JsonObject}
     * @throws IOException IO流错误
     * @since 1.5.2
     */
    public static JsonObject postFormReJsonObject(String url, Map params) throws IOException {
        return postFormReJsonObject(url, JSONObject.fromObject(params));
    }

    /**
     * 发送POST 后返回结果转换为{@link JsonObject}
     * <p>Content-Type:application/x-www-form-urlencoded</p>
     *
     * @param url    请求URL
     * @param params 参数
     * @return {@link JsonObject}
     * @throws IOException IO流错误
     * @since 1.3.0
     */
    public static JsonObject postFormReJsonObject(String url, JSONObject params) throws IOException {
        String result = postForm(url, params);
        try {
            return JsonObjectUtils.parse(result);
        } catch (Exception e) {
            throw new ToolboxException("url:" + url + ",返回结果参数格式错误:" + result);
        }
    }

    /**
     * 将json中键值对拼接为url对应的参数
     *
     * @param json 参数
     * @return String 拼接后的URL参数字符串
     */
    private static String jsonParse(JSONObject json) {
        if (json == null || json.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<?> iterator = json.keys();
        while (iterator.hasNext()) {
            String key = String.valueOf(iterator.next());
            String value = String.valueOf(json.get(key));
            try {
                value = URLEncoder.encode(value, CharsetCode.UTF_8);//将参数转换为urlEncoder码
                sb.append(key).append("=").append(value).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        sb.deleteCharAt(sb.length() - 1); // 删除结尾符号
        return sb.toString();
    }


    /**
     * 发送get请求
     * <p>将data中的键值对拼接成标准的url访问参数</p>
     *
     * @param url    URL
     * @param params 请求参数
     * @return String
     * @since 1.4.17
     */
    public static String get(String url, Map params) {
        return get(url, JSONObject.fromObject(params));
    }

    /**
     * 发送get请求
     * <p>将data中的键值对拼接成标准的url访问参数</p>
     *
     * @param url    URL
     * @param params 请求参数
     * @return String
     */
    public static String get(String url, JSONObject params) {
        url = urlParam(url, params);
        return get(url);
    }

    /**
     * get请求
     * <p>注：url自行拼接查询条件参数</p>
     *
     * @param url URL
     * @return String
     */
    public static String get(String url) {
        Request request = new Request.Builder().url(url).build();
        try {
            return send(request, buildClient());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 在url后拼接参数
     *
     * @param url  URL
     * @param data 参数
     * @return String 拼接好参数的Get请求url
     * @since 1.3.0
     */
    private static String urlParam(String url, JSONObject data) {
        if (EmptyUtils.isEmpty(url)) {
            return "";
        }
        String last = url.substring(url.length() - 1);
        if (!"?".equals(last)) {// 当最后一个字符不为"?"时进行拼接
            url += "?";
        }
        String params = jsonParse(data);
        url += params;
        return url;
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
        url = urlParam(url, data);
        Headers headers = Headers.of(headerContent);
        Request request = new Request.Builder()
                .url(url)
                .headers(headers).build();
        return send(request);
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
    private static String send(Request request, OkHttpClient okHttpClient) throws IOException {
        try (Response response = okHttpClient.newCall(request).execute()) {
            ResponseBody body1 = response.body();
            if (body1 == null) {
                throw new ToolboxException("result params is null ");
            }
            return body1.string();
        } catch (SocketTimeoutException timeEx) {
            timeEx.printStackTrace();
            return null;
        }
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
        String params = jsonParse(json);
        return post(url, params, FORM_TYPE, COOKIE_CLIENT);
    }

    /**
     * 上传文件方法
     *
     * @param url      URL
     * @param params   额外参数
     * @param fileName 文件的name
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
                String name = String.valueOf(entry.getKey());
                String value = String.valueOf(entry.getValue());
                requestBody.addFormDataPart(name, value);
            }
        }
        if (MapUtils.isNotEmpty(params)) {
            for (Map.Entry<K, V> entry : fileMap.entrySet()) {
                String name = String.valueOf(entry.getKey());
                String path = String.valueOf(entry.getValue());
                RequestBody fileBody = RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), new File(path));
                requestBody.addFormDataPart(fileName, name, fileBody);
            }
        }
        Request request = new Request.Builder().url(url).post(requestBody.build()).build();
        return send(request);
    }
}
