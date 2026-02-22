package BarterZone.Dashboard.trader;

import BarterZone.resources.IconManager;
import database.config.config;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Cursor;
import java.io.File;
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
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class finditems extends javax.swing.JFrame {

    private int traderId;
    private String traderName;
    private config db;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private IconManager iconManager;

    private javax.swing.JTextField searchField;
    private javax.swing.JComboBox<String> searchCategoryCombo;
    private javax.swing.JPanel detailsPanel;
    private javax.swing.JLabel itemImageLabel;
    private javax.swing.JLabel itemNameLabel;
    private javax.swing.JLabel itemBrandLabel;
    private javax.swing.JLabel itemConditionLabel;
    private javax.swing.JLabel itemDateLabel;
    private javax.swing.JTextArea itemDescriptionArea;
    private javax.swing.JScrollPane descScrollPane;
    private javax.swing.JLabel ownerNameLabel;
    private javax.swing.JButton messageButton;
    private javax.swing.JButton tradeRequestButton;
    private javax.swing.JLabel selectedStatusLabel;

    private JScrollPane tableScrollPane;
    private JScrollPane detailsScrollPane;

    private int selectedItemId = -1;
    private int selectedItemOwnerId = -1;
    private String selectedItemOwnerName = "";
    private String selectedItemImagePath = "";
    private int lastSelectedRow = -1;

    private java.util.List<Map<String, Object>> traderOwnItems;
    private javax.swing.JComboBox<String> traderItemsCombo;
    private int selectedOwnItemId = -1;
    private String selectedOwnItemName = "";

    private Color hoverColor = new Color(70, 210, 235);
    private Color defaultColor = new Color(12, 192, 223);
    private Color activeColor = new Color(0, 150, 180);
    private JPanel activePanel = null;

    public finditems(int traderId, String traderName) {
        this.traderId = traderId;
        this.traderName = traderName;
        this.db = new config();
        this.iconManager = IconManager.getInstance();
        initComponents();

        loadAndResizeIcons();

        setActivePanel(panelfinditems);

        setupCustomComponents();
        loadAllItems();
        loadTraderOwnItems();
        setupLiveSearch();

        setupSidebarHoverEffects();

        setTitle("Find Items - " + traderName);
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
        applyHoverEffectToPanelAndLabel(paneldashboard, dashboard);
        applyHoverEffectToPanelAndLabel(panelmyitems, myitems);
        applyHoverEffectToPanelAndLabel(panelfinditems, finditems);
        applyHoverEffectToPanelAndLabel(paneltrades, trades);
        applyHoverEffectToPanelAndLabel(panelmessages, messages);
        applyHoverEffectToPanelAndLabel(panelreports, Reports);
        applyHoverEffectToPanelAndLabel(panelprofile, Profile);
        applyHoverEffectToPanelAndLabel(panellogout, logout);
    }

    private void applyHoverEffectToPanelAndLabel(JPanel panel, javax.swing.JLabel label) {
        java.awt.event.MouseAdapter adapter = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setHover(panel);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setDefault(panel);
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (panel == paneldashboard) {
                    openDashboard();
                } else if (panel == panelmyitems) {
                    openMyItems();
                } else if (panel == paneltrades) {
                    openTrades();
                } else if (panel == panelmessages) {
                    openMessages();
                } else if (panel == panelreports) {
                    openReports();
                } else if (panel == panelprofile) {
                    openProfile();
                } else if (panel == panellogout) {
                    logout();
                }
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

    private void loadTraderOwnItems() {
        String sql = "SELECT i.items_id, i.item_Name, i.item_Brand, i.item_Condition "
                + "FROM tbl_items i "
                + "WHERE i.trader_id = ? AND i.is_active = 1 "
                + "AND i.items_id NOT IN ("
                + "    SELECT DISTINCT offer_item_id FROM tbl_trade WHERE trade_status IN ('accepted', 'completed', 'negotiating', 'arrangements_confirmed') "
                + "    UNION "
                + "    SELECT DISTINCT target_item_id FROM tbl_trade WHERE trade_status IN ('accepted', 'completed', 'negotiating', 'arrangements_confirmed')"
                + ") "
                + "ORDER BY i.item_Name ASC";

        traderOwnItems = db.fetchRecords(sql, traderId);

        if (traderItemsCombo != null) {
            traderItemsCombo.removeAllItems();
            traderItemsCombo.addItem("-- Select your item to trade --");

            for (Map<String, Object> item : traderOwnItems) {
                String itemDisplay = item.get("item_Name") + " (" + item.get("item_Brand") + ")";
                traderItemsCombo.addItem(itemDisplay);
            }
        }
    }

    private void setupCustomComponents() {
        username.setText(traderName);
        if (traderName != null && traderName.length() > 0) {
            avatarletter.setText(String.valueOf(traderName.charAt(0)).toUpperCase());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        CurrentDate.setText(sdf.format(new Date()));

        jPanel2.removeAll();
        jPanel2.setLayout(null);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBounds(0, 0, 620, 450);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(null);
        searchPanel.setBackground(new Color(245, 245, 245));
        searchPanel.setBorder(new LineBorder(new Color(12, 192, 223), 2));
        searchPanel.setBounds(10, 10, 600, 70);

        javax.swing.JLabel searchLabel = new javax.swing.JLabel("Search Items:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchLabel.setForeground(new Color(0, 102, 102));
        searchLabel.setBounds(15, 10, 120, 25);
        searchPanel.add(searchLabel);

        searchField = new javax.swing.JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBounds(15, 35, 350, 30);
        searchField.setBorder(new LineBorder(new Color(12, 192, 223)));
        searchPanel.add(searchField);

        String[] categories = {"All Fields", "Item Name", "Brand", "Condition", "Owner"};
        searchCategoryCombo = new javax.swing.JComboBox<>(categories);
        searchCategoryCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchCategoryCombo.setBounds(380, 35, 150, 30);
        searchCategoryCombo.setBackground(Color.WHITE);
        searchCategoryCombo.setBorder(new LineBorder(new Color(12, 192, 223)));
        searchCategoryCombo.addActionListener(e -> performLiveSearch());
        searchPanel.add(searchCategoryCombo);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(null);
        leftPanel.setBackground(new Color(245, 245, 245));
        leftPanel.setBorder(new LineBorder(new Color(12, 192, 223), 2));
        leftPanel.setBounds(10, 90, 300, 350);

        javax.swing.JLabel tableTitle = new javax.swing.JLabel("Available Items");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableTitle.setForeground(new Color(0, 102, 102));
        tableTitle.setBounds(10, 5, 200, 20);
        leftPanel.add(tableTitle);

        setupTable();

        tableScrollPane = new JScrollPane(myitemstable);
        tableScrollPane.setBounds(10, 30, 280, 310);
        tableScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        leftPanel.add(tableScrollPane);

        detailsPanel = new JPanel();
        detailsPanel.setLayout(null);
        detailsPanel.setBackground(new Color(245, 245, 245));
        detailsPanel.setBorder(new LineBorder(new Color(12, 192, 223), 2));
        detailsPanel.setBounds(320, 90, 290, 350);

        javax.swing.JLabel detailsTitle = new javax.swing.JLabel("Item Details");
        detailsTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        detailsTitle.setForeground(new Color(0, 102, 102));
        detailsTitle.setBounds(10, 5, 200, 20);
        detailsPanel.add(detailsTitle);

        selectedStatusLabel = new javax.swing.JLabel("No item selected");
        selectedStatusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        selectedStatusLabel.setForeground(new Color(102, 102, 102));
        selectedStatusLabel.setBounds(10, 25, 270, 15);
        detailsPanel.add(selectedStatusLabel);

        JPanel detailsContentPanel = new JPanel();
        detailsContentPanel.setLayout(null);
        detailsContentPanel.setBackground(new Color(245, 245, 245));
        detailsContentPanel.setPreferredSize(new java.awt.Dimension(260, 550));

        itemImageLabel = new javax.swing.JLabel();
        itemImageLabel.setBounds(30, 10, 200, 120);
        itemImageLabel.setBorder(new LineBorder(new Color(12, 192, 223), 2));
        itemImageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        itemImageLabel.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        itemImageLabel.setText("No Image");
        itemImageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        itemImageLabel.setBackground(Color.WHITE);
        itemImageLabel.setOpaque(true);
        detailsContentPanel.add(itemImageLabel);

        int y = 140;
        int labelWidth = 70;
        int valueWidth = 150;
        int labelX = 20;
        int valueX = 90;

        javax.swing.JLabel ownerTitle = new javax.swing.JLabel("Owner:");
        ownerTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        ownerTitle.setBounds(labelX, y, 50, 20);
        detailsContentPanel.add(ownerTitle);

        ownerNameLabel = new javax.swing.JLabel("-");
        ownerNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        ownerNameLabel.setForeground(new Color(0, 102, 102));
        ownerNameLabel.setBounds(valueX, y, valueWidth, 20);
        detailsContentPanel.add(ownerNameLabel);
        y += 25;

        javax.swing.JLabel nameTitle = new javax.swing.JLabel("Name:");
        nameTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nameTitle.setBounds(labelX, y, 50, 20);
        detailsContentPanel.add(nameTitle);

        itemNameLabel = new javax.swing.JLabel("-");
        itemNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        itemNameLabel.setBounds(valueX, y, valueWidth, 20);
        detailsContentPanel.add(itemNameLabel);
        y += 25;

        javax.swing.JLabel brandTitle = new javax.swing.JLabel("Brand:");
        brandTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        brandTitle.setBounds(labelX, y, 50, 20);
        detailsContentPanel.add(brandTitle);

        itemBrandLabel = new javax.swing.JLabel("-");
        itemBrandLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        itemBrandLabel.setBounds(valueX, y, valueWidth, 20);
        detailsContentPanel.add(itemBrandLabel);
        y += 25;

        javax.swing.JLabel conditionTitle = new javax.swing.JLabel("Condition:");
        conditionTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        conditionTitle.setBounds(labelX, y, 70, 20);
        detailsContentPanel.add(conditionTitle);

        itemConditionLabel = new javax.swing.JLabel("-");
        itemConditionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        itemConditionLabel.setBounds(valueX, y, valueWidth, 20);
        detailsContentPanel.add(itemConditionLabel);
        y += 25;

        javax.swing.JLabel dateTitle = new javax.swing.JLabel("Date:");
        dateTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        dateTitle.setBounds(labelX, y, 50, 20);
        detailsContentPanel.add(dateTitle);

        itemDateLabel = new javax.swing.JLabel("-");
        itemDateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        itemDateLabel.setBounds(valueX, y, valueWidth, 20);
        detailsContentPanel.add(itemDateLabel);
        y += 25;

        javax.swing.JLabel descTitle = new javax.swing.JLabel("Description:");
        descTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        descTitle.setBounds(labelX, y, 80, 20);
        detailsContentPanel.add(descTitle);

        itemDescriptionArea = new javax.swing.JTextArea();
        itemDescriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        itemDescriptionArea.setLineWrap(true);
        itemDescriptionArea.setWrapStyleWord(true);
        itemDescriptionArea.setEditable(false);
        itemDescriptionArea.setBackground(new Color(245, 245, 245));
        itemDescriptionArea.setText("-");

        descScrollPane = new javax.swing.JScrollPane(itemDescriptionArea);
        descScrollPane.setBounds(valueX, y - 2, valueWidth, 50);
        descScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        descScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        detailsContentPanel.add(descScrollPane);
        y += 60;

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBackground(new Color(12, 192, 223));
        buttonPanel.setBounds(20, y, 230, 50);
        buttonPanel.setBorder(new LineBorder(new Color(0, 102, 102), 2));

        javax.swing.JLabel actionLabel = new javax.swing.JLabel("ACTIONS");
        actionLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        actionLabel.setForeground(Color.WHITE);
        actionLabel.setBounds(10, 5, 100, 15);
        buttonPanel.add(actionLabel);

        messageButton = new javax.swing.JButton("MESSAGE");
        messageButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        messageButton.setBackground(new Color(0, 102, 102));
        messageButton.setForeground(Color.WHITE);
        messageButton.setBounds(10, 22, 100, 23);
        messageButton.setBorder(new LineBorder(Color.WHITE, 1));
        messageButton.setFocusPainted(false);
        messageButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        messageButton.setEnabled(false);
        messageButton.addActionListener(e -> sendMessage());
        buttonPanel.add(messageButton);

        tradeRequestButton = new javax.swing.JButton("TRADE");
        tradeRequestButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tradeRequestButton.setBackground(new Color(255, 140, 0));
        tradeRequestButton.setForeground(Color.WHITE);
        tradeRequestButton.setBounds(120, 22, 100, 23);
        tradeRequestButton.setBorder(new LineBorder(Color.WHITE, 1));
        tradeRequestButton.setFocusPainted(false);
        tradeRequestButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tradeRequestButton.setEnabled(false);
        tradeRequestButton.addActionListener(e -> sendTradeRequest());
        buttonPanel.add(tradeRequestButton);

        detailsContentPanel.add(buttonPanel);

        detailsScrollPane = new JScrollPane(detailsContentPanel);
        detailsScrollPane.setBounds(5, 45, 280, 300);
        detailsScrollPane.setBorder(null);
        detailsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        detailsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        detailsPanel.add(detailsScrollPane);

        contentPanel.add(searchPanel);
        contentPanel.add(leftPanel);
        contentPanel.add(detailsPanel);

        jPanel2.add(contentPanel);
        contentPanel.setBounds(0, 0, 620, 450);

        jPanel2.revalidate();
        jPanel2.repaint();
    }

    private void setupTable() {
        String[] columns = {"ID", "Item Name", "Brand", "Condition", "Owner", "Owner ID", "Photo"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        myitemstable.setModel(tableModel);
        myitemstable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        myitemstable.setRowHeight(60);
        myitemstable.setShowGrid(true);
        myitemstable.setGridColor(new Color(12, 192, 223));
        myitemstable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        myitemstable.getTableHeader().setBackground(new Color(12, 192, 223));
        myitemstable.getTableHeader().setForeground(Color.WHITE);
        myitemstable.getTableHeader().setBorder(null);
        myitemstable.setSelectionBackground(new Color(184, 239, 255));
        myitemstable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        myitemstable.getColumnModel().getColumn(0).setMinWidth(0);
        myitemstable.getColumnModel().getColumn(0).setMaxWidth(0);
        myitemstable.getColumnModel().getColumn(0).setWidth(0);

        myitemstable.getColumnModel().getColumn(5).setMinWidth(0);
        myitemstable.getColumnModel().getColumn(5).setMaxWidth(0);
        myitemstable.getColumnModel().getColumn(5).setWidth(0);

        myitemstable.getColumnModel().getColumn(6).setCellRenderer(new ImageRenderer());
        myitemstable.getColumnModel().getColumn(6).setPreferredWidth(60);
        myitemstable.getColumnModel().getColumn(6).setMinWidth(60);
        myitemstable.getColumnModel().getColumn(6).setMaxWidth(60);

        TableColumn col1 = myitemstable.getColumnModel().getColumn(1);
        col1.setPreferredWidth(100);
        col1.setMinWidth(80);

        TableColumn col2 = myitemstable.getColumnModel().getColumn(2);
        col2.setPreferredWidth(80);
        col2.setMinWidth(70);

        TableColumn col3 = myitemstable.getColumnModel().getColumn(3);
        col3.setPreferredWidth(70);
        col3.setMinWidth(60);

        TableColumn col4 = myitemstable.getColumnModel().getColumn(4);
        col4.setPreferredWidth(80);
        col4.setMinWidth(70);

        myitemstable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = myitemstable.getSelectedRow();

                    if (selectedRow == lastSelectedRow && selectedRow != -1) {
                        myitemstable.clearSelection();
                        clearSelection();
                        lastSelectedRow = -1;
                    } else if (selectedRow != -1) {
                        int modelRow = myitemstable.convertRowIndexToModel(selectedRow);
                        displayItemDetails(modelRow);
                        lastSelectedRow = selectedRow;
                    }
                }
            }
        });

        myitemstable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    myitemstable.clearSelection();
                    clearSelection();
                    lastSelectedRow = -1;
                }
            }
        });
    }

    private void clearSelection() {
        selectedItemId = -1;
        selectedItemOwnerId = -1;
        selectedItemOwnerName = "";
        selectedItemImagePath = "";

        ownerNameLabel.setText("-");
        itemNameLabel.setText("-");
        itemBrandLabel.setText("-");
        itemConditionLabel.setText("-");
        itemDateLabel.setText("-");
        itemDescriptionArea.setText("-");
        itemImageLabel.setIcon(null);
        itemImageLabel.setText("No Image");

        selectedStatusLabel.setText("No item selected");
        selectedStatusLabel.setForeground(new Color(102, 102, 102));

        messageButton.setEnabled(false);
        tradeRequestButton.setEnabled(false);
    }

    private void setupLiveSearch() {
        rowSorter = new TableRowSorter<>(tableModel);
        myitemstable.setRowSorter(rowSorter);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                performLiveSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                performLiveSearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                performLiveSearch();
            }
        });
    }

    private void performLiveSearch() {
        String text = searchField.getText().trim();
        String category = searchCategoryCombo.getSelectedItem().toString();

        if (text.isEmpty()) {
            rowSorter.setRowFilter(null);
            return;
        }

        int columnIndex = -1;
        switch (category) {
            case "Item Name":
                columnIndex = 1;
                break;
            case "Brand":
                columnIndex = 2;
                break;
            case "Condition":
                columnIndex = 3;
                break;
            case "Owner":
                columnIndex = 4;
                break;
            default:
                rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 1, 2, 3, 4));
                return;
        }

        if (columnIndex != -1) {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, columnIndex));
        }
    }

    private void loadAllItems() {
        tableModel.setRowCount(0);

        String sql = "SELECT i.items_id, i.item_Name, i.item_Brand, i.item_Condition, "
                + "i.item_Date, i.item_Description, i.item_picture, "
                + "u.user_fullname as owner_name, i.trader_id as owner_id "
                + "FROM tbl_items i "
                + "JOIN tbl_users u ON i.trader_id = u.user_id "
                + "WHERE i.trader_id != ? AND i.is_active = 1 "
                + "ORDER BY i.created_date DESC";

        List<Map<String, Object>> items = db.fetchRecords(sql, traderId);

        for (Map<String, Object> item : items) {
            String photoPath = item.get("item_picture") != null ? item.get("item_picture").toString() : "";

            tableModel.addRow(new Object[]{
                item.get("items_id"),
                item.get("item_Name"),
                item.get("item_Brand"),
                item.get("item_Condition"),
                item.get("owner_name"),
                item.get("owner_id"),
                photoPath
            });
        }

        int rowCount = tableModel.getRowCount();
        if (rowCount > 0) {
            selectedStatusLabel.setText(rowCount + " items available");
        } else {
            selectedStatusLabel.setText("No items available");
        }
    }

    private void displayItemDetails(int modelRow) {
        selectedItemId = Integer.parseInt(tableModel.getValueAt(modelRow, 0).toString());
        selectedItemOwnerName = tableModel.getValueAt(modelRow, 4).toString();
        selectedItemOwnerId = Integer.parseInt(tableModel.getValueAt(modelRow, 5).toString());

        selectedStatusLabel.setText("Selected: " + selectedItemOwnerName + "'s item");
        selectedStatusLabel.setForeground(new Color(0, 102, 102));

        String sql = "SELECT i.*, u.user_fullname as owner_name "
                + "FROM tbl_items i "
                + "JOIN tbl_users u ON i.trader_id = u.user_id "
                + "WHERE i.items_id = ?";

        List<Map<String, Object>> items = db.fetchRecords(sql, selectedItemId);

        if (!items.isEmpty()) {
            Map<String, Object> item = items.get(0);

            ownerNameLabel.setText(selectedItemOwnerName);
            itemNameLabel.setText(item.get("item_Name") != null ? item.get("item_Name").toString() : "-");
            itemBrandLabel.setText(item.get("item_Brand") != null ? item.get("item_Brand").toString() : "-");
            itemConditionLabel.setText(item.get("item_Condition") != null ? item.get("item_Condition").toString() : "-");
            itemDateLabel.setText(item.get("item_Date") != null ? item.get("item_Date").toString() : "-");

            String description = item.get("item_Description") != null ? item.get("item_Description").toString() : "-";
            itemDescriptionArea.setText(description);

            String photoPath = item.get("item_picture") != null ? item.get("item_picture").toString() : "";
            if (!photoPath.isEmpty()) {
                selectedItemImagePath = photoPath;
                displayItemImage(photoPath);
            } else {
                itemImageLabel.setIcon(null);
                itemImageLabel.setText("No Image");
                selectedItemImagePath = "";
            }

            messageButton.setEnabled(true);
            tradeRequestButton.setEnabled(true);
        }
    }

    private void displayItemImage(String imagePath) {
        try {
            String filePath = "src/" + imagePath.replace(".", "/");
            File imageFile = new File(filePath);

            if (imageFile.exists()) {
                ImageIcon icon = new ImageIcon(filePath);
                Image image = icon.getImage().getScaledInstance(190, 110, Image.SCALE_SMOOTH);
                itemImageLabel.setIcon(new ImageIcon(image));
                itemImageLabel.setText("");
            } else {
                itemImageLabel.setIcon(null);
                itemImageLabel.setText("Image Not Found");
            }
        } catch (Exception e) {
            itemImageLabel.setIcon(null);
            itemImageLabel.setText("No Image");
        }
    }

    class ImageRenderer extends javax.swing.table.DefaultTableCellRenderer {

        @Override
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            javax.swing.JLabel label = new javax.swing.JLabel();
            label.setHorizontalAlignment(javax.swing.JLabel.CENTER);
            label.setVerticalAlignment(javax.swing.JLabel.CENTER);

            if (value != null && !value.toString().isEmpty()) {
                try {
                    String imagePath = "src/" + value.toString().replace(".", "/");
                    File imgFile = new File(imagePath);
                    if (imgFile.exists()) {
                        ImageIcon icon = new ImageIcon(imagePath);
                        Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                        label.setIcon(new ImageIcon(img));
                    } else {
                        label.setText("No Img");
                        label.setFont(new Font("Segoe UI", Font.PLAIN, 9));
                    }
                } catch (Exception e) {
                    label.setText("Err");
                }
            } else {
                label.setText("No Img");
                label.setFont(new Font("Segoe UI", Font.PLAIN, 9));
            }

            if (isSelected) {
                label.setBackground(table.getSelectionBackground());
                label.setOpaque(true);
            }

            return label;
        }
    }

    private void sendMessage() {
        if (selectedItemId == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        messages messagesFrame = new messages(traderId, traderName);
        messagesFrame.setVisible(true);
        messagesFrame.setLocationRelativeTo(null);
        this.dispose();
    }

    private void sendTradeRequest() {
        if (selectedItemId == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedItemOwnerId == traderId) {
            JOptionPane.showMessageDialog(this, "You cannot trade with your own item.", "Invalid Trade", JOptionPane.WARNING_MESSAGE);
            return;
        }

        loadTraderOwnItems();

        if (traderOwnItems.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "You don't have any items available for trade.\n\n"
                    + "All your items may have been already traded or are inactive.\n"
                    + "Please add new items in 'My Items' section to start trading.",
                    "No Items Available",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        showTradeRequestDialog();
    }

    private boolean isItemAvailableForTrade(int itemId) {
        String sql = "SELECT COUNT(*) as count FROM tbl_items WHERE items_id = ? AND is_active = 1 "
                + "AND items_id NOT IN ("
                + "    SELECT DISTINCT offer_item_id FROM tbl_trade WHERE trade_status IN ('accepted', 'completed', 'negotiating', 'arrangements_confirmed') "
                + "    UNION "
                + "    SELECT DISTINCT target_item_id FROM tbl_trade WHERE trade_status IN ('accepted', 'completed', 'negotiating', 'arrangements_confirmed')"
                + ")";

        double count = db.getSingleValue(sql, itemId);
        return count > 0;
    }

    private void showTradeRequestDialog() {
        javax.swing.JDialog tradeDialog = new javax.swing.JDialog(this, "Send Trade Request", true);
        tradeDialog.setSize(450, 350);
        tradeDialog.setLayout(null);
        tradeDialog.setLocationRelativeTo(this);
        tradeDialog.getContentPane().setBackground(Color.WHITE);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(12, 192, 223));
        titlePanel.setBounds(0, 0, 450, 40);
        titlePanel.setLayout(null);

        javax.swing.JLabel titleLabel = new javax.swing.JLabel("TRADE REQUEST");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(20, 5, 200, 30);
        titlePanel.add(titleLabel);

        tradeDialog.add(titlePanel);

        javax.swing.JLabel requestedItemLabel = new javax.swing.JLabel("You want to trade for:");
        requestedItemLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        requestedItemLabel.setBounds(20, 60, 200, 20);
        tradeDialog.add(requestedItemLabel);

        javax.swing.JLabel itemInfoLabel = new javax.swing.JLabel(
                "<html><b>Item:</b> " + itemNameLabel.getText()
                + "<br><b>Brand:</b> " + itemBrandLabel.getText()
                + "<br><b>Owner:</b> " + selectedItemOwnerName + "</html>");
        itemInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        itemInfoLabel.setBounds(20, 80, 300, 60);
        itemInfoLabel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        tradeDialog.add(itemInfoLabel);

        javax.swing.JLabel yourItemLabel = new javax.swing.JLabel("Select your item to offer:");
        yourItemLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        yourItemLabel.setBounds(20, 150, 200, 20);
        tradeDialog.add(yourItemLabel);

        loadTraderOwnItems();

        traderItemsCombo = new javax.swing.JComboBox<>();
        traderItemsCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        traderItemsCombo.setBounds(20, 175, 300, 30);
        traderItemsCombo.setBackground(Color.WHITE);
        traderItemsCombo.setBorder(new LineBorder(new Color(12, 192, 223)));

        traderItemsCombo.addItem("-- Select your item to trade --");
        for (Map<String, Object> item : traderOwnItems) {
            String itemDisplay = item.get("item_Name") + " (" + item.get("item_Brand") + ")";
            traderItemsCombo.addItem(itemDisplay);
        }

        tradeDialog.add(traderItemsCombo);

        javax.swing.JLabel messageLabel = new javax.swing.JLabel("Optional message:");
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        messageLabel.setBounds(20, 220, 200, 20);
        tradeDialog.add(messageLabel);

        javax.swing.JTextArea messageArea = new javax.swing.JTextArea();
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);

        javax.swing.JScrollPane messageScroll = new javax.swing.JScrollPane(messageArea);
        messageScroll.setBounds(20, 240, 300, 50);
        messageScroll.setBorder(new LineBorder(new Color(200, 200, 200)));
        tradeDialog.add(messageScroll);

        javax.swing.JButton sendButton = new javax.swing.JButton("SEND REQUEST");
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendButton.setBackground(new Color(0, 102, 102));
        sendButton.setForeground(Color.WHITE);
        sendButton.setBounds(100, 300, 150, 35);
        sendButton.setBorder(null);
        sendButton.setFocusPainted(false);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendButton.addActionListener(e -> {
            if (traderItemsCombo.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(tradeDialog,
                        "Please select an item to trade.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int selectedIndex = traderItemsCombo.getSelectedIndex() - 1;
            if (selectedIndex >= 0 && selectedIndex < traderOwnItems.size()) {
                Map<String, Object> selectedItem = traderOwnItems.get(selectedIndex);
                int offeredItemId = Integer.parseInt(selectedItem.get("items_id").toString());
                String offeredItemName = selectedItem.get("item_Name").toString();

                if (!isItemAvailableForTrade(offeredItemId)) {
                    JOptionPane.showMessageDialog(tradeDialog,
                            "Sorry, the item '" + offeredItemName + "' is no longer available for trade.\n"
                            + "It may have been traded already.",
                            "Item Unavailable",
                            JOptionPane.WARNING_MESSAGE);

                    loadTraderOwnItems();
                    if (traderOwnItems.isEmpty()) {
                        tradeDialog.dispose();
                    }
                    return;
                }

                if (!isItemAvailableForTrade(selectedItemId)) {
                    JOptionPane.showMessageDialog(tradeDialog,
                            "Sorry, the item you requested is no longer available.\n"
                            + "It may have been traded to someone else.",
                            "Item Unavailable",
                            JOptionPane.WARNING_MESSAGE);
                    tradeDialog.dispose();
                    loadAllItems();
                    clearSelection();
                    return;
                }

                createTradeRequest(offeredItemId, offeredItemName, messageArea.getText().trim());
            }

            tradeDialog.dispose();
        });
        tradeDialog.add(sendButton);

        javax.swing.JButton cancelButton = new javax.swing.JButton("CANCEL");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setBackground(new Color(204, 0, 0));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBounds(260, 300, 100, 35);
        cancelButton.setBorder(null);
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> tradeDialog.dispose());
        tradeDialog.add(cancelButton);

        tradeDialog.setVisible(true);
    }

    private void createTradeRequest(int offeredItemId, String offeredItemName, String message) {
        if (!isItemAvailableForTrade(offeredItemId)) {
            JOptionPane.showMessageDialog(this,
                    "Your item '" + offeredItemName + "' is no longer available for trade.\n"
                    + "It may have been traded already.",
                    "Trade Failed",
                    JOptionPane.WARNING_MESSAGE);
            loadTraderOwnItems();
            return;
        }

        if (!isItemAvailableForTrade(selectedItemId)) {
            JOptionPane.showMessageDialog(this,
                    "The item you requested is no longer available.\n"
                    + "It may have been traded to someone else.",
                    "Trade Failed",
                    JOptionPane.WARNING_MESSAGE);
            loadAllItems();
            clearSelection();
            return;
        }

        String sql = "INSERT INTO tbl_trade ("
                + "offer_trader_id, offer_item_id, "
                + "target_trader_id, target_item_id, "
                + "trade_status, trade_DateRequest"
                + ") VALUES (?, ?, ?, ?, ?, datetime('now'))";

        try {
            db.addRecord(sql,
                    traderId, offeredItemId,
                    selectedItemOwnerId, selectedItemId,
                    "pending");

            JOptionPane.showMessageDialog(this,
                    "✅ Trade request sent successfully!\n\n"
                    + "Your item: " + offeredItemName + "\n"
                    + "Requesting: " + itemNameLabel.getText() + " from " + selectedItemOwnerName + "\n\n"
                    + "The owner will review your request.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            loadTraderOwnItems();
            loadAllItems();
            clearSelection();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to send trade request. Please try again.\nError: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

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

    @SuppressWarnings("unchecked")
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
        jScrollPane1 = new javax.swing.JScrollPane();
        myitemstable = new javax.swing.JTable();

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

        paneldashboard.setBackground(new java.awt.Color(12, 192, 223));

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

        panelmyitems.setBackground(new java.awt.Color(12, 192, 223));

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

        panelfinditems.setBackground(new java.awt.Color(12, 192, 223));

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

        paneltrades.setBackground(new java.awt.Color(12, 192, 223));

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

        panelmessages.setBackground(new java.awt.Color(12, 192, 223));

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

        panellogout.setBackground(new java.awt.Color(12, 192, 223));

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

        panelreports.setBackground(new java.awt.Color(12, 192, 223));

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

        panelprofile.setBackground(new java.awt.Color(12, 192, 223));

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

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 2));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30));
        jLabel1.setText("Find Items");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, 30));

        CurrentDate.setFont(new java.awt.Font("Segoe UI", 0, 14));
        CurrentDate.setForeground(new java.awt.Color(102, 102, 102));
        CurrentDate.setText("jLabel2");
        jPanel1.add(CurrentDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 10, 200, 30));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        myitemstable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null}
                },
                new String[]{
                    "ID", "Item Name", "Brand", "Condition", "Owner", "Owner ID", "Photo"
                }
        ));
        jScrollPane1.setViewportView(myitemstable);

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
    }

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
    javax.swing.JScrollPane jScrollPane1;
    javax.swing.JLabel logotext;
    javax.swing.JLabel logout;
    javax.swing.JLabel logouticon;
    javax.swing.JLabel messages;
    javax.swing.JLabel messagesicon;
    javax.swing.JLabel myitems;
    javax.swing.JLabel myitemsicon;
    javax.swing.JTable myitemstable;
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
}