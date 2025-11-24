package bank.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import bank.UIManager;

public class TellerWithdrawalPage extends JFrame {
    private UIManager uiManager;
    private String accountId; // The specific account for withdrawal

    public TellerWithdrawalPage(UIManager manager, String accountId) {
        this.uiManager = manager;
        this.accountId = accountId;

        setTitle("MyBankUML - Withdrawal Menu");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // 1. Header
        JLabel headerLabel = new JLabel("Withdrawal Menu", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        add(headerLabel, gbc);

        // 2. Info Label
        JLabel accountLabel = new JLabel("Withdrawing from Account: " + accountId, SwingConstants.CENTER);
        accountLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy = 1;
        add(accountLabel, gbc);

        // 3. Amount Label & Field
        gbc.gridy = 2;
        JLabel amountLbl = new JLabel("Withdrawal Amount:", SwingConstants.LEFT);
        amountLbl.setFont(new Font("Arial", Font.BOLD, 12));
        add(amountLbl, gbc);

        JTextField amountField = new JTextField("$0.00");
        gbc.gridy = 3;
        add(amountField, gbc);

        // 4. Complete Button
        JButton completeButton = new JButton("Complete Withdrawal");
        completeButton.setBackground(new Color(144, 238, 144)); // Light Green
        completeButton.setOpaque(true);
        completeButton.setBorderPainted(false);
        
        completeButton.addActionListener(e -> {
            String amount = amountField.getText();
            String status = uiManager.processWithdrawal(accountId, amount);
            
            if ("SUCCESS".equals(status)) {
                JOptionPane.showMessageDialog(this, "Withdrawal Successful!");
                dispose();
                new TellerAccountPage(uiManager, accountId);
            } else if ("PENDING".equals(status)) {
                JOptionPane.showMessageDialog(this, "Large Withdrawal Queued for Review.");
                dispose();
                new TellerAccountPage(uiManager, accountId);
            } else if ("INSUFFICIENT".equals(status)) {
                JOptionPane.showMessageDialog(this, "Error: Insufficient Funds.", "Failed", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error: Invalid Amount.", "Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridy = 4;
        gbc.insets = new Insets(20, 10, 10, 10);
        add(completeButton, gbc);

        // 5. Back Button
        JButton backButton = new JButton("Back to Account");
        backButton.setBackground(new Color(255, 102, 102)); // Light Red
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        
        backButton.addActionListener(e -> {
            dispose();
            new TellerAccountPage(uiManager, accountId);
        });

        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(backButton, gbc);

        setVisible(true);
    }
}