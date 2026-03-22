package BarterZone.Dashboard.trader;

import BarterZone.resources.IconManager;
import BarterZone.Dashboard.session.user_session;
import landing.landing;
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
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

public class trader_dashboard extends javax.swing.JFrame {

    private int traderId;
    private String traderName;
    private user_session session;
    private config db;
    private IconManager iconManager;

    private JPanel sidePanel;
    private JPanel avatarContainer;
    private JLabel avatarLabel;
    private JLabel avatarInitialLabel;

    private JPanel dashboardPanel;
    private JLabel dashboardIcon;
    private JLabel dashboardLabel;

    private JPanel myItemsPanel;
    private JLabel myItemsIcon;
    private JLabel myItemsLabel;

    private JPanel findItemsPanel;
    private JLabel findItemsIcon;
    private JLabel findItemsLabel;

    private JPanel tradesPanel;
    private JLabel tradesIcon;
    private JLabel tradesLabel;

    private JPanel messagesPanel;
    private JLabel messagesIcon;
    private JLabel messagesLabel;

    private JPanel reportsPanel;
    private JLabel reportsIcon;
    private JLabel reportsLabel;

    private JPanel settingsPanel;
    private JLabel settingsIcon;
    private JLabel settingsLabel;

    private JPanel headerPanel;
    private JLabel dashboardTitle;
    private JLabel currentDateLabel;

    private JPanel contentPanel;

    private JPanel welcomePanel;
    private JLabel welcomeMessage;
    private JLabel currentTimeLabel;

    private JPanel statsPanel;
    private JLabel statsTitle;
    private JPanel myItemsCard;
    private JLabel myItemsCount;
    private JLabel myItemsDesc;
    private JPanel activeTradesCard;
    private JLabel activeTradesCount;
    private JLabel activeTradesDesc;
    private JPanel pendingTradesCard;
    private JLabel pendingTradesCount;
    private JLabel pendingTradesDesc;
    private JPanel completedTradesCard;
    private JLabel completedTradesCount;
    private JLabel completedTradesDesc;

    private JPanel quickActionsPanel;
    private JLabel quickActionsTitle;
    private JButton addItemButton;
    private JButton findItemButton;
    private JButton viewTradesButton;
    private JButton viewMessagesButton;
    private JButton landingButton;

    private JPanel recentActivityPanel;
    private JLabel recentActivityTitle;
    private JPanel activityListPanel;

    private Color themeColor = new Color(12, 192, 223);
    private Color hoverColor = new Color(70, 210, 235);
    private Color activeColor = new Color(0, 150, 180);
    private Color headerBgColor = new Color(245, 245, 245);
    private Color cardBgColor = Color.WHITE;
    private Color textColor = new Color(80, 80, 80);
    private Color accentColor = new Color(0, 102, 102);
    private Color initialColor = new Color(0, 102, 102);

    private JPanel activePanel = null;

