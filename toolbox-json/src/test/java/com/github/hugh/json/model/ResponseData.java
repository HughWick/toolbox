package com.github.hugh.json.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author AS
 * Date 2023/3/9 11:17
 */
@NoArgsConstructor
@Data
public class ResponseData {

    /**
     * HTTP 请求的 URL 地址。
     */
    private String url;

    /**
     * HTTP 请求的参数信息。
     */
    private ArgsBean args;

    /**
     * HTTP 请求的头部信息。
     */
    private HeadersBean headers;

    private FormBean form;
    private String  json;
    @NoArgsConstructor
    @Data
    public static class ArgsBean {
        private String foo1;
        private String foo2;
    }

    @NoArgsConstructor
    @Data
    public static class FormBean {
        private String foo1;
        private String foo2;
    }
    @NoArgsConstructor
    @Data
    public static class HeadersBean {
        @SerializedName("x-forwarded-proto")
        private String xForwardedProto;
        @SerializedName("x-forwarded-port")
        private String xForwardedPort;
        private String host;
        @SerializedName("X-amzn-trace-id")
        private String xAmznTraceId;
        @SerializedName("sec-ch-ua")
        private String secChUa;
        @SerializedName("sec-ch-ua-mobile")
        private String seCchUaMobile;
        @SerializedName("sec-ch-ua-platform")
        private String secChUaPlatform;
        @SerializedName("upgrade-insecure-requests")
        private String upgradeInsecureRequests;
        @SerializedName("User-Agent")
        private String userAgent;
        private String accept;
        @SerializedName("sec-fetch-site")
        private String secFetchSite;
        @SerializedName("sec-fetch-mode")
        private String secFetchMode;
        @SerializedName("sec-fetch-user")
        private String secFetchUser;
        @SerializedName("sec-fetch-dest")
        private String secFetchDest;
        @SerializedName("Accept-Encoding")
        private String acceptEncoding;
        @SerializedName("accept-language")
        private String acceptLanguage;
        @SerializedName("Content-Length")
        private String contentLength;
        @SerializedName("Content-Type")
        private String contentType;
        private String cookie;
    }
}
