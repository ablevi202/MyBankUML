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

/**
 * The initial entry point for the application GUI.
 * <p>
 * This screen handles user authentication by collecting credentials and
 * validating them via the {@link UIManager}. Upon successful login, it directs
 * the user to the appropriate dashboard based on their role (Customer, Teller, or Admin).
 * </p>
 */
public class LoginScreen extends JFrame {
    private final UIManager uiManager;

    /**
     * Constructs the Login Screen.
     *
     * @param manager The application controller used for authentication logic.
     */
    public LoginScreen(UIManager manager) {
        this.uiManager = manager;
        
        // Window Setup
        setTitle("MyBankUML - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen

        // Layout Configuration
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Standard padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Header Label
        JLabel headerLabel = new JLabel("Please enter your login information:", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(headerLabel, gbc);

        // Username Field
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        
        gbc.gridx = 0;
        add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1;
        JTextField userField = new JTextField(15);
        add(userField, gbc);

        // Password Field
        gbc.gridy = 2;
        
        gbc.gridx = 0;
        add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        JPasswordField passField = new JPasswordField(15);
        add(passField, gbc);

        // Login Button
        JButton loginButton = new JButton("Enter");
        getRootPane().setDefaultButton(loginButton); // Enable 'Enter' key shortcut
        
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2; 
        gbc.fill = GridBagConstraints.NONE; 
        gbc.anchor = GridBagConstraints.CENTER; 
        add(loginButton, gbc);

        // Authentication Logic
        loginButton.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            
            // Validate credentials against the database
            if (uiManager.handleLogin(user, pass)) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                dispose(); 

                // Route to the correct dashboard based on User Role
                String role = uiManager.getDashboardType(user);
                
                if ("ADMIN".equals(role)) {
                    new AdminDashboard(uiManager);
                } else if ("TELLER".equals(role)) {
                    new TellerDashboard(uiManager);
                } else {
                    new CustomerDashboard(uiManager);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
                passField.setText(""); // Reset password field
            }
        });

        setVisible(true);
    }
}