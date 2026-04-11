package BarterZone.Dashboard.trader;

import database.config.config;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.List;
import java.util.Map;

public class trades_step5 {
    
    private int tradeId;
    private int traderId;
    private String traderName;
    private int otherTraderId;
    private String otherTraderName;
    private config db;
    private JFrame parent;
    private Runnable onStateChanged;
    private JButton proceedButton;
    
    private JButton addRefundDetailsButton;
    private JLabel myRefundStatusLabel;
    private JLabel otherRefundStatusLabel;
    private JLabel myRefundNumberLabel;
    private JLabel myRefundNameLabel;
    private JLabel otherRefundNumberLabel;
    private JLabel otherRefundNameLabel;
    private JButton viewMyQrCodeButton;
    private JButton viewAdminProofButton;
    private JButton markRefundedButton;
    private JLabel refundOverallStatusLabel;
    private JTextArea adminMessageArea;
    private JScrollPane adminMessageScroll;
    private JLabel myQrCodePreviewLabel;
    private JLabel adminProofStatusLabel;
    private JPanel adminReplyPanel;
    
    private boolean myRefundSubmitted = false;
    private boolean otherRefundSubmitted = false;
    private boolean myRefundConfirmed = false;
    private boolean otherRefundConfirmed = false;
    private String myUploadedQrPath = "";
    private String myRefundProofPath = "";
    private String myAdminMessage = "";
    private String myAdminProofPath = "";
    private String myRefundNumber = "-";
    private String myRefundName = "-";
    private String otherRefundNumber = "-";
    private String otherRefundName = "-";
    
    private static final String REFUND_QR_PATH = "src/BarterZone/resources/images/refund_qrcodes/";
    private static final String REFUND_PROOF_PATH = "src/BarterZone/resources/images/refund_proofs/";
    
    private Color themeColor = new Color(12, 192, 223);
    private Color accentColor = new Color(0, 102, 102);
    private Color successColor = new Color(46, 125, 50);
    private Color warningColor = new Color(255, 153, 0);
    private Color errorColor = new Color(204, 0, 0);
    private Color infoColor = new Color(33, 150, 243);
    private Color textColor = new Color(80, 80, 80);
    private Color bgColor = new Color(250, 250, 250);
    
    public trades_step5(int tradeId, int traderId, String traderName, int otherTraderId, String otherTraderName,
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
        
        createDirectories();
        initComponents();
    }
    
    private void createDirectories() {
        new File(REFUND_QR_PATH).mkdirs();
        new File(REFUND_PROOF_PATH).mkdirs();
    }
    
