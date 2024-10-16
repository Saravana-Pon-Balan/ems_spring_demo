package com.e5.ems.exception;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
      super(message, cause);
    }

    public DatabaseException(Throwable cause) {
      super(cause);
    }
}
