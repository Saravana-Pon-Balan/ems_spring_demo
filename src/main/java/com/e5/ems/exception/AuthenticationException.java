package com.e5.ems.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message, Throwable e) {
      super(message, e);
    }

    public AuthenticationException(String message) {
      super(message);
    }

    public AuthenticationException(Throwable e) {
      super(e);
    }

}
