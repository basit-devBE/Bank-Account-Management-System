package com.bank.models;

import com.bank.models.enums.AccountType;
import com.bank.models.enums.CustomerType;

public class CheckingAccount extends Account{
    private double overdraftLimit;
    private double monthlyFee;

    public CheckingAccount(String accountNumber, Customer accountHolder, double initialDeposit) {
        super(accountNumber, AccountType.CHECKING, accountHolder, initialDeposit);
        this.overdraftLimit = 1000.0;
        this.monthlyFee = 10.0;
    }
    
    public double getOverdraftLimit() {
        return overdraftLimit;
    }
    
    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }
    
    public double getMonthlyFee() {
        return monthlyFee;
    }
    
    public void setMonthlyFee(double monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    @Override
    public String getAccountType() {
        return "Checking";
    }
    
    @Override
    public void displayAccountDetails() {
        System.out.println(getAccountDetails());
    }

    @Override
    public String getCreationMessage() {
        return super.getCreationMessage() +
               "\n   Overdraft Limit: $" + String.format("%.2f", overdraftLimit) +
               "\n   Monthly Fee: $" + String.format("%.2f", monthlyFee);
    }

    @Override
    public void deposit(double amount) {
        if (amount > 0) {
            setBalance(getBalance() + amount);
        } else {
            System.out.println("Deposit amount must be positive.");
        }
    }

    @Override
    public void withdraw(double amount) {
        double currentBalance = getBalance();
        
        // Allow withdrawal up to overdraft limit
        if (amount > 0 && (currentBalance - amount) >= -overdraftLimit) {
            setBalance(currentBalance - amount);
            if (currentBalance - amount < 0) {
                System.out.println("⚠ Account is now overdrawn. Overdraft used: $" + 
                    String.format("%.2f", Math.abs(currentBalance - amount)));
            }
        } else if (amount > 0) {
            System.out.println("✗ Cannot withdraw: Would exceed overdraft limit of $" + 
                String.format("%.2f", overdraftLimit));
        } else {
            System.out.println("Invalid withdrawal amount.");
        }
    }
    
    public void applyMonthlyFee() {
        // Premium customers have fees waived
        if (getAccountHolder().getCustomerType() == CustomerType.PREMIUM) {
            System.out.println("✓ Monthly fee waived for Premium customer");
            return;
        }
        
        setBalance(getBalance() - monthlyFee);
        System.out.println("✓ Monthly fee charged: $" + String.format("%.2f", monthlyFee));
    }

    @Override
    public String getAccountDetails(){
        String feeInfo = (getAccountHolder().getCustomerType() == CustomerType.PREMIUM) 
            ? "Waived (Premium Customer)" 
            : "$" + String.format("%.2f", monthlyFee);
        return super.getAccountDetails() +
               "\n  Overdraft Limit: $" + String.format("%,.2f", overdraftLimit) +
               "\n  Monthly Fee: " + feeInfo;
    }
}
