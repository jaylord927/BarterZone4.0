package BarterZone.loginandsignup;

//import Barterzone.ui.Transition;
import database.config.config;
import javax.swing.JOptionPane;
import landing.landing;

public class login extends javax.swing.JFrame {

    public login() {
        initComponents();
        setSize(800, 500);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        left = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        logo = new javax.swing.JLabel();
        homebutton = new javax.swing.JButton();
        Right = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        emailusernametext = new javax.swing.JLabel();
        emailuser = new javax.swing.JTextField();
        passwordtext = new javax.swing.JLabel();
        pass = new javax.swing.JPasswordField();
        login = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        signup = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        left.setBackground(new java.awt.Color(12, 192, 223));
        left.setPreferredSize(new java.awt.Dimension(400, 500));

        jLabel5.setBackground(new java.awt.Color(102, 187, 198));

        logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/logo.png"))); // NOI18N

        homebutton.setBackground(new java.awt.Color(0, 102, 102));
        homebutton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        homebutton.setForeground(new java.awt.Color(11, 188, 46));
        homebutton.setText("HOME");
        homebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homebuttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout leftLayout = new javax.swing.GroupLayout(left);
        left.setLayout(leftLayout);
        leftLayout.setHorizontalGroup(
            leftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftLayout.createSequentialGroup()
                .addGroup(leftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(leftLayout.createSequentialGroup()
                        .addGap(424, 424, 424)
                        .addComponent(jLabel5))
                    .addGroup(leftLayout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(leftLayout.createSequentialGroup()
                        .addGap(164, 164, 164)
                        .addComponent(homebutton, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        leftLayout.setVerticalGroup(
            leftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftLayout.createSequentialGroup()
                .addGap(128, 128, 128)
                .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(homebutton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel5)
                .addContainerGap(112, Short.MAX_VALUE))
        );

        Right.setBackground(new java.awt.Color(255, 255, 255));
        Right.setMinimumSize(new java.awt.Dimension(400, 500));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 102));
        jLabel1.setText("LOGIN");

        emailusernametext.setBackground(new java.awt.Color(102, 102, 102));
        emailusernametext.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        emailusernametext.setText("Email or Username");

        emailuser.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        emailuser.setForeground(new java.awt.Color(102, 102, 102));
        emailuser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailuserActionPerformed(evt);
            }
        });

        passwordtext.setBackground(new java.awt.Color(102, 102, 102));
        passwordtext.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        passwordtext.setText("Password");

