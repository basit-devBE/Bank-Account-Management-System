package com.bank.models;

import com.bank.models.enums.AccountType;
import com.bank.models.enums.CustomerType;

public class SavingsAccount extends Account{
    private double interestRate;
    private double minimumBalance;

    public SavingsAccount(String accountNumber, Customer accountHolder, double initialDeposit) {
        super(accountNumber, AccountType.SAVINGS, accountHolder, initialDeposit);
        this.interestRate = 0.035; // 3.5%
        this.minimumBalance = 500.0;
    }
    
    public double getInterestRate() {
        return interestRate;
    }
    
    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
    
    public double getMinimumBalance() {
        return minimumBalance;
    }
    
    public void setMinimumBalance(double minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    @Override
    public String getAccountType() {
        return "Savings";
    }
    
    @Override
    public void displayAccountDetails() {
        System.out.println(getAccountDetails());
    }

    @Override
    public String getCreationMessage(){
        return super.getCreationMessage() + 
                "\n   Interest Rate: " + String.format("%.1f%%", interestRate * 100) +
                "\n   Minimum Balance: $" + String.format("%.2f", minimumBalance);
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
        
        if (getAccountHolder().getCustomerType() == CustomerType.PREMIUM) {
            if (amount > 0 && amount <= currentBalance) {
                setBalance(currentBalance - amount);
            } else {
                System.out.println("Insufficient balance or invalid amount.");
            }
        } else {
            if (amount > 0 && (currentBalance - amount) >= minimumBalance) {
                setBalance(currentBalance - amount);
            } else if (amount > 0 && (currentBalance - amount) < minimumBalance) {
                System.out.println("✗ Cannot withdraw: Would fall below minimum balance of $" + String.format("%.2f", minimumBalance));
            } else {
                System.out.println("Invalid withdrawal amount.");
            }
        }
    }
    
    public double calculateInterest() {
        return getBalance() * (interestRate / 12);
    }
    
    public void applyMonthlyInterest() {
        double monthlyInterest = calculateInterest();
        deposit(monthlyInterest);
        System.out.println("✓ Monthly interest applied: $" + String.format("%.2f", monthlyInterest));
    }

    @Override
    public String getAccountDetails(){
        String minBalanceInfo = (getAccountHolder().getCustomerType() == CustomerType.PREMIUM) 
            ? "None (Premium Customer)" 
            : "$" + String.format("%,.2f", minimumBalance);
        return super.getAccountDetails() +
               "\n  Interest Rate: " + String.format("%.1f%%", interestRate * 100) + " annual" +
               "\n  Minimum Balance: " + minBalanceInfo;
    }
}
