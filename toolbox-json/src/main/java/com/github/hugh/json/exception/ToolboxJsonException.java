package com.github.hugh.json.exception;

/**
 * 工具箱 http请求异常类
 *
 * @since 2.3.0
 */
public class ToolboxJsonException extends RuntimeException {
    public ToolboxJsonException() {
    }

    public ToolboxJsonException(String message) {
        super(message);
    }

    public ToolboxJsonException(String message, Throwable cause) {
        super(message, cause);
    }

    public ToolboxJsonException(Throwable cause) {
        super(cause);
    }

    public ToolboxJsonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
