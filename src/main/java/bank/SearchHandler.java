package bank;

import java.util.List;

/**
 * Handles all search and data retrieval operations for the application.
 * <p>
 * This class acts as a specialized delegate for the {@link UIManager}, separating
 * query logic from the main application flow. It routes search requests from
 * the UI to the appropriate database methods.
 * </p>
 */
public class SearchHandler {
    private final DatabaseManager dbManager;

    /**
     * Constructs a new SearchHandler.
     *
     * @param dbManager The persistent database manager used to execute queries.
     */
    public SearchHandler(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * Searches for customers based on specific criteria.
     *
     * @param criteria The field to search by (e.g., "Name", "Date of Birth", "ID").
     * @param keyword  The search term entered by the user.
     * @return A formatted string containing the search result or a "not found" message.
     */
    public String searchCustomers(String criteria, String keyword) {
        return dbManager.findUser(criteria, keyword);
    }

    /**
     * Searches for bank accounts based on specific criteria.
     *
     * @param criteria The field to search by (e.g., "Account ID", "Name").
     * @param keyword  The search term.
     * @return A formatted string containing account details or a "not found" message.
     */
    public String searchAccounts(String criteria, String keyword) {
        return dbManager.findAccounts(criteria, keyword);
    }

    /**
     * Searches for internal employees (Tellers/Admins).
     *
     * @param criteria The field to search by (e.g., "Name", "ID").
     * @param keyword  The search term.
     * @return A formatted string containing employee details.
     */
    public String searchEmployees(String criteria, String keyword) {
        return dbManager.findEmployees(criteria, keyword);
    }

    /**
     * Retrieves all accounts associated with a specific customer.
     *
     * @param username The unique username of the customer.
     * @return A list of string arrays, where each array represents an account's details (ID, Type, Balance).
     */
    public List<String[]> getCustomerAccounts(String username) {
        return dbManager.getUserAccounts(username);
    }
}