package controllers;
import models.Account;
import models.Transaction;
import models.enums.TransactionType;
import models.exceptions.InsufficientfundsException;
import models.exceptions.InvalidAmountException;
import models.exceptions.OverdraftExceededException;
import services.AccountManager;
import services.TransactionManager;

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
       String accountNumber = utils.ValidationUtils.getAccountNumberInput(scanner, "Enter Account Number: ");
       
       Account account = accountManager.findAccount(accountNumber);
       if(account == null){
           System.out.println("✗ Account not found!");
           return;
       }
       System.out.println("Account Details: " + account.getAccountSummary());
              
       System.out.println("\nTransaction Type:");
       System.out.println("1. Deposit");
       System.out.println("2. Withdraw");
       System.out.println("3. Transfer");
       int typeInput = utils.ValidationUtils.getIntInput(scanner, "Select type (1-3): ", 1, 3);
       
       double amount = utils.ValidationUtils.getDoubleInput(scanner, "Enter amount: $", 0.01);
       
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

            boolean confirm = utils.ValidationUtils.getConfirmation(scanner, "Confirm Transaction?");
            
            if(confirm){
                try {
                    account.deposit(amount);
                    double balanceAfter = account.getBalance();
                    Transaction transaction = new Transaction(account, amount, transactionType, transactionId, date, balanceAfter);
                    transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
                    transactionManager.addTransaction(transaction);
                    System.out.println("✓ Transaction Completed Successfully");
                    System.out.println("  New Balance: $" + String.format("%.2f", balanceAfter));
                } catch (InvalidAmountException e) {
                    System.out.println("✗ Deposit failed: " + e.getMessage());
                    Transaction transaction = new Transaction(account, amount, transactionType, transactionId, date, currentBalance);
                    transaction.setStatus(Transaction.TransactionStatus.FAILED);
                    transactionManager.addTransaction(transaction);
                }
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
            TransactionType transactionType = TransactionType.WITHDRAW;
            
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

            boolean confirm = utils.ValidationUtils.getConfirmation(scanner, "Confirm Transaction?");
            
            if(confirm){
                try {
                    account.withdraw(amount);
                    double balanceAfter = account.getBalance();
                    Transaction transaction = new Transaction(account, amount, transactionType, transactionId, date, balanceAfter);
                    transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
                    transactionManager.addTransaction(transaction);
                    System.out.println("✓ Withdrawal Completed Successfully");
                    System.out.println("  New Balance: $" + String.format("%.2f", balanceAfter));
                } catch (InsufficientfundsException | InvalidAmountException | OverdraftExceededException e) {
                    System.out.println("✗ Withdrawal failed: " + e.getMessage());
                    Transaction transaction = new Transaction(account, amount, transactionType, transactionId, date, currentBalance);
                    transaction.setStatus(Transaction.TransactionStatus.FAILED);
                    transactionManager.addTransaction(transaction);
                }
            } else {
                Transaction transaction = new Transaction(account, amount, transactionType, transactionId, date, currentBalance);
                transaction.setStatus(Transaction.TransactionStatus.FAILED);
                transactionManager.addTransaction(transaction);
                System.out.println("✗ Transaction cancelled.");
            }
            break;
        }
        case 3:{
            // Transfer Money
            String toAccountNumber = utils.ValidationUtils.getAccountNumberInput(scanner, "Enter recipient account number: ");
            
            Account toAccount = accountManager.findAccount(toAccountNumber);
            if(toAccount == null){
                System.out.println("✗ Recipient account not found!");
                return;
            }
            
            if(account.getAccountNumber().equals(toAccountNumber)){
                System.out.println("✗ Cannot transfer to the same account!");
                return;
            }
            
            double fromCurrentBalance = account.getBalance();
            
            System.out.println("\nTRANSFER CONFIRMATION");
            System.out.println("─".repeat(50));
            System.out.println("  Transaction ID: " + transactionId);
            System.out.println("  From Account: " + account.getAccountNumber() + " (" + account.getAccountHolder().getName() + ")");
            System.out.println("  To Account: " + toAccount.getAccountNumber() + " (" + toAccount.getAccountHolder().getName() + ")");
            System.out.println("  Transfer Amount: $" + String.format("%.2f", amount));
            System.out.println("  Your Current Balance: $" + String.format("%.2f", fromCurrentBalance));
            System.out.println("  Your New Balance: $" + String.format("%.2f", fromCurrentBalance - amount));
            System.out.println("  Date: " + date);
            System.out.println("─".repeat(50));
            
            boolean confirm = utils.ValidationUtils.getConfirmation(scanner, "Confirm Transfer?");
            
            if(confirm){
                try {
                    // Withdraw from sender
                    account.withdraw(amount);
                    double fromBalanceAfter = account.getBalance();
                    
                    // Deposit to recipient
                    toAccount.deposit(amount);
                    double toBalanceAfter = toAccount.getBalance();
                    
                    String withdrawTxnId = transactionManager.generateTransactionId();
                    Transaction withdrawTxn = new Transaction(account, amount, TransactionType.TRANSFER, withdrawTxnId, date, fromBalanceAfter);
                    withdrawTxn.setStatus(Transaction.TransactionStatus.COMPLETED);
                    transactionManager.addTransaction(withdrawTxn);
                    
                    String depositTxnId = transactionManager.generateTransactionId();
                    Transaction depositTxn = new Transaction(toAccount, amount, TransactionType.TRANSFER, depositTxnId, date, toBalanceAfter);
                    depositTxn.setStatus(Transaction.TransactionStatus.COMPLETED);
                    transactionManager.addTransaction(depositTxn);
                    
                    System.out.println("✓ Transfer Completed Successfully");
                    System.out.println("  Amount Transferred: $" + String.format("%.2f", amount));
                    System.out.println("  Your New Balance: $" + String.format("%.2f", fromBalanceAfter));
                    
                } catch (InsufficientfundsException | InvalidAmountException | OverdraftExceededException e) {
                    System.out.println("✗ Transfer failed: " + e.getMessage());
                    Transaction transaction = new Transaction(account, amount, TransactionType.TRANSFER, transactionId, date, fromCurrentBalance);
                    transaction.setStatus(Transaction.TransactionStatus.FAILED);
                    transactionManager.addTransaction(transaction);
                }
            } else {
                Transaction transaction = new Transaction(account, amount, TransactionType.TRANSFER, transactionId, date, fromCurrentBalance);
                transaction.setStatus(Transaction.TransactionStatus.FAILED);
                transactionManager.addTransaction(transaction);
                System.out.println("✗ Transfer cancelled.");
            }
            break;
        }
        default:{
            System.out.println("✗ Unknown transaction type.");
        }

       }    }

    public void viewTransactionHistory(){
        System.out.println("\n--- Transaction History ---");
        boolean viewSingle = utils.ValidationUtils.getConfirmation(scanner, "View for Single Account?");
        
        if(viewSingle){
            String accountNumber = utils.ValidationUtils.getAccountNumberInput(scanner, "Enter Account Number: ");
            Account account = accountManager.findAccount(accountNumber);
            if(account == null){
                System.out.println("✗ Account not found!");
                return;
            }
            transactionManager.viewTransactionsByAccount(accountNumber);
        } else {
            // Only managers can view all transactions
            String managerId = utils.ValidationUtils.getManagerIdInput(scanner, "Enter your Manager ID: ");
            System.out.println("✓ Manager verified");
            transactionManager.viewAllTransactions();
        }
    }

}
