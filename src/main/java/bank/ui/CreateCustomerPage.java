package bank.ui;

import java.awt.Color;
import java.awt.Component;
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

public class CreateCustomerPage extends JFrame {
    private UIManager uiManager;

    public CreateCustomerPage(UIManager manager) {
        this.uiManager = manager;

        setTitle("MyBankUML - Create Customer");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // 1. Header
        JLabel header = new JLabel("Create Customer Account", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across both columns
        add(header, gbc);

        // 2. Fields
        addField(gbc, 1, "Full Name:", new JTextField());
        addField(gbc, 2, "Date of Birth (YYYY-MM-DD):", new JTextField());
        addField(gbc, 3, "Initial Deposit:", new JTextField());

        // 3. Account Type (Fixed Overlap)
        gbc.gridy = 4;
        gbc.gridwidth = 1; 
        gbc.gridx = 0;
        add(new JLabel("Account Type:"), gbc);
        
        String[] types = {"Chequing", "Savings"};
        JComboBox<String> typeBox = new JComboBox<>(types);
        gbc.gridx = 1;
        add(typeBox, gbc);

        // 4. Buttons
        JButton createBtn = new JButton("Create Account");
        createBtn.setBackground(new Color(144, 238, 144));
        createBtn.setOpaque(true);
        createBtn.setBorderPainted(false);
        createBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Customer Account Created Successfully!");
            dispose();
            new TellerDashboard(uiManager);
        });

        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        add(createBtn, gbc);

        JButton backBtn = new JButton("Cancel");
        backBtn.addActionListener(e -> {
            dispose();
            new TellerDashboard(uiManager);
        });
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 10, 10, 10); // Less top padding for cancel
        add(backBtn, gbc);

        setVisible(true);
    }

    private void addField(GridBagConstraints gbc, int row, String labelText, Component field) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        add(new JLabel(labelText), gbc);
        
        gbc.gridx = 1;
        add(field, gbc);
    }
}