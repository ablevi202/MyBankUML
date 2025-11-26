package bank.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import bank.UIManager;

/**
 * A confirmation screen displayed after an Admin successfully creates a new employee.
 * <p>
 * This page provides immediate feedback, showing the generated credentials for the
 * new account before allowing the Admin to return to the dashboard.
 * </p>
 */
public class AdminStatusPage extends JFrame {
    private final UIManager uiManager;

    /**
     * Constructs the status confirmation page.
     *
     * @param manager  The application controller.
     * @param username The username of the newly created employee.
     * @param password The password of the newly created employee.
     */
    public AdminStatusPage(UIManager manager, String username, String password) {
        this.uiManager = manager;

        setTitle("MyBankUML - Employee Created");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.gridx = 0;

        // Success Message
        JLabel successLabel = new JLabel("Account creation was successful.", SwingConstants.CENTER);
        successLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(successLabel, gbc);

        // Account Details Display
        JLabel userLabel = new JLabel("New username: " + username);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userLabel.setForeground(Color.GRAY);
        gbc.gridy = 1;
        add(userLabel, gbc);

        JLabel passLabel = new JLabel("New password: " + password);
        passLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passLabel.setForeground(Color.GRAY);
        gbc.gridy = 2;
        add(passLabel, gbc);

        // Navigation
        JButton backButton = new JButton("Back to Dashboard");
        backButton.setBackground(new Color(100, 149, 237)); // Cornflower Blue
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.setForeground(Color.WHITE);
        
        backButton.addActionListener(e -> {
            dispose();
            new AdminDashboard(uiManager);
        });

        gbc.gridy = 3;
        gbc.insets = new Insets(30, 10, 10, 10);
        add(backButton, gbc);

        setVisible(true);
    }
}