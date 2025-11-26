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

/**
 * A confirmation screen displayed after a successful transaction.
 * <p>
 * This page provides feedback to the customer, summarizing the details of the
 * transfer (amount, source, and destination) before allowing them to return
 * to the dashboard.
 * </p>
 */
public class TransactionStatusPage extends JFrame {
    private final UIManager uiManager;

    /**
     * Constructs the Transaction Status page.
     *
     * @param manager     The application controller used for navigation.
     * @param fromAccount The ID of the account the funds were taken from.
     * @param toAccount   The ID of the account receiving the funds.
     * @param amount      The amount transferred.
     */
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

        // Success Header
        JLabel successLabel = new JLabel("Your transaction was successful!");
        successLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(successLabel, gbc);

        // Transaction Details
        String detailsText = amount + " from your account " + fromAccount + " to account " + toAccount + ".";
        JLabel detailsLabel = new JLabel(detailsText);
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 1;
        add(detailsLabel, gbc);

        // Navigation Button
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