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
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;

public class profile extends javax.swing.JFrame {

    private user_session session;
    private config db;
    private int adminId;
    private String adminName;

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
    
    // Profile card components
    private JPanel profileCard;
    private JPanel profileHeaderPanel;
    private JLabel profileAvatarLabel;
    private JLabel profileNameLabel;
    private JLabel profileRoleLabel;
    private JLabel profileStatusLabel;
    
    private JPanel infoPanel;
    private JLabel usernameLabel;
    private JLabel usernameValue;
    private JLabel emailLabel;
    private JLabel emailValue;
    private JLabel memberSinceLabel;
    private JLabel memberSinceValue;
    private JLabel lastLoginLabel;
    private JLabel lastLoginValue;
    private JLabel totalActionsLabel;
    private JLabel totalActionsValue;
    private JLabel reportsHandledLabel;
    private JLabel reportsHandledValue;
    
    private JPanel statsPanel;
    private JPanel usersStatCard;
    private JLabel usersStatValue;
    private JPanel tradesStatCard;
    private JLabel tradesStatValue;
    private JPanel reportsStatCard;
    private JLabel reportsStatValue;
    private JPanel feesStatCard;
    private JLabel feesStatValue;
    
    private JButton editProfileButton;
    private JButton changePasswordButton;
    private JButton activityLogButton;
    
    // Colors - Admin theme (dark blue/gold)
    private Color sideBarColor = new Color(8, 78, 128); // Darker blue for admin
    private Color hoverColor = new Color(20, 100, 150);
    private Color activeColor = new Color(0, 60, 100);
    private Color accentColor = new Color(255, 215, 0); // Gold accent
    private Color badgeColor = new Color(204, 0, 0);
    private Color headerGradientStart = new Color(8, 78, 128);
    private Color headerGradientEnd = new Color(0, 45, 80);
    
    private JPanel activePanel = null;

