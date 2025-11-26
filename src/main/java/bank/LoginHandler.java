package bank;

/**
 * Handles user authentication and role retrieval logic.
 * <p>
 * This class acts as a security delegate, separating authentication rules
 * from the main application controller. It communicates directly with the
 * {@link DatabaseManager} to verify credentials.
 * </p>
 */
public class LoginHandler {
    private final DatabaseManager dbManager;

    /**
     * Constructs a new LoginHandler with a connection to the database.
     *
     * @param dbManager The persistent database manager used for looking up user data.
     */
    public LoginHandler(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * Validates a user's login credentials against the database.
     *
     * @param username The username provided by the user.
     * @param password The password provided by the user.
     * @return {@code true} if the credentials match an active account; {@code false} otherwise.
     */
    public boolean validateLogin(String username, String password) {
        return dbManager.validateLogin(username, password);
    }

    /**
     * Retrieves the system role (e.g., "CUSTOMER", "TELLER", "ADMIN") for a given user.
     *
     * @param username The username to lookup.
     * @return A String representing the user's role.
     */
    public String getUserRole(String username) {
        return dbManager.getUserRole(username);
    }
}