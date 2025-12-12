package services;

import models.Account;
import models.Transaction;

public class TransactionManager {
    private Transaction[] transactions;
    private int transactionCount;

    public TransactionManager() {
        this.transactions = new Transaction[100]; 
        this.transactionCount = 0;
    }

    public String generateTransactionId() {
        return "TXN" + String.format("%03d", transactionCount + 1);
    }

    public void addTransaction(Transaction transaction) {
        if (transactionCount >= transactions.length) {
            resizeArray();
        }
        transactions[transactionCount++] = transaction;
    }

    public void resizeArray() {
        Transaction[] newTransactions = new Transaction[transactions.length * 2];
        System.arraycopy(transactions, 0, newTransactions, 0, transactions.length);
        transactions = newTransactions;
    }

    public void viewAllTransactions() {
        if (transactionCount == 0) {
            System.out.println("No transactions found.");
            return;
        }

        System.out.println("\nTRANSACTION HISTORY");
        System.out.println("─".repeat(100));
        System.out.printf("%-15s | %-10s | %-10s | %-15s | %-15s | %-10s%n",
                "TRANSACTION ID", "TYPE", "AMOUNT", "ACCOUNT NO", "DATE", "STATUS");
        System.out.println("─".repeat(100));

        for (int i = 0; i < transactionCount; i++) {
            Transaction t = transactions[i];
            System.out.printf("%-15s | %-10s | $%-9.2f | %-15s | %-15s | %-10s%n",
                    t.getTransactionId(),
                    t.getTransactionType(),
                    t.getAmount(),
                    t.getAccount().getAccountNumber(),
                    t.getDate(),
                    t.status);
            System.out.println("─".repeat(100));
        }

        System.out.println("\nTotal Transactions: " + transactionCount);
    }

    public void viewTransactionsByAccount(String accountNumber) {
        boolean found = false;
        
        System.out.println("\nTRANSACTION HISTORY FOR ACCOUNT: " + accountNumber);
        System.out.println("─".repeat(100));
        System.out.printf("%-15s | %-10s | %-10s | %-15s | %-10s%n",
                "TRANSACTION ID", "TYPE", "AMOUNT", "DATE", "STATUS");
        System.out.println("─".repeat(100));

        for (int i = 0; i < transactionCount; i++) {
            Transaction t = transactions[i];
            if (t.getAccount().getAccountNumber().equals(accountNumber)) {
                found = true;
                System.out.printf("%-15s | %-10s | $%-9.2f | %-15s | %-10s%n",
                        t.getTransactionId(),
                        t.getTransactionType(),
                        t.getAmount(),
                        t.getDate(),
                        t.status);
                System.out.println("─".repeat(100));
            }
        }

        if (!found) {
            System.out.println("No transactions found for this account.");
        }
    }

    public Transaction[] getTransactionsByAccount(String accountNumber) {
        // First count matching transactions
        int count = 0;
        for (int i = 0; i < transactionCount; i++) {
            if (transactions[i].getAccount().getAccountNumber().equals(accountNumber)) {
                count++;
            }
        }
        
        // Create array of exact size
        Transaction[] accountTransactions = new Transaction[count];
        int index = 0;
        for (int i = 0; i < transactionCount; i++) {
            if (transactions[i].getAccount().getAccountNumber().equals(accountNumber)) {
                accountTransactions[index++] = transactions[i];
            }
        }
        
        return accountTransactions;
    }

    
}
