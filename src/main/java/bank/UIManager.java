package bank;

import java.util.List;
import java.util.UUID;

public class UIManager {
    private DatabaseManager dbManager;
    
    // --- HANDLERS (New Architecture) ---
    private LoginHandler loginHandler;
    private SearchHandler searchHandler;
    private RiskVerification riskVerifier;
    
    private String currentUser; 

    public UIManager() {
        this.dbManager = new DatabaseManager();
        
        // Initialize Handlers
        this.loginHandler = new LoginHandler(dbManager);
        this.searchHandler = new SearchHandler(dbManager);
        this.riskVerifier = new RiskVerification();
        
        seedDummyData();
    }

    private void seedDummyData() {
        // Standard Seed Data (No POB)
        dbManager.saveUser("test", "123", "CUSTOMER", "Test Customer", "2000-01-01", "555-1234", "test@bank.com");
        dbManager.saveUser("teller", "123", "TELLER", "Branch Teller", "1990-05-20", "555-5678", "teller@bank.com");
        dbManager.saveUser("admin", "123", "ADMIN", "System Administrator", "1985-11-15", "555-9999", "admin@bank.com");
        dbManager.saveAccount("853013", "test", "Chequing", 5000.00);
    }

    // --- AUTHENTICATION (Delegated to LoginHandler) ---

    public boolean handleLogin(String username, String password) {
        if (loginHandler.validateLogin(username, password)) {
            this.currentUser = username;
            return true;
        }
        return false;
    }

