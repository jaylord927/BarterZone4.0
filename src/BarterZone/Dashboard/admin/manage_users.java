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
import javax.swing.JPasswordField;

public class manage_users extends javax.swing.JFrame {

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
    
    // Stats panel
    private JPanel statsPanel;
    private JPanel totalUsersCard;
    private JLabel totalUsersValue;
    private JPanel activeUsersCard;
    private JLabel activeUsersValue;
    private JPanel newUsersCard;
    private JLabel newUsersValue;
    private JPanel adminsCard;
    private JLabel adminsValue;
    
    // Filter components
    private JPanel filterPanel;
    private JComboBox<String> userTypeFilter;
    private JTextField searchField;
    private JButton refreshButton;
    
    // Table
    private JScrollPane tableScrollPane;
    private javax.swing.JTable usersTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    
    // Action buttons
    private JPanel actionPanel;
    private JButton deactivateButton;
    private JButton activateButton;
    private JButton addAdminButton;
    private JButton updateAdminButton;
    private JButton deleteAdminButton;
    private JButton addTraderButton;
    private JButton viewDetailsButton;
    
    // Colors - Matching profile.java theme (dark blue/gold)
    private Color sideBarColor = new Color(8, 78, 128);
    private Color hoverColor = new Color(20, 100, 150);
    private Color activeColor = new Color(0, 60, 100);
    private Color accentColor = new Color(255, 215, 0);
    private Color badgeColor = new Color(204, 0, 0);
    private Color headerGradientStart = new Color(8, 78, 128);
    private Color headerGradientEnd = new Color(0, 45, 80);
    private Color cardColors[] = {
        new Color(8, 78, 128),
        new Color(46, 125, 50),
        new Color(255, 153, 0),
        new Color(106, 27, 154)
    };
    
    private JPanel activePanel = null;
    private String currentFilter = "All";
    private int selectedUserId = -1;
    private String selectedUserType = "";
    private String selectedUserSource = "";

    public manage_users(int adminId, String adminName) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.session = user_session.getInstance();
        this.db = new config();
        
        initComponents();
        setupSidePanel();
        setupHeader();
        setupContentPanel();
        loadUserData();
        loadStats();
        updateBadges();
        
        setTitle("Manage Users - " + adminName);
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

        // Set active panel to Manage Users
        setActivePanel(manageUsersPanel);
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
        headerTitle = new JLabel("Manage Users");
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
        statsPanel.setBounds(20, 15, 840, 80);
        contentPanel.add(statsPanel);

        JLabel statsTitle = new JLabel("User Statistics");
        statsTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statsTitle.setForeground(new Color(8, 78, 128));
        statsTitle.setBounds(15, 10, 150, 25);
        statsPanel.add(statsTitle);

        int cardWidth = 160;
        int cardHeight = 45;
        int cardX = 180;
        int cardY = 20;

        // Total Users Card
        totalUsersCard = createStatCard(statsPanel, cardX, cardY, cardWidth, cardHeight, 
            cardColors[0], "Total Users", "0");
        totalUsersValue = (JLabel) totalUsersCard.getClientProperty("valueLabel");
        
        cardX += 170;
        activeUsersCard = createStatCard(statsPanel, cardX, cardY, cardWidth, cardHeight, 
            cardColors[1], "Active Users", "0");
        activeUsersValue = (JLabel) activeUsersCard.getClientProperty("valueLabel");
        
        cardX += 170;
        newUsersCard = createStatCard(statsPanel, cardX, cardY, cardWidth, cardHeight, 
            cardColors[2], "New Today", "0");
        newUsersValue = (JLabel) newUsersCard.getClientProperty("valueLabel");
        
        cardX += 170;
        adminsCard = createStatCard(statsPanel, cardX, cardY, cardWidth, cardHeight, 
            cardColors[3], "Admins", "0");
        adminsValue = (JLabel) adminsCard.getClientProperty("valueLabel");

        // Filter Panel
        filterPanel = new JPanel();
        filterPanel.setLayout(null);
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(new LineBorder(accentColor, 1));
        filterPanel.setBounds(20, 105, 840, 60);
        contentPanel.add(filterPanel);

