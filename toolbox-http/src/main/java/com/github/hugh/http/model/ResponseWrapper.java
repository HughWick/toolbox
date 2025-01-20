package com.github.hugh.http.model;

import lombok.Data;

/**
 * 响应包装类，用于封装 HTTP 响应的相关信息。
 * 包括响应的内容类型、状态码和响应体。
 * <p>
 * 使用该类可以方便地处理 HTTP 请求的返回结果。
 * </p>
 *
 * @author hugh
 * @version 2.7.16
 */
@Data
public class ResponseWrapper {

    /**
     * 响应的内容类型（Content-Type），例如 "application/json", "text/html" 等。
     * <p>
     * 该字段描述了响应体的媒体类型，通常由服务器返回的 HTTP 头部中的 Content-Type 决定。
     * </p>
     */
    private String contentType;

    /**
     * HTTP 响应的状态码，例如 200 表示成功，404 表示未找到，500 表示服务器错误等。
     * <p>
     * 该字段代表了响应的状态，通常由服务器返回的 HTTP 头部中的 Status Code 决定。
     * </p>
     */
    private int responseCode;

    /**
     * 响应体的内容，以字节数组形式存储。
     * <p>
     * 该字段存储服务器返回的实际数据内容，可能是 JSON、HTML、XML 或其他格式的数据。
     * </p>
     */
    private byte[] responseBody;

}