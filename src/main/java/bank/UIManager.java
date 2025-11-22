package bank;

public class UIManager {
    private String currentUser; // Stores the currently logged-in username

    // --- AUTHENTICATION ---

    public boolean handleLogin(String username, String password) {
        // Mock Authentication Logic: Allows test, admin, and teller
        boolean isValid = "test".equals(username) || 
                          "admin".equals(username) || 
                          "teller".equals(username);
        
        if (isValid) {
            this.currentUser = username;
        }
        return isValid;
    }

    public String getDashboardType(String username) {
        // Determine role based on username prefix
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