        JLabel filterLabel = new JLabel("Filter:");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        filterLabel.setForeground(new Color(8, 78, 128));
        filterLabel.setBounds(15, 20, 45, 25);
        filterPanel.add(filterLabel);

        String[] filterTypes = {"All Users", "Traders Only", "Admins Only"};
        userTypeFilter = new JComboBox<>(filterTypes);
        userTypeFilter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userTypeFilter.setBounds(65, 20, 120, 25);
        userTypeFilter.setBackground(Color.WHITE);
        userTypeFilter.setBorder(new LineBorder(new Color(200, 200, 200)));
        userTypeFilter.addActionListener(e -> applyFilter());
        filterPanel.add(userTypeFilter);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchLabel.setForeground(new Color(8, 78, 128));
        searchLabel.setBounds(200, 20, 60, 25);
        filterPanel.add(searchLabel);

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setBounds(260, 20, 200, 25);
        searchField.setBorder(new LineBorder(new Color(200, 200, 200)));
        filterPanel.add(searchField);

        refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshButton.setBackground(accentColor);
        refreshButton.setForeground(new Color(8, 78, 128));
        refreshButton.setBounds(470, 20, 90, 25);
        refreshButton.setBorder(null);
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> {
            loadUserData();
            loadStats();
            applyFilter();
        });
        filterPanel.add(refreshButton);

        // Add User Buttons
        addTraderButton = new JButton("+ Add Trader");
        addTraderButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addTraderButton.setBackground(new Color(8, 78, 128));
        addTraderButton.setForeground(Color.WHITE);
        addTraderButton.setBounds(570, 20, 110, 25);
        addTraderButton.setBorder(null);
        addTraderButton.setFocusPainted(false);
        addTraderButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addTraderButton.addActionListener(e -> showAddTraderDialog());
        filterPanel.add(addTraderButton);

        addAdminButton = new JButton("+ Add Admin");
        addAdminButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addAdminButton.setBackground(accentColor);
        addAdminButton.setForeground(new Color(8, 78, 128));
        addAdminButton.setBounds(690, 20, 110, 25);
        addAdminButton.setBorder(null);
        addAdminButton.setFocusPainted(false);
        addAdminButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addAdminButton.addActionListener(e -> showAddAdminDialog());
        filterPanel.add(addAdminButton);

        // Table with Scroll Pane
        setupTable();
        tableScrollPane = new JScrollPane(usersTable);
        tableScrollPane.setBounds(20, 175, 840, 280);
        tableScrollPane.setBorder(new LineBorder(accentColor, 1));
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        contentPanel.add(tableScrollPane);

        // Action Panel
        actionPanel = new JPanel();
        actionPanel.setLayout(null);
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(new LineBorder(accentColor, 1));
        actionPanel.setBounds(20, 465, 840, 70);
        contentPanel.add(actionPanel);

        JLabel actionLabel = new JLabel("Actions:");
        actionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        actionLabel.setForeground(new Color(8, 78, 128));
        actionLabel.setBounds(15, 22, 70, 25);
        actionPanel.add(actionLabel);

        // Calculate button positions
        int buttonWidth = 100;
        int buttonHeight = 30;
        int startX = 100;
        int yPos = 20;
        int spacing = 5;

        // Deactivate Button
        deactivateButton = new JButton("Deactivate");
        deactivateButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        deactivateButton.setBackground(new Color(204, 0, 0));
        deactivateButton.setForeground(Color.WHITE);
        deactivateButton.setBounds(startX, yPos, buttonWidth, buttonHeight);
        deactivateButton.setBorder(null);
        deactivateButton.setFocusPainted(false);
        deactivateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deactivateButton.setEnabled(false);
        deactivateButton.addActionListener(e -> deactivateUser());
        actionPanel.add(deactivateButton);

        // Activate Button
        activateButton = new JButton("Activate");
        activateButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        activateButton.setBackground(new Color(46, 125, 50));
        activateButton.setForeground(Color.WHITE);
        activateButton.setBounds(startX + buttonWidth + spacing, yPos, buttonWidth, buttonHeight);
        activateButton.setBorder(null);
        activateButton.setFocusPainted(false);
        activateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        activateButton.setEnabled(false);
        activateButton.addActionListener(e -> activateUser());
        actionPanel.add(activateButton);

        // View Details Button
        viewDetailsButton = new JButton("View Details");
        viewDetailsButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        viewDetailsButton.setBackground(new Color(8, 78, 128));
        viewDetailsButton.setForeground(Color.WHITE);
        viewDetailsButton.setBounds(startX + (buttonWidth + spacing) * 2, yPos, buttonWidth, buttonHeight);
        viewDetailsButton.setBorder(null);
        viewDetailsButton.setFocusPainted(false);
        viewDetailsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewDetailsButton.setEnabled(false);
        viewDetailsButton.addActionListener(e -> viewUserDetails());
        actionPanel.add(viewDetailsButton);

        // Update Admin Button
        updateAdminButton = new JButton("Update Admin");
        updateAdminButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        updateAdminButton.setBackground(accentColor);
        updateAdminButton.setForeground(new Color(8, 78, 128));
        updateAdminButton.setBounds(startX + (buttonWidth + spacing) * 3, yPos, buttonWidth, buttonHeight);
        updateAdminButton.setBorder(null);
        updateAdminButton.setFocusPainted(false);
        updateAdminButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateAdminButton.setEnabled(false);
        updateAdminButton.addActionListener(e -> updateAdmin());
        actionPanel.add(updateAdminButton);

        // Delete Admin Button
        deleteAdminButton = new JButton("Delete Admin");
        deleteAdminButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        deleteAdminButton.setBackground(new Color(204, 0, 0));
        deleteAdminButton.setForeground(Color.WHITE);
        deleteAdminButton.setBounds(startX + (buttonWidth + spacing) * 4, yPos, buttonWidth, buttonHeight);
        deleteAdminButton.setBorder(null);
        deleteAdminButton.setFocusPainted(false);
        deleteAdminButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteAdminButton.setEnabled(false);
        deleteAdminButton.addActionListener(e -> deleteAdmin());
        actionPanel.add(deleteAdminButton);

        // Quick Add Trader
        JButton quickAddTrader = new JButton("Quick Add Trader");
        quickAddTrader.setFont(new Font("Segoe UI", Font.BOLD, 11));
        quickAddTrader.setBackground(new Color(8, 78, 128));
        quickAddTrader.setForeground(Color.WHITE);
        quickAddTrader.setBounds(startX + (buttonWidth + spacing) * 5, yPos, 120, buttonHeight);
        quickAddTrader.setBorder(null);
        quickAddTrader.setFocusPainted(false);
        quickAddTrader.setCursor(new Cursor(Cursor.HAND_CURSOR));
        quickAddTrader.addActionListener(e -> showAddTraderDialog());
        actionPanel.add(quickAddTrader);

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
        valueLabel.setBounds(8, 20, width - 16, 20);
        card.add(valueLabel);
        
        card.putClientProperty("valueLabel", valueLabel);
        
        parent.add(card);
        return card;
    }

    private void setupTable() {
        String[] columns = {"ID", "Full Name", "Username", "Email", "Type", "Status", "Created By", "Editable"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        usersTable = new javax.swing.JTable(tableModel);
        usersTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        usersTable.setRowHeight(35);
        usersTable.setShowGrid(true);
        usersTable.setGridColor(new Color(230, 230, 230));
        usersTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        usersTable.getTableHeader().setBackground(sideBarColor);
        usersTable.getTableHeader().setForeground(Color.WHITE);
        usersTable.setSelectionBackground(new Color(255, 235, 204));
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Hide ID column
        usersTable.getColumnModel().getColumn(0).setMinWidth(0);
        usersTable.getColumnModel().getColumn(0).setMaxWidth(0);
        usersTable.getColumnModel().getColumn(0).setWidth(0);

        // Hide Editable column
        usersTable.getColumnModel().getColumn(7).setMinWidth(0);
        usersTable.getColumnModel().getColumn(7).setMaxWidth(0);
        usersTable.getColumnModel().getColumn(7).setWidth(0);

        // Set column widths
        usersTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        usersTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        usersTable.getColumnModel().getColumn(3).setPreferredWidth(180);
        usersTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        usersTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        usersTable.getColumnModel().getColumn(6).setPreferredWidth(150);

        usersTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = usersTable.getSelectedRow();
                if (selectedRow != -1) {
                    int modelRow = usersTable.convertRowIndexToModel(selectedRow);
                    updateSelection(modelRow);
                } else {
                    clearSelection();
                }
            }
        });
    }

    private void loadStats() {
        try {
            // Total Users
            String totalSql = "SELECT COUNT(*) as count FROM tbl_users";
            double total = db.getSingleValue(totalSql);
            totalUsersValue.setText(String.valueOf((int) total));

            // Active Users
            String activeSql = "SELECT COUNT(*) as count FROM tbl_users WHERE user_status = 'active'";
            double active = db.getSingleValue(activeSql);
            activeUsersValue.setText(String.valueOf((int) active));

            // New Users Today
            String newSql = "SELECT COUNT(*) as count FROM tbl_users WHERE date(created_date) = date('now')";
            double newToday = db.getSingleValue(newSql);
            newUsersValue.setText(String.valueOf((int) newToday));

            // Total Admins
            String adminSql = "SELECT COUNT(*) as count FROM tbl_users WHERE user_type = 'admin'";
            double admins = db.getSingleValue(adminSql);
            adminsValue.setText(String.valueOf((int) admins));

        } catch (Exception e) {
            System.out.println("Error loading stats: " + e.getMessage());
        }
    }

    private void updateSelection(int modelRow) {
        selectedUserId = Integer.parseInt(tableModel.getValueAt(modelRow, 0).toString());
        selectedUserType = tableModel.getValueAt(modelRow, 4).toString().toLowerCase();
        selectedUserSource = tableModel.getValueAt(modelRow, 6).toString();
        String status = tableModel.getValueAt(modelRow, 5).toString().toLowerCase();
        boolean isEditable = "yes".equalsIgnoreCase(tableModel.getValueAt(modelRow, 7).toString());

        // Enable/disable buttons based on selection
        viewDetailsButton.setEnabled(true);
        
        if (selectedUserType.equals("trader")) {
            deactivateButton.setEnabled(status.equals("active"));
            activateButton.setEnabled(status.equals("inactive"));
            updateAdminButton.setEnabled(false);
            deleteAdminButton.setEnabled(false);
        } else if (selectedUserType.equals("admin")) {
            if (isEditable && selectedUserId != adminId) {
                updateAdminButton.setEnabled(true);
                deleteAdminButton.setEnabled(true);
            } else {
                updateAdminButton.setEnabled(false);
                deleteAdminButton.setEnabled(false);
            }
            deactivateButton.setEnabled(false);
            activateButton.setEnabled(false);
        }
    }

    private void clearSelection() {
        selectedUserId = -1;
        selectedUserType = "";
        selectedUserSource = "";
        viewDetailsButton.setEnabled(false);
        deactivateButton.setEnabled(false);
        activateButton.setEnabled(false);
        updateAdminButton.setEnabled(false);
        deleteAdminButton.setEnabled(false);
    }

    private void applyFilter() {
        String filter = (String) userTypeFilter.getSelectedItem();
        currentFilter = filter;
        
        if (filter.equals("All Users")) {
            rowSorter.setRowFilter(null);
        } else if (filter.equals("Traders Only")) {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)trader", 4));
        } else if (filter.equals("Admins Only")) {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)admin", 4));
        }
    }

    private void applySearch() {
        String text = searchField.getText().trim();
        if (text.isEmpty()) {
            applyFilter();
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 1, 2, 3));
        }
    }

    private void loadUserData() {
        tableModel.setRowCount(0);

        String sql = "SELECT user_id, user_fullname, user_username, user_email, user_type, "
                + "user_status, COALESCE(created_by, 'System') as created_by "
                + "FROM tbl_users WHERE user_id != ? ORDER BY user_type, user_fullname";

        List<Map<String, Object>> users = db.fetchRecords(sql, adminId);

        for (Map<String, Object> user : users) {
            String userType = (String) user.get("user_type");
            String createdBy = (String) user.get("created_by");
            String status = (String) user.get("user_status");
            boolean isEditable = false;
            
            if (userType.equals("admin")) {
                isEditable = !createdBy.equals("System");
            } else if (userType.equals("trader")) {
                isEditable = !createdBy.equals("System");
            }

            tableModel.addRow(new Object[]{
                user.get("user_id"),
                user.get("user_fullname"),
                user.get("user_username"),
                user.get("user_email"),
                userType.substring(0, 1).toUpperCase() + userType.substring(1),
                status.substring(0, 1).toUpperCase() + status.substring(1),
                createdBy,
                isEditable ? "Yes" : "No"
            });
        }

        rowSorter = new TableRowSorter<>(tableModel);
        usersTable.setRowSorter(rowSorter);
    }

    private void viewUserDetails() {
        if (selectedUserId == -1) return;

        String sql = "SELECT * FROM tbl_users WHERE user_id = ?";
        List<Map<String, Object>> users = db.fetchRecords(sql, selectedUserId);

        if (!users.isEmpty()) {
            Map<String, Object> user = users.get(0);
            
            StringBuilder details = new StringBuilder();
            details.append("═══════════════════════════════════════\n");
            details.append("           USER DETAILS\n");
            details.append("═══════════════════════════════════════\n\n");
            
            details.append("User ID: ").append(user.get("user_id")).append("\n");
            details.append("Full Name: ").append(user.get("user_fullname")).append("\n");
            details.append("Username: ").append(user.get("user_username")).append("\n");
            details.append("Email: ").append(user.get("user_email")).append("\n");
            details.append("User Type: ").append(user.get("user_type")).append("\n");
            details.append("Status: ").append(user.get("user_status")).append("\n");
            details.append("Created By: ").append(user.get("created_by") != null ? user.get("created_by") : "System").append("\n");
            details.append("Created Date: ").append(formatDateTime(user.get("created_date"))).append("\n");

            JOptionPane.showMessageDialog(this, details.toString(), 
                "User Details - ID: " + selectedUserId, 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showAddTraderDialog() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new java.awt.Dimension(400, 280));

        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setBounds(20, 20, 100, 25);
        panel.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(130, 20, 200, 25);
        panel.add(nameField);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(20, 60, 100, 25);
        panel.add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(130, 60, 200, 25);
        panel.add(userField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 100, 100, 25);
        panel.add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(130, 100, 200, 25);
        panel.add(emailField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(20, 140, 100, 25);
        panel.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(130, 140, 200, 25);
        panel.add(passField);

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setBounds(20, 180, 100, 25);
        panel.add(statusLabel);

        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"active", "inactive"});
        statusCombo.setBounds(130, 180, 200, 25);
        panel.add(statusCombo);

        JLabel noteLabel = new JLabel("Note: Trader will be created by admin");
        noteLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        noteLabel.setForeground(new Color(102, 102, 102));
        noteLabel.setBounds(20, 220, 300, 25);
        panel.add(noteLabel);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Trader", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String fullName = nameField.getText().trim();
            String username = userField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passField.getPassword());
            String status = (String) statusCombo.getSelectedItem();

            if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!email.contains("@") || !email.contains(".")) {
                JOptionPane.showMessageDialog(this, "Invalid email format!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (password.length() < 6) {
                JOptionPane.showMessageDialog(this, "Password must be at least 6 characters!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String checkUserSql = "SELECT COUNT(*) as count FROM tbl_users WHERE user_username = ?";
            if (db.getSingleValue(checkUserSql, username) > 0) {
                JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String checkEmailSql = "SELECT COUNT(*) as count FROM tbl_users WHERE user_email = ?";
            if (db.getSingleValue(checkEmailSql, email) > 0) {
                JOptionPane.showMessageDialog(this, "Email already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String hashedPassword = db.hashPassword(password);
            String createdBy = adminName + " (ID: " + adminId + ")";

            String insertSql = "INSERT INTO tbl_users (user_fullname, user_username, user_email, user_pass, "
                    + "user_type, user_status, created_by, created_date) VALUES (?, ?, ?, ?, ?, ?, ?, datetime('now'))";

            db.addRecord(insertSql, fullName, username, email, hashedPassword, "trader", status, createdBy);

            JOptionPane.showMessageDialog(this, "Trader added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadUserData();
            loadStats();
            logActivity("Added trader: " + username);
        }
    }

    private void showAddAdminDialog() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new java.awt.Dimension(400, 280));

        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setBounds(20, 20, 100, 25);
        panel.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(130, 20, 200, 25);
        panel.add(nameField);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(20, 60, 100, 25);
        panel.add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(130, 60, 200, 25);
        panel.add(userField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 100, 100, 25);
        panel.add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(130, 100, 200, 25);
        panel.add(emailField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(20, 140, 100, 25);
        panel.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(130, 140, 200, 25);
        panel.add(passField);

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setBounds(20, 180, 100, 25);
        panel.add(statusLabel);

        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"active", "inactive"});
        statusCombo.setBounds(130, 180, 200, 25);
        panel.add(statusCombo);

        JLabel noteLabel = new JLabel("Note: Admin will be created by " + adminName);
        noteLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        noteLabel.setForeground(new Color(102, 102, 102));
        noteLabel.setBounds(20, 220, 300, 25);
        panel.add(noteLabel);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Admin", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String fullName = nameField.getText().trim();
            String username = userField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passField.getPassword());
            String status = (String) statusCombo.getSelectedItem();

            if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!email.contains("@") || !email.contains(".")) {
                JOptionPane.showMessageDialog(this, "Invalid email format!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (password.length() < 6) {
                JOptionPane.showMessageDialog(this, "Password must be at least 6 characters!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String checkUserSql = "SELECT COUNT(*) as count FROM tbl_users WHERE user_username = ?";
            if (db.getSingleValue(checkUserSql, username) > 0) {
                JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String checkEmailSql = "SELECT COUNT(*) as count FROM tbl_users WHERE user_email = ?";
            if (db.getSingleValue(checkEmailSql, email) > 0) {
                JOptionPane.showMessageDialog(this, "Email already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String hashedPassword = db.hashPassword(password);
            String createdBy = adminName + " (ID: " + adminId + ")";

            String insertSql = "INSERT INTO tbl_users (user_fullname, user_username, user_email, user_pass, "
                    + "user_type, user_status, created_by, created_date) VALUES (?, ?, ?, ?, ?, ?, ?, datetime('now'))";

            db.addRecord(insertSql, fullName, username, email, hashedPassword, "admin", status, createdBy);

            JOptionPane.showMessageDialog(this, "Admin added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadUserData();
            loadStats();
            logActivity("Added admin: " + username);
        }
    }

    private void deactivateUser() {
        if (selectedUserId == -1) return;

        int confirm = JOptionPane.showConfirmDialog(this,
            "Deactivate this user?\n\nUser: " + getSelectedUsername(),
            "Confirm Deactivation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_users SET user_status = 'inactive' WHERE user_id = ?";
            db.updateRecord(sql, selectedUserId);

            JOptionPane.showMessageDialog(this, "User deactivated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadUserData();
            loadStats();
            logActivity("Deactivated user ID: " + selectedUserId);
        }
    }

    private void activateUser() {
        if (selectedUserId == -1) return;

        int confirm = JOptionPane.showConfirmDialog(this,
            "Activate this user?\n\nUser: " + getSelectedUsername(),
            "Confirm Activation",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_users SET user_status = 'active' WHERE user_id = ?";
            db.updateRecord(sql, selectedUserId);

            JOptionPane.showMessageDialog(this, "User activated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadUserData();
            loadStats();
            logActivity("Activated user ID: " + selectedUserId);
        }
    }

    private void updateAdmin() {
        if (selectedUserId == -1 || !selectedUserType.equals("admin")) return;

        String sql = "SELECT * FROM tbl_users WHERE user_id = ?";
        List<Map<String, Object>> users = db.fetchRecords(sql, selectedUserId);

        if (users.isEmpty()) return;

        Map<String, Object> user = users.get(0);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new java.awt.Dimension(400, 280));

        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setBounds(20, 20, 100, 25);
        panel.add(nameLabel);

        JTextField nameField = new JTextField((String) user.get("user_fullname"));
        nameField.setBounds(130, 20, 200, 25);
        panel.add(nameField);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(20, 60, 100, 25);
        panel.add(userLabel);

        JTextField userField = new JTextField((String) user.get("user_username"));
        userField.setBounds(130, 60, 200, 25);
        panel.add(userField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 100, 100, 25);
        panel.add(emailLabel);

        JTextField emailField = new JTextField((String) user.get("user_email"));
        emailField.setBounds(130, 100, 200, 25);
        panel.add(emailField);

        JLabel passLabel = new JLabel("New Password:");
        passLabel.setBounds(20, 140, 100, 25);
        panel.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(130, 140, 200, 25);
        panel.add(passField);

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setBounds(20, 180, 100, 25);
        panel.add(statusLabel);

        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"active", "inactive"});
        statusCombo.setSelectedItem(user.get("user_status"));
        statusCombo.setBounds(130, 180, 200, 25);
        panel.add(statusCombo);

        JLabel noteLabel = new JLabel("Leave password blank to keep current");
        noteLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        noteLabel.setForeground(new Color(102, 102, 102));
        noteLabel.setBounds(20, 220, 300, 25);
        panel.add(noteLabel);

        int result = JOptionPane.showConfirmDialog(this, panel, "Update Admin", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String fullName = nameField.getText().trim();
            String username = userField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passField.getPassword());
            String status = (String) statusCombo.getSelectedItem();

            if (fullName.isEmpty() || username.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name, username, and email are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!email.contains("@") || !email.contains(".")) {
                JOptionPane.showMessageDialog(this, "Invalid email format!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String checkUserSql = "SELECT COUNT(*) as count FROM tbl_users WHERE user_username = ? AND user_id != ?";
            if (db.getSingleValue(checkUserSql, username, selectedUserId) > 0) {
                JOptionPane.showMessageDialog(this, "Username already taken by another user!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String checkEmailSql = "SELECT COUNT(*) as count FROM tbl_users WHERE user_email = ? AND user_id != ?";
            if (db.getSingleValue(checkEmailSql, email, selectedUserId) > 0) {
                JOptionPane.showMessageDialog(this, "Email already taken by another user!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            StringBuilder updateSql = new StringBuilder(
                "UPDATE tbl_users SET user_fullname = ?, user_username = ?, user_email = ?, user_status = ?");
            
            if (!password.isEmpty()) {
                updateSql.append(", user_pass = ?");
            }
            updateSql.append(" WHERE user_id = ?");

            try {
                if (!password.isEmpty()) {
                    String hashedPassword = db.hashPassword(password);
                    db.updateRecord(updateSql.toString(), fullName, username, email, status, hashedPassword, selectedUserId);
                } else {
                    db.updateRecord(updateSql.toString(), fullName, username, email, status, selectedUserId);
                }

                JOptionPane.showMessageDialog(this, "Admin updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadUserData();
                loadStats();
                logActivity("Updated admin ID: " + selectedUserId);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error updating admin: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteAdmin() {
        if (selectedUserId == -1 || !selectedUserType.equals("admin") || selectedUserId == adminId) return;

        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete this admin?\n\nAdmin: " + getSelectedUsername() + "\n\nThis action cannot be undone!",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM tbl_users WHERE user_id = ? AND user_type = 'admin'";
            db.deleteRecord(sql, selectedUserId);

            JOptionPane.showMessageDialog(this, "Admin deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadUserData();
            loadStats();
            logActivity("Deleted admin ID: " + selectedUserId);
        }
    }

    private String getSelectedUsername() {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow != -1) {
            int modelRow = usersTable.convertRowIndexToModel(selectedRow);
            return tableModel.getValueAt(modelRow, 2).toString();
        }
        return "";
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