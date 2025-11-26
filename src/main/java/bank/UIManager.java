package bank;

import java.util.List;
import java.util.UUID;

public class UIManager {
    private DatabaseManager dbManager; 
    private String currentUser; 

    public UIManager() {
        this.dbManager = new DatabaseManager();
        seedDummyData();
    }

    private void seedDummyData() {
        // Correct 7 arguments (user, pass, role, name, dob, phone, email)
        dbManager.saveUser("test", "123", "CUSTOMER", "Test Customer", "2000-01-01", "555-1234", "test@bank.com");
        dbManager.saveUser("teller", "123", "TELLER", "Branch Teller", "1990-05-20", "555-5678", "teller@bank.com");
        dbManager.saveUser("admin", "123", "ADMIN", "System Administrator", "1985-11-15", "555-9999", "admin@bank.com");
        
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
        return dbManager.getUserRole(username);
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

    // --- REAL DATA OPERATIONS ---

    // FIXED: Returns boolean (was void)
    public boolean createCustomerAccount(String name, String dob, String type, String phone, String email, String password) {
        String username = name.toLowerCase().replace(" ", "");
        
        if (dbManager.userExists(username)) {
            System.out.println("Error: User " + username + " already exists.");
            return false; // Failed
        }

        dbManager.saveUser(username, password, "CUSTOMER", name, dob, phone, email);
        
        String newAccId = String.valueOf((int)(Math.random() * 1000000));
        dbManager.saveAccount(newAccId, username, type, 0.0);
        
        System.out.println("Created Customer: " + username + " with Account #" + newAccId);
        return true; // Success
    }

    public void createNewAccount(String username, String type) {
        String newAccId = String.valueOf((int)(Math.random() * 1000000));
        dbManager.saveAccount(newAccId, username, type, 0.0);
        System.out.println("Added new " + type + " account #" + newAccId + " to customer " + username);
    }

    public void createEmployee(String username, String password) {
        // Save as TELLER role
        dbManager.saveUser(username, password, "TELLER", username, "N/A", "N/A", "N/A");
        System.out.println("Created new employee: " + username);
    }

    // --- TRANSACTIONS (RISK & BALANCE LOGIC) ---

    // FIXED: Returns String status
    public String processDeposit(String accountId, String amountStr) {
        try {
            double amount = Double.parseDouble(amountStr.replace("$", "").trim());
            if (amount > 10000) {
                String txId = UUID.randomUUID().toString().substring(0, 8);
                dbManager.saveTransaction(txId, "DEPOSIT", amount, null, accountId, "PENDING_REVIEW");
                return "PENDING";
            }
            double currentBalance = dbManager.getBalance(accountId);
            double newBalance = currentBalance + amount;
            String txId = UUID.randomUUID().toString().substring(0, 8);
            dbManager.saveTransaction(txId, "DEPOSIT", amount, null, accountId, "COMPLETED");
            dbManager.updateBalance(accountId, newBalance);
            return "SUCCESS";
        } catch (NumberFormatException e) { return "ERROR"; }
    }

    // FIXED: Returns String status
    public String processWithdrawal(String accountId, String amountStr) {
        try {
            double amount = Double.parseDouble(amountStr.replace("$", "").trim());
            if (amount > 10000) {
                String txId = UUID.randomUUID().toString().substring(0, 8);
                dbManager.saveTransaction(txId, "WITHDRAWAL", amount, accountId, null, "PENDING_REVIEW");
                return "PENDING";
            }
            double currentBalance = dbManager.getBalance(accountId);
            if (currentBalance >= amount) {
                double newBalance = currentBalance - amount;
                String txId = UUID.randomUUID().toString().substring(0, 8);
                dbManager.saveTransaction(txId, "WITHDRAWAL", amount, accountId, null, "COMPLETED");
                dbManager.updateBalance(accountId, newBalance);
                return "SUCCESS";
            } else {
                return "INSUFFICIENT";
            }
        } catch (NumberFormatException e) { return "ERROR"; }
    }

    public String performTransfer(String fromId, String toId, String amountStr) {
        try {
            double amount = Double.parseDouble(amountStr.replace("$", "").trim());
            if (amount > 10000) {
                String txId = UUID.randomUUID().toString().substring(0,8);
                dbManager.saveTransaction(txId, "TRANSFER", amount, fromId, toId, "PENDING_REVIEW");
                return "PENDING";
            }
            if (dbManager.getBalance(fromId) >= amount) {
                if (dbManager.transfer(fromId, toId, amount)) {
                    String txId = UUID.randomUUID().toString().substring(0,8);
                    dbManager.saveTransaction(txId, "TRANSFER", amount, fromId, toId, "COMPLETED");
                    return "SUCCESS";
                } else {
                    return "ERROR";
                }
            } else {
                return "INSUFFICIENT";
            }
        } catch (Exception e) { return "ERROR"; }
    }

    // --- UI DATA RETRIEVAL ---
    public List<String[]> getUserAccounts() { return dbManager.getUserAccounts(currentUser); }
    public List<String[]> getAccountHistory(String accountId) { return dbManager.getTransactionHistory(accountId); }
    public List<String[]> getCustomerAccounts(String username) { return dbManager.getUserAccounts(username); }
    public String searchCustomers(String criteria, String keyword) { return dbManager.findUser(criteria, keyword); }
    public boolean removeEmployee(String username) { return !username.equals("admin") && dbManager.deleteUser(username); }
    public boolean activateEmployee(String username) { return dbManager.activateUser(username); }
    public String searchEmployees(String criteria, String keyword) { return dbManager.findEmployees(criteria, keyword); }
    public String searchAccounts(String criteria, String keyword) { return dbManager.findAccounts(criteria, keyword); }
    public double getAccountBalance(String accountId) { return dbManager.getBalance(accountId); }
    public String getFormattedBalance(String accountId) { return String.format("$%.2f", getAccountBalance(accountId)); }
    public List<String[]> getPendingTransactions() { return dbManager.getPendingTransactions(); }

    public void approveTransaction(String transactionID) {
        double amount = dbManager.getTransactionAmount(transactionID);
        String[] parties = dbManager.getTransactionParties(transactionID);
        if (parties != null) {
            boolean success = false;
            // Handle Transfers
            if (parties[0] != null && parties[1] != null) success = dbManager.transfer(parties[0], parties[1], amount);
            // Handle Deposits
            else if (parties[1] != null) { double cur = dbManager.getBalance(parties[1]); dbManager.updateBalance(parties[1], cur + amount); success = true; }
            // Handle Withdrawals
            else if (parties[0] != null) { double cur = dbManager.getBalance(parties[0]); if (cur >= amount) { dbManager.updateBalance(parties[0], cur - amount); success = true; } }
            
            if (success) dbManager.updateTransactionStatus(transactionID, "COMPLETED");
        }
    }

    public void denyTransaction(String transactionID) {
        dbManager.updateTransactionStatus(transactionID, "CANCELLED");
    }
}