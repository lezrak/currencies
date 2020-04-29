package com.lezrak.currencies.exception;

public class ThirdPartyApiException extends RuntimeException {

    public ThirdPartyApiException() {
        super("Service is currently unavailable.");
    }
}
