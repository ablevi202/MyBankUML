package bank;

import java.util.UUID; // Required for generating unique Transaction IDs

public class UIManager {
    private DatabaseManager dbManager; // Reference to the Database
    private String currentUser; // Stores the currently logged-in username

    public UIManager() {
        // 1. Initialize the Database connection
        this.dbManager = new DatabaseManager();
        
        // 2. Seed Data (So you can login immediately)
        seedDummyData();
    }

    private void seedDummyData() {
        // Create default users
        dbManager.saveUser("test", "123", "CUSTOMER", "Test Customer");
        dbManager.saveUser("teller", "123", "TELLER", "Branch Teller");
        dbManager.saveUser("admin", "123", "ADMIN", "System Administrator");

        // --- NEW: Seed a real account so you can test Deposit/Withdrawal ---
        // Account ID: 853013 (matches your UI mock data), Balance: $5000.00
        dbManager.saveAccount("853013", "test", "Chequing", 5000.00);
    }

    // --- AUTHENTICATION ---

    public boolean handleLogin(String username, String password) {
        if (dbManager.validateLogin(username, password)) {
            this.currentUser = username;
            return true;
        }
        return false;
    }

    public String getDashboardType(String username) {
        if (username.startsWith("admin")) return "ADMIN";
        if (username.startsWith("teller")) return "TELLER";
        return "CUSTOMER";
    }

    public String getCurrentUserRole() {
        if (currentUser == null) return "";
        return getDashboardType(currentUser);
    }

    public String getCurrentUserName() {
        return currentUser != null ? currentUser : "User"; 
    }

    public void logout() {
        this.currentUser = null;
        System.out.println("User logged out.");
    }

    // --- REAL DATA OPERATIONS (Updated) ---

    public void createCustomerAccount(String name, String dob, String type) {
        // 1. Generate a simple username (in real app, check for duplicates)
        String username = name.toLowerCase().replace(" ", "");
        
        // 2. Save the User Profile
        dbManager.saveUser(username, "123", "CUSTOMER", name);
        
        // 3. Generate a Random Account ID
        String newAccId = String.valueOf((int)(Math.random() * 1000000));
        
        // 4. Save the Account with 0.0 Balance
        dbManager.saveAccount(newAccId, username, type, 0.0);
        
        System.out.println("Created " + type + " account #" + newAccId + " for " + name);
    }

    public void processDeposit(String accountId, String amountStr) {
        try {
            // 1. Parse Amount (Remove '$' if present)
            double amount = Double.parseDouble(amountStr.replace("$", "").trim());
            
            // 2. Get Current Balance from DB
            double currentBalance = dbManager.getBalance(accountId);
            
            // 3. Calculate New Balance
            double newBalance = currentBalance + amount;
            
            // 4. Save Transaction Record
            String txId = UUID.randomUUID().toString().substring(0, 8);
            dbManager.saveTransaction(txId, "DEPOSIT", amount, null, accountId, "COMPLETED");
            
            // 5. Update Account Balance
            dbManager.updateBalance(accountId, newBalance);
            
            System.out.println("Success: Deposited $" + amount + ". New Balance: $" + newBalance);
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid amount format.");
        }
    }

    public void processWithdrawal(String accountId, String amountStr) {
        try {
            double amount = Double.parseDouble(amountStr.replace("$", "").trim());
            double currentBalance = dbManager.getBalance(accountId);
            
            if (currentBalance >= amount) {
                double newBalance = currentBalance - amount;
                
                String txId = UUID.randomUUID().toString().substring(0, 8);
                dbManager.saveTransaction(txId, "WITHDRAWAL", amount, accountId, null, "COMPLETED");
                dbManager.updateBalance(accountId, newBalance);
                
                System.out.println("Success: Withdrew $" + amount + ". New Balance: $" + newBalance);
            } else {
                System.out.println("Error: Insufficient Funds. Balance is $" + currentBalance);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid amount format.");
        }
    }

    // --- MOCK OPERATIONS (Keep these for UI navigation until DB logic is ready) ---

    public boolean removeEmployee(String username) {
        return !username.equals("admin"); 
    }

    public boolean activateEmployee(String username) {
        return true;
    }

    public String searchEmployees(String criteria, String keyword) {
        return "Found 1 employee: " + keyword + " (Active)";
    }

    public String searchAccounts(String criteria, String keyword) {
        // In the future: dbManager.findAccount(keyword)...
        return "ID: 853013, Customer: Macy Sorenson\nPhone: 555-0129, Type: Chequing";
    }

    public String searchCustomers(String criteria, String keyword) {
        return "Found Customer: " + keyword + " (Matches " + criteria + ")";
    }

    public void approveTransaction(String transactionID) {
        System.out.println("Transaction " + transactionID + " APPROVED.");
    }

    public void denyTransaction(String transactionID) {
        System.out.println("Transaction " + transactionID + " DENIED.");
    }
    public double getAccountBalance(String accountId) {
        return dbManager.getBalance(accountId);
    }

    // Helper to get a formatted string (e.g., "$5000.00")
    public String getFormattedBalance(String accountId) {
        double balance = getAccountBalance(accountId);
        return String.format("$%.2f", balance);
    }
}