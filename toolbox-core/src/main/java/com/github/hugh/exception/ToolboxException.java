package com.github.hugh.exception;

/**
 * 工具箱运行异常类
 *
 * @author hugh
 * @since 1.0.0
 */
public class ToolboxException extends RuntimeException {
    /**
     * 构造一个空的 ToolboxException。
     */
    public ToolboxException() {
    }

    /**
     * 使用指定的详细信息构造一个 ToolboxException。
     *
     * @param message 异常的详细信息
     */
    public ToolboxException(String message) {
        super(message);
    }

    /**
     * 使用指定的详细信息和原因构造一个 ToolboxException。
     *
     * @param message 异常的详细信息
     * @param cause   引起此异常的原因
     */
    public ToolboxException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 使用指定的原因构造一个 ToolboxException。
     *
     * @param cause 引起此异常的原因
     */
    public ToolboxException(Throwable cause) {
        super(cause);
    }

    /**
     * 使用指定的详细信息、原因、是否启用抑制和是否可写堆栈跟踪构造一个 ToolboxException。
     *
     * @param message            异常的详细信息
     * @param cause              引起此异常的原因
     * @param enableSuppression  是否启用抑制
     * @param writableStackTrace 是否可写堆栈跟踪
     */
    public ToolboxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
