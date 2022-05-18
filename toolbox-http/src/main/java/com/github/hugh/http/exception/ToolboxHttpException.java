package com.github.hugh.http.exception;

/**
 * 工具箱 http请求异常类
 *
 * @since 2.3.0
 */
public class ToolboxHttpException extends RuntimeException {
    public ToolboxHttpException() {
    }

    public ToolboxHttpException(String message) {
        super(message);
    }

    public ToolboxHttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public ToolboxHttpException(Throwable cause) {
        super(cause);
    }

    public ToolboxHttpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
