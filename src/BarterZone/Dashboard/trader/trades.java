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

    private javax.swing.JButton acceptButton;
    private javax.swing.JButton declineButton;
    private javax.swing.JButton completeButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton messageButton;
    private javax.swing.JButton viewFullGuideButton;
    private javax.swing.JButton manageTradeButton;
    private javax.swing.JButton viewTraderDetailsButton;
    private javax.swing.JButton viewMyDetailsButton;

    private javax.swing.JLabel pendingCountLabel;
    private javax.swing.JLabel activeCountLabel;
    private javax.swing.JLabel completedCountLabel;

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

        loadAndResizeIcons();

        setActivePanel(paneltrades);

        setupCustomComponents();
        loadAllData();

        setupSidebarHoverEffects();

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
                } else if (panel == panelfinditems) {
                    openFindItems();
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

        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(null);
        summaryPanel.setBackground(new Color(245, 245, 245));
        summaryPanel.setBorder(new LineBorder(new Color(12, 192, 223), 2));
        summaryPanel.setBounds(10, 10, 600, 70);

        JPanel pendingCard = new JPanel();
        pendingCard.setLayout(null);
        pendingCard.setBackground(new Color(255, 153, 0));
        pendingCard.setBounds(10, 10, 130, 50);
        pendingCard.setBorder(new LineBorder(Color.WHITE, 2));

        javax.swing.JLabel pendingTitle = new javax.swing.JLabel("PENDING");
        pendingTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pendingTitle.setForeground(Color.WHITE);
        pendingTitle.setBounds(10, 5, 100, 20);
        pendingCard.add(pendingTitle);

        pendingCountLabel = new javax.swing.JLabel("0");
        pendingCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        pendingCountLabel.setForeground(Color.WHITE);
        pendingCountLabel.setBounds(90, 15, 40, 30);
        pendingCard.add(pendingCountLabel);
        summaryPanel.add(pendingCard);

        JPanel activeCard = new JPanel();
        activeCard.setLayout(null);
        activeCard.setBackground(new Color(0, 102, 102));
        activeCard.setBounds(150, 10, 130, 50);
        activeCard.setBorder(new LineBorder(Color.WHITE, 2));

        javax.swing.JLabel activeTitle = new javax.swing.JLabel("ACTIVE");
        activeTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        activeTitle.setForeground(Color.WHITE);
        activeTitle.setBounds(10, 5, 100, 20);
        activeCard.add(activeTitle);

        activeCountLabel = new javax.swing.JLabel("0");
        activeCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        activeCountLabel.setForeground(Color.WHITE);
        activeCountLabel.setBounds(90, 15, 40, 30);
        activeCard.add(activeCountLabel);
        summaryPanel.add(activeCard);

        JPanel completedCard = new JPanel();
        completedCard.setLayout(null);
        completedCard.setBackground(new Color(46, 125, 50));
        completedCard.setBounds(290, 10, 130, 50);
        completedCard.setBorder(new LineBorder(Color.WHITE, 2));

        javax.swing.JLabel completedTitle = new javax.swing.JLabel("COMPLETED");
        completedTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        completedTitle.setForeground(Color.WHITE);
        completedTitle.setBounds(10, 5, 100, 20);
        completedCard.add(completedTitle);

        completedCountLabel = new javax.swing.JLabel("0");
        completedCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        completedCountLabel.setForeground(Color.WHITE);
        completedCountLabel.setBounds(90, 15, 40, 30);
        completedCard.add(completedCountLabel);
        summaryPanel.add(completedCard);

        JPanel myItemsCard = new JPanel();
        myItemsCard.setLayout(null);
        myItemsCard.setBackground(new Color(12, 192, 223));
        myItemsCard.setBounds(430, 10, 160, 50);
        myItemsCard.setBorder(new LineBorder(Color.WHITE, 2));
        myItemsCard.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        java.awt.event.MouseAdapter myItemsAdapter = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openMyItems();
            }
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                myItemsCard.setBackground(hoverColor);
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                myItemsCard.setBackground(new Color(12, 192, 223));
            }
        };
        
        myItemsCard.addMouseListener(myItemsAdapter);

        javax.swing.JLabel myItemsTitle = new javax.swing.JLabel("MY ITEMS →");
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
        tabbedPane.setForeground(new Color(0, 102, 102));

        JPanel availablePanel = new JPanel();
        availablePanel.setLayout(null);
        availablePanel.setBackground(Color.WHITE);

        setupAvailableTable();
        availableScrollPane = new JScrollPane(availableTable);
        availableScrollPane.setBounds(10, 10, 580, 210);
        availableScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        availableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        availablePanel.add(availableScrollPane);

        javax.swing.JButton requestTradeButton = new javax.swing.JButton("REQUEST TRADE");
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
        pendingScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        pendingPanel.add(pendingScrollPane);

        acceptButton = new javax.swing.JButton("✓ ACCEPT");
        acceptButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        acceptButton.setBackground(new Color(46, 125, 50));
        acceptButton.setForeground(Color.WHITE);
        acceptButton.setBounds(150, 230, 100, 35);
        acceptButton.setBorder(null);
        acceptButton.setFocusPainted(false);
        acceptButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        acceptButton.setEnabled(false);
        acceptButton.addActionListener(e -> acceptTrade());
        pendingPanel.add(acceptButton);

        declineButton = new javax.swing.JButton("✗ DECLINE");
        declineButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        declineButton.setBackground(new Color(204, 0, 0));
        declineButton.setForeground(Color.WHITE);
        declineButton.setBounds(260, 230, 100, 35);
        declineButton.setBorder(null);
        declineButton.setFocusPainted(false);
        declineButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        declineButton.setEnabled(false);
        declineButton.addActionListener(e -> declineTrade());
        pendingPanel.add(declineButton);

        messageButton = new javax.swing.JButton("💬 MESSAGE");
        messageButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        messageButton.setBackground(new Color(0, 102, 102));
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
        activeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        activeMainPanel.add(activeScrollPane);

        instructionsPanel = new JPanel();
        instructionsPanel.setLayout(null);
        instructionsPanel.setBackground(new Color(245, 245, 245));
        instructionsPanel.setBorder(new LineBorder(new Color(12, 192, 223), 2));
        instructionsPanel.setBounds(370, 10, 220, 210);

        javax.swing.JLabel instructionsTitle = new javax.swing.JLabel("MANAGE TRADE");
        instructionsTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        instructionsTitle.setForeground(new Color(0, 102, 102));
        instructionsTitle.setBounds(10, 5, 150, 20);
        instructionsPanel.add(instructionsTitle);

        selectedTradeInfoLabel = new javax.swing.JLabel("Select a trade");
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
        instructionsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        instructionsPanel.add(instructionsScrollPane);

        viewFullGuideButton = new javax.swing.JButton("VIEW FULL GUIDE");
        viewFullGuideButton.setFont(new Font("Segoe UI", Font.BOLD, 10));
        viewFullGuideButton.setBackground(new Color(12, 192, 223));
        viewFullGuideButton.setForeground(Color.WHITE);
        viewFullGuideButton.setBounds(55, 170, 110, 20);
        viewFullGuideButton.setBorder(null);
        viewFullGuideButton.setFocusPainted(false);
        viewFullGuideButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewFullGuideButton.setEnabled(false);
        viewFullGuideButton.addActionListener(e -> showFullGuide());
        instructionsPanel.add(viewFullGuideButton);

        activeMainPanel.add(instructionsPanel);

        manageTradeButton = new javax.swing.JButton("MANAGE TRADE");
        manageTradeButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        manageTradeButton.setBackground(new Color(0, 102, 102));
        manageTradeButton.setForeground(Color.WHITE);
        manageTradeButton.setBounds(100, 230, 130, 30);
        manageTradeButton.setBorder(null);
        manageTradeButton.setFocusPainted(false);
        manageTradeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        manageTradeButton.setEnabled(false);
        manageTradeButton.addActionListener(e -> openManageTrade());
        activeMainPanel.add(manageTradeButton);

        viewTraderDetailsButton = new javax.swing.JButton("VIEW TRADER");
        viewTraderDetailsButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        viewTraderDetailsButton.setBackground(new Color(255, 153, 0));
        viewTraderDetailsButton.setForeground(Color.WHITE);
        viewTraderDetailsButton.setBounds(240, 230, 110, 30);
        viewTraderDetailsButton.setBorder(null);
        viewTraderDetailsButton.setFocusPainted(false);
        viewTraderDetailsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewTraderDetailsButton.setEnabled(false);
        viewTraderDetailsButton.addActionListener(e -> viewTraderDetails());
        activeMainPanel.add(viewTraderDetailsButton);

        viewMyDetailsButton = new javax.swing.JButton("MY DETAILS");
        viewMyDetailsButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        viewMyDetailsButton.setBackground(new Color(46, 125, 50));
        viewMyDetailsButton.setForeground(Color.WHITE);
        viewMyDetailsButton.setBounds(360, 230, 100, 30);
        viewMyDetailsButton.setBorder(null);
        viewMyDetailsButton.setFocusPainted(false);
        viewMyDetailsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewMyDetailsButton.setEnabled(false);
        viewMyDetailsButton.addActionListener(e -> viewMyDetails());
        activeMainPanel.add(viewMyDetailsButton);

        completeButton = new javax.swing.JButton("✓ COMPLETE");
        completeButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        completeButton.setBackground(new Color(46, 125, 50));
        completeButton.setForeground(Color.WHITE);
        completeButton.setBounds(470, 230, 110, 30);
        completeButton.setBorder(null);
        completeButton.setFocusPainted(false);
        completeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        completeButton.setEnabled(false);
        completeButton.addActionListener(e -> completeTrade());
        activeMainPanel.add(completeButton);

        tabbedPane.addTab("Active Trades", activeMainPanel);

        JPanel historyPanel = new JPanel();
        historyPanel.setLayout(null);
        historyPanel.setBackground(Color.WHITE);

        setupCompletedTable();
        completedScrollPane = new JScrollPane(completedTable);
        completedScrollPane.setBounds(10, 10, 580, 260);
        completedScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        completedScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        historyPanel.add(completedScrollPane);

        tabbedPane.addTab("History", historyPanel);

        contentPanel.add(summaryPanel);
        contentPanel.add(tabbedPane);

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
                    manageTradeButton.setEnabled(hasSelection);
                    viewTraderDetailsButton.setEnabled(hasSelection);
                    viewMyDetailsButton.setEnabled(hasSelection);
                    viewFullGuideButton.setEnabled(hasSelection);
                    completeButton.setEnabled(hasSelection);

                    if (hasSelection) {
                        int modelRow = activeTable.convertRowIndexToModel(activeTable.getSelectedRow());
                        displayTradeInfo(modelRow);
                    } else {
                        selectedTradeInfoLabel.setText("Select a trade");
                        instructionsArea.setText("Select a trade to see options.");
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
                + "    WHERE trade_status IN ('accepted', 'completed', 'negotiating', 'arrangements_confirmed') "
                + "    UNION "
                + "    SELECT DISTINCT offer_item_id FROM tbl_trade "
                + "    WHERE trade_status IN ('accepted', 'completed', 'negotiating', 'arrangements_confirmed')"
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
        int tradeId = Integer.parseInt(activeTableModel.getValueAt(modelRow, 6).toString());
        String theirItem = activeTableModel.getValueAt(modelRow, 1).toString();
        String owner = activeTableModel.getValueAt(modelRow, 2).toString();
        String myItem = activeTableModel.getValueAt(modelRow, 3).toString();

        selectedTradeInfoLabel.setText("Trade #" + tradeId);

        String info = "TRADE DETAILS\n"
                + "============\n\n"
                + "Your Item: " + myItem + "\n"
                + "Their Item: " + theirItem + "\n"
                + "Trading With: " + owner + "\n\n"
                + "OPTIONS AVAILABLE:\n"
                + "• Manage Trade - Set exchange method and details\n"
                + "• View Trader - See trader's profile\n"
                + "• My Details - View your contact information\n"
                + "• View Full Guide - Complete step-by-step process";

        instructionsArea.setText(info);
    }

    private void showFullGuide() {
        int selectedRow = activeTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        int modelRow = activeTable.convertRowIndexToModel(selectedRow);
        int tradeId = Integer.parseInt(activeTableModel.getValueAt(modelRow, 6).toString());

        String guide = "TRADE PROCESS GUIDE\n"
                + "===================\n\n"
                + "STEP 1: Propose Exchange Method\n"
                + "--------------------------------\n"
                + "• Click 'Manage Trade' button\n"
                + "• Choose between Delivery or Meetup\n"
                + "• Both traders must agree on the method\n\n"
                + "STEP 2: Exchange Details\n"
                + "------------------------\n"
                + "• If Delivery: Enter address, courier, tracking\n"
                + "• If Meetup: Enter location, date, time, contact info\n"
                + "• Both traders can see the details\n\n"
                + "STEP 3: Review & Confirm\n"
                + "------------------------\n"
                + "• Review all exchange details\n"
                + "• Click 'Confirm' when both are ready\n"
                + "• Admin can assist if needed\n\n"
                + "STEP 4: Item Receipt\n"
                + "--------------------\n"
                + "• Confirm when you have received the item\n"
                + "• Wait for the other trader to confirm\n\n"
                + "STEP 5: Complete Trade\n"
                + "----------------------\n"
                + "• When both have received items\n"
                + "• Click 'Complete' to finalize\n"
                + "• Trade moves to History\n\n"
                + "NEED HELP?\n"
                + "----------\n"
                + "• Contact admin for disputes\n"
                + "• Use Messages to communicate\n"
                + "• Admin can mediate if issues arise";

        JOptionPane.showMessageDialog(this, guide,
                "Trade Process Guide - Trade #" + tradeId,
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void viewTraderDetails() {
        int selectedRow = activeTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        int modelRow = activeTable.convertRowIndexToModel(selectedRow);
        String traderName = activeTableModel.getValueAt(modelRow, 2).toString();

        String sql = "SELECT user_fullname, user_username, user_email, user_status FROM tbl_users WHERE user_fullname = ?";
        List<Map<String, Object>> traders = db.fetchRecords(sql, traderName);

        if (!traders.isEmpty()) {
            Map<String, Object> trader = traders.get(0);
            
            String details = "TRADER DETAILS\n"
                    + "==============\n\n"
                    + "Name: " + trader.get("user_fullname") + "\n"
                    + "Username: " + trader.get("user_username") + "\n"
                    + "Email: " + trader.get("user_email") + "\n"
                    + "Status: " + trader.get("user_status");

            JOptionPane.showMessageDialog(this, details,
                    "Trader Information",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void viewMyDetails() {
        String sql = "SELECT user_fullname, user_username, user_email, user_status FROM tbl_users WHERE user_id = ?";
        List<Map<String, Object>> users = db.fetchRecords(sql, traderId);

        if (!users.isEmpty()) {
            Map<String, Object> user = users.get(0);
            
            String details = "YOUR DETAILS\n"
                    + "============\n\n"
                    + "Name: " + user.get("user_fullname") + "\n"
                    + "Username: " + user.get("user_username") + "\n"
                    + "Email: " + user.get("user_email") + "\n"
                    + "Status: " + user.get("user_status");

            JOptionPane.showMessageDialog(this, details,
                    "My Information",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void openManageTrade() {
        int selectedRow = activeTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        int modelRow = activeTable.convertRowIndexToModel(selectedRow);
        int tradeId = Integer.parseInt(activeTableModel.getValueAt(modelRow, 6).toString());
        String theirItem = activeTableModel.getValueAt(modelRow, 1).toString();
        String trader = activeTableModel.getValueAt(modelRow, 2).toString();
        String myItem = activeTableModel.getValueAt(modelRow, 3).toString();

        JOptionPane.showMessageDialog(this,
                "Manage Trade feature coming soon!\n\n"
                + "Trade ID: " + tradeId + "\n"
                + "Your Item: " + myItem + "\n"
                + "Their Item: " + theirItem + "\n"
                + "Trading With: " + trader,
                "Manage Trade",
                JOptionPane.INFORMATION_MESSAGE);
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
                + "    WHERE trade_status IN ('accepted', 'completed', 'negotiating', 'arrangements_confirmed') "
                + "    UNION "
                + "    SELECT DISTINCT offer_item_id FROM tbl_trade "
                + "    WHERE trade_status IN ('accepted', 'completed', 'negotiating', 'arrangements_confirmed')"
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

        javax.swing.JDialog tradeDialog = new javax.swing.JDialog(this, "Request Trade", true);
        tradeDialog.setSize(400, 300);
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
        sendButton.setBounds(80, 200, 150, 35);
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

        javax.swing.JButton cancelButton = new javax.swing.JButton("CANCEL");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setBackground(new Color(204, 0, 0));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBounds(240, 200, 100, 35);
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
                + "VALUES (?, ?, ?, ?, ?, datetime('now'))";

        try {
            db.addRecord(insertSql,
                    traderId,
                    targetOwnerId,
                    offerItemId,
                    targetItemId,
                    "pending"
            );

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
                    "Trade accepted!\n\n"
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
            String sql = "DELETE FROM tbl_trade WHERE trade_id = ? AND trade_status = 'pending'";
            db.deleteRecord(sql, tradeId);

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
            String getSql = "SELECT * FROM tbl_trade WHERE trade_id = ?";
            List<Map<String, Object>> trade = db.fetchRecords(getSql, tradeId);

            if (!trade.isEmpty()) {
                Map<String, Object> t = trade.get(0);

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

                String deleteSql = "DELETE FROM tbl_trade WHERE trade_id = ?";
                db.deleteRecord(deleteSql, tradeId);

                JOptionPane.showMessageDialog(this,
                        "Trade completed successfully!\n\n"
                        + "The trade has been moved to History.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                loadAllData();
            }
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

        messages messagesFrame = new messages(traderId, traderName);
        messagesFrame.setVisible(true);
        messagesFrame.setLocationRelativeTo(null);
        this.dispose();
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

    private void openFindItems() {
        finditems findItemsFrame = new finditems(traderId, traderName);
        findItemsFrame.setVisible(true);
        findItemsFrame.setLocationRelativeTo(null);
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
        profile profileFrame = new profile();
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
}