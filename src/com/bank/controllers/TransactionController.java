package com.bank.controllers;
import com.bank.models.Account;
import com.bank.models.Transaction;
import com.bank.models.enums.TransactionType;
import com.bank.repository.AccountManager;
import com.bank.repository.TransactionManager;

import java.time.LocalDate;
import java.util.Scanner;

public class TransactionController {
    private TransactionManager transactionManager = new TransactionManager();
    private AccountManager accountManager;
    private Scanner scanner = new Scanner(System.in);

    public TransactionController(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public void recordTransaction(){
       System.out.println("\n--- Record Transaction ---");
       System.out.print("Enter Account Number: ");
       String accountNumber = scanner.nextLine().trim();
       
       Account account = accountManager.findAccount(accountNumber);
       if(account == null){
           System.out.println("✗ Account not found!");
           return;
       }
       System.out.println("Account Details: " + account.getAccountSummary());
       
       System.out.print("Enter Transaction Type (DEPOSIT/WITHDRAW): ");
       String typeInput = scanner.nextLine().trim();
       TransactionType transactionType = typeInput.equalsIgnoreCase("DEPOSIT") 
           ? TransactionType.DEPOSIT 
           : TransactionType.WITHDRAW;
       
       System.out.print("Enter amount: $");
       double amount;
       try {
           amount = Double.parseDouble(scanner.nextLine().trim());
       } catch (NumberFormatException e) {
           System.out.println("✗ Invalid amount entered.");
           return;
       }
       
       String transactionId = "TXN" + System.currentTimeMillis();
       LocalDate date = LocalDate.now();
       
       switch (transactionType){
        case DEPOSIT:{
            double currentBalance = account.checkBalance();
            
            System.out.println("TRANSACTION CONFIRMATION");
            System.out.println("─".repeat(50));
            System.out.println("  Transaction ID: " + transactionId);
            System.out.println("  Account: " + account.getAccountNumber());
            System.out.println("  Type: DEPOSIT");
            System.out.println("  Amount: $" + String.format("%.2f", amount));
            System.out.println("  Current Balance: $" + String.format("%.2f", currentBalance));
            System.out.println("  New Balance: $" + String.format("%.2f", currentBalance + amount));
            System.out.println("  Date: " + date);
            System.out.println("─".repeat(50));

            System.out.print("Confirm Transaction? (Y/N): ");
            String confirm = scanner.nextLine().trim();
            
            if(confirm.equalsIgnoreCase("Y")){
                account.deposit(amount);
                double balanceAfter = account.checkBalance();
                Transaction transaction = new Transaction(account, amount, transactionType, transactionId, date, balanceAfter);
                transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
                transactionManager.addTransaction(transaction);
                System.out.println("✓ Transaction Completed Successfully");
                System.out.println("  New Balance: $" + String.format("%.2f", balanceAfter));
            } else {
                Transaction transaction = new Transaction(account, amount, transactionType, transactionId, date, currentBalance);
                transaction.setStatus(Transaction.TransactionStatus.FAILED);
                transactionManager.addTransaction(transaction);
                System.out.println("✗ Transaction cancelled.");
            }
            break;          
        }
        case WITHDRAW:{
            double currentBalance = account.checkBalance();
            
            if (amount > currentBalance) {
                System.out.println("TRANSACTION CONFIRMATION");
                System.out.println("─".repeat(50));
                System.out.println("✗ Insufficient funds!");
                System.out.println("  Available Balance: $" + String.format("%.2f", currentBalance));
                System.out.println("  Requested Amount: $" + String.format("%.2f", amount));
                System.out.println("─".repeat(50));
                Transaction transaction = new Transaction(account, amount, transactionType, transactionId, date, currentBalance);
                transaction.setStatus(Transaction.TransactionStatus.FAILED);
                transactionManager.addTransaction(transaction);
                return;
            }
            
            System.out.println("TRANSACTION CONFIRMATION");
            System.out.println("─".repeat(50));
            System.out.println("  Transaction ID: " + transactionId);
            System.out.println("  Account: " + account.getAccountNumber());
            System.out.println("  Type: WITHDRAWAL");
            System.out.println("  Amount: $" + String.format("%.2f", amount));
            System.out.println("  Current Balance: $" + String.format("%.2f", currentBalance));
            System.out.println("  New Balance: $" + String.format("%.2f", currentBalance - amount));
            System.out.println("  Date: " + date);
            System.out.println("─".repeat(50));

            System.out.print("Confirm Transaction? (Y/N): ");
            String confirm = scanner.nextLine().trim();
            
            if(confirm.equalsIgnoreCase("Y")){
                account.withdraw(amount);
                double balanceAfter = account.checkBalance();
                Transaction transaction = new Transaction(account, amount, transactionType, transactionId, date, balanceAfter);
                transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
                transactionManager.addTransaction(transaction);
                System.out.println("✓ Withdrawal Completed Successfully");
                System.out.println("  New Balance: $" + String.format("%.2f", balanceAfter));
            } else {
                Transaction transaction = new Transaction(account, amount, transactionType, transactionId, date, currentBalance);
                transaction.setStatus(Transaction.TransactionStatus.FAILED);
                transactionManager.addTransaction(transaction);
                System.out.println("✗ Transaction cancelled.");
            }
            break;
        }
        default:{
            System.out.println("✗ Unknown transaction type.");
        }

       }

    }

    public void viewTransactionHistory(){
        System.out.println("\n--- Transaction History ---");
        System.out.print("View for Single Account? (Y/N): ");
        String input = scanner.nextLine().trim();
        if(input.equalsIgnoreCase("Y")){
            System.out.print("Enter Account Number: ");
            String accountNumber = scanner.nextLine().trim();
            Account account = accountManager.findAccount(accountNumber);
            transactionManager.viewTransactionsByAccount(accountNumber);
            if(account == null){
                System.out.println("✗ Account not found!");
                return;
        }
       }else{
           transactionManager.viewAllTransactions();
       }
    }

}
