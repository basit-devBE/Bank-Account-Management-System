package com.bank.models;

import com.bank.models.enums.AccountType;

public class SavingsAccount extends Account{
    private double interestRate = 0.35;
    private double minimumBalance = 500.0;

    public SavingsAccount(String accountNumber, Customer accountHolder,double initialDeposit) {
        super(accountNumber, AccountType.SavingsAccount,accountHolder,initialDeposit);
    }
}
