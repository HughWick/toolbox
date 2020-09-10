package com.github.hugh.util;

import com.github.hugh.exception.ToolBoxException;
import net.sf.json.JSONObject;
import okhttp3.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp工具类
 *
 * @author hugh
 */
public class OkHttpUtils {
    private OkHttpUtils() {

    }

    private static OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS) // 设置连接超时
            .readTimeout(10, TimeUnit.SECONDS) // 设置读超时
//				 .writeTimeout(60,TimeUnit.SECONDS)          //设置写超时
//			     .retryOnConnectionFailure(true)             //是否自动重连
            .build(); // 构建OkHttpClient对象

    /**
     * 表单形式提交类型
     */
    private static final String APPLICATION_FORM_URLENCODED_VALUE = "application/x-www-form-urlencoded";

    /**
     * json形式提交类型
     */
    private static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";

    /**
     * 统一编码
     */
    private static final String CHARSET = "utf-8";


    /**
     * 表单参数请求类型
     */
    private static final MediaType FORM_TYPE = MediaType.parse(APPLICATION_FORM_URLENCODED_VALUE);

    /**
     * json参数请求类型
     */
    private static final MediaType JSON_TYPE = MediaType.get(APPLICATION_JSON_UTF8_VALUE);

    /**
     * 发送表单形式参数的POST
     * <p>Content-Type:application/x-www-form-urlencoded</p>
     *
     * @param url  请求URL
     * @param json 参数
     * @return String
     */
    public static String postForm(String url, JSONObject json) {
        String params = jsonParse(json);
        try {
            return post(url, params, FORM_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 发送json形式参数的post请求
     * <p>Content-Type:application/x-www-form-urlencoded</p>
     *
     * @param url  请求URL
     * @param json 参数
     * @return String
     */
    public static String postJson(String url, JSONObject json) {
        String params = jsonParse(json);
        try {
            return post(url, params, JSON_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 发送POST 后返回结果转换为JSON
     * <p>Content-Type:application/x-www-form-urlencoded</p>
     *
     * @param url  请求URL
     * @param json 参数
     * @return JSONObject
     */
    public static JSONObject postFormReJSON(String url, JSONObject json) {
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
                sb.append(key + "=" + value + "&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        sb.deleteCharAt(sb.length() - 1); // 删除结尾符号
        return sb.toString();
    }

    /**
     * 统一的post请求
     *
     * @param url       URL
     * @param params    参数
     * @param mediaType 请求类型
     * @return String
     * @throws IOException
     */
    private static String post(String url, String params, MediaType mediaType) throws IOException {
        RequestBody body = RequestBody.create(mediaType, params);
        Request request = new Request.Builder().url(url).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            ResponseBody body1 = response.body();
            if (body1 == null) {
                throw new ToolBoxException("result params is null ");
            }
            return body1.string();
        } catch (SocketTimeoutException timeEx) {
            timeEx.printStackTrace();
            return null;
        }
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
     *
     * @param url URL
     *            <p>注：url自行拼接查询条件参数</p>
     * @return String
     */
    public static String get(String url) {
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            ResponseBody body1 = response.body();
            if (body1 == null) {
                throw new ToolBoxException("result params is null ");
            }
            return body1.string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
