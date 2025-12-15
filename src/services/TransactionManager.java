package services;

import models.Transaction;
import utils.FileOperations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransactionManager {
//    private Transaction[] transactions;
    private HashMap<String, List<Transaction>> transactionsMap = new HashMap<>();
    private int transactionCount;
    private FileOperations fileOps = new FileOperations();

    public TransactionManager() {
        this.transactionsMap = new HashMap<>(); 
        this.transactionCount = 0;
    }

    public String generateTransactionId() {
        return "TXN" + String.format("%03d", transactionCount + 1);
    }

    public void addTransaction(Transaction transaction) {
        addTransaction(transaction, true);
    }
    
    /**
     * Add transaction with optional file write
     * @param transaction Transaction to add
     * @param writeToFile Whether to write to file (false when loading from file)
     */
    public void addTransaction(Transaction transaction, boolean writeToFile) {
        String accountNumber = transaction.getAccount().getAccountNumber();

        List<Transaction> accountTransactions = transactionsMap.getOrDefault(accountNumber, new ArrayList<>());
        accountTransactions.add(transaction);
        transactionsMap.put(accountNumber, accountTransactions);
        transactionCount++;
        if (writeToFile) {
            fileOps.writeTransactionToFile(transaction);
        }
    }

//    public void resizeArray() {
//        Transaction[] newTransactions = new Transaction[transactions.length * 2];
//        System.arraycopy(transactions, 0, newTransactions, 0, transactions.length);
//        transactions = newTransactions;
//    }

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

        for (List<Transaction> accountTransactions : transactionsMap.values()) {
            for (Transaction t : accountTransactions) {
                System.out.printf("%-15s | %-10s | $%-9.2f | %-15s | %-15s | %-10s%n",
                        t.getTransactionId(),
                        t.getTransactionType(),
                        t.getAmount(),
                        t.getAccount().getAccountNumber(),
                        t.getDate(),
                            t.status);
                }
            }
        }
    
        public void viewTransactionsByAccount(String accountNumber) {
            List<Transaction> accountTransactions = transactionsMap.get(accountNumber);

            if (accountTransactions == null || accountTransactions.isEmpty()) {
                System.out.println("No transactions found for this account.");
                return;
            }

            System.out.println("\nTRANSACTION HISTORY FOR ACCOUNT: " + accountNumber);
            System.out.println("─".repeat(100));
            System.out.printf("%-15s | %-10s | %-10s | %-15s | %-10s%n",
                    "TRANSACTION ID", "TYPE", "AMOUNT", "DATE", "STATUS");
            System.out.println("─".repeat(100));

            for (Transaction t : accountTransactions) {
                System.out.printf("%-15s | %-10s | $%-9.2f | %-15s | %-10s%n",
                        t.getTransactionId(),
                        t.getTransactionType(),
                        t.getAmount(),
                        t.getDate(),
                        t.status);
            }
            System.out.println("─".repeat(100));
        }


    public List<Transaction> getTransactionsByAccount(String accountNumber) {
        return transactionsMap.getOrDefault(accountNumber, new ArrayList<>());
    }
}

    

