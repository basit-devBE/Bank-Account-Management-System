package com.bank.repository;

import com.bank.models.Account;
import com.bank.models.Transaction;

public class AccountManager {
    private Account[] accounts;
    private int accountCount;
    
    public AccountManager() {
        this.accounts = new Account[50]; 
        this.accountCount = 0;
    }
    
    public void addAccount(Account account) {
        if (accountCount >= accounts.length) {
            resizeArray();
        }
        accounts[accountCount++] = account;
    }
    
    public Account findAccount(String accountNumber) {
        for (int i = 0; i < accountCount; i++) {
            if (accounts[i].getAccountNumber().equals(accountNumber)) {
                return accounts[i];
            }
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
        
        for (int i = 0; i < accountCount; i++) {
            System.out.println(accounts[i].getAccountSummary());
            System.out.println("─".repeat(85));
        }
        
        System.out.println("\nTotal Accounts: " + accountCount);
        System.out.println("Total Bank Balance: $" + String.format("%,.2f", getTotalBalance()));
    }
    
    public double getTotalBalance() {
        double total = 0;
        for (int i = 0; i < accountCount; i++) {
            total += accounts[i].checkBalance();
        }
        return total;
    }
    
    public int getAccountCount() {
        return accountCount;
    }
    
    private void resizeArray() {
        Account[] newAccounts = new Account[accounts.length * 2];
        System.arraycopy(accounts, 0, newAccounts, 0, accounts.length);
        accounts = newAccounts;
    }

    public Account getAccountDetail(String accountNumber) {
        for (int i = 0; i < accountCount; i++) {
            if (accounts[i].getAccountNumber().equals(accountNumber)) {
                return accounts[i];
            }
        }
        return null; 
    }

    public void deposit(Transaction transaction) {
        Account account = transaction.getAccount();
        double amount = transaction.getAmount();
        account.deposit(amount);
    }
}