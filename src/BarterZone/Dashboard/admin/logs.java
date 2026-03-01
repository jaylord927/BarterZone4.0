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
import javax.swing.JComboBox;
import javax.swing.JTabbedPane;

public class logs extends javax.swing.JFrame {

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
    
    // Tabbed pane for different log types
    private JTabbedPane tabbedPane;
    
    // Admin Logs Table
    private JScrollPane adminScrollPane;
    private javax.swing.JTable adminTable;
    private DefaultTableModel adminTableModel;
    private TableRowSorter<DefaultTableModel> adminRowSorter;
    
    // Trader Logs Table
    private JScrollPane traderScrollPane;
    private javax.swing.JTable traderTable;
    private DefaultTableModel traderTableModel;
    private TableRowSorter<DefaultTableModel> traderRowSorter;
    
    // Filter components
    private JPanel filterPanel;
    private JTextField searchField;
    private JComboBox<String> dateFilter;
    private JComboBox<String> actionFilter;
    private JButton refreshButton;
    private JButton exportButton;
    private JButton clearLogsButton;
    
    // Stats panel
    private JPanel statsPanel;
    private JLabel totalAdminLogsLabel;
    private JLabel totalTraderLogsLabel;
    private JLabel todayLogsLabel;
    private JLabel weekLogsLabel;
    
    // Selected log data
    private int selectedLogId = -1;
    private String selectedLogType = "";
    
    // Colors - Matching profile.java theme (dark blue/gold)
    private Color sideBarColor = new Color(8, 78, 128);
    private Color hoverColor = new Color(20, 100, 150);
    private Color activeColor = new Color(0, 60, 100);
    private Color accentColor = new Color(255, 215, 0);
    private Color badgeColor = new Color(204, 0, 0);
    private Color headerGradientStart = new Color(8, 78, 128);
    private Color headerGradientEnd = new Color(0, 45, 80);
    private Color adminColor = new Color(8, 78, 128);
    private Color traderColor = new Color(0, 102, 102);
    
    private JPanel activePanel = null;

    public logs(int adminId, String adminName) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.session = user_session.getInstance();
        this.db = new config();
        
        initComponents();
        setupSidePanel();
        setupHeader();
        setupContentPanel();
        loadLogsData();
        updateBadges();
        updateStats();
        
        setTitle("System Logs - " + adminName);
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

        // Set active panel to Logs
        setActivePanel(logsPanel);
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
        headerTitle = new JLabel("System Logs");
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
        // Stats Panel
        statsPanel = new JPanel();
        statsPanel.setLayout(null);
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(new LineBorder(accentColor, 1));
        statsPanel.setBounds(20, 15, 840, 60);
        contentPanel.add(statsPanel);

        JLabel statsTitle = new JLabel("Log Statistics:");
        statsTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statsTitle.setForeground(new Color(8, 78, 128));
        statsTitle.setBounds(15, 20, 100, 25);
        statsPanel.add(statsTitle);

        totalAdminLogsLabel = new JLabel("Admin Logs: 0");
        totalAdminLogsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        totalAdminLogsLabel.setForeground(adminColor);
        totalAdminLogsLabel.setBounds(120, 20, 120, 25);
        statsPanel.add(totalAdminLogsLabel);

        totalTraderLogsLabel = new JLabel("Trader Logs: 0");
        totalTraderLogsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        totalTraderLogsLabel.setForeground(traderColor);
        totalTraderLogsLabel.setBounds(250, 20, 120, 25);
        statsPanel.add(totalTraderLogsLabel);

        todayLogsLabel = new JLabel("Today: 0");
        todayLogsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        todayLogsLabel.setForeground(new Color(255, 153, 0));
        todayLogsLabel.setBounds(380, 20, 100, 25);
        statsPanel.add(todayLogsLabel);

        weekLogsLabel = new JLabel("This Week: 0");
        weekLogsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        weekLogsLabel.setForeground(new Color(46, 125, 50));
        weekLogsLabel.setBounds(490, 20, 120, 25);
        statsPanel.add(weekLogsLabel);

