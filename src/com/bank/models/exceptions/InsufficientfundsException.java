package com.bank.models.exceptions;

public class InsufficientfundsException extends Exception {
    public InsufficientfundsException(String message) {
        super(message);
    }
}