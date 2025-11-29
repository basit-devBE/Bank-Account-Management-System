# Bank Account Management System

A console-based Java application that simulates a comprehensive bank management system with support for multiple account types, transaction processing, and role-based access control.

## Project Overview

This project demonstrates core object-oriented programming concepts through a banking system that allows users to create accounts, perform financial transactions, and manage customer data. The system distinguishes between regular customers, premium customers, and bank managers, each with different privileges and transaction limits.

---

## Project Structure

```
src/
├── Main.java                          # Application entry point
├── com/bank/
│   ├── controllers/
│   │   ├── MenuController.java        # Main menu and application flow
│   │   ├── AccountController.java     # Account creation and viewing
│   │   └── TransactionController.java # Transaction processing
│   ├── interfaces/
│   │   └── AccountOperations.java     # Core banking operations interface
│   ├── models/
│   │   ├── Account.java               # Abstract base account class
│   │   ├── SavingsAccount.java        # Savings account implementation(extends from Base Account class)
│   │   ├── CheckingAccount.java       # Checking account implementation(extends from Base Account class)
│   │   ├── Customer.java              # Customer/Manager entity
│   │   ├── Transaction.java           # Transaction record
│   │   └── enums/
│   │       ├── AccountType.java       # SAVINGS, CHECKING
│   │       ├── CustomerType.java      # REGULAR, PREMIUM
│   │       ├── Role.java              # CUSTOMER, MANAGER
│   │       └── TransactionType.java   # DEPOSIT, WITHDRAW
│   └── repository/
│       ├── AccountManager.java        # Account storage and retrieval
│       ├── CustomerManager.java       # Customer storage
│       └── TransactionManager.java    # Transaction history management
```

---

## Core Functionalities and Code

### 1. Account Management

#### Account Creation
- **Supported Account Types**: Savings and Checking accounts
- **Customer Types**: Regular and Premium customers
- **Auto-generated IDs**: Accounts receive unique UUID-based account numbers
- **Minimum Deposit Requirements**: 
  - Premium customers: $10,000 minimum initial deposit
  - Regular customers: No minimum

**Key Classes:**
- `AccountController.java` - Handles account creation workflow
- `Account.java` - Abstract base class with common account properties
- `SavingsAccount.java` - Savings-specific implementation
- `CheckingAccount.java` - Checking-specific implementation

#### Account Viewing
- **Customer View**: View individual account details by account number
- **Manager View**: View all accounts in the system (requires Manager ID starting with "MGR")
- **Account Summary Display**: Includes account number, customer name, type, balance, and status

### 2. Account Types Features

#### Savings Account
- **Interest Rate**: 3.5% annual (applied monthly)
- **Minimum Balance**: $500 (enforced for regular customers)
- **Premium Benefit**: No minimum balance requirement for premium customers
- **Interest Calculation**: `monthlyInterest = balance × (0.035 / 12)`
  
## NB: These features were not implement in the code since this is a console project and is short lived 

#### Checking Account
- **Overdraft Limit**: $1,000 (allows negative balance up to limit)
- **Monthly Fee**: $10 (waived for premium customers)
- **Overdraft Protection**: Allows withdrawals beyond current balance
- **Fee Structure**: Premium customers exempt from monthly fees

### 3. Transaction Management

#### Transaction Types
- **Deposits**: Add funds to account with instant processing
- **Withdrawals**: Remove funds with validation checks

#### Transaction Features
- **Unique Transaction IDs**: Format `TXN{timestamp}`
- **Transaction Confirmation**: Displays preview before processing
- **Status Tracking**: PENDING, COMPLETED, FAILED
- **Balance Validation**: Prevents insufficient fund withdrawals
- **Transaction History**: Complete audit trail with timestamps

**Transaction Flow:**
1. Account lookup by account number
2. Transaction type selection (deposit/withdrawal)
3. Amount entry and validation
4. Confirmation display with current/new balance
5. User confirmation (Y/N)
6. Transaction execution and recording

### 4. Customer & Role Management

#### Customer Types
- **Regular Customers**:
  - Transaction limit: $10,000
  - Subject to minimum balance requirements
  - Subject to monthly fees on checking accounts
  
- **Premium Customers**:
  - Transaction limit: $50,000
  - Minimum initial deposit: $10,000
  - No minimum balance requirements on savings
  - Waived monthly fees on checking accounts

#### Roles
- **Customer (CUSTOMER)**:
  - Can view own accounts
  - Can perform transactions within limits
  - Auto-assigned ID format: `CUST{5-digit number}`
  
- **Manager (MANAGER)**:
  - Can view all accounts
  - Can view all transaction history
  - Unlimited transaction limit
  - Auto-assigned ID format: `MGR{5-digit number}`

### 5. Repository Pattern Implementation

#### AccountManager
- **Storage**: Array-based with dynamic resizing
- **Initial Capacity**: 50 accounts
- **Operations**:
  - `addAccount()` - Add new account
  - `findAccount()` - Retrieve by account number
  - `viewAllAccounts()` - Display formatted account listing
  - `getTotalBalance()` - Calculate total bank balance

