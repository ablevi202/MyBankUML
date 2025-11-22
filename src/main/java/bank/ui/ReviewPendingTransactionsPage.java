package bank.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

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

public class ReviewPendingTransactionsPage extends JFrame {
    private UIManager uiManager;

    public ReviewPendingTransactionsPage(UIManager manager) {
        this.uiManager = manager;

        setTitle("MyBankUML - Pending Transactions");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Header
        JLabel headerLabel = new JLabel("Transactions that are currently pending:", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(headerLabel, BorderLayout.NORTH);

        // 2. Scrollable List
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        
        addTransactionCard(listPanel, "851294", "Harlan Thrombey", "$64,000,000.00");
        addTransactionCard(listPanel, "823633", "Simon Reichard", "$6,000.00");
        addTransactionCard(listPanel, "932753", "Marta Cabrera", "$150,000.00");

        JScrollPane scrollPane = new JScrollPane(listPanel);
        add(scrollPane, BorderLayout.CENTER);

        // 3. Bottom Panel with Role-Aware Back Button
        JPanel bottomPanel = new JPanel();
        JButton backButton = new JButton("Back to Dashboard");
        backButton.setBackground(new Color(255, 102, 102)); // Light Red
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        
        backButton.addActionListener(e -> {
            dispose();
            // Check Role to redirect correctly
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

    private void addTransactionCard(JPanel container, String id, String name, String amount) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        card.setMaximumSize(new Dimension(550, 100));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel infoLabel = new JLabel("<html>ID: " + id + ", Customer: " + name + "<br/>Wants to transfer " + amount + "</html>");
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        card.add(infoLabel, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton approveBtn = new JButton("Approve");
        approveBtn.setBackground(new Color(144, 238, 144)); // Green
        approveBtn.setOpaque(true);
        approveBtn.setBorderPainted(false);
        approveBtn.addActionListener(e -> {
            uiManager.approveTransaction(id);
            container.remove(card);
            container.revalidate();
            container.repaint();
            JOptionPane.showMessageDialog(this, "Transaction " + id + " Approved.");
        });

        JButton denyBtn = new JButton("Deny");
        denyBtn.setBackground(new Color(255, 102, 102)); // Red
        denyBtn.setOpaque(true);
        denyBtn.setBorderPainted(false);
        denyBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to deny transaction " + id + "?");
            if (confirm == JOptionPane.YES_OPTION) {
                uiManager.denyTransaction(id);
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
        container.add(Box.createRigidArea(new Dimension(0, 10)));
    }
}