package bank.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import bank.UIManager;

public class AccountHistoryPage extends JFrame {
    private UIManager uiManager;

    public AccountHistoryPage(UIManager manager, String accountType) {
        this.uiManager = manager;

        setTitle("MyBankUML - " + accountType);
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // 1. Header
        JLabel headerLabel = new JLabel(accountType, SwingConstants.LEFT);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        add(headerLabel, gbc);

        // 2. Account Details (Hardcoded to match Report Page 54 example)
        addLabel(gbc, 1, "Balance: $4,912.53");
        addLabel(gbc, 2, "Customer: " + uiManager.getCurrentUserName());
        addLabel(gbc, 3, "ID: 501428");
        addLabel(gbc, 4, "Customer Phone Number: 555-2368");
        addLabel(gbc, 5, "Customer Email: customer@email.com");

        // 3. Transaction History Header
        JLabel historyHeader = new JLabel("Transaction History:");
        historyHeader.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridy = 6;
        gbc.insets = new Insets(15, 10, 5, 10); 
        add(historyHeader, gbc);

        // 4. Transaction List
        gbc.insets = new Insets(2, 20, 2, 10); // Indent list
        addLabel(gbc, 7, "- $100.00 to account 481292");
        addLabel(gbc, 8, "+ $513.23 from account 189732");
        addLabel(gbc, 9, "+ $2.99 from account 192312");

        // 5. Back Button
        JButton backButton = new JButton("Back to Dashboard");
        backButton.setBackground(new Color(255, 102, 102)); // Light Red
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> {
            dispose();
            new CustomerDashboard(uiManager);
        });

        gbc.gridy = 10;
        gbc.insets = new Insets(20, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        add(backButton, gbc);

        setVisible(true);
    }

    private void addLabel(GridBagConstraints gbc, int row, String text) {
        JLabel label = new JLabel(text);
        gbc.gridy = row;
        add(label, gbc);
    }
}