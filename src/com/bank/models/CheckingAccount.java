package com.bank.models;

import com.bank.models.enums.AccountType;
import com.bank.models.enums.CustomerType;

public class CheckingAccount extends Account{
    private static final double OVERDRAFT_LIMIT = 1000.0;
    private static final double MONTHLY_FEE = 10.0;

    public CheckingAccount(String accountNumber, Customer accountHolder, double initialDeposit) {
        super(accountNumber, AccountType.CHECKING, accountHolder, initialDeposit);
    }

    @Override
    public String getCreationMessage() {
        return super.getCreationMessage() +
               "\n   Overdraft Limit: $" + String.format("%.2f", OVERDRAFT_LIMIT) +
               "\n   Monthly Fee: $" + String.format("%.2f", MONTHLY_FEE);
    }

    @Override
    public void withdraw(double amount) {
        double currentBalance = checkBalance();
        
        // Allow withdrawal up to overdraft limit
        if (amount > 0 && (currentBalance - amount) >= -OVERDRAFT_LIMIT) {
            super.withdraw(amount);
            if (currentBalance - amount < 0) {
                System.out.println("⚠ Account is now overdrawn. Overdraft used: $" + 
                    String.format("%.2f", Math.abs(currentBalance - amount)));
            }
        } else if (amount > 0) {
            System.out.println("✗ Cannot withdraw: Would exceed overdraft limit of $" + 
                String.format("%.2f", OVERDRAFT_LIMIT));
        } else {
            System.out.println("Invalid withdrawal amount.");
        }
    }
    
    // Method to charge monthly fee
    public void chargeMonthlyFee() {
        // Premium customers have fees waived
        if (getAccountHolder().getCustomerType() == CustomerType.PREMIUM) {
            System.out.println("✓ Monthly fee waived for Premium customer");
            return;
        }
        
        super.withdraw(MONTHLY_FEE);
        System.out.println("✓ Monthly fee charged: $" + String.format("%.2f", MONTHLY_FEE));
    }
}
