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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class profile extends javax.swing.JFrame {

    private user_session session;
    private config db;
    private IconManager iconManager;
    private String selectedImagePath = "";
    private int traderId;
    private String traderName;

    private JPanel sidePanel;
    private JButton backButton;
    private JPanel profileOptionPanel;
    private JLabel profileIcon;
    private JLabel profileLabel;
    private JPanel privacyOptionPanel;
    private JLabel privacyIcon;
    private JLabel privacyLabel;
    private JPanel logoutOptionPanel;
    private JLabel logoutIcon;
    private JLabel logoutLabel;
    
    private JPanel headerPanel;
    private JLabel headerTitle;
    private JLabel currentDateLabel;
    
    private JPanel contentPanel;
    private JPanel profileCard;
    private JPanel avatarPanel;
    private JLabel avatarLabel;
    private JLabel avatarInitialLabel;
    private JButton changePhotoButton;
    
    private JPanel detailsPanel;
    private JLabel fullNameLabel;
    private JLabel fullNameValue;
    private JLabel usernameLabel;
    private JLabel usernameValue;
    private JLabel emailLabel;
    private JLabel emailValue;
    private JLabel accountTypeLabel;
    private JLabel accountTypeValue;
    private JLabel statusLabel;
    private JLabel statusValue;
    private JLabel memberSinceLabel;
    private JLabel memberSinceValue;
    
    private JButton editProfileButton;
    
    private Color themeColor = new Color(12, 192, 223);
    private Color hoverColor = new Color(70, 210, 235);
    private Color headerBgColor = new Color(245, 245, 245);
    private Color textColor = new Color(80, 80, 80);
    private Color accentColor = new Color(0, 102, 102);
    private Color initialColor = new Color(0, 102, 102);
    private Color cardBgColor = new Color(250, 250, 250);
    private Color borderColor = new Color(220, 220, 220);
    
    private JPanel currentHoverPanel = null;
    
    private static final String PROFILE_IMAGE_PATH = "src/BarterZone/resources/images/";

    public profile() {
        this.session = user_session.getInstance();
        this.db = new config();
        this.iconManager = IconManager.getInstance();
        
        if (!session.isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "No active session. Please login again.", "Session Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        this.traderId = session.getUserId();
        this.traderName = session.getFullName();
        
        initComponents();
        initializeIconLabels();
        loadAndResizeIcons();
        setupSidePanel();
        setupHeader();
        setupContentPanel();
        loadUserData();
        
        setTitle("BarterZone - " + traderName);
        setIconImage(new ImageIcon(getClass().getResource(
                "/BarterZone/resources/icon/logo.png")).getImage());
        setSize(800, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        createImageDirectory();
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
    
    private void createImageDirectory() {
        File directory = new File(PROFILE_IMAGE_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private void initializeIconLabels() {
        profileIcon = new JLabel();
        privacyIcon = new JLabel();
        logoutIcon = new JLabel();
    }

    private void loadAndResizeIcons() {
        setIconSafely(profileIcon, iconManager.getSideMenuIcon("profile"));
        setIconSafely(privacyIcon, iconManager.getSideMenuIcon("privacy"));
        setIconSafely(logoutIcon, iconManager.getSideMenuIcon("logout"));
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
                System.out.println("File not found: " + imagePath);
                return null;
            }
            
            BufferedImage originalImage = javax.imageio.ImageIO.read(file);
            if (originalImage == null) {
                System.out.println("Failed to read image: " + imagePath);
                return null;
            }
            
            BufferedImage circularImage = createCircularImage(originalImage, size);
            return new ImageIcon(circularImage);
            
        } catch (Exception e) {
            System.out.println("Error loading circular image: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void loadAvatar() {
        try {
            String sql = "SELECT user_profile_picture FROM tbl_users WHERE user_id = ?";
            List<Map<String, Object>> result = db.fetchRecords(sql, traderId);

            if (!result.isEmpty() && result.get(0).get("user_profile_picture") != null) {
                String profilePicPath = result.get(0).get("user_profile_picture").toString().trim();

                if (!profilePicPath.isEmpty()) {
                    String fullPath = convertResourcePathToFilePath(profilePicPath);

                    if (fullPath != null) {
                        ImageIcon circularIcon = loadAndCircleImage(fullPath, 100);
                        
                        if (circularIcon != null && circularIcon.getIconWidth() > 0) {
                            avatarLabel.setIcon(circularIcon);
                            avatarLabel.setText("");
                            avatarInitialLabel.setVisible(false);
//                            System.out.println("Avatar loaded successfully from: " + fullPath);
                            return;
                        } else {
                            System.out.println("Failed to create circular image");
                        }
                    } else {
                        System.out.println("Failed to convert path: " + profilePicPath);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading profile image: " + e.getMessage());
            e.printStackTrace();
        }

        avatarLabel.setIcon(null);
        if (traderName != null && !traderName.trim().isEmpty()) {
            avatarInitialLabel.setText(String.valueOf(traderName.trim().charAt(0)).toUpperCase());
            avatarInitialLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
            avatarInitialLabel.setForeground(initialColor);
        } else {
            avatarInitialLabel.setText("U");
        }
        avatarInitialLabel.setVisible(true);
        System.out.println("Showing initial: " + avatarInitialLabel.getText());
    }

    private void setupSidePanel() {
        backButton = new JButton("← Back");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        backButton.setBackground(accentColor);
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(30, 20, 100, 30);
        backButton.setBorder(null);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> goBackToSettings());
        sidePanel.add(backButton);
        
        int menuY = 80;
        int menuHeight = 40;
        int menuSpacing = 5;
        
        profileOptionPanel = createOptionPanel(20, menuY, 140, menuHeight);
        profileIcon = createOptionIcon(profileOptionPanel, 15, 10, profileIcon);
        profileLabel = createOptionLabel(profileOptionPanel, "Profile", 45, 12);
        menuY += menuHeight + menuSpacing;
        
        privacyOptionPanel = createOptionPanel(20, menuY, 140, menuHeight);
        privacyIcon = createOptionIcon(privacyOptionPanel, 15, 10, privacyIcon);
        privacyLabel = createOptionLabel(privacyOptionPanel, "Privacy", 45, 12);
        menuY += menuHeight + menuSpacing;
        
        logoutOptionPanel = createOptionPanel(20, menuY, 140, menuHeight);
        logoutIcon = createOptionIcon(logoutOptionPanel, 15, 10, logoutIcon);
        logoutLabel = createOptionLabel(logoutOptionPanel, "Logout", 45, 12);
    }
    
    private JPanel createOptionPanel(int x, int y, int width, int height) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(themeColor);
        panel.setBounds(x, y, width, height);
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (currentHoverPanel != null && currentHoverPanel != panel) {
                    currentHoverPanel.setBackground(themeColor);
                }
                panel.setBackground(hoverColor);
                currentHoverPanel = panel;
            }
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(themeColor);
                if (currentHoverPanel == panel) {
                    currentHoverPanel = null;
                }
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                handleOptionClick(panel);
            }
        };
        
        panel.addMouseListener(adapter);
        sidePanel.add(panel);
        return panel;
    }
    
    private JLabel createOptionIcon(JPanel panel, int x, int y, JLabel iconLabel) {
        iconLabel.setBounds(x, y, 25, 20);
        iconLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        iconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (currentHoverPanel != null && currentHoverPanel != panel) {
                    currentHoverPanel.setBackground(themeColor);
                }
                panel.setBackground(hoverColor);
                currentHoverPanel = panel;
            }
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(themeColor);
                if (currentHoverPanel == panel) {
                    currentHoverPanel = null;
                }
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                handleOptionClick(panel);
            }
        });
        
        panel.add(iconLabel);
        return iconLabel;
    }
    
    private JLabel createOptionLabel(JPanel panel, String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(Color.WHITE);
        label.setBounds(x, y, 100, 20);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (currentHoverPanel != null && currentHoverPanel != panel) {
                    currentHoverPanel.setBackground(themeColor);
                }
                panel.setBackground(hoverColor);
                currentHoverPanel = panel;
            }
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(themeColor);
                if (currentHoverPanel == panel) {
                    currentHoverPanel = null;
                }
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                handleOptionClick(panel);
            }
        });
        
        panel.add(label);
        return label;
    }
    
    private void handleOptionClick(JPanel panel) {
        if (panel == privacyOptionPanel) {
            showPrivacyMessage();
        } else if (panel == logoutOptionPanel) {
            logout();
        }
    }

    private void setupHeader() {
        headerTitle = new JLabel("My Profile");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerTitle.setForeground(accentColor);
        headerTitle.setBounds(30, 15, 200, 40);
        headerPanel.add(headerTitle);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        currentDateLabel = new JLabel(sdf.format(new Date()));
        currentDateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        currentDateLabel.setForeground(new Color(102, 102, 102));
        currentDateLabel.setBounds(400, 25, 200, 30);
        headerPanel.add(currentDateLabel);
    }

    private void setupContentPanel() {
        profileCard = new JPanel();
        profileCard.setLayout(null);
        profileCard.setBackground(Color.WHITE);
        profileCard.setBorder(new LineBorder(borderColor, 1));
        profileCard.setBounds(20, 20, 580, 390);
        contentPanel.add(profileCard);
        
        avatarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillOval(0, 0, 100, 100);
                g2.setColor(accentColor);
                g2.setStroke(new java.awt.BasicStroke(2));
                g2.drawOval(0, 0, 100, 100);
            }
        };
        avatarPanel.setLayout(null);
        avatarPanel.setBounds(240, 20, 100, 100);
        avatarPanel.setOpaque(false);
        profileCard.add(avatarPanel);
        
        avatarLabel = new JLabel();
        avatarLabel.setBounds(0, 0, 100, 100);
        avatarLabel.setHorizontalAlignment(JLabel.CENTER);
        avatarLabel.setVerticalAlignment(JLabel.CENTER);
        avatarPanel.add(avatarLabel);
        
        avatarInitialLabel = new JLabel();
        avatarInitialLabel.setBounds(0, 0, 100, 100);
        avatarInitialLabel.setHorizontalAlignment(JLabel.CENTER);
        avatarInitialLabel.setVerticalAlignment(JLabel.CENTER);
        avatarInitialLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        avatarInitialLabel.setForeground(accentColor);
        avatarPanel.add(avatarInitialLabel);
        
        loadAvatar();
        
        changePhotoButton = new JButton("Change Photo");
        changePhotoButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        changePhotoButton.setBackground(themeColor);
        changePhotoButton.setForeground(Color.WHITE);
        changePhotoButton.setBounds(240, 130, 100, 28);
        changePhotoButton.setBorder(null);
        changePhotoButton.setFocusPainted(false);
        changePhotoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        changePhotoButton.addActionListener(e -> changeProfilePhoto());
        profileCard.add(changePhotoButton);
        
        detailsPanel = new JPanel();
        detailsPanel.setLayout(null);
        detailsPanel.setBackground(cardBgColor);
        detailsPanel.setBorder(new LineBorder(borderColor, 1));
        detailsPanel.setBounds(20, 175, 540, 175);
        profileCard.add(detailsPanel);
        
        int labelWidth = 100;
        int valueWidth = 380;
        int labelX = 20;
        int valueX = 130;
        int startY = 15;
        int rowHeight = 25;
        
        fullNameLabel = new JLabel("Full Name:");
        fullNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        fullNameLabel.setForeground(textColor);
        fullNameLabel.setBounds(labelX, startY, labelWidth, 20);
        detailsPanel.add(fullNameLabel);
        
        fullNameValue = new JLabel();
        fullNameValue.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        fullNameValue.setForeground(accentColor);
        fullNameValue.setBounds(valueX, startY, valueWidth, 20);
        detailsPanel.add(fullNameValue);
        startY += rowHeight;
        
        usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        usernameLabel.setForeground(textColor);
        usernameLabel.setBounds(labelX, startY, labelWidth, 20);
        detailsPanel.add(usernameLabel);
        
        usernameValue = new JLabel();
        usernameValue.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        usernameValue.setForeground(textColor);
        usernameValue.setBounds(valueX, startY, valueWidth, 20);
        detailsPanel.add(usernameValue);
        startY += rowHeight;
        
        emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        emailLabel.setForeground(textColor);
        emailLabel.setBounds(labelX, startY, labelWidth, 20);
        detailsPanel.add(emailLabel);
        
        emailValue = new JLabel();
        emailValue.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        emailValue.setForeground(textColor);
        emailValue.setBounds(valueX, startY, valueWidth, 20);
        detailsPanel.add(emailValue);
        startY += rowHeight;
        
        accountTypeLabel = new JLabel("Account Type:");
        accountTypeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        accountTypeLabel.setForeground(textColor);
        accountTypeLabel.setBounds(labelX, startY, labelWidth, 20);
        detailsPanel.add(accountTypeLabel);
        
        accountTypeValue = new JLabel();
        accountTypeValue.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        accountTypeValue.setForeground(textColor);
        accountTypeValue.setBounds(valueX, startY, valueWidth, 20);
        detailsPanel.add(accountTypeValue);
        startY += rowHeight;
        
        statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setForeground(textColor);
        statusLabel.setBounds(labelX, startY, labelWidth, 20);
        detailsPanel.add(statusLabel);
        
        statusValue = new JLabel();
        statusValue.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusValue.setBounds(valueX, startY, valueWidth, 20);
        detailsPanel.add(statusValue);
        startY += rowHeight;
        
        memberSinceLabel = new JLabel("Member Since:");
        memberSinceLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        memberSinceLabel.setForeground(textColor);
        memberSinceLabel.setBounds(labelX, startY, labelWidth, 20);
        detailsPanel.add(memberSinceLabel);
        
        memberSinceValue = new JLabel();
        memberSinceValue.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        memberSinceValue.setForeground(textColor);
        memberSinceValue.setBounds(valueX, startY, valueWidth, 20);
        detailsPanel.add(memberSinceValue);
        
        editProfileButton = new JButton("EDIT PROFILE");
        editProfileButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        editProfileButton.setBackground(accentColor);
        editProfileButton.setForeground(Color.WHITE);
        editProfileButton.setBounds(240, 355, 100, 30);
        editProfileButton.setBorder(null);
        editProfileButton.setFocusPainted(false);
        editProfileButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editProfileButton.addActionListener(e -> openEditProfile());
        profileCard.add(editProfileButton);
    }

    private void loadUserData() {
        Map<String, Object> userData = session.getAllUserData();
        
        fullNameValue.setText((String) userData.get("user_fullname"));
        usernameValue.setText((String) userData.get("user_username"));
        emailValue.setText((String) userData.get("user_email"));
        
        String type = (String) userData.get("user_type");
        if (type != null) {
            accountTypeValue.setText(type.substring(0, 1).toUpperCase() + type.substring(1));
        }
        
        String status = (String) userData.get("user_status");
        if (status != null) {
            if (status.equalsIgnoreCase("active")) {
                statusValue.setText("Active");
                statusValue.setForeground(new Color(46, 125, 50));
            } else {
                statusValue.setText("Inactive");
                statusValue.setForeground(new Color(204, 0, 0));
            }
        }
        
        String memberSince = getMemberSinceDate();
        memberSinceValue.setText(memberSince);
    }

    private String getMemberSinceDate() {
        try {
            String sql = "SELECT created_date FROM tbl_users WHERE user_id = ?";
            List<Map<String, Object>> result = db.fetchRecords(sql, traderId);
            
            if (!result.isEmpty() && result.get(0).get("created_date") != null) {
                String dateStr = result.get(0).get("created_date").toString();
                if (dateStr.length() >= 10) {
                    String[] dateParts = dateStr.substring(0, 10).split("-");
                    if (dateParts.length == 3) {
                        int year = Integer.parseInt(dateParts[0]);
                        int month = Integer.parseInt(dateParts[1]);
                        int day = Integer.parseInt(dateParts[2]);
                        
                        String[] monthNames = {"January", "February", "March", "April", "May", "June",
                                              "July", "August", "September", "October", "November", "December"};
                        
                        return monthNames[month - 1] + " " + day + ", " + year;
                    }
                }
                return dateStr.substring(0, 10);
            }
            return "N/A";
        } catch (Exception e) {
            return "N/A";
        }
    }

    private String getUniqueFileName(String originalFileName) {
        File directory = new File(PROFILE_IMAGE_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        String nameWithoutExt = originalFileName;
        String extension = "";
        
        int lastDot = originalFileName.lastIndexOf(".");
        if (lastDot > 0) {
            nameWithoutExt = originalFileName.substring(0, lastDot);
            extension = originalFileName.substring(lastDot);
        }
        
        String destinationPath = PROFILE_IMAGE_PATH + originalFileName;
        File destFile = new File(destinationPath);
        
        if (!destFile.exists()) {
            return originalFileName;
        }
        
        int counter = 1;
        String newFileName;
        File newDestFile;
        
        do {
            newFileName = nameWithoutExt + "_" + counter + extension;
            newDestFile = new File(PROFILE_IMAGE_PATH + newFileName);
            counter++;
        } while (newDestFile.exists());
        
        System.out.println("Duplicate detected. New filename: " + newFileName);
        return newFileName;
    }

    private String saveProfileImage(String sourcePath) {
        if (sourcePath == null || sourcePath.isEmpty()) {
            return "";
        }

        try {
            File directory = new File(PROFILE_IMAGE_PATH);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File sourceFile = new File(sourcePath);
            String originalFileName = sourceFile.getName();
            String uniqueFileName = getUniqueFileName(originalFileName);
            String destinationPath = PROFILE_IMAGE_PATH + uniqueFileName;

            Files.copy(Paths.get(sourcePath), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
            
            File savedFile = new File(destinationPath);
            System.out.println("Image saved to: " + destinationPath);
            System.out.println("Saved filename: " + uniqueFileName);
            
            return "BarterZone.resources.images." + uniqueFileName;
            
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return "";
        }
    }

    private void changeProfilePhoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif", "bmp"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedImagePath = selectedFile.getAbsolutePath();
            
            System.out.println("Selected file: " + selectedImagePath);
            
            String savedPath = saveProfileImage(selectedImagePath);
            
            if (!savedPath.isEmpty()) {
                String sql = "UPDATE tbl_users SET user_profile_picture = ? WHERE user_id = ?";
                db.updateRecord(sql, savedPath, traderId);
                
                session.setProfilePicture(savedPath);
                session.refreshData();
                
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                }
                
                loadAvatar();
                
                avatarPanel.revalidate();
                avatarPanel.repaint();
                avatarLabel.repaint();
                
                JOptionPane.showMessageDialog(this, 
                    "Profile photo updated successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void openEditProfile() {
        edit_profile editFrame = new edit_profile();
        editFrame.setVisible(true);
        editFrame.setLocationRelativeTo(null);
        this.dispose();
    }
    
    private void showPrivacyMessage() {
        JOptionPane.showMessageDialog(this,
            "Privacy Settings\n\n"
            + "This feature is coming soon!\n\n"
            + "You will be able to manage:\n"
            + "Who can view your profile\n"
            + "Data sharing preferences\n"
            + "Account visibility settings\n"
            + "And more...",
            "Privacy Settings",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            session.logout();
            landing.landing landingFrame = new landing.landing();
            landingFrame.setVisible(true);
            landingFrame.setLocationRelativeTo(null);
            this.dispose();
        }
    }
    
    private void goBackToSettings() {
        settings settingsFrame = new settings(traderId, traderName);
        settingsFrame.setVisible(true);
        settingsFrame.setLocationRelativeTo(null);
        this.dispose();
    }
}