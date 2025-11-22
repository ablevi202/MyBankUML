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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import bank.UIManager;

public class SearchCustomerPage extends JFrame {
    private UIManager uiManager;

    public SearchCustomerPage(UIManager manager) {
        this.uiManager = manager;

        setTitle("MyBankUML - Search Customers");
        setSize(500, 450); // Increased height for result panel
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // 1. Header
        JLabel headerLabel = new JLabel("Search Customer Profiles", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        add(headerLabel, gbc);

        // 2. Search Criteria ( Distinct attributes for Customer)
        gbc.gridy = 1;
        add(new JLabel("Search by:"), gbc);

        // "Place of Birth" is the key differentiator (Report Page 67)
        String[] criteria = {"Name", "Place of Birth", "Customer ID", "Phone Number"};
        JComboBox<String> criteriaBox = new JComboBox<>(criteria);
        gbc.gridy = 2;
        add(criteriaBox, gbc);

        // 3. Keywords
        gbc.gridy = 3;
        add(new JLabel("Keywords:"), gbc);

        JTextField keywordField = new JTextField();
        gbc.gridy = 4;
        add(keywordField, gbc);

        // 4. Search Button
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(100, 149, 237));
        searchButton.setOpaque(true);
        searchButton.setBorderPainted(false);
        searchButton.setForeground(Color.WHITE);
        
        searchButton.addActionListener(e -> {
            // Show the Customer Result Panel (Visual Distinction)
            showResultPanel(keywordField.getText());
        });

        gbc.gridy = 5;
        gbc.insets = new Insets(20, 10, 10, 10);
        add(searchButton, gbc);

        // 5. Back Button
        JButton backButton = new JButton("Back to Dashboard");
        backButton.setBackground(new Color(255, 102, 102));
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

    // --- VISUAL DISTINCTION: Shows User Profile Data ---
    private void showResultPanel(String keyword) {
        getContentPane().removeAll();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Header
        JLabel header = new JLabel("Customer Profile Found", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(header, gbc);

        // Customer Details Card (Distinct from Account Details)
        JPanel profileCard = new JPanel(new GridLayout(3, 1));
        profileCard.setBorder(BorderFactory.createTitledBorder("Personal Details"));
        
        // Mock Data to show "Person" attributes
        profileCard.add(new JLabel("Name: Macy Sorenson"));
        profileCard.add(new JLabel("DOB: 1995-08-24"));
        profileCard.add(new JLabel("Place of Birth: Montreal, QC")); // Distinct attribute
        
        gbc.gridy = 1;
        add(profileCard, gbc);

        // Associated Accounts List (Customers have multiple accounts)
        JLabel accountsLbl = new JLabel("Associated Accounts:");
        gbc.gridy = 2;
        add(accountsLbl, gbc);

        JList<String> accountList = new JList<>(new String[]{"Chequing (ID: 853013)", "Savings (ID: 853999)"});
        accountList.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        gbc.gridy = 3;
        add(accountList, gbc);

        // Action Button
        JButton viewBtn = new JButton("View Selected Account");
        viewBtn.setBackground(new Color(173, 216, 230));
        viewBtn.setOpaque(true);
        viewBtn.setBorderPainted(false);
        viewBtn.addActionListener(e -> {
            dispose();
            // Go to Account Action page for the selected account
            new TellerAccountPage(uiManager);
        });
        
        gbc.gridy = 4;
        add(viewBtn, gbc);

        // Back Button
        JButton backButton = new JButton("Back to Search");
        backButton.addActionListener(e -> {
            dispose();
            new SearchCustomerPage(uiManager);
        });
        gbc.gridy = 5;
        add(backButton, gbc);

        revalidate();
        repaint();
    }
}