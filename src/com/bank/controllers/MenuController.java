package com.bank.controllers;

import java.time.LocalDate;
import java.util.Scanner;

import com.bank.models.CheckingAccount;
import com.bank.models.Customer;
import com.bank.models.SavingsAccount;
import com.bank.models.Transaction;
import com.bank.models.enums.CustomerType;
import com.bank.models.enums.Role;
import com.bank.models.enums.TransactionType;
import com.bank.repository.AccountManager;
import com.bank.repository.CustomerManager;
import com.bank.repository.TransactionManager;

public class MenuController {
    Scanner scanner = new Scanner(System.in);
    private AccountManager accountManager;
    private CustomerManager customerManager;
    private TransactionManager transactionManager;
    private AccountController accountController;
    private TransactionController transactionController;
   

    public MenuController(){
        this.accountManager = new AccountManager();
        this.customerManager = new CustomerManager();
        this.transactionManager = new TransactionManager();
        this.accountController = new AccountController(accountManager, customerManager, transactionManager);
        this.transactionController = new TransactionController(accountManager, customerManager, transactionManager);
        
        Customer manager = new Customer("Bank Manager", 35, "Bank HQ, Main Street", "000-000-0000", Role.MANAGER, null);
        customerManager.addCustomer(manager);
        
        // Seed initial accounts with transactions
        seedAccounts();
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  SYSTEM INITIALIZED - MANAGER ACCOUNT CREATED");
        System.out.println("=".repeat(60));
        System.out.println("  Manager ID: " + manager.getCustomerId());
        System.out.println("  Manager Name: " + manager.getName());
        System.out.println("  Use this ID to access manager functions");
        System.out.println("  Seeded Accounts: 5 demo accounts created");
        System.out.println("=".repeat(60));

    }
    
