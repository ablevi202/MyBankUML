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
 * The UI form for deactivating an employee's access.
 * <p>
 * This screen allows an Administrator to revoke system access for a Teller.
 * It includes a mandatory confirmation dialog to prevent accidental deletions,
 * addressing specific security requirements from the design review.
 * </p>
 */
public class RemoveEmployeePage extends JFrame {
    private final UIManager uiManager;

    /**
     * Constructs the Remove Employee form.
     *
     * @param manager The application controller used to execute the deactivation.
     */
    public RemoveEmployeePage(UIManager manager) {
        this.uiManager = manager;

        setTitle("MyBankUML - Remove Employee Access");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Warning Header
        JLabel headerLabel = new JLabel("Remove Employee Access", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setForeground(new Color(255, 102, 102)); // Red to indicate danger
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(headerLabel, gbc);

        // Instruction Label
        JLabel instructionLabel = new JLabel("Enter the username of the employee to deactivate:", SwingConstants.LEFT);
        gbc.gridy = 1;
        add(instructionLabel, gbc);

        // Username Input Field
        JTextField userField = new JTextField();
        gbc.gridy = 2;
        add(userField, gbc);

        // Remove Button
        JButton removeButton = new JButton("Remove Access");
        removeButton.setBackground(new Color(255, 102, 102)); // Light Red
        removeButton.setOpaque(true);
        removeButton.setBorderPainted(false);
        removeButton.setForeground(Color.WHITE);
        
        removeButton.addActionListener(e -> {
            String username = userField.getText();
            
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a username.");
                return;
            }

            // Critical Confirmation Dialog (Risk Mitigation)
            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "Are you sure you want to remove access for user: " + username + "?\nThis action cannot be undone.",
                "Confirm Deactivation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                // Call Facade to perform Soft Delete
                if (uiManager.removeEmployee(username)) {
                    JOptionPane.showMessageDialog(this, "Employee access removed successfully.");
                    dispose();
                    new AdminDashboard(uiManager);
                } else {
                    JOptionPane.showMessageDialog(this, "Error: User not found or could not be removed.", "Removal Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(removeButton, gbc);

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

        gbc.gridx = 1; // Place next to Remove button
        add(backButton, gbc);

        setVisible(true);
    }
}