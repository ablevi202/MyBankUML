package bank.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import bank.UIManager;

public class TransactionPage extends JFrame {
    private UIManager uiManager;

    public TransactionPage(UIManager manager) {
        this.uiManager = manager;

        setTitle("MyBankUML - Make Transaction");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // 1. Header
        JLabel headerLabel = new JLabel("Creating your transaction:");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridy = 0;
        add(headerLabel, gbc);

        // 2. From Account Selection
        gbc.gridy = 1;
        add(new JLabel("Select the account you would like to pay from:"), gbc);
        
        String[] accountTypes = {"Chequing", "Savings"};
        JComboBox<String> accountBox = new JComboBox<>(accountTypes);
        gbc.gridy = 2;
        add(accountBox, gbc);

        // 3. To Account Field
        gbc.gridy = 3;
        add(new JLabel("Enter the ID of the account you want to pay"), gbc);
        
        JTextField toAccountField = new JTextField();
        gbc.gridy = 4;
        add(toAccountField, gbc);

        // 4. Amount Field
        gbc.gridy = 5;
        add(new JLabel("How much to pay?"), gbc);
        
        JTextField amountField = new JTextField("$0.00");
        gbc.gridy = 6;
        add(amountField, gbc);

        // 5. Complete Button
        JButton completeButton = new JButton("Complete Transaction");
        completeButton.setBackground(new Color(144, 238, 144)); // Light Green
        completeButton.setOpaque(true);
        completeButton.setBorderPainted(false);
        
        completeButton.addActionListener(e -> {
            // Here you would normally call uiManager.performTransaction(...)
            // For now, we just move to the status page
            dispose();
            new TransactionStatusPage(uiManager, toAccountField.getText(), amountField.getText());
        });
        
        gbc.gridy = 7;
        gbc.insets = new Insets(20, 10, 5, 10);
        add(completeButton, gbc);

        // 6. Cancel Button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(255, 102, 102)); // Light Red
        cancelButton.setOpaque(true);
        cancelButton.setBorderPainted(false);
        cancelButton.addActionListener(e -> {
            dispose();
            new CustomerDashboard(uiManager);
        });

        gbc.gridy = 8;
        gbc.insets = new Insets(5, 10, 10, 10);
        add(cancelButton, gbc);

        setVisible(true);
    }
}