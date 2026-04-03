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
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class reports extends javax.swing.JFrame {

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
    
    private DefaultTableModel myReportsTableModel;
    private javax.swing.JTable myReportsTable;
    private JScrollPane myReportsScrollPane;

    private JComboBox<String> traderComboBox;
    private JComboBox<String> reasonComboBox;
    private JTextArea descriptionArea;
    private JScrollPane descriptionScrollPane;
    private JButton submitReportButton;
    private JButton refreshButton;
    private JButton cancelReportButton;
    private JButton viewRespondButton;

    private int selectedReportId = -1;
    private String selectedReportStatus = "";
    private int lastSelectedRow = -1;

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

    public reports(int traderId, String traderName) {
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
        loadMyReports();
        loadTraders();
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

        setActivePanel(reportsPanel);
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
        headerTitle = new JLabel("Reports");
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

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(null);
        leftPanel.setBackground(new Color(245, 245, 245));
        leftPanel.setBorder(new LineBorder(new Color(12, 192, 223), 2));
        leftPanel.setBounds(10, 10, 250, 410);

        JLabel myReportsTitle = new JLabel("My Reports");
        myReportsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        myReportsTitle.setForeground(new Color(0, 102, 102));
        myReportsTitle.setBounds(10, 10, 200, 25);
        leftPanel.add(myReportsTitle);

        setupMyReportsTable();
        myReportsScrollPane = new JScrollPane(myReportsTable);
        myReportsScrollPane.setBounds(10, 40, 230, 360);
        myReportsScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        myReportsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        myReportsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        leftPanel.add(myReportsScrollPane);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(null);
        rightPanel.setBackground(new Color(245, 245, 245));
        rightPanel.setBorder(new LineBorder(new Color(12, 192, 223), 2));
        rightPanel.setBounds(270, 10, 340, 410);

        JLabel submitTitle = new JLabel("Submit New Report");
        submitTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        submitTitle.setForeground(new Color(0, 102, 102));
        submitTitle.setBounds(10, 10, 200, 25);
        rightPanel.add(submitTitle);

        JLabel traderLabel = new JLabel("Report Trader:");
        traderLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        traderLabel.setBounds(10, 45, 100, 20);
        rightPanel.add(traderLabel);

        traderComboBox = new JComboBox<>();
        traderComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        traderComboBox.setBounds(10, 65, 320, 30);
        traderComboBox.setBackground(Color.WHITE);
        traderComboBox.setBorder(new LineBorder(new Color(12, 192, 223)));
        rightPanel.add(traderComboBox);

        JLabel reasonLabel = new JLabel("Reason for Report:");
        reasonLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        reasonLabel.setBounds(10, 105, 150, 20);
        rightPanel.add(reasonLabel);

        String[] reportReasons = {
            "Select a reason",
            "Scamming / Fraud",
            "Fake Account",
            "Fake Item Listing",
            "Harassment / Bullying",
            "Inappropriate Behavior",
            "Misleading Description",
            "Trade Cancellation Abuse",
            "No Show for Meetup",
            "Suspicious Activity",
            "Multiple Accounts",
            "Violation of Terms",
            "Other"
        };
        
        reasonComboBox = new JComboBox<>(reportReasons);
        reasonComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        reasonComboBox.setBounds(10, 125, 320, 30);
        reasonComboBox.setBackground(Color.WHITE);
        reasonComboBox.setBorder(new LineBorder(new Color(12, 192, 223)));
        rightPanel.add(reasonComboBox);

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        descriptionLabel.setBounds(10, 165, 100, 20);
        rightPanel.add(descriptionLabel);

        descriptionArea = new JTextArea();
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionScrollPane = new JScrollPane(descriptionArea);
        descriptionScrollPane.setBounds(10, 185, 320, 60);
        descriptionScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        descriptionScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightPanel.add(descriptionScrollPane);

        submitReportButton = new JButton("SUBMIT REPORT");
        submitReportButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        submitReportButton.setBackground(errorColor);
        submitReportButton.setForeground(Color.WHITE);
        submitReportButton.setBounds(20, 280, 140, 35);
        submitReportButton.setBorder(null);
        submitReportButton.setFocusPainted(false);
        submitReportButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitReportButton.addActionListener(e -> submitReport());
        rightPanel.add(submitReportButton);

        cancelReportButton = new JButton("CANCEL REPORT");
        cancelReportButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cancelReportButton.setBackground(new Color(102, 102, 102));
        cancelReportButton.setForeground(Color.WHITE);
        cancelReportButton.setBounds(180, 280, 140, 35);
        cancelReportButton.setBorder(null);
        cancelReportButton.setFocusPainted(false);
        cancelReportButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelReportButton.setEnabled(false);
        cancelReportButton.addActionListener(e -> cancelReport());
        rightPanel.add(cancelReportButton);

        refreshButton = new JButton("REFRESH");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshButton.setBackground(accentColor);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBounds(20, 330, 140, 35);
        refreshButton.setBorder(null);
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> refreshReports());
        rightPanel.add(refreshButton);

        viewRespondButton = new JButton("VIEW RESPOND");
        viewRespondButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        viewRespondButton.setBackground(themeColor);
        viewRespondButton.setForeground(Color.WHITE);
        viewRespondButton.setBounds(180, 330, 140, 35);
        viewRespondButton.setBorder(null);
        viewRespondButton.setFocusPainted(false);
        viewRespondButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewRespondButton.setEnabled(false);
        viewRespondButton.addActionListener(e -> viewRespond());
        rightPanel.add(viewRespondButton);

        contentWrapper.add(leftPanel);
        contentWrapper.add(rightPanel);

        contentPanel.add(contentWrapper);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void setupMyReportsTable() {
        String[] columns = {"Report ID", "Date", "Reason", "Status", "Reported Trader"};
        myReportsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        myReportsTable = new javax.swing.JTable(myReportsTableModel);
        myReportsTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        myReportsTable.setRowHeight(30);
        myReportsTable.setShowGrid(true);
        myReportsTable.setGridColor(new Color(12, 192, 223));
        myReportsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        myReportsTable.getTableHeader().setBackground(new Color(0, 102, 102));
        myReportsTable.getTableHeader().setForeground(Color.WHITE);
        myReportsTable.setSelectionBackground(new Color(184, 239, 255));
        myReportsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        myReportsTable.getColumnModel().getColumn(0).setMinWidth(0);
        myReportsTable.getColumnModel().getColumn(0).setMaxWidth(0);
        myReportsTable.getColumnModel().getColumn(0).setWidth(0);

        myReportsTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        myReportsTable.getColumnModel().getColumn(2).setPreferredWidth(70);
        myReportsTable.getColumnModel().getColumn(3).setPreferredWidth(50);
        myReportsTable.getColumnModel().getColumn(4).setPreferredWidth(50);

        myReportsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = myReportsTable.getSelectedRow();
                    
                    if (selectedRow == lastSelectedRow && selectedRow != -1) {
                        myReportsTable.clearSelection();
                        clearReportDetails();
                        lastSelectedRow = -1;
                    } else if (selectedRow != -1) {
                        int modelRow = myReportsTable.convertRowIndexToModel(selectedRow);
                        displayReportDetails(modelRow);
                        lastSelectedRow = selectedRow;
                    }
                }
            }
        });

        myReportsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    myReportsTable.clearSelection();
                    clearReportDetails();
                    lastSelectedRow = -1;
                }
            }
        });
    }

    private void loadMyReports() {
        myReportsTableModel.setRowCount(0);

        String sql = "SELECT r.report_id, r.report_date, r.report_reason, r.report_status, "
                + "u.user_fullname as reported_trader, r.admin_notes "
                + "FROM tbl_reports r "
                + "JOIN tbl_users u ON r.reported_trader_id = u.user_id "
                + "WHERE r.reporter_id = ? "
                + "ORDER BY r.report_date DESC";

        List<Map<String, Object>> reports = db.fetchRecords(sql, traderId);

        for (Map<String, Object> report : reports) {
            String status = report.get("report_status") != null ? report.get("report_status").toString() : "pending";
            String displayStatus = "";
            
            switch (status.toLowerCase()) {
                case "resolved":
                    displayStatus = "Resolved";
                    break;
                case "under_review":
                    displayStatus = "Under Review";
                    break;
                case "pending":
                default:
                    displayStatus = "Pending";
                    break;
            }

            myReportsTableModel.addRow(new Object[]{
                report.get("report_id"),
                formatDate(report.get("report_date")),
                report.get("report_reason"),
                displayStatus,
                report.get("reported_trader")
            });
        }
    }

    private void loadTraders() {
        traderComboBox.removeAllItems();
        traderComboBox.addItem("-- Select Trader to Report --");

        String sql = "SELECT user_id, user_fullname FROM tbl_users WHERE user_id != ? AND user_type = 'trader' AND user_status = 'active' ORDER BY user_fullname";
        List<Map<String, Object>> traders = db.fetchRecords(sql, traderId);

        for (Map<String, Object> trader : traders) {
            String displayName = trader.get("user_fullname").toString();
            traderComboBox.addItem(displayName);
        }
    }

    private void displayReportDetails(int modelRow) {
        selectedReportId = Integer.parseInt(myReportsTableModel.getValueAt(modelRow, 0).toString());
        String status = myReportsTableModel.getValueAt(modelRow, 3).toString();
        selectedReportStatus = status;
        
        String sql = "SELECT admin_notes, report_status FROM tbl_reports WHERE report_id = ?";
        List<Map<String, Object>> report = db.fetchRecords(sql, selectedReportId);

        if (!report.isEmpty()) {
            String adminNotes = report.get(0).get("admin_notes") != null ? 
                report.get(0).get("admin_notes").toString() : "";
            String reportStatus = report.get(0).get("report_status") != null ? 
                report.get(0).get("report_status").toString() : "pending";

            if (adminNotes != null && !adminNotes.isEmpty() && !adminNotes.equals("No admin reply yet.")) {
                viewRespondButton.setEnabled(true);
            } else {
                viewRespondButton.setEnabled(false);
            }

            String statusDisplay = "";
            switch (reportStatus.toLowerCase()) {
                case "resolved":
                    statusDisplay = "RESOLVED";
                    cancelReportButton.setEnabled(false);
                    break;
                case "under_review":
                    statusDisplay = "UNDER REVIEW";
                    cancelReportButton.setEnabled(true);
                    break;
                case "pending":
                default:
                    statusDisplay = "PENDING";
                    cancelReportButton.setEnabled(true);
                    break;
            }
        }
    }

    private void clearReportDetails() {
        selectedReportId = -1;
        selectedReportStatus = "";
        viewRespondButton.setEnabled(false);
        cancelReportButton.setEnabled(false);
    }

    private void viewRespond() {
        if (selectedReportId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a report first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "SELECT admin_notes, report_status FROM tbl_reports WHERE report_id = ?";
        List<Map<String, Object>> report = db.fetchRecords(sql, selectedReportId);

        if (!report.isEmpty()) {
            String adminNotes = report.get(0).get("admin_notes") != null ? 
                report.get(0).get("admin_notes").toString() : "";
            String reportStatus = report.get(0).get("report_status") != null ? 
                report.get(0).get("report_status").toString() : "pending";

            if (adminNotes != null && !adminNotes.isEmpty() && !adminNotes.equals("No admin reply yet.")) {
                JDialog respondDialog = new JDialog(this, "Admin Response", true);
                respondDialog.setSize(500, 450);
                respondDialog.setLayout(null);
                respondDialog.setLocationRelativeTo(this);
                respondDialog.getContentPane().setBackground(Color.WHITE);

                JPanel titlePanel = new JPanel();
                titlePanel.setBackground(themeColor);
                titlePanel.setBounds(0, 0, 500, 45);
                titlePanel.setLayout(null);

                JLabel titleLabel = new JLabel("ADMIN RESPONSE");
                titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
                titleLabel.setForeground(Color.WHITE);
                titleLabel.setBounds(20, 8, 200, 30);
                titlePanel.add(titleLabel);
                respondDialog.add(titlePanel);

                JLabel reportLabel = new JLabel("Report #" + selectedReportId);
                reportLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
                reportLabel.setForeground(accentColor);
                reportLabel.setBounds(20, 65, 200, 25);
                respondDialog.add(reportLabel);

                String statusText = "";
                Color statusColor = null;
                switch (reportStatus.toLowerCase()) {
                    case "resolved":
                        statusText = "RESOLVED";
                        statusColor = successColor;
                        break;
                    case "under_review":
                        statusText = "UNDER REVIEW";
                        statusColor = warningColor;
                        break;
                    default:
                        statusText = "PENDING";
                        statusColor = errorColor;
                        break;
                }
                
                JLabel statusLabel = new JLabel("Status: " + statusText);
                statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
                statusLabel.setForeground(statusColor);
                statusLabel.setBounds(20, 95, 200, 25);
                respondDialog.add(statusLabel);

                JLabel respondLabel = new JLabel("Admin Message:");
                respondLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
                respondLabel.setBounds(20, 135, 150, 25);
                respondDialog.add(respondLabel);

                JTextArea respondArea = new JTextArea();
                respondArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                respondArea.setLineWrap(true);
                respondArea.setWrapStyleWord(true);
                respondArea.setEditable(false);
                respondArea.setBackground(new Color(250, 250, 250));
                respondArea.setText(adminNotes);
                JScrollPane respondScrollPane = new JScrollPane(respondArea);
                respondScrollPane.setBounds(20, 165, 460, 150);
                respondScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
                respondDialog.add(respondScrollPane);

                JButton closeButton = new JButton("CLOSE");
                closeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
                closeButton.setBackground(accentColor);
                closeButton.setForeground(Color.WHITE);
                closeButton.setBounds(200, 340, 100, 35);
                closeButton.setBorder(null);
                closeButton.setFocusPainted(false);
                closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                closeButton.addActionListener(e -> respondDialog.dispose());
                respondDialog.add(closeButton);

                respondDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "No admin response available for this report yet.\n\n"
                    + "The admin will review your report and provide a response.\n"
                    + "Please check back later.",
                    "No Response",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void submitReport() {
        int selectedTraderIndex = traderComboBox.getSelectedIndex();
        if (selectedTraderIndex == 0) {
            JOptionPane.showMessageDialog(this, "Please select a trader to report.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int selectedReasonIndex = reasonComboBox.getSelectedIndex();
        if (selectedReasonIndex == 0) {
            JOptionPane.showMessageDialog(this, "Please select a reason for the report.", "No Reason", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String description = descriptionArea.getText().trim();
        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please provide a description of the issue.", "No Description", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedTraderName = traderComboBox.getSelectedItem().toString();
        
        String getTraderIdSql = "SELECT user_id FROM tbl_users WHERE user_fullname = ? AND user_type = 'trader'";
        List<Map<String, Object>> traders = db.fetchRecords(getTraderIdSql, selectedTraderName);

        if (traders.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Error: Trader not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int reportedTraderId = Integer.parseInt(traders.get(0).get("user_id").toString());
        String reason = reasonComboBox.getSelectedItem().toString();

        String checkSql = "SELECT COUNT(*) as count FROM tbl_reports WHERE reporter_id = ? AND reported_trader_id = ? AND report_status IN ('pending', 'under_review')";
        double pendingCount = db.getSingleValue(checkSql, traderId, reportedTraderId);

        if (pendingCount > 0) {
            JOptionPane.showMessageDialog(this, 
                "You already have a pending report against this trader.\nPlease wait for admin resolution.",
                "Duplicate Report", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String insertSql = "INSERT INTO tbl_reports (reporter_id, reported_trader_id, report_reason, report_description, report_date, report_status) "
                + "VALUES (?, ?, ?, ?, datetime('now'), 'pending')";

        try {
            db.addRecord(insertSql, traderId, reportedTraderId, reason, description);
            
            JOptionPane.showMessageDialog(this, 
                "Report submitted successfully!\n\n"
                + "Reported Trader: " + selectedTraderName + "\n"
                + "Reason: " + reason + "\n\n"
                + "An admin will review your report.",
                "Success", JOptionPane.INFORMATION_MESSAGE);

            traderComboBox.setSelectedIndex(0);
            reasonComboBox.setSelectedIndex(0);
            descriptionArea.setText("");
            
            loadMyReports();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to submit report: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelReport() {
        if (selectedReportId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a report to cancel.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!selectedReportStatus.equalsIgnoreCase("Pending") && !selectedReportStatus.equalsIgnoreCase("Under Review")) {
            JOptionPane.showMessageDialog(this, 
                "This report cannot be cancelled as it is already " + selectedReportStatus + ".\n\n"
                + "It will be removed from your view only.",
                "Cannot Cancel", JOptionPane.INFORMATION_MESSAGE);
            
            removeFromDisplay();
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel this report?\n\n"
                + "Report ID: " + selectedReportId + "\n"
                + "Status: " + selectedReportStatus,
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM tbl_reports WHERE report_id = ? AND reporter_id = ? AND report_status IN ('pending', 'under_review')";
                db.deleteRecord(sql, selectedReportId, traderId);
                
                JOptionPane.showMessageDialog(this, 
                    "Report cancelled successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                loadMyReports();
                clearReportDetails();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to cancel report: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removeFromDisplay() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Remove this report from your view?\n\n"
                + "Report ID: " + selectedReportId + "\n"
                + "Status: " + selectedReportStatus + "\n\n"
                + "Note: This will only hide it from your list. The report will still be in the database.",
                "Confirm Remove",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            int currentRow = myReportsTable.getSelectedRow();
            if (currentRow != -1) {
                int modelRow = myReportsTable.convertRowIndexToModel(currentRow);
                myReportsTableModel.removeRow(modelRow);
                clearReportDetails();
                
                JOptionPane.showMessageDialog(this, 
                    "Report removed from display.",
                    "Removed", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void refreshReports() {
        loadMyReports();
        loadTraders();
        clearReportDetails();
        JOptionPane.showMessageDialog(this, "Reports refreshed!", "Refresh", JOptionPane.INFORMATION_MESSAGE);
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
        } else if (panel == messagesPanel) {
            messages messagesFrame = new messages(traderId, traderName);
            messagesFrame.setVisible(true);
            messagesFrame.setLocationRelativeTo(null);
            this.dispose();
        } else if (panel == settingsPanel) {
            settings settingsFrame = new settings(traderId, traderName);
            settingsFrame.setVisible(true);
            settingsFrame.setLocationRelativeTo(null);
            this.dispose();
        }
    }
}