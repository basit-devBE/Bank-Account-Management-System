package utils;

import java.util.Scanner;


public class ValidationUtils {
   public  enum  InputType{
        String,
        Integer
    }
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

    public static boolean validateInput(String input, InputType type) {
        switch (type) {
            case String:
                return input != null && !input.trim().isEmpty();
            case Integer:
                try {
                    Integer.parseInt(input.trim());
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            default:
                return false;
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
    
    public static boolean isValidManagerId(String managerId) {
        return managerId != null && managerId.matches("MGR\\d{3}");
    }
    
    public static String getAccountNumberInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toUpperCase();
            if (isValidAccountNumber(input)) {
                return input;
            }
            System.out.println("✗ Invalid account number. Format: ACC### (e.g., ACC001)");
        }
    }
    
    public static String getManagerIdInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toUpperCase();
            if (isValidManagerId(input)) {
                return input;
            }
            System.out.println("✗ Invalid manager ID. Format: MGR### (e.g., MGR001)");
        }
    }
    
    public static boolean isPositiveAmount(double amount) {
        return amount > 0;
    }
    
    public static boolean isValidAge(int age) {
        return age >= 18 && age <= 120;
    }
    
    public static boolean isValidName(String name) {
        // Name should contain only letters, spaces, hyphens, and apostrophes
        // Must be at least 2 characters and start with a letter
        return name != null && 
               name.matches("^[A-Za-z][A-Za-z\\s'-]{1,}$") &&
               name.trim().length() >= 2;
    }
    
    public static boolean isValidContact(String contact) {
        // Contact should contain digits, spaces, hyphens, parentheses, and plus sign
        // Must have at least 7 digits
        return contact != null && 
               contact.matches("^[+\\d\\s()-]{7,}$") &&
               contact.replaceAll("[^\\d]", "").length() >= 7;
    }
}
