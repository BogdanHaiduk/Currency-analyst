package com.exchanges.exception;

public class FichaNotImplementException extends ExchangeRuntimeException {
    public FichaNotImplementException(String message) {
        super(message);
    }

    public FichaNotImplementException(String message, Throwable cause) {
        super(message, cause);
    }

    public FichaNotImplementException(Throwable cause) {
        super(cause);
    }

    public FichaNotImplementException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
