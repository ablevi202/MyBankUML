package bank.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import bank.UIManager;

/**
 * The UI form for creating a new customer account.
 * <p>
 * This screen allows a Teller to input customer details (Name, DOB, Contact Info)
 * and generate a starting account. It validates input and communicates with the
 * backend to ensure unique usernames.
 * </p>
 */
public class CreateCustomerPage extends JFrame {
    private final UIManager uiManager;

    public CreateCustomerPage(UIManager manager) {
        this.uiManager = manager;

        setTitle("MyBankUML - Create Customer");
        setSize(500, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Page Title
        JLabel header = new JLabel("Create Customer Account", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(header, gbc);

        // Input Fields (Dynamic Row Counter)
        int currentRow = 1;

        JTextField nameField = new JTextField();
        addField(gbc, currentRow++, "Full Name:", nameField);

        JTextField dobField = new JTextField();
        addField(gbc, currentRow++, "Date of Birth (YYYY-MM-DD):", dobField);

        JTextField phoneField = new JTextField();
        addField(gbc, currentRow++, "Phone Number:", phoneField);

        JTextField emailField = new JTextField();
        addField(gbc, currentRow++, "Email Address:", emailField);

        JPasswordField passField = new JPasswordField();
        addField(gbc, currentRow++, "Initial Password:", passField);

        JTextField depositField = new JTextField("0.00");
        addField(gbc, currentRow++, "Initial Deposit ($):", depositField);

        // Account Type Selection
        gbc.gridy = currentRow++;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        add(new JLabel("Account Type:"), gbc);
        
        String[] types = {"Chequing", "Savings"};
        JComboBox<String> typeBox = new JComboBox<>(types);
        gbc.gridx = 1;
        add(typeBox, gbc);

        // Action Buttons
        JButton createBtn = new JButton("Create Account");
        createBtn.setBackground(new Color(144, 238, 144)); // Light Green
        createBtn.setOpaque(true);
        createBtn.setBorderPainted(false);
        
        createBtn.addActionListener(e -> {
            String name = nameField.getText();
            String dob = dobField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();
            String password = new String(passField.getPassword());
            String deposit = depositField.getText();
            String type = (String) typeBox.getSelectedItem();

            // Basic Validation
            if (name.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Password are required.");
                return;
            }

            // Call backend to create user and account
            boolean success = uiManager.createCustomerAccount(name, dob, type, phone, email, password);
            
            if (success) {
                String msg = "Customer Account Created!\nUsername: " + name.toLowerCase().replace(" ", "");
                
                // Prompt for manual deposit if needed
                if (!deposit.isEmpty() && !deposit.equals("0") && !deposit.equals("0.00")) {
                    msg += "\n\nNOTE: Please process the deposit of " + deposit + " manually.";
                }
                
                JOptionPane.showMessageDialog(this, msg);
                dispose();
                new TellerDashboard(uiManager);
            } else {
                // Handle duplicate user error
                JOptionPane.showMessageDialog(this, 
                    "Error: A customer with this name already exists.\nUse 'Search for Customer' to add a new account instead.", 
                    "Creation Failed", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridy = currentRow++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        add(createBtn, gbc);

        JButton backBtn = new JButton("Cancel");
        backBtn.addActionListener(e -> {
            dispose();
            new TellerDashboard(uiManager);
        });
        
        gbc.gridy = currentRow;
        gbc.insets = new Insets(0, 10, 10, 10);
        add(backBtn, gbc);

        setVisible(true);
    }

    /**
     * Helper method to add a label and input field row to the layout.
     */
    private void addField(GridBagConstraints gbc, int row, String labelText, Component field) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        add(new JLabel(labelText), gbc);
        gbc.gridx = 1;
        add(field, gbc);
    }
}