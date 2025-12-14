package controllers;
import java.util.Scanner;

import models.Account;
import models.CheckingAccount;
import models.Customer;
import models.PremiumCustomer;
import models.RegularCustomer;
import models.SavingsAccount;
import models.Transaction;
import models.enums.AccountType;
import models.enums.CustomerType;
import models.enums.TransactionType;
import services.AccountManager;
import services.TransactionManager;
import utils.ValidationUtils;

import java.time.LocalDate;
import java.util.List;
public class AccountController {
    private AccountManager accountManager;
    private TransactionManager transactionManager;
    private Scanner scanner = new Scanner(System.in);

    public AccountController(AccountManager accountManager, TransactionManager transactionManager) {
        this.accountManager = accountManager;
        this.transactionManager = transactionManager;
    }

    public void createAccount(){
        System.out.println("\nACCOUNT CREATION");
        System.out.println("─".repeat(50));
        
        String accountHolderName;
        while (true) {
            System.out.print("Enter customer name: ");
            accountHolderName = scanner.nextLine().trim();
            if (ValidationUtils.isValidName(accountHolderName)) {
                break;
            }
            System.out.println("✗ Invalid name. Name must contain only letters and be at least 2 characters.");
        }
        
        int age = ValidationUtils.getIntInput(scanner, "Enter customer age: ", 18, 120);
        
        // Step 3: Get customer contact
        String contact;
        while (true) {
            System.out.print("Enter customer contact: ");
            contact = scanner.nextLine().trim();
            if (ValidationUtils.isValidContact(contact)) {
                break;
            }
            System.out.println("✗ Invalid contact. Please enter a valid phone number (e.g., +1-555-1234).");
        }
        
        // Step 4: Get customer address
        String accountHolderaddress;
        while (true) {
            System.out.print("Enter customer address: ");
            accountHolderaddress = scanner.nextLine().trim();
            if (ValidationUtils.validateInput(accountHolderaddress, ValidationUtils.InputType.String)) {
                break;
            }
            System.out.println("✗ Invalid address. Please enter a valid address.");
        }

        
        System.out.println();
        
        // Step 5: Customer Type
        System.out.println("Customer type:");
        System.out.println("1. Regular Customer (Standard banking services)");
        System.out.println("2. Premium Customer (Enhanced benefits, min balance $10,000)");
        System.out.println();
        int customerTypeChoice = ValidationUtils.getIntInput(scanner, "Select type (1-2): ", 1, 2);

        CustomerType customerType = customerTypeChoice == 2 ? CustomerType.PREMIUM : CustomerType.REGULAR;
        
        System.out.println();
        
        // Step 6: Account Type
        System.out.println("Account type:");
        System.out.println("1. Savings Account (Interest: 3.5%, Min Balance: $500)");
        System.out.println("2. Checking Account (Overdraft: $1,000, Monthly Fee: $10)");
        System.out.println();
        int accountTypeChoice = ValidationUtils.getIntInput(scanner, "Select type (1-2): ", 1, 2);
        AccountType accountType = accountTypeChoice == 1 ? AccountType.SAVINGS : AccountType.CHECKING;
        
        System.out.println();
        
        // Step 7: Initial deposit
        double initialDeposit = ValidationUtils.getDoubleInput(scanner, "Enter initial deposit amount: $", 0.01);
        
        // Validate premium customer minimum deposit
        if (customerType == CustomerType.PREMIUM) {
            double premiumMinDeposit = 10000.0;
            if (initialDeposit < premiumMinDeposit) {
                System.out.println("✗ Premium customers require minimum deposit of $" + String.format("%,.2f", premiumMinDeposit));
                return;
            }
        }
        
        String accountNumber = accountManager.generateAccountNumber();
        
        // Create customer based on type
        Customer accountHolder;
        if (customerType == CustomerType.PREMIUM) {
            accountHolder = new PremiumCustomer(accountHolderName, age, accountHolderaddress, contact);
        } else {
            accountHolder = new RegularCustomer(accountHolderName, age, accountHolderaddress, contact);
        }
        
        switch(accountType){
            case SAVINGS:{
                SavingsAccount newAccount = new SavingsAccount(accountNumber, accountHolder, initialDeposit);
                accountManager.addAccount(newAccount);
                
                String transactionId = transactionManager.generateTransactionId();
                Transaction initialDepositTxn = new Transaction(newAccount, initialDeposit, 
                    TransactionType.DEPOSIT, transactionId, LocalDate.now(), initialDeposit);
                initialDepositTxn.setStatus(Transaction.TransactionStatus.COMPLETED);
                transactionManager.addTransaction(initialDepositTxn);
                
                System.out.println(newAccount.getCreationMessage()); 
                break;
            }

            case CHECKING:{
                CheckingAccount newAccount = new CheckingAccount(accountNumber, accountHolder, initialDeposit);
                accountManager.addAccount(newAccount);
                
                // Record initial deposit transaction
                String transactionId = transactionManager.generateTransactionId();
                Transaction initialDepositTxn = new Transaction(newAccount, initialDeposit, 
                    TransactionType.DEPOSIT, transactionId, LocalDate.now(), initialDeposit);
                initialDepositTxn.setStatus(Transaction.TransactionStatus.COMPLETED);
                transactionManager.addTransaction(initialDepositTxn);
                
                System.out.println(newAccount.getCreationMessage());
                break;
            }
        }


    }

