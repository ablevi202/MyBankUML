package bank;

public class LoginHandler {
    private DatabaseManager dbManager;

    public LoginHandler(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public boolean validateLogin(String username, String password) {
        return dbManager.validateLogin(username, password);
    }
    
    public String getUserRole(String username) {
        return dbManager.getUserRole(username);
    }
}