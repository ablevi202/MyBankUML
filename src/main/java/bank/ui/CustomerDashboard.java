package bank.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import bank.UIManager;

public class CustomerDashboard extends JFrame {
    private UIManager uiManager;

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

        // 1. Welcome Message
        JLabel welcomeLabel = new JLabel("Welcome " + uiManager.getCurrentUserName() + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(welcomeLabel, gbc);

        // 2. Chequing Account Section (REAL DATA)
        // We use the ID "853013" which was seeded in UIManager
        String chequingBalance = uiManager.getFormattedBalance("853013"); 
        addAccountRow(gbc, 1, "Chequing Account (853013)", chequingBalance, "View Chequing History");

        // 3. Savings Account Section (REAL DATA)
        // Using a dummy ID "999999" which will return 0.00 since it doesn't exist yet
        String savingsBalance = uiManager.getFormattedBalance("999999");
        addAccountRow(gbc, 2, "Savings Account (999999)", savingsBalance, "View Savings History");

        // ... (Rest of the file remains the same: Action Buttons, Logout) ...
        
        JButton transButton = new JButton("Make a Transaction");
        transButton.setBackground(new Color(144, 238, 144)); 
        transButton.setOpaque(true);
        transButton.setBorderPainted(false);
        transButton.addActionListener(e -> {
            dispose();
            new TransactionPage(uiManager);
        });
        
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(transButton, gbc);

        JButton logoutButton = new JButton("Log out");
        logoutButton.setBackground(new Color(255, 102, 102)); 
        logoutButton.setOpaque(true);
        logoutButton.setBorderPainted(false);
        logoutButton.addActionListener(e -> {
            uiManager.logout();
            dispose(); 
            new LoginScreen(uiManager).setVisible(true);
        });

        gbc.gridy = 4;
        add(logoutButton, gbc);

        setVisible(true);
    }

    // ... (Keep addAccountRow helper method exactly as it was) ...
    private void addAccountRow(GridBagConstraints gbc, int row, String accountName, String balance, String buttonText) {
        gbc.gridwidth = 1;
        gbc.gridy = row;
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        JLabel nameLbl = new JLabel(accountName, SwingConstants.LEFT);
        nameLbl.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel balLbl = new JLabel(balance, SwingConstants.LEFT);
        textPanel.add(nameLbl);
        textPanel.add(balLbl);
        
        gbc.gridx = 0;
        gbc.weightx = 0.7;
        add(textPanel, gbc);

        JButton historyBtn = new JButton(buttonText);
        historyBtn.setBackground(new Color(173, 216, 230));
        historyBtn.setOpaque(true);
        historyBtn.setBorderPainted(false);
        
        historyBtn.addActionListener(e -> {
            dispose();
            new AccountHistoryPage(uiManager, accountName);
        });
        
        gbc.gridx = 1;
        gbc.weightx = 0.3;
        add(historyBtn, gbc);
        
        gbc.weightx = 0;
    }
}