package bank;

public class UIManager {
    private DatabaseManager dbManager; // Reference to the Database
    private String currentUser; // Stores the currently logged-in username

    public UIManager() {
        // 1. Initialize the Database connection
        // FIX: Use the correct field name 'dbManager'
        this.dbManager = new DatabaseManager();
        
        // 2. Seed Data (So you can login immediately)
        seedDummyData();
    }

    private void seedDummyData() {
        // Create default users in the database (Password is "123" for all)
        dbManager.saveUser("test", "123", "CUSTOMER", "Test Customer");
        dbManager.saveUser("teller", "123", "TELLER", "Branch Teller");
        dbManager.saveUser("admin", "123", "ADMIN", "System Administrator");
    }

    // --- AUTHENTICATION ---

    public boolean handleLogin(String username, String password) {
        // REAL DATABASE CHECK
        if (dbManager.validateLogin(username, password)) {
            this.currentUser = username;
            return true;
        }
        return false;
    }

    public String getDashboardType(String username) {
        // Logic to determine dashboard based on username
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

    // --- ADMIN OPERATIONS ---

    public boolean removeEmployee(String username) {
        // Mock: Cannot delete yourself (admin)
        return !username.equals("admin"); 
    }

    public boolean activateEmployee(String username) {
        System.out.println("Activating employee: " + username);
        return true;
    }

    public String searchEmployees(String criteria, String keyword) {
        return "Found 1 employee: " + keyword + " (Active)";
    }

    // --- TELLER OPERATIONS ---

    public String searchAccounts(String criteria, String keyword) {
        return "ID: 853013, Customer: Macy Sorenson\nPhone: 555-0129, Type: Chequing";
    }

    public String searchCustomers(String criteria, String keyword) {
        return "Found Customer: " + keyword + " (Matches " + criteria + ")";
    }

    public void createCustomerAccount(String name, String dob, String type) {
        System.out.println("Created " + type + " account for " + name);
    }

    // --- TRANSACTION OPERATIONS ---

    public void processDeposit(String accountId, String amount) {
        System.out.println("Deposited " + amount + " to " + accountId);
    }

    public void processWithdrawal(String accountId, String amount) {
        System.out.println("Withdrew " + amount + " from " + accountId);
    }

    public void approveTransaction(String transactionID) {
        System.out.println("Transaction " + transactionID + " APPROVED.");
    }

    public void denyTransaction(String transactionID) {
        System.out.println("Transaction " + transactionID + " DENIED.");
    }
}