    public trader_dashboard(int traderId, String traderName) {
        this.traderId = traderId;
        this.traderName = traderName;
        this.session = user_session.getInstance();
        this.db = new config();
        this.iconManager = IconManager.getInstance();

        session.login(traderId, "trader", traderName);

        initComponents();
        initializeIconLabels();
        loadAndResizeIcons();
        setupSidePanel();
        setupHeader();
        setupContentPanel();
        loadDashboardStats();

        setTitle("Trader Dashboard - " + traderName);
        setIconImage(new ImageIcon(getClass().getResource(
                "/BarterZone/resources/icon/logo.png")).getImage());
        setSize(800, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    }

    private void initComponents() {
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        sidePanel = new JPanel();
        sidePanel.setLayout(null);
        sidePanel.setBackground(themeColor);
        sidePanel.setBounds(0, 0, 180, 500);
        sidePanel.setBorder(new LineBorder(new Color(8, 150, 175), 1, true));
        getContentPane().add(sidePanel);

        headerPanel = new JPanel();
        headerPanel.setLayout(null);
        headerPanel.setBackground(headerBgColor);
        headerPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        headerPanel.setBounds(180, 0, 620, 70);
        getContentPane().add(headerPanel);

        contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(new Color(250, 250, 250));
        contentPanel.setBounds(180, 70, 620, 430);
        getContentPane().add(contentPanel);
    }

    private void initializeIconLabels() {
        dashboardIcon = new JLabel();
        myItemsIcon = new JLabel();
        findItemsIcon = new JLabel();
        tradesIcon = new JLabel();
        messagesIcon = new JLabel();
        reportsIcon = new JLabel();
        settingsIcon = new JLabel();
    }

    private void loadAndResizeIcons() {
        setIconSafely(dashboardIcon, iconManager.getSideMenuIcon("dashboard"));
        setIconSafely(myItemsIcon, iconManager.getSideMenuIcon("myitems"));
        setIconSafely(findItemsIcon, iconManager.getSideMenuIcon("finditems"));
        setIconSafely(tradesIcon, iconManager.getSideMenuIcon("trade"));
        setIconSafely(messagesIcon, iconManager.getSideMenuIcon("messages"));
        setIconSafely(reportsIcon, iconManager.getSideMenuIcon("report"));
        setIconSafely(settingsIcon, iconManager.getSideMenuIcon("setting"));
    }

    private void setIconSafely(JLabel label, ImageIcon icon) {
        if (label != null && icon != null) {
            label.setIcon(icon);
            label.setText("");
        }
    }

    private void setupSidePanel() {
        avatarContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillOval(0, 0, 100, 100);
                g2.setColor(initialColor);
                g2.setStroke(new java.awt.BasicStroke(3));
                g2.drawOval(0, 0, 100, 100);
            }
        };
        avatarContainer.setLayout(null);
        avatarContainer.setBounds(40, 20, 100, 100);
        avatarContainer.setOpaque(false);
        sidePanel.add(avatarContainer);

        avatarLabel = new JLabel();
        avatarLabel.setBounds(0, 0, 100, 100);
        avatarLabel.setHorizontalAlignment(JLabel.CENTER);
        avatarLabel.setVerticalAlignment(JLabel.CENTER);
        avatarContainer.add(avatarLabel);

        avatarInitialLabel = new JLabel();
        avatarInitialLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        avatarInitialLabel.setForeground(initialColor);
        avatarInitialLabel.setHorizontalAlignment(JLabel.CENTER);
        avatarInitialLabel.setBounds(40, 120, 100, 30);
        if (traderName != null && traderName.length() > 0) {
            avatarInitialLabel.setText(String.valueOf(traderName.charAt(0)).toUpperCase());
        }
        sidePanel.add(avatarInitialLabel);
        
        loadProfileAvatar();

        int menuY = 155;
        int menuHeight = 32;
        int menuSpacing = 2;

        dashboardPanel = createMenuItem(20, menuY, 140, menuHeight);
        dashboardIcon = createMenuItemIcon(dashboardPanel, 15, 6, dashboardIcon);
        dashboardLabel = createMenuItemLabel(dashboardPanel, "Dashboard", 45, 6);
        menuY += menuHeight + menuSpacing;

        myItemsPanel = createMenuItem(20, menuY, 140, menuHeight);
        myItemsIcon = createMenuItemIcon(myItemsPanel, 15, 6, myItemsIcon);
        myItemsLabel = createMenuItemLabel(myItemsPanel, "My Items", 45, 6);
        menuY += menuHeight + menuSpacing;

        findItemsPanel = createMenuItem(20, menuY, 140, menuHeight);
        findItemsIcon = createMenuItemIcon(findItemsPanel, 15, 6, findItemsIcon);
        findItemsLabel = createMenuItemLabel(findItemsPanel, "Find Items", 45, 6);
        menuY += menuHeight + menuSpacing;

