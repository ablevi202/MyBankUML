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

public class TellerDashboard extends JFrame {
    private UIManager uiManager;

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

        // 1. Welcome Message
        JLabel welcomeLabel = new JLabel("Welcome Teller!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridy = 0;
        add(welcomeLabel, gbc);

        // 2. Search for Account Button
        JButton searchAccBtn = createButton("Search for Account", new Color(173, 216, 230));
        searchAccBtn.addActionListener(e -> {
            dispose();
            new SearchAccountPage(uiManager);
        });
        gbc.gridy = 1;
        add(searchAccBtn, gbc);

        // 3. Search for Customer Button (New)
        JButton searchCustBtn = createButton("Search for Customer", new Color(173, 216, 230));
        searchCustBtn.addActionListener(e -> {
            dispose();
            new SearchCustomerPage(uiManager);
        });
        gbc.gridy = 2;
        add(searchCustBtn, gbc);

        // 4. Create Customer Account Button
        JButton createBtn = createButton("Create Customer Account", new Color(173, 216, 230));
        createBtn.addActionListener(e -> {
            dispose();
            new CreateCustomerPage(uiManager);
        });
        gbc.gridy = 3;
        add(createBtn, gbc);

        // 5. Review Pending Transactions Button (Green)
        JButton reviewBtn = createButton("Review Pending Transactions", new Color(144, 238, 144));
        reviewBtn.addActionListener(e -> {
            dispose();
            new ReviewPendingTransactionsPage(uiManager);
        });
        gbc.gridy = 4;
        add(reviewBtn, gbc);

        // 6. Logout Button (Red)
        JButton logoutButton = new JButton("Log out");
        logoutButton.setBackground(new Color(255, 102, 102)); 
        logoutButton.setOpaque(true);
        logoutButton.setBorderPainted(false);
        logoutButton.addActionListener(e -> {
            uiManager.logout();
            dispose();
            new LoginScreen(uiManager);
        });
        
        gbc.gridy = 5;
        add(logoutButton, gbc);

        setVisible(true);
    }

    // Helper method to create styled buttons
    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(300, 40));
        return btn;
    }
}