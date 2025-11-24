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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import bank.UIManager;

public class CreateEmployeePage extends JFrame {
    private UIManager uiManager;

    public CreateEmployeePage(UIManager manager) {
        this.uiManager = manager;

        setTitle("MyBankUML - Create Employee");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // 1. Header
        JLabel headerLabel = new JLabel("Create New Employee Account", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(headerLabel, gbc);

        // 2. Back Button
        JButton backButton = new JButton("Back to Dashboard");
        backButton.setBackground(new Color(255, 102, 102)); // Light Red
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> {
            dispose();
            new AdminDashboard(uiManager);
        });

        // 3. Username Field
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 2, 10);
        JLabel userLbl = new JLabel("New Account Username");
        userLbl.setFont(new Font("Arial", Font.BOLD, 12));
        add(userLbl, gbc);

        JTextField userField = new JTextField();
        gbc.gridy = 2;
        gbc.insets = new Insets(2, 10, 10, 10);
        add(userField, gbc);

        // 4. Password Field
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 10, 2, 10);
        JLabel passLbl = new JLabel("New Account Password");
        passLbl.setFont(new Font("Arial", Font.BOLD, 12));
        add(passLbl, gbc);

        JPasswordField passField = new JPasswordField();
        gbc.gridy = 4;
        gbc.insets = new Insets(2, 10, 10, 10);
        add(passField, gbc);

        // 5. Enter Button
        JButton enterButton = new JButton("Enter");
        enterButton.setBackground(new Color(100, 149, 237)); // Cornflower Blue
        enterButton.setOpaque(true);
        enterButton.setBorderPainted(false);
        enterButton.setForeground(Color.WHITE);
        
        enterButton.addActionListener(e -> {
            String newUser = userField.getText();
            String newPass = new String(passField.getPassword());
            
            // Basic Validation
            if (newUser.isEmpty() || newPass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in both fields.");
                return;
            }

            // --- CONNECT TO BACKEND ---
            // Saves the new Teller to the database
            uiManager.createEmployee(newUser, newPass);
            
            dispose();
            new AdminStatusPage(uiManager, newUser, newPass);
        });

        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 10, 10);
        add(enterButton, gbc);
        
        // Back Button position
        gbc.gridy = 6;
        add(backButton, gbc);

        setVisible(true);
    }
}