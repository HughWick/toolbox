package com.github.hugh.json.exception;

/**
 * 工具箱 http请求异常类
 *
 * @since 2.3.0
 */
public class ToolboxJsonException extends RuntimeException {
    /**
     * 无参构造器，用于创建一个默认的 ToolboxJsonException 异常实例。
     */
    public ToolboxJsonException() {
    }

    /**
     * 带有错误信息的构造器，用于创建一个带有指定错误信息的 ToolboxJsonException 异常实例。
     *
     * @param message 异常的详细信息，用于描述异常的原因。
     */
    public ToolboxJsonException(String message) {
        super(message);
    }

    /**
     * 带有错误信息和原始异常原因的构造器，用于创建一个带有指定错误信息和根本原因的 ToolboxJsonException 异常实例。
     *
     * @param message 异常的详细信息，用于描述异常的原因。
     * @param cause   另一个 Throwable 实例，表示导致此异常的根本原因。
     */
    public ToolboxJsonException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 带有原始异常原因的构造器，用于创建一个只包含异常原因的 ToolboxJsonException 异常实例。
     *
     * @param cause 另一个 Throwable 实例，表示导致此异常的根本原因。
     */
    public ToolboxJsonException(Throwable cause) {
        super(cause);
    }

    /**
     * 带有错误信息、异常原因、是否启用抑制和是否可写堆栈跟踪信息的构造器。
     * 该构造器用于创建一个详细控制异常特性的 ToolboxJsonException 异常实例。
     *
     * @param message             异常的详细信息，用于描述异常的原因。
     * @param cause               另一个 Throwable 实例，表示导致此异常的根本原因。
     * @param enableSuppression   是否启用抑制，可以抑制或禁用异常的栈跟踪信息。
     * @param writableStackTrace  是否允许堆栈跟踪信息可写，用于调试时查看详细堆栈信息。
     */
    public ToolboxJsonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
