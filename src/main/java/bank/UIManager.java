package bank;

import java.util.List;
import java.util.UUID;

/**
 * The central controller (Facade) for the application.
 * It coordinates actions between the UI and the specialized Logic Handlers.
 */
public class UIManager {
    private final DatabaseManager database;
    
    // Logic Handlers
    private final LoginHandler authHandler;
    private final SearchHandler searchHandler;
    private final RiskVerification riskManager;
    
    private String currentSessionUser; 

    public UIManager() {
        this.database = new DatabaseManager();
        
        // Delegate specific responsibilities to handlers
        this.authHandler = new LoginHandler(database);
        this.searchHandler = new SearchHandler(database);
        this.riskManager = new RiskVerification();
        
        seedDefaultData();
    }

    /**
     * Seeds the database with default users for testing purposes.
     */
    private void seedDefaultData() {
        database.saveUser("test", "123", "CUSTOMER", "Test Customer", "2000-01-01", "555-1234", "test@bank.com");
        database.saveUser("teller", "123", "TELLER", "Branch Teller", "1990-05-20", "555-5678", "teller@bank.com");
        database.saveUser("admin", "123", "ADMIN", "System Administrator", "1985-11-15", "555-9999", "admin@bank.com");
        
        database.saveAccount("853013", "test", "Chequing", 15000.00);
    }

    // --- Authentication (Delegated) ---

    public boolean handleLogin(String username, String password) {
        if (authHandler.validateLogin(username, password)) {
            this.currentSessionUser = username;
            return true;
        }
        return false;
    }

    public String getDashboardType(String username) {
        return authHandler.getUserRole(username);
    }

    public String getCurrentUserRole() {
        if (currentSessionUser == null) return "";
        return getDashboardType(currentSessionUser);
    }

    public String getCurrentUserName() {
        return currentSessionUser != null ? currentSessionUser : "User"; 
    }

    public void logout() {
        this.currentSessionUser = null;
    }

    // --- Search Operations (Delegated) ---

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

    // --- Data Management (Direct DB) ---

    public boolean createCustomerAccount(String name, String dob, String type, String phone, String email, String password) {
        String username = name.toLowerCase().replace(" ", "");
        
        if (database.userExists(username)) {
            return false; 
        }

        database.saveUser(username, password, "CUSTOMER", name, dob, phone, email);
        
        String newAccId = generateId();
        database.saveAccount(newAccId, username, type, 0.0);
        
        return true; 
    }

    public void createNewAccount(String username, String type) {
        String newAccId = generateId();
        database.saveAccount(newAccId, username, type, 0.0);
    }

    public void createEmployee(String username, String password) {
        database.saveUser(username, password, "TELLER", username, "N/A", "N/A", "N/A");
    }

    // --- Transactions (Risk Verified) ---

    public String processDeposit(String accountId, String amountStr) {
        try {
            double amount = parseAmount(amountStr);
            
            // Check for high-value transactions requiring review
            String status = riskManager.getTransactionStatus(amount);
            String txId = generateTxId();
            
            database.saveTransaction(txId, "DEPOSIT", amount, null, accountId, status);
            
            if ("PENDING_REVIEW".equals(status)) {
                return "PENDING";
            }

            updateBalance(accountId, amount); // Positive amount adds funds
            return "SUCCESS";
            
        } catch (NumberFormatException e) { return "ERROR"; }
    }

    public String processWithdrawal(String accountId, String amountStr) {
        try {
            double amount = parseAmount(amountStr);
            String status = riskManager.getTransactionStatus(amount);
            String txId = generateTxId();

            if ("PENDING_REVIEW".equals(status)) {
                database.saveTransaction(txId, "WITHDRAWAL", amount, accountId, null, status);
                return "PENDING";
            }

            double currentBalance = database.getBalance(accountId);
            if (currentBalance >= amount) {
                database.saveTransaction(txId, "WITHDRAWAL", amount, accountId, null, status);
                database.updateBalance(accountId, currentBalance - amount);
                return "SUCCESS";
            }
            return "INSUFFICIENT";
        } catch (NumberFormatException e) { return "ERROR"; }
    }

    public String performTransfer(String fromId, String toId, String amountStr) {
        try {
            double amount = parseAmount(amountStr);
            String status = riskManager.getTransactionStatus(amount);
            String txId = generateTxId();

            if ("PENDING_REVIEW".equals(status)) {
                database.saveTransaction(txId, "TRANSFER", amount, fromId, toId, status);
                return "PENDING";
            }

            if (database.getBalance(fromId) >= amount) {
                // Attempt atomic transfer in DB
                if (database.transfer(fromId, toId, amount)) {
                    database.saveTransaction(txId, "TRANSFER", amount, fromId, toId, "COMPLETED");
                    return "SUCCESS";
                }
                return "ERROR";
            }
            return "INSUFFICIENT";
        } catch (Exception e) { return "ERROR"; }
    }

    // --- UI Data Retrieval ---

    public List<String[]> getUserAccounts() { 
        return database.getUserAccounts(currentSessionUser); 
    }

    public List<String[]> getAccountHistory(String accountId) { 
        return database.getTransactionHistory(accountId); 
    }
    
    public boolean removeEmployee(String username) { 
        if ("admin".equals(username)) return false; 
        return database.deleteUser(username); 
    }
    
    public boolean activateEmployee(String username) { 
        return database.activateUser(username); 
    }
    
    public double getAccountBalance(String accountId) {
        return database.getBalance(accountId);
    }

    public String getFormattedBalance(String accountId) {
        return String.format("$%.2f", getAccountBalance(accountId));
    }

    public List<String[]> getPendingTransactions() {
        return database.getPendingTransactions();
    }

    // --- Review Logic ---

    public void approveTransaction(String transactionID) {
        double amount = database.getTransactionAmount(transactionID);
        String[] parties = database.getTransactionParties(transactionID); // [from, to]

        if (parties != null) {
            boolean success = false;
            
            // Identify transaction type based on null parties
            if (parties[0] != null && parties[1] != null) {
                 // Transfer: Sender -> Receiver
                 success = database.transfer(parties[0], parties[1], amount);
            } else if (parties[1] != null) { 
                 // Deposit: No Sender -> Receiver
                 updateBalance(parties[1], amount); 
                 success = true; 
            } else if (parties[0] != null) { 
                 // Withdrawal: Sender -> No Receiver
                 double cur = database.getBalance(parties[0]); 
                 if (cur >= amount) { 
                     database.updateBalance(parties[0], cur - amount); 
                     success = true; 
                 } 
            }

            if (success) {
                database.updateTransactionStatus(transactionID, "COMPLETED");
            }
        }
    }

    public void denyTransaction(String transactionID) {
        database.updateTransactionStatus(transactionID, "CANCELLED");
    }
    
    // Legacy overload for simpler creation calls
    public void createCustomerAccount(String name, String dob, String type) {
        createCustomerAccount(name, dob, type, "N/A", "N/A", "123");
    }

    // --- Helpers ---

    private String generateId() {
        return String.valueOf((int)(Math.random() * 1000000));
    }

    private String generateTxId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private double parseAmount(String amountStr) {
        return Double.parseDouble(amountStr.replace("$", "").trim());
    }

    private void updateBalance(String accountId, double amountToAdd) {
        double cur = database.getBalance(accountId);
        database.updateBalance(accountId, cur + amountToAdd);
    }
}