        // Filter Panel
        filterPanel = new JPanel();
        filterPanel.setLayout(null);
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(new LineBorder(accentColor, 1));
        filterPanel.setBounds(20, 85, 840, 60);
        contentPanel.add(filterPanel);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchLabel.setForeground(new Color(8, 78, 128));
        searchLabel.setBounds(15, 20, 60, 25);
        filterPanel.add(searchLabel);

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setBounds(80, 20, 200, 25);
        searchField.setBorder(new LineBorder(new Color(200, 200, 200)));
        filterPanel.add(searchField);

        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        dateLabel.setForeground(new Color(8, 78, 128));
        dateLabel.setBounds(300, 20, 40, 25);
        filterPanel.add(dateLabel);

        String[] dateRanges = {"All Time", "Today", "This Week", "This Month", "Last Month"};
        dateFilter = new JComboBox<>(dateRanges);
        dateFilter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateFilter.setBounds(345, 20, 100, 25);
        dateFilter.setBackground(Color.WHITE);
        dateFilter.setBorder(new LineBorder(new Color(200, 200, 200)));
        dateFilter.addActionListener(e -> applyFilters());
        filterPanel.add(dateFilter);

        JLabel actionLabel = new JLabel("Action:");
        actionLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        actionLabel.setForeground(new Color(8, 78, 128));
        actionLabel.setBounds(460, 20, 50, 25);
        filterPanel.add(actionLabel);

        String[] actionTypes = {"All Actions", "Login", "Logout", "Create", "Update", "Delete", "Verify", "Resolve"};
        actionFilter = new JComboBox<>(actionTypes);
        actionFilter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        actionFilter.setBounds(515, 20, 120, 25);
        actionFilter.setBackground(Color.WHITE);
        actionFilter.setBorder(new LineBorder(new Color(200, 200, 200)));
        actionFilter.addActionListener(e -> applyFilters());
        filterPanel.add(actionFilter);

        refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshButton.setBackground(accentColor);
        refreshButton.setForeground(new Color(8, 78, 128));
        refreshButton.setBounds(650, 20, 80, 25);
        refreshButton.setBorder(null);
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> {
            loadLogsData();
            applyFilters();
            updateStats();
        });
        filterPanel.add(refreshButton);

        exportButton = new JButton("Export");
        exportButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        exportButton.setBackground(new Color(8, 78, 128));
        exportButton.setForeground(Color.WHITE);
        exportButton.setBounds(740, 20, 80, 25);
        exportButton.setBorder(null);
        exportButton.setFocusPainted(false);
        exportButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exportButton.addActionListener(e -> exportLogs());
        filterPanel.add(exportButton);

        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabbedPane.setBackground(new Color(245, 245, 250));
        tabbedPane.setForeground(new Color(8, 78, 128));
        tabbedPane.setBounds(20, 155, 840, 380);
        
        // Initialize tables
        initializeTables();
        
        tabbedPane.addTab("Admin Logs", adminScrollPane);
        tabbedPane.addTab("Trader Logs", traderScrollPane);
        
        tabbedPane.addChangeListener(e -> {
            clearSelection();
        });
        
        contentPanel.add(tabbedPane);

        // Setup search
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                applyFilters();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                applyFilters();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                applyFilters();
            }
        });
    }

    private void initializeTables() {
        setupAdminTable();
        adminScrollPane = new JScrollPane(adminTable);
        adminScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        adminScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        setupTraderTable();
        traderScrollPane = new JScrollPane(traderTable);
        traderScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        traderScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    private void setupAdminTable() {
        String[] columns = {"Log ID", "Date & Time", "Admin", "Action", "Description", "IP Address"};
        adminTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        adminTable = new javax.swing.JTable(adminTableModel);
        adminTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        adminTable.setRowHeight(35);
        adminTable.setShowGrid(true);
        adminTable.setGridColor(new Color(230, 230, 230));
        adminTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        adminTable.getTableHeader().setBackground(adminColor);
        adminTable.getTableHeader().setForeground(Color.WHITE);
        adminTable.setSelectionBackground(new Color(184, 239, 255));
        adminTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Set column widths
        adminTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        adminTable.getColumnModel().getColumn(1).setPreferredWidth(130);
        adminTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        adminTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        adminTable.getColumnModel().getColumn(4).setPreferredWidth(300);
        adminTable.getColumnModel().getColumn(5).setPreferredWidth(100);

        adminTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabbedPane.getSelectedIndex() == 0) {
                int selectedRow = adminTable.getSelectedRow();
                if (selectedRow != -1) {
                    int modelRow = adminTable.convertRowIndexToModel(selectedRow);
                    selectedLogId = Integer.parseInt(adminTableModel.getValueAt(modelRow, 0).toString());
                    selectedLogType = "admin";
                } else {
                    clearSelection();
                }
            }
        });
    }

    private void setupTraderTable() {
        String[] columns = {"Log ID", "Date & Time", "Trader", "Action", "Description", "IP Address"};
        traderTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        traderTable = new javax.swing.JTable(traderTableModel);
        traderTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        traderTable.setRowHeight(35);
        traderTable.setShowGrid(true);
        traderTable.setGridColor(new Color(230, 230, 230));
        traderTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        traderTable.getTableHeader().setBackground(traderColor);
        traderTable.getTableHeader().setForeground(Color.WHITE);
        traderTable.setSelectionBackground(new Color(200, 230, 201));
        traderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Set column widths
        traderTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        traderTable.getColumnModel().getColumn(1).setPreferredWidth(130);
        traderTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        traderTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        traderTable.getColumnModel().getColumn(4).setPreferredWidth(300);
        traderTable.getColumnModel().getColumn(5).setPreferredWidth(100);

        traderTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabbedPane.getSelectedIndex() == 1) {
                int selectedRow = traderTable.getSelectedRow();
                if (selectedRow != -1) {
                    int modelRow = traderTable.convertRowIndexToModel(selectedRow);
                    selectedLogId = Integer.parseInt(traderTableModel.getValueAt(modelRow, 0).toString());
                    selectedLogType = "trader";
                } else {
                    clearSelection();
                }
            }
        });
    }

    private void loadLogsData() {
        // Clear tables
        adminTableModel.setRowCount(0);
        traderTableModel.setRowCount(0);

        // Load admin logs from tbl_logs
        String adminSql = "SELECT l.log_id, l.log_date, l.action, l.description, "
                + "u.user_fullname as admin_name, l.ip_address "
                + "FROM tbl_logs l "
                + "LEFT JOIN tbl_users u ON l.admin_id = u.user_id "
                + "ORDER BY l.log_date DESC";

        List<Map<String, Object>> adminLogs = db.fetchRecords(adminSql);
        for (Map<String, Object> log : adminLogs) {
            adminTableModel.addRow(new Object[]{
                log.get("log_id"),
                formatDateTime(log.get("log_date")),
                log.get("admin_name") != null ? log.get("admin_name") : "System",
                log.get("action") != null ? log.get("action") : "N/A",
                log.get("description") != null ? log.get("description") : "N/A",
                log.get("ip_address") != null ? log.get("ip_address") : "127.0.0.1"
            });
        }

        // Load trader actions from various tables (excluding messages)
        String traderSql = "SELECT 'trade' as source, t.trade_id as id, t.trade_DateRequest as date, "
                + "'Trade Request' as action, "
                + "u.user_fullname as trader_name, "
                + "('Trade #' || t.trade_id || ' - ' || t.trade_status) as description "
                + "FROM tbl_trade t "
                + "LEFT JOIN tbl_users u ON t.offer_trader_id = u.user_id "
                + "UNION ALL "
                + "SELECT 'item' as source, i.items_id as id, i.created_date as date, "
                + "'Item Action' as action, "
                + "u.user_fullname as trader_name, "
                + "('Item: ' || i.item_Name || ' - ' || i.item_Status) as description "
                + "FROM tbl_items i "
                + "LEFT JOIN tbl_users u ON i.trader_id = u.user_id "
                + "UNION ALL "
                + "SELECT 'report' as source, r.report_id as id, r.report_date as date, "
                + "'Report Filed' as action, "
                + "u.user_fullname as trader_name, "
                + "('Report #' || r.report_id || ' - ' || r.report_reason) as description "
                + "FROM tbl_reports r "
                + "LEFT JOIN tbl_users u ON r.reporter_id = u.user_id "
                + "ORDER BY date DESC";

        List<Map<String, Object>> traderLogs = db.fetchRecords(traderSql);
        for (Map<String, Object> log : traderLogs) {
            traderTableModel.addRow(new Object[]{
                log.get("id"),
                formatDateTime(log.get("date")),
                log.get("trader_name") != null ? log.get("trader_name") : "Unknown",
                log.get("action"),
                log.get("description"),
                "N/A"
            });
        }

        // Setup row sorters
        adminRowSorter = new TableRowSorter<>(adminTableModel);
        adminTable.setRowSorter(adminRowSorter);
        
        traderRowSorter = new TableRowSorter<>(traderTableModel);
        traderTable.setRowSorter(traderRowSorter);
    }

    private void applyFilters() {
        String searchText = searchField.getText().trim();
        String selectedDate = (String) dateFilter.getSelectedItem();
        String selectedAction = (String) actionFilter.getSelectedItem();
        
        // Build filter for admin table
        StringBuilder adminFilterPattern = new StringBuilder();
        if (!searchText.isEmpty()) {
            adminFilterPattern.append("(?i).*").append(searchText).append(".*");
        }
        
        // Apply date filter
        java.util.Date now = new Date();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        
        switch (selectedDate) {
            case "Today":
                cal.setTime(now);
                cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
                cal.set(java.util.Calendar.MINUTE, 0);
                cal.set(java.util.Calendar.SECOND, 0);
                Date startToday = cal.getTime();
                // This would require more complex filtering - simplified for now
                break;
            case "This Week":
                cal.setTime(now);
                cal.set(java.util.Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                Date startWeek = cal.getTime();
                break;
        }
        
        // Apply action filter
        if (!"All Actions".equals(selectedAction)) {
            // This would need action column filtering
        }
        
        if (adminRowSorter != null) {
            if (adminFilterPattern.length() > 0) {
                adminRowSorter.setRowFilter(RowFilter.regexFilter(adminFilterPattern.toString(), 2, 3, 4));
            } else {
                adminRowSorter.setRowFilter(null);
            }
        }
        
        if (traderRowSorter != null) {
            if (adminFilterPattern.length() > 0) {
                traderRowSorter.setRowFilter(RowFilter.regexFilter(adminFilterPattern.toString(), 2, 3, 4));
            } else {
                traderRowSorter.setRowFilter(null);
            }
        }
    }

    private void updateStats() {
        try {
            // Total admin logs
            String adminCountSql = "SELECT COUNT(*) as count FROM tbl_logs";
            double adminCount = db.getSingleValue(adminCountSql);
            totalAdminLogsLabel.setText("Admin Logs: " + (int) adminCount);

            // Total trader logs (approximate from various tables)
            String traderCountSql = "SELECT (SELECT COUNT(*) FROM tbl_trade) + "
                    + "(SELECT COUNT(*) FROM tbl_items) + "
                    + "(SELECT COUNT(*) FROM tbl_reports) as count";
            double traderCount = db.getSingleValue(traderCountSql);
            totalTraderLogsLabel.setText("Trader Logs: " + (int) traderCount);

            // Today's logs
            String todaySql = "SELECT COUNT(*) as count FROM tbl_logs WHERE date(log_date) = date('now')";
            double todayCount = db.getSingleValue(todaySql);
            todayLogsLabel.setText("Today: " + (int) todayCount);

            // This week's logs
            String weekSql = "SELECT COUNT(*) as count FROM tbl_logs WHERE log_date >= datetime('now', '-7 days')";
            double weekCount = db.getSingleValue(weekSql);
            weekLogsLabel.setText("This Week: " + (int) weekCount);

        } catch (Exception e) {
            System.out.println("Error updating stats: " + e.getMessage());
        }
    }

    private void clearSelection() {
        selectedLogId = -1;
        selectedLogType = "";
    }

    private void exportLogs() {
        JOptionPane.showMessageDialog(this,
            "Export logs feature will generate a CSV file with all log data.\n\n"
            + "This feature is coming soon!",
            "Export Logs",
            JOptionPane.INFORMATION_MESSAGE);
        
        logActivity("Exported system logs");
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