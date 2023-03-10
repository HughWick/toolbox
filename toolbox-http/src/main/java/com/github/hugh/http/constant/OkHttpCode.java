package com.github.hugh.http.constant;

import okhttp3.Cookie;

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
     * 本地cookie存储
     */
    public static final ConcurrentHashMap<String, List<Cookie>> COOKIE_STORE = new ConcurrentHashMap<>();

}
