package utils;

import models.Account;
import models.enums.TransactionType;
import models.exceptions.InsufficientfundsException;
import models.exceptions.InvalidAmountException;
import models.exceptions.OverdraftExceededException;
import services.AccountManager;
import services.TransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Simulates concurrent transactions to test thread safety
 * Uses ExecutorService for better thread management
 */
public class ConcurrentTransactions implements Callable<ConcurrentTransactions.TransactionResult> {
    private final Account account;
    private final TransactionType transactionType;
    private final double amount;
    private final String threadName;

    public ConcurrentTransactions(Account account, TransactionType transactionType, double amount, 
                                   String threadName) {
        this.account = account;
        this.transactionType = transactionType;
        this.amount = amount;
        this.threadName = threadName;
    }

    @Override
    public TransactionResult call() {
        try {
            System.out.println("[" + threadName + "] Starting " + transactionType + " of $" + 
                             String.format("%.2f", amount) + " on " + account.getAccountNumber());
            
            Thread.sleep(new Random().nextInt(100));
            
            synchronized (account) {
                if (transactionType == TransactionType.DEPOSIT) {
                    account.deposit(amount);
                    System.out.println("[" + threadName + "] ✓ Deposit successful. New balance: $" + 
                                     String.format("%.2f", account.getBalance()));
                    account.notifyAll();
                    return new TransactionResult(true, null, threadName);
                } else if (transactionType == TransactionType.WITHDRAW) {
                    account.withdraw(amount);
                    System.out.println("[" + threadName + "] ✓ Withdrawal successful. New balance: $" + 
                                     String.format("%.2f", account.getBalance()));
                    account.notifyAll();
                    return new TransactionResult(true, null, threadName);
                }
            }
            
        } catch (InsufficientfundsException | InvalidAmountException | OverdraftExceededException e) {
            System.out.println("[" + threadName + "] ✗ Transaction failed: " + e.getMessage());
            return new TransactionResult(false, e.getMessage(), threadName);
        } catch (InterruptedException e) {
            System.out.println("[" + threadName + "] ✗ Thread interrupted");
            Thread.currentThread().interrupt();
            return new TransactionResult(false, "Thread interrupted", threadName);
        }
        
        return new TransactionResult(false, "Unknown transaction type", threadName);
    }

    /**
     * Result class to hold transaction execution results
     */
    public static class TransactionResult {
        private final boolean success;
        private final String errorMessage;
        private final String threadName;

        public TransactionResult(boolean success, String errorMessage, String threadName) {
            this.success = success;
            this.errorMessage = errorMessage;
            this.threadName = threadName;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public String getThreadName() {
            return threadName;
        }
    }

    /**
     * Run a concurrent transaction simulation with multiple threads using ExecutorService
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
        
        // Create a fixed thread pool with 5 threads
        ExecutorService executor = Executors.newFixedThreadPool(5);
        
        // Create list of tasks
        List<Callable<TransactionResult>> tasks = new ArrayList<>();
        tasks.add(new ConcurrentTransactions(testAccount, TransactionType.DEPOSIT, 100.0, "Thread-1"));
        tasks.add(new ConcurrentTransactions(testAccount, TransactionType.WITHDRAW, 50.0, "Thread-2"));
        tasks.add(new ConcurrentTransactions(testAccount, TransactionType.DEPOSIT, 200.0, "Thread-3"));
        tasks.add(new ConcurrentTransactions(testAccount, TransactionType.WITHDRAW, 75.0, "Thread-4"));
        tasks.add(new ConcurrentTransactions(testAccount, TransactionType.DEPOSIT, 150.0, "Thread-5"));
        
        try {
            // Execute all tasks and wait for completion
            List<Future<TransactionResult>> futures = executor.invokeAll(tasks);
            
            // Collect results
            int successCount = 0;
            int failureCount = 0;
            
            for (Future<TransactionResult> future : futures) {
                TransactionResult result = future.get();
                if (result.isSuccess()) {
                    successCount++;
                } else {
                    failureCount++;
                }
            }
            
            System.out.println("\n" + "─".repeat(80));
            System.out.println("All threads completed!");
            System.out.println("Successful transactions: " + successCount);
            System.out.println("Failed transactions: " + failureCount);
            System.out.println("Final Balance: $" + String.format("%.2f", testAccount.getBalance()));
            System.out.println("Expected Change: +$325.00 (deposits: $450, withdrawals: $125)");
            System.out.println("=".repeat(80));
            
        } catch (InterruptedException e) {
            System.out.println("✗ Simulation interrupted");
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            System.out.println("✗ Error executing transaction: " + e.getMessage());
        } finally {
            // Shutdown executor service
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}
