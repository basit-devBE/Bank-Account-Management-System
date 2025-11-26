package com.bank.interfaces;

public interface AccountOperations {
    void deposit(double amount);
    void withdraw(double amount);
    double checkBalance();
}
