1. Main

Create Bank and MenuController objects

Run menu loop that calls controller methods

2. Controller / Menu

showMenu(), handleChoice()

Calls Bank methods for create, deposit, withdraw, transfer, close, statements, monthlyProcess()

3. Bank

Fields:

Map<String, Account> accounts

Map<String, Customer> customers
Methods:

createAccount(customerId, accountType, initialDeposit)

getAccount(accountNumber)

closeAccount(accountNumber)

transfer(from, to, amount)

applyMonthlyProcessing() (fees, interest)

listAccounts()

4. Customer (composition)

Class Customer
Fields:

customerId

name

type (enum: REGULAR, PREMIUM)

transactionLimit (apply higher for PREMIUM)

waiveFees (boolean for PREMIUM)

other contact details
Methods:

getters, possibly upgradeToPremium()

5. Base Account (parent)

Class Account
Fields:

accountNumber

Customer owner

balance

List<Transaction> transactions
Methods:

deposit(amount)

withdraw(amount) — checks rules in subclasses

getBalance()

addTransaction(...)

monthlyProcess() — abstract or default no-op

6. SavingsAccount extends Account

Extra fields:

static final double INTEREST_RATE = 0.035 (3.5% annually)

static final double MIN_BALANCE = 500.0
Behavior:

withdraw must enforce balance - amount >= MIN_BALANCE unless owner is PREMIUM and different rule applied

monthlyProcess() applies pro-rated monthly interest to balance

track if below min balance and possibly apply restriction

7. CheckingAccount extends Account

Extra fields:

double overdraftLimit = 1000.0

double monthlyFee = 10.0
Behavior:

withdraw allows balance - amount >= -overdraftLimit

monthlyProcess() charges monthlyFee unless owner.waiveFees == true (premium)

if balance < 0 and beyond overdraftLimit reject

8. Transaction

Fields:

id, type (DEPOSIT, WITHDRAW, TRANSFER), amount, date, balanceAfter
Methods:

create records on each operation

9. Business rules / interactions

Account always references Customer to read customer type (REGULAR or PREMIUM)

Premium customers: higher transaction limits, waived monthly fees, relaxed min-balance enforcement where specified, priority service flag

Monthly processing sequence in Bank.applyMonthlyProcessing():

For each account call monthlyProcess() which:

Savings: add interest monthly = balance * (INTEREST_RATE/12)

Checking: charge monthlyFee unless waived

Persist transactions for interest and fees

10. Validation & errors

All public methods return success/fail with clear error messages (insufficient funds, overdraft exceeded, min balance violation, daily transaction limit reached)

Enforce transaction limits based on Customer.type