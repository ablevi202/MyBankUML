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

/**
 * The UI screen for Tellers to execute fund transfers between accounts.
 * <p>
 * This page allows a Teller to initiate a transfer from a specific customer account
 * to another destination account. It handles input validation and communicates with
 * the {@link UIManager} to process the transaction or queue it for risk review.
 * </p>
 */
public class TellerTransactionPage extends JFrame {
    private final UIManager uiManager;
    private final String fromAccountId; // The account initiating the transfer

    /**
     * Constructs the Transaction form.
     *
     * @param manager   The application controller used to process the transfer.
     * @param accountId The ID of the source account (From Account).
     */
    public TellerTransactionPage(UIManager manager, String accountId) {
        this.uiManager = manager;
        this.fromAccountId = accountId;

        setTitle("MyBankUML - Make Transaction");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Page Header
        JLabel header = new JLabel("Teller - Make Transaction", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(header, gbc);

        // Transaction Type Dropdown
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(new JLabel("Transaction Type:"), gbc);
        
        String[] types = {"Transfer", "Bill Payment"};
        JComboBox<String> typeBox = new JComboBox<>(types);
        gbc.gridx = 1;
        add(typeBox, gbc);

        // From Account Field (Read-Only)
        gbc.gridy = 2;
        gbc.gridx = 0;
        add(new JLabel("From Account ID:"), gbc);
        
        JTextField fromField = new JTextField(fromAccountId); 
        fromField.setEditable(false); // Lock this field to ensure context safety
        gbc.gridx = 1;
        add(fromField, gbc);

        // Destination Account Field
        gbc.gridy = 3;
        gbc.gridx = 0;
        add(new JLabel("To Account ID:"), gbc);
        
        JTextField toField = new JTextField();
        gbc.gridx = 1;
        add(toField, gbc);

        // Amount Field
        gbc.gridy = 4;
        gbc.gridx = 0;
        add(new JLabel("Amount:"), gbc);
        
        JTextField amountField = new JTextField("$0.00");
        gbc.gridx = 1;
        add(amountField, gbc);

        // Submit Button
        JButton submitBtn = new JButton("Complete Transaction");
        submitBtn.setBackground(new Color(144, 238, 144)); // Green
        submitBtn.setOpaque(true);
        submitBtn.setBorderPainted(false);
        
        submitBtn.addActionListener(e -> {
            String toAcc = toField.getText();
            String amount = amountField.getText();
            
            if (toAcc.isEmpty() || amount.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }

            // Call backend and handle the returned status string
            String status = uiManager.performTransfer(fromAccountId, toAcc, amount);
            
            if ("SUCCESS".equals(status)) {
                JOptionPane.showMessageDialog(this, "Transaction Successful!");
                dispose();
                new TellerAccountPage(uiManager, fromAccountId);
            } else if ("PENDING".equals(status)) {
                JOptionPane.showMessageDialog(this, "Transaction queued for review (Amount > $10,000).");
                dispose();
                new TellerAccountPage(uiManager, fromAccountId);
            } else if ("INSUFFICIENT".equals(status)) {
                JOptionPane.showMessageDialog(this, "Error: Insufficient Funds!", "Failed", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error: Transaction failed (Check Account ID).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        add(submitBtn, gbc);

        // Cancel Button
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> {
            dispose();
            new TellerAccountPage(uiManager, fromAccountId);
        });
        
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 10, 10, 10);
        add(cancelBtn, gbc);

        setVisible(true);
    }
}