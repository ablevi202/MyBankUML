package bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    // 1. Initialize the Database File
    private void createNewDatabase() {
        try (Connection conn = this.connect()) {
            if (conn != null) {
                // Just connecting creates the file if it doesn't exist
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // 2. Create Tables (Schema)
    private void createTables() {
        // SQL to create Users table
        String sqlUsers = "CREATE TABLE IF NOT EXISTS users ("
                + "username TEXT PRIMARY KEY,"
                + "password TEXT NOT NULL,"
                + "role TEXT NOT NULL,"
                + "full_name TEXT"
                + ");";

        // SQL to create Accounts table
        String sqlAccounts = "CREATE TABLE IF NOT EXISTS accounts ("
                + "account_id TEXT PRIMARY KEY,"
                + "owner_username TEXT NOT NULL,"
                + "type TEXT NOT NULL,"
                + "balance REAL,"
                + "FOREIGN KEY (owner_username) REFERENCES users(username)"
                + ");";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlUsers);
            stmt.execute(sqlAccounts);
            // Add tables for Transactions and AuditLogs here as per design...
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // 3. Example Method: Save a User
    public void saveUser(String username, String password, String role, String fullName) {
        String sql = "INSERT INTO users(username, password, role, full_name) VALUES(?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.setString(4, fullName);
            pstmt.executeUpdate();
            System.out.println("User saved: " + username);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // 4. Example Method: Validate Login
    public boolean validateLogin(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Returns true if a record is found
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}