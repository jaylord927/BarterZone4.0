package BarterZone.Dashboard.trader;

public class myitemsframe extends javax.swing.JFrame {

    public myitemsframe() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        myitemspanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        searchfield = new javax.swing.JTextField();
        searchbutton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        myitemstable = new javax.swing.JTable();
        tradermenu = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        Profile = new javax.swing.JLabel();
        dashboard = new javax.swing.JLabel();
        myitems = new javax.swing.JLabel();
        finditems = new javax.swing.JLabel();
        trades = new javax.swing.JLabel();
        messages = new javax.swing.JLabel();
        Reports = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        header = new javax.swing.JPanel();
        logo = new javax.swing.JLabel();
        logout = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        myitemspanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("My Trading Items ");
        myitemspanel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, -1, -1));

        searchfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchfieldActionPerformed(evt);
            }
        });
        myitemspanel.add(searchfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 10, 230, 50));

        searchbutton.setBackground(new java.awt.Color(12, 192, 223));
        searchbutton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        searchbutton.setForeground(new java.awt.Color(255, 255, 255));
        searchbutton.setText("Search");
        searchbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchbuttonActionPerformed(evt);
            }
        });
        myitemspanel.add(searchbutton, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 10, 100, 50));

        jLabel4.setText("Search:");
        myitemspanel.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 20, 50, 30));

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

        myitemspanel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 640, 250));

        tradermenu.setBackground(new java.awt.Color(12, 192, 223));
        tradermenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Trader Menu");
        tradermenu.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 120, 40));

        Profile.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Profile.setText("Profile");
        Profile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ProfileMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ProfileMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ProfileMouseExited(evt);
            }
        });
        tradermenu.add(Profile, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 100, 40));

        dashboard.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        dashboard.setText("Dashboard");
        dashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dashboardMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                dashboardMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                dashboardMouseExited(evt);
            }
        });
        tradermenu.add(dashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 100, 40));

        myitems.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        myitems.setText("My Items");
        myitems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                myitemsMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                myitemsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                myitemsMouseExited(evt);
            }
        });
        tradermenu.add(myitems, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 100, 40));

        finditems.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        finditems.setText("Find Items");
        finditems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                finditemsMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                finditemsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                finditemsMouseExited(evt);
            }
        });
        tradermenu.add(finditems, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 100, 40));

        trades.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        trades.setText("Trades");
        trades.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tradesMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tradesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tradesMouseExited(evt);
            }
        });
        tradermenu.add(trades, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, 100, 40));

        messages.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        messages.setText("Messages");
        messages.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                messagesMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                messagesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                messagesMouseExited(evt);
            }
        });
        tradermenu.add(messages, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, 100, 40));

        Reports.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Reports.setText("Reports");
        Reports.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ReportsMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ReportsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ReportsMouseExited(evt);
            }
        });
        tradermenu.add(Reports, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, 100, 40));

        jLabel5.setText("QUICK VIEW");
        tradermenu.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, 120, 130));

        header.setBackground(new java.awt.Color(12, 192, 223));
        header.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logo.setText("LOGO");
        header.add(logo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 90, 50));

        logout.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        logout.setText("Logout");
        logout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutMouseExited(evt);
            }
        });
        header.add(logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 10, 50, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(tradermenu, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(myitemspanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, 653, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tradermenu, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(myitemspanel, javax.swing.GroupLayout.PREFERRED_SIZE, 427, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void searchfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchfieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchfieldActionPerformed

    private void searchbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchbuttonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchbuttonActionPerformed

    private void myitemstableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_myitemstableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_myitemstableMouseClicked

    private void ProfileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ProfileMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_ProfileMouseClicked

    private void ProfileMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ProfileMouseEntered
        Profile.setForeground(new java.awt.Color(255, 255, 255));
        Profile.setBackground(new java.awt.Color(8, 145, 178));
        Profile.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_ProfileMouseEntered

    private void ProfileMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ProfileMouseExited
        Profile.setForeground(new java.awt.Color(0, 0, 0));
        Profile.setBackground(new java.awt.Color(12, 192, 223));
        Profile.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        // TODO add your ha ndling code here:
    }//GEN-LAST:event_ProfileMouseExited

    private void dashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_dashboardMouseClicked

    private void dashboardMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardMouseEntered
        dashboard.setForeground(new java.awt.Color(255, 255, 255));
        dashboard.setBackground(new java.awt.Color(8, 145, 178));
        dashboard.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        // TODO add your handling code here:
    }//GEN-LAST:event_dashboardMouseEntered

    private void dashboardMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardMouseExited
        dashboard.setForeground(new java.awt.Color(0, 0, 0));
        dashboard.setBackground(new java.awt.Color(12, 192, 223));
        dashboard.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        // TODO add your handling code here:
    }//GEN-LAST:event_dashboardMouseExited

    private void finditemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_finditemsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_finditemsMouseClicked

    private void finditemsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_finditemsMouseEntered
        finditems.setForeground(new java.awt.Color(255, 255, 255));
        finditems.setBackground(new java.awt.Color(8, 145, 178));
        finditems.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        // TODO add your handling code here:
    }//GEN-LAST:event_finditemsMouseEntered

    private void finditemsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_finditemsMouseExited
        finditems.setForeground(new java.awt.Color(0, 0, 0));
        finditems.setBackground(new java.awt.Color(12, 192, 223));
        finditems.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        // TODO add your handling code here:
    }//GEN-LAST:event_finditemsMouseExited

    private void tradesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tradesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tradesMouseClicked

    private void tradesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tradesMouseEntered
        trades.setForeground(new java.awt.Color(255, 255, 255));
        trades.setBackground(new java.awt.Color(8, 145, 178));
        trades.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        // TODO add your handling code here:
    }//GEN-LAST:event_tradesMouseEntered

    private void tradesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tradesMouseExited
        trades.setForeground(new java.awt.Color(0, 0, 0));
        trades.setBackground(new java.awt.Color(12, 192, 223));
        trades.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));    // TODO add your handling code here:
    }//GEN-LAST:event_tradesMouseExited

    private void messagesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_messagesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_messagesMouseClicked

    private void messagesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_messagesMouseEntered
        messages.setForeground(new java.awt.Color(255, 255, 255));
        messages.setBackground(new java.awt.Color(8, 145, 178));
        messages.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));    // TODO add your handling code here:
    }//GEN-LAST:event_messagesMouseEntered

    private void messagesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_messagesMouseExited
        messages.setForeground(new java.awt.Color(0, 0, 0));
        messages.setBackground(new java.awt.Color(12, 192, 223));
        messages.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));    // TODO add your handling code here:
    }//GEN-LAST:event_messagesMouseExited

    private void ReportsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ReportsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_ReportsMouseClicked

    private void ReportsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ReportsMouseEntered
        Reports.setForeground(new java.awt.Color(255, 255, 255));
        Reports.setBackground(new java.awt.Color(8, 145, 178));
        Reports.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));    // TODO add your handling code here:
    }//GEN-LAST:event_ReportsMouseEntered

    private void ReportsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ReportsMouseExited
        Reports.setForeground(new java.awt.Color(0, 0, 0));
        Reports.setBackground(new java.awt.Color(12, 192, 223));
        Reports.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));    // TODO add your handling code here:
    }//GEN-LAST:event_ReportsMouseExited

    private void logoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseClicked
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            loginandsignup.Login loginFrame = new loginandsignup.Login();
            loginFrame.setVisible(true);
            loginFrame.setLocationRelativeTo(null);
            this.dispose();
        }    // TODO add your handling code here:
    }//GEN-LAST:event_logoutMouseClicked

    private void logoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseEntered
        logout.setForeground(new java.awt.Color(255, 255, 255));
        logout.setBackground(new java.awt.Color(8, 145, 178));
        logout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));    // TODO add your handling code here:
    }//GEN-LAST:event_logoutMouseEntered

    private void logoutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseExited
        logout.setForeground(new java.awt.Color(0, 0, 0));
        logout.setBackground(new java.awt.Color(12, 192, 223));
        logout.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));    // TODO add your handling code here:
    }//GEN-LAST:event_logoutMouseExited

    private void myitemsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_myitemsMouseExited
        myitems.setForeground(new java.awt.Color(0, 0, 0));
        myitems.setBackground(new java.awt.Color(12, 192, 223));
        myitems.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        // TODO add your handling code here:
    }//GEN-LAST:event_myitemsMouseExited

    private void myitemsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_myitemsMouseEntered
        myitems.setForeground(new java.awt.Color(255, 255, 255));
        myitems.setBackground(new java.awt.Color(8, 145, 178));
        myitems.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        // TODO add your handling code here:
    }//GEN-LAST:event_myitemsMouseEntered

    private void myitemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_myitemsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_myitemsMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Profile;
    private javax.swing.JLabel Reports;
    private javax.swing.JLabel dashboard;
    private javax.swing.JLabel finditems;
    private javax.swing.JPanel header;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel logo;
    private javax.swing.JLabel logout;
    private javax.swing.JLabel messages;
    private javax.swing.JLabel myitems;
    private javax.swing.JPanel myitemspanel;
    private javax.swing.JTable myitemstable;
    private javax.swing.JButton searchbutton;
    private javax.swing.JTextField searchfield;
    private javax.swing.JPanel tradermenu;
    private javax.swing.JLabel trades;
    // End of variables declaration//GEN-END:variables
}
