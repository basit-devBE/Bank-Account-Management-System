package com.bank.controllers;
import java.util.Scanner;
import java.util.UUID;

import com.bank.models.CheckingAccount;
import com.bank.models.Customer;
import com.bank.models.SavingsAccount;
import com.bank.models.enums.CustomerType;
import com.bank.repository.AccountManager;
import com.bank.repository.CustomerManager;
public class Account {
    AccountManager accountManager = new AccountManager();
    CustomerManager customerManager = new CustomerManager();
    Scanner scanner = new Scanner(System.in);

    public void createAccount(){
        System.out.println("Enter the account type (Savings/Checking): ");
        String accountType = scanner.nextLine().trim();
        String accountNumber;
        String accountHolderName;
        String email;
        double initialDeposit;
        accountNumber = UUID.randomUUID().toString();
        System.out.println("Enter account holder name: ");
        accountHolderName = scanner.nextLine().trim();
        System.out.println("Enter account holder email: ");
        email = scanner.nextLine().trim();
        System.out.println("Enter initial deposit amount: ");
        initialDeposit = Double.parseDouble(scanner.nextLine().trim());
        switch(accountType.toLowerCase()){
            case "savings":{
                Customer accountHolder = new Customer(accountHolderName,email,CustomerType.Regular);
                SavingsAccount newAccount = new SavingsAccount(accountNumber,accountHolder, initialDeposit);
                accountManager.addAccount(newAccount);
                customerManager.addCustomer(accountHolder);
                System.out.println("Savings account created successfully. Account Details: " + accountManager.getAccountDetail(accountNumber));
                break;
            }

            case "checking":{
                Customer accountHolder = new Customer(accountHolderName, email, CustomerType.Regular);
                CheckingAccount newAccount = new CheckingAccount(accountNumber, accountHolder, initialDeposit);
                accountManager.addAccount(newAccount);
                customerManager.addCustomer(accountHolder);
                System.out.println("Savings account created successfully. Account Details: " + accountManager.getAccountDetail(accountNumber));
                break;
            }
        }


    }

}