#### CustomerManager
- **Storage**: Array-based customer repository
- **Auto-resizing**: Doubles capacity when full
- **Customer Tracking**: Maintains customer records

#### TransactionManager
- **Storage**: Array-based transaction log (initial: 100 transactions)
- **Operations**:
  - `addTransaction()` - Record new transaction
  - `viewAllTransactions()` - Manager-level view
  - `viewTransactionsByAccount()` - Account-specific history

---

## Architecture & Design Patterns

### Separation of Concerns (MVC-Inspired Pattern)

This project implements a **layered architecture** that separates concerns into distinct layers, making the code maintainable, testable, and scalable. The architecture follows a modified MVC (Model-View-Controller) pattern adapted for console applications.

#### **1. Models Layer** (`com/bank/models/`)
**Responsibility**: Represents the domain entities and business logic

**What Models Do:**
- Define the data structure and state of business entities
- Encapsulate business rules and validation logic
- Contain no knowledge of how data is stored or presented
- Implement core business operations (e.g., `withdraw()`, `deposit()`)

**Key Models:**
- `Account.java` - Abstract base class for all accounts with common properties and operations
- `SavingsAccount.java` / `CheckingAccount.java` - Specific account implementations with unique rules
- `Customer.java` - Represents customer/manager entities with role-based attributes
- `Transaction.java` - Immutable transaction records with status tracking

**Example**: `SavingsAccount` enforces minimum balance rules - this is business logic that belongs in the model, not in controllers or repositories.

```java
// Business logic in the model
public void withdraw(double amount) {
    if ((currentBalance - amount) >= MIN_BALANCE) {
        super.withdraw(amount);
    } else {
        // Business rule enforcement
    }
}
```

#### **2. Controllers Layer** (`com/bank/controllers/`)
**Responsibility**: Handles user interaction and application flow

**What Controllers Do:**
- Receive user input from the console
- Validate and sanitize input data
- Coordinate between repositories and models
- Control application workflow and navigation
- Format and display output to users
- **Do NOT contain business logic** - delegate to models

**Key Controllers:**
- `MenuController.java` - Main application flow and menu navigation
- `AccountController.java` - Handles account creation and viewing workflows
- `TransactionController.java` - Manages transaction processing workflows

**Example**: `AccountController` doesn't know how to store accounts - it delegates to `AccountManager`:

```java
// Controller coordinates but doesn't implement storage
public void createAccount() {
    // 1. Get user input
    // 2. Create model object
    SavingsAccount newAccount = new SavingsAccount(accountNumber, holder, deposit);
    // 3. Delegate storage to repository
    accountManager.addAccount(newAccount);
    // 4. Display result to user
}
```

#### **3. Repository Layer** (`com/bank/repository/`)
**Responsibility**: Manages data storage, retrieval, and persistence

**What Repositories Do:**
- Abstract away data storage implementation details
- Provide CRUD operations (Create, Read, Update, Delete)
- Manage collections of domain objects
- Handle data access logic (searching, filtering, aggregating)
- **Do NOT contain business logic** - only data operations

**Key Repositories:**
- `AccountManager.java` - Stores and retrieves accounts
- `CustomerManager.java` - Manages customer records
- `TransactionManager.java` - Maintains transaction history

**Example**: `AccountManager` handles storage without knowing business rules:

```java
// Repository only handles data operations
public void addAccount(Account account) {
    if (accountCount >= accounts.length) {
        resizeArray(); // Infrastructure concern
    }
    accounts[accountCount++] = account; // Storage operation
}
```

### Why This Separation Matters

#### **1. Single Responsibility Principle**
Each layer has ONE clear responsibility:
- **Models** = Business logic and domain rules
- **Controllers** = User interaction and flow control
- **Repositories** = Data storage and retrieval

#### **2. Maintainability**
- Changing storage (e.g., from arrays to database) only affects repositories
- Changing business rules only affects models
- Changing UI (e.g., from console to GUI) only affects controllers

#### **3. Testability**
- Can test business logic in models independently
- Can test data operations without UI
- Can mock repositories to test controllers

#### **4. Scalability**
- Easy to add new account types (extend models)
- Easy to add new operations (add controller methods)
- Easy to switch storage mechanisms (modify repositories)

#### **5. Code Reusability**
- Models can be used by different controllers
- Repositories can serve multiple controllers
- Same model can have different presentation formats

### Data Flow Example: Creating an Account

```
User Input (Console)
       ↓
MenuController.start() 
       ↓
AccountController.createAccount()
       ↓ (gather input & validate)
Create Model: new SavingsAccount(...)
       ↓ (business logic in model constructor)
AccountManager.addAccount(account)
       ↓ (storage logic in repository)
Data Stored in Array
       ↓
Return to Controller
       ↓
Display Success Message to User
```

**Notice how each layer only talks to adjacent layers:**
- Controllers use Repositories and Models
- Models are independent (no dependencies)
- Repositories store Models (no UI knowledge)

