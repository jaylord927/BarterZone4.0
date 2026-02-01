package BarterZone.dialmessage;

public class successmessage extends javax.swing.JFrame {

    public successmessage() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        logo = new javax.swing.JLabel();
        okaybutton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/images/message.png"))); // NOI18N
        logo.setText("jLabel1");
        getContentPane().add(logo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -10, 130, 120));

        okaybutton.setBackground(new java.awt.Color(6, 154, 19));
        okaybutton.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        okaybutton.setForeground(new java.awt.Color(51, 255, 51));
        okaybutton.setText("OKAY");
        okaybutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okaybuttonActionPerformed(evt);
            }
        });
        getContentPane().add(okaybutton, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, 80, 30));

        jLabel1.setBackground(new java.awt.Color(51, 255, 51));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 19)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 153, 0));
        jLabel1.setText("SUCCESFUL!");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 0, 140, 60));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okaybuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okaybuttonActionPerformed
        landing.landing landingFrame = new landing.landing();
        landingFrame.setVisible(true);
        landingFrame.pack();
        landingFrame.setLocationRelativeTo(null);

        this.dispose();


    }//GEN-LAST:event_okaybuttonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel logo;
    private javax.swing.JButton okaybutton;
    // End of variables declaration//GEN-END:variables
}
