package landing;

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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class search_items extends JFrame {

    private String searchQuery;
    private boolean isLoggedIn;
    private int traderId;
    private String traderName;
    private config db;

    private JPanel mainPanel;
    private JPanel headerPanel;
    private JLabel titleLabel;
    private JLabel searchLabel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton backButton;
    private JScrollPane scrollPane;
    private JPanel resultsPanel;

    private Color bgColor = new Color(245, 245, 250);
    private Color accentColor = new Color(12, 192, 223);
    private Color textColor = new Color(80, 80, 80);
    private Color cardBgColor = Color.WHITE;
    private Color primaryColor = new Color(0, 102, 102);
    private Color successColor = new Color(46, 125, 50);
    private Color warningColor = new Color(255, 153, 0);
    private Color buttonColor = new Color(12, 192, 223);
    
    private static final String IMAGE_BASE_PATH = "src/BarterZone/resources/images/items/";

    public search_items(String searchQuery, boolean isLoggedIn, int traderId, String traderName) {
        this.searchQuery = searchQuery;
        this.isLoggedIn = isLoggedIn;
        this.traderId = traderId;
        this.traderName = traderName;
        this.db = new config();

        initComponents();
        loadSearchResults();

        setTitle("BarterZone - Search Results");
        setIconImage(new ImageIcon(getClass().getResource(
                "/BarterZone/resources/icon/logo.png")).getImage());
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(null);
        getContentPane().setBackground(bgColor);

        // Header Panel
        headerPanel = new JPanel();
        headerPanel.setLayout(null);
        headerPanel.setBackground(accentColor);
        headerPanel.setBounds(0, 0, 800, 80);
        headerPanel.setBorder(new LineBorder(new Color(8, 150, 175), 1, true));
        add(headerPanel);

        titleLabel = new JLabel("Search Results");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(30, 20, 300, 40);
        headerPanel.add(titleLabel);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        JLabel dateLabel = new JLabel(sdf.format(new Date()));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setBounds(500, 25, 250, 30);
        headerPanel.add(dateLabel);

        // Search Panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(null);
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBounds(20, 100, 760, 60);
        searchPanel.setBorder(new LineBorder(accentColor, 1));
        add(searchPanel);

        searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchLabel.setForeground(primaryColor);
        searchLabel.setBounds(15, 18, 70, 25);
        searchPanel.add(searchLabel);

        searchField = new JTextField(searchQuery);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBounds(90, 15, 450, 30);
        searchField.setBorder(new LineBorder(new Color(200, 200, 200)));
        searchPanel.add(searchField);

        searchButton = new JButton("Search");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchButton.setBackground(accentColor);
        searchButton.setForeground(Color.WHITE);
        searchButton.setBounds(550, 15, 100, 30);
        searchButton.setBorder(null);
        searchButton.setFocusPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.addActionListener(e -> performNewSearch());
        searchPanel.add(searchButton);

        backButton = new JButton("Back to Home");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        backButton.setBackground(new Color(102, 102, 102));
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(660, 15, 100, 30);
        backButton.setBorder(null);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> goBackToLanding());
        searchPanel.add(backButton);

        // Results Panel with Scroll
        resultsPanel = new JPanel();
        resultsPanel.setLayout(null);
        resultsPanel.setBackground(bgColor);

        scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setBounds(20, 170, 760, 380);
        scrollPane.setBorder(new LineBorder(accentColor, 1));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane);
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

    private void loadSearchResults() {
        resultsPanel.removeAll();

        String sql = "SELECT i.items_id, i.item_Name, i.item_Brand, i.item_Condition, "
                + "i.item_Date, i.item_Description, i.item_picture, "
                + "u.user_fullname as owner_name, u.user_id as owner_id "
                + "FROM tbl_items i "
                + "JOIN tbl_users u ON i.trader_id = u.user_id "
                + "WHERE i.is_active = 1 "
                + "AND (i.item_Name LIKE ? OR i.item_Brand LIKE ? OR i.item_Description LIKE ?) "
                + "ORDER BY i.created_date DESC";

        String searchPattern = "%" + searchQuery + "%";
        List<Map<String, Object>> items = db.fetchRecords(sql, searchPattern, searchPattern, searchPattern);

        if (items.isEmpty()) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setLayout(null);
            emptyPanel.setBackground(cardBgColor);
            emptyPanel.setBorder(new LineBorder(accentColor, 1));
            emptyPanel.setBounds(10, 10, 730, 100);

            JLabel emptyLabel = new JLabel("No items found matching: " + searchQuery);
            emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            emptyLabel.setForeground(textColor);
            emptyLabel.setBounds(200, 35, 400, 30);
            emptyPanel.add(emptyLabel);

            resultsPanel.add(emptyPanel);
            resultsPanel.setPreferredSize(new java.awt.Dimension(740, 130));
        } else {
            int yPos = 10;
            for (Map<String, Object> item : items) {
                JPanel card = createItemCard(item, yPos);
                resultsPanel.add(card);
                yPos += 160;
            }
            resultsPanel.setPreferredSize(new java.awt.Dimension(740, yPos + 10));
        }

        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    private JPanel createItemCard(Map<String, Object> item, int yPos) {
        int itemId = Integer.parseInt(item.get("items_id").toString());
        String itemName = (String) item.get("item_Name");
        String brand = (String) item.get("item_Brand");
        String condition = (String) item.get("item_Condition");
        String date = (String) item.get("item_Date");
        String description = (String) item.get("item_Description");
        String ownerName = (String) item.get("owner_name");
        int ownerId = Integer.parseInt(item.get("owner_id").toString());
        String photoPath = item.get("item_picture") != null ? item.get("item_picture").toString() : "";

        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBackground(cardBgColor);
        card.setBorder(new LineBorder(accentColor, 1));
        card.setBounds(10, yPos, 730, 150);

        // Image Panel
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(null);
        imagePanel.setBackground(new Color(240, 240, 240));
        imagePanel.setBounds(10, 10, 120, 120);
        imagePanel.setBorder(new LineBorder(new Color(200, 200, 200)));
        card.add(imagePanel);

        JLabel imageLabel = new JLabel();
        imageLabel.setBounds(0, 0, 120, 120);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        // Load image properly
        if (!photoPath.isEmpty()) {
            try {
                String filePath = convertResourcePathToFilePath(photoPath);
                if (filePath != null) {
                    File imgFile = new File(filePath);
                    if (imgFile.exists()) {
                        ImageIcon icon = new ImageIcon(filePath);
                        Image img = icon.getImage().getScaledInstance(110, 110, Image.SCALE_SMOOTH);
                        imageLabel.setIcon(new ImageIcon(img));
                        imageLabel.setText("");
                    } else {
                        imageLabel.setText("No Image");
                        imageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                    }
                } else {
                    imageLabel.setText("No Image");
                    imageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                }
            } catch (Exception e) {
                imageLabel.setText("No Image");
                imageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            }
        } else {
            imageLabel.setText("No Image");
            imageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        }
        imagePanel.add(imageLabel);

        // Item Details
        JLabel nameLabel = new JLabel("<html><b>" + itemName + "</b> (" + brand + ")</html>");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameLabel.setForeground(primaryColor);
        nameLabel.setBounds(140, 10, 400, 25);
        card.add(nameLabel);

        JLabel ownerLabel = new JLabel("Owner: " + ownerName);
        ownerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ownerLabel.setForeground(textColor);
        ownerLabel.setBounds(140, 35, 200, 20);
        card.add(ownerLabel);

        JLabel conditionLabel = new JLabel("Condition: " + condition);
        conditionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        conditionLabel.setForeground(textColor);
        conditionLabel.setBounds(140, 55, 200, 20);
        card.add(conditionLabel);

        JLabel dateLabel = new JLabel("Date: " + (date != null ? date : "N/A"));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(textColor);
        dateLabel.setBounds(140, 75, 200, 20);
        card.add(dateLabel);

        // Truncate description if too long
        String displayDesc = description != null ? description : "N/A";
        if (displayDesc.length() > 60) {
            displayDesc = displayDesc.substring(0, 57) + "...";
        }
        JLabel descLabel = new JLabel("<html>Description: " + displayDesc + "</html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        descLabel.setForeground(textColor);
        descLabel.setBounds(140, 95, 400, 40);
        card.add(descLabel);

        // Action Buttons (if logged in and not owner)
        if (isLoggedIn && traderId != ownerId) {
            int buttonWidth = 75;
            int buttonHeight = 32;
            int startX = 560;
            int buttonY = 55;
            int spacing = 5;

            // Trade Button
            JButton tradeButton = new JButton("Trade");
            tradeButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
            tradeButton.setBackground(warningColor);
            tradeButton.setForeground(Color.WHITE);
            tradeButton.setBounds(startX, buttonY, buttonWidth, buttonHeight);
            tradeButton.setBorder(null);
            tradeButton.setFocusPainted(false);
            tradeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            tradeButton.addActionListener(e -> openTrades());
            card.add(tradeButton);

            // Message Button
            JButton messageButton = new JButton("Message");
            messageButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
            messageButton.setBackground(primaryColor);
            messageButton.setForeground(Color.WHITE);
            messageButton.setBounds(startX + buttonWidth + spacing, buttonY, buttonWidth, buttonHeight);
            messageButton.setBorder(null);
            messageButton.setFocusPainted(false);
            messageButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            messageButton.addActionListener(e -> openMessages());
            card.add(messageButton);

            // Give Button
            JButton giveButton = new JButton("Give");
            giveButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
            giveButton.setBackground(successColor);
            giveButton.setForeground(Color.WHITE);
            giveButton.setBounds(startX, buttonY + buttonHeight + spacing, buttonWidth, buttonHeight);
            giveButton.setBorder(null);
            giveButton.setFocusPainted(false);
            giveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            giveButton.addActionListener(e -> openMyItems());
            card.add(giveButton);

            // Want Button
            JButton wantButton = new JButton("Want");
            wantButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
            wantButton.setBackground(accentColor);
            wantButton.setForeground(Color.WHITE);
            wantButton.setBounds(startX + buttonWidth + spacing, buttonY + buttonHeight + spacing, buttonWidth, buttonHeight);
            wantButton.setBorder(null);
            wantButton.setFocusPainted(false);
            wantButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            wantButton.addActionListener(e -> openFindItems());
            card.add(wantButton);
            
        } else if (isLoggedIn && traderId == ownerId) {
            JLabel yourItemLabel = new JLabel("Your Item");
            yourItemLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            yourItemLabel.setForeground(successColor);
            yourItemLabel.setBounds(580, 60, 100, 30);
            card.add(yourItemLabel);
        } else {
            JLabel loginLabel = new JLabel("Login to trade");
            loginLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            loginLabel.setForeground(errorColor);
            loginLabel.setBounds(580, 60, 150, 30);
            card.add(loginLabel);
        }

        return card;
    }

    private void performNewSearch() {
        String newQuery = searchField.getText().trim();
        if (!newQuery.isEmpty()) {
            search_items newSearch = new search_items(newQuery, isLoggedIn, traderId, traderName);
            newSearch.setVisible(true);
            newSearch.setLocationRelativeTo(null);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a search term.", "Search", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void goBackToLanding() {
        if (isLoggedIn) {
            landing landingFrame = new landing(traderId, traderName);
            landingFrame.setVisible(true);
            landingFrame.setLocationRelativeTo(null);
        } else {
            landing landingFrame = new landing();
            landingFrame.setVisible(true);
            landingFrame.setLocationRelativeTo(null);
        }
        dispose();
    }
    
    private void openTrades() {
        if (isLoggedIn && traderId != -1) {
            BarterZone.Dashboard.trader.trades tradesFrame = new BarterZone.Dashboard.trader.trades(traderId, traderName);
            tradesFrame.setVisible(true);
            tradesFrame.setLocationRelativeTo(null);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Please login first to access trades.", "Login Required", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void openMessages() {
        if (isLoggedIn && traderId != -1) {
            BarterZone.Dashboard.trader.messages messagesFrame = new BarterZone.Dashboard.trader.messages(traderId, traderName);
            messagesFrame.setVisible(true);
            messagesFrame.setLocationRelativeTo(null);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Please login first to access messages.", "Login Required", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void openMyItems() {
        if (isLoggedIn && traderId != -1) {
            BarterZone.Dashboard.trader.myitems myItemsFrame = new BarterZone.Dashboard.trader.myitems(traderId, traderName);
            myItemsFrame.setVisible(true);
            myItemsFrame.setLocationRelativeTo(null);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Please login first to access your items.", "Login Required", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void openFindItems() {
        if (isLoggedIn && traderId != -1) {
            BarterZone.Dashboard.trader.finditems findItemsFrame = new BarterZone.Dashboard.trader.finditems(traderId, traderName);
            findItemsFrame.setVisible(true);
            findItemsFrame.setLocationRelativeTo(null);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Please login first to find items.", "Login Required", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private Color errorColor = new Color(204, 0, 0);
}