package bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class DatabaseManager {
    // The connection string creates a file named 'bank.db' in your project folder
    private static final String URL = "jdbc:sqlite:bank.db";

    public DatabaseManager() {
        createNewDatabase();
        createTables();
    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
        return conn;
    }

    private void createNewDatabase() {
        try (Connection conn = this.connect()) {
            if (conn != null) {
                // Database created or existing one connected
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createTables() {
        // 1. Users Table
        String sqlUsers = "CREATE TABLE IF NOT EXISTS users ("
                + "username TEXT PRIMARY KEY,"
                + "password TEXT NOT NULL,"
                + "role TEXT NOT NULL,"
                + "full_name TEXT"
                + ");";

        // 2. Accounts Table
        String sqlAccounts = "CREATE TABLE IF NOT EXISTS accounts ("
                + "account_id TEXT PRIMARY KEY,"
                + "owner_username TEXT NOT NULL,"
                + "type TEXT NOT NULL,"
                + "balance REAL,"
                + "FOREIGN KEY (owner_username) REFERENCES users(username)"
                + ");";

        // 3. Transactions Table
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

    // --- DATA SAVING METHODS (These were missing) ---

    public void saveUser(String username, String password, String role, String fullName) {
        String sql = "INSERT OR REPLACE INTO users(username, password, role, full_name) VALUES(?,?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.setString(4, fullName);
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

    public boolean validateLogin(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            return pstmt.executeQuery().next();
        } catch (SQLException e) {
            return false;
        }
    }
    
    public double getBalance(String accountId) {
        String sql = "SELECT balance FROM accounts WHERE account_id = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accountId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0.0;
    }

    public void updateBalance(String accountId, double newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, newBalance);
            pstmt.setString(2, accountId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}