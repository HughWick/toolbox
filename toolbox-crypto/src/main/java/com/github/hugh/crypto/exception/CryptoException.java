package com.github.hugh.crypto.exception;

/**
 * 验证异常
 * User: AS
 * Date: 2022/1/20 10:25
 */
public class CryptoException extends RuntimeException {
    public CryptoException() {
    }

    public CryptoException(String message) {
        super(message);
    }

    public CryptoException(String message, Throwable cause) {
        super(message, cause);
    }

    public CryptoException(Throwable cause) {
        super(cause);
    }

    public CryptoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
