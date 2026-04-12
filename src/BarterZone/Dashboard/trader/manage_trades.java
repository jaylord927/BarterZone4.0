package BarterZone.Dashboard.trader;

import BarterZone.Dashboard.session.user_session;
import BarterZone.resources.IconManager;
import database.config.config;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Cursor;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.swing.*;
import javax.swing.border.LineBorder;

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

    private int currentStep = 1;
    private Stack<Integer> stepHistory = new Stack<>();

    // Step managers
    private trades_step1 step1Manager;
    private trades_step2 step2Manager;
    private trades_step3 step3Manager;
    private trades_step4 step4Manager;
    private trades_step5 step5Manager;
    private trades_step6 step6Manager;

    // Trade state
    private String exchangeMethod;
    private String proposedMethod;
    private int proposedBy;
    private boolean methodConfirmed;
    private boolean myItemReceived;
    private boolean otherItemReceived;
    private boolean myRefundConfirmed;
    private boolean otherRefundConfirmed;
    private boolean myPaymentVerified;
    private boolean otherPaymentVerified;
    private boolean myDetailsSubmitted;
    private boolean otherDetailsSubmitted;
    private int myDetailsAgreed;
    private int otherDetailsAgreed;
    private boolean tradeCompleted;

    // For step2 - need to track meetup/delivery IDs
    private int myMeetupId = -1;
    private int myDeliveryId = -1;

    // Step 3 state
    private boolean myPaymentSubmitted;
    private boolean otherPaymentSubmitted;
    private String uploadedProofPath;
    private String otherUploadedProofPath;
    private int selectedMethodId;
    private double currentServiceFee;
    private double currentTotalAmount;
    private String myPaymentNumber;
    private String myAccountName;
    private String otherPaymentNumber;
    private String otherAccountName;

    // Step 5 state 
    private boolean myRefundSubmitted;
    private boolean otherRefundSubmitted;
    private int myRefundId = -1;
    private int otherRefundId = -1;
    private String myUploadedQrPath;
    private String myRefundProofPath;
    private String myAdminMessage = "";
    private String myAdminProofPath = "";
    private String adminRefundMessage;
    private String adminRefundProofPath;
    private String myRefundNumber;
    private String myRefundName;
    private String otherRefundNumber;
    private String otherRefundName;

    // UI Components
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

    private Color themeColor = new Color(12, 192, 223);
    private Color accentColor = new Color(0, 102, 102);
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

        initComponents();
        loadTradeState();

        // Initialize step managers
        step1Manager = new trades_step1(tradeId, traderId, traderName, otherTraderId, otherTraderName, db, this, this::refreshState, proceedButton);
        step2Manager = new trades_step2(tradeId, traderId, otherTraderId, otherTraderName, exchangeMethod, db, this, this::refreshState, proceedButton);
        step3Manager = new trades_step3(tradeId, traderId, traderName, otherTraderId, otherTraderName, db, this, this::refreshState, proceedButton);
        step4Manager = new trades_step4(tradeId, traderId, otherTraderId, otherTraderName, proposedBy, db, this, this::refreshState, proceedButton);
        step5Manager = new trades_step5(tradeId, traderId, traderName, otherTraderId, otherTraderName, db, this, this::refreshState, proceedButton);
        step6Manager = new trades_step6(this, this::goBackToTrades);

        updateUI();

        setTitle("Manage Trade - Trade #" + tradeId);
        setIconImage(new ImageIcon(getClass().getResource(
                "/BarterZone/resources/icon/logo.png")).getImage());
        setSize(1000, 800);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Header Panel
        headerPanel = new JPanel();
        headerPanel.setLayout(null);
        headerPanel.setBackground(new Color(245, 245, 245));
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

        // Content Panel
        contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(new Color(250, 250, 250));
        contentPanel.setBounds(0, 70, 1000, 730);
        getContentPane().add(contentPanel);

        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(null);
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        infoPanel.setBounds(20, 10, 960, 70);

        tradeInfoLabel = new JLabel();
        tradeInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tradeInfoLabel.setBounds(10, 10, 940, 50);
        tradeInfoLabel.setText("<html><b>Your Item:</b> " + myItem + "<br><b>Their Item:</b> " + theirItem + "<br><b>Trading with:</b> " + otherTraderName + "</html>");
        infoPanel.add(tradeInfoLabel);
        contentPanel.add(infoPanel);

        // Step Indicator
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
        statusLabel.setForeground(new Color(80, 80, 80));
        statusLabel.setBounds(20, 145, 960, 25);
        contentPanel.add(statusLabel);

        // Step Panel
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

        // Navigation Panel
        navigationPanel = new JPanel();
        navigationPanel.setLayout(null);
        navigationPanel.setBackground(new Color(240, 240, 240));
        navigationPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        navigationPanel.setBounds(20, 655, 960, 55);
        contentPanel.add(navigationPanel);

        proceedButton = new JButton("PROCEED TO NEXT STEP");
        proceedButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        proceedButton.setBackground(new Color(46, 125, 50));
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
        refreshButton.addActionListener(e -> refreshState());
        navigationPanel.add(refreshButton);

        cancelTradeButton = new JButton("CANCEL TRADE");
        cancelTradeButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        cancelTradeButton.setBackground(new Color(204, 0, 0));
        cancelTradeButton.setForeground(Color.WHITE);
        cancelTradeButton.setBounds(860, 12, 90, 32);
        cancelTradeButton.setBorder(null);
        cancelTradeButton.setFocusPainted(false);
        cancelTradeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelTradeButton.addActionListener(e -> cancelTrade());
        navigationPanel.add(cancelTradeButton);
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
            myItemReceived = t.get("my_item_received") != null && Integer.parseInt(t.get("my_item_received").toString()) == 1;
            otherItemReceived = t.get("other_item_received") != null && Integer.parseInt(t.get("other_item_received").toString()) == 1;
            tradeCompleted = "completed".equals(t.get("trade_status"));
        }

        loadMyTradeDetails();
        loadPaymentStatus();
        loadRefundStatus();
        determineCurrentStep();
    }

    private void loadMyTradeDetails() {
        String sql = "SELECT * FROM tbl_trade_details WHERE trade_id = ? AND trader_id = ?";
        List<Map<String, Object>> details = db.fetchRecords(sql, tradeId, traderId);

        if (!details.isEmpty()) {
            Map<String, Object> d = details.get(0);
            myDetailsSubmitted = d.get("my_details_submitted") != null && Integer.parseInt(d.get("my_details_submitted").toString()) == 1;
            myDetailsAgreed = d.get("my_agreed") != null ? Integer.parseInt(d.get("my_agreed").toString()) : 0;

            // Load meetup/delivery IDs - THIS IS CRITICAL
            if (d.get("meetup_id") != null && Integer.parseInt(d.get("meetup_id").toString()) > 0) {
                myMeetupId = Integer.parseInt(d.get("meetup_id").toString());
                System.out.println("Loaded myMeetupId: " + myMeetupId);
            }
            if (d.get("delivery_id") != null && Integer.parseInt(d.get("delivery_id").toString()) > 0) {
                myDeliveryId = Integer.parseInt(d.get("delivery_id").toString());
                System.out.println("Loaded myDeliveryId: " + myDeliveryId);
            }
        }

        String otherSql = "SELECT * FROM tbl_trade_details WHERE trade_id = ? AND trader_id = ?";
        List<Map<String, Object>> otherDetails = db.fetchRecords(otherSql, tradeId, otherTraderId);

        if (!otherDetails.isEmpty()) {
            Map<String, Object> od = otherDetails.get(0);
            otherDetailsSubmitted = od.get("my_details_submitted") != null
                    && Integer.parseInt(od.get("my_details_submitted").toString()) == 1;
            otherDetailsAgreed = od.get("my_agreed") != null
                    ? Integer.parseInt(od.get("my_agreed").toString()) : 0;
        }
    }

    private void loadPaymentStatus() {
        String mySql = "SELECT payment_submitted, payment_verified, my_number, acc_name, payment_proof FROM tbl_payment_details WHERE trade_id = ? AND trader_id = ?";
        List<Map<String, Object>> myPayment = db.fetchRecords(mySql, tradeId, traderId);
        if (!myPayment.isEmpty()) {
            myPaymentSubmitted = Integer.parseInt(myPayment.get(0).get("payment_submitted").toString()) == 1;
            myPaymentVerified = Integer.parseInt(myPayment.get(0).get("payment_verified").toString()) == 1;
            myPaymentNumber = myPayment.get(0).get("my_number") != null ? myPayment.get(0).get("my_number").toString() : "";
            myAccountName = myPayment.get(0).get("acc_name") != null ? myPayment.get(0).get("acc_name").toString() : "";
            uploadedProofPath = myPayment.get(0).get("payment_proof") != null ? myPayment.get(0).get("payment_proof").toString() : "";
        } else {
            myPaymentSubmitted = false;
            myPaymentVerified = false;
            myPaymentNumber = "";
            myAccountName = "";
            uploadedProofPath = "";
        }

        List<Map<String, Object>> otherPayment = db.fetchRecords(mySql, tradeId, otherTraderId);
        if (!otherPayment.isEmpty()) {
            otherPaymentSubmitted = Integer.parseInt(otherPayment.get(0).get("payment_submitted").toString()) == 1;
            otherPaymentVerified = Integer.parseInt(otherPayment.get(0).get("payment_verified").toString()) == 1;
            otherPaymentNumber = otherPayment.get(0).get("my_number") != null ? otherPayment.get(0).get("my_number").toString() : "";
            otherAccountName = otherPayment.get(0).get("acc_name") != null ? otherPayment.get(0).get("acc_name").toString() : "";
            otherUploadedProofPath = otherPayment.get(0).get("payment_proof") != null ? otherPayment.get(0).get("payment_proof").toString() : "";
        } else {
            otherPaymentSubmitted = false;
            otherPaymentVerified = false;
            otherPaymentNumber = "";
            otherAccountName = "";
            otherUploadedProofPath = "";
        }

        // Load payment settings
        String feeSql = "SELECT service_fee, total_amount, method_id FROM tbl_payment_details WHERE trade_id = ? AND trader_id = ? LIMIT 1";
        List<Map<String, Object>> feeResult = db.fetchRecords(feeSql, tradeId, traderId);
        if (!feeResult.isEmpty()) {
            currentServiceFee = feeResult.get(0).get("service_fee") != null ? Double.parseDouble(feeResult.get(0).get("service_fee").toString()) : 15.00;
            currentTotalAmount = feeResult.get(0).get("total_amount") != null ? Double.parseDouble(feeResult.get(0).get("total_amount").toString()) : 215.00;
            selectedMethodId = feeResult.get(0).get("method_id") != null ? Integer.parseInt(feeResult.get(0).get("method_id").toString()) : -1;
        }
    }

    private void loadRefundStatus() {
        String sql = "SELECT refund_id, account_number, account_name, qr_code_path, refund_proof, refund_message, is_refunded FROM tbl_refund WHERE trade_id = ? AND user_id = ?";

        // Load my refund details
        List<Map<String, Object>> myRefund = db.fetchRecords(sql, tradeId, traderId);
        if (!myRefund.isEmpty()) {
            myRefundSubmitted = true;
            myRefundId = Integer.parseInt(myRefund.get(0).get("refund_id").toString());
            myRefundNumber = myRefund.get(0).get("account_number") != null ? myRefund.get(0).get("account_number").toString() : "-";
            myRefundName = myRefund.get(0).get("account_name") != null ? myRefund.get(0).get("account_name").toString() : "-";
            myRefundConfirmed = Integer.parseInt(myRefund.get(0).get("is_refunded").toString()) == 1;
            myUploadedQrPath = myRefund.get(0).get("qr_code_path") != null ? myRefund.get(0).get("qr_code_path").toString() : "";
            myRefundProofPath = myRefund.get(0).get("refund_proof") != null ? myRefund.get(0).get("refund_proof").toString() : "";

            // Load admin message and proof for THIS trader
            myAdminMessage = myRefund.get(0).get("refund_message") != null ? myRefund.get(0).get("refund_message").toString() : "";
            myAdminProofPath = myRefund.get(0).get("refund_proof") != null ? myRefund.get(0).get("refund_proof").toString() : "";
        } else {
            myRefundSubmitted = false;
            myRefundId = -1;
            myRefundNumber = "-";
            myRefundName = "-";
            myRefundConfirmed = false;
            myUploadedQrPath = "";
            myRefundProofPath = "";
            myAdminMessage = "";
            myAdminProofPath = "";
        }

        // Load other trader's refund details (for display only)
        List<Map<String, Object>> otherRefund = db.fetchRecords(sql, tradeId, otherTraderId);
        if (!otherRefund.isEmpty()) {
            otherRefundSubmitted = true;
            otherRefundId = Integer.parseInt(otherRefund.get(0).get("refund_id").toString());
            otherRefundNumber = otherRefund.get(0).get("account_number") != null ? otherRefund.get(0).get("account_number").toString() : "-";
            otherRefundName = otherRefund.get(0).get("account_name") != null ? otherRefund.get(0).get("account_name").toString() : "-";
            otherRefundConfirmed = Integer.parseInt(otherRefund.get(0).get("is_refunded").toString()) == 1;
        } else {
            otherRefundSubmitted = false;
            otherRefundId = -1;
            otherRefundNumber = "-";
            otherRefundName = "-";
            otherRefundConfirmed = false;
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
            currentStep = canProceed ? 2 : 1;
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
        switch (step) {
            case 1:
                return "Step 1: Propose Method";
            case 2:
                return "Step 2: Exchange Details";
            case 3:
                return "Step 3: Payment";
            case 4:
                return "Step 4: Confirm Receive";
            case 5:
                return "Step 5: Refund";
            case 6:
                return "Completed";
            default:
                return "Step " + step;
        }
    }

    private void refreshState() {
        loadTradeState();
        updateUI();
    }

    private void updateUI() {
        String[] stepNames = {"", "Step 1: Propose Exchange Method", "Step 2: Enter Exchange Details",
            "Step 3: Make Payment", "Step 4: Confirm Receive", "Step 5: Refund", "Step 6: Trade Completed"};
        stepIndicatorLabel.setText(stepNames[currentStep]);

        stepPanel.removeAll();
        stepPanel.setPreferredSize(new Dimension(940, 650));

        switch (currentStep) {
            case 1:
                statusLabel.setText("Choose an exchange method. Both traders must agree before proceeding.");
                step1Manager.loadState(exchangeMethod, proposedMethod, proposedBy, methodConfirmed);
                stepPanel.add(step1Manager.buildPanel());
                break;
            case 2:
                statusLabel.setText("Enter your exchange details. Both traders must submit and agree to proceed.");
                // Load state with 4 parameters: myDetailsSubmitted, myDetailsAgreed, myMeetupId, myDeliveryId
                step2Manager.loadState(myDetailsSubmitted, myDetailsAgreed == 1, myMeetupId, myDeliveryId);
                stepPanel.add(step2Manager.buildPanel());
                break;
            case 3:
                statusLabel.setText("Step 3: Submit your payment proof. Admin will verify both payments.");
                step3Manager.loadState(myPaymentSubmitted, otherPaymentSubmitted, myPaymentVerified, otherPaymentVerified,
                        uploadedProofPath, otherUploadedProofPath, selectedMethodId, currentServiceFee, currentTotalAmount,
                        myPaymentNumber, myAccountName, otherPaymentNumber, otherAccountName);
                stepPanel.add(step3Manager.buildPanel());
                break;
            case 4:
                statusLabel.setText("Step 4: Confirm when you receive the item.");
                step4Manager.loadState(myItemReceived, otherItemReceived);
                stepPanel.add(step4Manager.buildPanel());
                break;
            // In the updateUI() method, case 5:
            case 5:
                statusLabel.setText("Step 5: Provide refund details to receive your refund.");
                step5Manager.loadState(myRefundSubmitted, otherRefundSubmitted, myRefundConfirmed, otherRefundConfirmed,
                        myUploadedQrPath, myRefundProofPath, myAdminMessage, myAdminProofPath,
                        myRefundNumber, myRefundName, otherRefundNumber, otherRefundName);
                stepPanel.add(step5Manager.buildPanel());
                break;
            case 6:
                step6Manager.setTradeInfo(tradeId);  
                stepPanel.add(step6Manager.buildPanel(proceedButton, backStepButton, cancelTradeButton));
                break;
        }

        stepPanel.revalidate();
        stepPanel.repaint();
        backStepButton.setEnabled(currentStep > 1 && currentStep < 6);
    }

    private void handleProceed() {
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
                    "Review Mode", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void cancelTrade() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel this trade?\n\nThis action cannot be undone.",
                "Cancel Trade", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

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
