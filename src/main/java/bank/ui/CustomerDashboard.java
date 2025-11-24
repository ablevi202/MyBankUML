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

        // 1. Welcome
        JLabel welcomeLabel = new JLabel("Welcome " + uiManager.getCurrentUserName() + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(welcomeLabel, gbc);

        // 2. DYNAMIC ACCOUNT LOADING
        // Fetches accounts from DB instead of hardcoded IDs
        List<String[]> accounts = uiManager.getUserAccounts();
        
        int row = 1;
        if (accounts.isEmpty()) {
            gbc.gridy = row++;
            JLabel noAcc = new JLabel("No accounts found.", SwingConstants.CENTER);
            add(noAcc, gbc);
        } else {
            for (String[] acc : accounts) {
                String id = acc[0];
                String type = acc[1];
                String balance = "$" + acc[2];
                
                addAccountRow(gbc, row++, type + " (" + id + ")", balance, "View History", id);
            }
        }

        // 3. Action Buttons
        JButton transButton = new JButton("Make a Transaction");
        transButton.setBackground(new Color(144, 238, 144)); 
        transButton.setOpaque(true);
        transButton.setBorderPainted(false);
        transButton.addActionListener(e -> {
            dispose();
            new TransactionPage(uiManager);
        });
        
        gbc.gridy = row++;
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

        gbc.gridy = row;
        add(logoutButton, gbc);

        setVisible(true);
    }

    private void addAccountRow(GridBagConstraints gbc, int row, String name, String balance, String btnText, String accountId) {
        gbc.gridwidth = 1;
        gbc.gridy = row;
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        JLabel nameLbl = new JLabel(name, SwingConstants.LEFT);
        nameLbl.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel balLbl = new JLabel(balance, SwingConstants.LEFT);
        textPanel.add(nameLbl);
        textPanel.add(balLbl);
        
        gbc.gridx = 0;
        gbc.weightx = 0.7;
        add(textPanel, gbc);

        JButton historyBtn = new JButton(btnText);
        historyBtn.setBackground(new Color(173, 216, 230));
        historyBtn.setOpaque(true);
        historyBtn.setBorderPainted(false);
        
        historyBtn.addActionListener(e -> {
            dispose();
            // Pass the REAL account ID to the history page
            new AccountHistoryPage(uiManager, accountId); 
        });
        
        gbc.gridx = 1;
        gbc.weightx = 0.3;
        add(historyBtn, gbc);
        
        gbc.weightx = 0;
        gbc.gridx = 0; // Reset for next component
    }
}