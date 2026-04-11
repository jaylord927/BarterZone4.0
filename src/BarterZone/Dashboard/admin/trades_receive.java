package BarterZone.Dashboard.admin;

import database.config.config;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class trades_receive extends JFrame {
    
    private int adminId;
    private String adminName;
    private config db;
    
    // UI Components
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JLabel titleLabel;
    private JLabel currentDateLabel;
    private JButton backButton;
    private JButton refreshButton;
    
    private JPanel filterPanel;
    private JTextField searchField;
    private JComboBox<String> statusFilter;
    
    private JScrollPane tableScrollPane;
    private javax.swing.JTable tradesTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    
    private JPanel detailsPanel;
    private JTextArea detailsArea;
    private JScrollPane detailsScrollPane;
    
    private JPanel actionPanel;
    private JButton viewTradeDetailsButton;
    private JButton markBothReceivedButton;
    private JButton proceedToRefundButton;
    
    // Selected trade data
    private int selectedTradeId = -1;
    private int selectedOfferTraderId = -1;
    private int selectedTargetTraderId = -1;
    private String selectedOfferTraderName = "";
    private String selectedTargetTraderName = "";
    private boolean myItemReceived = false;
    private boolean otherItemReceived = false;
    
    // Colors
    private Color sideBarColor = new Color(8, 78, 128);
    private Color accentColor = new Color(255, 215, 0);
    private Color headerGradientStart = new Color(8, 78, 128);
    private Color headerGradientEnd = new Color(0, 45, 80);
    private Color successColor = new Color(46, 125, 50);
    private Color warningColor = new Color(255, 153, 0);
    private Color errorColor = new Color(204, 0, 0);
    private Color infoColor = new Color(33, 150, 243);
    
    public trades_receive(int adminId, String adminName) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.db = new config();
        
        initComponents();
        setupHeader();
        setupContentPanel();
        loadTradesData();
        
        setTitle("Monitor Item Receipt - " + adminName);
        setSize(900, 650);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    private void initComponents() {
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);
        
        // Header Panel
        headerPanel = new JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                java.awt.GradientPaint gp = new java.awt.GradientPaint(0, 0, headerGradientStart, w, 0, headerGradientEnd);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        headerPanel.setLayout(null);
        headerPanel.setBounds(0, 0, 900, 70);
        getContentPane().add(headerPanel);
        
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(245, 245, 250));
        mainPanel.setBounds(0, 70, 900, 580);
        getContentPane().add(mainPanel);
    }
    
    private void setupHeader() {
        titleLabel = new JLabel("Monitor Item Receipt - Step 4");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(20, 15, 400, 40);
        headerPanel.add(titleLabel);
        
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE, dd MMMM yyyy");
        currentDateLabel = new JLabel(sdf.format(new java.util.Date()));
        currentDateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        currentDateLabel.setForeground(Color.WHITE);
        currentDateLabel.setBounds(600, 25, 250, 30);
        headerPanel.add(currentDateLabel);
        
        backButton = new JButton("← Back to Manage Trades");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        backButton.setBackground(sideBarColor);
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(20, 20, 180, 30);
        backButton.setBorder(null);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            manage_trades tradesFrame = new manage_trades(adminId, adminName);
            tradesFrame.setVisible(true);
            tradesFrame.setLocationRelativeTo(null);
            this.dispose();
        });
        // Position back button differently since we have it in header
        // Actually let's put it in main panel
    }
    
    private void setupContentPanel() {
        // Back button at top of main panel
        backButton = new JButton("← Back to Manage Trades");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        backButton.setBackground(sideBarColor);
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(20, 10, 180, 35);
        backButton.setBorder(null);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            manage_trades tradesFrame = new manage_trades(adminId, adminName);
            tradesFrame.setVisible(true);
            tradesFrame.setLocationRelativeTo(null);
            this.dispose();
        });
        mainPanel.add(backButton);
        
        refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshButton.setBackground(accentColor);
        refreshButton.setForeground(sideBarColor);
        refreshButton.setBounds(210, 10, 100, 35);
        refreshButton.setBorder(null);
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> {
            loadTradesData();
            clearSelection();
        });
        mainPanel.add(refreshButton);
        
        // Filter Panel
        filterPanel = new JPanel();
        filterPanel.setLayout(null);
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(new LineBorder(accentColor, 1));
        filterPanel.setBounds(20, 55, 860, 60);
        mainPanel.add(filterPanel);
        
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchLabel.setForeground(sideBarColor);
        searchLabel.setBounds(15, 20, 60, 25);
        filterPanel.add(searchLabel);
        
        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setBounds(80, 20, 200, 25);
        searchField.setBorder(new LineBorder(new Color(200, 200, 200)));
        filterPanel.add(searchField);
        
        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setForeground(sideBarColor);
        statusLabel.setBounds(300, 20, 50, 25);
        filterPanel.add(statusLabel);
        
        String[] statuses = {"All Trades", "Both Received", "One Received", "None Received"};
        statusFilter = new JComboBox<>(statuses);
        statusFilter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusFilter.setBounds(355, 20, 130, 25);
        statusFilter.setBackground(Color.WHITE);
        statusFilter.setBorder(new LineBorder(new Color(200, 200, 200)));
        statusFilter.addActionListener(e -> applyFilter());
        filterPanel.add(statusFilter);
        
        // Stats Summary
        JLabel statsLabel = new JLabel();
        statsLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statsLabel.setForeground(sideBarColor);
        statsLabel.setBounds(520, 20, 200, 25);
        statsLabel.setText("Trades Awaiting Receipt: 0");
        filterPanel.add(statsLabel);
        
        // Setup Table
        setupTable();
        tableScrollPane = new JScrollPane(tradesTable);
        tableScrollPane.setBounds(20, 125, 550, 400);
        tableScrollPane.setBorder(new LineBorder(accentColor, 1));
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainPanel.add(tableScrollPane);
        
        // Details Panel
        detailsPanel = new JPanel();
        detailsPanel.setLayout(null);
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(new LineBorder(accentColor, 1));
        detailsPanel.setBounds(580, 125, 300, 310);
        mainPanel.add(detailsPanel);
        
        JLabel detailsTitle = new JLabel("Trade Status");
        detailsTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        detailsTitle.setForeground(sideBarColor);
        detailsTitle.setBounds(10, 10, 150, 25);
        detailsPanel.add(detailsTitle);
        
        detailsArea = new JTextArea();
        detailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        detailsArea.setEditable(false);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setBackground(new Color(250, 250, 250));
        detailsArea.setText("Select a trade to view details");
        
        detailsScrollPane = new JScrollPane(detailsArea);
        detailsScrollPane.setBounds(10, 40, 280, 260);
        detailsScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        detailsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        detailsPanel.add(detailsScrollPane);
        
        // Action Panel
        actionPanel = new JPanel();
        actionPanel.setLayout(null);
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(new LineBorder(accentColor, 1));
        actionPanel.setBounds(580, 445, 300, 80);
        mainPanel.add(actionPanel);
        
        JLabel actionLabel = new JLabel("Actions:");
        actionLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        actionLabel.setForeground(sideBarColor);
        actionLabel.setBounds(10, 10, 60, 25);
        actionPanel.add(actionLabel);
        
        viewTradeDetailsButton = new JButton("View Trade Details");
        viewTradeDetailsButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        viewTradeDetailsButton.setBackground(infoColor);
        viewTradeDetailsButton.setForeground(Color.WHITE);
        viewTradeDetailsButton.setBounds(10, 40, 130, 30);
        viewTradeDetailsButton.setBorder(null);
        viewTradeDetailsButton.setFocusPainted(false);
        viewTradeDetailsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewTradeDetailsButton.setEnabled(false);
        viewTradeDetailsButton.addActionListener(e -> viewTradeDetails());
        actionPanel.add(viewTradeDetailsButton);
        
        markBothReceivedButton = new JButton("Mark Both Received");
        markBothReceivedButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        markBothReceivedButton.setBackground(successColor);
        markBothReceivedButton.setForeground(Color.WHITE);
        markBothReceivedButton.setBounds(150, 10, 140, 30);
        markBothReceivedButton.setBorder(null);
        markBothReceivedButton.setFocusPainted(false);
        markBothReceivedButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        markBothReceivedButton.setEnabled(false);
        markBothReceivedButton.addActionListener(e -> markBothReceived());
        actionPanel.add(markBothReceivedButton);
        
        proceedToRefundButton = new JButton("Proceed to Refund");
        proceedToRefundButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        proceedToRefundButton.setBackground(warningColor);
        proceedToRefundButton.setForeground(Color.WHITE);
        proceedToRefundButton.setBounds(150, 40, 140, 30);
        proceedToRefundButton.setBorder(null);
        proceedToRefundButton.setFocusPainted(false);
        proceedToRefundButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        proceedToRefundButton.setEnabled(false);
        proceedToRefundButton.addActionListener(e -> proceedToRefund());
        actionPanel.add(proceedToRefundButton);
        
        // Setup search
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { applySearch(); }
            @Override
            public void removeUpdate(DocumentEvent e) { applySearch(); }
            @Override
            public void changedUpdate(DocumentEvent e) { applySearch(); }
        });
    }
    
    private void setupTable() {
        String[] columns = {"Trade ID", "Trader 1", "Trader 2", "Item 1", "Item 2", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tradesTable = new javax.swing.JTable(tableModel);
        tradesTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tradesTable.setRowHeight(35);
        tradesTable.setShowGrid(true);
        tradesTable.setGridColor(new Color(200, 200, 200));
        tradesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tradesTable.getTableHeader().setBackground(sideBarColor);
        tradesTable.getTableHeader().setForeground(Color.WHITE);
        tradesTable.setSelectionBackground(new Color(255, 235, 204));
        tradesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Set column widths
        tradesTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        tradesTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        tradesTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        tradesTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        tradesTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        tradesTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        
        tradesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tradesTable.getSelectedRow();
                if (selectedRow != -1) {
                    int modelRow = tradesTable.convertRowIndexToModel(selectedRow);
                    displayTradeDetails(modelRow);
                } else {
                    clearSelection();
                }
            }
        });
    }
    
    private void loadTradesData() {
        tableModel.setRowCount(0);
        
        // Get trades that are in payment_verified or items_received status
        String sql = "SELECT t.trade_id, t.offer_trader_id, t.target_trader_id, "
                + "t.my_item_received, t.other_item_received, t.trade_status, "
                + "u1.user_fullname as trader1_name, u2.user_fullname as trader2_name, "
                + "i1.item_Name as item1_name, i2.item_Name as item2_name "
                + "FROM tbl_trade t "
                + "LEFT JOIN tbl_users u1 ON t.offer_trader_id = u1.user_id "
                + "LEFT JOIN tbl_users u2 ON t.target_trader_id = u2.user_id "
                + "LEFT JOIN tbl_items i1 ON t.offer_item_id = i1.items_id "
                + "LEFT JOIN tbl_items i2 ON t.target_item_id = i2.items_id "
                + "WHERE t.trade_status IN ('payment_verified', 'items_received') "
                + "ORDER BY t.trade_id DESC";
        
        List<Map<String, Object>> trades = db.fetchRecords(sql);
        
        int statsTotal = 0;
        
        for (Map<String, Object> trade : trades) {
            int tradeId = Integer.parseInt(trade.get("trade_id").toString());
            int myReceived = trade.get("my_item_received") != null ? 
                Integer.parseInt(trade.get("my_item_received").toString()) : 0;
            int otherReceived = trade.get("other_item_received") != null ? 
                Integer.parseInt(trade.get("other_item_received").toString()) : 0;
            
            String statusText = "";
            Color statusColor = null;
            
            if (myReceived == 1 && otherReceived == 1) {
                statusText = "Both Received ✓";
                statusColor = successColor;
                statsTotal++;
            } else if (myReceived == 1 || otherReceived == 1) {
                statusText = "One Received ⏳";
                statusColor = warningColor;
                statsTotal++;
            } else {
                statusText = "Pending ❌";
                statusColor = errorColor;
                statsTotal++;
            }
            
            tableModel.addRow(new Object[]{
                tradeId,
                trade.get("trader1_name"),
                trade.get("trader2_name"),
                trade.get("item1_name"),
                trade.get("item2_name"),
                statusText
            });
        }
        
        // Update stats label
        JLabel statsLabel = (JLabel) filterPanel.getComponent(5);
        if (statsLabel != null) {
            statsLabel.setText("Trades Awaiting Receipt: " + statsTotal);
        }
        
        rowSorter = new TableRowSorter<>(tableModel);
        tradesTable.setRowSorter(rowSorter);
    }
    
    private void displayTradeDetails(int modelRow) {
        selectedTradeId = Integer.parseInt(tableModel.getValueAt(modelRow, 0).toString());
        selectedOfferTraderName = tableModel.getValueAt(modelRow, 1).toString();
        selectedTargetTraderName = tableModel.getValueAt(modelRow, 2).toString();
        
        // Get detailed trade info
        String sql = "SELECT t.*, "
                + "u1.user_fullname as trader1_name, u2.user_fullname as trader2_name, "
                + "i1.item_Name as item1_name, i2.item_Name as item2_name "
                + "FROM tbl_trade t "
                + "LEFT JOIN tbl_users u1 ON t.offer_trader_id = u1.user_id "
                + "LEFT JOIN tbl_users u2 ON t.target_trader_id = u2.user_id "
                + "LEFT JOIN tbl_items i1 ON t.offer_item_id = i1.items_id "
                + "LEFT JOIN tbl_items i2 ON t.target_item_id = i2.items_id "
                + "WHERE t.trade_id = ?";
        
        List<Map<String, Object>> result = db.fetchRecords(sql, selectedTradeId);
        
        if (!result.isEmpty()) {
            Map<String, Object> trade = result.get(0);
            selectedOfferTraderId = Integer.parseInt(trade.get("offer_trader_id").toString());
            selectedTargetTraderId = Integer.parseInt(trade.get("target_trader_id").toString());
            myItemReceived = trade.get("my_item_received") != null && 
                Integer.parseInt(trade.get("my_item_received").toString()) == 1;
            otherItemReceived = trade.get("other_item_received") != null && 
                Integer.parseInt(trade.get("other_item_received").toString()) == 1;
            
            StringBuilder details = new StringBuilder();
            details.append("═══════════════════════════════════════\n");
            details.append("           TRADE DETAILS\n");
            details.append("═══════════════════════════════════════\n\n");
            
            details.append("Trade ID: ").append(selectedTradeId).append("\n\n");
            
            details.append("━━━━━━━━━━ TRADER 1 ━━━━━━━━━━\n");
            details.append("Name: ").append(trade.get("trader1_name")).append("\n");
            details.append("Item: ").append(trade.get("item1_name")).append("\n");
            details.append("Item Received: ").append(myItemReceived ? "✓ YES" : "❌ NO").append("\n\n");
            
            details.append("━━━━━━━━━━ TRADER 2 ━━━━━━━━━━\n");
            details.append("Name: ").append(trade.get("trader2_name")).append("\n");
            details.append("Item: ").append(trade.get("item2_name")).append("\n");
            details.append("Item Received: ").append(otherItemReceived ? "✓ YES" : "❌ NO").append("\n\n");
            
            details.append("━━━━━━━━━━ STATUS ━━━━━━━━━━\n");
            if (myItemReceived && otherItemReceived) {
                details.append("Status: BOTH TRADERS HAVE RECEIVED ITEMS\n");
                details.append("Action: Ready to proceed to refund step.");
            } else if (myItemReceived) {
                details.append("Status: ").append(selectedOfferTraderName).append(" has received the item.\n");
                details.append("Waiting for ").append(selectedTargetTraderName).append(" to confirm receipt.");
            } else if (otherItemReceived) {
                details.append("Status: ").append(selectedTargetTraderName).append(" has received the item.\n");
                details.append("Waiting for ").append(selectedOfferTraderName).append(" to confirm receipt.");
            } else {
                details.append("Status: No items confirmed yet.\n");
                details.append("Waiting for both traders to confirm receipt.");
            }
            
            detailsArea.setText(details.toString());
            detailsArea.setCaretPosition(0);
            
            // Enable/disable buttons
            viewTradeDetailsButton.setEnabled(true);
            markBothReceivedButton.setEnabled(!myItemReceived || !otherItemReceived);
            proceedToRefundButton.setEnabled(myItemReceived && otherItemReceived);
        }
    }
    
    private void clearSelection() {
        selectedTradeId = -1;
        selectedOfferTraderId = -1;
        selectedTargetTraderId = -1;
        selectedOfferTraderName = "";
        selectedTargetTraderName = "";
        myItemReceived = false;
        otherItemReceived = false;
        
        detailsArea.setText("Select a trade to view details");
        viewTradeDetailsButton.setEnabled(false);
        markBothReceivedButton.setEnabled(false);
        proceedToRefundButton.setEnabled(false);
    }
    
    private void applyFilter() {
        String filter = (String) statusFilter.getSelectedItem();
        
        if (filter.equals("All Trades")) {
            rowSorter.setRowFilter(null);
        } else if (filter.equals("Both Received")) {
            rowSorter.setRowFilter(RowFilter.regexFilter("Both Received", 5));
        } else if (filter.equals("One Received")) {
            rowSorter.setRowFilter(RowFilter.regexFilter("One Received", 5));
        } else if (filter.equals("None Received")) {
            rowSorter.setRowFilter(RowFilter.regexFilter("Pending", 5));
        }
    }
    
    private void applySearch() {
        String text = searchField.getText().trim();
        if (text.isEmpty()) {
            applyFilter();
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 1, 2, 3, 4));
        }
    }
    
    private void viewTradeDetails() {
        if (selectedTradeId == -1) return;
        
        JDialog detailsDialog = new JDialog(this, "Trade #" + selectedTradeId + " Details", true);
        detailsDialog.setSize(500, 400);
        detailsDialog.setLayout(null);
        detailsDialog.setLocationRelativeTo(this);
        detailsDialog.getContentPane().setBackground(Color.WHITE);
        
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(sideBarColor);
        titlePanel.setBounds(0, 0, 500, 45);
        titlePanel.setLayout(null);
        
        JLabel titleLabel = new JLabel("TRADE #" + selectedTradeId + " DETAILS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(20, 8, 300, 30);
        titlePanel.add(titleLabel);
        detailsDialog.add(titlePanel);
        
        JTextArea fullDetailsArea = new JTextArea();
        fullDetailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        fullDetailsArea.setEditable(false);
        fullDetailsArea.setLineWrap(true);
        fullDetailsArea.setWrapStyleWord(true);
        
        String sql = "SELECT t.*, "
                + "u1.user_fullname as trader1_name, u1.user_email as trader1_email, "
                + "u2.user_fullname as trader2_name, u2.user_email as trader2_email, "
                + "i1.item_Name as item1_name, i1.item_Brand as item1_brand, "
                + "i2.item_Name as item2_name, i2.item_Brand as item2_brand "
                + "FROM tbl_trade t "
                + "LEFT JOIN tbl_users u1 ON t.offer_trader_id = u1.user_id "
                + "LEFT JOIN tbl_users u2 ON t.target_trader_id = u2.user_id "
                + "LEFT JOIN tbl_items i1 ON t.offer_item_id = i1.items_id "
                + "LEFT JOIN tbl_items i2 ON t.target_item_id = i2.items_id "
                + "WHERE t.trade_id = ?";
        
        List<Map<String, Object>> result = db.fetchRecords(sql, selectedTradeId);
        
        if (!result.isEmpty()) {
            Map<String, Object> trade = result.get(0);
            
            StringBuilder fullDetails = new StringBuilder();
            fullDetails.append("TRADE INFORMATION\n");
            fullDetails.append("=================\n\n");
            fullDetails.append("Trade ID: ").append(trade.get("trade_id")).append("\n");
            fullDetails.append("Status: ").append(trade.get("trade_status")).append("\n");
            fullDetails.append("Exchange Method: ").append(trade.get("exchange_method") != null ? trade.get("exchange_method") : "Not set").append("\n");
            fullDetails.append("Date Requested: ").append(trade.get("trade_DateRequest")).append("\n\n");
            
            fullDetails.append("TRADER 1 (Offerer)\n");
            fullDetails.append("-----------------\n");
            fullDetails.append("Name: ").append(trade.get("trader1_name")).append("\n");
            fullDetails.append("Email: ").append(trade.get("trader1_email")).append("\n");
            fullDetails.append("Item Offered: ").append(trade.get("item1_name")).append("\n");
            fullDetails.append("Brand: ").append(trade.get("item1_brand")).append("\n");
            fullDetails.append("Item Received: ").append(myItemReceived ? "YES" : "NO").append("\n\n");
            
            fullDetails.append("TRADER 2 (Target)\n");
            fullDetails.append("-----------------\n");
            fullDetails.append("Name: ").append(trade.get("trader2_name")).append("\n");
            fullDetails.append("Email: ").append(trade.get("trader2_email")).append("\n");
            fullDetails.append("Item Offered: ").append(trade.get("item2_name")).append("\n");
            fullDetails.append("Brand: ").append(trade.get("item2_brand")).append("\n");
            fullDetails.append("Item Received: ").append(otherItemReceived ? "YES" : "NO").append("\n");
            
            fullDetailsArea.setText(fullDetails.toString());
        }
        
        JScrollPane scrollPane = new JScrollPane(fullDetailsArea);
        scrollPane.setBounds(20, 55, 460, 280);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        detailsDialog.add(scrollPane);
        
        JButton closeButton = new JButton("CLOSE");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeButton.setBackground(sideBarColor);
        closeButton.setForeground(Color.WHITE);
        closeButton.setBounds(200, 345, 100, 35);
        closeButton.setBorder(null);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> detailsDialog.dispose());
        detailsDialog.add(closeButton);
        
        detailsDialog.setVisible(true);
    }
    
    private void markBothReceived() {
        if (selectedTradeId == -1) return;
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Mark both traders as having received items?\n\n"
            + "Trade #" + selectedTradeId + "\n"
            + "Trader 1: " + selectedOfferTraderName + " - " + (myItemReceived ? "Already received" : "Not yet" + "\n")
            + "Trader 2: " + selectedTargetTraderName + " - " + (otherItemReceived ? "Already received" : "Not yet") + "\n\n"
            + "This will update the trade status to 'items_received'.",
            "Confirm Both Received",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (!myItemReceived) {
                String sql1 = "UPDATE tbl_trade SET my_item_received = 1 WHERE trade_id = ?";
                db.updateRecord(sql1, selectedTradeId);
            }
            if (!otherItemReceived) {
                String sql2 = "UPDATE tbl_trade SET other_item_received = 1 WHERE trade_id = ?";
                db.updateRecord(sql2, selectedTradeId);
            }
            
            String updateStatusSql = "UPDATE tbl_trade SET trade_status = 'items_received' WHERE trade_id = ?";
            db.updateRecord(updateStatusSql, selectedTradeId);
            
            JOptionPane.showMessageDialog(this,
                "Both traders marked as having received items!\n\n"
                + "Trade #" + selectedTradeId + " is now ready for refund processing.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            logActivity("Marked both traders as received for Trade #" + selectedTradeId);
            loadTradesData();
            clearSelection();
        }
    }
    
    private void proceedToRefund() {
        if (selectedTradeId == -1) return;
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Proceed to Refund Management for Trade #" + selectedTradeId + "?\n\n"
            + "This will take you to the Refund Management tab where you can process refunds.",
            "Proceed to Refund",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            manage_trades tradesFrame = new manage_trades(adminId, adminName);
            tradesFrame.setVisible(true);
            tradesFrame.setLocationRelativeTo(null);
            
            // Switch to Refund Management tab
            // Note: This will need to be handled in manage_trades - you may want to add a parameter
            // to open directly to the refund tab
            
            this.dispose();
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
} 