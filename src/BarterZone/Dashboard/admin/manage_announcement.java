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
import javax.swing.JCheckBox;

public class manage_announcement extends javax.swing.JFrame {

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
    
    // Announcement form panel
    private JPanel formPanel;
    private JTextField titleField;
    private JTextArea messageArea;
    private JScrollPane messageScroll;
    private JComboBox<String> typeCombo;
    private JCheckBox activeCheck;
    private JButton createButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    
    // Announcements table
    private JScrollPane tableScrollPane;
    private javax.swing.JTable announcementsTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    
    // Search panel
    private JPanel searchPanel;
    private JTextField searchField;
    private JButton refreshButton;
    
    // Selected announcement data
    private int selectedAnnouncementId = -1;
    
    // Colors - Admin theme (dark blue/gold)
    private Color sideBarColor = new Color(8, 78, 128);
    private Color hoverColor = new Color(20, 100, 150);
    private Color activeColor = new Color(0, 60, 100);
    private Color accentColor = new Color(255, 215, 0);
    private Color badgeColor = new Color(204, 0, 0);
    private Color headerGradientStart = new Color(8, 78, 128);
    private Color headerGradientEnd = new Color(0, 45, 80);
    private Color formBgColor = new Color(250, 250, 250);
    
    private JPanel activePanel = null;

    public manage_announcement(int adminId, String adminName) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.session = user_session.getInstance();
        this.db = new config();
        
        initComponents();
        setupSidePanel();
        setupHeader();
        setupContentPanel();
        loadAnnouncements();
        updateBadges();
        
        setTitle("Manage Announcements - " + adminName);
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

