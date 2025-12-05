package com.bank.models;

import com.bank.models.enums.AccountType;
import com.bank.models.exceptions.InsufficientfundsException;
import com.bank.models.exceptions.InvalidAmountException;
import com.bank.models.exceptions.OverdraftExceededException;

public abstract class Account {
    private String accountNumber;
    private Customer customer;
    private double balance;
    private String status;
    private static int accountCounter = 0;

    public Account(String accountNumber, AccountType accountType, Customer accountHolder, double initialDeposit) {
        this.accountNumber = accountNumber;
        this.customer = accountHolder;
        this.balance = initialDeposit;
        this.status = "Active";
        accountCounter++;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
    
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public static int getAccountCounter() {
        return accountCounter;
    }
    
    // Kept for backward compatibility
    public Customer getAccountHolder() {
        return customer;
    }

    @Override
    public String toString() {
        return "Account{" +
               "accountNumber='" + accountNumber + '\'' +
               ", customer=" + customer.getName() +
               ", balance=$" + String.format("%.2f", balance) +
               ", status='" + status + '\'' +
               '}';
    }

    public abstract void displayAccountDetails();
    public abstract String getAccountType();
    public abstract void deposit(double amount) throws InvalidAmountException;
    public abstract void withdraw(double amount) throws InsufficientfundsException, InvalidAmountException, OverdraftExceededException;

    public String getCreationMessage(){
        return "\n✓ Account created successfully!" +
               "\n   Account Number: " + accountNumber +
               "\n   Customer: " + customer.getName() + " (" + customer.getCustomerType() + ")" +
               "\n   Account Type: " + getAccountType() +
               "\n   Initial Balance: $" + String.format("%.2f", balance) +
               "\n   Status: " + status;
    }

    public String getAccountSummary(){
       return String.format("%-10s | %-20s | %-10s | $%-13s | %s",
                accountNumber.substring(0, Math.min(6, accountNumber.length())),
                customer.getName().length() > 20 ? customer.getName().substring(0, 17) + "..." : customer.getName(),
                getAccountType(),
                String.format("%,.2f", balance),
                status);
    }

    public String getAccountDetails(){
        return "  Account Number: " + accountNumber +
               "\n  Account Type: " + getAccountType() +
               "\n  Account Holder: " + customer.getName() +
               "\n  Customer Type: " + customer.getCustomerType() +
               "\n  Customer ID: " + customer.getCustomerId() +
               "\n  Current Balance: $" + String.format("%,.2f", balance) +
               "\n  Status: " + status;
    }
}
