import java.util.Scanner;

import com.bank.controllers.Account;
class Main{
    public static void main(String[] args) {
       // main.start();
    Account account = new Account();

        Scanner scanner = new Scanner(System.in);
       System.out.println("Welcome to the Bank Account Management System");
       System.out.println("1. Create Account");
       String input = scanner.nextLine();
       if(input.equals("1")){
        System.out.println("Create Account");
        account.createAccount();
       }
       scanner.close();
    }

    
}