package services;

import models.Account;
import models.exceptions.InsufficientfundsException;
import models.exceptions.InvalidAmountException;
import models.exceptions.OverdraftExceededException;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class AccountManager {
    private HashMap<String, Account> accountsMap = new HashMap<>();
    private int accountCount;
    
    public AccountManager() {
        this.accountsMap = new HashMap<>();
        this.accountCount = 0;
    }
    
    public String generateAccountNumber() {
        return "ACC" + String.format("%03d", accountCount + 1);
    }
    
    public void addAccount(Account account) {
        accountsMap.put(account.getAccountNumber(), account);
        accountCount++;
    }
    
    public Account findAccount(String accountNumber) {
        return accountsMap.get(accountNumber);
    }
    
   public void viewAllAccounts() {
        if (accountCount == 0) {
            System.out.println("No accounts found.");
            return;
        }
        
        System.out.println("\nACCOUNT LISTING");
        System.out.println("─".repeat(85));
        System.out.printf("%-10s | %-20s | %-10s | %-15s | %-10s%n",
                "ACC NO", "CUSTOMER NAME", "TYPE", "BALANCE", "STATUS");
        System.out.println("─".repeat(85));
        
        accountsMap.values().stream()
                .sorted(Comparator.comparing(Account::getAccountNumber))
                .forEach(account -> {
                    System.out.println(account.getAccountSummary());
                    System.out.println("─".repeat(85));
                });
        
        System.out.println("\nTotal Accounts: " + accountCount);
        System.out.println("Total Bank Balance: $" + String.format("%,.2f", getTotalBalance()));
    }
    
    public double getTotalBalance() {
        return accountsMap.values().stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }
    
    /**
     * Transfer funds from one account to another
     * @param fromAccountNumber Source account number
     * @param toAccountNumber Destination account number
     * @param amount Amount to transfer
     * @throws InvalidAmountException If amount is invalid
     * @throws InsufficientfundsException If source account has insufficient funds
     * @throws OverdraftExceededException If withdrawal would exceed overdraft limit
     * @throws IllegalArgumentException If either account is not found or if attempting self-transfer
     */
    public void transfer(String fromAccountNumber, String toAccountNumber, double amount)
            throws InvalidAmountException, OverdraftExceededException, InsufficientfundsException {

        // Validate amount
        if (amount <= 0) {
            throw new InvalidAmountException("Transfer amount must be positive.");
        }

        // Validate accounts
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new IllegalArgumentException("Cannot transfer to the same account.");
        }

        Account fromAccount = findAccount(fromAccountNumber);
        Account toAccount = findAccount(toAccountNumber);

        if (fromAccount == null) {
            throw new IllegalArgumentException("Source account not found: " + fromAccountNumber);
        }

        if (toAccount == null) {
            throw new IllegalArgumentException("Destination account not found: " + toAccountNumber);
        }

        // Perform transfer
        fromAccount.withdraw(amount);
        toAccount.deposit(amount);
    }

    private void resizeArray() {
        Account[] newAccounts = new Account[accounts.length * 2];
        System.arraycopy(accounts, 0, newAccounts, 0, accounts.length);
        accounts = newAccounts;
    }
}
