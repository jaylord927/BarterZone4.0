package BarterZone.Landing;

import database.config.config;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;

public class announcement extends javax.swing.JFrame {

    private config db;
    private JPanel contentPanel;
    private JScrollPane mainScrollPane;
    private JPanel announcementsContainer;
    private JLabel headerTitle;
    private JLabel currentDateLabel;
    private JButton backButton;
    private JButton refreshButton;

    // Colors - Trader theme (light blue/white)
    private Color headerColor = new Color(12, 192, 223);
    private Color accentColor = new Color(0, 102, 102);
    private Color cardBgColor = new Color(250, 250, 250);
    private Color borderColor = new Color(12, 192, 223);
    private Color textColor = new Color(80, 80, 80);
    private Color titleColor = new Color(0, 102, 102);
    
    // Type colors
    private Color generalColor = new Color(0, 102, 102);
    private Color featureColor = new Color(46, 125, 50);
    private Color scammerColor = new Color(204, 0, 0);
    private Color maintenanceColor = new Color(255, 153, 0);
    private Color policyColor = new Color(106, 27, 154);
    private Color warningColor = new Color(255, 87, 34);

    public announcement() {
        this.db = new config();
        
        initComponents();
        setupHeader();
        loadAnnouncements();
        
        setTitle("Announcements");
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(null);
        headerPanel.setBackground(headerColor);
        headerPanel.setBounds(0, 0, 800, 70);
        headerPanel.setBorder(new LineBorder(new Color(8, 150, 175), 1, true));
        getContentPane().add(headerPanel);

        headerTitle = new JLabel("Announcements");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerTitle.setForeground(Color.WHITE);
        headerTitle.setBounds(30, 15, 300, 40);
        headerPanel.add(headerTitle);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        currentDateLabel = new JLabel(sdf.format(new Date()));
        currentDateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        currentDateLabel.setForeground(Color.WHITE);
        currentDateLabel.setBounds(450, 25, 250, 30);
        headerPanel.add(currentDateLabel);

        backButton = new JButton("← Back");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        backButton.setBackground(new Color(0, 102, 102));
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(700, 20, 80, 30);
        backButton.setBorder(null);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> dispose());
        headerPanel.add(backButton);

