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
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

public class settings extends javax.swing.JFrame {

    private int traderId;
    private String traderName;
    private user_session session;
    private config db;
    private IconManager iconManager;

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
    private JPanel welcomePanel;
    private JLabel welcomeLabel;
    private JLabel infoLabel;
    
    private Color themeColor = new Color(12, 192, 223);
    private Color hoverColor = new Color(70, 210, 235);
    private Color activeColor = new Color(0, 150, 180);
    private Color headerBgColor = new Color(245, 245, 245);
    private Color textColor = new Color(80, 80, 80);
    private Color accentColor = new Color(0, 102, 102);
    
    private JPanel currentHoverPanel = null;

    public settings(int traderId, String traderName) {
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
        
        setTitle("Settings - " + traderName);
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

    private void setupSidePanel() {
        backButton = new JButton("← Back");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        backButton.setBackground(accentColor);
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(30, 20, 100, 30);
        backButton.setBorder(null);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> goBackToDashboard());
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
        if (panel == profileOptionPanel) {
            openProfile();
        } else if (panel == privacyOptionPanel) {
            showPrivacyMessage();
        } else if (panel == logoutOptionPanel) {
            logout();
        }
    }

    private void setupHeader() {
        headerTitle = new JLabel("Settings");
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
        welcomePanel = new JPanel();
        welcomePanel.setLayout(null);
        welcomePanel.setBackground(Color.WHITE);
        welcomePanel.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        welcomePanel.setBounds(20, 20, 580, 70);
        contentPanel.add(welcomePanel);
        
        welcomeLabel = new JLabel("Welcome to BarterZone Settings");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        welcomeLabel.setForeground(accentColor);
        welcomeLabel.setBounds(15, 12, 350, 25);
        welcomePanel.add(welcomeLabel);
        
        infoLabel = new JLabel("Manage your account preferences and privacy settings");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        infoLabel.setForeground(textColor);
        infoLabel.setBounds(15, 40, 400, 18);
        welcomePanel.add(infoLabel);
        
        JPanel infoCard = new JPanel();
        infoCard.setLayout(null);
        infoCard.setBackground(Color.WHITE);
        infoCard.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        infoCard.setBounds(20, 100, 580, 280);
        contentPanel.add(infoCard);
        
        JLabel settingsTitle = new JLabel("Account Settings");
        settingsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        settingsTitle.setForeground(accentColor);
        settingsTitle.setBounds(15, 15, 200, 25);
        infoCard.add(settingsTitle);
        
        JLabel profileDesc = new JLabel("• Profile - View and edit your personal information");
        profileDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        profileDesc.setForeground(textColor);
        profileDesc.setBounds(15, 50, 500, 22);
        infoCard.add(profileDesc);
        
        JLabel privacyDesc = new JLabel("• Privacy - Manage your privacy preferences (Coming Soon)");
        privacyDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        privacyDesc.setForeground(textColor);
        privacyDesc.setBounds(15, 80, 500, 22);
        infoCard.add(privacyDesc);
        
        JLabel securityDesc = new JLabel("• Security - Change password and security settings");
        securityDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        securityDesc.setForeground(textColor);
        securityDesc.setBounds(15, 110, 500, 22);
        infoCard.add(securityDesc);
        
        JLabel notificationDesc = new JLabel("• Notifications - Configure email and push notifications");
        notificationDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        notificationDesc.setForeground(textColor);
        notificationDesc.setBounds(15, 140, 500, 22);
        infoCard.add(notificationDesc);
        
        JLabel logoutDesc = new JLabel("• Logout - Sign out of your account securely");
        logoutDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        logoutDesc.setForeground(textColor);
        logoutDesc.setBounds(15, 170, 500, 22);
        infoCard.add(logoutDesc);
        
        JLabel versionLabel = new JLabel("BarterZone Version 3.0.0");
        versionLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        versionLabel.setForeground(new Color(150, 150, 150));
        versionLabel.setBounds(15, 210, 200, 20);
        infoCard.add(versionLabel);
        
        JLabel helpLabel = new JLabel("Need help? Contact support@barterzone.com");
        helpLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        helpLabel.setForeground(new Color(150, 150, 150));
        helpLabel.setBounds(15, 245, 250, 18);
        infoCard.add(helpLabel);
    }
    
    private void openProfile() {
        profile profileFrame = new profile();
        profileFrame.setVisible(true);
        profileFrame.setLocationRelativeTo(null);
        this.dispose();
    }
    
    private void showPrivacyMessage() {
        JOptionPane.showMessageDialog(this,
            "Privacy Settings\n\n"
            + "This feature is coming soon!\n\n"
            + "You will be able to manage:\n"
            + "• Who can view your profile\n"
            + "• Data sharing preferences\n"
            + "• Account visibility settings\n"
            + "• And more...",
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
    
    private void goBackToDashboard() {
        trader_dashboard dashboardFrame = new trader_dashboard(traderId, traderName);
        dashboardFrame.setVisible(true);
        dashboardFrame.setLocationRelativeTo(null);
        this.dispose();
    }
}