package com.bank;


import com.bank.controllers.AccountController;
import com.bank.controllers.MenuController;
import com.bank.controllers.TransactionController;
import com.bank.services.AccountManager;
import com.bank.services.TransactionManager;

public class Main{
    public static void main(String[] args) {
        AccountManager accountManager = new AccountManager();
        TransactionManager transactionManager = new TransactionManager();
        AccountController accountController = new AccountController(accountManager, transactionManager);
        TransactionController transactionController = new TransactionController(accountManager, transactionManager);
        
        MenuController menu = new MenuController(accountController, transactionController);
        menu.initializeDemoData(accountManager, transactionManager);
        menu.start();
    }

}