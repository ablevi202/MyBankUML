package bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all direct interactions with the SQLite database.
 * <p>
 * This class is responsible for establishing connections, creating the schema,
 * and executing CRUD (Create, Read, Update, Delete) operations for Users,
 * Accounts, and Transactions.
 * </p>
 */
public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:bank.db";

    /**
     * Initializes the database manager by creating the database file
     * and ensuring all necessary tables exist.
     */
    public DatabaseManager() {
        createNewDatabase();
        createTables();
    }

    /**
     * Establishes a connection to the SQLite database.
     *
     * @return A Connection object or null if connection fails.
     */
    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
        return conn;
    }

    /**
     * Creates the 'bank.db' file if it does not already exist.
     */
    private void createNewDatabase() {
        try (Connection conn = this.connect()) {
            if (conn != null) {
                // Database file created successfully or already exists
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Defines and creates the table schema for the banking application.
     * Includes tables for Users, Accounts, and Transactions.
     */
    private void createTables() {
        // 1. Users Table: Stores authentication and profile data
        String sqlUsers = "CREATE TABLE IF NOT EXISTS users ("
                + "username TEXT PRIMARY KEY,"
                + "password TEXT NOT NULL,"
                + "role TEXT NOT NULL,"
                + "full_name TEXT,"
                + "date_of_birth TEXT,"
                + "phone TEXT,"
                + "email TEXT,"
                + "is_active INTEGER DEFAULT 1"
                + ");";

        // 2. Accounts Table: Stores financial ledgers linked to users
        String sqlAccounts = "CREATE TABLE IF NOT EXISTS accounts ("
                + "account_id TEXT PRIMARY KEY,"
                + "owner_username TEXT NOT NULL,"
                + "type TEXT NOT NULL,"
                + "balance REAL,"
                + "FOREIGN KEY (owner_username) REFERENCES users(username)"
                + ");";

        // 3. Transactions Table: Logs history of all financial movements
        String sqlTransactions = "CREATE TABLE IF NOT EXISTS transactions ("
                + "id TEXT PRIMARY KEY,"
                + "type TEXT NOT NULL,"
                + "amount REAL,"
                + "from_acc TEXT,"
                + "to_acc TEXT,"
                + "status TEXT,"
                + "timestamp TEXT"
                + ");";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlUsers);
            stmt.execute(sqlAccounts);
            stmt.execute(sqlTransactions);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // --- DATA SAVING OPERATIONS ---

    /**
     * Saves or updates a user record in the database.
     *
     * @param username Unique identifier for login.
     * @param password User's password (plaintext in this prototype).
     * @param role Role of the user (CUSTOMER, TELLER, ADMIN).
     * @param fullName User's full legal name.
     * @param dob Date of Birth (YYYY-MM-DD).
     * @param phone Contact phone number.
     * @param email Contact email address.
     */
    public void saveUser(String username, String password, String role, String fullName, String dob, String phone, String email) {
        String sql = "INSERT OR REPLACE INTO users(username, password, role, full_name, date_of_birth, phone, email, is_active) VALUES(?,?,?,?,?,?,?,1)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.setString(4, fullName);
            pstmt.setString(5, dob);
            pstmt.setString(6, phone);
            pstmt.setString(7, email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveAccount(String accountId, String owner, String type, double balance) {
        String sql = "INSERT OR REPLACE INTO accounts(account_id, owner_username, type, balance) VALUES(?,?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accountId);
            pstmt.setString(2, owner);
            pstmt.setString(3, type);
            pstmt.setDouble(4, balance);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveTransaction(String id, String type, double amount, String from, String to, String status) {
        String sql = "INSERT INTO transactions(id, type, amount, from_acc, to_acc, status, timestamp) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setString(2, type);
            pstmt.setDouble(3, amount);
            pstmt.setString(4, from);
            pstmt.setString(5, to);
            pstmt.setString(6, status);
            pstmt.setString(7, LocalDateTime.now().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // --- QUERY & SEARCH OPERATIONS ---

    public boolean validateLogin(String username, String password) {
        // Ensures user exists, password matches, and account is active (1)
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND is_active = 1";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            return pstmt.executeQuery().next();
        } catch (SQLException e) { return false; }
    }
    
    public String getUserRole(String username) {
        String sql = "SELECT role FROM users WHERE username = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getString("role");
        } catch (SQLException e) { }
        return "CUSTOMER"; 
    }

    public double getBalance(String accountId) {
        String sql = "SELECT balance FROM accounts WHERE account_id = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accountId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getDouble("balance");
        } catch (SQLException e) { }
        return 0.0;
    }

    public void updateBalance(String accountId, double newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, newBalance);
            pstmt.setString(2, accountId);
            pstmt.executeUpdate();
        } catch (SQLException e) { }
    }

    public List<String[]> getUserAccounts(String username) {
        List<String[]> accounts = new ArrayList<>();
        String sql = "SELECT account_id, type, balance FROM accounts WHERE owner_username = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                accounts.add(new String[]{
                    rs.getString("account_id"),
                    rs.getString("type"),
                    String.valueOf(rs.getDouble("balance"))
                });
            }
        } catch (SQLException e) { }
        return accounts;
    }

    public List<String[]> getTransactionHistory(String accountId) {
        List<String[]> history = new ArrayList<>();
        String sql = "SELECT type, amount, to_acc, timestamp FROM transactions WHERE from_acc = ? OR to_acc = ? ORDER BY timestamp DESC";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accountId);
            pstmt.setString(2, accountId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                history.add(new String[]{
                    rs.getString("type"),
                    String.valueOf(rs.getDouble("amount")),
                    rs.getString("to_acc"),
                    rs.getString("timestamp")
                });
            }
        } catch (SQLException e) { }
        return history;
    }

    public String findUser(String criteria, String keyword) {
        String column = "username";
        if (criteria.contains("Name")) column = "full_name";
        else if (criteria.contains("Date")) column = "date_of_birth";
        else if (criteria.contains("ID")) column = "username";
        
        String sql = "SELECT * FROM users WHERE " + column + " LIKE ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String dob = rs.getString("date_of_birth");
                if (dob == null) dob = "N/A";
                
                String phone = rs.getString("phone");
                if (phone == null) phone = "N/A";
                
                String email = rs.getString("email");
                if (email == null) email = "N/A";

                String status = (rs.getInt("is_active") == 1) ? "Active" : "Inactive";
                
                return "Found: " + rs.getString("full_name") + 
                       " (ID: " + rs.getString("username") + ") " +
                       "[" + status + "] " +
                       "{DOB: " + dob + ", Phone: " + phone + ", Email: " + email + "}";
            }
        } catch (SQLException e) { }
        return "No user found matching " + criteria + ": " + keyword;
    }

    public boolean userExists(String username) {
        String sql = "SELECT username FROM users WHERE username = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            return pstmt.executeQuery().next(); // Returns true if found
        } catch (SQLException e) { 
            System.out.println(e.getMessage());
            return false;
        }
    }

    public String findEmployees(String criteria, String keyword) {
        String column = "username";
        if (criteria.contains("Name")) column = "full_name";
        if (criteria.contains("ID")) column = "username";

        String sql = "SELECT * FROM users WHERE role != 'CUSTOMER' AND " + column + " LIKE ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String status = (rs.getInt("is_active") == 1) ? "Active" : "Inactive";
                return "Found Employee: " + rs.getString("username") + 
                       " (" + rs.getString("role") + ") - " + status;
            }
        } catch (SQLException e) { }
        return "No employee found.";
    }
    
    public boolean deleteUser(String username) {
        // Soft delete: Set is_active to 0
        String sql = "UPDATE users SET is_active = 0 WHERE username = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean activateUser(String username) {
        String sql = "UPDATE users SET is_active = 1 WHERE username = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
    
    /**
     * Searches for an account using complex criteria (Name, Phone, ID).
     * Uses SQL JOINs to link Accounts to Users for name/phone searches.
     */
    public String findAccounts(String criteria, String keyword) {
        String sql;
        
        if (criteria.contains("Name")) {
            sql = "SELECT a.* FROM accounts a JOIN users u ON a.owner_username = u.username WHERE u.full_name LIKE ?";
        } else if (criteria.contains("Phone")) {
            sql = "SELECT a.* FROM accounts a JOIN users u ON a.owner_username = u.username WHERE u.phone LIKE ?";
        } else {
            // Default to Account ID search
            sql = "SELECT * FROM accounts WHERE account_id LIKE ?";
        }

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return "ID: " + rs.getString("account_id") + 
                       ", Type: " + rs.getString("type") +
                       ", Owner: " + rs.getString("owner_username") + 
                       ", Balance: $" + rs.getDouble("balance");
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return "No account found matching " + criteria + ": " + keyword;
    }

    // --- TRANSACTION MANAGEMENT ---

    public List<String[]> getPendingTransactions() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT id, from_acc, amount, type FROM transactions WHERE status = 'PENDING_REVIEW'";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new String[]{
                    rs.getString("id"),
                    rs.getString("from_acc"),
                    "$" + rs.getDouble("amount"),
                    rs.getString("type")
                });
            }
        } catch (SQLException e) { }
        return list;
    }

    public void updateTransactionStatus(String id, String status) {
        String sql = "UPDATE transactions SET status = ? WHERE id = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setString(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { }
    }
    
    public double getTransactionAmount(String id) {
        String sql = "SELECT amount FROM transactions WHERE id = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getDouble("amount");
        } catch (SQLException e) { }
        return 0.0;
    }
    
    public String[] getTransactionParties(String id) {
        String sql = "SELECT from_acc, to_acc FROM transactions WHERE id = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return new String[]{rs.getString("from_acc"), rs.getString("to_acc")};
        } catch (SQLException e) { }
        return null;
    }

    /**
     * Executes an atomic transfer of funds between two accounts.
     * Uses SQL transaction (commit/rollback) to ensure data integrity.
     *
     * @return true if successful, false if error occurred.
     */
    public boolean transfer(String fromId, String toId, double amount) {
        String withdrawSql = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?";
        String depositSql = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";
        
        try (Connection conn = this.connect()) {
            conn.setAutoCommit(false); // Start Atomic Transaction
            
            try (PreparedStatement wStmt = conn.prepareStatement(withdrawSql);
                 PreparedStatement dStmt = conn.prepareStatement(depositSql)) {
                
                // Step 1: Withdraw from Sender
                wStmt.setDouble(1, amount);
                wStmt.setString(2, fromId);
                wStmt.executeUpdate();
                
                // Step 2: Deposit to Receiver
                dStmt.setDouble(1, amount);
                dStmt.setString(2, toId);
                dStmt.executeUpdate();
                
                conn.commit(); // Commit Both Steps
                return true;
            } catch (SQLException e) {
                conn.rollback(); // Undo Both if Error
                return false;
            }
        } catch (SQLException e) { return false; }
    }
}