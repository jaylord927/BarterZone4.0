package BarterZone.Dashboard.trader;

import BarterZone.resources.IconManager;
import BarterZone.Dashboard.session.user_session;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class trades extends javax.swing.JFrame {

    private int traderId;
    private String traderName;
    private config db;
    private IconManager iconManager;
    private user_session session;

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
    private JLabel headerTitle;
    private JLabel currentDateLabel;
    
    private JPanel contentPanel;
    
    private javax.swing.JTabbedPane tabbedPane;

    private DefaultTableModel availableTableModel;
    private javax.swing.JTable availableTable;
    private JScrollPane availableScrollPane;

    private DefaultTableModel pendingTableModel;
    private javax.swing.JTable pendingTable;
    private JScrollPane pendingScrollPane;

    private DefaultTableModel activeTableModel;
    private javax.swing.JTable activeTable;
    private JScrollPane activeScrollPane;

    private DefaultTableModel completedTableModel;
    private javax.swing.JTable completedTable;
    private JScrollPane completedScrollPane;

    private JButton acceptButton;
    private JButton declineButton;
    private JButton messageButton;
    private JButton viewFullGuideButton;
    private JButton manageTradeButton;
    private JButton viewTraderDetailsButton;
    private JButton viewMyDetailsButton;

    private JLabel pendingCountLabel;
    private JLabel activeCountLabel;
    private JLabel completedCountLabel;

    private JPanel instructionsPanel;
    private javax.swing.JTextArea instructionsArea;
    private JScrollPane instructionsScrollPane;
    private JLabel selectedTradeInfoLabel;

    private Color themeColor = new Color(12, 192, 223);
    private Color hoverColor = new Color(70, 210, 235);
    private Color activeColor = new Color(0, 150, 180);
    private Color headerBgColor = new Color(245, 245, 245);
    private Color textColor = new Color(80, 80, 80);
    private Color accentColor = new Color(0, 102, 102);
    private Color initialColor = new Color(0, 102, 102);
    
    private Color pendingColor = new Color(255, 153, 0);
    private Color activeColor2 = new Color(0, 102, 102);
    private Color completedColor = new Color(46, 125, 50);
    private Color disputedColor = new Color(204, 0, 0);
    
    private JPanel activePanel = null;
    
    private int selectedTradeId = -1;
    private int selectedOtherTraderId = -1;
    private String selectedOtherTraderName = "";
    private String selectedMyItem = "";
    private String selectedTheirItem = "";
    private String selectedTradeStatus = "";

    public trades(int traderId, String traderName) {
        this.traderId = traderId;
        this.traderName = traderName;
        this.session = user_session.getInstance();
        this.db = new config();
        this.iconManager = IconManager.getInstance();
        
        initComponents();
        initializeIconLabels();
        loadAndResizeIcons();
        setupSidePanel();
        setupHeader();
        setupContentPanel();
        loadAllData();
        loadProfileAvatar();

        setTitle("BarterZone - " + traderName);
        setIconImage(new ImageIcon(getClass().getResource(
                "/BarterZone/resources/icon/logo.png")).getImage());
        setSize(800, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
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

    private BufferedImage createCircularImage(BufferedImage sourceImage, int size) {
        BufferedImage circularImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circularImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Image scaledImage = sourceImage.getScaledInstance(size, size, Image.SCALE_SMOOTH);
        BufferedImage scaledBuffered = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledBuffered.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, size, size, null);
        g2d.dispose();
        g2.setClip(new Ellipse2D.Float(0, 0, size, size));
        g2.drawImage(scaledBuffered, 0, 0, size, size, null);
        g2.dispose();
        return circularImage;
    }

    private ImageIcon loadAndCircleImage(String imagePath, int size) {
        try {
            File file = new File(imagePath);
            if (!file.exists()) {
                return null;
            }
            BufferedImage originalImage = ImageIO.read(file);
            if (originalImage == null) {
                return null;
            }
            BufferedImage circularImage = createCircularImage(originalImage, size);
            return new ImageIcon(circularImage);
        } catch (Exception e) {
            System.out.println("Error loading circular image: " + e.getMessage());
            return null;
        }
    }

    private void loadProfileAvatar() {
        try {
            String sql = "SELECT user_profile_picture FROM tbl_users WHERE user_id = ?";
            List<Map<String, Object>> result = db.fetchRecords(sql, traderId);
            if (!result.isEmpty() && result.get(0).get("user_profile_picture") != null) {
                String profilePicPath = result.get(0).get("user_profile_picture").toString().trim();
                if (!profilePicPath.isEmpty()) {
                    String fullPath = convertResourcePathToFilePath(profilePicPath);
                    if (fullPath != null) {
                        ImageIcon circularIcon = loadAndCircleImage(fullPath, 90);
                        if (circularIcon != null && circularIcon.getIconWidth() > 0) {
                            avatarLabel.setIcon(circularIcon);
                            avatarLabel.setText("");
                            avatarInitialLabel.setVisible(false);
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading profile image: " + e.getMessage());
        }
        avatarLabel.setIcon(null);
        if (traderName != null && !traderName.trim().isEmpty()) {
            avatarInitialLabel.setText(String.valueOf(traderName.trim().charAt(0)).toUpperCase());
        } else {
            avatarInitialLabel.setText("U");
        }
        avatarInitialLabel.setVisible(true);
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

        setActivePanel(tradesPanel);
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

    private void setupHeader() {
        headerTitle = new JLabel("Trades");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerTitle.setForeground(accentColor);
        headerTitle.setBounds(20, 15, 200, 30);
        headerPanel.add(headerTitle);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        currentDateLabel = new JLabel(sdf.format(new Date()));
        currentDateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        currentDateLabel.setForeground(new Color(102, 102, 102));
        currentDateLabel.setBounds(450, 25, 250, 20);
        headerPanel.add(currentDateLabel);
    }

    private void setupContentPanel() {
        contentPanel.removeAll();
        contentPanel.setLayout(null);

        JPanel contentWrapper = new JPanel();
        contentWrapper.setLayout(null);
        contentWrapper.setBackground(Color.WHITE);
        contentWrapper.setBounds(0, 0, 620, 430);

        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(null);
        summaryPanel.setBackground(new Color(245, 245, 245));
        summaryPanel.setBorder(new LineBorder(new Color(12, 192, 223), 2));
        summaryPanel.setBounds(10, 10, 600, 70);

        JPanel pendingCard = new JPanel();
        pendingCard.setLayout(null);
        pendingCard.setBackground(pendingColor);
        pendingCard.setBounds(10, 10, 130, 50);
        pendingCard.setBorder(new LineBorder(Color.WHITE, 2));
        JLabel pendingTitle = new JLabel("PENDING");
        pendingTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pendingTitle.setForeground(Color.WHITE);
        pendingTitle.setBounds(10, 5, 100, 20);
        pendingCard.add(pendingTitle);
        pendingCountLabel = new JLabel("0");
        pendingCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        pendingCountLabel.setForeground(Color.WHITE);
        pendingCountLabel.setBounds(90, 15, 40, 30);
        pendingCard.add(pendingCountLabel);
        summaryPanel.add(pendingCard);

        JPanel activeCard = new JPanel();
        activeCard.setLayout(null);
        activeCard.setBackground(activeColor2);
        activeCard.setBounds(150, 10, 130, 50);
        activeCard.setBorder(new LineBorder(Color.WHITE, 2));
        JLabel activeTitle = new JLabel("ACTIVE");
        activeTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        activeTitle.setForeground(Color.WHITE);
        activeTitle.setBounds(10, 5, 100, 20);
        activeCard.add(activeTitle);
        activeCountLabel = new JLabel("0");
        activeCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        activeCountLabel.setForeground(Color.WHITE);
        activeCountLabel.setBounds(90, 15, 40, 30);
        activeCard.add(activeCountLabel);
        summaryPanel.add(activeCard);

        JPanel completedCard = new JPanel();
        completedCard.setLayout(null);
        completedCard.setBackground(completedColor);
        completedCard.setBounds(290, 10, 130, 50);
        completedCard.setBorder(new LineBorder(Color.WHITE, 2));
        JLabel completedTitle = new JLabel("COMPLETED");
        completedTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        completedTitle.setForeground(Color.WHITE);
        completedTitle.setBounds(10, 5, 100, 20);
        completedCard.add(completedTitle);
        completedCountLabel = new JLabel("0");
        completedCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        completedCountLabel.setForeground(Color.WHITE);
        completedCountLabel.setBounds(90, 15, 40, 30);
        completedCard.add(completedCountLabel);
        summaryPanel.add(completedCard);

        JPanel myItemsCard = new JPanel();
        myItemsCard.setLayout(null);
        myItemsCard.setBackground(themeColor);
        myItemsCard.setBounds(430, 10, 160, 50);
        myItemsCard.setBorder(new LineBorder(Color.WHITE, 2));
        myItemsCard.setCursor(new Cursor(Cursor.HAND_CURSOR));
        MouseAdapter myItemsAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                openMyItems();
            }
            @Override
            public void mouseEntered(MouseEvent evt) {
                myItemsCard.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                myItemsCard.setBackground(themeColor);
            }
        };
        myItemsCard.addMouseListener(myItemsAdapter);
        JLabel myItemsTitle = new JLabel("MY ITEMS →");
        myItemsTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        myItemsTitle.setForeground(Color.WHITE);
        myItemsTitle.setBounds(20, 15, 120, 20);
        myItemsTitle.addMouseListener(myItemsAdapter);
        myItemsCard.add(myItemsTitle);
        summaryPanel.add(myItemsCard);

        tabbedPane = new javax.swing.JTabbedPane();
        tabbedPane.setBounds(10, 90, 600, 300);
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(new Color(245, 245, 245));
        tabbedPane.setForeground(accentColor);

        JPanel availablePanel = new JPanel();
        availablePanel.setLayout(null);
        availablePanel.setBackground(Color.WHITE);
        setupAvailableTable();
        availableScrollPane = new JScrollPane(availableTable);
        availableScrollPane.setBounds(10, 10, 580, 210);
        availableScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        availablePanel.add(availableScrollPane);
        JButton requestTradeButton = new JButton("REQUEST TRADE");
        requestTradeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        requestTradeButton.setBackground(new Color(255, 140, 0));
        requestTradeButton.setForeground(Color.WHITE);
        requestTradeButton.setBounds(220, 230, 160, 40);
        requestTradeButton.setBorder(null);
        requestTradeButton.setFocusPainted(false);
        requestTradeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        requestTradeButton.addActionListener(e -> requestTrade());
        availablePanel.add(requestTradeButton);
        tabbedPane.addTab("Available Items", availablePanel);

        JPanel pendingPanel = new JPanel();
        pendingPanel.setLayout(null);
        pendingPanel.setBackground(Color.WHITE);
        setupPendingTable();
        pendingScrollPane = new JScrollPane(pendingTable);
        pendingScrollPane.setBounds(10, 10, 580, 210);
        pendingScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        pendingPanel.add(pendingScrollPane);
        acceptButton = new JButton("ACCEPT");
        acceptButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        acceptButton.setBackground(completedColor);
        acceptButton.setForeground(Color.WHITE);
        acceptButton.setBounds(150, 230, 100, 35);
        acceptButton.setBorder(null);
        acceptButton.setFocusPainted(false);
        acceptButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        acceptButton.setEnabled(false);
        acceptButton.addActionListener(e -> acceptTrade());
        pendingPanel.add(acceptButton);
        declineButton = new JButton("DECLINE");
        declineButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        declineButton.setBackground(disputedColor);
        declineButton.setForeground(Color.WHITE);
        declineButton.setBounds(260, 230, 100, 35);
        declineButton.setBorder(null);
        declineButton.setFocusPainted(false);
        declineButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        declineButton.setEnabled(false);
        declineButton.addActionListener(e -> declineTrade());
        pendingPanel.add(declineButton);
        messageButton = new JButton("MESSAGE");
        messageButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        messageButton.setBackground(activeColor2);
        messageButton.setForeground(Color.WHITE);
        messageButton.setBounds(370, 230, 100, 35);
        messageButton.setBorder(null);
        messageButton.setFocusPainted(false);
        messageButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        messageButton.setEnabled(false);
        messageButton.addActionListener(e -> sendMessage());
        pendingPanel.add(messageButton);
        tabbedPane.addTab("Pending Trades", pendingPanel);

        JPanel activeMainPanel = new JPanel();
        activeMainPanel.setLayout(null);
        activeMainPanel.setBackground(Color.WHITE);
        setupActiveTable();
        activeScrollPane = new JScrollPane(activeTable);
        activeScrollPane.setBounds(10, 10, 350, 210);
        activeScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        activeMainPanel.add(activeScrollPane);
        instructionsPanel = new JPanel();
        instructionsPanel.setLayout(null);
        instructionsPanel.setBackground(new Color(245, 245, 245));
        instructionsPanel.setBorder(new LineBorder(themeColor, 2));
        instructionsPanel.setBounds(370, 10, 220, 210);
        JLabel instructionsTitle = new JLabel("TRADE STATUS");
        instructionsTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        instructionsTitle.setForeground(accentColor);
        instructionsTitle.setBounds(10, 5, 150, 20);
        instructionsPanel.add(instructionsTitle);
        selectedTradeInfoLabel = new JLabel("Select a trade");
        selectedTradeInfoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        selectedTradeInfoLabel.setForeground(new Color(102, 102, 102));
        selectedTradeInfoLabel.setBounds(10, 25, 200, 15);
        instructionsPanel.add(selectedTradeInfoLabel);
        instructionsArea = new javax.swing.JTextArea();
        instructionsArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        instructionsArea.setLineWrap(true);
        instructionsArea.setWrapStyleWord(true);
        instructionsArea.setEditable(false);
        instructionsArea.setBackground(new Color(245, 245, 245));
        instructionsArea.setText("Select a trade to see options.");
        instructionsScrollPane = new JScrollPane(instructionsArea);
        instructionsScrollPane.setBounds(10, 45, 200, 120);
        instructionsScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        instructionsPanel.add(instructionsScrollPane);
        viewFullGuideButton = new JButton("VIEW FULL GUIDE");
        viewFullGuideButton.setFont(new Font("Segoe UI", Font.BOLD, 10));
        viewFullGuideButton.setBackground(themeColor);
        viewFullGuideButton.setForeground(Color.WHITE);
        viewFullGuideButton.setBounds(55, 170, 110, 20);
        viewFullGuideButton.setBorder(null);
        viewFullGuideButton.setFocusPainted(false);
        viewFullGuideButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewFullGuideButton.setEnabled(false);
        viewFullGuideButton.addActionListener(e -> showFullGuide());
        instructionsPanel.add(viewFullGuideButton);
        activeMainPanel.add(instructionsPanel);
        manageTradeButton = new JButton("MANAGE TRADE");
        manageTradeButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        manageTradeButton.setBackground(activeColor2);
        manageTradeButton.setForeground(Color.WHITE);
        manageTradeButton.setBounds(150, 230, 130, 30);
        manageTradeButton.setBorder(null);
        manageTradeButton.setFocusPainted(false);
        manageTradeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        manageTradeButton.setEnabled(false);
        manageTradeButton.addActionListener(e -> openManageTrade());
        activeMainPanel.add(manageTradeButton);
        viewTraderDetailsButton = new JButton("VIEW TRADER");
        viewTraderDetailsButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        viewTraderDetailsButton.setBackground(new Color(255, 153, 0));
        viewTraderDetailsButton.setForeground(Color.WHITE);
        viewTraderDetailsButton.setBounds(290, 230, 110, 30);
        viewTraderDetailsButton.setBorder(null);
        viewTraderDetailsButton.setFocusPainted(false);
        viewTraderDetailsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewTraderDetailsButton.setEnabled(false);
        viewTraderDetailsButton.addActionListener(e -> viewTraderDetails());
        activeMainPanel.add(viewTraderDetailsButton);
        viewMyDetailsButton = new JButton("MY DETAILS");
        viewMyDetailsButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        viewMyDetailsButton.setBackground(completedColor);
        viewMyDetailsButton.setForeground(Color.WHITE);
        viewMyDetailsButton.setBounds(410, 230, 100, 30);
        viewMyDetailsButton.setBorder(null);
        viewMyDetailsButton.setFocusPainted(false);
        viewMyDetailsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewMyDetailsButton.setEnabled(false);
        viewMyDetailsButton.addActionListener(e -> viewMyDetails());
        activeMainPanel.add(viewMyDetailsButton);
        tabbedPane.addTab("Active Trades", activeMainPanel);

        JPanel historyPanel = new JPanel();
        historyPanel.setLayout(null);
        historyPanel.setBackground(Color.WHITE);
        setupCompletedTable();
        completedScrollPane = new JScrollPane(completedTable);
        completedScrollPane.setBounds(10, 10, 580, 260);
        completedScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        historyPanel.add(completedScrollPane);
        tabbedPane.addTab("Trade History", historyPanel);

        contentWrapper.add(summaryPanel);
        contentWrapper.add(tabbedPane);
        contentPanel.add(contentWrapper);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void setupAvailableTable() {
        String[] columns = {"ID", "Item Name", "Brand", "Condition", "Owner", "Owner ID"};
        availableTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        availableTable = new javax.swing.JTable(availableTableModel);
        availableTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        availableTable.setRowHeight(25);
        availableTable.setShowGrid(true);
        availableTable.setGridColor(themeColor);
        availableTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        availableTable.getTableHeader().setBackground(themeColor);
        availableTable.getTableHeader().setForeground(Color.WHITE);
        availableTable.getTableHeader().setBorder(null);
        availableTable.setSelectionBackground(new Color(184, 239, 255));
        availableTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        availableTable.getColumnModel().getColumn(0).setMinWidth(0);
        availableTable.getColumnModel().getColumn(0).setMaxWidth(0);
        availableTable.getColumnModel().getColumn(0).setWidth(0);
        availableTable.getColumnModel().getColumn(5).setMinWidth(0);
        availableTable.getColumnModel().getColumn(5).setMaxWidth(0);
        availableTable.getColumnModel().getColumn(5).setWidth(0);
    }

    private void setupPendingTable() {
        String[] columns = {"ID", "Requester", "Their Item", "My Item", "Date", "Status", "Trade ID"};
        pendingTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        pendingTable = new javax.swing.JTable(pendingTableModel);
        pendingTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pendingTable.setRowHeight(25);
        pendingTable.setShowGrid(true);
        pendingTable.setGridColor(themeColor);
        pendingTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        pendingTable.getTableHeader().setBackground(pendingColor);
        pendingTable.getTableHeader().setForeground(Color.WHITE);
        pendingTable.getTableHeader().setBorder(null);
        pendingTable.setSelectionBackground(new Color(255, 235, 204));
        pendingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pendingTable.getColumnModel().getColumn(0).setMinWidth(0);
        pendingTable.getColumnModel().getColumn(0).setMaxWidth(0);
        pendingTable.getColumnModel().getColumn(0).setWidth(0);
        pendingTable.getColumnModel().getColumn(6).setMinWidth(0);
        pendingTable.getColumnModel().getColumn(6).setMaxWidth(0);
        pendingTable.getColumnModel().getColumn(6).setWidth(0);
        pendingTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    boolean hasSelection = pendingTable.getSelectedRow() != -1;
                    acceptButton.setEnabled(hasSelection);
                    declineButton.setEnabled(hasSelection);
                    messageButton.setEnabled(hasSelection);
                }
            }
        });
    }

    private void setupActiveTable() {
        String[] columns = {"ID", "Their Item", "Owner", "My Item", "Date", "Status", "Trade ID"};
        activeTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        activeTable = new javax.swing.JTable(activeTableModel);
        activeTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        activeTable.setRowHeight(25);
        activeTable.setShowGrid(true);
        activeTable.setGridColor(themeColor);
        activeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        activeTable.getTableHeader().setBackground(activeColor2);
        activeTable.getTableHeader().setForeground(Color.WHITE);
        activeTable.getTableHeader().setBorder(null);
        activeTable.setSelectionBackground(new Color(184, 239, 255));
        activeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        activeTable.getColumnModel().getColumn(0).setMinWidth(0);
        activeTable.getColumnModel().getColumn(0).setMaxWidth(0);
        activeTable.getColumnModel().getColumn(0).setWidth(0);
        activeTable.getColumnModel().getColumn(6).setMinWidth(0);
        activeTable.getColumnModel().getColumn(6).setMaxWidth(0);
        activeTable.getColumnModel().getColumn(6).setWidth(0);
        activeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    boolean hasSelection = activeTable.getSelectedRow() != -1;
                    if (hasSelection) {
                        int modelRow = activeTable.convertRowIndexToModel(activeTable.getSelectedRow());
                        displayTradeInfo(modelRow);
                    } else {
                        selectedTradeInfoLabel.setText("Select a trade");
                        instructionsArea.setText("Select a trade to see options.");
                        manageTradeButton.setEnabled(false);
                        viewTraderDetailsButton.setEnabled(false);
                        viewMyDetailsButton.setEnabled(false);
                        viewFullGuideButton.setEnabled(false);
                    }
                }
            }
        });
    }

    private void setupCompletedTable() {
        String[] columns = {"ID", "Their Item", "Owner", "My Item", "Date Completed", "Status"};
        completedTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        completedTable = new javax.swing.JTable(completedTableModel);
        completedTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        completedTable.setRowHeight(25);
        completedTable.setShowGrid(true);
        completedTable.setGridColor(themeColor);
        completedTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        completedTable.getTableHeader().setBackground(completedColor);
        completedTable.getTableHeader().setForeground(Color.WHITE);
        completedTable.getTableHeader().setBorder(null);
        completedTable.setSelectionBackground(new Color(200, 230, 201));
        completedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        completedTable.getColumnModel().getColumn(0).setMinWidth(0);
        completedTable.getColumnModel().getColumn(0).setMaxWidth(0);
        completedTable.getColumnModel().getColumn(0).setWidth(0);
    }

    private void loadAllData() {
        loadAvailableItems();
        loadPendingTrades();
        loadActiveTrades();
        loadCompletedTrades();
        updateCounts();
    }

    private void loadAvailableItems() {
        availableTableModel.setRowCount(0);
        String sql = "SELECT i.items_id, i.item_Name, i.item_Brand, i.item_Condition, "
                + "u.user_fullname as owner_name, i.trader_id as owner_id "
                + "FROM tbl_items i "
                + "JOIN tbl_users u ON i.trader_id = u.user_id "
                + "WHERE i.trader_id != ? AND i.is_active = 1 "
                + "AND i.items_id NOT IN ("
                + "    SELECT DISTINCT target_item_id FROM tbl_trade "
                + "    WHERE trade_status IN ('pending', 'negotiating', 'arrangements_confirmed') "
                + "    UNION "
                + "    SELECT DISTINCT offer_item_id FROM tbl_trade "
                + "    WHERE trade_status IN ('pending', 'negotiating', 'arrangements_confirmed')"
                + ") "
                + "ORDER BY i.created_date DESC";
        List<Map<String, Object>> items = db.fetchRecords(sql, traderId);
        for (Map<String, Object> item : items) {
            availableTableModel.addRow(new Object[]{
                item.get("items_id"),
                item.get("item_Name"),
                item.get("item_Brand"),
                item.get("item_Condition"),
                item.get("owner_name"),
                item.get("owner_id")
            });
        }
    }

    private void loadPendingTrades() {
        pendingTableModel.setRowCount(0);
        String sql = "SELECT t.trade_id, "
                + "CASE "
                + "    WHEN t.offer_trader_id = ? THEN 'You' "
                + "    ELSE u_offer.user_fullname "
                + "END as requester, "
                + "CASE "
                + "    WHEN t.offer_trader_id = ? THEN i_offer.item_Name "
                + "    ELSE i_target.item_Name "
                + "END as their_item, "
                + "CASE "
                + "    WHEN t.offer_trader_id = ? THEN i_target.item_Name "
                + "    ELSE i_offer.item_Name "
                + "END as my_item, "
                + "t.trade_DateRequest as date, t.trade_status "
                + "FROM tbl_trade t "
                + "JOIN tbl_items i_offer ON t.offer_item_id = i_offer.items_id "
                + "JOIN tbl_items i_target ON t.target_item_id = i_target.items_id "
                + "LEFT JOIN tbl_users u_offer ON t.offer_trader_id = u_offer.user_id "
                + "WHERE (t.offer_trader_id = ? OR t.target_trader_id = ?) "
                + "AND t.trade_status = 'pending' "
                + "ORDER BY t.trade_DateRequest DESC";
        List<Map<String, Object>> trades = db.fetchRecords(sql, traderId, traderId, traderId, traderId, traderId);
        for (Map<String, Object> trade : trades) {
            pendingTableModel.addRow(new Object[]{
                trade.get("trade_id"),
                trade.get("requester"),
                trade.get("their_item"),
                trade.get("my_item"),
                formatDate(trade.get("date")),
                "Pending",
                trade.get("trade_id")
            });
        }
    }

    private void loadActiveTrades() {
        activeTableModel.setRowCount(0);
        // Show trades that are NOT completed (status not 'completed')
        // Active trades include: negotiating, arrangements_confirmed, payment_verified, items_received
        String sql = "SELECT t.trade_id, "
                + "CASE WHEN t.offer_trader_id = ? THEN i_target.item_Name ELSE i_offer.item_Name END as their_item, "
                + "CASE WHEN t.offer_trader_id = ? THEN u_target.user_fullname ELSE u_offer.user_fullname END as other_trader, "
                + "CASE WHEN t.offer_trader_id = ? THEN i_offer.item_Name ELSE i_target.item_Name END as my_item, "
                + "t.trade_DateRequest as date, t.trade_status "
                + "FROM tbl_trade t "
                + "JOIN tbl_items i_offer ON t.offer_item_id = i_offer.items_id "
                + "JOIN tbl_items i_target ON t.target_item_id = i_target.items_id "
                + "JOIN tbl_users u_offer ON t.offer_trader_id = u_offer.user_id "
                + "JOIN tbl_users u_target ON t.target_trader_id = u_target.user_id "
                + "WHERE (t.offer_trader_id = ? OR t.target_trader_id = ?) "
                + "AND t.trade_status != 'completed' "
                + "AND t.trade_status != 'pending' "
                + "ORDER BY t.trade_DateRequest DESC";
        List<Map<String, Object>> trades = db.fetchRecords(sql, traderId, traderId, traderId, traderId, traderId);
        for (Map<String, Object> trade : trades) {
            String status = trade.get("trade_status").toString();
            String displayStatus = "";
            switch (status) {
                case "negotiating":
                    displayStatus = "Negotiating";
                    break;
                case "arrangements_confirmed":
                    displayStatus = "Arrangements Confirmed";
                    break;
                case "payment_verified":
                    displayStatus = "Payment Verified";
                    break;
                case "items_received":
                    displayStatus = "Items Received";
                    break;
                default:
                    displayStatus = status;
            }
            activeTableModel.addRow(new Object[]{
                trade.get("trade_id"),
                trade.get("their_item"),
                trade.get("other_trader"),
                trade.get("my_item"),
                formatDateTime(trade.get("date")),
                displayStatus,
                trade.get("trade_id")
            });
        }
    }

    private void loadCompletedTrades() {
        completedTableModel.setRowCount(0);
        String sql = "SELECT h.history_id, "
                + "CASE WHEN h.offer_trader_id = ? THEN i_target.item_Name ELSE i_offer.item_Name END as their_item, "
                + "CASE WHEN h.offer_trader_id = ? THEN u_target.user_fullname ELSE u_offer.user_fullname END as other_trader, "
                + "CASE WHEN h.offer_trader_id = ? THEN i_offer.item_Name ELSE i_target.item_Name END as my_item, "
                + "h.trade_DateCompleted as date, h.trade_status "
                + "FROM tbl_trade_history h "
                + "JOIN tbl_items i_offer ON h.offer_item_id = i_offer.items_id "
                + "JOIN tbl_items i_target ON h.target_item_id = i_target.items_id "
                + "JOIN tbl_users u_offer ON h.offer_trader_id = u_offer.user_id "
                + "JOIN tbl_users u_target ON h.target_trader_id = u_target.user_id "
                + "WHERE (h.offer_trader_id = ? OR h.target_trader_id = ?) "
                + "ORDER BY h.trade_DateCompleted DESC";
        List<Map<String, Object>> trades = db.fetchRecords(sql, traderId, traderId, traderId, traderId, traderId);
        for (Map<String, Object> trade : trades) {
            completedTableModel.addRow(new Object[]{
                trade.get("history_id"),
                trade.get("their_item"),
                trade.get("other_trader"),
                trade.get("my_item"),
                formatDateTime(trade.get("date")),
                "Completed"
            });
        }
    }

    private void displayTradeInfo(int modelRow) {
        selectedTradeId = Integer.parseInt(activeTableModel.getValueAt(modelRow, 6).toString());
        selectedTheirItem = activeTableModel.getValueAt(modelRow, 1).toString();
        selectedOtherTraderName = activeTableModel.getValueAt(modelRow, 2).toString();
        selectedMyItem = activeTableModel.getValueAt(modelRow, 3).toString();
        selectedTradeStatus = activeTableModel.getValueAt(modelRow, 5).toString();

        String sql = "SELECT offer_trader_id, target_trader_id FROM tbl_trade WHERE trade_id = ?";
        List<Map<String, Object>> result = db.fetchRecords(sql, selectedTradeId);
        if (!result.isEmpty()) {
            int offerId = Integer.parseInt(result.get(0).get("offer_trader_id").toString());
            int targetId = Integer.parseInt(result.get(0).get("target_trader_id").toString());
            selectedOtherTraderId = (offerId == traderId) ? targetId : offerId;
        }

        selectedTradeInfoLabel.setText("Trade #" + selectedTradeId);
        String detailsSql = "SELECT * FROM tbl_trade WHERE trade_id = ?";
        List<Map<String, Object>> tradeDetails = db.fetchRecords(detailsSql, selectedTradeId);
        StringBuilder info = new StringBuilder();
        info.append("TRADE DETAILS\n");
        info.append("=============\n\n");
        info.append("Your Item: ").append(selectedMyItem).append("\n");
        info.append("Their Item: ").append(selectedTheirItem).append("\n");
        info.append("Trading With: ").append(selectedOtherTraderName).append("\n\n");
        if (!tradeDetails.isEmpty()) {
            Map<String, Object> trade = tradeDetails.get(0);
            String method = trade.get("exchange_method") != null ? trade.get("exchange_method").toString() : "Not set";
            info.append("Exchange Method: ").append(method).append("\n");
            String proposedMethod = trade.get("proposed_method") != null ? trade.get("proposed_method").toString() : "None";
            info.append("Proposed Method: ").append(proposedMethod).append("\n");
            int proposedBy = trade.get("proposed_by") != null ? Integer.parseInt(trade.get("proposed_by").toString()) : -1;
            info.append("Proposed By: ").append(proposedBy == traderId ? "You" : (proposedBy == selectedOtherTraderId ? selectedOtherTraderName : "None")).append("\n");
            int methodConfirmed = trade.get("method_confirmed") != null ? Integer.parseInt(trade.get("method_confirmed").toString()) : 0;
            info.append("Method Confirmed: ").append(methodConfirmed == 1 ? "Yes" : "No").append("\n\n");
        }
        info.append("\nAVAILABLE ACTIONS:\n");
        info.append("Manage Trade - Continue the trade process\n");
        info.append("View Trader - See trader's profile\n");
        info.append("My Details - View your contact information\n");
        info.append("View Full Guide - Complete step-by-step process");
        instructionsArea.setText(info.toString());
        manageTradeButton.setEnabled(true);
        viewTraderDetailsButton.setEnabled(true);
        viewMyDetailsButton.setEnabled(true);
        viewFullGuideButton.setEnabled(true);
    }

    private void showFullGuide() {
        JDialog guideDialog = new JDialog(this, "Complete Trade Guide", true);
        guideDialog.setSize(550, 500);
        guideDialog.setLocationRelativeTo(this);
        guideDialog.getContentPane().setBackground(Color.WHITE);
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(null);
        titlePanel.setBackground(themeColor);
        titlePanel.setBounds(0, 0, 550, 40);
        JLabel titleLabel = new JLabel("COMPLETE TRADE GUIDE");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(20, 5, 300, 30);
        titlePanel.add(titleLabel);
        JButton closeButton = new JButton("CLOSE");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        closeButton.setBackground(accentColor);
        closeButton.setForeground(Color.WHITE);
        closeButton.setBounds(460, 5, 70, 30);
        closeButton.setBorder(null);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> guideDialog.dispose());
        titlePanel.add(closeButton);
        JTextArea guideArea = new JTextArea();
        guideArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        guideArea.setEditable(false);
        guideArea.setLineWrap(true);
        guideArea.setWrapStyleWord(true);
        guideArea.setText(
            "STEP 1: PROPOSE EXCHANGE METHOD\n" +
            "--------------------------------\n" +
            "Click 'Manage Trade' button\n" +
            "Choose between Delivery or Meetup\n" +
            "Both traders must agree on the method\n" +
            "Once agreed, you'll proceed to Step 2\n\n" +
            "STEP 2: EXCHANGE DETAILS\n" +
            "------------------------\n" +
            "Enter your exchange details:\n" +
            "  - For Delivery: Address, courier, tracking, special instructions\n" +
            "  - For Meetup: Location, date, time, contact info\n" +
            "  - OPTIONAL: You can share Google Maps link for meetup location\n" +
            "Both traders enter their details\n" +
            "Review each other's details\n" +
            "When both traders confirm, you proceed to Step 3\n\n" +
            "STEP 3: PAYMENT PROCESSING\n" +
            "--------------------------\n" +
            "Admin will set payment details\n" +
            "Submit your payment proof\n" +
            "Admin verifies both payments\n\n" +
            "STEP 4: SHIPPING & RECEIVING\n" +
            "----------------------------\n" +
            "Ship your item (if delivery) or prepare for meetup\n" +
            "Once you receive the item, mark as received\n" +
            "When both traders mark received, proceed to Step 5\n\n" +
            "STEP 5: COMPLETION & REFUND\n" +
            "---------------------------\n" +
            "Admin processes refunds\n" +
            "Admin keeps the service fee\n" +
            "Trade moves to History\n\n" +
            "TRADE COMPLETED!\n\n" +
            "IMPORTANT REMINDERS\n" +
            "--------------------\n" +
            "Always communicate through BarterZone messages\n" +
            "Keep all payment receipts and screenshots\n" +
            "Verify trader identity before shipping\n" +
            "Report any suspicious activity immediately\n\n" +
            "NEED HELP? CONTACT ADMIN"
        );
        JScrollPane guideScrollPane = new JScrollPane(guideArea);
        guideScrollPane.setBounds(10, 50, 520, 400);
        guideScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        guideScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        guideDialog.setLayout(null);
        guideDialog.add(titlePanel);
        guideDialog.add(guideScrollPane);
        guideDialog.setVisible(true);
    }

    private void viewTraderDetails() {
        if (selectedOtherTraderId == -1) return;
        String sql = "SELECT user_fullname, user_username, user_email, user_status, created_date " +
                     "FROM tbl_users WHERE user_id = ?";
        List<Map<String, Object>> traders = db.fetchRecords(sql, selectedOtherTraderId);
        if (!traders.isEmpty()) {
            Map<String, Object> trader = traders.get(0);
            String details = "TRADER DETAILS\n" +
                    "==============\n\n" +
                    "Name: " + trader.get("user_fullname") + "\n" +
                    "Username: " + trader.get("user_username") + "\n" +
                    "Email: " + trader.get("user_email") + "\n" +
                    "Status: " + trader.get("user_status") + "\n" +
                    "Member Since: " + formatDate(trader.get("created_date"));
            JOptionPane.showMessageDialog(this, details, "Trader Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void viewMyDetails() {
        String sql = "SELECT user_fullname, user_username, user_email, user_status, created_date " +
                     "FROM tbl_users WHERE user_id = ?";
        List<Map<String, Object>> users = db.fetchRecords(sql, traderId);
        if (!users.isEmpty()) {
            Map<String, Object> user = users.get(0);
            String details = "YOUR DETAILS\n" +
                    "============\n\n" +
                    "Name: " + user.get("user_fullname") + "\n" +
                    "Username: " + user.get("user_username") + "\n" +
                    "Email: " + user.get("user_email") + "\n" +
                    "Status: " + user.get("user_status") + "\n" +
                    "Member Since: " + formatDate(user.get("created_date"));
            JOptionPane.showMessageDialog(this, details, "My Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void openManageTrade() {
        if (selectedTradeId == -1) return;
        manage_trades manageFrame = new manage_trades(selectedTradeId, selectedMyItem, 
            selectedTheirItem, selectedOtherTraderName, selectedOtherTraderId);
        manageFrame.setVisible(true);
        manageFrame.setLocationRelativeTo(null);
        this.dispose();
    }

    private void updateCounts() {
        pendingCountLabel.setText(String.valueOf(pendingTableModel.getRowCount()));
        activeCountLabel.setText(String.valueOf(activeTableModel.getRowCount()));
        completedCountLabel.setText(String.valueOf(completedTableModel.getRowCount()));
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

    private void requestTrade() {
        int selectedRow = availableTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to request trade.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = availableTable.convertRowIndexToModel(selectedRow);
        int targetItemId = Integer.parseInt(availableTableModel.getValueAt(modelRow, 0).toString());
        String ownerName = availableTableModel.getValueAt(modelRow, 4).toString();
        int targetOwnerId = Integer.parseInt(availableTableModel.getValueAt(modelRow, 5).toString());
        showTradeRequestDialog(targetItemId, ownerName, targetOwnerId);
    }

    private void showTradeRequestDialog(int targetItemId, String ownerName, int targetOwnerId) {
        String sql = "SELECT items_id, item_Name FROM tbl_items "
                + "WHERE trader_id = ? AND is_active = 1 "
                + "AND items_id NOT IN ("
                + "    SELECT DISTINCT target_item_id FROM tbl_trade "
                + "    WHERE trade_status IN ('pending', 'negotiating', 'arrangements_confirmed') "
                + "    UNION "
                + "    SELECT DISTINCT offer_item_id FROM tbl_trade "
                + "    WHERE trade_status IN ('pending', 'negotiating', 'arrangements_confirmed')"
                + ")";
        List<Map<String, Object>> myItems = db.fetchRecords(sql, traderId);
        if (myItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "You don't have any items available for trade.\n\n"
                    + "All your items may be already in active/completed trades.\n"
                    + "Add new items in 'My Items' to start trading.",
                    "No Items Available", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        String[] itemNames = new String[myItems.size()];
        Integer[] itemIds = new Integer[myItems.size()];
        for (int i = 0; i < myItems.size(); i++) {
            itemNames[i] = myItems.get(i).get("item_Name").toString();
            itemIds[i] = Integer.parseInt(myItems.get(i).get("items_id").toString());
        }
        JDialog tradeDialog = new JDialog(this, "Request Trade", true);
        tradeDialog.setSize(400, 300);
        tradeDialog.setLayout(null);
        tradeDialog.setLocationRelativeTo(this);
        tradeDialog.getContentPane().setBackground(Color.WHITE);
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(themeColor);
        titlePanel.setBounds(0, 0, 400, 40);
        titlePanel.setLayout(null);
        JLabel titleLabel = new JLabel("REQUEST TRADE");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(20, 5, 200, 30);
        titlePanel.add(titleLabel);
        tradeDialog.add(titlePanel);
        JLabel infoLabel = new JLabel("Trading with: " + ownerName);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoLabel.setBounds(20, 60, 300, 25);
        tradeDialog.add(infoLabel);
        JLabel selectLabel = new JLabel("Your Item to offer:");
        selectLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        selectLabel.setBounds(20, 100, 150, 20);
        tradeDialog.add(selectLabel);
        JComboBox<String> itemCombo = new JComboBox<>(itemNames);
        itemCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        itemCombo.setBounds(20, 125, 250, 30);
        itemCombo.setBackground(Color.WHITE);
        tradeDialog.add(itemCombo);
        JButton sendButton = new JButton("SEND REQUEST");
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendButton.setBackground(activeColor2);
        sendButton.setForeground(Color.WHITE);
        sendButton.setBounds(80, 180, 150, 35);
        sendButton.setBorder(null);
        sendButton.setFocusPainted(false);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendButton.addActionListener(e -> {
            int selectedIdx = itemCombo.getSelectedIndex();
            if (selectedIdx >= 0) {
                int selectedOfferItemId = itemIds[selectedIdx];
                createTradeRequest(targetItemId, selectedOfferItemId, ownerName, targetOwnerId);
                tradeDialog.dispose();
            }
        });
        tradeDialog.add(sendButton);
        JButton cancelButton = new JButton("CANCEL");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setBackground(disputedColor);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBounds(240, 180, 100, 35);
        cancelButton.setBorder(null);
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> tradeDialog.dispose());
        tradeDialog.add(cancelButton);
        tradeDialog.setVisible(true);
    }

    private void createTradeRequest(int targetItemId, int offerItemId, String ownerName, int targetOwnerId) {
        if (targetOwnerId == traderId) {
            JOptionPane.showMessageDialog(this, "You cannot trade with your own item.", "Invalid Trade", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String checkSql = "SELECT COUNT(*) as count FROM tbl_trade WHERE "
                + "((offer_item_id = ? AND target_item_id = ?) OR "
                + "(offer_item_id = ? AND target_item_id = ?)) "
                + "AND trade_status IN ('pending', 'negotiating', 'arrangements_confirmed')";
        double count = db.getSingleValue(checkSql, offerItemId, targetItemId, targetItemId, offerItemId);
        if (count > 0) {
            JOptionPane.showMessageDialog(this,
                    "A trade with these exact items is already pending or active.",
                    "Duplicate Trade", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String insertSql = "INSERT INTO tbl_trade (offer_trader_id, target_trader_id, offer_item_id, "
                + "target_item_id, trade_status, trade_DateRequest) "
                + "VALUES (?, ?, ?, ?, 'pending', datetime('now'))";
        try {
            db.addRecord(insertSql, traderId, targetOwnerId, offerItemId, targetItemId);
            JOptionPane.showMessageDialog(this,
                    "Trade request sent to " + ownerName + "!\n\n"
                    + "They will see your request in their Pending Trades.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            loadAllData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error creating trade request: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void acceptTrade() {
        int selectedRow = pendingTable.getSelectedRow();
        if (selectedRow == -1) return;
        int modelRow = pendingTable.convertRowIndexToModel(selectedRow);
        int tradeId = Integer.parseInt(pendingTableModel.getValueAt(modelRow, 6).toString());
        String requester = pendingTableModel.getValueAt(modelRow, 1).toString();
        String theirItem = pendingTableModel.getValueAt(modelRow, 2).toString();
        String myItem = pendingTableModel.getValueAt(modelRow, 3).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Accept this trade?\n\n"
                + "Requester: " + requester + "\n"
                + "Their Item: " + theirItem + "\n"
                + "Your Item: " + myItem,
                "Confirm Accept",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_trade SET trade_status = 'negotiating' WHERE trade_id = ?";
            db.updateRecord(sql, tradeId);
            JOptionPane.showMessageDialog(this,
                    "Trade accepted!\n\n"
                    + "The trade has been moved to Active Trades.\n"
                    + "You can now negotiate exchange details.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            loadAllData();
        }
    }

    private void declineTrade() {
        int selectedRow = pendingTable.getSelectedRow();
        if (selectedRow == -1) return;
        int modelRow = pendingTable.convertRowIndexToModel(selectedRow);
        int tradeId = Integer.parseInt(pendingTableModel.getValueAt(modelRow, 6).toString());
        int confirm = JOptionPane.showConfirmDialog(this,
                "Decline this trade request?", "Confirm Decline",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM tbl_trade WHERE trade_id = ? AND trade_status = 'pending'";
            db.deleteRecord(sql, tradeId);
            JOptionPane.showMessageDialog(this, "Trade declined.", "Info", JOptionPane.INFORMATION_MESSAGE);
            loadAllData();
        }
    }

    private void sendMessage() {
        messages messagesFrame = new messages(traderId, traderName);
        messagesFrame.setVisible(true);
        messagesFrame.setLocationRelativeTo(null);
        this.dispose();
    }

    private void openMyItems() {
        myitems myItemsFrame = new myitems(traderId, traderName);
        myItemsFrame.setVisible(true);
        myItemsFrame.setLocationRelativeTo(null);
        this.dispose();
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
            trader_dashboard dashboard = new trader_dashboard(traderId, traderName);
            dashboard.setVisible(true);
            dashboard.setLocationRelativeTo(null);
            this.dispose();
        } else if (panel == myItemsPanel) {
            myitems myItemsFrame = new myitems(traderId, traderName);
            myItemsFrame.setVisible(true);
            myItemsFrame.setLocationRelativeTo(null);
            this.dispose();
        } else if (panel == findItemsPanel) {
            finditems findItemsFrame = new finditems(traderId, traderName);
            findItemsFrame.setVisible(true);
            findItemsFrame.setLocationRelativeTo(null);
            this.dispose();
        } else if (panel == messagesPanel) {
            messages messagesFrame = new messages(traderId, traderName);
            messagesFrame.setVisible(true);
            messagesFrame.setLocationRelativeTo(null);
            this.dispose();
        } else if (panel == reportsPanel) {
            reports reportsFrame = new reports(traderId, traderName);
            reportsFrame.setVisible(true);
            reportsFrame.setLocationRelativeTo(null);
            this.dispose();
        } else if (panel == settingsPanel) {
            settings settingsFrame = new settings(traderId, traderName);
            settingsFrame.setVisible(true);
            settingsFrame.setLocationRelativeTo(null);
            this.dispose();
        }
    }
} 