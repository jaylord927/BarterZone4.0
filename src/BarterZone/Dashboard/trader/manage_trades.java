package BarterZone.Dashboard.trader;

import BarterZone.Dashboard.session.user_session;
import BarterZone.resources.IconManager;
import database.config.config;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.border.LineBorder;
import java.awt.Dimension;
import javax.swing.BorderFactory;

public class manage_trades extends javax.swing.JFrame {

    private int tradeId;
    private int traderId;
    private String traderName;
    private int otherTraderId;
    private String otherTraderName;
    private String myItem;
    private String theirItem;
    private user_session session;
    private config db;
    private IconManager iconManager;
    
    private int currentStep = 1;
    private String exchangeMethod = null;
    private String proposedMethod = null;
    private int proposedBy = -1;
    private boolean methodConfirmed = false;
    
    private int myDetailId = -1;
    private int myMeetupId = -1;
    private int myDeliveryId = -1;
    private int myPaymentId = -1;
    private int otherDetailId = -1;
    private int otherMeetupId = -1;
    private int otherDeliveryId = -1;
    private int otherPaymentId = -1;
    
    private boolean myDetailsSubmitted = false;
    private boolean otherDetailsSubmitted = false;
    private int myDetailsAgreed = 0;
    private int otherDetailsAgreed = 0;
    
    private boolean paymentVerified = false;
    private boolean paymentSubmitted = false;
    
    private boolean myItemReceived = false;
    private boolean otherItemReceived = false;
    private boolean refundProcessed = false;
    private boolean tradeCompleted = false;
    
    // Step 2 and Step 3 handlers
    private step2_submit step2Handler;
    private step3_submit step3Handler;
    
    private JPanel headerPanel;
    private JPanel contentPanel;
    private JLabel stepIndicatorLabel;
    private JLabel statusLabel;
    private JLabel tradeInfoLabel;
    private JPanel stepPanel;
    private JPanel navigationPanel;
    private JButton proceedButton;
    private JButton backStepButton;
    private JButton backToTradesButton;
    private JButton refreshButton;
    private JButton cancelTradeButton;
    private JScrollPane stepScrollPane;
    private JLabel stepReminderLabel;
    
    private JRadioButton deliveryRadio;
    private JRadioButton meetupRadio;
    private ButtonGroup methodGroup;
    private JLabel methodStatusLabel;
    private JButton agreeMethodButton;
    private JButton disagreeMethodButton;
    private JButton proposeButton;
    private JLabel otherTraderMethodLabel;
    
    private JPanel myDetailsPanel;
    private JPanel otherDetailsPanel;
    
    private JTextArea otherDetailsArea;
    private JScrollPane otherDetailsScroll;
    private JLabel myDetailsStatus;
    private JLabel otherDetailsStatus;
    private JButton submitDetailsButton;
    private JButton agreeToOtherDetailsButton;
    private JLabel myAgreementStatus;
    private JLabel otherAgreementStatus;
    
    private JLabel paymentStatusLabel;
    private JCheckBox confirmReceivedCheck;
    
    private java.util.Stack<Integer> stepHistory = new java.util.Stack<>();
    
    private Color themeColor = new Color(12, 192, 223);
    private Color hoverColor = new Color(70, 210, 235);
    private Color activeColor = new Color(0, 150, 180);
    private Color headerBgColor = new Color(245, 245, 245);
    private Color textColor = new Color(80, 80, 80);
    private Color accentColor = new Color(0, 102, 102);
    private Color successColor = new Color(46, 125, 50);
    private Color warningColor = new Color(255, 153, 0);
    private Color errorColor = new Color(204, 0, 0);
    private Color infoColor = new Color(33, 150, 243);

    public manage_trades(int tradeId, String myItem, String theirItem, String otherTraderName, int otherTraderId) {
        this.tradeId = tradeId;
        this.myItem = myItem;
        this.theirItem = theirItem;
        this.otherTraderName = otherTraderName;
        this.otherTraderId = otherTraderId;
        this.session = user_session.getInstance();
        this.traderId = session.getUserId();
        this.traderName = session.getFullName();
        this.db = new config();
        this.iconManager = IconManager.getInstance();
        
        initComponents();
        loadTradeState();
        updateUI();
        
        setTitle("Manage Trade - Trade #" + tradeId);
        setSize(1000, 800);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        headerPanel = new JPanel();
        headerPanel.setLayout(null);
        headerPanel.setBackground(headerBgColor);
        headerPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        headerPanel.setBounds(0, 0, 1000, 70);
        getContentPane().add(headerPanel);

        JLabel headerTitle = new JLabel("MANAGE TRADE");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerTitle.setForeground(accentColor);
        headerTitle.setBounds(20, 15, 250, 30);
        headerPanel.add(headerTitle);

        JLabel tradeIdLabel = new JLabel("Trade #" + tradeId);
        tradeIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tradeIdLabel.setForeground(accentColor);
        tradeIdLabel.setBounds(880, 25, 100, 25);
        headerPanel.add(tradeIdLabel);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        JLabel currentDateLabel = new JLabel(sdf.format(new Date()));
        currentDateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        currentDateLabel.setForeground(new Color(102, 102, 102));
        currentDateLabel.setBounds(700, 25, 180, 20);
        headerPanel.add(currentDateLabel);

        contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(new Color(250, 250, 250));
        contentPanel.setBounds(0, 70, 1000, 730);
        getContentPane().add(contentPanel);
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(null);
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        infoPanel.setBounds(20, 10, 960, 70);

        tradeInfoLabel = new JLabel();
        tradeInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tradeInfoLabel.setBounds(10, 10, 940, 50);
        tradeInfoLabel.setText("<html>"
                + "<b>Your Item:</b> " + myItem + "<br>"
                + "<b>Their Item:</b> " + theirItem + "<br>"
                + "<b>Trading with:</b> " + otherTraderName
                + "</html>");
        infoPanel.add(tradeInfoLabel);

        contentPanel.add(infoPanel);

        stepIndicatorLabel = new JLabel();
        stepIndicatorLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        stepIndicatorLabel.setForeground(accentColor);
        stepIndicatorLabel.setBounds(20, 90, 960, 35);
        contentPanel.add(stepIndicatorLabel);
        
        stepReminderLabel = new JLabel();
        stepReminderLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        stepReminderLabel.setForeground(infoColor);
        stepReminderLabel.setBounds(20, 120, 960, 25);
        contentPanel.add(stepReminderLabel);

        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(textColor);
        statusLabel.setBounds(20, 145, 960, 25);
        contentPanel.add(statusLabel);

        stepPanel = new JPanel();
        stepPanel.setLayout(null);
        stepPanel.setBackground(Color.WHITE);
        
        stepScrollPane = new JScrollPane(stepPanel);
        stepScrollPane.setBounds(20, 175, 960, 470);
        stepScrollPane.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        stepScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        stepScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contentPanel.add(stepScrollPane);
        
        stepPanel.setPreferredSize(new Dimension(940, 700));

        navigationPanel = new JPanel();
        navigationPanel.setLayout(null);
        navigationPanel.setBackground(new Color(240, 240, 240));
        navigationPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        navigationPanel.setBounds(20, 655, 960, 55);
        contentPanel.add(navigationPanel);

        proceedButton = new JButton("PROCEED TO NEXT STEP");
        proceedButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        proceedButton.setBackground(successColor);
        proceedButton.setForeground(Color.WHITE);
        proceedButton.setBounds(410, 12, 180, 32);
        proceedButton.setBorder(null);
        proceedButton.setFocusPainted(false);
        proceedButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        proceedButton.addActionListener(e -> handleProceed());
        navigationPanel.add(proceedButton);
        
        backStepButton = new JButton("BACK TO PREVIOUS STEP");
        backStepButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        backStepButton.setBackground(new Color(102, 102, 102));
        backStepButton.setForeground(Color.WHITE);
        backStepButton.setBounds(210, 12, 180, 32);
        backStepButton.setBorder(null);
        backStepButton.setFocusPainted(false);
        backStepButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backStepButton.addActionListener(e -> goBackStep());
        navigationPanel.add(backStepButton);
        
        backToTradesButton = new JButton("BACK TO TRADES");
        backToTradesButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        backToTradesButton.setBackground(accentColor);
        backToTradesButton.setForeground(Color.WHITE);
        backToTradesButton.setBounds(610, 12, 140, 32);
        backToTradesButton.setBorder(null);
        backToTradesButton.setFocusPainted(false);
        backToTradesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backToTradesButton.addActionListener(e -> goBackToTrades());
        navigationPanel.add(backToTradesButton);

        refreshButton = new JButton("REFRESH");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        refreshButton.setBackground(themeColor);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBounds(770, 12, 80, 32);
        refreshButton.setBorder(null);
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> {
            loadTradeState();
            updateUI();
        });
        navigationPanel.add(refreshButton);

