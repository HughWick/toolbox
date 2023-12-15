package com.github.hugh.mongodb.exception;

/**
 * 自定义的 MongoDB 查询异常。
 *
 * @author hugh
 * @since 2.7.0
 */
public class ToolboxMongoException extends RuntimeException {

    /**
     * 构造一个空的 MongoQueryException。
     */
    public ToolboxMongoException() {
    }

    /**
     * 使用指定的详细信息构造一个 MongoQueryException。
     *
     * @param message 异常的详细信息
     */
    public ToolboxMongoException(String message) {
        super(message);
    }

    /**
     * 使用指定的详细信息和原因构造一个 MongoQueryException。
     *
     * @param message 异常的详细信息
     * @param cause   引起此异常的原因
     */
    public ToolboxMongoException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 使用指定的原因构造一个 MongoQueryException。
     *
     * @param cause 引起此异常的原因
     */
    public ToolboxMongoException(Throwable cause) {
        super(cause);
    }

    /**
     * 使用指定的详细信息、原因、是否启用抑制和是否可写堆栈跟踪构造一个 MongoQueryException。
     *
     * @param message            异常的详细信息
     * @param cause              引起此异常的原因
     * @param enableSuppression  是否启用抑制
     * @param writableStackTrace 是否可写堆栈跟踪
     */
    public ToolboxMongoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
