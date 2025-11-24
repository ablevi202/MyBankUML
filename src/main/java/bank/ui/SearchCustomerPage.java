package bank.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import bank.UIManager;

public class SearchCustomerPage extends JFrame {
    private UIManager uiManager;

    public SearchCustomerPage(UIManager manager) {
        this.uiManager = manager;

        setTitle("MyBankUML - Search Customers");
        setSize(500, 550); // Increased height to fit the new button
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

        // 2. Search Criteria
        gbc.gridy = 1;
        add(new JLabel("Search by:"), gbc);

        // UPDATED: Removed "Place of Birth", Added "Date of Birth"
        String[] criteria = {"Name", "Date of Birth", "Customer ID"};
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
        searchButton.setBackground(new Color(100, 149, 237)); // Cornflower Blue
        searchButton.setOpaque(true);
        searchButton.setBorderPainted(false);
        searchButton.setForeground(Color.WHITE);
        
        searchButton.addActionListener(e -> {
            showResultPanel((String) criteriaBox.getSelectedItem(), keywordField.getText());
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

    private void showResultPanel(String criteria, String keyword) {
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

        String resultString = uiManager.searchCustomers(criteria, keyword);
        boolean found = resultString.startsWith("Found:");

        // Details Card
        JPanel profileCard = new JPanel(new GridLayout(0, 1));
        profileCard.setBorder(BorderFactory.createTitledBorder("Personal Details"));
        
        String customerId = "";
        String customerName = "Customer";

        if (found) {
            try {
                // Parse Format: "Found: Name (ID: username) [Status] {DOB: Date}"
                customerName = resultString.substring(7, resultString.indexOf(" (ID:"));
                
                int idStart = resultString.indexOf("(ID: ") + 5;
                int idEnd = resultString.indexOf(") [");
                customerId = resultString.substring(idStart, idEnd);
                
                String status = resultString.substring(resultString.indexOf("[") + 1, resultString.indexOf("]"));
                
                // Extract DOB (Replaces POB logic)
                int dobStart = resultString.indexOf("DOB: ") + 5;
                int dobEnd = resultString.indexOf("}");
                String dob = resultString.substring(dobStart, dobEnd);

                profileCard.add(new JLabel("Name: " + customerName));
                profileCard.add(new JLabel("Customer ID: " + customerId));
                profileCard.add(new JLabel("Status: " + status));
                profileCard.add(new JLabel("Date of Birth: " + dob));
            } catch (Exception e) {
                profileCard.add(new JLabel(resultString)); // Fallback if parsing fails
            }
        } else {
            profileCard.add(new JLabel("No result found."));
        }
        
        gbc.gridy = 1;
        add(profileCard, gbc);

        // Associated Accounts List
        JLabel accountsLbl = new JLabel("Associated Accounts:");
        gbc.gridy = 2;
        add(accountsLbl, gbc);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> accountList = new JList<>(listModel);
        accountList.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        if (found && !customerId.isEmpty()) {
            List<String[]> accounts = uiManager.getCustomerAccounts(customerId);
            if (accounts.isEmpty()) {
                listModel.addElement("No accounts found for this user.");
            } else {
                for (String[] acc : accounts) {
                    // Format: "Chequing (ID: 853013) - $5000.00"
                    listModel.addElement(acc[1] + " (ID: " + acc[0] + ") - $" + acc[2]);
                }
            }
        }
        
        JScrollPane listScroll = new JScrollPane(accountList);
        listScroll.setPreferredSize(new Dimension(400, 100));
        gbc.gridy = 3;
        add(listScroll, gbc);

        // Button 1: View/Manage Selected Account
        JButton viewBtn = new JButton("Manage Selected Account");
        viewBtn.setBackground(new Color(173, 216, 230));
        viewBtn.setOpaque(true);
        viewBtn.setBorderPainted(false);
        
        viewBtn.addActionListener(e -> {
            String selected = accountList.getSelectedValue();
            if (selected != null && selected.contains("(ID:")) {
                try {
                    int start = selected.indexOf("(ID: ") + 5;
                    int end = selected.indexOf(")");
                    String accId = selected.substring(start, end);
                    dispose();
                    new TellerAccountPage(uiManager, accId);
                } catch (Exception ex) {
                     JOptionPane.showMessageDialog(this, "Error selecting account.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an account from the list.");
            }
        });
        
        gbc.gridy = 4;
        add(viewBtn, gbc);

        // Button 2: Open New Account
        if (found && !customerId.isEmpty()) {
            JButton addAccBtn = new JButton("Open New Account");
            addAccBtn.setBackground(new Color(144, 238, 144)); // Green
            addAccBtn.setOpaque(true);
            addAccBtn.setBorderPainted(false);
            
            final String targetId = customerId;
            final String targetName = customerName;

            addAccBtn.addActionListener(e -> {
                dispose();
                new AddAccountPage(uiManager, targetId, targetName);
            });

            gbc.gridy = 5;
            add(addAccBtn, gbc);
        }

        // Back Button
        JButton backButton = new JButton("Back to Search");
        backButton.addActionListener(e -> {
            dispose();
            new SearchCustomerPage(uiManager);
        });
        gbc.gridy = 6;
        add(backButton, gbc);

        revalidate();
        repaint();
    }
}