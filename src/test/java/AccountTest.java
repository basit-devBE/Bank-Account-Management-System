package test.java;

import models.*;
import models.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Account Tests")
class AccountTest {

    private SavingsAccount savingsAccount;
    private CheckingAccount checkingAccount;
    private Customer regularCustomer;
    private Customer premiumCustomer;

    @BeforeEach
    void setUp() {
        regularCustomer = new RegularCustomer("John Doe", 30, "123 Main St", "555-1234");
        premiumCustomer = new PremiumCustomer("Jane Smith", 35, "456 Oak Ave", "555-5678");
        savingsAccount = new SavingsAccount("SAV001", regularCustomer, 1000.0);
        checkingAccount = new CheckingAccount("CHK001", premiumCustomer, 2000.0);
    }

    // ========== ACCOUNT CREATION TESTS ==========

    @Test
    @DisplayName("Should create savings account with correct initial values")
    void testCreateSavingsAccount() {
        assertEquals("SAV001", savingsAccount.getAccountNumber());
        assertEquals(1000.0, savingsAccount.getBalance(), 0.01);
        assertEquals("Active", savingsAccount.getStatus());
        assertEquals("Savings", savingsAccount.getAccountType());
        assertEquals(regularCustomer, savingsAccount.getCustomer());
    }

    @Test
    @DisplayName("Should create checking account with correct initial values")
    void testCreateCheckingAccount() {
        assertEquals("CHK001", checkingAccount.getAccountNumber());
        assertEquals(2000.0, checkingAccount.getBalance(), 0.01);
        assertEquals("Active", checkingAccount.getStatus());
        assertEquals("Checking", checkingAccount.getAccountType());
        assertEquals(premiumCustomer, checkingAccount.getCustomer());
    }

    @Test
    @DisplayName("Should increment account counter on creation")
    void testAccountCounter() {
        int initialCount = Account.getAccountCounter();
        new SavingsAccount("SAV002", regularCustomer, 500.0);
        assertEquals(initialCount + 1, Account.getAccountCounter());
    }

    // ========== GETTERS AND SETTERS TESTS ==========

    @Test
    @DisplayName("Should get and set account number")
    void testAccountNumber() {
        savingsAccount.setAccountNumber("SAV999");
        assertEquals("SAV999", savingsAccount.getAccountNumber());
    }

