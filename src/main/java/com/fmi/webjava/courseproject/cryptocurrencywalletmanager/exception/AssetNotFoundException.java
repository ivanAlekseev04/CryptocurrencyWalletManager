package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.exception;

public class AssetNotFoundException extends RuntimeException {
    public AssetNotFoundException(String message) {
        super(message);
    }

    public AssetNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
