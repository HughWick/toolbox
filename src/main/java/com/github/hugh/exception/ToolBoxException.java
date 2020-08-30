package com.github.hugh.exception;

/**
 * 工具箱运行异常类
 *
 * @author hugh
 * @version 1.0.0
 * @since JDK 1.8
 */
public class ToolBoxException extends RuntimeException{
    public ToolBoxException() {
    }

    public ToolBoxException(String message) {
        super(message);
    }

    public ToolBoxException(String message, Throwable cause) {
        super(message, cause);
    }

    public ToolBoxException(Throwable cause) {
        super(cause);
    }

    public ToolBoxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
