package com.lezrak.currencies.exception;

public class BlankCurrencyException extends RuntimeException{

    public BlankCurrencyException() {
        super("Currency name cannot be blank.");
    }
}
