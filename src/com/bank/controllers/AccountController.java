package com.bank.controllers;
import java.util.Scanner;

import com.bank.models.CheckingAccount;
import com.bank.models.Customer;
import com.bank.models.PremiumCustomer;
import com.bank.models.RegularCustomer;
import com.bank.models.SavingsAccount;
import com.bank.models.Transaction;
import com.bank.models.enums.AccountType;
import com.bank.models.enums.CustomerType;
import com.bank.models.enums.TransactionType;
import com.bank.repository.AccountManager;
import com.bank.repository.CustomerManager;
import com.bank.repository.TransactionManager;

import java.time.LocalDate;
public class AccountController {
    private AccountManager accountManager;
    private CustomerManager customerManager;
    private TransactionManager transactionManager;
    private Scanner scanner = new Scanner(System.in);

    public AccountController(AccountManager accountManager, CustomerManager customerManager, TransactionManager transactionManager) {
        this.accountManager = accountManager;
        this.customerManager = customerManager;
        this.transactionManager = transactionManager;
    }

    public void createAccount(){
        System.out.println("\nACCOUNT CREATION");
        System.out.println("─".repeat(50));
        
        System.out.print("Enter customer name: ");
        String accountHolderName = scanner.nextLine().trim();
        
        System.out.print("Enter customer age: ");
        int age = Integer.parseInt(scanner.nextLine().trim());
        
        // Step 3: Get customer contact
        System.out.print("Enter customer contact: ");
        String contact = scanner.nextLine().trim();
        
        // Step 4: Get customer address
        System.out.print("Enter customer address: ");
        String accountHolderaddress = scanner.nextLine().trim();
        
        System.out.println();
        
        // Step 5: Customer Type
        System.out.println("Customer type:");
        System.out.println("1. Regular Customer (Standard banking services)");
        System.out.println("2. Premium Customer (Enhanced benefits, min balance $10,000)");
        System.out.println();
        System.out.print("Select type (1-2): ");
        int customerTypeChoice = Integer.parseInt(scanner.nextLine().trim());
        CustomerType customerType = customerTypeChoice == 2 ? CustomerType.PREMIUM : CustomerType.REGULAR;
        
        System.out.println();
        
        // Step 6: Account Type
        System.out.println("Account type:");
        System.out.println("1. Savings Account (Interest: 3.5%, Min Balance: $500)");
        System.out.println("2. Checking Account (Overdraft: $1,000, Monthly Fee: $10)");
        System.out.println();
        System.out.print("Select type (1-2): ");
        int accountTypeChoice = Integer.parseInt(scanner.nextLine().trim());
        AccountType accountType = accountTypeChoice == 1 ? AccountType.SAVINGS : AccountType.CHECKING;
        
        System.out.println();
        
        // Step 7: Initial deposit
        System.out.print("Enter initial deposit amount: $");
        double initialDeposit = Double.parseDouble(scanner.nextLine().trim());
        
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
                customerManager.addCustomer(accountHolder);
                
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
                customerManager.addCustomer(accountHolder);
                
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
        System.out.print("Select option (1 or 2): ");
        String choice = scanner.nextLine().trim();
        
        if (choice.equals("1")) {
            // View own account
            System.out.print("Enter your Account Number: ");
            String accountNumber = scanner.nextLine().trim();
            
            com.bank.models.Account account = accountManager.findAccount(accountNumber);
            if (account == null) {
                System.out.println("✗ Account not found!");
                return;
            }
            
            System.out.println("\n" + "=".repeat(80));
            System.out.println("ACCOUNT DETAILS");
            System.out.println("=".repeat(80));
            System.out.println(account.getAccountDetails());
            System.out.println("=".repeat(80));
        } else if (choice.equals("2")) {
            System.out.print("Enter your Manager ID: ");
            String userId = scanner.nextLine().trim();
            
            if (!userId.startsWith("MGR")) {
                System.out.println("✗ Access Denied: Only managers can view all accounts.");
                return;
            }
            
            accountManager.viewAllAccounts();
        } else {
            System.out.println("✗ Invalid option.");
        }
    }

}
