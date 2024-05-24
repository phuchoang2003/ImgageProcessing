package com.example.dev.version1.exceptionHandling;

public class InputInvalidException extends RuntimeException{
    public InputInvalidException(String message) {
        super(message);
    }

    public InputInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public InputInvalidException(Throwable cause) {
        super(cause);
    }

    protected InputInvalidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
