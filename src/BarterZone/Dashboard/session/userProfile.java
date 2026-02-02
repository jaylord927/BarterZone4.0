package BarterZone.Dashboard.session;

import database.config.config;
import javax.swing.JOptionPane;

public class userProfile extends javax.swing.JFrame {
    private int userId;
    private String userType;
    
    public userProfile(int userId, String userType) {
        this.userId = userId;
        this.userType = userType;
        initComponents();
        loadUserData();
        setSize(600, 400);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("User Profile");
    }
    
    private void loadUserData() {
        try {
            config dbConfig = new config();
            String sql = "SELECT user_fullname, user_username, user_email, user_type, user_status FROM tbl_users WHERE user_id = ?";
            
            java.util.List<java.util.Map<String, Object>> users = dbConfig.fetchRecords(sql, userId);
            
            if (!users.isEmpty()) {
                java.util.Map<String, Object> user = users.get(0);
                
                nameValue.setText((String) user.get("user_fullname"));
                usernameValue.setText((String) user.get("user_username"));
                emailValue.setText((String) user.get("user_email"));
                typeValue.setText((String) user.get("user_type"));
                statusValue.setText((String) user.get("user_status"));
            } else {
                JOptionPane.showMessageDialog(this, "User data not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading user data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        nameValue = new javax.swing.JLabel();
        usernameValue = new javax.swing.JLabel();
        emailValue = new javax.swing.JLabel();
        typeValue = new javax.swing.JLabel();
        statusValue = new javax.swing.JLabel();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Full Name:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Username:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Email:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("User Type:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Status:");

        nameValue.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        nameValue.setText("Loading...");

        usernameValue.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        usernameValue.setText("Loading...");

        emailValue.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        emailValue.setText("Loading...");

        typeValue.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        typeValue.setText("Loading...");

        statusValue.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        statusValue.setText("Loading...");

        closeButton.setBackground(new java.awt.Color(12, 192, 223));
        closeButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        closeButton.setForeground(new java.awt.Color(255, 255, 255));
        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(50, 50, 50)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nameValue)
                    .addComponent(usernameValue)
                    .addComponent(emailValue)
                    .addComponent(typeValue)
                    .addComponent(statusValue)
                    .addComponent(closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(200, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nameValue))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(usernameValue))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(emailValue))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(typeValue))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(statusValue))
                .addGap(50, 50, 50)
                .addComponent(closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>                        

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {                                            
        this.dispose();
    }                                           

    // Variables declaration - do not modify                     
    private javax.swing.JButton closeButton;
    private javax.swing.JLabel emailValue;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel nameValue;
    private javax.swing.JLabel statusValue;
    private javax.swing.JLabel typeValue;
    private javax.swing.JLabel usernameValue;
    // End of variables declaration                   
}