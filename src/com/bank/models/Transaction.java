package com.bank.models;

import java.time.LocalDate;

import com.bank.models.enums.TransactionType;

public class Transaction {
    private Account account;
    private double amount;
    private TransactionType transactionType;
    private String transactionId;
    private LocalDate date;
    private double balanceAfter ;
    public enum TransactionStatus {
        PENDING,
        COMPLETED,
        FAILED
    }
    public TransactionStatus status;

    public Transaction(Account account, double amount, TransactionType transactionType, String transactionId, LocalDate date, double balance) {
        this.account = account;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionId = transactionId;
        this.date = date;
        this.balanceAfter = balance;
        this.status = TransactionStatus.PENDING;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public String getTransactionId() {
        return transactionId;
    }
    public Account getAccount() {
        return account;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

}
