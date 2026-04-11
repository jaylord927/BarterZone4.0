package BarterZone.Dashboard.trader;

import database.config.config;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;

public class trades_step1 {
    
    private int tradeId;
    private int traderId;
    private String traderName;
    private int otherTraderId;
    private String otherTraderName;
    private config db;
    private JFrame parent;
    private Runnable onStateChanged;
    private JButton proceedButton;
    
    private JRadioButton deliveryRadio;
    private JRadioButton meetupRadio;
    private ButtonGroup methodGroup;
    private JLabel methodStatusLabel;
    private JButton agreeMethodButton;
    private JButton disagreeMethodButton;
    private JButton proposeButton;
    private JLabel otherTraderMethodLabel;
    
    private String exchangeMethod;
    private String proposedMethod;
    private int proposedBy;
    private boolean methodConfirmed;
    
    private Color accentColor = new Color(0, 102, 102);
    private Color successColor = new Color(46, 125, 50);
    private Color warningColor = new Color(255, 153, 0);
    private Color errorColor = new Color(204, 0, 0);
    private Color textColor = new Color(80, 80, 80);
    
    public trades_step1(int tradeId, int traderId, String traderName, int otherTraderId, String otherTraderName, 
                        config db, JFrame parent, Runnable onStateChanged, JButton proceedButton) {
        this.tradeId = tradeId;
        this.traderId = traderId;
        this.traderName = traderName;
        this.otherTraderId = otherTraderId;
        this.otherTraderName = otherTraderName;
        this.db = db;
        this.parent = parent;
        this.onStateChanged = onStateChanged;
        this.proceedButton = proceedButton;
        
        initComponents();
    }
    
    private void initComponents() {
        methodGroup = new ButtonGroup();
        deliveryRadio = new JRadioButton("Delivery (Ship item)");
        deliveryRadio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        deliveryRadio.setBackground(Color.WHITE);
        
        meetupRadio = new JRadioButton("Meetup (In-person)");
        meetupRadio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        meetupRadio.setBackground(Color.WHITE);
        
        methodGroup.add(deliveryRadio);
        methodGroup.add(meetupRadio);
        
        methodStatusLabel = new JLabel();
        methodStatusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        
        agreeMethodButton = new JButton("AGREE");
        agreeMethodButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        agreeMethodButton.setBackground(successColor);
        agreeMethodButton.setForeground(Color.WHITE);
        agreeMethodButton.setBorder(null);
        agreeMethodButton.setFocusPainted(false);
        agreeMethodButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        disagreeMethodButton = new JButton("DISAGREE - PROPOSE DIFFERENT");
        disagreeMethodButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        disagreeMethodButton.setBackground(errorColor);
        disagreeMethodButton.setForeground(Color.WHITE);
        disagreeMethodButton.setBorder(null);
        disagreeMethodButton.setFocusPainted(false);
        disagreeMethodButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        proposeButton = new JButton("PROPOSE METHOD");
        proposeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        proposeButton.setBackground(accentColor);
        proposeButton.setForeground(Color.WHITE);
        proposeButton.setBorder(null);
        proposeButton.setFocusPainted(false);
        proposeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        otherTraderMethodLabel = new JLabel();
        otherTraderMethodLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    }
    
    public void loadState(String exchangeMethod, String proposedMethod, int proposedBy, boolean methodConfirmed) {
        this.exchangeMethod = exchangeMethod;
        this.proposedMethod = proposedMethod;
        this.proposedBy = proposedBy;
        this.methodConfirmed = methodConfirmed;
        
        if (exchangeMethod != null) {
            if (exchangeMethod.equals("delivery")) deliveryRadio.setSelected(true);
            else if (exchangeMethod.equals("meetup")) meetupRadio.setSelected(true);
        }
    }
    
