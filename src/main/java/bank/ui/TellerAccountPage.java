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

public class TellerAccountPage extends JFrame {
    private UIManager uiManager;
    // The ID of the account currently being managed
    private String currentAccountId = "853013"; 

    public TellerAccountPage(UIManager manager) {
        this.uiManager = manager;

        setTitle("MyBankUML - Account Management");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // 1. Account Info
        JLabel header = new JLabel("Chequing Account", SwingConstants.LEFT);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        add(header, gbc);

        // --- REAL DATA FETCH ---
        String balance = uiManager.getFormattedBalance(currentAccountId);
        // -----------------------

        addLabel(gbc, 1, "Balance: " + balance);
        addLabel(gbc, 2, "Customer: " + uiManager.getCurrentUserName()); // Or fetch real owner name
        addLabel(gbc, 3, "ID: " + currentAccountId);
        
        addLabel(gbc, 4, "Customer Phone Number: 555-0129");
        addLabel(gbc, 5, "Customer Email: customer@email.com");

        // ... (Rest of the file: History, Buttons, etc. remains the same) ...
        
        // Transaction History Preview (Static for now)
        JLabel historyHeader = new JLabel("Transaction History:");
        historyHeader.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridy = 6;
        gbc.insets = new Insets(15, 10, 5, 10);
        add(historyHeader, gbc);

        gbc.insets = new Insets(2, 20, 2, 10);
        addLabel(gbc, 7, "- $150.00 to account 481292");
        addLabel(gbc, 8, "+ $524.23 from account 189732");

        // Actions Panel
        JPanel btnPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        
        JButton depositBtn = new JButton("Deposit");
        depositBtn.setBackground(new Color(173, 216, 230));
        depositBtn.setOpaque(true);
        depositBtn.setBorderPainted(false);
        depositBtn.addActionListener(e -> {
            dispose();
            new TellerDepositPage(uiManager);
        });

        JButton transferBtn = new JButton("Make a Transaction");
        transferBtn.setBackground(new Color(144, 238, 144));
        transferBtn.setOpaque(true);
        transferBtn.setBorderPainted(false);
        transferBtn.addActionListener(e -> {
            dispose();
            new TellerTransactionPage(uiManager);
        });

        JButton withdrawBtn = new JButton("Withdrawal");
        withdrawBtn.setBackground(new Color(255, 102, 102));
        withdrawBtn.setOpaque(true);
        withdrawBtn.setBorderPainted(false);
        withdrawBtn.addActionListener(e -> {
            dispose();
            new TellerWithdrawalPage(uiManager);
        });

        btnPanel.add(depositBtn);
        btnPanel.add(transferBtn);
        btnPanel.add(withdrawBtn);

        gbc.gridy = 9;
        gbc.insets = new Insets(20, 10, 20, 10);
        add(btnPanel, gbc);

        JButton backButton = new JButton("Back to Search");
        backButton.addActionListener(e -> {
            dispose();
            new SearchAccountPage(uiManager);
        });
        gbc.gridy = 10;
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