package com.exchanges.exception;

public class ExchangeRuntimeException extends RuntimeException {
    public ExchangeRuntimeException() {
    }

    public ExchangeRuntimeException(String message) {
        super(message);
    }

    public ExchangeRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExchangeRuntimeException(Throwable cause) {
        super(cause);
    }

    public ExchangeRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