        // Set active panel to Manage Announcement
        setActivePanel(manageAnnouncementPanel);
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
        headerTitle = new JLabel("Manage Announcements");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerTitle.setForeground(Color.WHITE);
        headerTitle.setBounds(30, 15, 400, 40);
        headerPanel.add(headerTitle);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        currentDateLabel = new JLabel(sdf.format(new Date()));
        currentDateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        currentDateLabel.setForeground(Color.WHITE);
        currentDateLabel.setBounds(600, 25, 250, 30);
        headerPanel.add(currentDateLabel);
    }

    private void setupContentPanel() {
        // Form Panel
        formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBackground(formBgColor);
        formPanel.setBorder(new LineBorder(accentColor, 1));
        formPanel.setBounds(20, 15, 400, 280);
        contentPanel.add(formPanel);

        JLabel formTitle = new JLabel("Create New Announcement");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formTitle.setForeground(new Color(8, 78, 128));
        formTitle.setBounds(15, 15, 300, 25);
        formPanel.add(formTitle);

        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(new Color(8, 78, 128));
        titleLabel.setBounds(15, 55, 80, 25);
        formPanel.add(titleLabel);

        titleField = new JTextField();
        titleField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleField.setBounds(100, 55, 280, 25);
        titleField.setBorder(new LineBorder(new Color(200, 200, 200)));
        formPanel.add(titleField);

        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        typeLabel.setForeground(new Color(8, 78, 128));
        typeLabel.setBounds(15, 95, 80, 25);
        formPanel.add(typeLabel);

        String[] types = {"General Announcement", "New Feature", "Scammer Alert", "Maintenance", "Policy Update", "Warning"};
        typeCombo = new JComboBox<>(types);
        typeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        typeCombo.setBounds(100, 95, 280, 25);
        typeCombo.setBackground(Color.WHITE);
        typeCombo.setBorder(new LineBorder(new Color(200, 200, 200)));
        formPanel.add(typeCombo);

        JLabel messageLabel = new JLabel("Message:");
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        messageLabel.setForeground(new Color(8, 78, 128));
        messageLabel.setBounds(15, 135, 80, 25);
        formPanel.add(messageLabel);

        messageArea = new JTextArea();
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageScroll = new JScrollPane(messageArea);
        messageScroll.setBounds(15, 165, 365, 60);
        messageScroll.setBorder(new LineBorder(new Color(200, 200, 200)));
        formPanel.add(messageScroll);

        activeCheck = new JCheckBox("Active (Visible to users)");
        activeCheck.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        activeCheck.setBackground(formBgColor);
        activeCheck.setForeground(new Color(8, 78, 128));
        activeCheck.setBounds(15, 235, 200, 25);
        activeCheck.setSelected(true);
        formPanel.add(activeCheck);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBackground(formBgColor);
        buttonPanel.setBounds(430, 15, 430, 60);
        contentPanel.add(buttonPanel);

        createButton = new JButton("CREATE");
        createButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        createButton.setBackground(new Color(8, 78, 128));
        createButton.setForeground(Color.WHITE);
        createButton.setBounds(20, 15, 100, 30);
        createButton.setBorder(null);
        createButton.setFocusPainted(false);
        createButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createButton.addActionListener(e -> createAnnouncement());
        buttonPanel.add(createButton);

        updateButton = new JButton("UPDATE");
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        updateButton.setBackground(accentColor);
        updateButton.setForeground(new Color(8, 78, 128));
        updateButton.setBounds(130, 15, 100, 30);
        updateButton.setBorder(null);
        updateButton.setFocusPainted(false);
        updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateButton.setEnabled(false);
        updateButton.addActionListener(e -> updateAnnouncement());
        buttonPanel.add(updateButton);

        deleteButton = new JButton("DELETE");
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        deleteButton.setBackground(new Color(204, 0, 0));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setBounds(240, 15, 100, 30);
        deleteButton.setBorder(null);
        deleteButton.setFocusPainted(false);
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(e -> deleteAnnouncement());
        buttonPanel.add(deleteButton);

        clearButton = new JButton("CLEAR");
        clearButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        clearButton.setBackground(new Color(102, 102, 102));
        clearButton.setForeground(Color.WHITE);
        clearButton.setBounds(350, 15, 80, 30);
        clearButton.setBorder(null);
        clearButton.setFocusPainted(false);
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearButton.addActionListener(e -> clearForm());
        buttonPanel.add(clearButton);

        // Search Panel
        searchPanel = new JPanel();
        searchPanel.setLayout(null);
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(new LineBorder(accentColor, 1));
        searchPanel.setBounds(20, 305, 840, 50);
        contentPanel.add(searchPanel);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchLabel.setForeground(new Color(8, 78, 128));
        searchLabel.setBounds(15, 15, 60, 25);
        searchPanel.add(searchLabel);

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setBounds(80, 15, 200, 25);
        searchField.setBorder(new LineBorder(new Color(200, 200, 200)));
        searchPanel.add(searchField);

        refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshButton.setBackground(accentColor);
        refreshButton.setForeground(new Color(8, 78, 128));
        refreshButton.setBounds(300, 15, 90, 25);
        refreshButton.setBorder(null);
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> {
            loadAnnouncements();
            clearForm();
        });
        searchPanel.add(refreshButton);

        JLabel countLabel = new JLabel("Total Announcements: " + getAnnouncementCount());
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        countLabel.setForeground(new Color(8, 78, 128));
        countLabel.setBounds(500, 15, 200, 25);
        searchPanel.add(countLabel);

        // Announcements Table
        setupTable();
        tableScrollPane = new JScrollPane(announcementsTable);
        tableScrollPane.setBounds(20, 365, 840, 170);
        tableScrollPane.setBorder(new LineBorder(accentColor, 1));
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        contentPanel.add(tableScrollPane);

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
        String[] columns = {"ID", "Date", "Type", "Title", "Message", "Status", "Posted By"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        announcementsTable = new javax.swing.JTable(tableModel);
        announcementsTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        announcementsTable.setRowHeight(35);
        announcementsTable.setShowGrid(true);
        announcementsTable.setGridColor(new Color(230, 230, 230));
        announcementsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        announcementsTable.getTableHeader().setBackground(sideBarColor);
        announcementsTable.getTableHeader().setForeground(Color.WHITE);
        announcementsTable.setSelectionBackground(new Color(255, 235, 204));
        announcementsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Set column widths
        announcementsTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        announcementsTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        announcementsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        announcementsTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        announcementsTable.getColumnModel().getColumn(4).setPreferredWidth(300);
        announcementsTable.getColumnModel().getColumn(5).setPreferredWidth(60);
        announcementsTable.getColumnModel().getColumn(6).setPreferredWidth(100);

        announcementsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = announcementsTable.getSelectedRow();
                if (selectedRow != -1) {
                    int modelRow = announcementsTable.convertRowIndexToModel(selectedRow);
                    loadSelectedAnnouncement(modelRow);
                }
            }
        });
    }

    private void loadAnnouncements() {
        tableModel.setRowCount(0);

        String sql = "SELECT a.announcement_id, a.announcement_date, a.title, a.message, "
                + "a.is_active, a.type, u.user_fullname as admin_name "
                + "FROM tbl_announcement a "
                + "LEFT JOIN tbl_users u ON a.admin_id = u.user_id "
                + "ORDER BY a.announcement_date DESC";

        List<Map<String, Object>> announcements = db.fetchRecords(sql);

        for (Map<String, Object> ann : announcements) {
            Object isActive = ann.get("is_active");
            String status = "Inactive";
            if (isActive instanceof Boolean) {
                status = (Boolean) isActive ? "Active" : "Inactive";
            } else if (isActive instanceof Integer) {
                status = ((Integer) isActive == 1) ? "Active" : "Inactive";
            }

            tableModel.addRow(new Object[]{
                ann.get("announcement_id"),
                formatDate(ann.get("announcement_date")),
                ann.get("type") != null ? ann.get("type") : "General",
                ann.get("title"),
                ann.get("message"),
                status,
                ann.get("admin_name") != null ? ann.get("admin_name") : adminName
            });
        }

        rowSorter = new TableRowSorter<>(tableModel);
        announcementsTable.setRowSorter(rowSorter);
    }

    private void loadSelectedAnnouncement(int modelRow) {
        selectedAnnouncementId = Integer.parseInt(tableModel.getValueAt(modelRow, 0).toString());
        String title = tableModel.getValueAt(modelRow, 3).toString();
        String message = tableModel.getValueAt(modelRow, 4).toString();
        String type = tableModel.getValueAt(modelRow, 2).toString();
        String status = tableModel.getValueAt(modelRow, 5).toString();

        titleField.setText(title);
        messageArea.setText(message);
        typeCombo.setSelectedItem(type);
        activeCheck.setSelected(status.equals("Active"));

        createButton.setEnabled(false);
        updateButton.setEnabled(true);
        deleteButton.setEnabled(true);
    }

    private void clearForm() {
        titleField.setText("");
        messageArea.setText("");
        typeCombo.setSelectedIndex(0);
        activeCheck.setSelected(true);
        selectedAnnouncementId = -1;
        announcementsTable.clearSelection();

        createButton.setEnabled(true);
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private void createAnnouncement() {
        String title = titleField.getText().trim();
        String message = messageArea.getText().trim();
        String type = (String) typeCombo.getSelectedItem();
        boolean isActive = activeCheck.isSelected();

        if (title.isEmpty() || message.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter both title and message.",
                "Incomplete Information",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "INSERT INTO tbl_announcement (admin_id, title, message, type, is_active, announcement_date) "
                + "VALUES (?, ?, ?, ?, ?, datetime('now'))";

        db.addRecord(sql, adminId, title, message, type, isActive ? 1 : 0);

        JOptionPane.showMessageDialog(this,
            "Announcement created successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);

        logActivity("Created announcement: " + title);
        loadAnnouncements();
        clearForm();
    }

    private void updateAnnouncement() {
        if (selectedAnnouncementId == -1) return;

        String title = titleField.getText().trim();
        String message = messageArea.getText().trim();
        String type = (String) typeCombo.getSelectedItem();
        boolean isActive = activeCheck.isSelected();

        if (title.isEmpty() || message.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter both title and message.",
                "Incomplete Information",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Update this announcement?\n\n" + title,
            "Confirm Update",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_announcement SET title = ?, message = ?, type = ?, is_active = ? "
                    + "WHERE announcement_id = ?";

            db.updateRecord(sql, title, message, type, isActive ? 1 : 0, selectedAnnouncementId);

            JOptionPane.showMessageDialog(this,
                "Announcement updated successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

            logActivity("Updated announcement ID: " + selectedAnnouncementId);
            loadAnnouncements();
            clearForm();
        }
    }

    private void deleteAnnouncement() {
        if (selectedAnnouncementId == -1) return;

        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete this announcement?\n\nTitle: " + titleField.getText() + "\n\nThis action cannot be undone!",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM tbl_announcement WHERE announcement_id = ?";
            db.deleteRecord(sql, selectedAnnouncementId);

            JOptionPane.showMessageDialog(this,
                "Announcement deleted successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

            logActivity("Deleted announcement ID: " + selectedAnnouncementId);
            loadAnnouncements();
            clearForm();
        }
    }

    private void applySearch() {
        String text = searchField.getText().trim();
        if (text.isEmpty()) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 2, 3, 4));
        }
    }

    private int getAnnouncementCount() {
        try {
            String sql = "SELECT COUNT(*) as count FROM tbl_announcement";
            return (int) db.getSingleValue(sql);
        } catch (Exception e) {
            return 0;
        }
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