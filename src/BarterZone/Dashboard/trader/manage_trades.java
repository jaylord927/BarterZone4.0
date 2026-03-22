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
    
    // Trade state variables
    private int currentStep = 1;
    private String exchangeMethod = null;
    private boolean myMethodConfirmed = false;
    private boolean otherMethodConfirmed = false;
    private boolean myDetailsSubmitted = false;
    private boolean otherDetailsSubmitted = false;
    private boolean detailsAgreed = false;
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
    
    // UI Components
    private JPanel headerPanel;
    private JPanel contentPanel;
    private JLabel stepIndicatorLabel;
    private JLabel statusLabel;
    private JLabel tradeInfoLabel;
    private JPanel stepPanel;
    private JPanel navigationPanel;
    private JButton proceedButton;
    private JButton backButton;
    private JButton refreshButton;
    private JButton cancelTradeButton;
    private JScrollPane stepScrollPane;
    
    // Step 1 - Method selection
    private JRadioButton deliveryRadio;
    private JRadioButton meetupRadio;
    private ButtonGroup methodGroup;
    private JLabel methodStatusLabel;
    
    // Step 2 - Details fields
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
    
    // Step 3 - Payment fields
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
    
    // Step 4 - Receipt confirmation
    private JCheckBox confirmReceivedCheck;
    
    // History for back navigation
    private java.util.Stack<Integer> stepHistory = new java.util.Stack<>();
    
    // Colors
    private Color themeColor = new Color(12, 192, 223);
    private Color hoverColor = new Color(70, 210, 235);
    private Color activeColor = new Color(0, 150, 180);
    private Color headerBgColor = new Color(245, 245, 245);
    private Color textColor = new Color(80, 80, 80);
    private Color accentColor = new Color(0, 102, 102);
    private Color successColor = new Color(46, 125, 50);
    private Color warningColor = new Color(255, 153, 0);
    private Color errorColor = new Color(204, 0, 0);
    
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
        setSize(850, 700);
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

        // Header Panel
        headerPanel = new JPanel();
        headerPanel.setLayout(null);
        headerPanel.setBackground(headerBgColor);
        headerPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        headerPanel.setBounds(0, 0, 850, 70);
        getContentPane().add(headerPanel);

        JLabel headerTitle = new JLabel("MANAGE TRADE");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerTitle.setForeground(accentColor);
        headerTitle.setBounds(20, 15, 250, 30);
        headerPanel.add(headerTitle);

        JLabel tradeIdLabel = new JLabel("Trade #" + tradeId);
        tradeIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tradeIdLabel.setForeground(accentColor);
        tradeIdLabel.setBounds(730, 25, 100, 25);
        headerPanel.add(tradeIdLabel);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        JLabel currentDateLabel = new JLabel(sdf.format(new Date()));
        currentDateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        currentDateLabel.setForeground(new Color(102, 102, 102));
        currentDateLabel.setBounds(550, 25, 180, 20);
        headerPanel.add(currentDateLabel);

        // Main Content Panel
        contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(new Color(250, 250, 250));
        contentPanel.setBounds(0, 70, 850, 630);
        getContentPane().add(contentPanel);
        
        // Trade Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(null);
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        infoPanel.setBounds(20, 10, 810, 70);

        tradeInfoLabel = new JLabel();
        tradeInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tradeInfoLabel.setBounds(10, 10, 790, 50);
        tradeInfoLabel.setText("<html>"
                + "<b>Your Item:</b> " + myItem + "<br>"
                + "<b>Their Item:</b> " + theirItem + "<br>"
                + "<b>Trading with:</b> " + otherTraderName
                + "</html>");
        infoPanel.add(tradeInfoLabel);

        contentPanel.add(infoPanel);

        // Step Indicator
        stepIndicatorLabel = new JLabel();
        stepIndicatorLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        stepIndicatorLabel.setForeground(accentColor);
        stepIndicatorLabel.setBounds(20, 90, 810, 30);
        contentPanel.add(stepIndicatorLabel);

        // Status Label
        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(textColor);
        statusLabel.setBounds(20, 120, 810, 25);
        contentPanel.add(statusLabel);

        // Step Panel with Scroll Pane
        stepPanel = new JPanel();
        stepPanel.setLayout(null);
        stepPanel.setBackground(Color.WHITE);
        
        stepScrollPane = new JScrollPane(stepPanel);
        stepScrollPane.setBounds(20, 150, 810, 380);
        stepScrollPane.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        stepScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        stepScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contentPanel.add(stepScrollPane);
        
        stepPanel.setPreferredSize(new Dimension(790, 600));

        // Navigation Panel
        navigationPanel = new JPanel();
        navigationPanel.setLayout(null);
        navigationPanel.setBackground(new Color(240, 240, 240));
        navigationPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        navigationPanel.setBounds(20, 540, 810, 50);
        contentPanel.add(navigationPanel);

        proceedButton = new JButton("PROCEED");
        proceedButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        proceedButton.setBackground(accentColor);
        proceedButton.setForeground(Color.WHITE);
        proceedButton.setBounds(320, 10, 120, 30);
        proceedButton.setBorder(null);
        proceedButton.setFocusPainted(false);
        proceedButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        proceedButton.addActionListener(e -> handleProceed());
        navigationPanel.add(proceedButton);

        backButton = new JButton("BACK");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setBackground(new Color(102, 102, 102));
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(450, 10, 100, 30);
        backButton.setBorder(null);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> goBack());
        backButton.setEnabled(false);
        navigationPanel.add(backButton);

        refreshButton = new JButton("REFRESH");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshButton.setBackground(themeColor);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBounds(560, 10, 100, 30);
        refreshButton.setBorder(null);
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> {
            loadTradeState();
            updateUI();
        });
        navigationPanel.add(refreshButton);

        cancelTradeButton = new JButton("CANCEL TRADE");
        cancelTradeButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cancelTradeButton.setBackground(errorColor);
        cancelTradeButton.setForeground(Color.WHITE);
        cancelTradeButton.setBounds(670, 10, 120, 30);
        cancelTradeButton.setBorder(null);
        cancelTradeButton.setFocusPainted(false);
        cancelTradeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelTradeButton.addActionListener(e -> cancelTrade());
        navigationPanel.add(cancelTradeButton);

        // Initialize step components
        initializeStepComponents();
    }

    private void initializeStepComponents() {
        // Step 1 - Method selection
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

        // Step 2 - Delivery fields
        deliveryAddressField = new JTextField();
        courierField = new JTextField();
        expectedDateField = new JTextField();
        trackingField = new JTextField();
        deliveryInstructionsArea = new JTextArea(3, 20);
        deliveryInstructionsArea.setLineWrap(true);
        deliveryInstructionsArea.setWrapStyleWord(true);
        deliveryInstructionsScroll = new JScrollPane(deliveryInstructionsArea);
        deliveryInstructionsScroll.setPreferredSize(new Dimension(450, 60));
        
        // Step 2 - Meetup fields
        meetupLocationField = new JTextField();
        meetupDateField = new JTextField();
        meetupTimeField = new JTextField();
        contactPersonField = new JTextField();
        contactNumberField = new JTextField();
        googleMapsLinkField = new JTextField();
        googleMapsLinkField.setToolTipText("Optional: Paste Google Maps link for exact location");
        meetupInstructionsArea = new JTextArea(3, 20);
        meetupInstructionsArea.setLineWrap(true);
        meetupInstructionsArea.setWrapStyleWord(true);
        meetupInstructionsScroll = new JScrollPane(meetupInstructionsArea);
        meetupInstructionsScroll.setPreferredSize(new Dimension(450, 60));
        
        // Other trader details area
        otherDetailsArea = new JTextArea(5, 30);
        otherDetailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        otherDetailsArea.setLineWrap(true);
        otherDetailsArea.setWrapStyleWord(true);
        otherDetailsArea.setEditable(false);
        otherDetailsArea.setBackground(new Color(245, 245, 245));
        otherDetailsScroll = new JScrollPane(otherDetailsArea);
        otherDetailsScroll.setPreferredSize(new Dimension(720, 80));
        
        // Step 3 - Payment fields
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
        uploadScreenshotButton.addActionListener(e -> uploadScreenshot());
        
        screenshotFileNameLabel = new JLabel();
        screenshotFileNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        // Payment details fields
        String[] paymentMethods = {"Select Method", "GCash", "PayMaya"};
        paymentMethodCombo = new JComboBox<>(paymentMethods);
        paymentMethodCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        accountNumberField = new JTextField();
        accountNameField = new JTextField();
        
        // Step 4 - Receipt confirmation
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
            myMethodConfirmed = t.get("method_proposed_by") != null && 
                Integer.parseInt(t.get("method_proposed_by").toString()) == traderId;
            otherMethodConfirmed = t.get("method_proposed_by") != null && 
                Integer.parseInt(t.get("method_proposed_by").toString()) == otherTraderId;
            
            myDetailsSubmitted = t.get("my_details_submitted") != null && 
                Integer.parseInt(t.get("my_details_submitted").toString()) == 1;
            otherDetailsSubmitted = t.get("other_details_submitted") != null && 
                Integer.parseInt(t.get("other_details_submitted").toString()) == 1;
            detailsAgreed = t.get("details_agreed") != null && 
                Integer.parseInt(t.get("details_agreed").toString()) == 1;
            
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
        } else if (detailsAgreed) {
            currentStep = 2;
        } else {
            currentStep = 1;
        }
        
        stepHistory.push(currentStep);
    }

    private void updateUI() {
        String[] stepNames = {"", "Step 1: Propose Method", "Step 2: Exchange Details", 
                               "Step 3: Payment", "Step 4: Item Receipt", "Step 5: Refund", "Step 6: Completed"};
        stepIndicatorLabel.setText(stepNames[currentStep]);
        
        stepPanel.removeAll();
        stepPanel.setPreferredSize(new Dimension(790, 500));
        
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
        
        backButton.setEnabled(stepHistory.size() > 1 && currentStep > 1 && !tradeCompleted);
    }

    private void showStep1ProposeMethod() {
        statusLabel.setText("Step 1: Propose exchange method. Both traders must agree before proceeding.");
        
        int y = 20;
        
        JLabel methodLabel = new JLabel("Choose exchange method:");
        methodLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        methodLabel.setBounds(20, y, 300, 25);
        stepPanel.add(methodLabel);
        y += 35;
        
        deliveryRadio.setBounds(20, y, 200, 30);
        meetupRadio.setBounds(230, y, 200, 30);
        
        if (exchangeMethod != null) {
            if (exchangeMethod.equals("delivery")) deliveryRadio.setSelected(true);
            else if (exchangeMethod.equals("meetup")) meetupRadio.setSelected(true);
        }
        
        stepPanel.add(deliveryRadio);
        stepPanel.add(meetupRadio);
        y += 40;
        
        methodStatusLabel.setBounds(20, y, 600, 20);
        
        if (myMethodConfirmed && otherMethodConfirmed) {
            methodStatusLabel.setText("✓ Both traders have confirmed. You can proceed to Step 2.");
            methodStatusLabel.setForeground(successColor);
            proceedButton.setEnabled(true);
            proceedButton.setText("PROCEED");
        } else if (myMethodConfirmed) {
            methodStatusLabel.setText("✓ You have confirmed. Waiting for " + otherTraderName + " to confirm.");
            methodStatusLabel.setForeground(warningColor);
            proceedButton.setEnabled(false);
        } else {
            methodStatusLabel.setText("Select a method and click CONFIRM METHOD.");
            methodStatusLabel.setForeground(textColor);
            proceedButton.setEnabled(true);
            proceedButton.setText("CONFIRM METHOD");
        }
        
        stepPanel.add(methodStatusLabel);
        y += 30;
        
        JLabel warningLabel = new JLabel(
            "<html><i>Note: Once confirmed, you cannot change the method without restarting the trade.</i></html>");
        warningLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        warningLabel.setForeground(warningColor);
        warningLabel.setBounds(20, y, 600, 20);
        stepPanel.add(warningLabel);
    }

    private void showStep2SetDetails() {
        statusLabel.setText("Step 2: Enter your " + exchangeMethod + " details. Both traders must submit and agree.");
        
        int y = 15;
        
        // Your details section
        JLabel yourLabel = new JLabel("YOUR DETAILS:");
        yourLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        yourLabel.setForeground(accentColor);
        yourLabel.setBounds(20, y, 200, 25);
        stepPanel.add(yourLabel);
        y += 35;
        
        if (exchangeMethod.equals("delivery")) {
            addDeliveryFields(y);
            y = 210;
        } else {
            addMeetupFields(y);
            y = 280;
        }
        
        // Other trader's details section
        JLabel otherLabel = new JLabel(otherTraderName + "'S DETAILS:");
        otherLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        otherLabel.setForeground(accentColor);
        otherLabel.setBounds(20, y, 300, 25);
        stepPanel.add(otherLabel);
        y += 30;
        
        otherDetailsScroll.setBounds(20, y, 720, 80);
        stepPanel.add(otherDetailsScroll);
        
        loadOtherTraderDetails();
        y += 90;
        
        // Status and button
        JLabel submitStatus = new JLabel();
        submitStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        submitStatus.setBounds(20, y, 600, 20);
        
        if (myDetailsSubmitted && otherDetailsSubmitted) {
            if (detailsAgreed) {
                submitStatus.setText("✓ Both traders have agreed! You can proceed to Step 3.");
                submitStatus.setForeground(successColor);
                proceedButton.setEnabled(true);
                proceedButton.setText("PROCEED");
            } else {
                submitStatus.setText("✓ Both traders have submitted details. Click AGREE to confirm.");
                submitStatus.setForeground(warningColor);
                proceedButton.setEnabled(true);
                proceedButton.setText("AGREE");
            }
        } else if (myDetailsSubmitted) {
            submitStatus.setText("✓ You have submitted your details. Waiting for " + otherTraderName + ".");
            submitStatus.setForeground(warningColor);
            proceedButton.setEnabled(false);
        } else {
            submitStatus.setText("Please fill in all fields and click SUBMIT DETAILS.");
            submitStatus.setForeground(textColor);
            proceedButton.setEnabled(true);
            proceedButton.setText("SUBMIT DETAILS");
        }
        
        stepPanel.add(submitStatus);
    }

    private void addDeliveryFields(int startY) {
        int y = startY;
        int labelWidth = 120;
        int fieldWidth = 450;
        int fieldX = 140;
        
        JLabel addrLabel = new JLabel("Delivery Address:*");
        addrLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        addrLabel.setBounds(20, y, labelWidth, 25);
        stepPanel.add(addrLabel);
        deliveryAddressField.setBounds(fieldX, y, fieldWidth, 30);
        stepPanel.add(deliveryAddressField);
        y += 40;
        
        JLabel courierLabel = new JLabel("Courier Service:*");
        courierLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        courierLabel.setBounds(20, y, labelWidth, 25);
        stepPanel.add(courierLabel);
        courierField.setBounds(fieldX, y, fieldWidth, 30);
        stepPanel.add(courierField);
        y += 40;
        
        JLabel dateLabel = new JLabel("Expected Date (YYYY-MM-DD):*");
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setBounds(20, y, labelWidth, 25);
        stepPanel.add(dateLabel);
        expectedDateField.setBounds(fieldX, y, fieldWidth, 30);
        stepPanel.add(expectedDateField);
        y += 40;
        
        JLabel trackLabel = new JLabel("Tracking Number:");
        trackLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trackLabel.setBounds(20, y, labelWidth, 25);
        stepPanel.add(trackLabel);
        trackingField.setBounds(fieldX, y, fieldWidth, 30);
        stepPanel.add(trackingField);
        y += 40;
        
        JLabel instLabel = new JLabel("Special Instructions:");
        instLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        instLabel.setBounds(20, y, labelWidth, 25);
        stepPanel.add(instLabel);
        deliveryInstructionsScroll.setBounds(fieldX, y, fieldWidth, 60);
        stepPanel.add(deliveryInstructionsScroll);
        
        // Load existing data
        loadMyDeliveryDetails();
    }

    private void addMeetupFields(int startY) {
        int y = startY;
        int labelWidth = 120;
        int fieldWidth = 450;
        int fieldX = 140;
        
        JLabel locLabel = new JLabel("Meetup Location:*");
        locLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        locLabel.setBounds(20, y, labelWidth, 25);
        stepPanel.add(locLabel);
        meetupLocationField.setBounds(fieldX, y, fieldWidth, 30);
        stepPanel.add(meetupLocationField);
        y += 40;
        
        JLabel mapsLabel = new JLabel("Google Maps Link (Optional):");
        mapsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        mapsLabel.setBounds(20, y, labelWidth, 25);
        stepPanel.add(mapsLabel);
        googleMapsLinkField.setBounds(fieldX, y, fieldWidth, 30);
        stepPanel.add(googleMapsLinkField);
        y += 40;
        
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):*");
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setBounds(20, y, labelWidth, 25);
        stepPanel.add(dateLabel);
        meetupDateField.setBounds(fieldX, y, fieldWidth, 30);
        stepPanel.add(meetupDateField);
        y += 40;
        
        JLabel timeLabel = new JLabel("Time (HH:MM):*");
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        timeLabel.setBounds(20, y, labelWidth, 25);
        stepPanel.add(timeLabel);
        meetupTimeField.setBounds(fieldX, y, fieldWidth, 30);
        stepPanel.add(meetupTimeField);
        y += 40;
        
        JLabel personLabel = new JLabel("Contact Person:*");
        personLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        personLabel.setBounds(20, y, labelWidth, 25);
        stepPanel.add(personLabel);
        contactPersonField.setBounds(fieldX, y, fieldWidth, 30);
        stepPanel.add(contactPersonField);
        y += 40;
        
        JLabel numberLabel = new JLabel("Contact Number:*");
        numberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        numberLabel.setBounds(20, y, labelWidth, 25);
        stepPanel.add(numberLabel);
        contactNumberField.setBounds(fieldX, y, fieldWidth, 30);
        stepPanel.add(contactNumberField);
        y += 40;
        
        JLabel instLabel = new JLabel("Special Instructions:");
        instLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        instLabel.setBounds(20, y, labelWidth, 25);
        stepPanel.add(instLabel);
        meetupInstructionsScroll.setBounds(fieldX, y, fieldWidth, 60);
        stepPanel.add(meetupInstructionsScroll);
        
        // Load existing data
        loadMyMeetupDetails();
    }

    private void loadMyDeliveryDetails() {
        String sql = "SELECT * FROM tbl_trade_details WHERE trade_id = ? AND trader_id = ?";
        List<Map<String, Object>> details = db.fetchRecords(sql, tradeId, traderId);
        
        if (!details.isEmpty()) {
            Map<String, Object> d = details.get(0);
            deliveryAddressField.setText(d.get("delivery_address") != null ? d.get("delivery_address").toString() : "");
            courierField.setText(d.get("courier") != null ? d.get("courier").toString() : "");
            expectedDateField.setText(d.get("expected_date") != null ? d.get("expected_date").toString() : "");
            trackingField.setText(d.get("tracking_number") != null ? d.get("tracking_number").toString() : "");
            deliveryInstructionsArea.setText(d.get("delivery_instructions") != null ? d.get("delivery_instructions").toString() : "");
        }
    }

    private void loadMyMeetupDetails() {
        String sql = "SELECT * FROM tbl_trade_details WHERE trade_id = ? AND trader_id = ?";
        List<Map<String, Object>> details = db.fetchRecords(sql, tradeId, traderId);
        
        if (!details.isEmpty()) {
            Map<String, Object> d = details.get(0);
            meetupLocationField.setText(d.get("meetup_location") != null ? d.get("meetup_location").toString() : "");
            meetupDateField.setText(d.get("meetup_date") != null ? d.get("meetup_date").toString() : "");
            meetupTimeField.setText(d.get("meetup_time") != null ? d.get("meetup_time").toString() : "");
            contactPersonField.setText(d.get("contact_person") != null ? d.get("contact_person").toString() : "");
            contactNumberField.setText(d.get("contact_number") != null ? d.get("contact_number").toString() : "");
            meetupInstructionsArea.setText(d.get("meetup_instructions") != null ? d.get("meetup_instructions").toString() : "");
            googleMapsLinkField.setText(d.get("google_maps_link") != null ? d.get("google_maps_link").toString() : "");
        }
    }

    private void loadOtherTraderDetails() {
        String sql = "SELECT * FROM tbl_trade_details WHERE trade_id = ? AND trader_id = ?";
        List<Map<String, Object>> details = db.fetchRecords(sql, tradeId, otherTraderId);
        
        StringBuilder detailsText = new StringBuilder();
        if (!details.isEmpty()) {
            Map<String, Object> d = details.get(0);
            String method = d.get("exchange_method") != null ? d.get("exchange_method").toString() : "";
            
            if (method.equals("delivery")) {
                detailsText.append("Exchange Method: Delivery\n\n");
                detailsText.append("Delivery Address: ").append(d.get("delivery_address")).append("\n");
                detailsText.append("Courier: ").append(d.get("courier")).append("\n");
                detailsText.append("Expected Date: ").append(d.get("expected_date")).append("\n");
                detailsText.append("Tracking: ").append(d.get("tracking_number")).append("\n");
                detailsText.append("Instructions: ").append(d.get("delivery_instructions"));
            } else if (method.equals("meetup")) {
                detailsText.append("Exchange Method: Meetup\n\n");
                detailsText.append("Location: ").append(d.get("meetup_location")).append("\n");
                String mapsLink = d.get("google_maps_link") != null ? d.get("google_maps_link").toString() : "";
                if (!mapsLink.isEmpty()) detailsText.append("Google Maps: ").append(mapsLink).append("\n");
                detailsText.append("Date: ").append(d.get("meetup_date")).append("\n");
                detailsText.append("Time: ").append(d.get("meetup_time")).append("\n");
                detailsText.append("Contact: ").append(d.get("contact_person")).append(" - ").append(d.get("contact_number")).append("\n");
                detailsText.append("Instructions: ").append(d.get("meetup_instructions"));
            }
        } else {
            detailsText.append("No details submitted yet.");
        }
        
        otherDetailsArea.setText(detailsText.toString());
        otherDetailsArea.setCaretPosition(0);
    }

    private void showStep3Payment() {
        statusLabel.setText("Step 3: Payment processing with admin as middleman.");
        
        int y = 15;
        
        // Payment summary
        JLabel paymentTitle = new JLabel("PAYMENT SUMMARY");
        paymentTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        paymentTitle.setForeground(accentColor);
        paymentTitle.setBounds(20, y, 300, 25);
        stepPanel.add(paymentTitle);
        y += 35;
        
        baseAmountLabel.setText("Item Value: ₱" + String.format("%.2f", baseAmount));
        baseAmountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        baseAmountLabel.setBounds(20, y, 200, 25);
        stepPanel.add(baseAmountLabel);
        y += 30;
        
        feeAmountLabel.setText("Admin Fee: ₱" + String.format("%.2f", feeAmount));
        feeAmountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        feeAmountLabel.setBounds(20, y, 200, 25);
        stepPanel.add(feeAmountLabel);
        y += 30;
        
        feePayerLabel.setText("Fee Payer: " + (feePayer ? "YOU" : otherTraderName));
        feePayerLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        feePayerLabel.setForeground(feePayer ? errorColor : warningColor);
        feePayerLabel.setBounds(20, y, 300, 25);
        stepPanel.add(feePayerLabel);
        y += 35;
        
        totalAmountLabel.setText("TOTAL AMOUNT TO PAY: ₱" + String.format("%.2f", totalAmount));
        totalAmountLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalAmountLabel.setForeground(successColor);
        totalAmountLabel.setBounds(20, y, 400, 30);
        stepPanel.add(totalAmountLabel);
        y += 45;
        
        // Payment details section
        JLabel detailsTitle = new JLabel("YOUR PAYMENT DETAILS (For Refund)");
        detailsTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        detailsTitle.setForeground(accentColor);
        detailsTitle.setBounds(20, y, 300, 25);
        stepPanel.add(detailsTitle);
        y += 35;
        
        JLabel methodLabel = new JLabel("Payment Method:*");
        methodLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        methodLabel.setBounds(20, y, 120, 25);
        stepPanel.add(methodLabel);
        paymentMethodCombo.setBounds(150, y, 200, 30);
        stepPanel.add(paymentMethodCombo);
        y += 40;
        
        JLabel numberLabel = new JLabel("Account Number:*");
        numberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        numberLabel.setBounds(20, y, 120, 25);
        stepPanel.add(numberLabel);
        accountNumberField.setBounds(150, y, 250, 30);
        stepPanel.add(accountNumberField);
        y += 40;
        
        JLabel nameLabel = new JLabel("Registered Name:*");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nameLabel.setBounds(20, y, 120, 25);
        stepPanel.add(nameLabel);
        accountNameField.setBounds(150, y, 250, 30);
        stepPanel.add(accountNameField);
        y += 45;
        
        // Screenshot upload
        uploadScreenshotButton.setBounds(20, y, 150, 35);
        stepPanel.add(uploadScreenshotButton);
        screenshotFileNameLabel.setBounds(180, y, 400, 35);
        stepPanel.add(screenshotFileNameLabel);
        y += 50;
        
        // Warning message
        JTextArea warningArea = new JTextArea();
        warningArea.setFont(new Font("Segoe UI", Font.BOLD, 11));
        warningArea.setForeground(errorColor);
        warningArea.setBackground(new Color(255, 240, 240));
        warningArea.setLineWrap(true);
        warningArea.setWrapStyleWord(true);
        warningArea.setEditable(false);
        warningArea.setText("⚠️ IMPORTANT: Money is NON-REFUNDABLE if sent to wrong number!\n"
                          + "• Double-check your payment details before submitting\n"
                          + "• Verify the number is correct and active\n"
                          + "• Make sure the name matches your account");
        warningArea.setBounds(20, y, 720, 60);
        warningArea.setBorder(new LineBorder(errorColor, 1));
        stepPanel.add(warningArea);
        y += 70;
        
        // Status
        paymentStatusLabel.setBounds(20, y, 500, 25);
        
        if (myPaymentSubmitted && otherPaymentSubmitted) {
            if (paymentVerified) {
                paymentStatusLabel.setText("✓ Payment verified by admin! You can proceed.");
                paymentStatusLabel.setForeground(successColor);
                proceedButton.setEnabled(true);
                proceedButton.setText("PROCEED");
            } else {
                paymentStatusLabel.setText("⏳ Both payments submitted. Waiting for admin verification...");
                paymentStatusLabel.setForeground(warningColor);
                proceedButton.setEnabled(false);
            }
        } else if (myPaymentSubmitted) {
            paymentStatusLabel.setText("✓ Your payment submitted. Waiting for " + otherTraderName + ".");
            paymentStatusLabel.setForeground(warningColor);
            proceedButton.setEnabled(false);
        } else {
            paymentStatusLabel.setText("Please provide your payment details and upload screenshot.");
            paymentStatusLabel.setForeground(textColor);
            proceedButton.setEnabled(true);
            proceedButton.setText("SUBMIT PAYMENT");
        }
        
        stepPanel.add(paymentStatusLabel);
        
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
        statusLabel.setText("Step 4: Confirm item receipt.");
        
        int y = 20;
        
        JLabel receiptLabel = new JLabel("Have you received the item?");
        receiptLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        receiptLabel.setForeground(accentColor);
        receiptLabel.setBounds(20, y, 300, 25);
        stepPanel.add(receiptLabel);
        y += 40;
        
        confirmReceivedCheck.setBounds(20, y, 300, 30);
        stepPanel.add(confirmReceivedCheck);
        y += 45;
        
        JButton confirmButton = new JButton("CONFIRM RECEIPT");
        confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        confirmButton.setBackground(successColor);
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setBounds(20, y, 150, 35);
        confirmButton.setBorder(null);
        confirmButton.setFocusPainted(false);
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmButton.addActionListener(e -> confirmReceipt());
        stepPanel.add(confirmButton);
        y += 50;
        
        JLabel receiptStatus = new JLabel();
        receiptStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        receiptStatus.setBounds(20, y, 400, 20);
        
        if (myItemReceived) {
            receiptStatus.setText("✓ You have confirmed receipt. Waiting for " + otherTraderName + ".");
            receiptStatus.setForeground(successColor);
            proceedButton.setEnabled(false);
        } else {
            receiptStatus.setText("Please confirm once you have received the item.");
            receiptStatus.setForeground(textColor);
            proceedButton.setEnabled(false);
        }
        
        stepPanel.add(receiptStatus);
        y += 30;
        
        JLabel bothStatus = new JLabel();
        bothStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        bothStatus.setBounds(20, y, 400, 20);
        
        if (myItemReceived && otherItemReceived) {
            bothStatus.setText("✓ Both traders have received items! Proceed to refund.");
            bothStatus.setForeground(successColor);
            proceedButton.setEnabled(true);
            proceedButton.setText("PROCEED");
        } else {
            bothStatus.setText("");
        }
        
        stepPanel.add(bothStatus);
    }

    private void showStep5Refund() {
        statusLabel.setText("Step 5: Refund processing.");
        
        int y = 20;
        
        JLabel refundLabel = new JLabel("REFUND PROCESSING");
        refundLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        refundLabel.setForeground(accentColor);
        refundLabel.setBounds(20, y, 300, 25);
        stepPanel.add(refundLabel);
        y += 35;
        
        JLabel refundInfo = new JLabel(
            "<html>Both traders have confirmed receipt.<br>"
            + "The base amount of ₱" + String.format("%.2f", baseAmount) + " will be refunded to both parties.<br>"
            + "The fee of ₱" + String.format("%.2f", feeAmount) + " is retained by BarterZone.</html>");
        refundInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        refundInfo.setBounds(20, y, 600, 60);
        stepPanel.add(refundInfo);
        y += 75;
        
        JLabel refundStatus = new JLabel();
        refundStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refundStatus.setBounds(20, y, 400, 25);
        
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
        
        stepPanel.add(refundStatus);
    }

    private void showCompleted() {
        statusLabel.setText("Trade Completed Successfully!");
        
        int y = 50;
        
        JLabel completedLabel = new JLabel(
            "<html><h2 style='color:#2E7D32;'>✓ TRADE COMPLETED</h2>"
            + "<p>This trade has been successfully completed.</p>"
            + "<p>Thank you for using BarterZone!</p></html>");
        completedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        completedLabel.setBounds(20, y, 600, 120);
        stepPanel.add(completedLabel);
        y += 130;
        
        JButton closeButton = new JButton("CLOSE");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeButton.setBackground(themeColor);
        closeButton.setForeground(Color.WHITE);
        closeButton.setBounds(300, y, 150, 40);
        closeButton.setBorder(null);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> dispose());
        stepPanel.add(closeButton);
        
        proceedButton.setEnabled(false);
        backButton.setEnabled(false);
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
                confirmMethod();
                break;
            case 2:
                if (proceedButton.getText().equals("AGREE")) {
                    agreeDetails();
                } else if (proceedButton.getText().equals("SUBMIT DETAILS")) {
                    submitDetails();
                } else {
                    proceedToNext();
                }
                break;
            case 3:
                if (proceedButton.getText().equals("SUBMIT PAYMENT")) {
                    submitPayment();
                } else {
                    proceedToNext();
                }
                break;
            case 4:
                proceedToNext();
                break;
            case 5:
                completeTrade();
                break;
        }
    }

    private void confirmMethod() {
        if (!deliveryRadio.isSelected() && !meetupRadio.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please select an exchange method.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String method = deliveryRadio.isSelected() ? "delivery" : "meetup";
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Confirm " + (method.equals("delivery") ? "Delivery" : "Meetup") + " as the exchange method?\n\n"
            + "This cannot be changed without restarting the trade.",
            "Confirm Method",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_trade SET exchange_method = ?, method_proposed_by = ? WHERE trade_id = ?";
            db.updateRecord(sql, method, traderId, tradeId);
            
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
        boolean isValid = true;
        
        if (exchangeMethod.equals("delivery")) {
            if (deliveryAddressField.getText().trim().isEmpty() ||
                courierField.getText().trim().isEmpty() ||
                expectedDateField.getText().trim().isEmpty()) {
                isValid = false;
                JOptionPane.showMessageDialog(this,
                    "Please fill in all required fields (Address, Courier, and Expected Date).",
                    "Incomplete Information",
                    JOptionPane.WARNING_MESSAGE);
            }
        } else {
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
            saveDetails();
            JOptionPane.showMessageDialog(this,
                "Details submitted successfully!\n\nWaiting for " + otherTraderName + " to submit their details.",
                "Submission Complete",
                JOptionPane.INFORMATION_MESSAGE);
            
            loadTradeState();
            updateUI();
        }
    }

    private void saveDetails() {
        String googleMapsLink = googleMapsLinkField.getText().trim();
        
        if (exchangeMethod.equals("delivery")) {
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
        } else {
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
        
        String updateSql = "UPDATE tbl_trade SET my_details_submitted = 1 WHERE trade_id = ?";
        db.updateRecord(updateSql, tradeId);
        myDetailsSubmitted = true;
    }

    private void agreeDetails() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Confirm that you agree with the exchange details?\n\n"
            + "This means you have reviewed and confirmed:\n"
            + "• Your exchange details are correct\n"
            + "• The other trader's details are acceptable\n\n"
            + "Once both traders agree, you'll proceed to payment.",
            "Confirm Agreement",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_trade SET details_agreed = 1 WHERE trade_id = ?";
            db.updateRecord(sql, tradeId);
            
            // Check if both have agreed
            String checkSql = "SELECT details_agreed FROM tbl_trade WHERE trade_id = ?";
            List<Map<String, Object>> result = db.fetchRecords(checkSql, tradeId);
            
            if (!result.isEmpty()) {
                int detailsAgreed = Integer.parseInt(result.get(0).get("details_agreed").toString());
                
                if (detailsAgreed == 1) {
                    // Show fee payer selection
                    showFeePayerDialog();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "✓ You have marked your agreement.\n\nWaiting for " + otherTraderName + " to agree.",
                        "Agreement Recorded",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
            
            loadTradeState();
            updateUI();
        }
    }

    private void showFeePayerDialog() {
        Object[] options = {"I will pay the fee", "Let them pay the fee"};
        int choice = JOptionPane.showOptionDialog(this,
            "Who will pay the admin fee of ₱15.00?\n\n"
            + "If you pay the fee: You will pay ₱215 total\n"
            + "If they pay the fee: You will pay ₱200 total\n\n"
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
            + "You will pay: ₱" + (feePayer ? "215.00" : "200.00") + "\n"
            + "Please proceed to Step 3 to submit your payment.",
            "Fee Payer Set",
            JOptionPane.INFORMATION_MESSAGE);
        
        loadTradeState();
        updateUI();
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
            + "Amount: ₱" + String.format("%.2f", totalAmount) + "\n\n"
            + "⚠️ This information will be used for refunds.\n"
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
            + "• Both traders have received items\n"
            + "• Refund has been processed\n"
            + "• This action cannot be undone",
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
                    "✅ TRADE COMPLETED SUCCESSFULLY!\n\n"
                    + "Thank you for using BarterZone.",
                    "Trade Complete",
                    JOptionPane.INFORMATION_MESSAGE);
                
                dispose();
            }
        }
    }

    private void proceedToNext() {
        if (currentStep < 6) {
            currentStep++;
            updateUI();
        }
    }

    private void goBack() {
        if (stepHistory.size() > 1) {
            stepHistory.pop();
            currentStep = stepHistory.peek();
            updateUI();
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
            dispose();
        }
    }
}