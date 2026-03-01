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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

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
    
    // Tabbed pane for different trade statuses
    private JTabbedPane tabbedPane;
    
    // Tables for different trade statuses
    private JScrollPane pendingScrollPane;
    private javax.swing.JTable pendingTable;
    private DefaultTableModel pendingTableModel;
    
    private JScrollPane activeScrollPane;
    private javax.swing.JTable activeTable;
    private DefaultTableModel activeTableModel;
    
    private JScrollPane completedScrollPane;
    private javax.swing.JTable completedTable;
    private DefaultTableModel completedTableModel;
    
    private JScrollPane disputedScrollPane;
    private javax.swing.JTable disputedTable;
    private DefaultTableModel disputedTableModel;
    
    // Filter components
    private JPanel filterPanel;
    private JTextField searchField;
    private JButton refreshButton;
    
    // Trade details panel
    private JPanel detailsPanel;
    private JTextArea tradeDetailsArea;
    private JScrollPane detailsScrollPane;
    
    // Action buttons
    private JPanel actionPanel;
    private JButton verifyPaymentButton;
    private JButton markPaidButton;
    private JButton processRefundButton;
    private JButton resolveDisputeButton;
    private JButton cancelTradeButton;
    private JButton viewMessagesButton;
    
    // Table row sorter
    private TableRowSorter<DefaultTableModel> pendingRowSorter;
    private TableRowSorter<DefaultTableModel> activeRowSorter;
    private TableRowSorter<DefaultTableModel> completedRowSorter;
    private TableRowSorter<DefaultTableModel> disputedRowSorter;
    
    // Selected trade data
    private int selectedTradeId = -1;
    private String selectedTradeStatus = "";
    private int selectedTabIndex = 0;
    
    // Colors - Matching profile.java theme (dark blue/gold)
    private Color sideBarColor = new Color(8, 78, 128); // Darker blue for admin
    private Color hoverColor = new Color(20, 100, 150);
    private Color activeColor = new Color(0, 60, 100);
    private Color accentColor = new Color(255, 215, 0); // Gold accent
    private Color badgeColor = new Color(204, 0, 0);
    private Color headerGradientStart = new Color(8, 78, 128);
    private Color headerGradientEnd = new Color(0, 45, 80);
    
    // Tab colors
    private Color pendingColor = new Color(255, 153, 0); // Orange for pending
    private Color activeColor2 = new Color(8, 78, 128); // Dark blue for active
    private Color completedColor = new Color(46, 125, 50); // Green for completed
    private Color disputedColor = new Color(204, 0, 0); // Red for disputed
    
    private JPanel activePanel = null;

    public manage_trades(int adminId, String adminName) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.session = user_session.getInstance();
        this.db = new config();
        
        initComponents();
        setupSidePanel();
        setupHeader();
        setupContentPanel();
        loadTradeData();
        updateBadges();
        
        setTitle("Manage Trades - " + adminName);
        setSize(1100, 650);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Side Panel
        sidePanel = new JPanel();
        sidePanel.setLayout(null);
        sidePanel.setBackground(sideBarColor);
        sidePanel.setBounds(0, 0, 220, 650);
        sidePanel.setBorder(new LineBorder(new Color(8, 78, 128), 1, true));
        getContentPane().add(sidePanel);

        // Header Panel
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

        // Main Content Panel
        contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(new Color(245, 245, 250));
        contentPanel.setBounds(220, 70, 880, 580);
        getContentPane().add(contentPanel);
    }

    private void setupSidePanel() {
        // Admin Avatar and Name
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

        // Menu Items
        int menuY = 180;
        int menuHeight = 45;

        // Dashboard
        dashboardPanel = createMenuItem(20, menuY, 180, menuHeight);
        dashboardLabel = createMenuItemLabel(dashboardPanel, "Dashboard", 20, 12);
        menuY += menuHeight;

        // Manage Users
        manageUsersPanel = createMenuItem(20, menuY, 180, menuHeight);
        manageUsersLabel = createMenuItemLabel(manageUsersPanel, "Manage Users", 20, 12);
        usersBadge = createBadge(manageUsersPanel, 140, 10, 30, 20);
        menuY += menuHeight;

        // Manage Announcement
        manageAnnouncementPanel = createMenuItem(20, menuY, 180, menuHeight);
        manageAnnouncementLabel = createMenuItemLabel(manageAnnouncementPanel, "Manage Announcement", 20, 12);
        announcementBadge = createBadge(manageAnnouncementPanel, 140, 10, 30, 20);
        menuY += menuHeight;

        // Manage Trades
        manageTradesPanel = createMenuItem(20, menuY, 180, menuHeight);
        manageTradesLabel = createMenuItemLabel(manageTradesPanel, "Manage Trades", 20, 12);
        tradesBadge = createBadge(manageTradesPanel, 140, 10, 30, 20);
        menuY += menuHeight;

        // Manage Reports
        manageReportsPanel = createMenuItem(20, menuY, 180, menuHeight);
        manageReportsLabel = createMenuItemLabel(manageReportsPanel, "Manage Reports", 20, 12);
        reportsBadge = createBadge(manageReportsPanel, 140, 10, 30, 20);
        menuY += menuHeight;

        // Profile
        profilePanel = createMenuItem(20, menuY, 180, menuHeight);
        profileLabel = createMenuItemLabel(profilePanel, "Profile", 20, 12);
        menuY += menuHeight;

        // Logs
        logsPanel = createMenuItem(20, menuY, 180, menuHeight);
        logsLabel = createMenuItemLabel(logsPanel, "Logs", 20, 12);
        logsBadge = createBadge(logsPanel, 140, 10, 30, 20);
        menuY += menuHeight;

        // Logout
        logoutPanel = createMenuItem(20, menuY, 180, menuHeight);
        logoutLabel = createMenuItemLabel(logoutPanel, "Logout", 20, 12);

        // Set active panel to Manage Trades
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
        // Filter Panel
        filterPanel = new JPanel();
        filterPanel.setLayout(null);
        filterPanel.setBackground(new Color(250, 250, 250));
        filterPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        filterPanel.setBounds(20, 15, 840, 60);
        contentPanel.add(filterPanel);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchLabel.setBounds(15, 20, 60, 25);
        filterPanel.add(searchLabel);

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setBounds(80, 20, 250, 25);
        searchField.setBorder(new LineBorder(new Color(200, 200, 200)));
        filterPanel.add(searchField);

        refreshButton = new JButton("Refresh Data");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshButton.setBackground(accentColor);
        refreshButton.setForeground(new Color(8, 78, 128));
        refreshButton.setBounds(340, 20, 120, 25);
        refreshButton.setBorder(null);
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> {
            loadTradeData();
            updateBadges();
        });
        filterPanel.add(refreshButton);

        // Stats Summary
        JLabel statsLabel = new JLabel("Total Fees Collected: ₱" + String.format("%.2f", getTotalFees()));
        statsLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statsLabel.setForeground(new Color(8, 78, 128));
        statsLabel.setBounds(480, 20, 200, 25);
        filterPanel.add(statsLabel);

        JLabel pendingLabel = new JLabel("Pending Payments: " + getPendingPaymentCount());
        pendingLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pendingLabel.setForeground(pendingColor);
        pendingLabel.setBounds(680, 20, 150, 25);
        filterPanel.add(pendingLabel);

        // Tabbed Pane for different trade statuses
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabbedPane.setBackground(new Color(245, 245, 250));
        tabbedPane.setForeground(new Color(8, 78, 128));
        tabbedPane.setBounds(20, 85, 550, 350);
        
        // Initialize tables first
        initializeTables();
        
        // Then add tabs
        tabbedPane.addTab("Pending", pendingScrollPane);
        tabbedPane.addTab("Active", activeScrollPane);
        tabbedPane.addTab("Completed", completedScrollPane);
        tabbedPane.addTab("Disputed", disputedScrollPane);
        
        tabbedPane.addChangeListener(e -> {
            selectedTabIndex = tabbedPane.getSelectedIndex();
            clearSelection();
        });
        
        contentPanel.add(tabbedPane);

        // Trade Details Panel
        detailsPanel = new JPanel();
        detailsPanel.setLayout(null);
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(new LineBorder(accentColor, 1));
        detailsPanel.setBounds(580, 85, 280, 350);
        contentPanel.add(detailsPanel);

        JLabel detailsTitle = new JLabel("Trade Details");
        detailsTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        detailsTitle.setForeground(new Color(8, 78, 128));
        detailsTitle.setBounds(10, 10, 150, 25);
        detailsPanel.add(detailsTitle);

        tradeDetailsArea = new JTextArea();
        tradeDetailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tradeDetailsArea.setEditable(false);
        tradeDetailsArea.setLineWrap(true);
        tradeDetailsArea.setWrapStyleWord(true);
        tradeDetailsArea.setBackground(new Color(250, 250, 250));
        tradeDetailsArea.setText("Select a trade to view details");

        detailsScrollPane = new JScrollPane(tradeDetailsArea);
        detailsScrollPane.setBounds(10, 40, 260, 300);
        detailsScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        detailsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        detailsPanel.add(detailsScrollPane);

        // Action Buttons Panel
        actionPanel = new JPanel();
        actionPanel.setLayout(null);
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(new LineBorder(accentColor, 1));
        actionPanel.setBounds(20, 445, 840, 90);
        contentPanel.add(actionPanel);

        JLabel actionLabel = new JLabel("Admin Actions:");
        actionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        actionLabel.setForeground(new Color(8, 78, 128));
        actionLabel.setBounds(15, 15, 120, 25);
        actionPanel.add(actionLabel);

        // Calculate button positions
        int buttonWidth = 120;
        int buttonHeight = 35;
        int startX = 150;
        int yPos = 15;
        int spacing = 10;

        // Verify Payment Button
        verifyPaymentButton = new JButton("Verify Payment");
        verifyPaymentButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        verifyPaymentButton.setBackground(new Color(8, 78, 128));
        verifyPaymentButton.setForeground(Color.WHITE);
        verifyPaymentButton.setBounds(startX, yPos, buttonWidth, buttonHeight);
        verifyPaymentButton.setBorder(null);
        verifyPaymentButton.setFocusPainted(false);
        verifyPaymentButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        verifyPaymentButton.setEnabled(false);
        verifyPaymentButton.addActionListener(e -> verifyPayment());
        actionPanel.add(verifyPaymentButton);

        // Mark as Paid Button
        markPaidButton = new JButton("Mark as Paid");
        markPaidButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        markPaidButton.setBackground(completedColor);
        markPaidButton.setForeground(Color.WHITE);
        markPaidButton.setBounds(startX + (buttonWidth + spacing), yPos, buttonWidth, buttonHeight);
        markPaidButton.setBorder(null);
        markPaidButton.setFocusPainted(false);
        markPaidButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        markPaidButton.setEnabled(false);
        markPaidButton.addActionListener(e -> markAsPaid());
        actionPanel.add(markPaidButton);

        // Process Refund Button
        processRefundButton = new JButton("Process Refund");
        processRefundButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        processRefundButton.setBackground(pendingColor);
        processRefundButton.setForeground(Color.WHITE);
        processRefundButton.setBounds(startX + (buttonWidth + spacing) * 2, yPos, buttonWidth, buttonHeight);
        processRefundButton.setBorder(null);
        processRefundButton.setFocusPainted(false);
        processRefundButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        processRefundButton.setEnabled(false);
        processRefundButton.addActionListener(e -> processRefund());
        actionPanel.add(processRefundButton);

        // Resolve Dispute Button
        resolveDisputeButton = new JButton("Resolve Dispute");
        resolveDisputeButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        resolveDisputeButton.setBackground(disputedColor);
        resolveDisputeButton.setForeground(Color.WHITE);
        resolveDisputeButton.setBounds(startX + (buttonWidth + spacing) * 3, yPos, buttonWidth, buttonHeight);
        resolveDisputeButton.setBorder(null);
        resolveDisputeButton.setFocusPainted(false);
        resolveDisputeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resolveDisputeButton.setEnabled(false);
        resolveDisputeButton.addActionListener(e -> resolveDispute());
        actionPanel.add(resolveDisputeButton);

        // Cancel Trade Button
        cancelTradeButton = new JButton("Cancel Trade");
        cancelTradeButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        cancelTradeButton.setBackground(new Color(102, 102, 102));
        cancelTradeButton.setForeground(Color.WHITE);
        cancelTradeButton.setBounds(startX + (buttonWidth + spacing) * 4, yPos, buttonWidth, buttonHeight);
        cancelTradeButton.setBorder(null);
        cancelTradeButton.setFocusPainted(false);
        cancelTradeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelTradeButton.setEnabled(false);
        cancelTradeButton.addActionListener(e -> cancelTrade());
        actionPanel.add(cancelTradeButton);

        // View Messages Button
        viewMessagesButton = new JButton("View Messages");
        viewMessagesButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        viewMessagesButton.setBackground(accentColor);
        viewMessagesButton.setForeground(new Color(8, 78, 128));
        viewMessagesButton.setBounds(startX + (buttonWidth + spacing) * 5, yPos, buttonWidth, buttonHeight);
        viewMessagesButton.setBorder(null);
        viewMessagesButton.setFocusPainted(false);
        viewMessagesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewMessagesButton.setEnabled(false);
        viewMessagesButton.addActionListener(e -> viewTradeMessages());
        actionPanel.add(viewMessagesButton);

        // Setup search
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                applySearch();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                applySearch();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                applySearch();
            }
        });
    }

    private void initializeTables() {
        setupPendingTable();
        pendingScrollPane = new JScrollPane(pendingTable);
        pendingScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        pendingScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        setupActiveTable();
        activeScrollPane = new JScrollPane(activeTable);
        activeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        activeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        setupCompletedTable();
        completedScrollPane = new JScrollPane(completedTable);
        completedScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        completedScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        setupDisputedTable();
        disputedScrollPane = new JScrollPane(disputedTable);
        disputedScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        disputedScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    private void setupPendingTable() {
        String[] columns = {"Trade ID", "Date", "Trader 1", "Item 1", "Trader 2", "Item 2", "Amount", "Status", "Payment Status"};
        pendingTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        pendingTable = new javax.swing.JTable(pendingTableModel);
        pendingTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        pendingTable.setRowHeight(35);
        pendingTable.setShowGrid(true);
        pendingTable.setGridColor(new Color(230, 230, 230));
        pendingTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        pendingTable.getTableHeader().setBackground(pendingColor);
        pendingTable.getTableHeader().setForeground(Color.WHITE);
        pendingTable.setSelectionBackground(new Color(255, 235, 204));
        pendingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Set column widths
        pendingTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        pendingTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        pendingTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        pendingTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        pendingTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        pendingTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        pendingTable.getColumnModel().getColumn(6).setPreferredWidth(70);
        pendingTable.getColumnModel().getColumn(7).setPreferredWidth(80);
        pendingTable.getColumnModel().getColumn(8).setPreferredWidth(90);

        pendingTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && selectedTabIndex == 0) {
                int selectedRow = pendingTable.getSelectedRow();
                if (selectedRow != -1) {
                    int modelRow = pendingTable.convertRowIndexToModel(selectedRow);
                    displayTradeDetails(modelRow, pendingTableModel, "pending");
                } else {
                    clearSelection();
                }
            }
        });
    }

    private void setupActiveTable() {
        String[] columns = {"Trade ID", "Date", "Trader 1", "Item 1", "Trader 2", "Item 2", "Amount", "Status", "Method"};
        activeTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        activeTable = new javax.swing.JTable(activeTableModel);
        activeTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        activeTable.setRowHeight(35);
        activeTable.setShowGrid(true);
        activeTable.setGridColor(new Color(230, 230, 230));
        activeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        activeTable.getTableHeader().setBackground(activeColor2);
        activeTable.getTableHeader().setForeground(Color.WHITE);
        activeTable.setSelectionBackground(new Color(184, 239, 255));
        activeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        activeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && selectedTabIndex == 1) {
                int selectedRow = activeTable.getSelectedRow();
                if (selectedRow != -1) {
                    int modelRow = activeTable.convertRowIndexToModel(selectedRow);
                    displayTradeDetails(modelRow, activeTableModel, "active");
                } else {
                    clearSelection();
                }
            }
        });
    }

    private void setupCompletedTable() {
        String[] columns = {"Trade ID", "Date", "Trader 1", "Item 1", "Trader 2", "Item 2", "Amount", "Completed", "Fee"};
        completedTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        completedTable = new javax.swing.JTable(completedTableModel);
        completedTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        completedTable.setRowHeight(35);
        completedTable.setShowGrid(true);
        completedTable.setGridColor(new Color(230, 230, 230));
        completedTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        completedTable.getTableHeader().setBackground(completedColor);
        completedTable.getTableHeader().setForeground(Color.WHITE);
        completedTable.setSelectionBackground(new Color(200, 230, 201));
        completedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        completedTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && selectedTabIndex == 2) {
                int selectedRow = completedTable.getSelectedRow();
                if (selectedRow != -1) {
                    int modelRow = completedTable.convertRowIndexToModel(selectedRow);
                    displayTradeDetails(modelRow, completedTableModel, "completed");
                } else {
                    clearSelection();
                }
            }
        });
    }

    private void setupDisputedTable() {
        String[] columns = {"Report ID", "Date", "Reporter", "Item", "Reported", "Item", "Amount", "Reason"};
        disputedTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        disputedTable = new javax.swing.JTable(disputedTableModel);
        disputedTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        disputedTable.setRowHeight(35);
        disputedTable.setShowGrid(true);
        disputedTable.setGridColor(new Color(230, 230, 230));
        disputedTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        disputedTable.getTableHeader().setBackground(disputedColor);
        disputedTable.getTableHeader().setForeground(Color.WHITE);
        disputedTable.setSelectionBackground(new Color(255, 204, 204));
        disputedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        disputedTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && selectedTabIndex == 3) {
                int selectedRow = disputedTable.getSelectedRow();
                if (selectedRow != -1) {
                    int modelRow = disputedTable.convertRowIndexToModel(selectedRow);
                    displayTradeDetails(modelRow, disputedTableModel, "disputed");
                } else {
                    clearSelection();
                }
            }
        });
    }

    private void loadTradeData() {
        // Clear all tables
        if (pendingTableModel != null) pendingTableModel.setRowCount(0);
        if (activeTableModel != null) activeTableModel.setRowCount(0);
        if (completedTableModel != null) completedTableModel.setRowCount(0);
        if (disputedTableModel != null) disputedTableModel.setRowCount(0);

        // Load pending trades
        String pendingSql = "SELECT t.trade_id, t.trade_DateRequest, "
                + "u1.user_fullname as trader1, i1.item_Name as item1, "
                + "u2.user_fullname as trader2, i2.item_Name as item2, "
                + "COALESCE(t.base_amount, 0) as base_amount, t.trade_status, "
                + "CASE WHEN t.payment_verified = 1 THEN 'Verified' ELSE 'Pending' END as payment_status "
                + "FROM tbl_trade t "
                + "LEFT JOIN tbl_users u1 ON t.offer_trader_id = u1.user_id "
                + "LEFT JOIN tbl_users u2 ON t.target_trader_id = u2.user_id "
                + "LEFT JOIN tbl_items i1 ON t.offer_item_id = i1.items_id "
                + "LEFT JOIN tbl_items i2 ON t.target_item_id = i2.items_id "
                + "WHERE t.trade_status = 'pending' "
                + "ORDER BY t.trade_DateRequest DESC";

        List<Map<String, Object>> pendingTrades = db.fetchRecords(pendingSql);
        for (Map<String, Object> trade : pendingTrades) {
            pendingTableModel.addRow(new Object[]{
                trade.get("trade_id"),
                formatDate(trade.get("trade_DateRequest")),
                trade.get("trader1") != null ? trade.get("trader1") : "N/A",
                trade.get("item1") != null ? trade.get("item1") : "N/A",
                trade.get("trader2") != null ? trade.get("trader2") : "N/A",
                trade.get("item2") != null ? trade.get("item2") : "N/A",
                "₱" + String.format("%.2f", trade.get("base_amount")),
                trade.get("trade_status"),
                trade.get("payment_status")
            });
        }

        // Load active trades
        String activeSql = "SELECT t.trade_id, t.trade_DateRequest, "
                + "u1.user_fullname as trader1, i1.item_Name as item1, "
                + "u2.user_fullname as trader2, i2.item_Name as item2, "
                + "COALESCE(t.base_amount, 0) as base_amount, t.trade_status, "
                + "COALESCE(t.exchange_method, 'Not set') as exchange_method "
                + "FROM tbl_trade t "
                + "LEFT JOIN tbl_users u1 ON t.offer_trader_id = u1.user_id "
                + "LEFT JOIN tbl_users u2 ON t.target_trader_id = u2.user_id "
                + "LEFT JOIN tbl_items i1 ON t.offer_item_id = i1.items_id "
                + "LEFT JOIN tbl_items i2 ON t.target_item_id = i2.items_id "
                + "WHERE t.trade_status IN ('negotiating', 'arrangements_confirmed') "
                + "ORDER BY t.trade_DateRequest DESC";

        List<Map<String, Object>> activeTrades = db.fetchRecords(activeSql);
        for (Map<String, Object> trade : activeTrades) {
            activeTableModel.addRow(new Object[]{
                trade.get("trade_id"),
                formatDate(trade.get("trade_DateRequest")),
                trade.get("trader1") != null ? trade.get("trader1") : "N/A",
                trade.get("item1") != null ? trade.get("item1") : "N/A",
                trade.get("trader2") != null ? trade.get("trader2") : "N/A",
                trade.get("item2") != null ? trade.get("item2") : "N/A",
                "₱" + String.format("%.2f", trade.get("base_amount")),
                trade.get("trade_status"),
                trade.get("exchange_method")
            });
        }

        // Load completed trades
        String completedSql = "SELECT h.history_id, h.trade_DateCompleted, h.trade_DateRequest, "
                + "u1.user_fullname as trader1, i1.item_Name as item1, "
                + "u2.user_fullname as trader2, i2.item_Name as item2, "
                + "h.trade_status "
                + "FROM tbl_trade_history h "
                + "LEFT JOIN tbl_users u1 ON h.offer_trader_id = u1.user_id "
                + "LEFT JOIN tbl_users u2 ON h.target_trader_id = u2.user_id "
                + "LEFT JOIN tbl_items i1 ON h.offer_item_id = i1.items_id "
                + "LEFT JOIN tbl_items i2 ON h.target_item_id = i2.items_id "
                + "WHERE h.trade_status = 'completed' "
                + "ORDER BY h.trade_DateCompleted DESC";

        List<Map<String, Object>> completedTrades = db.fetchRecords(completedSql);
        for (Map<String, Object> trade : completedTrades) {
            completedTableModel.addRow(new Object[]{
                trade.get("history_id"),
                formatDate(trade.get("trade_DateRequest")),
                trade.get("trader1") != null ? trade.get("trader1") : "N/A",
                trade.get("item1") != null ? trade.get("item1") : "N/A",
                trade.get("trader2") != null ? trade.get("trader2") : "N/A",
                trade.get("item2") != null ? trade.get("item2") : "N/A",
                "₱0",
                formatDate(trade.get("trade_DateCompleted")),
                "₱0"
            });
        }

        // Load disputed trades (from reports related to trades)
        String disputedSql = "SELECT r.report_id, r.report_date, "
                + "u1.user_fullname as reporter, "
                + "u2.user_fullname as reported, "
                + "r.report_reason, r.report_description, r.report_status "
                + "FROM tbl_reports r "
                + "LEFT JOIN tbl_users u1 ON r.reporter_id = u1.user_id "
                + "LEFT JOIN tbl_users u2 ON r.reported_trader_id = u2.user_id "
                + "WHERE r.report_status IN ('pending', 'under_review') "
                + "ORDER BY r.report_date DESC";

        List<Map<String, Object>> disputedTrades = db.fetchRecords(disputedSql);
        for (Map<String, Object> trade : disputedTrades) {
            disputedTableModel.addRow(new Object[]{
                trade.get("report_id"),
                formatDate(trade.get("report_date")),
                trade.get("reporter") != null ? trade.get("reporter") : "N/A",
                "N/A",
                trade.get("reported") != null ? trade.get("reported") : "N/A",
                "N/A",
                "₱0",
                trade.get("report_reason") != null ? trade.get("report_reason") : "N/A"
            });
        }

        // Setup row sorters
        if (pendingTableModel != null) {
            pendingRowSorter = new TableRowSorter<>(pendingTableModel);
            pendingTable.setRowSorter(pendingRowSorter);
        }
        
        if (activeTableModel != null) {
            activeRowSorter = new TableRowSorter<>(activeTableModel);
            activeTable.setRowSorter(activeRowSorter);
        }
        
        if (completedTableModel != null) {
            completedRowSorter = new TableRowSorter<>(completedTableModel);
            completedTable.setRowSorter(completedRowSorter);
        }
        
        if (disputedTableModel != null) {
            disputedRowSorter = new TableRowSorter<>(disputedTableModel);
            disputedTable.setRowSorter(disputedRowSorter);
        }
    }

    private void displayTradeDetails(int modelRow, DefaultTableModel model, String status) {
        if (model == null || model.getRowCount() <= modelRow) {
            return;
        }
        
        selectedTradeId = Integer.parseInt(model.getValueAt(modelRow, 0).toString());
        selectedTradeStatus = status;

        StringBuilder details = new StringBuilder();
        details.append("=== TRADE DETAILS ===\n\n");
        details.append("Trade ID: ").append(model.getValueAt(modelRow, 0)).append("\n");
        details.append("Date: ").append(model.getValueAt(modelRow, 1)).append("\n\n");

        if (status.equals("pending")) {
            details.append("Trader 1: ").append(model.getValueAt(modelRow, 2)).append("\n");
            details.append("Item 1: ").append(model.getValueAt(modelRow, 3)).append("\n\n");
            details.append("Trader 2: ").append(model.getValueAt(modelRow, 4)).append("\n");
            details.append("Item 2: ").append(model.getValueAt(modelRow, 5)).append("\n\n");
            details.append("Amount: ").append(model.getValueAt(modelRow, 6)).append("\n");
            details.append("Status: ").append(model.getValueAt(modelRow, 7)).append("\n");
            details.append("Payment: ").append(model.getValueAt(modelRow, 8)).append("\n");

            // Enable appropriate buttons
            verifyPaymentButton.setEnabled(true);
            markPaidButton.setEnabled(true);
            cancelTradeButton.setEnabled(true);
            processRefundButton.setEnabled(false);
            resolveDisputeButton.setEnabled(false);

        } else if (status.equals("active")) {
            details.append("Trader 1: ").append(model.getValueAt(modelRow, 2)).append("\n");
            details.append("Item 1: ").append(model.getValueAt(modelRow, 3)).append("\n\n");
            details.append("Trader 2: ").append(model.getValueAt(modelRow, 4)).append("\n");
            details.append("Item 2: ").append(model.getValueAt(modelRow, 5)).append("\n\n");
            details.append("Amount: ").append(model.getValueAt(modelRow, 6)).append("\n");
            details.append("Status: ").append(model.getValueAt(modelRow, 7)).append("\n");
            details.append("Method: ").append(model.getValueAt(modelRow, 8)).append("\n");

            // Load exchange details
            loadExchangeDetails(selectedTradeId, details);

            verifyPaymentButton.setEnabled(false);
            markPaidButton.setEnabled(false);
            cancelTradeButton.setEnabled(true);
            processRefundButton.setEnabled(false);
            resolveDisputeButton.setEnabled(true);

        } else if (status.equals("completed")) {
            details.append("Trader 1: ").append(model.getValueAt(modelRow, 2)).append("\n");
            details.append("Item 1: ").append(model.getValueAt(modelRow, 3)).append("\n\n");
            details.append("Trader 2: ").append(model.getValueAt(modelRow, 4)).append("\n");
            details.append("Item 2: ").append(model.getValueAt(modelRow, 5)).append("\n\n");
            details.append("Completed: ").append(model.getValueAt(modelRow, 7)).append("\n");

            verifyPaymentButton.setEnabled(false);
            markPaidButton.setEnabled(false);
            cancelTradeButton.setEnabled(false);
            processRefundButton.setEnabled(false);
            resolveDisputeButton.setEnabled(false);

        } else if (status.equals("disputed")) {
            details.append("Reporter: ").append(model.getValueAt(modelRow, 2)).append("\n");
            details.append("Reported: ").append(model.getValueAt(modelRow, 4)).append("\n\n");
            details.append("Reason: ").append(model.getValueAt(modelRow, 7)).append("\n");
            details.append("Description: ").append(getDisputeDescription(selectedTradeId)).append("\n");

            verifyPaymentButton.setEnabled(false);
            markPaidButton.setEnabled(false);
            cancelTradeButton.setEnabled(true);
            processRefundButton.setEnabled(true);
            resolveDisputeButton.setEnabled(true);
        }

        viewMessagesButton.setEnabled(true);
        tradeDetailsArea.setText(details.toString());
        tradeDetailsArea.setCaretPosition(0);
    }

    private void loadExchangeDetails(int tradeId, StringBuilder details) {
        String sql = "SELECT * FROM tbl_trade_details WHERE trade_id = ?";
        List<Map<String, Object>> exchangeDetails = db.fetchRecords(sql, tradeId);

        if (!exchangeDetails.isEmpty()) {
            details.append("\n=== EXCHANGE DETAILS ===\n");
            for (Map<String, Object> detail : exchangeDetails) {
                String method = (String) detail.get("exchange_method");
                int traderId = Integer.parseInt(detail.get("trader_id").toString());
                String traderName = getTraderName(traderId);

                details.append("\n").append(traderName).append(":\n");
                if ("delivery".equals(method)) {
                    details.append("  Address: ").append(detail.get("delivery_address")).append("\n");
                    details.append("  Courier: ").append(detail.get("courier")).append("\n");
                    details.append("  Expected: ").append(detail.get("expected_date")).append("\n");
                    details.append("  Tracking: ").append(detail.get("tracking_number")).append("\n");
                } else {
                    details.append("  Location: ").append(detail.get("meetup_location")).append("\n");
                    details.append("  Date: ").append(detail.get("meetup_date")).append("\n");
                    details.append("  Time: ").append(detail.get("meetup_time")).append("\n");
                    details.append("  Contact: ").append(detail.get("contact_person")).append("\n");
                    details.append("  Phone: ").append(detail.get("contact_number")).append("\n");
                }
            }
        }
    }

    private String getTraderName(int traderId) {
        String sql = "SELECT user_fullname FROM tbl_users WHERE user_id = ?";
        List<Map<String, Object>> result = db.fetchRecords(sql, traderId);
        if (!result.isEmpty()) {
            return (String) result.get(0).get("user_fullname");
        }
        return "Unknown Trader";
    }

    private String getDisputeDescription(int reportId) {
        String sql = "SELECT report_description FROM tbl_reports WHERE report_id = ?";
        List<Map<String, Object>> result = db.fetchRecords(sql, reportId);
        if (!result.isEmpty()) {
            return (String) result.get(0).get("report_description");
        }
        return "No description provided";
    }

    private void clearSelection() {
        selectedTradeId = -1;
        selectedTradeStatus = "";
        if (tradeDetailsArea != null) {
            tradeDetailsArea.setText("Select a trade to view details");
        }
        if (verifyPaymentButton != null) verifyPaymentButton.setEnabled(false);
        if (markPaidButton != null) markPaidButton.setEnabled(false);
        if (processRefundButton != null) processRefundButton.setEnabled(false);
        if (resolveDisputeButton != null) resolveDisputeButton.setEnabled(false);
        if (cancelTradeButton != null) cancelTradeButton.setEnabled(false);
        if (viewMessagesButton != null) viewMessagesButton.setEnabled(false);
    }

    private void applySearch() {
        String text = searchField.getText().trim();
        
        if (text.isEmpty()) {
            if (pendingRowSorter != null) pendingRowSorter.setRowFilter(null);
            if (activeRowSorter != null) activeRowSorter.setRowFilter(null);
            if (completedRowSorter != null) completedRowSorter.setRowFilter(null);
            if (disputedRowSorter != null) disputedRowSorter.setRowFilter(null);
        } else {
            RowFilter<DefaultTableModel, Object> filter = RowFilter.regexFilter("(?i)" + text, 2, 3, 4, 5);
            if (pendingRowSorter != null) pendingRowSorter.setRowFilter(filter);
            if (activeRowSorter != null) activeRowSorter.setRowFilter(filter);
            if (completedRowSorter != null) completedRowSorter.setRowFilter(filter);
            if (disputedRowSorter != null) disputedRowSorter.setRowFilter(filter);
        }
    }

    private void verifyPayment() {
        if (selectedTradeId == -1) return;

        int confirm = JOptionPane.showConfirmDialog(this,
            "Verify payment for Trade #" + selectedTradeId + "?\n\n"
            + "This will confirm that the payment screenshot is valid.",
            "Verify Payment",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_trade SET payment_verified = 1 WHERE trade_id = ?";
            db.updateRecord(sql, selectedTradeId);

            JOptionPane.showMessageDialog(this,
                "Payment verified successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

            logActivity("Verified payment for Trade #" + selectedTradeId);
            loadTradeData();
            clearSelection();
        }
    }

    private void markAsPaid() {
        if (selectedTradeId == -1) return;

        String[] options = {"Trader 1 Paid", "Trader 2 Paid", "Both Paid"};
        int choice = JOptionPane.showOptionDialog(this,
            "Who has made the payment?",
            "Mark as Paid",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);

        if (choice >= 0) {
            String sql = "UPDATE tbl_trade SET ";
            switch (choice) {
                case 0:
                    sql += "my_payment_submitted = 1 WHERE trade_id = ?";
                    break;
                case 1:
                    sql += "other_payment_submitted = 1 WHERE trade_id = ?";
                    break;
                case 2:
                    sql += "my_payment_submitted = 1, other_payment_submitted = 1 WHERE trade_id = ?";
                    break;
            }
            db.updateRecord(sql, selectedTradeId);

            JOptionPane.showMessageDialog(this,
                "Payment status updated!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

            logActivity("Marked payment for Trade #" + selectedTradeId);
            loadTradeData();
            clearSelection();
        }
    }

    private void processRefund() {
        if (selectedTradeId == -1) return;

        String[] options = {"Full Refund", "Partial Refund", "No Refund (Scammer)"};
        int choice = JOptionPane.showOptionDialog(this,
            "Select refund type:",
            "Process Refund",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null,
            options,
            options[0]);

        if (choice >= 0) {
            String message = "";
            switch (choice) {
                case 0:
                    message = "Full refund processed. Both traders will receive their money back.";
                    break;
                case 1:
                    message = "Partial refund processed. Amount will be calculated based on dispute resolution.";
                    break;
                case 2:
                    message = "No refund. Funds will be transferred to the victim.";
                    break;
            }

            String sql = "UPDATE tbl_trade SET refund_processed = 1, trade_status = 'completed' WHERE trade_id = ?";
            db.updateRecord(sql, selectedTradeId);

            JOptionPane.showMessageDialog(this,
                "Refund processed successfully!\n\n" + message,
                "Refund Complete",
                JOptionPane.INFORMATION_MESSAGE);

            logActivity("Processed refund for Trade #" + selectedTradeId);
            loadTradeData();
            clearSelection();
        }
    }

    private void resolveDispute() {
        if (selectedTradeId == -1) return;

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new java.awt.Dimension(400, 200));

        JLabel resolutionLabel = new JLabel("Resolution Notes:");
        resolutionLabel.setBounds(20, 20, 150, 25);
        panel.add(resolutionLabel);

        JTextArea resolutionArea = new JTextArea();
        resolutionArea.setLineWrap(true);
        resolutionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resolutionArea);
        scrollPane.setBounds(20, 50, 350, 100);
        panel.add(scrollPane);

        int result = JOptionPane.showConfirmDialog(this, panel, "Resolve Dispute", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String resolution = resolutionArea.getText().trim();
            
            String sql = "UPDATE tbl_reports SET report_status = 'resolved', admin_notes = ?, resolved_date = datetime('now') WHERE report_id = ?";
            db.updateRecord(sql, resolution, selectedTradeId);

            JOptionPane.showMessageDialog(this,
                "Dispute resolved successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

            logActivity("Resolved dispute for Report #" + selectedTradeId);
            loadTradeData();
            clearSelection();
        }
    }

    private void cancelTrade() {
        if (selectedTradeId == -1) return;

        int confirm = JOptionPane.showConfirmDialog(this,
            "Cancel Trade #" + selectedTradeId + "?\n\n"
            + "This action cannot be undone!",
            "Cancel Trade",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM tbl_trade WHERE trade_id = ?";
            db.deleteRecord(sql, selectedTradeId);

            JOptionPane.showMessageDialog(this,
                "Trade cancelled successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

            logActivity("Cancelled Trade #" + selectedTradeId);
            loadTradeData();
            clearSelection();
        }
    }

    private void viewTradeMessages() {
        if (selectedTradeId == -1) return;

        JOptionPane.showMessageDialog(this,
            "Messages for Trade #" + selectedTradeId + "\n\n"
            + "This feature will display all messages between traders\n"
            + "related to this trade for admin review.",
            "Trade Messages",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private String formatDate(Object dateObj) {
        if (dateObj == null) return "-";
        try {
            String dateStr = dateObj.toString();
            if (dateStr.length() >= 10) {
                return dateStr.substring(0, 10);
            }
            return dateStr;
        } catch (Exception e) {
            return "-";
        }
    }

    private double getTotalFees() {
        try {
            String sql = "SELECT SUM(fee_amount) as total FROM tbl_trade WHERE trade_status = 'completed'";
            return db.getSingleValue(sql);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private int getPendingPaymentCount() {
        try {
            String sql = "SELECT COUNT(*) as count FROM tbl_trade WHERE payment_verified = 0 OR payment_verified IS NULL";
            double count = db.getSingleValue(sql);
            return (int) count;
        } catch (Exception e) {
            return 0;
        }
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