    private void initComponents() {
        addRefundDetailsButton = new JButton("ADD REFUND DETAILS");
        addRefundDetailsButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        addRefundDetailsButton.setBackground(themeColor);
        addRefundDetailsButton.setForeground(Color.WHITE);
        addRefundDetailsButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addRefundDetailsButton.setFocusPainted(false);
        addRefundDetailsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        myRefundStatusLabel = new JLabel();
        myRefundStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        otherRefundStatusLabel = new JLabel();
        otherRefundStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        myRefundNumberLabel = new JLabel();
        myRefundNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        myRefundNameLabel = new JLabel();
        myRefundNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        otherRefundNumberLabel = new JLabel();
        otherRefundNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        otherRefundNameLabel = new JLabel();
        otherRefundNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        myQrCodePreviewLabel = new JLabel();
        myQrCodePreviewLabel.setHorizontalAlignment(JLabel.CENTER);
        myQrCodePreviewLabel.setVerticalAlignment(JLabel.CENTER);
        myQrCodePreviewLabel.setBorder(new LineBorder(new Color(200, 200, 200)));
        myQrCodePreviewLabel.setText("No QR Code");
        myQrCodePreviewLabel.setBackground(Color.WHITE);
        myQrCodePreviewLabel.setOpaque(true);
        
        viewMyQrCodeButton = new JButton("VIEW MY QR CODE");
        viewMyQrCodeButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        viewMyQrCodeButton.setBackground(accentColor);
        viewMyQrCodeButton.setForeground(Color.WHITE);
        viewMyQrCodeButton.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        viewMyQrCodeButton.setFocusPainted(false);
        viewMyQrCodeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewMyQrCodeButton.setEnabled(false);
        
        viewAdminProofButton = new JButton("VIEW ADMIN PROOF");
        viewAdminProofButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        viewAdminProofButton.setBackground(infoColor);
        viewAdminProofButton.setForeground(Color.WHITE);
        viewAdminProofButton.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        viewAdminProofButton.setFocusPainted(false);
        viewAdminProofButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewAdminProofButton.setEnabled(false);
        
        markRefundedButton = new JButton("MARK AS REFUNDED");
        markRefundedButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        markRefundedButton.setBackground(successColor);
        markRefundedButton.setForeground(Color.WHITE);
        markRefundedButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        markRefundedButton.setFocusPainted(false);
        markRefundedButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        markRefundedButton.setEnabled(false);
        
        adminMessageArea = new JTextArea();
        adminMessageArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        adminMessageArea.setLineWrap(true);
        adminMessageArea.setWrapStyleWord(true);
        adminMessageArea.setEditable(false);
        adminMessageArea.setBackground(new Color(245, 245, 245));
        adminMessageScroll = new JScrollPane(adminMessageArea);
        adminMessageScroll.setBorder(new LineBorder(new Color(200, 200, 200)));
        
        adminProofStatusLabel = new JLabel();
        adminProofStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        refundOverallStatusLabel = new JLabel();
        refundOverallStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }
    
    // Helper method to convert resource path to file path
    private String convertResourcePathToFilePath(String resourcePath) {
        if (resourcePath == null || resourcePath.trim().isEmpty()) {
            return null;
        }
        resourcePath = resourcePath.trim();
        int lastDot = resourcePath.lastIndexOf(".");
        if (lastDot == -1) {
            return null;
        }
        String extension = resourcePath.substring(lastDot + 1);
        String pathWithoutExtension = resourcePath.substring(0, lastDot).replace(".", "/");
        return "src/" + pathWithoutExtension + "." + extension;
    }
    
    public void loadState(boolean myRefundSubmitted, boolean otherRefundSubmitted, boolean myRefundConfirmed, boolean otherRefundConfirmed,
                          String myUploadedQrPath, String myRefundProofPath, String myAdminMessage, String myAdminProofPath,
                          String myRefundNumber, String myRefundName, String otherRefundNumber, String otherRefundName) {
        this.myRefundSubmitted = myRefundSubmitted;
        this.otherRefundSubmitted = otherRefundSubmitted;
        this.myRefundConfirmed = myRefundConfirmed;
        this.otherRefundConfirmed = otherRefundConfirmed;
        this.myUploadedQrPath = myUploadedQrPath;
        this.myRefundProofPath = myRefundProofPath;
        this.myAdminMessage = myAdminMessage;
        this.myAdminProofPath = myAdminProofPath;
        this.myRefundNumber = myRefundNumber;
        this.myRefundName = myRefundName;
        this.otherRefundNumber = otherRefundNumber;
        this.otherRefundName = otherRefundName;
        
        myRefundNumberLabel.setText(myRefundNumber);
        myRefundNameLabel.setText(myRefundName);
        otherRefundNumberLabel.setText(otherRefundNumber);
        otherRefundNameLabel.setText(otherRefundName);
        
        // Load QR code preview if exists
        loadAndDisplayQrCode();
        
        // Update admin reply section with current trader's data only
        updateAdminReplySection();
        
        // Enable view admin proof button if proof exists for current trader
        boolean hasAdminProof = (myAdminProofPath != null && !myAdminProofPath.isEmpty());
        viewAdminProofButton.setEnabled(hasAdminProof);
        
        // Update proof status label
        if (hasAdminProof) {
            adminProofStatusLabel.setText("Proof attached - Click VIEW to see");
            adminProofStatusLabel.setForeground(successColor);
        } else {
            adminProofStatusLabel.setText("No proof attached yet");
            adminProofStatusLabel.setForeground(warningColor);
        }
        
        // Enable mark refunded button if conditions are met
        if (myRefundSubmitted && !myRefundConfirmed && myRefundProofPath != null && !myRefundProofPath.isEmpty()) {
            markRefundedButton.setEnabled(true);
            markRefundedButton.setText("MARK AS REFUNDED");
            markRefundedButton.setBackground(successColor);
        } else {
            markRefundedButton.setEnabled(false);
            if (myRefundConfirmed) {
                markRefundedButton.setText("REFUND CONFIRMED");
                markRefundedButton.setBackground(new Color(150, 150, 150));
            } else {
                markRefundedButton.setText("MARK AS REFUNDED");
                markRefundedButton.setBackground(successColor);
            }
        }
    }
    
