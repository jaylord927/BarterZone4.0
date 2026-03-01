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
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JComboBox;

public class manage_reports extends javax.swing.JFrame {

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
    
    // Filter components
    private JPanel filterPanel;
    private JTextField searchField;
    private JComboBox<String> statusFilter;
    private JButton refreshButton;
    
    // Reports table
    private JScrollPane tableScrollPane;
    private javax.swing.JTable reportsTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    
    // Report details panel
    private JPanel detailsPanel;
    private JTextArea reportDetailsArea;
    private JScrollPane detailsScrollPane;
    
    // Action buttons panel
    private JPanel actionPanel;
    private JButton markUnderReviewButton;
    private JButton resolveReportButton;
    private JButton dismissReportButton;
    private JButton contactReporterButton;
    private JButton contactReportedButton;
    private JButton viewTradeButton;
    
    // Selected report data
    private int selectedReportId = -1;
    private String selectedReportStatus = "";
    
    // Colors - Matching profile.java theme (dark blue/gold)
    private Color sideBarColor = new Color(8, 78, 128);
    private Color hoverColor = new Color(20, 100, 150);
    private Color activeColor = new Color(0, 60, 100);
    private Color accentColor = new Color(255, 215, 0);
    private Color badgeColor = new Color(204, 0, 0);
    private Color headerGradientStart = new Color(8, 78, 128);
    private Color headerGradientEnd = new Color(0, 45, 80);
    private Color pendingColor = new Color(255, 153, 0);
    private Color underReviewColor = new Color(0, 102, 102);
    private Color resolvedColor = new Color(46, 125, 50);
    
    private JPanel activePanel = null;

    public manage_reports(int adminId, String adminName) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.session = user_session.getInstance();
        this.db = new config();
        
        initComponents();
        setupSidePanel();
        setupHeader();
        setupContentPanel();
        loadReportsData();
        updateBadges();
        
        setTitle("Manage Reports - " + adminName);
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

        // Set active panel to Manage Reports
        setActivePanel(manageReportsPanel);
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
        headerTitle = new JLabel("Manage Reports");
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
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(new LineBorder(accentColor, 1));
        filterPanel.setBounds(20, 15, 840, 70);
        contentPanel.add(filterPanel);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchLabel.setForeground(new Color(8, 78, 128));
        searchLabel.setBounds(15, 25, 60, 25);
        filterPanel.add(searchLabel);

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setBounds(80, 25, 200, 25);
        searchField.setBorder(new LineBorder(new Color(200, 200, 200)));
        filterPanel.add(searchField);

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setForeground(new Color(8, 78, 128));
        statusLabel.setBounds(300, 25, 50, 25);
        filterPanel.add(statusLabel);

        String[] statuses = {"All Reports", "Pending", "Under Review", "Resolved"};
        statusFilter = new JComboBox<>(statuses);
        statusFilter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusFilter.setBounds(355, 25, 120, 25);
        statusFilter.setBackground(Color.WHITE);
        statusFilter.setBorder(new LineBorder(new Color(200, 200, 200)));
        statusFilter.addActionListener(e -> applyFilter());
        filterPanel.add(statusFilter);

        refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshButton.setBackground(accentColor);
        refreshButton.setForeground(new Color(8, 78, 128));
        refreshButton.setBounds(490, 25, 90, 25);
        refreshButton.setBorder(null);
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> {
            loadReportsData();
            applyFilter();
        });
        filterPanel.add(refreshButton);

        // Stats Summary
        JLabel totalLabel = new JLabel("Total: " + getTotalReports());
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        totalLabel.setForeground(new Color(8, 78, 128));
        totalLabel.setBounds(600, 25, 80, 25);
        filterPanel.add(totalLabel);

        JLabel pendingCount = new JLabel("Pending: " + getPendingCount());
        pendingCount.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pendingCount.setForeground(pendingColor);
        pendingCount.setBounds(690, 25, 80, 25);
        filterPanel.add(pendingCount);

        JLabel resolvedCount = new JLabel("Resolved: " + getResolvedCount());
        resolvedCount.setFont(new Font("Segoe UI", Font.BOLD, 12));
        resolvedCount.setForeground(resolvedColor);
        resolvedCount.setBounds(780, 25, 80, 25);
        filterPanel.add(resolvedCount);

        // Reports Table
        setupTable();
        tableScrollPane = new JScrollPane(reportsTable);
        tableScrollPane.setBounds(20, 95, 550, 340);
        tableScrollPane.setBorder(new LineBorder(accentColor, 1));
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        contentPanel.add(tableScrollPane);

        // Report Details Panel
        detailsPanel = new JPanel();
        detailsPanel.setLayout(null);
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(new LineBorder(accentColor, 1));
        detailsPanel.setBounds(580, 95, 280, 340);
        contentPanel.add(detailsPanel);

        JLabel detailsTitle = new JLabel("Report Details");
        detailsTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        detailsTitle.setForeground(new Color(8, 78, 128));
        detailsTitle.setBounds(10, 10, 150, 25);
        detailsPanel.add(detailsTitle);

        reportDetailsArea = new JTextArea();
        reportDetailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        reportDetailsArea.setEditable(false);
        reportDetailsArea.setLineWrap(true);
        reportDetailsArea.setWrapStyleWord(true);
        reportDetailsArea.setBackground(new Color(250, 250, 250));
        reportDetailsArea.setText("Select a report to view details");

        detailsScrollPane = new JScrollPane(reportDetailsArea);
        detailsScrollPane.setBounds(10, 40, 260, 290);
        detailsScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        detailsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        detailsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        detailsPanel.add(detailsScrollPane);

        // Action Buttons Panel
        actionPanel = new JPanel();
        actionPanel.setLayout(null);
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(new LineBorder(accentColor, 1));
        actionPanel.setBounds(20, 445, 840, 90);
        contentPanel.add(actionPanel);

        JLabel actionLabel = new JLabel("Actions:");
        actionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        actionLabel.setForeground(new Color(8, 78, 128));
        actionLabel.setBounds(15, 15, 70, 25);
        actionPanel.add(actionLabel);

        // Calculate button positions - 6 buttons with proper spacing
        int buttonWidth = 120;
        int buttonHeight = 35;
        int startX = 100;
        int yPos = 15;
        int spacing = 10;

        // Mark Under Review Button
        markUnderReviewButton = new JButton("Mark Under Review");
        markUnderReviewButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        markUnderReviewButton.setBackground(underReviewColor);
        markUnderReviewButton.setForeground(Color.WHITE);
        markUnderReviewButton.setBounds(startX, yPos, buttonWidth, buttonHeight);
        markUnderReviewButton.setBorder(null);
        markUnderReviewButton.setFocusPainted(false);
        markUnderReviewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        markUnderReviewButton.setEnabled(false);
        markUnderReviewButton.addActionListener(e -> markUnderReview());
        actionPanel.add(markUnderReviewButton);

        // Resolve Report Button
        resolveReportButton = new JButton("Resolve Report");
        resolveReportButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        resolveReportButton.setBackground(resolvedColor);
        resolveReportButton.setForeground(Color.WHITE);
        resolveReportButton.setBounds(startX + (buttonWidth + spacing), yPos, buttonWidth, buttonHeight);
        resolveReportButton.setBorder(null);
        resolveReportButton.setFocusPainted(false);
        resolveReportButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resolveReportButton.setEnabled(false);
        resolveReportButton.addActionListener(e -> resolveReport());
        actionPanel.add(resolveReportButton);

        // Dismiss Report Button
        dismissReportButton = new JButton("Dismiss Report");
        dismissReportButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        dismissReportButton.setBackground(new Color(102, 102, 102));
        dismissReportButton.setForeground(Color.WHITE);
        dismissReportButton.setBounds(startX + (buttonWidth + spacing) * 2, yPos, buttonWidth, buttonHeight);
        dismissReportButton.setBorder(null);
        dismissReportButton.setFocusPainted(false);
        dismissReportButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dismissReportButton.setEnabled(false);
        dismissReportButton.addActionListener(e -> dismissReport());
        actionPanel.add(dismissReportButton);

        // Contact Reporter Button
        contactReporterButton = new JButton("Contact Reporter");
        contactReporterButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        contactReporterButton.setBackground(new Color(8, 78, 128));
        contactReporterButton.setForeground(Color.WHITE);
        contactReporterButton.setBounds(startX + (buttonWidth + spacing) * 3, yPos, buttonWidth, buttonHeight);
        contactReporterButton.setBorder(null);
        contactReporterButton.setFocusPainted(false);
        contactReporterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contactReporterButton.setEnabled(false);
        contactReporterButton.addActionListener(e -> contactReporter());
        actionPanel.add(contactReporterButton);

        // Contact Reported Button
        contactReportedButton = new JButton("Contact Reported");
        contactReportedButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        contactReportedButton.setBackground(new Color(255, 153, 0));
        contactReportedButton.setForeground(Color.WHITE);
        contactReportedButton.setBounds(startX + (buttonWidth + spacing) * 4, yPos, buttonWidth, buttonHeight);
        contactReportedButton.setBorder(null);
        contactReportedButton.setFocusPainted(false);
        contactReportedButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contactReportedButton.setEnabled(false);
        contactReportedButton.addActionListener(e -> contactReported());
        actionPanel.add(contactReportedButton);

        // View Trade Button
        viewTradeButton = new JButton("View Trade");
        viewTradeButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        viewTradeButton.setBackground(accentColor);
        viewTradeButton.setForeground(new Color(8, 78, 128));
        viewTradeButton.setBounds(startX + (buttonWidth + spacing) * 5, yPos, buttonWidth, buttonHeight);
        viewTradeButton.setBorder(null);
        viewTradeButton.setFocusPainted(false);
        viewTradeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewTradeButton.setEnabled(false);
        viewTradeButton.addActionListener(e -> viewRelatedTrade());
        actionPanel.add(viewTradeButton);

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

    private void setupTable() {
        String[] columns = {"Report ID", "Date", "Reporter", "Reported User", "Reason", "Status", "Trade ID"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        reportsTable = new javax.swing.JTable(tableModel);
        reportsTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        reportsTable.setRowHeight(35);
        reportsTable.setShowGrid(true);
        reportsTable.setGridColor(new Color(230, 230, 230));
        reportsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        reportsTable.getTableHeader().setBackground(sideBarColor);
        reportsTable.getTableHeader().setForeground(Color.WHITE);
        reportsTable.setSelectionBackground(new Color(255, 235, 204));
        reportsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Set column widths
        reportsTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        reportsTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        reportsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        reportsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        reportsTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        reportsTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        reportsTable.getColumnModel().getColumn(6).setPreferredWidth(60);

        reportsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = reportsTable.getSelectedRow();
                if (selectedRow != -1) {
                    int modelRow = reportsTable.convertRowIndexToModel(selectedRow);
                    displayReportDetails(modelRow);
                } else {
                    clearSelection();
                }
            }
        });
    }

    private void loadReportsData() {
        tableModel.setRowCount(0);

        String sql = "SELECT r.report_id, r.report_date, "
                + "reporter.user_fullname as reporter_name, "
                + "reported.user_fullname as reported_name, "
                + "r.report_reason, r.report_status, "
                + "r.report_description, r.admin_notes, "
                + "r.reported_trader_id, r.reporter_id, "
                + "t.trade_id "
                + "FROM tbl_reports r "
                + "LEFT JOIN tbl_users reporter ON r.reporter_id = reporter.user_id "
                + "LEFT JOIN tbl_users reported ON r.reported_trader_id = reported.user_id "
                + "LEFT JOIN tbl_trade t ON (t.offer_trader_id = r.reporter_id AND t.target_trader_id = r.reported_trader_id) "
                + "OR (t.target_trader_id = r.reporter_id AND t.offer_trader_id = r.reported_trader_id) "
                + "ORDER BY r.report_date DESC";

        List<Map<String, Object>> reports = db.fetchRecords(sql);

        for (Map<String, Object> report : reports) {
            String status = (String) report.get("report_status");
            if (status == null) status = "pending";
            
            tableModel.addRow(new Object[]{
                report.get("report_id"),
                formatDate(report.get("report_date")),
                report.get("reporter_name") != null ? report.get("reporter_name") : "Unknown",
                report.get("reported_name") != null ? report.get("reported_name") : "Unknown",
                report.get("report_reason"),
                status.substring(0, 1).toUpperCase() + status.substring(1),
                report.get("trade_id") != null ? report.get("trade_id") : "N/A"
            });
        }

        rowSorter = new TableRowSorter<>(tableModel);
        reportsTable.setRowSorter(rowSorter);
    }

    private void displayReportDetails(int modelRow) {
        selectedReportId = Integer.parseInt(tableModel.getValueAt(modelRow, 0).toString());
        selectedReportStatus = tableModel.getValueAt(modelRow, 5).toString().toLowerCase();

        // Fetch full report details
        String sql = "SELECT r.*, "
                + "reporter.user_fullname as reporter_name, reporter.user_email as reporter_email, reporter.user_username as reporter_username, "
                + "reported.user_fullname as reported_name, reported.user_email as reported_email, reported.user_username as reported_username "
                + "FROM tbl_reports r "
                + "LEFT JOIN tbl_users reporter ON r.reporter_id = reporter.user_id "
                + "LEFT JOIN tbl_users reported ON r.reported_trader_id = reported.user_id "
                + "WHERE r.report_id = ?";

        List<Map<String, Object>> reports = db.fetchRecords(sql, selectedReportId);

        if (!reports.isEmpty()) {
            Map<String, Object> report = reports.get(0);

            StringBuilder details = new StringBuilder();
            details.append("═══════════════════════════════════════\n");
            details.append("           REPORT DETAILS\n");
            details.append("═══════════════════════════════════════\n\n");
            
            details.append("Report ID: ").append(report.get("report_id")).append("\n");
            details.append("Date Filed: ").append(formatDateTime(report.get("report_date"))).append("\n");
            details.append("Status: ").append(report.get("report_status")).append("\n\n");
            
            details.append("━━━━━━━━━━ REPORTER INFORMATION ━━━━━━━━━━\n");
            details.append("Name: ").append(report.get("reporter_name")).append("\n");
            details.append("Username: ").append(report.get("reporter_username")).append("\n");
            details.append("Email: ").append(report.get("reporter_email")).append("\n\n");
            
            details.append("━━━━━━━━ REPORTED USER INFORMATION ━━━━━━━━\n");
            details.append("Name: ").append(report.get("reported_name")).append("\n");
            details.append("Username: ").append(report.get("reported_username")).append("\n");
            details.append("Email: ").append(report.get("reported_email")).append("\n\n");
            
            details.append("━━━━━━━━━━━━ REPORT DETAILS ━━━━━━━━━━━━━\n");
            details.append("Reason: ").append(report.get("report_reason")).append("\n\n");
            details.append("Description:\n").append(report.get("report_description")).append("\n\n");
            
            if (report.get("admin_notes") != null && !report.get("admin_notes").toString().isEmpty()) {
                details.append("━━━━━━━━━━━━ ADMIN NOTES ━━━━━━━━━━━━━\n");
                details.append(report.get("admin_notes")).append("\n\n");
            }
            
            if (report.get("resolved_date") != null) {
                details.append("Resolved Date: ").append(formatDateTime(report.get("resolved_date"))).append("\n");
            }

            reportDetailsArea.setText(details.toString());
            reportDetailsArea.setCaretPosition(0);

            // Enable/disable buttons based on status
            boolean isPending = "pending".equals(selectedReportStatus);
            boolean isUnderReview = "under review".equals(selectedReportStatus) || "under_review".equals(selectedReportStatus);
            boolean isResolved = "resolved".equals(selectedReportStatus);

            markUnderReviewButton.setEnabled(isPending);
            resolveReportButton.setEnabled(isPending || isUnderReview);
            dismissReportButton.setEnabled(isPending || isUnderReview);
            contactReporterButton.setEnabled(true);
            contactReportedButton.setEnabled(true);
            viewTradeButton.setEnabled(report.get("trade_id") != null);
        }
    }

    private void clearSelection() {
        selectedReportId = -1;
        selectedReportStatus = "";
        reportDetailsArea.setText("Select a report to view details");
        
        markUnderReviewButton.setEnabled(false);
        resolveReportButton.setEnabled(false);
        dismissReportButton.setEnabled(false);
        contactReporterButton.setEnabled(false);
        contactReportedButton.setEnabled(false);
        viewTradeButton.setEnabled(false);
    }

    private void applyFilter() {
        String filter = (String) statusFilter.getSelectedItem();
        
        if (filter.equals("All Reports")) {
            rowSorter.setRowFilter(null);
        } else if (filter.equals("Pending")) {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)pending", 5));
        } else if (filter.equals("Under Review")) {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)under.*review", 5));
        } else if (filter.equals("Resolved")) {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)resolved", 5));
        }
    }

    private void applySearch() {
        String text = searchField.getText().trim();
        if (text.isEmpty()) {
            applyFilter();
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 2, 3, 4));
        }
    }

    private void markUnderReview() {
        if (selectedReportId == -1) return;

        int confirm = JOptionPane.showConfirmDialog(this,
            "Mark this report as Under Review?\n\n"
            + "Report ID: " + selectedReportId,
            "Mark Under Review",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_reports SET report_status = 'under_review' WHERE report_id = ?";
            db.updateRecord(sql, selectedReportId);

            JOptionPane.showMessageDialog(this,
                "Report marked as Under Review.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

            logActivity("Marked report #" + selectedReportId + " as under review");
            loadReportsData();
            clearSelection();
        }
    }

    private void resolveReport() {
        if (selectedReportId == -1) return;

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new java.awt.Dimension(400, 250));

        JLabel resolutionLabel = new JLabel("Resolution Notes:");
        resolutionLabel.setBounds(20, 20, 150, 25);
        panel.add(resolutionLabel);

        JTextArea resolutionArea = new JTextArea();
        resolutionArea.setLineWrap(true);
        resolutionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resolutionArea);
        scrollPane.setBounds(20, 50, 350, 120);
        panel.add(scrollPane);

        JLabel actionLabel = new JLabel("Action Taken:");
        actionLabel.setBounds(20, 180, 100, 25);
        panel.add(actionLabel);

        JComboBox<String> actionCombo = new JComboBox<>(new String[]{
            "Warning issued", "Account suspended", "Account banned", 
            "Trade cancelled", "Refund processed", "No action needed"
        });
        actionCombo.setBounds(130, 180, 240, 25);
        panel.add(actionCombo);

        int result = JOptionPane.showConfirmDialog(this, panel, "Resolve Report", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String resolution = resolutionArea.getText().trim();
            String action = (String) actionCombo.getSelectedItem();
            String notes = "Action: " + action + "\nNotes: " + resolution;

            String sql = "UPDATE tbl_reports SET report_status = 'resolved', admin_notes = ?, resolved_date = datetime('now') WHERE report_id = ?";
            db.updateRecord(sql, notes, selectedReportId);

            JOptionPane.showMessageDialog(this,
                "Report resolved successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

            logActivity("Resolved report #" + selectedReportId + " - " + action);
            loadReportsData();
            clearSelection();
        }
    }

    private void dismissReport() {
        if (selectedReportId == -1) return;

        int confirm = JOptionPane.showConfirmDialog(this,
            "Dismiss this report?\n\n"
            + "Report ID: " + selectedReportId + "\n\n"
            + "This will mark the report as dismissed with no action taken.",
            "Dismiss Report",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_reports SET report_status = 'resolved', admin_notes = 'Report dismissed - no action taken', resolved_date = datetime('now') WHERE report_id = ?";
            db.updateRecord(sql, selectedReportId);

            JOptionPane.showMessageDialog(this,
                "Report dismissed.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

            logActivity("Dismissed report #" + selectedReportId);
            loadReportsData();
            clearSelection();
        }
    }

    private void contactReporter() {
        if (selectedReportId == -1) return;

        String sql = "SELECT reporter_id FROM tbl_reports WHERE report_id = ?";
        List<Map<String, Object>> result = db.fetchRecords(sql, selectedReportId);

        if (!result.isEmpty()) {
            int reporterId = Integer.parseInt(result.get(0).get("reporter_id").toString());
            
            JOptionPane.showMessageDialog(this,
                "Opening message interface with reporter...\n\n"
                + "This will open the messaging system to contact the reporter.",
                "Contact Reporter",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Here you would open the messaging system
            logActivity("Contacted reporter for report #" + selectedReportId);
        }
    }

    private void contactReported() {
        if (selectedReportId == -1) return;

        String sql = "SELECT reported_trader_id FROM tbl_reports WHERE report_id = ?";
        List<Map<String, Object>> result = db.fetchRecords(sql, selectedReportId);

        if (!result.isEmpty()) {
            int reportedId = Integer.parseInt(result.get(0).get("reported_trader_id").toString());
            
            JOptionPane.showMessageDialog(this,
                "Opening message interface with reported user...\n\n"
                + "This will open the messaging system to contact the reported user.",
                "Contact Reported User",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Here you would open the messaging system
            logActivity("Contacted reported user for report #" + selectedReportId);
        }
    }

    private void viewRelatedTrade() {
        if (selectedReportId == -1) return;

        JOptionPane.showMessageDialog(this,
            "Viewing related trade...\n\n"
            + "This will open the trade management interface for the related trade.",
            "View Trade",
            JOptionPane.INFORMATION_MESSAGE);
        
        // Here you would open the trade management for the related trade
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

    private String formatDateTime(Object dateObj) {
        if (dateObj == null) return "-";
        try {
            String dateStr = dateObj.toString();
            if (dateStr.length() >= 16) {
                return dateStr.substring(0, 16).replace("T", " ");
            }
            return dateStr;
        } catch (Exception e) {
            return "-";
        }
    }

    private int getTotalReports() {
        try {
            String sql = "SELECT COUNT(*) as count FROM tbl_reports";
            return (int) db.getSingleValue(sql);
        } catch (Exception e) {
            return 0;
        }
    }

    private int getPendingCount() {
        try {
            String sql = "SELECT COUNT(*) as count FROM tbl_reports WHERE report_status IN ('pending', 'under_review')";
            return (int) db.getSingleValue(sql);
        } catch (Exception e) {
            return 0;
        }
    }

    private int getResolvedCount() {
        try {
            String sql = "SELECT COUNT(*) as count FROM tbl_reports WHERE report_status = 'resolved'";
            return (int) db.getSingleValue(sql);
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
        } else if (panel == manageTradesPanel) {
            manage_trades tradesFrame = new manage_trades(adminId, adminName);
            tradesFrame.setVisible(true);
            tradesFrame.setLocationRelativeTo(null);
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