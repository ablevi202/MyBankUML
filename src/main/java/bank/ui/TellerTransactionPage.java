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

public class TellerTransactionPage extends JFrame {
    private UIManager uiManager;

    public TellerTransactionPage(UIManager manager) {
        this.uiManager = manager;

        setTitle("MyBankUML - Make Transaction");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // 1. Header
        JLabel header = new JLabel("Teller - Make Transaction", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(header, gbc);

        // 2. Transaction Type
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(new JLabel("Transaction Type:"), gbc);
        String[] types = {"Transfer", "Bill Payment"};
        JComboBox<String> typeBox = new JComboBox<>(types);
        gbc.gridx = 1;
        add(typeBox, gbc);

        // 3. From Account (Auto-filled or selected)
        gbc.gridy = 2;
        gbc.gridx = 0;
        add(new JLabel("From Account ID:"), gbc);
        JTextField fromField = new JTextField("853031"); // Pre-filled for demo
        gbc.gridx = 1;
        add(fromField, gbc);

        // 4. To Account
        gbc.gridy = 3;
        gbc.gridx = 0;
        add(new JLabel("To Account ID:"), gbc);
        JTextField toField = new JTextField();
        gbc.gridx = 1;
        add(toField, gbc);

        // 5. Amount
        gbc.gridy = 4;
        gbc.gridx = 0;
        add(new JLabel("Amount:"), gbc);
        JTextField amountField = new JTextField("$0.00");
        gbc.gridx = 1;
        add(amountField, gbc);

        // 6. Submit Button
        JButton submitBtn = new JButton("Complete Transaction");
        submitBtn.setBackground(new Color(144, 238, 144));
        submitBtn.setOpaque(true);
        submitBtn.setBorderPainted(false);
        submitBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Transaction Successful!");
            dispose();
            new TellerAccountPage(uiManager); // Go back to account view
        });

        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        add(submitBtn, gbc);

        // 7. Cancel Button
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> {
            dispose();
            new TellerAccountPage(uiManager);
        });
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 10, 10, 10);
        add(cancelBtn, gbc);

        setVisible(true);
    }
}