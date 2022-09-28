package com.exchanges.exception;

public class FeatureNotImplementException extends ExchangeRuntimeException {
    public FeatureNotImplementException(String message) {
        super(message);
    }

    public FeatureNotImplementException(String message, Throwable cause) {
        super(message, cause);
    }

    public FeatureNotImplementException(Throwable cause) {
        super(cause);
    }

    public FeatureNotImplementException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
