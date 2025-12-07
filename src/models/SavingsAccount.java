package models;

import models.enums.AccountType;
import models.enums.CustomerType;
import models.exceptions.InsufficientfundsException;
import models.exceptions.InvalidAmountException;
import models.exceptions.OverdraftExceededException;

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
    public void deposit(double amount) throws InvalidAmountException {
        if (amount > 0) {
            setBalance(getBalance() + amount);
        } else {
            throw new InvalidAmountException("Deposit amount must be positive.");
        }
    }

    @Override
    public void withdraw(double amount) throws InsufficientfundsException, InvalidAmountException, OverdraftExceededException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive.");
        }
        
        double currentBalance = getBalance();

        String message = "Insufficient funds! Available balance: $" + String.format("%.2f", currentBalance) +
                ", Requested: $" + String.format("%.2f", amount);
        if (getAccountHolder().getCustomerType() == CustomerType.PREMIUM) {
            if (amount > currentBalance) {
                throw new InsufficientfundsException(
                        message
                );
            }
            setBalance(currentBalance - amount);
        } else {
            double balanceAfterWithdrawal = getBalanceAfterWithdrawal(amount, currentBalance,message);
            setBalance(balanceAfterWithdrawal);
        }
    }

    private double getBalanceAfterWithdrawal(double amount, double currentBalance, String message) throws InsufficientfundsException {
        double balanceAfterWithdrawal = currentBalance - amount;

        if (balanceAfterWithdrawal < minimumBalance) {
            if (amount > currentBalance) {
                throw new InsufficientfundsException(
                        message
                );
            } else {
                throw new InsufficientfundsException(
                    "Cannot withdraw: Would fall below minimum balance of $" + String.format("%.2f", minimumBalance) +
                    ". Maximum withdrawal allowed: $" + String.format("%.2f", currentBalance - minimumBalance)
                );
            }
        }
        return balanceAfterWithdrawal;
    }

    public double calculateInterest() {
        return getBalance() * (interestRate / 12);
    }
    
    public void applyMonthlyInterest() {
        double monthlyInterest = calculateInterest();
        try {
            deposit(monthlyInterest);
            System.out.println("✓ Monthly interest applied: $" + String.format("%.2f", monthlyInterest));
        } catch (InvalidAmountException e) {
            System.err.println("Failed to apply monthly interest: " + e.getMessage());
        }
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
