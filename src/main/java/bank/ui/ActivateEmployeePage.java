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
 * The UI form for reactivating a suspended employee account.
 * <p>
 * This screen allows an Admin to restore access to a Teller or other employee
 * who was previously removed or deactivated. It communicates with the
 * {@link UIManager} to update the user's status in the database.
 * </p>
 */
public class ActivateEmployeePage extends JFrame {
    private final UIManager uiManager;

    /**
     * Constructs the activation form.
     *
     * @param manager The application controller for handling data updates.
     */
    public ActivateEmployeePage(UIManager manager) {
        this.uiManager = manager;

        // Window Setup
        setTitle("MyBankUML - Activate Employee");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Header
        JLabel headerLabel = new JLabel("Activate Employee Account", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(headerLabel, gbc);

        // Instruction Label
        JLabel instructionLabel = new JLabel("Enter the username to activate:", SwingConstants.LEFT);
        gbc.gridy = 1;
        add(instructionLabel, gbc);

        // Username Input
        JTextField userField = new JTextField();
        gbc.gridy = 2;
        add(userField, gbc);

        // Activate Button
        JButton activateButton = new JButton("Activate Access");
        activateButton.setBackground(new Color(144, 238, 144)); // Light Green
        activateButton.setOpaque(true);
        activateButton.setBorderPainted(false);
        
        activateButton.addActionListener(e -> {
            String username = userField.getText();
            
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a username.");
                return;
            }

            // Call backend to update status
            if (uiManager.activateEmployee(username)) {
                JOptionPane.showMessageDialog(this, "Employee account activated successfully.");
                dispose();
                new AdminDashboard(uiManager);
            } else {
                JOptionPane.showMessageDialog(this, "Error: User not found.", "Activation Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(activateButton, gbc);

        // Cancel Button
        JButton backButton = new JButton("Cancel");
        backButton.setBackground(new Color(100, 149, 237)); // Cornflower Blue
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