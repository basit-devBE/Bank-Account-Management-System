package models;

import models.enums.AccountType;
import models.enums.CustomerType;
import models.exceptions.InsufficientfundsException;
import models.exceptions.InvalidAmountException;
import models.exceptions.OverdraftExceededException;

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
    public void deposit(double amount) throws InvalidAmountException {
        if (amount > 0) {
            setBalance(getBalance() + amount);
        } else {
           throw new InvalidAmountException("Deposit amount must be positive.");
        }
    }

    @Override
    public void withdraw(double amount) throws InvalidAmountException, InsufficientfundsException, OverdraftExceededException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive.");
        }
        
        double currentBalance = getBalance();
        double newBalance = currentBalance - amount;
        
        // Check if withdrawal would exceed overdraft limit
        if (newBalance < -overdraftLimit) {
            throw new OverdraftExceededException(
                "Withdrawal of $" + String.format("%.2f", amount) + 
                " would exceed overdraft limit of $" + String.format("%.2f", overdraftLimit)
            );
        }
        
        setBalance(newBalance);
        
        // Notify if account is now overdrawn
        if (newBalance < 0) {
            System.out.println("⚠ Account is now overdrawn. Overdraft used: $" + 
                String.format("%.2f", Math.abs(newBalance)));
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
