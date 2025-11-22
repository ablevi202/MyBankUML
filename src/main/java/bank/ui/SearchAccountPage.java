package bank.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import bank.UIManager;

public class SearchAccountPage extends JFrame {
    private UIManager uiManager;

    public SearchAccountPage(UIManager manager) {
        this.uiManager = manager;

        setTitle("MyBankUML - Search Accounts");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // 1. Header
        JLabel headerLabel = new JLabel("Search Menu", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        add(headerLabel, gbc);

        // 2. Search Criteria (Attributes specific to Account Ledgers)
        gbc.gridy = 1;
        add(new JLabel("Search accounts by:"), gbc);

        String[] criteria = {"Name", "Account ID", "Phone"};
        JComboBox<String> criteriaBox = new JComboBox<>(criteria);
        gbc.gridy = 2;
        add(criteriaBox, gbc);

        // 3. Keywords
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
            // Show the specific Account Result Panel
            showResultPanel(keywordField.getText());
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
            new TellerDashboard(uiManager);
        });

        gbc.gridy = 6;
        add(backButton, gbc);

        setVisible(true);
    }

    // --- RESULT VIEW: Focuses on Financial Details ---
    private void showResultPanel(String keyword) {
        getContentPane().removeAll();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Header
        JLabel header = new JLabel("Account Search Result", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(header, gbc);

        // Account Details Card (Visually distinct - Light Blue Background)
        JPanel accountCard = new JPanel(new GridLayout(3, 1));
        accountCard.setBorder(BorderFactory.createTitledBorder("Account Details"));
        accountCard.setBackground(new Color(240, 248, 255)); // Alice Blue
        
        // Mock Data focusing on the Asset (Account) rather than the Person
        accountCard.add(new JLabel("Account ID: 853013")); 
        accountCard.add(new JLabel("Type: Chequing"));
        accountCard.add(new JLabel("Current Balance: $912.98")); 
        
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(accountCard, gbc);

        // Action Button
        JButton viewBtn = new JButton("Manage This Account");
        viewBtn.setBackground(new Color(173, 216, 230)); // Light Blue
        viewBtn.setOpaque(true);
        viewBtn.setBorderPainted(false);
        viewBtn.addActionListener(e -> {
            dispose();
            // Go to the Teller Account Action Page (Deposit/Withdraw)
            new TellerAccountPage(uiManager);
        });
        
        gbc.gridy = 2;
        add(viewBtn, gbc);

        // Back Button
        JButton backButton = new JButton("Back to Search");
        backButton.setBackground(new Color(255, 102, 102));
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> {
            dispose();
            new SearchAccountPage(uiManager); // Reset page
        });

        gbc.gridy = 3;
        add(backButton, gbc);

        revalidate();
        repaint();
    }
}