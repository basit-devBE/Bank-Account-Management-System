package utils;

import models.Account;
import models.enums.TransactionType;
import models.exceptions.InsufficientfundsException;
import models.exceptions.InvalidAmountException;
import models.exceptions.OverdraftExceededException;
import services.AccountManager;
import services.TransactionManager;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Simulates concurrent transactions to test thread safety
 * Implements Runnable for multi-threaded execution
 */
public class ConcurrentTransactions implements Runnable {
    private final Account account;
    private final TransactionType transactionType;
    private final double amount;
    private final String threadName;
    private final CountDownLatch latch;
    private boolean success = false;
    private String errorMessage = null;

    public ConcurrentTransactions(Account account, TransactionType transactionType, double amount, 
                                   String threadName, CountDownLatch latch) {
        this.account = account;
        this.transactionType = transactionType;
        this.amount = amount;
        this.threadName = threadName;
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            System.out.println("[" + threadName + "] Starting " + transactionType + " of $" + 
                             String.format("%.2f", amount) + " on " + account.getAccountNumber());
            
            Thread.sleep(new Random().nextInt(100));
            
            if (transactionType == TransactionType.DEPOSIT) {
                account.deposit(amount);
                success = true;
                System.out.println("[" + threadName + "] ✓ Deposit successful. New balance: $" + 
                                 String.format("%.2f", account.getBalance()));
            } else if (transactionType == TransactionType.WITHDRAW) {
                account.withdraw(amount);
                success = true;
                System.out.println("[" + threadName + "] ✓ Withdrawal successful. New balance: $" + 
                                 String.format("%.2f", account.getBalance()));
            }
            
        } catch (InsufficientfundsException | InvalidAmountException | OverdraftExceededException e) {
            success = false;
            errorMessage = e.getMessage();
            System.out.println("[" + threadName + "] ✗ Transaction failed: " + e.getMessage());
        } catch (InterruptedException e) {
            success = false;
            errorMessage = "Thread interrupted";
            System.out.println("[" + threadName + "] ✗ Thread interrupted");
            Thread.currentThread().interrupt();
        } finally {
            latch.countDown();
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Run a concurrent transaction simulation with multiple threads
     */
    public static void simulateConcurrentTransactions(AccountManager accountManager, 
                                                      TransactionManager transactionManager) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("  CONCURRENT TRANSACTION SIMULATION");
        System.out.println("=".repeat(80));
        
        // Find an account with sufficient balance for testing
        Account testAccount = null;
        for (int i = 0; i < accountManager.getAccountCount(); i++) {
            Account acc = accountManager.getAccountByIndex(i);
            if (acc != null && acc.getBalance() >= 1000) {
                testAccount = acc;
                break;
            }
        }
        
        if (testAccount == null) {
            System.out.println("⚠ No suitable account found for testing. Need account with balance >= $1000");
            return;
        }
        
        System.out.println("\nTest Account: " + testAccount.getAccountNumber());
        System.out.println("Initial Balance: $" + String.format("%.2f", testAccount.getBalance()));
        System.out.println("\nLaunching 5 concurrent threads (3 deposits + 2 withdrawals)...\n");
        
        CountDownLatch latch = new CountDownLatch(5);
        
        // Create multiple threads performing concurrent operations
        Thread t1 = new Thread(new ConcurrentTransactions(testAccount, TransactionType.DEPOSIT, 100.0, 
                                                          "Thread-1", latch));
        Thread t2 = new Thread(new ConcurrentTransactions(testAccount, TransactionType.WITHDRAW, 50.0, 
                                                          "Thread-2", latch));
        Thread t3 = new Thread(new ConcurrentTransactions(testAccount, TransactionType.DEPOSIT, 200.0, 
                                                          "Thread-3", latch));
        Thread t4 = new Thread(new ConcurrentTransactions(testAccount, TransactionType.WITHDRAW, 75.0, 
                                                          "Thread-4", latch));
        Thread t5 = new Thread(new ConcurrentTransactions(testAccount, TransactionType.DEPOSIT, 150.0, 
                                                          "Thread-5", latch));
        
        // Start all threads simultaneously
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        
        try {
            // Wait for all threads to complete
            latch.await();
            
            System.out.println("\n" + "─".repeat(80));
            System.out.println("All threads completed!");
            System.out.println("Final Balance: $" + String.format("%.2f", testAccount.getBalance()));
            System.out.println("Expected Change: +$325.00 (deposits: $450, withdrawals: $125)");
            System.out.println("=".repeat(80));
            
        } catch (InterruptedException e) {
            System.out.println("✗ Simulation interrupted");
            Thread.currentThread().interrupt();
        }
    }
}
