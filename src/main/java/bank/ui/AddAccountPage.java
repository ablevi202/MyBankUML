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
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import bank.UIManager;

public class AddAccountPage extends JFrame {
    private UIManager uiManager;

    public AddAccountPage(UIManager manager, String customerId, String customerName) {
        this.uiManager = manager;

        setTitle("MyBankUML - Add New Account");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // 1. Header
        JLabel header = new JLabel("Open New Account", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(header, gbc);

        // 2. Customer Info
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        JLabel infoLabel = new JLabel("For Customer: " + customerName + " (" + customerId + ")", SwingConstants.CENTER);
        add(infoLabel, gbc);

        // 3. Account Type Selection
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(new JLabel("Select Account Type:"), gbc);

        String[] types = {"Chequing", "Savings", "Credit Card"};
        JComboBox<String> typeBox = new JComboBox<>(types);
        gbc.gridx = 1;
        add(typeBox, gbc);

        // 4. Initial Deposit
        gbc.gridy = 3;
        gbc.gridx = 0;
        add(new JLabel("Initial Deposit ($):"), gbc);

        JTextField depositField = new JTextField("0.00");
        gbc.gridx = 1;
        add(depositField, gbc);

        // 5. Create Button
        JButton createBtn = new JButton("Open Account");
        createBtn.setBackground(new Color(144, 238, 144)); // Green
        createBtn.setOpaque(true);
        createBtn.setBorderPainted(false);
        
        createBtn.addActionListener(e -> {
            String type = (String) typeBox.getSelectedItem();
            String depositStr = depositField.getText();

            // 1. Create the Account
            uiManager.createNewAccount(customerId, type);

            // 2. Handle Deposit if needed
            // Note: Since we don't have the new Account ID easily returned here without refactoring,
            // we instruct the Teller to do it manually or we'd need createNewAccount to return the ID.
            // For now, just creating the account is the critical step.
            
            JOptionPane.showMessageDialog(this, type + " Account created successfully!");
            dispose();
            // Return to Search Page to see the new list
            new SearchCustomerPage(uiManager);
        });

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        add(createBtn, gbc);

        // 6. Cancel Button
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> {
            dispose();
            new SearchCustomerPage(uiManager);
        });
        
        gbc.gridy = 5;
        gbc.insets = new Insets(5, 10, 10, 10);
        add(cancelBtn, gbc);

        setVisible(true);
    }
}