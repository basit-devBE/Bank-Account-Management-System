package test.java;

import models.*;
import models.enums.TransactionType;
import services.TransactionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Transaction Manager Tests")
class TransactionManagerTest {

    private TransactionManager transactionManager;
    private Account savingsAccount;
    private Account checkingAccount;
    private Customer customer;

    @BeforeEach
    void setUp() {
        transactionManager = new TransactionManager();
        customer = new RegularCustomer("John Doe", 30, "123 Main St", "555-1234");
        savingsAccount = new SavingsAccount("SAV001", customer, 1000.0);
        checkingAccount = new CheckingAccount("CHK001", customer, 2000.0);
    }

    // ========== TRANSACTION ID GENERATION TESTS ==========

    @Test
    @DisplayName("Should generate transaction ID in correct format")
    void testGenerateTransactionIdFormat() {
        String txnId = transactionManager.generateTransactionId();
        assertTrue(txnId.matches("TXN\\d{3}"));
        assertTrue(txnId.startsWith("TXN"));
    }

    @Test
    @DisplayName("Should generate same ID when no transactions added")
    void testGenerateTransactionIdBeforeAdd() {
        String txn1 = transactionManager.generateTransactionId();
        String txn2 = transactionManager.generateTransactionId();
        
        // Both should be TXN001 because counter only increments on add
        assertEquals("TXN001", txn1);
        assertEquals("TXN001", txn2);
    }

    @Test
    @DisplayName("Should increment transaction ID after adding transaction")
    void testTransactionIdIncrementsAfterAdd() {
        String firstId = transactionManager.generateTransactionId();
        
        Transaction txn = new Transaction(
            savingsAccount,
            100.0,
            TransactionType.DEPOSIT,
            firstId,
            LocalDate.now(),
            1100.0
        );
        
        transactionManager.addTransaction(txn);
        
        String secondId = transactionManager.generateTransactionId();
        assertEquals("TXN001", firstId);
        assertEquals("TXN002", secondId);
    }

    // ========== ADD TRANSACTION TESTS ==========

    @Test
    @DisplayName("Should add transaction successfully")
    void testAddTransaction() {
        Transaction transaction = new Transaction(
            savingsAccount,
            500.0,
            TransactionType.DEPOSIT,
            "TXN001",
            LocalDate.now(),
            1500.0
        );

        assertDoesNotThrow(() -> transactionManager.addTransaction(transaction));
    }

    @Test
    @DisplayName("Should add multiple transactions")
    void testAddMultipleTransactions() {
        for (int i = 1; i <= 5; i++) {
            Transaction txn = new Transaction(
                savingsAccount,
                100.0 * i,
                TransactionType.DEPOSIT,
                "TXN" + String.format("%03d", i),
                LocalDate.now(),
                1000.0 + (100.0 * i)
            );
            transactionManager.addTransaction(txn);
        }

        // Should not throw any exception
        assertTrue(true);
    }

    @Test
    @DisplayName("Should handle different transaction types")
    void testDifferentTransactionTypes() {
        Transaction deposit = new Transaction(
            savingsAccount,
            200.0,
            TransactionType.DEPOSIT,
            "TXN001",
            LocalDate.now(),
            1200.0
        );

        Transaction withdrawal = new Transaction(
            checkingAccount,
            300.0,
            TransactionType.WITHDRAW,
            "TXN002",
            LocalDate.now(),
            1700.0
        );

        transactionManager.addTransaction(deposit);
        transactionManager.addTransaction(withdrawal);

        assertEquals(TransactionType.DEPOSIT, deposit.getTransactionType());
        assertEquals(TransactionType.WITHDRAW, withdrawal.getTransactionType());
    }

    // ========== TRANSACTION STATUS TESTS ==========

    @Test
    @DisplayName("Should create transaction with PENDING status by default")
    void testDefaultTransactionStatus() {
        Transaction transaction = new Transaction(
            savingsAccount,
            100.0,
            TransactionType.DEPOSIT,
            "TXN001",
            LocalDate.now(),
            1100.0
        );

        assertEquals(Transaction.TransactionStatus.PENDING, transaction.status);
    }

    @Test
    @DisplayName("Should update transaction status to COMPLETED")
    void testUpdateStatusToCompleted() {
        Transaction transaction = new Transaction(
            savingsAccount,
            100.0,
            TransactionType.DEPOSIT,
            "TXN001",
            LocalDate.now(),
            1100.0
        );

        transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
        assertEquals(Transaction.TransactionStatus.COMPLETED, transaction.status);
    }

    @Test
    @DisplayName("Should update transaction status to FAILED")
    void testUpdateStatusToFailed() {
        Transaction transaction = new Transaction(
            savingsAccount,
            100.0,
            TransactionType.WITHDRAW,
            "TXN001",
            LocalDate.now(),
            900.0
        );

        transaction.setStatus(Transaction.TransactionStatus.FAILED);
        assertEquals(Transaction.TransactionStatus.FAILED, transaction.status);
    }

    // ========== TRANSACTION PROPERTIES TESTS ==========

    @Test
    @DisplayName("Should store correct transaction amount")
    void testTransactionAmount() {
        Transaction transaction = new Transaction(
            savingsAccount,
            250.50,
            TransactionType.DEPOSIT,
            "TXN001",
            LocalDate.now(),
            1250.50
        );

        assertEquals(250.50, transaction.getAmount(), 0.01);
    }

