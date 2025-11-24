package bank.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import bank.UIManager;

public class AccountHistoryPage extends JFrame {
    private UIManager uiManager;

    public AccountHistoryPage(UIManager manager, String accountId) {
        this.uiManager = manager;

        setTitle("MyBankUML - History");
        setSize(500, 450); // Slightly taller for details
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // 1. Header
        JLabel headerLabel = new JLabel("Account Details", SwingConstants.LEFT);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        add(headerLabel, gbc);

        // 2. Dynamic Account Info
        // Fetch the real balance from the database
        String liveBalance = uiManager.getFormattedBalance(accountId);
        String currentUser = uiManager.getCurrentUserName();

        addLabel(gbc, 1, "Account ID: " + accountId);
        addLabel(gbc, 2, "Owner: " + currentUser);
        addLabel(gbc, 3, "Current Balance: " + liveBalance);

        // 3. Transaction History Header
        JLabel historyHeader = new JLabel("Recent Transactions:");
        historyHeader.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridy = 4;
        gbc.insets = new Insets(20, 10, 5, 10); // Add extra spacing above
        add(historyHeader, gbc);

        // 4. Dynamic Transaction List
        // Fetches real transaction rows from 'transactions' table
        List<String[]> history = uiManager.getAccountHistory(accountId);
        int row = 5;
        
        if (history.isEmpty()) {
            gbc.gridy = row++;
            gbc.insets = new Insets(5, 20, 5, 10);
            JLabel noTrans = new JLabel("No transactions found.");
            noTrans.setForeground(Color.GRAY);
            add(noTrans, gbc);
        } else {
            gbc.insets = new Insets(2, 20, 2, 10); // Indent list
            for (String[] tx : history) {
                // Data Format: [Type, Amount, To_Account, Timestamp]
                // Display: "DEPOSIT $500.00 (2025-11-24)"
                String type = tx[0];
                String amount = tx[1];
                String date = tx[3].length() > 10 ? tx[3].substring(0, 10) : tx[3];
                
                String labelText = String.format("%s $%s (%s)", type, amount, date);
                addLabel(gbc, row++, labelText);
            }
        }

        // 5. Back Button
        JButton backButton = new JButton("Back to Dashboard");
        backButton.setBackground(new Color(255, 102, 102)); // Light Red
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> {
            dispose();
            new CustomerDashboard(uiManager);
        });

        gbc.gridy = row;
        gbc.insets = new Insets(30, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        add(backButton, gbc);

        setVisible(true);
    }

    // Helper to create standard labels
    private void addLabel(GridBagConstraints gbc, int row, String text) {
        JLabel label = new JLabel(text);
        gbc.gridy = row;
        add(label, gbc);
    }
}