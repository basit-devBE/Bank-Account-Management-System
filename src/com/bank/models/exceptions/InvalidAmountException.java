package com.bank.models.exceptions;

public class InvalidAmountException extends Exception{
    public InvalidAmountException(String message) {
        super(message);
    }
}