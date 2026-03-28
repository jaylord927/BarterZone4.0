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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
import javax.swing.JFileChooser;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
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
    private boolean myDetailsSubmitted = false;
    private boolean otherDetailsSubmitted = false;
    private boolean myDetailsAgreed = false;
    private boolean otherDetailsAgreed = false;
    private boolean feePayer = false;
    private double baseAmount = 200;
    private double feeAmount = 15;
    private double totalAmount = 0;
    private boolean myPaymentSubmitted = false;
    private boolean otherPaymentSubmitted = false;
    private boolean paymentVerified = false;
    private boolean myItemReceived = false;
    private boolean otherItemReceived = false;
    private boolean refundProcessed = false;
    private boolean tradeCompleted = false;
    
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
    private JTextField deliveryAddressField;
    private JTextField courierField;
    private JTextField expectedDateField;
    private JTextField trackingField;
    private JTextArea deliveryInstructionsArea;
    private JScrollPane deliveryInstructionsScroll;
    
    private JTextField meetupLocationField;
    private JTextField meetupDateField;
    private JTextField meetupTimeField;
    private JTextField contactPersonField;
    private JTextField contactNumberField;
    private JTextField googleMapsLinkField;
    private JTextArea meetupInstructionsArea;
    private JScrollPane meetupInstructionsScroll;
    
    private JTextArea otherDetailsArea;
    private JScrollPane otherDetailsScroll;
    private JLabel myDetailsStatus;
    private JLabel otherDetailsStatus;
    private JButton submitDetailsButton;
    private JButton agreeToOtherDetailsButton;
    private JButton declineOtherDetailsButton;
    private JLabel myAgreementStatus;
    private JLabel otherAgreementStatus;
    
    private JLabel baseAmountLabel;
    private JLabel feeAmountLabel;
    private JLabel totalAmountLabel;
    private JLabel feePayerLabel;
    private JLabel paymentStatusLabel;
    private JButton uploadScreenshotButton;
    private JLabel screenshotFileNameLabel;
    private String uploadedScreenshotPath = "";
    
    private JComboBox<String> paymentMethodCombo;
    private JTextField accountNumberField;
    private JTextField accountNameField;
    
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
    
    private static final String SCREENSHOT_PATH = "src/BarterZone/resources/images/payment/";

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
        createImageDirectory();
        loadTradeState();
        updateUI();
        
        setTitle("Manage Trade - Trade #" + tradeId);
        setSize(1000, 800);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void createImageDirectory() {
        File directory = new File(SCREENSHOT_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
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

        deliveryAddressField = new JTextField();
        courierField = new JTextField();
        expectedDateField = new JTextField();
        trackingField = new JTextField();
        deliveryInstructionsArea = new JTextArea(3, 20);
        deliveryInstructionsArea.setLineWrap(true);
        deliveryInstructionsArea.setWrapStyleWord(true);
        deliveryInstructionsScroll = new JScrollPane(deliveryInstructionsArea);
        
        meetupLocationField = new JTextField();
        meetupDateField = new JTextField();
        meetupTimeField = new JTextField();
        contactPersonField = new JTextField();
        contactNumberField = new JTextField();
        googleMapsLinkField = new JTextField();
        meetupInstructionsArea = new JTextArea(3, 20);
        meetupInstructionsArea.setLineWrap(true);
        meetupInstructionsArea.setWrapStyleWord(true);
        meetupInstructionsScroll = new JScrollPane(meetupInstructionsArea);
        
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
        
        submitDetailsButton = new JButton();
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
        
        declineOtherDetailsButton = new JButton("DECLINE");
        declineOtherDetailsButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        declineOtherDetailsButton.setBackground(errorColor);
        declineOtherDetailsButton.setForeground(Color.WHITE);
        declineOtherDetailsButton.setBorder(null);
        declineOtherDetailsButton.setFocusPainted(false);
        declineOtherDetailsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        myAgreementStatus = new JLabel();
        myAgreementStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        otherAgreementStatus = new JLabel();
        otherAgreementStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        baseAmountLabel = new JLabel();
        feeAmountLabel = new JLabel();
        totalAmountLabel = new JLabel();
        feePayerLabel = new JLabel();
        paymentStatusLabel = new JLabel();
        
        uploadScreenshotButton = new JButton("Upload Screenshot");
        uploadScreenshotButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        uploadScreenshotButton.setBackground(themeColor);
        uploadScreenshotButton.setForeground(Color.WHITE);
        uploadScreenshotButton.setBorder(null);
        uploadScreenshotButton.setFocusPainted(false);
        uploadScreenshotButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        screenshotFileNameLabel = new JLabel();
        
        String[] paymentMethods = {"Select Method", "GCash", "PayMaya"};
        paymentMethodCombo = new JComboBox<>(paymentMethods);
        paymentMethodCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        accountNumberField = new JTextField();
        accountNameField = new JTextField();
        
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
            
            myDetailsSubmitted = t.get("my_details_submitted") != null && 
                Integer.parseInt(t.get("my_details_submitted").toString()) == 1;
            otherDetailsSubmitted = t.get("other_details_submitted") != null && 
                Integer.parseInt(t.get("other_details_submitted").toString()) == 1;
            myDetailsAgreed = t.get("my_details_agreed") != null && 
                Integer.parseInt(t.get("my_details_agreed").toString()) == 1;
            otherDetailsAgreed = t.get("other_details_agreed") != null && 
                Integer.parseInt(t.get("other_details_agreed").toString()) == 1;
            
            feePayer = t.get("fee_payer_id") != null && 
                Integer.parseInt(t.get("fee_payer_id").toString()) == traderId;
            
            myPaymentSubmitted = t.get("my_payment_submitted") != null && 
                Integer.parseInt(t.get("my_payment_submitted").toString()) == 1;
            otherPaymentSubmitted = t.get("other_payment_submitted") != null && 
                Integer.parseInt(t.get("other_payment_submitted").toString()) == 1;
            paymentVerified = t.get("payment_verified") != null && 
                Integer.parseInt(t.get("payment_verified").toString()) == 1;
            
            myItemReceived = t.get("my_item_received") != null && 
                Integer.parseInt(t.get("my_item_received").toString()) == 1;
            otherItemReceived = t.get("other_item_received") != null && 
                Integer.parseInt(t.get("other_item_received").toString()) == 1;
            
            refundProcessed = t.get("refund_processed") != null && 
                Integer.parseInt(t.get("refund_processed").toString()) == 1;
            tradeCompleted = "completed".equals(t.get("trade_status"));
            
            totalAmount = baseAmount + (feePayer ? feeAmount : 0);
            determineCurrentStep();
        }
        
        loadMyDetailsFromDB();
        loadOtherDetailsFromDB();
    }
    
    private void loadMyDetailsFromDB() {
        String sql = "SELECT * FROM tbl_trade_details WHERE trade_id = ? AND trader_id = ?";
        List<Map<String, Object>> details = db.fetchRecords(sql, tradeId, traderId);
        
        if (!details.isEmpty()) {
            Map<String, Object> d = details.get(0);
            if (exchangeMethod != null && exchangeMethod.equals("delivery")) {
                deliveryAddressField.setText(d.get("delivery_address") != null ? d.get("delivery_address").toString() : "");
                courierField.setText(d.get("courier") != null ? d.get("courier").toString() : "");
                expectedDateField.setText(d.get("expected_date") != null ? d.get("expected_date").toString() : "");
                trackingField.setText(d.get("tracking_number") != null ? d.get("tracking_number").toString() : "");
                deliveryInstructionsArea.setText(d.get("delivery_instructions") != null ? d.get("delivery_instructions").toString() : "");
            } else if (exchangeMethod != null && exchangeMethod.equals("meetup")) {
                meetupLocationField.setText(d.get("meetup_location") != null ? d.get("meetup_location").toString() : "");
                meetupDateField.setText(d.get("meetup_date") != null ? d.get("meetup_date").toString() : "");
                meetupTimeField.setText(d.get("meetup_time") != null ? d.get("meetup_time").toString() : "");
                contactPersonField.setText(d.get("contact_person") != null ? d.get("contact_person").toString() : "");
                contactNumberField.setText(d.get("contact_number") != null ? d.get("contact_number").toString() : "");
                meetupInstructionsArea.setText(d.get("meetup_instructions") != null ? d.get("meetup_instructions").toString() : "");
                googleMapsLinkField.setText(d.get("google_maps_link") != null ? d.get("google_maps_link").toString() : "");
            }
        }
    }
    
    private void loadOtherDetailsFromDB() {
        String sql = "SELECT * FROM tbl_trade_details WHERE trade_id = ? AND trader_id = ?";
        List<Map<String, Object>> details = db.fetchRecords(sql, tradeId, otherTraderId);
        
        StringBuilder detailsText = new StringBuilder();
        if (!details.isEmpty()) {
            Map<String, Object> d = details.get(0);
            String method = d.get("exchange_method") != null ? d.get("exchange_method").toString() : "";
            
            if (method.equals("delivery")) {
                detailsText.append("Exchange Method: Delivery\n\n");
                detailsText.append("Delivery Address: ").append(d.get("delivery_address") != null ? d.get("delivery_address") : "Not provided").append("\n");
                detailsText.append("Courier: ").append(d.get("courier") != null ? d.get("courier") : "Not provided").append("\n");
                detailsText.append("Expected Date: ").append(d.get("expected_date") != null ? d.get("expected_date") : "Not provided").append("\n");
                detailsText.append("Tracking: ").append(d.get("tracking_number") != null ? d.get("tracking_number") : "Not provided").append("\n");
                detailsText.append("Instructions: ").append(d.get("delivery_instructions") != null ? d.get("delivery_instructions") : "None");
            } else if (method.equals("meetup")) {
                detailsText.append("Exchange Method: Meetup\n\n");
                detailsText.append("Location: ").append(d.get("meetup_location") != null ? d.get("meetup_location") : "Not provided").append("\n");
                String mapsLink = d.get("google_maps_link") != null ? d.get("google_maps_link").toString() : "";
                if (!mapsLink.isEmpty()) detailsText.append("Google Maps: ").append(mapsLink).append("\n");
                detailsText.append("Date: ").append(d.get("meetup_date") != null ? d.get("meetup_date") : "Not provided").append("\n");
                detailsText.append("Time: ").append(d.get("meetup_time") != null ? d.get("meetup_time") : "Not provided").append("\n");
                detailsText.append("Contact: ").append(d.get("contact_person") != null ? d.get("contact_person") : "Not provided").append(" - ").append(d.get("contact_number") != null ? d.get("contact_number") : "Not provided").append("\n");
                detailsText.append("Instructions: ").append(d.get("meetup_instructions") != null ? d.get("meetup_instructions") : "None");
            }
        } else {
            detailsText.append("No details submitted yet.");
        }
        
        otherDetailsArea.setText(detailsText.toString());
        otherDetailsArea.setCaretPosition(0);
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
        } else if (myDetailsAgreed && otherDetailsAgreed && myDetailsSubmitted && otherDetailsSubmitted) {
            currentStep = 2;
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
        stepPanel.setPreferredSize(new Dimension(940, 700));
        
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
            if (myDetailsAgreed && otherDetailsAgreed) {
                agreementStatus.setText("Both traders have agreed to the details! Click PROCEED to continue to payment.");
                agreementStatus.setForeground(successColor);
                proceedButton.setEnabled(true);
                proceedButton.setText("PROCEED TO PAYMENT");
            } else if (myDetailsAgreed) {
                agreementStatus.setText("You have agreed. Waiting for " + otherTraderName + " to agree.");
                agreementStatus.setForeground(warningColor);
                proceedButton.setEnabled(false);
            } else if (otherDetailsAgreed) {
                agreementStatus.setText(otherTraderName + " has agreed. Click AGREE on your side to confirm.");
                agreementStatus.setForeground(warningColor);
                proceedButton.setEnabled(false);
            } else {
                agreementStatus.setText("Both traders have submitted details. Click AGREE on your side to confirm.");
                agreementStatus.setForeground(warningColor);
                proceedButton.setEnabled(false);
            }
        } else if (myDetailsSubmitted) {
            agreementStatus.setText("You have submitted your details. Waiting for " + otherTraderName + " to submit their details.");
            agreementStatus.setForeground(warningColor);
            proceedButton.setEnabled(false);
        } else if (otherDetailsSubmitted) {
            agreementStatus.setText(otherTraderName + " has submitted their details. Please submit your details and then agree.");
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
        int labelWidth = 120;
        int fieldWidth = 280;
        int fieldX = 130;
        
        if (exchangeMethod != null && exchangeMethod.equals("delivery")) {
            JLabel addrLabel = new JLabel("Delivery Address:*");
            addrLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            addrLabel.setBounds(10, fieldY, labelWidth, 25);
            myDetailsPanel.add(addrLabel);
            deliveryAddressField.setBounds(fieldX, fieldY, fieldWidth, 30);
            myDetailsPanel.add(deliveryAddressField);
            fieldY += 40;
            
            JLabel courierLabel = new JLabel("Courier Service:*");
            courierLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            courierLabel.setBounds(10, fieldY, labelWidth, 25);
            myDetailsPanel.add(courierLabel);
            courierField.setBounds(fieldX, fieldY, fieldWidth, 30);
            myDetailsPanel.add(courierField);
            fieldY += 40;
            
            JLabel dateLabel = new JLabel("Expected Date:*");
            dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            dateLabel.setBounds(10, fieldY, labelWidth, 25);
            myDetailsPanel.add(dateLabel);
            expectedDateField.setBounds(fieldX, fieldY, fieldWidth, 30);
            myDetailsPanel.add(expectedDateField);
            fieldY += 40;
            
            JLabel trackLabel = new JLabel("Tracking Number:");
            trackLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            trackLabel.setBounds(10, fieldY, labelWidth, 25);
            myDetailsPanel.add(trackLabel);
            trackingField.setBounds(fieldX, fieldY, fieldWidth, 30);
            myDetailsPanel.add(trackingField);
            fieldY += 40;
            
            JLabel instLabel = new JLabel("Special Instructions:");
            instLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            instLabel.setBounds(10, fieldY, labelWidth, 25);
            myDetailsPanel.add(instLabel);
            deliveryInstructionsScroll.setBounds(fieldX, fieldY, fieldWidth, 70);
            myDetailsPanel.add(deliveryInstructionsScroll);
            fieldY += 80;
        } else if (exchangeMethod != null && exchangeMethod.equals("meetup")) {
            JLabel locLabel = new JLabel("Meetup Location:*");
            locLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            locLabel.setBounds(10, fieldY, labelWidth, 25);
            myDetailsPanel.add(locLabel);
            meetupLocationField.setBounds(fieldX, fieldY, fieldWidth, 30);
            myDetailsPanel.add(meetupLocationField);
            fieldY += 40;
            
            JLabel mapsLabel = new JLabel("Google Maps Link:");
            mapsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            mapsLabel.setBounds(10, fieldY, labelWidth, 25);
            myDetailsPanel.add(mapsLabel);
            googleMapsLinkField.setBounds(fieldX, fieldY, fieldWidth, 30);
            myDetailsPanel.add(googleMapsLinkField);
            fieldY += 40;
            
            JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):*");
            dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            dateLabel.setBounds(10, fieldY, labelWidth, 25);
            myDetailsPanel.add(dateLabel);
            meetupDateField.setBounds(fieldX, fieldY, fieldWidth, 30);
            myDetailsPanel.add(meetupDateField);
            fieldY += 40;
            
            JLabel timeLabel = new JLabel("Time (HH:MM):*");
            timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            timeLabel.setBounds(10, fieldY, labelWidth, 25);
            myDetailsPanel.add(timeLabel);
            meetupTimeField.setBounds(fieldX, fieldY, fieldWidth, 30);
            myDetailsPanel.add(meetupTimeField);
            fieldY += 40;
            
            JLabel personLabel = new JLabel("Contact Person:*");
            personLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            personLabel.setBounds(10, fieldY, labelWidth, 25);
            myDetailsPanel.add(personLabel);
            contactPersonField.setBounds(fieldX, fieldY, fieldWidth, 30);
            myDetailsPanel.add(contactPersonField);
            fieldY += 40;
            
            JLabel numberLabel = new JLabel("Contact Number:*");
            numberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            numberLabel.setBounds(10, fieldY, labelWidth, 25);
            myDetailsPanel.add(numberLabel);
            contactNumberField.setBounds(fieldX, fieldY, fieldWidth, 30);
            myDetailsPanel.add(contactNumberField);
            fieldY += 40;
            
            JLabel instLabel = new JLabel("Special Instructions:");
            instLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            instLabel.setBounds(10, fieldY, labelWidth, 25);
            myDetailsPanel.add(instLabel);
            meetupInstructionsScroll.setBounds(fieldX, fieldY, fieldWidth, 70);
            myDetailsPanel.add(meetupInstructionsScroll);
            fieldY += 80;
        }
        
        if (myDetailsSubmitted) {
            submitDetailsButton.setText("UPDATE MY DETAILS");
        } else {
            submitDetailsButton.setText("SUBMIT MY DETAILS");
        }
        submitDetailsButton.setBounds(120, fieldY, 200, 35);
        submitDetailsButton.addActionListener(e -> submitDetails());
        myDetailsPanel.add(submitDetailsButton);
        fieldY += 50;
        
        if (myDetailsSubmitted) {
            myDetailsStatus.setText("Your details have been submitted");
            myDetailsStatus.setForeground(successColor);
            myDetailsStatus.setBounds(120, fieldY, 250, 30);
            myDetailsPanel.add(myDetailsStatus);
            fieldY += 45;
        }
        
        if (myDetailsAgreed) {
            myAgreementStatus.setText("You have agreed to the other trader's details");
            myAgreementStatus.setForeground(successColor);
            myAgreementStatus.setBounds(80, fieldY, 300, 30);
            myDetailsPanel.add(myAgreementStatus);
        }
        
        JLabel statusTitle = new JLabel("Status:");
        statusTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        statusTitle.setBounds(10, fieldY + 10, 50, 20);
        myDetailsPanel.add(statusTitle);
    }

    private void buildOtherDetailsPanel() {
        int fieldY = 30;
        
        otherDetailsScroll.setBounds(10, fieldY, 420, 280);
        otherDetailsPanel.add(otherDetailsScroll);
        loadOtherDetailsFromDB();
        fieldY += 300;
        
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
        
        if (otherDetailsSubmitted && !myDetailsAgreed) {
            agreeToOtherDetailsButton.setBounds(100, fieldY, 140, 35);
            declineOtherDetailsButton.setBounds(250, fieldY, 100, 35);
            agreeToOtherDetailsButton.addActionListener(e -> agreeToOtherDetails());
            declineOtherDetailsButton.addActionListener(e -> declineOtherDetails());
            otherDetailsPanel.add(agreeToOtherDetailsButton);
            otherDetailsPanel.add(declineOtherDetailsButton);
            fieldY += 50;
        } else if (myDetailsAgreed && otherDetailsSubmitted) {
            otherAgreementStatus.setText("You have agreed to these details");
            otherAgreementStatus.setForeground(successColor);
            otherAgreementStatus.setBounds(100, fieldY, 300, 25);
            otherDetailsPanel.add(otherAgreementStatus);
        } else if (otherDetailsSubmitted) {
            JLabel waitLabel = new JLabel("Waiting for you to agree...");
            waitLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            waitLabel.setForeground(warningColor);
            waitLabel.setBounds(100, fieldY, 300, 25);
            otherDetailsPanel.add(waitLabel);
        }
    }

    private void agreeToOtherDetails() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Confirm that you agree with " + otherTraderName + "'s exchange details?\n\n"
            + "This means you have reviewed and confirmed:\n"
            + "Their exchange details are acceptable\n\n"
            + "Both traders must agree to proceed to payment.",
            "Confirm Agreement",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_trade SET my_details_agreed = 1 WHERE trade_id = ?";
            db.updateRecord(sql, tradeId);
            myDetailsAgreed = true;
            
            String checkSql = "SELECT my_details_agreed, other_details_agreed FROM tbl_trade WHERE trade_id = ?";
            List<Map<String, Object>> result = db.fetchRecords(checkSql, tradeId);
            
            if (!result.isEmpty()) {
                int myAgreed = Integer.parseInt(result.get(0).get("my_details_agreed").toString());
                int otherAgreed = Integer.parseInt(result.get(0).get("other_details_agreed").toString());
                
                if (myAgreed == 1 && otherAgreed == 1) {
                    showFeePayerDialog();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "You have marked your agreement.\n\nWaiting for " + otherTraderName + " to agree.",
                        "Agreement Recorded",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
            
            loadTradeState();
            updateUI();
        }
    }
    
    private void declineOtherDetails() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Decline " + otherTraderName + "'s exchange details?\n\n"
            + "If you decline, you can request changes to their details.\n"
            + "Both traders must agree to proceed.",
            "Decline Details",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_trade SET my_details_agreed = 0 WHERE trade_id = ?";
            db.updateRecord(sql, tradeId);
            myDetailsAgreed = false;
            
            JOptionPane.showMessageDialog(this,
                "You have declined the details.\n\n"
                + "Please communicate with " + otherTraderName + " to make necessary changes.\n"
                + "Both traders must agree again to proceed.",
                "Declined",
                JOptionPane.INFORMATION_MESSAGE);
            
            loadTradeState();
            updateUI();
        }
    }

    private void submitDetails() {
        boolean isValid = true;
        
        if (exchangeMethod != null && exchangeMethod.equals("delivery")) {
            if (deliveryAddressField.getText().trim().isEmpty() ||
                courierField.getText().trim().isEmpty() ||
                expectedDateField.getText().trim().isEmpty()) {
                isValid = false;
                JOptionPane.showMessageDialog(this,
                    "Please fill in all required fields (Address, Courier, and Expected Date).",
                    "Incomplete Information",
                    JOptionPane.WARNING_MESSAGE);
            }
        } else if (exchangeMethod != null && exchangeMethod.equals("meetup")) {
            if (meetupLocationField.getText().trim().isEmpty() ||
                meetupDateField.getText().trim().isEmpty() ||
                meetupTimeField.getText().trim().isEmpty() ||
                contactPersonField.getText().trim().isEmpty() ||
                contactNumberField.getText().trim().isEmpty()) {
                isValid = false;
                JOptionPane.showMessageDialog(this,
                    "Please fill in all required fields.",
                    "Incomplete Information",
                    JOptionPane.WARNING_MESSAGE);
            }
        }
        
        if (isValid) {
            saveDetailsToDB();
            
            String message = myDetailsSubmitted ? 
                "Details updated successfully!" : 
                "Details submitted successfully!";
            message += "\n\nWaiting for " + otherTraderName + " to submit their details.";
            
            JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
            
            loadTradeState();
            updateUI();
        }
    }

    private void saveDetailsToDB() {
        String googleMapsLink = googleMapsLinkField.getText().trim();
        
        if (exchangeMethod != null && exchangeMethod.equals("delivery")) {
            String sql = "INSERT OR REPLACE INTO tbl_trade_details "
                + "(trade_id, trader_id, exchange_method, delivery_address, courier, "
                + "expected_date, tracking_number, delivery_instructions, submitted_date) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, datetime('now'))";
            
            db.addRecord(sql, tradeId, traderId, exchangeMethod,
                deliveryAddressField.getText().trim(),
                courierField.getText().trim(),
                expectedDateField.getText().trim(),
                trackingField.getText().trim(),
                deliveryInstructionsArea.getText().trim());
        } else if (exchangeMethod != null && exchangeMethod.equals("meetup")) {
            String sql = "INSERT OR REPLACE INTO tbl_trade_details "
                + "(trade_id, trader_id, exchange_method, meetup_location, meetup_date, "
                + "meetup_time, contact_person, contact_number, meetup_instructions, "
                + "google_maps_link, submitted_date) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, datetime('now'))";
            
            db.addRecord(sql, tradeId, traderId, exchangeMethod,
                meetupLocationField.getText().trim(),
                meetupDateField.getText().trim(),
                meetupTimeField.getText().trim(),
                contactPersonField.getText().trim(),
                contactNumberField.getText().trim(),
                meetupInstructionsArea.getText().trim(),
                googleMapsLink);
        }
        
        if (!myDetailsSubmitted) {
            String updateSql = "UPDATE tbl_trade SET my_details_submitted = 1 WHERE trade_id = ?";
            db.updateRecord(updateSql, tradeId);
            myDetailsSubmitted = true;
        }
        
        myDetailsAgreed = false;
        String resetAgreementSql = "UPDATE tbl_trade SET my_details_agreed = 0 WHERE trade_id = ?";
        db.updateRecord(resetAgreementSql, tradeId);
        
        loadMyDetailsFromDB();
        loadOtherDetailsFromDB();
    }

    private void showFeePayerDialog() {
        Object[] options = {"I will pay the fee", "Let them pay the fee"};
        int choice = JOptionPane.showOptionDialog(this,
            "Who will pay the admin fee of 15.00?\n\n"
            + "If you pay the fee: You will pay 215 total\n"
            + "If they pay the fee: You will pay 200 total\n\n"
            + "Select your preference:",
            "Fee Payer Selection",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);
        
        if (choice == 0) {
            feePayer = true;
        } else if (choice == 1) {
            feePayer = false;
        }
        
        String sql = "UPDATE tbl_trade SET fee_payer_id = ? WHERE trade_id = ?";
        db.updateRecord(sql, feePayer ? traderId : otherTraderId, tradeId);
        
        JOptionPane.showMessageDialog(this,
            "Fee payer selected!\n\n"
            + "You will pay: " + (feePayer ? "215.00" : "200.00") + "\n"
            + "Please proceed to Step 3 to submit your payment.",
            "Fee Payer Set",
            JOptionPane.INFORMATION_MESSAGE);
        
        loadTradeState();
        updateUI();
    }

    private void showStep3Payment() {
        statusLabel.setText("Step 3: Make payment to the admin escrow account.");
        
        int y = 15;
        
        JPanel paymentPanel = new JPanel();
        paymentPanel.setLayout(null);
        paymentPanel.setBackground(Color.WHITE);
        paymentPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor), "Payment Information"));
        paymentPanel.setBounds(20, y, 900, 600);
        stepPanel.add(paymentPanel);
        
        int py = 25;
        
        JLabel paymentTitle = new JLabel("PAYMENT SUMMARY");
        paymentTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        paymentTitle.setForeground(accentColor);
        paymentTitle.setBounds(20, py, 300, 25);
        paymentPanel.add(paymentTitle);
        py += 35;
        
        baseAmountLabel.setText("Item Value: " + String.format("%.2f", baseAmount));
        baseAmountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        baseAmountLabel.setBounds(20, py, 200, 25);
        paymentPanel.add(baseAmountLabel);
        py += 30;
        
        feeAmountLabel.setText("Admin Fee: " + String.format("%.2f", feeAmount));
        feeAmountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        feeAmountLabel.setBounds(20, py, 200, 25);
        paymentPanel.add(feeAmountLabel);
        py += 30;
        
        feePayerLabel.setText("Fee Payer: " + (feePayer ? "YOU" : otherTraderName));
        feePayerLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        feePayerLabel.setForeground(feePayer ? errorColor : warningColor);
        feePayerLabel.setBounds(20, py, 300, 25);
        paymentPanel.add(feePayerLabel);
        py += 35;
        
        totalAmountLabel.setText("TOTAL AMOUNT TO PAY: " + String.format("%.2f", totalAmount));
        totalAmountLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalAmountLabel.setForeground(successColor);
        totalAmountLabel.setBounds(20, py, 400, 30);
        paymentPanel.add(totalAmountLabel);
        py += 45;
        
        JLabel detailsTitle = new JLabel("YOUR PAYMENT DETAILS (For Refund)");
        detailsTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        detailsTitle.setForeground(accentColor);
        detailsTitle.setBounds(20, py, 300, 25);
        paymentPanel.add(detailsTitle);
        py += 35;
        
        JLabel methodLabel = new JLabel("Payment Method:*");
        methodLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        methodLabel.setBounds(20, py, 120, 25);
        paymentPanel.add(methodLabel);
        paymentMethodCombo.setBounds(150, py, 200, 30);
        paymentPanel.add(paymentMethodCombo);
        py += 40;
        
        JLabel numberLabel = new JLabel("Account Number:*");
        numberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        numberLabel.setBounds(20, py, 120, 25);
        paymentPanel.add(numberLabel);
        accountNumberField.setBounds(150, py, 250, 30);
        paymentPanel.add(accountNumberField);
        py += 40;
        
        JLabel nameLabel = new JLabel("Registered Name:*");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nameLabel.setBounds(20, py, 120, 25);
        paymentPanel.add(nameLabel);
        accountNameField.setBounds(150, py, 250, 30);
        paymentPanel.add(accountNameField);
        py += 45;
        
        uploadScreenshotButton.setBounds(20, py, 150, 35);
        paymentPanel.add(uploadScreenshotButton);
        screenshotFileNameLabel.setBounds(180, py, 400, 35);
        paymentPanel.add(screenshotFileNameLabel);
        py += 50;
        
        JTextArea warningArea = new JTextArea();
        warningArea.setFont(new Font("Segoe UI", Font.BOLD, 11));
        warningArea.setForeground(errorColor);
        warningArea.setBackground(new Color(255, 240, 240));
        warningArea.setLineWrap(true);
        warningArea.setWrapStyleWord(true);
        warningArea.setEditable(false);
        warningArea.setText("IMPORTANT: Money is NON-REFUNDABLE if sent to wrong number!\n"
                          + "Double-check your payment details before submitting\n"
                          + "Verify the number is correct and active\n"
                          + "Make sure the name matches your account");
        warningArea.setBounds(20, py, 860, 60);
        warningArea.setBorder(new LineBorder(errorColor, 1));
        paymentPanel.add(warningArea);
        py += 70;
        
        paymentStatusLabel.setBounds(20, py, 500, 25);
        
        if (myPaymentSubmitted && otherPaymentSubmitted) {
            if (paymentVerified) {
                paymentStatusLabel.setText("Payment verified by admin! You can proceed.");
                paymentStatusLabel.setForeground(successColor);
                proceedButton.setEnabled(true);
                proceedButton.setText("PROCEED TO NEXT STEP");
            } else {
                paymentStatusLabel.setText("Both payments submitted. Waiting for admin verification...");
                paymentStatusLabel.setForeground(warningColor);
                proceedButton.setEnabled(false);
            }
        } else if (myPaymentSubmitted) {
            paymentStatusLabel.setText("Your payment submitted. Waiting for " + otherTraderName + ".");
            paymentStatusLabel.setForeground(warningColor);
            proceedButton.setEnabled(false);
        } else {
            paymentStatusLabel.setText("Please provide your payment details and upload screenshot.");
            paymentStatusLabel.setForeground(textColor);
            proceedButton.setEnabled(true);
            proceedButton.setText("SUBMIT PAYMENT");
        }
        
        paymentPanel.add(paymentStatusLabel);
        
        loadPaymentDetails();
    }

    private void loadPaymentDetails() {
        String sql = "SELECT my_payment_screenshot, my_payment_details FROM tbl_trade WHERE trade_id = ?";
        List<Map<String, Object>> result = db.fetchRecords(sql, tradeId);
        
        if (!result.isEmpty() && result.get(0).get("my_payment_details") != null) {
            String details = result.get(0).get("my_payment_details").toString();
            if (details.contains("GCash") || details.contains("PayMaya")) {
                String[] parts = details.split(": ");
                if (parts.length >= 2) {
                    String[] numberName = parts[1].split(" \\(");
                    if (numberName.length >= 1) {
                        accountNumberField.setText(numberName[0]);
                        if (numberName.length >= 2) {
                            accountNameField.setText(numberName[1].replace(")", ""));
                        }
                    }
                }
                if (details.contains("GCash")) paymentMethodCombo.setSelectedItem("GCash");
                else if (details.contains("PayMaya")) paymentMethodCombo.setSelectedItem("PayMaya");
            }
            
            if (result.get(0).get("my_payment_screenshot") != null) {
                screenshotFileNameLabel.setText("Screenshot uploaded");
                myPaymentSubmitted = true;
            }
        }
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
        refundPanel.setBounds(20, y, 900, 200);
        stepPanel.add(refundPanel);
        
        JLabel refundLabel = new JLabel("REFUND PROCESSING");
        refundLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        refundLabel.setForeground(accentColor);
        refundLabel.setBounds(20, 20, 300, 25);
        refundPanel.add(refundLabel);
        
        JLabel refundInfo = new JLabel(
            "<html>Both traders have confirmed receipt.<br>"
            + "The base amount of " + String.format("%.2f", baseAmount) + " will be refunded to both parties.<br>"
            + "The fee of " + String.format("%.2f", feeAmount) + " is retained by BarterZone.</html>");
        refundInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        refundInfo.setBounds(20, 55, 600, 60);
        refundPanel.add(refundInfo);
        
        JLabel refundStatus = new JLabel();
        refundStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refundStatus.setBounds(20, 125, 400, 25);
        
        if (refundProcessed) {
            refundStatus.setText("Refund has been processed by admin!");
            refundStatus.setForeground(successColor);
            proceedButton.setEnabled(true);
            proceedButton.setText("COMPLETE TRADE");
        } else {
            refundStatus.setText("Waiting for admin to process refund...");
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

    private void uploadScreenshot() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            uploadedScreenshotPath = saveScreenshot(selectedFile.getAbsolutePath());
            screenshotFileNameLabel.setText(selectedFile.getName());
            JOptionPane.showMessageDialog(this,
                "Screenshot uploaded successfully!\n\nAdmin will verify your payment.",
                "Upload Complete",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private String saveScreenshot(String sourcePath) {
        try {
            File directory = new File(SCREENSHOT_PATH);
            if (!directory.exists()) directory.mkdirs();
            
            File sourceFile = new File(sourcePath);
            String fileName = "payment_" + traderId + "_" + tradeId + "_" + System.currentTimeMillis() 
                + sourceFile.getName().substring(sourceFile.getName().lastIndexOf("."));
            String destPath = SCREENSHOT_PATH + fileName;
            
            Files.copy(Paths.get(sourcePath), Paths.get(destPath), StandardCopyOption.REPLACE_EXISTING);
            return "BarterZone.resources.images.payment." + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
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
                    submitPayment();
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

    private void submitPayment() {
        if (paymentMethodCombo.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a payment method.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String number = accountNumberField.getText().trim();
        if (number.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter account number.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String accName = accountNameField.getText().trim();
        if (accName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter registered name.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (uploadedScreenshotPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please upload payment screenshot.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Submit payment?\n\n"
            + "Method: " + paymentMethodCombo.getSelectedItem() + "\n"
            + "Number: " + number + "\n"
            + "Name: " + accName + "\n"
            + "Amount: " + String.format("%.2f", totalAmount) + "\n\n"
            + "This information will be used for refunds.\n"
            + "Double-check that all details are correct!\n"
            + "Money is non-refundable if sent to wrong number.",
            "Confirm Payment",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String paymentDetails = paymentMethodCombo.getSelectedItem() + ": " + number + " (" + accName + ")";
            
            String sql = "UPDATE tbl_trade SET my_payment_submitted = 1, "
                + "my_payment_details = ?, my_payment_screenshot = ? WHERE trade_id = ?";
            db.updateRecord(sql, paymentDetails, uploadedScreenshotPath, tradeId);
            
            myPaymentSubmitted = true;
            
            JOptionPane.showMessageDialog(this,
                "Payment submitted successfully!\n\n"
                + "Admin will verify your payment.\n"
                + "You will be notified once verified.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            loadTradeState();
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

    private void proceedToNext() {
        if (currentStep < 6) {
            currentStep++;
            updateUI();
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