package BarterZone.Dashboard.trader;

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
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class trades extends javax.swing.JFrame {

    private int traderId;
    private String traderName;
    private config db;
    private IconManager iconManager;

    // UI Components
    private javax.swing.JTabbedPane tabbedPane;

    // Available Items Table
    private DefaultTableModel availableTableModel;
    private javax.swing.JTable availableTable;
    private JScrollPane availableScrollPane;

    // Pending Trades Table
    private DefaultTableModel pendingTableModel;
    private javax.swing.JTable pendingTable;
    private JScrollPane pendingScrollPane;

    // Active Trades Table
    private DefaultTableModel activeTableModel;
    private javax.swing.JTable activeTable;
    private JScrollPane activeScrollPane;

    // Completed Trades Table
    private DefaultTableModel completedTableModel;
    private javax.swing.JTable completedTable;
    private JScrollPane completedScrollPane;

    // Action Buttons
    private javax.swing.JButton acceptButton;
    private javax.swing.JButton declineButton;
    private javax.swing.JButton completeButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton messageButton;
    private javax.swing.JButton viewInstructionsButton;

    // Status Labels
    private javax.swing.JLabel pendingCountLabel;
    private javax.swing.JLabel activeCountLabel;
    private javax.swing.JLabel completedCountLabel;

    // Instructions Panel
    private JPanel instructionsPanel;
    private javax.swing.JTextArea instructionsArea;
    private JScrollPane instructionsScrollPane;
    private javax.swing.JLabel selectedTradeInfoLabel;

    private Color hoverColor = new Color(70, 210, 235);
    private Color defaultColor = new Color(12, 192, 223);
    private Color activeColor = new Color(0, 150, 180);
    private JPanel activePanel = null;

    public trades(int traderId, String traderName) {
        this.traderId = traderId;
        this.traderName = traderName;
        this.db = new config();
        this.iconManager = IconManager.getInstance();
        initComponents();

        // Load icons for side panel
        loadAndResizeIcons();

        // Set as active panel
        setActivePanel(paneltrades);

        setupCustomComponents();
        loadAllData();

        // Add hover effects to all side panel items
        setupSidebarHoverEffects();

        // Set title and properties
        setTitle("Trades - " + traderName);
        setSize(800, 500);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void loadAndResizeIcons() {
        setIconSafely(dashboardicon, iconManager.getSideMenuIcon("dashboard"));
        setIconSafely(myitemsicon, iconManager.getSideMenuIcon("myitems"));
        setIconSafely(finditemsicon, iconManager.getSideMenuIcon("finditems"));
        setIconSafely(tradesicon, iconManager.getSideMenuIcon("trade"));
        setIconSafely(messagesicon, iconManager.getSideMenuIcon("messages"));
        setIconSafely(reportsicon, iconManager.getSideMenuIcon("report"));
        setIconSafely(profileicon, iconManager.getSideMenuIcon("profile"));
        setIconSafely(logouticon, iconManager.getSideMenuIcon("logout"));

        setIconSafely(barterzonelogo, iconManager.getLogoIcon());
    }

    private void setIconSafely(javax.swing.JLabel label, ImageIcon icon) {
        if (icon != null) {
            label.setIcon(icon);
            label.setText("");
        }
    }

    private void setupSidebarHoverEffects() {
        applyHoverEffect(paneldashboard, dashboard);
        applyHoverEffect(panelmyitems, myitems);
        applyHoverEffect(panelfinditems, finditems);
        applyHoverEffect(paneltrades, trades);
        applyHoverEffect(panelmessages, messages);
        applyHoverEffect(panelreports, Reports);
        applyHoverEffect(panelprofile, Profile);
        applyHoverEffect(panellogout, logout);
    }

    private void applyHoverEffect(JPanel panel, javax.swing.JLabel label) {
        java.awt.event.MouseAdapter adapter = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setHover(panel);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setDefault(panel);
            }
        };

        panel.addMouseListener(adapter);
        label.addMouseListener(adapter);
    }

    private void setActivePanel(JPanel panel) {
        if (activePanel != null) {
            activePanel.setBackground(defaultColor);
        }
        activePanel = panel;
        activePanel.setBackground(activeColor);
    }

    private void setHover(JPanel panel) {
        if (panel != activePanel) {
            panel.setBackground(hoverColor);
        }
    }

    private void setDefault(JPanel panel) {
        if (panel != activePanel) {
            panel.setBackground(defaultColor);
        }
    }

    private void setupCustomComponents() {
        // Set username and avatar
        username.setText(traderName);
        if (traderName != null && traderName.length() > 0) {
            avatarletter.setText(String.valueOf(traderName.charAt(0)).toUpperCase());
        }

        // Set current date
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        CurrentDate.setText(sdf.format(new Date()));

        // Clear jPanel2 and set up new content
        jPanel2.removeAll();
        jPanel2.setLayout(null);

        // Create main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBounds(0, 0, 620, 450);

        // Summary Cards Panel
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(null);
        summaryPanel.setBackground(new Color(245, 245, 245));
        summaryPanel.setBorder(new LineBorder(new Color(12, 192, 223), 2));
        summaryPanel.setBounds(10, 10, 600, 80);

        // Pending Card
        JPanel pendingCard = new JPanel();
        pendingCard.setLayout(null);
        pendingCard.setBackground(new Color(255, 153, 0));
        pendingCard.setBounds(10, 15, 140, 50);
        pendingCard.setBorder(new LineBorder(Color.WHITE, 2));

        javax.swing.JLabel pendingTitle = new javax.swing.JLabel("PENDING");
        pendingTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pendingTitle.setForeground(Color.WHITE);
        pendingTitle.setBounds(10, 5, 100, 20);
        pendingCard.add(pendingTitle);

        pendingCountLabel = new javax.swing.JLabel("0");
        pendingCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        pendingCountLabel.setForeground(Color.WHITE);
        pendingCountLabel.setBounds(100, 15, 40, 30);
        pendingCard.add(pendingCountLabel);
        summaryPanel.add(pendingCard);

        // Active Card
        JPanel activeCard = new JPanel();
        activeCard.setLayout(null);
        activeCard.setBackground(new Color(0, 102, 102));
        activeCard.setBounds(160, 15, 140, 50);
        activeCard.setBorder(new LineBorder(Color.WHITE, 2));

        javax.swing.JLabel activeTitle = new javax.swing.JLabel("ACTIVE");
        activeTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        activeTitle.setForeground(Color.WHITE);
        activeTitle.setBounds(10, 5, 100, 20);
        activeCard.add(activeTitle);

        activeCountLabel = new javax.swing.JLabel("0");
        activeCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        activeCountLabel.setForeground(Color.WHITE);
        activeCountLabel.setBounds(100, 15, 40, 30);
        activeCard.add(activeCountLabel);
        summaryPanel.add(activeCard);

        // Completed Card
        JPanel completedCard = new JPanel();
        completedCard.setLayout(null);
        completedCard.setBackground(new Color(46, 125, 50));
        completedCard.setBounds(310, 15, 140, 50);
        completedCard.setBorder(new LineBorder(Color.WHITE, 2));

        javax.swing.JLabel completedTitle = new javax.swing.JLabel("COMPLETED");
        completedTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        completedTitle.setForeground(Color.WHITE);
        completedTitle.setBounds(10, 5, 100, 20);
        completedCard.add(completedTitle);

        completedCountLabel = new javax.swing.JLabel("0");
        completedCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        completedCountLabel.setForeground(Color.WHITE);
        completedCountLabel.setBounds(100, 15, 40, 30);
        completedCard.add(completedCountLabel);
        summaryPanel.add(completedCard);

        // My Items Card (Quick link to myitems)
        JPanel myItemsCard = new JPanel();
        myItemsCard.setLayout(null);
        myItemsCard.setBackground(new Color(12, 192, 223));
        myItemsCard.setBounds(460, 15, 130, 50);
        myItemsCard.setBorder(new LineBorder(Color.WHITE, 2));
        myItemsCard.setCursor(new Cursor(Cursor.HAND_CURSOR));
        myItemsCard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openMyItems();
            }
        });

        javax.swing.JLabel myItemsTitle = new javax.swing.JLabel("MY ITEMS");
        myItemsTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        myItemsTitle.setForeground(Color.WHITE);
        myItemsTitle.setBounds(10, 5, 100, 20);
        myItemsCard.add(myItemsTitle);

        javax.swing.JLabel myItemsIcon = new javax.swing.JLabel("→");
        myItemsIcon.setFont(new Font("Segoe UI", Font.BOLD, 20));
        myItemsIcon.setForeground(Color.WHITE);
        myItemsIcon.setBounds(100, 10, 30, 30);
        myItemsCard.add(myItemsIcon);
        summaryPanel.add(myItemsCard);

        // Create Tabbed Pane
        tabbedPane = new javax.swing.JTabbedPane();
        tabbedPane.setBounds(10, 100, 600, 340);
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(new Color(245, 245, 245));
        tabbedPane.setForeground(new Color(0, 102, 102));

        // Available Items Tab - ADJUSTED BUTTON POSITION
        JPanel availablePanel = new JPanel();
        availablePanel.setLayout(null);
        availablePanel.setBackground(Color.WHITE);

        setupAvailableTable();
        availableScrollPane = new JScrollPane(availableTable);
        availableScrollPane.setBounds(10, 10, 565, 270); // Increased height to 270
        availableScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        availableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        availablePanel.add(availableScrollPane);

        javax.swing.JButton requestTradeButton = new javax.swing.JButton("Request Trade");
        requestTradeButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        requestTradeButton.setBackground(new Color(255, 140, 0));
        requestTradeButton.setForeground(Color.WHITE);
        requestTradeButton.setBounds(220, 290, 150, 30); // Moved up from 280 to 290
        requestTradeButton.setBorder(null);
        requestTradeButton.setFocusPainted(false);
        requestTradeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        requestTradeButton.addActionListener(e -> requestTrade());
        availablePanel.add(requestTradeButton);

        tabbedPane.addTab("Available Items", availablePanel);

        // Pending Trades Tab - ADJUSTED BUTTON POSITION
        JPanel pendingPanel = new JPanel();
        pendingPanel.setLayout(null);
        pendingPanel.setBackground(Color.WHITE);

        setupPendingTable();
        pendingScrollPane = new JScrollPane(pendingTable);
        pendingScrollPane.setBounds(10, 10, 565, 210); // Increased height to 210
        pendingScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        pendingScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        pendingPanel.add(pendingScrollPane);

        acceptButton = new javax.swing.JButton("✓ Accept");
        acceptButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        acceptButton.setBackground(new Color(46, 125, 50));
        acceptButton.setForeground(Color.WHITE);
        acceptButton.setBounds(120, 230, 100, 30); // Moved up from 220 to 230
        acceptButton.setBorder(null);
        acceptButton.setFocusPainted(false);
        acceptButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        acceptButton.setEnabled(false);
        acceptButton.addActionListener(e -> acceptTrade());
        pendingPanel.add(acceptButton);

        declineButton = new javax.swing.JButton("✗ Decline");
        declineButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        declineButton.setBackground(new Color(204, 0, 0));
        declineButton.setForeground(Color.WHITE);
        declineButton.setBounds(230, 230, 100, 30); // Moved up from 220 to 230
        declineButton.setBorder(null);
        declineButton.setFocusPainted(false);
        declineButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        declineButton.setEnabled(false);
        declineButton.addActionListener(e -> declineTrade());
        pendingPanel.add(declineButton);

        messageButton = new javax.swing.JButton("💬 Message");
        messageButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        messageButton.setBackground(new Color(0, 102, 102));
        messageButton.setForeground(Color.WHITE);
        messageButton.setBounds(340, 230, 100, 30); // Moved up from 220 to 230
        messageButton.setBorder(null);
        messageButton.setFocusPainted(false);
        messageButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        messageButton.setEnabled(false);
        messageButton.addActionListener(e -> sendMessage());
        pendingPanel.add(messageButton);

        tabbedPane.addTab("Pending Trades", pendingPanel);

        // Active Trades Tab with Instructions Panel - ADJUSTED BUTTON POSITION
        JPanel activeMainPanel = new JPanel();
        activeMainPanel.setLayout(null);
        activeMainPanel.setBackground(Color.WHITE);

        // Active Trades Table
        setupActiveTable();
        activeScrollPane = new JScrollPane(activeTable);
        activeScrollPane.setBounds(10, 10, 350, 210); // Increased height to 210
        activeScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        activeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        activeMainPanel.add(activeScrollPane);

        // Instructions Panel (Manage Trades)
        instructionsPanel = new JPanel();
        instructionsPanel.setLayout(null);
        instructionsPanel.setBackground(new Color(245, 245, 245));
        instructionsPanel.setBorder(new LineBorder(new Color(12, 192, 223), 2));
        instructionsPanel.setBounds(370, 10, 215, 210);

        javax.swing.JLabel instructionsTitle = new javax.swing.JLabel("📋 MANAGE TRADE");
        instructionsTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        instructionsTitle.setForeground(new Color(0, 102, 102));
        instructionsTitle.setBounds(10, 5, 150, 20);
        instructionsPanel.add(instructionsTitle);

        selectedTradeInfoLabel = new javax.swing.JLabel("Select a trade to view instructions");
        selectedTradeInfoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        selectedTradeInfoLabel.setForeground(new Color(102, 102, 102));
        selectedTradeInfoLabel.setBounds(10, 25, 195, 15);
        instructionsPanel.add(selectedTradeInfoLabel);

        instructionsArea = new javax.swing.JTextArea();
        instructionsArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        instructionsArea.setLineWrap(true);
        instructionsArea.setWrapStyleWord(true);
        instructionsArea.setEditable(false);
        instructionsArea.setBackground(new Color(245, 245, 245));
        instructionsArea.setText("Select a trade to see exchange instructions.");

        instructionsScrollPane = new JScrollPane(instructionsArea);
        instructionsScrollPane.setBounds(10, 45, 195, 130); // Increased height to 130
        instructionsScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        instructionsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        instructionsPanel.add(instructionsScrollPane);

        viewInstructionsButton = new javax.swing.JButton("View Full Instructions");
        viewInstructionsButton.setFont(new Font("Segoe UI", Font.BOLD, 10));
        viewInstructionsButton.setBackground(new Color(12, 192, 223));
        viewInstructionsButton.setForeground(Color.WHITE);
        viewInstructionsButton.setBounds(40, 180, 140, 20); // Moved up from 170 to 180
        viewInstructionsButton.setBorder(null);
        viewInstructionsButton.setFocusPainted(false);
        viewInstructionsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewInstructionsButton.setEnabled(false);
        viewInstructionsButton.addActionListener(e -> showFullInstructions());
        instructionsPanel.add(viewInstructionsButton);

        activeMainPanel.add(instructionsPanel);

        // Buttons for Active Trades
        completeButton = new javax.swing.JButton("✓ Mark Complete");
        completeButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        completeButton.setBackground(new Color(46, 125, 50));
        completeButton.setForeground(Color.WHITE);
        completeButton.setBounds(120, 280, 140, 30); // Moved up from 270 to 280
        completeButton.setBorder(null);
        completeButton.setFocusPainted(false);
        completeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        completeButton.setEnabled(false);
        completeButton.addActionListener(e -> completeTrade());
        activeMainPanel.add(completeButton);

        cancelButton = new javax.swing.JButton("✗ Cancel Trade");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cancelButton.setBackground(new Color(204, 0, 0));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBounds(270, 280, 130, 30); // Moved up from 270 to 280
        cancelButton.setBorder(null);
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setEnabled(false);
        cancelButton.addActionListener(e -> cancelTrade());
        activeMainPanel.add(cancelButton);

        tabbedPane.addTab("Active Trades", activeMainPanel);

        // History Tab
        JPanel historyPanel = new JPanel();
        historyPanel.setLayout(null);
        historyPanel.setBackground(Color.WHITE);

        setupCompletedTable();
        completedScrollPane = new JScrollPane(completedTable);
        completedScrollPane.setBounds(10, 10, 565, 300); // Increased height to 300
        completedScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        completedScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        historyPanel.add(completedScrollPane);

        tabbedPane.addTab("History", historyPanel);

        contentPanel.add(summaryPanel);
        contentPanel.add(tabbedPane);

        // Add content panel to jPanel2
        jPanel2.add(contentPanel);
        contentPanel.setBounds(0, 0, 620, 450);

        jPanel2.revalidate();
        jPanel2.repaint();
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
        availableTable.setGridColor(new Color(12, 192, 223));
        availableTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        availableTable.getTableHeader().setBackground(new Color(12, 192, 223));
        availableTable.getTableHeader().setForeground(Color.WHITE);
        availableTable.getTableHeader().setBorder(null);
        availableTable.setSelectionBackground(new Color(184, 239, 255));
        availableTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Hide ID columns
        availableTable.getColumnModel().getColumn(0).setMinWidth(0);
        availableTable.getColumnModel().getColumn(0).setMaxWidth(0);
        availableTable.getColumnModel().getColumn(0).setWidth(0);

        availableTable.getColumnModel().getColumn(5).setMinWidth(0);
        availableTable.getColumnModel().getColumn(5).setMaxWidth(0);
        availableTable.getColumnModel().getColumn(5).setWidth(0);
    }

    private void setupPendingTable() {
        String[] columns = {"ID", "Their Item", "Their Item Owner", "My Item", "Date", "Status", "Trade ID"};
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
        pendingTable.setGridColor(new Color(12, 192, 223));
        pendingTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        pendingTable.getTableHeader().setBackground(new Color(255, 153, 0));
        pendingTable.getTableHeader().setForeground(Color.WHITE);
        pendingTable.getTableHeader().setBorder(null);
        pendingTable.setSelectionBackground(new Color(255, 235, 204));
        pendingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Hide ID columns
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
        String[] columns = {"ID", "Their Item", "Owner", "My Item", "Date Started", "Status", "Trade ID"};
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
        activeTable.setGridColor(new Color(12, 192, 223));
        activeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        activeTable.getTableHeader().setBackground(new Color(0, 102, 102));
        activeTable.getTableHeader().setForeground(Color.WHITE);
        activeTable.getTableHeader().setBorder(null);
        activeTable.setSelectionBackground(new Color(184, 239, 255));
        activeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Hide ID columns
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
                    completeButton.setEnabled(hasSelection);
                    cancelButton.setEnabled(hasSelection);
                    viewInstructionsButton.setEnabled(hasSelection);

                    if (hasSelection) {
                        int modelRow = activeTable.convertRowIndexToModel(activeTable.getSelectedRow());
                        displayTradeInstructions(modelRow);
                    } else {
                        selectedTradeInfoLabel.setText("Select a trade to view instructions");
                        instructionsArea.setText("Select a trade to see exchange instructions.");
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
        completedTable.setGridColor(new Color(12, 192, 223));
        completedTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        completedTable.getTableHeader().setBackground(new Color(46, 125, 50));
        completedTable.getTableHeader().setForeground(Color.WHITE);
        completedTable.getTableHeader().setBorder(null);
        completedTable.setSelectionBackground(new Color(200, 230, 201));
        completedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Hide ID column
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

        // SQL for trades where this trader is the target (incoming requests)
        String sql = "SELECT t.trade_id, "
                + "i_offer.item_Name as their_item, "
                + "u_offer.user_fullname as offer_trader, "
                + "i_target.item_Name as my_item, "
                + "t.trade_DateRequest as date, t.trade_status "
                + "FROM tbl_trade t "
                + "JOIN tbl_items i_offer ON t.offer_item_id = i_offer.items_id "
                + "JOIN tbl_items i_target ON t.target_item_id = i_target.items_id "
                + "JOIN tbl_users u_offer ON t.offer_trader_id = u_offer.user_id "
                + "WHERE t.target_trader_id = ? AND t.trade_status = 'pending' "
                + "ORDER BY t.trade_DateRequest DESC";

        List<Map<String, Object>> trades = db.fetchRecords(sql, traderId);

        for (Map<String, Object> trade : trades) {
            pendingTableModel.addRow(new Object[]{
                trade.get("trade_id"),
                trade.get("their_item"),
                trade.get("offer_trader"),
                trade.get("my_item"),
                formatDate(trade.get("date")),
                "Pending",
                trade.get("trade_id")
            });
        }
    }

    private void loadActiveTrades() {
        activeTableModel.setRowCount(0);

        // SQL for active trades (both as offerer and target)
        String sql = "SELECT t.trade_id, "
                + "CASE WHEN t.offer_trader_id = ? THEN i_target.item_Name ELSE i_offer.item_Name END as their_item, "
                + "CASE WHEN t.offer_trader_id = ? THEN u_target.user_fullname ELSE u_offer.user_fullname END as other_trader, "
                + "CASE WHEN t.offer_trader_id = ? THEN i_offer.item_Name ELSE i_target.item_Name END as my_item, "
                + "t.trade_DateRequest as date, t.trade_status, "
                + "t.offer_trader_id, t.target_trader_id "
                + "FROM tbl_trade t "
                + "JOIN tbl_items i_offer ON t.offer_item_id = i_offer.items_id "
                + "JOIN tbl_items i_target ON t.target_item_id = i_target.items_id "
                + "JOIN tbl_users u_offer ON t.offer_trader_id = u_offer.user_id "
                + "JOIN tbl_users u_target ON t.target_trader_id = u_target.user_id "
                + "WHERE (t.offer_trader_id = ? OR t.target_trader_id = ?) "
                + "AND t.trade_status IN ('negotiating', 'arrangements_confirmed') "
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
                    displayStatus = "Confirmed";
                    break;
                default:
                    displayStatus = "Active";
            }

            activeTableModel.addRow(new Object[]{
                trade.get("trade_id"),
                trade.get("their_item"),
                trade.get("other_trader"),
                trade.get("my_item"),
                formatDate(trade.get("date")),
                displayStatus,
                trade.get("trade_id")
            });
        }
    }

    private void loadCompletedTrades() {
        completedTableModel.setRowCount(0);

        // SQL for completed trades (from trade_history)
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
                formatDate(trade.get("date")),
                "Completed"
            });
        }
    }

    private void displayTradeInstructions(int modelRow) {
        int tradeId = Integer.parseInt(activeTableModel.getValueAt(modelRow, 6).toString());
        String theirItem = activeTableModel.getValueAt(modelRow, 1).toString();
        String owner = activeTableModel.getValueAt(modelRow, 2).toString();
        String myItem = activeTableModel.getValueAt(modelRow, 3).toString();

        selectedTradeInfoLabel.setText("Trade #" + tradeId + ": " + theirItem);

        String instructions
                = "╔════════════════════════════════╗\n"
                + "║     EXCHANGE INSTRUCTIONS      ║\n"
                + "╚════════════════════════════════╝\n\n"
                + "📦 YOUR ITEM: " + myItem + "\n"
                + "📦 THEIR ITEM: " + theirItem + "\n"
                + "👤 TRADING WITH: " + owner + "\n\n"
                + "📋 STEP 1: CONTACT TRADER\n"
                + "   • Send a message to coordinate\n"
                + "   • Agree on meetup location or shipping method\n\n"
                + "📍 STEP 2: EXCHANGE OPTIONS\n"
                + "   • Meet in person at public location\n"
                + "   • Ship items with tracking numbers\n"
                + "   • Use courier service\n\n"
                + "✅ STEP 3: COMPLETE TRADE\n"
                + "   • Verify item condition matches description\n"
                + "   • Click 'Mark Complete' when done\n"
                + "   • Leave feedback for the trader\n\n"
                + "⚠️ SAFETY TIPS:\n"
                + "   • Meet in well-lit public places\n"
                + "   • Bring a friend if possible\n"
                + "   • Inspect items before completing\n"
                + "   • Report any issues to admin";

        instructionsArea.setText(instructions);
    }

    private void showFullInstructions() {
        int selectedRow = activeTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        int modelRow = activeTable.convertRowIndexToModel(selectedRow);
        int tradeId = Integer.parseInt(activeTableModel.getValueAt(modelRow, 6).toString());
        String theirItem = activeTableModel.getValueAt(modelRow, 1).toString();
        String owner = activeTableModel.getValueAt(modelRow, 2).toString();
        String myItem = activeTableModel.getValueAt(modelRow, 3).toString();

        String fullInstructions
                = "COMPLETE TRADE INSTRUCTIONS\n"
                + "============================\n\n"
                + "TRADE DETAILS:\n"
                + "• Trade ID: " + tradeId + "\n"
                + "• Your Item: " + myItem + "\n"
                + "• Their Item: " + theirItem + "\n"
                + "• Trading With: " + owner + "\n\n"
                + "EXCHANGE PROCESS:\n"
                + "-----------------\n"
                + "1. Contact the trader through the Messages feature\n"
                + "2. Discuss preferred exchange method (meetup or shipping)\n"
                + "3. Agree on date, time, and location\n"
                + "4. Exchange items and verify condition\n"
                + "5. Mark trade as complete\n\n"
                + "MEETUP GUIDELINES:\n"
                + "-----------------\n"
                + "• Choose a public, well-lit location\n"
                + "• Consider police station lobbies or coffee shops\n"
                + "• Bring a friend for safety\n"
                + "• Inspect items thoroughly before accepting\n"
                + "• Don't hand over your item until you receive theirs\n\n"
                + "SHIPPING GUIDELINES:\n"
                + "-----------------\n"
                + "• Use trackable shipping services\n"
                + "• Share tracking numbers with each other\n"
                + "• Pack items securely\n"
                + "• Consider insured shipping for valuable items\n"
                + "• Take photos before shipping as proof\n\n"
                + "AFTER EXCHANGE:\n"
                + "--------------\n"
                + "• Click 'Mark Complete' to finalize\n"
                + "• The trade will move to History\n"
                + "• Leave feedback for the trader (coming soon)\n\n"
                + "NEED HELP?\n"
                + "----------\n"
                + "• Contact support if issues arise\n"
                + "• Report suspicious behavior\n"
                + "• For disputes, email admin@barterzone.com";

        JOptionPane.showMessageDialog(this, fullInstructions,
                "Trade Instructions - Trade #" + tradeId,
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateCounts() {
        pendingCountLabel.setText(String.valueOf(pendingTableModel.getRowCount()));
        activeCountLabel.setText(String.valueOf(activeTableModel.getRowCount()));
        completedCountLabel.setText(String.valueOf(completedTableModel.getRowCount()));
    }

    private String formatDate(Object dateObj) {
        if (dateObj == null) {
            return "-";
        }
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

    private void requestTrade() {
        int selectedRow = availableTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to request trade.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Convert view row to model row
        int modelRow = availableTable.convertRowIndexToModel(selectedRow);
        int itemId = Integer.parseInt(availableTableModel.getValueAt(modelRow, 0).toString());
        String ownerName = availableTableModel.getValueAt(modelRow, 4).toString();

        // Show dialog to select your item for trade
        showTradeRequestDialog(itemId, ownerName);
    }

    private void showTradeRequestDialog(int targetItemId, String ownerName) {
        // Get trader's items
        String sql = "SELECT items_id, item_Name FROM tbl_items WHERE trader_id = ? AND is_active = 1";
        List<Map<String, Object>> myItems = db.fetchRecords(sql, traderId);

        if (myItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "You don't have any items to trade. Please add items first.", "No Items", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Create array of item names for dropdown
        String[] itemNames = new String[myItems.size()];
        Integer[] itemIds = new Integer[myItems.size()];
        for (int i = 0; i < myItems.size(); i++) {
            itemNames[i] = myItems.get(i).get("item_Name").toString();
            itemIds[i] = Integer.parseInt(myItems.get(i).get("items_id").toString());
        }

        // Create dialog
        javax.swing.JDialog tradeDialog = new javax.swing.JDialog(this, "Request Trade", true);
        tradeDialog.setSize(400, 280);
        tradeDialog.setLayout(null);
        tradeDialog.setLocationRelativeTo(this);
        tradeDialog.getContentPane().setBackground(Color.WHITE);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(12, 192, 223));
        titlePanel.setBounds(0, 0, 400, 40);
        titlePanel.setLayout(null);

        javax.swing.JLabel titleLabel = new javax.swing.JLabel("REQUEST TRADE");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(20, 5, 200, 30);
        titlePanel.add(titleLabel);
        tradeDialog.add(titlePanel);

        javax.swing.JLabel infoLabel = new javax.swing.JLabel("Trading with: " + ownerName);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoLabel.setBounds(20, 60, 300, 25);
        tradeDialog.add(infoLabel);

        javax.swing.JLabel theirItemLabel = new javax.swing.JLabel("Their Item: Selected from list");
        theirItemLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        theirItemLabel.setForeground(new Color(0, 102, 102));
        theirItemLabel.setBounds(20, 90, 300, 20);
        tradeDialog.add(theirItemLabel);

        javax.swing.JLabel selectLabel = new javax.swing.JLabel("Your Item to offer:");
        selectLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        selectLabel.setBounds(20, 120, 150, 20);
        tradeDialog.add(selectLabel);

        javax.swing.JComboBox<String> itemCombo = new javax.swing.JComboBox<>(itemNames);
        itemCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        itemCombo.setBounds(20, 145, 250, 30);
        itemCombo.setBackground(Color.WHITE);
        tradeDialog.add(itemCombo);

        javax.swing.JButton sendButton = new javax.swing.JButton("SEND REQUEST");
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendButton.setBackground(new Color(0, 102, 102));
        sendButton.setForeground(Color.WHITE);
        sendButton.setBounds(80, 195, 150, 35);
        sendButton.setBorder(null);
        sendButton.setFocusPainted(false);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        sendButton.addActionListener(e -> {
            int selectedIdx = itemCombo.getSelectedIndex();
            if (selectedIdx >= 0) {
                int selectedOfferItemId = itemIds[selectedIdx];
                createTradeRequest(targetItemId, selectedOfferItemId, ownerName);
                tradeDialog.dispose();
            }
        });
        tradeDialog.add(sendButton);

        javax.swing.JButton cancelButton = new javax.swing.JButton("CANCEL");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setBackground(new Color(204, 0, 0));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBounds(240, 195, 100, 35);
        cancelButton.setBorder(null);
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> tradeDialog.dispose());
        tradeDialog.add(cancelButton);

        tradeDialog.setVisible(true);
    }

    private void createTradeRequest(int targetItemId, int offerItemId, String ownerName) {
        // Get owner ID from target item
        String sql = "SELECT trader_id FROM tbl_items WHERE items_id = ?";
        List<Map<String, Object>> result = db.fetchRecords(sql, targetItemId);

        if (result.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Error: Item not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int targetTraderId = Integer.parseInt(result.get(0).get("trader_id").toString());

        // Check if trying to trade with self
        if (targetTraderId == traderId) {
            JOptionPane.showMessageDialog(this, "You cannot trade with your own item.", "Invalid Trade", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check if a pending trade already exists
        String checkSql = "SELECT COUNT(*) as count FROM tbl_trade WHERE "
                + "((offer_trader_id = ? AND target_trader_id = ?) OR "
                + "(offer_trader_id = ? AND target_trader_id = ?)) "
                + "AND trade_status IN ('pending', 'negotiating', 'arrangements_confirmed')";

        double count = db.getSingleValue(checkSql, traderId, targetTraderId, targetTraderId, traderId);

        if (count > 0) {
            JOptionPane.showMessageDialog(this,
                    "You already have a pending or active trade with this trader.",
                    "Trade Exists", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Insert trade request - using 'pending' (lowercase) to match CHECK constraint
        String insertSql = "INSERT INTO tbl_trade (offer_trader_id, target_trader_id, offer_item_id, "
                + "target_item_id, trade_status, trade_DateRequest) "
                + "VALUES (?, ?, ?, ?, ?, datetime('now'))";

        try {
            // Verify all IDs are valid
            if (traderId <= 0 || targetTraderId <= 0 || offerItemId <= 0 || targetItemId <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Invalid trade parameters. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verify items exist and are active
            String checkItemSql = "SELECT COUNT(*) as count FROM tbl_items WHERE items_id = ? AND is_active = 1";
            double offerItemCount = db.getSingleValue(checkItemSql, offerItemId);
            double targetItemCount = db.getSingleValue(checkItemSql, targetItemId);

            if (offerItemCount == 0 || targetItemCount == 0) {
                JOptionPane.showMessageDialog(this,
                        "One or both items are no longer available for trade.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Insert with lowercase 'pending' to match CHECK constraint
            db.addRecord(insertSql,
                    traderId,
                    targetTraderId,
                    offerItemId,
                    targetItemId,
                    "pending"
            );

            JOptionPane.showMessageDialog(this,
                    "✅ Trade request sent to " + ownerName + "!\n\n"
                    + "They will see your request in their Pending Trades.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            // Refresh data
            loadAllData();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error creating trade request: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void acceptTrade() {
        int selectedRow = pendingTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        int modelRow = pendingTable.convertRowIndexToModel(selectedRow);
        int tradeId = Integer.parseInt(pendingTableModel.getValueAt(modelRow, 6).toString());
        String theirItem = pendingTableModel.getValueAt(modelRow, 1).toString();
        String trader = pendingTableModel.getValueAt(modelRow, 2).toString();
        String myItem = pendingTableModel.getValueAt(modelRow, 3).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Accept this trade?\n\n"
                + "Their Item: " + theirItem + "\n"
                + "Your Item: " + myItem + "\n"
                + "Trader: " + trader,
                "Confirm Accept",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_trade SET trade_status = 'negotiating' WHERE trade_id = ?";
            db.updateRecord(sql, tradeId);

            JOptionPane.showMessageDialog(this,
                    "✅ Trade accepted!\n\n"
                    + "The trade has been moved to Active Trades.\n"
                    + "You can now negotiate exchange details.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            loadAllData();
        }
    }

    private void declineTrade() {
        int selectedRow = pendingTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        int modelRow = pendingTable.convertRowIndexToModel(selectedRow);
        int tradeId = Integer.parseInt(pendingTableModel.getValueAt(modelRow, 6).toString());

        int confirm = JOptionPane.showConfirmDialog(this,
                "Decline this trade request?", "Confirm Decline",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_trade SET trade_status = 'declined' WHERE trade_id = ?";
            db.updateRecord(sql, tradeId);

            JOptionPane.showMessageDialog(this, "Trade declined.", "Info", JOptionPane.INFORMATION_MESSAGE);
            loadAllData();
        }
    }

    private void completeTrade() {
        int selectedRow = activeTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        int modelRow = activeTable.convertRowIndexToModel(selectedRow);
        int tradeId = Integer.parseInt(activeTableModel.getValueAt(modelRow, 6).toString());
        String theirItem = activeTableModel.getValueAt(modelRow, 1).toString();
        String trader = activeTableModel.getValueAt(modelRow, 2).toString();
        String myItem = activeTableModel.getValueAt(modelRow, 3).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Have you completed the exchange?\n\n"
                + "✓ You should have received: " + theirItem + "\n"
                + "✓ Trader should have received: " + myItem + "\n\n"
                + "Make sure you have both received your items before marking complete.",
                "Confirm Complete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // Get trade details
            String getSql = "SELECT * FROM tbl_trade WHERE trade_id = ?";
            List<Map<String, Object>> trade = db.fetchRecords(getSql, tradeId);

            if (!trade.isEmpty()) {
                Map<String, Object> t = trade.get(0);

                // Insert into history
                String historySql = "INSERT INTO tbl_trade_history "
                        + "(trade_id, offer_trader_id, target_trader_id, offer_item_id, "
                        + "target_item_id, trade_status, trade_DateRequest, trade_DateCompleted) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, datetime('now'))";

                db.addRecord(historySql,
                        tradeId,
                        t.get("offer_trader_id"),
                        t.get("target_trader_id"),
                        t.get("offer_item_id"),
                        t.get("target_item_id"),
                        "completed",
                        t.get("trade_DateRequest"));

                // Delete from active trades
                String deleteSql = "DELETE FROM tbl_trade WHERE trade_id = ?";
                db.deleteRecord(deleteSql, tradeId);

                JOptionPane.showMessageDialog(this,
                        "✅ Trade completed successfully!\n\n"
                        + "The trade has been moved to History.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                loadAllData();
            }
        }
    }

    private void cancelTrade() {
        int selectedRow = activeTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        int modelRow = activeTable.convertRowIndexToModel(selectedRow);
        int tradeId = Integer.parseInt(activeTableModel.getValueAt(modelRow, 6).toString());

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel this trade?\n\n"
                + "This action cannot be undone.",
                "Confirm Cancel",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM tbl_trade WHERE trade_id = ?";
            db.deleteRecord(sql, tradeId);

            JOptionPane.showMessageDialog(this, "Trade cancelled.", "Info", JOptionPane.INFORMATION_MESSAGE);
            loadAllData();
        }
    }

    private void sendMessage() {
        int selectedRow = pendingTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        int modelRow = pendingTable.convertRowIndexToModel(selectedRow);
        String trader = pendingTableModel.getValueAt(modelRow, 2).toString();
        String theirItem = pendingTableModel.getValueAt(modelRow, 1).toString();

        JOptionPane.showMessageDialog(this,
                "📧 Message feature coming soon!\n\n"
                + "Send message to: " + trader + "\n"
                + "Regarding: " + theirItem,
                "Messages", JOptionPane.INFORMATION_MESSAGE);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        tradermenu1 = new javax.swing.JPanel();
        logotext = new javax.swing.JLabel();
        avatar = new javax.swing.JPanel();
        avatarletter = new javax.swing.JLabel();
        username = new javax.swing.JLabel();
        barterzonelogo = new javax.swing.JLabel();
        paneldashboard = new javax.swing.JPanel();
        dashboardicon = new javax.swing.JLabel();
        dashboard = new javax.swing.JLabel();
        panelmyitems = new javax.swing.JPanel();
        myitemsicon = new javax.swing.JLabel();
        myitems = new javax.swing.JLabel();
        panelfinditems = new javax.swing.JPanel();
        finditemsicon = new javax.swing.JLabel();
        finditems = new javax.swing.JLabel();
        paneltrades = new javax.swing.JPanel();
        tradesicon = new javax.swing.JLabel();
        trades = new javax.swing.JLabel();
        panelmessages = new javax.swing.JPanel();
        messagesicon = new javax.swing.JLabel();
        messages = new javax.swing.JLabel();
        panellogout = new javax.swing.JPanel();
        logout = new javax.swing.JLabel();
        logouticon = new javax.swing.JLabel();
        panelreports = new javax.swing.JPanel();
        Reports = new javax.swing.JLabel();
        reportsicon = new javax.swing.JLabel();
        panelprofile = new javax.swing.JPanel();
        profileicon = new javax.swing.JLabel();
        Profile = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        CurrentDate = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 500));
        setPreferredSize(new java.awt.Dimension(800, 500));
        setResizable(false);

        tradermenu1.setBackground(new java.awt.Color(12, 192, 223));
        tradermenu1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(8, 150, 175), 1, true));
        tradermenu1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logotext.setFont(new java.awt.Font("Segoe UI", 1, 22));
        logotext.setForeground(new java.awt.Color(255, 255, 255));
        logotext.setText("BarterZone");
        logotext.setAlignmentX(0.5F);
        tradermenu1.add(logotext, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 0, -1, 40));

        avatar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3));
        avatar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        avatarletter.setFont(new java.awt.Font("Arial", 1, 36));
        avatarletter.setForeground(new java.awt.Color(12, 192, 223));
        avatarletter.setText("T");
        avatar.add(avatarletter, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 50, 30));

        username.setBackground(new java.awt.Color(255, 255, 255));
        username.setFont(new java.awt.Font("Segoe UI", 1, 14));
        username.setForeground(new java.awt.Color(255, 255, 255));
        avatar.add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 110, 30));

        tradermenu1.add(avatar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 110, 60));

        barterzonelogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/logo.png")));
        tradermenu1.add(barterzonelogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        // DASHBOARD PANEL
        paneldashboard.setBackground(new java.awt.Color(12, 192, 223));
        paneldashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                paneldashboardMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                paneldashboardMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                paneldashboardMouseExited(evt);
            }
        });

        dashboardicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/dashboard.png")));

        dashboard.setFont(new java.awt.Font("Segoe UI", 1, 14));
        dashboard.setForeground(new java.awt.Color(255, 255, 255));
        dashboard.setText("Dashboard");

        javax.swing.GroupLayout paneldashboardLayout = new javax.swing.GroupLayout(paneldashboard);
        paneldashboard.setLayout(paneldashboardLayout);
        paneldashboardLayout.setHorizontalGroup(
                paneldashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneldashboardLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(dashboardicon, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14))
        );
        paneldashboardLayout.setVerticalGroup(
                paneldashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(dashboardicon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGroup(paneldashboardLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(dashboard)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tradermenu1.add(paneldashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 130, 40));

        // MY ITEMS PANEL
        panelmyitems.setBackground(new java.awt.Color(12, 192, 223));
        panelmyitems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelmyitemsMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelmyitemsMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelmyitemsMouseExited(evt);
            }
        });

        myitemsicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/myitems.png")));

        myitems.setFont(new java.awt.Font("Segoe UI", 1, 14));
        myitems.setForeground(new java.awt.Color(255, 255, 255));
        myitems.setText("My Items");

        javax.swing.GroupLayout panelmyitemsLayout = new javax.swing.GroupLayout(panelmyitems);
        panelmyitems.setLayout(panelmyitemsLayout);
        panelmyitemsLayout.setHorizontalGroup(
                panelmyitemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelmyitemsLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(myitemsicon, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(myitems)
                                .addContainerGap(17, Short.MAX_VALUE))
        );
        panelmyitemsLayout.setVerticalGroup(
                panelmyitemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelmyitemsLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(panelmyitemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(myitems)
                                        .addComponent(myitemsicon, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(21, 21, 21))
        );

        tradermenu1.add(panelmyitems, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 130, 40));

        // FIND ITEMS PANEL
        panelfinditems.setBackground(new java.awt.Color(12, 192, 223));
        panelfinditems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelfinditemsMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelfinditemsMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelfinditemsMouseExited(evt);
            }
        });

        finditemsicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/finditems.png")));

        finditems.setFont(new java.awt.Font("Segoe UI", 1, 14));
        finditems.setForeground(new java.awt.Color(255, 255, 255));
        finditems.setText("Find Items");

        javax.swing.GroupLayout panelfinditemsLayout = new javax.swing.GroupLayout(panelfinditems);
        panelfinditems.setLayout(panelfinditemsLayout);
        panelfinditemsLayout.setHorizontalGroup(
                panelfinditemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelfinditemsLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(finditemsicon, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(finditems)
                                .addGap(18, 18, 18))
        );
        panelfinditemsLayout.setVerticalGroup(
                panelfinditemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelfinditemsLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelfinditemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(finditems)
                                        .addComponent(finditemsicon, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tradermenu1.add(panelfinditems, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, 130, 40));

        // TRADES PANEL
        paneltrades.setBackground(new java.awt.Color(12, 192, 223));
        paneltrades.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Already in trades, refresh
                loadAllData();
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                paneltradesMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                paneltradesMouseExited(evt);
            }
        });

        tradesicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/trade.png")));

        trades.setFont(new java.awt.Font("Segoe UI", 1, 14));
        trades.setForeground(new java.awt.Color(255, 255, 255));
        trades.setText("Trades");

        javax.swing.GroupLayout paneltradesLayout = new javax.swing.GroupLayout(paneltrades);
        paneltrades.setLayout(paneltradesLayout);
        paneltradesLayout.setHorizontalGroup(
                paneltradesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneltradesLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(tradesicon, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(trades)
                                .addContainerGap(34, Short.MAX_VALUE))
        );
        paneltradesLayout.setVerticalGroup(
                paneltradesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneltradesLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(paneltradesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(trades)
                                        .addComponent(tradesicon, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(21, 21, 21))
        );

        tradermenu1.add(paneltrades, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, 130, 40));

        // MESSAGES PANEL
        panelmessages.setBackground(new java.awt.Color(12, 192, 223));
        panelmessages.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelmessagesMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelmessagesMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelmessagesMouseExited(evt);
            }
        });

        messagesicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/messages.png")));

        messages.setFont(new java.awt.Font("Segoe UI", 1, 14));
        messages.setForeground(new java.awt.Color(255, 255, 255));
        messages.setText("Messages");

        javax.swing.GroupLayout panelmessagesLayout = new javax.swing.GroupLayout(panelmessages);
        panelmessages.setLayout(panelmessagesLayout);
        panelmessagesLayout.setHorizontalGroup(
                panelmessagesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelmessagesLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(messagesicon, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(messages)
                                .addContainerGap(14, Short.MAX_VALUE))
        );
        panelmessagesLayout.setVerticalGroup(
                panelmessagesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelmessagesLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(panelmessagesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(messages)
                                        .addComponent(messagesicon, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22))
        );

        tradermenu1.add(panelmessages, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, 130, 40));

        // REPORTS PANEL
        panelreports.setBackground(new java.awt.Color(12, 192, 223));
        panelreports.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelreportsMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelreportsMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelreportsMouseExited(evt);
            }
        });

        Reports.setFont(new java.awt.Font("Segoe UI", 1, 14));
        Reports.setForeground(new java.awt.Color(255, 255, 255));
        Reports.setText("Reports");

        reportsicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/report.png")));

        javax.swing.GroupLayout panelreportsLayout = new javax.swing.GroupLayout(panelreports);
        panelreports.setLayout(panelreportsLayout);
        panelreportsLayout.setHorizontalGroup(
                panelreportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelreportsLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(reportsicon, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Reports)
                                .addContainerGap(27, Short.MAX_VALUE))
        );
        panelreportsLayout.setVerticalGroup(
                panelreportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelreportsLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelreportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(reportsicon, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                        .addComponent(Reports, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tradermenu1.add(panelreports, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 130, 40));

        // PROFILE PANEL
        panelprofile.setBackground(new java.awt.Color(12, 192, 223));
        panelprofile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelprofileMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelprofileMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelprofileMouseExited(evt);
            }
        });

        profileicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/profile.png")));

        Profile.setFont(new java.awt.Font("Segoe UI", 1, 14));
        Profile.setForeground(new java.awt.Color(255, 255, 255));
        Profile.setText("Profile");

        javax.swing.GroupLayout panelprofileLayout = new javax.swing.GroupLayout(panelprofile);
        panelprofile.setLayout(panelprofileLayout);
        panelprofileLayout.setHorizontalGroup(
                panelprofileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelprofileLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(profileicon, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Profile)
                                .addContainerGap(34, Short.MAX_VALUE))
        );
        panelprofileLayout.setVerticalGroup(
                panelprofileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelprofileLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelprofileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(Profile)
                                        .addComponent(profileicon, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tradermenu1.add(panelprofile, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, 130, 40));

        // LOGOUT PANEL
        panellogout.setBackground(new java.awt.Color(12, 192, 223));
        panellogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panellogoutMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panellogoutMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                panellogoutMouseExited(evt);
            }
        });

        logout.setFont(new java.awt.Font("Segoe UI", 1, 14));
        logout.setForeground(new java.awt.Color(255, 255, 255));
        logout.setText("Logout");

        logouticon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/logout.png")));

        javax.swing.GroupLayout panellogoutLayout = new javax.swing.GroupLayout(panellogout);
        panellogout.setLayout(panellogoutLayout);
        panellogoutLayout.setHorizontalGroup(
                panellogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panellogoutLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(logouticon, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(16, 16, 16)
                                .addComponent(logout)
                                .addContainerGap(34, Short.MAX_VALUE))
        );
        panellogoutLayout.setVerticalGroup(
                panellogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panellogoutLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(panellogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(logout)
                                        .addComponent(logouticon, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22))
        );

        tradermenu1.add(panellogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 400, 130, 40));

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 2));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30));
        jLabel1.setText("Trades");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, 30));

        CurrentDate.setFont(new java.awt.Font("Segoe UI", 0, 14));
        CurrentDate.setForeground(new java.awt.Color(102, 102, 102));
        CurrentDate.setText("jLabel2");
        jPanel1.add(CurrentDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 10, 200, 30));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(tradermenu1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(tradermenu1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, 0)
                                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>                        

    // Sidebar Navigation Methods
    private void paneldashboardMouseClicked(java.awt.event.MouseEvent evt) {
        openDashboard();
    }

    private void panelmyitemsMouseClicked(java.awt.event.MouseEvent evt) {
        openMyItems();
    }

    private void panelfinditemsMouseClicked(java.awt.event.MouseEvent evt) {
        openFindItems();
    }

    private void panelmessagesMouseClicked(java.awt.event.MouseEvent evt) {
        openMessages();
    }

    private void panelreportsMouseClicked(java.awt.event.MouseEvent evt) {
        openReports();
    }

    private void panelprofileMouseClicked(java.awt.event.MouseEvent evt) {
        openProfile();
    }

    private void panellogoutMouseClicked(java.awt.event.MouseEvent evt) {
        logout();
    }

    // Hover Effects
    private void paneldashboardMouseEntered(java.awt.event.MouseEvent evt) {
        setHover(paneldashboard);
    }

    private void paneldashboardMouseExited(java.awt.event.MouseEvent evt) {
        setDefault(paneldashboard);
    }

    private void panelmyitemsMouseEntered(java.awt.event.MouseEvent evt) {
        setHover(panelmyitems);
    }

    private void panelmyitemsMouseExited(java.awt.event.MouseEvent evt) {
        setDefault(panelmyitems);
    }

    private void panelfinditemsMouseEntered(java.awt.event.MouseEvent evt) {
        setHover(panelfinditems);
    }

    private void panelfinditemsMouseExited(java.awt.event.MouseEvent evt) {
        setDefault(panelfinditems);
    }

    private void paneltradesMouseEntered(java.awt.event.MouseEvent evt) {
        setHover(paneltrades);
    }

    private void paneltradesMouseExited(java.awt.event.MouseEvent evt) {
        setDefault(paneltrades);
    }

    private void panelmessagesMouseEntered(java.awt.event.MouseEvent evt) {
        setHover(panelmessages);
    }

    private void panelmessagesMouseExited(java.awt.event.MouseEvent evt) {
        setDefault(panelmessages);
    }

    private void panelreportsMouseEntered(java.awt.event.MouseEvent evt) {
        setHover(panelreports);
    }

    private void panelreportsMouseExited(java.awt.event.MouseEvent evt) {
        setDefault(panelreports);
    }

    private void panelprofileMouseEntered(java.awt.event.MouseEvent evt) {
        setHover(panelprofile);
    }

    private void panelprofileMouseExited(java.awt.event.MouseEvent evt) {
        setDefault(panelprofile);
    }

    private void panellogoutMouseEntered(java.awt.event.MouseEvent evt) {
        setHover(panellogout);
    }

    private void panellogoutMouseExited(java.awt.event.MouseEvent evt) {
        setDefault(panellogout);
    }

    // Navigation Methods
    private void openDashboard() {
        trader_dashboard dashboard = new trader_dashboard(traderId, traderName);
        dashboard.setVisible(true);
        dashboard.setLocationRelativeTo(null);
        this.dispose();
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

    private void openMessages() {
        JOptionPane.showMessageDialog(this, "Messages feature coming soon!", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openReports() {
        JOptionPane.showMessageDialog(this, "Reports feature coming soon!", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openProfile() {
        profile profileFrame = new profile(traderId, traderName);
        profileFrame.setVisible(true);
        profileFrame.setLocationRelativeTo(null);
        this.dispose();
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            landing.landing landingFrame = new landing.landing();
            landingFrame.setVisible(true);
            landingFrame.setLocationRelativeTo(null);
            this.dispose();
        }
    }

    // Variables declaration - do not modify                     
    private javax.swing.JLabel CurrentDate;
    private javax.swing.JLabel Profile;
    javax.swing.JLabel Reports;
    javax.swing.JPanel avatar;
    javax.swing.JLabel avatarletter;
    javax.swing.JLabel barterzonelogo;
    javax.swing.JLabel dashboard;
    javax.swing.JLabel dashboardicon;
    javax.swing.JLabel finditems;
    javax.swing.JLabel finditemsicon;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    javax.swing.JPanel jPanel2;
    javax.swing.JLabel logotext;
    javax.swing.JLabel logout;
    javax.swing.JLabel logouticon;
    javax.swing.JLabel messages;
    javax.swing.JLabel messagesicon;
    javax.swing.JLabel myitems;
    javax.swing.JLabel myitemsicon;
    javax.swing.JPanel paneldashboard;
    javax.swing.JPanel panelfinditems;
    javax.swing.JPanel panellogout;
    javax.swing.JPanel panelmessages;
    javax.swing.JPanel panelmyitems;
    javax.swing.JPanel panelprofile;
    javax.swing.JPanel panelreports;
    javax.swing.JPanel paneltrades;
    javax.swing.JLabel profileicon;
    javax.swing.JLabel reportsicon;
    javax.swing.JPanel tradermenu1;
    javax.swing.JLabel trades;
    javax.swing.JLabel tradesicon;
    javax.swing.JLabel username;
    // End of variables declaration                   
}
