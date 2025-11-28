package com.bank.models;

import com.bank.models.enums.AccountType;
import com.bank.models.enums.CustomerType;

public class SavingsAccount extends Account{
    private static final double INTEREST_RATE = 0.035; // 3.5% annual
    private static final double MIN_BALANCE = 500.0;

    public SavingsAccount(String accountNumber, Customer accountHolder, double initialDeposit) {
        super(accountNumber, AccountType.SAVINGS, accountHolder, initialDeposit);
    }

    @Override
    public String getCreationMessage(){
        return super.getCreationMessage() + 
                "\n   Interest Rate: " + String.format("%.1f%%", INTEREST_RATE * 100) +
                "\n   Minimum Balance: $" + String.format("%.2f", MIN_BALANCE);
    }

    @Override
    public void withdraw(double amount) {
        double currentBalance = checkBalance();
        
        if (getAccountHolder().getCustomerType() == CustomerType.PREMIUM) {
            if (amount > 0 && amount <= currentBalance) {
                super.withdraw(amount);
            } else {
                System.out.println("Insufficient balance or invalid amount.");
            }
        } else {
            if (amount > 0 && (currentBalance - amount) >= MIN_BALANCE) {
                super.withdraw(amount);
            } else if (amount > 0 && (currentBalance - amount) < MIN_BALANCE) {
                System.out.println("✗ Cannot withdraw: Would fall below minimum balance of $" + String.format("%.2f", MIN_BALANCE));
            } else {
                System.out.println("Invalid withdrawal amount.");
            }
        }
    }
    
    public void applyMonthlyInterest() {
        double monthlyInterest = checkBalance() * (INTEREST_RATE / 12);
        deposit(monthlyInterest);
        System.out.println("✓ Monthly interest applied: $" + String.format("%.2f", monthlyInterest));
    }
}