    private void loadAndDisplayQrCode() {
        if (myUploadedQrPath != null && !myUploadedQrPath.isEmpty()) {
            try {
                String fullPath = convertResourcePathToFilePath(myUploadedQrPath);
                if (fullPath != null) {
                    File qrFile = new File(fullPath);
                    if (qrFile.exists()) {
                        ImageIcon qrIcon = new ImageIcon(fullPath);
                        Image scaledImage = qrIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                        myQrCodePreviewLabel.setIcon(new ImageIcon(scaledImage));
                        myQrCodePreviewLabel.setText("");
                        viewMyQrCodeButton.setEnabled(true);
                    } else {
                        myQrCodePreviewLabel.setText("QR Code Not Found");
                        viewMyQrCodeButton.setEnabled(false);
                    }
                } else {
                    myQrCodePreviewLabel.setText("Invalid Path");
                    viewMyQrCodeButton.setEnabled(false);
                }
            } catch (Exception e) {
                myQrCodePreviewLabel.setText("Load Error");
                viewMyQrCodeButton.setEnabled(false);
            }
        } else {
            myQrCodePreviewLabel.setText("No QR Code");
            viewMyQrCodeButton.setEnabled(false);
        }
    }
    
    private void updateAdminReplySection() {
        // Only show admin reply for the current logged-in trader
        boolean hasAdminMessage = (myAdminMessage != null && !myAdminMessage.isEmpty());
        boolean hasAdminProof = (myAdminProofPath != null && !myAdminProofPath.isEmpty());
        
        if (hasAdminMessage || hasAdminProof) {
            if (hasAdminMessage) {
                adminMessageArea.setText(myAdminMessage);
                adminMessageArea.setCaretPosition(0);
            } else {
                adminMessageArea.setText("No message from admin.");
            }
        } else {
            adminMessageArea.setText("No admin response yet. Please wait for the admin to process your refund.");
        }
    }
    
