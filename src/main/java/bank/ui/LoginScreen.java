package bank.ui;

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

public class LoginScreen extends JFrame {
    private UIManager uiManager;

    public LoginScreen(UIManager manager) {
        this.uiManager = manager;
        
        // 1. Window Setup
        setTitle("MyBankUML - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centers the window on screen

        // 2. Layout Setup
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 3. Header Label
        JLabel headerLabel = new JLabel("Please enter your login information:", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(headerLabel, gbc);

        // 4. Username Row
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        
        gbc.gridx = 0;
        add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1;
        JTextField userField = new JTextField(15);
        add(userField, gbc);

        // 5. Password Row
        gbc.gridy = 2;
        
        gbc.gridx = 0;
        add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        JPasswordField passField = new JPasswordField(15);
        add(passField, gbc);

        // 6. Enter Button
        JButton loginButton = new JButton("Enter");
        getRootPane().setDefaultButton(loginButton); // Allow pressing 'Enter' key to login
        
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2; 
        gbc.fill = GridBagConstraints.NONE; 
        gbc.anchor = GridBagConstraints.CENTER; 
        add(loginButton, gbc);

        // 7. Interaction Logic
        loginButton.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            
            // Validate Credentials via Facade (Connected to DB)
            if (uiManager.handleLogin(user, pass)) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                dispose(); 

                // Determine Role and Launch Dashboard
                // Note: Ensure UIManager uses DB roles for this to be 100% dynamic
                String role = uiManager.getDashboardType(user);
                
                if ("ADMIN".equals(role)) {
                    new AdminDashboard(uiManager);
                } else if ("TELLER".equals(role)) {
                    new TellerDashboard(uiManager);
                } else {
                    new CustomerDashboard(uiManager);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials");
                passField.setText(""); // Clear password field on failure
            }
        });

        setVisible(true);
    }
}