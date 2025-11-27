package com.bank.repository;

import com.bank.models.Transaction;

public class TransactionManager {
    private Transaction[] transactions;
    private int transactionCount;

    public TransactionManager() {
        this.transactions = new Transaction[100]; 
        this.transactionCount = 0;
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
    

}
