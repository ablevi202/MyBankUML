package bank.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import bank.UIManager;

/**
 * The main account management screen for Tellers.
 * <p>
 * This page displays the details and transaction history of a specific customer account.
 * It serves as the launchpad for financial operations, allowing the Teller to
 * initiate Deposits, Withdrawals, and Transfers for this account.
 * </p>
 */
public class TellerAccountPage extends JFrame {
    private final UIManager uiManager;
    private final String accountId;

    /**
     * Constructs the Account Management page.
     *
     * @param manager   The application controller used for data access and navigation.
     * @param accountId The unique ID of the account being managed.
     */
    public TellerAccountPage(UIManager manager, String accountId) {
        this.uiManager = manager;
        this.accountId = accountId;

        setTitle("MyBankUML - Account Management");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Page Header
        JLabel header = new JLabel("Account Details", SwingConstants.LEFT);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        add(header, gbc);

        // Account Information
        // Fetch the latest balance from the database
        String balance = uiManager.getFormattedBalance(accountId);

        addLabel(gbc, 1, "Account ID: " + accountId);
        addLabel(gbc, 2, "Current Balance: " + balance);
        
        // Transaction History Section
        JLabel historyHeader = new JLabel("Transaction History:");
        historyHeader.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridy = 4;
        gbc.insets = new Insets(15, 10, 5, 10);
        add(historyHeader, gbc);

        // Load History
        List<String[]> history = uiManager.getAccountHistory(accountId);
        int currentRow = 5;
        
        if (history.isEmpty()) {
            gbc.gridy = currentRow++;
            gbc.insets = new Insets(2, 20, 2, 10);
            JLabel noTrans = new JLabel("No recent transactions.");
            noTrans.setForeground(Color.GRAY);
            add(noTrans, gbc);
        } else {
            gbc.insets = new Insets(2, 20, 2, 10);
            
            for (String[] tx : history) {
                // Format: [Type, Amount, To_Acc, Timestamp]
                String date = tx[3].length() > 10 ? tx[3].substring(0, 10) : tx[3];
                String labelText = String.format("%s $%s (%s)", tx[0], tx[1], date);
                addLabel(gbc, currentRow++, labelText);
            }
        }

        // Actions Panel (Deposit, Transfer, Withdraw)
        JPanel btnPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        
        JButton depositBtn = new JButton("Deposit");
        depositBtn.setBackground(new Color(173, 216, 230));
        depositBtn.setOpaque(true);
        depositBtn.setBorderPainted(false);
        depositBtn.addActionListener(e -> {
            dispose();
            new TellerDepositPage(uiManager, accountId);
        });

        JButton transferBtn = new JButton("Transfer");
        transferBtn.setBackground(new Color(144, 238, 144));
        transferBtn.setOpaque(true);
        transferBtn.setBorderPainted(false);
        transferBtn.addActionListener(e -> {
            dispose();
            new TellerTransactionPage(uiManager, accountId);
        });

        JButton withdrawBtn = new JButton("Withdrawal");
        withdrawBtn.setBackground(new Color(255, 102, 102));
        withdrawBtn.setOpaque(true);
        withdrawBtn.setBorderPainted(false);
        withdrawBtn.addActionListener(e -> {
            dispose();
            new TellerWithdrawalPage(uiManager, accountId);
        });

        btnPanel.add(depositBtn);
        btnPanel.add(transferBtn);
        btnPanel.add(withdrawBtn);

        gbc.gridy = currentRow++;
        gbc.insets = new Insets(20, 10, 20, 10);
        add(btnPanel, gbc);

        // Navigation Button
        JButton backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(e -> {
            dispose();
            new TellerDashboard(uiManager);
        });
        
        gbc.gridy = currentRow;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(backButton, gbc);

        setVisible(true);
    }

    /**
     * Helper method to add a standard text label to the grid.
     */
    private void addLabel(GridBagConstraints gbc, int row, String text) {
        JLabel label = new JLabel(text);
        gbc.gridy = row;
        add(label, gbc);
    }
}