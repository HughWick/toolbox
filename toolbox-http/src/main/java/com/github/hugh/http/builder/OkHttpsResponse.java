package com.github.hugh.http.builder;

import com.github.hugh.crypto.Md5Utils;
import com.github.hugh.json.gson.GsonUtils;
import com.github.hugh.json.gson.Jsons;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 代表 OkHttp 客户端接收到的 HTTP 响应消息。
 * <p>
 * 该类封装了 OkHttp 客户端发起请求后返回的响应信息，包括响应的状态码、响应消息体内容、URL 以及内容类型等信息。
 * </p>
 *
 * @author hugh
 * @since 2.5.1
 */
@Slf4j
@Getter
public class OkHttpsResponse {

    /**
     * 响应消息的描述信息，通常用于表示响应的具体信息。
     * <p>
     * 该字段可能包含一些简单的状态描述，如“OK”, “Not Found” 或“Internal Server Error”等。
     * 具体的消息内容通常由服务器根据响应的状态码设置。
     * </p>
     */
    private final String message;

    /**
     * 响应体的内容，存储为字节数组形式。
     * <p>
     * 该字段包含了从服务器返回的实际数据，通常是 JSON、HTML、XML 或其他格式的数据。
     * 需要根据响应的内容类型来解析这些字节数据。
     * </p>
     */
    private final byte[] bytes;

    /**
     * 请求的 URL 地址。
     * <p>
     * 该字段存储当前 HTTP 请求所请求的完整 URL 地址。
     * 可以用于调试或记录请求的来源信息。
     * </p>
     */
    private String url;

    /**
     * 响应内容的类型（Content-Type），例如 "application/json", "text/html" 等。
     * <p>
     * 该字段描述了响应体的媒体类型，通常由服务器返回的 HTTP 头部中的 Content-Type 决定。
     * 根据该信息，可以决定如何解析响应体的数据格式。
     * </p>
     */
    private String contentType;

    /**
     * HTTP 响应的状态码，例如 200 表示成功，404 表示未找到，500 表示服务器错误等。
     * <p>
     * 该字段代表了响应的状态，通常由服务器返回的 HTTP 头部中的 Status Code 决定。
     * 可以通过该字段快速了解响应的状态，如成功、失败或发生错误等。
     * </p>
     */
    private int responseCode;

    /**
     * 构造一个 {@link OkHttpsResponse} 实例，用于封装 HTTP 响应的相关信息。
     * <p>
     * 该构造方法接受响应体的字节数组、响应的 URL、响应内容的类型、以及响应的状态码，创建一个新的响应对象。
     * </p>
     *
     * @param result       HTTP 响应体的字节数组，表示从服务器返回的实际数据。
     *                     该数据可能是 JSON、HTML、XML 或其他格式，具体内容由响应的 Content-Type 决定。
     * @param url          响应请求的 URL 地址，指示当前 HTTP 请求的目标。
     *                     用于追踪或记录请求的来源。
     * @param contentType  响应体的内容类型，表示响应的媒体类型（如 "application/json"、"text/html" 等）。
     *                     用于根据内容类型解析响应体的数据格式。
     * @param responseCode HTTP 响应的状态码，指示请求的处理结果。例如，200 表示成功，404 表示未找到，500 表示服务器错误等。
     *                     该状态码帮助客户端快速了解请求的处理结果。
     * @since 2.7.16
     */
    public OkHttpsResponse(byte[] result, String url, String contentType, int responseCode) {
        this.bytes = result;
        // 将字节数组 result 转换为字符串并赋值给 message 字段
        this.message = new String(result);
        this.url = url;
        this.contentType = contentType;
        this.responseCode = responseCode;
    }

    /**
     * 将 HTTP 响应消息反序列化为指定类型的 Java 对象。
     *
     * @param clazz 反序列化的目标类型。
     * @param <T>   泛型参数，表示反序列化的目标类型。
     * @return 返回反序列化后的 Java 对象。
     */
    public <T> T fromJson(Class<T> clazz) {
        if (isErrorResponse()) {
            return null;
        }
        return GsonUtils.fromJson(this.message, clazz);
    }