    public String getDashboardType(String username) {
        return loginHandler.getUserRole(username);
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

    // --- SEARCH (Delegated to SearchHandler) ---

    public String searchCustomers(String criteria, String keyword) {
        return searchHandler.searchCustomers(criteria, keyword);
    }

    public String searchEmployees(String criteria, String keyword) { 
        return searchHandler.searchEmployees(criteria, keyword); 
    }

    public String searchAccounts(String criteria, String keyword) { 
        return searchHandler.searchAccounts(criteria, keyword); 
    }
    
    public List<String[]> getCustomerAccounts(String username) {
        return searchHandler.getCustomerAccounts(username);
    }

    // --- REAL DATA OPERATIONS (Direct DB or Hybrid) ---

    public boolean createCustomerAccount(String name, String dob, String type, String phone, String email, String password) {
        String username = name.toLowerCase().replace(" ", "");
        
        if (dbManager.userExists(username)) {
            System.out.println("Error: User " + username + " already exists.");
            return false; 
        }

        dbManager.saveUser(username, password, "CUSTOMER", name, dob, phone, email);
        
        String newAccId = String.valueOf((int)(Math.random() * 1000000));
        dbManager.saveAccount(newAccId, username, type, 0.0);
        
        System.out.println("Created Customer: " + username + " with Account #" + newAccId);
        return true; 
    }

    public void createNewAccount(String username, String type) {
        String newAccId = String.valueOf((int)(Math.random() * 1000000));
        dbManager.saveAccount(newAccId, username, type, 0.0);
        System.out.println("Added new " + type + " account #" + newAccId + " to customer " + username);
    }

    public void createEmployee(String username, String password) {
        dbManager.saveUser(username, password, "TELLER", username, "N/A", "N/A", "N/A");
        System.out.println("Created new employee: " + username);
    }

    // --- TRANSACTIONS (Using RiskVerification) ---

    public String processDeposit(String accountId, String amountStr) {
        try {
            double amount = Double.parseDouble(amountStr.replace("$", "").trim());
            
            // Use RiskHandler to determine status
            String status = riskVerifier.getTransactionStatus(amount);
            
            String txId = UUID.randomUUID().toString().substring(0, 8);
            dbManager.saveTransaction(txId, "DEPOSIT", amount, null, accountId, status);
            
            if ("PENDING_REVIEW".equals(status)) {
                return "PENDING";
            }

            // If COMPLETED, update balance
            double currentBalance = dbManager.getBalance(accountId);
            dbManager.updateBalance(accountId, currentBalance + amount);
            return "SUCCESS";
            
        } catch (NumberFormatException e) { return "ERROR"; }
    }

    public String processWithdrawal(String accountId, String amountStr) {
        try {
            double amount = Double.parseDouble(amountStr.replace("$", "").trim());
            String status = riskVerifier.getTransactionStatus(amount);
            
            String txId = UUID.randomUUID().toString().substring(0, 8);
            dbManager.saveTransaction(txId, "WITHDRAWAL", amount, accountId, null, status);

            if ("PENDING_REVIEW".equals(status)) {
                return "PENDING";
            }

            double currentBalance = dbManager.getBalance(accountId);
            if (currentBalance >= amount) {
                dbManager.updateBalance(accountId, currentBalance - amount);
                return "SUCCESS";
            } else {
                return "INSUFFICIENT";
            }
        } catch (NumberFormatException e) { return "ERROR"; }
    }

    public String performTransfer(String fromId, String toId, String amountStr) {
        try {
            double amount = Double.parseDouble(amountStr.replace("$", "").trim());
            String status = riskVerifier.getTransactionStatus(amount);

            String txId = UUID.randomUUID().toString().substring(0,8);
            dbManager.saveTransaction(txId, "TRANSFER", amount, fromId, toId, status);

            if ("PENDING_REVIEW".equals(status)) {
                return "PENDING";
            }

            if (dbManager.getBalance(fromId) >= amount) {
                if (dbManager.transfer(fromId, toId, amount)) {
                    // Update status to COMPLETED if DB transfer succeeds
                    dbManager.updateTransactionStatus(txId, "COMPLETED"); 
                    return "SUCCESS";
                }
                return "ERROR";
            }
            return "INSUFFICIENT";
        } catch (Exception e) { return "ERROR"; }
    }

    // --- UI DATA RETRIEVAL & ADMIN ---

    public List<String[]> getUserAccounts() { return dbManager.getUserAccounts(currentUser); }
    public List<String[]> getAccountHistory(String accountId) { return dbManager.getTransactionHistory(accountId); }
    
    public boolean removeEmployee(String username) { 
        if (username.equals("admin")) return false;
        return dbManager.deleteUser(username); 
    }
    
    public boolean activateEmployee(String username) { return dbManager.activateUser(username); }
    public double getAccountBalance(String accountId) { return dbManager.getBalance(accountId); }
    public String getFormattedBalance(String accountId) { return String.format("$%.2f", getAccountBalance(accountId)); }
    public List<String[]> getPendingTransactions() { return dbManager.getPendingTransactions(); }

    public void approveTransaction(String transactionID) {
        double amount = dbManager.getTransactionAmount(transactionID);
        String[] parties = dbManager.getTransactionParties(transactionID);

        if (parties != null) {
            boolean success = false;
            
            // Simplified atomic checks
            if (parties[0] != null && parties[1] != null) {
                 success = dbManager.transfer(parties[0], parties[1], amount);
            } else if (parties[1] != null) { 
                 double cur = dbManager.getBalance(parties[1]); 
                 dbManager.updateBalance(parties[1], cur + amount); 
                 success = true; 
            } else if (parties[0] != null) { 
                 double cur = dbManager.getBalance(parties[0]); 
                 if (cur >= amount) { 
                     dbManager.updateBalance(parties[0], cur - amount); 
                     success = true; 
                 } 
            }

            if (success) {
                dbManager.updateTransactionStatus(transactionID, "COMPLETED");
                System.out.println("Transaction " + transactionID + " APPROVED and Processed.");
            }
        }
    }

    public void denyTransaction(String transactionID) {
        dbManager.updateTransactionStatus(transactionID, "CANCELLED");
    }
    
    // Helper for legacy creation calls
    public void createCustomerAccount(String name, String dob, String type) {
        createCustomerAccount(name, dob, type, "N/A", "N/A", "123");
    }
}