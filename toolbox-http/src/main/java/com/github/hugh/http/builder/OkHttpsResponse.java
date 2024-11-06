package com.github.hugh.http.builder;

import com.github.hugh.crypto.Md5Utils;
import com.github.hugh.json.gson.GsonUtils;
import com.github.hugh.json.gson.Jsons;
import com.google.gson.JsonParseException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 代表 OkHttp 客户端接收到的 HTTP 响应消息。
 *
 * @author hugh
 * @since 2.5.1
 */
@Slf4j
@Getter
public class OkHttpsResponse {

    private final String message; // 响应消息
    private final byte[] bytes;
    private String url;//

    /**
     * 构造一个 OkHttpsResponse 对象。
     *
     * @param result 响应的字节数组
     * @since 2.7.5
     */
    public OkHttpsResponse(byte[] result) {
        this.bytes = result;
        this.message = new String(result);
    }

    /**
     * 构造一个 {@code OkHttpsResponse} 实例。
     *
     * @param result 服务器响应的字节数组。这些字节数据会被用来创建响应消息。
     * @param url    请求的 URL。这是发送请求时使用的 URL。
     * @since 2.7.13
     * <p>此构造函数将响应的字节数组转换为字符串，并存储请求的 URL。</p>
     */
    public OkHttpsResponse(byte[] result, String url) {
        this.bytes = result;
        this.message = new String(result);
        this.url = url;
    }

    /**
     * 将 HTTP 响应消息反序列化为指定类型的 Java 对象。
     *
     * @param clazz 反序列化的目标类型。
     * @param <T>   泛型参数，表示反序列化的目标类型。
     * @return 返回反序列化后的 Java 对象。
     */
    public <T> T fromJson(Class<T> clazz) {
        if (GsonUtils.isNotJsonValid(this.message)) {
            log.error("请求接口返回非法json字符串，URL：{}，内容：{}", this.url, this.message);
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
}
