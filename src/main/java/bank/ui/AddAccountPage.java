package bank.ui;

import bank.UIManager;
import javax.swing.*;
import java.awt.*;

/**
 * A dialog form that allows a Teller to open a new account for an existing customer.
 * <p>
 * This screen is accessed via the {@link SearchCustomerPage} when a customer profile
 * is found. It collects the account type and initial deposit, then delegates the
 * creation to the {@link UIManager}.
 * </p>
 */
public class AddAccountPage extends JFrame {
    private final UIManager uiManager;

    /**
     * Constructs the Add Account form for a specific customer.
     *
     * @param manager      The application controller.
     * @param customerId   The username/ID of the existing customer.
     * @param customerName The full name of the customer (for display purposes).
     */
    public AddAccountPage(UIManager manager, String customerId, String customerName) {
        this.uiManager = manager;

        setTitle("MyBankUML - Add New Account");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Page Title
        JLabel header = new JLabel("Open New Account", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(header, gbc);

        // Customer Context Info
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        JLabel infoLabel = new JLabel("For Customer: " + customerName + " (" + customerId + ")", SwingConstants.CENTER);
        add(infoLabel, gbc);

        // Account Type Selection
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(new JLabel("Select Account Type:"), gbc);

        String[] types = {"Chequing", "Savings", "Credit Card"};
        JComboBox<String> typeBox = new JComboBox<>(types);
        gbc.gridx = 1;
        add(typeBox, gbc);

        // Initial Deposit Field
        gbc.gridy = 3;
        gbc.gridx = 0;
        add(new JLabel("Initial Deposit ($):"), gbc);

        JTextField depositField = new JTextField("0.00");
        gbc.gridx = 1;
        add(depositField, gbc);

        // Action Buttons
        JButton createBtn = new JButton("Open Account");
        createBtn.setBackground(new Color(144, 238, 144)); // Green
        createBtn.setOpaque(true);
        createBtn.setBorderPainted(false);
        
        createBtn.addActionListener(e -> {
            String type = (String) typeBox.getSelectedItem();
            String depositStr = depositField.getText();
            
            double amount = 0.0;
            try {
                amount = Double.parseDouble(depositStr.replace("$", "").trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid deposit amount.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Call backend to create the account with the initial balance
            uiManager.createNewAccount(customerId, type, amount);

            JOptionPane.showMessageDialog(this, type + " Account created successfully with $" + amount);
            dispose();
            
            // Return to Search Page to refresh the account list
            new SearchCustomerPage(uiManager);
        });

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        add(createBtn, gbc);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> {
            dispose();
            new SearchCustomerPage(uiManager);
        });
        
        gbc.gridy = 5;
        gbc.insets = new Insets(5, 10, 10, 10);
        add(cancelBtn, gbc);

        setVisible(true);
    }
}