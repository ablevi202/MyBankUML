package bank.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import bank.UIManager;

/**
 * The main dashboard for Customers.
 * <p>
 * This screen serves as the landing page after a customer logs in. It dynamically
 * retrieves and displays all accounts owned by the user, showing their current
 * balances and providing access to transaction history and transfer features.
 * </p>
 */
public class CustomerDashboard extends JFrame {
    private final UIManager uiManager;

    /**
     * Constructs the Customer Dashboard.
     *
     * @param manager The application controller used to fetch account data.
     */
    public CustomerDashboard(UIManager manager) {
        this.uiManager = manager;

        setTitle("MyBankUML - Customer Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Welcome Message
        String welcomeText = "Welcome " + uiManager.getCurrentUserName() + "!";
        JLabel welcomeLabel = new JLabel(welcomeText, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        gbc.gridx = 0; 
        gbc.gridy = 0; 
        gbc.gridwidth = 2;
        add(welcomeLabel, gbc);

        // Dynamic Account Loading
        // Retrieve the list of accounts associated with the logged-in user
        List<String[]> accounts = uiManager.getUserAccounts();
        
        int currentRow = 1;
        
        if (accounts.isEmpty()) {
            gbc.gridy = currentRow++;
            JLabel noAcc = new JLabel("No accounts found.", SwingConstants.CENTER);
            add(noAcc, gbc);
        } else {
            for (String[] acc : accounts) {
                // Data format: [AccountID, Type, Balance]
                String id = acc[0];
                String type = acc[1];
                String balance = "$" + acc[2];
                
                // Add a row for this account
                addAccountRow(gbc, currentRow++, type + " (" + id + ")", balance, "View History", id);
            }
        }

        // Action Buttons
        JButton transButton = new JButton("Make a Transaction");
        transButton.setBackground(new Color(144, 238, 144)); // Light Green
        transButton.setOpaque(true);
        transButton.setBorderPainted(false);
        transButton.addActionListener(e -> {
            dispose();
            new TransactionPage(uiManager);
        });
        
        gbc.gridy = currentRow++;
        gbc.gridwidth = 2;
        add(transButton, gbc);

        // Logout Button
        JButton logoutButton = new JButton("Log out");
        logoutButton.setBackground(new Color(255, 102, 102)); // Light Red
        logoutButton.setOpaque(true);
        logoutButton.setBorderPainted(false);
        logoutButton.addActionListener(e -> {
            uiManager.logout();
            dispose(); 
            new LoginScreen(uiManager).setVisible(true);
        });

        gbc.gridy = currentRow;
        add(logoutButton, gbc);

        setVisible(true);
    }

    /**
     * Helper method to add a standardized row displaying account info and a history button.
     *
     * @param gbc         The GridBagConstraints layout object.
     * @param row         The row index to place this account.
     * @param accountName The display name of the account (Type + ID).
     * @param balance     The formatted balance string.
     * @param btnText     The label for the action button.
     * @param accountId   The ID used to link to the history page.
     */
    private void addAccountRow(GridBagConstraints gbc, int row, String accountName, String balance, String btnText, String accountId) {
        gbc.gridwidth = 1;
        gbc.gridy = row;
        
        // Left Column: Name and Balance
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        JLabel nameLbl = new JLabel(accountName, SwingConstants.LEFT);
        nameLbl.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel balLbl = new JLabel(balance, SwingConstants.LEFT);
        textPanel.add(nameLbl);
        textPanel.add(balLbl);
        
        gbc.gridx = 0;
        gbc.weightx = 0.7;
        add(textPanel, gbc);

        // Right Column: History Button
        JButton historyBtn = new JButton(btnText);
        historyBtn.setBackground(new Color(173, 216, 230)); // Light Blue
        historyBtn.setOpaque(true);
        historyBtn.setBorderPainted(false);
        
        historyBtn.addActionListener(e -> {
            dispose();
            // Navigate to history view for this specific account
            new AccountHistoryPage(uiManager, accountId); 
        });
        
        gbc.gridx = 1;
        gbc.weightx = 0.3;
        add(historyBtn, gbc);
        
        // Reset grid configuration
        gbc.weightx = 0;
        gbc.gridx = 0; 
    }
}