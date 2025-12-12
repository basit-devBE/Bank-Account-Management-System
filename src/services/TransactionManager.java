package services;

import models.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransactionManager {
//    private Transaction[] transactions;
    private HashMap<String, List<Transaction>> transactionsMap = new HashMap<>();
    private int transactionCount;

    public TransactionManager() {
        this.transactionsMap = new HashMap<>(); 
        this.transactionCount = 0;
    }

    public String generateTransactionId() {
        return "TXN" + String.format("%03d", transactionCount + 1);
    }

    public void addTransaction(Transaction transaction) {
        String accountNumber = transaction.getAccount().getAccountNumber();

        List<Transaction> accountTransactions = transactionsMap.getOrDefault(accountNumber, new ArrayList<>());
        accountTransactions.add(transaction);
        transactionsMap.put(accountNumber, accountTransactions);
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
    }

    

