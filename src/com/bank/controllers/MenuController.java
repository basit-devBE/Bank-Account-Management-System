package com.bank.controllers;

import java.util.Scanner;

import com.bank.models.Customer;
import com.bank.models.enums.Role;
import com.bank.repository.AccountManager;
import com.bank.repository.CustomerManager;

public class MenuController {
    Scanner scanner = new Scanner(System.in);
    private AccountManager accountManager;
    private CustomerManager customerManager;
    private AccountController accountController;
    private TransactionController transactionController;
   

    public MenuController(){
        this.accountManager = new AccountManager();
        this.customerManager = new CustomerManager();
        this.accountController = new AccountController(accountManager, customerManager);
        this.transactionController = new TransactionController(accountManager);
        
        Customer manager = new Customer("Bank Manager", 35, "Bank HQ, Main Street", "000-000-0000", Role.MANAGER, null);
        customerManager.addCustomer(manager);
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  SYSTEM INITIALIZED - MANAGER ACCOUNT CREATED");
        System.out.println("=".repeat(60));
        System.out.println("  Manager ID: " + manager.getCustomerId());
        System.out.println("  Manager Name: " + manager.getName());
        System.out.println("  Use this ID to access manager functions");
        System.out.println("=".repeat(60));

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
