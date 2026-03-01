package BarterZone.Dashboard.trader;

import BarterZone.Dashboard.session.user_session;
import database.config.config;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.JScrollPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import javax.swing.ButtonGroup;

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
    
    // Trade state variables
    private int currentStep = 1;
    private String exchangeMethod = null;
    private boolean methodAgreed = false;
    private boolean myMethodConfirmed = false;
    private boolean otherMethodConfirmed = false;
    private boolean myDetailsSubmitted = false;
    private boolean otherDetailsSubmitted = false;
    private boolean detailsAgreed = false;
    private boolean feePayer = false; // true if this user pays the fee
    private double baseAmount = 0;
    private double feeAmount = 0;
    private double totalAmount = 0;
    private boolean myPaymentSubmitted = false;
    private boolean otherPaymentSubmitted = false;
    private boolean paymentVerified = false;
    private boolean myItemReceived = false;
    private boolean otherItemReceived = false;
    private boolean refundProcessed = false;
    private boolean tradeCompleted = false;
    
    // UI Components
    private javax.swing.JLabel stepIndicatorLabel;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JLabel tradeInfoLabel;
    private javax.swing.JPanel stepPanel;
    private javax.swing.JPanel navigationPanel;
    private javax.swing.JButton proceedButton;
    private javax.swing.JButton backButton;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton cancelTradeButton;
    
    // Method selection
    private javax.swing.JRadioButton deliveryRadio;
    private javax.swing.JRadioButton meetupRadio;
    private ButtonGroup methodGroup;
    private javax.swing.JLabel methodStatusLabel;
    
    // Delivery fields
    private javax.swing.JTextField deliveryAddressField;
    private javax.swing.JTextField courierField;
    private javax.swing.JTextField expectedDateField;
    private javax.swing.JTextField trackingField;
    private javax.swing.JTextArea deliveryInstructionsArea;
    private JScrollPane deliveryInstructionsScroll;
    
    // Meetup fields
    private javax.swing.JTextField meetupLocationField;
    private javax.swing.JTextField meetupDateField;
    private javax.swing.JTextField meetupTimeField;
    private javax.swing.JTextField contactPersonField;
    private javax.swing.JTextField contactNumberField;
    private javax.swing.JTextArea meetupInstructionsArea;
    private JScrollPane meetupInstructionsScroll;
    
    // Display areas
    private javax.swing.JTextArea otherDetailsArea;
    private JScrollPane otherDetailsScroll;
    
    // Payment fields
    private javax.swing.JLabel baseAmountLabel;
    private javax.swing.JLabel feeAmountLabel;
    private javax.swing.JLabel totalAmountLabel;
    private javax.swing.JLabel feePayerLabel;
    private javax.swing.JLabel paymentStatusLabel;
    private javax.swing.JButton uploadScreenshotButton;
    private javax.swing.JLabel screenshotFileNameLabel;
    private String uploadedScreenshotPath = "";
    
    // Receipt confirmation
    private javax.swing.JCheckBox confirmReceivedCheck;
    
    // History array for back navigation
    private java.util.Stack<Integer> stepHistory = new java.util.Stack<>();

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
        
        initComponents();
        loadTradeState();
        updateUI();
        
        setTitle("Manage Trade - Trade #" + tradeId);
        setSize(750, 650);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(12, 192, 223));
        titlePanel.setBounds(0, 0, 750, 70);
        titlePanel.setLayout(null);

        javax.swing.JLabel titleLabel = new javax.swing.JLabel("MANAGE TRADE");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(20, 20, 300, 30);
        titlePanel.add(titleLabel);

        javax.swing.JLabel tradeIdLabel = new javax.swing.JLabel("Trade #" + tradeId);
        tradeIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tradeIdLabel.setForeground(Color.WHITE);
        tradeIdLabel.setBounds(600, 25, 150, 25);
        titlePanel.add(tradeIdLabel);

        getContentPane().add(titlePanel);

        // Trade Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(null);
        infoPanel.setBackground(new Color(245, 245, 245));
        infoPanel.setBorder(new LineBorder(new Color(12, 192, 223), 1));
        infoPanel.setBounds(20, 80, 710, 80);

        tradeInfoLabel = new javax.swing.JLabel();
        tradeInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tradeInfoLabel.setBounds(10, 10, 690, 60);
        tradeInfoLabel.setText("<html>"
                + "<b>Your Item:</b> " + myItem + "<br>"
                + "<b>Their Item:</b> " + theirItem + "<br>"
                + "<b>Trading with:</b> " + otherTraderName
                + "</html>");
        infoPanel.add(tradeInfoLabel);

        getContentPane().add(infoPanel);

        // Step Indicator
        stepIndicatorLabel = new javax.swing.JLabel();
        stepIndicatorLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        stepIndicatorLabel.setForeground(new Color(0, 102, 102));
        stepIndicatorLabel.setBounds(20, 170, 710, 30);
        getContentPane().add(stepIndicatorLabel);

        // Status Label
        statusLabel = new javax.swing.JLabel();
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(102, 102, 102));
        statusLabel.setBounds(20, 200, 710, 25);
        getContentPane().add(statusLabel);

        // Main Step Panel
        stepPanel = new JPanel();
        stepPanel.setLayout(null);
        stepPanel.setBackground(new Color(250, 250, 250));
        stepPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        stepPanel.setBounds(20, 230, 710, 320);
        getContentPane().add(stepPanel);

        // Navigation Panel
        navigationPanel = new JPanel();
        navigationPanel.setLayout(null);
        navigationPanel.setBackground(new Color(240, 240, 240));
        navigationPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        navigationPanel.setBounds(20, 560, 710, 50);
        getContentPane().add(navigationPanel);

        proceedButton = new javax.swing.JButton("PROCEED");
        proceedButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        proceedButton.setBackground(new Color(0, 102, 102));
        proceedButton.setForeground(Color.WHITE);
        proceedButton.setBounds(250, 10, 120, 30);
        proceedButton.setBorder(null);
        proceedButton.setFocusPainted(false);
        proceedButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        proceedButton.addActionListener(e -> handleProceed());
        navigationPanel.add(proceedButton);

        backButton = new javax.swing.JButton("BACK");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setBackground(new Color(102, 102, 102));
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(380, 10, 100, 30);
        backButton.setBorder(null);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> goBack());
        backButton.setEnabled(false);
        navigationPanel.add(backButton);

        refreshButton = new javax.swing.JButton("REFRESH");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        refreshButton.setBackground(new Color(12, 192, 223));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBounds(490, 10, 100, 30);
        refreshButton.setBorder(null);
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> {
            loadTradeState();
            updateUI();
        });
        navigationPanel.add(refreshButton);

        cancelTradeButton = new javax.swing.JButton("CANCEL TRADE");
        cancelTradeButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cancelTradeButton.setBackground(new Color(204, 0, 0));
        cancelTradeButton.setForeground(Color.WHITE);
        cancelTradeButton.setBounds(600, 10, 100, 30);
        cancelTradeButton.setBorder(null);
        cancelTradeButton.setFocusPainted(false);
        cancelTradeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelTradeButton.addActionListener(e -> cancelTrade());
        navigationPanel.add(cancelTradeButton);

        // Initialize method selection
        methodGroup = new ButtonGroup();
        deliveryRadio = new javax.swing.JRadioButton("Delivery (Ship item)");
        deliveryRadio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        deliveryRadio.setBackground(new Color(250, 250, 250));
        
        meetupRadio = new javax.swing.JRadioButton("Meetup (In-person)");
        meetupRadio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        meetupRadio.setBackground(new Color(250, 250, 250));
        
        methodGroup.add(deliveryRadio);
        methodGroup.add(meetupRadio);

        methodStatusLabel = new javax.swing.JLabel();
        methodStatusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));

        // Initialize delivery fields
        deliveryAddressField = new javax.swing.JTextField();
        deliveryAddressField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        deliveryAddressField.setBorder(new LineBorder(new Color(200, 200, 200)));
        
        courierField = new javax.swing.JTextField();
        courierField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        courierField.setBorder(new LineBorder(new Color(200, 200, 200)));
        
        expectedDateField = new javax.swing.JTextField();
        expectedDateField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        expectedDateField.setBorder(new LineBorder(new Color(200, 200, 200)));
        
        trackingField = new javax.swing.JTextField();
        trackingField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trackingField.setBorder(new LineBorder(new Color(200, 200, 200)));
        
        deliveryInstructionsArea = new javax.swing.JTextArea();
        deliveryInstructionsArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        deliveryInstructionsArea.setLineWrap(true);
        deliveryInstructionsArea.setWrapStyleWord(true);
        deliveryInstructionsScroll = new JScrollPane(deliveryInstructionsArea);
        deliveryInstructionsScroll.setBorder(new LineBorder(new Color(200, 200, 200)));

        // Initialize meetup fields
        meetupLocationField = new javax.swing.JTextField();
        meetupLocationField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        meetupLocationField.setBorder(new LineBorder(new Color(200, 200, 200)));
        
        meetupDateField = new javax.swing.JTextField();
        meetupDateField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        meetupDateField.setBorder(new LineBorder(new Color(200, 200, 200)));
        
        meetupTimeField = new javax.swing.JTextField();
        meetupTimeField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        meetupTimeField.setBorder(new LineBorder(new Color(200, 200, 200)));
        
        contactPersonField = new javax.swing.JTextField();
        contactPersonField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        contactPersonField.setBorder(new LineBorder(new Color(200, 200, 200)));
        
        contactNumberField = new javax.swing.JTextField();
        contactNumberField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        contactNumberField.setBorder(new LineBorder(new Color(200, 200, 200)));
        
        meetupInstructionsArea = new javax.swing.JTextArea();
        meetupInstructionsArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        meetupInstructionsArea.setLineWrap(true);
        meetupInstructionsArea.setWrapStyleWord(true);
        meetupInstructionsScroll = new JScrollPane(meetupInstructionsArea);
        meetupInstructionsScroll.setBorder(new LineBorder(new Color(200, 200, 200)));

        // Other trader details area
        otherDetailsArea = new javax.swing.JTextArea();
        otherDetailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        otherDetailsArea.setLineWrap(true);
        otherDetailsArea.setWrapStyleWord(true);
        otherDetailsArea.setEditable(false);
        otherDetailsArea.setBackground(new Color(240, 240, 240));
        otherDetailsScroll = new JScrollPane(otherDetailsArea);
        otherDetailsScroll.setBorder(new LineBorder(new Color(200, 200, 200)));

        // Payment labels
        baseAmountLabel = new javax.swing.JLabel();
        baseAmountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        feeAmountLabel = new javax.swing.JLabel();
        feeAmountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        totalAmountLabel = new javax.swing.JLabel();
        totalAmountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        feePayerLabel = new javax.swing.JLabel();
        feePayerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        paymentStatusLabel = new javax.swing.JLabel();
        paymentStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        uploadScreenshotButton = new javax.swing.JButton("Upload Screenshot");
        uploadScreenshotButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        uploadScreenshotButton.setBackground(new Color(12, 192, 223));
        uploadScreenshotButton.setForeground(Color.WHITE);
        uploadScreenshotButton.setBorder(null);
        uploadScreenshotButton.setFocusPainted(false);
        uploadScreenshotButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        uploadScreenshotButton.addActionListener(e -> uploadScreenshot());
        
        screenshotFileNameLabel = new javax.swing.JLabel();
        screenshotFileNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        screenshotFileNameLabel.setForeground(new Color(0, 102, 0));
        
        confirmReceivedCheck = new javax.swing.JCheckBox("I have received the item");
        confirmReceivedCheck.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        confirmReceivedCheck.setBackground(new Color(250, 250, 250));
    }

    private void loadTradeState() {
        String sql = "SELECT * FROM tbl_trade WHERE trade_id = ?";
        List<Map<String, Object>> trade = db.fetchRecords(sql, tradeId);
        
        if (!trade.isEmpty()) {
            Map<String, Object> t = trade.get(0);
            
            exchangeMethod = (String) t.get("exchange_method");
            methodAgreed = t.get("method_agreed") != null && Integer.parseInt(t.get("method_agreed").toString()) == 1;
            myMethodConfirmed = t.get("my_method_confirmed") != null && Integer.parseInt(t.get("my_method_confirmed").toString()) == 1;
            otherMethodConfirmed = t.get("other_method_confirmed") != null && Integer.parseInt(t.get("other_method_confirmed").toString()) == 1;
            
            myDetailsSubmitted = t.get("my_details_submitted") != null && Integer.parseInt(t.get("my_details_submitted").toString()) == 1;
            otherDetailsSubmitted = t.get("other_details_submitted") != null && Integer.parseInt(t.get("other_details_submitted").toString()) == 1;
            detailsAgreed = t.get("details_agreed") != null && Integer.parseInt(t.get("details_agreed").toString()) == 1;
            
            feePayer = t.get("fee_payer_id") != null && Integer.parseInt(t.get("fee_payer_id").toString()) == traderId;
            baseAmount = t.get("base_amount") != null ? Double.parseDouble(t.get("base_amount").toString()) : 0;
            feeAmount = t.get("fee_amount") != null ? Double.parseDouble(t.get("fee_amount").toString()) : 0;
            totalAmount = baseAmount + (feePayer ? feeAmount : 0);
            
            myPaymentSubmitted = t.get("my_payment_submitted") != null && Integer.parseInt(t.get("my_payment_submitted").toString()) == 1;
            otherPaymentSubmitted = t.get("other_payment_submitted") != null && Integer.parseInt(t.get("other_payment_submitted").toString()) == 1;
            paymentVerified = t.get("payment_verified") != null && Integer.parseInt(t.get("payment_verified").toString()) == 1;
            
            myItemReceived = t.get("my_item_received") != null && Integer.parseInt(t.get("my_item_received").toString()) == 1;
            otherItemReceived = t.get("other_item_received") != null && Integer.parseInt(t.get("other_item_received").toString()) == 1;
            
            refundProcessed = t.get("refund_processed") != null && Integer.parseInt(t.get("refund_processed").toString()) == 1;
            tradeCompleted = "completed".equals(t.get("trade_status"));
            
            determineCurrentStep();
        }
    }

    private void determineCurrentStep() {
        if (tradeCompleted) {
            currentStep = 7;
        } else if (refundProcessed) {
            currentStep = 6;
        } else if (myItemReceived && otherItemReceived) {
            currentStep = 5;
        } else if (paymentVerified) {
            currentStep = 4;
        } else if (myPaymentSubmitted && otherPaymentSubmitted) {
            currentStep = 3;
        } else if (detailsAgreed) {
            currentStep = 2;
        } else {
            currentStep = 1;
        }
        
        stepHistory.push(currentStep);
    }

    private void updateUI() {
        stepIndicatorLabel.setText("Step " + currentStep + " of 6");
        
        stepPanel.removeAll();
        
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
                showStep4AwaitShipment();
                break;
            case 5:
                showStep5ConfirmReceipt();
                break;
            case 6:
                showStep6Refund();
                break;
            case 7:
                showCompleted();
                break;
        }
        
        stepPanel.revalidate();
        stepPanel.repaint();
        
        backButton.setEnabled(stepHistory.size() > 1 && currentStep > 1 && !tradeCompleted);
    }

    private void showStep1ProposeMethod() {
        statusLabel.setText("Step 1: Propose exchange method. Both traders must agree before proceeding.");
        
        javax.swing.JLabel methodLabel = new javax.swing.JLabel("Choose exchange method:");
        methodLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        methodLabel.setBounds(20, 20, 300, 25);
        stepPanel.add(methodLabel);
        
        deliveryRadio.setBounds(20, 50, 200, 30);
        meetupRadio.setBounds(230, 50, 200, 30);
        
        stepPanel.add(deliveryRadio);
        stepPanel.add(meetupRadio);
        
        if (exchangeMethod != null) {
            if (exchangeMethod.equals("delivery")) {
                deliveryRadio.setSelected(true);
            } else if (exchangeMethod.equals("meetup")) {
                meetupRadio.setSelected(true);
            }
        }
        
        methodStatusLabel.setBounds(20, 90, 600, 20);
        
        if (myMethodConfirmed && otherMethodConfirmed) {
            methodStatusLabel.setText("✓ Both traders have confirmed. You can proceed to Step 2.");
            methodStatusLabel.setForeground(new Color(0, 102, 0));
            proceedButton.setEnabled(true);
        } else if (myMethodConfirmed) {
            methodStatusLabel.setText("✓ You have confirmed. Waiting for " + otherTraderName + " to confirm.");
            methodStatusLabel.setForeground(new Color(204, 102, 0));
            proceedButton.setEnabled(false);
        } else {
            methodStatusLabel.setText("Select a method and click CONFIRM METHOD.");
            methodStatusLabel.setForeground(new Color(102, 102, 102));
            proceedButton.setEnabled(true);
            proceedButton.setText("CONFIRM METHOD");
        }
        
        stepPanel.add(methodStatusLabel);
        
        // Warning about confirmation
        javax.swing.JLabel warningLabel = new javax.swing.JLabel(
            "<html><i>Note: Once confirmed, you cannot change the method without restarting.</i></html>");
        warningLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        warningLabel.setForeground(new Color(204, 0, 0));
        warningLabel.setBounds(20, 120, 600, 20);
        stepPanel.add(warningLabel);
    }

    private void showStep2SetDetails() {
        statusLabel.setText("Step 2: Enter your " + exchangeMethod + " details. Both traders must submit.");
        
        javax.swing.JLabel detailsLabel = new javax.swing.JLabel(
            "Enter your " + exchangeMethod + " details below:");
        detailsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        detailsLabel.setBounds(20, 10, 400, 25);
        stepPanel.add(detailsLabel);
        
        int y = 40;
        int labelWidth = 150;
        int fieldWidth = 450;
        int fieldX = 180;
        
        if (exchangeMethod.equals("delivery")) {
            javax.swing.JLabel addrLabel = new javax.swing.JLabel("Delivery Address:");
            addrLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            addrLabel.setBounds(20, y, labelWidth, 25);
            stepPanel.add(addrLabel);
            
            deliveryAddressField.setBounds(fieldX, y, fieldWidth, 30);
            stepPanel.add(deliveryAddressField);
            y += 40;
            
            javax.swing.JLabel courierLabel = new javax.swing.JLabel("Courier Service:");
            courierLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            courierLabel.setBounds(20, y, labelWidth, 25);
            stepPanel.add(courierLabel);
            
            courierField.setBounds(fieldX, y, fieldWidth, 30);
            stepPanel.add(courierField);
            y += 40;
            
            javax.swing.JLabel dateLabel = new javax.swing.JLabel("Expected Date (YYYY-MM-DD):");
            dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            dateLabel.setBounds(20, y, labelWidth, 25);
            stepPanel.add(dateLabel);
            
            expectedDateField.setBounds(fieldX, y, fieldWidth, 30);
            stepPanel.add(expectedDateField);
            y += 40;
            
            javax.swing.JLabel trackLabel = new javax.swing.JLabel("Tracking Number:");
            trackLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            trackLabel.setBounds(20, y, labelWidth, 25);
            stepPanel.add(trackLabel);
            
            trackingField.setBounds(fieldX, y, fieldWidth, 30);
            stepPanel.add(trackingField);
            y += 40;
            
            javax.swing.JLabel instLabel = new javax.swing.JLabel("Special Instructions:");
            instLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            instLabel.setBounds(20, y, labelWidth, 25);
            stepPanel.add(instLabel);
            
            deliveryInstructionsScroll.setBounds(fieldX, y, fieldWidth, 60);
            stepPanel.add(deliveryInstructionsScroll);
            
        } else if (exchangeMethod.equals("meetup")) {
            javax.swing.JLabel locLabel = new javax.swing.JLabel("Meetup Location:");
            locLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            locLabel.setBounds(20, y, labelWidth, 25);
            stepPanel.add(locLabel);
            
            meetupLocationField.setBounds(fieldX, y, fieldWidth, 30);
            stepPanel.add(meetupLocationField);
            y += 40;
            
            javax.swing.JLabel dateLabel = new javax.swing.JLabel("Date (YYYY-MM-DD):");
            dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            dateLabel.setBounds(20, y, labelWidth, 25);
            stepPanel.add(dateLabel);
            
            meetupDateField.setBounds(fieldX, y, fieldWidth, 30);
            stepPanel.add(meetupDateField);
            y += 40;
            
            javax.swing.JLabel timeLabel = new javax.swing.JLabel("Time (HH:MM):");
            timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            timeLabel.setBounds(20, y, labelWidth, 25);
            stepPanel.add(timeLabel);
            
            meetupTimeField.setBounds(fieldX, y, fieldWidth, 30);
            stepPanel.add(meetupTimeField);
            y += 40;
            
            javax.swing.JLabel personLabel = new javax.swing.JLabel("Contact Person:");
            personLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            personLabel.setBounds(20, y, labelWidth, 25);
            stepPanel.add(personLabel);
            
            contactPersonField.setBounds(fieldX, y, fieldWidth, 30);
            stepPanel.add(contactPersonField);
            y += 40;
            
            javax.swing.JLabel numberLabel = new javax.swing.JLabel("Contact Number:");
            numberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            numberLabel.setBounds(20, y, labelWidth, 25);
            stepPanel.add(numberLabel);
            
            contactNumberField.setBounds(fieldX, y, fieldWidth, 30);
            stepPanel.add(contactNumberField);
            y += 40;
            
            javax.swing.JLabel instLabel = new javax.swing.JLabel("Special Instructions:");
            instLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            instLabel.setBounds(20, y, labelWidth, 25);
            stepPanel.add(instLabel);
            
            meetupInstructionsScroll.setBounds(fieldX, y, fieldWidth, 60);
            stepPanel.add(meetupInstructionsScroll);
        }
        
        javax.swing.JLabel submitStatus = new javax.swing.JLabel();
        submitStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        submitStatus.setBounds(20, 280, 600, 20);
        
        if (myDetailsSubmitted && otherDetailsSubmitted) {
            submitStatus.setText("✓ Both traders have submitted details. You can now review.");
            submitStatus.setForeground(new Color(0, 102, 0));
            proceedButton.setEnabled(true);
            proceedButton.setText("REVIEW DETAILS");
        } else if (myDetailsSubmitted) {
            submitStatus.setText("✓ You have submitted your details. Waiting for " + otherTraderName + ".");
            submitStatus.setForeground(new Color(204, 102, 0));
            proceedButton.setEnabled(false);
        } else {
            submitStatus.setText("Please fill in all fields and click SUBMIT DETAILS.");
            submitStatus.setForeground(new Color(102, 102, 102));
            proceedButton.setEnabled(true);
            proceedButton.setText("SUBMIT DETAILS");
        }
        
        stepPanel.add(submitStatus);
    }

    private void showStep3Payment() {
        statusLabel.setText("Step 3: Payment processing with admin as middleman.");
        
        // Load payment details from admin
        loadPaymentDetails();
        
        int y = 20;
        
        javax.swing.JLabel paymentTitle = new javax.swing.JLabel("Payment Details");
        paymentTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        paymentTitle.setForeground(new Color(0, 102, 102));
        paymentTitle.setBounds(20, y, 300, 25);
        stepPanel.add(paymentTitle);
        y += 35;
        
        javax.swing.JLabel baseLabel = new javax.swing.JLabel("Base Amount:");
        baseLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        baseLabel.setBounds(20, y, 120, 25);
        stepPanel.add(baseLabel);
        
        baseAmountLabel.setText("₱" + String.format("%.2f", baseAmount));
        baseAmountLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        baseAmountLabel.setBounds(150, y, 100, 25);
        stepPanel.add(baseAmountLabel);
        y += 25;
        
        javax.swing.JLabel feeLabel = new javax.swing.JLabel("Fee Amount:");
        feeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        feeLabel.setBounds(20, y, 120, 25);
        stepPanel.add(feeLabel);
        
        feeAmountLabel.setText("₱" + String.format("%.2f", feeAmount));
        feeAmountLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        feeAmountLabel.setBounds(150, y, 100, 25);
        stepPanel.add(feeAmountLabel);
        y += 25;
        
        feePayerLabel.setText("Fee Payer: " + (feePayer ? "You" : otherTraderName));
        feePayerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        feePayerLabel.setForeground(new Color(204, 0, 0));
        feePayerLabel.setBounds(20, y, 300, 25);
        stepPanel.add(feePayerLabel);
        y += 30;
        
        totalAmountLabel.setText("TOTAL TO PAY: ₱" + String.format("%.2f", totalAmount));
        totalAmountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalAmountLabel.setForeground(new Color(0, 102, 0));
        totalAmountLabel.setBounds(20, y, 300, 25);
        stepPanel.add(totalAmountLabel);
        y += 40;
        
        javax.swing.JLabel instructionLabel = new javax.swing.JLabel(
            "<html>Please send the total amount to the following account:<br>"
            + "<b>Bank: BarterZone Escrow Account</b><br>"
            + "<b>Account Number: 1234-5678-9012</b><br>"
            + "<b>Account Name: BarterZone Trust</b></html>");
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        instructionLabel.setBounds(20, y, 600, 60);
        stepPanel.add(instructionLabel);
        y += 70;
        
        uploadScreenshotButton.setBounds(20, y, 150, 30);
        stepPanel.add(uploadScreenshotButton);
        
        screenshotFileNameLabel.setBounds(180, y, 400, 30);
        stepPanel.add(screenshotFileNameLabel);
        y += 40;
        
        paymentStatusLabel.setBounds(20, y, 400, 25);
        
        if (myPaymentSubmitted && otherPaymentSubmitted) {
            if (paymentVerified) {
                paymentStatusLabel.setText("✓ Payment verified by admin! You can proceed.");
                paymentStatusLabel.setForeground(new Color(0, 102, 0));
                proceedButton.setEnabled(true);
                proceedButton.setText("PROCEED TO NEXT STEP");
            } else {
                paymentStatusLabel.setText("⏳ Both payments submitted. Waiting for admin verification...");
                paymentStatusLabel.setForeground(new Color(255, 140, 0));
                proceedButton.setEnabled(false);
            }
        } else if (myPaymentSubmitted) {
            paymentStatusLabel.setText("✓ Your payment submitted. Waiting for " + otherTraderName + ".");
            paymentStatusLabel.setForeground(new Color(204, 102, 0));
            proceedButton.setEnabled(false);
        } else {
            paymentStatusLabel.setText("Please upload your payment screenshot.");
            paymentStatusLabel.setForeground(new Color(102, 102, 102));
            proceedButton.setEnabled(false);
        }
        
        stepPanel.add(paymentStatusLabel);
    }

    private void showStep4AwaitShipment() {
        statusLabel.setText("Step 4: Items are being shipped/exchanged.");
        
        javax.swing.JLabel shipmentLabel = new javax.swing.JLabel("Items are on the way!");
        shipmentLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        shipmentLabel.setForeground(new Color(0, 102, 102));
        shipmentLabel.setBounds(20, 20, 300, 25);
        stepPanel.add(shipmentLabel);
        
        javax.swing.JLabel trackingInfo = new javax.swing.JLabel(
            "<html>Both parties have made payment.<br>"
            + "Please wait for your item to arrive. Track your shipment using the tracking numbers provided.</html>");
        trackingInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trackingInfo.setBounds(20, 60, 600, 50);
        stepPanel.add(trackingInfo);
        
        // Load and display tracking info
        loadTrackingInfo();
        
        javax.swing.JTextArea trackingArea = new javax.swing.JTextArea();
        trackingArea.setEditable(false);
        trackingArea.setBackground(new Color(240, 240, 240));
        trackingArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trackingArea.setText(getTrackingInfo());
        
        JScrollPane trackingScroll = new JScrollPane(trackingArea);
        trackingScroll.setBounds(20, 120, 600, 100);
        trackingScroll.setBorder(new LineBorder(new Color(200, 200, 200)));
        stepPanel.add(trackingScroll);
        
        javax.swing.JLabel waitLabel = new javax.swing.JLabel(
            "Once you receive the item, you can confirm receipt in the next step.");
        waitLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        waitLabel.setForeground(new Color(102, 102, 102));
        waitLabel.setBounds(20, 230, 600, 25);
        stepPanel.add(waitLabel);
        
        proceedButton.setEnabled(true);
        proceedButton.setText("ITEM RECEIVED");
    }

    private void showStep5ConfirmReceipt() {
        statusLabel.setText("Step 5: Confirm item receipt.");
        
        javax.swing.JLabel receiptLabel = new javax.swing.JLabel("Have you received the item?");
        receiptLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        receiptLabel.setForeground(new Color(0, 102, 102));
        receiptLabel.setBounds(20, 20, 300, 25);
        stepPanel.add(receiptLabel);
        
        confirmReceivedCheck.setBounds(20, 60, 300, 30);
        stepPanel.add(confirmReceivedCheck);
        
        javax.swing.JButton confirmButton = new javax.swing.JButton("CONFIRM RECEIPT");
        confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        confirmButton.setBackground(new Color(0, 102, 102));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setBounds(20, 100, 150, 35);
        confirmButton.setBorder(null);
        confirmButton.setFocusPainted(false);
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmButton.addActionListener(e -> confirmReceipt());
        stepPanel.add(confirmButton);
        
        javax.swing.JLabel receiptStatus = new javax.swing.JLabel();
        receiptStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        receiptStatus.setBounds(20, 150, 400, 20);
        
        if (myItemReceived) {
            receiptStatus.setText("✓ You have confirmed receipt. Waiting for " + otherTraderName + ".");
            receiptStatus.setForeground(new Color(0, 102, 0));
            proceedButton.setEnabled(false);
        } else {
            receiptStatus.setText("Please confirm once you have received the item.");
            receiptStatus.setForeground(new Color(102, 102, 102));
            proceedButton.setEnabled(false);
        }
        
        stepPanel.add(receiptStatus);
        
        javax.swing.JLabel bothStatus = new javax.swing.JLabel();
        bothStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        bothStatus.setBounds(20, 175, 400, 20);
        
        if (myItemReceived && otherItemReceived) {
            bothStatus.setText("✓ Both traders have received items! Proceed to refund.");
            bothStatus.setForeground(new Color(0, 102, 0));
            proceedButton.setEnabled(true);
            proceedButton.setText("PROCEED TO REFUND");
        } else {
            bothStatus.setText("");
        }
        
        stepPanel.add(bothStatus);
    }

    private void showStep6Refund() {
        statusLabel.setText("Step 6: Refund processing.");
        
        javax.swing.JLabel refundLabel = new javax.swing.JLabel("Refund Processing");
        refundLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        refundLabel.setForeground(new Color(0, 102, 102));
        refundLabel.setBounds(20, 20, 300, 25);
        stepPanel.add(refundLabel);
        
        javax.swing.JLabel refundInfo = new javax.swing.JLabel(
            "<html>Both traders have confirmed receipt.<br>"
            + "The base amount of ₱" + String.format("%.2f", baseAmount) + " will be refunded to both parties.<br>"
            + "The fee of ₱" + String.format("%.2f", feeAmount) + " is retained by BarterZone.</html>");
        refundInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        refundInfo.setBounds(20, 60, 600, 60);
        stepPanel.add(refundInfo);
        
        javax.swing.JLabel refundStatus = new javax.swing.JLabel();
        refundStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refundStatus.setBounds(20, 140, 400, 25);
        
        if (refundProcessed) {
            refundStatus.setText("✓ Refund has been processed!");
            refundStatus.setForeground(new Color(0, 102, 0));
            proceedButton.setEnabled(true);
            proceedButton.setText("COMPLETE TRADE");
        } else {
            refundStatus.setText("⏳ Waiting for admin to process refund...");
            refundStatus.setForeground(new Color(255, 140, 0));
            proceedButton.setEnabled(false);
        }
        
        stepPanel.add(refundStatus);
    }

    private void showCompleted() {
        statusLabel.setText("Trade Completed Successfully!");
        
        javax.swing.JLabel completedLabel = new javax.swing.JLabel(
            "<html><h2>✓ TRADE COMPLETED</h2>"
            + "<p>This trade has been successfully completed.</p>"
            + "<p>Thank you for using BarterZone!</p></html>");
        completedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        completedLabel.setForeground(new Color(0, 102, 0));
        completedLabel.setBounds(20, 20, 600, 150);
        stepPanel.add(completedLabel);
        
        javax.swing.JButton closeButton = new javax.swing.JButton("CLOSE");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeButton.setBackground(new Color(12, 192, 223));
        closeButton.setForeground(Color.WHITE);
        closeButton.setBounds(250, 180, 150, 40);
        closeButton.setBorder(null);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> dispose());
        stepPanel.add(closeButton);
        
        proceedButton.setEnabled(false);
        backButton.setEnabled(false);
        cancelTradeButton.setEnabled(false);
    }

    private void loadPaymentDetails() {
        // In a real implementation, these would come from the admin via database
        String sql = "SELECT base_amount, fee_amount, fee_payer_id FROM tbl_trade WHERE trade_id = ?";
        List<Map<String, Object>> result = db.fetchRecords(sql, tradeId);
        
        if (!result.isEmpty()) {
            Map<String, Object> data = result.get(0);
            baseAmount = data.get("base_amount") != null ? Double.parseDouble(data.get("base_amount").toString()) : 0;
            feeAmount = data.get("fee_amount") != null ? Double.parseDouble(data.get("fee_amount").toString()) : 0;
            int feePayerId = data.get("fee_payer_id") != null ? Integer.parseInt(data.get("fee_payer_id").toString()) : -1;
            feePayer = feePayerId == traderId;
            totalAmount = baseAmount + (feePayer ? feeAmount : 0);
        }
    }

    private void loadTrackingInfo() {
        // Load tracking info from database
    }

    private String getTrackingInfo() {
        StringBuilder info = new StringBuilder();
        info.append("YOUR SHIPMENT:\n");
        
        if (exchangeMethod.equals("delivery")) {
            String sql = "SELECT * FROM tbl_trade_details WHERE trade_id = ? AND trader_id = ?";
            List<Map<String, Object>> myDetails = db.fetchRecords(sql, tradeId, traderId);
            if (!myDetails.isEmpty()) {
                Map<String, Object> d = myDetails.get(0);
                info.append("Courier: ").append(d.get("courier")).append("\n");
                info.append("Tracking: ").append(d.get("tracking_number")).append("\n");
                info.append("Expected: ").append(d.get("expected_date")).append("\n");
            }
            
            info.append("\n").append(otherTraderName).append("'S SHIPMENT:\n");
            List<Map<String, Object>> otherDetails = db.fetchRecords(sql, tradeId, otherTraderId);
            if (!otherDetails.isEmpty()) {
                Map<String, Object> d = otherDetails.get(0);
                info.append("Courier: ").append(d.get("courier")).append("\n");
                info.append("Tracking: ").append(d.get("tracking_number")).append("\n");
                info.append("Expected: ").append(d.get("expected_date")).append("\n");
            }
        } else {
            info.append("Meetup arranged with ").append(otherTraderName);
        }
        
        return info.toString();
    }

    private void uploadScreenshot() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            uploadedScreenshotPath = selectedFile.getAbsolutePath();
            screenshotFileNameLabel.setText(selectedFile.getName());
            
            // Save screenshot path to database
            String sql = "UPDATE tbl_trade SET my_payment_screenshot = ?, my_payment_submitted = 1 WHERE trade_id = ?";
            db.updateRecord(sql, uploadedScreenshotPath, tradeId);
            
            myPaymentSubmitted = true;
            
            JOptionPane.showMessageDialog(this,
                "Screenshot uploaded successfully!\n\n"
                + "Admin will verify your payment.",
                "Upload Complete",
                JOptionPane.INFORMATION_MESSAGE);
            
            loadTradeState();
            updateUI();
        }
    }

    private void handleProceed() {
        switch (currentStep) {
            case 1:
                confirmMethod();
                break;
            case 2:
                if (proceedButton.getText().equals("REVIEW DETAILS")) {
                    goToReview();
                } else {
                    submitDetails();
                }
                break;
            case 4:
                // Move to receipt confirmation
                currentStep = 5;
                updateUI();
                break;
            case 5:
                if (proceedButton.getText().equals("PROCEED TO REFUND")) {
                    currentStep = 6;
                    updateUI();
                }
                break;
            case 6:
                completeTrade();
                break;
        }
    }

    private void confirmMethod() {
        if (!deliveryRadio.isSelected() && !meetupRadio.isSelected()) {
            JOptionPane.showMessageDialog(this,
                "Please select an exchange method.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String method = deliveryRadio.isSelected() ? "delivery" : "meetup";
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Confirm " + method + " as the exchange method?\n\n"
            + "This cannot be changed without restarting the trade.",
            "Confirm Method",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_trade SET exchange_method = ?, my_method_confirmed = 1 WHERE trade_id = ?";
            db.updateRecord(sql, method, tradeId);
            
            exchangeMethod = method;
            myMethodConfirmed = true;
            
            JOptionPane.showMessageDialog(this,
                "Method confirmed! Waiting for " + otherTraderName + " to confirm.",
                "Confirmation Sent",
                JOptionPane.INFORMATION_MESSAGE);
            
            loadTradeState();
            updateUI();
        }
    }

    private void submitDetails() {
        if (exchangeMethod.equals("delivery")) {
            if (deliveryAddressField.getText().trim().isEmpty() ||
                courierField.getText().trim().isEmpty() ||
                expectedDateField.getText().trim().isEmpty()) {
                
                JOptionPane.showMessageDialog(this,
                    "Please fill in all required fields (Address, Courier, and Expected Date).",
                    "Incomplete Information",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
        } else {
            if (meetupLocationField.getText().trim().isEmpty() ||
                meetupDateField.getText().trim().isEmpty() ||
                meetupTimeField.getText().trim().isEmpty() ||
                contactPersonField.getText().trim().isEmpty() ||
                contactNumberField.getText().trim().isEmpty()) {
                
                JOptionPane.showMessageDialog(this,
                    "Please fill in all required fields.",
                    "Incomplete Information",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Submit these details?\n\n"
            + "You can go back and edit before " + otherTraderName + " submits.",
            "Confirm Submission",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            saveDetails();
            
            myDetailsSubmitted = true;
            
            JOptionPane.showMessageDialog(this,
                "Details submitted successfully!\n\n"
                + "Waiting for " + otherTraderName + " to submit their details.",
                "Submission Complete",
                JOptionPane.INFORMATION_MESSAGE);
            
            loadTradeState();
            updateUI();
        }
    }

    private void saveDetails() {
        String sql = "INSERT OR REPLACE INTO tbl_trade_details "
            + "(trade_id, trader_id, exchange_method, "
            + "delivery_address, courier, expected_date, tracking_number, delivery_instructions, "
            + "meetup_location, meetup_date, meetup_time, contact_person, contact_number, meetup_instructions) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        if (exchangeMethod.equals("delivery")) {
            db.addRecord(sql,
                tradeId, traderId, exchangeMethod,
                deliveryAddressField.getText().trim(),
                courierField.getText().trim(),
                expectedDateField.getText().trim(),
                trackingField.getText().trim(),
                deliveryInstructionsArea.getText().trim(),
                null, null, null, null, null, null);
        } else {
            db.addRecord(sql,
                tradeId, traderId, exchangeMethod,
                null, null, null, null, null,
                meetupLocationField.getText().trim(),
                meetupDateField.getText().trim(),
                meetupTimeField.getText().trim(),
                contactPersonField.getText().trim(),
                contactNumberField.getText().trim(),
                meetupInstructionsArea.getText().trim());
        }
        
        String updateSql = "UPDATE tbl_trade SET my_details_submitted = 1 WHERE trade_id = ?";
        db.updateRecord(updateSql, tradeId);
    }

    private void goToReview() {
        currentStep = 3;
        updateUI();
    }

    private void goBack() {
        if (stepHistory.size() > 1) {
            stepHistory.pop();
            currentStep = stepHistory.peek();
            updateUI();
        }
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

    private void completeTrade() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Complete this trade?\n\n"
            + "• Both traders have received items\n"
            + "• Refund has been processed\n"
            + "• This action cannot be undone",
            "Complete Trade",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Move to history
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
                    "✅ TRADE COMPLETED SUCCESSFULLY!\n\n"
                    + "Thank you for using BarterZone.",
                    "Trade Complete",
                    JOptionPane.INFORMATION_MESSAGE);
                
                dispose();
            }
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
            
            JOptionPane.showMessageDialog(this,
                "Trade cancelled.",
                "Cancelled",
                JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
        }
    }
}