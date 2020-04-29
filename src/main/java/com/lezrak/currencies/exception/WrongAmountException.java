package com.lezrak.currencies.exception;

public class WrongAmountException extends RuntimeException {

    public WrongAmountException(String value) {
        super(String.format("Amount must be greather than zero, received %s is not.", value));
    }
}