    public void viewAllAccounts(){
        System.out.println("\n--- View Accounts ---");
        System.out.println("1. View My Account");
        System.out.println("2. View All Accounts (Manager Only)");
        int choice = ValidationUtils.getIntInput(scanner, "Select option (1 or 2): ", 1, 2);
        
        if (choice == 1) {
            // View own account
            String accountNumber = ValidationUtils.getAccountNumberInput(scanner, "Enter your Account Number: ");
            
            models.Account account = accountManager.findAccount(accountNumber);
            if (account == null) {
                System.out.println("✗ Account not found!");
                return;
            }
            
            System.out.println("\n" + "=".repeat(80));
            System.out.println("ACCOUNT DETAILS");
            System.out.println("=".repeat(80));
            System.out.println(account.getAccountDetails());
            System.out.println("=".repeat(80));
        } else {
            @SuppressWarnings("unused")
            String userId = ValidationUtils.getManagerIdInput(scanner, "Enter your Manager ID: ");
            viewManagerAccountMenu();
        }
    }

    private void viewManagerAccountMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("  ACCOUNT VIEW OPTIONS");
        System.out.println("=".repeat(50));
        System.out.println();
        System.out.println("1. View All Accounts (Sorted by Account Number)");
        System.out.println("2. View Accounts Sorted by Balance");
        System.out.println("3. View Accounts Sorted by Customer Name");
        System.out.println("4. Filter by Account Type");
        System.out.println("5. Filter by Balance Range");
        System.out.println("6. Filter by Customer Type");
        System.out.println("7. View Account Analytics");
        System.out.println("8. Back");
        System.out.println();
        System.out.print("Enter choice: ");
        
        int choice = ValidationUtils.getIntInput(scanner, "", 1, 8);
        
