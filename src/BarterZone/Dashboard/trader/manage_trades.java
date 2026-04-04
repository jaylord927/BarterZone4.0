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
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

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
    
    private boolean myPaymentSubmitted = false;
    private boolean otherPaymentSubmitted = false;
    private boolean myPaymentVerified = false;
    private boolean otherPaymentVerified = false;
    
    private boolean myItemReceived = false;
    private boolean otherItemReceived = false;
    private boolean refundProcessed = false;
    private boolean tradeCompleted = false;
    
    // Refund details
    private boolean myRefundSubmitted = false;
    private boolean otherRefundSubmitted = false;
    private boolean myRefundConfirmed = false;
    private boolean otherRefundConfirmed = false;
    private int myRefundId = -1;
    private int otherRefundId = -1;
    
    // Step 2 handler
    private step2_submit step2Handler;
    
    // Step 3 Payment Components
    private step3_submit step3SubmitHandler;
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
    private JLabel qrCodeLabel;
    private ImageIcon qrCodeIcon;
    private String uploadedProofPath = "";
    private String otherUploadedProofPath = "";
    private int selectedMethodId = -1;
    private double currentServiceFee = 0;
    private double currentTotalAmount = 0;
    
    // Step 5 Refund Components
    private JPanel refundDetailsPanel;
    private JButton addRefundDetailsButton;
    private JLabel myRefundStatusLabel;
    private JLabel otherRefundStatusLabel;
    private JLabel myRefundNumberLabel;
    private JLabel myRefundNameLabel;
    private JLabel otherRefundNumberLabel;
    private JLabel otherRefundNameLabel;
    private JButton viewRefundProofButton;
    private JButton confirmRefundButton;
    private JLabel refundOverallStatusLabel;
    
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
    
    // Step 4 Components
    private JCheckBox confirmReceivedCheck;
    private JLabel myReceiptStatusLabel;
    private JLabel otherReceiptStatusLabel;
    private JLabel receiptInfoLabel;
    
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
    
    private static final String PROOF_IMAGE_PATH = "src/BarterZone/resources/images/payment_proofs/";
    private static final String REFUND_QR_PATH = "src/BarterZone/resources/images/refund_qrcodes/";

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
        
        createDirectories();
        initComponents();
        loadTradeState();
        updateUI();
        
        setTitle("Manage Trade - Trade #" + tradeId);
        setSize(1000, 800);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }
    
    private void createDirectories() {
        new File(PROOF_IMAGE_PATH).mkdirs();
        new File(REFUND_QR_PATH).mkdirs();
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
        
        // Step 3 Payment Components
        paymentMethodCombo = new JComboBox<>();
        paymentMethodCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        paymentMethodCombo.addActionListener(e -> loadPaymentMethodDetails());
        
        serviceFeeLabel = new JLabel();
        serviceFeeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        totalAmountLabel = new JLabel();
        totalAmountLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        paymentMethodDetailLabel = new JLabel();
        paymentMethodDetailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        accountNumberLabel = new JLabel();
        accountNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        accountNameLabel = new JLabel();
        accountNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        qrCodeLabel = new JLabel();
        qrCodeLabel.setHorizontalAlignment(JLabel.CENTER);
        qrCodeLabel.setVerticalAlignment(JLabel.CENTER);
        
        myPaymentStatusLabel = new JLabel();
        myPaymentStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        otherPaymentStatusLabel = new JLabel();
        otherPaymentStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        paymentStatusLabel = new JLabel();
        paymentStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        paymentNumberValueLabel = new JLabel();
        paymentNumberValueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        accountNameValueLabel = new JLabel();
        accountNameValueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        otherPaymentNumberValueLabel = new JLabel();
        otherPaymentNumberValueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        otherAccountNameValueLabel = new JLabel();
        otherAccountNameValueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Step 4 Components
        confirmReceivedCheck = new JCheckBox("I have received the item");
        confirmReceivedCheck.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        confirmReceivedCheck.setBackground(Color.WHITE);
        
        myReceiptStatusLabel = new JLabel();
        myReceiptStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        otherReceiptStatusLabel = new JLabel();
        otherReceiptStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        receiptInfoLabel = new JLabel();
        receiptInfoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        
        // Step 5 Refund Components
        refundDetailsPanel = new JPanel();
        refundDetailsPanel.setLayout(null);
        refundDetailsPanel.setBackground(Color.WHITE);
        
        addRefundDetailsButton = new JButton("ADD REFUND DETAILS");
        addRefundDetailsButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addRefundDetailsButton.setBackground(themeColor);
        addRefundDetailsButton.setForeground(Color.WHITE);
        addRefundDetailsButton.setBorder(null);
        addRefundDetailsButton.setFocusPainted(false);
        addRefundDetailsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        myRefundStatusLabel = new JLabel();
        myRefundStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        otherRefundStatusLabel = new JLabel();
        otherRefundStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        myRefundNumberLabel = new JLabel();
        myRefundNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        myRefundNameLabel = new JLabel();
        myRefundNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        otherRefundNumberLabel = new JLabel();
        otherRefundNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        otherRefundNameLabel = new JLabel();
        otherRefundNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        viewRefundProofButton = new JButton("View Refund Proof");
        viewRefundProofButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        viewRefundProofButton.setBackground(accentColor);
        viewRefundProofButton.setForeground(Color.WHITE);
        viewRefundProofButton.setBorder(null);
        viewRefundProofButton.setFocusPainted(false);
        viewRefundProofButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewRefundProofButton.setEnabled(false);
        
        confirmRefundButton = new JButton("Mark as Refunded");
        confirmRefundButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        confirmRefundButton.setBackground(successColor);
        confirmRefundButton.setForeground(Color.WHITE);
        confirmRefundButton.setBorder(null);
        confirmRefundButton.setFocusPainted(false);
        confirmRefundButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmRefundButton.setEnabled(false);
        
        refundOverallStatusLabel = new JLabel();
        refundOverallStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
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
        loadPaymentStatus();
        loadRefundStatus();
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
    
    private void loadPaymentStatus() {
        // Load my payment status from tbl_payment_details
        String mySql = "SELECT payment_submitted, payment_verified, my_number, acc_name, payment_proof FROM tbl_payment_details WHERE trade_id = ? AND trader_id = ?";
        List<Map<String, Object>> myPayment = db.fetchRecords(mySql, tradeId, traderId);
        if (!myPayment.isEmpty()) {
            myPaymentSubmitted = Integer.parseInt(myPayment.get(0).get("payment_submitted").toString()) == 1;
            myPaymentVerified = Integer.parseInt(myPayment.get(0).get("payment_verified").toString()) == 1;
            String myNumber = myPayment.get(0).get("my_number") != null ? myPayment.get(0).get("my_number").toString() : "";
            String accName = myPayment.get(0).get("acc_name") != null ? myPayment.get(0).get("acc_name").toString() : "";
            uploadedProofPath = myPayment.get(0).get("payment_proof") != null ? myPayment.get(0).get("payment_proof").toString() : "";
            
            if (paymentNumberValueLabel != null) {
                paymentNumberValueLabel.setText(myNumber.isEmpty() ? "-" : myNumber);
            }
            if (accountNameValueLabel != null) {
                accountNameValueLabel.setText(accName.isEmpty() ? "-" : accName);
            }
        } else {
            myPaymentSubmitted = false;
            myPaymentVerified = false;
            uploadedProofPath = "";
            if (paymentNumberValueLabel != null) paymentNumberValueLabel.setText("-");
            if (accountNameValueLabel != null) accountNameValueLabel.setText("-");
        }
        
        // Load other trader's payment status
        List<Map<String, Object>> otherPayment = db.fetchRecords(mySql, tradeId, otherTraderId);
        if (!otherPayment.isEmpty()) {
            otherPaymentSubmitted = Integer.parseInt(otherPayment.get(0).get("payment_submitted").toString()) == 1;
            otherPaymentVerified = Integer.parseInt(otherPayment.get(0).get("payment_verified").toString()) == 1;
            String otherNumber = otherPayment.get(0).get("my_number") != null ? otherPayment.get(0).get("my_number").toString() : "";
            String otherAccName = otherPayment.get(0).get("acc_name") != null ? otherPayment.get(0).get("acc_name").toString() : "";
            otherUploadedProofPath = otherPayment.get(0).get("payment_proof") != null ? otherPayment.get(0).get("payment_proof").toString() : "";
            
            if (otherPaymentNumberValueLabel != null) {
                otherPaymentNumberValueLabel.setText(otherNumber.isEmpty() ? "-" : otherNumber);
            }
            if (otherAccountNameValueLabel != null) {
                otherAccountNameValueLabel.setText(otherAccName.isEmpty() ? "-" : otherAccName);
            }
        } else {
            otherPaymentSubmitted = false;
            otherPaymentVerified = false;
            otherUploadedProofPath = "";
            if (otherPaymentNumberValueLabel != null) otherPaymentNumberValueLabel.setText("-");
            if (otherAccountNameValueLabel != null) otherAccountNameValueLabel.setText("-");
        }
    }
    
    private void loadRefundStatus() {
        String sql = "SELECT refund_id, account_number, account_name, qr_code_path, refund_proof, is_refunded FROM tbl_refund WHERE trade_id = ? AND user_id = ?";
        
        // Load my refund details
        List<Map<String, Object>> myRefund = db.fetchRecords(sql, tradeId, traderId);
        if (!myRefund.isEmpty()) {
            myRefundSubmitted = true;
            myRefundId = Integer.parseInt(myRefund.get(0).get("refund_id").toString());
            myRefundNumberLabel.setText(myRefund.get(0).get("account_number") != null ? myRefund.get(0).get("account_number").toString() : "-");
            myRefundNameLabel.setText(myRefund.get(0).get("account_name") != null ? myRefund.get(0).get("account_name").toString() : "-");
            myRefundConfirmed = Integer.parseInt(myRefund.get(0).get("is_refunded").toString()) == 1;
        } else {
            myRefundSubmitted = false;
            myRefundNumberLabel.setText("-");
            myRefundNameLabel.setText("-");
            myRefundConfirmed = false;
        }
        
        // Load other trader's refund details
        List<Map<String, Object>> otherRefund = db.fetchRecords(sql, tradeId, otherTraderId);
        if (!otherRefund.isEmpty()) {
            otherRefundSubmitted = true;
            otherRefundId = Integer.parseInt(otherRefund.get(0).get("refund_id").toString());
            otherRefundNumberLabel.setText(otherRefund.get(0).get("account_number") != null ? otherRefund.get(0).get("account_number").toString() : "-");
            otherRefundNameLabel.setText(otherRefund.get(0).get("account_name") != null ? otherRefund.get(0).get("account_name").toString() : "-");
            otherRefundConfirmed = Integer.parseInt(otherRefund.get(0).get("is_refunded").toString()) == 1;
        } else {
            otherRefundSubmitted = false;
            otherRefundNumberLabel.setText("-");
            otherRefundNameLabel.setText("-");
            otherRefundConfirmed = false;
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
        } else if (myRefundConfirmed && otherRefundConfirmed) {
            currentStep = 5;
        } else if (myItemReceived && otherItemReceived) {
            currentStep = 4;
        } else if (myPaymentVerified && otherPaymentVerified) {
            currentStep = 3;
        } else if (myDetailsSubmitted && otherDetailsSubmitted) {
            boolean canProceed = false;
            
            if (exchangeMethod != null && exchangeMethod.equals("delivery")) {
                canProceed = (myDetailsAgreed == 1 && otherDetailsAgreed == 1);
            } else if (exchangeMethod != null && exchangeMethod.equals("meetup")) {
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
                               "Step 3: Make Payment", "Step 4: Confirm Receipt", "Step 5: Refund", "Step 6: Trade Completed"};
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

    // ========== STEP 1: PROPOSE METHOD (NO CHANGES) ==========
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
            String sql = "UPDATE tbl_trade SET exchange_method = ?, method_confirmed = 1 WHERE trade_id = ?";
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

    // ========== STEP 2: EXCHANGE DETAILS (FIXED SUBMISSION LOGIC) ==========
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
        
        boolean showAgreeButton = false;
        
        if (exchangeMethod != null && exchangeMethod.equals("delivery")) {
            showAgreeButton = (otherDetailsSubmitted && otherDetailsAgreed == 0);
        } else if (exchangeMethod != null && exchangeMethod.equals("meetup")) {
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

    // FIXED: When trader submits details, it updates both my_details_submitted AND other_details_submitted for the other trader
    private void showDetailsInputDialog() {
        if (exchangeMethod == null) {
            JOptionPane.showMessageDialog(this, "Exchange method not set. Please complete Step 1 first.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        step2Handler = new step2_submit(tradeId, traderId, otherTraderId, exchangeMethod, this);
        step2Handler.showDialog();
        
        // After submitting details, update the other trader's other_details_submitted flag
        String updateOtherSql = "UPDATE tbl_trade_details SET other_details_submitted = 1 WHERE trade_id = ? AND trader_id = ?";
        db.updateRecord(updateOtherSql, tradeId, otherTraderId);
        
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
            String sql = "UPDATE tbl_trade_details SET other_details_agreed = 1 WHERE trade_id = ? AND trader_id = ?";
            db.updateRecord(sql, tradeId, traderId);
            
            String sqlOther = "UPDATE tbl_trade_details SET my_details_agreed = 1 WHERE trade_id = ? AND trader_id = ?";
            db.updateRecord(sqlOther, tradeId, otherTraderId);
            
            loadTradeState();
            
            if (exchangeMethod.equals("delivery")) {
                String checkSql = "SELECT my_details_agreed, other_details_agreed FROM tbl_trade_details WHERE trade_id = ?";
                List<Map<String, Object>> results = db.fetchRecords(checkSql, tradeId);
                
                boolean allAgreed = true;
                for (Map<String, Object> result : results) {
                    int myAgreed = Integer.parseInt(result.get("my_details_agreed").toString());
                    int otherAgreed = Integer.parseInt(result.get("other_details_agreed").toString());
                    if (myAgreed != 1 || otherAgreed != 1) {
                        allAgreed = false;
                        break;
                    }
                }
                
                if (allAgreed) {
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
            } else {
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

    // ========== STEP 3: PAYMENT (WITH QR CODE DISPLAY) ==========
    private void showStep3Payment() {
        statusLabel.setText("Step 3: Submit your payment proof. Admin will verify both payments.");
        
        // Load existing payment details
        loadPaymentStatus();
        loadTradePaymentSettings();
        
        int y = 15;
        
        // Payment Information Panel
        JPanel paymentInfoPanel = new JPanel();
        paymentInfoPanel.setLayout(null);
        paymentInfoPanel.setBackground(new Color(255, 255, 200));
        paymentInfoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor), "PAYMENT INFORMATION"));
        paymentInfoPanel.setBounds(20, y, 900, 220);
        stepPanel.add(paymentInfoPanel);
        
        int py = 25;
        
        // Payment Method Selection
        JLabel methodSelectLabel = new JLabel("Select Payment Method:");
        methodSelectLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        methodSelectLabel.setBounds(20, py, 180, 25);
        paymentInfoPanel.add(methodSelectLabel);
        
        paymentMethodCombo.setBounds(200, py, 250, 30);
        paymentInfoPanel.add(paymentMethodCombo);
        py += 45;
        
        // Payment Method Details
        paymentMethodDetailLabel.setBounds(20, py, 400, 25);
        paymentInfoPanel.add(paymentMethodDetailLabel);
        py += 30;
        
        accountNumberLabel.setBounds(20, py, 400, 25);
        paymentInfoPanel.add(accountNumberLabel);
        py += 30;
        
        accountNameLabel.setBounds(20, py, 400, 25);
        paymentInfoPanel.add(accountNameLabel);
        py += 30;
        
        // QR Code Display
        JLabel qrTitleLabel = new JLabel("QR Code:");
        qrTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        qrTitleLabel.setBounds(20, py, 100, 25);
        paymentInfoPanel.add(qrTitleLabel);
        
        qrCodeLabel.setBounds(130, py, 100, 100);
        qrCodeLabel.setBorder(new LineBorder(new Color(200, 200, 200)));
        paymentInfoPanel.add(qrCodeLabel);
        py += 110;
        
        // Fee and Amount
        serviceFeeLabel.setBounds(20, py, 300, 25);
        paymentInfoPanel.add(serviceFeeLabel);
        
        totalAmountLabel.setBounds(350, py, 300, 25);
        paymentInfoPanel.add(totalAmountLabel);
        py += 50;
        
        // Add Payment Proof Button Section
        JPanel proofPanel = new JPanel();
        proofPanel.setLayout(null);
        proofPanel.setBackground(Color.WHITE);
        proofPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor), "YOUR PAYMENT PROOF"));
        proofPanel.setBounds(20, y + 235, 440, 200);
        stepPanel.add(proofPanel);
        
        int ppy = 25;
        
        JButton addProofButton = new JButton("ADD PAYMENT PROOF");
        addProofButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addProofButton.setBackground(themeColor);
        addProofButton.setForeground(Color.WHITE);
        addProofButton.setBounds(20, ppy, 180, 35);
        addProofButton.setBorder(null);
        addProofButton.setFocusPainted(false);
        addProofButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addProofButton.addActionListener(e -> openStep3SubmitDialog());
        proofPanel.add(addProofButton);
        ppy += 50;
        
        // Display saved payment details
        JLabel paymentNumberDisplayLabel = new JLabel("Payment Number:");
        paymentNumberDisplayLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        paymentNumberDisplayLabel.setBounds(20, ppy, 120, 25);
        proofPanel.add(paymentNumberDisplayLabel);
        
        paymentNumberValueLabel.setBounds(150, ppy, 250, 25);
        proofPanel.add(paymentNumberValueLabel);
        ppy += 30;
        
        JLabel accountNameDisplayLabel = new JLabel("Account Name:");
        accountNameDisplayLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        accountNameDisplayLabel.setBounds(20, ppy, 120, 25);
        proofPanel.add(accountNameDisplayLabel);
        
        accountNameValueLabel.setBounds(150, ppy, 250, 25);
        proofPanel.add(accountNameValueLabel);
        ppy += 30;
        
        JLabel proofStatusLabel = new JLabel("Payment Status:");
        proofStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        proofStatusLabel.setBounds(20, ppy, 120, 25);
        proofPanel.add(proofStatusLabel);
        
        myPaymentStatusLabel.setBounds(150, ppy, 250, 25);
        if (myPaymentVerified) {
            myPaymentStatusLabel.setText("✓ VERIFIED BY ADMIN");
            myPaymentStatusLabel.setForeground(successColor);
        } else if (myPaymentSubmitted) {
            myPaymentStatusLabel.setText("⏳ Submitted - Waiting for admin verification");
            myPaymentStatusLabel.setForeground(warningColor);
        } else {
            myPaymentStatusLabel.setText("❌ Not submitted yet");
            myPaymentStatusLabel.setForeground(errorColor);
        }
        proofPanel.add(myPaymentStatusLabel);
        ppy += 35;
        
        // View uploaded proof button
        if (myPaymentSubmitted && !uploadedProofPath.isEmpty()) {
            JButton viewProofButton = new JButton("View Uploaded Proof");
            viewProofButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
            viewProofButton.setBackground(accentColor);
            viewProofButton.setForeground(Color.WHITE);
            viewProofButton.setBounds(20, ppy, 180, 30);
            viewProofButton.setBorder(null);
            viewProofButton.setFocusPainted(false);
            viewProofButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            final String proofPath = uploadedProofPath;
            viewProofButton.addActionListener(e -> viewUploadedProof(proofPath));
            proofPanel.add(viewProofButton);
        }
        
        // Other Trader Payment Status
        JPanel otherPaymentPanel = new JPanel();
        otherPaymentPanel.setLayout(null);
        otherPaymentPanel.setBackground(Color.WHITE);
        otherPaymentPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor), otherTraderName + "'S PAYMENT STATUS"));
        otherPaymentPanel.setBounds(480, y + 235, 440, 200);
        stepPanel.add(otherPaymentPanel);
        
        int opy = 25;
        
        JLabel otherPaymentNumberLabel = new JLabel("Payment Number:");
        otherPaymentNumberLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        otherPaymentNumberLabel.setBounds(20, opy, 120, 25);
        otherPaymentPanel.add(otherPaymentNumberLabel);
        
        otherPaymentNumberValueLabel.setBounds(150, opy, 250, 25);
        otherPaymentPanel.add(otherPaymentNumberValueLabel);
        opy += 30;
        
        JLabel otherAccountNameLabel = new JLabel("Account Name:");
        otherAccountNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        otherAccountNameLabel.setBounds(20, opy, 120, 25);
        otherPaymentPanel.add(otherAccountNameLabel);
        
        otherAccountNameValueLabel.setBounds(150, opy, 250, 25);
        otherPaymentPanel.add(otherAccountNameValueLabel);
        opy += 30;
        
        JLabel otherProofStatusLabel = new JLabel("Payment Status:");
        otherProofStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        otherProofStatusLabel.setBounds(20, opy, 120, 25);
        otherPaymentPanel.add(otherProofStatusLabel);
        
        otherPaymentStatusLabel.setBounds(150, opy, 250, 25);
        if (otherPaymentVerified) {
            otherPaymentStatusLabel.setText("✓ VERIFIED BY ADMIN");
            otherPaymentStatusLabel.setForeground(successColor);
        } else if (otherPaymentSubmitted) {
            otherPaymentStatusLabel.setText("⏳ Submitted - Waiting for admin verification");
            otherPaymentStatusLabel.setForeground(warningColor);
        } else {
            otherPaymentStatusLabel.setText("❌ Not submitted yet");
            otherPaymentStatusLabel.setForeground(errorColor);
        }
        otherPaymentPanel.add(otherPaymentStatusLabel);
        opy += 35;
        
        // View other trader's proof button
        if (otherPaymentSubmitted && !otherUploadedProofPath.isEmpty()) {
            JButton viewOtherProofButton = new JButton("View Their Proof");
            viewOtherProofButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
            viewOtherProofButton.setBackground(accentColor);
            viewOtherProofButton.setForeground(Color.WHITE);
            viewOtherProofButton.setBounds(20, opy, 180, 30);
            viewOtherProofButton.setBorder(null);
            viewOtherProofButton.setFocusPainted(false);
            viewOtherProofButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            final String otherProofPath = otherUploadedProofPath;
            viewOtherProofButton.addActionListener(e -> viewUploadedProof(otherProofPath));
            otherPaymentPanel.add(viewOtherProofButton);
        }
        
        // Note
        JLabel noteLabel = new JLabel("Note: Both traders must submit payment proof. Admin will verify each payment.");
        noteLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        noteLabel.setForeground(infoColor);
        noteLabel.setBounds(20, 170, 400, 25);
        otherPaymentPanel.add(noteLabel);
        
        // Overall Payment Status
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(null);
        statusPanel.setBackground(Color.WHITE);
        statusPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor), "PAYMENT STATUS"));
        statusPanel.setBounds(20, y + 450, 900, 80);
        stepPanel.add(statusPanel);
        
        paymentStatusLabel.setBounds(20, 30, 860, 30);
        
        if (myPaymentVerified && otherPaymentVerified) {
            paymentStatusLabel.setText("✓ BOTH PAYMENTS VERIFIED! You can now proceed to Step 4.");
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
        
        // Load payment methods for dropdown
        loadActivePaymentMethods();
    }
    
    private void loadTradePaymentSettings() {
        String feeSql = "SELECT service_fee, total_amount, method_id FROM tbl_payment_details WHERE trade_id = ? AND trader_id = ? LIMIT 1";
        List<Map<String, Object>> feeResult = db.fetchRecords(feeSql, tradeId, traderId);
        
        if (!feeResult.isEmpty()) {
            Map<String, Object> feeData = feeResult.get(0);
            currentServiceFee = feeData.get("service_fee") != null ? Double.parseDouble(feeData.get("service_fee").toString()) : 15.00;
            currentTotalAmount = feeData.get("total_amount") != null ? Double.parseDouble(feeData.get("total_amount").toString()) : 215.00;
            
            serviceFeeLabel.setText("Service Fee: ₱" + String.format("%.2f", currentServiceFee));
            totalAmountLabel.setText("Total Amount: ₱" + String.format("%.2f", currentTotalAmount));
            
            if (feeData.get("method_id") != null) {
                selectedMethodId = Integer.parseInt(feeData.get("method_id").toString());
            }
        } else {
            serviceFeeLabel.setText("Service Fee: ₱15.00");
            totalAmountLabel.setText("Total Amount: ₱215.00");
        }
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
            qrCodeLabel.setIcon(null);
            qrCodeLabel.setText("No QR Code");
            return;
        }
        
        String selected = paymentMethodCombo.getSelectedItem().toString();
        int methodId = Integer.parseInt(selected.substring(0, selected.indexOf(" -")));
        selectedMethodId = methodId;
        
        String sql = "SELECT method_name, account_number, account_name, qr_code_path FROM tbl_payment_methods WHERE method_id = ? AND is_active = 1";
        List<Map<String, Object>> result = db.fetchRecords(sql, methodId);
        
        if (!result.isEmpty()) {
            Map<String, Object> method = result.get(0);
            paymentMethodDetailLabel.setText("Method: " + method.get("method_name"));
            accountNumberLabel.setText("Account Number: " + method.get("account_number"));
            if (method.get("account_name") != null && !method.get("account_name").toString().isEmpty()) {
                accountNameLabel.setText("Account Name: " + method.get("account_name"));
            } else {
                accountNameLabel.setText("");
            }
            
            // Display QR Code if available
            if (method.get("qr_code_path") != null && !method.get("qr_code_path").toString().isEmpty()) {
                String qrPath = method.get("qr_code_path").toString();
                String fullPath = "src/" + qrPath;
                File qrFile = new File(fullPath);
                if (qrFile.exists()) {
                    try {
                        ImageIcon qrIcon = new ImageIcon(fullPath);
                        Image scaledImage = qrIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                        qrCodeLabel.setIcon(new ImageIcon(scaledImage));
                        qrCodeLabel.setText("");
                    } catch (Exception e) {
                        qrCodeLabel.setIcon(null);
                        qrCodeLabel.setText("QR Code Error");
                    }
                } else {
                    qrCodeLabel.setIcon(null);
                    qrCodeLabel.setText("QR Code Not Found");
                }
            } else {
                qrCodeLabel.setIcon(null);
                qrCodeLabel.setText("No QR Code");
            }
        }
    }
    
    private void openStep3SubmitDialog() {
        step3SubmitHandler = new step3_submit(tradeId, traderId, traderName, otherTraderId, this);
        step3SubmitHandler.showDialog();
        
        loadPaymentStatus();
        loadTradeState();
        updateUI();
    }
    
    private void viewUploadedProof(String proofPath) {
        if (proofPath == null || proofPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No proof image available.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try {
            String fullPath = "src/" + proofPath;
            File imgFile = new File(fullPath);
            
            if (imgFile.exists()) {
                ImageIcon icon = new ImageIcon(fullPath);
                Image img = icon.getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH);
                JOptionPane.showMessageDialog(this, new JLabel(new ImageIcon(img)), "Payment Proof", JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Proof image not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ========== STEP 4: CONFIRM RECEIPT (UPDATED WITH PROPOSED_BY LOGIC) ==========
    private void showStep4ConfirmReceipt() {
        statusLabel.setText("Step 4: Confirm when you receive the item.");
        
        int y = 50;
        
        JPanel receiptPanel = new JPanel();
        receiptPanel.setLayout(null);
        receiptPanel.setBackground(Color.WHITE);
        receiptPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor), "Item Receipt Confirmation"));
        receiptPanel.setBounds(20, y, 900, 250);
        stepPanel.add(receiptPanel);
        
        boolean iAmProposer = (proposedBy == traderId);
        String proposerName = iAmProposer ? traderName : otherTraderName;
        String otherName = iAmProposer ? otherTraderName : traderName;
        
        JLabel receiptInfoLabel = new JLabel("<html><b>Trade Information:</b><br>" +
            "Trade initiated by: " + proposerName + "<br>" +
            "Item receipt confirmation required from both traders.</html>");
        receiptInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        receiptInfoLabel.setBounds(20, 20, 500, 60);
        receiptPanel.add(receiptInfoLabel);
        
        JPanel myReceiptPanel = new JPanel();
        myReceiptPanel.setLayout(null);
        myReceiptPanel.setBackground(new Color(250, 250, 250));
        myReceiptPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor), "YOUR RECEIPT STATUS"));
        myReceiptPanel.setBounds(20, 90, 420, 130);
        receiptPanel.add(myReceiptPanel);
        
        int mrp = 20;
        
        JLabel myStatusTitle = new JLabel("Your confirmation:");
        myStatusTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        myStatusTitle.setBounds(15, mrp, 200, 25);
        myReceiptPanel.add(myStatusTitle);
        mrp += 35;
        
        myReceiptStatusLabel.setBounds(15, mrp, 380, 25);
        if (myItemReceived) {
            myReceiptStatusLabel.setText("✓ You have confirmed receipt of the item.");
            myReceiptStatusLabel.setForeground(successColor);
        } else {
            myReceiptStatusLabel.setText("❌ You have NOT confirmed receipt yet.");
            myReceiptStatusLabel.setForeground(errorColor);
        }
        myReceiptPanel.add(myReceiptStatusLabel);
        mrp += 40;
        
        confirmReceivedCheck.setBounds(15, mrp, 250, 30);
        myReceiptPanel.add(confirmReceivedCheck);
        
        JPanel otherReceiptPanel = new JPanel();
        otherReceiptPanel.setLayout(null);
        otherReceiptPanel.setBackground(new Color(250, 250, 250));
        otherReceiptPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor), otherTraderName + "'S RECEIPT STATUS"));
        otherReceiptPanel.setBounds(460, 90, 420, 130);
        receiptPanel.add(otherReceiptPanel);
        
        int orp = 20;
        
        JLabel otherStatusTitle = new JLabel(otherTraderName + "'s confirmation:");
        otherStatusTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        otherStatusTitle.setBounds(15, orp, 300, 25);
        otherReceiptPanel.add(otherStatusTitle);
        orp += 35;
        
        otherReceiptStatusLabel.setBounds(15, orp, 380, 25);
        if (otherItemReceived) {
            otherReceiptStatusLabel.setText("✓ " + otherTraderName + " has confirmed receipt of the item.");
            otherReceiptStatusLabel.setForeground(successColor);
        } else {
            otherReceiptStatusLabel.setText("❌ " + otherTraderName + " has NOT confirmed receipt yet.");
            otherReceiptStatusLabel.setForeground(errorColor);
        }
        otherReceiptPanel.add(otherReceiptStatusLabel);
        
        JButton confirmButton = new JButton("CONFIRM RECEIPT");
        confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        confirmButton.setBackground(successColor);
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setBounds(20, 235, 150, 35);
        confirmButton.setBorder(null);
        confirmButton.setFocusPainted(false);
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmButton.addActionListener(e -> confirmReceipt());
        receiptPanel.add(confirmButton);
        
        JLabel bothStatus = new JLabel();
        bothStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        bothStatus.setBounds(200, 240, 500, 25);
        
        if (myItemReceived && otherItemReceived) {
            bothStatus.setText("✓ Both traders have received items! Click PROCEED to continue.");
            bothStatus.setForeground(successColor);
            proceedButton.setEnabled(true);
            proceedButton.setText("PROCEED TO REFUND");
        } else if (myItemReceived) {
            bothStatus.setText("You have received the item. Waiting for " + otherTraderName + " to confirm.");
            bothStatus.setForeground(warningColor);
            proceedButton.setEnabled(false);
        } else if (otherItemReceived) {
            bothStatus.setText(otherTraderName + " has received the item. Waiting for you to confirm.");
            bothStatus.setForeground(warningColor);
            proceedButton.setEnabled(false);
        } else {
            bothStatus.setText("Waiting for both traders to confirm receipt.");
            bothStatus.setForeground(textColor);
            proceedButton.setEnabled(false);
        }
        
        receiptPanel.add(bothStatus);
    }

    private void confirmReceipt() {
        if (!confirmReceivedCheck.isSelected()) {
            JOptionPane.showMessageDialog(this,
                "Please check the box to confirm you have received the item.",
                "Confirmation Required",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        boolean iAmProposer = (proposedBy == traderId);
        String columnToUpdate = iAmProposer ? "my_item_received" : "other_item_received";
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Confirm that you have received the item?\n\n"
            + "This action cannot be undone.\n\n"
            + "Note: This will update the trade status for your side.",
            "Confirm Receipt",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_trade SET " + columnToUpdate + " = 1 WHERE trade_id = ?";
            db.updateRecord(sql, tradeId);
            
            if (iAmProposer) {
                myItemReceived = true;
            } else {
                otherItemReceived = true;
            }
            
            String checkSql = "SELECT my_item_received, other_item_received FROM tbl_trade WHERE trade_id = ?";
            List<Map<String, Object>> result = db.fetchRecords(checkSql, tradeId);
            if (!result.isEmpty()) {
                int myReceived = Integer.parseInt(result.get(0).get("my_item_received").toString());
                int otherReceived = Integer.parseInt(result.get(0).get("other_item_received").toString());
                
                if (myReceived == 1 && otherReceived == 1) {
                    String updateSql = "UPDATE tbl_trade SET trade_status = 'items_received' WHERE trade_id = ?";
                    db.updateRecord(updateSql, tradeId);
                }
            }
            
            JOptionPane.showMessageDialog(this,
                "Receipt confirmed! Waiting for " + otherTraderName + ".",
                "Confirmation Recorded",
                JOptionPane.INFORMATION_MESSAGE);
            
            loadTradeState();
            updateUI();
        }
    }

    // ========== STEP 5: REFUND (NEW FEATURE) ==========
    private void showStep5Refund() {
        statusLabel.setText("Step 5: Provide refund details to receive your refund.");
        
        int y = 15;
        
        // Refund Information Panel
        JPanel refundInfoPanel = new JPanel();
        refundInfoPanel.setLayout(null);
        refundInfoPanel.setBackground(new Color(255, 245, 220));
        refundInfoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(warningColor), "REFUND INFORMATION"));
        refundInfoPanel.setBounds(20, y, 900, 100);
        stepPanel.add(refundInfoPanel);
        
        JLabel refundInfoLabel = new JLabel(
            "<html>Admin will process refunds after both traders have confirmed receipt.<br>"
            + "Please provide your refund details below. Once submitted, you cannot edit them.<br>"
            + "After admin sends the refund, you will see the proof and can mark as refunded.</html>");
        refundInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        refundInfoLabel.setBounds(20, 20, 860, 60);
        refundInfoPanel.add(refundInfoLabel);
        
        y += 115;
        
        // My Refund Details Panel
        JPanel myRefundPanel = new JPanel();
        myRefundPanel.setLayout(null);
        myRefundPanel.setBackground(Color.WHITE);
        myRefundPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor), "YOUR REFUND DETAILS"));
        myRefundPanel.setBounds(20, y, 440, 250);
        stepPanel.add(myRefundPanel);
        
        int mry = 25;
        
        addRefundDetailsButton.setBounds(120, mry, 200, 35);
        addRefundDetailsButton.addActionListener(e -> showAddRefundDetailsDialog());
        myRefundPanel.add(addRefundDetailsButton);
        mry += 50;
        
        JLabel refundNumberTitle = new JLabel("Account Number:");
        refundNumberTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refundNumberTitle.setBounds(20, mry, 120, 25);
        myRefundPanel.add(refundNumberTitle);
        
        myRefundNumberLabel.setBounds(150, mry, 250, 25);
        myRefundPanel.add(myRefundNumberLabel);
        mry += 35;
        
        JLabel refundNameTitle = new JLabel("Account Name:");
        refundNameTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refundNameTitle.setBounds(20, mry, 120, 25);
        myRefundPanel.add(refundNameTitle);
        
        myRefundNameLabel.setBounds(150, mry, 250, 25);
        myRefundPanel.add(myRefundNameLabel);
        mry += 40;
        
        myRefundStatusLabel.setBounds(20, mry, 400, 25);
        if (myRefundConfirmed) {
            myRefundStatusLabel.setText("✓ Refund has been processed and confirmed!");
            myRefundStatusLabel.setForeground(successColor);
        } else if (myRefundSubmitted) {
            myRefundStatusLabel.setText("⏳ Refund details submitted. Waiting for admin to process...");
            myRefundStatusLabel.setForeground(warningColor);
        } else {
            myRefundStatusLabel.setText("❌ Refund details not yet submitted");
            myRefundStatusLabel.setForeground(errorColor);
        }
        myRefundPanel.add(myRefundStatusLabel);
        
        // Other Trader Refund Details Panel
        JPanel otherRefundPanel = new JPanel();
        otherRefundPanel.setLayout(null);
        otherRefundPanel.setBackground(Color.WHITE);
        otherRefundPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor), otherTraderName + "'S REFUND DETAILS"));
        otherRefundPanel.setBounds(480, y, 440, 250);
        stepPanel.add(otherRefundPanel);
        
        int ory = 25;
        
        JLabel otherRefundNumberTitle = new JLabel("Account Number:");
        otherRefundNumberTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        otherRefundNumberTitle.setBounds(20, ory, 120, 25);
        otherRefundPanel.add(otherRefundNumberTitle);
        
        otherRefundNumberLabel.setBounds(150, ory, 250, 25);
        otherRefundPanel.add(otherRefundNumberLabel);
        ory += 35;
        
        JLabel otherRefundNameTitle = new JLabel("Account Name:");
        otherRefundNameTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        otherRefundNameTitle.setBounds(20, ory, 120, 25);
        otherRefundPanel.add(otherRefundNameTitle);
        
        otherRefundNameLabel.setBounds(150, ory, 250, 25);
        otherRefundPanel.add(otherRefundNameLabel);
        ory += 40;
        
        otherRefundStatusLabel.setBounds(20, ory, 400, 25);
        if (otherRefundConfirmed) {
            otherRefundStatusLabel.setText("✓ Refund has been processed and confirmed!");
            otherRefundStatusLabel.setForeground(successColor);
        } else if (otherRefundSubmitted) {
            otherRefundStatusLabel.setText("⏳ Refund details submitted. Waiting for admin to process...");
            otherRefundStatusLabel.setForeground(warningColor);
        } else {
            otherRefundStatusLabel.setText("❌ Refund details not yet submitted");
            otherRefundStatusLabel.setForeground(errorColor);
        }
        otherRefundPanel.add(otherRefundStatusLabel);
        ory += 40;
        
        // View Refund Proof Button (only if admin has uploaded proof)
        if (myRefundSubmitted && myRefundConfirmed) {
            viewRefundProofButton.setBounds(20, ory, 180, 30);
            viewRefundProofButton.addActionListener(e -> viewRefundProof());
            otherRefundPanel.add(viewRefundProofButton);
            
            confirmRefundButton.setBounds(210, ory, 150, 30);
            confirmRefundButton.addActionListener(e -> confirmRefund());
            otherRefundPanel.add(confirmRefundButton);
        }
        
        y += 265;
        
        // Overall Refund Status
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(null);
        statusPanel.setBackground(Color.WHITE);
        statusPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor), "REFUND STATUS"));
        statusPanel.setBounds(20, y, 900, 80);
        stepPanel.add(statusPanel);
        
        refundOverallStatusLabel.setBounds(20, 30, 860, 30);
        
        if (myRefundConfirmed && otherRefundConfirmed) {
            refundOverallStatusLabel.setText("✓ BOTH REFUNDS CONFIRMED! Click PROCEED to complete the trade.");
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
    }
    
    private void showAddRefundDetailsDialog() {
        if (myRefundSubmitted) {
            JOptionPane.showMessageDialog(this, "You have already submitted refund details and cannot edit them.", "Already Submitted", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JDialog refundDialog = new JDialog(this, "Add Refund Details", true);
        refundDialog.setSize(500, 400);
        refundDialog.setLayout(null);
        refundDialog.setLocationRelativeTo(this);
        refundDialog.getContentPane().setBackground(Color.WHITE);
        
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(themeColor);
        titlePanel.setBounds(0, 0, 500, 45);
        titlePanel.setLayout(null);
        
        JLabel titleLabel = new JLabel("ADD REFUND DETAILS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(20, 8, 300, 30);
        titlePanel.add(titleLabel);
        refundDialog.add(titlePanel);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBounds(10, 55, 480, 290);
        refundDialog.add(contentPanel);
        
        int y = 20;
        int labelWidth = 120;
        int fieldWidth = 300;
        int fieldX = 150;
        
        JLabel numberLabel = new JLabel("Account Number:*");
        numberLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        numberLabel.setBounds(20, y, labelWidth, 30);
        contentPanel.add(numberLabel);
        
        JTextField accountNumberField = new JTextField();
        accountNumberField.setBounds(fieldX, y, fieldWidth, 35);
        contentPanel.add(accountNumberField);
        y += 55;
        
        JLabel nameLabel = new JLabel("Account Name:*");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLabel.setBounds(20, y, labelWidth, 30);
        contentPanel.add(nameLabel);
        
        JTextField accountNameField = new JTextField();
        accountNameField.setBounds(fieldX, y, fieldWidth, 35);
        contentPanel.add(accountNameField);
        y += 55;
        
        JLabel qrLabel = new JLabel("QR Code (Optional):");
        qrLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        qrLabel.setBounds(20, y, labelWidth, 30);
        contentPanel.add(qrLabel);
        
        JButton uploadQrButton = new JButton("Upload QR Code");
        uploadQrButton.setBounds(fieldX, y, 150, 35);
        uploadQrButton.setBackground(themeColor);
        uploadQrButton.setForeground(Color.WHITE);
        uploadQrButton.setBorder(null);
        uploadQrButton.setFocusPainted(false);
        uploadQrButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPanel.add(uploadQrButton);
        
        JLabel qrFileNameLabel = new JLabel();
        qrFileNameLabel.setBounds(fieldX + 160, y, 200, 35);
        contentPanel.add(qrFileNameLabel);
        
        final String[] uploadedQrPath = {""};
        
        uploadQrButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));
            if (fileChooser.showOpenDialog(refundDialog) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String savedPath = saveRefundQrImage(selectedFile.getAbsolutePath(), selectedFile.getName());
                uploadedQrPath[0] = savedPath;
                qrFileNameLabel.setText(selectedFile.getName());
            }
        });
        
        y += 55;
        
        JButton submitButton = new JButton("SUBMIT REFUND DETAILS");
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitButton.setBackground(successColor);
        submitButton.setForeground(Color.WHITE);
        submitButton.setBounds(150, y, 200, 40);
        submitButton.setBorder(null);
        submitButton.setFocusPainted(false);
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.addActionListener(e -> {
            String accountNumber = accountNumberField.getText().trim();
            String accountName = accountNameField.getText().trim();
            
            if (accountNumber.isEmpty() || accountName.isEmpty()) {
                JOptionPane.showMessageDialog(refundDialog, "Account Number and Account Name are required!", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String sql = "INSERT INTO tbl_refund (trade_id, user_id, account_number, account_name, qr_code_path, created_date) "
                    + "VALUES (?, ?, ?, ?, ?, datetime('now'))";
            db.addRecord(sql, tradeId, traderId, accountNumber, accountName, uploadedQrPath[0]);
            
            JOptionPane.showMessageDialog(refundDialog, "Refund details submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            refundDialog.dispose();
            loadRefundStatus();
            loadTradeState();
            updateUI();
        });
        contentPanel.add(submitButton);
        
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
    
    private void viewRefundProof() {
        String sql = "SELECT refund_proof FROM tbl_refund WHERE trade_id = ? AND user_id = ?";
        List<Map<String, Object>> result = db.fetchRecords(sql, tradeId, traderId);
        
        if (!result.isEmpty() && result.get(0).get("refund_proof") != null) {
            String proofPath = result.get(0).get("refund_proof").toString();
            String fullPath = "src/" + proofPath;
            File imgFile = new File(fullPath);
            
            if (imgFile.exists()) {
                try {
                    ImageIcon icon = new ImageIcon(fullPath);
                    Image img = icon.getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH);
                    JOptionPane.showMessageDialog(this, new JLabel(new ImageIcon(img)), "Refund Proof", JOptionPane.PLAIN_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error loading image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Refund proof not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No refund proof available yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void confirmRefund() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Confirm that you have received your refund?\n\n"
            + "This action cannot be undone.",
            "Confirm Refund",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_refund SET is_refunded = 1, refund_confirmed_date = datetime('now') WHERE trade_id = ? AND user_id = ?";
            db.updateRecord(sql, tradeId, traderId);
            
            JOptionPane.showMessageDialog(this, "Refund confirmed! Thank you.", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            loadRefundStatus();
            loadTradeState();
            updateUI();
        }
    }

    // ========== STEP 6: COMPLETED ==========
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
                if (proceedButton.getText().equals("PROCEED TO NEXT STEP")) {
                    proceedToNext();
                }
                break;
            case 4:
                if (proceedButton.getText().equals("PROCEED TO REFUND")) {
                    proceedToNext();
                }
                break;
            case 5:
                if (proceedButton.getText().equals("PROCEED TO NEXT STEP")) {
                    proceedToNext();
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