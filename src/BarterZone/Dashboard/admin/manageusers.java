package BarterZone.Dashboard.admin;

import BarterZone.resources.IconManager;
import database.config.config;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class manageusers extends javax.swing.JFrame {

    private int adminId;
    private String adminName;
    private config db;
    private IconManager iconManager;
    
    // UI Components
    private javax.swing.JTextField searchField;
    private javax.swing.JComboBox<String> userTypeFilter;
    private javax.swing.JTabbedPane tabbedPane;
    
    // Traders Table
    private DefaultTableModel tradersTableModel;
    private javax.swing.JTable tradersTable;
    private JScrollPane tradersScrollPane;
    private TableRowSorter<DefaultTableModel> tradersRowSorter;
    
    // Admins Table
    private DefaultTableModel adminsTableModel;
    private javax.swing.JTable adminsTable;
    private JScrollPane adminsScrollPane;
    private TableRowSorter<DefaultTableModel> adminsRowSorter;
    
    // Action Buttons
    private javax.swing.JButton addAdminButton;
    private javax.swing.JButton deactivateUserButton;
    private javax.swing.JButton activateUserButton;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton viewDetailsButton;
    
    // Status Labels
    private javax.swing.JLabel totalTradersLabel;
    private javax.swing.JLabel totalAdminsLabel;
    private javax.swing.JLabel activeTradersLabel;
    private javax.swing.JLabel activeAdminsLabel;
    private javax.swing.JLabel lastUpdatedLabel;
    
    // Selected user data
    private int selectedUserId = -1;
    private String selectedUserType = "";
    private String selectedUserStatus = "";

    private Color hoverColor = new Color(70, 210, 235);
    private Color defaultColor = new Color(12, 192, 223);
    private Color activeColor = new Color(0, 150, 180);

    public manageusers(int adminId, String adminName) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.db = new config();
        this.iconManager = IconManager.getInstance();
        initComponents();
        
        // Load icons for header
        loadIcons();
        
        setupCustomComponents();
        loadAllData();
        setupLiveSearch();
        
        // Set title and properties
        setTitle("Manage Users - " + adminName);
        setSize(800, 500);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void loadIcons() {
        // You can add icons here if needed
    }

    private void setupCustomComponents() {
        // Set current date
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        lastUpdatedLabel = new javax.swing.JLabel("Last updated: " + sdf.format(new Date()));
        lastUpdatedLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lastUpdatedLabel.setForeground(new Color(102, 102, 102));
        
        // Clear jPanel2 and set up new content
        jPanel2.removeAll();
        jPanel2.setLayout(null);

        // Create main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBounds(0, 0, 620, 450);

        // Search and Filter Panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(null);
        searchPanel.setBackground(new Color(245, 245, 245));
        searchPanel.setBorder(new LineBorder(new Color(12, 192, 223), 2));
        searchPanel.setBounds(10, 10, 600, 70);

        javax.swing.JLabel searchLabel = new javax.swing.JLabel("🔍 Search Users:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchLabel.setForeground(new Color(0, 102, 102));
        searchLabel.setBounds(15, 10, 130, 25);
        searchPanel.add(searchLabel);

        searchField = new javax.swing.JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBounds(15, 35, 250, 30);
        searchField.setBorder(new LineBorder(new Color(12, 192, 223)));
        searchPanel.add(searchField);

        javax.swing.JLabel filterLabel = new javax.swing.JLabel("Filter by:");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        filterLabel.setBounds(280, 40, 60, 20);
        searchPanel.add(filterLabel);

        String[] filterTypes = {"All Users", "Active Only", "Inactive Only"};
        userTypeFilter = new javax.swing.JComboBox<>(filterTypes);
        userTypeFilter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userTypeFilter.setBounds(340, 38, 120, 25);
        userTypeFilter.setBackground(Color.WHITE);
        userTypeFilter.setBorder(new LineBorder(new Color(12, 192, 223)));
        userTypeFilter.addActionListener(e -> applyFilters());
        searchPanel.add(userTypeFilter);

        refreshButton = new javax.swing.JButton("⟳ Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshButton.setBackground(new Color(12, 192, 223));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBounds(480, 35, 100, 30);
        refreshButton.setBorder(null);
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> {
            loadAllData();
            applyFilters();
        });
        searchPanel.add(refreshButton);

        // Summary Cards Panel
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(null);
        summaryPanel.setBackground(new Color(245, 245, 245));
        summaryPanel.setBorder(new LineBorder(new Color(12, 192, 223), 2));
        summaryPanel.setBounds(10, 90, 600, 70);

        // Total Traders Card
        JPanel totalTradersCard = new JPanel();
        totalTradersCard.setLayout(null);
        totalTradersCard.setBackground(new Color(0, 102, 102));
        totalTradersCard.setBounds(10, 10, 140, 50);
        totalTradersCard.setBorder(new LineBorder(Color.WHITE, 2));
        
        javax.swing.JLabel totalTradersTitle = new javax.swing.JLabel("TRADERS");
        totalTradersTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        totalTradersTitle.setForeground(Color.WHITE);
        totalTradersTitle.setBounds(10, 5, 80, 15);
        totalTradersCard.add(totalTradersTitle);
        
        totalTradersLabel = new javax.swing.JLabel("0");
        totalTradersLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalTradersLabel.setForeground(Color.WHITE);
        totalTradersLabel.setBounds(100, 15, 40, 30);
        totalTradersCard.add(totalTradersLabel);
        summaryPanel.add(totalTradersCard);

        // Active Traders Card
        JPanel activeTradersCard = new JPanel();
        activeTradersCard.setLayout(null);
        activeTradersCard.setBackground(new Color(46, 125, 50));
        activeTradersCard.setBounds(160, 10, 90, 50);
        activeTradersCard.setBorder(new LineBorder(Color.WHITE, 2));
        
        javax.swing.JLabel activeTradersTitle = new javax.swing.JLabel("ACTIVE");
        activeTradersTitle.setFont(new Font("Segoe UI", Font.BOLD, 10));
        activeTradersTitle.setForeground(Color.WHITE);
        activeTradersTitle.setBounds(10, 5, 50, 15);
        activeTradersCard.add(activeTradersTitle);
        
        activeTradersLabel = new javax.swing.JLabel("0");
        activeTradersLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        activeTradersLabel.setForeground(Color.WHITE);
        activeTradersLabel.setBounds(60, 15, 30, 25);
        activeTradersCard.add(activeTradersLabel);
        summaryPanel.add(activeTradersCard);

        // Total Admins Card
        JPanel totalAdminsCard = new JPanel();
        totalAdminsCard.setLayout(null);
        totalAdminsCard.setBackground(new Color(12, 192, 223));
        totalAdminsCard.setBounds(270, 10, 140, 50);
        totalAdminsCard.setBorder(new LineBorder(Color.WHITE, 2));
        
        javax.swing.JLabel totalAdminsTitle = new javax.swing.JLabel("ADMINS");
        totalAdminsTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        totalAdminsTitle.setForeground(Color.WHITE);
        totalAdminsTitle.setBounds(10, 5, 80, 15);
        totalAdminsCard.add(totalAdminsTitle);
        
        totalAdminsLabel = new javax.swing.JLabel("0");
        totalAdminsLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalAdminsLabel.setForeground(Color.WHITE);
        totalAdminsLabel.setBounds(100, 15, 40, 30);
        totalAdminsCard.add(totalAdminsLabel);
        summaryPanel.add(totalAdminsCard);

        // Active Admins Card
        JPanel activeAdminsCard = new JPanel();
        activeAdminsCard.setLayout(null);
        activeAdminsCard.setBackground(new Color(46, 125, 50));
        activeAdminsCard.setBounds(420, 10, 90, 50);
        activeAdminsCard.setBorder(new LineBorder(Color.WHITE, 2));
        
        javax.swing.JLabel activeAdminsTitle = new javax.swing.JLabel("ACTIVE");
        activeAdminsTitle.setFont(new Font("Segoe UI", Font.BOLD, 10));
        activeAdminsTitle.setForeground(Color.WHITE);
        activeAdminsTitle.setBounds(10, 5, 50, 15);
        activeAdminsCard.add(activeAdminsTitle);
        
        activeAdminsLabel = new javax.swing.JLabel("0");
        activeAdminsLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        activeAdminsLabel.setForeground(Color.WHITE);
        activeAdminsLabel.setBounds(60, 15, 30, 25);
        activeAdminsCard.add(activeAdminsLabel);
        summaryPanel.add(activeAdminsCard);

        // Last Updated
        lastUpdatedLabel.setBounds(520, 45, 150, 20);
        summaryPanel.add(lastUpdatedLabel);

        // Create Tabbed Pane for Traders and Admins
        tabbedPane = new javax.swing.JTabbedPane();
        tabbedPane.setBounds(10, 170, 600, 220);
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(new Color(245, 245, 245));
        tabbedPane.setForeground(new Color(0, 102, 102));

        // Traders Tab
        JPanel tradersPanel = new JPanel();
        tradersPanel.setLayout(null);
        tradersPanel.setBackground(Color.WHITE);
        
        setupTradersTable();
        tradersScrollPane = new JScrollPane(tradersTable);
        tradersScrollPane.setBounds(10, 10, 565, 160);
        tradersScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        tradersScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        tradersPanel.add(tradersScrollPane);
        
        tabbedPane.addTab("Traders", tradersPanel);

        // Admins Tab
        JPanel adminsPanel = new JPanel();
        adminsPanel.setLayout(null);
        adminsPanel.setBackground(Color.WHITE);
        
        setupAdminsTable();
        adminsScrollPane = new JScrollPane(adminsTable);
        adminsScrollPane.setBounds(10, 10, 565, 160);
        adminsScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        adminsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        adminsPanel.add(adminsScrollPane);
        
        tabbedPane.addTab("Administrators", adminsPanel);

        // Action Buttons Panel
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(null);
        actionPanel.setBackground(new Color(245, 245, 245));
        actionPanel.setBorder(new LineBorder(new Color(12, 192, 223), 2));
        actionPanel.setBounds(10, 400, 600, 40);

        addAdminButton = new javax.swing.JButton("➕ Add New Admin");
        addAdminButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addAdminButton.setBackground(new Color(0, 102, 102));
        addAdminButton.setForeground(Color.WHITE);
        addAdminButton.setBounds(10, 5, 150, 30);
        addAdminButton.setBorder(null);
        addAdminButton.setFocusPainted(false);
        addAdminButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addAdminButton.addActionListener(e -> showAddAdminDialog());
        actionPanel.add(addAdminButton);

        deactivateUserButton = new javax.swing.JButton("🔴 Deactivate User");
        deactivateUserButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        deactivateUserButton.setBackground(new Color(204, 0, 0));
        deactivateUserButton.setForeground(Color.WHITE);
        deactivateUserButton.setBounds(170, 5, 140, 30);
        deactivateUserButton.setBorder(null);
        deactivateUserButton.setFocusPainted(false);
        deactivateUserButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deactivateUserButton.setEnabled(false);
        deactivateUserButton.addActionListener(e -> deactivateUser());
        actionPanel.add(deactivateUserButton);

        activateUserButton = new javax.swing.JButton("🟢 Activate User");
        activateUserButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        activateUserButton.setBackground(new Color(46, 125, 50));
        activateUserButton.setForeground(Color.WHITE);
        activateUserButton.setBounds(320, 5, 130, 30);
        activateUserButton.setBorder(null);
        activateUserButton.setFocusPainted(false);
        activateUserButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        activateUserButton.setEnabled(false);
        activateUserButton.addActionListener(e -> activateUser());
        actionPanel.add(activateUserButton);

        viewDetailsButton = new javax.swing.JButton("👤 View Details");
        viewDetailsButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        viewDetailsButton.setBackground(new Color(12, 192, 223));
        viewDetailsButton.setForeground(Color.WHITE);
        viewDetailsButton.setBounds(460, 5, 120, 30);
        viewDetailsButton.setBorder(null);
        viewDetailsButton.setFocusPainted(false);
        viewDetailsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewDetailsButton.setEnabled(false);
        viewDetailsButton.addActionListener(e -> viewUserDetails());
        actionPanel.add(viewDetailsButton);

        contentPanel.add(searchPanel);
        contentPanel.add(summaryPanel);
        contentPanel.add(tabbedPane);
        contentPanel.add(actionPanel);

        // Add content panel to jPanel2
        jPanel2.add(contentPanel);
        contentPanel.setBounds(0, 0, 620, 450);

        jPanel2.revalidate();
        jPanel2.repaint();
    }

    private void setupTradersTable() {
        String[] columns = {"ID", "Full Name", "Username", "Email", "Status", "Created By"};
        tradersTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tradersTable = new javax.swing.JTable(tradersTableModel);
        tradersTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tradersTable.setRowHeight(25);
        tradersTable.setShowGrid(true);
        tradersTable.setGridColor(new Color(12, 192, 223));
        tradersTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tradersTable.getTableHeader().setBackground(new Color(0, 102, 102));
        tradersTable.getTableHeader().setForeground(Color.WHITE);
        tradersTable.getTableHeader().setBorder(null);
        tradersTable.setSelectionBackground(new Color(184, 239, 255));
        tradersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Hide ID column
        tradersTable.getColumnModel().getColumn(0).setMinWidth(0);
        tradersTable.getColumnModel().getColumn(0).setMaxWidth(0);
        tradersTable.getColumnModel().getColumn(0).setWidth(0);

        // Set column widths
        tradersTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        tradersTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        tradersTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        tradersTable.getColumnModel().getColumn(4).setPreferredWidth(70);
        tradersTable.getColumnModel().getColumn(5).setPreferredWidth(100);

        tradersTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (tradersTable.getSelectedRow() != -1) {
                        // Clear selection from other table
                        adminsTable.clearSelection();
                        
                        int modelRow = tradersTable.convertRowIndexToModel(tradersTable.getSelectedRow());
                        selectedUserId = Integer.parseInt(tradersTableModel.getValueAt(modelRow, 0).toString());
                        selectedUserType = "trader";
                        selectedUserStatus = tradersTableModel.getValueAt(modelRow, 4).toString();
                        
                        updateActionButtons();
                    }
                }
            }
        });
    }

    private void setupAdminsTable() {
        String[] columns = {"ID", "Full Name", "Username", "Email", "Status", "Created By"};
        adminsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        adminsTable = new javax.swing.JTable(adminsTableModel);
        adminsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        adminsTable.setRowHeight(25);
        adminsTable.setShowGrid(true);
        adminsTable.setGridColor(new Color(12, 192, 223));
        adminsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        adminsTable.getTableHeader().setBackground(new Color(12, 192, 223));
        adminsTable.getTableHeader().setForeground(Color.WHITE);
        adminsTable.getTableHeader().setBorder(null);
        adminsTable.setSelectionBackground(new Color(184, 239, 255));
        adminsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Hide ID column
        adminsTable.getColumnModel().getColumn(0).setMinWidth(0);
        adminsTable.getColumnModel().getColumn(0).setMaxWidth(0);
        adminsTable.getColumnModel().getColumn(0).setWidth(0);

        // Set column widths
        adminsTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        adminsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        adminsTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        adminsTable.getColumnModel().getColumn(4).setPreferredWidth(70);
        adminsTable.getColumnModel().getColumn(5).setPreferredWidth(100);

        adminsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (adminsTable.getSelectedRow() != -1) {
                        // Clear selection from other table
                        tradersTable.clearSelection();
                        
                        int modelRow = adminsTable.convertRowIndexToModel(adminsTable.getSelectedRow());
                        selectedUserId = Integer.parseInt(adminsTableModel.getValueAt(modelRow, 0).toString());
                        selectedUserType = "admin";
                        selectedUserStatus = adminsTableModel.getValueAt(modelRow, 4).toString();
                        
                        updateActionButtons();
                    }
                }
            }
        });
    }

    private void setupLiveSearch() {
        tradersRowSorter = new TableRowSorter<>(tradersTableModel);
        tradersTable.setRowSorter(tradersRowSorter);
        
        adminsRowSorter = new TableRowSorter<>(adminsTableModel);
        adminsTable.setRowSorter(adminsRowSorter);

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

    private void applyFilters() {
        String searchText = searchField.getText().trim();
        String filterType = userTypeFilter.getSelectedItem().toString();
        
        // Apply search filter
        if (searchText.isEmpty()) {
            tradersRowSorter.setRowFilter(null);
            adminsRowSorter.setRowFilter(null);
        } else {
            tradersRowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText, 1, 2, 3));
            adminsRowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText, 1, 2, 3));
        }
        
        // Apply status filter
        if (filterType.equals("Active Only")) {
            tradersRowSorter.setRowFilter(RowFilter.regexFilter("(?i)active", 4));
            adminsRowSorter.setRowFilter(RowFilter.regexFilter("(?i)active", 4));
        } else if (filterType.equals("Inactive Only")) {
            tradersRowSorter.setRowFilter(RowFilter.regexFilter("(?i)inactive", 4));
            adminsRowSorter.setRowFilter(RowFilter.regexFilter("(?i)inactive", 4));
        }
    }

    private void loadAllData() {
        loadTraders();
        loadAdmins();
        updateSummaryCards();
        
        // Update last updated timestamp
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss");
        lastUpdatedLabel.setText("Last updated: " + sdf.format(new Date()));
    }

    private void loadTraders() {
        tradersTableModel.setRowCount(0);

        String sql = "SELECT user_id, user_fullname, user_username, user_email, user_status, "
                + "COALESCE(created_by, 'System') as created_by "
                + "FROM tbl_users WHERE user_type = 'trader' ORDER BY user_id DESC";

        List<Map<String, Object>> users = db.fetchRecords(sql);

        for (Map<String, Object> user : users) {
            tradersTableModel.addRow(new Object[]{
                user.get("user_id"),
                user.get("user_fullname"),
                user.get("user_username"),
                user.get("user_email"),
                user.get("user_status") != null ? user.get("user_status").toString() : "active",
                user.get("created_by")
            });
        }
    }

    private void loadAdmins() {
        adminsTableModel.setRowCount(0);

        String sql = "SELECT user_id, user_fullname, user_username, user_email, user_status, "
                + "COALESCE(created_by, 'System') as created_by "
                + "FROM tbl_users WHERE user_type = 'admin' ORDER BY user_id DESC";

        List<Map<String, Object>> users = db.fetchRecords(sql);

        for (Map<String, Object> user : users) {
            adminsTableModel.addRow(new Object[]{
                user.get("user_id"),
                user.get("user_fullname"),
                user.get("user_username"),
                user.get("user_email"),
                user.get("user_status") != null ? user.get("user_status").toString() : "active",
                user.get("created_by")
            });
        }
    }

    private void updateSummaryCards() {
        // Count traders
        String traderSql = "SELECT COUNT(*) as count FROM tbl_users WHERE user_type = 'trader'";
        double totalTraders = db.getSingleValue(traderSql);
        totalTradersLabel.setText(String.valueOf((int) totalTraders));

        String activeTraderSql = "SELECT COUNT(*) as count FROM tbl_users WHERE user_type = 'trader' AND user_status = 'active'";
        double activeTraders = db.getSingleValue(activeTraderSql);
        activeTradersLabel.setText(String.valueOf((int) activeTraders));

        // Count admins
        String adminSql = "SELECT COUNT(*) as count FROM tbl_users WHERE user_type = 'admin'";
        double totalAdmins = db.getSingleValue(adminSql);
        totalAdminsLabel.setText(String.valueOf((int) totalAdmins));

        String activeAdminSql = "SELECT COUNT(*) as count FROM tbl_users WHERE user_type = 'admin' AND user_status = 'active'";
        double activeAdmins = db.getSingleValue(activeAdminSql);
        activeAdminsLabel.setText(String.valueOf((int) activeAdmins));
    }

    private void updateActionButtons() {
        boolean hasSelection = selectedUserId != -1;
        deactivateUserButton.setEnabled(hasSelection && selectedUserStatus.equalsIgnoreCase("active"));
        activateUserButton.setEnabled(hasSelection && selectedUserStatus.equalsIgnoreCase("inactive"));
        viewDetailsButton.setEnabled(hasSelection);
    }

    private void showAddAdminDialog() {
        // Create dialog
        javax.swing.JDialog adminDialog = new javax.swing.JDialog(this, "Add New Administrator", true);
        adminDialog.setSize(450, 400);
        adminDialog.setLayout(null);
        adminDialog.setLocationRelativeTo(this);
        adminDialog.getContentPane().setBackground(Color.WHITE);

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(12, 192, 223));
        titlePanel.setBounds(0, 0, 450, 50);
        titlePanel.setLayout(null);
        
        javax.swing.JLabel titleLabel = new javax.swing.JLabel("➕ ADD NEW ADMINISTRATOR");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(20, 10, 300, 30);
        titlePanel.add(titleLabel);
        
        adminDialog.add(titlePanel);

        // Created by info
        javax.swing.JLabel createdByLabel = new javax.swing.JLabel("Created by: " + adminName + " (Admin ID: " + adminId + ")");
        createdByLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        createdByLabel.setForeground(new Color(0, 102, 102));
        createdByLabel.setBounds(20, 70, 350, 20);
        adminDialog.add(createdByLabel);

        // Form fields
        int y = 110;
        int labelWidth = 100;
        int fieldWidth = 250;
        int fieldX = 130;

        // Full Name
        javax.swing.JLabel lblFullName = new javax.swing.JLabel("Full Name:");
        lblFullName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFullName.setBounds(20, y, labelWidth, 25);
        adminDialog.add(lblFullName);

        javax.swing.JTextField txtFullName = new javax.swing.JTextField();
        txtFullName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtFullName.setBounds(fieldX, y, fieldWidth, 30);
        txtFullName.setBorder(new LineBorder(new Color(200, 200, 200)));
        adminDialog.add(txtFullName);
        y += 45;

        // Username
        javax.swing.JLabel lblUsername = new javax.swing.JLabel("Username:");
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsername.setBounds(20, y, labelWidth, 25);
        adminDialog.add(lblUsername);

        javax.swing.JTextField txtUsername = new javax.swing.JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setBounds(fieldX, y, fieldWidth, 30);
        txtUsername.setBorder(new LineBorder(new Color(200, 200, 200)));
        adminDialog.add(txtUsername);
        y += 45;

        // Email
        javax.swing.JLabel lblEmail = new javax.swing.JLabel("Email:");
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblEmail.setBounds(20, y, labelWidth, 25);
        adminDialog.add(lblEmail);

        javax.swing.JTextField txtEmail = new javax.swing.JTextField();
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtEmail.setBounds(fieldX, y, fieldWidth, 30);
        txtEmail.setBorder(new LineBorder(new Color(200, 200, 200)));
        adminDialog.add(txtEmail);
        y += 45;

        // Password
        javax.swing.JLabel lblPassword = new javax.swing.JLabel("Password:");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPassword.setBounds(20, y, labelWidth, 25);
        adminDialog.add(lblPassword);

        javax.swing.JPasswordField txtPassword = new javax.swing.JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBounds(fieldX, y, fieldWidth, 30);
        txtPassword.setBorder(new LineBorder(new Color(200, 200, 200)));
        adminDialog.add(txtPassword);
        y += 45;

        // Confirm Password
        javax.swing.JLabel lblConfirmPassword = new javax.swing.JLabel("Confirm:");
        lblConfirmPassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblConfirmPassword.setBounds(20, y, labelWidth, 25);
        adminDialog.add(lblConfirmPassword);

        javax.swing.JPasswordField txtConfirmPassword = new javax.swing.JPasswordField();
        txtConfirmPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtConfirmPassword.setBounds(fieldX, y, fieldWidth, 30);
        txtConfirmPassword.setBorder(new LineBorder(new Color(200, 200, 200)));
        adminDialog.add(txtConfirmPassword);
        y += 55;

        // Buttons
        javax.swing.JButton btnCreate = new javax.swing.JButton("CREATE ADMIN");
        btnCreate.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCreate.setBackground(new Color(0, 102, 102));
        btnCreate.setForeground(Color.WHITE);
        btnCreate.setBounds(100, y, 150, 35);
        btnCreate.setBorder(null);
        btnCreate.setFocusPainted(false);
        btnCreate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnCreate.addActionListener(e -> {
            String fullName = txtFullName.getText().trim();
            String username = txtUsername.getText().trim();
            String email = txtEmail.getText().trim();
            String password = new String(txtPassword.getPassword());
            String confirmPassword = new String(txtConfirmPassword.getPassword());

            // Validate inputs
            if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(adminDialog, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(adminDialog, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (password.length() < 6) {
                JOptionPane.showMessageDialog(adminDialog, "Password must be at least 6 characters!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!email.contains("@") || !email.contains(".")) {
                JOptionPane.showMessageDialog(adminDialog, "Please enter a valid email address!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if username already exists
            String checkUsernameSql = "SELECT COUNT(*) as count FROM tbl_users WHERE user_username = ?";
            double usernameCount = db.getSingleValue(checkUsernameSql, username);
            if (usernameCount > 0) {
                JOptionPane.showMessageDialog(adminDialog, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if email already exists
            String checkEmailSql = "SELECT COUNT(*) as count FROM tbl_users WHERE user_email = ?";
            double emailCount = db.getSingleValue(checkEmailSql, email);
            if (emailCount > 0) {
                JOptionPane.showMessageDialog(adminDialog, "Email already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Hash password
            String hashedPassword = db.hashPassword(password);

            // Insert new admin
            String insertSql = "INSERT INTO tbl_users (user_fullname, user_username, user_email, user_pass, user_type, user_status, created_by) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            db.addRecord(insertSql, fullName, username, email, hashedPassword, "admin", "active", adminName + " (ID: " + adminId + ")");

            JOptionPane.showMessageDialog(adminDialog, 
                    "✅ Administrator created successfully!\n\n" +
                    "Created by: " + adminName, 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            
            adminDialog.dispose();
            loadAllData();
        });
        adminDialog.add(btnCreate);

        javax.swing.JButton btnCancel = new javax.swing.JButton("CANCEL");
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setBackground(new Color(204, 0, 0));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setBounds(260, y, 100, 35);
        btnCancel.setBorder(null);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> adminDialog.dispose());
        adminDialog.add(btnCancel);

        adminDialog.setVisible(true);
    }

    private void deactivateUser() {
        if (selectedUserId == -1) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Deactivate this user?\n\n" +
                "User ID: " + selectedUserId + "\n" +
                "Type: " + selectedUserType + "\n\n" +
                "Deactivated users cannot log in or use the system.",
                "Confirm Deactivation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_users SET user_status = 'inactive' WHERE user_id = ?";
            db.updateRecord(sql, selectedUserId);

            JOptionPane.showMessageDialog(this, 
                    "✅ User deactivated successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            
            selectedUserId = -1;
            loadAllData();
            updateActionButtons();
        }
    }

    private void activateUser() {
        if (selectedUserId == -1) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Activate this user?\n\n" +
                "User ID: " + selectedUserId + "\n" +
                "Type: " + selectedUserType,
                "Confirm Activation",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_users SET user_status = 'active' WHERE user_id = ?";
            db.updateRecord(sql, selectedUserId);

            JOptionPane.showMessageDialog(this, 
                    "✅ User activated successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            
            selectedUserId = -1;
            loadAllData();
            updateActionButtons();
        }
    }

    private void viewUserDetails() {
        if (selectedUserId == -1) return;

        String sql = "SELECT * FROM tbl_users WHERE user_id = ?";
        List<Map<String, Object>> users = db.fetchRecords(sql, selectedUserId);

        if (!users.isEmpty()) {
            Map<String, Object> user = users.get(0);
            
            String details = 
                "╔════════════════════════════════╗\n" +
                "║       USER DETAILS             ║\n" +
                "╚════════════════════════════════╝\n\n" +
                
                "📋 User ID: " + user.get("user_id") + "\n" +
                "👤 Full Name: " + user.get("user_fullname") + "\n" +
                "🔤 Username: " + user.get("user_username") + "\n" +
                "📧 Email: " + user.get("user_email") + "\n" +
                "👑 User Type: " + user.get("user_type") + "\n" +
                "⚡ Status: " + user.get("user_status") + "\n" +
                "📅 Created By: " + (user.get("created_by") != null ? user.get("created_by") : "System") + "\n";
            
            if (user.containsKey("created_date")) {
                details += "📆 Created Date: " + user.get("created_date") + "\n";
            }
            
            JOptionPane.showMessageDialog(this, details, 
                    "User Details - ID: " + selectedUserId, 
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 500));
        setPreferredSize(new java.awt.Dimension(800, 500));
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 2));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setText("Manage Users");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, 30));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                        

    // Variables declaration - do not modify                     
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration                   
}