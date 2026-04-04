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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import javax.swing.ListSelectionModel;

public class manage_trades extends javax.swing.JFrame {

    private int adminId;
    private String adminName;
    private user_session session;
    private config db;

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
    private JLabel qrFileNameLabel;
    private JButton addMethodButton;
    private JButton updateMethodButton;
    private JButton deleteMethodButton;
    private JButton activateMethodButton;
    private String uploadedQrPath = "";
    private int selectedMethodId = -1;

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

    // Trader 1 Panel Components
    private JPanel trader1Panel;
    private JLabel trader1NameLabel;
    private JLabel trader1PaymentNumberLabel;
    private JLabel trader1AccountNameLabel;
    private JLabel trader1StatusLabel;
    private JButton trader1ViewProofButton;
    private JButton trader1MarkPaidButton;
    private int trader1PaymentId = -1;

    // Trader 2 Panel Components
    private JPanel trader2Panel;
    private JLabel trader2NameLabel;
    private JLabel trader2PaymentNumberLabel;
    private JLabel trader2AccountNameLabel;
    private JLabel trader2StatusLabel;
    private JButton trader2ViewProofButton;
    private JButton trader2MarkPaidButton;
    private int trader2PaymentId = -1;

    // Overall Status
    private JLabel overallStatusLabel;

    // ========== REFUND MANAGEMENT TAB ==========
    private JPanel refundManagementPanel;
    private JComboBox<String> refundTradeComboBox;
    private JPanel refundTradersPanel;

    // Trader 1 Refund Panel
    private JPanel trader1RefundPanel;
    private JLabel trader1RefundNameLabel;
    private JLabel trader1AccountNumberLabel;
    private JLabel trader1AccountNameLabel;
    private JLabel trader1RefundStatusLabel;
    private JButton trader1UploadProofButton;
    private JButton trader1MarkRefundedButton;
    private int trader1RefundId = -1;
    private String trader1RefundProofPath = "";

    // Trader 2 Refund Panel
    private JPanel trader2RefundPanel;
    private JLabel trader2RefundNameLabel;
    private JLabel trader2AccountNumberLabel;
    private JLabel trader2AccountNameLabel;
    private JLabel trader2RefundStatusLabel;
    private JButton trader2UploadProofButton;
    private JButton trader2MarkRefundedButton;
    private int trader2RefundId = -1;
    private String trader2RefundProofPath = "";

    // Refund Overall Status
    private JLabel refundOverallStatusLabel;

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
    private Color textColor = new Color(80, 80, 80);

    private JPanel activePanel = null;
    private static final String QR_CODE_PATH = "src/BarterZone/resources/images/admin_qrcodes/";
    private static final String PROOF_PATH = "src/BarterZone/resources/images/payment_proofs/";
    private static final String REFUND_PROOF_PATH = "src/BarterZone/resources/images/refund_proofs/";

    public manage_trades(int adminId, String adminName) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.session = user_session.getInstance();
        this.db = new config();

        createDirectories();
        initComponents();
        setupSidePanel();
        setupHeader();
        setupContentPanel();
        loadPaymentMethods();
        loadTradesForDropdown();
        loadTradesForVerifyDropdown();
        loadTradesForRefundDropdown();
        updateBadges();

        setTitle("Manage Trades - " + adminName);
        setSize(1100, 750);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void createDirectories() {
        new File(QR_CODE_PATH).mkdirs();
        new File(PROOF_PATH).mkdirs();
        new File(REFUND_PROOF_PATH).mkdirs();
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

        // Tab 1: Payment Methods
        setupPaymentMethodsPanel();
        tabbedPane.addTab("Payment Methods", paymentMethodsPanel);

        // Tab 2: Trade Payment Setup
        setupTradeSetupPanel();
        tabbedPane.addTab("Trade Payment Setup", tradeSetupPanel);

        // Tab 3: Verify Payments (Step 3)
        setupVerifyPaymentsPanel();
        tabbedPane.addTab("Verify Payments", verifyPaymentsPanel);

        // Tab 4: Refund Management (Step 5)
        setupRefundManagementPanel();
        tabbedPane.addTab("Refund Management", refundManagementPanel);

        contentPanel.add(tabbedPane);
    }

