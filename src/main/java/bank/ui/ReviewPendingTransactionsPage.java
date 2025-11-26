package bank.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import bank.UIManager;

/**
 * The UI screen for reviewing high-risk transactions.
 * <p>
 * This page is accessible by Tellers and Admins. It displays a list of transactions
 * flagged as "PENDING_REVIEW" (e.g., amounts over $10,000). Users can approve
 * or deny these transactions, triggering the appropriate backend logic.
 * </p>
 */
public class ReviewPendingTransactionsPage extends JFrame {
    private final UIManager uiManager;

    /**
     * Constructs the Transaction Review page.
     *
     * @param manager The application controller used to fetch and update transaction status.
     */
    public ReviewPendingTransactionsPage(UIManager manager) {
        this.uiManager = manager;

        setTitle("MyBankUML - Pending Transactions");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Page Header
        JLabel headerLabel = new JLabel("Transactions that are currently pending:", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(headerLabel, BorderLayout.NORTH);

        // Scrollable List of Transactions
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        
        // Retrieve pending transactions from the database
        List<String[]> pendingList = uiManager.getPendingTransactions();
        
        if (pendingList.isEmpty()) {
            JLabel emptyLabel = new JLabel("No pending transactions found.");
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            listPanel.add(Box.createVerticalStrut(20));
            listPanel.add(emptyLabel);
        } else {
            for (String[] tx : pendingList) {
                // Data format: [ID, From_Account, Amount, Type]
                String id = tx[0];
                String customer = tx[1]; // Represents Account or User ID
                String amount = tx[2];   // Pre-formatted currency string
                
                addTransactionCard(listPanel, id, customer, amount);
            }
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Navigation Panel (Role-Aware)
        JPanel bottomPanel = new JPanel();
        JButton backButton = new JButton("Back to Dashboard");
        backButton.setBackground(new Color(255, 102, 102)); // Light Red
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        
        backButton.addActionListener(e -> {
            dispose();
            // Redirect to the appropriate dashboard based on the logged-in user's role
            String role = uiManager.getCurrentUserRole();
            if ("ADMIN".equals(role)) {
                new AdminDashboard(uiManager);
            } else {
                new TellerDashboard(uiManager);
            }
        });
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Creates and adds a UI card representing a single pending transaction.
     *
     * @param container The parent panel to add the card to.
     * @param id        The transaction ID.
     * @param name      The name/ID of the account initiating the transaction.
     * @param amount    The transaction amount.
     */
    private void addTransactionCard(JPanel container, String id, String name, String amount) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        card.setMaximumSize(new Dimension(550, 100));
        card.setBackground(Color.WHITE); 
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Transaction Details
        JLabel infoLabel = new JLabel("<html><b>ID:</b> " + id + " &nbsp;&nbsp; <b>From:</b> " + name + "<br/><b>Request:</b> Transfer " + amount + "</html>");
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        card.add(infoLabel, gbc);

        // Action Buttons Panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);
        
        // Approve Button
        JButton approveBtn = new JButton("Approve");
        approveBtn.setBackground(new Color(144, 238, 144)); // Green
        approveBtn.setOpaque(true);
        approveBtn.setBorderPainted(false);
        approveBtn.addActionListener(e -> {
            uiManager.approveTransaction(id);
            
            // Refresh UI locally
            container.remove(card);
            container.revalidate();
            container.repaint();
            
            JOptionPane.showMessageDialog(this, "Transaction " + id + " Approved.");
        });

        // Deny Button
        JButton denyBtn = new JButton("Deny");
        denyBtn.setBackground(new Color(255, 102, 102)); // Red
        denyBtn.setOpaque(true);
        denyBtn.setBorderPainted(false);
        denyBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to deny transaction " + id + "?");
            if (confirm == JOptionPane.YES_OPTION) {
                uiManager.denyTransaction(id);
                
                // Refresh UI locally
                container.remove(card);
                container.revalidate();
                container.repaint();
            }
        });

        btnPanel.add(approveBtn);
        btnPanel.add(denyBtn);

        gbc.gridx = 1;
        gbc.weightx = 0;
        card.add(btnPanel, gbc);

        container.add(card);
        container.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing between cards
    }
}