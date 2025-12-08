package services;

import models.Account;
import models.Transaction;
import java.time.LocalDate;

public class StatementGenerator {
    private TransactionManager transactionManager;

    public StatementGenerator(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
    
    public void generateStatement(Account account) {
        System.out.println("\nGENERATE ACCOUNT STATEMENT");
        System.out.println("─".repeat(50));
        System.out.println();
        System.out.println("Enter Account Number: " + account.getAccountNumber());
        System.out.println();
        
        // Account details
        String accountTypeStr = account.getAccountType();
        System.out.println("Account: " + account.getCustomer().getName() + " (" + accountTypeStr + ")");
        System.out.println("Current Balance: $" + String.format("%,d", (int)account.getBalance()) + "." + 
                          String.format("%02d", (int)((account.getBalance() - (int)account.getBalance()) * 100)));
        System.out.println();
        
        // Transactions section
        System.out.println("Transactions:");
        System.out.println("─".repeat(50));
        
        Transaction[] transactions = transactionManager.getTransactionsByAccount(account.getAccountNumber());
        
        if (transactions == null || transactions.length == 0) {
            System.out.println("No transactions found.");
            System.out.println();
        } else {
            double initialBalance = account.getBalance();
            double totalChange = 0.0;
            
            // Calculate initial balance by reversing transactions
            for (int i = transactions.length - 1; i >= 0; i--) {
                Transaction t = transactions[i];
                if (t != null && t.status == Transaction.TransactionStatus.COMPLETED) {
                    switch (t.getTransactionType()) {
                        case DEPOSIT:
                            initialBalance -= t.getAmount();
                            break;
                        case WITHDRAW:
                            initialBalance += t.getAmount();
                            break;
                    }
                }
            }
            
            double runningBalance = initialBalance;
            
            for (Transaction t : transactions) {
                if (t != null && t.status == Transaction.TransactionStatus.COMPLETED) {
                    String type = t.getTransactionType().toString();
                    double amount = t.getAmount();
                    
                    // Update running balance
                    if (t.getTransactionType().toString().equals("DEPOSIT")) {
                        runningBalance += amount;
                        totalChange += amount;
                        System.out.printf("%-8s | %-12s | +$%,d.%02d | $%,d.%02d%n",
                            t.getTransactionId(),
                            type,
                            (int)amount,
                            (int)((amount - (int)amount) * 100),
                            (int)runningBalance,
                            (int)((runningBalance - (int)runningBalance) * 100));
                    } else {
                        runningBalance -= amount;
                        totalChange -= amount;
                        System.out.printf("%-8s | %-12s | -$%,d.%02d | $%,d.%02d%n",
                            t.getTransactionId(),
                            type,
                            (int)amount,
                            (int)((amount - (int)amount) * 100),
                            (int)runningBalance,
                            (int)((runningBalance - (int)runningBalance) * 100));
                    }
                }
            }
            
            System.out.println("─".repeat(50));
            System.out.println();
            
            // Net change
            String sign = totalChange >= 0 ? "+" : "";
            System.out.println("Net Change: " + sign + "$" + String.format("%,d", (int)Math.abs(totalChange)) + "." + 
                              String.format("%02d", (int)((Math.abs(totalChange) - (int)Math.abs(totalChange)) * 100)));
            System.out.println();
        }
        
        System.out.println("✓ Statement generated successfully.");
    }
    
    public void generateMiniStatement(Account account) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("           MINI STATEMENT");
        System.out.println("=".repeat(60));
        System.out.println(account.getAccountSummary());
        System.out.println("=".repeat(60));
    }
}
