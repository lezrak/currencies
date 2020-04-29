package com.lezrak.currencies.exception;

public class CurrencyNotFoundException extends RuntimeException {

    public CurrencyNotFoundException(String currency) {
        super(String.format("Currency %s cannot be found.", currency));
    }
}
