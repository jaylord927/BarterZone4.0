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

public class messages extends javax.swing.JFrame {

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
    
    private DefaultTableModel conversationsTableModel;
    private javax.swing.JTable conversationsTable;
    private JScrollPane conversationsScrollPane;

    private DefaultTableModel messagesTableModel;
    private javax.swing.JTable messagesTable;
    private JScrollPane messagesScrollPane;

    private JTextField searchField;
    private JTextField messageInputField;
    private JButton sendButton;
    private JButton newMessageButton;
    private JButton refreshButton;
    private JLabel selectedConversationLabel;
    private JTextArea messagePreviewArea;
    private JScrollPane previewScrollPane;

    private int selectedConversationId = -1;
    private int selectedOtherTraderId = -1;
    private String selectedOtherTraderName = "";

    private Color themeColor = new Color(12, 192, 223);
    private Color hoverColor = new Color(70, 210, 235);
    private Color activeColor = new Color(0, 150, 180);
    private Color headerBgColor = new Color(245, 245, 245);
    private Color textColor = new Color(80, 80, 80);
    private Color accentColor = new Color(0, 102, 102);
    private Color initialColor = new Color(0, 102, 102);
    
    private JPanel activePanel = null;

    public messages(int traderId, String traderName) {
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
        loadConversations();
        setupLiveSearch();
        loadProfileAvatar();

        setTitle("Messages - " + traderName);
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

    private ImageIcon createScaledImageIcon(String imagePath, int width, int height) {
        try {
            File file = new File(imagePath);
            if (!file.exists()) {
                return null;
            }

            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            System.out.println("Error creating scaled image: " + e.getMessage());
            return null;
        }
    }

    private ImageIcon createCircularImageIcon(String imagePath, int width, int height) {
        try {
            File file = new File(imagePath);
            if (!file.exists()) {
                return null;
            }

            ImageIcon originalIcon = new ImageIcon(imagePath);
            Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);

            BufferedImage circularImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = circularImage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setClip(new Ellipse2D.Float(0, 0, width, height));
            g2.drawImage(scaledImage, 0, 0, width, height, null);
            g2.dispose();

            return new ImageIcon(circularImage);
        } catch (Exception e) {
            System.out.println("Error creating circular image: " + e.getMessage());
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
                        ImageIcon circularIcon = createCircularImageIcon(fullPath, 90, 90);

                        if (circularIcon != null) {
                            avatarLabel.setIcon(circularIcon);
                            avatarLabel.setText("");
                            avatarLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                            avatarLabel.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
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
        avatarLabel.setText("");
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

        setActivePanel(messagesPanel);
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
        headerTitle = new JLabel("Messages");
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

        JPanel topPanel = new JPanel();
        topPanel.setLayout(null);
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.setBorder(new LineBorder(new Color(12, 192, 223), 2));
        topPanel.setBounds(10, 10, 600, 60);

        JLabel searchLabel = new JLabel("Search Conversations:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchLabel.setForeground(new Color(0, 102, 102));
        searchLabel.setBounds(15, 10, 180, 25);
        topPanel.add(searchLabel);

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBounds(15, 35, 300, 25);
        searchField.setBorder(new LineBorder(new Color(12, 192, 223)));
        topPanel.add(searchField);

        newMessageButton = new JButton("New Message");
        newMessageButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        newMessageButton.setBackground(new Color(0, 102, 102));
        newMessageButton.setForeground(Color.WHITE);
        newMessageButton.setBounds(330, 30, 130, 30);
        newMessageButton.setBorder(null);
        newMessageButton.setFocusPainted(false);
        newMessageButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        newMessageButton.addActionListener(e -> showNewMessageDialog());
        topPanel.add(newMessageButton);

        refreshButton = new JButton("Refresh");
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

        JLabel conversationsTitle = new JLabel("Conversations");
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
        rightPanel.setBounds(270, 80, 340, 340);

        selectedConversationLabel = new JLabel("Select a conversation");
        selectedConversationLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        selectedConversationLabel.setForeground(new Color(0, 102, 102));
        selectedConversationLabel.setBounds(10, 5, 300, 20);
        rightPanel.add(selectedConversationLabel);

        setupMessagesTable();
        messagesScrollPane = new JScrollPane(messagesTable);
        messagesScrollPane.setBounds(10, 30, 320, 200);
        messagesScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        messagesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightPanel.add(messagesScrollPane);

        messagePreviewArea = new JTextArea();
        messagePreviewArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        messagePreviewArea.setLineWrap(true);
        messagePreviewArea.setWrapStyleWord(true);
        messagePreviewArea.setEditable(false);
        messagePreviewArea.setBackground(new Color(245, 245, 245));
        previewScrollPane = new JScrollPane(messagePreviewArea);
        previewScrollPane.setBounds(10, 235, 320, 50);
        previewScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        previewScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightPanel.add(previewScrollPane);

        messageInputField = new JTextField();
        messageInputField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageInputField.setBounds(10, 290, 230, 30);
        messageInputField.setBorder(new LineBorder(new Color(12, 192, 223)));
        messageInputField.setEnabled(false);
        rightPanel.add(messageInputField);

        sendButton = new JButton("SEND");
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        sendButton.setBackground(new Color(0, 102, 102));
        sendButton.setForeground(Color.WHITE);
        sendButton.setBounds(250, 290, 80, 30);
        sendButton.setBorder(null);
        sendButton.setFocusPainted(false);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendButton.setEnabled(false);
        sendButton.addActionListener(e -> sendMessage());
        rightPanel.add(sendButton);

        contentWrapper.add(topPanel);
        contentWrapper.add(leftPanel);
        contentWrapper.add(rightPanel);

        contentPanel.add(contentWrapper);
        contentPanel.revalidate();
        contentPanel.repaint();
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

        JDialog dialog = new JDialog(this, "New Message", true);
        dialog.setSize(400, 250);
        dialog.setLayout(null);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Color.WHITE);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(12, 192, 223));
        titlePanel.setBounds(0, 0, 400, 40);
        titlePanel.setLayout(null);

        JLabel titleLabel = new JLabel("NEW MESSAGE");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(20, 5, 200, 30);
        titlePanel.add(titleLabel);
        dialog.add(titlePanel);

        JLabel toLabel = new JLabel("To:");
        toLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        toLabel.setBounds(20, 60, 50, 25);
        dialog.add(toLabel);

        JComboBox<String> traderCombo = new JComboBox<>(traderNames);
        traderCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        traderCombo.setBounds(80, 60, 250, 30);
        traderCombo.setBackground(Color.WHITE);
        dialog.add(traderCombo);

        JLabel messageLabel = new JLabel("Message:");
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        messageLabel.setBounds(20, 100, 80, 25);
        dialog.add(messageLabel);

        JTextArea messageArea = new JTextArea();
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setBounds(20, 130, 350, 60);
        scrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        dialog.add(scrollPane);

        JButton sendBtn = new JButton("SEND");
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

        JButton cancelBtn = new JButton("CANCEL");
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
        } else if (panel == tradesPanel) {
            trades tradesFrame = new trades(traderId, traderName);
            tradesFrame.setVisible(true);
            tradesFrame.setLocationRelativeTo(null);
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