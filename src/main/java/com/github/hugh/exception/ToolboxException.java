package com.github.hugh.exception;

/**
 * 工具箱运行异常类
 *
 * @author hugh
 * @version 1.0.0
 * @since JDK 1.8
 */
public class ToolboxException extends RuntimeException{
    public ToolboxException() {
    }

    public ToolboxException(String message) {
        super(message);
    }

    public ToolboxException(String message, Throwable cause) {
        super(message, cause);
    }

    public ToolboxException(Throwable cause) {
        super(cause);
    }

    public ToolboxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
