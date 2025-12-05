package com.bank.controllers;
import com.bank.models.Account;
import com.bank.models.Transaction;
import com.bank.models.enums.TransactionType;
import com.bank.services.AccountManager;
import com.bank.services.TransactionManager;

import java.time.LocalDate;
import java.util.Scanner;

public class TransactionController {
    private TransactionManager transactionManager;
    private AccountManager accountManager;
    private Scanner scanner = new Scanner(System.in);

    public TransactionController(AccountManager accountManager, TransactionManager transactionManager) {
        this.accountManager = accountManager;
        this.transactionManager = transactionManager;
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
              
       System.out.println("\nTransaction Type:");
       System.out.println("1. Deposit");
       System.out.println("2. Withdraw");
       System.out.print("Select type (1 or 2): ");
       int typeInput = Integer.parseInt(scanner.nextLine().trim());
       
       System.out.print("Enter amount: $");
       double amount;
       try {
           amount = Double.parseDouble(scanner.nextLine().trim());
       } catch (NumberFormatException e) {
           System.out.println("✗ Invalid amount entered.");
           return;
       }
       
       String transactionId = transactionManager.generateTransactionId();
       LocalDate date = LocalDate.now();
       
       switch (typeInput){
        case 1:{
            double currentBalance = account.getBalance();
            
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
            TransactionType transactionType = TransactionType.DEPOSIT;


            System.out.print("Confirm Transaction? (Y/N): ");
            String confirm = scanner.nextLine().trim();
            
            if(confirm.equalsIgnoreCase("Y")){
                account.deposit(amount);
                double balanceAfter = account.getBalance();
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
        case 2:{
            double currentBalance = account.getBalance();
            TransactionType transactionType = TransactionType.DEPOSIT;
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
                double balanceAfter = account.getBalance();
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
            if(account == null){
                System.out.println("✗ Account not found!");
                return;
            }
            transactionManager.viewTransactionsByAccount(accountNumber);
        } else {
            // Only managers can view all transactions
            System.out.print("Enter your Manager ID: ");
            String managerId = scanner.nextLine().trim();
            
            // Simple validation - check if ID starts with MGR
            if (!managerId.startsWith("MGR")) {
                System.out.println("✗ Access Denied: Invalid Manager ID.");
                System.out.println("Only registered managers can view all transaction history.");
                System.out.println("Please select 'Y' to view your own account transactions.");
                return;
            }
            
            System.out.println("✓ Manager verified");
            transactionManager.viewAllTransactions();
        }
    }

}
