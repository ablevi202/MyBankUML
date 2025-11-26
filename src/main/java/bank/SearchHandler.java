package bank;

import java.util.List;

public class SearchHandler {
    private DatabaseManager dbManager;

    public SearchHandler(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public String searchCustomers(String criteria, String keyword) {
        return dbManager.findUser(criteria, keyword);
    }

    public String searchAccounts(String criteria, String keyword) {
        return dbManager.findAccounts(criteria, keyword);
    }

    public String searchEmployees(String criteria, String keyword) {
        return dbManager.findEmployees(criteria, keyword);
    }

    public List<String[]> getCustomerAccounts(String username) {
        return dbManager.getUserAccounts(username);
    }
}