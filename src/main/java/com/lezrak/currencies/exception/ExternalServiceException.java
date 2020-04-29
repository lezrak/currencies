package com.lezrak.currencies.exception;

public class ExternalServiceException extends RuntimeException {

    public ExternalServiceException() {
        super("Service is currently unavailable.");
    }
}
