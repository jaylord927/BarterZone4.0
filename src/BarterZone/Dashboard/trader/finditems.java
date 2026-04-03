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
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JDialog;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.BorderFactory;

public class finditems extends javax.swing.JFrame {

    private int traderId;
    private String traderName;
    private config db;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
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
    private JComboBox<String> searchCategoryCombo;
    private JPanel detailsPanel;
    private JLabel itemImageLabel;
    private JLabel itemNameLabel;
    private JLabel itemBrandLabel;
    private JLabel itemConditionLabel;
    private JLabel itemDateLabel;
    private JTextArea itemDescriptionArea;
    private JScrollPane descScrollPane;
    private JLabel ownerNameLabel;
    private JButton messageButton;
    private JButton tradeRequestButton;
    private JLabel selectedStatusLabel;

    private JScrollPane tableScrollPane;
    private JScrollPane detailsScrollPane;
    private javax.swing.JTable myitemstable;

    private int selectedItemId = -1;
    private int selectedItemOwnerId = -1;
    private String selectedItemOwnerName = "";
    private String selectedItemImagePath = "";
    private int lastSelectedRow = -1;
    private java.util.List<Map<String, Object>> traderOwnItems;
    private int selectedOwnItemId = -1;
    private String selectedOwnItemName = "";

    private Color themeColor = new Color(12, 192, 223);
    private Color hoverColor = new Color(70, 210, 235);
    private Color activeColor = new Color(0, 150, 180);
    private Color headerBgColor = new Color(245, 245, 245);
    private Color textColor = new Color(80, 80, 80);
    private Color accentColor = new Color(0, 102, 102);
    private Color initialColor = new Color(0, 102, 102);
    private Color successColor = new Color(46, 125, 50);
    private Color warningColor = new Color(255, 153, 0);
    private Color errorColor = new Color(204, 0, 0);
    
    private JPanel activePanel = null;

    public finditems(int traderId, String traderName) {
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
        loadAllItems();
        loadTraderOwnItems();
        setupLiveSearch();
        loadProfileAvatar();

        setTitle("BarterZone - " + traderName);
        setIconImage(new ImageIcon(getClass().getResource(
                "/BarterZone/resources/icon/logo.png")).getImage());
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

    private BufferedImage createCircularImage(BufferedImage sourceImage, int size) {
        BufferedImage circularImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circularImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        Image scaledImage = sourceImage.getScaledInstance(size, size, Image.SCALE_SMOOTH);
        BufferedImage scaledBuffered = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledBuffered.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, size, size, null);
        g2d.dispose();
        
        g2.setClip(new Ellipse2D.Float(0, 0, size, size));
        g2.drawImage(scaledBuffered, 0, 0, size, size, null);
        g2.dispose();
        
        return circularImage;
    }