    public profile() {
        this.session = user_session.getInstance();
        this.db = new config();
        
        if (!session.isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "No active session. Please login again.", "Session Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        this.adminId = session.getUserId();
        this.adminName = session.getFullName();
        
        initComponents();
        setupSidePanel();
        setupHeader();
        setupContentPanel();
        loadUserData();
        loadAdminStats();
        updateBadges();
        
        setTitle("Admin Profile - " + adminName);
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

        // Set active panel to Profile
        setActivePanel(profilePanel);
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
        headerTitle = new JLabel("My Profile");
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
        // Profile Header Card
        profileHeaderPanel = new JPanel();
        profileHeaderPanel.setLayout(null);
        profileHeaderPanel.setBackground(new Color(8, 78, 128));
        profileHeaderPanel.setBounds(20, 20, 840, 100);
        profileHeaderPanel.setBorder(new LineBorder(accentColor, 2));
        contentPanel.add(profileHeaderPanel);

        // Admin Avatar in header
        profileAvatarLabel = new JLabel();
        profileAvatarLabel.setFont(new Font("Arial", Font.BOLD, 48));
        profileAvatarLabel.setForeground(accentColor);
        profileAvatarLabel.setHorizontalAlignment(JLabel.CENTER);
        profileAvatarLabel.setBounds(20, 10, 80, 80);
        profileAvatarLabel.setOpaque(true);
        profileAvatarLabel.setBackground(new Color(255, 255, 255, 30));
        profileAvatarLabel.setBorder(new LineBorder(accentColor, 2));
        profileAvatarLabel.setText(String.valueOf(adminName.charAt(0)).toUpperCase());
        profileHeaderPanel.add(profileAvatarLabel);

        profileNameLabel = new JLabel();
        profileNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        profileNameLabel.setForeground(Color.WHITE);
        profileNameLabel.setBounds(120, 20, 400, 35);
        profileHeaderPanel.add(profileNameLabel);

        profileRoleLabel = new JLabel("System Administrator");
        profileRoleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        profileRoleLabel.setForeground(accentColor);
        profileRoleLabel.setBounds(120, 55, 200, 25);
        profileHeaderPanel.add(profileRoleLabel);

        profileStatusLabel = new JLabel();
        profileStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        profileStatusLabel.setForeground(Color.WHITE);
        profileStatusLabel.setBounds(700, 40, 120, 25);
        profileStatusLabel.setHorizontalAlignment(JLabel.CENTER);
        profileStatusLabel.setOpaque(true);
        profileStatusLabel.setBackground(new Color(46, 125, 50));
        profileHeaderPanel.add(profileStatusLabel);

        // Stats Cards
        statsPanel = new JPanel();
        statsPanel.setLayout(null);
        statsPanel.setBackground(new Color(250, 250, 250));
        statsPanel.setBounds(20, 130, 840, 100);
        statsPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        contentPanel.add(statsPanel);

        JLabel statsTitle = new JLabel("Admin Statistics");
        statsTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statsTitle.setForeground(new Color(8, 78, 128));
        statsTitle.setBounds(15, 10, 150, 25);
        statsPanel.add(statsTitle);

        int cardWidth = 180;
        int cardHeight = 50;
        int cardX = 30;
        int cardY = 40;

        // Users Stat Card
        usersStatCard = createStatCard(statsPanel, cardX, cardY, cardWidth, cardHeight, 
            new Color(8, 78, 128), "Total Users", "0");
        usersStatValue = (JLabel) usersStatCard.getClientProperty("valueLabel");
        
        cardX += 200;
        tradesStatCard = createStatCard(statsPanel, cardX, cardY, cardWidth, cardHeight, 
            new Color(0, 102, 102), "Active Trades", "0");
        tradesStatValue = (JLabel) tradesStatCard.getClientProperty("valueLabel");
        
        cardX += 200;
        reportsStatCard = createStatCard(statsPanel, cardX, cardY, cardWidth, cardHeight, 
            new Color(204, 0, 0), "Pending Reports", "0");
        reportsStatValue = (JLabel) reportsStatCard.getClientProperty("valueLabel");
        
        cardX += 200;
        feesStatCard = createStatCard(statsPanel, cardX, cardY, cardWidth, cardHeight, 
            new Color(255, 153, 0), "Total Fees", "₱0");
        feesStatValue = (JLabel) feesStatCard.getClientProperty("valueLabel");

        // Profile Information Card
        JPanel infoCard = new JPanel();
        infoCard.setLayout(null);
        infoCard.setBackground(Color.WHITE);
        infoCard.setBounds(20, 240, 500, 250);
        infoCard.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        contentPanel.add(infoCard);

        JLabel infoTitle = new JLabel("Account Information");
        infoTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        infoTitle.setForeground(new Color(8, 78, 128));
        infoTitle.setBounds(20, 15, 200, 25);
        infoCard.add(infoTitle);

        int labelX = 30;
        int valueX = 180;
        int startY = 60;
        int rowHeight = 35;

        // Username
        usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        usernameLabel.setBounds(labelX, startY, 120, 25);
        infoCard.add(usernameLabel);

        usernameValue = new JLabel();
        usernameValue.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        usernameValue.setBounds(valueX, startY, 250, 25);
        infoCard.add(usernameValue);
        startY += rowHeight;

        // Email
        emailLabel = new JLabel("Email Address:");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        emailLabel.setBounds(labelX, startY, 120, 25);
        infoCard.add(emailLabel);

        emailValue = new JLabel();
        emailValue.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        emailValue.setBounds(valueX, startY, 250, 25);
        infoCard.add(emailValue);
        startY += rowHeight;

        // Member Since
        memberSinceLabel = new JLabel("Member Since:");
        memberSinceLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        memberSinceLabel.setBounds(labelX, startY, 120, 25);
        infoCard.add(memberSinceLabel);

        memberSinceValue = new JLabel();
        memberSinceValue.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        memberSinceValue.setBounds(valueX, startY, 250, 25);
        infoCard.add(memberSinceValue);
        startY += rowHeight;

        // Last Login
        lastLoginLabel = new JLabel("Last Login:");
        lastLoginLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lastLoginLabel.setBounds(labelX, startY, 120, 25);
        infoCard.add(lastLoginLabel);

        lastLoginValue = new JLabel("Today at " + new SimpleDateFormat("hh:mm a").format(new Date()));
        lastLoginValue.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lastLoginValue.setBounds(valueX, startY, 250, 25);
        infoCard.add(lastLoginValue);
        startY += rowHeight;

        // Total Actions
        totalActionsLabel = new JLabel("Total Actions:");
        totalActionsLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        totalActionsLabel.setBounds(labelX, startY, 120, 25);
        infoCard.add(totalActionsLabel);

        totalActionsValue = new JLabel();
        totalActionsValue.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        totalActionsValue.setBounds(valueX, startY, 250, 25);
        infoCard.add(totalActionsValue);

        // Activity Card
        JPanel activityCard = new JPanel();
        activityCard.setLayout(null);
        activityCard.setBackground(Color.WHITE);
        activityCard.setBounds(540, 240, 320, 250);
        activityCard.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        contentPanel.add(activityCard);

        JLabel activityTitle = new JLabel("Recent Activity");
        activityTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        activityTitle.setForeground(new Color(8, 78, 128));
        activityTitle.setBounds(20, 15, 200, 25);
        activityCard.add(activityTitle);

        // Activity list (placeholder)
        String[] activities = getRecentActivities();
        int activityY = 50;
        for (int i = 0; i < Math.min(activities.length, 5); i++) {
            JLabel activityLabel = new JLabel("• " + activities[i]);
            activityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            activityLabel.setForeground(new Color(80, 80, 80));
            activityLabel.setBounds(20, activityY, 280, 20);
            activityCard.add(activityLabel);
            activityY += 25;
        }

        // Action Buttons
        editProfileButton = new JButton("EDIT PROFILE");
        editProfileButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        editProfileButton.setBackground(new Color(8, 78, 128));
        editProfileButton.setForeground(Color.WHITE);
        editProfileButton.setBounds(20, 500, 120, 35);
        editProfileButton.setBorder(null);
        editProfileButton.setFocusPainted(false);
        editProfileButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editProfileButton.addActionListener(e -> editProfile());
        contentPanel.add(editProfileButton);

        changePasswordButton = new JButton("CHANGE PASSWORD");
        changePasswordButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        changePasswordButton.setBackground(new Color(255, 153, 0));
        changePasswordButton.setForeground(Color.WHITE);
        changePasswordButton.setBounds(150, 500, 150, 35);
        changePasswordButton.setBorder(null);
        changePasswordButton.setFocusPainted(false);
        changePasswordButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        changePasswordButton.addActionListener(e -> changePassword());
        contentPanel.add(changePasswordButton);

        activityLogButton = new JButton("VIEW ACTIVITY LOG");
        activityLogButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        activityLogButton.setBackground(new Color(12, 192, 223));
        activityLogButton.setForeground(Color.WHITE);
        activityLogButton.setBounds(310, 500, 150, 35);
        activityLogButton.setBorder(null);
        activityLogButton.setFocusPainted(false);
        activityLogButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        activityLogButton.addActionListener(e -> viewActivityLog());
        contentPanel.add(activityLogButton);
    }

    private JPanel createStatCard(JPanel parent, int x, int y, int width, int height, 
                                  Color color, String title, String value) {
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBackground(color);
        card.setBounds(x, y, width, height);
        card.setBorder(new LineBorder(Color.WHITE, 1));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(8, 5, width - 16, 15);
        card.add(titleLabel);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setBounds(8, 20, width - 16, 25);
        card.add(valueLabel);
        
        card.putClientProperty("valueLabel", valueLabel);
        
        parent.add(card);
        return card;
    }

    private void loadUserData() {
        Map<String, Object> userData = session.getAllUserData();
        
        profileNameLabel.setText((String) userData.get("user_fullname"));
        usernameValue.setText((String) userData.get("user_username"));
        emailValue.setText((String) userData.get("user_email"));
        
        String status = (String) userData.get("user_status");
        if (status != null) {
            if (status.equalsIgnoreCase("active")) {
                profileStatusLabel.setText("ACTIVE");
                profileStatusLabel.setBackground(new Color(46, 125, 50));
            } else {
                profileStatusLabel.setText("INACTIVE");
                profileStatusLabel.setBackground(new Color(204, 0, 0));
            }
        }
        
        String createdDate = getCreatedDate();
        memberSinceValue.setText(createdDate);
        
        // Load admin-specific stats
        totalActionsValue.setText(getTotalActions() + " actions");
    }

    private void loadAdminStats() {
        try {
            String usersSql = "SELECT COUNT(*) as count FROM tbl_users";
            double usersCount = db.getSingleValue(usersSql);
            usersStatValue.setText(String.valueOf((int) usersCount));

            String tradesSql = "SELECT COUNT(*) as count FROM tbl_trade WHERE trade_status IN ('pending', 'negotiating', 'arrangements_confirmed')";
            double tradesCount = db.getSingleValue(tradesSql);
            tradesStatValue.setText(String.valueOf((int) tradesCount));

            String reportsSql = "SELECT COUNT(*) as count FROM tbl_reports WHERE report_status IN ('pending', 'under_review')";
            double reportsCount = db.getSingleValue(reportsSql);
            reportsStatValue.setText(String.valueOf((int) reportsCount));

            String feesSql = "SELECT SUM(fee_amount) as total FROM tbl_trade WHERE trade_status = 'completed'";
            double totalFees = db.getSingleValue(feesSql);
            feesStatValue.setText("₱" + String.format("%.2f", totalFees));

        } catch (Exception e) {
            System.out.println("Error loading admin stats: " + e.getMessage());
        }
    }

    private String getCreatedDate() {
        try {
            String sql = "SELECT created_date FROM tbl_users WHERE user_id = ?";
            java.util.List<Map<String, Object>> result = db.fetchRecords(sql, adminId);
            
            if (!result.isEmpty() && result.get(0).get("created_date") != null) {
                String dateStr = result.get(0).get("created_date").toString();
                if (dateStr.length() >= 10) {
                    String[] dateParts = dateStr.substring(0, 10).split("-");
                    if (dateParts.length == 3) {
                        int year = Integer.parseInt(dateParts[0]);
                        int month = Integer.parseInt(dateParts[1]);
                        int day = Integer.parseInt(dateParts[2]);
                        
                        String[] monthNames = {"January", "February", "March", "April", "May", "June",
                                              "July", "August", "September", "October", "November", "December"};
                        
                        return monthNames[month - 1] + " " + day + ", " + year;
                    }
                }
                return dateStr.substring(0, 10);
            }
            return "N/A";
        } catch (Exception e) {
            return "N/A";
        }
    }

    private int getTotalActions() {
        try {
            String sql = "SELECT COUNT(*) as count FROM tbl_logs WHERE admin_id = ?";
            double count = db.getSingleValue(sql, adminId);
            return (int) count;
        } catch (Exception e) {
            return 0;
        }
    }

    private String[] getRecentActivities() {
        try {
            String sql = "SELECT action, log_date FROM tbl_logs WHERE admin_id = ? ORDER BY log_date DESC LIMIT 5";
            java.util.List<Map<String, Object>> logs = db.fetchRecords(sql, adminId);
            
            String[] activities = new String[logs.size()];
            for (int i = 0; i < logs.size(); i++) {
                Map<String, Object> log = logs.get(i);
                String date = log.get("log_date").toString();
                if (date.length() >= 10) {
                    date = date.substring(0, 10);
                }
                activities[i] = date + " - " + log.get("action");
            }
            return activities;
        } catch (Exception e) {
            return new String[]{"No recent activities"};
        }
    }

    private void updateBadges() {
        try {
            String pendingReportsSql = "SELECT COUNT(*) as count FROM tbl_reports WHERE report_status IN ('pending', 'under_review')";
            double pendingReports = db.getSingleValue(pendingReportsSql);
            reportsBadge.setText(String.valueOf((int) pendingReports));
            reportsBadge.setVisible(pendingReports > 0);

            String pendingTradesSql = "SELECT COUNT(*) as count FROM tbl_trade WHERE trade_status = 'pending'";
            double pendingTrades = db.getSingleValue(pendingTradesSql);
            tradesBadge.setText(String.valueOf((int) pendingTrades));
            tradesBadge.setVisible(pendingTrades > 0);

            String newUsersSql = "SELECT COUNT(*) as count FROM tbl_users WHERE created_date >= datetime('now', '-1 day')";
            double newUsers = db.getSingleValue(newUsersSql);
            usersBadge.setText(String.valueOf((int) newUsers));
            usersBadge.setVisible(newUsers > 0);

            String activeAnnouncementsSql = "SELECT COUNT(*) as count FROM tbl_announcement WHERE is_active = 1";
            double activeAnnouncements = db.getSingleValue(activeAnnouncementsSql);
            announcementBadge.setText(String.valueOf((int) activeAnnouncements));
            announcementBadge.setVisible(activeAnnouncements > 0);

            String recentLogsSql = "SELECT COUNT(*) as count FROM tbl_logs WHERE log_date >= datetime('now', '-1 day')";
            double recentLogs = db.getSingleValue(recentLogsSql);
            logsBadge.setText(String.valueOf((int) recentLogs));
            logsBadge.setVisible(recentLogs > 0);

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
        } else if (panel == logsPanel) {
            logs logsFrame = new logs(adminId, adminName);
            logsFrame.setVisible(true);
            logsFrame.setLocationRelativeTo(null);
            this.dispose();
        } else if (panel == logoutPanel) {
            logout();
        }
    }

    private void editProfile() {
        JOptionPane.showMessageDialog(this, 
            "Edit Profile feature will open edit_profile.java",
            "Edit Profile", JOptionPane.INFORMATION_MESSAGE);
    }

    private void changePassword() {
        JOptionPane.showMessageDialog(this, 
            "Change Password feature coming soon!",
            "Change Password", JOptionPane.INFORMATION_MESSAGE);
    }

    private void viewActivityLog() {
        JOptionPane.showMessageDialog(this, 
            "Activity Log feature will open logs.java",
            "Activity Log", JOptionPane.INFORMATION_MESSAGE);
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