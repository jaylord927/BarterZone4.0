package BarterZone.Dashboard.trader;

import database.config.config;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.util.List;
import java.util.Map;

public class trades_step4 {
    
    private int tradeId;
    private int traderId;
    private int otherTraderId;
    private String otherTraderName;
    private int proposedBy;
    private config db;
    private JFrame parent;
    private Runnable onStateChanged;
    private JButton proceedButton;
    
    private JCheckBox confirmReceivedCheck;
    private JLabel myReceiveStatusLabel;
    private JLabel otherReceiveStatusLabel;
    private JButton confirmReceiveButton;
    private JLabel receiveInfoLabel;
    
    private boolean myItemReceived;
    private boolean otherItemReceived;
    
    private Color primaryColor = new Color(0, 102, 102);
    private Color successColor = new Color(46, 125, 50);
    private Color warningColor = new Color(255, 153, 0);
    private Color errorColor = new Color(204, 0, 0);
    private Color infoColor = new Color(33, 150, 243);
    private Color borderColor = new Color(200, 200, 200);
    private Color bgColor = new Color(250, 250, 250);
    private Color textColor = new Color(80, 80, 80);
    
    public trades_step4(int tradeId, int traderId, int otherTraderId, String otherTraderName, 
                        int proposedBy, config db, JFrame parent, Runnable onStateChanged, JButton proceedButton) {
        this.tradeId = tradeId;
        this.traderId = traderId;
        this.otherTraderId = otherTraderId;
        this.otherTraderName = otherTraderName;
        this.proposedBy = proposedBy;
        this.db = db;
        this.parent = parent;
        this.onStateChanged = onStateChanged;
        this.proceedButton = proceedButton;
        
        initComponents();
    }
    
    private void initComponents() {
        confirmReceivedCheck = new JCheckBox("I have received the item from " + otherTraderName);
        confirmReceivedCheck.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        confirmReceivedCheck.setBackground(Color.WHITE);
        
        confirmReceiveButton = new JButton("CONFIRM RECEIVE");
        confirmReceiveButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        confirmReceiveButton.setBackground(successColor);
        confirmReceiveButton.setForeground(Color.WHITE);
        confirmReceiveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        confirmReceiveButton.setFocusPainted(false);
        confirmReceiveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        myReceiveStatusLabel = new JLabel();
        myReceiveStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        otherReceiveStatusLabel = new JLabel();
        otherReceiveStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        receiveInfoLabel = new JLabel();
        receiveInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        receiveInfoLabel.setForeground(infoColor);
    }
    
    public void loadState(boolean myItemReceived, boolean otherItemReceived) {
        this.myItemReceived = myItemReceived;
        this.otherItemReceived = otherItemReceived;
    }
    
