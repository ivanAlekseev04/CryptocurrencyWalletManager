package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
