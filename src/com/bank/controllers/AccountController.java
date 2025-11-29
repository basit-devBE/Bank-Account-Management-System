package com.bank.controllers;
import java.util.Scanner;
import java.util.UUID;

import com.bank.models.CheckingAccount;
import com.bank.models.Customer;
import com.bank.models.SavingsAccount;
import com.bank.models.enums.AccountType;
import com.bank.models.enums.CustomerType;
import com.bank.models.enums.Role;
import com.bank.repository.AccountManager;
import com.bank.repository.CustomerManager;
public class AccountController {
    private AccountManager accountManager;
    private CustomerManager customerManager;
    private Scanner scanner = new Scanner(System.in);

    public AccountController(AccountManager accountManager, CustomerManager customerManager) {
        this.accountManager = accountManager;
        this.customerManager = customerManager;
    }

    public void createAccount(){
        System.out.println("\n--- Create Account ---");
        System.out.println("Account Type:");
        System.out.println("1. Savings");
        System.out.println("2. Checking");
        System.out.print("Select type (1 or 2): ");
        int accountTypeChoice = Integer.parseInt(scanner.nextLine().trim());
        AccountType accountType = accountTypeChoice == 1 ? AccountType.SAVINGS : AccountType.CHECKING;
        
        String accountNumber;
        String accountHolderName;
        String accountHolderaddress;
        String contact;
    
        int age;
        double initialDeposit;
        accountNumber = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        
        System.out.print("Enter account holder name: ");
        accountHolderName = scanner.nextLine().trim();
        
        System.out.print("Enter account holder address: ");
        accountHolderaddress = scanner.nextLine().trim();
        
        System.out.print("Enter account holder age: ");
        age = Integer.parseInt(scanner.nextLine().trim());
        
        System.out.print("Enter account holder contact number: ");
        contact = scanner.nextLine().trim();
        
        System.out.println("Customer Type:");
        System.out.println("1. Regular");
        System.out.println("2. Premium");
        System.out.print("Select type (1 or 2): ");
        int customerTypeChoice = Integer.parseInt(scanner.nextLine().trim());
        CustomerType customerType = customerTypeChoice == 2 ? CustomerType.PREMIUM : CustomerType.REGULAR;
        

        System.out.print("Enter initial deposit amount: ");
        initialDeposit = Double.parseDouble(scanner.nextLine().trim());
        
        if (customerType == CustomerType.PREMIUM) {
            double premiumMinDeposit = 10000.0;
            if (initialDeposit < premiumMinDeposit) {
                System.out.println("✗ Premium customers require minimum deposit of $" + String.format("%,.2f", premiumMinDeposit));
                return;
            }
        }
        
        Customer accountHolder = new Customer(accountHolderName, age, accountHolderaddress, contact, Role.CUSTOMER, customerType);
        
        switch(accountType){
            case SAVINGS:{
                SavingsAccount newAccount = new SavingsAccount(accountNumber, accountHolder, initialDeposit);
                accountManager.addAccount(newAccount);
                customerManager.addCustomer(accountHolder);
                System.out.println(newAccount.getCreationMessage()); 
                break;
            }

            case CHECKING:{
                CheckingAccount newAccount = new CheckingAccount(accountNumber, accountHolder, initialDeposit);
                accountManager.addAccount(newAccount);
                customerManager.addCustomer(accountHolder);
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