---

## OOP Concepts Applied

### 1. **Abstraction**
- `Account` abstract class defines template for all account types
- `AccountOperations` interface defines contract for account operations
- Hiding implementation details while exposing necessary functionality

### 2. **Inheritance**
- `SavingsAccount` and `CheckingAccount` extend `Account` base class
- Inherits common properties: accountNumber, accountHolder, balance
- Demonstrates IS-A relationship (SavingsAccount IS-A Account)

### 3. **Polymorphism**
- **Method Overriding**: `withdraw()` method overridden in both account types
  - `SavingsAccount`: Enforces minimum balance for regular customers
  - `CheckingAccount`: Allows overdraft up to limit
- **Interface Implementation**: `Account` implements `AccountOperations` interface
- Runtime polymorphism through account type-specific behavior

### 4. **Encapsulation**
- Private fields with public getters (e.g., `accountNumber`, `balance`)
- Data hiding through access modifiers
- Controlled access to object state through methods
- Example: Balance can only be modified via `deposit()` and `withdraw()`

### 5. **Composition**
- `Account` HAS-A `Customer` (accountHolder)
- `Transaction` HAS-A `Account`
- `MenuController` composes `AccountController` and `TransactionController`

### 6. **Enum Usage**
- Type-safe constants for:
  - `AccountType` (SAVINGS, CHECKING)
  - `CustomerType` (REGULAR, PREMIUM)
  - `Role` (CUSTOMER, MANAGER)
  - `TransactionType` (DEPOSIT, WITHDRAW)
  - `TransactionStatus` (PENDING, COMPLETED, FAILED)

### 7. **Static Members**
- `static int customerCount` in `Customer` class ensures unique sequential IDs
- Class-level variables shared across all instances
- Used for maintaining system-wide state

### 8. **Constructor Overloading & Chaining**
- Account constructors initialize common properties
- Subclass constructors call `super()` to initialize parent class

---

## Lessons Learned

### 1. **Static vs Instance Variables**
**Problem**: Initially, when initializing arrays inside methods, each method call created new objects, and the array didn't retain previously stored objects.

**Solution**: Use `static` keyword or initialize arrays outside methods (in constructors or as instance variables).

**Example from the code:**
```java
// In Customer.java
private static int customerCount = 0; // Shared across all instances

// In AccountManager.java
private Account[] accounts; // Instance variable
private int accountCount;

public AccountManager() {
    this.accounts = new Account[50]; // Initialize in constructor, not in methods
    this.accountCount = 0;
}
```

**Key Insight**: 
- **Static fields** are shared across all class instances (e.g., customer ID counter)
- **Instance fields** are unique to each object but persist across method calls
- Initializing collections in constructors (not methods) prevents data loss

### 2. **Array Management & Dynamic Resizing**
Implemented dynamic array resizing to handle unlimited growth:
```java
private void resizeArray() {
    Account[] newAccounts = new Account[accounts.length * 2];
    System.arraycopy(accounts, 0, newAccounts, 0, accounts.length);
    accounts = newAccounts;
}
```

### 3. **Validation & Business Logic Separation**
Separated validation logic in model classes (e.g., minimum balance checks in `SavingsAccount`) from controller logic, following Single Responsibility Principle.

### 4. **String Formatting for User Experience**
Used `String.format()` for consistent currency and number display:
```java
String.format("$%,.2f", amount) // Outputs: $1,234.56
```

### 5. **Role-Based Access Control**
Implemented simple but effective authorization by checking ID prefixes:
```java
if (!userId.startsWith("MGR")) {
    System.out.println("✗ Access Denied");
    return;
}
```

### 6. **Transaction Confirmation Pattern**
Implemented preview-confirm workflow to prevent accidental operations, improving user experience and data integrity.

---

## How to Run

1. **Compile the project:**
   ```bash
   javac Main.java
   ```

2. **Run the application:**
   ```bash
   java Main
   ```

3. **Initial Setup:**
   - System auto-creates a Manager account on startup
   - Manager ID is displayed (format: MGR00001)
   - Use this ID to access manager-only features

---

## System Features Summary

| Feature | Regular Customer | Premium Customer | Manager |
|---------|-----------------|------------------|---------|
| Transaction Limit | $10,000 | $50,000 | Unlimited |
| Savings Min Balance | $500 | None | N/A |
| Checking Monthly Fee | $10 | Waived | N/A |
| View All Accounts | ✗ | ✗ | ✓ |
| View All Transactions | ✗ | ✗ | ✓ |
| Overdraft Limit | $1,000 | $1,000 | N/A |

---

## Future Enhancements

- Database integration (replace in-memory arrays)
- User authentication and password management
- Interest calculation automation
- Loan and credit features
- Account statements and reports generation
- Multi-currency support
- Transfer between accounts

---

## Author

**Basit** - [basit-devBE](https://github.com/basit-devBE)

## License

This project is open source and available for educational purposes.