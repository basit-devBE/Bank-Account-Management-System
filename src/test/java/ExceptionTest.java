package test.java;

import models.*;
import models.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Exception Tests")
class ExceptionTest {
    private SavingsAccount savingsAccount;
    private CheckingAccount checkingAccount;
    private Customer regularCustomer;
    private Customer premiumCustomer;

    @BeforeEach
    void setUp() {
        regularCustomer = new RegularCustomer("John Doe", 30, "123 Main St", "555-1234");
        premiumCustomer = new PremiumCustomer("Jane Smith", 35, "456 Oak Ave", "555-5678");
        savingsAccount = new SavingsAccount("SAV001", regularCustomer, 1000.0);
        checkingAccount = new CheckingAccount("CHK001", premiumCustomer, 500.0);
    }

    // ========== INVALID AMOUNT EXCEPTION TESTS ==========

    @Test
    @DisplayName("Should throw InvalidAmountException for negative deposit in savings account")
    void testNegativeDepositSavings() {
        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> {
            savingsAccount.deposit(-100.0);
        });
        assertEquals("Deposit amount must be positive.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidAmountException for zero deposit in savings account")
    void testZeroDepositSavings() {
        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> {
            savingsAccount.deposit(0.0);
        });
        assertEquals("Deposit amount must be positive.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidAmountException for negative withdrawal in savings account")
    void testNegativeWithdrawalSavings() {
        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> {
            savingsAccount.withdraw(-50.0);
        });
        assertEquals("Withdrawal amount must be positive.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidAmountException for zero withdrawal in savings account")
    void testZeroWithdrawalSavings() {
        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> {
            savingsAccount.withdraw(0.0);
        });
        assertEquals("Withdrawal amount must be positive.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidAmountException for negative deposit in checking account")
    void testNegativeDepositChecking() {
        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> {
            checkingAccount.deposit(-200.0);
        });
        assertEquals("Deposit amount must be positive.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidAmountException for negative withdrawal in checking account")
    void testNegativeWithdrawalChecking() {
        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> {
            checkingAccount.withdraw(-75.0);
        });
        assertEquals("Withdrawal amount must be positive.", exception.getMessage());
    }

    // ========== INSUFFICIENT FUNDS EXCEPTION TESTS ==========

    @Test
    @DisplayName("Should throw InsufficientfundsException when withdrawing more than balance in savings")
    void testInsufficientFundsSavings() {
        InsufficientfundsException exception = assertThrows(InsufficientfundsException.class, () -> {
            savingsAccount.withdraw(2000.0); // More than balance
        });
        assertTrue(exception.getMessage().contains("Insufficient funds"));
    }

    @Test
    @DisplayName("Should throw InsufficientfundsException when balance would go below minimum in savings")
    void testBelowMinimumBalanceSavings() {
        InsufficientfundsException exception = assertThrows(InsufficientfundsException.class, () -> {
            savingsAccount.withdraw(600.0); // Would leave 400, below 500 minimum
        });
        assertTrue(exception.getMessage().contains("minimum balance"));
    }

    @Test
    @DisplayName("Premium customer should not throw exception for below minimum balance in savings")
    void testPremiumCustomerBelowMinimum() throws Exception {
        SavingsAccount premiumSavings = new SavingsAccount("SAV002", premiumCustomer, 1000.0);
        // Premium customers can go below minimum balance
        premiumSavings.withdraw(600.0); // Leaves 400, below 500 minimum - should work
        assertEquals(400.0, premiumSavings.getBalance(), 0.01);
    }

    // ========== OVERDRAFT EXCEEDED EXCEPTION TESTS ==========

    @Test
    @DisplayName("Should throw OverdraftExceededException when exceeding overdraft limit")
    void testOverdraftExceeded() {
        OverdraftExceededException exception = assertThrows(OverdraftExceededException.class, () -> {
            // Checking account has 500 balance and 1000 overdraft limit
            // Withdrawing 1600 would require 1100 overdraft, exceeding the limit
            checkingAccount.withdraw(1600.0);
        });
        assertTrue(exception.getMessage().contains("Overdraft") || 
                   exception.getMessage().contains("overdraft"));
    }

    @Test
    @DisplayName("Should allow withdrawal within overdraft limit")
    void testWithdrawalWithinOverdraftLimit() throws Exception {
        // Checking account has 500 balance and 1000 overdraft limit
        checkingAccount.withdraw(1200.0); // Requires 700 overdraft, within limit
        assertEquals(-700.0, checkingAccount.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Should throw OverdraftExceededException at exact overdraft limit plus one")
    void testExactOverdraftLimitExceeded() {
        OverdraftExceededException exception = assertThrows(OverdraftExceededException.class, () -> {
            // Balance: 500, Overdraft limit: 1000
            // Max withdrawal: 1500, trying 1500.01
            checkingAccount.withdraw(1500.01);
        });
        assertTrue(exception.getMessage().contains("Overdraft") || 
                   exception.getMessage().contains("overdraft"));
    }

    // ========== EXCEPTION MESSAGE VALIDATION TESTS ==========

    @Test
    @DisplayName("InvalidAmountException should contain descriptive message")
    void testInvalidAmountExceptionMessage() {
        InvalidAmountException exception = new InvalidAmountException("Amount must be positive");
        assertEquals("Amount must be positive", exception.getMessage());
        assertNotNull(exception.getMessage());
    }

    @Test
    @DisplayName("InsufficientfundsException should contain descriptive message")
    void testInsufficientFundsExceptionMessage() {
        InsufficientfundsException exception = new InsufficientfundsException("Not enough funds");
        assertEquals("Not enough funds", exception.getMessage());
        assertNotNull(exception.getMessage());
    }

    @Test
    @DisplayName("OverdraftExceededException should contain descriptive message")
    void testOverdraftExceededExceptionMessage() {
        OverdraftExceededException exception = new OverdraftExceededException("Limit exceeded");
        assertEquals("Limit exceeded", exception.getMessage());
        assertNotNull(exception.getMessage());
    }
}