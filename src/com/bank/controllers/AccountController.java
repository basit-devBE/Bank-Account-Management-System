package com.bank.controllers;
import java.util.Scanner;
import java.util.UUID;

import com.bank.models.CheckingAccount;
import com.bank.models.Customer;
import com.bank.models.SavingsAccount;
import com.bank.models.enums.AccountType;
import com.bank.models.enums.CustomerType;
import com.bank.repository.AccountManager;
import com.bank.repository.CustomerManager;
public class AccountController {
    AccountManager accountManager = new AccountManager();
    CustomerManager customerManager = new CustomerManager();
    Scanner scanner = new Scanner(System.in);

    public void createAccount(){
        System.out.println("Enter the account type (Savings/Checking): ");
        AccountType accountType = scanner.nextLine().trim().equalsIgnoreCase("savings") ? AccountType.SAVINGS : AccountType.CHECKING;
        String accountNumber;
        String accountHolderName;
        String accountHolderaddress;
        String contact;
        CustomerType customerType;
    
        int age;
        double initialDeposit;
        accountNumber = UUID.randomUUID().toString();
        System.out.println("Enter account holder name: ");
        accountHolderName = scanner.nextLine().trim();
        System.out.println("Enter account holder address: ");
        accountHolderaddress = scanner.nextLine().trim();
        System.out.println("Enter account holder age: ");
        age = Integer.parseInt(scanner.nextLine().trim());
        System.out.println("Enter account holder contact number: ");
        contact = scanner.nextLine().trim();
        System.out.println("Enter customer Type REGULAR/PREMIUM: ");
        customerType = scanner.nextLine().trim().equalsIgnoreCase("PREMIUM") ? CustomerType.PREMIUM : CustomerType.REGULAR;

        System.out.println("Enter initial deposit amount: ");
        initialDeposit = Double.parseDouble(scanner.nextLine().trim());
        switch(accountType){
            case SAVINGS:{
                Customer accountHolder = new Customer(accountHolderName,age,customerType,accountHolderaddress,contact);
                SavingsAccount newAccount = new SavingsAccount(accountNumber,accountHolder, initialDeposit);
                accountManager.addAccount(newAccount);
                customerManager.addCustomer(accountHolder);
                System.out.println(newAccount.getCreationMessage()); 
                break;
            }

            case CHECKING:{
                Customer accountHolder = new Customer(accountHolderName,age,customerType,accountHolderaddress,contact);
                CheckingAccount newAccount = new CheckingAccount(accountNumber, accountHolder, initialDeposit);
                accountManager.addAccount(newAccount);
                customerManager.addCustomer(accountHolder);
                System.out.println(newAccount.getCreationMessage());
                break;
            }
        }


    }

    public void viewAllAccounts(){
        accountManager.viewAllAccounts();
    }

}
