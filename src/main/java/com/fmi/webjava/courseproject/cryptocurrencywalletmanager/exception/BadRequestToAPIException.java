package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.exception;

public class BadRequestToAPIException extends RuntimeException {
    public BadRequestToAPIException(String message) {
        super(message);
    }

    public BadRequestToAPIException(String message, Throwable cause) {
        super(message, cause);
    }
}