        switch(choice) {
            case 1:
                accountManager.viewAllAccounts();
                break;
            case 2:
                viewAccountsSortedByBalance();
                break;
            case 3:
                viewAccountsSortedByCustomerName();
                break;
            case 4:
                filterByAccountType();
                break;
            case 5:
                filterByBalanceRange();
                break;
            case 6:
                filterByCustomerType();
                break;
            case 7:
                viewAccountAnalytics();
                break;
            case 8:
                return;
        }
    }

    private void viewAccountsSortedByBalance() {
        List<Account> accounts = accountManager.getAccountsSortedByBalance();
        
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }
        
        System.out.println("\nACCOUNTS SORTED BY BALANCE (Highest to Lowest)");
        System.out.println("─".repeat(85));
        System.out.printf("%-10s | %-20s | %-10s | %-15s | %-10s%n",
                "ACC NO", "CUSTOMER NAME", "TYPE", "BALANCE", "STATUS");
        System.out.println("─".repeat(85));
        
        accounts.forEach(account -> {
            System.out.println(account.getAccountSummary());
            System.out.println("─".repeat(85));
        });
    }

    private void viewAccountsSortedByCustomerName() {
        List<Account> accounts = accountManager.getAccountsSortedByCustomerName();
        
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }
        
        System.out.println("\nACCOUNTS SORTED BY CUSTOMER NAME");
        System.out.println("─".repeat(85));
        System.out.printf("%-10s | %-20s | %-10s | %-15s | %-10s%n",
                "ACC NO", "CUSTOMER NAME", "TYPE", "BALANCE", "STATUS");
        System.out.println("─".repeat(85));
        
        accounts.forEach(account -> {
            System.out.println(account.getAccountSummary());
            System.out.println("─".repeat(85));
        });
    }

    private void filterByAccountType() {
        System.out.println("\nSelect Account Type:");
        System.out.println("1. SAVINGS");
        System.out.println("2. CHECKING");
        
        int typeChoice = ValidationUtils.getIntInput(scanner, "Enter choice (1-2): ", 1, 2);
        
        String selectedType = typeChoice == 1 ? "Savings" : "Checking";
        List<Account> filtered = accountManager.filterAccountsByType(selectedType);
        
        if (filtered.isEmpty()) {
            System.out.println("\nNo " + selectedType + " accounts found.");
            return;
        }
        
        System.out.println("\nFILTERED ACCOUNTS - Type: " + selectedType.toUpperCase());
        System.out.println("─".repeat(85));
        System.out.printf("%-10s | %-20s | %-10s | %-15s | %-10s%n",
                "ACC NO", "CUSTOMER NAME", "TYPE", "BALANCE", "STATUS");
        System.out.println("─".repeat(85));
        
        filtered.forEach(account -> {
            System.out.println(account.getAccountSummary());
            System.out.println("─".repeat(85));
        });
        System.out.println("Total Accounts: " + filtered.size());
    }

    private void filterByBalanceRange() {
        double minBalance = ValidationUtils.getDoubleInput(scanner, "Enter minimum balance: $", 0.0);
        double maxBalance = ValidationUtils.getDoubleInput(scanner, "Enter maximum balance: $", minBalance);
        
        List<Account> filtered = accountManager.filterAccountsByBalanceRange(minBalance, maxBalance);
        
        if (filtered.isEmpty()) {
            System.out.println("\nNo accounts found in balance range $" + String.format("%.2f", minBalance) + " - $" + String.format("%.2f", maxBalance));
            return;
        }
        
        System.out.println("\nFILTERED ACCOUNTS - Balance Range: $" + String.format("%.2f", minBalance) + " - $" + String.format("%.2f", maxBalance));
        System.out.println("─".repeat(85));
        System.out.printf("%-10s | %-20s | %-10s | %-15s | %-10s%n",
                "ACC NO", "CUSTOMER NAME", "TYPE", "BALANCE", "STATUS");
        System.out.println("─".repeat(85));
        
        filtered.forEach(account -> {
            System.out.println(account.getAccountSummary());
            System.out.println("─".repeat(85));
        });
        System.out.println("Total Accounts: " + filtered.size());
    }

    private void filterByCustomerType() {
        System.out.println("\nSelect Customer Type:");
        System.out.println("1. REGULAR");
        System.out.println("2. PREMIUM");
        
        int typeChoice = ValidationUtils.getIntInput(scanner, "Enter choice (1-2): ", 1, 2);
        
        String selectedType = typeChoice == 1 ? "REGULAR" : "PREMIUM";
        List<Account> filtered = accountManager.filterAccountsByCustomerType(selectedType);
        
        if (filtered.isEmpty()) {
            System.out.println("\nNo " + selectedType + " customer accounts found.");
            return;
        }
        
        System.out.println("\nFILTERED ACCOUNTS - Customer Type: " + selectedType);
        System.out.println("─".repeat(85));
        System.out.printf("%-10s | %-20s | %-10s | %-15s | %-10s%n",
                "ACC NO", "CUSTOMER NAME", "TYPE", "BALANCE", "STATUS");
        System.out.println("─".repeat(85));
        
        filtered.forEach(account -> {
            System.out.println(account.getAccountSummary());
            System.out.println("─".repeat(85));
        });
        System.out.println("Total Accounts: " + filtered.size());
    }

    private void viewAccountAnalytics() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("           ACCOUNT ANALYTICS");
        System.out.println("=".repeat(60));
        System.out.println();
        
        int totalAccounts = accountManager.getAccountCount();
        double totalBalance = accountManager.getTotalBalance();
        double averageBalance = accountManager.getAverageBalance();
        
        long savingsCount = accountManager.countAccountsByType("Savings");
        long checkingCount = accountManager.countAccountsByType("Checking");
        
        Account highestBalanceAccount = accountManager.getAccountWithHighestBalance();
        Account lowestBalanceAccount = accountManager.getAccountWithLowestBalance();
        
        System.out.println("ACCOUNT STATISTICS:");
        System.out.println("  Total Accounts: " + totalAccounts);
        System.out.println("  Savings Accounts: " + savingsCount);
        System.out.println("  Checking Accounts: " + checkingCount);
        System.out.println();
        System.out.println("BALANCE STATISTICS:");
        System.out.println("  Total Bank Balance: $" + String.format("%,.2f", totalBalance));
        System.out.println("  Average Account Balance: $" + String.format("%,.2f", averageBalance));
        System.out.println();
        
        if (highestBalanceAccount != null) {
            System.out.println("HIGHEST BALANCE ACCOUNT:");
            System.out.println("  Account: " + highestBalanceAccount.getAccountNumber());
            System.out.println("  Customer: " + highestBalanceAccount.getCustomer().getName());
            System.out.println("  Balance: $" + String.format("%,.2f", highestBalanceAccount.getBalance()));
            System.out.println();
        }
        
        if (lowestBalanceAccount != null) {
            System.out.println("LOWEST BALANCE ACCOUNT:");
            System.out.println("  Account: " + lowestBalanceAccount.getAccountNumber());
            System.out.println("  Customer: " + lowestBalanceAccount.getCustomer().getName());
            System.out.println("  Balance: $" + String.format("%,.2f", lowestBalanceAccount.getBalance()));
            System.out.println();
        }
        
        System.out.println("=".repeat(60));
    }

}
