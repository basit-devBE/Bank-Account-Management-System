package com.bank.controllers;

import java.util.Scanner;

public class MenuController {
    Scanner scanner = new Scanner(System.in);
    AccountController accountController = new AccountController();

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
                    // accountController.withdrawFunds();
                    break;
                case "4":
                    // accountController.checkBalance();
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
    System.out.println("3. Withdraw Funds");
    System.out.println("4. Check Balance");
    System.out.println("5. Exit");
    System.out.print("Please select an option (1-5): ");
}
}
