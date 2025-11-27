package com.bank.models;

import com.bank.models.enums.AccountType;

public class SavingsAccount extends Account{
    private double interestRate = 0.35;
    private double minimumBalance = 500.0;

    public SavingsAccount(String accountNumber, Customer accountHolder,double initialDeposit) {
        super(accountNumber, AccountType.SAVINGS,accountHolder,initialDeposit);
    }

    @Override
    public String getCreationMessage(){
        return super.getCreationMessage() + 
                "\n   Interest Rate: " + interestRate + "%" +
                "\n   Minimum Balance: $" + String.format("%.2f", minimumBalance);
    }

    // @Override
    // public String getAccountSummary(){
    //     return super.getAccountSummary();
    // }
}
