package bank.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import bank.UIManager;

/**
 * The main dashboard for Tellers.
 * <p>
 * This screen provides access to all Teller-specific functions, including
 * searching for customers and accounts, creating new profiles, and reviewing
 * high-value transactions.
 * </p>
 */
public class TellerDashboard extends JFrame {
    private final UIManager uiManager;

    /**
     * Constructs the Teller Dashboard.
     *
     * @param manager The application controller used for navigation and session management.
     */
    public TellerDashboard(UIManager manager) {
        this.uiManager = manager;

        setTitle("MyBankUML - Teller Dashboard");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Welcome Message
        JLabel welcomeLabel = new JLabel("Welcome Teller!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridy = 0;
        add(welcomeLabel, gbc);

        // Button: Search for Account
        JButton searchAccBtn = createButton("Search for Account", new Color(173, 216, 230));
        searchAccBtn.addActionListener(e -> {
            dispose();
            new SearchAccountPage(uiManager);
        });
        gbc.gridy = 1;
        add(searchAccBtn, gbc);

        // Button: Search for Customer
        JButton searchCustBtn = createButton("Search for Customer", new Color(173, 216, 230));
        searchCustBtn.addActionListener(e -> {
            dispose();
            new SearchCustomerPage(uiManager);
        });
        gbc.gridy = 2;
        add(searchCustBtn, gbc);

        // Button: Create Customer Account
        JButton createBtn = createButton("Create Customer Account", new Color(173, 216, 230));
        createBtn.addActionListener(e -> {
            dispose();
            new CreateCustomerPage(uiManager);
        });
        gbc.gridy = 3;
        add(createBtn, gbc);

        // Button: Review Pending Transactions (Green for emphasis)
        JButton reviewBtn = createButton("Review Pending Transactions", new Color(144, 238, 144));
        reviewBtn.addActionListener(e -> {
            dispose();
            new ReviewPendingTransactionsPage(uiManager);
        });
        gbc.gridy = 4;
        add(reviewBtn, gbc);

        // Button: Logout (Red for caution)
        JButton logoutButton = new JButton("Log out");
        logoutButton.setBackground(new Color(255, 102, 102)); 
        logoutButton.setOpaque(true);
        logoutButton.setBorderPainted(false);
        logoutButton.addActionListener(e -> {
            uiManager.logout();
            dispose();
            new LoginScreen(uiManager).setVisible(true);
        });
        
        gbc.gridy = 5;
        add(logoutButton, gbc);

        setVisible(true);
    }

    /**
     * Helper method to create consistently styled menu buttons.
     *
     * @param text  The label text for the button.
     * @param color The background color of the button.
     * @return A styled JButton ready to be added to the layout.
     */
    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(300, 40));
        return btn;
    }
}