    private ImageIcon loadAndCircleImage(String imagePath, int size) {
        try {
            File file = new File(imagePath);
            if (!file.exists()) {
                return null;
            }
            
            BufferedImage originalImage = ImageIO.read(file);
            if (originalImage == null) {
                return null;
            }
            
            BufferedImage circularImage = createCircularImage(originalImage, size);
            return new ImageIcon(circularImage);
            
        } catch (Exception e) {
            System.out.println("Error loading circular image: " + e.getMessage());
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
                        ImageIcon circularIcon = loadAndCircleImage(fullPath, 90);
                        
                        if (circularIcon != null && circularIcon.getIconWidth() > 0) {
                            avatarLabel.setIcon(circularIcon);
                            avatarLabel.setText("");
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

        setActivePanel(findItemsPanel);
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
        headerTitle = new JLabel("Find Items");
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

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(null);
        searchPanel.setBackground(new Color(245, 245, 245));
        searchPanel.setBorder(new LineBorder(new Color(12, 192, 223), 2));
        searchPanel.setBounds(10, 10, 600, 70);

        JLabel searchLabel = new JLabel("Search Items:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchLabel.setForeground(new Color(0, 102, 102));
        searchLabel.setBounds(15, 10, 120, 25);
        searchPanel.add(searchLabel);

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBounds(15, 35, 350, 30);
        searchField.setBorder(new LineBorder(new Color(12, 192, 223)));
        searchPanel.add(searchField);

        String[] categories = {"All Fields", "Item Name", "Brand", "Condition", "Owner"};
        searchCategoryCombo = new JComboBox<>(categories);
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
        leftPanel.setBounds(10, 90, 300, 330);

        JLabel tableTitle = new JLabel("Available Items");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableTitle.setForeground(new Color(0, 102, 102));
        tableTitle.setBounds(10, 5, 200, 20);
        leftPanel.add(tableTitle);

        setupTable();

        tableScrollPane = new JScrollPane(myitemstable);
        tableScrollPane.setBounds(10, 30, 280, 290);
        tableScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        leftPanel.add(tableScrollPane);

        detailsPanel = new JPanel();
        detailsPanel.setLayout(null);
        detailsPanel.setBackground(new Color(245, 245, 245));
        detailsPanel.setBorder(new LineBorder(new Color(12, 192, 223), 2));
        detailsPanel.setBounds(320, 90, 290, 330);

        JLabel detailsTitle = new JLabel("Item Details");
        detailsTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        detailsTitle.setForeground(new Color(0, 102, 102));
        detailsTitle.setBounds(10, 5, 200, 20);
        detailsPanel.add(detailsTitle);

        selectedStatusLabel = new JLabel("No item selected");
        selectedStatusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        selectedStatusLabel.setForeground(new Color(102, 102, 102));
        selectedStatusLabel.setBounds(10, 25, 270, 15);
        detailsPanel.add(selectedStatusLabel);

        JPanel detailsContentPanel = new JPanel();
        detailsContentPanel.setLayout(null);
        detailsContentPanel.setBackground(new Color(245, 245, 245));
        detailsContentPanel.setPreferredSize(new java.awt.Dimension(260, 550));

        itemImageLabel = new JLabel();
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

        JLabel ownerTitle = new JLabel("Owner:");
        ownerTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        ownerTitle.setBounds(labelX, y, 50, 20);
        detailsContentPanel.add(ownerTitle);

        ownerNameLabel = new JLabel("-");
        ownerNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        ownerNameLabel.setForeground(new Color(0, 102, 102));
        ownerNameLabel.setBounds(valueX, y, valueWidth, 20);
        detailsContentPanel.add(ownerNameLabel);
        y += 25;

        JLabel nameTitle = new JLabel("Name:");
        nameTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nameTitle.setBounds(labelX, y, 50, 20);
        detailsContentPanel.add(nameTitle);

        itemNameLabel = new JLabel("-");
        itemNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        itemNameLabel.setBounds(valueX, y, valueWidth, 20);
        detailsContentPanel.add(itemNameLabel);
        y += 25;

        JLabel brandTitle = new JLabel("Brand:");
        brandTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        brandTitle.setBounds(labelX, y, 50, 20);
        detailsContentPanel.add(brandTitle);

        itemBrandLabel = new JLabel("-");
        itemBrandLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        itemBrandLabel.setBounds(valueX, y, valueWidth, 20);
        detailsContentPanel.add(itemBrandLabel);
        y += 25;

        JLabel conditionTitle = new JLabel("Condition:");
        conditionTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        conditionTitle.setBounds(labelX, y, 70, 20);
        detailsContentPanel.add(conditionTitle);

        itemConditionLabel = new JLabel("-");
        itemConditionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        itemConditionLabel.setBounds(valueX, y, valueWidth, 20);
        detailsContentPanel.add(itemConditionLabel);
        y += 25;

        JLabel dateTitle = new JLabel("Date:");
        dateTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        dateTitle.setBounds(labelX, y, 50, 20);
        detailsContentPanel.add(dateTitle);

        itemDateLabel = new JLabel("-");
        itemDateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        itemDateLabel.setBounds(valueX, y, valueWidth, 20);
        detailsContentPanel.add(itemDateLabel);
        y += 25;

        JLabel descTitle = new JLabel("Description:");
        descTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        descTitle.setBounds(labelX, y, 80, 20);
        detailsContentPanel.add(descTitle);

        itemDescriptionArea = new JTextArea();
        itemDescriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        itemDescriptionArea.setLineWrap(true);
        itemDescriptionArea.setWrapStyleWord(true);
        itemDescriptionArea.setEditable(false);
        itemDescriptionArea.setBackground(new Color(245, 245, 245));
        itemDescriptionArea.setText("-");

        descScrollPane = new JScrollPane(itemDescriptionArea);
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

        JLabel actionLabel = new JLabel("ACTIONS");
        actionLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        actionLabel.setForeground(Color.WHITE);
        actionLabel.setBounds(10, 5, 100, 15);
        buttonPanel.add(actionLabel);

        messageButton = new JButton("MESSAGE");
        messageButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        messageButton.setBackground(new Color(0, 102, 102));
        messageButton.setForeground(Color.WHITE);
        messageButton.setBounds(10, 22, 100, 23);
        messageButton.setBorder(new LineBorder(Color.WHITE, 1));
        messageButton.setFocusPainted(false);
        messageButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        messageButton.setEnabled(false);
        messageButton.addActionListener(e -> showMessageDialog());
        buttonPanel.add(messageButton);

        tradeRequestButton = new JButton("TRADE");
        tradeRequestButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tradeRequestButton.setBackground(new Color(255, 140, 0));
        tradeRequestButton.setForeground(Color.WHITE);
        tradeRequestButton.setBounds(120, 22, 100, 23);
        tradeRequestButton.setBorder(new LineBorder(Color.WHITE, 1));
        tradeRequestButton.setFocusPainted(false);
        tradeRequestButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tradeRequestButton.setEnabled(false);
        tradeRequestButton.addActionListener(e -> showTradeDialog());
        buttonPanel.add(tradeRequestButton);

        detailsContentPanel.add(buttonPanel);

        detailsScrollPane = new JScrollPane(detailsContentPanel);
        detailsScrollPane.setBounds(5, 45, 280, 280);
        detailsScrollPane.setBorder(null);
        detailsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        detailsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        detailsPanel.add(detailsScrollPane);

        contentWrapper.add(searchPanel);
        contentWrapper.add(leftPanel);
        contentWrapper.add(detailsPanel);

        contentPanel.add(contentWrapper);
        contentPanel.revalidate();
        contentPanel.repaint();
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
        myitemstable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

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

    private void loadTraderOwnItems() {
        String sql = "SELECT i.items_id, i.item_Name, i.item_Brand, i.item_Condition, "
                + "i.trader_id "
                + "FROM tbl_items i "
                + "WHERE i.trader_id = ? AND i.is_active = 1 "
                + "AND i.items_id NOT IN ("
                + "    SELECT DISTINCT offer_item_id FROM tbl_trade WHERE trade_status IN ('pending', 'negotiating', 'arrangements_confirmed', 'step2_agreed', 'step3_payment', 'step4_shipping', 'step5_completing') "
                + "    UNION "
                + "    SELECT DISTINCT target_item_id FROM tbl_trade WHERE trade_status IN ('pending', 'negotiating', 'arrangements_confirmed', 'step2_agreed', 'step3_payment', 'step4_shipping', 'step5_completing')"
                + ") "
                + "ORDER BY i.item_Name ASC";

        traderOwnItems = db.fetchRecords(sql, traderId);
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
            String filePath = convertResourcePathToFilePath(imagePath);
            if (filePath != null) {
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
            } else {
                itemImageLabel.setIcon(null);
                itemImageLabel.setText("Image Not Found");
            }
        } catch (Exception e) {
            itemImageLabel.setIcon(null);
            itemImageLabel.setText("No Image");
        }
    }

    private void showMessageDialog() {
        if (selectedItemId == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog messageDialog = new JDialog(this, "Send Message", true);
        messageDialog.setSize(450, 380);
        messageDialog.setLayout(null);
        messageDialog.setLocationRelativeTo(this);
        messageDialog.getContentPane().setBackground(Color.WHITE);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(themeColor);
        titlePanel.setBounds(0, 0, 450, 45);
        titlePanel.setLayout(null);

        JLabel titleLabel = new JLabel("SEND MESSAGE");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(20, 8, 200, 30);
        titlePanel.add(titleLabel);
        messageDialog.add(titlePanel);

        JLabel toLabel = new JLabel("To: " + selectedItemOwnerName);
        toLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        toLabel.setBounds(20, 65, 400, 25);
        messageDialog.add(toLabel);

        JLabel itemLabel = new JLabel("Item: " + itemNameLabel.getText());
        itemLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        itemLabel.setForeground(textColor);
        itemLabel.setBounds(20, 95, 400, 20);
        messageDialog.add(itemLabel);

        JLabel messageLabel = new JLabel("Message:");
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        messageLabel.setBounds(20, 135, 100, 25);
        messageDialog.add(messageLabel);

        JTextArea messageArea = new JTextArea();
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setBounds(20, 165, 410, 100);
        scrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        messageDialog.add(scrollPane);

        JButton sendButton = new JButton("SEND MESSAGE");
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendButton.setBackground(successColor);
        sendButton.setForeground(Color.WHITE);
        sendButton.setBounds(100, 290, 150, 35);
        sendButton.setBorder(null);
        sendButton.setFocusPainted(false);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendButton.addActionListener(e -> {
            String messageText = messageArea.getText().trim();
            if (messageText.isEmpty()) {
                JOptionPane.showMessageDialog(messageDialog, "Please enter a message.", "Empty Message", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String insertSql = "INSERT INTO tbl_trade_messages (sender_id, receiver_id, message_text, message_date) "
                    + "VALUES (?, ?, ?, datetime('now'))";

            try {
                db.addRecord(insertSql, traderId, selectedItemOwnerId, messageText);
                JOptionPane.showMessageDialog(messageDialog, "Message sent successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                messageDialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(messageDialog, "Failed to send message: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        messageDialog.add(sendButton);

        JButton cancelButton = new JButton("CANCEL");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setBackground(errorColor);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBounds(270, 290, 100, 35);
        cancelButton.setBorder(null);
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> messageDialog.dispose());
        messageDialog.add(cancelButton);

        messageDialog.setVisible(true);
    }

    private void showTradeDialog() {
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
                    + "All your items may be already in active/completed trades.\n"
                    + "Please add new items in 'My Items' section to start trading.",
                    "No Items Available",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog tradeDialog = new JDialog(this, "Send Trade Request", true);
        tradeDialog.setSize(500, 480);
        tradeDialog.setLayout(null);
        tradeDialog.setLocationRelativeTo(this);
        tradeDialog.getContentPane().setBackground(Color.WHITE);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(themeColor);
        titlePanel.setBounds(0, 0, 500, 45);
        titlePanel.setLayout(null);

        JLabel titleLabel = new JLabel("TRADE REQUEST");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(20, 8, 200, 30);
        titlePanel.add(titleLabel);
        tradeDialog.add(titlePanel);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(null);
        infoPanel.setBackground(new Color(250, 250, 250));
        infoPanel.setBorder(new LineBorder(accentColor, 1));
        infoPanel.setBounds(20, 65, 460, 85);

        JLabel requestedItemLabel = new JLabel("You want to trade for:");
        requestedItemLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        requestedItemLabel.setBounds(10, 10, 150, 20);
        infoPanel.add(requestedItemLabel);

        String itemInfoText = "<html><b>Item:</b> " + itemNameLabel.getText() + 
                              "<br><b>Brand:</b> " + itemBrandLabel.getText() + 
                              "<br><b>Owner:</b> " + selectedItemOwnerName + "</html>";
        JLabel itemInfoLabel = new JLabel(itemInfoText);
        itemInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        itemInfoLabel.setBounds(10, 35, 440, 45);
        infoPanel.add(itemInfoLabel);

        tradeDialog.add(infoPanel);

        JLabel yourItemLabel = new JLabel("Select your item to offer:");
        yourItemLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        yourItemLabel.setBounds(20, 170, 200, 25);
        tradeDialog.add(yourItemLabel);

        String[] availableItems = new String[traderOwnItems.size()];
        Integer[] availableItemIds = new Integer[traderOwnItems.size()];
        
        for (int i = 0; i < traderOwnItems.size(); i++) {
            Map<String, Object> item = traderOwnItems.get(i);
            availableItems[i] = item.get("item_Name") + " (" + item.get("item_Brand") + ")";
            availableItemIds[i] = Integer.parseInt(item.get("items_id").toString());
        }

        JComboBox<String> itemCombo = new JComboBox<>(availableItems);
        itemCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        itemCombo.setBounds(20, 200, 460, 35);
        itemCombo.setBackground(Color.WHITE);
        itemCombo.setBorder(new LineBorder(new Color(12, 192, 223), 1));
        tradeDialog.add(itemCombo);

        JLabel messageLabel = new JLabel("Optional Message:");
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        messageLabel.setBounds(20, 255, 150, 25);
        tradeDialog.add(messageLabel);

        JTextArea messageArea = new JTextArea();
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane messageScroll = new JScrollPane(messageArea);
        messageScroll.setBounds(20, 285, 460, 70);
        messageScroll.setBorder(new LineBorder(new Color(200, 200, 200)));
        tradeDialog.add(messageScroll);

        JLabel noteLabel = new JLabel("Note: Only available items are shown in the list.");
        noteLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        noteLabel.setForeground(new Color(100, 100, 100));
        noteLabel.setBounds(20, 370, 300, 20);
        tradeDialog.add(noteLabel);

        JButton sendButton = new JButton("SEND REQUEST");
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendButton.setBackground(successColor);
        sendButton.setForeground(Color.WHITE);
        sendButton.setBounds(130, 410, 150, 35);
        sendButton.setBorder(null);
        sendButton.setFocusPainted(false);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendButton.addActionListener(e -> {
            int selectedIndex = itemCombo.getSelectedIndex();
            if (selectedIndex >= 0) {
                int selectedOfferItemId = availableItemIds[selectedIndex];
                String selectedOfferItemName = availableItems[selectedIndex];
                
                if (!isItemAvailableForTrade(selectedOfferItemId)) {
                    JOptionPane.showMessageDialog(tradeDialog,
                            "Sorry, the selected item is no longer available for trade.",
                            "Item Unavailable",
                            JOptionPane.WARNING_MESSAGE);
                    loadTraderOwnItems();
                    return;
                }

                if (!isItemAvailableForTrade(selectedItemId)) {
                    JOptionPane.showMessageDialog(tradeDialog,
                            "Sorry, the item you requested is no longer available.",
                            "Item Unavailable",
                            JOptionPane.WARNING_MESSAGE);
                    tradeDialog.dispose();
                    loadAllItems();
                    clearSelection();
                    return;
                }

                createTradeRequest(selectedOfferItemId, selectedOfferItemName, messageArea.getText().trim());
                tradeDialog.dispose();
            }
        });
        tradeDialog.add(sendButton);

        JButton cancelButton = new JButton("CANCEL");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setBackground(errorColor);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBounds(310, 410, 100, 35);
        cancelButton.setBorder(null);
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> tradeDialog.dispose());
        tradeDialog.add(cancelButton);

        tradeDialog.setVisible(true);
    }

    private boolean isItemAvailableForTrade(int itemId) {
        String sql = "SELECT COUNT(*) as count FROM tbl_items WHERE items_id = ? AND is_active = 1 "
                + "AND items_id NOT IN ("
                + "    SELECT DISTINCT offer_item_id FROM tbl_trade WHERE trade_status IN ('pending', 'negotiating', 'arrangements_confirmed', 'step2_agreed', 'step3_payment', 'step4_shipping', 'step5_completing') "
                + "    UNION "
                + "    SELECT DISTINCT target_item_id FROM tbl_trade WHERE trade_status IN ('pending', 'negotiating', 'arrangements_confirmed', 'step2_agreed', 'step3_payment', 'step4_shipping', 'step5_completing')"
                + ")";

        double count = db.getSingleValue(sql, itemId);
        return count > 0;
    }

    private void createTradeRequest(int offeredItemId, String offeredItemName, String message) {
        if (!isItemAvailableForTrade(offeredItemId)) {
            JOptionPane.showMessageDialog(this,
                    "Your item is no longer available for trade.",
                    "Trade Failed",
                    JOptionPane.WARNING_MESSAGE);
            loadTraderOwnItems();
            return;
        }

        if (!isItemAvailableForTrade(selectedItemId)) {
            JOptionPane.showMessageDialog(this,
                    "The item you requested is no longer available.",
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

            if (message != null && !message.trim().isEmpty()) {
                String msgSql = "INSERT INTO tbl_trade_messages (sender_id, receiver_id, message_text, message_date) "
                        + "VALUES (?, ?, ?, datetime('now'))";
                db.addRecord(msgSql, traderId, selectedItemOwnerId, message);
            }

            JOptionPane.showMessageDialog(this,
                    "Trade request sent successfully!\n\n"
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

    class ImageRenderer extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = new JLabel();
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);

            if (value != null && !value.toString().isEmpty()) {
                try {
                    String filePath = convertResourcePathToFilePath(value.toString());
                    if (filePath != null) {
                        File imgFile = new File(filePath);
                        if (imgFile.exists()) {
                            ImageIcon icon = new ImageIcon(filePath);
                            Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                            label.setIcon(new ImageIcon(img));
                        } else {
                            label.setText("No Img");
                            label.setFont(new Font("Segoe UI", Font.PLAIN, 9));
                        }
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
}