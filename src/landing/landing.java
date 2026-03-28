package landing;

import BarterZone.Dashboard.session.user_session;
import BarterZone.Dashboard.trader.trader_dashboard;
import BarterZone.loginandsignup.login;
import BarterZone.loginandsignup.signup;
import landing.announcement;
import landing.pp;
import landing.tos;
import landing.search_items;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class landing extends JFrame {
    
    private user_session session;
    private int traderId = -1;
    private String traderName = "";

    private static final String SEARCH_PLACEHOLDER = "Search for items to trade";
    
    private JLabel backgroundLabel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton iGiveButton;
    private JButton iWantButton;
    private JButton loginButton;
    private JButton signupButton;
    private JButton announcementButton;
    private JButton privacyPolicyButton;
    private JButton termsButton;
    private JLabel titleLabel;
    private JLabel subtitleLabel;

    public landing() {
        this.session = user_session.getInstance();
        this.traderId = -1;
        this.traderName = "";
        setTitle("BarterZone");
        setIconImage(new ImageIcon(getClass().getResource(
                "/BarterZone/resources/icon/logo.png")).getImage());
        initComponents();
        updateButtonsForSession();
        setupFrame();
    }

    public landing(int traderId, String traderName) {
        this.session = user_session.getInstance();
        this.traderId = traderId;
        this.traderName = traderName;
        setTitle("BarterZone - " +traderName);
        setIconImage(new ImageIcon(getClass().getResource(
                "/BarterZone/resources/icon/logo.png")).getImage());
        if (traderId != -1 && traderName != null) {
            this.session.login(traderId, "trader", traderName);
        }
        
        initComponents();
        updateButtonsForSession();
        setupFrame();
    }
    
    private void setupFrame() {
        setTitle("BarterZone - " + traderName);
        setSize(800, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
    }
    
    private void updateButtonsForSession() {
        boolean isLoggedIn = session.isLoggedIn() && traderId != -1;
        
        if (isLoggedIn) {
            loginButton.setText("Dashboard");
            signupButton.setText("Logout");
            
            // Update trader info from session if needed
            if (traderName == null || traderName.isEmpty()) {
                traderName = session.getFullName();
            }
            if (traderId == -1) {
                traderId = session.getUserId();
            }
        } else {
            loginButton.setText("Login");
            signupButton.setText("Sign Up");
            
            // Clear any stale session data
            this.traderId = -1;
            this.traderName = "";
        }
    }

    private void initComponents() {
        // Background Label - This displays landing.png
        backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, 800, 500);
        backgroundLabel.setIcon(new ImageIcon(getClass().getResource("/BarterZone/resources/images/landing.png")));
        backgroundLabel.setLayout(null);
        add(backgroundLabel);

        // Title Label
        titleLabel = new JLabel("BarterZone");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 70, 800, 60);
        backgroundLabel.add(titleLabel);

        // Subtitle
        subtitleLabel = new JLabel("Trade what you have, get what you want");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setBounds(0, 130, 800, 30);
        backgroundLabel.add(subtitleLabel);

        // Login Button (Top Right)
        loginButton = new JButton("Login"); // Default text, will be updated
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setBackground(new Color(0, 102, 102));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBounds(520, 20, 100, 35);
        loginButton.setBorder(null);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginButtonActionPerformed();
            }
        });
        backgroundLabel.add(loginButton);

        // Signup Button (Top Right)
        signupButton = new JButton("Sign Up"); // Default text, will be updated
        signupButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        signupButton.setBackground(new Color(0, 102, 102));
        signupButton.setForeground(Color.WHITE);
        signupButton.setBounds(630, 20, 100, 35);
        signupButton.setBorder(null);
        signupButton.setFocusPainted(false);
        signupButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signupButtonActionPerformed();
            }
        });
        backgroundLabel.add(signupButton);

        // Search Field with placeholder
        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setForeground(Color.GRAY);
        searchField.setText(SEARCH_PLACEHOLDER);
        searchField.setBounds(200, 200, 320, 38);
        searchField.setBorder(new LineBorder(Color.WHITE, 2));
        searchField.setBackground(new Color(255, 255, 255, 200));

        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals(SEARCH_PLACEHOLDER)) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().trim().isEmpty()) {
                    searchField.setText(SEARCH_PLACEHOLDER);
                    searchField.setForeground(Color.GRAY);
                }
            }
        });

        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchButtonActionPerformed();
            }
        });

        backgroundLabel.add(searchField);

        // Search Button
        searchButton = new JButton("Search");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchButton.setBackground(new Color(255, 140, 0));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBounds(530, 200, 90, 38);
        searchButton.setBorder(null);
        searchButton.setFocusPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchButtonActionPerformed();
            }
        });
        backgroundLabel.add(searchButton);

        // I Give Button
        iGiveButton = new JButton("I Give");
        iGiveButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        iGiveButton.setBackground(new Color(46, 125, 50));
        iGiveButton.setForeground(Color.WHITE);
        iGiveButton.setBounds(260, 270, 120, 40);
        iGiveButton.setBorder(null);
        iGiveButton.setFocusPainted(false);
        iGiveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        iGiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iGiveButtonActionPerformed();
            }
        });
        backgroundLabel.add(iGiveButton);

        // I Want Button
        iWantButton = new JButton("I Want");
        iWantButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        iWantButton.setBackground(new Color(240, 128, 22));
        iWantButton.setForeground(Color.WHITE);
        iWantButton.setBounds(420, 270, 120, 40);
        iWantButton.setBorder(null);
        iWantButton.setFocusPainted(false);
        iWantButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        iWantButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iWantButtonActionPerformed();
            }
        });
        backgroundLabel.add(iWantButton);

        // Bottom buttons
        announcementButton = new JButton("Announcement");
        announcementButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        announcementButton.setBackground(new Color(255, 153, 0));
        announcementButton.setForeground(Color.WHITE);
        announcementButton.setBounds(240, 430, 110, 28);
        announcementButton.setBorder(null);
        announcementButton.setFocusPainted(false);
        announcementButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        announcementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                announcementButtonActionPerformed();
            }
        });
        backgroundLabel.add(announcementButton);

        privacyPolicyButton = new JButton("Privacy Policy");
        privacyPolicyButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        privacyPolicyButton.setBackground(new Color(46, 125, 50));
        privacyPolicyButton.setForeground(Color.WHITE);
        privacyPolicyButton.setBounds(360, 430, 110, 28);
        privacyPolicyButton.setBorder(null);
        privacyPolicyButton.setFocusPainted(false);
        privacyPolicyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        privacyPolicyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                privacyPolicyButtonActionPerformed();
            }
        });
        backgroundLabel.add(privacyPolicyButton);

        termsButton = new JButton("Terms of Service");
        termsButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        termsButton.setBackground(new Color(8, 78, 128));
        termsButton.setForeground(Color.WHITE);
        termsButton.setBounds(480, 430, 110, 28);
        termsButton.setBorder(null);
        termsButton.setFocusPainted(false);
        termsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        termsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                termsButtonActionPerformed();
            }
        });
        backgroundLabel.add(termsButton);
        
        // Initial button update based on session
        updateButtonsForSession();
    }

    private void searchButtonActionPerformed() {
        String searchQuery = searchField.getText().trim();

        if (searchQuery.isEmpty() || searchQuery.equals(SEARCH_PLACEHOLDER)) {
            JOptionPane.showMessageDialog(this, "Please enter a search term.", "Search", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        boolean isLoggedIn = session.isLoggedIn() && traderId != -1;
        search_items searchFrame = new search_items(searchQuery, isLoggedIn, traderId, traderName);
        searchFrame.setVisible(true);
        searchFrame.setLocationRelativeTo(null);
        this.dispose();
    }

    private void iGiveButtonActionPerformed() {
        boolean isLoggedIn = session.isLoggedIn() && traderId != -1;
        
        if (!isLoggedIn) {
            JOptionPane.showMessageDialog(this, "Please login first to access this feature.", "Login Required", JOptionPane.WARNING_MESSAGE);
        } else {
            BarterZone.Dashboard.trader.myitems myItemsFrame = new BarterZone.Dashboard.trader.myitems(traderId, traderName);
            myItemsFrame.setVisible(true);
            myItemsFrame.setLocationRelativeTo(null);
            this.dispose();
        }
    }

    private void iWantButtonActionPerformed() {
        boolean isLoggedIn = session.isLoggedIn() && traderId != -1;
        
        if (!isLoggedIn) {
            JOptionPane.showMessageDialog(this, "Please login first to access this feature.", "Login Required", JOptionPane.WARNING_MESSAGE);
        } else {
            BarterZone.Dashboard.trader.finditems findItemsFrame = new BarterZone.Dashboard.trader.finditems(traderId, traderName);
            findItemsFrame.setVisible(true);
            findItemsFrame.setLocationRelativeTo(null);
            this.dispose();
        }
    }

    private void loginButtonActionPerformed() {
        boolean isLoggedIn = session.isLoggedIn() && traderId != -1;
        
        if (!isLoggedIn) {
            login loginFrame = new login();
            loginFrame.setVisible(true);
            loginFrame.setLocationRelativeTo(null);
            this.dispose();
        } else {
            // Open trader dashboard for logged-in user
            trader_dashboard dashboardFrame = new trader_dashboard(traderId, traderName);
            dashboardFrame.setVisible(true);
            dashboardFrame.setLocationRelativeTo(null);
            this.dispose();
        }
    }

    private void signupButtonActionPerformed() {
        boolean isLoggedIn = session.isLoggedIn() && traderId != -1;
        
        if (!isLoggedIn) {
            signup signupFrame = new signup();
            signupFrame.setVisible(true);
            signupFrame.setLocationRelativeTo(null);
            this.dispose();
        } else {
            // Logout
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                session.logout();
                landing landingFrame = new landing();
                landingFrame.setVisible(true);
                landingFrame.setLocationRelativeTo(null);
                this.dispose();
            }
        }
    }

    private void announcementButtonActionPerformed() {
        announcement announcementFrame = new announcement();
        announcementFrame.setVisible(true);
        announcementFrame.setLocationRelativeTo(null);
    }

    private void privacyPolicyButtonActionPerformed() {
        pp ppFrame = new pp();
        ppFrame.setVisible(true);
        ppFrame.setLocationRelativeTo(null);
    }

    private void termsButtonActionPerformed() {
        tos tosFrame = new tos();
        tosFrame.setVisible(true);
        tosFrame.setLocationRelativeTo(null);
    }
}