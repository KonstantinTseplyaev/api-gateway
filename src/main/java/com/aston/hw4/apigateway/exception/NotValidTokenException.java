package com.aston.hw4.apigateway.exception;

public class NotValidTokenException extends RuntimeException {
    public NotValidTokenException(String message) {
        super(message);
    }
}
