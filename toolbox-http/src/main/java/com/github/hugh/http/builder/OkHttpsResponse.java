package com.github.hugh.http.builder;

import com.github.hugh.json.gson.GsonUtils;
import com.github.hugh.json.gson.Jsons;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 代表 OkHttp 客户端接收到的 HTTP 响应消息。
 *
 * @author hugh
 * @since 2.5.1
 */
@Data
@Builder
@AllArgsConstructor
public class OkHttpsResponse {

    private String message; // 响应消息

    /**
     * 将 HTTP 响应消息反序列化为指定类型的 Java 对象。
     *
     * @param clazz 反序列化的目标类型。
     * @param <T>   泛型参数，表示反序列化的目标类型。
     * @return 返回反序列化后的 Java 对象。
     */
    public <T> T fromJson(Class<T> clazz) {
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
    public Jsons reJsons() {
        return new Jsons(this.message);
    }
}
