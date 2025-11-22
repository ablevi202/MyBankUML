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

        // 2. Chequing Account Section
        addAccountRow(gbc, 1, "Chequing Account", "$5,012.53", "View Chequing History");

        // 3. Savings Account Section
        addAccountRow(gbc, 2, "Savings Account", "$12,841.53", "View Savings History");

        // 4. Action Buttons
        JButton transButton = new JButton("Make a Transaction");
        transButton.setBackground(new Color(144, 238, 144)); // Light Green
        transButton.setOpaque(true);
        transButton.setBorderPainted(false);
        
        // Navigate to Transaction Page
        transButton.addActionListener(e -> {
            dispose();
            new TransactionPage(uiManager);
        });
        
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(transButton, gbc);

        // 5. Logout Button
        JButton logoutButton = new JButton("Log out");
        logoutButton.setBackground(new Color(255, 102, 102)); // Light Red
        logoutButton.setOpaque(true);
        logoutButton.setBorderPainted(false);
        logoutButton.addActionListener(e -> {
            uiManager.logout();
            dispose(); 
            LoginScreen login = new LoginScreen(uiManager); 
            login.setVisible(true);
        });

        gbc.gridy = 4;
        add(logoutButton, gbc);

        setVisible(true);
    }

    private void addAccountRow(GridBagConstraints gbc, int row, String accountName, String balance, String buttonText) {
        gbc.gridwidth = 1;
        gbc.gridy = row;
        
        // Left side: Name and Balance
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        JLabel nameLbl = new JLabel(accountName, SwingConstants.LEFT);
        nameLbl.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel balLbl = new JLabel(balance, SwingConstants.LEFT);
        textPanel.add(nameLbl);
        textPanel.add(balLbl);
        
        gbc.gridx = 0;
        gbc.weightx = 0.7;
        add(textPanel, gbc);

        // Right side: Button
        JButton historyBtn = new JButton(buttonText);
        historyBtn.setBackground(new Color(173, 216, 230)); // Light Blue
        historyBtn.setOpaque(true);
        historyBtn.setBorderPainted(false);
        
        // Navigate to Account History
        historyBtn.addActionListener(e -> {
            dispose();
            new AccountHistoryPage(uiManager, accountName);
        });
        
        gbc.gridx = 1;
        gbc.weightx = 0.3;
        add(historyBtn, gbc);
        
        // Reset weights
        gbc.weightx = 0;
    }
}