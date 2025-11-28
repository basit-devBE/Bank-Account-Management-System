package com.bank.controllers;

import java.util.Scanner;

import com.bank.repository.AccountManager;

public class MenuController {
    Scanner scanner = new Scanner(System.in);
    private AccountManager accountManager;
    private AccountController accountController;
    private TransactionController transactionController;
   

    public MenuController(){
        this.accountManager = new AccountManager();
        this.accountController = new AccountController(accountManager);
        this.transactionController = new TransactionController(accountManager);

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
    }

    }



private void displayMenu(){
    System.out.println("Welcome to the Bank Account Management System");
    System.out.println("1. Create Account");
    System.out.println("2. View Accounts");
    System.out.println("3. Process Transaction");
    System.out.println("4. View Transaction History");
    System.out.println("5. Exit");
    System.out.print("Please select an option (1-5): ");
}
}
