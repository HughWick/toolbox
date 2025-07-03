package com.github.hugh.json.model;

import lombok.Data;

/**
 * XML 错误信息根对象。
 * 用于封装从 XML 响应中解析出的错误详情。
 */
@Data
public class XmlError {
    /**
     * 错误响应的详细信息。
     */
    private ErrorResponse Error;

    /**
     * 错误响应的详细内容。
     */
    @Data
    public static class ErrorResponse {
        /**
         * 错误代码。
         * 例如："NoSuchBucket"、"InvalidArgument" 等。
         */
        private String Code;

        /**
         * 错误消息。
         * 对错误代码的详细描述，通常更具可读性。
         */
        private String Message;

        /**
         * 相关的键（Key）。
         * 如果错误与特定对象或资源相关，这里会提供该对象的键。
         */
        private String key;

        /**
         * 相关的存储桶名称。
         * 如果错误与特定存储桶相关，这里会提供该存储桶的名称。
         */
        private String BucketName;

        /**
         * 请求的资源。
         * 发生错误的具体资源路径或标识。
         */
        private String Resource;

        /**
         * 请求 ID。
         * 唯一标识一次请求的 ID，可用于问题追踪和排查。
         */
        private String RequestId;

        /**
         * 主机 ID。
         * 处理请求的主机标识。
         */
        private String HostId;
    }
}