    @Test
    @DisplayName("Should store correct account reference")
    void testTransactionAccount() {
        Transaction transaction = new Transaction(
            savingsAccount,
            100.0,
            TransactionType.DEPOSIT,
            "TXN001",
            LocalDate.now(),
            1100.0
        );

        assertEquals(savingsAccount, transaction.getAccount());
        assertEquals("SAV001", transaction.getAccount().getAccountNumber());
    }

    @Test
    @DisplayName("Should store correct transaction date")
    void testTransactionDate() {
        LocalDate today = LocalDate.now();
        Transaction transaction = new Transaction(
            savingsAccount,
            100.0,
            TransactionType.DEPOSIT,
            "TXN001",
            today,
            1100.0
        );

        assertEquals(today, transaction.getDate());
    }

    @Test
    @DisplayName("Should store correct balance after transaction")
    void testBalanceAfterTransaction() {
        Transaction transaction = new Transaction(
            savingsAccount,
            100.0,
            TransactionType.DEPOSIT,
            "TXN001",
            LocalDate.now(),
            1100.0
        );

        assertEquals(1100.0, transaction.getBalanceAfter(), 0.01);
    }

    @Test
    @DisplayName("Should store correct transaction ID")
    void testTransactionId() {
        Transaction transaction = new Transaction(
            savingsAccount,
            100.0,
            TransactionType.DEPOSIT,
            "TXN123",
            LocalDate.now(),
            1100.0
        );

        assertEquals("TXN123", transaction.getTransactionId());
    }

    // ========== ARRAY RESIZE TESTS ==========

    @Test
    @DisplayName("Should resize array when capacity is reached")
    void testArrayResize() {
        // Add 101 transactions to trigger resize (initial capacity is 100)
        for (int i = 1; i <= 101; i++) {
            Transaction txn = new Transaction(
                savingsAccount,
                10.0,
                TransactionType.DEPOSIT,
                "TXN" + String.format("%03d", i),
                LocalDate.now(),
                1000.0 + (10.0 * i)
            );
            transactionManager.addTransaction(txn);
        }

        // Should not throw exception
        assertTrue(true);
    }

    // ========== VIEW TRANSACTIONS TESTS ==========

    @Test
    @DisplayName("Should display message when no transactions exist")
    void testViewAllTransactionsEmpty() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        transactionManager.viewAllTransactions();

        String output = outContent.toString();
        assertTrue(output.contains("No transactions found"));

        System.setOut(System.out);
    }

    @Test
    @DisplayName("Should display all transactions when they exist")
    void testViewAllTransactions() {
        Transaction txn1 = new Transaction(
            savingsAccount,
            100.0,
            TransactionType.DEPOSIT,
            "TXN001",
            LocalDate.now(),
            1100.0
        );
        Transaction txn2 = new Transaction(
            checkingAccount,
            50.0,
            TransactionType.WITHDRAW,
            "TXN002",
            LocalDate.now(),
            1950.0
        );

        transactionManager.addTransaction(txn1);
        transactionManager.addTransaction(txn2);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        transactionManager.viewAllTransactions();

        String output = outContent.toString();
        assertTrue(output.contains("TRANSACTION HISTORY"));
        assertTrue(output.contains("TXN001"));
        assertTrue(output.contains("TXN002"));
        assertTrue(output.contains("Total Transactions: 2"));

        System.setOut(System.out);
    }

    @Test
    @DisplayName("Should view transactions by specific account")
    void testViewTransactionsByAccount() {
        Transaction txn1 = new Transaction(
            savingsAccount,
            100.0,
            TransactionType.DEPOSIT,
            "TXN001",
            LocalDate.now(),
            1100.0
        );
        Transaction txn2 = new Transaction(
            checkingAccount,
            50.0,
            TransactionType.WITHDRAW,
            "TXN002",
            LocalDate.now(),
            1950.0
        );
        Transaction txn3 = new Transaction(
            savingsAccount,
            200.0,
            TransactionType.DEPOSIT,
            "TXN003",
            LocalDate.now(),
            1300.0
        );

        transactionManager.addTransaction(txn1);
        transactionManager.addTransaction(txn2);
        transactionManager.addTransaction(txn3);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        transactionManager.viewTransactionsByAccount("SAV001");

        String output = outContent.toString();
        assertTrue(output.contains("SAV001"));
        assertTrue(output.contains("TXN001"));
        assertTrue(output.contains("TXN003"));
        assertFalse(output.contains("TXN002")); // CHK001 transaction shouldn't appear

        System.setOut(System.out);
    }

    @Test
    @DisplayName("Should display message when account has no transactions")
    void testViewTransactionsByAccountNotFound() {
        Transaction txn = new Transaction(
            savingsAccount,
            100.0,
            TransactionType.DEPOSIT,
            "TXN001",
            LocalDate.now(),
            1100.0
        );

        transactionManager.addTransaction(txn);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        transactionManager.viewTransactionsByAccount("CHK999");

        String output = outContent.toString();
        assertTrue(output.contains("No transactions found"));

        System.setOut(System.out);
    }
}
