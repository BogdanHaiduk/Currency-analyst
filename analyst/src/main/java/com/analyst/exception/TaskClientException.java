package com.analyst.exception;

public class TaskClientException extends RuntimeException {
    public TaskClientException() {
    }

    public TaskClientException(String message) {
        super(message);
    }

    public TaskClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskClientException(Throwable cause) {
        super(cause);
    }

    public TaskClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