    public JPanel buildPanel() {
        JPanel container = new JPanel();
        container.setLayout(null);
        container.setBackground(bgColor);
        container.setBounds(0, 0, 940, 750);
        
        int y = 15;
        
        // ========== REFUND INFORMATION PANEL ==========
        JPanel refundInfoPanel = new JPanel();
        refundInfoPanel.setLayout(null);
        refundInfoPanel.setBackground(new Color(255, 245, 220));
        refundInfoPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(warningColor, 2),
            "REFUND INFORMATION",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), warningColor));
        refundInfoPanel.setBounds(20, y, 900, 80);
        container.add(refundInfoPanel);
        
        JLabel refundInfoLabel = new JLabel("<html>Admin will process refunds after both traders have confirmed receipt.<br>Please provide your refund details below. Once submitted, you cannot edit them.</html>");
        refundInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        refundInfoLabel.setBounds(15, 20, 870, 50);
        refundInfoPanel.add(refundInfoLabel);
        y += 100;
        
        // ========== MY REFUND DETAILS PANEL ==========
        JPanel myRefundPanel = new JPanel();
        myRefundPanel.setLayout(null);
        myRefundPanel.setBackground(Color.WHITE);
        myRefundPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(accentColor, 2),
            "MY REFUND DETAILS",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), accentColor));
        myRefundPanel.setBounds(20, y, 440, 380);
        container.add(myRefundPanel);
        
        int myY = 25;
        
        // Add Refund Details Button
        addRefundDetailsButton.setBounds(120, myY, 200, 40);
        addRefundDetailsButton.addActionListener(e -> showAddRefundDetailsDialog());
        myRefundPanel.add(addRefundDetailsButton);
        myY += 55;
        
        // Account Number
        JLabel refundNumberTitle = new JLabel("Account Number:");
        refundNumberTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refundNumberTitle.setBounds(20, myY, 120, 25);
        myRefundPanel.add(refundNumberTitle);
        
        myRefundNumberLabel.setBounds(150, myY, 250, 25);
        myRefundPanel.add(myRefundNumberLabel);
        myY += 35;
        
        // Account Name
        JLabel refundNameTitle = new JLabel("Account Name:");
        refundNameTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refundNameTitle.setBounds(20, myY, 120, 25);
        myRefundPanel.add(refundNameTitle);
        
        myRefundNameLabel.setBounds(150, myY, 250, 25);
        myRefundPanel.add(myRefundNameLabel);
        myY += 45;
        
        // QR Code Section
        JLabel qrPreviewTitle = new JLabel("QR Code:");
        qrPreviewTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        qrPreviewTitle.setBounds(20, myY, 120, 25);
        myRefundPanel.add(qrPreviewTitle);
        
        myQrCodePreviewLabel.setBounds(150, myY, 80, 80);
        myRefundPanel.add(myQrCodePreviewLabel);
        
        viewMyQrCodeButton.setBounds(240, myY + 25, 150, 30);
        viewMyQrCodeButton.addActionListener(e -> viewQrCode());
        myRefundPanel.add(viewMyQrCodeButton);
        myY += 95;
        
        // Separator
        JSeparator mySeparator = new JSeparator();
        mySeparator.setBounds(20, myY, 400, 2);
        myRefundPanel.add(mySeparator);
        myY += 20;
        
        // Refund Status
        JLabel refundStatusTitle = new JLabel("Refund Status:");
        refundStatusTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refundStatusTitle.setBounds(20, myY, 120, 25);
        myRefundPanel.add(refundStatusTitle);
        
        if (myRefundConfirmed) {
            myRefundStatusLabel.setText("REFUND COMPLETED AND CONFIRMED");
            myRefundStatusLabel.setForeground(successColor);
        } else if (myRefundSubmitted) {
            myRefundStatusLabel.setText("DETAILS SUBMITTED - WAITING FOR ADMIN");
            myRefundStatusLabel.setForeground(warningColor);
        } else {
            myRefundStatusLabel.setText("NOT SUBMITTED YET");
            myRefundStatusLabel.setForeground(errorColor);
        }
        myRefundStatusLabel.setBounds(150, myY, 250, 25);
        myRefundPanel.add(myRefundStatusLabel);
        myY += 40;
        
        // Mark as Refunded Button
        markRefundedButton.setBounds(120, myY, 200, 40);
        markRefundedButton.addActionListener(e -> markAsRefunded());
        myRefundPanel.add(markRefundedButton);
        
        // ========== OTHER TRADER REFUND DETAILS PANEL ==========
        JPanel otherRefundPanel = new JPanel();
        otherRefundPanel.setLayout(null);
        otherRefundPanel.setBackground(Color.WHITE);
        otherRefundPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(accentColor, 2),
            otherTraderName.toUpperCase() + "'S REFUND DETAILS",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), accentColor));
        otherRefundPanel.setBounds(480, y, 440, 250);
        container.add(otherRefundPanel);
        
        int otherY = 25;
        
        // Account Number
        JLabel otherRefundNumberTitle = new JLabel("Account Number:");
        otherRefundNumberTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        otherRefundNumberTitle.setBounds(20, otherY, 120, 25);
        otherRefundPanel.add(otherRefundNumberTitle);
        
        otherRefundNumberLabel.setBounds(150, otherY, 250, 25);
        otherRefundPanel.add(otherRefundNumberLabel);
        otherY += 35;
        
        // Account Name
        JLabel otherRefundNameTitle = new JLabel("Account Name:");
        otherRefundNameTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        otherRefundNameTitle.setBounds(20, otherY, 120, 25);
        otherRefundPanel.add(otherRefundNameTitle);
        
        otherRefundNameLabel.setBounds(150, otherY, 250, 25);
        otherRefundPanel.add(otherRefundNameLabel);
        otherY += 45;
        
        // Separator
        JSeparator otherSeparator = new JSeparator();
        otherSeparator.setBounds(20, otherY, 400, 2);
        otherRefundPanel.add(otherSeparator);
        otherY += 20;
        
        // Refund Status
        JLabel otherRefundStatusTitle = new JLabel("Refund Status:");
        otherRefundStatusTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        otherRefundStatusTitle.setBounds(20, otherY, 120, 25);
        otherRefundPanel.add(otherRefundStatusTitle);
        
        if (otherRefundConfirmed) {
            otherRefundStatusLabel.setText("REFUND COMPLETED AND CONFIRMED");
            otherRefundStatusLabel.setForeground(successColor);
        } else if (otherRefundSubmitted) {
            otherRefundStatusLabel.setText("DETAILS SUBMITTED - WAITING FOR ADMIN");
            otherRefundStatusLabel.setForeground(warningColor);
        } else {
            otherRefundStatusLabel.setText("NOT SUBMITTED YET");
            otherRefundStatusLabel.setForeground(errorColor);
        }
        otherRefundStatusLabel.setBounds(150, otherY, 250, 25);
        otherRefundPanel.add(otherRefundStatusLabel);
        
        y += 400;
        
        // ========== ADMIN REPLY SECTION (CURRENT TRADER ONLY) ==========
        adminReplyPanel = new JPanel();
        adminReplyPanel.setLayout(null);
        adminReplyPanel.setBackground(new Color(220, 240, 255));
        adminReplyPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(infoColor, 2),
            "ADMIN REPLY",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), infoColor));
        adminReplyPanel.setBounds(20, y, 900, 180);
        container.add(adminReplyPanel);
        
        int arY = 25;
        
        // Admin Message Section
        JLabel messageTitle = new JLabel("Admin Message:");
        messageTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        messageTitle.setBounds(20, arY, 120, 25);
        adminReplyPanel.add(messageTitle);
        
        adminMessageScroll.setBounds(150, arY, 550, 80);
        adminReplyPanel.add(adminMessageScroll);
        arY += 95;
        
        // Admin Proof Section
        JLabel proofTitle = new JLabel("Admin Proof:");
        proofTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        proofTitle.setBounds(20, arY, 120, 25);
        adminReplyPanel.add(proofTitle);
        
        adminProofStatusLabel.setBounds(150, arY, 200, 25);
        adminReplyPanel.add(adminProofStatusLabel);
        
        viewAdminProofButton.setBounds(370, arY, 160, 30);
        viewAdminProofButton.addActionListener(e -> viewAdminProof());
        adminReplyPanel.add(viewAdminProofButton);
        
        // ========== OVERALL REFUND STATUS PANEL ==========
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(null);
        statusPanel.setBackground(Color.WHITE);
        statusPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(accentColor, 2),
            "OVERALL REFUND STATUS",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), accentColor));
        statusPanel.setBounds(20, y + 200, 900, 70);
        container.add(statusPanel);
        
        refundOverallStatusLabel.setBounds(20, 20, 860, 35);
        refundOverallStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        if (myRefundConfirmed && otherRefundConfirmed) {
            refundOverallStatusLabel.setText("BOTH REFUNDS CONFIRMED! Click PROCEED to complete the trade.");
            refundOverallStatusLabel.setForeground(successColor);
            proceedButton.setEnabled(true);
            proceedButton.setText("PROCEED TO NEXT STEP");
        } else if (myRefundConfirmed) {
            refundOverallStatusLabel.setText("Your refund confirmed. Waiting for " + otherTraderName + " to confirm refund.");
            refundOverallStatusLabel.setForeground(warningColor);
            proceedButton.setEnabled(false);
        } else if (otherRefundConfirmed) {
            refundOverallStatusLabel.setText(otherTraderName + "'s refund confirmed. Waiting for your confirmation.");
            refundOverallStatusLabel.setForeground(warningColor);
            proceedButton.setEnabled(false);
        } else if (myRefundSubmitted && otherRefundSubmitted) {
            refundOverallStatusLabel.setText("Both refund details submitted. Waiting for admin to process refunds...");
            refundOverallStatusLabel.setForeground(warningColor);
            proceedButton.setEnabled(false);
        } else if (myRefundSubmitted) {
            refundOverallStatusLabel.setText("Your refund details submitted. Waiting for " + otherTraderName + " to submit and admin to process.");
            refundOverallStatusLabel.setForeground(warningColor);
            proceedButton.setEnabled(false);
        } else if (otherRefundSubmitted) {
            refundOverallStatusLabel.setText(otherTraderName + " submitted refund details. Waiting for your submission and admin to process.");
            refundOverallStatusLabel.setForeground(warningColor);
            proceedButton.setEnabled(false);
        } else {
            refundOverallStatusLabel.setText("Both traders must submit refund details. Admin will process after submission.");
            refundOverallStatusLabel.setForeground(textColor);
            proceedButton.setEnabled(false);
        }
        
        statusPanel.add(refundOverallStatusLabel);
        
        return container;
    }
    
    private void showAddRefundDetailsDialog() {
        if (myRefundSubmitted) {
            JOptionPane.showMessageDialog(parent, "You have already submitted refund details and cannot edit them.", "Already Submitted", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JDialog refundDialog = new JDialog(parent, "Add Refund Details", true);
        refundDialog.setSize(550, 600);
        refundDialog.setLayout(null);
        refundDialog.setLocationRelativeTo(parent);
        refundDialog.getContentPane().setBackground(Color.WHITE);
        
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(themeColor);
        titlePanel.setBounds(0, 0, 550, 50);
        titlePanel.setLayout(null);
        
        JLabel titleLabel = new JLabel("ADD REFUND DETAILS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(20, 10, 300, 30);
        titlePanel.add(titleLabel);
        refundDialog.add(titlePanel);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBounds(10, 60, 530, 490);
        refundDialog.add(contentPanel);
        
        int y = 20;
        int labelWidth = 130;
        int fieldWidth = 330;
        int fieldX = 160;
        
        // Account Number Field
        JLabel numberLabel = new JLabel("Account Number:");
        numberLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        numberLabel.setBounds(20, y, labelWidth, 35);
        contentPanel.add(numberLabel);
        
        JTextField accountNumberField = new JTextField();
        accountNumberField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        accountNumberField.setBounds(fieldX, y, fieldWidth, 35);
        accountNumberField.setBorder(new LineBorder(new Color(200, 200, 200)));
        contentPanel.add(accountNumberField);
        y += 50;
        
        // Account Name Field
        JLabel nameLabel = new JLabel("Account Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLabel.setBounds(20, y, labelWidth, 35);
        contentPanel.add(nameLabel);
        
        JTextField accountNameField = new JTextField();
        accountNameField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        accountNameField.setBounds(fieldX, y, fieldWidth, 35);
        accountNameField.setBorder(new LineBorder(new Color(200, 200, 200)));
        contentPanel.add(accountNameField);
        y += 55;
        
        // QR Code Upload Section
        JLabel qrLabel = new JLabel("QR Code (Optional):");
        qrLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        qrLabel.setBounds(20, y, labelWidth, 35);
        contentPanel.add(qrLabel);
        
        JButton uploadQrButton = new JButton("UPLOAD QR CODE");
        uploadQrButton.setBounds(fieldX, y, 160, 35);
        uploadQrButton.setBackground(themeColor);
        uploadQrButton.setForeground(Color.WHITE);
        uploadQrButton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        uploadQrButton.setFocusPainted(false);
        uploadQrButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPanel.add(uploadQrButton);
        
        JLabel qrFileNameLabel = new JLabel("No file chosen");
        qrFileNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        qrFileNameLabel.setForeground(textColor);
        qrFileNameLabel.setBounds(fieldX + 175, y, 200, 35);
        contentPanel.add(qrFileNameLabel);
        y += 50;
        
        // QR Code Preview
        JLabel qrPreviewLabel = new JLabel();
        qrPreviewLabel.setBounds(fieldX, y, 100, 100);
        qrPreviewLabel.setBorder(new LineBorder(new Color(200, 200, 200)));
        qrPreviewLabel.setHorizontalAlignment(JLabel.CENTER);
        qrPreviewLabel.setVerticalAlignment(JLabel.CENTER);
        qrPreviewLabel.setText("Preview");
        qrPreviewLabel.setBackground(Color.WHITE);
        qrPreviewLabel.setOpaque(true);
        contentPanel.add(qrPreviewLabel);
        y += 115;
        
        // Refund Reason Field
        JLabel reasonLabel = new JLabel("Refund Reason (Optional):");
        reasonLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        reasonLabel.setBounds(20, y, 180, 35);
        contentPanel.add(reasonLabel);
        
        JTextArea reasonArea = new JTextArea();
        reasonArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        reasonArea.setLineWrap(true);
        reasonArea.setWrapStyleWord(true);
        JScrollPane reasonScroll = new JScrollPane(reasonArea);
        reasonScroll.setBounds(fieldX, y, fieldWidth, 70);
        reasonScroll.setBorder(new LineBorder(new Color(200, 200, 200)));
        contentPanel.add(reasonScroll);
        y += 85;
        
        final String[] uploadedQrPath = {""};
        
        uploadQrButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));
            if (fileChooser.showOpenDialog(refundDialog) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String savedPath = saveRefundQrImage(selectedFile.getAbsolutePath(), selectedFile.getName());
                uploadedQrPath[0] = savedPath;
                qrFileNameLabel.setText(selectedFile.getName());
                
                try {
                    ImageIcon previewIcon = new ImageIcon(selectedFile.getAbsolutePath());
                    Image scaledImage = previewIcon.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
                    qrPreviewLabel.setIcon(new ImageIcon(scaledImage));
                    qrPreviewLabel.setText("");
                } catch (Exception ex) {
                    qrPreviewLabel.setText("Error");
                }
            }
        });
        
        // Submit Button
        JButton submitButton = new JButton("SUBMIT REFUND DETAILS");
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitButton.setBackground(successColor);
        submitButton.setForeground(Color.WHITE);
        submitButton.setBounds(150, y, 250, 45);
        submitButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        submitButton.setFocusPainted(false);
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.addActionListener(e -> {
            String accountNumber = accountNumberField.getText().trim();
            String accountName = accountNameField.getText().trim();
            
            if (accountNumber.isEmpty() || accountName.isEmpty()) {
                JOptionPane.showMessageDialog(refundDialog, "Account Number and Account Name are required!", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String reason = reasonArea.getText().trim();
            
            String sql = "INSERT INTO tbl_refund (trade_id, user_id, account_number, account_name, qr_code_path, refund_reason, created_date) "
                    + "VALUES (?, ?, ?, ?, ?, ?, datetime('now'))";
            db.addRecord(sql, tradeId, traderId, accountNumber, accountName, uploadedQrPath[0], reason);
            
            JOptionPane.showMessageDialog(refundDialog, "Refund details submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            refundDialog.dispose();
            
            if (onStateChanged != null) onStateChanged.run();
        });
        contentPanel.add(submitButton);
        
        JButton cancelButton = new JButton("CANCEL");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setBackground(errorColor);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBounds(420, y, 100, 45);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> refundDialog.dispose());
        contentPanel.add(cancelButton);
        
        refundDialog.setVisible(true);
    }
    
    private String saveRefundQrImage(String sourcePath, String originalFileName) {
        try {
            File directory = new File(REFUND_QR_PATH);
            if (!directory.exists()) directory.mkdirs();
            
            String extension = "";
            String nameWithoutExt = originalFileName;
            int dotIndex = originalFileName.lastIndexOf(".");
            if (dotIndex > 0) {
                nameWithoutExt = originalFileName.substring(0, dotIndex);
                extension = originalFileName.substring(dotIndex);
            }
            
            String destinationPath = REFUND_QR_PATH + originalFileName;
            File destFile = new File(destinationPath);
            int counter = 1;
            
            while (destFile.exists()) {
                String newFileName = nameWithoutExt + "_" + counter + extension;
                destinationPath = REFUND_QR_PATH + newFileName;
                destFile = new File(destinationPath);
                counter++;
            }
            
            Files.copy(Paths.get(sourcePath), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
            return "BarterZone.resources.images.refund_qrcodes." + destFile.getName();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    private void viewQrCode() {
        if (myUploadedQrPath == null || myUploadedQrPath.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No QR Code available.", "QR Code Not Found", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String fullPath = convertResourcePathToFilePath(myUploadedQrPath);
        if (fullPath == null) {
            JOptionPane.showMessageDialog(parent, "Invalid QR Code path.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            File qrFile = new File(fullPath);
            if (qrFile.exists()) {
                ImageIcon qrIcon = new ImageIcon(fullPath);
                Image scaledImage = qrIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                JOptionPane.showMessageDialog(parent, new JLabel(new ImageIcon(scaledImage)), "My QR Code", JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parent, "QR Code image file not found at: " + fullPath, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "Error loading QR Code: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void viewAdminProof() {
        if (myAdminProofPath == null || myAdminProofPath.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No admin proof available.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String fullPath = convertResourcePathToFilePath(myAdminProofPath);
        if (fullPath == null) {
            JOptionPane.showMessageDialog(parent, "Invalid admin proof path.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            File proofFile = new File(fullPath);
            if (proofFile.exists()) {
                ImageIcon proofIcon = new ImageIcon(fullPath);
                Image scaledImage = proofIcon.getImage().getScaledInstance(600, 600, Image.SCALE_SMOOTH);
                JOptionPane.showMessageDialog(parent, new JLabel(new ImageIcon(scaledImage)), "Admin Refund Proof", JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parent, "Admin proof image not found at: " + fullPath, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "Error loading admin proof: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void markAsRefunded() {
        if (!myRefundSubmitted) {
            JOptionPane.showMessageDialog(parent,
                "You must submit your refund details first before marking as refunded.\n\nPlease click 'ADD REFUND DETAILS' to provide your account information.",
                "Refund Details Required",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (myRefundConfirmed) {
            JOptionPane.showMessageDialog(parent, "Your refund has already been confirmed.", "Already Refunded", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (myRefundProofPath == null || myRefundProofPath.isEmpty()) {
            JOptionPane.showMessageDialog(parent,
                "Admin has not yet processed your refund.\n\nPlease wait for the admin to process and upload the refund proof.\nYou will be notified when your refund is ready.",
                "Refund Not Ready",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(parent,
            "Confirm that you have received your refund?\n\nPlease verify that the refund amount has been credited to your account.\nThis action cannot be undone.",
            "Confirm Refund Receipt",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_refund SET is_refunded = 1, refund_confirmed_date = datetime('now') WHERE trade_id = ? AND user_id = ?";
            db.updateRecord(sql, tradeId, traderId);
            
            JOptionPane.showMessageDialog(parent,
                "Refund confirmed! Thank you for using BarterZone.\n\nYour refund has been successfully recorded.",
                "Refund Confirmed",
                JOptionPane.INFORMATION_MESSAGE);
            
            if (onStateChanged != null) onStateChanged.run();
        }
    }
}