package com.example.demo.exception;

public class DataParseException extends RuntimeException {
    public DataParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
