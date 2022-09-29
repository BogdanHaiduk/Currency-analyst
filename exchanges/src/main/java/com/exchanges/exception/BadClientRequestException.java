package com.exchanges.exception;

public class BadClientRequestException extends RuntimeException {
    public BadClientRequestException() {
    }

    public BadClientRequestException(String message) {
        super(message);
    }

    public BadClientRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadClientRequestException(Throwable cause) {
        super(cause);
    }

    public BadClientRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
