package com.bank.controllers;
import com.bank.models.enums.TransactionType;
import com.bank.repository.AccountManager;
import com.bank.repository.TransactionManager;
import java.util.Scanner;

public class TransactionController {
    private TransactionManager transactionManager = new TransactionManager();
    private String transactionId = "TXN" + System.currentTimeMillis();
    double amount = 0.0;
    double balance = 0.0;
    private TransactionType transactionType;
    AccountManager accountManager = new AccountManager();
    String accountNumber;
    Scanner scanner = new Scanner(System.in);

    public void recordTransaction(){
        System.out.println("Recording transaction with ID: " + transactionId);
        System.out.println("Enter Account Number: ");
       accountNumber = scanner.nextLine().trim();
       accountManager.findAccount(accountNumber);
       System.out.println("Enter Transaction Type (DEPOSIT/WITHDRAW): ");
       transactionType = scanner.nextLine().trim().equalsIgnoreCase("DEPOSIT") ? TransactionType.DEPOSIT : TransactionType.WITHDRAW;
       System.out.println("Enter amount: ");
       try {
           amount = Double.parseDouble(scanner.nextLine().trim());
       } catch (NumberFormatException e) {
           System.out.println("Invalid amount entered.");
           return;
       }
       switch (transactionType){
        case DEPOSIT:{
            break;
        }
        case WITHDRAW:{
           
            break;
        }
        default:{
            System.out.println("Unknown transaction type.");
        }

       }

    }

}
