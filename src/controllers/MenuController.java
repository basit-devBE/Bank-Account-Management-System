package controllers;

import java.time.LocalDate;
import java.util.Scanner;

import models.Account;
import models.CheckingAccount;
import models.Customer;
import models.PremiumCustomer;
import models.RegularCustomer;
import models.SavingsAccount;
import models.Transaction;
import models.enums.TransactionType;
import services.AccountManager;
import services.StatementGenerator;
import services.TransactionManager;

public class MenuController {
    private final Scanner scanner;
    private final AccountController accountController;
    private final TransactionController transactionController;
    private final AccountManager accountManager;
    private final TransactionManager transactionManager;
    private final StatementGenerator statementGenerator;
   

    public MenuController(AccountController accountController, TransactionController transactionController, 
                          AccountManager accountManager, TransactionManager transactionManager){
        this.scanner = new Scanner(System.in);
        this.accountController = accountController;
        this.transactionController = transactionController;
        this.accountManager = accountManager;
        this.transactionManager = transactionManager;
        this.statementGenerator = new StatementGenerator(transactionManager);
    }
    
    public void initializeDemoData(AccountManager accountManager, TransactionManager transactionManager) {
        seedAccounts(accountManager, transactionManager);
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  SYSTEM INITIALIZED - Demo Mode");
        System.out.println("=".repeat(60));
        System.out.println("  Seeded Accounts: 5 demo accounts created");
        System.out.println("=".repeat(60));
    }
    
    private void seedAccounts(AccountManager accountManager, TransactionManager transactionManager) {
        Customer customer1 = new RegularCustomer("John Smith", 28, "123 Main St, Boston", "+1-555-1001");
        String accNum1 = accountManager.generateAccountNumber();
        SavingsAccount acc1 = new SavingsAccount(accNum1, customer1, 5000.0);
        accountManager.addAccount(acc1);
        
        String txn1 = transactionManager.generateTransactionId();
        Transaction t1 = new Transaction(acc1, 5000.0, TransactionType.DEPOSIT, txn1, LocalDate.now().minusDays(30), 5000.0);
        t1.setStatus(Transaction.TransactionStatus.COMPLETED);
        transactionManager.addTransaction(t1);
        
        String txn2 = transactionManager.generateTransactionId();
        Transaction t2 = new Transaction(acc1, 500.0, TransactionType.WITHDRAW, txn2, LocalDate.now().minusDays(15), 4500.0);
        t2.setStatus(Transaction.TransactionStatus.COMPLETED);
        transactionManager.addTransaction(t2);
        
        Customer customer2 = new PremiumCustomer("Sarah Johnson", 45, "456 Oak Ave, Chicago", "+1-555-2002");
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
        
        Customer customer3 = new RegularCustomer("Michael Chen", 32, "789 Pine Rd, Seattle", "+1-555-3003");
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
        
        Customer customer4 = new PremiumCustomer("Emily Davis", 38, "321 Elm St, Miami", "+1-555-4004");
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
        
        Customer customer5 = new RegularCustomer("David Wilson", 29, "654 Maple Dr, Denver", "+1-555-5005");
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
        try {
            boolean running = true;
            while(running){
                displayMenu();
                String input = scanner.nextLine().trim();
                switch(input){
                    case "1":
                        manageAccounts();
                        break;
                    case "2":
                        performTransactions();
                        break;
                    case "3":
                        generateStatements();
                        break;
                    case "4":
                        runTests();
                        break;
                    case "5":
                        System.out.println("Thank you for using the Bank Account Management System! ");
                        System.out.println("All data saved in memory. Remember to commit your latest changes to Git!");
                        System.out.println("Goodbye!");
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
        } finally {
            scanner.close();
        }
    }

    private void manageAccounts() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("  ACCOUNT MANAGEMENT");
        System.out.println("=".repeat(50));
        System.out.println();
        System.out.println("1. Create New Account");
        System.out.println("2. View Account(s)");
        System.out.println("3. Back to Main Menu");
        System.out.println();
        System.out.print("Enter choice: ");
        
        String choice = scanner.nextLine().trim();
        
        switch(choice) {
            case "1":
                accountController.createAccount();
                break;
            case "2":
                accountController.viewAllAccounts();
                break;
            case "3":
                return;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private void performTransactions() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("  TRANSACTION MANAGEMENT");
        System.out.println("=".repeat(50));
        System.out.println();
        System.out.println("1. Record New Transaction");
        System.out.println("2. View Transaction History");
        System.out.println("3. Back to Main Menu");
        System.out.println();
        System.out.print("Enter choice: ");
        
        String choice = scanner.nextLine().trim();
        
        switch(choice) {
            case "1":
                transactionController.recordTransaction();
                break;
            case "2":
                transactionController.viewTransactionHistory();
                break;
            case "3":
                return;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private void generateStatements() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("  ACCOUNT STATEMENT GENERATION");
        System.out.println("=".repeat(50));
        System.out.println();
        System.out.println("1. Full Account Statement");
        System.out.println("2. Mini Statement");
        System.out.println("3. Back to Main Menu");
        System.out.println();
        System.out.print("Enter choice: ");
        
        String choice = scanner.nextLine().trim();
        
        switch(choice) {
            case "1":
                generateFullStatement();
                break;
            case "2":
                generateMiniStatement();
                break;
            case "3":
                return;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private void generateFullStatement() {
        System.out.print("\nEnter Account Number: ");
        String accountNumber = scanner.nextLine().trim();
        
        Account account = accountManager.findAccount(accountNumber);
        if (account == null) {
            System.out.println("✗ Account not found!");
            return;
        }
        
        statementGenerator.generateStatement(account);
    }

    private void generateMiniStatement() {
        System.out.print("\nEnter Account Number: ");
        String accountNumber = scanner.nextLine().trim();
        
        Account account = accountManager.findAccount(accountNumber);
        if (account == null) {
            System.out.println("✗ Account not found!");
            return;
        }
        
        statementGenerator.generateMiniStatement(account);
    }

    private void runTests() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("  TEST EXECUTION");
        System.out.println("=".repeat(50));
        System.out.println();
        System.out.println("This feature runs the JUnit test suite.");
        System.out.println();
        System.out.println("To run tests, please execute:");
        System.out.println("  mvn test");
        System.out.println();
        System.out.println("Or from your IDE:");
        System.out.println("  Right-click on 'src/test/java' → Run Tests");
        System.out.println();
        System.out.println("Current Test Coverage:");
        System.out.println("  • AccountTest: 24 tests");
        System.out.println("  • TransactionManagerTest: 19 tests");
        System.out.println("  • ExceptionTest: 15 tests");
        System.out.println("  • Total: 58 comprehensive test cases");
        System.out.println("=".repeat(50));
    }



private void displayMenu(){
    System.out.println("\n" + "=".repeat(50));
    System.out.println("  BANK ACCOUNT MANAGEMENT - MAIN MENU");
    System.out.println("=".repeat(50));
    System.out.println();
    System.out.println("1. Manage Accounts");
    System.out.println("2. Perform Transactions");
    System.out.println("3. Generate Account Statements");
    System.out.println("4. Run Tests");
    System.out.println("5. Exit");
    System.out.println();
    System.out.print("Enter choice: ");
}
}