        pass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passActionPerformed(evt);
            }
        });

        login.setBackground(new java.awt.Color(0, 102, 102));
        login.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        login.setForeground(new java.awt.Color(255, 255, 255));
        login.setText("Login");
        login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginActionPerformed(evt);
            }
        });

        jLabel4.setText("I don't have an account");

        signup.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        signup.setForeground(new java.awt.Color(11, 188, 46));
        signup.setText("Sign Up");
        signup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signupActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout RightLayout = new javax.swing.GroupLayout(Right);
        Right.setLayout(RightLayout);
        RightLayout.setHorizontalGroup(
            RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RightLayout.createSequentialGroup()
                .addGroup(RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(RightLayout.createSequentialGroup()
                        .addGap(138, 138, 138)
                        .addComponent(jLabel1))
                    .addGroup(RightLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(emailusernametext)
                                .addComponent(emailuser)
                                .addComponent(passwordtext)
                                .addComponent(pass, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                                .addComponent(login, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(RightLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(signup)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        RightLayout.setVerticalGroup(
            RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RightLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(jLabel1)
                .addGap(40, 40, 40)
                .addComponent(emailusernametext)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(emailuser, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(passwordtext)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pass, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(login, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(signup))
                .addContainerGap(77, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(left, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(Right, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(left, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Right, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void homebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homebuttonActionPerformed
        landing landingFrame = new landing();
        landingFrame.setVisible(true);
        landingFrame.pack();
        landingFrame.setLocationRelativeTo(null);
        this.dispose();

    }//GEN-LAST:event_homebuttonActionPerformed

    private void emailuserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailuserActionPerformed

    }//GEN-LAST:event_emailuserActionPerformed

    private void passActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passActionPerformed

        // TODO add your handling code here:
    }//GEN-LAST:event_passActionPerformed

    private void loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginActionPerformed

        String usernameOrEmail = emailuser.getText().trim();
        String password = new String(pass.getPassword());

        if (usernameOrEmail.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username/email and password!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            config dbConfig = new config();

            String checkUserSQL = "SELECT user_id, user_fullname, user_username, user_email, user_pass, user_type, user_status FROM tbl_users WHERE user_username = ? OR user_email = ?";

            java.util.List<java.util.Map<String, Object>> users = dbConfig.fetchRecords(checkUserSQL, usernameOrEmail, usernameOrEmail);

            if (users.isEmpty()) {
                JOptionPane.showMessageDialog(this, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            java.util.Map<String, Object> user = users.get(0);
            String storedHashedPassword = (String) user.get("user_pass");
            String userFullName = (String) user.get("user_fullname");
            int userId = (int) user.get("user_id");
            String userType = (String) user.get("user_type");
            String userStatus = (String) user.get("user_status");

            if (!"active".equalsIgnoreCase(userStatus)) {
                JOptionPane.showMessageDialog(this, "Your account is inactive. Please wait for admin approval.", "Account Inactive", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String inputHashedPassword = dbConfig.hashPassword(password);

            if (inputHashedPassword.equals(storedHashedPassword)) {
                JOptionPane.showMessageDialog(this, "Login successful!\nWelcome " + userFullName + " (" + userType + ")", "Success", JOptionPane.INFORMATION_MESSAGE);

                if ("admin".equalsIgnoreCase(userType)) {
                    try {
                        BarterZone.Dashboard.admin.admin_dashboard adminFrame = new BarterZone.Dashboard.admin.admin_dashboard(userId, userFullName);
                        adminFrame.setVisible(true);
                        adminFrame.pack();
                        adminFrame.setLocationRelativeTo(null);
                        this.dispose();

                    } catch (Exception e) {
                        try {
                            BarterZone.Dashboard.admin.admin_dashboard adminFrame = new BarterZone.Dashboard.admin.admin_dashboard(userId, userFullName);
                            adminFrame.setVisible(true);
                            adminFrame.pack();
                            adminFrame.setLocationRelativeTo(null);
                            this.dispose();
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this,
                                    "Admin Dashboard not found! Redirecting to main page.",
                                    "Admin Dashboard Error",
                                    JOptionPane.WARNING_MESSAGE);

                            landing landingFrame = new landing(userId, userFullName);
                            landingFrame.setVisible(true);
                            landingFrame.pack();
                            landingFrame.setLocationRelativeTo(null);
                            this.dispose();
                        }
                    }

                } else if ("trader".equalsIgnoreCase(userType)) {
                    BarterZone.Dashboard.trader.trader_dashboard traderDashboard = new BarterZone.Dashboard.trader.trader_dashboard(userId, userFullName);
                    traderDashboard.setVisible(true);
                    traderDashboard.pack();
                    traderDashboard.setLocationRelativeTo(null);
                    this.dispose();

                } else {
                    landing landingFrame = new landing(userId, userFullName);
                    landingFrame.setVisible(true);
                    landingFrame.pack();
                    landingFrame.setLocationRelativeTo(null);
                    this.dispose();
                }

                pass.setText("");

            } else {
                JOptionPane.showMessageDialog(this, "Incorrect password!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }//GEN-LAST:event_loginActionPerformed

    private void signupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signupActionPerformed

        signup signupFrame = new signup();
        signupFrame.setVisible(true);
        signupFrame.pack();
        signupFrame.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_signupActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Right;
    private javax.swing.JTextField emailuser;
    private javax.swing.JLabel emailusernametext;
    private javax.swing.JButton homebutton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel left;
    private javax.swing.JButton login;
    private javax.swing.JLabel logo;
    private javax.swing.JPasswordField pass;
    private javax.swing.JLabel passwordtext;
    private javax.swing.JButton signup;
    // End of variables declaration//GEN-END:variables
}
