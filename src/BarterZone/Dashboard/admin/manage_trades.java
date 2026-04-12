package BarterZone.Dashboard.admin;

import BarterZone.Dashboard.session.user_session;
import database.config.config;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Cursor;
import java.awt.Image;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class manage_trades extends javax.swing.JFrame {

    private int adminId;
    private String adminName;
    private user_session session;
    private config db;

    // Managers
    private trades_method paymentMethodsManager;
    private trades_payment paymentManager;
    private trades_refund refundManager;
    private trades_history historyPanel;

    // Side panel components
    private JPanel sidePanel;
    private JLabel adminAvatarLetter;
    private JLabel adminNameLabel;

    // Menu items
    private JPanel dashboardPanel;
    private JLabel dashboardLabel;
    private JPanel manageUsersPanel;
    private JLabel manageUsersLabel;
    private JLabel usersBadge;
    private JPanel manageAnnouncementPanel;
    private JLabel manageAnnouncementLabel;
    private JLabel announcementBadge;
    private JPanel manageTradesPanel;
    private JLabel manageTradesLabel;
    private JLabel tradesBadge;
    private JPanel manageReportsPanel;
    private JLabel manageReportsLabel;
    private JLabel reportsBadge;
    private JPanel profilePanel;
    private JLabel profileLabel;
    private JPanel logsPanel;
    private JLabel logsLabel;
    private JLabel logsBadge;
    private JPanel logoutPanel;
    private JLabel logoutLabel;

    // Header components
    private JPanel headerPanel;
    private JLabel headerTitle;
    private JLabel currentDateLabel;

    // Main content panel
    private JPanel contentPanel;

    // Tabbed pane
    private JTabbedPane tabbedPane;

    // ========== PAYMENT METHODS TAB ==========
    private JPanel paymentMethodsPanel;
    private DefaultTableModel methodsTableModel;
    private javax.swing.JTable methodsTable;
    private JScrollPane methodsScrollPane;
    private JTextField methodNameField;
    private JTextField accountNumberField;
    private JTextField accountNameField;
    private JButton uploadQrButton;
    private JButton viewQrCodeButton;
    private JLabel qrFileNameLabel;
    private JButton addMethodButton;
    private JButton updateMethodButton;
    private JButton deleteMethodButton;
    private JButton activateMethodButton;
    private String uploadedQrPath = "";
    private int selectedMethodId = -1;
    private String selectedMethodQrPath = "";

    // ========== TRADE PAYMENT SETUP TAB ==========
    private JPanel tradeSetupPanel;
    private JComboBox<String> tradeComboBox;
    private JLabel tradeInfoLabel;
    private JLabel trader1Label;
    private JLabel trader2Label;
    private JLabel item1Label;
    private JLabel item2Label;
    private JComboBox<String> paymentMethodCombo;
    private JTextField serviceFeeField;
    private JTextField totalAmountField;
    private JButton saveTradePaymentButton;
    private int selectedTradeId = -1;
    private int selectedOfferTraderId = -1;
    private int selectedTargetTraderId = -1;
    private String selectedOfferTraderName = "";
    private String selectedTargetTraderName = "";

    // ========== VERIFY PAYMENTS TAB ==========
    private JPanel verifyPaymentsPanel;
    private JComboBox<String> verifyTradeComboBox;
    private JPanel tradersPaymentPanel;
    private JLabel trader1ItemLabel;
    private JLabel trader2ItemLabel;
    private JLabel trader1ProofInfoLabel;
    private JLabel trader2ProofInfoLabel;
    private JPanel trader1Panel;
    private JLabel trader1NameLabel;
    private JLabel trader1PaymentNumberLabel;
    private JLabel trader1AccountNameLabel;
    private JLabel trader1StatusLabel;
    private JButton trader1ViewProofButton;
    private JButton trader1MarkPaidButton;
    private int trader1PaymentId = -1;

    private JPanel trader2Panel;
    private JLabel trader2NameLabel;
    private JLabel trader2PaymentNumberLabel;
    private JLabel trader2AccountNameLabel;
    private JLabel trader2StatusLabel;
    private JButton trader2ViewProofButton;
    private JButton trader2MarkPaidButton;
    private int trader2PaymentId = -1;

    private JLabel overallStatusLabel;

    // ========== VIEW RECEIVE TAB (STEP 4) ==========
    private JPanel viewReceivePanel;
    private JComboBox<String> receiveTradeComboBox;
    private JPanel receiveTradersPanel;

    private JPanel trader1ReceivePanel;
    private JLabel trader1ReceiveNameLabel;
    private JLabel trader1ReceiveItemLabel;
    private JLabel trader1ReceiveStatusLabel;
    private JLabel trader1ReceiveDateLabel;
    private JButton trader1MarkReceivedButton;
    private boolean trader1Received = false;

    private JPanel trader2ReceivePanel;
    private JLabel trader2ReceiveNameLabel;
    private JLabel trader2ReceiveItemLabel;
    private JLabel trader2ReceiveStatusLabel;
    private JLabel trader2ReceiveDateLabel;
    private JButton trader2MarkReceivedButton;
    private boolean trader2Received = false;

    private JLabel receiveOverallStatusLabel;
    private JButton refreshReceiveButton;

    // ========== REFUND MANAGEMENT TAB ==========
    private JPanel refundManagementPanel;
    private JComboBox<String> refundTradeComboBox;
    private JPanel refundTradersPanel;
    private JButton trader1ViewPaymentProofButton;
    private JButton trader2ViewPaymentProofButton;
    private JLabel trader1RefundItemLabel;
    private JLabel trader2RefundItemLabel;
    private JPanel trader1RefundPanel;
    private JLabel trader1RefundNameLabel;
    private JLabel trader1RefundAccountNumberLabel;
    private JLabel trader1RefundAccountNameLabel;
    private JLabel trader1RefundStatusLabel;
    private JButton trader1UploadProofButton;
    private JButton trader1MarkRefundedButton;
    private int trader1RefundId = -1;

    private JPanel trader2RefundPanel;
    private JLabel trader2RefundNameLabel;
    private JLabel trader2RefundAccountNumberLabel;
    private JLabel trader2RefundAccountNameLabel;
    private JLabel trader2RefundStatusLabel;
    private JButton trader2UploadProofButton;
    private JButton trader2MarkRefundedButton;
    private int trader2RefundId = -1;

    private JLabel refundOverallLabel;
    private JButton autoCompleteButton;

    // Colors
    private Color sideBarColor = new Color(8, 78, 128);
    private Color hoverColor = new Color(20, 100, 150);
    private Color activeColor = new Color(0, 60, 100);
    private Color accentColor = new Color(255, 215, 0);
    private Color badgeColor = new Color(204, 0, 0);
    private Color headerGradientStart = new Color(8, 78, 128);
    private Color headerGradientEnd = new Color(0, 45, 80);
    private Color successColor = new Color(46, 125, 50);
    private Color warningColor = new Color(255, 153, 0);
    private Color errorColor = new Color(204, 0, 0);
    private Color themeColor = new Color(12, 192, 223);
    private Color infoColor = new Color(33, 150, 243);

    private JPanel activePanel = null;

    public manage_trades(int adminId, String adminName) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.session = user_session.getInstance();
        this.db = new config();

        // Initialize managers
        this.paymentMethodsManager = new trades_method(db, this, adminId, adminName);
        this.paymentManager = new trades_payment(db, this, adminId, adminName);
        this.refundManager = new trades_refund(db, this, adminId, adminName);

        initComponents();
        setupSidePanel();
        setupHeader();
        setupContentPanel();
        loadPaymentMethods();
        loadTradesForDropdown();
        loadTradesForVerifyDropdown();
        loadTradesForReceiveDropdown();
        refundManager.loadTradesForRefundDropdown(refundTradeComboBox);
        updateBadges();

        setTitle("BarterZone - " + adminName);
        setIconImage(new ImageIcon(getClass().getResource(
                "/BarterZone/resources/icon/logo.png")).getImage());
        setSize(1100, 750);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        sidePanel = new JPanel();
        sidePanel.setLayout(null);
        sidePanel.setBackground(sideBarColor);
        sidePanel.setBounds(0, 0, 220, 750);
        sidePanel.setBorder(new LineBorder(new Color(8, 78, 128), 1, true));
        getContentPane().add(sidePanel);

        headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, headerGradientStart, w, 0, headerGradientEnd);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        headerPanel.setLayout(null);
        headerPanel.setBounds(220, 0, 880, 70);
        getContentPane().add(headerPanel);

        contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(new Color(245, 245, 250));
        contentPanel.setBounds(220, 70, 880, 680);
        getContentPane().add(contentPanel);
    }

    private void setupSidePanel() {
        JPanel avatarPanel = new JPanel();
        avatarPanel.setLayout(null);
        avatarPanel.setBackground(sideBarColor);
        avatarPanel.setBorder(new LineBorder(accentColor, 3));
        avatarPanel.setBounds(60, 30, 100, 80);
        sidePanel.add(avatarPanel);

        adminAvatarLetter = new JLabel();
        adminAvatarLetter.setFont(new Font("Arial", Font.BOLD, 48));
        adminAvatarLetter.setForeground(accentColor);
        adminAvatarLetter.setHorizontalAlignment(JLabel.CENTER);
        adminAvatarLetter.setBounds(0, 10, 100, 60);
        adminAvatarLetter.setText(String.valueOf(adminName.charAt(0)).toUpperCase());
        avatarPanel.add(adminAvatarLetter);

        adminNameLabel = new JLabel(adminName);
        adminNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        adminNameLabel.setForeground(Color.WHITE);
        adminNameLabel.setHorizontalAlignment(JLabel.CENTER);
        adminNameLabel.setBounds(0, 115, 220, 25);
        sidePanel.add(adminNameLabel);

        JLabel adminRoleLabel = new JLabel("Administrator");
        adminRoleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        adminRoleLabel.setForeground(accentColor);
        adminRoleLabel.setHorizontalAlignment(JLabel.CENTER);
        adminRoleLabel.setBounds(0, 135, 220, 20);
        sidePanel.add(adminRoleLabel);

        int menuY = 180;
        int menuHeight = 45;

        dashboardPanel = createMenuItem(20, menuY, 180, menuHeight);
        dashboardLabel = createMenuItemLabel(dashboardPanel, "Dashboard", 20, 12);
        menuY += menuHeight;

        manageUsersPanel = createMenuItem(20, menuY, 180, menuHeight);
        manageUsersLabel = createMenuItemLabel(manageUsersPanel, "Manage Users", 20, 12);
        usersBadge = createBadge(manageUsersPanel, 140, 10, 30, 20);
        menuY += menuHeight;

        manageAnnouncementPanel = createMenuItem(20, menuY, 180, menuHeight);
        manageAnnouncementLabel = createMenuItemLabel(manageAnnouncementPanel, "Manage Announcement", 20, 12);
        announcementBadge = createBadge(manageAnnouncementPanel, 140, 10, 30, 20);
        menuY += menuHeight;

        manageTradesPanel = createMenuItem(20, menuY, 180, menuHeight);
        manageTradesLabel = createMenuItemLabel(manageTradesPanel, "Manage Trades", 20, 12);
        tradesBadge = createBadge(manageTradesPanel, 140, 10, 30, 20);
        menuY += menuHeight;

        manageReportsPanel = createMenuItem(20, menuY, 180, menuHeight);
        manageReportsLabel = createMenuItemLabel(manageReportsPanel, "Manage Reports", 20, 12);
        reportsBadge = createBadge(manageReportsPanel, 140, 10, 30, 20);
        menuY += menuHeight;

        profilePanel = createMenuItem(20, menuY, 180, menuHeight);
        profileLabel = createMenuItemLabel(profilePanel, "Profile", 20, 12);
        menuY += menuHeight;

        logsPanel = createMenuItem(20, menuY, 180, menuHeight);
        logsLabel = createMenuItemLabel(logsPanel, "Logs", 20, 12);
        logsBadge = createBadge(logsPanel, 140, 10, 30, 20);
        menuY += menuHeight;

        logoutPanel = createMenuItem(20, menuY, 180, menuHeight);
        logoutLabel = createMenuItemLabel(logoutPanel, "Logout", 20, 12);

        setActivePanel(manageTradesPanel);
    }

    private JPanel createMenuItem(int x, int y, int width, int height) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(sideBarColor);
        panel.setBounds(x, y, width, height);
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (panel != activePanel) {
                    panel.setBackground(hoverColor);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (panel != activePanel) {
                    panel.setBackground(sideBarColor);
                }
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                handleMenuClick(panel);
            }
        });

        sidePanel.add(panel);
        return panel;
    }

    private JLabel createMenuItemLabel(JPanel panel, String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(Color.WHITE);
        label.setBounds(x, y, 150, 20);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                handleMenuClick(panel);
            }
        });
        panel.add(label);
        return label;
    }

    private JLabel createBadge(JPanel panel, int x, int y, int width, int height) {
        JLabel badge = new JLabel();
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setForeground(Color.WHITE);
        badge.setBackground(badgeColor);
        badge.setOpaque(true);
        badge.setHorizontalAlignment(JLabel.CENTER);
        badge.setBounds(x, y, width, height);
        badge.setVisible(false);
        panel.add(badge);
        return badge;
    }

    private void setupHeader() {
        headerTitle = new JLabel("Manage Trades");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerTitle.setForeground(Color.WHITE);
        headerTitle.setBounds(30, 15, 300, 40);
        headerPanel.add(headerTitle);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        currentDateLabel = new JLabel(sdf.format(new Date()));
        currentDateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        currentDateLabel.setForeground(Color.WHITE);
        currentDateLabel.setBounds(600, 25, 250, 30);
        headerPanel.add(currentDateLabel);
    }

    private void setupContentPanel() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(new Color(245, 245, 250));
        tabbedPane.setBounds(20, 15, 840, 640);

        setupPaymentMethodsPanel();
        tabbedPane.addTab("Payment Methods", paymentMethodsPanel);

        setupTradeSetupPanel();
        tabbedPane.addTab("Trade Payment Setup", tradeSetupPanel);

        setupVerifyPaymentsPanel();
        tabbedPane.addTab("Verify Payments", verifyPaymentsPanel);

        setupViewReceivePanel();
        tabbedPane.addTab("View Receive", viewReceivePanel);

        setupRefundManagementPanel();
        tabbedPane.addTab("Refund Management", refundManagementPanel);

        historyPanel = new trades_history(db, adminId, adminName);
        tabbedPane.addTab("History", historyPanel);

        contentPanel.add(tabbedPane);
    }

    // ========== PAYMENT METHODS TAB ==========
    private void setupPaymentMethodsPanel() {
        paymentMethodsPanel = new JPanel();
        paymentMethodsPanel.setLayout(null);
        paymentMethodsPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Payment Methods");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(sideBarColor);
        titleLabel.setBounds(20, 20, 300, 30);
        paymentMethodsPanel.add(titleLabel);

        JLabel descLabel = new JLabel("Manage payment methods (GCash, PayMaya, etc.) that traders can use");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(new Color(102, 102, 102));
        descLabel.setBounds(20, 55, 500, 20);
        paymentMethodsPanel.add(descLabel);

        String[] columns = {"ID", "Method", "Account Number", "Account Name", "Status"};
        methodsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        methodsTable = new javax.swing.JTable(methodsTableModel);
        methodsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        methodsTable.setRowHeight(40);
        methodsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        methodsTable.getTableHeader().setBackground(sideBarColor);
        methodsTable.getTableHeader().setForeground(Color.WHITE);
        methodsTable.setSelectionBackground(new Color(255, 235, 204));
        methodsTable.getColumnModel().getColumn(0).setMinWidth(0);
        methodsTable.getColumnModel().getColumn(0).setMaxWidth(0);
        methodsTable.getColumnModel().getColumn(0).setWidth(0);

        // Set column widths
        methodsTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        methodsTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        methodsTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        methodsTable.getColumnModel().getColumn(4).setPreferredWidth(80);

        methodsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = methodsTable.getSelectedRow();
                if (row != -1) {
                    int modelRow = methodsTable.convertRowIndexToModel(row);
                    selectedMethodId = Integer.parseInt(methodsTableModel.getValueAt(modelRow, 0).toString());
                    String methodName = methodsTableModel.getValueAt(modelRow, 1).toString();
                    String accountNumber = methodsTableModel.getValueAt(modelRow, 2).toString();
                    String accountName = methodsTableModel.getValueAt(modelRow, 3).toString();

                    methodNameField.setText(methodName);
                    accountNumberField.setText(accountNumber);
                    accountNameField.setText(accountName);

                    // Load QR code path from database for the selected method
                    loadSelectedMethodQrPath(selectedMethodId);

                    updateMethodButton.setEnabled(true);
                    deleteMethodButton.setEnabled(true);
                    activateMethodButton.setEnabled(true);

                    // Enable view QR code button only if QR code exists
                    if (selectedMethodQrPath != null && !selectedMethodQrPath.isEmpty()) {
                        viewQrCodeButton.setEnabled(true);
                    } else {
                        viewQrCodeButton.setEnabled(false);
                    }
                }
            }
        });

        methodsScrollPane = new JScrollPane(methodsTable);
        methodsScrollPane.setBounds(20, 90, 800, 180);
        paymentMethodsPanel.add(methodsScrollPane);

        // Form Panel - Left side for input fields
        JPanel formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBackground(new Color(250, 250, 250));
        formPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        formPanel.setBounds(20, 285, 550, 260);
        paymentMethodsPanel.add(formPanel);

        int y = 20;
        int labelWidth = 120;
        int fieldWidth = 250;
        int fieldX = 140;

        JLabel methodNameLabel = new JLabel("Method Name:*");
        methodNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        methodNameLabel.setBounds(20, y, labelWidth, 30);
        formPanel.add(methodNameLabel);

        methodNameField = new JTextField();
        methodNameField.setBounds(fieldX, y, fieldWidth, 30);
        formPanel.add(methodNameField);
        y += 45;

        JLabel accountNumLabel = new JLabel("Account Number:*");
        accountNumLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        accountNumLabel.setBounds(20, y, labelWidth, 30);
        formPanel.add(accountNumLabel);

        accountNumberField = new JTextField();
        accountNumberField.setBounds(fieldX, y, fieldWidth, 30);
        formPanel.add(accountNumberField);
        y += 45;

        JLabel accountNameLabel = new JLabel("Account Name:");
        accountNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        accountNameLabel.setBounds(20, y, labelWidth, 30);
        formPanel.add(accountNameLabel);

        accountNameField = new JTextField();
        accountNameField.setBounds(fieldX, y, fieldWidth, 30);
        formPanel.add(accountNameField);
        y += 45;

        // QR Code section
        JLabel qrLabel = new JLabel("QR Code:");
        qrLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        qrLabel.setBounds(20, y, labelWidth, 30);
        formPanel.add(qrLabel);

        uploadQrButton = new JButton("Upload QR Code");
        uploadQrButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        uploadQrButton.setBackground(sideBarColor);
        uploadQrButton.setForeground(Color.WHITE);
        uploadQrButton.setBounds(fieldX, y, 150, 30);
        uploadQrButton.setBorder(null);
        uploadQrButton.setFocusPainted(false);
        uploadQrButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        uploadQrButton.addActionListener(e -> {
            uploadedQrPath = paymentMethodsManager.uploadQRCode(this);
            if (!uploadedQrPath.isEmpty()) {
                qrFileNameLabel.setText("QR Code uploaded");
            }
        });
        formPanel.add(uploadQrButton);

        viewQrCodeButton = new JButton("View QR Code");
        viewQrCodeButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        viewQrCodeButton.setBackground(infoColor);
        viewQrCodeButton.setForeground(Color.WHITE);
        viewQrCodeButton.setBounds(fieldX + 160, y, 130, 30);
        viewQrCodeButton.setBorder(null);
        viewQrCodeButton.setFocusPainted(false);
        viewQrCodeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewQrCodeButton.setEnabled(false);
        viewQrCodeButton.addActionListener(e -> viewQrCode());
        formPanel.add(viewQrCodeButton);

        qrFileNameLabel = new JLabel();
        qrFileNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        qrFileNameLabel.setBounds(fieldX + 300, y, 200, 30);
        formPanel.add(qrFileNameLabel);

        // Right side - Action Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBackground(new Color(250, 250, 250));
        buttonPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        buttonPanel.setBounds(590, 285, 230, 260);
        paymentMethodsPanel.add(buttonPanel);

        JLabel actionTitle = new JLabel("Actions");
        actionTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        actionTitle.setForeground(sideBarColor);
        actionTitle.setBounds(15, 15, 100, 25);
        buttonPanel.add(actionTitle);

        // Calculate button positions - 4 buttons vertically stacked
        int btnWidth = 180;
        int btnHeight = 40;
        int btnX = 25;
        int btnSpacing = 15;
        int startY = 55;

        // ADD METHOD Button
        addMethodButton = new JButton("ADD METHOD");
        addMethodButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addMethodButton.setBackground(successColor);
        addMethodButton.setForeground(Color.WHITE);
        addMethodButton.setBounds(btnX, startY, btnWidth, btnHeight);
        addMethodButton.setBorder(null);
        addMethodButton.setFocusPainted(false);
        addMethodButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addMethodButton.addActionListener(e -> {
            paymentMethodsManager.addPaymentMethod(
                    methodNameField.getText().trim(),
                    accountNumberField.getText().trim(),
                    accountNameField.getText().trim(),
                    uploadedQrPath,
                    methodNameField, accountNumberField, accountNameField, qrFileNameLabel,
                    () -> {
                        clearMethodForm();
                        loadPaymentMethods();
                        paymentMethodsManager.loadPaymentMethodsForCombo(paymentMethodCombo);
                        logActivity("Added payment method: " + methodNameField.getText().trim());
                    }
            );
        });
        buttonPanel.add(addMethodButton);

        // UPDATE METHOD Button
        updateMethodButton = new JButton("UPDATE METHOD");
        updateMethodButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        updateMethodButton.setBackground(warningColor);
        updateMethodButton.setForeground(Color.WHITE);
        updateMethodButton.setBounds(btnX, startY + btnHeight + btnSpacing, btnWidth, btnHeight);
        updateMethodButton.setBorder(null);
        updateMethodButton.setFocusPainted(false);
        updateMethodButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateMethodButton.setEnabled(false);
        updateMethodButton.addActionListener(e -> {
            paymentMethodsManager.updatePaymentMethod(
                    selectedMethodId,
                    methodNameField.getText().trim(),
                    accountNumberField.getText().trim(),
                    accountNameField.getText().trim(),
                    uploadedQrPath.isEmpty() ? selectedMethodQrPath : uploadedQrPath,
                    () -> {
                        clearMethodForm();
                        loadPaymentMethods();
                        paymentMethodsManager.loadPaymentMethodsForCombo(paymentMethodCombo);
                        logActivity("Updated payment method ID: " + selectedMethodId);
                    }
            );
        });
        buttonPanel.add(updateMethodButton);

        // ACTIVATE Button
        activateMethodButton = new JButton("ACTIVATE");
        activateMethodButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        activateMethodButton.setBackground(successColor);
        activateMethodButton.setForeground(Color.WHITE);
        activateMethodButton.setBounds(btnX, startY + (btnHeight + btnSpacing) * 2, btnWidth, btnHeight);
        activateMethodButton.setBorder(null);
        activateMethodButton.setFocusPainted(false);
        activateMethodButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        activateMethodButton.setEnabled(false);
        activateMethodButton.addActionListener(e -> {
            paymentMethodsManager.activatePaymentMethod(selectedMethodId, () -> {
                clearMethodForm();
                loadPaymentMethods();
                paymentMethodsManager.loadPaymentMethodsForCombo(paymentMethodCombo);
                logActivity("Activated payment method ID: " + selectedMethodId);
            });
        });
        buttonPanel.add(activateMethodButton);

        // DEACTIVATE Button
        deleteMethodButton = new JButton("DEACTIVATE");
        deleteMethodButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        deleteMethodButton.setBackground(errorColor);
        deleteMethodButton.setForeground(Color.WHITE);
        deleteMethodButton.setBounds(btnX, startY + (btnHeight + btnSpacing) * 3, btnWidth, btnHeight);
        deleteMethodButton.setBorder(null);
        deleteMethodButton.setFocusPainted(false);
        deleteMethodButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteMethodButton.setEnabled(false);
        deleteMethodButton.addActionListener(e -> {
            paymentMethodsManager.deactivatePaymentMethod(selectedMethodId, () -> {
                clearMethodForm();
                loadPaymentMethods();
                paymentMethodsManager.loadPaymentMethodsForCombo(paymentMethodCombo);
                logActivity("Deactivated payment method ID: " + selectedMethodId);
            });
        });
        buttonPanel.add(deleteMethodButton);
    }

    private void loadSelectedMethodQrPath(int methodId) {
        String sql = "SELECT qr_code_path FROM tbl_payment_methods WHERE method_id = ?";
        List<Map<String, Object>> result = db.fetchRecords(sql, methodId);
        if (!result.isEmpty() && result.get(0).get("qr_code_path") != null) {
            selectedMethodQrPath = result.get(0).get("qr_code_path").toString();
        } else {
            selectedMethodQrPath = "";
        }
    }

    private void viewQrCode() {
        if (selectedMethodQrPath == null || selectedMethodQrPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No QR Code available for this payment method.", "QR Code Not Found", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {

            String fullPath = convertResourcePathToFilePath(selectedMethodQrPath);

            if (fullPath == null) {
                JOptionPane.showMessageDialog(this, "Invalid QR Code path format.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            File qrFile = new File(fullPath);
            if (qrFile.exists()) {
                ImageIcon qrIcon = new ImageIcon(fullPath);
                Image scaledImage = qrIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                JOptionPane.showMessageDialog(this, new JLabel(new ImageIcon(scaledImage)), "QR Code - " + methodNameField.getText(), JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "QR Code image file not found at: " + fullPath, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading QR Code: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

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

    private void loadPaymentMethods() {
        paymentMethodsManager.loadPaymentMethods(methodsTableModel);
    }

    private void clearMethodForm() {
        methodNameField.setText("");
        accountNumberField.setText("");
        accountNameField.setText("");
        qrFileNameLabel.setText("");
        uploadedQrPath = "";
        selectedMethodId = -1;
        selectedMethodQrPath = "";
        methodsTable.clearSelection();
        updateMethodButton.setEnabled(false);
        deleteMethodButton.setEnabled(false);
        activateMethodButton.setEnabled(false);
        viewQrCodeButton.setEnabled(false);
    }

    // ========== TRADE PAYMENT SETUP TAB ==========
    private void setupTradeSetupPanel() {
        tradeSetupPanel = new JPanel();
        tradeSetupPanel.setLayout(null);
        tradeSetupPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Trade Payment Setup");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(sideBarColor);
        titleLabel.setBounds(20, 20, 300, 30);
        tradeSetupPanel.add(titleLabel);

        JLabel descLabel = new JLabel("Configure payment method and fee for selected trade");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(new Color(102, 102, 102));
        descLabel.setBounds(20, 55, 400, 20);
        tradeSetupPanel.add(descLabel);

        // Select Trade Section
        JPanel selectTradePanel = new JPanel();
        selectTradePanel.setLayout(null);
        selectTradePanel.setBackground(new Color(250, 250, 250));
        selectTradePanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        selectTradePanel.setBounds(20, 90, 800, 60);
        tradeSetupPanel.add(selectTradePanel);

        JLabel selectLabel = new JLabel("Select Trade:");
        selectLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        selectLabel.setBounds(15, 18, 100, 25);
        selectTradePanel.add(selectLabel);

        tradeComboBox = new JComboBox<>();
        tradeComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tradeComboBox.setBounds(120, 15, 350, 30);
        tradeComboBox.setBackground(Color.WHITE);
        tradeComboBox.setBorder(new LineBorder(new Color(200, 200, 200)));
        tradeComboBox.addActionListener(e -> loadTradeDetails());
        selectTradePanel.add(tradeComboBox);

        JLabel infoNote = new JLabel("Select a trade to configure payment settings");
        infoNote.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        infoNote.setForeground(infoColor);
        infoNote.setBounds(490, 20, 290, 20);
        selectTradePanel.add(infoNote);

        // Trade Information Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(null);
        infoPanel.setBackground(new Color(250, 250, 250));
        infoPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        infoPanel.setBounds(20, 165, 800, 140);
        tradeSetupPanel.add(infoPanel);

        JLabel infoTitle = new JLabel("Trade Information");
        infoTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        infoTitle.setForeground(sideBarColor);
        infoTitle.setBounds(15, 10, 200, 25);
        infoPanel.add(infoTitle);

        tradeInfoLabel = new JLabel();
        tradeInfoLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tradeInfoLabel.setBounds(15, 40, 770, 25);
        infoPanel.add(tradeInfoLabel);

        trader1Label = new JLabel();
        trader1Label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trader1Label.setBounds(15, 70, 350, 25);
        infoPanel.add(trader1Label);

        trader2Label = new JLabel();
        trader2Label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trader2Label.setBounds(15, 95, 350, 25);
        infoPanel.add(trader2Label);

        item1Label = new JLabel();
        item1Label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        item1Label.setBounds(400, 70, 350, 25);
        infoPanel.add(item1Label);

        item2Label = new JLabel();
        item2Label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        item2Label.setBounds(400, 95, 350, 25);
        infoPanel.add(item2Label);

        // Payment Settings Panel
        JPanel paymentSettingsPanel = new JPanel();
        paymentSettingsPanel.setLayout(null);
        paymentSettingsPanel.setBackground(Color.WHITE);
        paymentSettingsPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        paymentSettingsPanel.setBounds(20, 320, 800, 200);
        tradeSetupPanel.add(paymentSettingsPanel);

        JLabel settingsTitle = new JLabel("Payment Settings");
        settingsTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        settingsTitle.setForeground(sideBarColor);
        settingsTitle.setBounds(15, 10, 200, 25);
        paymentSettingsPanel.add(settingsTitle);

        int y = 55;
        int labelWidth = 150;
        int fieldWidth = 250;
        int fieldX = 180;

        // Payment Method
        JLabel methodLabel = new JLabel("Payment Method:");
        methodLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        methodLabel.setBounds(20, y, labelWidth, 30);
        paymentSettingsPanel.add(methodLabel);

        paymentMethodCombo = new JComboBox<>();
        paymentMethodCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        paymentMethodCombo.setBounds(fieldX, y, fieldWidth, 35);
        paymentMethodCombo.setBackground(Color.WHITE);
        paymentMethodCombo.setBorder(new LineBorder(new Color(200, 200, 200)));
        paymentSettingsPanel.add(paymentMethodCombo);
        y += 55;

        // Service Fee
        JLabel feeLabel = new JLabel("Service Fee (PHP):");
        feeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        feeLabel.setBounds(20, y, labelWidth, 30);
        paymentSettingsPanel.add(feeLabel);

        serviceFeeField = new JTextField("15.00");
        serviceFeeField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        serviceFeeField.setBounds(fieldX, y, fieldWidth, 35);
        serviceFeeField.setBorder(new LineBorder(new Color(200, 200, 200)));
        paymentSettingsPanel.add(serviceFeeField);
        y += 55;

        // Total Amount
        JLabel totalLabel = new JLabel("Total Amount (PHP):");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        totalLabel.setBounds(20, y, labelWidth, 30);
        paymentSettingsPanel.add(totalLabel);

        totalAmountField = new JTextField("215.00");
        totalAmountField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        totalAmountField.setBounds(fieldX, y, fieldWidth, 35);
        totalAmountField.setBorder(new LineBorder(new Color(200, 200, 200)));
        paymentSettingsPanel.add(totalAmountField);

        // Separator line
        JSeparator separator = new JSeparator();
        separator.setBounds(20, y + 50, 760, 2);
        paymentSettingsPanel.add(separator);

        // Save Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBounds(20, 535, 800, 70);
        tradeSetupPanel.add(buttonPanel);

        saveTradePaymentButton = new JButton("SAVE TRADE PAYMENT SETTINGS");
        saveTradePaymentButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveTradePaymentButton.setBackground(successColor);
        saveTradePaymentButton.setForeground(Color.WHITE);
        saveTradePaymentButton.setBounds(250, 15, 300, 45);
        saveTradePaymentButton.setBorder(null);
        saveTradePaymentButton.setFocusPainted(false);
        saveTradePaymentButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveTradePaymentButton.addActionListener(e -> {
            paymentManager.saveTradePaymentSettings(selectedTradeId, selectedOfferTraderId, selectedTargetTraderId,
                    paymentMethodCombo, serviceFeeField, totalAmountField, this);
            logActivity("Saved payment settings for Trade #" + selectedTradeId);
        });
        buttonPanel.add(saveTradePaymentButton);

        JLabel buttonNote = new JLabel("Note: Settings will be applied to both traders");
        buttonNote.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        buttonNote.setForeground(infoColor);
        buttonNote.setBounds(250, 65, 300, 20);
        buttonPanel.add(buttonNote);

        paymentMethodsManager.loadPaymentMethodsForCombo(paymentMethodCombo);
    }

    private void loadTradesForDropdown() {
        tradeComboBox.removeAllItems();
        String sql = "SELECT t.trade_id, 'Trade #' || t.trade_id || ' - ' || u1.user_fullname || ' ↔ ' || u2.user_fullname as display "
                + "FROM tbl_trade t "
                + "LEFT JOIN tbl_users u1 ON t.offer_trader_id = u1.user_id "
                + "LEFT JOIN tbl_users u2 ON t.target_trader_id = u2.user_id "
                + "WHERE t.trade_status NOT IN ('completed') "
                + "ORDER BY t.trade_id DESC";
        List<Map<String, Object>> trades = db.fetchRecords(sql);

        for (Map<String, Object> trade : trades) {
            tradeComboBox.addItem(trade.get("display").toString());
        }
    }

    private void loadTradeDetails() {
        int selectedIndex = tradeComboBox.getSelectedIndex();
        if (selectedIndex < 0 || tradeComboBox.getItemCount() == 0) {
            return;
        }

        String selected = tradeComboBox.getSelectedItem().toString();
        String tradeIdStr = selected.substring(selected.indexOf("#") + 1, selected.indexOf(" -"));
        selectedTradeId = Integer.parseInt(tradeIdStr);

        String sql = "SELECT t.trade_id, t.offer_trader_id, t.target_trader_id, "
                + "u1.user_fullname as offer_trader, u2.user_fullname as target_trader, "
                + "i1.item_Name as offer_item, i2.item_Name as target_item "
                + "FROM tbl_trade t "
                + "LEFT JOIN tbl_users u1 ON t.offer_trader_id = u1.user_id "
                + "LEFT JOIN tbl_users u2 ON t.target_trader_id = u2.user_id "
                + "LEFT JOIN tbl_items i1 ON t.offer_item_id = i1.items_id "
                + "LEFT JOIN tbl_items i2 ON t.target_item_id = i2.items_id "
                + "WHERE t.trade_id = ?";

        List<Map<String, Object>> result = db.fetchRecords(sql, selectedTradeId);
        if (!result.isEmpty()) {
            Map<String, Object> trade = result.get(0);
            selectedOfferTraderId = Integer.parseInt(trade.get("offer_trader_id").toString());
            selectedTargetTraderId = Integer.parseInt(trade.get("target_trader_id").toString());
            selectedOfferTraderName = trade.get("offer_trader").toString();
            selectedTargetTraderName = trade.get("target_trader").toString();

            // Format display text
            tradeInfoLabel.setText("<html><b>Trade #" + selectedTradeId + "</b> - Status: Active</html>");
            trader1Label.setText("<html><b>Trader 1:</b> " + selectedOfferTraderName + "</html>");
            trader2Label.setText("<html><b>Trader 2:</b> " + selectedTargetTraderName + "</html>");
            item1Label.setText("<html><b>Item 1:</b> " + trade.get("offer_item") + "</html>");
            item2Label.setText("<html><b>Item 2:</b> " + trade.get("target_item") + "</html>");

            paymentManager.loadTradePaymentInfo(selectedTradeId, selectedOfferTraderId,
                    new JLabel(), new JLabel(), paymentMethodCombo);
        }
    }

    // ========== VERIFY PAYMENTS TAB ==========
    private void setupVerifyPaymentsPanel() {
        verifyPaymentsPanel = new JPanel();
        verifyPaymentsPanel.setLayout(null);
        verifyPaymentsPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Verify Payments - Step 3");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(sideBarColor);
        titleLabel.setBounds(20, 20, 300, 30);
        verifyPaymentsPanel.add(titleLabel);

        JLabel selectLabel = new JLabel("Select Trade:");
        selectLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        selectLabel.setBounds(20, 70, 100, 30);
        verifyPaymentsPanel.add(selectLabel);

        verifyTradeComboBox = new JComboBox<>();
        verifyTradeComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        verifyTradeComboBox.setBounds(130, 70, 300, 30);
        verifyTradeComboBox.addActionListener(e -> loadPaymentVerificationData());
        verifyPaymentsPanel.add(verifyTradeComboBox);

        tradersPaymentPanel = new JPanel();
        tradersPaymentPanel.setLayout(null);
        tradersPaymentPanel.setBackground(new Color(250, 250, 250));
        tradersPaymentPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        tradersPaymentPanel.setBounds(20, 120, 840, 480);
        verifyPaymentsPanel.add(tradersPaymentPanel);

        // Trader 1 Panel - Wider to accommodate full text
        trader1Panel = new JPanel();
        trader1Panel.setLayout(null);
        trader1Panel.setBackground(Color.WHITE);
        trader1Panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor), "TRADER 1"));
        trader1Panel.setBounds(20, 20, 385, 420);
        tradersPaymentPanel.add(trader1Panel);

        int py = 25;

        trader1NameLabel = new JLabel();
        trader1NameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        trader1NameLabel.setForeground(sideBarColor);
        trader1NameLabel.setBounds(15, py, 355, 25);
        trader1Panel.add(trader1NameLabel);
        py += 35;

        // Item label
        JLabel itemTitle1 = new JLabel("Item:");
        itemTitle1.setFont(new Font("Segoe UI", Font.BOLD, 12));
        itemTitle1.setBounds(15, py, 50, 25);
        trader1Panel.add(itemTitle1);

        trader1ItemLabel = new JLabel("-");
        trader1ItemLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trader1ItemLabel.setBounds(70, py, 300, 25);
        trader1Panel.add(trader1ItemLabel);
        py += 30;

        JLabel paymentNumTitle = new JLabel("Payment Number:");
        paymentNumTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        paymentNumTitle.setBounds(15, py, 120, 25);
        trader1Panel.add(paymentNumTitle);

        trader1PaymentNumberLabel = new JLabel("-");
        trader1PaymentNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trader1PaymentNumberLabel.setBounds(145, py, 225, 25);
        trader1Panel.add(trader1PaymentNumberLabel);
        py += 30;

        JLabel accNameTitle = new JLabel("Account Name:");
        accNameTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        accNameTitle.setBounds(15, py, 120, 25);
        trader1Panel.add(accNameTitle);

        trader1AccountNameLabel = new JLabel("-");
        trader1AccountNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trader1AccountNameLabel.setBounds(145, py, 225, 25);
        trader1Panel.add(trader1AccountNameLabel);
        py += 35;

        trader1ViewProofButton = new JButton("View Payment Proof");
        trader1ViewProofButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        trader1ViewProofButton.setBackground(warningColor);
        trader1ViewProofButton.setForeground(Color.WHITE);
        trader1ViewProofButton.setBounds(15, py, 160, 35);
        trader1ViewProofButton.setBorder(null);
        trader1ViewProofButton.setFocusPainted(false);
        trader1ViewProofButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        trader1ViewProofButton.setEnabled(false);
        trader1ViewProofButton.addActionListener(e -> paymentManager.viewPaymentProof(trader1PaymentId));
        trader1Panel.add(trader1ViewProofButton);

        trader1MarkPaidButton = new JButton("Mark as Paid");
        trader1MarkPaidButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        trader1MarkPaidButton.setBackground(successColor);
        trader1MarkPaidButton.setForeground(Color.WHITE);
        trader1MarkPaidButton.setBounds(200, py, 170, 35);
        trader1MarkPaidButton.setBorder(null);
        trader1MarkPaidButton.setFocusPainted(false);
        trader1MarkPaidButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        trader1MarkPaidButton.setEnabled(false);
        trader1MarkPaidButton.addActionListener(e -> {
            paymentManager.markPaymentAsPaid(trader1PaymentId, selectedOfferTraderName, selectedTradeId, this, () -> {
                loadPaymentVerificationData();
                logActivity("Verified payment for " + selectedOfferTraderName + " in Trade #" + selectedTradeId);
            });
        });
        trader1Panel.add(trader1MarkPaidButton);
        py += 50;

        trader1StatusLabel = new JLabel();
        trader1StatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        trader1StatusLabel.setBounds(15, py, 355, 25);
        trader1Panel.add(trader1StatusLabel);

        // Add proof info label
        py += 30;
        trader1ProofInfoLabel = new JLabel();
        trader1ProofInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        trader1ProofInfoLabel.setForeground(new Color(100, 100, 100));
        trader1ProofInfoLabel.setBounds(15, py, 355, 20);
        trader1Panel.add(trader1ProofInfoLabel);

        // Trader 2 Panel - Wider to accommodate full text
        trader2Panel = new JPanel();
        trader2Panel.setLayout(null);
        trader2Panel.setBackground(Color.WHITE);
        trader2Panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor), "TRADER 2"));
        trader2Panel.setBounds(430, 20, 385, 420);
        tradersPaymentPanel.add(trader2Panel);

        py = 25;

        trader2NameLabel = new JLabel();
        trader2NameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        trader2NameLabel.setForeground(sideBarColor);
        trader2NameLabel.setBounds(15, py, 355, 25);
        trader2Panel.add(trader2NameLabel);
        py += 35;

        // Item label
        JLabel itemTitle2 = new JLabel("Item:");
        itemTitle2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        itemTitle2.setBounds(15, py, 50, 25);
        trader2Panel.add(itemTitle2);

        trader2ItemLabel = new JLabel("-");
        trader2ItemLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trader2ItemLabel.setBounds(70, py, 300, 25);
        trader2Panel.add(trader2ItemLabel);
        py += 30;

        JLabel paymentNumTitle2 = new JLabel("Payment Number:");
        paymentNumTitle2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        paymentNumTitle2.setBounds(15, py, 120, 25);
        trader2Panel.add(paymentNumTitle2);

        trader2PaymentNumberLabel = new JLabel("-");
        trader2PaymentNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trader2PaymentNumberLabel.setBounds(145, py, 225, 25);
        trader2Panel.add(trader2PaymentNumberLabel);
        py += 30;

        JLabel accNameTitle2 = new JLabel("Account Name:");
        accNameTitle2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        accNameTitle2.setBounds(15, py, 120, 25);
        trader2Panel.add(accNameTitle2);

        trader2AccountNameLabel = new JLabel("-");
        trader2AccountNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trader2AccountNameLabel.setBounds(145, py, 225, 25);
        trader2Panel.add(trader2AccountNameLabel);
        py += 35;

        trader2ViewProofButton = new JButton("View Payment Proof");
        trader2ViewProofButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        trader2ViewProofButton.setBackground(warningColor);
        trader2ViewProofButton.setForeground(Color.WHITE);
        trader2ViewProofButton.setBounds(15, py, 160, 35);
        trader2ViewProofButton.setBorder(null);
        trader2ViewProofButton.setFocusPainted(false);
        trader2ViewProofButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        trader2ViewProofButton.setEnabled(false);
        trader2ViewProofButton.addActionListener(e -> paymentManager.viewPaymentProof(trader2PaymentId));
        trader2Panel.add(trader2ViewProofButton);

        trader2MarkPaidButton = new JButton("Mark as Paid");
        trader2MarkPaidButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        trader2MarkPaidButton.setBackground(successColor);
        trader2MarkPaidButton.setForeground(Color.WHITE);
        trader2MarkPaidButton.setBounds(200, py, 170, 35);
        trader2MarkPaidButton.setBorder(null);
        trader2MarkPaidButton.setFocusPainted(false);
        trader2MarkPaidButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        trader2MarkPaidButton.setEnabled(false);
        trader2MarkPaidButton.addActionListener(e -> {
            paymentManager.markPaymentAsPaid(trader2PaymentId, selectedTargetTraderName, selectedTradeId, this, () -> {
                loadPaymentVerificationData();
                logActivity("Verified payment for " + selectedTargetTraderName + " in Trade #" + selectedTradeId);
            });
        });
        trader2Panel.add(trader2MarkPaidButton);
        py += 50;

        trader2StatusLabel = new JLabel();
        trader2StatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        trader2StatusLabel.setBounds(15, py, 355, 25);
        trader2Panel.add(trader2StatusLabel);

        // Add proof info label
        py += 30;
        trader2ProofInfoLabel = new JLabel();
        trader2ProofInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        trader2ProofInfoLabel.setForeground(new Color(100, 100, 100));
        trader2ProofInfoLabel.setBounds(15, py, 355, 20);
        trader2Panel.add(trader2ProofInfoLabel);

        overallStatusLabel = new JLabel();
        overallStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        overallStatusLabel.setBounds(20, 440, 800, 30);
        tradersPaymentPanel.add(overallStatusLabel);
    }

    private void loadTradesForVerifyDropdown() {
        verifyTradeComboBox.removeAllItems();

        // FIXED: Show trades where BOTH traders have submitted payment (payment_submitted = 1)
        // This ensures trades with submitted payment details appear in the dropdown
        String sql = "SELECT DISTINCT t.trade_id, 'Trade #' || t.trade_id || ' - ' || u1.user_fullname || ' ↔ ' || u2.user_fullname as display "
                + "FROM tbl_trade t "
                + "LEFT JOIN tbl_users u1 ON t.offer_trader_id = u1.user_id "
                + "LEFT JOIN tbl_users u2 ON t.target_trader_id = u2.user_id "
                + "WHERE EXISTS (SELECT 1 FROM tbl_payment_details p1 WHERE p1.trade_id = t.trade_id AND p1.trader_id = t.offer_trader_id AND p1.payment_submitted = 1) "
                + "AND EXISTS (SELECT 1 FROM tbl_payment_details p2 WHERE p2.trade_id = t.trade_id AND p2.trader_id = t.target_trader_id AND p2.payment_submitted = 1) "
                + "AND t.trade_status NOT IN ('completed', 'cancelled') "
                + "ORDER BY t.trade_id DESC";

        List<Map<String, Object>> trades = db.fetchRecords(sql);

        for (Map<String, Object> trade : trades) {
            verifyTradeComboBox.addItem(trade.get("display").toString());
        }
    }

    private void loadPaymentVerificationData() {
        int selectedIndex = verifyTradeComboBox.getSelectedIndex();
        if (selectedIndex < 0 || verifyTradeComboBox.getItemCount() == 0) {
            resetVerificationPanel();
            return;
        }

        String selected = verifyTradeComboBox.getSelectedItem().toString();
        String tradeIdStr = selected.substring(selected.indexOf("#") + 1, selected.indexOf(" -"));
        int tradeId = Integer.parseInt(tradeIdStr);
        selectedTradeId = tradeId;

        // Get trade details including items
        String sql = "SELECT t.offer_trader_id, t.target_trader_id, "
                + "u1.user_fullname as offer_trader_name, u2.user_fullname as target_trader_name, "
                + "i1.item_Name as offer_item_name, i2.item_Name as target_item_name "
                + "FROM tbl_trade t "
                + "LEFT JOIN tbl_users u1 ON t.offer_trader_id = u1.user_id "
                + "LEFT JOIN tbl_users u2 ON t.target_trader_id = u2.user_id "
                + "LEFT JOIN tbl_items i1 ON t.offer_item_id = i1.items_id "
                + "LEFT JOIN tbl_items i2 ON t.target_item_id = i2.items_id "
                + "WHERE t.trade_id = ?";

        List<Map<String, Object>> result = db.fetchRecords(sql, tradeId);
        if (!result.isEmpty()) {
            Map<String, Object> trade = result.get(0);
            selectedOfferTraderId = Integer.parseInt(trade.get("offer_trader_id").toString());
            selectedTargetTraderId = Integer.parseInt(trade.get("target_trader_id").toString());
            selectedOfferTraderName = trade.get("offer_trader_name").toString();
            selectedTargetTraderName = trade.get("target_trader_name").toString();

            String offerItemName = trade.get("offer_item_name") != null ? trade.get("offer_item_name").toString() : "Unknown Item";
            String targetItemName = trade.get("target_item_name") != null ? trade.get("target_item_name").toString() : "Unknown Item";

            trader1NameLabel.setText(selectedOfferTraderName);
            trader2NameLabel.setText(selectedTargetTraderName);
            trader1ItemLabel.setText(offerItemName);
            trader2ItemLabel.setText(targetItemName);

            int[] paymentIdHolder1 = new int[]{trader1PaymentId};
            int[] paymentIdHolder2 = new int[]{trader2PaymentId};

            // Load Trader 1 payment data with proof info
            paymentManager.loadPaymentVerificationData(tradeId, selectedOfferTraderId,
                    trader1PaymentNumberLabel, trader1AccountNameLabel, trader1StatusLabel,
                    trader1ViewProofButton, trader1MarkPaidButton, paymentIdHolder1, trader1ProofInfoLabel);
            trader1PaymentId = paymentIdHolder1[0];

            // Load Trader 2 payment data with proof info
            paymentManager.loadPaymentVerificationData(tradeId, selectedTargetTraderId,
                    trader2PaymentNumberLabel, trader2AccountNameLabel, trader2StatusLabel,
                    trader2ViewProofButton, trader2MarkPaidButton, paymentIdHolder2, trader2ProofInfoLabel);
            trader2PaymentId = paymentIdHolder2[0];

            paymentManager.checkOverallVerificationStatus(tradeId, overallStatusLabel);
        }
    }

    private void resetVerificationPanel() {
        trader1NameLabel.setText("");
        trader1ItemLabel.setText("-");
        trader1PaymentNumberLabel.setText("-");
        trader1AccountNameLabel.setText("-");
        trader1StatusLabel.setText("");
        trader1ProofInfoLabel.setText("");
        trader1ViewProofButton.setEnabled(false);
        trader1MarkPaidButton.setEnabled(false);

        trader2NameLabel.setText("");
        trader2ItemLabel.setText("-");
        trader2PaymentNumberLabel.setText("-");
        trader2AccountNameLabel.setText("-");
        trader2StatusLabel.setText("");
        trader2ProofInfoLabel.setText("");
        trader2ViewProofButton.setEnabled(false);
        trader2MarkPaidButton.setEnabled(false);

        overallStatusLabel.setText("");
        trader1PaymentId = -1;
        trader2PaymentId = -1;
    }

    // ========== VIEW RECEIVE TAB (STEP 4) ==========
    private void setupViewReceivePanel() {
        viewReceivePanel = new JPanel();
        viewReceivePanel.setLayout(null);
        viewReceivePanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("View Receive - Step 4");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(sideBarColor);
        titleLabel.setBounds(20, 20, 300, 30);
        viewReceivePanel.add(titleLabel);

        JLabel descLabel = new JLabel("Monitor which traders have received their items");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(new Color(102, 102, 102));
        descLabel.setBounds(20, 55, 400, 20);
        viewReceivePanel.add(descLabel);

        JLabel selectLabel = new JLabel("Select Trade:");
        selectLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        selectLabel.setBounds(20, 90, 100, 30);
        viewReceivePanel.add(selectLabel);

        receiveTradeComboBox = new JComboBox<>();
        receiveTradeComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        receiveTradeComboBox.setBounds(130, 90, 300, 30);
        receiveTradeComboBox.addActionListener(e -> loadReceiveData());
        viewReceivePanel.add(receiveTradeComboBox);

        refreshReceiveButton = new JButton("Refresh");
        refreshReceiveButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshReceiveButton.setBackground(accentColor);
        refreshReceiveButton.setForeground(sideBarColor);
        refreshReceiveButton.setBounds(440, 90, 100, 30);
        refreshReceiveButton.setBorder(null);
        refreshReceiveButton.setFocusPainted(false);
        refreshReceiveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshReceiveButton.addActionListener(e -> {
            loadTradesForReceiveDropdown();
            loadReceiveData();
        });
        viewReceivePanel.add(refreshReceiveButton);

        receiveTradersPanel = new JPanel();
        receiveTradersPanel.setLayout(null);
        receiveTradersPanel.setBackground(new Color(250, 250, 250));
        receiveTradersPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        receiveTradersPanel.setBounds(20, 140, 800, 350);
        viewReceivePanel.add(receiveTradersPanel);

        // Trader 1 Receive Panel
        trader1ReceivePanel = new JPanel();
        trader1ReceivePanel.setLayout(null);
        trader1ReceivePanel.setBackground(Color.WHITE);
        trader1ReceivePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor), "TRADER 1"));
        trader1ReceivePanel.setBounds(20, 20, 360, 280);
        receiveTradersPanel.add(trader1ReceivePanel);

        int rp = 25;

        trader1ReceiveNameLabel = new JLabel();
        trader1ReceiveNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        trader1ReceiveNameLabel.setForeground(sideBarColor);
        trader1ReceiveNameLabel.setBounds(15, rp, 330, 25);
        trader1ReceivePanel.add(trader1ReceiveNameLabel);
        rp += 40;

        JLabel itemTitle = new JLabel("Item:");
        itemTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        itemTitle.setBounds(15, rp, 80, 25);
        trader1ReceivePanel.add(itemTitle);

        trader1ReceiveItemLabel = new JLabel("-");
        trader1ReceiveItemLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trader1ReceiveItemLabel.setBounds(100, rp, 250, 25);
        trader1ReceivePanel.add(trader1ReceiveItemLabel);
        rp += 35;

        JLabel statusTitle = new JLabel("Status:");
        statusTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusTitle.setBounds(15, rp, 80, 25);
        trader1ReceivePanel.add(statusTitle);

        trader1ReceiveStatusLabel = new JLabel("Not Received");
        trader1ReceiveStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        trader1ReceiveStatusLabel.setForeground(errorColor);
        trader1ReceiveStatusLabel.setBounds(100, rp, 250, 25);
        trader1ReceivePanel.add(trader1ReceiveStatusLabel);
        rp += 35;

        JLabel dateTitle = new JLabel("Received Date:");
        dateTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        dateTitle.setBounds(15, rp, 100, 25);
        trader1ReceivePanel.add(dateTitle);

        trader1ReceiveDateLabel = new JLabel("-");
        trader1ReceiveDateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trader1ReceiveDateLabel.setBounds(120, rp, 230, 25);
        trader1ReceivePanel.add(trader1ReceiveDateLabel);
        rp += 50;

        trader1MarkReceivedButton = new JButton("Mark as Received");
        trader1MarkReceivedButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        trader1MarkReceivedButton.setBackground(successColor);
        trader1MarkReceivedButton.setForeground(Color.WHITE);
        trader1MarkReceivedButton.setBounds(100, rp, 160, 35);
        trader1MarkReceivedButton.setBorder(null);
        trader1MarkReceivedButton.setFocusPainted(false);
        trader1MarkReceivedButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        trader1MarkReceivedButton.setEnabled(false);
        trader1MarkReceivedButton.addActionListener(e -> markTraderReceived(selectedOfferTraderId, selectedOfferTraderName, true));
        trader1ReceivePanel.add(trader1MarkReceivedButton);

        // Trader 2 Receive Panel
        trader2ReceivePanel = new JPanel();
        trader2ReceivePanel.setLayout(null);
        trader2ReceivePanel.setBackground(Color.WHITE);
        trader2ReceivePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor), "TRADER 2"));
        trader2ReceivePanel.setBounds(420, 20, 360, 280);
        receiveTradersPanel.add(trader2ReceivePanel);

        rp = 25;

        trader2ReceiveNameLabel = new JLabel();
        trader2ReceiveNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        trader2ReceiveNameLabel.setForeground(sideBarColor);
        trader2ReceiveNameLabel.setBounds(15, rp, 330, 25);
        trader2ReceivePanel.add(trader2ReceiveNameLabel);
        rp += 40;

        JLabel itemTitle2 = new JLabel("Item:");
        itemTitle2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        itemTitle2.setBounds(15, rp, 80, 25);
        trader2ReceivePanel.add(itemTitle2);

        trader2ReceiveItemLabel = new JLabel("-");
        trader2ReceiveItemLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trader2ReceiveItemLabel.setBounds(100, rp, 250, 25);
        trader2ReceivePanel.add(trader2ReceiveItemLabel);
        rp += 35;

        JLabel statusTitle2 = new JLabel("Status:");
        statusTitle2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusTitle2.setBounds(15, rp, 80, 25);
        trader2ReceivePanel.add(statusTitle2);

        trader2ReceiveStatusLabel = new JLabel("Not Received");
        trader2ReceiveStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        trader2ReceiveStatusLabel.setForeground(errorColor);
        trader2ReceiveStatusLabel.setBounds(100, rp, 250, 25);
        trader2ReceivePanel.add(trader2ReceiveStatusLabel);
        rp += 35;

        JLabel dateTitle2 = new JLabel("Received Date:");
        dateTitle2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        dateTitle2.setBounds(15, rp, 100, 25);
        trader2ReceivePanel.add(dateTitle2);

        trader2ReceiveDateLabel = new JLabel("-");
        trader2ReceiveDateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trader2ReceiveDateLabel.setBounds(120, rp, 230, 25);
        trader2ReceivePanel.add(trader2ReceiveDateLabel);
        rp += 50;

        trader2MarkReceivedButton = new JButton("Mark as Received");
        trader2MarkReceivedButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        trader2MarkReceivedButton.setBackground(successColor);
        trader2MarkReceivedButton.setForeground(Color.WHITE);
        trader2MarkReceivedButton.setBounds(100, rp, 160, 35);
        trader2MarkReceivedButton.setBorder(null);
        trader2MarkReceivedButton.setFocusPainted(false);
        trader2MarkReceivedButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        trader2MarkReceivedButton.setEnabled(false);
        trader2MarkReceivedButton.addActionListener(e -> markTraderReceived(selectedTargetTraderId, selectedTargetTraderName, false));
        trader2ReceivePanel.add(trader2MarkReceivedButton);

        receiveOverallStatusLabel = new JLabel();
        receiveOverallStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        receiveOverallStatusLabel.setBounds(20, 310, 760, 30);
        receiveTradersPanel.add(receiveOverallStatusLabel);
    }

    private void loadTradesForReceiveDropdown() {
        receiveTradeComboBox.removeAllItems();
        String sql = "SELECT t.trade_id, 'Trade #' || t.trade_id || ' - ' || u1.user_fullname || ' ↔ ' || u2.user_fullname as display "
                + "FROM tbl_trade t "
                + "LEFT JOIN tbl_users u1 ON t.offer_trader_id = u1.user_id "
                + "LEFT JOIN tbl_users u2 ON t.target_trader_id = u2.user_id "
                + "WHERE t.trade_status IN ('payment_verified', 'items_received') "
                + "ORDER BY t.trade_id DESC";
        List<Map<String, Object>> trades = db.fetchRecords(sql);

        for (Map<String, Object> trade : trades) {
            receiveTradeComboBox.addItem(trade.get("display").toString());
        }
    }

    private void loadReceiveData() {
        int selectedIndex = receiveTradeComboBox.getSelectedIndex();
        if (selectedIndex < 0 || receiveTradeComboBox.getItemCount() == 0) {
            resetReceivePanel();
            return;
        }

        String selected = receiveTradeComboBox.getSelectedItem().toString();
        String tradeIdStr = selected.substring(selected.indexOf("#") + 1, selected.indexOf(" -"));
        int tradeId = Integer.parseInt(tradeIdStr);
        selectedTradeId = tradeId;

        String sql = "SELECT t.offer_trader_id, t.target_trader_id, "
                + "t.my_item_received, t.other_item_received, "
                + "t.my_item_received_date, t.other_item_received_date, "
                + "u1.user_fullname as offer_trader_name, u2.user_fullname as target_trader_name, "
                + "i1.item_Name as offer_item, i2.item_Name as target_item "
                + "FROM tbl_trade t "
                + "LEFT JOIN tbl_users u1 ON t.offer_trader_id = u1.user_id "
                + "LEFT JOIN tbl_users u2 ON t.target_trader_id = u2.user_id "
                + "LEFT JOIN tbl_items i1 ON t.offer_item_id = i1.items_id "
                + "LEFT JOIN tbl_items i2 ON t.target_item_id = i2.items_id "
                + "WHERE t.trade_id = ?";

        List<Map<String, Object>> result = db.fetchRecords(sql, tradeId);
        if (!result.isEmpty()) {
            Map<String, Object> trade = result.get(0);
            selectedOfferTraderId = Integer.parseInt(trade.get("offer_trader_id").toString());
            selectedTargetTraderId = Integer.parseInt(trade.get("target_trader_id").toString());
            selectedOfferTraderName = trade.get("offer_trader_name").toString();
            selectedTargetTraderName = trade.get("target_trader_name").toString();

            trader1ReceiveNameLabel.setText(selectedOfferTraderName);
            trader2ReceiveNameLabel.setText(selectedTargetTraderName);
            trader1ReceiveItemLabel.setText(trade.get("offer_item").toString());
            trader2ReceiveItemLabel.setText(trade.get("target_item").toString());

            // Trader 1 receive status
            trader1Received = trade.get("my_item_received") != null
                    && Integer.parseInt(trade.get("my_item_received").toString()) == 1;

            if (trader1Received) {
                trader1ReceiveStatusLabel.setText("✓ RECEIVED");
                trader1ReceiveStatusLabel.setForeground(successColor);
                trader1MarkReceivedButton.setEnabled(false);
                trader1MarkReceivedButton.setText("Already Received");
                trader1MarkReceivedButton.setBackground(new Color(150, 150, 150));

                Object receivedDate = trade.get("my_item_received_date");
                if (receivedDate != null) {
                    trader1ReceiveDateLabel.setText(receivedDate.toString());
                } else {
                    trader1ReceiveDateLabel.setText("Unknown date");
                }
            } else {
                trader1ReceiveStatusLabel.setText("❌ NOT RECEIVED");
                trader1ReceiveStatusLabel.setForeground(errorColor);
                trader1MarkReceivedButton.setEnabled(true);
                trader1MarkReceivedButton.setText("Mark as Received");
                trader1MarkReceivedButton.setBackground(successColor);
                trader1ReceiveDateLabel.setText("-");
            }

            // Trader 2 receive status
            trader2Received = trade.get("other_item_received") != null
                    && Integer.parseInt(trade.get("other_item_received").toString()) == 1;

            if (trader2Received) {
                trader2ReceiveStatusLabel.setText("✓ RECEIVED");
                trader2ReceiveStatusLabel.setForeground(successColor);
                trader2MarkReceivedButton.setEnabled(false);
                trader2MarkReceivedButton.setText("Already Received");
                trader2MarkReceivedButton.setBackground(new Color(150, 150, 150));

                Object receivedDate = trade.get("other_item_received_date");
                if (receivedDate != null) {
                    trader2ReceiveDateLabel.setText(receivedDate.toString());
                } else {
                    trader2ReceiveDateLabel.setText("Unknown date");
                }
            } else {
                trader2ReceiveStatusLabel.setText("❌ NOT RECEIVED");
                trader2ReceiveStatusLabel.setForeground(errorColor);
                trader2MarkReceivedButton.setEnabled(true);
                trader2MarkReceivedButton.setText("Mark as Received");
                trader2MarkReceivedButton.setBackground(successColor);
                trader2ReceiveDateLabel.setText("-");
            }

            // Update overall status
            if (trader1Received && trader2Received) {
                receiveOverallStatusLabel.setText("✓ BOTH TRADERS HAVE RECEIVED THEIR ITEMS! Ready to proceed to Refund Management.");
                receiveOverallStatusLabel.setForeground(successColor);

                // Auto-update trade status if both received
                String updateSql = "UPDATE tbl_trade SET trade_status = 'items_received' WHERE trade_id = ? AND trade_status != 'items_received'";
                db.updateRecord(updateSql, tradeId);
            } else if (trader1Received) {
                receiveOverallStatusLabel.setText("⏳ " + selectedOfferTraderName + " has received the item. Waiting for " + selectedTargetTraderName + " to confirm.");
                receiveOverallStatusLabel.setForeground(warningColor);
            } else if (trader2Received) {
                receiveOverallStatusLabel.setText("⏳ " + selectedTargetTraderName + " has received the item. Waiting for " + selectedOfferTraderName + " to confirm.");
                receiveOverallStatusLabel.setForeground(warningColor);
            } else {
                receiveOverallStatusLabel.setText("❌ No items confirmed yet. Mark each trader as received when they confirm.");
                receiveOverallStatusLabel.setForeground(errorColor);
            }
        }
    }

    private void markTraderReceived(int traderId, String traderName, boolean isOfferTrader) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Mark " + traderName + " as having received the item?\n\n"
                + "This action confirms that the trader has received their item.\n"
                + "This cannot be undone.",
                "Confirm Item Receipt",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            String columnName = isOfferTrader ? "my_item_received" : "other_item_received";
            String dateColumn = isOfferTrader ? "my_item_received_date" : "other_item_received_date";

            String sql = "UPDATE tbl_trade SET " + columnName + " = 1, " + dateColumn + " = datetime('now') WHERE trade_id = ?";
            db.updateRecord(sql, selectedTradeId);

            JOptionPane.showMessageDialog(this,
                    traderName + " marked as received!\n\n"
                    + "The trade status has been updated.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            logActivity("Marked " + traderName + " as received for Trade #" + selectedTradeId);
            loadReceiveData();

            // Refresh refund dropdown as well
            refundManager.loadTradesForRefundDropdown(refundTradeComboBox);
        }
    }

    private void resetReceivePanel() {
        trader1ReceiveNameLabel.setText("");
        trader1ReceiveItemLabel.setText("-");
        trader1ReceiveStatusLabel.setText("Not Received");
        trader1ReceiveStatusLabel.setForeground(errorColor);
        trader1ReceiveDateLabel.setText("-");
        trader1MarkReceivedButton.setEnabled(false);

        trader2ReceiveNameLabel.setText("");
        trader2ReceiveItemLabel.setText("-");
        trader2ReceiveStatusLabel.setText("Not Received");
        trader2ReceiveStatusLabel.setForeground(errorColor);
        trader2ReceiveDateLabel.setText("-");
        trader2MarkReceivedButton.setEnabled(false);

        receiveOverallStatusLabel.setText("");
    }

    // ========== REFUND MANAGEMENT TAB ==========
    private void setupRefundManagementPanel() {
        refundManagementPanel = new JPanel();
        refundManagementPanel.setLayout(null);
        refundManagementPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Refund Management - Step 5");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(sideBarColor);
        titleLabel.setBounds(20, 20, 300, 30);
        refundManagementPanel.add(titleLabel);

        JLabel selectLabel = new JLabel("Select Trade:");
        selectLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        selectLabel.setBounds(20, 70, 100, 30);
        refundManagementPanel.add(selectLabel);

        refundTradeComboBox = new JComboBox<>();
        refundTradeComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        refundTradeComboBox.setBounds(130, 70, 300, 30);
        refundTradeComboBox.addActionListener(e -> loadRefundData());
        refundManagementPanel.add(refundTradeComboBox);

        refundTradersPanel = new JPanel();
        refundTradersPanel.setLayout(null);
        refundTradersPanel.setBackground(new Color(250, 250, 250));
        refundTradersPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        refundTradersPanel.setBounds(20, 120, 840, 470);
        refundManagementPanel.add(refundTradersPanel);

        // Trader 1 Refund Panel
        trader1RefundPanel = new JPanel();
        trader1RefundPanel.setLayout(null);
        trader1RefundPanel.setBackground(Color.WHITE);
        trader1RefundPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor), "TRADER 1 REFUND"));
        trader1RefundPanel.setBounds(20, 20, 385, 420);
        refundTradersPanel.add(trader1RefundPanel);

        int rp = 20;

        trader1RefundNameLabel = new JLabel();
        trader1RefundNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        trader1RefundNameLabel.setForeground(sideBarColor);
        trader1RefundNameLabel.setBounds(15, rp, 355, 25);
        trader1RefundPanel.add(trader1RefundNameLabel);
        rp += 35;

        // Item label for Trader 1
        JLabel itemLabel1 = new JLabel("Item:");
        itemLabel1.setFont(new Font("Segoe UI", Font.BOLD, 12));
        itemLabel1.setBounds(15, rp, 50, 25);
        trader1RefundPanel.add(itemLabel1);

        trader1RefundItemLabel = new JLabel("-");
        trader1RefundItemLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trader1RefundItemLabel.setBounds(70, rp, 300, 25);
        trader1RefundPanel.add(trader1RefundItemLabel);
        rp += 30;

        JLabel accNumTitle = new JLabel("Account Number:");
        accNumTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        accNumTitle.setBounds(15, rp, 120, 25);
        trader1RefundPanel.add(accNumTitle);

        trader1RefundAccountNumberLabel = new JLabel("-");
        trader1RefundAccountNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trader1RefundAccountNumberLabel.setBounds(145, rp, 225, 25);
        trader1RefundPanel.add(trader1RefundAccountNumberLabel);
        rp += 30;

        JLabel accNameTitle = new JLabel("Account Name:");
        accNameTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        accNameTitle.setBounds(15, rp, 120, 25);
        trader1RefundPanel.add(accNameTitle);

        trader1RefundAccountNameLabel = new JLabel("-");
        trader1RefundAccountNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trader1RefundAccountNameLabel.setBounds(145, rp, 225, 25);
        trader1RefundPanel.add(trader1RefundAccountNameLabel);
        rp += 35;

        // Message area for admin message
        JTextArea trader1MessageArea = new JTextArea();
        trader1MessageArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        trader1MessageArea.setLineWrap(true);
        trader1MessageArea.setWrapStyleWord(true);
        trader1MessageArea.setEditable(false);
        trader1MessageArea.setBackground(new Color(245, 245, 245));
        JScrollPane trader1MessageScroll = new JScrollPane(trader1MessageArea);
        trader1MessageScroll.setBounds(15, rp, 355, 50);
        trader1MessageScroll.setBorder(new LineBorder(new Color(200, 200, 200)));
        trader1RefundPanel.add(trader1MessageScroll);
        rp += 65;

        JLabel proofStatusLabel1 = new JLabel();
        proofStatusLabel1.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        proofStatusLabel1.setBounds(15, rp, 200, 25);
        trader1RefundPanel.add(proofStatusLabel1);
        rp += 30;

        // Button Panel for Trader 1 - Three buttons side by side
        JPanel trader1ButtonPanel = new JPanel();
        trader1ButtonPanel.setLayout(null);
        trader1ButtonPanel.setBackground(Color.WHITE);
        trader1ButtonPanel.setBounds(15, rp, 355, 45);
        trader1RefundPanel.add(trader1ButtonPanel);

        // View Payment Proof Button
        trader1ViewPaymentProofButton = new JButton("View Payment");
        trader1ViewPaymentProofButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        trader1ViewPaymentProofButton.setBackground(new Color(33, 150, 243));
        trader1ViewPaymentProofButton.setForeground(Color.WHITE);
        trader1ViewPaymentProofButton.setBounds(0, 5, 110, 35);
        trader1ViewPaymentProofButton.setBorder(null);
        trader1ViewPaymentProofButton.setFocusPainted(false);
        trader1ViewPaymentProofButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        trader1ViewPaymentProofButton.setEnabled(false);
        trader1ViewPaymentProofButton.addActionListener(e -> viewPaymentProofForTrader(selectedOfferTraderId, selectedTradeId));
        trader1ButtonPanel.add(trader1ViewPaymentProofButton);

        // Add Refund Proof Button
        trader1UploadProofButton = new JButton("Add Refund");
        trader1UploadProofButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        trader1UploadProofButton.setBackground(themeColor);
        trader1UploadProofButton.setForeground(Color.WHITE);
        trader1UploadProofButton.setBounds(120, 5, 110, 35);
        trader1UploadProofButton.setBorder(null);
        trader1UploadProofButton.setFocusPainted(false);
        trader1UploadProofButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        trader1UploadProofButton.setEnabled(false);
        final JTextArea finalTrader1MessageArea = trader1MessageArea;
        trader1UploadProofButton.addActionListener(e -> {
            refundManager.showAddRefundProofDialog(trader1RefundId, trader1RefundNameLabel.getText(), selectedTradeId, () -> {
                loadRefundData();
                logActivity("Added refund proof for " + trader1RefundNameLabel.getText() + " in Trade #" + selectedTradeId);
            });
        });
        trader1ButtonPanel.add(trader1UploadProofButton);

        // Mark as Refunded Button
        trader1MarkRefundedButton = new JButton("Mark Refunded");
        trader1MarkRefundedButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        trader1MarkRefundedButton.setBackground(successColor);
        trader1MarkRefundedButton.setForeground(Color.WHITE);
        trader1MarkRefundedButton.setBounds(240, 5, 115, 35);
        trader1MarkRefundedButton.setBorder(null);
        trader1MarkRefundedButton.setFocusPainted(false);
        trader1MarkRefundedButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        trader1MarkRefundedButton.setEnabled(false);
        trader1MarkRefundedButton.addActionListener(e -> {
            refundManager.markRefundAsProcessed(trader1RefundId, trader1RefundNameLabel.getText(), selectedTradeId, this, () -> {
                loadRefundData();
                logActivity("Marked refund as processed for " + trader1RefundNameLabel.getText() + " in Trade #" + selectedTradeId);
            });
        });
        trader1ButtonPanel.add(trader1MarkRefundedButton);
        rp += 55;

        trader1RefundStatusLabel = new JLabel();
        trader1RefundStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        trader1RefundStatusLabel.setBounds(15, rp, 355, 25);
        trader1RefundPanel.add(trader1RefundStatusLabel);

        // Trader 2 Refund Panel
        trader2RefundPanel = new JPanel();
        trader2RefundPanel.setLayout(null);
        trader2RefundPanel.setBackground(Color.WHITE);
        trader2RefundPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor), "TRADER 2 REFUND"));
        trader2RefundPanel.setBounds(430, 20, 385, 420);
        refundTradersPanel.add(trader2RefundPanel);

        rp = 20;

        trader2RefundNameLabel = new JLabel();
        trader2RefundNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        trader2RefundNameLabel.setForeground(sideBarColor);
        trader2RefundNameLabel.setBounds(15, rp, 355, 25);
        trader2RefundPanel.add(trader2RefundNameLabel);
        rp += 35;

        // Item label for Trader 2
        JLabel itemLabel2 = new JLabel("Item:");
        itemLabel2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        itemLabel2.setBounds(15, rp, 50, 25);
        trader2RefundPanel.add(itemLabel2);

        trader2RefundItemLabel = new JLabel("-");
        trader2RefundItemLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trader2RefundItemLabel.setBounds(70, rp, 300, 25);
        trader2RefundPanel.add(trader2RefundItemLabel);
        rp += 30;

        JLabel accNumTitle2 = new JLabel("Account Number:");
        accNumTitle2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        accNumTitle2.setBounds(15, rp, 120, 25);
        trader2RefundPanel.add(accNumTitle2);

        trader2RefundAccountNumberLabel = new JLabel("-");
        trader2RefundAccountNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trader2RefundAccountNumberLabel.setBounds(145, rp, 225, 25);
        trader2RefundPanel.add(trader2RefundAccountNumberLabel);
        rp += 30;

        JLabel accNameTitle2 = new JLabel("Account Name:");
        accNameTitle2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        accNameTitle2.setBounds(15, rp, 120, 25);
        trader2RefundPanel.add(accNameTitle2);

        trader2RefundAccountNameLabel = new JLabel("-");
        trader2RefundAccountNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trader2RefundAccountNameLabel.setBounds(145, rp, 225, 25);
        trader2RefundPanel.add(trader2RefundAccountNameLabel);
        rp += 35;

        // Message area for admin message
        JTextArea trader2MessageArea = new JTextArea();
        trader2MessageArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        trader2MessageArea.setLineWrap(true);
        trader2MessageArea.setWrapStyleWord(true);
        trader2MessageArea.setEditable(false);
        trader2MessageArea.setBackground(new Color(245, 245, 245));
        JScrollPane trader2MessageScroll = new JScrollPane(trader2MessageArea);
        trader2MessageScroll.setBounds(15, rp, 355, 50);
        trader2MessageScroll.setBorder(new LineBorder(new Color(200, 200, 200)));
        trader2RefundPanel.add(trader2MessageScroll);
        rp += 65;

        JLabel proofStatusLabel2 = new JLabel();
        proofStatusLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        proofStatusLabel2.setBounds(15, rp, 200, 25);
        trader2RefundPanel.add(proofStatusLabel2);
        rp += 30;

        // Button Panel for Trader 2 - Three buttons side by side
        JPanel trader2ButtonPanel = new JPanel();
        trader2ButtonPanel.setLayout(null);
        trader2ButtonPanel.setBackground(Color.WHITE);
        trader2ButtonPanel.setBounds(15, rp, 355, 45);
        trader2RefundPanel.add(trader2ButtonPanel);

        // View Payment Proof Button
        trader2ViewPaymentProofButton = new JButton("View Payment");
        trader2ViewPaymentProofButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        trader2ViewPaymentProofButton.setBackground(new Color(33, 150, 243));
        trader2ViewPaymentProofButton.setForeground(Color.WHITE);
        trader2ViewPaymentProofButton.setBounds(0, 5, 110, 35);
        trader2ViewPaymentProofButton.setBorder(null);
        trader2ViewPaymentProofButton.setFocusPainted(false);
        trader2ViewPaymentProofButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        trader2ViewPaymentProofButton.setEnabled(false);
        trader2ViewPaymentProofButton.addActionListener(e -> viewPaymentProofForTrader(selectedTargetTraderId, selectedTradeId));
        trader2ButtonPanel.add(trader2ViewPaymentProofButton);

        // Add Refund Proof Button
        trader2UploadProofButton = new JButton("Add Refund");
        trader2UploadProofButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        trader2UploadProofButton.setBackground(themeColor);
        trader2UploadProofButton.setForeground(Color.WHITE);
        trader2UploadProofButton.setBounds(120, 5, 110, 35);
        trader2UploadProofButton.setBorder(null);
        trader2UploadProofButton.setFocusPainted(false);
        trader2UploadProofButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        trader2UploadProofButton.setEnabled(false);
        trader2UploadProofButton.addActionListener(e -> {
            refundManager.showAddRefundProofDialog(trader2RefundId, trader2RefundNameLabel.getText(), selectedTradeId, () -> {
                loadRefundData();
                logActivity("Added refund proof for " + trader2RefundNameLabel.getText() + " in Trade #" + selectedTradeId);
            });
        });
        trader2ButtonPanel.add(trader2UploadProofButton);

        // Mark as Refunded Button
        trader2MarkRefundedButton = new JButton("Mark Refunded");
        trader2MarkRefundedButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        trader2MarkRefundedButton.setBackground(successColor);
        trader2MarkRefundedButton.setForeground(Color.WHITE);
        trader2MarkRefundedButton.setBounds(240, 5, 115, 35);
        trader2MarkRefundedButton.setBorder(null);
        trader2MarkRefundedButton.setFocusPainted(false);
        trader2MarkRefundedButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        trader2MarkRefundedButton.setEnabled(false);
        trader2MarkRefundedButton.addActionListener(e -> {
            refundManager.markRefundAsProcessed(trader2RefundId, trader2RefundNameLabel.getText(), selectedTradeId, this, () -> {
                loadRefundData();
                logActivity("Marked refund as processed for " + trader2RefundNameLabel.getText() + " in Trade #" + selectedTradeId);
            });
        });
        trader2ButtonPanel.add(trader2MarkRefundedButton);
        rp += 55;

        trader2RefundStatusLabel = new JLabel();
        trader2RefundStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        trader2RefundStatusLabel.setBounds(15, rp, 355, 25);
        trader2RefundPanel.add(trader2RefundStatusLabel);

        refundOverallLabel = new JLabel();
        refundOverallLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        refundOverallLabel.setBounds(20, 445, 600, 25);
        refundTradersPanel.add(refundOverallLabel);

        autoCompleteButton = new JButton("AUTO-COMPLETE TRADE");
        autoCompleteButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        autoCompleteButton.setBackground(accentColor);
        autoCompleteButton.setForeground(sideBarColor);
        autoCompleteButton.setBounds(650, 445, 170, 30);
        autoCompleteButton.setBorder(null);
        autoCompleteButton.setFocusPainted(false);
        autoCompleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        autoCompleteButton.addActionListener(e -> checkAndCompleteTrade());
        refundTradersPanel.add(autoCompleteButton);
    }

    private void viewPaymentProofForTrader(int traderId, int tradeId) {
        if (tradeId == -1 || traderId == -1) {
            JOptionPane.showMessageDialog(this, "No trade selected or invalid trader.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "SELECT payment_proof FROM tbl_payment_details WHERE trade_id = ? AND trader_id = ?";
        List<Map<String, Object>> result = db.fetchRecords(sql, tradeId, traderId);

        if (result.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No payment record found for this trader.", "Not Found", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String proofPath = result.get(0).get("payment_proof") != null ? result.get(0).get("payment_proof").toString() : "";

        if (proofPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No payment proof image uploaded for this trader.", "No Proof", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Convert resource path to file system path
            String fullPath = convertResourcePathToFilePath(proofPath);

            if (fullPath == null) {
                JOptionPane.showMessageDialog(this, "Invalid proof path format.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            File imgFile = new File(fullPath);
            if (imgFile.exists()) {
                ImageIcon icon = new ImageIcon(fullPath);
                Image scaledImage = icon.getImage().getScaledInstance(600, 600, Image.SCALE_SMOOTH);
                JOptionPane.showMessageDialog(this, new JLabel(new ImageIcon(scaledImage)),
                        "Payment Proof - Trader ID: " + traderId, JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Payment proof image not found at: " + fullPath, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading payment proof: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadRefundData() {
        int selectedIndex = refundTradeComboBox.getSelectedIndex();
        if (selectedIndex < 0 || refundTradeComboBox.getItemCount() == 0) {
            resetRefundPanel();
            return;
        }

        String selected = refundTradeComboBox.getSelectedItem().toString();
        String tradeIdStr = selected.substring(selected.indexOf("#") + 1, selected.indexOf(" -"));
        int tradeId = Integer.parseInt(tradeIdStr);
        selectedTradeId = tradeId;

        // Include item names in the query
        String sql = "SELECT t.offer_trader_id, t.target_trader_id, "
                + "u1.user_fullname as offer_trader_name, u2.user_fullname as target_trader_name, "
                + "i1.item_Name as offer_item_name, i2.item_Name as target_item_name "
                + "FROM tbl_trade t "
                + "LEFT JOIN tbl_users u1 ON t.offer_trader_id = u1.user_id "
                + "LEFT JOIN tbl_users u2 ON t.target_trader_id = u2.user_id "
                + "LEFT JOIN tbl_items i1 ON t.offer_item_id = i1.items_id "
                + "LEFT JOIN tbl_items i2 ON t.target_item_id = i2.items_id "
                + "WHERE t.trade_id = ?";

        List<Map<String, Object>> result = db.fetchRecords(sql, tradeId);
        if (!result.isEmpty()) {
            Map<String, Object> trade = result.get(0);
            selectedOfferTraderId = Integer.parseInt(trade.get("offer_trader_id").toString());
            selectedTargetTraderId = Integer.parseInt(trade.get("target_trader_id").toString());
            selectedOfferTraderName = trade.get("offer_trader_name").toString();
            selectedTargetTraderName = trade.get("target_trader_name").toString();

            String offerItemName = trade.get("offer_item_name") != null ? trade.get("offer_item_name").toString() : "Unknown";
            String targetItemName = trade.get("target_item_name") != null ? trade.get("target_item_name").toString() : "Unknown";

            trader1RefundNameLabel.setText(selectedOfferTraderName);
            trader2RefundNameLabel.setText(selectedTargetTraderName);
            trader1RefundItemLabel.setText(offerItemName);
            trader2RefundItemLabel.setText(targetItemName);

            int[] refundIdHolder1 = new int[]{trader1RefundId};
            int[] refundIdHolder2 = new int[]{trader2RefundId};

            // Find the message area and proof status label components
            JTextArea trader1MessageArea = null;
            JTextArea trader2MessageArea = null;
            JLabel proofStatusLabel1 = null;
            JLabel proofStatusLabel2 = null;

            // Get the message areas from the panels
            for (java.awt.Component comp : trader1RefundPanel.getComponents()) {
                if (comp instanceof JScrollPane) {
                    JScrollPane scroll = (JScrollPane) comp;
                    if (scroll.getViewport().getView() instanceof JTextArea) {
                        trader1MessageArea = (JTextArea) scroll.getViewport().getView();
                        break;
                    }
                }
            }
            for (java.awt.Component comp : trader2RefundPanel.getComponents()) {
                if (comp instanceof JScrollPane) {
                    JScrollPane scroll = (JScrollPane) comp;
                    if (scroll.getViewport().getView() instanceof JTextArea) {
                        trader2MessageArea = (JTextArea) scroll.getViewport().getView();
                        break;
                    }
                }
            }

            // Get proof status labels
            for (java.awt.Component comp : trader1RefundPanel.getComponents()) {
                if (comp instanceof JLabel && ((JLabel) comp).getFont().getSize() == 11 && ((JLabel) comp).getText() == null) {
                    proofStatusLabel1 = (JLabel) comp;
                    break;
                }
            }
            for (java.awt.Component comp : trader2RefundPanel.getComponents()) {
                if (comp instanceof JLabel && ((JLabel) comp).getFont().getSize() == 11 && ((JLabel) comp).getText() == null) {
                    proofStatusLabel2 = (JLabel) comp;
                    break;
                }
            }

            refundManager.loadTraderRefundData(tradeId, selectedOfferTraderId, selectedOfferTraderName,
                    trader1RefundAccountNumberLabel, trader1RefundAccountNameLabel,
                    trader1RefundStatusLabel, trader1UploadProofButton, trader1MarkRefundedButton,
                    trader1MessageArea, proofStatusLabel1, refundIdHolder1);
            trader1RefundId = refundIdHolder1[0];

            refundManager.loadTraderRefundData(tradeId, selectedTargetTraderId, selectedTargetTraderName,
                    trader2RefundAccountNumberLabel, trader2RefundAccountNameLabel,
                    trader2RefundStatusLabel, trader2UploadProofButton, trader2MarkRefundedButton,
                    trader2MessageArea, proofStatusLabel2, refundIdHolder2);
            trader2RefundId = refundIdHolder2[0];

            // Enable View Payment Proof buttons if payment exists
            enableViewPaymentProofButtons(selectedOfferTraderId, selectedTargetTraderId, tradeId);

            refundManager.checkOverallRefundStatus(tradeId, refundOverallLabel, this, () -> {
                checkAndCompleteTrade();
            });
        }
    }

    private void enableViewPaymentProofButtons(int offerTraderId, int targetTraderId, int tradeId) {
        // Check Trader 1 (offer trader)
        String sql1 = "SELECT payment_proof FROM tbl_payment_details WHERE trade_id = ? AND trader_id = ? AND payment_proof IS NOT NULL AND payment_proof != ''";
        List<Map<String, Object>> result1 = db.fetchRecords(sql1, tradeId, offerTraderId);
        trader1ViewPaymentProofButton.setEnabled(!result1.isEmpty());

        // Check Trader 2 (target trader)
        String sql2 = "SELECT payment_proof FROM tbl_payment_details WHERE trade_id = ? AND trader_id = ? AND payment_proof IS NOT NULL AND payment_proof != ''";
        List<Map<String, Object>> result2 = db.fetchRecords(sql2, tradeId, targetTraderId);
        trader2ViewPaymentProofButton.setEnabled(!result2.isEmpty());
    }

    private void checkAndCompleteTrade() {
        String checkSql = "SELECT COUNT(*) as refunded_count FROM tbl_refund WHERE trade_id = ? AND is_refunded = 1";
        double refundedCount = db.getSingleValue(checkSql, selectedTradeId);

        if (refundedCount == 2) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Both traders have been refunded!\n\n"
                    + "Do you want to mark this trade as COMPLETED?\n"
                    + "This will move the trade to History.",
                    "Complete Trade",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                completeTradeAndMoveToHistory();
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Cannot complete trade yet.\n\n"
                    + "Both traders must be refunded first.\n"
                    + "Current status: " + (int) refundedCount + "/2 refunded.",
                    "Cannot Complete",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void completeTradeAndMoveToHistory() {
        String getSql = "SELECT * FROM tbl_trade WHERE trade_id = ?";
        List<Map<String, Object>> trade = db.fetchRecords(getSql, selectedTradeId);

        if (!trade.isEmpty()) {
            Map<String, Object> t = trade.get(0);

            String historySql = "INSERT INTO tbl_trade_history "
                    + "(trade_id, offer_trader_id, target_trader_id, offer_item_id, "
                    + "target_item_id, trade_status, trade_DateRequest, trade_DateCompleted) "
                    + "VALUES (?, ?, ?, ?, ?, 'completed', ?, datetime('now'))";

            db.addRecord(historySql,
                    selectedTradeId,
                    t.get("offer_trader_id"),
                    t.get("target_trader_id"),
                    t.get("offer_item_id"),
                    t.get("target_item_id"),
                    t.get("trade_DateRequest"));

            String deleteSql = "DELETE FROM tbl_trade WHERE trade_id = ?";
            db.deleteRecord(deleteSql, selectedTradeId);

            JOptionPane.showMessageDialog(this,
                    "TRADE COMPLETED SUCCESSFULLY!\n\n"
                    + "Trade #" + selectedTradeId + " has been moved to History.",
                    "Trade Complete",
                    JOptionPane.INFORMATION_MESSAGE);

            logActivity("Completed Trade #" + selectedTradeId);

            loadTradesForDropdown();
            loadTradesForVerifyDropdown();
            loadTradesForReceiveDropdown();
            refundManager.loadTradesForRefundDropdown(refundTradeComboBox);

            if (historyPanel != null) {
                historyPanel.refresh();
            }

            resetRefundPanel();
            resetReceivePanel();
        }
    }

    private void resetRefundPanel() {
        trader1RefundNameLabel.setText("");
        trader1RefundItemLabel.setText("-");
        trader1RefundAccountNumberLabel.setText("-");
        trader1RefundAccountNameLabel.setText("-");
        trader1RefundStatusLabel.setText("");
        trader1UploadProofButton.setEnabled(false);
        trader1MarkRefundedButton.setEnabled(false);
        trader1ViewPaymentProofButton.setEnabled(false);

        trader2RefundNameLabel.setText("");
        trader2RefundItemLabel.setText("-");
        trader2RefundAccountNumberLabel.setText("-");
        trader2RefundAccountNameLabel.setText("-");
        trader2RefundStatusLabel.setText("");
        trader2UploadProofButton.setEnabled(false);
        trader2MarkRefundedButton.setEnabled(false);
        trader2ViewPaymentProofButton.setEnabled(false);

        refundOverallLabel.setText("");
        trader1RefundId = -1;
        trader2RefundId = -1;
    }

    private void updateBadges() {
        try {
            String pendingReportsSql = "SELECT COUNT(*) as count FROM tbl_reports WHERE report_status IN ('pending', 'under_review')";
            double pendingReports = db.getSingleValue(pendingReportsSql);
            if (reportsBadge != null) {
                reportsBadge.setText(String.valueOf((int) pendingReports));
                reportsBadge.setVisible(pendingReports > 0);
            }

            String pendingTradesSql = "SELECT COUNT(*) as count FROM tbl_trade WHERE trade_status = 'pending'";
            double pendingTrades = db.getSingleValue(pendingTradesSql);
            if (tradesBadge != null) {
                tradesBadge.setText(String.valueOf((int) pendingTrades));
                tradesBadge.setVisible(pendingTrades > 0);
            }

            String newUsersSql = "SELECT COUNT(*) as count FROM tbl_users WHERE created_date >= datetime('now', '-1 day')";
            double newUsers = db.getSingleValue(newUsersSql);
            if (usersBadge != null) {
                usersBadge.setText(String.valueOf((int) newUsers));
                usersBadge.setVisible(newUsers > 0);
            }

            String activeAnnouncementsSql = "SELECT COUNT(*) as count FROM tbl_announcement WHERE is_active = 1";
            double activeAnnouncements = db.getSingleValue(activeAnnouncementsSql);
            if (announcementBadge != null) {
                announcementBadge.setText(String.valueOf((int) activeAnnouncements));
                announcementBadge.setVisible(activeAnnouncements > 0);
            }

            String recentLogsSql = "SELECT COUNT(*) as count FROM tbl_logs WHERE log_date >= datetime('now', '-1 day')";
            double recentLogs = db.getSingleValue(recentLogsSql);
            if (logsBadge != null) {
                logsBadge.setText(String.valueOf((int) recentLogs));
                logsBadge.setVisible(recentLogs > 0);
            }

        } catch (Exception e) {
            System.out.println("Error updating badges: " + e.getMessage());
        }
    }

    private void setActivePanel(JPanel panel) {
        if (activePanel != null) {
            activePanel.setBackground(sideBarColor);
        }
        activePanel = panel;
        activePanel.setBackground(activeColor);
    }

    private void handleMenuClick(JPanel panel) {
        setActivePanel(panel);

        if (panel == dashboardPanel) {
            admin_dashboard dashboardFrame = new admin_dashboard(adminId, adminName);
            dashboardFrame.setVisible(true);
            dashboardFrame.setLocationRelativeTo(null);
            this.dispose();
        } else if (panel == manageUsersPanel) {
            manage_users usersFrame = new manage_users(adminId, adminName);
            usersFrame.setVisible(true);
            usersFrame.setLocationRelativeTo(null);
            this.dispose();
        } else if (panel == manageAnnouncementPanel) {
            manage_announcement announcementFrame = new manage_announcement(adminId, adminName);
            announcementFrame.setVisible(true);
            announcementFrame.setLocationRelativeTo(null);
            this.dispose();
        } else if (panel == manageReportsPanel) {
            manage_reports reportsFrame = new manage_reports(adminId, adminName);
            reportsFrame.setVisible(true);
            reportsFrame.setLocationRelativeTo(null);
            this.dispose();
        } else if (panel == profilePanel) {
            BarterZone.Dashboard.admin.profile profileFrame = new BarterZone.Dashboard.admin.profile();
            profileFrame.setVisible(true);
            profileFrame.setLocationRelativeTo(null);
            this.dispose();
        } else if (panel == logsPanel) {
            logs logsFrame = new logs(adminId, adminName);
            logsFrame.setVisible(true);
            logsFrame.setLocationRelativeTo(null);
            this.dispose();
        } else if (panel == logoutPanel) {
            logout();
        }
    }

    private void logActivity(String action) {
        try {
            String sql = "INSERT INTO tbl_logs (admin_id, action, description, log_date) VALUES (?, ?, ?, datetime('now'))";
            db.addRecord(sql, adminId, action, "Admin " + adminName + ": " + action);
        } catch (Exception e) {
            System.out.println("Error logging activity: " + e.getMessage());
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            session.logout();
            landing.landing landingFrame = new landing.landing();
            landingFrame.setVisible(true);
            landingFrame.setLocationRelativeTo(null);
            this.dispose();
        }
    }
}
