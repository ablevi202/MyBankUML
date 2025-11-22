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

public class SearchEmployeePage extends JFrame {
    private UIManager uiManager;

    public SearchEmployeePage(UIManager manager) {
        this.uiManager = manager;

        setTitle("MyBankUML - Search Employee Accounts");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // 1. Header
        JLabel headerLabel = new JLabel("Search Employee Accounts", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        add(headerLabel, gbc);

        // 2. Search By Dropdown
        gbc.gridy = 1;
        add(new JLabel("Search accounts by:"), gbc);

        String[] searchOptions = {"Name", "Employee ID", "Username"};
        JComboBox<String> searchBox = new JComboBox<>(searchOptions);
        gbc.gridy = 2;
        add(searchBox, gbc);

        // 3. Keywords Field
        gbc.gridy = 3;
        add(new JLabel("Search keywords:"), gbc);

        JTextField keywordField = new JTextField();
        gbc.gridy = 4;
        add(keywordField, gbc);

        // 4. Search Button
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(100, 149, 237)); // Cornflower Blue
        searchButton.setOpaque(true);
        searchButton.setBorderPainted(false);
        searchButton.setForeground(Color.WHITE);
        
        searchButton.addActionListener(e -> {
            String criteria = (String) searchBox.getSelectedItem();
            String keyword = keywordField.getText();
            
            // Call Facade to search
            String result = uiManager.searchEmployees(criteria, keyword);
            JOptionPane.showMessageDialog(this, result);
        });

        gbc.gridy = 5;
        gbc.insets = new Insets(20, 10, 10, 10);
        add(searchButton, gbc);

        // 5. Back Button
        JButton backButton = new JButton("Back to Dashboard");
        backButton.setBackground(new Color(255, 102, 102)); // Light Red
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        
        backButton.addActionListener(e -> {
            dispose();
            new AdminDashboard(uiManager);
        });

        gbc.gridy = 6;
        add(backButton, gbc);

        setVisible(true);
    }
}