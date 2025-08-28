package com.github.hugh.exception;

/**
 * {@code ConstantsLoadException} 是一个运行时异常，
 * 当尝试通过反射机制加载类中的常量（如静态 final 字段）时发生错误时抛出。
 * <p>
 * 此异常通常表示以下情况之一：
 */
public class ConstantsLoadException extends RuntimeException {

    /**
     * 构造一个新的 {@code ConstantsLoadException} 实例，带指定的详细消息和原因。
     *
     * @param message 详细消息，用于描述异常的具体原因。
     * @param cause   导致此异常的原因（例如，原始的反射异常）。
     * 此参数用于异常链，可以为 {@code null}。
     */
    public ConstantsLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
