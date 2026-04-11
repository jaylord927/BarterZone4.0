package BarterZone.Dashboard.trader;

import database.config.config;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import java.awt.Image;
import java.io.File;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.util.List;
import java.util.Map;

public class trades_step3 {
    
    private int tradeId;
    private int traderId;
    private String traderName;
    private int otherTraderId;
    private String otherTraderName;
    private config db;
    private JFrame parent;
    private Runnable onStateChanged;
    private JButton proceedButton;
    
    private JComboBox<String> paymentMethodCombo;
    private JLabel serviceFeeLabel;
    private JLabel totalAmountLabel;
    private JLabel paymentMethodDetailLabel;
    private JLabel accountNumberLabel;
    private JLabel accountNameLabel;
    private JLabel myPaymentStatusLabel;
    private JLabel otherPaymentStatusLabel;
    private JLabel paymentStatusLabel;
    private JLabel paymentNumberValueLabel;
    private JLabel accountNameValueLabel;
    private JLabel otherPaymentNumberValueLabel;
    private JLabel otherAccountNameValueLabel;
    private JButton viewQrCodeButton;
    private JButton addPaymentProofButton;
    private JButton viewMyProofButton;
    private JButton viewOtherProofButton;
    private String currentQrCodePath = "";
    
    private step3_submit step3SubmitHandler;
    
    private boolean myPaymentSubmitted = false;
    private boolean otherPaymentSubmitted = false;
    private boolean myPaymentVerified = false;
    private boolean otherPaymentVerified = false;
    private String uploadedProofPath = "";
    private String otherUploadedProofPath = "";
    private int selectedMethodId = -1;
    private double currentServiceFee = 0;
    private double currentTotalAmount = 0;
    
    private Color themeColor = new Color(12, 192, 223);
    private Color accentColor = new Color(0, 102, 102);
    private Color successColor = new Color(46, 125, 50);
    private Color warningColor = new Color(255, 153, 0);
    private Color errorColor = new Color(204, 0, 0);
    private Color infoColor = new Color(33, 150, 243);
    private Color textColor = new Color(80, 80, 80);
    private Color bgColor = new Color(250, 250, 250);
    
    public trades_step3(int tradeId, int traderId, String traderName, int otherTraderId, String otherTraderName,
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
        paymentMethodCombo = new JComboBox<>();
        paymentMethodCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        paymentMethodCombo.addActionListener(e -> loadPaymentMethodDetails());
        
        serviceFeeLabel = new JLabel();
        serviceFeeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        totalAmountLabel = new JLabel();
        totalAmountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        paymentMethodDetailLabel = new JLabel();
        paymentMethodDetailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        accountNumberLabel = new JLabel();
        accountNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        accountNameLabel = new JLabel();
        accountNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        viewQrCodeButton = new JButton("VIEW QR CODE");
        viewQrCodeButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        viewQrCodeButton.setBackground(accentColor);
        viewQrCodeButton.setForeground(Color.WHITE);
        viewQrCodeButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        viewQrCodeButton.setFocusPainted(false);
        viewQrCodeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewQrCodeButton.setEnabled(false);
        viewQrCodeButton.addActionListener(e -> viewQrCode());
        
        addPaymentProofButton = new JButton("ADD PAYMENT PROOF");
        addPaymentProofButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        addPaymentProofButton.setBackground(themeColor);
        addPaymentProofButton.setForeground(Color.WHITE);
        addPaymentProofButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addPaymentProofButton.setFocusPainted(false);
        addPaymentProofButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addPaymentProofButton.addActionListener(e -> openStep3SubmitDialog());
        
        viewMyProofButton = new JButton("VIEW MY PROOF");
        viewMyProofButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        viewMyProofButton.setBackground(infoColor);
        viewMyProofButton.setForeground(Color.WHITE);
        viewMyProofButton.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        viewMyProofButton.setFocusPainted(false);
        viewMyProofButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewMyProofButton.setVisible(false);
        viewMyProofButton.addActionListener(e -> viewUploadedProof(uploadedProofPath));
        
        viewOtherProofButton = new JButton("VIEW THEIR PROOF");
        viewOtherProofButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        viewOtherProofButton.setBackground(infoColor);
        viewOtherProofButton.setForeground(Color.WHITE);
        viewOtherProofButton.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        viewOtherProofButton.setFocusPainted(false);
        viewOtherProofButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewOtherProofButton.setVisible(false);
        viewOtherProofButton.addActionListener(e -> viewUploadedProof(otherUploadedProofPath));
        
        myPaymentStatusLabel = new JLabel();
        myPaymentStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        otherPaymentStatusLabel = new JLabel();
        otherPaymentStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        paymentStatusLabel = new JLabel();
        paymentStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        paymentNumberValueLabel = new JLabel();
        paymentNumberValueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        accountNameValueLabel = new JLabel();
        accountNameValueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        otherPaymentNumberValueLabel = new JLabel();
        otherPaymentNumberValueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        otherAccountNameValueLabel = new JLabel();
        otherAccountNameValueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    }
    