        refreshButton = new JButton("↻");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        refreshButton.setBackground(new Color(0, 102, 102));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBounds(670, 20, 30, 30);
        refreshButton.setBorder(null);
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> {
            loadAnnouncements();
            JOptionPane.showMessageDialog(this, "Announcements refreshed!", "Refresh", JOptionPane.INFORMATION_MESSAGE);
        });
        headerPanel.add(refreshButton);

        // Main Content Panel with Scroll
        contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBounds(0, 70, 800, 530);

        mainScrollPane = new JScrollPane();
        mainScrollPane.setBounds(20, 20, 760, 490);
        mainScrollPane.setBorder(new LineBorder(headerColor, 1));
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        announcementsContainer = new JPanel();
        announcementsContainer.setLayout(null);
        announcementsContainer.setBackground(Color.WHITE);
        
        mainScrollPane.setViewportView(announcementsContainer);
        contentPanel.add(mainScrollPane);
        
        getContentPane().add(contentPanel);
    }

    private void setupHeader() {
        // Header already set up in initComponents
    }

    private void loadAnnouncements() {
        announcementsContainer.removeAll();
        announcementsContainer.setPreferredSize(new java.awt.Dimension(740, 100));

        String sql = "SELECT a.announcement_id, a.title, a.message, a.announcement_date, a.type, "
                + "u.user_fullname as admin_name "
                + "FROM tbl_announcement a "
                + "LEFT JOIN tbl_users u ON a.admin_id = u.user_id "
                + "WHERE a.is_active = 1 "
                + "ORDER BY a.announcement_date DESC";

        List<Map<String, Object>> announcements = db.fetchRecords(sql);

        if (announcements.isEmpty()) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setLayout(null);
            emptyPanel.setBackground(cardBgColor);
            emptyPanel.setBorder(new LineBorder(borderColor, 1));
            emptyPanel.setBounds(10, 10, 720, 100);
            
            JLabel emptyLabel = new JLabel("No announcements at this time.");
            emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            emptyLabel.setForeground(textColor);
            emptyLabel.setBounds(250, 35, 300, 30);
            emptyPanel.add(emptyLabel);
            
            announcementsContainer.add(emptyPanel);
            announcementsContainer.setPreferredSize(new java.awt.Dimension(740, 130));
        } else {
            int yPos = 10;
            for (Map<String, Object> ann : announcements) {
                JPanel card = createAnnouncementCard(ann, yPos);
                announcementsContainer.add(card);
                yPos += getCardHeight(ann) + 15;
            }
            announcementsContainer.setPreferredSize(new java.awt.Dimension(740, yPos + 10));
        }

        announcementsContainer.revalidate();
        announcementsContainer.repaint();
    }

    private JPanel createAnnouncementCard(Map<String, Object> ann, int yPos) {
        String title = (String) ann.get("title");
        String message = (String) ann.get("message");
        String date = formatDateTime(ann.get("announcement_date"));
        String type = (String) ann.get("type");
        String admin = (String) ann.get("admin_name");
        
        if (type == null) type = "General Announcement";
        if (admin == null) admin = "Administrator";

        // Calculate heights based on content
        int titleHeight = 25;
        int messageHeight = calculateMessageHeight(message);
        int typeHeight = 20;
        int dateHeight = 20;
        int padding = 30;
        int cardHeight = titleHeight + messageHeight + typeHeight + dateHeight + padding + 30;

        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBackground(cardBgColor);
        card.setBorder(new LineBorder(getTypeColor(type), 2));
        card.setBounds(10, yPos, 720, cardHeight);

        // Type indicator
        JLabel typeLabel = new JLabel("🔔 " + type);
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        typeLabel.setForeground(getTypeColor(type));
        typeLabel.setBounds(15, 10, 200, typeHeight);
        card.add(typeLabel);

        // Date
        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        dateLabel.setForeground(textColor);
        dateLabel.setBounds(550, 10, 150, dateHeight);
        card.add(dateLabel);

        // Title
        JLabel titleLabel = new JLabel("<html><b>" + title + "</b></html>");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(titleColor);
        titleLabel.setBounds(15, 35, 690, titleHeight);
        card.add(titleLabel);

        // Message
        JLabel messageLabel = new JLabel("<html>" + message.replace("\n", "<br>") + "</html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageLabel.setForeground(textColor);
        messageLabel.setBounds(15, 65, 690, messageHeight);
        card.add(messageLabel);

        // Posted by
        JLabel postedLabel = new JLabel("Posted by: " + admin);
        postedLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        postedLabel.setForeground(textColor);
        postedLabel.setBounds(15, cardHeight - 35, 200, 20);
        card.add(postedLabel);

        return card;
    }

    private int calculateMessageHeight(String message) {
        if (message == null) return 50;
        
        // Rough estimation: average 50 characters per line, 20px per line
        int charsPerLine = 80;
        int lines = message.length() / charsPerLine + 1;
        return lines * 20 + 10;
    }

    private int getCardHeight(Map<String, Object> ann) {
        String message = (String) ann.get("message");
        return calculateMessageHeight(message) + 130;
    }

    private Color getTypeColor(String type) {
        if (type == null) return generalColor;
        
        if (type.contains("General")) return generalColor;
        if (type.contains("Feature")) return featureColor;
        if (type.contains("Scammer")) return scammerColor;
        if (type.contains("Maintenance")) return maintenanceColor;
        if (type.contains("Policy")) return policyColor;
        if (type.contains("Warning")) return warningColor;
        return generalColor;
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
}