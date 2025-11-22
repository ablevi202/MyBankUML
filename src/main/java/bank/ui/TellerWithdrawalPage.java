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

public class TellerWithdrawalPage extends JFrame {
    private UIManager uiManager;

    public TellerWithdrawalPage(UIManager manager) {
        this.uiManager = manager;

        setTitle("MyBankUML - Withdrawal Menu");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // 1. Header (Matches "Withdrawal Menu")
        JLabel headerLabel = new JLabel("Withdrawal Menu", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        add(headerLabel, gbc);

        // 2. Amount Label & Field
        gbc.gridy = 1;
        JLabel amountLbl = new JLabel("Withdrawal Amount:", SwingConstants.LEFT);
        amountLbl.setFont(new Font("Arial", Font.BOLD, 12));
        add(amountLbl, gbc);

        JTextField amountField = new JTextField("$0.00");
        gbc.gridy = 2;
        add(amountField, gbc);

        // 3. Complete Button (Green)
        JButton completeButton = new JButton("Complete Withdrawal");
        completeButton.setBackground(new Color(144, 238, 144)); // Light Green
        completeButton.setOpaque(true);
        completeButton.setBorderPainted(false);
        
        completeButton.addActionListener(e -> {
            String amount = amountField.getText();
            // Call Facade
            uiManager.processWithdrawal("853031", amount); // Mock Account ID
            
            JOptionPane.showMessageDialog(this, "Withdrawal Successful!");
            dispose();
            new TellerAccountPage(uiManager);
        });

        gbc.gridy = 3;
        gbc.insets = new Insets(20, 10, 10, 10);
        add(completeButton, gbc);

        // 4. Back Button (Red)
        JButton backButton = new JButton("Back to Account");
        backButton.setBackground(new Color(255, 102, 102)); // Light Red
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        
        backButton.addActionListener(e -> {
            dispose();
            new TellerAccountPage(uiManager);
        });

        // Place Back button at top-right or bottom? 
        // Report Page 57 shows it at Top Right, but standard GridBag places it easier at bottom.
        // We will place it at the bottom for consistency with other screens.
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(backButton, gbc);

        setVisible(true);
    }
}