    public JPanel buildPanel() {
        JPanel container = new JPanel();
        container.setLayout(null);
        container.setBackground(bgColor);
        container.setBounds(0, 0, 940, 650);
        
        int y = 20;
                
        boolean iAmProposer = (traderId == proposedBy);
        String traderRole = iAmProposer ? "Proposer" : "Receiver";
        String myColumnName = iAmProposer ? "my_item_received" : "other_item_received";
        String otherColumnName = iAmProposer ? "other_item_received" : "my_item_received";
        
        // ========== INFORMATION PANEL ==========
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(null);
        infoPanel.setBackground(new Color(220, 240, 255));
        infoPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(infoColor, 2),
            "TRADE INFORMATION",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), infoColor));
        infoPanel.setBounds(20, y, 900, 80);
        container.add(infoPanel);
        
        receiveInfoLabel.setText("<html>Trade initiated by: <b>" + (proposedBy == traderId ? "You" : otherTraderName) + "</b><br>" +
            "Your role: <b>" + traderRole + "</b> - You are responsible for confirming when you receive the item from the other trader.</html>");
        receiveInfoLabel.setBounds(15, 20, 870, 50);
        infoPanel.add(receiveInfoLabel);
        y += 100;
        
        // ========== MY RECEIVE STATUS PANEL ==========
        JPanel myReceivePanel = new JPanel();
        myReceivePanel.setLayout(null);
        myReceivePanel.setBackground(Color.WHITE);
        myReceivePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(primaryColor, 2),
            "MY RECEIVE STATUS",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), primaryColor));
        myReceivePanel.setBounds(20, y, 440, 250);
        container.add(myReceivePanel);
        
        int myY = 25;
        
        // Status icon and text
        JLabel myStatusIcon = new JLabel();
        myStatusIcon.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        myStatusIcon.setBounds(30, myY, 40, 40);
        myReceivePanel.add(myStatusIcon);
        
        // Check this trader's confirmation status using the correct column
        boolean iHaveConfirmed = false;
        if (iAmProposer) {
            iHaveConfirmed = myItemReceived;  // Proposer updates my_item_received
        } else {
            iHaveConfirmed = otherItemReceived;  // Target updates other_item_received
        }
        
        if (iHaveConfirmed) {
            myStatusIcon.setText("[V]");
            myStatusIcon.setForeground(successColor);
            myReceiveStatusLabel.setText("You have confirmed receiving the item from " + otherTraderName + ".");
            myReceiveStatusLabel.setForeground(successColor);
            confirmReceivedCheck.setSelected(true);
            confirmReceivedCheck.setEnabled(false);
            confirmReceiveButton.setEnabled(false);
            confirmReceiveButton.setText("ALREADY CONFIRMED");
            confirmReceiveButton.setBackground(new Color(150, 150, 150));
        } else {
            myStatusIcon.setText("[ ]");
            myStatusIcon.setForeground(warningColor);
            myReceiveStatusLabel.setText("You have not yet received the item from " + otherTraderName + ".");
            myReceiveStatusLabel.setForeground(warningColor);
            confirmReceivedCheck.setSelected(false);
            confirmReceivedCheck.setEnabled(true);
            confirmReceiveButton.setEnabled(true);
            confirmReceiveButton.setText("CONFIRM RECEIVE");
            confirmReceiveButton.setBackground(successColor);
        }
        
        myReceiveStatusLabel.setBounds(80, myY, 330, 40);
        myReceivePanel.add(myReceiveStatusLabel);
        myY += 55;
        
        // Separator
        JSeparator mySeparator = new JSeparator();
        mySeparator.setBounds(20, myY, 400, 2);
        myReceivePanel.add(mySeparator);
        myY += 20;
        
        // Confirmation checkbox
        confirmReceivedCheck.setBounds(30, myY, 380, 30);
        myReceivePanel.add(confirmReceivedCheck);
        myY += 50;
        
        // Confirm button
        confirmReceiveButton.setBounds(120, myY, 200, 45);
        confirmReceiveButton.addActionListener(e -> confirmReceive(myColumnName));
        myReceivePanel.add(confirmReceiveButton);
        myY += 65;
        
        // Help text
        JLabel myHelpLabel = new JLabel("Check the box above and click CONFIRM RECEIVE when you receive the item.");
        myHelpLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        myHelpLabel.setForeground(textColor);
        myHelpLabel.setBounds(20, myY, 400, 20);
        myReceivePanel.add(myHelpLabel);
        
        // ========== OTHER TRADER RECEIVE STATUS PANEL ==========
        JPanel otherReceivePanel = new JPanel();
        otherReceivePanel.setLayout(null);
        otherReceivePanel.setBackground(Color.WHITE);
        otherReceivePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(primaryColor, 2),
            otherTraderName.toUpperCase() + "'S RECEIVE STATUS",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), primaryColor));
        otherReceivePanel.setBounds(480, y, 440, 250);
        container.add(otherReceivePanel);
        
        int otherY = 25;
        
        JLabel otherStatusIcon = new JLabel();
        otherStatusIcon.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        otherStatusIcon.setBounds(30, otherY, 40, 40);
        otherReceivePanel.add(otherStatusIcon);
        
        // Check other trader's confirmation status using the correct column
        boolean otherHasConfirmed = false;
        if (iAmProposer) {
            otherHasConfirmed = otherItemReceived;  // For proposer, other trader updates other_item_received
        } else {
            otherHasConfirmed = myItemReceived;  // For target, other trader (proposer) updates my_item_received
        }
        
        if (otherHasConfirmed) {
            otherStatusIcon.setText("[V]");
            otherStatusIcon.setForeground(successColor);
            otherReceiveStatusLabel.setText(otherTraderName + " has confirmed receiving the item from you.");
            otherReceiveStatusLabel.setForeground(successColor);
        } else {
            otherStatusIcon.setText("[ ]");
            otherStatusIcon.setForeground(warningColor);
            otherReceiveStatusLabel.setText(otherTraderName + " has not yet confirmed receiving the item from you.");
            otherReceiveStatusLabel.setForeground(warningColor);
        }
        
        otherReceiveStatusLabel.setBounds(80, otherY, 330, 40);
        otherReceivePanel.add(otherReceiveStatusLabel);
        otherY += 55;
        
        // Separator
        JSeparator otherSeparator = new JSeparator();
        otherSeparator.setBounds(20, otherY, 400, 2);
        otherReceivePanel.add(otherSeparator);
        otherY += 30;
        
        // Additional info
        JLabel otherInfoLabel = new JLabel();
        if (!otherHasConfirmed) {
            otherInfoLabel.setText("Waiting for " + otherTraderName + " to confirm receipt...");
            otherInfoLabel.setForeground(warningColor);
        } else {
            otherInfoLabel.setText(otherTraderName + " has already confirmed receipt.");
            otherInfoLabel.setForeground(successColor);
        }
        otherInfoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        otherInfoLabel.setBounds(30, otherY, 380, 25);
        otherReceivePanel.add(otherInfoLabel);
        otherY += 40;
        
        // Status summary card
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(null);
        summaryPanel.setBackground(new Color(255, 250, 230));
        summaryPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(warningColor, 1),
            "RECEIPT SUMMARY",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12), warningColor));
        summaryPanel.setBounds(30, otherY, 380, 80);
        otherReceivePanel.add(summaryPanel);
        
        JLabel summaryLabel = new JLabel();
        summaryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        boolean bothConfirmed = false;
        if (iAmProposer) {
            bothConfirmed = (myItemReceived && otherItemReceived);
        } else {
            bothConfirmed = (otherItemReceived && myItemReceived);
        }
        
        if (bothConfirmed) {
            summaryLabel.setText("<html>Status: BOTH TRADERS HAVE RECEIVED ITEMS<br>You may now proceed to Step 5 - Refund.</html>");
            summaryLabel.setForeground(successColor);
        } else if (iHaveConfirmed) {
            summaryLabel.setText("<html>Status: You have received your item.<br>Waiting for " + otherTraderName + " to confirm receipt.</html>");
            summaryLabel.setForeground(warningColor);
        } else if (otherHasConfirmed) {
            summaryLabel.setText("<html>Status: " + otherTraderName + " has received their item.<br>Waiting for you to confirm receipt.</html>");
            summaryLabel.setForeground(warningColor);
        } else {
            summaryLabel.setText("<html>Status: No items confirmed yet.<br>Both traders need to confirm receipt.</html>");
            summaryLabel.setForeground(textColor);
        }
        summaryLabel.setBounds(10, 15, 360, 55);
        summaryPanel.add(summaryLabel);
        
        y += 270;
        
        // ========== OVERALL STATUS PANEL ==========
        JPanel overallPanel = new JPanel();
        overallPanel.setLayout(null);
        overallPanel.setBackground(Color.WHITE);
        overallPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(primaryColor, 2),
            "OVERALL RECEIVE STATUS",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), primaryColor));
        overallPanel.setBounds(20, y, 900, 70);
        container.add(overallPanel);
        
        JLabel overallStatusLabel = new JLabel();
        overallStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        overallStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        overallStatusLabel.setBounds(20, 20, 860, 35);
        
        if (bothConfirmed) {
            overallStatusLabel.setText("BOTH TRADERS HAVE RECEIVED ITEMS! Click PROCEED to continue to Step 5.");
            overallStatusLabel.setForeground(successColor);
            proceedButton.setEnabled(true);
            proceedButton.setText("PROCEED TO REFUND");
        } else if (iHaveConfirmed) {
            overallStatusLabel.setText("You have received the item. Waiting for " + otherTraderName + " to confirm receipt.");
            overallStatusLabel.setForeground(warningColor);
            proceedButton.setEnabled(false);
        } else if (otherHasConfirmed) {
            overallStatusLabel.setText(otherTraderName + " has received the item. Waiting for you to confirm receipt.");
            overallStatusLabel.setForeground(warningColor);
            proceedButton.setEnabled(false);
        } else {
            overallStatusLabel.setText("Waiting for both traders to confirm receipt.");
            overallStatusLabel.setForeground(textColor);
            proceedButton.setEnabled(false);
        }
        
        overallPanel.add(overallStatusLabel);
        
        return container;
    }
    
    private void confirmReceive(String columnToUpdate) {
        if (!confirmReceivedCheck.isSelected()) {
            JOptionPane.showMessageDialog(parent,
                "Please check the box to confirm you have received the item from " + otherTraderName + ".",
                "Confirmation Required",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String dateColumn = (columnToUpdate.equals("my_item_received")) ? "my_item_received_date" : "other_item_received_date";
        
        boolean iAmProposer = (traderId == proposedBy);
        String traderRole = iAmProposer ? "the proposer" : "the receiver";
        
        int confirm = JOptionPane.showConfirmDialog(parent,
            "Confirm that you have received the item from " + otherTraderName + "?\n\n" +
            "Your role: " + traderRole + "\n" +
            "This action confirms that:\n" +
            "• You have physically received the item\n" +
            "• The item is in the condition described\n" +
            "• This confirmation cannot be undone",
            "Confirm Receive",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Update the database using the correct column
            String sql = "UPDATE tbl_trade SET " + columnToUpdate + " = 1, " + dateColumn + " = datetime('now') WHERE trade_id = ?";
            db.updateRecord(sql, tradeId);
            
            // Refresh the UI
            if (onStateChanged != null) onStateChanged.run();
        }
    }
}