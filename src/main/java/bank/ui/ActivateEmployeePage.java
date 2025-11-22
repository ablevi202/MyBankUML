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

public class ActivateEmployeePage extends JFrame {
    private UIManager uiManager;

    public ActivateEmployeePage(UIManager manager) {
        this.uiManager = manager;

        setTitle("MyBankUML - Activate Employee");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // 1. Header
        JLabel headerLabel = new JLabel("Activate Employee Account", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(headerLabel, gbc);

        // 2. Instruction
        JLabel instructionLabel = new JLabel("Enter the username to activate:", SwingConstants.LEFT);
        gbc.gridy = 1;
        add(instructionLabel, gbc);

        // 3. Username Field
        JTextField userField = new JTextField();
        gbc.gridy = 2;
        add(userField, gbc);

        // 4. Activate Button
        JButton activateButton = new JButton("Activate Access");
        activateButton.setBackground(new Color(144, 238, 144)); // Green
        activateButton.setOpaque(true);
        activateButton.setBorderPainted(false);
        
        activateButton.addActionListener(e -> {
            String username = userField.getText();
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a username.");
                return;
            }

            if (uiManager.activateEmployee(username)) {
                JOptionPane.showMessageDialog(this, "Employee account activated successfully.");
                dispose();
                new AdminDashboard(uiManager);
            } else {
                JOptionPane.showMessageDialog(this, "Error: User not found.");
            }
        });

        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(activateButton, gbc);

        // 5. Cancel Button
        JButton backButton = new JButton("Cancel");
        backButton.setBackground(new Color(100, 149, 237));
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            dispose();
            new ManageEmployeesPage(uiManager);
        });

        gbc.gridx = 1; 
        add(backButton, gbc);

        setVisible(true);
    }
}