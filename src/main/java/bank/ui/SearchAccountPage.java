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

/**
 * The UI screen for searching specifically for Bank Accounts.
 * <p>
 * This page allows Tellers to look up accounts using criteria such as Account ID,
 * Customer Name, or Phone Number. Unlike the Customer Search, this result focuses
 * on financial ledger details (Balance, Type) rather than personal profile info.
 * </p>
 */
public class SearchAccountPage extends JFrame {
    private final UIManager uiManager;

    /**
     * Constructs the Search Account form.
     *
     * @param manager The application controller used to execute search queries.
     */
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

        // Page Header
        JLabel headerLabel = new JLabel("Search Menu", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        add(headerLabel, gbc);

        // Criteria Selection
        gbc.gridy = 1;
        add(new JLabel("Search accounts by:"), gbc);

        String[] criteria = {"Account ID", "Name", "Phone"};
        JComboBox<String> criteriaBox = new JComboBox<>(criteria);
        gbc.gridy = 2;
        add(criteriaBox, gbc);

        // Keyword Input
        gbc.gridy = 3;
        add(new JLabel("Search keywords:"), gbc);

        JTextField keywordField = new JTextField();
        gbc.gridy = 4;
        add(keywordField, gbc);

        // Search Button
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(100, 149, 237)); // Cornflower Blue
        searchButton.setOpaque(true);
        searchButton.setBorderPainted(false);
        searchButton.setForeground(Color.WHITE);
        
        searchButton.addActionListener(e -> {
            String selectedCriteria = (String) criteriaBox.getSelectedItem();
            String keyword = keywordField.getText();
            
            // Switch view to show results
            showResultPanel(selectedCriteria, keyword);
        });

        gbc.gridy = 5;
        gbc.insets = new Insets(20, 10, 10, 10);
        add(searchButton, gbc);

        // Navigation Button
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

    /**
     * Clears the current form and displays the search results.
     *
     * @param criteria The field to search by.
     * @param keyword  The value to search for.
     */
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

        // Execute Search
        String resultString = uiManager.searchAccounts(criteria, keyword);
        boolean found = resultString.startsWith("ID:");

        // Result Display Card
        JPanel accountCard = new JPanel(new GridLayout(3, 1));
        accountCard.setBorder(BorderFactory.createTitledBorder("Account Details"));
        accountCard.setBackground(new Color(240, 248, 255)); // Alice Blue
        
        String accountId = "";

        if (found) {
            // Parse the raw string returned by the backend
            // Expected Format: "ID: <id>, Type: <type>, Owner: <owner>, Balance: $<balance>"
            try {
                String[] parts = resultString.split(", ");
                accountId = parts[0].substring(4); // Extract ID after "ID: "
                String type = parts[1].substring(6);
                String balance = parts[3].substring(9);

                accountCard.add(new JLabel("Account ID: " + accountId)); 
                accountCard.add(new JLabel("Type: " + type));
                accountCard.add(new JLabel("Current Balance: " + balance)); 
            } catch (Exception e) {
                accountCard.add(new JLabel("Error parsing account data."));
            }
        } else {
            accountCard.add(new JLabel(resultString)); // Display "No account found."
        }
        
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(accountCard, gbc);

        // Action Button: Manage Account (Only if found)
        if (found) {
            JButton viewBtn = new JButton("Manage This Account");
            viewBtn.setBackground(new Color(173, 216, 230)); // Light Blue
            viewBtn.setOpaque(true);
            viewBtn.setBorderPainted(false);
            
            final String targetId = accountId;
            viewBtn.addActionListener(e -> {
                dispose();
                // Navigate to the management page for this specific account
                new TellerAccountPage(uiManager, targetId);
            });
            
            gbc.gridy = 2;
            add(viewBtn, gbc);
        }

        // Navigation: Reset Search
        JButton backButton = new JButton("Back to Search");
        backButton.setBackground(new Color(255, 102, 102));
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> {
            dispose();
            new SearchAccountPage(uiManager); // Reload the search form
        });

        gbc.gridy = 3;
        add(backButton, gbc);

        revalidate();
        repaint();
    }
}