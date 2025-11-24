# 🏦 MyBankUML: Banking System Simulation (GUI Version)

Welcome to **MyBankUML**, a Java-based banking application featuring a complete Graphical User Interface (GUI). This system simulates core banking operations for Customers, Tellers, and Administrators using a role-based architecture.

Currently, the application features a **fully interactive UI** connected to a **Mock Facade (UIManager)**, allowing you to navigate all workflows without a live database connection.

## 📌 Current Features

### 🎨 Graphical User Interface (Swing)
- **Customer Interface:**
  - Dashboard with Chequing & Savings views.
  - Transaction history exploration.
  - Fund transfer workflows with status confirmation.
- **Teller Interface:**
  - Customer & Account Search (by Name, ID, Place of Birth).
  - Account Management (Deposit, Withdraw, Transfer).
  - Create new Customer profiles.
  - Review & Approve pending transactions.
- **Admin Interface:**
  - Manage Employees (Create, Remove, Activate).
  - Search Employee records.
  - System-wide transaction oversight.

### 🏗 Architecture
- **Facade Pattern:** All UI actions route through `UIManager`.
- **Role-Based Access Control:** Distinct dashboards for different user types.
- **Standard Java Structure:** Organized in `src/main/java` packages.
- **Persistence:** SQLite database (`bank.db`) stores all users, accounts, and transactions.

---

## 🚀 How to Run

### Prerequisites
- **Java Development Kit (JDK) 21** or higher.
- **SQLite JDBC Driver** (Included in `libs/` folder).

### Step 1: Clone or Download
Navigate to the project root folder (`MyBankUML`).

### Step 2: Compile
Run the following command from the root folder to compile all source files:

```bash
javac -cp "libs/*" -sourcepath src/main/java src/main/java/bank/Main.java
```
### Step 3: Run
Launch the application:
```bash
java -cp "src/main/java:libs/*" bank.Main  
```