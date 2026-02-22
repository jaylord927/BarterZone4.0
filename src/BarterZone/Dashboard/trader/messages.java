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
import javax.swing.table.TableRowSorter;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class messages extends javax.swing.JFrame {

    private int traderId;
    private String traderName;
    private config db;
    private IconManager iconManager;

    private DefaultTableModel conversationsTableModel;
    private javax.swing.JTable conversationsTable;
    private JScrollPane conversationsScrollPane;

    private DefaultTableModel messagesTableModel;
    private javax.swing.JTable messagesTable;
    private JScrollPane messagesScrollPane;

    private javax.swing.JTextField searchField;
    private javax.swing.JTextField messageInputField;
    private javax.swing.JButton sendButton;
    private javax.swing.JButton newMessageButton;
    private javax.swing.JButton refreshButton;
    private javax.swing.JLabel selectedConversationLabel;
    private javax.swing.JTextArea messagePreviewArea;
    private JScrollPane previewScrollPane;

    private int selectedConversationId = -1;
    private int selectedOtherTraderId = -1;
    private String selectedOtherTraderName = "";

    private Color hoverColor = new Color(70, 210, 235);
    private Color defaultColor = new Color(12, 192, 223);
    private Color activeColor = new Color(0, 150, 180);
    private JPanel activePanel = null;

    public messages(int traderId, String traderName) {
        this.traderId = traderId;
        this.traderName = traderName;
        this.db = new config();
        this.iconManager = IconManager.getInstance();
        initComponents();

        loadAndResizeIcons();

        setActivePanel(panelmessages);

        setupCustomComponents();
        loadConversations();
        setupLiveSearch();

        setupSidebarHoverEffects();

        setTitle("Messages - " + traderName);
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
                } else if (panel == paneltrades) {
                    openTrades();
                } else if (panel == panelmessages) {
                    refreshMessages();
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

        JPanel topPanel = new JPanel();
        topPanel.setLayout(null);
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.setBorder(new LineBorder(new Color(12, 192, 223), 2));
        topPanel.setBounds(10, 10, 600, 60);

        javax.swing.JLabel searchLabel = new javax.swing.JLabel("Search Conversations:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchLabel.setForeground(new Color(0, 102, 102));
        searchLabel.setBounds(15, 10, 180, 25);
        topPanel.add(searchLabel);

        searchField = new javax.swing.JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBounds(15, 35, 300, 25);
        searchField.setBorder(new LineBorder(new Color(12, 192, 223)));
        topPanel.add(searchField);

        newMessageButton = new javax.swing.JButton("New Message");
        newMessageButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        newMessageButton.setBackground(new Color(0, 102, 102));
        newMessageButton.setForeground(Color.WHITE);
        newMessageButton.setBounds(330, 30, 130, 30);
        newMessageButton.setBorder(null);
        newMessageButton.setFocusPainted(false);
        newMessageButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        newMessageButton.addActionListener(e -> showNewMessageDialog());
        topPanel.add(newMessageButton);

        refreshButton = new javax.swing.JButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshButton.setBackground(new Color(12, 192, 223));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBounds(470, 30, 100, 30);
        refreshButton.setBorder(null);
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> refreshMessages());
        topPanel.add(refreshButton);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(null);
        leftPanel.setBackground(new Color(245, 245, 245));
        leftPanel.setBorder(new LineBorder(new Color(12, 192, 223), 2));
        leftPanel.setBounds(10, 80, 250, 270);

        javax.swing.JLabel conversationsTitle = new javax.swing.JLabel("Conversations");
        conversationsTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        conversationsTitle.setForeground(new Color(0, 102, 102));
        conversationsTitle.setBounds(10, 5, 200, 20);
        leftPanel.add(conversationsTitle);

        setupConversationsTable();
        conversationsScrollPane = new JScrollPane(conversationsTable);
        conversationsScrollPane.setBounds(10, 30, 230, 230);
        conversationsScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        conversationsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        leftPanel.add(conversationsScrollPane);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(null);
        rightPanel.setBackground(new Color(245, 245, 245));
        rightPanel.setBorder(new LineBorder(new Color(12, 192, 223), 2));
        rightPanel.setBounds(270, 80, 340, 360);

        selectedConversationLabel = new javax.swing.JLabel("Select a conversation");
        selectedConversationLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        selectedConversationLabel.setForeground(new Color(0, 102, 102));
        selectedConversationLabel.setBounds(10, 5, 300, 20);
        rightPanel.add(selectedConversationLabel);

        setupMessagesTable();
        messagesScrollPane = new JScrollPane(messagesTable);
        messagesScrollPane.setBounds(10, 30, 320, 220);
        messagesScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        messagesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightPanel.add(messagesScrollPane);

        messagePreviewArea = new javax.swing.JTextArea();
        messagePreviewArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        messagePreviewArea.setLineWrap(true);
        messagePreviewArea.setWrapStyleWord(true);
        messagePreviewArea.setEditable(false);
        messagePreviewArea.setBackground(new Color(245, 245, 245));
        previewScrollPane = new JScrollPane(messagePreviewArea);
        previewScrollPane.setBounds(10, 255, 320, 50);
        previewScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        previewScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightPanel.add(previewScrollPane);

        messageInputField = new javax.swing.JTextField();
        messageInputField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageInputField.setBounds(10, 310, 230, 30);
        messageInputField.setBorder(new LineBorder(new Color(12, 192, 223)));
        messageInputField.setEnabled(false);
        rightPanel.add(messageInputField);

        sendButton = new javax.swing.JButton("SEND");
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        sendButton.setBackground(new Color(0, 102, 102));
        sendButton.setForeground(Color.WHITE);
        sendButton.setBounds(250, 310, 80, 30);
        sendButton.setBorder(null);
        sendButton.setFocusPainted(false);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendButton.setEnabled(false);
        sendButton.addActionListener(e -> sendMessage());
        rightPanel.add(sendButton);

        contentPanel.add(topPanel);
        contentPanel.add(leftPanel);
        contentPanel.add(rightPanel);

        jPanel2.add(contentPanel);
        contentPanel.setBounds(0, 0, 620, 450);

        jPanel2.revalidate();
        jPanel2.repaint();
    }

    private void setupConversationsTable() {
        String[] columns = {"Conversation ID", "With", "Last Message", "Date", "Other ID"};
        conversationsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        conversationsTable = new javax.swing.JTable(conversationsTableModel);
        conversationsTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        conversationsTable.setRowHeight(30);
        conversationsTable.setShowGrid(true);
        conversationsTable.setGridColor(new Color(12, 192, 223));
        conversationsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        conversationsTable.getTableHeader().setBackground(new Color(0, 102, 102));
        conversationsTable.getTableHeader().setForeground(Color.WHITE);
        conversationsTable.setSelectionBackground(new Color(184, 239, 255));
        conversationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        conversationsTable.getColumnModel().getColumn(0).setMinWidth(0);
        conversationsTable.getColumnModel().getColumn(0).setMaxWidth(0);
        conversationsTable.getColumnModel().getColumn(0).setWidth(0);

        conversationsTable.getColumnModel().getColumn(4).setMinWidth(0);
        conversationsTable.getColumnModel().getColumn(4).setMaxWidth(0);
        conversationsTable.getColumnModel().getColumn(4).setWidth(0);

        conversationsTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        conversationsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        conversationsTable.getColumnModel().getColumn(3).setPreferredWidth(50);

        conversationsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = conversationsTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int modelRow = conversationsTable.convertRowIndexToModel(selectedRow);
                        loadConversationMessages(modelRow);
                    } else {
                        clearMessagePanel();
                    }
                }
            }
        });
    }

    private void setupMessagesTable() {
        String[] columns = {"ID", "Sender", "Message", "Date", "Sender ID"};
        messagesTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        messagesTable = new javax.swing.JTable(messagesTableModel);
        messagesTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        messagesTable.setRowHeight(25);
        messagesTable.setShowGrid(true);
        messagesTable.setGridColor(new Color(12, 192, 223));
        messagesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        messagesTable.getTableHeader().setBackground(new Color(12, 192, 223));
        messagesTable.getTableHeader().setForeground(Color.WHITE);
        messagesTable.setSelectionBackground(new Color(184, 239, 255));
        messagesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        messagesTable.getColumnModel().getColumn(0).setMinWidth(0);
        messagesTable.getColumnModel().getColumn(0).setMaxWidth(0);
        messagesTable.getColumnModel().getColumn(0).setWidth(0);

        messagesTable.getColumnModel().getColumn(4).setMinWidth(0);
        messagesTable.getColumnModel().getColumn(4).setMaxWidth(0);
        messagesTable.getColumnModel().getColumn(4).setWidth(0);

        messagesTable.getColumnModel().getColumn(1).setPreferredWidth(50);
        messagesTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        messagesTable.getColumnModel().getColumn(3).setPreferredWidth(80);

        messagesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = messagesTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int modelRow = messagesTable.convertRowIndexToModel(selectedRow);
                        String message = messagesTableModel.getValueAt(modelRow, 2).toString();
                        messagePreviewArea.setText(message);
                    }
                }
            }
        });
    }

    private void setupLiveSearch() {
        TableRowSorter<DefaultTableModel> rowSorter = new TableRowSorter<>(conversationsTableModel);
        conversationsTable.setRowSorter(rowSorter);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                performSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                performSearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                performSearch();
            }

            private void performSearch() {
                String text = searchField.getText().trim();
                if (text.isEmpty()) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(javax.swing.RowFilter.regexFilter("(?i)" + text, 1, 2));
                }
            }
        });
    }

    private void loadConversations() {
        conversationsTableModel.setRowCount(0);

        String sql = "WITH conversation_list AS ("
                + "    SELECT DISTINCT "
                + "    CASE "
                + "        WHEN sender_id = ? THEN receiver_id "
                + "        ELSE sender_id "
                + "    END as other_id "
                + "    FROM tbl_trade_messages "
                + "    WHERE sender_id = ? OR receiver_id = ? "
                + ") "
                + "SELECT "
                + "    cl.other_id, "
                + "    u.user_fullname as other_name, "
                + "    (SELECT message_text FROM tbl_trade_messages "
                + "     WHERE (sender_id = ? AND receiver_id = cl.other_id) "
                + "        OR (sender_id = cl.other_id AND receiver_id = ?) "
                + "     ORDER BY message_date DESC LIMIT 1) as last_message, "
                + "    (SELECT message_date FROM tbl_trade_messages "
                + "     WHERE (sender_id = ? AND receiver_id = cl.other_id) "
                + "        OR (sender_id = cl.other_id AND receiver_id = ?) "
                + "     ORDER BY message_date DESC LIMIT 1) as last_date "
                + "FROM conversation_list cl "
                + "JOIN tbl_users u ON cl.other_id = u.user_id "
                + "ORDER BY last_date DESC";

        List<Map<String, Object>> conversations = db.fetchRecords(sql, 
            traderId, traderId, traderId, traderId, traderId, traderId, traderId);

        for (Map<String, Object> conv : conversations) {
            conversationsTableModel.addRow(new Object[]{
                conv.get("other_id"),
                conv.get("other_name"),
                conv.get("last_message") != null ? conv.get("last_message").toString() : "No messages yet",
                formatDate(conv.get("last_date")),
                conv.get("other_id")
            });
        }
    }

    private void loadConversationMessages(int modelRow) {
        selectedOtherTraderId = Integer.parseInt(conversationsTableModel.getValueAt(modelRow, 4).toString());
        selectedOtherTraderName = conversationsTableModel.getValueAt(modelRow, 1).toString();

        selectedConversationLabel.setText("Conversation with: " + selectedOtherTraderName);
        messageInputField.setEnabled(true);
        sendButton.setEnabled(true);

        messagesTableModel.setRowCount(0);

        String sql = "SELECT m.message_id, m.sender_id, m.message_text, m.message_date, "
                + "u.user_fullname as sender_name "
                + "FROM tbl_trade_messages m "
                + "JOIN tbl_users u ON m.sender_id = u.user_id "
                + "WHERE (m.sender_id = ? AND m.receiver_id = ?) "
                + "   OR (m.sender_id = ? AND m.receiver_id = ?) "
                + "ORDER BY m.message_date ASC";

        List<Map<String, Object>> messages = db.fetchRecords(sql, 
            traderId, selectedOtherTraderId, selectedOtherTraderId, traderId);

        for (Map<String, Object> msg : messages) {
            String sender = msg.get("sender_id").toString().equals(String.valueOf(traderId)) ? "You" : msg.get("sender_name").toString();
            messagesTableModel.addRow(new Object[]{
                msg.get("message_id"),
                sender,
                msg.get("message_text"),
                formatDateTime(msg.get("message_date")),
                msg.get("sender_id")
            });
        }

        if (messagesTableModel.getRowCount() > 0) {
            messagesTable.scrollRectToVisible(messagesTable.getCellRect(messagesTableModel.getRowCount() - 1, 0, true));
        }
    }

    private void clearMessagePanel() {
        selectedConversationLabel.setText("Select a conversation");
        messageInputField.setEnabled(false);
        sendButton.setEnabled(false);
        messageInputField.setText("");
        messagePreviewArea.setText("");
        messagesTableModel.setRowCount(0);
    }

    private void sendMessage() {
        String messageText = messageInputField.getText().trim();
        if (messageText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a message.", "Empty Message", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "INSERT INTO tbl_trade_messages (sender_id, receiver_id, message_text, message_date) "
                + "VALUES (?, ?, ?, datetime('now'))";

        try {
            db.addRecord(sql, traderId, selectedOtherTraderId, messageText);
            
            messageInputField.setText("");
            int selectedRow = conversationsTable.getSelectedRow();
            if (selectedRow != -1) {
                int modelRow = conversationsTable.convertRowIndexToModel(selectedRow);
                loadConversationMessages(modelRow);
            }
            loadConversations();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to send message: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showNewMessageDialog() {
        String sql = "SELECT user_id, user_fullname FROM tbl_users WHERE user_id != ? AND user_type = 'trader' AND user_status = 'active' ORDER BY user_fullname";
        List<Map<String, Object>> traders = db.fetchRecords(sql, traderId);

        if (traders.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No other traders available.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] traderNames = new String[traders.size()];
        Integer[] traderIds = new Integer[traders.size()];
        for (int i = 0; i < traders.size(); i++) {
            traderNames[i] = traders.get(i).get("user_fullname").toString();
            traderIds[i] = Integer.parseInt(traders.get(i).get("user_id").toString());
        }

        javax.swing.JDialog dialog = new javax.swing.JDialog(this, "New Message", true);
        dialog.setSize(400, 250);
        dialog.setLayout(null);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Color.WHITE);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(12, 192, 223));
        titlePanel.setBounds(0, 0, 400, 40);
        titlePanel.setLayout(null);

        javax.swing.JLabel titleLabel = new javax.swing.JLabel("NEW MESSAGE");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(20, 5, 200, 30);
        titlePanel.add(titleLabel);
        dialog.add(titlePanel);

        javax.swing.JLabel toLabel = new javax.swing.JLabel("To:");
        toLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        toLabel.setBounds(20, 60, 50, 25);
        dialog.add(toLabel);

        javax.swing.JComboBox<String> traderCombo = new javax.swing.JComboBox<>(traderNames);
        traderCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        traderCombo.setBounds(80, 60, 250, 30);
        traderCombo.setBackground(Color.WHITE);
        dialog.add(traderCombo);

        javax.swing.JLabel messageLabel = new javax.swing.JLabel("Message:");
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        messageLabel.setBounds(20, 100, 80, 25);
        dialog.add(messageLabel);

        javax.swing.JTextArea messageArea = new javax.swing.JTextArea();
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(messageArea);
        scrollPane.setBounds(20, 130, 350, 60);
        scrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        dialog.add(scrollPane);

        javax.swing.JButton sendBtn = new javax.swing.JButton("SEND");
        sendBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendBtn.setBackground(new Color(0, 102, 102));
        sendBtn.setForeground(Color.WHITE);
        sendBtn.setBounds(100, 200, 100, 30);
        sendBtn.setBorder(null);
        sendBtn.setFocusPainted(false);
        sendBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendBtn.addActionListener(e -> {
            int selectedIndex = traderCombo.getSelectedIndex();
            if (selectedIndex >= 0) {
                String msgText = messageArea.getText().trim();
                if (!msgText.isEmpty()) {
                    int receiverId = traderIds[selectedIndex];
                    String receiverName = traderNames[selectedIndex];
                    
                    String insertSql = "INSERT INTO tbl_trade_messages (sender_id, receiver_id, message_text, message_date) "
                            + "VALUES (?, ?, ?, datetime('now'))";
                    
                    try {
                        db.addRecord(insertSql, traderId, receiverId, msgText);
                        JOptionPane.showMessageDialog(dialog, "Message sent to " + receiverName + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        loadConversations();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dialog, "Failed to send message: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(dialog, "Please enter a message.", "Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        dialog.add(sendBtn);

        javax.swing.JButton cancelBtn = new javax.swing.JButton("CANCEL");
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelBtn.setBackground(new Color(204, 0, 0));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setBounds(210, 200, 100, 30);
        cancelBtn.setBorder(null);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.addActionListener(e -> dialog.dispose());
        dialog.add(cancelBtn);

        dialog.setVisible(true);
    }

    private void refreshMessages() {
        loadConversations();
        clearMessagePanel();
        JOptionPane.showMessageDialog(this, "Messages refreshed!", "Refresh", JOptionPane.INFORMATION_MESSAGE);
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

    private void openTrades() {
        trades tradesFrame = new trades(traderId, traderName);
        tradesFrame.setVisible(true);
        tradesFrame.setLocationRelativeTo(null);
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
        jLabel1.setText("Messages");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, 30));

        CurrentDate.setFont(new java.awt.Font("Segoe UI", 0, 14));
        CurrentDate.setForeground(new java.awt.Color(102, 102, 102));
        CurrentDate.setText("jLabel2");
        jPanel1.add(CurrentDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 10, 200, 30));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        myitemstable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
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