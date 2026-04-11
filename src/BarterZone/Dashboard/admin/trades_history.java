package BarterZone.Dashboard.admin;

import database.config.config;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class trades_history extends JPanel {
    
    private config db;
    private int adminId;
    private String adminName;
    
    // Components
    private JTextField searchField;
    private JComboBox<String> filterCombo;
    private JButton refreshButton;
    private JTable historyTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private JLabel totalLabel;
    
    // Colors
    private Color sideBarColor = new Color(8, 78, 128);
    private Color accentColor = new Color(255, 215, 0);
    private Color successColor = new Color(46, 125, 50);
    
    public trades_history(config db, int adminId, String adminName) {
        this.db = db;
        this.adminId = adminId;
        this.adminName = adminName;
        initPanel();
        loadHistoryData();
    }
    
    private void initPanel() {
        setLayout(null);
        setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Trade History");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(sideBarColor);
        titleLabel.setBounds(20, 20, 300, 30);
        add(titleLabel);
        
        JLabel descLabel = new JLabel("View all completed trades");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(new Color(102, 102, 102));
        descLabel.setBounds(20, 55, 300, 20);
        add(descLabel);
        
        // Filter Panel
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(null);
        filterPanel.setBackground(new Color(250, 250, 250));
        filterPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        filterPanel.setBounds(20, 90, 800, 50);
        add(filterPanel);
        
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchLabel.setForeground(sideBarColor);
        searchLabel.setBounds(15, 15, 60, 25);
        filterPanel.add(searchLabel);
        
        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setBounds(80, 15, 200, 25);
        searchField.setBorder(new LineBorder(new Color(200, 200, 200)));
        filterPanel.add(searchField);
        
        JLabel filterLabel = new JLabel("Filter:");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        filterLabel.setForeground(sideBarColor);
        filterLabel.setBounds(300, 15, 50, 25);
        filterPanel.add(filterLabel);
        
        String[] filters = {"All Trades", "Last 7 Days", "Last 30 Days", "Last 90 Days"};
        filterCombo = new JComboBox<>(filters);
        filterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        filterCombo.setBounds(355, 15, 120, 25);
        filterCombo.setBackground(Color.WHITE);
        filterCombo.setBorder(new LineBorder(new Color(200, 200, 200)));
        filterCombo.addActionListener(e -> filterHistory());
        filterPanel.add(filterCombo);
        
        refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshButton.setBackground(accentColor);
        refreshButton.setForeground(sideBarColor);
        refreshButton.setBounds(500, 15, 100, 25);
        refreshButton.setBorder(null);
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> loadHistoryData());
        filterPanel.add(refreshButton);
        
        totalLabel = new JLabel("Total Completed: 0");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        totalLabel.setForeground(successColor);
        totalLabel.setBounds(620, 15, 200, 25);
        filterPanel.add(totalLabel);
        
        // Setup Table
        String[] columns = {"ID", "Trade ID", "Trader 1", "Trader 2", "Item 1", "Item 2", "Date Completed"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        historyTable = new JTable(tableModel);
        historyTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        historyTable.setRowHeight(35);
        historyTable.setShowGrid(true);
        historyTable.setGridColor(new Color(200, 200, 200));
        historyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        historyTable.getTableHeader().setBackground(sideBarColor);
        historyTable.getTableHeader().setForeground(Color.WHITE);
        historyTable.setSelectionBackground(new Color(200, 230, 201));
        historyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Hide ID column
        historyTable.getColumnModel().getColumn(0).setMinWidth(0);
        historyTable.getColumnModel().getColumn(0).setMaxWidth(0);
        historyTable.getColumnModel().getColumn(0).setWidth(0);
        
        // Set column widths
        historyTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        historyTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        historyTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        historyTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        historyTable.getColumnModel().getColumn(5).setPreferredWidth(120);
        historyTable.getColumnModel().getColumn(6).setPreferredWidth(140);
        
        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setBounds(20, 155, 800, 440);
        scrollPane.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane);
        
        // Setup search
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filterHistory(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filterHistory(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filterHistory(); }
        });
    }
    
    public void loadHistoryData() {
        tableModel.setRowCount(0);
        
        String sql = "SELECT h.history_id, h.trade_id, "
                + "COALESCE(u1.user_fullname, 'Unknown') as trader1_name, "
                + "COALESCE(u2.user_fullname, 'Unknown') as trader2_name, "
                + "COALESCE(i1.item_Name, 'Unknown') as item1_name, "
                + "COALESCE(i2.item_Name, 'Unknown') as item2_name, "
                + "h.trade_DateCompleted "
                + "FROM tbl_trade_history h "
                + "LEFT JOIN tbl_users u1 ON h.offer_trader_id = u1.user_id "
                + "LEFT JOIN tbl_users u2 ON h.target_trader_id = u2.user_id "
                + "LEFT JOIN tbl_items i1 ON h.offer_item_id = i1.items_id "
                + "LEFT JOIN tbl_items i2 ON h.target_item_id = i2.items_id "
                + "ORDER BY h.trade_DateCompleted DESC";
        
        List<Map<String, Object>> histories = db.fetchRecords(sql);
        
        for (Map<String, Object> history : histories) {
            tableModel.addRow(new Object[]{
                history.get("history_id"),
                history.get("trade_id"),
                history.get("trader1_name"),
                history.get("trader2_name"),
                history.get("item1_name"),
                history.get("item2_name"),
                formatDateTime(history.get("trade_DateCompleted"))
            });
        }
        
        rowSorter = new TableRowSorter<>(tableModel);
        historyTable.setRowSorter(rowSorter);
        
        // Update total count
        totalLabel.setText("Total Completed: " + tableModel.getRowCount());
    }
    
    private void filterHistory() {
        String searchText = searchField.getText().trim();
        String filterOption = (String) filterCombo.getSelectedItem();
        
        if (rowSorter != null) {
            RowFilter<DefaultTableModel, Object> searchFilter = null;
            RowFilter<DefaultTableModel, Object> dateFilter = null;
            
            // Search filter
            if (!searchText.isEmpty()) {
                searchFilter = RowFilter.regexFilter("(?i)" + searchText, 2, 3, 4, 5);
            }
            
            // Date filter
            if (filterOption != null && !filterOption.equals("All Trades")) {
                int days = 0;
                if (filterOption.equals("Last 7 Days")) days = 7;
                else if (filterOption.equals("Last 30 Days")) days = 30;
                else if (filterOption.equals("Last 90 Days")) days = 90;
                
                if (days > 0) {
                    final int daysAgo = days;
                    dateFilter = new RowFilter<DefaultTableModel, Object>() {
                        @Override
                        public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                            String dateStr = entry.getStringValue(6);
                            if (dateStr != null && !dateStr.equals("-")) {
                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date tradeDate = sdf.parse(dateStr);
                                    Date cutoffDate = new Date(System.currentTimeMillis() - (daysAgo * 24L * 60L * 60L * 1000L));
                                    return tradeDate.after(cutoffDate);
                                } catch (Exception e) {
                                    return true;
                                }
                            }
                            return true;
                        }
                    };
                }
            }
            
            if (searchFilter != null && dateFilter != null) {
                rowSorter.setRowFilter(RowFilter.andFilter(java.util.Arrays.asList(searchFilter, dateFilter)));
            } else if (searchFilter != null) {
                rowSorter.setRowFilter(searchFilter);
            } else if (dateFilter != null) {
                rowSorter.setRowFilter(dateFilter);
            } else {
                rowSorter.setRowFilter(null);
            }
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
    
    public void refresh() {
        loadHistoryData();
    }
}