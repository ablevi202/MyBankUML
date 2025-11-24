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

        // 2. Search Criteria
        gbc.gridy = 1;
        add(new JLabel("Search accounts by:"), gbc);

        String[] criteria = {"Account ID", "Name", "Phone"};
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
            String selectedCriteria = (String) criteriaBox.getSelectedItem();
            String keyword = keywordField.getText();
            // Show the specific Account Result Panel with REAL data
            showResultPanel(selectedCriteria, keyword);
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
    private void showResultPanel(String criteria, String keyword) {
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

        // Fetch Data from Backend
        String resultString = uiManager.searchAccounts(criteria, keyword);
        boolean found = resultString.startsWith("ID:");

        // Result Card
        JPanel accountCard = new JPanel(new GridLayout(3, 1));
        accountCard.setBorder(BorderFactory.createTitledBorder("Account Details"));
        accountCard.setBackground(new Color(240, 248, 255)); // Alice Blue
        
        String accountId = "";

        if (found) {
            // Parse the string returned by DatabaseManager
            // Format: "ID: <id>, Type: <type>, Owner: <owner>, Balance: $<balance>"
            try {
                String[] parts = resultString.split(", ");
                accountId = parts[0].substring(4); // Extract ID
                String type = parts[1].substring(6);
                String balance = parts[3].substring(9);

                accountCard.add(new JLabel("Account ID: " + accountId)); 
                accountCard.add(new JLabel("Type: " + type));
                accountCard.add(new JLabel("Current Balance: " + balance)); 
            } catch (Exception e) {
                accountCard.add(new JLabel("Error parsing account data."));
            }
        } else {
            accountCard.add(new JLabel(resultString)); // "No account found."
        }
        
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(accountCard, gbc);

        // Action Buttons
        if (found) {
            JButton viewBtn = new JButton("Manage This Account");
            viewBtn.setBackground(new Color(173, 216, 230)); // Light Blue
            viewBtn.setOpaque(true);
            viewBtn.setBorderPainted(false);
            
            // Final variable for lambda
            final String targetId = accountId;
            viewBtn.addActionListener(e -> {
                dispose();
                // Go to the Teller Account Action Page with the SPECIFIC ID
                // Note: You will need to update TellerAccountPage constructor to accept an ID!
                new TellerAccountPage(uiManager, targetId);
            });
            
            gbc.gridy = 2;
            add(viewBtn, gbc);
        }

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