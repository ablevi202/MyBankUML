package bank.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import bank.UIManager;

public class TransactionStatusPage extends JFrame {
    private UIManager uiManager;

    // Updated Constructor: Added 'fromAccount' parameter
    public TransactionStatusPage(UIManager manager, String fromAccount, String toAccount, String amount) {
        this.uiManager = manager;

        setTitle("MyBankUML - Transaction Status");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        // 1. Success Message
        JLabel successLabel = new JLabel("Your transaction was successful!");
        successLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(successLabel, gbc);

        // 2. Details
        // Now uses the real 'fromAccount' passed from the previous page
        String detailsText = amount + " from your account " + fromAccount + " to account " + toAccount + ".";
        JLabel detailsLabel = new JLabel(detailsText);
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 1;
        add(detailsLabel, gbc);

        // 3. Back Button
        JButton backButton = new JButton("Back to Dashboard");
        backButton.setBackground(new Color(100, 149, 237)); // Cornflower Blue
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            dispose();
            new CustomerDashboard(uiManager);
        });

        gbc.gridy = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        add(backButton, gbc);

        setVisible(true);
    }
}