package com.github.hugh.http.constant;

import okhttp3.Cookie;
import okhttp3.MediaType;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * okhttp 请求常量类
 *
 * @author hugh
 * @since 2.5.1
 */
public class OkHttpCode {
    /**
     * 参数以表单形式提交类型
     */
    public static final String APPLICATION_FORM_URLENCODED_VALUE = "application/x-www-form-urlencoded";

    /**
     * 参数以json形式提交类型
     */
    public static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";

    /**
     * 参数以form表单提交类型
     */
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    /**
     * 本地cookie存储
     */
    public static final ConcurrentHashMap<String, List<Cookie>> COOKIE_STORE = new ConcurrentHashMap<>();

    /**
     * 表单参数请求类型
     */
    public static final MediaType FORM_TYPE = MediaType.parse(APPLICATION_FORM_URLENCODED_VALUE);

    /**
     * json参数请求类型
     */
    public static final MediaType JSON_TYPE = MediaType.get(APPLICATION_JSON_UTF8_VALUE);

}