    // ========== PAYMENT METHODS TAB ==========
    private void setupPaymentMethodsPanel() {
        paymentMethodsPanel = new JPanel();
        paymentMethodsPanel.setLayout(null);
        paymentMethodsPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Payment Methods");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(8, 78, 128));
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
        methodsTable.setRowHeight(30);
        methodsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        methodsTable.getTableHeader().setBackground(sideBarColor);
        methodsTable.getTableHeader().setForeground(Color.WHITE);
        methodsTable.setSelectionBackground(new Color(255, 235, 204));
        methodsTable.getColumnModel().getColumn(0).setMinWidth(0);
        methodsTable.getColumnModel().getColumn(0).setMaxWidth(0);

        methodsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = methodsTable.getSelectedRow();
                if (row != -1) {
                    int modelRow = methodsTable.convertRowIndexToModel(row);
                    selectedMethodId = Integer.parseInt(methodsTableModel.getValueAt(modelRow, 0).toString());
                    methodNameField.setText(methodsTableModel.getValueAt(modelRow, 1).toString());
                    accountNumberField.setText(methodsTableModel.getValueAt(modelRow, 2).toString());
                    accountNameField.setText(methodsTableModel.getValueAt(modelRow, 3).toString());
                    updateMethodButton.setEnabled(true);
                    deleteMethodButton.setEnabled(true);
                    activateMethodButton.setEnabled(true);
                }
            }
        });

        methodsScrollPane = new JScrollPane(methodsTable);
        methodsScrollPane.setBounds(20, 90, 800, 200);
        paymentMethodsPanel.add(methodsScrollPane);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBackground(new Color(250, 250, 250));
        formPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        formPanel.setBounds(20, 310, 800, 200);
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

        JLabel qrLabel = new JLabel("QR Code:");
        qrLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        qrLabel.setBounds(20, y, labelWidth, 30);
        formPanel.add(qrLabel);

        uploadQrButton = new JButton("Upload QR Code");
        uploadQrButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        uploadQrButton.setBackground(new Color(8, 78, 128));
        uploadQrButton.setForeground(Color.WHITE);
        uploadQrButton.setBounds(fieldX, y, 150, 30);
        uploadQrButton.setBorder(null);
        uploadQrButton.setFocusPainted(false);
        uploadQrButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        uploadQrButton.addActionListener(e -> uploadQRCode());
        formPanel.add(uploadQrButton);

        qrFileNameLabel = new JLabel();
        qrFileNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        qrFileNameLabel.setBounds(fieldX + 160, y, 200, 30);
        formPanel.add(qrFileNameLabel);

        addMethodButton = new JButton("ADD METHOD");
        addMethodButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addMethodButton.setBackground(successColor);
        addMethodButton.setForeground(Color.WHITE);
        addMethodButton.setBounds(20, 155, 120, 35);
        addMethodButton.setBorder(null);
        addMethodButton.setFocusPainted(false);
        addMethodButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addMethodButton.addActionListener(e -> addPaymentMethod());
        formPanel.add(addMethodButton);

        updateMethodButton = new JButton("UPDATE METHOD");
        updateMethodButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        updateMethodButton.setBackground(warningColor);
        updateMethodButton.setForeground(Color.WHITE);
        updateMethodButton.setBounds(150, 155, 130, 35);
        updateMethodButton.setBorder(null);
        updateMethodButton.setFocusPainted(false);
        updateMethodButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateMethodButton.setEnabled(false);
        updateMethodButton.addActionListener(e -> updatePaymentMethod());
        formPanel.add(updateMethodButton);

        activateMethodButton = new JButton("ACTIVATE");
        activateMethodButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        activateMethodButton.setBackground(successColor);
        activateMethodButton.setForeground(Color.WHITE);
        activateMethodButton.setBounds(290, 155, 100, 35);
        activateMethodButton.setBorder(null);
        activateMethodButton.setFocusPainted(false);
        activateMethodButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        activateMethodButton.setEnabled(false);
        activateMethodButton.addActionListener(e -> activatePaymentMethod());
        formPanel.add(activateMethodButton);

        deleteMethodButton = new JButton("DEACTIVATE");
        deleteMethodButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        deleteMethodButton.setBackground(errorColor);
        deleteMethodButton.setForeground(Color.WHITE);
        deleteMethodButton.setBounds(400, 155, 120, 35);
        deleteMethodButton.setBorder(null);
        deleteMethodButton.setFocusPainted(false);
        deleteMethodButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteMethodButton.setEnabled(false);
        deleteMethodButton.addActionListener(e -> deactivatePaymentMethod());
        formPanel.add(deleteMethodButton);
    }

    private void loadPaymentMethods() {
        methodsTableModel.setRowCount(0);
        String sql = "SELECT method_id, method_name, account_number, account_name, is_active FROM tbl_payment_methods WHERE admin_id = ? ORDER BY method_id DESC";
        List<Map<String, Object>> methods = db.fetchRecords(sql, adminId);

        for (Map<String, Object> method : methods) {
            int isActive = Integer.parseInt(method.get("is_active").toString());
            String status = isActive == 1 ? "Active" : "Inactive";
            methodsTableModel.addRow(new Object[]{
                method.get("method_id"),
                method.get("method_name"),
                method.get("account_number"),
                method.get("account_name") != null ? method.get("account_name") : "",
                status
            });
        }
    }

    private void uploadQRCode() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String savedPath = saveQRCodeImage(selectedFile.getAbsolutePath(), selectedFile.getName());
            if (!savedPath.isEmpty()) {
                uploadedQrPath = savedPath;
                qrFileNameLabel.setText(selectedFile.getName());
                JOptionPane.showMessageDialog(this, "QR Code uploaded successfully!", "Upload Complete", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private String saveQRCodeImage(String sourcePath, String originalFileName) {
        try {
            File directory = new File(QR_CODE_PATH);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String extension = "";
            String nameWithoutExt = originalFileName;
            int dotIndex = originalFileName.lastIndexOf(".");
            if (dotIndex > 0) {
                nameWithoutExt = originalFileName.substring(0, dotIndex);
                extension = originalFileName.substring(dotIndex);
            }

            String destinationPath = QR_CODE_PATH + originalFileName;
            File destFile = new File(destinationPath);
            int counter = 1;

            while (destFile.exists()) {
                String newFileName = nameWithoutExt + "_" + counter + extension;
                destinationPath = QR_CODE_PATH + newFileName;
                destFile = new File(destinationPath);
                counter++;
            }

            Files.copy(Paths.get(sourcePath), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
            return "BarterZone.resources.images.admin_qrcodes." + destFile.getName();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void addPaymentMethod() {
        String methodName = methodNameField.getText().trim();
        String accountNumber = accountNumberField.getText().trim();

        if (methodName.isEmpty() || accountNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Method Name and Account Number are required!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "INSERT INTO tbl_payment_methods (admin_id, method_name, account_number, account_name, qr_code_path, is_active, created_date) "
                + "VALUES (?, ?, ?, ?, ?, 1, datetime('now'))";
        db.addRecord(sql, adminId, methodName, accountNumber, accountNameField.getText().trim(), uploadedQrPath);

        JOptionPane.showMessageDialog(this, "Payment method added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        clearMethodForm();
        loadPaymentMethods();
        loadPaymentMethodsForCombo();
        logActivity("Added payment method: " + methodName);
    }

    private void updatePaymentMethod() {
        if (selectedMethodId == -1) {
            return;
        }

        String methodName = methodNameField.getText().trim();
        String accountNumber = accountNumberField.getText().trim();

        if (methodName.isEmpty() || accountNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Method Name and Account Number are required!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "UPDATE tbl_payment_methods SET method_name = ?, account_number = ?, account_name = ?, qr_code_path = ?, updated_date = datetime('now') "
                + "WHERE method_id = ?";
        db.updateRecord(sql, methodName, accountNumber, accountNameField.getText().trim(), uploadedQrPath, selectedMethodId);

        JOptionPane.showMessageDialog(this, "Payment method updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        clearMethodForm();
        loadPaymentMethods();
        loadPaymentMethodsForCombo();
        logActivity("Updated payment method ID: " + selectedMethodId);
    }

    private void activatePaymentMethod() {
        if (selectedMethodId == -1) {
            return;
        }

        String sql = "UPDATE tbl_payment_methods SET is_active = 1, updated_date = datetime('now') WHERE method_id = ?";
        db.updateRecord(sql, selectedMethodId);

        JOptionPane.showMessageDialog(this, "Payment method activated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        clearMethodForm();
        loadPaymentMethods();
        loadPaymentMethodsForCombo();
        logActivity("Activated payment method ID: " + selectedMethodId);
    }

    private void deactivatePaymentMethod() {
        if (selectedMethodId == -1) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Deactivate this payment method?\n\nIt will not be visible to traders.",
                "Confirm Deactivate",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_payment_methods SET is_active = 0, updated_date = datetime('now') WHERE method_id = ?";
            db.updateRecord(sql, selectedMethodId);

            JOptionPane.showMessageDialog(this, "Payment method deactivated!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearMethodForm();
            loadPaymentMethods();
            loadPaymentMethodsForCombo();
            logActivity("Deactivated payment method ID: " + selectedMethodId);
        }
    }

    private void clearMethodForm() {
        methodNameField.setText("");
        accountNumberField.setText("");
        accountNameField.setText("");
        qrFileNameLabel.setText("");
        uploadedQrPath = "";
        selectedMethodId = -1;
        methodsTable.clearSelection();
        updateMethodButton.setEnabled(false);
        deleteMethodButton.setEnabled(false);
        activateMethodButton.setEnabled(false);
    }

    // ========== TRADE PAYMENT SETUP TAB ==========
    private void setupTradeSetupPanel() {
        tradeSetupPanel = new JPanel();
        tradeSetupPanel.setLayout(null);
        tradeSetupPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Trade Payment Setup");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(8, 78, 128));
        titleLabel.setBounds(20, 20, 300, 30);
        tradeSetupPanel.add(titleLabel);

        JLabel selectLabel = new JLabel("Select Trade:");
        selectLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        selectLabel.setBounds(20, 70, 100, 30);
        tradeSetupPanel.add(selectLabel);

        tradeComboBox = new JComboBox<>();
        tradeComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tradeComboBox.setBounds(130, 70, 300, 30);
        tradeComboBox.addActionListener(e -> loadTradeDetails());
        tradeSetupPanel.add(tradeComboBox);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(null);
        infoPanel.setBackground(new Color(250, 250, 250));
        infoPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        infoPanel.setBounds(20, 120, 800, 150);
        tradeSetupPanel.add(infoPanel);

        tradeInfoLabel = new JLabel();
        tradeInfoLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tradeInfoLabel.setBounds(15, 10, 770, 25);
        infoPanel.add(tradeInfoLabel);

        trader1Label = new JLabel();
        trader1Label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trader1Label.setBounds(15, 40, 350, 25);
        infoPanel.add(trader1Label);

        trader2Label = new JLabel();
        trader2Label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trader2Label.setBounds(15, 65, 350, 25);
        infoPanel.add(trader2Label);

        item1Label = new JLabel();
        item1Label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        item1Label.setBounds(400, 40, 350, 25);
        infoPanel.add(item1Label);

        item2Label = new JLabel();
        item2Label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        item2Label.setBounds(400, 65, 350, 25);
        infoPanel.add(item2Label);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBackground(new Color(250, 250, 250));
        formPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        formPanel.setBounds(20, 290, 800, 200);
        tradeSetupPanel.add(formPanel);

        int y = 25;
        int labelWidth = 150;
        int fieldWidth = 250;
        int fieldX = 180;

        JLabel methodLabel = new JLabel("Select Payment Method:*");
        methodLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        methodLabel.setBounds(20, y, labelWidth, 30);
        formPanel.add(methodLabel);

        paymentMethodCombo = new JComboBox<>();
        paymentMethodCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        paymentMethodCombo.setBounds(fieldX, y, fieldWidth, 30);
        formPanel.add(paymentMethodCombo);
        y += 50;

        JLabel feeLabel = new JLabel("Service Fee (₱):*");
        feeLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        feeLabel.setBounds(20, y, labelWidth, 30);
        formPanel.add(feeLabel);

        serviceFeeField = new JTextField("15.00");
        serviceFeeField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        serviceFeeField.setBounds(fieldX, y, fieldWidth, 35);
        formPanel.add(serviceFeeField);
        y += 50;

        JLabel totalLabel = new JLabel("Total Amount (₱):*");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        totalLabel.setBounds(20, y, labelWidth, 30);
        formPanel.add(totalLabel);

        totalAmountField = new JTextField("215.00");
        totalAmountField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        totalAmountField.setBounds(fieldX, y, fieldWidth, 35);
        formPanel.add(totalAmountField);
        y += 60;

        saveTradePaymentButton = new JButton("SAVE TRADE PAYMENT SETTINGS");
        saveTradePaymentButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveTradePaymentButton.setBackground(successColor);
        saveTradePaymentButton.setForeground(Color.WHITE);
        saveTradePaymentButton.setBounds(250, y, 300, 45);
        saveTradePaymentButton.setBorder(null);
        saveTradePaymentButton.setFocusPainted(false);
        saveTradePaymentButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveTradePaymentButton.addActionListener(e -> saveTradePaymentSettings());
        formPanel.add(saveTradePaymentButton);

        loadPaymentMethodsForCombo();
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

    private void loadPaymentMethodsForCombo() {
        paymentMethodCombo.removeAllItems();
        String sql = "SELECT method_id, method_name FROM tbl_payment_methods WHERE admin_id = ? AND is_active = 1";
        List<Map<String, Object>> methods = db.fetchRecords(sql, adminId);

        paymentMethodCombo.addItem("-- Select Payment Method --");
        for (Map<String, Object> method : methods) {
            paymentMethodCombo.addItem(method.get("method_id") + " - " + method.get("method_name"));
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

            tradeInfoLabel.setText("Trade #" + selectedTradeId);
            trader1Label.setText("Trader 1: " + selectedOfferTraderName);
            trader2Label.setText("Trader 2: " + selectedTargetTraderName);
            item1Label.setText("Item: " + trade.get("offer_item"));
            item2Label.setText("Item: " + trade.get("target_item"));

            String feeSql = "SELECT service_fee, total_amount, method_id FROM tbl_payment_details WHERE trade_id = ? LIMIT 1";
            List<Map<String, Object>> feeResult = db.fetchRecords(feeSql, selectedTradeId);
            if (!feeResult.isEmpty()) {
                Map<String, Object> feeData = feeResult.get(0);
                serviceFeeField.setText(feeData.get("service_fee") != null ? feeData.get("service_fee").toString() : "15.00");
                totalAmountField.setText(feeData.get("total_amount") != null ? feeData.get("total_amount").toString() : "215.00");

                if (feeData.get("method_id") != null) {
                    int methodId = Integer.parseInt(feeData.get("method_id").toString());
                    for (int i = 0; i < paymentMethodCombo.getItemCount(); i++) {
                        String item = paymentMethodCombo.getItemAt(i);
                        if (item.startsWith(String.valueOf(methodId))) {
                            paymentMethodCombo.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            } else {
                serviceFeeField.setText("15.00");
                totalAmountField.setText("215.00");
            }
        }
    }

    private void saveTradePaymentSettings() {
        if (selectedTradeId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a trade first.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (paymentMethodCombo.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a payment method.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double serviceFee = Double.parseDouble(serviceFeeField.getText().trim());
            double totalAmount = Double.parseDouble(totalAmountField.getText().trim());
            String selectedMethod = paymentMethodCombo.getSelectedItem().toString();
            int methodId = Integer.parseInt(selectedMethod.substring(0, selectedMethod.indexOf(" -")));

            for (int traderId : new int[]{selectedOfferTraderId, selectedTargetTraderId}) {
                String checkSql = "SELECT COUNT(*) as count FROM tbl_payment_details WHERE trade_id = ? AND trader_id = ?";
                double count = db.getSingleValue(checkSql, selectedTradeId, traderId);

                if (count == 0) {
                    String insertSql = "INSERT INTO tbl_payment_details (trade_id, trader_id, method_id, service_fee, total_amount, created_date) "
                            + "VALUES (?, ?, ?, ?, ?, datetime('now'))";
                    db.addRecord(insertSql, selectedTradeId, traderId, methodId, serviceFee, totalAmount);
                } else {
                    String updateSql = "UPDATE tbl_payment_details SET method_id = ?, service_fee = ?, total_amount = ?, updated_date = datetime('now') "
                            + "WHERE trade_id = ? AND trader_id = ?";
                    db.updateRecord(updateSql, methodId, serviceFee, totalAmount, selectedTradeId, traderId);
                }
            }

            JOptionPane.showMessageDialog(this, "Trade payment settings saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            logActivity("Saved payment settings for Trade #" + selectedTradeId);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for fee and amount.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    // ========== VERIFY PAYMENTS TAB (STEP 3) ==========
    private void setupVerifyPaymentsPanel() {
        verifyPaymentsPanel = new JPanel();
        verifyPaymentsPanel.setLayout(null);
        verifyPaymentsPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Verify Payments - Step 3");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(8, 78, 128));
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
        tradersPaymentPanel.setBounds(20, 120, 800, 400);
        verifyPaymentsPanel.add(tradersPaymentPanel);

        // Trader 1 Panel
        trader1Panel = new JPanel();
        trader1Panel.setLayout(null);
        trader1Panel.setBackground(Color.WHITE);
        trader1Panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor), "TRADER 1"));
        trader1Panel.setBounds(20, 20, 360, 350);
        tradersPaymentPanel.add(trader1Panel);

        int py = 30;

        trader1NameLabel = new JLabel();
        trader1NameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        trader1NameLabel.setForeground(sideBarColor);
        trader1NameLabel.setBounds(15, py, 330, 25);
        trader1Panel.add(trader1NameLabel);
        py += 40;

        JLabel paymentNumTitle = new JLabel("Payment Number:");
        paymentNumTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        paymentNumTitle.setBounds(15, py, 120, 25);
        trader1Panel.add(paymentNumTitle);

        trader1PaymentNumberLabel = new JLabel("-");
        trader1PaymentNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trader1PaymentNumberLabel.setBounds(145, py, 200, 25);
        trader1Panel.add(trader1PaymentNumberLabel);
        py += 35;

        JLabel accNameTitle = new JLabel("Account Name:");
        accNameTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        accNameTitle.setBounds(15, py, 120, 25);
        trader1Panel.add(accNameTitle);

        trader1AccountNameLabel = new JLabel("-");
        trader1AccountNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trader1AccountNameLabel.setBounds(145, py, 200, 25);
        trader1Panel.add(trader1AccountNameLabel);
        py += 40;

        trader1ViewProofButton = new JButton("View Payment Proof");
        trader1ViewProofButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        trader1ViewProofButton.setBackground(warningColor);
        trader1ViewProofButton.setForeground(Color.WHITE);
        trader1ViewProofButton.setBounds(15, py, 160, 35);
        trader1ViewProofButton.setBorder(null);
        trader1ViewProofButton.setFocusPainted(false);
        trader1ViewProofButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        trader1ViewProofButton.setEnabled(false);
        trader1ViewProofButton.addActionListener(e -> viewPaymentProof(trader1PaymentId));
        trader1Panel.add(trader1ViewProofButton);

        trader1MarkPaidButton = new JButton("Mark as Paid");
        trader1MarkPaidButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        trader1MarkPaidButton.setBackground(successColor);
        trader1MarkPaidButton.setForeground(Color.WHITE);
        trader1MarkPaidButton.setBounds(185, py, 150, 35);
        trader1MarkPaidButton.setBorder(null);
        trader1MarkPaidButton.setFocusPainted(false);
        trader1MarkPaidButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        trader1MarkPaidButton.setEnabled(false);
        trader1MarkPaidButton.addActionListener(e -> markPaymentAsPaid(trader1PaymentId, selectedOfferTraderName));
        trader1Panel.add(trader1MarkPaidButton);
        py += 50;

        trader1StatusLabel = new JLabel();
        trader1StatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        trader1StatusLabel.setBounds(15, py, 330, 25);
        trader1Panel.add(trader1StatusLabel);

        // Trader 2 Panel
        trader2Panel = new JPanel();
        trader2Panel.setLayout(null);
        trader2Panel.setBackground(Color.WHITE);
        trader2Panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor), "TRADER 2"));
        trader2Panel.setBounds(420, 20, 360, 350);
        tradersPaymentPanel.add(trader2Panel);

        py = 30;

        trader2NameLabel = new JLabel();
        trader2NameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        trader2NameLabel.setForeground(sideBarColor);
        trader2NameLabel.setBounds(15, py, 330, 25);
        trader2Panel.add(trader2NameLabel);
        py += 40;

        JLabel paymentNumTitle2 = new JLabel("Payment Number:");
        paymentNumTitle2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        paymentNumTitle2.setBounds(15, py, 120, 25);
        trader2Panel.add(paymentNumTitle2);

        trader2PaymentNumberLabel = new JLabel("-");
        trader2PaymentNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trader2PaymentNumberLabel.setBounds(145, py, 200, 25);
        trader2Panel.add(trader2PaymentNumberLabel);
        py += 35;

        JLabel accNameTitle2 = new JLabel("Account Name:");
        accNameTitle2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        accNameTitle2.setBounds(15, py, 120, 25);
        trader2Panel.add(accNameTitle2);

        trader2AccountNameLabel = new JLabel("-");
        trader2AccountNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trader2AccountNameLabel.setBounds(145, py, 200, 25);
        trader2Panel.add(trader2AccountNameLabel);
        py += 40;

        trader2ViewProofButton = new JButton("View Payment Proof");
        trader2ViewProofButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        trader2ViewProofButton.setBackground(warningColor);
        trader2ViewProofButton.setForeground(Color.WHITE);
        trader2ViewProofButton.setBounds(15, py, 160, 35);
        trader2ViewProofButton.setBorder(null);
        trader2ViewProofButton.setFocusPainted(false);
        trader2ViewProofButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        trader2ViewProofButton.setEnabled(false);
        trader2ViewProofButton.addActionListener(e -> viewPaymentProof(trader2PaymentId));
        trader2Panel.add(trader2ViewProofButton);

        trader2MarkPaidButton = new JButton("Mark as Paid");
        trader2MarkPaidButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        trader2MarkPaidButton.setBackground(successColor);
        trader2MarkPaidButton.setForeground(Color.WHITE);
        trader2MarkPaidButton.setBounds(185, py, 150, 35);
        trader2MarkPaidButton.setBorder(null);
        trader2MarkPaidButton.setFocusPainted(false);
        trader2MarkPaidButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        trader2MarkPaidButton.setEnabled(false);
        trader2MarkPaidButton.addActionListener(e -> markPaymentAsPaid(trader2PaymentId, selectedTargetTraderName));
        trader2Panel.add(trader2MarkPaidButton);
        py += 50;

        trader2StatusLabel = new JLabel();
        trader2StatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        trader2StatusLabel.setBounds(15, py, 330, 25);
        trader2Panel.add(trader2StatusLabel);

        overallStatusLabel = new JLabel();
        overallStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        overallStatusLabel.setBounds(20, 380, 760, 30);
        tradersPaymentPanel.add(overallStatusLabel);
    }

    private void loadTradesForVerifyDropdown() {
        verifyTradeComboBox.removeAllItems();
        String sql = "SELECT t.trade_id, 'Trade #' || t.trade_id || ' - ' || u1.user_fullname || ' ↔ ' || u2.user_fullname as display "
                + "FROM tbl_trade t "
                + "LEFT JOIN tbl_users u1 ON t.offer_trader_id = u1.user_id "
                + "LEFT JOIN tbl_users u2 ON t.target_trader_id = u2.user_id "
                + "WHERE t.trade_status NOT IN ('completed', 'payment_verified', 'items_received', 'refund_pending') "
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

        String sql = "SELECT t.offer_trader_id, t.target_trader_id, "
                + "u1.user_fullname as offer_trader_name, u2.user_fullname as target_trader_name "
                + "FROM tbl_trade t "
                + "LEFT JOIN tbl_users u1 ON t.offer_trader_id = u1.user_id "
                + "LEFT JOIN tbl_users u2 ON t.target_trader_id = u2.user_id "
                + "WHERE t.trade_id = ?";

        List<Map<String, Object>> result = db.fetchRecords(sql, tradeId);
        if (!result.isEmpty()) {
            Map<String, Object> trade = result.get(0);
            selectedOfferTraderId = Integer.parseInt(trade.get("offer_trader_id").toString());
            selectedTargetTraderId = Integer.parseInt(trade.get("target_trader_id").toString());
            selectedOfferTraderName = trade.get("offer_trader_name").toString();
            selectedTargetTraderName = trade.get("target_trader_name").toString();

            trader1NameLabel.setText(selectedOfferTraderName);
            trader2NameLabel.setText(selectedTargetTraderName);

            loadTraderPaymentData(tradeId, selectedOfferTraderId,
                    trader1PaymentNumberLabel, trader1AccountNameLabel,
                    trader1StatusLabel, trader1ViewProofButton, trader1MarkPaidButton);

            loadTraderPaymentData(tradeId, selectedTargetTraderId,
                    trader2PaymentNumberLabel, trader2AccountNameLabel,
                    trader2StatusLabel, trader2ViewProofButton, trader2MarkPaidButton);

            checkOverallVerificationStatus(tradeId);
        }
    }

    private void loadTraderPaymentData(int tradeId, int traderId,
            JLabel paymentNumberLabel, JLabel accountNameLabel,
            JLabel statusLabel, JButton viewButton, JButton markButton) {
        String sql = "SELECT payment_id, my_number, acc_name, payment_proof, payment_submitted, payment_verified, payment_verified_date "
                + "FROM tbl_payment_details WHERE trade_id = ? AND trader_id = ?";
        List<Map<String, Object>> result = db.fetchRecords(sql, tradeId, traderId);

        if (!result.isEmpty()) {
            Map<String, Object> payment = result.get(0);
            int paymentId = Integer.parseInt(payment.get("payment_id").toString());
            String myNumber = payment.get("my_number") != null ? payment.get("my_number").toString() : "";
            String accName = payment.get("acc_name") != null ? payment.get("acc_name").toString() : "";
            int paymentSubmitted = Integer.parseInt(payment.get("payment_submitted").toString());
            int paymentVerified = Integer.parseInt(payment.get("payment_verified").toString());
            String proofPath = payment.get("payment_proof") != null ? payment.get("payment_proof").toString() : "";

            if (traderId == selectedOfferTraderId) {
                trader1PaymentId = paymentId;
            } else {
                trader2PaymentId = paymentId;
            }

            paymentNumberLabel.setText(myNumber.isEmpty() ? "Not submitted" : myNumber);
            accountNameLabel.setText(accName.isEmpty() ? "Not submitted" : accName);

            if (paymentVerified == 1) {
                statusLabel.setText("✓ VERIFIED");
                statusLabel.setForeground(successColor);
                viewButton.setEnabled(!proofPath.isEmpty());
                markButton.setEnabled(false);
                markButton.setText("Verified");
                markButton.setBackground(new Color(150, 150, 150));
            } else if (paymentSubmitted == 1) {
                statusLabel.setText("⏳ PENDING VERIFICATION");
                statusLabel.setForeground(warningColor);
                viewButton.setEnabled(!proofPath.isEmpty());
                markButton.setEnabled(true);
                markButton.setText("Mark as Paid");
                markButton.setBackground(successColor);
            } else {
                statusLabel.setText("❌ NOT SUBMITTED");
                statusLabel.setForeground(errorColor);
                viewButton.setEnabled(false);
                markButton.setEnabled(false);
                markButton.setText("Mark as Paid");
                markButton.setBackground(successColor);
            }
        } else {
            paymentNumberLabel.setText("Not submitted");
            accountNameLabel.setText("Not submitted");
            statusLabel.setText("❌ NO RECORD");
            statusLabel.setForeground(errorColor);
            viewButton.setEnabled(false);
            markButton.setEnabled(false);
        }
    }

    private void viewPaymentProof(int paymentId) {
        if (paymentId == -1) {
            JOptionPane.showMessageDialog(this, "No payment record found.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "SELECT payment_proof FROM tbl_payment_details WHERE payment_id = ?";
        List<Map<String, Object>> result = db.fetchRecords(sql, paymentId);

        if (!result.isEmpty() && result.get(0).get("payment_proof") != null) {
            String proofPath = result.get(0).get("payment_proof").toString();
            String fullPath = "src/" + proofPath;
            File imgFile = new File(fullPath);

            if (imgFile.exists()) {
                try {
                    ImageIcon icon = new ImageIcon(fullPath);
                    Image img = icon.getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH);
                    JOptionPane.showMessageDialog(this, new JLabel(new ImageIcon(img)), "Payment Proof", JOptionPane.PLAIN_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error loading image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Proof image not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No payment proof uploaded yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void markPaymentAsPaid(int paymentId, String traderName) {
        if (paymentId == -1) {
            JOptionPane.showMessageDialog(this, "No payment record found.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new java.awt.Dimension(400, 120));

        JLabel notesLabel = new JLabel("Admin Notes (Optional):");
        notesLabel.setBounds(20, 20, 150, 25);
        panel.add(notesLabel);

        JTextArea notesArea = new JTextArea();
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(notesArea);
        scrollPane.setBounds(20, 50, 360, 50);
        panel.add(scrollPane);

        int confirm = JOptionPane.showConfirmDialog(this, panel,
                "Verify Payment for " + traderName,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.OK_OPTION) {
            String adminNotes = notesArea.getText().trim();

            String sql = "UPDATE tbl_payment_details SET payment_verified = 1, payment_verified_date = datetime('now'), admin_notes = ? WHERE payment_id = ?";
            db.updateRecord(sql, adminNotes.isEmpty() ? null : adminNotes, paymentId);

            String checkSql = "SELECT COUNT(*) as verified_count FROM tbl_payment_details WHERE trade_id = ? AND payment_verified = 1";
            double verifiedCount = db.getSingleValue(checkSql, selectedTradeId);

            if (verifiedCount == 2) {
                String updateTradeSql = "UPDATE tbl_trade SET payment_verified = 1, trade_status = 'payment_verified' WHERE trade_id = ?";
                db.updateRecord(updateTradeSql, selectedTradeId);

                JOptionPane.showMessageDialog(this,
                        "Both traders' payments have been verified!\n\n"
                        + "The trade can now proceed to Step 4.",
                        "Payments Verified",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        traderName + "'s payment has been verified!\n\n"
                        + "Waiting for the other trader's payment verification.",
                        "Payment Verified",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            logActivity("Verified payment for " + traderName + " in Trade #" + selectedTradeId);
            loadPaymentVerificationData();
        }
    }

    private void checkOverallVerificationStatus(int tradeId) {
        String sql = "SELECT COUNT(*) as verified_count FROM tbl_payment_details WHERE trade_id = ? AND payment_verified = 1";
        double verifiedCount = db.getSingleValue(sql, tradeId);

        if (verifiedCount == 2) {
            overallStatusLabel.setText("✓ BOTH TRADERS HAVE BEEN VERIFIED! Ready to proceed to Step 4.");
            overallStatusLabel.setForeground(successColor);
        } else if (verifiedCount == 1) {
            overallStatusLabel.setText("⏳ One trader verified. Waiting for the other trader's payment verification.");
            overallStatusLabel.setForeground(warningColor);
        } else {
            overallStatusLabel.setText("❌ No payments verified yet. Please verify each trader's payment.");
            overallStatusLabel.setForeground(errorColor);
        }
    }

    private void resetVerificationPanel() {
        trader1NameLabel.setText("");
        trader1PaymentNumberLabel.setText("-");
        trader1AccountNameLabel.setText("-");
        trader1StatusLabel.setText("");
        trader1ViewProofButton.setEnabled(false);
        trader1MarkPaidButton.setEnabled(false);

        trader2NameLabel.setText("");
        trader2PaymentNumberLabel.setText("-");
        trader2AccountNameLabel.setText("-");
        trader2StatusLabel.setText("");
        trader2ViewProofButton.setEnabled(false);
        trader2MarkPaidButton.setEnabled(false);

        overallStatusLabel.setText("");
        trader1PaymentId = -1;
        trader2PaymentId = -1;
    }

    // ========== REFUND MANAGEMENT TAB (STEP 5) ==========
    private void setupRefundManagementPanel() {
        refundManagementPanel = new JPanel();
        refundManagementPanel.setLayout(null);
        refundManagementPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Refund Management - Step 5");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(8, 78, 128));
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
        refundTradersPanel.setBounds(20, 120, 800, 420);
        refundManagementPanel.add(refundTradersPanel);

        // Trader 1 Refund Panel
        trader1RefundPanel = new JPanel();
        trader1RefundPanel.setLayout(null);
        trader1RefundPanel.setBackground(Color.WHITE);
        trader1RefundPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor), "TRADER 1 REFUND"));
        trader1RefundPanel.setBounds(20, 20, 360, 370);
        refundTradersPanel.add(trader1RefundPanel);

        int rp = 25;

        trader1RefundNameLabel = new JLabel();
        trader1RefundNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        trader1RefundNameLabel.setForeground(sideBarColor);
        trader1RefundNameLabel.setBounds(15, rp, 330, 25);
        trader1RefundPanel.add(trader1RefundNameLabel);
        rp += 40;

        JLabel accNumTitle = new JLabel("Account Number:");
        accNumTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        accNumTitle.setBounds(15, rp, 120, 25);
        trader1RefundPanel.add(accNumTitle);

        trader1AccountNumberLabel = new JLabel("-");
        trader1AccountNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trader1AccountNumberLabel.setBounds(145, rp, 200, 25);
        trader1RefundPanel.add(trader1AccountNumberLabel);
        rp += 35;

        JLabel accNameTitle = new JLabel("Account Name:");
        accNameTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        accNameTitle.setBounds(15, rp, 120, 25);
        trader1RefundPanel.add(accNameTitle);

        trader1AccountNameLabel = new JLabel("-");
        trader1AccountNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trader1AccountNameLabel.setBounds(145, rp, 200, 25);
        trader1RefundPanel.add(trader1AccountNameLabel);
        rp += 40;

        trader1UploadProofButton = new JButton("Upload Refund Proof");
        trader1UploadProofButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        trader1UploadProofButton.setBackground(themeColor);
        trader1UploadProofButton.setForeground(Color.WHITE);
        trader1UploadProofButton.setBounds(15, rp, 160, 35);
        trader1UploadProofButton.setBorder(null);
        trader1UploadProofButton.setFocusPainted(false);
        trader1UploadProofButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        trader1UploadProofButton.setEnabled(false);
        trader1UploadProofButton.addActionListener(e -> uploadRefundProof(trader1RefundId, trader1RefundNameLabel.getText()));
        trader1RefundPanel.add(trader1UploadProofButton);

        trader1MarkRefundedButton = new JButton("Mark as Refunded");
        trader1MarkRefundedButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        trader1MarkRefundedButton.setBackground(successColor);
        trader1MarkRefundedButton.setForeground(Color.WHITE);
        trader1MarkRefundedButton.setBounds(185, rp, 150, 35);
        trader1MarkRefundedButton.setBorder(null);
        trader1MarkRefundedButton.setFocusPainted(false);
        trader1MarkRefundedButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        trader1MarkRefundedButton.setEnabled(false);
        trader1MarkRefundedButton.addActionListener(e -> markRefundAsProcessed(trader1RefundId, trader1RefundNameLabel.getText()));
        trader1RefundPanel.add(trader1MarkRefundedButton);
        rp += 50;

        trader1RefundStatusLabel = new JLabel();
        trader1RefundStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        trader1RefundStatusLabel.setBounds(15, rp, 330, 25);
        trader1RefundPanel.add(trader1RefundStatusLabel);

        // Trader 2 Refund Panel
        trader2RefundPanel = new JPanel();
        trader2RefundPanel.setLayout(null);
        trader2RefundPanel.setBackground(Color.WHITE);
        trader2RefundPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor), "TRADER 2 REFUND"));
        trader2RefundPanel.setBounds(420, 20, 360, 370);
        refundTradersPanel.add(trader2RefundPanel);

        rp = 25;

        trader2RefundNameLabel = new JLabel();
        trader2RefundNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        trader2RefundNameLabel.setForeground(sideBarColor);
        trader2RefundNameLabel.setBounds(15, rp, 330, 25);
        trader2RefundPanel.add(trader2RefundNameLabel);
        rp += 40;

        JLabel accNumTitle2 = new JLabel("Account Number:");
        accNumTitle2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        accNumTitle2.setBounds(15, rp, 120, 25);
        trader2RefundPanel.add(accNumTitle2);

        trader2AccountNumberLabel = new JLabel("-");
        trader2AccountNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trader2AccountNumberLabel.setBounds(145, rp, 200, 25);
        trader2RefundPanel.add(trader2AccountNumberLabel);
        rp += 35;

        JLabel accNameTitle2 = new JLabel("Account Name:");
        accNameTitle2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        accNameTitle2.setBounds(15, rp, 120, 25);
        trader2RefundPanel.add(accNameTitle2);

        trader2AccountNameLabel = new JLabel("-");
        trader2AccountNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trader2AccountNameLabel.setBounds(145, rp, 200, 25);
        trader2RefundPanel.add(trader2AccountNameLabel);
        rp += 40;

        trader2UploadProofButton = new JButton("Upload Refund Proof");
        trader2UploadProofButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        trader2UploadProofButton.setBackground(themeColor);
        trader2UploadProofButton.setForeground(Color.WHITE);
        trader2UploadProofButton.setBounds(15, rp, 160, 35);
        trader2UploadProofButton.setBorder(null);
        trader2UploadProofButton.setFocusPainted(false);
        trader2UploadProofButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        trader2UploadProofButton.setEnabled(false);
        trader2UploadProofButton.addActionListener(e -> uploadRefundProof(trader2RefundId, trader2RefundNameLabel.getText()));
        trader2RefundPanel.add(trader2UploadProofButton);

        trader2MarkRefundedButton = new JButton("Mark as Refunded");
        trader2MarkRefundedButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        trader2MarkRefundedButton.setBackground(successColor);
        trader2MarkRefundedButton.setForeground(Color.WHITE);
        trader2MarkRefundedButton.setBounds(185, rp, 150, 35);
        trader2MarkRefundedButton.setBorder(null);
        trader2MarkRefundedButton.setFocusPainted(false);
        trader2MarkRefundedButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        trader2MarkRefundedButton.setEnabled(false);
        trader2MarkRefundedButton.addActionListener(e -> markRefundAsProcessed(trader2RefundId, trader2RefundNameLabel.getText()));
        trader2RefundPanel.add(trader2MarkRefundedButton);
        rp += 50;

        trader2RefundStatusLabel = new JLabel();
        trader2RefundStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        trader2RefundStatusLabel.setBounds(15, rp, 330, 25);
        trader2RefundPanel.add(trader2RefundStatusLabel);

        // Overall Refund Status
        refundOverallStatusLabel = new JLabel();
        refundOverallStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        refundOverallStatusLabel.setBounds(20, 400, 760, 30);
        refundTradersPanel.add(refundOverallStatusLabel);
    }

    private void loadTradesForRefundDropdown() {
        refundTradeComboBox.removeAllItems();
        String sql = "SELECT t.trade_id, 'Trade #' || t.trade_id || ' - ' || u1.user_fullname || ' ↔ ' || u2.user_fullname as display "
                + "FROM tbl_trade t "
                + "LEFT JOIN tbl_users u1 ON t.offer_trader_id = u1.user_id "
                + "LEFT JOIN tbl_users u2 ON t.target_trader_id = u2.user_id "
                + "WHERE t.trade_status IN ('items_received', 'refund_pending') "
                + "ORDER BY t.trade_id DESC";
        List<Map<String, Object>> trades = db.fetchRecords(sql);

        for (Map<String, Object> trade : trades) {
            refundTradeComboBox.addItem(trade.get("display").toString());
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

        String sql = "SELECT t.offer_trader_id, t.target_trader_id, "
                + "u1.user_fullname as offer_trader_name, u2.user_fullname as target_trader_name "
                + "FROM tbl_trade t "
                + "LEFT JOIN tbl_users u1 ON t.offer_trader_id = u1.user_id "
                + "LEFT JOIN tbl_users u2 ON t.target_trader_id = u2.user_id "
                + "WHERE t.trade_id = ?";

        List<Map<String, Object>> result = db.fetchRecords(sql, tradeId);
        if (!result.isEmpty()) {
            Map<String, Object> trade = result.get(0);
            selectedOfferTraderId = Integer.parseInt(trade.get("offer_trader_id").toString());
            selectedTargetTraderId = Integer.parseInt(trade.get("target_trader_id").toString());
            selectedOfferTraderName = trade.get("offer_trader_name").toString();
            selectedTargetTraderName = trade.get("target_trader_name").toString();

            trader1RefundNameLabel.setText(selectedOfferTraderName);
            trader2RefundNameLabel.setText(selectedTargetTraderName);

            loadTraderRefundData(tradeId, selectedOfferTraderId, trader1AccountNumberLabel, trader1AccountNameLabel,
                    trader1RefundStatusLabel, trader1UploadProofButton, trader1MarkRefundedButton);

            loadTraderRefundData(tradeId, selectedTargetTraderId, trader2AccountNumberLabel, trader2AccountNameLabel,
                    trader2RefundStatusLabel, trader2UploadProofButton, trader2MarkRefundedButton);

            checkOverallRefundStatus(tradeId);
        }
    }

    private void loadTraderRefundData(int tradeId, int traderId, JLabel accountNumberLabel, JLabel accountNameLabel,
            JLabel statusLabel, JButton uploadButton, JButton markButton) {
        String sql = "SELECT refund_id, account_number, account_name, qr_code_path, refund_proof, is_refunded FROM tbl_refund WHERE trade_id = ? AND user_id = ?";
        List<Map<String, Object>> result = db.fetchRecords(sql, tradeId, traderId);

        if (!result.isEmpty()) {
            Map<String, Object> refund = result.get(0);
            int refundId = Integer.parseInt(refund.get("refund_id").toString());
            String accountNumber = refund.get("account_number").toString();
            String accountName = refund.get("account_name").toString();
            String refundProof = refund.get("refund_proof") != null ? refund.get("refund_proof").toString() : "";
            int isRefunded = Integer.parseInt(refund.get("is_refunded").toString());

            if (traderId == selectedOfferTraderId) {
                trader1RefundId = refundId;
                trader1RefundProofPath = refundProof;
            } else {
                trader2RefundId = refundId;
                trader2RefundProofPath = refundProof;
            }

            accountNumberLabel.setText(accountNumber);
            accountNameLabel.setText(accountName);

            if (isRefunded == 1) {
                statusLabel.setText("✓ REFUND COMPLETED");
                statusLabel.setForeground(successColor);
                uploadButton.setEnabled(false);
                markButton.setEnabled(false);
            } else if (!refundProof.isEmpty()) {
                statusLabel.setText("⏳ REFUND PROOF UPLOADED - Waiting for trader confirmation");
                statusLabel.setForeground(warningColor);
                uploadButton.setEnabled(false);
                markButton.setEnabled(true);
            } else {
                statusLabel.setText("❌ REFUND NOT PROCESSED YET");
                statusLabel.setForeground(errorColor);
                uploadButton.setEnabled(true);
                markButton.setEnabled(false);
            }
        } else {
            accountNumberLabel.setText("Not submitted");
            accountNameLabel.setText("Not submitted");
            statusLabel.setText("❌ REFUND DETAILS NOT SUBMITTED");
            statusLabel.setForeground(errorColor);
            uploadButton.setEnabled(false);
            markButton.setEnabled(false);
        }
    }

    private void uploadRefundProof(int refundId, String traderName) {
        if (refundId == -1) {
            JOptionPane.showMessageDialog(this, "No refund record found for this trader.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String savedPath = saveRefundProofImage(selectedFile.getAbsolutePath(), selectedFile.getName());

            if (!savedPath.isEmpty()) {
                String sql = "UPDATE tbl_refund SET refund_proof = ?, updated_date = datetime('now') WHERE refund_id = ?";
                db.updateRecord(sql, savedPath, refundId);

                JOptionPane.showMessageDialog(this, "Refund proof uploaded successfully for " + traderName + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
                logActivity("Uploaded refund proof for " + traderName + " in Trade #" + selectedTradeId);
                loadRefundData();
            }
        }
    }

    private String saveRefundProofImage(String sourcePath, String originalFileName) {
        try {
            File directory = new File(REFUND_PROOF_PATH);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String extension = "";
            String nameWithoutExt = originalFileName;
            int dotIndex = originalFileName.lastIndexOf(".");
            if (dotIndex > 0) {
                nameWithoutExt = originalFileName.substring(0, dotIndex);
                extension = originalFileName.substring(dotIndex);
            }

            String destinationPath = REFUND_PROOF_PATH + originalFileName;
            File destFile = new File(destinationPath);
            int counter = 1;

            while (destFile.exists()) {
                String newFileName = nameWithoutExt + "_" + counter + extension;
                destinationPath = REFUND_PROOF_PATH + newFileName;
                destFile = new File(destinationPath);
                counter++;
            }

            Files.copy(Paths.get(sourcePath), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
            return "BarterZone.resources.images.refund_proofs." + destFile.getName();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void markRefundAsProcessed(int refundId, String traderName) {
        if (refundId == -1) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Mark refund as processed for " + traderName + "?\n\n"
                + "This will notify the trader that their refund has been sent.",
                "Confirm Refund",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_refund SET is_refunded = 1, refund_confirmed_date = datetime('now'), updated_date = datetime('now') WHERE refund_id = ?";
            db.updateRecord(sql, refundId);

            JOptionPane.showMessageDialog(this, "Refund marked as processed for " + traderName + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
            logActivity("Marked refund as processed for " + traderName + " in Trade #" + selectedTradeId);

            checkOverallRefundStatus(selectedTradeId);
            loadRefundData();
        }
    }

    private void checkOverallRefundStatus(int tradeId) {
        String sql = "SELECT COUNT(*) as refunded_count FROM tbl_refund WHERE trade_id = ? AND is_refunded = 1";
        double refundedCount = db.getSingleValue(sql, tradeId);

        if (refundedCount == 2) {
            refundOverallStatusLabel.setText("✓ BOTH REFUNDS HAVE BEEN PROCESSED! Ready to complete the trade.");
            refundOverallStatusLabel.setForeground(successColor);

            // Update trade status to ready for completion
            String updateSql = "UPDATE tbl_trade SET trade_status = 'ready_for_completion' WHERE trade_id = ?";
            db.updateRecord(updateSql, tradeId);

            int completeConfirm = JOptionPane.showConfirmDialog(this,
                    "Both refunds have been processed!\n\n"
                    + "Do you want to mark this trade as COMPLETED now?\n"
                    + "This will move the trade to history.",
                    "Complete Trade",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (completeConfirm == JOptionPane.YES_OPTION) {
                completeTrade(tradeId);
            }
        } else if (refundedCount == 1) {
            refundOverallStatusLabel.setText("⏳ One refund processed. Waiting for the other trader's refund confirmation.");
            refundOverallStatusLabel.setForeground(warningColor);
        } else {
            refundOverallStatusLabel.setText("❌ No refunds processed yet. Upload proof and mark as refunded for each trader.");
            refundOverallStatusLabel.setForeground(errorColor);
        }
    }

    private void completeTrade(int tradeId) {
        String getSql = "SELECT * FROM tbl_trade WHERE trade_id = ?";
        List<Map<String, Object>> trade = db.fetchRecords(getSql, tradeId);

        if (!trade.isEmpty()) {
            Map<String, Object> t = trade.get(0);

            String historySql = "INSERT INTO tbl_trade_history "
                    + "(trade_id, offer_trader_id, target_trader_id, offer_item_id, "
                    + "target_item_id, trade_status, trade_DateRequest, trade_DateCompleted) "
                    + "VALUES (?, ?, ?, ?, ?, 'completed', ?, datetime('now'))";

            db.addRecord(historySql,
                    tradeId,
                    t.get("offer_trader_id"),
                    t.get("target_trader_id"),
                    t.get("offer_item_id"),
                    t.get("target_item_id"),
                    t.get("trade_DateRequest"));

            String deleteSql = "DELETE FROM tbl_trade WHERE trade_id = ?";
            db.deleteRecord(deleteSql, tradeId);

            JOptionPane.showMessageDialog(this,
                    "TRADE COMPLETED SUCCESSFULLY!\n\n"
                    + "The trade has been moved to history.",
                    "Trade Complete",
                    JOptionPane.INFORMATION_MESSAGE);

            logActivity("Completed Trade #" + tradeId);
            loadTradesForRefundDropdown();
            resetRefundPanel();
        }
    }

    private void resetRefundPanel() {
        trader1RefundNameLabel.setText("");
        trader1AccountNumberLabel.setText("-");
        trader1AccountNameLabel.setText("-");
        trader1RefundStatusLabel.setText("");
        trader1UploadProofButton.setEnabled(false);
        trader1MarkRefundedButton.setEnabled(false);

        trader2RefundNameLabel.setText("");
        trader2AccountNumberLabel.setText("-");
        trader2AccountNameLabel.setText("-");
        trader2RefundStatusLabel.setText("");
        trader2UploadProofButton.setEnabled(false);
        trader2MarkRefundedButton.setEnabled(false);

        refundOverallStatusLabel.setText("");
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
