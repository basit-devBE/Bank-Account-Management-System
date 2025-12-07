package utils;

import java.util.Scanner;

public class ValidationUtils {
    
    public static int getIntInput(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int input = Integer.parseInt(scanner.nextLine().trim());
                if (input >= min && input <= max) {
                    return input;
                }
                System.out.println("✗ Please enter a number between " + min + " and " + max);
            } catch (NumberFormatException e) {
                System.out.println("✗ Invalid input. Please enter a valid number.");
            }
        }
    }
    
    public static double getDoubleInput(Scanner scanner, String prompt, double min) {
        while (true) {
            System.out.print(prompt);
            try {
                double input = Double.parseDouble(scanner.nextLine().trim().replace("$", ""));
                if (input >= min) {
                    return input;
                }
                System.out.println("✗ Amount must be at least $" + String.format("%.2f", min));
            } catch (NumberFormatException e) {
                System.out.println("✗ Invalid input. Please enter a valid amount.");
            }
        }
    }
    
    public static String getStringInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    public static boolean getConfirmation(Scanner scanner, String prompt) {
        System.out.print(prompt + " (Y/N): ");
        return scanner.nextLine().trim().equalsIgnoreCase("Y");
    }
    
    public static boolean isValidAccountNumber(String accountNumber) {
        return accountNumber != null && accountNumber.matches("ACC\\d{3}");
    }
    
    public static boolean isValidTransactionId(String transactionId) {
        return transactionId != null && transactionId.matches("TXN\\d{3}");
    }
    
    public static boolean isValidCustomerId(String customerId) {
        return customerId != null && (customerId.matches("CUST\\d{5}") || customerId.matches("MGR\\d{5}"));
    }
    
    public static boolean isPositiveAmount(double amount) {
        return amount > 0;
    }
    
    public static boolean isValidAge(int age) {
        return age >= 18 && age <= 120;
    }
}
