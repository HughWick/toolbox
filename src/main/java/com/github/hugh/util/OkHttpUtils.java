package com.github.hugh.util;

import com.github.hugh.exception.ToolboxException;
import net.sf.json.JSONObject;
import okhttp3.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp工具类
 *
 * @author hugh
 */
public class OkHttpUtils {
    private OkHttpUtils() {

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
     * 统一编码
     */
    private static final String CHARSET = "utf-8";

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
     * 默认OkHttp请求客户端
     */
    private static OkHttpClient CLIENT = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS) // 设置连接超时
            .readTimeout(10, TimeUnit.SECONDS) // 设置读超时
//				 .writeTimeout(60,TimeUnit.SECONDS)          //设置写超时
//			     .retryOnConnectionFailure(true)             //是否自动重连
            .build(); // 构建OkHttpClient对象

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
     *
     * @param url  请求URL
     * @param json 参数
     * @return String
     */
    public static String postForm(String url, JSONObject json) throws IOException {
        String params = jsonParse(json);
        return post(url, params, FORM_TYPE, CLIENT);
    }

    /**
     * 发送json形式参数的post请求
     * <p>Content-Type:application/x-www-form-urlencoded</p>
     *
     * @param url  请求URL
     * @param json 参数
     * @return String
     */
    public static String postJson(String url, JSONObject json) throws IOException {
        String params = jsonParse(json);
        return post(url, params, JSON_TYPE, CLIENT);
    }

    /**
     * 发送POST 后返回结果转换为JSON
     * <p>Content-Type:application/x-www-form-urlencoded</p>
     *
     * @param url  请求URL
     * @param json 参数
     * @return JSONObject
     */
    public static JSONObject postFormReJSON(String url, JSONObject json) throws IOException {
        String result = postForm(url, json);
        if (EmptyUtils.isEmpty(result)) {
            return null;
        }
        try {
            return JSONObject.fromObject(result);
        } catch (Exception e) {
            throw new RuntimeException("request 返回结果格式错误:" + result);
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
                value = URLEncoder.encode(value, CHARSET);//将参数转换为urlEncoder码
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
     * @param url  URL
     * @param data 请求参数
     * @return String
     */
    public static String get(String url, JSONObject data) {
        if (EmptyUtils.isEmpty(url)) {
            return "";
        }
        String last = url.substring(url.length() - 1);
        if (!"?".equals(last)) {// 当最后一个字符不为"?"时进行拼接
            url += "?";
        }
        String params = jsonParse(data);
        url += params;
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
            return send(request, CLIENT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 统一的post请求
     *
     * @param url          URL
     * @param params       参数
     * @param mediaType    请求类型
     * @param okHttpClient 请求客户端
     * @return String 请求结果
     * @throws IOException
     */
    private static String post(String url, String params, MediaType mediaType, OkHttpClient okHttpClient) throws IOException {
        RequestBody body = RequestBody.create(mediaType, params);
        Request request = new Request.Builder().url(url).post(body).build();
        return send(request, okHttpClient);
    }

    /**
     * 统一请求
     *
     * @param request      请求内容
     * @param okHttpClient OkHttpClient
     * @return String 返回结果
     * @throws IOException
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
     * @since 1.2.4
     */
    public static String postFormCookie(String url, JSONObject json) throws IOException {
        String params = jsonParse(json);
        return post(url, params, FORM_TYPE, COOKIE_CLIENT);
    }
}
