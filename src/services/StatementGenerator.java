package services;

import models.Account;
import java.time.LocalDate;

public class StatementGenerator {
    
    public void generateStatement(Account account) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("             ACCOUNT STATEMENT");
        System.out.println("=".repeat(60));
        System.out.println("Statement Date: " + LocalDate.now());
        System.out.println();
        account.displayAccountDetails();
        System.out.println("=".repeat(60));
    }
    
    public void generateMiniStatement(Account account) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("           MINI STATEMENT");
        System.out.println("=".repeat(60));
        System.out.println(account.getAccountSummary());
        System.out.println("=".repeat(60));
    }
}