    public void loadState(boolean myPaymentSubmitted, boolean otherPaymentSubmitted, boolean myPaymentVerified, boolean otherPaymentVerified,
                          String uploadedProofPath, String otherUploadedProofPath, int selectedMethodId,
                          double currentServiceFee, double currentTotalAmount,
                          String paymentNumber, String accountName, String otherPaymentNumber, String otherAccountName) {
        this.myPaymentSubmitted = myPaymentSubmitted;
        this.otherPaymentSubmitted = otherPaymentSubmitted;
        this.myPaymentVerified = myPaymentVerified;
        this.otherPaymentVerified = otherPaymentVerified;
        this.uploadedProofPath = uploadedProofPath;
        this.otherUploadedProofPath = otherUploadedProofPath;
        this.selectedMethodId = selectedMethodId;
        this.currentServiceFee = currentServiceFee;
        this.currentTotalAmount = currentTotalAmount;
        
        paymentNumberValueLabel.setText(paymentNumber.isEmpty() ? "Not submitted" : paymentNumber);
        accountNameValueLabel.setText(accountName.isEmpty() ? "Not submitted" : accountName);
        otherPaymentNumberValueLabel.setText(otherPaymentNumber.isEmpty() ? "Not submitted" : otherPaymentNumber);
        otherAccountNameValueLabel.setText(otherAccountName.isEmpty() ? "Not submitted" : otherAccountName);
        
        serviceFeeLabel.setText("PHP " + String.format("%.2f", currentServiceFee));
        totalAmountLabel.setText("PHP " + String.format("%.2f", currentTotalAmount));
        
        // Show/hide view proof buttons
        viewMyProofButton.setVisible(myPaymentSubmitted && !uploadedProofPath.isEmpty());
        viewOtherProofButton.setVisible(otherPaymentSubmitted && !otherUploadedProofPath.isEmpty());
        
        loadActivePaymentMethods();
    }
    
    private void loadActivePaymentMethods() {
        paymentMethodCombo.removeAllItems();
        
        String sql = "SELECT method_id, method_name, qr_code_path FROM tbl_payment_methods WHERE is_active = 1 ORDER BY method_name";
        List<Map<String, Object>> methods = db.fetchRecords(sql);
        
        paymentMethodCombo.addItem("-- Select Payment Method --");
        for (Map<String, Object> method : methods) {
            paymentMethodCombo.addItem(method.get("method_id") + " - " + method.get("method_name"));
        }
        
        if (selectedMethodId > 0) {
            for (int i = 0; i < paymentMethodCombo.getItemCount(); i++) {
                String item = paymentMethodCombo.getItemAt(i);
                if (item.startsWith(String.valueOf(selectedMethodId))) {
                    paymentMethodCombo.setSelectedIndex(i);
                    loadPaymentMethodDetails();
                    break;
                }
            }
        }
    }
    
    private void loadPaymentMethodDetails() {
        int selectedIndex = paymentMethodCombo.getSelectedIndex();
        if (selectedIndex <= 0) {
            paymentMethodDetailLabel.setText("");
            accountNumberLabel.setText("");
            accountNameLabel.setText("");
            viewQrCodeButton.setEnabled(false);
            currentQrCodePath = "";
            return;
        }
        
        String selected = paymentMethodCombo.getSelectedItem().toString();
        int methodId = Integer.parseInt(selected.substring(0, selected.indexOf(" -")));
        selectedMethodId = methodId;
        
        String sql = "SELECT method_name, account_number, account_name, qr_code_path FROM tbl_payment_methods WHERE method_id = ? AND is_active = 1";
        List<Map<String, Object>> result = db.fetchRecords(sql, methodId);
        
        if (!result.isEmpty()) {
            Map<String, Object> method = result.get(0);
            paymentMethodDetailLabel.setText(method.get("method_name").toString());
            accountNumberLabel.setText(method.get("account_number").toString());
            if (method.get("account_name") != null && !method.get("account_name").toString().isEmpty()) {
                accountNameLabel.setText(method.get("account_name").toString());
            } else {
                accountNameLabel.setText("");
            }
            
            if (method.get("qr_code_path") != null && !method.get("qr_code_path").toString().isEmpty()) {
                currentQrCodePath = method.get("qr_code_path").toString();
                viewQrCodeButton.setEnabled(true);
            } else {
                currentQrCodePath = "";
                viewQrCodeButton.setEnabled(false);
            }
        }
    }
    
