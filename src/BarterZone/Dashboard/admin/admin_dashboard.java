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
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;

public class admin_dashboard extends javax.swing.JFrame {

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
    
    // Welcome section
    private JPanel welcomePanel;
    private JLabel welcomeLabel;
    private JLabel welcomeMessageLabel;
    private JLabel adminBadge;
    
    // Stats cards
    private JPanel statsPanel;
    private JLabel statsTitle;
    private JPanel totalReportsCard;
    private JLabel totalReportsValue;
    private JLabel totalReportsLabel;
    private JPanel totalUsersCard;
    private JLabel totalUsersValue;
    private JLabel totalUsersLabel;
    private JPanel totalAnnouncementsCard;
    private JLabel totalAnnouncementsValue;
    private JLabel totalAnnouncementsLabel;
    private JPanel totalTradesCard;
    private JLabel totalTradesValue;
    private JLabel totalTradesLabel;
    private JPanel totalFeesCard;
    private JLabel totalFeesValue;
    private JLabel totalFeesLabel;
    
    // Quick actions panel
    private JPanel quickActionsPanel;
    private JLabel quickActionsTitle;
    private JButton addAdminButton;
    private JButton newAnnouncementButton;
    private JButton viewReportsButton;
    private JButton refreshDataButton;
    
    // Recent activity panel
    private JPanel recentActivityPanel;
    private JLabel recentActivityTitle;
    private JPanel activityListPanel;
    private JLabel[] activityLabels;
    private String[] activities;
    
    // System health panel
    private JPanel systemHealthPanel;
    private JLabel systemHealthTitle;
    private JLabel dbStatusLabel;
    private JLabel serverTimeLabel;
    private JLabel uptimeLabel;
    private JLabel lastBackupLabel;
    
    // Colors - Matching profile.java theme (dark blue/gold)
    private Color sideBarColor = new Color(8, 78, 128);
    private Color hoverColor = new Color(20, 100, 150);
    private Color activeColor = new Color(0, 60, 100);
    private Color accentColor = new Color(255, 215, 0);
    private Color badgeColor = new Color(204, 0, 0);
    private Color headerGradientStart = new Color(8, 78, 128);
    private Color headerGradientEnd = new Color(0, 45, 80);
    private Color cardColors[] = {
        new Color(8, 78, 128),     // Dark blue
        new Color(46, 125, 50),    // Green
        new Color(255, 153, 0),    // Orange
        new Color(106, 27, 154),   // Purple
        new Color(255, 215, 0)     // Gold
    };
    
    private JPanel activePanel = null;

    public admin_dashboard(int adminId, String adminName) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.session = user_session.getInstance();
        this.db = new config();
        
        initComponents();
        setupSidePanel();
        setupHeader();
        setupContentPanel();
        loadStats();
        loadRecentActivities();
        updateBadges();
        
        setTitle("Admin Dashboard - " + adminName);
        setSize(1100, 650);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
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

