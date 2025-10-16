package com.example.demo.exception;

public class UserNotLoggedInException extends IllegalStateException {

    public UserNotLoggedInException(String message) {
        super(message);
    }
}