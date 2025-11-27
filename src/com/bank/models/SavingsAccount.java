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

    @Override
    public String getAccountSummary(){
        String baseInfo = super.getAccountSummary();
        String additionalInfo = "\n" + String.format("%-10s   %-20s   %-10s",
                "",
                "Interest Rate: " + String.format("%.1f%%", interestRate * 100),
                "Min Balance: $" + String.format("%.2f", minimumBalance));
        return baseInfo + additionalInfo;
    }
}
