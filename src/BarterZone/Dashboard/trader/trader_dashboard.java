package BarterZone.Dashboard.trader;

import BarterZone.resources.IconManager;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class trader_dashboard extends javax.swing.JFrame {

    private int traderId;
    private String traderName;
    private IconManager iconManager;

    private Color hoverColor = new Color(70, 210, 235);
    private Color defaultColor = new Color(12, 192, 223);
    private Color activeColor = new Color(0, 150, 180);
    private JPanel activePanel = null;

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

    public trader_dashboard(int traderId, String traderName) {
        this.traderId = traderId;
        this.traderName = traderName;
        this.iconManager = IconManager.getInstance();
        initComponents();
//        paneldashboard.setOpaque(true);
//        panelmyitems.setOpaque(true);
//        panelfinditems.setOpaque(true);
//        paneltrades.setOpaque(true);
//        panelmessages.setOpaque(true);
//        panelreports.setOpaque(true);
//        panelprofile.setOpaque(true);
//        panellogout.setOpaque(true);

        applyHoverEffect(paneldashboard, dashboard);
        applyHoverEffect(panelmyitems, myitems);
        applyHoverEffect(panelfinditems, finditems);
        applyHoverEffect(paneltrades, trades);
        applyHoverEffect(panelmessages, messages);
        applyHoverEffect(panelreports, Reports);
        applyHoverEffect(panelprofile, Profile);
        applyHoverEffect(panellogout, logout);
        setTitle("Trader Dashboard - " + traderName);
        setSize(800, 500);
        setResizable(false);
        setLocationRelativeTo(null);

        loadAndResizeIcons();
        applyModernStyling();
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

    private void setIconSafely(JLabel label, ImageIcon icon) {
        if (icon != null) {
            label.setIcon(icon);
            label.setText("");
        }
    }

    public void applyModernStyling() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE, dd MMMM yyyy");
        CurrentDate.setText(sdf.format(new java.util.Date()));

        configureIconContainer(dashboardicon);
        configureIconContainer(myitemsicon);
        configureIconContainer(finditemsicon);
        configureIconContainer(tradesicon);
        configureIconContainer(messagesicon);
        configureIconContainer(reportsicon);
        configureIconContainer(profileicon);
        configureIconContainer(logouticon);
        configureIconContainer(barterzonelogo);

        username.setText(traderName);

        if (traderName != null && traderName.length() > 0) {
            avatarletter.setText(String.valueOf(traderName.charAt(0)).toUpperCase());
        }

        paneldashboard.setOpaque(true);
        panelmyitems.setOpaque(true);
        panelfinditems.setOpaque(true);
        paneltrades.setOpaque(true);
        panelmessages.setOpaque(true);
        panelreports.setOpaque(true);
        panelprofile.setOpaque(true);
        panellogout.setOpaque(true);
    }

    private void configureIconContainer(JLabel iconLabel) {
        iconLabel.setHorizontalAlignment(JLabel.CENTER);
        iconLabel.setVerticalAlignment(JLabel.CENTER);
        iconLabel.setPreferredSize(new java.awt.Dimension(25, 25));
        iconLabel.setMinimumSize(new java.awt.Dimension(25, 25));
        iconLabel.setMaximumSize(new java.awt.Dimension(25, 25));
    }

    private void setActivePanel(JPanel panel) {

        if (activePanel != null) {
            activePanel.setBackground(defaultColor);
        }

        activePanel = panel;
        activePanel.setBackground(activeColor);
    }

    private void applyHoverEffect(JPanel panel, JLabel label) {

        MouseAdapter adapter = new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                setHover(panel);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setDefault(panel);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                setActivePanel(panel);
            }
        };

        panel.addMouseListener(adapter);
        label.addMouseListener(adapter);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
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
        dashboardpanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        welcomebacktext = new javax.swing.JLabel();
        activeandpending = new javax.swing.JLabel();
        totalitems = new javax.swing.JPanel();
        myitemsicon1 = new javax.swing.JLabel();
        completedtrades = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        postnewitem = new javax.swing.JPanel();
        findtrades = new javax.swing.JPanel();
        tradehistory = new javax.swing.JPanel();
        reports = new javax.swing.JPanel();
        activetrades = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMaximizedBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setMinimumSize(new java.awt.Dimension(800, 500));
        setPreferredSize(new java.awt.Dimension(800, 500));
        setResizable(false);

        tradermenu1.setBackground(new java.awt.Color(12, 192, 223));
        tradermenu1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(8, 150, 175), 1, true));
        tradermenu1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logotext.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        logotext.setForeground(new java.awt.Color(255, 255, 255));
        logotext.setText("BarterZone");
        logotext.setAlignmentX(0.5F);
        tradermenu1.add(logotext, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 0, -1, 40));

        avatar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3));
        avatar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        avatarletter.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        avatarletter.setForeground(new java.awt.Color(12, 192, 223));
        avatarletter.setText("T");
        avatar.add(avatarletter, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 50, 30));

        username.setBackground(new java.awt.Color(255, 255, 255));
        username.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        username.setForeground(new java.awt.Color(255, 255, 255));
        avatar.add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 110, 30));

        tradermenu1.add(avatar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 110, 60));

        barterzonelogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/logo.png"))); // NOI18N
        tradermenu1.add(barterzonelogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        paneldashboard.setBackground(new java.awt.Color(12, 192, 223));
        paneldashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                paneldashboardMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                paneldashboardMouseExited(evt);
            }
        });

        dashboardicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/dashboard.png"))); // NOI18N

        dashboard.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
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
        panelmyitems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelmyitemsMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelmyitemsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelmyitemsMouseExited(evt);
            }
        });

        myitemsicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/myitems.png"))); // NOI18N

        myitems.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
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
        panelfinditems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelfinditemsMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelfinditemsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelfinditemsMouseExited(evt);
            }
        });

        finditemsicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/finditems.png"))); // NOI18N

        finditems.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
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
        paneltrades.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                paneltradesMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                paneltradesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                paneltradesMouseExited(evt);
            }
        });

        tradesicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/trade.png"))); // NOI18N

        trades.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
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
        panelmessages.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelmessagesMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelmessagesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelmessagesMouseExited(evt);
            }
        });

        messagesicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/messages.png"))); // NOI18N

        messages.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
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

        panellogout.setBackground(new java.awt.Color(12, 192, 223));
        panellogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panellogoutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panellogoutMouseExited(evt);
            }
        });

        logout.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        logout.setForeground(new java.awt.Color(255, 255, 255));
        logout.setText("Logout");

        logouticon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/logout.png"))); // NOI18N

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

        panelreports.setBackground(new java.awt.Color(12, 192, 223));
        panelreports.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelreportsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelreportsMouseExited(evt);
            }
        });

        Reports.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Reports.setForeground(new java.awt.Color(255, 255, 255));
        Reports.setText("Reports");

        reportsicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/report.png"))); // NOI18N

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
        panelprofile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelprofileMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelprofileMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelprofileMouseExited(evt);
            }
        });

        profileicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/profile.png"))); // NOI18N

        Profile.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
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

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 2));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setText("Trader Dashboard ");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, 30));

        CurrentDate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        CurrentDate.setForeground(new java.awt.Color(102, 102, 102));
        CurrentDate.setText("jLabel2");
        jPanel1.add(CurrentDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 0, 142, 39));

        dashboardpanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(12, 192, 223));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(12, 192, 223), 5, true));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        welcomebacktext.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        welcomebacktext.setForeground(new java.awt.Color(255, 255, 255));
        welcomebacktext.setText("Welcome back, Trader! ");
        jPanel4.add(welcomebacktext, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 300, 30));

        activeandpending.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        activeandpending.setForeground(new java.awt.Color(255, 255, 255));
        activeandpending.setText("active pending function");
        jPanel4.add(activeandpending, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, 300, 30));

        dashboardpanel.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 560, 90));

        totalitems.setBackground(new java.awt.Color(255, 255, 255));

        myitemsicon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/myitems.png"))); // NOI18N

        javax.swing.GroupLayout totalitemsLayout = new javax.swing.GroupLayout(totalitems);
        totalitems.setLayout(totalitemsLayout);
        totalitemsLayout.setHorizontalGroup(
            totalitemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalitemsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(myitemsicon1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        totalitemsLayout.setVerticalGroup(
            totalitemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalitemsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(myitemsicon1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(59, Short.MAX_VALUE))
        );

        dashboardpanel.add(totalitems, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 120, -1, -1));

        completedtrades.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout completedtradesLayout = new javax.swing.GroupLayout(completedtrades);
        completedtrades.setLayout(completedtradesLayout);
        completedtradesLayout.setHorizontalGroup(
            completedtradesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        completedtradesLayout.setVerticalGroup(
            completedtradesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        dashboardpanel.add(completedtrades, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 120, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setText("Quick Actions  ");
        dashboardpanel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, 180, 40));

        postnewitem.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout postnewitemLayout = new javax.swing.GroupLayout(postnewitem);
        postnewitem.setLayout(postnewitemLayout);
        postnewitemLayout.setHorizontalGroup(
            postnewitemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
        );
        postnewitemLayout.setVerticalGroup(
            postnewitemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        dashboardpanel.add(postnewitem, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, 110, 50));

        findtrades.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout findtradesLayout = new javax.swing.GroupLayout(findtrades);
        findtrades.setLayout(findtradesLayout);
        findtradesLayout.setHorizontalGroup(
            findtradesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
        );
        findtradesLayout.setVerticalGroup(
            findtradesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        dashboardpanel.add(findtrades, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 300, -1, 50));

        tradehistory.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout tradehistoryLayout = new javax.swing.GroupLayout(tradehistory);
        tradehistory.setLayout(tradehistoryLayout);
        tradehistoryLayout.setHorizontalGroup(
            tradehistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
        );
        tradehistoryLayout.setVerticalGroup(
            tradehistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        dashboardpanel.add(tradehistory, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 300, -1, 50));

        reports.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout reportsLayout = new javax.swing.GroupLayout(reports);
        reports.setLayout(reportsLayout);
        reportsLayout.setHorizontalGroup(
            reportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
        );
        reportsLayout.setVerticalGroup(
            reportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        dashboardpanel.add(reports, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 300, -1, 50));

        activetrades.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout activetradesLayout = new javax.swing.GroupLayout(activetrades);
        activetrades.setLayout(activetradesLayout);
        activetradesLayout.setHorizontalGroup(
            activetradesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
        );
        activetradesLayout.setVerticalGroup(
            activetradesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        dashboardpanel.add(activetrades, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, 110, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tradermenu1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(177, 177, 177)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 594, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(180, 180, 180)
                        .addComponent(dashboardpanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tradermenu1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dashboardpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void paneldashboardMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paneldashboardMouseEntered
        paneldashboard.setBackground(hoverColor);

    }//GEN-LAST:event_paneldashboardMouseEntered

    private void paneldashboardMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paneldashboardMouseExited
        paneldashboard.setBackground(hoverColor);

    }//GEN-LAST:event_paneldashboardMouseExited

    private void panelmyitemsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelmyitemsMouseEntered
        panelmyitems.setBackground(hoverColor);


    }//GEN-LAST:event_panelmyitemsMouseEntered

    private void panelmyitemsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelmyitemsMouseExited
        panelmyitems.setBackground(defaultColor);


    }//GEN-LAST:event_panelmyitemsMouseExited

    private void panelfinditemsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelfinditemsMouseEntered
        panelfinditems.setBackground(hoverColor);


    }//GEN-LAST:event_panelfinditemsMouseEntered

    private void paneltradesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paneltradesMouseEntered
        paneltrades.setBackground(hoverColor);

    }//GEN-LAST:event_paneltradesMouseEntered

    private void panelfinditemsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelfinditemsMouseExited
        panelfinditems.setBackground(defaultColor);


    }//GEN-LAST:event_panelfinditemsMouseExited

    private void paneltradesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paneltradesMouseExited
        paneltrades.setBackground(defaultColor);
    }//GEN-LAST:event_paneltradesMouseExited

    private void panelmessagesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelmessagesMouseEntered
        panelmessages.setBackground(hoverColor);
    }//GEN-LAST:event_panelmessagesMouseEntered

    private void panelmessagesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelmessagesMouseExited
        panelmessages.setBackground(defaultColor);
    }//GEN-LAST:event_panelmessagesMouseExited

    private void panelreportsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelreportsMouseEntered
        panelreports.setBackground(hoverColor);
    }//GEN-LAST:event_panelreportsMouseEntered

    private void panelreportsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelreportsMouseExited
        panelreports.setBackground(defaultColor);
    }//GEN-LAST:event_panelreportsMouseExited

    private void panelprofileMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelprofileMouseEntered
        panelprofile.setBackground(hoverColor);
    }//GEN-LAST:event_panelprofileMouseEntered

    private void panelprofileMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelprofileMouseExited
        panelprofile.setBackground(defaultColor);
    }//GEN-LAST:event_panelprofileMouseExited

    private void panellogoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panellogoutMouseEntered
        panellogout.setBackground(hoverColor);
    }//GEN-LAST:event_panellogoutMouseEntered

    private void panellogoutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panellogoutMouseExited
        panellogout.setBackground(defaultColor);
    }//GEN-LAST:event_panellogoutMouseExited

    private void panelmyitemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelmyitemsMouseClicked
        System.out.println("My Items clicked! traderId: " + traderId + ", traderName: " + traderName);
        myitems myItemsFrame = new myitems(traderId, traderName);
        myItemsFrame.setVisible(true);
        myItemsFrame.setLocationRelativeTo(null);
        this.dispose();

    }//GEN-LAST:event_panelmyitemsMouseClicked

    private void panelfinditemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelfinditemsMouseClicked
        finditems findItemsFrame = new finditems(traderId, traderName);
        findItemsFrame.setVisible(true);
        findItemsFrame.setLocationRelativeTo(null);
        this.dispose();


    }//GEN-LAST:event_panelfinditemsMouseClicked

    private void paneltradesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paneltradesMouseClicked
        trades tradesFrame = new trades(traderId, traderName);
        tradesFrame.setVisible(true);
        tradesFrame.setLocationRelativeTo(null);
        this.dispose();

    }//GEN-LAST:event_paneltradesMouseClicked

    private void panelmessagesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelmessagesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_panelmessagesMouseClicked

    private void panelprofileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelprofileMouseClicked
        profile profileFrame = new profile(traderId, traderName);
        profileFrame.setVisible(true);
        profileFrame.setLocationRelativeTo(null);
        this.dispose();

    }//GEN-LAST:event_panelprofileMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CurrentDate;
    private javax.swing.JLabel Profile;
    private javax.swing.JLabel Reports;
    private javax.swing.JLabel activeandpending;
    private javax.swing.JPanel activetrades;
    private javax.swing.JPanel avatar;
    private javax.swing.JLabel avatarletter;
    private javax.swing.JLabel barterzonelogo;
    private javax.swing.JPanel completedtrades;
    private javax.swing.JLabel dashboard;
    private javax.swing.JLabel dashboardicon;
    private javax.swing.JPanel dashboardpanel;
    private javax.swing.JLabel finditems;
    private javax.swing.JLabel finditemsicon;
    private javax.swing.JPanel findtrades;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel logotext;
    private javax.swing.JLabel logout;
    private javax.swing.JLabel logouticon;
    private javax.swing.JLabel messages;
    private javax.swing.JLabel messagesicon;
    private javax.swing.JLabel myitems;
    private javax.swing.JLabel myitemsicon;
    private javax.swing.JLabel myitemsicon1;
    private javax.swing.JPanel paneldashboard;
    private javax.swing.JPanel panelfinditems;
    private javax.swing.JPanel panellogout;
    private javax.swing.JPanel panelmessages;
    private javax.swing.JPanel panelmyitems;
    private javax.swing.JPanel panelprofile;
    private javax.swing.JPanel panelreports;
    private javax.swing.JPanel paneltrades;
    private javax.swing.JPanel postnewitem;
    private javax.swing.JLabel profileicon;
    private javax.swing.JPanel reports;
    private javax.swing.JLabel reportsicon;
    private javax.swing.JPanel totalitems;
    private javax.swing.JPanel tradehistory;
    private javax.swing.JPanel tradermenu1;
    private javax.swing.JLabel trades;
    private javax.swing.JLabel tradesicon;
    private javax.swing.JLabel username;
    private javax.swing.JLabel welcomebacktext;
    // End of variables declaration//GEN-END:variables
}