    @Test
    @DisplayName("Should get and set balance")
    void testBalance() {
        savingsAccount.setBalance(5000.0);
        assertEquals(5000.0, savingsAccount.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Should get and set status")
    void testStatus() {
        savingsAccount.setStatus("Inactive");
        assertEquals("Inactive", savingsAccount.getStatus());
    }

    @Test
    @DisplayName("Should get and set customer")
    void testCustomer() {
        savingsAccount.setCustomer(premiumCustomer);
        assertEquals(premiumCustomer, savingsAccount.getCustomer());
        assertEquals(premiumCustomer, savingsAccount.getAccountHolder()); // Test backward compatibility
    }

    // ========== ACCOUNT TYPE SPECIFIC TESTS ==========

    @Test
    @DisplayName("Savings account should have interest rate and minimum balance")
    void testSavingsAccountProperties() {
        assertEquals(0.035, savingsAccount.getInterestRate(), 0.001);
        assertEquals(500.0, savingsAccount.getMinimumBalance(), 0.01);
    }

    @Test
    @DisplayName("Checking account should have overdraft limit and monthly fee")
    void testCheckingAccountProperties() {
        assertEquals(1000.0, checkingAccount.getOverdraftLimit(), 0.01);
        assertEquals(10.0, checkingAccount.getMonthlyFee(), 0.01);
    }

    @Test
    @DisplayName("Should update savings account interest rate")
    void testUpdateInterestRate() {
        savingsAccount.setInterestRate(0.05);
        assertEquals(0.05, savingsAccount.getInterestRate(), 0.001);
    }

    @Test
    @DisplayName("Should update checking account overdraft limit")
    void testUpdateOverdraftLimit() {
        checkingAccount.setOverdraftLimit(1500.0);
        assertEquals(1500.0, checkingAccount.getOverdraftLimit(), 0.01);
    }

    // ========== DEPOSIT TESTS ==========

    @Test
    @DisplayName("Should deposit valid amount in savings account")
    void testDepositSavings() throws InvalidAmountException {
        savingsAccount.deposit(500.0);
        assertEquals(1500.0, savingsAccount.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Should deposit valid amount in checking account")
    void testDepositChecking() throws InvalidAmountException {
        checkingAccount.deposit(1000.0);
        assertEquals(3000.0, checkingAccount.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Should throw InvalidAmountException for negative deposit")
    void testNegativeDeposit() {
        assertThrows(InvalidAmountException.class, () -> {
            savingsAccount.deposit(-100.0);
        });
    }

    @Test
    @DisplayName("Should throw InvalidAmountException for zero deposit")
    void testZeroDeposit() {
        assertThrows(InvalidAmountException.class, () -> {
            savingsAccount.deposit(0.0);
        });
    }

    // ========== WITHDRAWAL TESTS ==========

    @Test
    @DisplayName("Should withdraw valid amount from savings account")
    void testWithdrawSavings() throws InvalidAmountException, InsufficientfundsException, OverdraftExceededException {
        savingsAccount.withdraw(200.0);
        assertEquals(800.0, savingsAccount.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Should withdraw valid amount from checking account")
    void testWithdrawChecking() throws InvalidAmountException, InsufficientfundsException, OverdraftExceededException {
        checkingAccount.withdraw(500.0);
        assertEquals(1500.0, checkingAccount.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Should throw InvalidAmountException for negative withdrawal")
    void testNegativeWithdrawal() {
        assertThrows(InvalidAmountException.class, () -> {
            savingsAccount.withdraw(-100.0);
        });
    }

    @Test
    @DisplayName("Should throw InvalidAmountException for zero withdrawal")
    void testZeroWithdrawal() {
        assertThrows(InvalidAmountException.class, () -> {
            savingsAccount.withdraw(0.0);
        });
    }

    // ========== STRING REPRESENTATION TESTS ==========

    @Test
    @DisplayName("Should generate correct toString representation")
    void testToString() {
        String result = savingsAccount.toString();
        assertTrue(result.contains("SAV001"));
        assertTrue(result.contains("John Doe"));
        assertTrue(result.contains("1000.00"));
        assertTrue(result.contains("Active"));
    }

    @Test
    @DisplayName("Should generate account summary")
    void testAccountSummary() {
        String summary = savingsAccount.getAccountSummary();
        assertNotNull(summary);
        assertTrue(summary.contains("SAV001"));
    }

    @Test
    @DisplayName("Should generate account details")
    void testAccountDetails() {
        String details = savingsAccount.getAccountDetails();
        assertNotNull(details);
        assertTrue(details.contains("SAV001"));
        assertTrue(details.contains("John Doe"));
        assertTrue(details.contains("1,000.00"));
    }

    @Test
    @DisplayName("Should generate creation message for savings account")
    void testSavingsCreationMessage() {
        String message = savingsAccount.getCreationMessage();
        assertTrue(message.contains("Account created successfully"));
        assertTrue(message.contains("SAV001"));
        assertTrue(message.contains("3.5%")); // Interest rate
        assertTrue(message.contains("500.00")); // Minimum balance
    }

    @Test
    @DisplayName("Should generate creation message for checking account")
    void testCheckingCreationMessage() {
        String message = checkingAccount.getCreationMessage();
        assertTrue(message.contains("Account created successfully"));
        assertTrue(message.contains("CHK001"));
        assertTrue(message.contains("Overdraft Limit")); // Overdraft limit text
        assertTrue(message.contains("Monthly Fee")); // Monthly fee text
    }
}