        cancelTradeButton = new JButton("CANCEL TRADE");
        cancelTradeButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        cancelTradeButton.setBackground(errorColor);
        cancelTradeButton.setForeground(Color.WHITE);
        cancelTradeButton.setBounds(860, 12, 90, 32);
        cancelTradeButton.setBorder(null);
        cancelTradeButton.setFocusPainted(false);
        cancelTradeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelTradeButton.addActionListener(e -> cancelTrade());
        navigationPanel.add(cancelTradeButton);

        initializeStepComponents();
    }

    private void initializeStepComponents() {
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
        
        myDetailsPanel = new JPanel();
        myDetailsPanel.setLayout(null);
        myDetailsPanel.setBackground(new Color(250, 250, 250));
        myDetailsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor), "MY DETAILS"));
        
        otherDetailsPanel = new JPanel();
        otherDetailsPanel.setLayout(null);
        otherDetailsPanel.setBackground(new Color(250, 250, 250));
        otherDetailsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor), otherTraderName + "'S DETAILS"));
        
        otherDetailsArea = new JTextArea(8, 30);
        otherDetailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        otherDetailsArea.setLineWrap(true);
        otherDetailsArea.setWrapStyleWord(true);
        otherDetailsArea.setEditable(false);
        otherDetailsArea.setBackground(new Color(245, 245, 245));
        otherDetailsScroll = new JScrollPane(otherDetailsArea);
        
        myDetailsStatus = new JLabel();
        myDetailsStatus.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        otherDetailsStatus = new JLabel();
        otherDetailsStatus.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        
        submitDetailsButton = new JButton("SUBMIT MY DETAILS");
        submitDetailsButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        submitDetailsButton.setBackground(accentColor);
        submitDetailsButton.setForeground(Color.WHITE);
        submitDetailsButton.setBorder(null);
        submitDetailsButton.setFocusPainted(false);
        submitDetailsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        agreeToOtherDetailsButton = new JButton("AGREE TO DETAILS");
        agreeToOtherDetailsButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        agreeToOtherDetailsButton.setBackground(successColor);
        agreeToOtherDetailsButton.setForeground(Color.WHITE);
        agreeToOtherDetailsButton.setBorder(null);
        agreeToOtherDetailsButton.setFocusPainted(false);
        agreeToOtherDetailsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        myAgreementStatus = new JLabel();
        myAgreementStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        otherAgreementStatus = new JLabel();
        otherAgreementStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        paymentStatusLabel = new JLabel();
        paymentStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        confirmReceivedCheck = new JCheckBox("I have received the item");
        confirmReceivedCheck.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        confirmReceivedCheck.setBackground(Color.WHITE);
    }

    private void loadTradeState() {
        String sql = "SELECT * FROM tbl_trade WHERE trade_id = ?";
        List<Map<String, Object>> trade = db.fetchRecords(sql, tradeId);
        
        if (!trade.isEmpty()) {
            Map<String, Object> t = trade.get(0);
            
            exchangeMethod = (String) t.get("exchange_method");
            proposedMethod = t.get("proposed_method") != null ? (String) t.get("proposed_method") : null;
            proposedBy = t.get("proposed_by") != null ? Integer.parseInt(t.get("proposed_by").toString()) : -1;
            methodConfirmed = t.get("method_confirmed") != null && Integer.parseInt(t.get("method_confirmed").toString()) == 1;
            
            myItemReceived = t.get("my_item_received") != null && 
                Integer.parseInt(t.get("my_item_received").toString()) == 1;
            otherItemReceived = t.get("other_item_received") != null && 
                Integer.parseInt(t.get("other_item_received").toString()) == 1;
            tradeCompleted = "completed".equals(t.get("trade_status"));
            
            determineCurrentStep();
        }
        
        loadMyTradeDetails();
        loadOtherTradeDetails();
    }
    
    private void loadMyTradeDetails() {
        String sql = "SELECT * FROM tbl_trade_details WHERE trade_id = ? AND trader_id = ?";
        List<Map<String, Object>> details = db.fetchRecords(sql, tradeId, traderId);
        
        if (!details.isEmpty()) {
            Map<String, Object> d = details.get(0);
            myDetailId = Integer.parseInt(d.get("detail_id").toString());
            myDetailsSubmitted = d.get("my_details_submitted") != null && 
                Integer.parseInt(d.get("my_details_submitted").toString()) == 1;
            myDetailsAgreed = d.get("my_details_agreed") != null ? 
                Integer.parseInt(d.get("my_details_agreed").toString()) : 0;
            otherDetailsAgreed = d.get("other_details_agreed") != null ? 
                Integer.parseInt(d.get("other_details_agreed").toString()) : 0;
            
            if (d.get("meetup_id") != null && Integer.parseInt(d.get("meetup_id").toString()) > 0) {
                myMeetupId = Integer.parseInt(d.get("meetup_id").toString());
            }
            if (d.get("delivery_id") != null && Integer.parseInt(d.get("delivery_id").toString()) > 0) {
                myDeliveryId = Integer.parseInt(d.get("delivery_id").toString());
            }
            if (d.get("payment_id") != null && Integer.parseInt(d.get("payment_id").toString()) > 0) {
                myPaymentId = Integer.parseInt(d.get("payment_id").toString());
            }
        }
    }
    
    private void loadOtherTradeDetails() {
        String sql = "SELECT * FROM tbl_trade_details WHERE trade_id = ? AND trader_id = ?";
        List<Map<String, Object>> details = db.fetchRecords(sql, tradeId, otherTraderId);
        
        StringBuilder detailsText = new StringBuilder();
        
        if (!details.isEmpty()) {
            Map<String, Object> d = details.get(0);
            otherDetailId = Integer.parseInt(d.get("detail_id").toString());
            otherDetailsSubmitted = d.get("other_details_submitted") != null && 
                Integer.parseInt(d.get("other_details_submitted").toString()) == 1;
            
            if (d.get("meetup_id") != null && Integer.parseInt(d.get("meetup_id").toString()) > 0) {
                otherMeetupId = Integer.parseInt(d.get("meetup_id").toString());
                loadMeetupDetails(otherMeetupId);
            } else if (d.get("delivery_id") != null && Integer.parseInt(d.get("delivery_id").toString()) > 0) {
                otherDeliveryId = Integer.parseInt(d.get("delivery_id").toString());
                loadDeliveryDetails(otherDeliveryId);
            } else {
                detailsText.append("No details submitted yet.");
                otherDetailsArea.setText(detailsText.toString());
            }
        } else {
            detailsText.append("No details submitted yet.");
            otherDetailsArea.setText(detailsText.toString());
        }
    }
    
    private void loadMeetupDetails(int meetupId) {
        String sql = "SELECT * FROM tbl_meetup_details WHERE meetup_id = ?";
        List<Map<String, Object>> details = db.fetchRecords(sql, meetupId);
        
        if (!details.isEmpty()) {
            Map<String, Object> d = details.get(0);
            StringBuilder detailsText = new StringBuilder();
            detailsText.append("Exchange Method: Meetup\n\n");
            detailsText.append("Location: ").append(d.get("location") != null ? d.get("location") : "Not provided").append("\n");
            String mapsLink = d.get("google_maps_link") != null ? d.get("google_maps_link").toString() : "";
            if (!mapsLink.isEmpty()) detailsText.append("Google Maps: ").append(mapsLink).append("\n");
            detailsText.append("Date: ").append(d.get("date") != null ? d.get("date") : "Not provided").append("\n");
            detailsText.append("Time: ").append(d.get("time") != null ? d.get("time") : "Not provided").append("\n");
            detailsText.append("Contact: ").append(d.get("contact_person") != null ? d.get("contact_person") : "Not provided");
            detailsText.append(" - ").append(d.get("contact_number") != null ? d.get("contact_number") : "Not provided").append("\n");
            detailsText.append("Instructions: ").append(d.get("instructions") != null ? d.get("instructions") : "None");
            otherDetailsArea.setText(detailsText.toString());
            otherDetailsArea.setCaretPosition(0);
        }
    }
    
    private void loadDeliveryDetails(int deliveryId) {
        String sql = "SELECT * FROM tbl_delivery_details WHERE delivery_id = ?";
        List<Map<String, Object>> details = db.fetchRecords(sql, deliveryId);
        
        if (!details.isEmpty()) {
            Map<String, Object> d = details.get(0);
            StringBuilder detailsText = new StringBuilder();
            detailsText.append("Exchange Method: Delivery\n\n");
            detailsText.append("Address: ").append(d.get("address") != null ? d.get("address") : "Not provided").append("\n");
            detailsText.append("Courier: ").append(d.get("courier") != null ? d.get("courier") : "Not provided").append("\n");
            detailsText.append("Expected Date: ").append(d.get("expected_date") != null ? d.get("expected_date") : "Not provided").append("\n");
            detailsText.append("Tracking: ").append(d.get("tracking_number") != null ? d.get("tracking_number") : "Not provided").append("\n");
            detailsText.append("Instructions: ").append(d.get("instructions") != null ? d.get("instructions") : "None");
            otherDetailsArea.setText(detailsText.toString());
            otherDetailsArea.setCaretPosition(0);
        }
    }

    private void determineCurrentStep() {
        if (tradeCompleted) {
            currentStep = 6;
        } else if (refundProcessed) {
            currentStep = 5;
        } else if (myItemReceived && otherItemReceived) {
            currentStep = 4;
        } else if (paymentVerified) {
            currentStep = 3;
        } else if (myDetailsSubmitted && otherDetailsSubmitted) {
            // Check agreement based on exchange method
            boolean canProceed = false;
            
            if (exchangeMethod != null && exchangeMethod.equals("delivery")) {
                // Delivery: Both must agree (my_details_agreed = 1 AND other_details_agreed = 1 for BOTH traders)
                // For current user: my_details_agreed = 1 means other trader agreed to this user's details
                // For other user: need to check their my_details_agreed (which is this user's other_details_agreed)
                canProceed = (myDetailsAgreed == 1 && otherDetailsAgreed == 1);
            } else if (exchangeMethod != null && exchangeMethod.equals("meetup")) {
                // Meetup: Only one agreement needed
                canProceed = (myDetailsAgreed == 1 || otherDetailsAgreed == 1);
            }
            
            if (canProceed) {
                currentStep = 2;
            } else {
                currentStep = 1;
            }
        } else if (exchangeMethod != null && methodConfirmed) {
            currentStep = 1;
        } else {
            currentStep = 1;
        }
        
        stepHistory.push(currentStep);
        
        if (currentStep > 1) {
            stepReminderLabel.setText("Your current step is " + getStepName(currentStep) + ". Click PROCEED to continue, or click BACK TO PREVIOUS STEP to review.");
        } else {
            stepReminderLabel.setText("");
        }
    }
    
    private String getStepName(int step) {
        switch(step) {
            case 1: return "Step 1: Propose Method";
            case 2: return "Step 2: Exchange Details";
            case 3: return "Step 3: Payment";
            case 4: return "Step 4: Item Receipt";
            case 5: return "Step 5: Refund";
            case 6: return "Completed";
            default: return "Step " + step;
        }
    }

    private void updateUI() {
        String[] stepNames = {"", "Step 1: Propose Exchange Method", "Step 2: Enter Exchange Details", 
                               "Step 3: Make Payment", "Step 4: Confirm Receipt", "Step 5: Refund Processing", "Step 6: Trade Completed"};
        stepIndicatorLabel.setText(stepNames[currentStep]);
        
        stepPanel.removeAll();
        stepPanel.setPreferredSize(new Dimension(940, 650));
        
        switch (currentStep) {
            case 1:
                showStep1ProposeMethod();
                break;
            case 2:
                showStep2SetDetails();
                break;
            case 3:
                showStep3Payment();
                break;
            case 4:
                showStep4ConfirmReceipt();
                break;
            case 5:
                showStep5Refund();
                break;
            case 6:
                showCompleted();
                break;
        }
        
        stepPanel.revalidate();
        stepPanel.repaint();
        
        backStepButton.setEnabled(currentStep > 1 && currentStep < 6);
    }

    private void showStep1ProposeMethod() {
        statusLabel.setText("Choose an exchange method. Both traders must agree before proceeding.");
        
        int y = 20;
        
        JPanel methodPanel = new JPanel();
        methodPanel.setLayout(null);
        methodPanel.setBackground(Color.WHITE);
        methodPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor), "Exchange Method Selection"));
        methodPanel.setBounds(20, y, 900, 200);
        stepPanel.add(methodPanel);
        
        JLabel methodLabel = new JLabel("Select your preferred exchange method:");
        methodLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        methodLabel.setBounds(20, 25, 300, 25);
        methodPanel.add(methodLabel);
        
        boolean methodEditable = (currentStep == 1 && exchangeMethod == null) || 
                                  (currentStep == 1 && !methodConfirmed);
        
        deliveryRadio.setEnabled(methodEditable);
        meetupRadio.setEnabled(methodEditable);
        
        deliveryRadio.setBounds(20, 60, 200, 30);
        meetupRadio.setBounds(230, 60, 200, 30);
        methodPanel.add(deliveryRadio);
        methodPanel.add(meetupRadio);
        
        if (exchangeMethod != null) {
            if (exchangeMethod.equals("delivery")) deliveryRadio.setSelected(true);
            else if (exchangeMethod.equals("meetup")) meetupRadio.setSelected(true);
        }
        
        int yPos = 100;
        
        if (proposedMethod != null && proposedBy != traderId && !methodConfirmed && currentStep == 1) {
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
    }

    private void proposeMethod() {
        if (!deliveryRadio.isSelected() && !meetupRadio.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please select an exchange method.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String method = deliveryRadio.isSelected() ? "delivery" : "meetup";
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Propose " + (method.equals("delivery") ? "Delivery" : "Meetup") + " as the exchange method?\n\n"
            + otherTraderName + " will be notified and can either agree or propose a different method.",
            "Propose Method",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_trade SET proposed_method = ?, proposed_by = ?, exchange_method = NULL, method_confirmed = 0 WHERE trade_id = ?";
            db.updateRecord(sql, method, traderId, tradeId);
            
            proposedMethod = method;
            proposedBy = traderId;
            exchangeMethod = null;
            methodConfirmed = false;
            
            JOptionPane.showMessageDialog(this,
                "Method proposed to " + otherTraderName + "!\n\n"
                + "They will review and either agree or propose a different method.",
                "Proposal Sent",
                JOptionPane.INFORMATION_MESSAGE);
            
            loadTradeState();
            updateUI();
        }
    }

    private void acceptProposedMethod() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Accept " + otherTraderName + "'s proposed method: " + 
            (proposedMethod.equals("delivery") ? "Delivery" : "Meetup") + "?\n\n"
            + "Both traders will then proceed to Step 2.",
            "Accept Method",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_trade SET exchange_method = ?, proposed_method = NULL, proposed_by = NULL, method_confirmed = 1 WHERE trade_id = ?";
            db.updateRecord(sql, proposedMethod, tradeId);
            
            exchangeMethod = proposedMethod;
            methodConfirmed = true;
            
            JOptionPane.showMessageDialog(this,
                "Method accepted!\n\n"
                + "Exchange method confirmed: " + (exchangeMethod.equals("delivery") ? "Delivery" : "Meetup") + "\n"
                + "You can now proceed to Step 2.",
                "Method Accepted",
                JOptionPane.INFORMATION_MESSAGE);
            
            loadTradeState();
            updateUI();
        }
    }

    private void rejectProposedMethod() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Reject " + otherTraderName + "'s proposed method?\n\n"
            + "You will be able to propose your own method instead.",
            "Reject Method",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_trade SET proposed_method = NULL, proposed_by = NULL WHERE trade_id = ?";
            db.updateRecord(sql, tradeId);
            
            proposedMethod = null;
            proposedBy = -1;
            
            JOptionPane.showMessageDialog(this,
                "You rejected the proposal.\n\n"
                + "You can now propose your own exchange method.",
                "Proposal Rejected",
                JOptionPane.INFORMATION_MESSAGE);
            
            loadTradeState();
            updateUI();
        }
    }

    private void showStep2SetDetails() {
        statusLabel.setText("Enter your exchange details. Both traders must submit and agree to proceed.");
        
        int panelWidth = 440;
        int panelHeight = 580;
        int leftX = 20;
        int rightX = 480;
        int y = 20;
        
        myDetailsPanel.setBounds(leftX, y, panelWidth, panelHeight);
        otherDetailsPanel.setBounds(rightX, y, panelWidth, panelHeight);
        
        myDetailsPanel.removeAll();
        otherDetailsPanel.removeAll();
        
        myDetailsPanel.setLayout(null);
        otherDetailsPanel.setLayout(null);
        
        buildMyDetailsPanel();
        buildOtherDetailsPanel();
        
        stepPanel.add(myDetailsPanel);
        stepPanel.add(otherDetailsPanel);
        
        JLabel agreementStatus = new JLabel();
        agreementStatus.setFont(new Font("Segoe UI", Font.BOLD, 13));
        agreementStatus.setBounds(20, panelHeight + 30, 900, 30);
        
        if (myDetailsSubmitted && otherDetailsSubmitted) {
            boolean canProceed = false;
            String agreementMessage = "";
            
            if (exchangeMethod != null && exchangeMethod.equals("delivery")) {
                // Delivery: Both must agree
                canProceed = (myDetailsAgreed == 1 && otherDetailsAgreed == 1);
                if (myDetailsAgreed == 1 && otherDetailsAgreed == 1) {
                    agreementMessage = "Both traders have agreed to the details! Click PROCEED to continue.";
                    agreementStatus.setForeground(successColor);
                } else if (myDetailsAgreed == 1) {
                    agreementMessage = "You have agreed. Waiting for " + otherTraderName + " to agree.";
                    agreementStatus.setForeground(warningColor);
                } else if (otherDetailsAgreed == 1) {
                    agreementMessage = otherTraderName + " has agreed. Click AGREE on your side to confirm.";
                    agreementStatus.setForeground(warningColor);
                } else {
                    agreementMessage = "Both traders have submitted details. Click AGREE on your side to confirm.";
                    agreementStatus.setForeground(warningColor);
                }
            } else {
                // Meetup: Only one needs to agree
                canProceed = (myDetailsAgreed == 1 || otherDetailsAgreed == 1);
                if (myDetailsAgreed == 1) {
                    agreementMessage = "You have agreed to " + otherTraderName + "'s details! Click PROCEED to continue.";
                    agreementStatus.setForeground(successColor);
                } else if (otherDetailsAgreed == 1) {
                    agreementMessage = otherTraderName + " has agreed to your details! Click PROCEED to continue.";
                    agreementStatus.setForeground(successColor);
                } else {
                    agreementMessage = "Both traders have submitted details. Click AGREE to confirm the other trader's details.";
                    agreementStatus.setForeground(warningColor);
                }
            }
            
            agreementStatus.setText(agreementMessage);
            proceedButton.setEnabled(canProceed);
            
            if (canProceed) {
                proceedButton.setText("PROCEED TO PAYMENT");
            }
            
        } else if (myDetailsSubmitted) {
            agreementStatus.setText("You have submitted your details. Waiting for " + otherTraderName + " to submit.");
            agreementStatus.setForeground(warningColor);
            proceedButton.setEnabled(false);
        } else if (otherDetailsSubmitted) {
            agreementStatus.setText(otherTraderName + " has submitted details. Please submit your details and then agree.");
            agreementStatus.setForeground(warningColor);
            proceedButton.setEnabled(false);
        } else {
            agreementStatus.setText("Please fill in your details and click SUBMIT MY DETAILS.");
            agreementStatus.setForeground(textColor);
            proceedButton.setEnabled(false);
        }
        
        stepPanel.add(agreementStatus);
    }

    private void buildMyDetailsPanel() {
        int fieldY = 30;
        
        JLabel instructionLabel = new JLabel("Click 'Submit My Details' to enter your information:");
        instructionLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        instructionLabel.setForeground(textColor);
        instructionLabel.setBounds(10, fieldY, 400, 20);
        myDetailsPanel.add(instructionLabel);
        fieldY += 30;
        
        submitDetailsButton.setBounds(120, fieldY, 200, 35);
        submitDetailsButton.addActionListener(e -> showDetailsInputDialog());
        myDetailsPanel.add(submitDetailsButton);
        fieldY += 50;
        
        if (myDetailsSubmitted) {
            myDetailsStatus.setText("Your details have been submitted");
            myDetailsStatus.setForeground(successColor);
            myDetailsStatus.setBounds(120, fieldY, 250, 30);
            myDetailsPanel.add(myDetailsStatus);
            fieldY += 45;
            
            // Display your own submitted details
            if (exchangeMethod != null && exchangeMethod.equals("meetup")) {
                JLabel previewLabel = new JLabel("Your submitted details:");
                previewLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
                previewLabel.setForeground(accentColor);
                previewLabel.setBounds(10, fieldY, 200, 20);
                myDetailsPanel.add(previewLabel);
                fieldY += 25;
                
                JTextArea previewArea = new JTextArea();
                previewArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                previewArea.setEditable(false);
                previewArea.setBackground(new Color(245, 245, 245));
                previewArea.setText(buildMyMeetupPreview());
                previewArea.setLineWrap(true);
                previewArea.setWrapStyleWord(true);
                JScrollPane previewScroll = new JScrollPane(previewArea);
                previewScroll.setBounds(10, fieldY, 420, 120);
                previewScroll.setBorder(new LineBorder(new Color(200, 200, 200)));
                myDetailsPanel.add(previewScroll);
                fieldY += 130;
            } else if (exchangeMethod != null && exchangeMethod.equals("delivery")) {
                JLabel previewLabel = new JLabel("Your submitted details:");
                previewLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
                previewLabel.setForeground(accentColor);
                previewLabel.setBounds(10, fieldY, 200, 20);
                myDetailsPanel.add(previewLabel);
                fieldY += 25;
                
                JTextArea previewArea = new JTextArea();
                previewArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                previewArea.setEditable(false);
                previewArea.setBackground(new Color(245, 245, 245));
                previewArea.setText(buildMyDeliveryPreview());
                previewArea.setLineWrap(true);
                previewArea.setWrapStyleWord(true);
                JScrollPane previewScroll = new JScrollPane(previewArea);
                previewScroll.setBounds(10, fieldY, 420, 120);
                previewScroll.setBorder(new LineBorder(new Color(200, 200, 200)));
                myDetailsPanel.add(previewScroll);
                fieldY += 130;
            }
        }
        
        if (myDetailsAgreed == 1) {
            myAgreementStatus.setText("✓ You have agreed to the other trader's details");
            myAgreementStatus.setForeground(successColor);
            myAgreementStatus.setBounds(80, fieldY + 45, 300, 30);
            myDetailsPanel.add(myAgreementStatus);
        }
        
        JLabel statusTitle = new JLabel("Status:");
        statusTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        statusTitle.setBounds(10, fieldY + 10, 50, 20);
        myDetailsPanel.add(statusTitle);
    }
    
    private String buildMyMeetupPreview() {
        StringBuilder preview = new StringBuilder();
        if (myMeetupId != -1) {
            String sql = "SELECT * FROM tbl_meetup_details WHERE meetup_id = ?";
            List<Map<String, Object>> details = db.fetchRecords(sql, myMeetupId);
            if (!details.isEmpty()) {
                Map<String, Object> d = details.get(0);
                preview.append("Location: ").append(d.get("location") != null ? d.get("location") : "Not provided").append("\n");
                if (d.get("google_maps_link") != null && !d.get("google_maps_link").toString().isEmpty()) {
                    preview.append("Google Maps: ").append(d.get("google_maps_link")).append("\n");
                }
                preview.append("Date: ").append(d.get("date") != null ? d.get("date") : "Not provided").append("\n");
                preview.append("Time: ").append(d.get("time") != null ? d.get("time") : "Not provided").append("\n");
                preview.append("Contact Person: ").append(d.get("contact_person") != null ? d.get("contact_person") : "Not provided").append("\n");
                preview.append("Contact Number: ").append(d.get("contact_number") != null ? d.get("contact_number") : "Not provided").append("\n");
                if (d.get("instructions") != null && !d.get("instructions").toString().isEmpty()) {
                    preview.append("Instructions: ").append(d.get("instructions"));
                }
            }
        } else {
            preview.append("No details submitted yet.");
        }
        return preview.toString();
    }
    
    private String buildMyDeliveryPreview() {
        StringBuilder preview = new StringBuilder();
        if (myDeliveryId != -1) {
            String sql = "SELECT * FROM tbl_delivery_details WHERE delivery_id = ?";
            List<Map<String, Object>> details = db.fetchRecords(sql, myDeliveryId);
            if (!details.isEmpty()) {
                Map<String, Object> d = details.get(0);
                preview.append("Address: ").append(d.get("address") != null ? d.get("address") : "Not provided").append("\n");
                preview.append("Courier: ").append(d.get("courier") != null ? d.get("courier") : "Not provided").append("\n");
                preview.append("Expected Date: ").append(d.get("expected_date") != null ? d.get("expected_date") : "Not provided").append("\n");
                if (d.get("tracking_number") != null && !d.get("tracking_number").toString().isEmpty()) {
                    preview.append("Tracking Number: ").append(d.get("tracking_number")).append("\n");
                }
                if (d.get("instructions") != null && !d.get("instructions").toString().isEmpty()) {
                    preview.append("Instructions: ").append(d.get("instructions"));
                }
            }
        } else {
            preview.append("No details submitted yet.");
        }
        return preview.toString();
    }
    
    private void buildOtherDetailsPanel() {
        int fieldY = 30;
        
        JLabel instructionLabel = new JLabel(otherTraderName + "'s details will appear here when submitted:");
        instructionLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        instructionLabel.setForeground(textColor);
        instructionLabel.setBounds(10, fieldY, 400, 20);
        otherDetailsPanel.add(instructionLabel);
        fieldY += 30;
        
        otherDetailsScroll.setBounds(10, fieldY, 420, 200);
        otherDetailsPanel.add(otherDetailsScroll);
        fieldY += 220;
        
        JLabel statusTitle = new JLabel("Status:");
        statusTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        statusTitle.setBounds(10, fieldY, 50, 25);
        otherDetailsPanel.add(statusTitle);
        fieldY += 25;
        
        if (otherDetailsSubmitted) {
            otherDetailsStatus.setText("Details submitted");
            otherDetailsStatus.setForeground(successColor);
            otherDetailsStatus.setBounds(10, fieldY, 200, 25);
            otherDetailsPanel.add(otherDetailsStatus);
            fieldY += 30;
        } else {
            otherDetailsStatus.setText("Waiting for submission...");
            otherDetailsStatus.setForeground(warningColor);
            otherDetailsStatus.setBounds(10, fieldY, 200, 25);
            otherDetailsPanel.add(otherDetailsStatus);
            fieldY += 30;
        }
        
        // Show Agree button based on exchange method
        boolean showAgreeButton = false;
        
        if (exchangeMethod != null && exchangeMethod.equals("delivery")) {
            // Delivery: Show agree button if other has submitted AND current hasn't agreed yet (otherDetailsAgreed = 0)
            showAgreeButton = (otherDetailsSubmitted && otherDetailsAgreed == 0);
        } else if (exchangeMethod != null && exchangeMethod.equals("meetup")) {
            // Meetup: Show agree button if other has submitted AND no one has agreed yet (both myDetailsAgreed and otherDetailsAgreed are 0)
            showAgreeButton = (otherDetailsSubmitted && myDetailsAgreed == 0 && otherDetailsAgreed == 0);
        }
        
        if (showAgreeButton) {
            String buttonText = exchangeMethod.equals("meetup") ? 
                "AGREE TO USE THEIR MEETUP DETAILS" : 
                "AGREE TO DETAILS";
            agreeToOtherDetailsButton.setText(buttonText);
            agreeToOtherDetailsButton.setBounds(80, fieldY, 250, 35);
            agreeToOtherDetailsButton.addActionListener(e -> agreeToOtherDetails());
            otherDetailsPanel.add(agreeToOtherDetailsButton);
            fieldY += 50;
        } else if ((exchangeMethod != null && exchangeMethod.equals("delivery") && otherDetailsAgreed == 1) ||
                   (exchangeMethod != null && exchangeMethod.equals("meetup") && (myDetailsAgreed == 1 || otherDetailsAgreed == 1))) {
            if (exchangeMethod.equals("delivery")) {
                otherAgreementStatus.setText("✓ " + otherTraderName + " has agreed to your details");
            } else {
                otherAgreementStatus.setText("✓ Agreement has been reached on meetup details");
            }
            otherAgreementStatus.setForeground(successColor);
            otherAgreementStatus.setBounds(80, fieldY, 300, 25);
            otherDetailsPanel.add(otherAgreementStatus);
        } else if (otherDetailsSubmitted) {
            JLabel waitLabel = new JLabel(exchangeMethod.equals("meetup") ? 
                "Waiting for either trader to agree..." : 
                "Waiting for " + otherTraderName + " to agree...");
            waitLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            waitLabel.setForeground(warningColor);
            waitLabel.setBounds(80, fieldY, 300, 25);
            otherDetailsPanel.add(waitLabel);
        }
    }

    private void showDetailsInputDialog() {
        if (exchangeMethod == null) {
            JOptionPane.showMessageDialog(this, "Exchange method not set. Please complete Step 1 first.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        step2Handler = new step2_submit(tradeId, traderId, otherTraderId, exchangeMethod, this);
        step2Handler.showDialog();
        
        // Refresh all data after dialog closes
        loadTradeState();
        updateUI();
    }

    private void agreeToOtherDetails() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Confirm that you agree with " + otherTraderName + "'s exchange details?\n\n"
            + "This means you have reviewed and confirmed:\n"
            + "Their exchange details are acceptable\n\n"
            + (exchangeMethod.equals("delivery") ? 
                "Both traders must agree to proceed to payment." : 
                "Once you agree, the trade will proceed to payment using their meetup details."),
            "Confirm Agreement",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Update other_details_agreed = 1 for the current user's row (agreeing to other trader's details)
            String sql = "UPDATE tbl_trade_details SET other_details_agreed = 1 WHERE trade_id = ? AND trader_id = ?";
            db.updateRecord(sql, tradeId, traderId);
            
            // Also update the other trader's row to set my_details_agreed = 1 (someone agreed to their details)
            String sqlOther = "UPDATE tbl_trade_details SET my_details_agreed = 1 WHERE trade_id = ? AND trader_id = ?";
            db.updateRecord(sqlOther, tradeId, otherTraderId);
            
            // Refresh data
            loadTradeState();
            
            if (exchangeMethod.equals("delivery")) {
                // Delivery: Need both agreements - check if both are now agreed
                String checkSql = "SELECT my_details_agreed, other_details_agreed FROM tbl_trade_details WHERE trade_id = ? AND trader_id = ?";
                List<Map<String, Object>> myResult = db.fetchRecords(checkSql, tradeId, traderId);
                List<Map<String, Object>> otherResult = db.fetchRecords(checkSql, tradeId, otherTraderId);
                
                if (!myResult.isEmpty() && !otherResult.isEmpty()) {
                    int myMyAgreed = Integer.parseInt(myResult.get(0).get("my_details_agreed").toString());
                    int myOtherAgreed = Integer.parseInt(myResult.get(0).get("other_details_agreed").toString());
                    int otherMyAgreed = Integer.parseInt(otherResult.get(0).get("my_details_agreed").toString());
                    int otherOtherAgreed = Integer.parseInt(otherResult.get(0).get("other_details_agreed").toString());
                    
                    // Both traders have agreed when: 
                    // my_agreed = 1 AND other_agreed = 1 for BOTH traders
                    if (myMyAgreed == 1 && myOtherAgreed == 1 && otherMyAgreed == 1 && otherOtherAgreed == 1) {
                        String updateTradeSql = "UPDATE tbl_trade SET trade_status = 'arrangements_confirmed' WHERE trade_id = ?";
                        db.updateRecord(updateTradeSql, tradeId);
                        
                        JOptionPane.showMessageDialog(this,
                            "Both traders have agreed! You can now proceed to payment.",
                            "Agreement Complete",
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "You have agreed to " + otherTraderName + "'s details.\n\n"
                            + "Waiting for " + otherTraderName + " to agree as well.",
                            "Agreement Recorded",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } else {
                // Meetup: Only one agreement needed - proceed immediately
                String updateTradeSql = "UPDATE tbl_trade SET trade_status = 'arrangements_confirmed' WHERE trade_id = ?";
                db.updateRecord(updateTradeSql, tradeId);
                
                JOptionPane.showMessageDialog(this,
                    "You have agreed to " + otherTraderName + "'s meetup details!\n\n"
                    + "The trade will now proceed to payment using their meetup information.",
                    "Agreement Complete - Proceeding to Payment",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
            loadTradeState();
            updateUI();
        }
    }

    private void showStep3Payment() {
        statusLabel.setText("Step 3: Select admin and make payment.");
        
        step3Handler = new step3_submit(tradeId, traderId, this);
        JPanel paymentPanel = step3Handler.createPaymentPanel();
        paymentPanel.setBounds(20, 15, 900, 350);
        stepPanel.add(paymentPanel);
        
        JTextArea instructionArea = new JTextArea();
        instructionArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        instructionArea.setForeground(textColor);
        instructionArea.setBackground(new Color(255, 255, 200));
        instructionArea.setLineWrap(true);
        instructionArea.setWrapStyleWord(true);
        instructionArea.setEditable(false);
        instructionArea.setText("Instructions:\n1. Select an admin as middleman\n2. Send the total amount to the admin's account\n3. Upload payment proof (screenshot/QR)\n4. Wait for admin verification");
        instructionArea.setBounds(20, 380, 900, 80);
        instructionArea.setBorder(new LineBorder(warningColor, 1));
        stepPanel.add(instructionArea);
        
        paymentStatusLabel.setBounds(20, 475, 500, 25);
        
        paymentSubmitted = step3Handler.isPaymentSubmitted();
        paymentVerified = step3Handler.isPaymentVerified();
        
        if (paymentVerified) {
            paymentStatusLabel.setText("✓ Payment verified by admin! You can proceed.");
            paymentStatusLabel.setForeground(successColor);
            proceedButton.setEnabled(true);
            proceedButton.setText("PROCEED TO NEXT STEP");
        } else if (paymentSubmitted) {
            paymentStatusLabel.setText("Payment submitted. Waiting for admin verification...");
            paymentStatusLabel.setForeground(warningColor);
            proceedButton.setEnabled(false);
        } else if (step3Handler.hasSelectedAdmin()) {
            paymentStatusLabel.setText("Please provide payment details and upload proof.");
            paymentStatusLabel.setForeground(textColor);
            proceedButton.setEnabled(true);
            proceedButton.setText("SUBMIT PAYMENT");
        } else {
            paymentStatusLabel.setText("Please select an admin first.");
            paymentStatusLabel.setForeground(textColor);
            proceedButton.setEnabled(false);
        }
        
        stepPanel.add(paymentStatusLabel);
    }

    private void showStep4ConfirmReceipt() {
        statusLabel.setText("Step 4: Confirm when you receive the item.");
        
        int y = 50;
        
        JPanel receiptPanel = new JPanel();
        receiptPanel.setLayout(null);
        receiptPanel.setBackground(Color.WHITE);
        receiptPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor), "Item Receipt Confirmation"));
        receiptPanel.setBounds(20, y, 900, 200);
        stepPanel.add(receiptPanel);
        
        JLabel receiptLabel = new JLabel("Have you received the item?");
        receiptLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        receiptLabel.setForeground(accentColor);
        receiptLabel.setBounds(20, 30, 300, 25);
        receiptPanel.add(receiptLabel);
        
        confirmReceivedCheck.setBounds(20, 65, 300, 30);
        receiptPanel.add(confirmReceivedCheck);
        
        JButton confirmButton = new JButton("CONFIRM RECEIPT");
        confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        confirmButton.setBackground(successColor);
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setBounds(20, 105, 150, 35);
        confirmButton.setBorder(null);
        confirmButton.setFocusPainted(false);
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmButton.addActionListener(e -> confirmReceipt());
        receiptPanel.add(confirmButton);
        
        JLabel receiptStatus = new JLabel();
        receiptStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        receiptStatus.setBounds(200, 115, 400, 20);
        
        if (myItemReceived) {
            receiptStatus.setText("You have confirmed receipt. Waiting for " + otherTraderName + ".");
            receiptStatus.setForeground(successColor);
            proceedButton.setEnabled(false);
        } else {
            receiptStatus.setText("Please confirm once you have received the item.");
            receiptStatus.setForeground(textColor);
            proceedButton.setEnabled(false);
        }
        
        receiptPanel.add(receiptStatus);
        
        JLabel bothStatus = new JLabel();
        bothStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        bothStatus.setBounds(20, 150, 500, 25);
        
        if (myItemReceived && otherItemReceived) {
            bothStatus.setText("Both traders have received items! Click PROCEED to continue.");
            bothStatus.setForeground(successColor);
            proceedButton.setEnabled(true);
            proceedButton.setText("PROCEED TO REFUND");
        } else if (myItemReceived) {
            bothStatus.setText("You have received the item. Waiting for " + otherTraderName + " to confirm.");
            bothStatus.setForeground(warningColor);
        } else if (otherItemReceived) {
            bothStatus.setText(otherTraderName + " has received the item. Waiting for you to confirm.");
            bothStatus.setForeground(warningColor);
        } else {
            bothStatus.setText("Waiting for both traders to confirm receipt.");
            bothStatus.setForeground(textColor);
        }
        
        receiptPanel.add(bothStatus);
    }

    private void showStep5Refund() {
        statusLabel.setText("Step 5: Admin will process refund.");
        
        int y = 50;
        
        JPanel refundPanel = new JPanel();
        refundPanel.setLayout(null);
        refundPanel.setBackground(Color.WHITE);
        refundPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor), "Refund Processing"));
        refundPanel.setBounds(20, y, 900, 250);
        stepPanel.add(refundPanel);
        
        JLabel refundLabel = new JLabel("REFUND PROCESSING");
        refundLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        refundLabel.setForeground(accentColor);
        refundLabel.setBounds(20, 20, 300, 25);
        refundPanel.add(refundLabel);
        
        JLabel refundInfo = new JLabel(
            "<html>Both traders have confirmed receipt.<br>"
            + "The base amount of ₱200.00 will be refunded to both parties.<br>"
            + "The admin fee of ₱15.00 is retained by BarterZone.<br><br>"
            + "Admin will send proof of transaction to both traders.</html>");
        refundInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        refundInfo.setBounds(20, 55, 600, 80);
        refundPanel.add(refundInfo);
        
        JLabel refundStatus = new JLabel();
        refundStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refundStatus.setBounds(20, 150, 400, 25);
        
        if (refundProcessed) {
            refundStatus.setText("✓ Refund has been processed by admin!");
            refundStatus.setForeground(successColor);
            proceedButton.setEnabled(true);
            proceedButton.setText("COMPLETE TRADE");
        } else {
            refundStatus.setText("⏳ Waiting for admin to process refund...");
            refundStatus.setForeground(warningColor);
            proceedButton.setEnabled(false);
        }
        
        refundPanel.add(refundStatus);
    }

    private void showCompleted() {
        statusLabel.setText("Trade Completed Successfully!");
        
        int y = 100;
        
        JPanel completePanel = new JPanel();
        completePanel.setLayout(null);
        completePanel.setBackground(Color.WHITE);
        completePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(successColor), "Trade Complete"));
        completePanel.setBounds(20, y, 900, 250);
        stepPanel.add(completePanel);
        
        JLabel completedLabel = new JLabel(
            "<html><h2 style='color:#2E7D32;'>TRADE COMPLETED</h2>"
            + "<p>This trade has been successfully completed.</p>"
            + "<p>Thank you for using BarterZone!</p></html>");
        completedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        completedLabel.setBounds(20, 30, 600, 120);
        completePanel.add(completedLabel);
        
        JButton closeButton = new JButton("CLOSE");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeButton.setBackground(themeColor);
        closeButton.setForeground(Color.WHITE);
        closeButton.setBounds(320, 160, 150, 40);
        closeButton.setBorder(null);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> goBackToTrades());
        completePanel.add(closeButton);
        
        proceedButton.setEnabled(false);
        backStepButton.setEnabled(false);
        cancelTradeButton.setEnabled(false);
    }

    private void confirmReceipt() {
        if (!confirmReceivedCheck.isSelected()) {
            JOptionPane.showMessageDialog(this,
                "Please check the box to confirm you have received the item.",
                "Confirmation Required",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Confirm that you have received the item?\n\n"
            + "This cannot be undone.",
            "Confirm Receipt",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_trade SET my_item_received = 1 WHERE trade_id = ?";
            db.updateRecord(sql, tradeId);
            
            myItemReceived = true;
            
            JOptionPane.showMessageDialog(this,
                "Receipt confirmed! Waiting for " + otherTraderName + ".",
                "Confirmation Recorded",
                JOptionPane.INFORMATION_MESSAGE);
            
            loadTradeState();
            updateUI();
        }
    }

    private void handleProceed() {
        switch (currentStep) {
            case 1:
                if (proceedButton.getText().equals("PROCEED TO NEXT STEP")) {
                    proceedToNext();
                }
                break;
            case 2:
                if (proceedButton.getText().equals("PROCEED TO PAYMENT")) {
                    proceedToNext();
                }
                break;
            case 3:
                if (proceedButton.getText().equals("SUBMIT PAYMENT")) {
                    if (step3Handler != null && step3Handler.submitPayment()) {
                        loadTradeState();
                        updateUI();
                    }
                } else if (proceedButton.getText().equals("PROCEED TO NEXT STEP")) {
                    proceedToNext();
                }
                break;
            case 4:
                if (proceedButton.getText().equals("PROCEED TO REFUND")) {
                    proceedToNext();
                }
                break;
            case 5:
                if (proceedButton.getText().equals("COMPLETE TRADE")) {
                    completeTrade();
                }
                break;
        }
    }
    
    private void proceedToNext() {
        if (currentStep < 6) {
            currentStep++;
            updateUI();
        }
    }
    
    private void completeTrade() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Complete this trade?\n\n"
            + "Both traders have received items\n"
            + "Refund has been processed\n"
            + "This action cannot be undone",
            "Complete Trade",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String getSql = "SELECT * FROM tbl_trade WHERE trade_id = ?";
            List<Map<String, Object>> trade = db.fetchRecords(getSql, tradeId);
            
            if (!trade.isEmpty()) {
                Map<String, Object> t = trade.get(0);
                
                String historySql = "INSERT INTO tbl_trade_history "
                    + "(trade_id, offer_trader_id, target_trader_id, offer_item_id, "
                    + "target_item_id, trade_status, trade_DateRequest, trade_DateCompleted) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, datetime('now'))";
                
                db.addRecord(historySql,
                    tradeId,
                    t.get("offer_trader_id"),
                    t.get("target_trader_id"),
                    t.get("offer_item_id"),
                    t.get("target_item_id"),
                    "completed",
                    t.get("trade_DateRequest"));
                
                String deleteSql = "DELETE FROM tbl_trade WHERE trade_id = ?";
                db.deleteRecord(deleteSql, tradeId);
                
                JOptionPane.showMessageDialog(this,
                    "TRADE COMPLETED SUCCESSFULLY!\n\n"
                    + "Thank you for using BarterZone.",
                    "Trade Complete",
                    JOptionPane.INFORMATION_MESSAGE);
                
                goBackToTrades();
            }
        }
    }
    
    private void goBackStep() {
        if (currentStep > 1) {
            currentStep--;
            stepHistory.push(currentStep);
            updateUI();
            JOptionPane.showMessageDialog(this,
                "You are now reviewing " + getStepName(currentStep) + ".\n"
                + "You can make changes if needed, then click PROCEED to continue.",
                "Review Mode",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void cancelTrade() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to cancel this trade?\n\n"
            + "This action cannot be undone.",
            "Cancel Trade",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM tbl_trade WHERE trade_id = ?";
            db.deleteRecord(sql, tradeId);
            
            JOptionPane.showMessageDialog(this, "Trade cancelled.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
            goBackToTrades();
        }
    }
    
    private void goBackToTrades() {
        trades tradesFrame = new trades(traderId, traderName);
        tradesFrame.setVisible(true);
        tradesFrame.setLocationRelativeTo(null);
        this.dispose();
    }
}