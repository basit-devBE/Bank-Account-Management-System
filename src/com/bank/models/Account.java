package com.bank.models;

import com.bank.interfaces.AccountOperations;
import com.bank.models.enums.AccountType;


public abstract class Account implements AccountOperations {
    private String accountNumber;
    private AccountType accountType;
    private Customer accountHolder;
    private double balance;
    public String currency = "USD";

    public Account(String accountNumber, AccountType accountType,Customer accountHolder, double initialDeposit) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = initialDeposit;
        this.accountHolder = accountHolder;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", accountType=" + accountType +
                ", balance=" + balance +
                ", currency='" + currency + '\'' +
                '}';
    }
    @Override
    public void deposit(double amount){
        if(amount > 0){
            balance += amount;
        } else {
            System.out.println("Deposit amount must be positive.");
        }
    }

    @Override
    public void withdraw(double amount){
        if(amount > 0 && amount <= balance){
            balance -= amount;
        } else {
            System.out.println("Insufficient balance or invalid amount.");
        }
    }

    @Override
    public double checkBalance(){
        return balance;
    }
}


