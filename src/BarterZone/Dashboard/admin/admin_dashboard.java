package BarterZone.Dashboard.admin;

import BarterZone.loginandsignup.login;
import database.config.config;
import javax.swing.JOptionPane;

public class admin_dashboard extends javax.swing.JFrame {

    private int adminId;
    private String adminName;

    public admin_dashboard(int adminId, String adminName) {
        this.adminId = adminId;
        this.adminName = adminName;
        initComponents();
        setTitle("Admin Dashboard - " + adminName);
        setSize(800, 500);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void loadAllUsersToTable() {
        database.config.config dbConfig = new database.config.config();
        String sql = "SELECT user_id, user_fullname, user_username, user_email, user_type, user_status FROM tbl_users";
        dbConfig.displayData(sql, tableusers);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        header = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        logout = new javax.swing.JLabel();
        manageusers = new javax.swing.JLabel();
        manageannouncement = new javax.swing.JLabel();
        managereports = new javax.swing.JLabel();
        adminprofile = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableusers = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        header.setBackground(new java.awt.Color(12, 192, 223));
        header.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel3.setText("Admin Menu");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });
        header.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 140, 60));

        logout.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
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
        header.add(logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 20, 60, 30));

        manageusers.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        manageusers.setText("Manage Users");
        manageusers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                manageusersMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                manageusersMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                manageusersMouseExited(evt);
            }
        });
        header.add(manageusers, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 20, 130, 30));

        manageannouncement.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        manageannouncement.setText("Manage Announcement");
        manageannouncement.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                manageannouncementMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                manageannouncementMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                manageannouncementMouseExited(evt);
            }
        });
        header.add(manageannouncement, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 20, 180, 30));

        managereports.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        managereports.setText("Manage Reports");
        managereports.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                managereportsMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                managereportsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                managereportsMouseExited(evt);
            }
        });
        header.add(managereports, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 20, 130, 30));

        adminprofile.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        adminprofile.setText("Profile");
        adminprofile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                adminprofileMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                adminprofileMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                adminprofileMouseExited(evt);
            }
        });
        header.add(adminprofile, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 70, 30));

        tableusers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableusers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableusersMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableusers);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(header, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel3MouseClicked

    private void logoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseClicked
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            login loginFrame = new login();
            loginFrame.setVisible(true);
            loginFrame.setLocationRelativeTo(null);
            this.dispose();
        }

    }//GEN-LAST:event_logoutMouseClicked

    private void logoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseEntered
        logout.setForeground(new java.awt.Color(255, 255, 255));
        logout.setBackground(new java.awt.Color(8, 145, 178));
        logout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

    }//GEN-LAST:event_logoutMouseEntered

    private void logoutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseExited
        logout.setForeground(new java.awt.Color(0, 0, 0));
        logout.setBackground(new java.awt.Color(12, 192, 223));
        logout.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_logoutMouseExited

    private void manageusersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manageusersMouseClicked
//        loadAllUsersToTable();
        manageusers manageusersFrame = new manageusers(adminId, adminName);
        manageusersFrame.setVisible(true);
        manageusersFrame.setLocationRelativeTo(null);
//        this.dispose();
    }//GEN-LAST:event_manageusersMouseClicked

    private void manageusersMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manageusersMouseEntered
        manageusers.setForeground(new java.awt.Color(255, 255, 255));
        manageusers.setBackground(new java.awt.Color(8, 145, 178));
        manageusers.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));


    }//GEN-LAST:event_manageusersMouseEntered

    private void manageusersMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manageusersMouseExited
        manageusers.setForeground(new java.awt.Color(0, 0, 0));
        manageusers.setBackground(new java.awt.Color(12, 192, 223));
        manageusers.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

// TODO add your handling code here:
    }//GEN-LAST:event_manageusersMouseExited

    private void manageannouncementMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manageannouncementMouseClicked

// TODO add your handling code here:
    }//GEN-LAST:event_manageannouncementMouseClicked

    private void manageannouncementMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manageannouncementMouseEntered
        manageannouncement.setForeground(new java.awt.Color(255, 255, 255));
        manageannouncement.setBackground(new java.awt.Color(8, 145, 178));
        manageannouncement.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));


    }//GEN-LAST:event_manageannouncementMouseEntered

    private void manageannouncementMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manageannouncementMouseExited
        manageannouncement.setForeground(new java.awt.Color(0, 0, 0));
        manageannouncement.setBackground(new java.awt.Color(12, 192, 223));
        manageannouncement.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

    }//GEN-LAST:event_manageannouncementMouseExited

    private void managereportsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_managereportsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_managereportsMouseClicked

    private void managereportsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_managereportsMouseEntered
        managereports.setForeground(new java.awt.Color(255, 255, 255));
        managereports.setBackground(new java.awt.Color(8, 145, 178));
        managereports.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

    }//GEN-LAST:event_managereportsMouseEntered

    private void managereportsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_managereportsMouseExited
        managereports.setForeground(new java.awt.Color(0, 0, 0));
        managereports.setBackground(new java.awt.Color(12, 192, 223));
        managereports.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));


    }//GEN-LAST:event_managereportsMouseExited

    private void tableusersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableusersMouseClicked


    }//GEN-LAST:event_tableusersMouseClicked

    private void adminprofileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_adminprofileMouseClicked
        BarterZone.Dashboard.session.user_profile profileFrame = new BarterZone.Dashboard.session.user_profile(adminId, "admin");
        profileFrame.setVisible(true);
        profileFrame.setLocationRelativeTo(null);
    }//GEN-LAST:event_adminprofileMouseClicked

    private void adminprofileMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_adminprofileMouseEntered
        adminprofile.setForeground(new java.awt.Color(255, 255, 255));
        adminprofile.setBackground(new java.awt.Color(8, 145, 178));
        adminprofile.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_adminprofileMouseEntered

    private void adminprofileMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_adminprofileMouseExited
        adminprofile.setForeground(new java.awt.Color(0, 0, 0));
        adminprofile.setBackground(new java.awt.Color(12, 192, 223));
        adminprofile.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));        // TODO add your handling code here:
    }//GEN-LAST:event_adminprofileMouseExited


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel adminprofile;
    private javax.swing.JPanel header;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel logout;
    private javax.swing.JLabel manageannouncement;
    private javax.swing.JLabel managereports;
    private javax.swing.JLabel manageusers;
    private javax.swing.JTable tableusers;
    // End of variables declaration//GEN-END:variables
}
