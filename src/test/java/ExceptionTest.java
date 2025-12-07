// Placeholder for ExceptionTest.java
package  test.java;

import models.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;


@DisplayName("Exception Tests")
class ExceptionTest{
    private InvalidAmountException invalidAmountException;
    private InsufficientfundsException insufficientFundsException;
    private OverdraftExceededException overdraftExceededException;

    @BeforeEach
    void setUp() {
        invalidAmountException = new InvalidAmountException("Invalid amount provided.");
        insufficientFundsException = new InsufficientfundsException("Insufficient funds for this operation.");
        overdraftExceededException = new OverdraftExceededException("Overdraft limit exceeded.");
    }

    @Test
    @DisplayName("Should create InvalidAmountException with correct message")
    void testInvalidAmountException() {
        assertEquals("Invalid amount provided.", invalidAmountException.getMessage());
    }

    @Test
    @DisplayName("Should create InsufficientfundsException with correct message")
    void testInsufficientFundsException() {
        assertEquals("Insufficient funds for this operation.", insufficientFundsException.getMessage());
    }

    @Test
    @DisplayName("Should create OverdraftExceededException with correct message")
    void testOverdraftExceededException() {
        assertEquals("Overdraft limit exceeded.", overdraftExceededException.getMessage());
    }
}