    public JPanel buildPanel() {
        JPanel methodPanel = new JPanel();
        methodPanel.setLayout(null);
        methodPanel.setBackground(Color.WHITE);
        methodPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor), "Exchange Method Selection"));
        methodPanel.setBounds(20, 20, 900, 200);
        
        JLabel methodLabel = new JLabel("Select your preferred exchange method:");
        methodLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        methodLabel.setBounds(20, 25, 300, 25);
        methodPanel.add(methodLabel);
        
        boolean methodEditable = (exchangeMethod == null || !methodConfirmed);
        
        deliveryRadio.setEnabled(methodEditable);
        meetupRadio.setEnabled(methodEditable);
        
        deliveryRadio.setBounds(20, 60, 200, 30);
        meetupRadio.setBounds(230, 60, 200, 30);
        methodPanel.add(deliveryRadio);
        methodPanel.add(meetupRadio);
        
        int yPos = 100;
        
        if (proposedMethod != null && proposedBy != traderId && !methodConfirmed) {
            otherTraderMethodLabel.setText(otherTraderName + " proposed: " + (proposedMethod.equals("delivery") ? "Delivery" : "Meetup"));
            otherTraderMethodLabel.setBounds(20, yPos, 400, 25);
            methodPanel.add(otherTraderMethodLabel);
            yPos += 35;
            
            agreeMethodButton.setBounds(20, yPos, 100, 35);
            disagreeMethodButton.setBounds(130, yPos, 200, 35);
            agreeMethodButton.addActionListener(e -> acceptProposedMethod());
            disagreeMethodButton.addActionListener(e -> rejectProposedMethod());
            methodPanel.add(agreeMethodButton);
            methodPanel.add(disagreeMethodButton);
            yPos += 50;
        }
        
        if ((proposedMethod == null || (proposedBy == traderId && !methodConfirmed)) && methodEditable) {
            proposeButton.setBounds(20, yPos, 150, 35);
            proposeButton.addActionListener(e -> proposeMethod());
            methodPanel.add(proposeButton);
            yPos += 50;
        }
        
        methodStatusLabel.setBounds(20, yPos, 860, 25);
        
        if (exchangeMethod != null && methodConfirmed) {
            methodStatusLabel.setText("Method confirmed: " + (exchangeMethod.equals("delivery") ? "Delivery" : "Meetup"));
            methodStatusLabel.setForeground(successColor);
            proceedButton.setEnabled(true);
            proceedButton.setText("PROCEED TO NEXT STEP");
        } else if (exchangeMethod != null) {
            methodStatusLabel.setText("Method selected: " + (exchangeMethod.equals("delivery") ? "Delivery" : "Meetup") + ". Waiting for " + otherTraderName + " to agree.");
            methodStatusLabel.setForeground(warningColor);
            proceedButton.setEnabled(false);
        } else if (proposedMethod != null && proposedBy == traderId && !methodConfirmed) {
            methodStatusLabel.setText("You proposed: " + (proposedMethod.equals("delivery") ? "Delivery" : "Meetup") + ". Waiting for " + otherTraderName + " to respond.");
            methodStatusLabel.setForeground(warningColor);
            proceedButton.setEnabled(false);
        } else {
            methodStatusLabel.setText("Select a method and click PROPOSE METHOD");
            methodStatusLabel.setForeground(textColor);
            proceedButton.setEnabled(false);
        }
        
        methodPanel.add(methodStatusLabel);
        
        return methodPanel;
    }
    
    private void proposeMethod() {
        if (!deliveryRadio.isSelected() && !meetupRadio.isSelected()) {
            JOptionPane.showMessageDialog(parent, "Please select an exchange method.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String method = deliveryRadio.isSelected() ? "delivery" : "meetup";
        
        int confirm = JOptionPane.showConfirmDialog(parent,
            "Propose " + (method.equals("delivery") ? "Delivery" : "Meetup") + " as the exchange method?\n\n"
            + otherTraderName + " will be notified and can either agree or propose a different method.",
            "Propose Method", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_trade SET proposed_method = ?, proposed_by = ?, exchange_method = NULL, method_confirmed = 0 WHERE trade_id = ?";
            db.updateRecord(sql, method, traderId, tradeId);
            
            if (onStateChanged != null) onStateChanged.run();
        }
    }
    
    private void acceptProposedMethod() {
        int confirm = JOptionPane.showConfirmDialog(parent,
            "Accept " + otherTraderName + "'s proposed method: " + 
            (proposedMethod.equals("delivery") ? "Delivery" : "Meetup") + "?\n\n"
            + "Both traders will then proceed to Step 2.",
            "Accept Method", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_trade SET exchange_method = ?, method_confirmed = 1 WHERE trade_id = ?";
            db.updateRecord(sql, proposedMethod, tradeId);
            
            if (onStateChanged != null) onStateChanged.run();
        }
    }
    
    private void rejectProposedMethod() {
        int confirm = JOptionPane.showConfirmDialog(parent,
            "Reject " + otherTraderName + "'s proposed method?\n\n"
            + "You will be able to propose your own method instead.",
            "Reject Method", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_trade SET proposed_method = NULL, proposed_by = NULL WHERE trade_id = ?";
            db.updateRecord(sql, tradeId);
            
            if (onStateChanged != null) onStateChanged.run();
        }
    }
}