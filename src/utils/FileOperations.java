package utils;
import models.*;
import models.enums.TransactionType;
import services.AccountManager;
import services.TransactionManager;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FileOperations {
    private static final String TRANSACTIONS_FILE = "transactions.txt";
    private static final String ACCOUNTS_FILE = "accounts.txt";
    
    // ==================== TRANSACTION OPERATIONS ====================
    
    // Write single transaction
    public void writeTransactionToFile(Transaction content, String filePath) {
        try {
            String transactionData = String.format("TRANSACTION,%s,%s,%.2f,%s,%s,%s,%.2f%n",
                    content.getTransactionId(),
                    content.getAccount().getAccountNumber(),
                    content.getAmount(),
                    content.getTransactionType(),
                    content.getDate(),
                    content.status,
                    content.getBalanceAfter()
            );
            Files.write(Paths.get(filePath), transactionData.getBytes(), 
                       StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("✗ Error writing transaction: " + e.getMessage());
        }
    }
    
    public void writeTransactionToFile(Transaction content) {
        writeTransactionToFile(content, TRANSACTIONS_FILE);
    }
    
    // Write multiple transactions
    public void writeTransactionsToFile(List<Transaction> transactions, String filePath) {
        try {
            StringBuilder data = new StringBuilder();
            data.append("=== TRANSACTIONS ===\n");
            data.append(String.format("%-10s | %-12s | %-10s | %-15s | %-12s | %-10s%n",
                    "TXN ID", "ACCOUNT", "AMOUNT", "TYPE", "DATE", "STATUS"));
            data.append("─".repeat(80)).append("\n");
            
            for (Transaction t : transactions) {
                if (t != null) {
                    data.append(String.format("%-10s | %-12s | $%-9.2f | %-15s | %-12s | %-10s%n",
                            t.getTransactionId(),
                            t.getAccount().getAccountNumber(),
                            t.getAmount(),
                            t.getTransactionType(),
                            t.getDate(),
                            t.status));
                }
            }
            data.append("\n");
            
            Files.write(Paths.get(filePath), data.toString().getBytes(), 
                       StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            System.out.println("✓ Wrote " + transactions.size() + " transactions to " + filePath);
        } catch (IOException e) {
            System.out.println("✗ Error writing transactions: " + e.getMessage());
        }
    }
    
    public void writeTransactionsToFile(List<Transaction> transactions) {
        writeTransactionsToFile(transactions, TRANSACTIONS_FILE);
    }
    
    // ==================== ACCOUNT OPERATIONS ====================
    
    // Write single account
    public void writeAccountToFile(Account account, String filePath) {
        try {
            String accountData = String.format("ACCOUNT,%s,%s,%s,%.2f,%s,%s%n",
                    account.getAccountNumber(),
                    account.getAccountHolder().getName(),
                    account.getAccountType(),
                    account.getBalance(),
                    account.getStatus(),
                    account.getAccountHolder().getCustomerType()
            );
            Files.write(Paths.get(filePath), accountData.getBytes(), 
                       StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("✗ Error writing account: " + e.getMessage());
        }
    }
    
    public void writeAccountToFile(Account account) {
        writeAccountToFile(account, ACCOUNTS_FILE);
    }
    
    /**
     * Update account balance in the accounts file
     * Rewrites the entire file with updated account information
     */
    public void updateAccountBalance(Account account) {
        updateAccountBalance(account, ACCOUNTS_FILE);
    }
    
    public void updateAccountBalance(Account account, String filePath) {
        try {
            if (!fileExists(filePath)) {
                // If file doesn't exist, just write the account
                writeAccountToFile(account, filePath);
                return;
            }
            
            // Read all lines from file
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            List<String> updatedLines = new java.util.ArrayList<>();
            boolean accountFound = false;
            
            for (String line : lines) {
                if (line.trim().startsWith("ACCOUNT,")) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2 && parts[1].trim().equals(account.getAccountNumber())) {
                        // Update this account's line with new balance
                        String updatedLine = String.format("ACCOUNT,%s,%s,%s,%.2f,%s,%s",
                                account.getAccountNumber(),
                                account.getAccountHolder().getName(),
                                account.getAccountType(),
                                account.getBalance(),
                                account.getStatus(),
                                account.getAccountHolder().getCustomerType()
                        );
                        updatedLines.add(updatedLine);
                        accountFound = true;
                    } else {
                        updatedLines.add(line);
                    }
                } else {
                    updatedLines.add(line);
                }
            }
            
            // If account wasn't found in file, add it
            if (!accountFound) {
                String newLine = String.format("ACCOUNT,%s,%s,%s,%.2f,%s,%s",
                        account.getAccountNumber(),
                        account.getAccountHolder().getName(),
                        account.getAccountType(),
                        account.getBalance(),
                        account.getStatus(),
                        account.getAccountHolder().getCustomerType()
                );
                updatedLines.add(newLine);
            }
            
            // Write all lines back to file
            Files.write(Paths.get(filePath), updatedLines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            
        } catch (IOException e) {
            System.out.println("✗ Error updating account balance: " + e.getMessage());
        }
    }
    
    // Write multiple accounts
    public void writeAccountsToFile(Account[] accounts, String filePath) {
        try {
            StringBuilder data = new StringBuilder();
            data.append("=== ACCOUNTS ===\n");
            data.append(String.format("%-12s | %-25s | %-15s | %-15s | %-10s%n",
                    "ACCOUNT NO", "HOLDER NAME", "TYPE", "BALANCE", "STATUS"));
            data.append("─".repeat(80)).append("\n");
            
            int count = 0;
            for (Account account : accounts) {
                if (account != null) {
                    data.append(String.format("%-12s | %-25s | %-15s | $%-14.2f | %-10s%n",
                            account.getAccountNumber(),
                            account.getAccountHolder().getName(),
                            account.getAccountType(),
                            account.getBalance(),
                            account.getStatus()));
                    count++;
                }
            }
            data.append("\n");
            
            Files.write(Paths.get(filePath), data.toString().getBytes(), 
                       StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            System.out.println("✓ Wrote " + count + " accounts to " + filePath);
        } catch (IOException e) {
            System.out.println("✗ Error writing accounts: " + e.getMessage());
        }
    }
    
    public void writeAccountsToFile(Account[] accounts) {
        writeAccountsToFile(accounts, ACCOUNTS_FILE);
    }
    
    // ==================== FILE OPERATIONS ====================
    
    // Read entire file
    public String readFromFile(String filePath) {
        try {
            if (!Files.exists(Paths.get(filePath))) {
                return "File does not exist.";
            }
            return Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            return "✗ Error reading file: " + e.getMessage();
        }
    }
    
    // Read transactions file
    public String readTransactionsFile() {
        return readFromFile(TRANSACTIONS_FILE);
    }
    
    // Read accounts file
    public String readAccountsFile() {
        return readFromFile(ACCOUNTS_FILE);
    }
    
    // Clear transactions file
    public void clearTransactionsFile() {
        clearFile(TRANSACTIONS_FILE);
    }
    
    // Clear accounts file
    public void clearAccountsFile() {
        clearFile(ACCOUNTS_FILE);
    }
    
    // Clear specific file
    public void clearFile(String filePath) {
        try {
            Files.write(Paths.get(filePath), "".getBytes());
            System.out.println("✓ Cleared " + filePath);
        } catch (IOException e) {
            System.out.println("✗ Error clearing file: " + e.getMessage());
        }
    }
    
    // Check if file exists
    public boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }
    
    // Check if transactions file exists
    public boolean transactionsFileExists() {
        return fileExists(TRANSACTIONS_FILE);
    }
    
    // Check if accounts file exists
    public boolean accountsFileExists() {
        return fileExists(ACCOUNTS_FILE);
    }
    
    
    /**
     * Load accounts from accounts.txt file into AccountManager
     * Format: ACCOUNT,ACC###,CustomerName,Type,Balance,Status
     */
    public int loadAccountsFromFile(AccountManager accountManager) {
        try {
            if (!accountsFileExists()) {
                System.out.println("⚠ No accounts file found (accounts.txt)");
                return 0;
            }
            
            String content = readAccountsFile();
            if (content.isEmpty() || content.equals("File does not exist.")) {
                System.out.println("⚠ Accounts file is empty");
                return 0;
            }
            
            String[] lines = content.split("\n");
            int loadedCount = 0;
            
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("===") || line.startsWith("─") || 
                    line.startsWith("ACC NO") || !line.startsWith("ACCOUNT,")) {
                    continue;
                }
                
                try {
                    String[] parts = line.split(",");
                    if (parts.length >= 6) {
                        String accountNumber = parts[1].trim();
                        String customerName = parts[2].trim();
                        String accountType = parts[3].trim();
                        double balance = Double.parseDouble(parts[4].trim());
                        String status = parts[5].trim();
                        
                        // Create customer (simplified - using RegularCustomer as default)
                        Customer customer = new RegularCustomer(customerName, 30, "Loaded Address", "0000000000");
                        
                        // Create appropriate account type
                        Account account;
                        if (accountType.equalsIgnoreCase("SAVINGS")) {
                            account = new SavingsAccount(accountNumber, customer, balance);
                        } else {
                            account = new CheckingAccount(accountNumber, customer, balance);
                        }
                        
                        account.setStatus(status);
                        accountManager.addAccount(account, false); // Don't write back to file
                        loadedCount++;
                    }
                } catch (Exception e) {
                    System.out.println("⚠ Skipping invalid account line: " + line);
                }
            }
            
            System.out.println("✓ Loaded " + loadedCount + " account(s) from file");
            return loadedCount;
            
        } catch (Exception e) {
            System.out.println("✗ Error loading accounts: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Load transactions from transactions.txt file into TransactionManager
     * Format: TRANSACTION,TXN###,ACC###,Amount,Type,Date,Status,BalanceAfter
     */
    public int loadTransactionsFromFile(TransactionManager transactionManager, AccountManager accountManager) {
        try {
            if (!transactionsFileExists()) {
                System.out.println("⚠ No transactions file found (transactions.txt)");
                return 0;
            }
            
            String content = readTransactionsFile();
            if (content.isEmpty() || content.equals("File does not exist.")) {
                System.out.println("⚠ Transactions file is empty");
                return 0;
            }
            
            String[] lines = content.split("\n");
            int loadedCount = 0;
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
            
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("===") || line.startsWith("─") || 
                    line.startsWith("TXN ID") || !line.startsWith("TRANSACTION,")) {
                    continue;
                }
                
                try {
                    String[] parts = line.split(",");
                    if (parts.length >= 8) {
                        String transactionId = parts[1].trim();
                        String accountNumber = parts[2].trim();
                        double amount = Double.parseDouble(parts[3].trim());
                        String typeStr = parts[4].trim();
                        String dateStr = parts[5].trim();
                        String statusStr = parts[6].trim();
                        double balanceAfter = Double.parseDouble(parts[7].trim());
                        
                        // Find the account
                        Account account = accountManager.findAccount(accountNumber);
                        if (account == null) {
                            System.out.println("⚠ Account " + accountNumber + " not found for transaction " + transactionId);
                            continue;
                        }
                        
                        // Parse transaction type
                        TransactionType type = TransactionType.valueOf(typeStr.toUpperCase());
                        
                        // Parse date
                        LocalDate date = LocalDate.parse(dateStr, formatter);
                        
                        // Create transaction
                        Transaction transaction = new Transaction(account, amount, type, transactionId, date, balanceAfter);
                        
                        // Set status
                        if (statusStr.equalsIgnoreCase("COMPLETED")) {
                            transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
                        } else if (statusStr.equalsIgnoreCase("FAILED")) {
                            transaction.setStatus(Transaction.TransactionStatus.FAILED);
                        }
                        
                        transactionManager.addTransaction(transaction, false); // Don't write back to file
                        loadedCount++;
                    }
                } catch (Exception e) {
                    System.out.println("⚠ Skipping invalid transaction line: " + line + " - " + e.getMessage());
                }
            }
            
            System.out.println("✓ Loaded " + loadedCount + " transaction(s) from file");
            return loadedCount;
            
        } catch (Exception e) {
            System.out.println("✗ Error loading transactions: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Load both accounts and transactions from files
     */
    public void loadAllData(AccountManager accountManager, TransactionManager transactionManager) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  LOADING DATA FROM FILES");
        System.out.println("=".repeat(60));
        
        int accountsLoaded = loadAccountsFromFile(accountManager);
        int transactionsLoaded = loadTransactionsFromFile(transactionManager, accountManager);
        
        System.out.println("─".repeat(60));
        System.out.println("  Total Loaded: " + accountsLoaded + " accounts, " + transactionsLoaded + " transactions");
        System.out.println("=".repeat(60));
    }
}
