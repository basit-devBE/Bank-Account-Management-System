package services;

import models.Account;
import models.exceptions.InsufficientfundsException;
import models.exceptions.InvalidAmountException;
import models.exceptions.OverdraftExceededException;
import utils.FileOperations;

import java.util.*;
import java.util.stream.Collectors;

public class AccountManager {
    private final Map<String, Account> accountsMap;
//    private Map<String, Account> accountMap = new
    //Use the map interface instead
    private static int accountCount;
    private final FileOperations fileOps = new FileOperations();
    
    public AccountManager() {
        this.accountsMap = new HashMap<>();
    }
    
    public String generateAccountNumber() {
        return "ACC" + String.format("%03d", accountCount + 1);
    }
    
    public void addAccount(Account account) {
        addAccount(account, true);
    }
    
    /**
     * Add account with optional file write
     * @param account Account to add
     * @param writeToFile Whether to write to file (false when loading from file)
     */
    public void addAccount(Account account, boolean writeToFile) {
        accountsMap.put(account.getAccountNumber(), account);
        accountCount++;
        if (writeToFile) {
            fileOps.writeAccountToFile(account);
        }
    }
    
    public Account findAccount(String accountNumber) {
        return accountsMap.get(accountNumber);
    }
    
    public int getAccountCount() {
        return accountCount;
    }
    
    public Account getAccountByIndex(int index) {
        List<Account> accountsList = new ArrayList<>(accountsMap.values());
        if (index >= 0 && index < accountsList.size()) {
            return accountsList.get(index);
        }
        return null;
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

    public List<Account> filterAccountsByType(String accountType) {
        return accountsMap.values().stream()
                .filter(account -> account.getAccountType().equalsIgnoreCase(accountType))
                .sorted(Comparator.comparing(Account::getAccountNumber))
                .collect(Collectors.toList());
    }

    public List<Account> filterAccountsByBalanceRange(double minBalance, double maxBalance) {
        return accountsMap.values().stream()
                .filter(account -> account.getBalance() >= minBalance && account.getBalance() <= maxBalance)
                .sorted(Comparator.comparing(Account::getBalance).reversed())
                .collect(Collectors.toList());
    }

    public List<Account> filterAccountsByCustomerType(String customerType) {
        return accountsMap.values().stream()
                .filter(account -> account.getCustomer().getCustomerType().toString().equalsIgnoreCase(customerType))
                .sorted(Comparator.comparing(Account::getAccountNumber))
                .collect(Collectors.toList());
    }

    public double getAverageBalance() {
        return accountsMap.values().stream()
                .mapToDouble(Account::getBalance)
                .average()
                .orElse(0.0);
    }

    public long countAccountsByType(String accountType) {
        return accountsMap.values().stream()
                .filter(account -> account.getAccountType().equalsIgnoreCase(accountType))
                .count();
    }

    public Account getAccountWithHighestBalance() {
        return accountsMap.values().stream()
                .max(Comparator.comparing(Account::getBalance))
                .orElse(null);
    }

    public Account getAccountWithLowestBalance() {
        return accountsMap.values().stream()
                .min(Comparator.comparing(Account::getBalance))
                .orElse(null);
    }

    public List<Account> getAllAccounts() {
        return new ArrayList<>(accountsMap.values());
    }
}