        tradesPanel = createMenuItem(20, menuY, 140, menuHeight);
        tradesIcon = createMenuItemIcon(tradesPanel, 15, 6, tradesIcon);
        tradesLabel = createMenuItemLabel(tradesPanel, "Trades", 45, 6);
        menuY += menuHeight + menuSpacing;

        messagesPanel = createMenuItem(20, menuY, 140, menuHeight);
        messagesIcon = createMenuItemIcon(messagesPanel, 15, 6, messagesIcon);
        messagesLabel = createMenuItemLabel(messagesPanel, "Messages", 45, 6);
        menuY += menuHeight + menuSpacing;

        reportsPanel = createMenuItem(20, menuY, 140, menuHeight);
        reportsIcon = createMenuItemIcon(reportsPanel, 15, 6, reportsIcon);
        reportsLabel = createMenuItemLabel(reportsPanel, "Reports", 45, 6);
        menuY += menuHeight + menuSpacing;

        settingsPanel = createMenuItem(20, menuY, 140, menuHeight);
        settingsIcon = createMenuItemIcon(settingsPanel, 15, 6, settingsIcon);
        settingsLabel = createMenuItemLabel(settingsPanel, "Settings", 45, 6);

        setActivePanel(dashboardPanel);
    }

    private JPanel createMenuItem(int x, int y, int width, int height) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(themeColor);
        panel.setBounds(x, y, width, height);
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        MouseAdapter panelAdapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (panel != activePanel) {
                    panel.setBackground(hoverColor);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (panel != activePanel) {
                    panel.setBackground(themeColor);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                handleMenuClick(panel);
            }
        };
        
        panel.addMouseListener(panelAdapter);
        sidePanel.add(panel);
        return panel;
    }

    private JLabel createMenuItemIcon(JPanel panel, int x, int y, JLabel iconLabel) {
        iconLabel.setBounds(x, y, 25, 20);
        iconLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        iconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (panel != activePanel) {
                    panel.setBackground(hoverColor);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (panel != activePanel) {
                    panel.setBackground(themeColor);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                handleMenuClick(panel);
            }
        });
        
        panel.add(iconLabel);
        return iconLabel;
    }

    private JLabel createMenuItemLabel(JPanel panel, String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(Color.WHITE);
        label.setBounds(x, y, 100, 20);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (panel != activePanel) {
                    panel.setBackground(hoverColor);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (panel != activePanel) {
                    panel.setBackground(themeColor);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                handleMenuClick(panel);
            }
        });
        
        panel.add(label);
        return label;
    }

    private void loadProfileAvatar() {
        try {
            String sql = "SELECT user_profile_picture FROM tbl_users WHERE user_id = ?";
            List<Map<String, Object>> result = db.fetchRecords(sql, traderId);

            if (!result.isEmpty() && result.get(0).get("user_profile_picture") != null) {
                String profilePicPath = result.get(0).get("user_profile_picture").toString();

                if (!profilePicPath.isEmpty()) {
                    String[] possiblePaths = {
                        "src/" + profilePicPath.replace(".", "/"),
                        profilePicPath.replace(".", "/")
                    };
                    
                    int lastDotIndex = profilePicPath.lastIndexOf(".");
                    if (lastDotIndex > 0) {
                        String fileName = profilePicPath.substring(profilePicPath.lastIndexOf(".") + 1);
                        possiblePaths = new String[]{
                            "src/" + profilePicPath.replace(".", "/"),
                            profilePicPath.replace(".", "/"),
                            "src/BarterZone/resources/images/" + fileName,
                            "BarterZone/resources/images/" + fileName
                        };
                    }
                    
                    File imgFile = null;
                    String foundPath = null;
                    
                    for (String path : possiblePaths) {
                        File testFile = new File(path);
                        if (testFile.exists()) {
                            imgFile = testFile;
                            foundPath = path;
                            break;
                        }
                    }

                    if (imgFile != null && imgFile.exists()) {
                        ImageIcon icon = new ImageIcon(foundPath);
                        Image img = icon.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);

                        java.awt.image.BufferedImage circularImg = new java.awt.image.BufferedImage(90, 90, java.awt.image.BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g2 = circularImg.createGraphics();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setClip(new Ellipse2D.Float(0, 0, 90, 90));
                        g2.drawImage(img, 0, 0, 90, 90, null);
                        g2.dispose();

                        avatarLabel.setIcon(new ImageIcon(circularImg));
                        avatarLabel.setText("");
                        avatarInitialLabel.setVisible(false);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading profile image: " + e.getMessage());
        }

        avatarLabel.setIcon(null);
        avatarLabel.setText("");
        avatarInitialLabel.setVisible(true);
    }

    private void setupHeader() {
        dashboardTitle = new JLabel("Dashboard");
        dashboardTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        dashboardTitle.setForeground(accentColor);
        dashboardTitle.setBounds(20, 15, 150, 30);
        headerPanel.add(dashboardTitle);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        currentDateLabel = new JLabel(sdf.format(new Date()));
        currentDateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        currentDateLabel.setForeground(new Color(102, 102, 102));
        currentDateLabel.setBounds(450, 25, 250, 20);
        headerPanel.add(currentDateLabel);
    }

    private void setupContentPanel() {
        welcomePanel = new JPanel();
        welcomePanel.setLayout(null);
        welcomePanel.setBackground(Color.WHITE);
        welcomePanel.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        welcomePanel.setBounds(15, 10, 590, 60);
        contentPanel.add(welcomePanel);

        welcomeMessage = new JLabel("Welcome back, " + traderName + "!");
        welcomeMessage.setFont(new Font("Segoe UI", Font.BOLD, 16));
        welcomeMessage.setForeground(accentColor);
        welcomeMessage.setBounds(15, 10, 300, 25);
        welcomePanel.add(welcomeMessage);

        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        currentTimeLabel = new JLabel("Current time: " + timeFormat.format(new Date()));
        currentTimeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        currentTimeLabel.setForeground(new Color(120, 120, 120));
        currentTimeLabel.setBounds(15, 30, 200, 20);
        welcomePanel.add(currentTimeLabel);

        statsPanel = new JPanel();
        statsPanel.setLayout(null);
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        statsPanel.setBounds(15, 75, 590, 100);
        contentPanel.add(statsPanel);

        statsTitle = new JLabel("Quick Overview");
        statsTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statsTitle.setForeground(accentColor);
        statsTitle.setBounds(10, 8, 150, 20);
        statsPanel.add(statsTitle);

        int cardWidth = 130;
        int cardHeight = 60;
        int cardX = 15;
        int cardY = 32;

        myItemsCard = createStatCard(statsPanel, cardX, cardY, cardWidth, cardHeight,
                themeColor, "My Items");
        myItemsCount = (JLabel) myItemsCard.getClientProperty("valueLabel");
        myItemsDesc = (JLabel) myItemsCard.getClientProperty("descLabel");

        cardX += 145;
        activeTradesCard = createStatCard(statsPanel, cardX, cardY, cardWidth, cardHeight,
                accentColor, "Active Trades");
        activeTradesCount = (JLabel) activeTradesCard.getClientProperty("valueLabel");
        activeTradesDesc = (JLabel) activeTradesCard.getClientProperty("descLabel");

        cardX += 145;
        pendingTradesCard = createStatCard(statsPanel, cardX, cardY, cardWidth, cardHeight,
                new Color(255, 153, 0), "Pending");
        pendingTradesCount = (JLabel) pendingTradesCard.getClientProperty("valueLabel");
        pendingTradesDesc = (JLabel) pendingTradesCard.getClientProperty("descLabel");

        cardX += 145;
        completedTradesCard = createStatCard(statsPanel, cardX, cardY, cardWidth, cardHeight,
                new Color(46, 125, 50), "Completed");
        completedTradesCount = (JLabel) completedTradesCard.getClientProperty("valueLabel");
        completedTradesDesc = (JLabel) completedTradesCard.getClientProperty("descLabel");

        quickActionsPanel = new JPanel();
        quickActionsPanel.setLayout(null);
        quickActionsPanel.setBackground(Color.WHITE);
        quickActionsPanel.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        quickActionsPanel.setBounds(15, 180, 290, 150);
        contentPanel.add(quickActionsPanel);

        quickActionsTitle = new JLabel("Quick Actions");
        quickActionsTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        quickActionsTitle.setForeground(accentColor);
        quickActionsTitle.setBounds(10, 8, 150, 20);
        quickActionsPanel.add(quickActionsTitle);

        int buttonX = 15;
        int buttonY = 35;
        int buttonWidth = 120;
        int buttonHeight = 28;

        addItemButton = new JButton("Add Item");
        addItemButton.setFont(new Font("Segoe UI", Font.BOLD, 10));
        addItemButton.setBackground(themeColor);
        addItemButton.setForeground(Color.WHITE);
        addItemButton.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
        addItemButton.setBorder(null);
        addItemButton.setFocusPainted(false);
        addItemButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addItemButton.addActionListener(e -> openMyItems());
        quickActionsPanel.add(addItemButton);

        findItemButton = new JButton("Find Items");
        findItemButton.setFont(new Font("Segoe UI", Font.BOLD, 10));
        findItemButton.setBackground(accentColor);
        findItemButton.setForeground(Color.WHITE);
        findItemButton.setBounds(buttonX + 140, buttonY, buttonWidth, buttonHeight);
        findItemButton.setBorder(null);
        findItemButton.setFocusPainted(false);
        findItemButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        findItemButton.addActionListener(e -> openFindItems());
        quickActionsPanel.add(findItemButton);

        viewTradesButton = new JButton("My Trades");
        viewTradesButton.setFont(new Font("Segoe UI", Font.BOLD, 10));
        viewTradesButton.setBackground(new Color(255, 153, 0));
        viewTradesButton.setForeground(Color.WHITE);
        viewTradesButton.setBounds(buttonX, buttonY + 38, buttonWidth, buttonHeight);
        viewTradesButton.setBorder(null);
        viewTradesButton.setFocusPainted(false);
        viewTradesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewTradesButton.addActionListener(e -> openTrades());
        quickActionsPanel.add(viewTradesButton);

        viewMessagesButton = new JButton("Messages");
        viewMessagesButton.setFont(new Font("Segoe UI", Font.BOLD, 10));
        viewMessagesButton.setBackground(new Color(46, 125, 50));
        viewMessagesButton.setForeground(Color.WHITE);
        viewMessagesButton.setBounds(buttonX + 140, buttonY + 38, buttonWidth, buttonHeight);
        viewMessagesButton.setBorder(null);
        viewMessagesButton.setFocusPainted(false);
        viewMessagesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewMessagesButton.addActionListener(e -> openMessages());
        quickActionsPanel.add(viewMessagesButton);

        landingButton = new JButton("Landing Page");
        landingButton.setFont(new Font("Segoe UI", Font.BOLD, 10));
        landingButton.setBackground(themeColor);
        landingButton.setForeground(Color.WHITE);
        landingButton.setBounds(buttonX, buttonY + 76, buttonWidth, buttonHeight);
        landingButton.setBorder(null);
        landingButton.setFocusPainted(false);
        landingButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        landingButton.addActionListener(e -> openLanding());
        quickActionsPanel.add(landingButton);

        recentActivityPanel = new JPanel();
        recentActivityPanel.setLayout(null);
        recentActivityPanel.setBackground(Color.WHITE);
        recentActivityPanel.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        recentActivityPanel.setBounds(315, 180, 290, 150);
        contentPanel.add(recentActivityPanel);

        recentActivityTitle = new JLabel("Recent Activity");
        recentActivityTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        recentActivityTitle.setForeground(accentColor);
        recentActivityTitle.setBounds(10, 8, 150, 20);
        recentActivityPanel.add(recentActivityTitle);

        activityListPanel = new JPanel();
        activityListPanel.setLayout(null);
        activityListPanel.setBackground(Color.WHITE);
        activityListPanel.setBounds(10, 32, 270, 105);
        recentActivityPanel.add(activityListPanel);

        loadRecentActivity();

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(null);
        infoPanel.setBackground(new Color(240, 240, 240));
        infoPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        infoPanel.setBounds(15, 340, 590, 35);
        contentPanel.add(infoPanel);

        JLabel infoLabel = new JLabel("Need help? Contact support or check our FAQ section.");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        infoLabel.setForeground(textColor);
        infoLabel.setBounds(10, 8, 400, 18);
        infoPanel.add(infoLabel);

        JLabel versionLabel = new JLabel("BarterZone v3.0");
        versionLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        versionLabel.setForeground(new Color(150, 150, 150));
        versionLabel.setBounds(510, 8, 70, 18);
        infoPanel.add(versionLabel);
    }

    private JPanel createStatCard(JPanel parent, int x, int y, int width, int height,
            Color color, String title) {
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBackground(color);
        card.setBounds(x, y, width, height);
        card.setBorder(new LineBorder(Color.WHITE, 1));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(5, 5, width - 10, 15);
        card.add(titleLabel);

        JLabel valueLabel = new JLabel("0");
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setBounds(5, 20, width - 10, 25);
        card.add(valueLabel);

        JLabel descLabel = new JLabel("items");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 8));
        descLabel.setForeground(Color.WHITE);
        descLabel.setBounds(5, 42, width - 10, 12);
        card.add(descLabel);

        card.putClientProperty("valueLabel", valueLabel);
        card.putClientProperty("descLabel", descLabel);

        parent.add(card);
        return card;
    }

    private void loadDashboardStats() {
        try {
            String itemsSql = "SELECT COUNT(*) as count FROM tbl_items WHERE trader_id = ? AND is_active = 1";
            double itemsCount = db.getSingleValue(itemsSql, traderId);
            myItemsCount.setText(String.valueOf((int) itemsCount));
            myItemsDesc.setText((int) itemsCount == 1 ? "item" : "items");

            String activeSql = "SELECT COUNT(*) as count FROM tbl_trade WHERE (offer_trader_id = ? OR target_trader_id = ?) AND trade_status IN ('negotiating', 'arrangements_confirmed')";
            double activeCount = db.getSingleValue(activeSql, traderId, traderId);
            activeTradesCount.setText(String.valueOf((int) activeCount));
            activeTradesDesc.setText((int) activeCount == 1 ? "active" : "active");

            String pendingSql = "SELECT COUNT(*) as count FROM tbl_trade WHERE target_trader_id = ? AND trade_status = 'pending'";
            double pendingCount = db.getSingleValue(pendingSql, traderId);
            pendingTradesCount.setText(String.valueOf((int) pendingCount));
            pendingTradesDesc.setText((int) pendingCount == 1 ? "request" : "requests");

            String completedSql = "SELECT COUNT(*) as count FROM tbl_trade_history WHERE (offer_trader_id = ? OR target_trader_id = ?)";
            double completedCount = db.getSingleValue(completedSql, traderId, traderId);
            completedTradesCount.setText(String.valueOf((int) completedCount));
            completedTradesDesc.setText((int) completedCount == 1 ? "trade" : "trades");

        } catch (Exception e) {
            System.out.println("Error loading stats: " + e.getMessage());
        }
    }

    private void loadRecentActivity() {
        activityListPanel.removeAll();

        try {
            String sql = "SELECT 'Trade' as type, trade_id as id, trade_DateRequest as date, trade_status as status "
                    + "FROM tbl_trade WHERE offer_trader_id = ? OR target_trader_id = ? "
                    + "UNION ALL "
                    + "SELECT 'Item' as type, items_id as id, created_date as date, item_Name as status "
                    + "FROM tbl_items WHERE trader_id = ? "
                    + "ORDER BY date DESC LIMIT 5";

            List<Map<String, Object>> activities = db.fetchRecords(sql, traderId, traderId, traderId);

            int yPos = 0;
            if (activities.isEmpty()) {
                JLabel emptyLabel = new JLabel("No recent activity");
                emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
                emptyLabel.setForeground(new Color(150, 150, 150));
                emptyLabel.setBounds(5, yPos, 250, 18);
                activityListPanel.add(emptyLabel);
            } else {
                for (Map<String, Object> act : activities) {
                    String type = (String) act.get("type");
                    String date = act.get("date") != null ? act.get("date").toString() : "";
                    if (date.length() > 10) {
                        date = date.substring(0, 10);
                    }

                    String display = "• " + type + ": " + act.get("status") + " (" + date + ")";
                    if (display.length() > 35) {
                        display = display.substring(0, 32) + "...";
                    }

                    JLabel activityLabel = new JLabel(display);
                    activityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                    activityLabel.setForeground(textColor);
                    activityLabel.setBounds(5, yPos, 250, 18);
                    activityListPanel.add(activityLabel);

                    yPos += 18;
                }
            }
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Unable to load activities");
            errorLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            errorLabel.setForeground(new Color(204, 0, 0));
            errorLabel.setBounds(5, 10, 250, 18);
            activityListPanel.add(errorLabel);
        }

        activityListPanel.revalidate();
        activityListPanel.repaint();
    }

    private void setActivePanel(JPanel panel) {
        if (activePanel != null) {
            activePanel.setBackground(themeColor);
        }
        activePanel = panel;
        activePanel.setBackground(activeColor);
    }

    private void handleMenuClick(JPanel panel) {
        setActivePanel(panel);

        if (panel == dashboardPanel) {
            refreshDashboard();
        } else if (panel == myItemsPanel) {
            openMyItems();
        } else if (panel == findItemsPanel) {
            openFindItems();
        } else if (panel == tradesPanel) {
            openTrades();
        } else if (panel == messagesPanel) {
            openMessages();
        } else if (panel == reportsPanel) {
            openReports();
        } else if (panel == settingsPanel) {
            openSettings();
        }
    }

    private void refreshDashboard() {
        loadDashboardStats();
        loadRecentActivity();
        JOptionPane.showMessageDialog(this, "Dashboard refreshed!", "Refresh", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openMyItems() {
        myitems myItemsFrame = new myitems(traderId, traderName);
        myItemsFrame.setVisible(true);
        myItemsFrame.setLocationRelativeTo(null);
        this.dispose();
    }

    private void openFindItems() {
        finditems findItemsFrame = new finditems(traderId, traderName);
        findItemsFrame.setVisible(true);
        findItemsFrame.setLocationRelativeTo(null);
        this.dispose();
    }

    private void openTrades() {
        trades tradesFrame = new trades(traderId, traderName);
        tradesFrame.setVisible(true);
        tradesFrame.setLocationRelativeTo(null);
        this.dispose();
    }

    private void openMessages() {
        messages messagesFrame = new messages(traderId, traderName);
        messagesFrame.setVisible(true);
        messagesFrame.setLocationRelativeTo(null);
        this.dispose();
    }

    private void openReports() {
        reports reportsFrame = new reports(traderId, traderName);
        reportsFrame.setVisible(true);
        reportsFrame.setLocationRelativeTo(null);
        this.dispose();
    }

    private void openSettings() {
        settings settingsFrame = new settings(traderId, traderName);
        settingsFrame.setVisible(true);
        settingsFrame.setLocationRelativeTo(null);
        this.dispose();
    }

    private void openLanding() {
        landing landingFrame = new landing(traderId, traderName);
        landingFrame.setVisible(true);
        landingFrame.setLocationRelativeTo(null);
        this.dispose();
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            session.logout();
            landing landingFrame = new landing();
            landingFrame.setVisible(true);
            landingFrame.setLocationRelativeTo(null);
            this.dispose();
        }
    }
}