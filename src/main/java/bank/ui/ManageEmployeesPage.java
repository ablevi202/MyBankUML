package bank.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import bank.UIManager;

public class ManageEmployeesPage extends JFrame {
    private UIManager uiManager;

    public ManageEmployeesPage(UIManager manager) {
        this.uiManager = manager;

        setTitle("MyBankUML - Manage Employees");
        setSize(500, 450); // Increased height
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // 1. Header
        JLabel headerLabel = new JLabel("Manage Employee Accounts", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        add(headerLabel, gbc);

        // 2. Create Button
        JButton createButton = new JButton("Create Employee Account");
        createButton.setBackground(new Color(144, 238, 144)); 
        createButton.setOpaque(true);
        createButton.setBorderPainted(false);
        createButton.setPreferredSize(new Dimension(300, 40));
        createButton.addActionListener(e -> {
            dispose();
            new CreateEmployeePage(uiManager);
        });
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 10, 5, 10);
        add(createButton, gbc);

        // 3. Activate Button (NEW)
        JButton activateButton = new JButton("Activate Employee Account");
        activateButton.setBackground(new Color(173, 216, 230)); // Blue
        activateButton.setOpaque(true);
        activateButton.setBorderPainted(false);
        activateButton.setPreferredSize(new Dimension(300, 40));
        activateButton.addActionListener(e -> {
            dispose();
            new ActivateEmployeePage(uiManager);
        });
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 10, 5, 10);
        add(activateButton, gbc);

        // 4. Remove Button
        JButton removeButton = new JButton("Remove Employee Access");
        removeButton.setBackground(new Color(255, 102, 102)); // Red
        removeButton.setOpaque(true);
        removeButton.setBorderPainted(false);
        removeButton.setPreferredSize(new Dimension(300, 40));
        removeButton.addActionListener(e -> {
            dispose();
            new RemoveEmployeePage(uiManager);
        });
        gbc.gridy = 3;
        add(removeButton, gbc);

        // 5. Back Button
        JButton backButton = new JButton("Back to Dashboard");
        backButton.setBackground(new Color(255, 102, 102));
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.setPreferredSize(new Dimension(150, 30));
        backButton.addActionListener(e -> {
            dispose();
            new AdminDashboard(uiManager);
        });

        gbc.gridy = 4;
        gbc.insets = new Insets(30, 10, 10, 10);
        gbc.fill = GridBagConstraints.NONE;
        add(backButton, gbc);

        setVisible(true);
    }
}