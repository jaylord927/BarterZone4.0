package BarterZone.Dashboard.trader;

import BarterZone.resources.IconManager;
import BarterZone.Dashboard.session.user_session;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Cursor;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class profile extends javax.swing.JFrame {

    private user_session session;
    private IconManager iconManager;
    private String selectedImagePath = "";

    private javax.swing.JLabel nameValue;
    private javax.swing.JLabel usernameValue;
    private javax.swing.JLabel emailValue;
    private javax.swing.JLabel passwordValue;
    private javax.swing.JLabel typeValue;
    private javax.swing.JLabel statusValue;
    private javax.swing.JLabel createdDateValue;
    private javax.swing.JLabel profilePictureLabel;
    private javax.swing.JButton editProfileButton;
    private javax.swing.JButton changePhotoButton;

    private Color hoverColor = new Color(70, 210, 235);
    private Color defaultColor = new Color(12, 192, 223);
    private Color activeColor = new Color(0, 150, 180);
    private JPanel activePanel = null;

    private static final String PROFILE_IMAGE_PATH = "src/BarterZone/resources/images/";

    public profile() {
        this.session = user_session.getInstance();
        this.iconManager = IconManager.getInstance();
        
        if (!session.isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "No active session. Please login again.", "Session Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        initComponents();
        loadAndResizeIcons();
        setActivePanel(panelprofile);
        setupCustomComponents();
        loadUserData();
        setupSidebarHoverEffects();
        createImageDirectory();

        setTitle("Profile - " + session.getFullName());
        setSize(800, 500);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void createImageDirectory() {
        File directory = new File(PROFILE_IMAGE_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
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
                handleNavigation(panel);
            }
        };

        panel.addMouseListener(adapter);
        label.addMouseListener(adapter);
    }

    private void handleNavigation(JPanel panel) {
        if (panel == paneldashboard) {
            openDashboard();
        } else if (panel == panelmyitems) {
            openMyItems();
        } else if (panel == panelfinditems) {
            openFindItems();
        } else if (panel == paneltrades) {
            openTrades();
        } else if (panel == panelmessages) {
            openMessages();
        } else if (panel == panelreports) {
            openReports();
        } else if (panel == panelprofile) {
            refreshProfile();
        } else if (panel == panellogout) {
            logout();
        }
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
        // Set the username in the sidebar
        username.setText(session.getFullName());
        if (session.getFullName() != null && session.getFullName().length() > 0) {
            avatarletter.setText(String.valueOf(session.getFullName().charAt(0)).toUpperCase());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        CurrentDate.setText(sdf.format(new Date()));

        jPanel2.removeAll();
        jPanel2.setLayout(null);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBounds(0, 0, 620, 450);

        JPanel profileCard = new JPanel();
        profileCard.setLayout(null);
        profileCard.setBackground(new Color(245, 245, 245));
        profileCard.setBorder(new LineBorder(new Color(12, 192, 223), 2));
        profileCard.setBounds(10, 10, 600, 430);

        javax.swing.JLabel profileTitle = new javax.swing.JLabel("My Profile");
        profileTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        profileTitle.setForeground(new Color(0, 102, 102));
        profileTitle.setBounds(20, 15, 200, 30);
        profileCard.add(profileTitle);

        profilePictureLabel = new javax.swing.JLabel();
        profilePictureLabel.setBounds(40, 60, 150, 150);
        profilePictureLabel.setBorder(new LineBorder(new Color(12, 192, 223), 3));
        profilePictureLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        profilePictureLabel.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        profilePictureLabel.setBackground(Color.WHITE);
        profilePictureLabel.setOpaque(true);
        profilePictureLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        profilePictureLabel.setText("No Photo");
        profileCard.add(profilePictureLabel);

        changePhotoButton = new javax.swing.JButton("Change Photo");
        changePhotoButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        changePhotoButton.setBackground(new Color(12, 192, 223));
        changePhotoButton.setForeground(Color.WHITE);
        changePhotoButton.setBounds(55, 215, 120, 30);
        changePhotoButton.setBorder(null);
        changePhotoButton.setFocusPainted(false);
        changePhotoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        changePhotoButton.addActionListener(e -> changeProfilePhoto());
        profileCard.add(changePhotoButton);

        int startY = 55;
        int labelWidth = 120;
        int valueWidth = 280;
        int labelX = 220;
        int valueX = 340;

        javax.swing.JLabel nameLabel = new javax.swing.JLabel("Full Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setBounds(labelX, startY, labelWidth, 25);
        profileCard.add(nameLabel);

        nameValue = new javax.swing.JLabel();
        nameValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameValue.setBounds(valueX, startY, valueWidth, 25);
        profileCard.add(nameValue);

        javax.swing.JLabel usernameLabel = new javax.swing.JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        usernameLabel.setBounds(labelX, startY + 30, labelWidth, 25);
        profileCard.add(usernameLabel);

        usernameValue = new javax.swing.JLabel();
        usernameValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameValue.setBounds(valueX, startY + 30, valueWidth, 25);
        profileCard.add(usernameValue);

        javax.swing.JLabel emailLabel = new javax.swing.JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        emailLabel.setBounds(labelX, startY + 60, labelWidth, 25);
        profileCard.add(emailLabel);

        emailValue = new javax.swing.JLabel();
        emailValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailValue.setBounds(valueX, startY + 60, valueWidth, 25);
        profileCard.add(emailValue);

        javax.swing.JLabel passwordLabel = new javax.swing.JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passwordLabel.setBounds(labelX, startY + 90, labelWidth, 25);
        profileCard.add(passwordLabel);

        passwordValue = new javax.swing.JLabel("••••••••");
        passwordValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordValue.setBounds(valueX, startY + 90, valueWidth, 25);
        profileCard.add(passwordValue);

        javax.swing.JLabel typeLabel = new javax.swing.JLabel("Account Type:");
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        typeLabel.setBounds(labelX, startY + 120, labelWidth, 25);
        profileCard.add(typeLabel);

        typeValue = new javax.swing.JLabel();
        typeValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        typeValue.setBounds(valueX, startY + 120, valueWidth, 25);
        profileCard.add(typeValue);

        javax.swing.JLabel statusLabel = new javax.swing.JLabel("Account Status:");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusLabel.setBounds(labelX, startY + 150, labelWidth, 25);
        profileCard.add(statusLabel);

        statusValue = new javax.swing.JLabel();
        statusValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusValue.setBounds(valueX, startY + 150, valueWidth, 25);
        profileCard.add(statusValue);

        javax.swing.JLabel createdLabel = new javax.swing.JLabel("Member Since:");
        createdLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        createdLabel.setBounds(labelX, startY + 180, labelWidth, 25);
        profileCard.add(createdLabel);

        createdDateValue = new javax.swing.JLabel();
        createdDateValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        createdDateValue.setBounds(valueX, startY + 180, valueWidth, 25);
        profileCard.add(createdDateValue);

        editProfileButton = new javax.swing.JButton("EDIT PROFILE");
        editProfileButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        editProfileButton.setBackground(new Color(0, 102, 102));
        editProfileButton.setForeground(Color.WHITE);
        editProfileButton.setBounds(220, 380, 160, 35);
        editProfileButton.setBorder(null);
        editProfileButton.setFocusPainted(false);
        editProfileButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editProfileButton.addActionListener(e -> openEditProfile());
        profileCard.add(editProfileButton);

        contentPanel.add(profileCard);
        jPanel2.add(contentPanel);
        contentPanel.setBounds(0, 0, 620, 450);

        jPanel2.revalidate();
        jPanel2.repaint();
    }

    private void loadUserData() {
        // Debug print to check session data
        System.out.println("Loading user data from session:");
        System.out.println("Full Name: " + session.getFullName());
        System.out.println("Username: " + session.getUsername());
        System.out.println("Email: " + session.getEmail());
        System.out.println("User Type: " + session.getUserType());
        System.out.println("Status: " + session.getStatus());
        System.out.println("Created Date: " + session.getCreatedDate());
        
        // Load all user data from session
        nameValue.setText(session.getFullName() != null ? session.getFullName() : "N/A");
        usernameValue.setText(session.getUsername() != null ? session.getUsername() : "N/A");
        emailValue.setText(session.getEmail() != null ? session.getEmail() : "N/A");
        
        String type = session.getUserType();
        if (type != null) {
            typeValue.setText(type.substring(0, 1).toUpperCase() + type.substring(1));
        } else {
            typeValue.setText("N/A");
        }
        
        String status = session.getStatus();
        if (status != null) {
            if (status.equalsIgnoreCase("active")) {
                statusValue.setText("Active");
                statusValue.setForeground(new Color(46, 125, 50));
            } else {
                statusValue.setText("Inactive");
                statusValue.setForeground(new Color(204, 0, 0));
            }
        } else {
            statusValue.setText("N/A");
        }
        
        // Format and display created date
        String createdDate = session.getCreatedDate();
        createdDateValue.setText(formatCreatedDate(createdDate));
        
        // Load profile picture
        String profilePic = session.getProfilePicture();
        if (profilePic != null && !profilePic.isEmpty()) {
            loadProfileImage(profilePic);
        }
    }

    private String formatCreatedDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return "N/A";
        }
        
        try {
            // Try to parse and format the date nicely
            // SQLite date format is typically "yyyy-MM-dd HH:mm:ss"
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
        } catch (Exception e) {
            return dateStr;
        }
    }

    private void loadProfileImage(String imagePath) {
        try {
            String fullPath = "src/" + imagePath.replace(".", "/");
            File imgFile = new File(fullPath);
            if (imgFile.exists()) {
                ImageIcon icon = new ImageIcon(fullPath);
                Image image = icon.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
                profilePictureLabel.setIcon(new ImageIcon(image));
                profilePictureLabel.setText("");
            }
        } catch (Exception e) {
            System.out.println("Error loading profile image: " + e.getMessage());
        }
    }

    private void changeProfilePhoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedImagePath = selectedFile.getAbsolutePath();
            
            String savedPath = saveProfileImage(selectedImagePath);
            if (!savedPath.isEmpty()) {
                session.setProfilePicture(savedPath);
                loadProfileImage(savedPath);
                JOptionPane.showMessageDialog(this, "Profile photo updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private String saveProfileImage(String sourcePath) {
        try {
            File directory = new File(PROFILE_IMAGE_PATH);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String extension = sourcePath.substring(sourcePath.lastIndexOf("."));
            String fileName = "profile_" + session.getUserId() + "_" + System.currentTimeMillis() + extension;
            String destinationPath = PROFILE_IMAGE_PATH + fileName;

            Files.copy(Paths.get(sourcePath), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);

            return "BarterZone.resources.images." + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return "";
        }
    }

    private void openEditProfile() {
        edit_profile editFrame = new edit_profile();
        editFrame.setVisible(true);
        editFrame.setLocationRelativeTo(null);
        
    }

    private void refreshProfile() {
        session.refreshData();
        loadUserData();
        JOptionPane.showMessageDialog(this, "Profile refreshed!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openDashboard() {
        trader_dashboard dashboard = new trader_dashboard(session.getUserId(), session.getFullName());
        dashboard.setVisible(true);
        dashboard.setLocationRelativeTo(null);
        this.dispose();
    }

    private void openMyItems() {
        myitems myItemsFrame = new myitems(session.getUserId(), session.getFullName());
        myItemsFrame.setVisible(true);
        myItemsFrame.setLocationRelativeTo(null);
        this.dispose();
    }

    private void openFindItems() {
        finditems findItemsFrame = new finditems(session.getUserId(), session.getFullName());
        findItemsFrame.setVisible(true);
        findItemsFrame.setLocationRelativeTo(null);
        this.dispose();
    }

    private void openTrades() {
        trades tradesFrame = new trades(session.getUserId(), session.getFullName());
        tradesFrame.setVisible(true);
        tradesFrame.setLocationRelativeTo(null);
        this.dispose();
    }

    private void openMessages() {
        messages messagesFrame = new messages(session.getUserId(), session.getFullName());
        messagesFrame.setVisible(true);
        messagesFrame.setLocationRelativeTo(null);
        this.dispose();
    }

    private void openReports() {
        reports reportsFrame = new reports(session.getUserId(), session.getFullName());
        reportsFrame.setVisible(true);
        reportsFrame.setLocationRelativeTo(null);
        this.dispose();
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

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 2));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30));
        jLabel1.setText("Profile");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, 30));

        CurrentDate.setFont(new java.awt.Font("Segoe UI", 0, 14));
        CurrentDate.setForeground(new java.awt.Color(102, 102, 102));
        CurrentDate.setText("");
        jPanel1.add(CurrentDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 10, 250, 30));

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
    private javax.swing.JPanel avatar;
    private javax.swing.JLabel avatarletter;
    private javax.swing.JLabel barterzonelogo;
    private javax.swing.JLabel dashboard;
    private javax.swing.JLabel dashboardicon;
    private javax.swing.JLabel finditems;
    private javax.swing.JLabel finditemsicon;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    javax.swing.JPanel jPanel2;
    private javax.swing.JLabel logotext;
    private javax.swing.JLabel logout;
    private javax.swing.JLabel logouticon;
    private javax.swing.JLabel messages;
    private javax.swing.JLabel messagesicon;
    private javax.swing.JLabel myitems;
    private javax.swing.JLabel myitemsicon;
    private javax.swing.JPanel paneldashboard;
    private javax.swing.JPanel panelfinditems;
    private javax.swing.JPanel panellogout;
    private javax.swing.JPanel panelmessages;
    private javax.swing.JPanel panelmyitems;
    private javax.swing.JPanel panelprofile;
    private javax.swing.JPanel panelreports;
    private javax.swing.JPanel paneltrades;
    private javax.swing.JLabel profileicon;
    private javax.swing.JLabel reportsicon;
    private javax.swing.JPanel tradermenu1;
    private javax.swing.JLabel trades;
    private javax.swing.JLabel tradesicon;
    private javax.swing.JLabel username;
}