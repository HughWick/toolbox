package com.github.hugh.http.builder;

import com.github.hugh.http.constant.OkHttpCode;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * HttpClient 类封装了 OkHttp 的单例实现，提供 HTTP 请求相关功能。
 *
 * @author hugh
 * @since 2.5.1
 */
//public class HttpClient {
//    private OkHttpClient okHttpClient;

    // 私有构造方法，初始化 OkHttpClient 对象
//    private HttpClient() {
//        okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(10, TimeUnit.SECONDS)
//                .writeTimeout(10, TimeUnit.SECONDS)
//                .retryOnConnectionFailure(true)
//                .build();
//    }

    /**
     * OkHttp管理cookie
     */
//    public static final OkHttpClient cookieClient = new OkHttpClient.Builder()
//            .cookieJar(new CookieJar() {
//                @Override
//                public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
//                    OkHttpCode.COOKIE_STORE.put(httpUrl.host(), list);
//                }
//
//                @NotNull
//                @Override
//                public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
//                    List<Cookie> cookies = OkHttpCode.COOKIE_STORE.get(httpUrl.host());
//                    return cookies == null ? new ArrayList<>() : cookies;
//                }
//            }).connectTimeout(10, TimeUnit.SECONDS) // 设置连接超时
//            .readTimeout(10, TimeUnit.SECONDS)// 设置读超时
//            .build();

//    /**
//     * 获取 HttpClient 实例。
//     *
//     * @return 返回 HttpClient 实例。
//     */
//    public static HttpClient getInstance() {
//        return SingletonHolder.INSTANCE;
//    }
//
//    /**
//     * 设置连接超时时间。
//     *
//     * @param timeout 连接超时时间，单位为秒。
//     */
//    public void setConnectTimeout(int timeout) {
//        this.okHttpClient = this.okHttpClient.newBuilder().connectTimeout(timeout, TimeUnit.SECONDS).build();
//    }
//
//    /**
//     * 设置读取超时时间。
//     *
//     * @param timeout 读取超时时间，单位为秒。
//     */
//    public void setReadTimeout(int timeout) {
//        this.okHttpClient = this.okHttpClient.newBuilder().readTimeout(timeout, TimeUnit.SECONDS).build();
//    }
//
//    /**
//     * 设置写入超时时间。
//     *
//     * @param timeout 写入超时时间，单位为秒。
//     */
//    public void setWriteTimeout(int timeout) {
//        this.okHttpClient = this.okHttpClient.newBuilder().writeTimeout(timeout, TimeUnit.SECONDS).build();
//    }
//
//    /**
//     * 获取 OkHttpClient 实例。
//     *
//     * @return 返回 OkHttpClient 实例。
//     */
//    public OkHttpClient getOkHttpClient() {
//        return okHttpClient;
//    }
//
//    // 静态内部类，用于创建 HttpClient 的单例实例
//    private static class SingletonHolder {
//        private static final HttpClient INSTANCE = new HttpClient();
//    }
//}
