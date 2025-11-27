package com.bank.models;

import java.time.LocalDate;

import com.bank.models.enums.TransactionType;

public class Transaction {
    private Account account;
    private double amount;
    private TransactionType transactionType;
    private String transactionId;
    private LocalDate date;
    private double balance ;

    public Transaction(Account account, double amount, TransactionType transactionType, String transactionId, LocalDate date,double balance) {
        this.account = account;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionId = transactionId;
        this.date = date;
        this.balance = balance;
    }

}
