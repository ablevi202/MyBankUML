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

public class TellerAccountPage extends JFrame {
    private UIManager uiManager;
    private String accountId; // The specific account being managed

    public TellerAccountPage(UIManager manager, String accountId) {
        this.uiManager = manager;
        this.accountId = accountId;

        setTitle("MyBankUML - Account Management");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // 1. Header
        JLabel header = new JLabel("Account Details", SwingConstants.LEFT);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        add(header, gbc);

        // 2. Account Info (Fetched from Database)
        String balance = uiManager.getFormattedBalance(accountId);

        addLabel(gbc, 1, "Account ID: " + accountId);
        addLabel(gbc, 2, "Current Balance: " + balance);
        
        // 3. Transaction History (Dynamic)
        JLabel historyHeader = new JLabel("Transaction History:");
        historyHeader.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridy = 4;
        gbc.insets = new Insets(15, 10, 5, 10);
        add(historyHeader, gbc);

        List<String[]> history = uiManager.getAccountHistory(accountId);
        int row = 5;
        
        if (history.isEmpty()) {
            gbc.gridy = row++;
            gbc.insets = new Insets(2, 20, 2, 10);
            JLabel noTrans = new JLabel("No recent transactions.");
            noTrans.setForeground(Color.GRAY);
            add(noTrans, gbc);
        } else {
            gbc.insets = new Insets(2, 20, 2, 10);
            for (String[] tx : history) {
                String date = tx[3].length() > 10 ? tx[3].substring(0, 10) : tx[3];
                String labelText = String.format("%s $%s (%s)", tx[0], tx[1], date);
                addLabel(gbc, row++, labelText);
            }
        }

        // 4. Actions Panel
        JPanel btnPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        
        JButton depositBtn = new JButton("Deposit");
        depositBtn.setBackground(new Color(173, 216, 230));
        depositBtn.setOpaque(true);
        depositBtn.setBorderPainted(false);
        depositBtn.addActionListener(e -> {
            dispose();
            new TellerDepositPage(uiManager, accountId);
        });

        JButton transferBtn = new JButton("Transfer");
        transferBtn.setBackground(new Color(144, 238, 144));
        transferBtn.setOpaque(true);
        transferBtn.setBorderPainted(false);
        transferBtn.addActionListener(e -> {
            dispose();
            new TellerTransactionPage(uiManager, accountId);
        });

        JButton withdrawBtn = new JButton("Withdrawal");
        withdrawBtn.setBackground(new Color(255, 102, 102));
        withdrawBtn.setOpaque(true);
        withdrawBtn.setBorderPainted(false);
        withdrawBtn.addActionListener(e -> {
            dispose();
            new TellerWithdrawalPage(uiManager, accountId);
        });

        btnPanel.add(depositBtn);
        btnPanel.add(transferBtn);
        btnPanel.add(withdrawBtn);

        gbc.gridy = row++;
        gbc.insets = new Insets(20, 10, 20, 10);
        add(btnPanel, gbc);

        // 5. Back Button (UPDATED)
        // Now returns to Teller Dashboard for consistent navigation
        JButton backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(e -> {
            dispose();
            new TellerDashboard(uiManager);
        });
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(backButton, gbc);

        setVisible(true);
    }

    private void addLabel(GridBagConstraints gbc, int row, String text) {
        JLabel label = new JLabel(text);
        gbc.gridy = row;
        add(label, gbc);
    }
}