package landing;

import Barterzone.ui.Transition;
import loginandsignup.Login;
import loginandsignup.SignUp;
//import BarterZone.Dashboard.trader;
import database.config.config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class landing extends javax.swing.JFrame {

    private int traderId = -1;
    private String traderName = "";

    public landing() {
        initComponents();
        setButtonsForGuest();
        setSize(800, 500);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public landing(int traderId, String traderName) {
        this.traderId = traderId;
        this.traderName = traderName;
        initComponents();
        setButtonsForLoggedIn();
    }

    private void setButtonsForGuest() {
        login.setText("Login");
        signup.setText("Sign Up");
    }

    private void setButtonsForLoggedIn() {
        login.setText("Trader Menu");
        signup.setText("Logout");

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        Searchbutton = new javax.swing.JTextField();
        igive = new javax.swing.JButton();
        iwant = new javax.swing.JButton();
        signup = new javax.swing.JButton();
        login = new javax.swing.JButton();
        searchbutton = new javax.swing.JButton();
        landing = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setBackground(new java.awt.Color(22, 114, 127));
        jLabel1.setForeground(new java.awt.Color(240, 240, 240));
        jLabel1.setText("Search here");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 230, 110, 20));

        Searchbutton.setBackground(new java.awt.Color(22, 114, 127));
        Searchbutton.setFont(new java.awt.Font("Comic Sans MS", 1, 10)); // NOI18N
        Searchbutton.setForeground(new java.awt.Color(51, 255, 51));
        Searchbutton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Searchbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchbuttonActionPerformed(evt);
            }
        });
        getContentPane().add(Searchbutton, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 210, 190, 60));

        igive.setBackground(new java.awt.Color(255, 255, 255));
        igive.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        igive.setForeground(new java.awt.Color(32, 118, 3));
        igive.setText("I give");
        igive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                igiveActionPerformed(evt);
            }
        });
        getContentPane().add(igive, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 280, 80, 40));

        iwant.setBackground(new java.awt.Color(255, 255, 255));
        iwant.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        iwant.setForeground(new java.awt.Color(240, 128, 22));
        iwant.setText("I want");
        iwant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iwantActionPerformed(evt);
            }
        });
        getContentPane().add(iwant, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 280, 80, 40));

        signup.setBackground(new java.awt.Color(0, 102, 102));
        signup.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        signup.setForeground(new java.awt.Color(51, 255, 51));
        signup.setText("Sign Up");
        signup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signupActionPerformed(evt);
            }
        });
        getContentPane().add(signup, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 10, 150, 40));

        login.setBackground(new java.awt.Color(0, 102, 102));
        login.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        login.setForeground(new java.awt.Color(51, 255, 51));
        login.setText("Login");
        login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginActionPerformed(evt);
            }
        });
        getContentPane().add(login, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 10, 150, 40));

        searchbutton.setBackground(new java.awt.Color(22, 114, 127));
        searchbutton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        searchbutton.setForeground(new java.awt.Color(51, 255, 51));
        searchbutton.setText("Click here");
        searchbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchbuttonActionPerformed(evt);
            }
        });
        getContentPane().add(searchbutton, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 210, -1, 60));

        landing.setBackground(new java.awt.Color(22, 114, 127));
        landing.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        landing.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/images/landing.png"))); // NOI18N
        getContentPane().add(landing, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 500));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SearchbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchbuttonActionPerformed


    }//GEN-LAST:event_SearchbuttonActionPerformed

    private void igiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_igiveActionPerformed

// TODO add your handling code here:
    }//GEN-LAST:event_igiveActionPerformed

    private void iwantActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iwantActionPerformed

// TODO add your handling code here:
    }//GEN-LAST:event_iwantActionPerformed

    private void signupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signupActionPerformed

        if (traderId == -1) {
            SignUp signupFrame = new SignUp();
            signupFrame.setVisible(true);
            signupFrame.pack();
            signupFrame.setLocationRelativeTo(null);
            this.dispose();
        } else {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                Login loginFrame = new Login();
                loginFrame.setVisible(true);
                loginFrame.setLocationRelativeTo(null);
                this.dispose();
            }
        }


    }//GEN-LAST:event_signupActionPerformed

    private void loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginActionPerformed

        if (traderId == -1) {
            Login loginFrame = new Login();
            loginFrame.setVisible(true);
            loginFrame.pack();
            loginFrame.setLocationRelativeTo(null);
            this.dispose();
        } else {
            openTraderDashboard();
        }
    }//GEN-LAST:event_loginActionPerformed

    private void openTraderDashboard() {
        BarterZone.Dashboard.trader.trader_dashboard traderDashboard = new BarterZone.Dashboard.trader.trader_dashboard(traderId, traderName);
        traderDashboard.setVisible(true);
        traderDashboard.setLocationRelativeTo(null);
        this.dispose();

    }


    private void searchbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchbuttonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchbuttonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Searchbutton;
    private javax.swing.JButton igive;
    private javax.swing.JButton iwant;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel landing;
    private javax.swing.JButton login;
    private javax.swing.JButton searchbutton;
    private javax.swing.JButton signup;
    // End of variables declaration//GEN-END:variables
}