        // Set active panel to Dashboard
        setActivePanel(dashboardPanel);
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
        headerTitle = new JLabel("Dashboard");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerTitle.setForeground(Color.WHITE);
        headerTitle.setBounds(30, 15, 200, 40);
        headerPanel.add(headerTitle);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        currentDateLabel = new JLabel(sdf.format(new Date()));
        currentDateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        currentDateLabel.setForeground(Color.WHITE);
        currentDateLabel.setBounds(600, 25, 250, 30);
        headerPanel.add(currentDateLabel);
    }

    private void setupContentPanel() {
        // Welcome Panel
        welcomePanel = new JPanel();
        welcomePanel.setLayout(null);
        welcomePanel.setBackground(Color.WHITE);
        welcomePanel.setBorder(new LineBorder(accentColor, 1));
        welcomePanel.setBounds(20, 15, 840, 80);
        contentPanel.add(welcomePanel);

        welcomeLabel = new JLabel("Welcome back, " + adminName + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcomeLabel.setForeground(new Color(8, 78, 128));
        welcomeLabel.setBounds(20, 15, 400, 30);
        welcomePanel.add(welcomeLabel);

        welcomeMessageLabel = new JLabel("Here's what's happening with your system today.");
        welcomeMessageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        welcomeMessageLabel.setForeground(new Color(102, 102, 102));
        welcomeMessageLabel.setBounds(20, 45, 400, 25);
        welcomePanel.add(welcomeMessageLabel);

        adminBadge = new JLabel("Administrator");
        adminBadge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        adminBadge.setForeground(accentColor);
        adminBadge.setBackground(new Color(8, 78, 128));
        adminBadge.setOpaque(true);
        adminBadge.setHorizontalAlignment(JLabel.CENTER);
        adminBadge.setBounds(700, 25, 120, 30);
        adminBadge.setBorder(new LineBorder(accentColor, 1));
        welcomePanel.add(adminBadge);

        // Stats Panel
        statsPanel = new JPanel();
        statsPanel.setLayout(null);
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(new LineBorder(accentColor, 1));
        statsPanel.setBounds(20, 105, 840, 120);
        contentPanel.add(statsPanel);

        statsTitle = new JLabel("System Overview");
        statsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        statsTitle.setForeground(new Color(8, 78, 128));
        statsTitle.setBounds(15, 10, 200, 25);
        statsPanel.add(statsTitle);

        int cardWidth = 150;
        int cardHeight = 70;
        int cardX = 30;
        int cardY = 40;

        // Total Reports Card
        totalReportsCard = createStatCard(statsPanel, cardX, cardY, cardWidth, cardHeight, 
            cardColors[0], "Reports");
        totalReportsValue = (JLabel) totalReportsCard.getClientProperty("valueLabel");
        totalReportsLabel = (JLabel) totalReportsCard.getClientProperty("titleLabel");
        
        cardX += 160;
        totalUsersCard = createStatCard(statsPanel, cardX, cardY, cardWidth, cardHeight, 
            cardColors[1], "Users");
        totalUsersValue = (JLabel) totalUsersCard.getClientProperty("valueLabel");
        totalUsersLabel = (JLabel) totalUsersCard.getClientProperty("titleLabel");
        
        cardX += 160;
        totalAnnouncementsCard = createStatCard(statsPanel, cardX, cardY, cardWidth, cardHeight, 
            cardColors[2], "Announcements");
        totalAnnouncementsValue = (JLabel) totalAnnouncementsCard.getClientProperty("valueLabel");
        totalAnnouncementsLabel = (JLabel) totalAnnouncementsCard.getClientProperty("titleLabel");
        
        cardX += 160;
        totalTradesCard = createStatCard(statsPanel, cardX, cardY, cardWidth, cardHeight, 
            cardColors[3], "Active Trades");
        totalTradesValue = (JLabel) totalTradesCard.getClientProperty("valueLabel");
        totalTradesLabel = (JLabel) totalTradesCard.getClientProperty("titleLabel");
        
        cardX += 160;
        totalFeesCard = createStatCard(statsPanel, cardX, cardY, cardWidth, cardHeight, 
            cardColors[4], "Total Fees");
        totalFeesValue = (JLabel) totalFeesCard.getClientProperty("valueLabel");
        totalFeesLabel = (JLabel) totalFeesCard.getClientProperty("titleLabel");

        // Quick Actions Panel
        quickActionsPanel = new JPanel();
        quickActionsPanel.setLayout(null);
        quickActionsPanel.setBackground(Color.WHITE);
        quickActionsPanel.setBorder(new LineBorder(accentColor, 1));
        quickActionsPanel.setBounds(20, 235, 410, 150);
        contentPanel.add(quickActionsPanel);

        quickActionsTitle = new JLabel("Quick Actions");
        quickActionsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        quickActionsTitle.setForeground(new Color(8, 78, 128));
        quickActionsTitle.setBounds(15, 10, 150, 25);
        quickActionsPanel.add(quickActionsTitle);

        int buttonWidth = 180;
        int buttonHeight = 35;
        int buttonX = 30;
        int buttonY = 45;
        int buttonSpacing = 10;

        addAdminButton = createActionButton(buttonX, buttonY, buttonWidth, buttonHeight, 
            "➕ Add New Admin", new Color(8, 78, 128));
        addAdminButton.addActionListener(e -> {
            manage_users usersFrame = new manage_users(adminId, adminName);
            usersFrame.setVisible(true);
            usersFrame.setLocationRelativeTo(null);
            this.dispose();
        });
        quickActionsPanel.add(addAdminButton);

        newAnnouncementButton = createActionButton(buttonX + buttonWidth + buttonSpacing, buttonY, 
            buttonWidth, buttonHeight, "📢 New Announcement", accentColor, new Color(8, 78, 128));
        newAnnouncementButton.addActionListener(e -> {
            manage_announcement announcementFrame = new manage_announcement(adminId, adminName);
            announcementFrame.setVisible(true);
            announcementFrame.setLocationRelativeTo(null);
            this.dispose();
        });
        quickActionsPanel.add(newAnnouncementButton);

        viewReportsButton = createActionButton(buttonX, buttonY + buttonHeight + buttonSpacing, 
            buttonWidth, buttonHeight, "⚠️ View Pending Reports", new Color(204, 0, 0));
        viewReportsButton.addActionListener(e -> {
            manage_reports reportsFrame = new manage_reports(adminId, adminName);
            reportsFrame.setVisible(true);
            reportsFrame.setLocationRelativeTo(null);
            this.dispose();
        });
        quickActionsPanel.add(viewReportsButton);

        refreshDataButton = createActionButton(buttonX + buttonWidth + buttonSpacing, 
            buttonY + buttonHeight + buttonSpacing, buttonWidth, buttonHeight, 
            "🔄 Refresh Dashboard", new Color(46, 125, 50));
        refreshDataButton.addActionListener(e -> {
            loadStats();
            loadRecentActivities();
            updateBadges();
            JOptionPane.showMessageDialog(this, "Dashboard data refreshed!", "Refresh", JOptionPane.INFORMATION_MESSAGE);
        });
        quickActionsPanel.add(refreshDataButton);

        // Recent Activity Panel
        recentActivityPanel = new JPanel();
        recentActivityPanel.setLayout(null);
        recentActivityPanel.setBackground(Color.WHITE);
        recentActivityPanel.setBorder(new LineBorder(accentColor, 1));
        recentActivityPanel.setBounds(440, 235, 420, 150);
        contentPanel.add(recentActivityPanel);

        recentActivityTitle = new JLabel("Recent Activity");
        recentActivityTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        recentActivityTitle.setForeground(new Color(8, 78, 128));
        recentActivityTitle.setBounds(15, 10, 150, 25);
        recentActivityPanel.add(recentActivityTitle);

        activityListPanel = new JPanel();
        activityListPanel.setLayout(null);
        activityListPanel.setBackground(Color.WHITE);
        activityListPanel.setBounds(15, 40, 390, 95);
        recentActivityPanel.add(activityListPanel);

        // System Health Panel
        systemHealthPanel = new JPanel();
        systemHealthPanel.setLayout(null);
        systemHealthPanel.setBackground(Color.WHITE);
        systemHealthPanel.setBorder(new LineBorder(accentColor, 1));
        systemHealthPanel.setBounds(20, 395, 840, 140);
        contentPanel.add(systemHealthPanel);

        systemHealthTitle = new JLabel("System Health");
        systemHealthTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        systemHealthTitle.setForeground(new Color(8, 78, 128));
        systemHealthTitle.setBounds(15, 10, 150, 25);
        systemHealthPanel.add(systemHealthTitle);

        dbStatusLabel = new JLabel("✓ Database: Connected");
        dbStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dbStatusLabel.setForeground(new Color(46, 125, 50));
        dbStatusLabel.setBounds(30, 45, 200, 25);
        systemHealthPanel.add(dbStatusLabel);

        serverTimeLabel = new JLabel("🕒 Server Time: " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        serverTimeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        serverTimeLabel.setForeground(new Color(8, 78, 128));
        serverTimeLabel.setBounds(30, 70, 250, 25);
        systemHealthPanel.add(serverTimeLabel);

        uptimeLabel = new JLabel("⏱️ System Uptime: 24d 13h");
        uptimeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        uptimeLabel.setForeground(new Color(8, 78, 128));
        uptimeLabel.setBounds(30, 95, 200, 25);
        systemHealthPanel.add(uptimeLabel);

        lastBackupLabel = new JLabel("💾 Last Backup: " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
        lastBackupLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lastBackupLabel.setForeground(new Color(8, 78, 128));
        lastBackupLabel.setBounds(400, 45, 250, 25);
        systemHealthPanel.add(lastBackupLabel);

        JLabel versionLabel = new JLabel("📦 Version: 3.0.0");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        versionLabel.setForeground(new Color(8, 78, 128));
        versionLabel.setBounds(400, 70, 200, 25);
        systemHealthPanel.add(versionLabel);

        JLabel environmentLabel = new JLabel("🌐 Environment: Production");
        environmentLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        environmentLabel.setForeground(new Color(8, 78, 128));
        environmentLabel.setBounds(400, 95, 200, 25);
        systemHealthPanel.add(environmentLabel);
    }

    private JPanel createStatCard(JPanel parent, int x, int y, int width, int height, 
                                  Color color, String title) {
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBackground(color);
        card.setBounds(x, y, width, height);
        card.setBorder(new LineBorder(Color.WHITE, 1));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(8, 8, width - 16, 15);
        card.add(titleLabel);
        
        JLabel valueLabel = new JLabel("0");
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setBounds(8, 25, width - 16, 30);
        card.add(valueLabel);
        
        card.putClientProperty("titleLabel", titleLabel);
        card.putClientProperty("valueLabel", valueLabel);
        
        parent.add(card);
        return card;
    }

    private JButton createActionButton(int x, int y, int width, int height, String text, Color bgColor) {
        return createActionButton(x, y, width, height, text, bgColor, Color.WHITE);
    }

    private JButton createActionButton(int x, int y, int width, int height, String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setBounds(x, y, width, height);
        button.setBorder(null);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void loadStats() {
        try {
            // Total Reports
            String reportsSql = "SELECT COUNT(*) as count FROM tbl_reports";
            double reportsCount = db.getSingleValue(reportsSql);
            totalReportsValue.setText(String.valueOf((int) reportsCount));
            
            // Total Users
            String usersSql = "SELECT COUNT(*) as count FROM tbl_users";
            double usersCount = db.getSingleValue(usersSql);
            totalUsersValue.setText(String.valueOf((int) usersCount));
            
            // Total Active Announcements
            String announcementsSql = "SELECT COUNT(*) as count FROM tbl_announcement WHERE is_active = 1";
            double announcementsCount = db.getSingleValue(announcementsSql);
            totalAnnouncementsValue.setText(String.valueOf((int) announcementsCount));
            
            // Total Active Trades
            String tradesSql = "SELECT COUNT(*) as count FROM tbl_trade WHERE trade_status IN ('pending', 'negotiating', 'arrangements_confirmed')";
            double tradesCount = db.getSingleValue(tradesSql);
            totalTradesValue.setText(String.valueOf((int) tradesCount));
            
            // Total Fees (from completed trades)
            String feesSql = "SELECT SUM(fee_amount) as total FROM tbl_trade WHERE trade_status = 'completed'";
            double totalFees = db.getSingleValue(feesSql);
            totalFeesValue.setText("₱" + String.format("%.0f", totalFees));
            
        } catch (Exception e) {
            System.out.println("Error loading stats: " + e.getMessage());
        }
    }

    private void loadRecentActivities() {
        activityListPanel.removeAll();
        
        try {
            String sql = "SELECT action, log_date FROM tbl_logs ORDER BY log_date DESC LIMIT 4";
            List<Map<String, Object>> logs = db.fetchRecords(sql);
            
            activities = new String[logs.size()];
            activityLabels = new JLabel[logs.size()];
            
            int yPos = 0;
            for (int i = 0; i < logs.size(); i++) {
                Map<String, Object> log = logs.get(i);
                String date = formatDateTime(log.get("log_date"));
                String action = (String) log.get("action");
                
                String displayText = "• " + date + " - " + action;
                if (displayText.length() > 50) {
                    displayText = displayText.substring(0, 47) + "...";
                }
                
                activityLabels[i] = new JLabel(displayText);
                activityLabels[i].setFont(new Font("Segoe UI", Font.PLAIN, 11));
                activityLabels[i].setForeground(new Color(80, 80, 80));
                activityLabels[i].setBounds(5, yPos, 380, 20);
                activityListPanel.add(activityLabels[i]);
                
                yPos += 22;
            }
            
            if (logs.isEmpty()) {
                JLabel emptyLabel = new JLabel("• No recent activities");
                emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
                emptyLabel.setForeground(new Color(150, 150, 150));
                emptyLabel.setBounds(5, 10, 380, 20);
                activityListPanel.add(emptyLabel);
            }
            
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("• Unable to load activities");
            errorLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            errorLabel.setForeground(new Color(204, 0, 0));
            errorLabel.setBounds(5, 10, 380, 20);
            activityListPanel.add(errorLabel);
        }
        
        activityListPanel.revalidate();
        activityListPanel.repaint();
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
            // Pending Reports Badge
            String pendingReportsSql = "SELECT COUNT(*) as count FROM tbl_reports WHERE report_status IN ('pending', 'under_review')";
            double pendingReports = db.getSingleValue(pendingReportsSql);
            if (pendingReports > 0) {
                reportsBadge.setText(String.valueOf((int) pendingReports));
                reportsBadge.setVisible(true);
            } else {
                reportsBadge.setVisible(false);
            }
            
            // Pending Trades Badge
            String pendingTradesSql = "SELECT COUNT(*) as count FROM tbl_trade WHERE trade_status = 'pending'";
            double pendingTrades = db.getSingleValue(pendingTradesSql);
            if (pendingTrades > 0) {
                tradesBadge.setText(String.valueOf((int) pendingTrades));
                tradesBadge.setVisible(true);
            } else {
                tradesBadge.setVisible(false);
            }
            
            // New Users Badge (users created in last 24 hours)
            String newUsersSql = "SELECT COUNT(*) as count FROM tbl_users WHERE created_date >= datetime('now', '-1 day')";
            double newUsers = db.getSingleValue(newUsersSql);
            if (newUsers > 0) {
                usersBadge.setText(String.valueOf((int) newUsers));
                usersBadge.setVisible(true);
            } else {
                usersBadge.setVisible(false);
            }
            
            // Active Announcements Badge
            String activeAnnouncementsSql = "SELECT COUNT(*) as count FROM tbl_announcement WHERE is_active = 1";
            double activeAnnouncements = db.getSingleValue(activeAnnouncementsSql);
            if (activeAnnouncements > 0) {
                announcementBadge.setText(String.valueOf((int) activeAnnouncements));
                announcementBadge.setVisible(true);
            } else {
                announcementBadge.setVisible(false);
            }
            
            // Recent Logs Badge
            String recentLogsSql = "SELECT COUNT(*) as count FROM tbl_logs WHERE log_date >= datetime('now', '-1 day')";
            double recentLogs = db.getSingleValue(recentLogsSql);
            if (recentLogs > 0) {
                logsBadge.setText(String.valueOf((int) recentLogs));
                logsBadge.setVisible(true);
            } else {
                logsBadge.setVisible(false);
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
            // Already on dashboard
            return;
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
        } else if (panel == logsPanel) {
            logs logsFrame = new logs(adminId, adminName);
            logsFrame.setVisible(true);
            logsFrame.setLocationRelativeTo(null);
            this.dispose();
        } else if (panel == logoutPanel) {
            logout();
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