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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import bank.UIManager;

public class CreateCustomerPage extends JFrame {
    private UIManager uiManager;

    public CreateCustomerPage(UIManager manager) {
        this.uiManager = manager;

        setTitle("MyBankUML - Create Customer");
        setSize(500, 600); 
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
        gbc.gridwidth = 2;
        add(header, gbc);

        // 2. Fields
        JTextField nameField = new JTextField();
        addField(gbc, 1, "Full Name:", nameField);

        JTextField dobField = new JTextField();
        addField(gbc, 2, "Date of Birth (YYYY-MM-DD):", dobField);

        JTextField phoneField = new JTextField();
        addField(gbc, 4, "Phone Number:", phoneField);

        JTextField emailField = new JTextField();
        addField(gbc, 5, "Email Address:", emailField);

        JPasswordField passField = new JPasswordField();
        addField(gbc, 6, "Initial Password:", passField);

        JTextField depositField = new JTextField("0.00");
        addField(gbc, 7, "Initial Deposit ($):", depositField);

        // 3. Account Type
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        add(new JLabel("Account Type:"), gbc);
        
        String[] types = {"Chequing", "Savings"};
        JComboBox<String> typeBox = new JComboBox<>(types);
        gbc.gridx = 1;
        add(typeBox, gbc);

        // 4. Create Button
        JButton createBtn = new JButton("Create Account");
        createBtn.setBackground(new Color(144, 238, 144)); // Green
        createBtn.setOpaque(true);
        createBtn.setBorderPainted(false);
        
        createBtn.addActionListener(e -> {
            String name = nameField.getText();
            String dob = dobField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();
            String password = new String(passField.getPassword());
            String deposit = depositField.getText();
            String type = (String) typeBox.getSelectedItem();

            if (name.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Password are required.");
                return;
            }

            // --- CALL BACKEND & CHECK RESULT ---
            boolean success = uiManager.createCustomerAccount(name, dob, type, phone, email, password);
            
            if (success) {
                String msg = "Customer Account Created!\nUsername: " + name.toLowerCase().replace(" ", "");
                if (!deposit.isEmpty() && !deposit.equals("0") && !deposit.equals("0.00")) {
                    msg += "\n\nNOTE: Please process the deposit of " + deposit + " manually.";
                }
                JOptionPane.showMessageDialog(this, msg);
                dispose();
                new TellerDashboard(uiManager);
            } else {
                // ERROR: DUPLICATE USER
                JOptionPane.showMessageDialog(this, 
                    "Error: A customer with this name already exists.\nUse 'Search for Customer' to add a new account instead.", 
                    "Creation Failed", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridy = 9;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        add(createBtn, gbc);

        // 5. Cancel Button
        JButton backBtn = new JButton("Cancel");
        backBtn.addActionListener(e -> {
            dispose();
            new TellerDashboard(uiManager);
        });
        gbc.gridy = 10;
        gbc.insets = new Insets(0, 10, 10, 10);
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