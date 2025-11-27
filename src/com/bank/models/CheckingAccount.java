package com.bank.models;

import com.bank.models.enums.AccountType;

public class CheckingAccount extends Account{
    private double overdraftLimit = 1000.0;
    private  double monthlyFee = 10.0;

    public CheckingAccount(String accountNumber, Customer accountHolder,double initialDeposit) {
        super(accountNumber, AccountType.CHECKING,accountHolder,initialDeposit);
    }

     @Override
    public String getCreationMessage() {
        return super.getCreationMessage() +
               "\n   Overdraft Limit: $" + String.format("%.2f", overdraftLimit) +
               "\n   Monthly Fee: $" + String.format("%.2f", monthlyFee);
    }

    // @Override
    // public String getAccountSummary() {
    //     return super.getAccountSummary();
    // }

}