    private void viewQrCode() {
        if (currentQrCodePath == null || currentQrCodePath.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No QR Code available for this payment method.", "QR Code Not Found", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String fullPath = "src/" + currentQrCodePath;
            File qrFile = new File(fullPath);
            if (qrFile.exists()) {
                ImageIcon qrIcon = new ImageIcon(fullPath);
                Image scaledImage = qrIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                JOptionPane.showMessageDialog(parent, new JLabel(new ImageIcon(scaledImage)), "Payment QR Code", JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parent, "QR Code image file not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "Error loading QR Code: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public JPanel buildPanel() {
        JPanel container = new JPanel();
        container.setLayout(null);
        container.setBackground(bgColor);
        container.setBounds(0, 0, 940, 700);
        
        int y = 15;
        
        // ========== PAYMENT INFORMATION SECTION ==========
        JPanel paymentInfoPanel = new JPanel();
        paymentInfoPanel.setLayout(null);
        paymentInfoPanel.setBackground(Color.WHITE);
        paymentInfoPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(accentColor, 2),
            "PAYMENT INFORMATION",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), accentColor));
        paymentInfoPanel.setBounds(20, y, 900, 280);
        container.add(paymentInfoPanel);
        
        int py = 30;
        int labelWidth = 160;
        int fieldWidth = 300;
        int fieldX = 190;
        int buttonX = 510;
        
        // Payment Method Selection
        JLabel methodSelectLabel = new JLabel("Select Payment Method:");
        methodSelectLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        methodSelectLabel.setBounds(20, py, labelWidth, 35);
        paymentInfoPanel.add(methodSelectLabel);
        
        paymentMethodCombo.setBounds(fieldX, py, fieldWidth, 35);
        paymentInfoPanel.add(paymentMethodCombo);
        py += 50;
        
        // Payment Method
        JLabel methodDetailTitle = new JLabel("Payment Method:");
        methodDetailTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        methodDetailTitle.setBounds(20, py, labelWidth, 25);
        paymentInfoPanel.add(methodDetailTitle);
        
        paymentMethodDetailLabel.setBounds(fieldX, py, fieldWidth, 25);
        paymentInfoPanel.add(paymentMethodDetailLabel);
        py += 30;
        
        // Account Number
        JLabel accountNumTitle = new JLabel("Account Number:");
        accountNumTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        accountNumTitle.setBounds(20, py, labelWidth, 25);
        paymentInfoPanel.add(accountNumTitle);
        
        accountNumberLabel.setBounds(fieldX, py, fieldWidth, 25);
        paymentInfoPanel.add(accountNumberLabel);
        py += 30;
        
        // Account Name
        JLabel accountNameTitle = new JLabel("Account Name:");
        accountNameTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        accountNameTitle.setBounds(20, py, labelWidth, 25);
        paymentInfoPanel.add(accountNameTitle);
        
        accountNameLabel.setBounds(fieldX, py, fieldWidth, 25);
        paymentInfoPanel.add(accountNameLabel);
        
        // View QR Code Button - aligned with the fields
        viewQrCodeButton.setBounds(buttonX, py - 85, 150, 35);
        paymentInfoPanel.add(viewQrCodeButton);
        py += 40;
        
        // Separator
        JSeparator separator = new JSeparator();
        separator.setBounds(20, py, 860, 2);
        paymentInfoPanel.add(separator);
        py += 20;
        
        // Fee Details Title
        JLabel feeDetailsTitle = new JLabel("FEE DETAILS");
        feeDetailsTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        feeDetailsTitle.setForeground(accentColor);
        feeDetailsTitle.setBounds(20, py, 200, 25);
        paymentInfoPanel.add(feeDetailsTitle);
        py += 30;
        
        // Service Fee
        JLabel serviceFeeTitle = new JLabel("Service Fee:");
        serviceFeeTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        serviceFeeTitle.setBounds(40, py, 120, 30);
        paymentInfoPanel.add(serviceFeeTitle);
        
        serviceFeeLabel.setBounds(170, py, 150, 30);
        serviceFeeLabel.setForeground(accentColor);
        paymentInfoPanel.add(serviceFeeLabel);
        
        // Total Amount
        JLabel totalAmountTitle = new JLabel("Total Amount to Pay:");
        totalAmountTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        totalAmountTitle.setBounds(400, py, 160, 30);
        paymentInfoPanel.add(totalAmountTitle);
        
        totalAmountLabel.setBounds(570, py, 150, 30);
        totalAmountLabel.setForeground(successColor);
        paymentInfoPanel.add(totalAmountLabel);
        
        // ========== YOUR PAYMENT PROOF SECTION ==========
        JPanel yourProofPanel = new JPanel();
        yourProofPanel.setLayout(null);
        yourProofPanel.setBackground(Color.WHITE);
        yourProofPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(accentColor, 2),
            "YOUR PAYMENT PROOF",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), accentColor));
        yourProofPanel.setBounds(20, y + 300, 440, 230);
        container.add(yourProofPanel);
        
        int ypY = 25;
        
        // Add Payment Proof Button
        addPaymentProofButton.setBounds(110, ypY, 220, 45);
        yourProofPanel.add(addPaymentProofButton);
        ypY += 60;
        
        // Payment Number Display
        JLabel paymentNumTitle = new JLabel("Payment Number:");
        paymentNumTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        paymentNumTitle.setBounds(20, ypY, 120, 25);
        yourProofPanel.add(paymentNumTitle);
        
        paymentNumberValueLabel.setBounds(150, ypY, 250, 25);
        yourProofPanel.add(paymentNumberValueLabel);
        ypY += 30;
        
        // Account Name Display
        JLabel accNameDisplayTitle = new JLabel("Account Name:");
        accNameDisplayTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        accNameDisplayTitle.setBounds(20, ypY, 120, 25);
        yourProofPanel.add(accNameDisplayTitle);
        
        accountNameValueLabel.setBounds(150, ypY, 250, 25);
        yourProofPanel.add(accountNameValueLabel);
        ypY += 40;
        
        // Payment Status
        JLabel paymentStatTitle = new JLabel("Payment Status:");
        paymentStatTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        paymentStatTitle.setBounds(20, ypY, 120, 25);
        yourProofPanel.add(paymentStatTitle);
        
        if (myPaymentVerified) {
            myPaymentStatusLabel.setText("VERIFIED BY ADMIN");
            myPaymentStatusLabel.setForeground(successColor);
        } else if (myPaymentSubmitted) {
            myPaymentStatusLabel.setText("SUBMITTED - PENDING VERIFICATION");
            myPaymentStatusLabel.setForeground(warningColor);
        } else {
            myPaymentStatusLabel.setText("NOT SUBMITTED");
            myPaymentStatusLabel.setForeground(errorColor);
        }
        myPaymentStatusLabel.setBounds(150, ypY, 250, 25);
        yourProofPanel.add(myPaymentStatusLabel);
        ypY += 40;
        
        // View My Proof Button
        viewMyProofButton.setBounds(110, ypY, 220, 30);
        yourProofPanel.add(viewMyProofButton);
        
        // ========== OTHER TRADER PAYMENT STATUS SECTION ==========
        JPanel otherProofPanel = new JPanel();
        otherProofPanel.setLayout(null);
        otherProofPanel.setBackground(Color.WHITE);
        otherProofPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(accentColor, 2),
            otherTraderName.toUpperCase() + "'S PAYMENT STATUS",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), accentColor));
        otherProofPanel.setBounds(480, y + 300, 440, 230);
        container.add(otherProofPanel);
        
        int opY = 25;
        
        // Other Trader Payment Number
        JLabel otherPaymentNumTitle = new JLabel("Payment Number:");
        otherPaymentNumTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        otherPaymentNumTitle.setBounds(20, opY, 120, 25);
        otherProofPanel.add(otherPaymentNumTitle);
        
        otherPaymentNumberValueLabel.setBounds(150, opY, 250, 25);
        otherProofPanel.add(otherPaymentNumberValueLabel);
        opY += 30;
        
        // Other Trader Account Name
        JLabel otherAccNameTitle = new JLabel("Account Name:");
        otherAccNameTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        otherAccNameTitle.setBounds(20, opY, 120, 25);
        otherProofPanel.add(otherAccNameTitle);
        
        otherAccountNameValueLabel.setBounds(150, opY, 250, 25);
        otherProofPanel.add(otherAccountNameValueLabel);
        opY += 40;
        
        // Other Trader Payment Status
        JLabel otherPaymentStatTitle = new JLabel("Payment Status:");
        otherPaymentStatTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        otherPaymentStatTitle.setBounds(20, opY, 120, 25);
        otherProofPanel.add(otherPaymentStatTitle);
        
        if (otherPaymentVerified) {
            otherPaymentStatusLabel.setText("VERIFIED BY ADMIN");
            otherPaymentStatusLabel.setForeground(successColor);
        } else if (otherPaymentSubmitted) {
            otherPaymentStatusLabel.setText("SUBMITTED - PENDING VERIFICATION");
            otherPaymentStatusLabel.setForeground(warningColor);
        } else {
            otherPaymentStatusLabel.setText("NOT SUBMITTED");
            otherPaymentStatusLabel.setForeground(errorColor);
        }
        otherPaymentStatusLabel.setBounds(150, opY, 250, 25);
        otherProofPanel.add(otherPaymentStatusLabel);
        opY += 40;
        
        // View Other Proof Button
        viewOtherProofButton.setBounds(110, opY, 220, 30);
        otherProofPanel.add(viewOtherProofButton);
        
        // ========== OVERALL PAYMENT STATUS SECTION ==========
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(null);
        statusPanel.setBackground(Color.WHITE);
        statusPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(accentColor, 2),
            "OVERALL PAYMENT STATUS",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), accentColor));
        statusPanel.setBounds(20, y + 550, 900, 70);
        container.add(statusPanel);
        
        paymentStatusLabel.setBounds(20, 22, 860, 30);
        paymentStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        if (myPaymentVerified && otherPaymentVerified) {
            paymentStatusLabel.setText("BOTH PAYMENTS VERIFIED! You can now proceed to Step 4.");
            paymentStatusLabel.setForeground(successColor);
            proceedButton.setEnabled(true);
            proceedButton.setText("PROCEED TO NEXT STEP");
        } else if (myPaymentVerified) {
            paymentStatusLabel.setText("Your payment verified. Waiting for " + otherTraderName + "'s payment verification.");
            paymentStatusLabel.setForeground(warningColor);
            proceedButton.setEnabled(false);
        } else if (otherPaymentVerified) {
            paymentStatusLabel.setText(otherTraderName + "'s payment verified. Waiting for your payment verification.");
            paymentStatusLabel.setForeground(warningColor);
            proceedButton.setEnabled(false);
        } else if (myPaymentSubmitted && otherPaymentSubmitted) {
            paymentStatusLabel.setText("Both payments submitted. Waiting for admin verification...");
            paymentStatusLabel.setForeground(warningColor);
            proceedButton.setEnabled(false);
        } else if (myPaymentSubmitted) {
            paymentStatusLabel.setText("Your payment submitted. Waiting for " + otherTraderName + " to submit and admin verification.");
            paymentStatusLabel.setForeground(warningColor);
            proceedButton.setEnabled(false);
        } else if (otherPaymentSubmitted) {
            paymentStatusLabel.setText(otherTraderName + " has submitted payment. Waiting for your payment and admin verification.");
            paymentStatusLabel.setForeground(warningColor);
            proceedButton.setEnabled(false);
        } else {
            paymentStatusLabel.setText("Both traders must submit payment proof. Admin will verify after submission.");
            paymentStatusLabel.setForeground(textColor);
            proceedButton.setEnabled(false);
        }
        
        statusPanel.add(paymentStatusLabel);
        
        return container;
    }
    
    private void openStep3SubmitDialog() {
        step3SubmitHandler = new step3_submit(tradeId, traderId, traderName, otherTraderId, parent);
        step3SubmitHandler.showDialog();
        
        if (onStateChanged != null) onStateChanged.run();
    }
    
    private void viewUploadedProof(String proofPath) {
        if (proofPath == null || proofPath.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No proof image available.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try {
            String fullPath = "src/" + proofPath;
            File imgFile = new File(fullPath);
            
            if (imgFile.exists()) {
                ImageIcon icon = new ImageIcon(fullPath);
                Image img = icon.getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH);
                JOptionPane.showMessageDialog(parent, new JLabel(new ImageIcon(img)), "Payment Proof", JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parent, "Proof image not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "Error loading image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}