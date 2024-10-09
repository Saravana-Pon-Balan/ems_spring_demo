package com.e5.ems.exception;

public class AccessException extends RuntimeException {
    public AccessException(String message, Throwable e) {
        super(message, e);
    }

    public AccessException(String message) {
        super(message);
    }

    public AccessException(Throwable e) {
        super(e);
    }
}
