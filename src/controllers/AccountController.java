package controllers;
import java.util.Scanner;

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
            String userId = ValidationUtils.getManagerIdInput(scanner, "Enter your Manager ID: ");
            accountManager.viewAllAccounts();
        }
    }

}