    /**
     * 获取 HTTP 响应消息的字符串形式。
     *
     * @return 返回 HTTP 响应消息的字符串形式。
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * 将 message 字符串转换为 Jsons 对象。
     *
     * @return Jsons 对象
     */
    public Jsons toJsons() {
        if (isErrorResponse()) {
            return null;
        }
        return new Jsons(this.message);
    }

    /**
     * 使用Gson库将JSON字符串转换为给定类别的对象列表。
     *
     * @param clazz 对象列表中的对象类型
     * @param <T>   对象列表中的对象类型
     * @return 给定类别的对象列表
     * @since 2.5.9
     */
    public <T> List<T> toList(Class<T> clazz) {
        if (isErrorResponse()) {
            return null;
        }
        return GsonUtils.toArrayList(this.message, clazz);
    }

    /**
     * 返回通过 MD5 算法加密字节数组后的结果。
     *
     * @return 加密后的 MD5 值
     * @since 2.7.15
     */
    public String md5() {
        return Md5Utils.encryptBytes(bytes);
    }

    /**
     * 判断响应状态码是否为 404（即未找到页面）。
     *
     * @return 如果响应码是 404，返回 true；否则返回 false。
     * @since 2.7.16
     */
    public boolean is404() {
        return this.responseCode == 404;
    }

    /**
     * 判断响应状态码是否为 200（即请求成功）。
     *
     * @return 如果响应码是 200，返回 true；否则返回 false。
     * @since 2.7.16
     */
    public boolean is200() {
        return this.responseCode == 200;
    }

    /**
     * 判断响应状态码是否为 400（即错误的请求）。
     *
     * @return 如果响应码是 400，返回 true；否则返回 false。
     * @since 2.7.16
     */
    public boolean is400() {
        return this.responseCode == 400;
    }

    /**
     * 判断响应状态码是否为 401（即未授权）。
     *
     * @return 如果响应码是 401，返回 true；否则返回 false。
     * @since 2.7.16
     */
    public boolean is401() {
        return this.responseCode == 401;
    }

    /**
     * 判断响应状态码是否为 403（即禁止访问）。
     *
     * @return 如果响应码是 403，返回 true；否则返回 false。
     * @since 2.7.16
     */
    public boolean is403() {
        return this.responseCode == 403;
    }

    /**
     * 判断响应状态码是否为 500（即服务器内部错误）。
     *
     * @return 如果响应码是 500，返回 true；否则返回 false。
     * @since 2.7.16
     */
    public boolean is500() {
        return this.responseCode == 500;
    }

    /**
     * 判断响应状态码是否为 502（即坏网关）。
     *
     * @return 如果响应码是 502，返回 true；否则返回 false。
     * @since 2.7.16
     */
    public boolean is502() {
        return this.responseCode == 502;
    }

    /**
     * 判断响应状态码是否为 503（即服务不可用）。
     *
     * @return 如果响应码是 503，返回 true；否则返回 false。
     * @since 2.7.16
     */
    public boolean is503() {
        return this.responseCode == 503;
    }

    /**
     * 判断响应状态码是否为 504（即网关超时）。
     *
     * @return 如果响应码是 504，返回 true；否则返回 false。
     * @since 2.7.16
     */
    public boolean is504() {
        return this.responseCode == 504;
    }

    /**
     * 检查接口响应是否出错。
     * 主要检查两种错误：
     * 1. 404 错误（找不到资源）。
     * 2. JSON 格式错误（响应内容不是合法的 JSON 字符串）。
     *
     * @return 如果发生错误则返回 true，否则返回 false。
     */
    private boolean isErrorResponse() {
        if (is404()) {
            log.error("请求接口响应404，URL：{}，响应内容：{}", this.url, this.message);
            return true;
        }
        if (GsonUtils.isNotJsonValid(this.message)) {
            log.error("请求接口返回非法json字符串，URL：{}，响应内容：{}", this.url, this.message);
            return true;
        }
        return false;
    }
}
