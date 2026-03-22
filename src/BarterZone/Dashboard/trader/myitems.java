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
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class myitems extends javax.swing.JFrame {

    private int traderId;
    private String traderName;
    private config db;
    private DefaultTableModel tableModel;
    private TableRowSorter<TableModel> rowSorter;
    private int selectedItemId = -1;
    private int lastSelectedRow = -1;
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
    
    private JTextField searchField;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton removeButton;
    
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable myitemstable;

    private Color themeColor = new Color(12, 192, 223);
    private Color hoverColor = new Color(70, 210, 235);
    private Color activeColor = new Color(0, 150, 180);
    private Color headerBgColor = new Color(245, 245, 245);
    private Color textColor = new Color(80, 80, 80);
    private Color accentColor = new Color(0, 102, 102);
    private Color initialColor = new Color(0, 102, 102);
    
    private JPanel activePanel = null;

    public myitems(int traderId, String traderName) {
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
        loadItems();
        setupLiveSearch();
        loadProfileAvatar();

        setTitle("My Items - " + traderName);
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
        
        jScrollPane1 = new javax.swing.JScrollPane();
        myitemstable = new javax.swing.JTable();
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

        setActivePanel(myItemsPanel);
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
        headerTitle = new JLabel("My Items");
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
        JPanel contentWrapper = new JPanel();
        contentWrapper.setLayout(null);
        contentWrapper.setBackground(Color.WHITE);
        contentWrapper.setBounds(0, 0, 620, 430);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(null);
        searchPanel.setBackground(new Color(245, 245, 245));
        searchPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        searchPanel.setBounds(10, 10, 600, 50);

        JLabel searchLabel = new JLabel("Search Items:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchLabel.setBounds(10, 15, 100, 20);
        searchPanel.add(searchLabel);

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBounds(120, 12, 200, 26);
        searchField.setBorder(new LineBorder(new Color(200, 200, 200)));
        searchPanel.add(searchField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        buttonPanel.setBounds(10, 70, 600, 50);

        addButton = new JButton("Add Item");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addButton.setBackground(new Color(0, 102, 102));
        addButton.setForeground(Color.WHITE);
        addButton.setBounds(10, 10, 100, 30);
        addButton.setBorder(null);
        addButton.setFocusPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addActionListener(e -> openAddItem());
        buttonPanel.add(addButton);

        editButton = new JButton("Edit Item");
        editButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        editButton.setBackground(new Color(255, 153, 0));
        editButton.setForeground(Color.WHITE);
        editButton.setBounds(120, 10, 100, 30);
        editButton.setBorder(null);
        editButton.setFocusPainted(false);
        editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editButton.addActionListener(e -> openEditItem());
        buttonPanel.add(editButton);

        deleteButton = new JButton("Delete Item");
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        deleteButton.setBackground(new Color(204, 0, 0));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setBounds(230, 10, 110, 30);
        deleteButton.setBorder(null);
        deleteButton.setFocusPainted(false);
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.addActionListener(e -> deleteSelectedItem());
        buttonPanel.add(deleteButton);

        removeButton = new JButton("Remove Selection");
        removeButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        removeButton.setBackground(new Color(102, 102, 102));
        removeButton.setForeground(Color.WHITE);
        removeButton.setBounds(350, 10, 140, 30);
        removeButton.setBorder(null);
        removeButton.setFocusPainted(false);
        removeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        removeButton.addActionListener(e -> clearSelection());
        buttonPanel.add(removeButton);

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(null);
        tablePanel.setBackground(new Color(245, 245, 245));
        tablePanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        tablePanel.setBounds(10, 130, 600, 290);

        setupTable();

        jScrollPane1.setBounds(10, 10, 580, 270);
        jScrollPane1.setBorder(null);
        jScrollPane1.setViewportView(myitemstable);
        tablePanel.add(jScrollPane1);

        contentWrapper.add(searchPanel);
        contentWrapper.add(buttonPanel);
        contentWrapper.add(tablePanel);

        contentPanel.add(contentWrapper);
    }

    private void setupTable() {
        String[] columns = {"ID", "Item Name", "Brand", "Condition", "Date Bought", "Description", "Status", "Photo"};
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
        myitemstable.setGridColor(new Color(230, 230, 230));
        myitemstable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        myitemstable.getTableHeader().setBackground(new Color(12, 192, 223));
        myitemstable.getTableHeader().setForeground(Color.WHITE);
        myitemstable.getTableHeader().setBorder(null);
        myitemstable.setSelectionBackground(new Color(184, 239, 255));

        myitemstable.getColumnModel().getColumn(0).setMinWidth(0);
        myitemstable.getColumnModel().getColumn(0).setMaxWidth(0);
        myitemstable.getColumnModel().getColumn(0).setWidth(0);

        myitemstable.getColumnModel().getColumn(7).setCellRenderer(new ImageRenderer());
        myitemstable.getColumnModel().getColumn(7).setPreferredWidth(80);
        myitemstable.getColumnModel().getColumn(7).setMinWidth(80);
        myitemstable.getColumnModel().getColumn(7).setMaxWidth(80);

        TableColumn column1 = myitemstable.getColumnModel().getColumn(1);
        column1.setPreferredWidth(120);
        column1.setMinWidth(100);

        TableColumn column2 = myitemstable.getColumnModel().getColumn(2);
        column2.setPreferredWidth(100);
        column2.setMinWidth(80);

        TableColumn column3 = myitemstable.getColumnModel().getColumn(3);
        column3.setPreferredWidth(80);
        column3.setMinWidth(70);

        TableColumn column4 = myitemstable.getColumnModel().getColumn(4);
        column4.setPreferredWidth(100);
        column4.setMinWidth(80);

        TableColumn column5 = myitemstable.getColumnModel().getColumn(5);
        column5.setPreferredWidth(150);
        column5.setMinWidth(120);

        TableColumn column6 = myitemstable.getColumnModel().getColumn(6);
        column6.setPreferredWidth(70);
        column6.setMinWidth(60);

        myitemstable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = myitemstable.getSelectedRow();
                if (selectedRow != -1) {
                    int modelRow = myitemstable.convertRowIndexToModel(selectedRow);
                    selectedItemId = Integer.parseInt(tableModel.getValueAt(modelRow, 0).toString());
                }
            }
        });

        myitemstable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int selectedRow = myitemstable.getSelectedRow();
                    if (selectedRow != -1 && selectedRow == lastSelectedRow) {
                        myitemstable.clearSelection();
                        clearSelection();
                    }
                    lastSelectedRow = selectedRow;
                }
            }
        });
    }

    private void clearSelection() {
        myitemstable.clearSelection();
        selectedItemId = -1;
        lastSelectedRow = -1;
    }

    private void setupLiveSearch() {
        rowSorter = new TableRowSorter<>(myitemstable.getModel());
        myitemstable.setRowSorter(rowSorter);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }
        });
    }

    private void filterTable() {
        String text = searchField.getText();
        if (text.trim().length() == 0) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 1, 2, 3, 5));
        }
    }

    private void loadItems() {
        tableModel.setRowCount(0);

        String sql = "SELECT items_id, item_Name, item_Brand, item_Condition, "
                + "item_Date, item_Description, is_active, item_picture "
                + "FROM tbl_items WHERE trader_id = ? ORDER BY items_id DESC";

        List<Map<String, Object>> items = db.fetchRecords(sql, traderId);

        for (Map<String, Object> item : items) {
            Object status = item.get("is_active");
            String statusText = "Active";
            if (status instanceof Boolean) {
                statusText = (Boolean) status ? "Active" : "Inactive";
            } else if (status instanceof Integer) {
                statusText = ((Integer) status == 1) ? "Active" : "Inactive";
            }

            String photoPath = item.get("item_picture") != null ? item.get("item_picture").toString() : "";

            tableModel.addRow(new Object[]{
                item.get("items_id"),
                item.get("item_Name"),
                item.get("item_Brand"),
                item.get("item_Condition"),
                item.get("item_Date"),
                item.get("item_Description"),
                statusText,
                photoPath
            });
        }
    }

    private void openAddItem() {
        add_items addFrame = new add_items(traderId, traderName);
        addFrame.setVisible(true);
        addFrame.setLocationRelativeTo(null);
        this.dispose();
    }

    private void openEditItem() {
        if (selectedItemId == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        edit_items editFrame = new edit_items(traderId, traderName, selectedItemId);
        editFrame.setVisible(true);
        editFrame.setLocationRelativeTo(null);
        this.dispose();
    }

    private void deleteSelectedItem() {
        if (selectedItemId == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this item?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM tbl_items WHERE items_id = ? AND trader_id = ?";
            db.deleteRecord(sql, selectedItemId, traderId);

            JOptionPane.showMessageDialog(this, "Item deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            selectedItemId = -1;
            loadItems();
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
        } else if (panel == messagesPanel) {
            messages messagesFrame = new messages(traderId, traderName);
            messagesFrame.setVisible(true);
            messagesFrame.setLocationRelativeTo(null);
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

    class ImageRenderer extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            JPanel panel = new JPanel(new java.awt.BorderLayout());
            panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());

            JLabel label = new JLabel();
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
            label.setOpaque(false);

            if (value != null && !value.toString().trim().isEmpty()) {
                try {
                    String imagePath = value.toString().trim();
                    String filePath = convertResourcePathToFilePath(imagePath);

                    ImageIcon imageIcon = null;
                    if (filePath != null) {
                        imageIcon = createScaledImageIcon(filePath, 50, 50);
                    }

                    if (imageIcon != null) {
                        label.setIcon(imageIcon);
                        label.setText("");
                    } else {
                        label.setIcon(null);
                        label.setText("Image Missing");
                        label.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                        label.setForeground(Color.GRAY);
                    }
                } catch (Exception e) {
                    label.setIcon(null);
                    label.setText("Image Missing");
                    label.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                    label.setForeground(Color.GRAY);
                    System.out.println("Error loading item image: " + e.getMessage());
                }
            } else {
                label.setIcon(null);
                label.setText("No Image");
                label.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                label.setForeground(Color.GRAY);
            }

            panel.add(label, java.awt.BorderLayout.CENTER);
            return panel;
        }
    }
}