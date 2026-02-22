//package BarterZone.Dashboard.trader;
//
//public class myitems1 extends javax.swing.JFrame {
//
//    public myitems1() {
//        initComponents();
//    }
//
//    
//    @SuppressWarnings("unchecked")
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
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        myitemstable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        addpanel = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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
        dashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dashboardMouseClicked(evt);
            }
        });

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
        myitems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                myitemsMouseClicked(evt);
            }
        });

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
        finditems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                finditemsMouseClicked(evt);
            }
        });

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
        trades.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tradesMouseClicked(evt);
            }
        });

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
        messages.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                messagesMouseClicked(evt);
            }
        });

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
        Reports.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ReportsMouseClicked(evt);
            }
        });

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
        Profile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ProfileMouseClicked(evt);
            }
        });

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
        jLabel1.setText("My Items");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, 40));

        CurrentDate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        CurrentDate.setForeground(new java.awt.Color(102, 102, 102));
        CurrentDate.setText("jLabel2");
        jPanel1.add(CurrentDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 0, 160, 39));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        myitemstable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Item name", "Brand", "Condition", "Date bought", "Description", "Status", "Picture Items"
            }
        ));
        myitemstable.setEditingColumn(1);
        myitemstable.setEditingRow(1);
        myitemstable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                myitemstableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(myitemstable);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 640, 90));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        addpanel.setText("jLabel2");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addpanel, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(addpanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, 40));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setText("jLabel3");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tradermenu1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(tradermenu1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardMouseClicked
//        myitemsframe test = new myitemsframe();
//        test.setVisible(true);
//        test.pack();
//        test.setLocationRelativeTo(null);
//        this.dispose();

    }//GEN-LAST:event_dashboardMouseClicked

    private void paneldashboardMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paneldashboardMouseEntered
//        paneldashboard.setBackground(hoverColor);
    }//GEN-LAST:event_paneldashboardMouseEntered

    private void paneldashboardMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paneldashboardMouseExited
//        paneldashboard.setBackground(defaultColor);
    }//GEN-LAST:event_paneldashboardMouseExited

    private void myitemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_myitemsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_myitemsMouseClicked

    private void panelmyitemsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelmyitemsMouseEntered
//        panelmyitems.setBackground(hoverColor);

    }//GEN-LAST:event_panelmyitemsMouseEntered

    private void panelmyitemsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelmyitemsMouseExited
//        panelmyitems.setBackground(defaultColor);

    }//GEN-LAST:event_panelmyitemsMouseExited

    private void finditemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_finditemsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_finditemsMouseClicked

    private void panelfinditemsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelfinditemsMouseEntered
//        panelfinditems.setBackground(hoverColor);

    }//GEN-LAST:event_panelfinditemsMouseEntered

    private void panelfinditemsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelfinditemsMouseExited
//        panelfinditems.setBackground(defaultColor);

    }//GEN-LAST:event_panelfinditemsMouseExited

    private void tradesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tradesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tradesMouseClicked

    private void paneltradesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paneltradesMouseEntered
        paneltrades.setBackground(hoverColor);
    }//GEN-LAST:event_paneltradesMouseEntered

    private void paneltradesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paneltradesMouseExited
        paneltrades.setBackground(defaultColor);
    }//GEN-LAST:event_paneltradesMouseExited

    private void messagesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_messagesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_messagesMouseClicked

    private void panelmessagesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelmessagesMouseEntered
        panelmessages.setBackground(hoverColor);
    }//GEN-LAST:event_panelmessagesMouseEntered

    private void panelmessagesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelmessagesMouseExited
        panelmessages.setBackground(defaultColor);
    }//GEN-LAST:event_panelmessagesMouseExited

    private void panellogoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panellogoutMouseEntered
        panellogout.setBackground(hoverColor);
    }//GEN-LAST:event_panellogoutMouseEntered

    private void panellogoutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panellogoutMouseExited
        panellogout.setBackground(defaultColor);
    }//GEN-LAST:event_panellogoutMouseExited

    private void ReportsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ReportsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_ReportsMouseClicked

    private void panelreportsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelreportsMouseEntered
        panelreports.setBackground(hoverColor);
    }//GEN-LAST:event_panelreportsMouseEntered

    private void panelreportsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelreportsMouseExited
//        panelreports.setBackground(defaultColor);
    }//GEN-LAST:event_panelreportsMouseExited

    private void ProfileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ProfileMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_ProfileMouseClicked

    private void panelprofileMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelprofileMouseEntered
//        panelprofile.setBackground(hoverColor);
    }//GEN-LAST:event_panelprofileMouseEntered

    private void panelprofileMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelprofileMouseExited
//        panelprofile.setBackground(defaultColor);
    }//GEN-LAST:event_panelprofileMouseExited

    private void myitemstableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_myitemstableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_myitemstableMouseClicked

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CurrentDate;
    private javax.swing.JLabel Profile;
    private javax.swing.JLabel Reports;
    private javax.swing.JLabel addpanel;
    private javax.swing.JPanel avatar;
    private javax.swing.JLabel avatarletter;
    private javax.swing.JLabel barterzonelogo;
    private javax.swing.JLabel dashboard;
    private javax.swing.JLabel dashboardicon;
    private javax.swing.JLabel finditems;
    private javax.swing.JLabel finditemsicon;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel logotext;
    private javax.swing.JLabel logout;
    private javax.swing.JLabel logouticon;
    private javax.swing.JLabel messages;
    private javax.swing.JLabel messagesicon;
    private javax.swing.JLabel myitems;
    private javax.swing.JLabel myitemsicon;
    private javax.swing.JTable myitemstable;
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
    // End of variables declaration//GEN-END:variables
}
