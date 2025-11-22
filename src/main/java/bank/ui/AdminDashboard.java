package bank.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import bank.UIManager;

public class AdminDashboard extends JFrame {
    private UIManager uiManager;

    public AdminDashboard(UIManager manager) {
        this.uiManager = manager;

        setTitle("MyBankUML - Admin Dashboard");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // 1. Welcome Message
        JLabel welcomeLabel = new JLabel("Welcome Admin!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridy = 0;
        add(welcomeLabel, gbc);

        // 2. Menu Buttons
        JButton manageBtn = createButton("Manage Employee Accounts", new Color(173, 216, 230));
        // Link to Manage Employees Page
        manageBtn.addActionListener(e -> {
            dispose();
            new ManageEmployeesPage(uiManager);
        });
        gbc.gridy = 1;
        add(manageBtn, gbc);

        JButton searchBtn = createButton("Search Employee Accounts", new Color(173, 216, 230));
        searchBtn.addActionListener(e -> {
            dispose();
            new SearchEmployeePage(uiManager);
        });
        gbc.gridy = 2;
        add(searchBtn, gbc);

        JButton reviewBtn = createButton("Review Pending Transactions", new Color(144, 238, 144));
        reviewBtn.addActionListener(e -> {
            dispose();
            new ReviewPendingTransactionsPage(uiManager);
        });
        gbc.gridy = 3;
        add(reviewBtn, gbc);

        // 3. Logout
        JButton logoutButton = new JButton("Log out");
        logoutButton.setBackground(new Color(255, 102, 102)); // Red
        logoutButton.setOpaque(true);
        logoutButton.setBorderPainted(false);
        logoutButton.addActionListener(e -> {
            uiManager.logout();
            dispose();
            new LoginScreen(uiManager);
        });
        
        gbc.gridy = 4;
        add(logoutButton, gbc);

        setVisible(true);
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(300, 40));
        return btn;
    }
}