    private void seedAccounts() {
        // Customer 1 - Regular Savings Account
        Customer customer1 = new Customer("John Smith", 28, "123 Main St, Boston", "+1-555-1001", Role.CUSTOMER, CustomerType.REGULAR);
        customerManager.addCustomer(customer1);
        String accNum1 = accountManager.generateAccountNumber();
        SavingsAccount acc1 = new SavingsAccount(accNum1, customer1, 5000.0);
        accountManager.addAccount(acc1);
        
        // Initial deposit transaction
        String txn1 = transactionManager.generateTransactionId();
        Transaction t1 = new Transaction(acc1, 5000.0, TransactionType.DEPOSIT, txn1, LocalDate.now().minusDays(30), 5000.0);
        t1.setStatus(Transaction.TransactionStatus.COMPLETED);
        transactionManager.addTransaction(t1);
        
        // Additional transaction
        String txn2 = transactionManager.generateTransactionId();
        Transaction t2 = new Transaction(acc1, 500.0, TransactionType.WITHDRAW, txn2, LocalDate.now().minusDays(15), 4500.0);
        t2.setStatus(Transaction.TransactionStatus.COMPLETED);
        transactionManager.addTransaction(t2);
        
        // Customer 2 - Premium Savings Account
        Customer customer2 = new Customer("Sarah Johnson", 45, "456 Oak Ave, Chicago", "+1-555-2002", Role.CUSTOMER, CustomerType.PREMIUM);
        customerManager.addCustomer(customer2);
        String accNum2 = accountManager.generateAccountNumber();
        SavingsAccount acc2 = new SavingsAccount(accNum2, customer2, 15000.0);
        accountManager.addAccount(acc2);
        
        String txn3 = transactionManager.generateTransactionId();
        Transaction t3 = new Transaction(acc2, 15000.0, TransactionType.DEPOSIT, txn3, LocalDate.now().minusDays(45), 15000.0);
        t3.setStatus(Transaction.TransactionStatus.COMPLETED);
        transactionManager.addTransaction(t3);
        
        String txn4 = transactionManager.generateTransactionId();
        Transaction t4 = new Transaction(acc2, 2000.0, TransactionType.DEPOSIT, txn4, LocalDate.now().minusDays(20), 17000.0);
        t4.setStatus(Transaction.TransactionStatus.COMPLETED);
        transactionManager.addTransaction(t4);
        
        // Customer 3 - Regular Checking Account
        Customer customer3 = new Customer("Michael Chen", 32, "789 Pine Rd, Seattle", "+1-555-3003", Role.CUSTOMER, CustomerType.REGULAR);
        customerManager.addCustomer(customer3);
        String accNum3 = accountManager.generateAccountNumber();
        CheckingAccount acc3 = new CheckingAccount(accNum3, customer3, 3000.0);
        accountManager.addAccount(acc3);
        
        String txn5 = transactionManager.generateTransactionId();
        Transaction t5 = new Transaction(acc3, 3000.0, TransactionType.DEPOSIT, txn5, LocalDate.now().minusDays(25), 3000.0);
        t5.setStatus(Transaction.TransactionStatus.COMPLETED);
        transactionManager.addTransaction(t5);
        
        String txn6 = transactionManager.generateTransactionId();
        Transaction t6 = new Transaction(acc3, 800.0, TransactionType.WITHDRAW, txn6, LocalDate.now().minusDays(10), 2200.0);
        t6.setStatus(Transaction.TransactionStatus.COMPLETED);
        transactionManager.addTransaction(t6);
        
        // Customer 4 - Premium Checking Account
        Customer customer4 = new Customer("Emily Davis", 38, "321 Elm St, Miami", "+1-555-4004", Role.CUSTOMER, CustomerType.PREMIUM);
        customerManager.addCustomer(customer4);
        String accNum4 = accountManager.generateAccountNumber();
        CheckingAccount acc4 = new CheckingAccount(accNum4, customer4, 12000.0);
        accountManager.addAccount(acc4);
        
        String txn7 = transactionManager.generateTransactionId();
        Transaction t7 = new Transaction(acc4, 12000.0, TransactionType.DEPOSIT, txn7, LocalDate.now().minusDays(60), 12000.0);
        t7.setStatus(Transaction.TransactionStatus.COMPLETED);
        transactionManager.addTransaction(t7);
        
        String txn8 = transactionManager.generateTransactionId();
        Transaction t8 = new Transaction(acc4, 1500.0, TransactionType.WITHDRAW, txn8, LocalDate.now().minusDays(5), 10500.0);
        t8.setStatus(Transaction.TransactionStatus.COMPLETED);
        transactionManager.addTransaction(t8);
        
        // Customer 5 - Regular Savings Account
        Customer customer5 = new Customer("David Wilson", 29, "654 Maple Dr, Denver", "+1-555-5005", Role.CUSTOMER, CustomerType.REGULAR);
        customerManager.addCustomer(customer5);
        String accNum5 = accountManager.generateAccountNumber();
        SavingsAccount acc5 = new SavingsAccount(accNum5, customer5, 2500.0);
        accountManager.addAccount(acc5);
        
        String txn9 = transactionManager.generateTransactionId();
        Transaction t9 = new Transaction(acc5, 2500.0, TransactionType.DEPOSIT, txn9, LocalDate.now().minusDays(40), 2500.0);
        t9.setStatus(Transaction.TransactionStatus.COMPLETED);
        transactionManager.addTransaction(t9);
        
        String txn10 = transactionManager.generateTransactionId();
        Transaction t10 = new Transaction(acc5, 1000.0, TransactionType.DEPOSIT, txn10, LocalDate.now().minusDays(8), 3500.0);
        t10.setStatus(Transaction.TransactionStatus.COMPLETED);
        transactionManager.addTransaction(t10);

    }
    public void start(){
        boolean running = true;
        while(running){
            displayMenu();
            String input = scanner.nextLine().trim();
            switch(input){
                case "1":
                    accountController.createAccount();
                    break;
                case "2":
                    accountController.viewAllAccounts();
                    break;
                case "3":
                    transactionController.recordTransaction();
                    break;
                case "4":
                    transactionController.viewTransactionHistory();
                    break;
                case "5":
                    System.out.println("Exiting the system. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
            
            if(running){
                System.out.print("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }

    }



private void displayMenu(){
    System.out.println("\n" + "=".repeat(50));
    System.out.println("  BANK ACCOUNT MANAGEMENT - MAIN MENU");
    System.out.println("=".repeat(50));
    System.out.println();
    System.out.println("1. Create Account");
    System.out.println("2. View Accounts");
    System.out.println("3. Process Transaction");
    System.out.println("4. View Transaction History");
    System.out.println("5. Exit");
    System.out.println();
    System.out.print("Enter choice: ");
}
}
