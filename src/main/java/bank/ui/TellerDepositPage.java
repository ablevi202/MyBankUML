package bank.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import bank.UIManager;

/**
 * The UI screen for processing cash deposits into a specific account.
 * <p>
 * This page allows Tellers to input a deposit amount for a customer. It communicates
 * with the {@link UIManager} to update the balance or queue the transaction for review
 * if it exceeds risk thresholds.
 * </p>
 */
public class TellerDepositPage extends JFrame {
    private final UIManager uiManager;
    private final String accountId; // The account receiving funds

    /**
     * Constructs the Deposit form.
     *
     * @param manager   The application controller used to process the transaction.
     * @param accountId The ID of the account where funds will be deposited.
     */
    public TellerDepositPage(UIManager manager, String accountId) {
        this.uiManager = manager;
        this.accountId = accountId;

        setTitle("MyBankUML - Deposit Menu");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Page Header
        JLabel headerLabel = new JLabel("Deposit Menu", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        add(headerLabel, gbc);

        // Account Context Label
        JLabel accountLabel = new JLabel("Depositing to Account: " + accountId, SwingConstants.CENTER);
        accountLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy = 1;
        add(accountLabel, gbc);

        // Amount Input
        gbc.gridy = 2;
        JLabel amountLbl = new JLabel("Deposit Amount:", SwingConstants.LEFT);
        amountLbl.setFont(new Font("Arial", Font.BOLD, 12));
        add(amountLbl, gbc);

        JTextField amountField = new JTextField("$0.00");
        gbc.gridy = 3;
        add(amountField, gbc);

        // Complete Button
        JButton completeButton = new JButton("Complete Deposit");
        completeButton.setBackground(new Color(173, 216, 230)); // Light Blue
        completeButton.setOpaque(true);
        completeButton.setBorderPainted(false);
        
        completeButton.addActionListener(e -> {
            String amount = amountField.getText();
            
            // Call backend and handle status response (Success vs Pending vs Error)
            String status = uiManager.processDeposit(accountId, amount);
            
            if ("SUCCESS".equals(status)) {
                JOptionPane.showMessageDialog(this, "Deposit Successful!");
                dispose();
                new TellerAccountPage(uiManager, accountId);
            } else if ("PENDING".equals(status)) {
                JOptionPane.showMessageDialog(this, "Large Deposit Queued for Review.");
                dispose();
                new TellerAccountPage(uiManager, accountId);
            } else {
                JOptionPane.showMessageDialog(this, "Error: Invalid Amount.", "Deposit Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridy = 4;
        gbc.insets = new Insets(20, 10, 10, 10);
        add(completeButton, gbc);

        // Navigation Button
        JButton backButton = new JButton("Back to Account");
        backButton.setBackground(new Color(255, 102, 102)); // Light Red
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        
        backButton.addActionListener(e -> {
            dispose();
            new TellerAccountPage(uiManager, accountId);
        });

        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(backButton, gbc);

        setVisible(true);
    }
}