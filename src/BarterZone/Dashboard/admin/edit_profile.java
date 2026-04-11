package BarterZone.Dashboard.admin;

import BarterZone.Dashboard.session.user_session;
import database.config.config;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import java.awt.Dialog;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

public class edit_profile extends JDialog {

    private user_session session;
    private config db;
    private int adminId;
    private String adminName;
    
    private JTextField fullNameField;
    private JTextField usernameField;
    private JTextField emailField;
    private JButton saveButton;
    private JButton cancelButton;

    public edit_profile(int adminId, String adminName) {
        super();
        this.session = user_session.getInstance();
        this.db = new config();
        this.adminId = adminId;
        this.adminName = adminName;
        
        initComponents();
        loadUserData();
        
        setTitle("BarterZone - Edit Profile");
        setIconImage(new ImageIcon(getClass().getResource(
                "/BarterZone/resources/icon/logo.png")).getImage());
        setSize(450, 350);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
    }

    private void loadUserData() {
        String sql = "SELECT user_fullname, user_username, user_email FROM tbl_users WHERE user_id = ?";
        List<Map<String, Object>> result = db.fetchRecords(sql, adminId);
        
        if (!result.isEmpty()) {
            Map<String, Object> user = result.get(0);
            fullNameField.setText(user.get("user_fullname") != null ? user.get("user_fullname").toString() : "");
            usernameField.setText(user.get("user_username") != null ? user.get("user_username").toString() : "");
            emailField.setText(user.get("user_email") != null ? user.get("user_email").toString() : "");
        }
    }

    private void initComponents() {
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(8, 78, 128));
        titlePanel.setBounds(0, 0, 450, 50);
        titlePanel.setLayout(null);

        JLabel titleLabel = new JLabel("EDIT PROFILE");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(20, 10, 200, 30);
        titlePanel.add(titleLabel);
        getContentPane().add(titlePanel);

        int startY = 80;
        int labelWidth = 100;
        int fieldWidth = 250;
        int labelX = 50;
        int fieldX = 160;

        JLabel fullNameLabel = new JLabel("Full Name:");
        fullNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        fullNameLabel.setBounds(labelX, startY, labelWidth, 30);
        getContentPane().add(fullNameLabel);

        fullNameField = new JTextField();
        fullNameField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        fullNameField.setBounds(fieldX, startY, fieldWidth, 35);
        fullNameField.setBorder(new LineBorder(new Color(200, 200, 200)));
        getContentPane().add(fullNameField);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        usernameLabel.setBounds(labelX, startY + 55, labelWidth, 30);
        getContentPane().add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        usernameField.setBounds(fieldX, startY + 55, fieldWidth, 35);
        usernameField.setBorder(new LineBorder(new Color(200, 200, 200)));
        getContentPane().add(usernameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        emailLabel.setBounds(labelX, startY + 110, labelWidth, 30);
        getContentPane().add(emailLabel);

        emailField = new JTextField();
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        emailField.setBounds(fieldX, startY + 110, fieldWidth, 35);
        emailField.setBorder(new LineBorder(new Color(200, 200, 200)));
        getContentPane().add(emailField);

        JLabel noteLabel = new JLabel("");
        noteLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        noteLabel.setForeground(new Color(150, 150, 150));
        noteLabel.setBounds(50, startY + 165, 350, 20);
        getContentPane().add(noteLabel);

        saveButton = new JButton("SAVE CHANGES");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        saveButton.setBackground(new Color(46, 125, 50));
        saveButton.setForeground(Color.WHITE);
        saveButton.setBounds(100, 250, 130, 40);
        saveButton.setBorder(null);
        saveButton.setFocusPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(e -> saveChanges());
        getContentPane().add(saveButton);

        cancelButton = new JButton("CANCEL");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cancelButton.setBackground(new Color(204, 0, 0));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBounds(250, 250, 100, 40);
        cancelButton.setBorder(null);
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> dispose());
        getContentPane().add(cancelButton);
    }

    private boolean isUsernameTaken(String username, int currentUserId) {
        String sql = "SELECT COUNT(*) as count FROM tbl_users WHERE user_username = ? AND user_id != ?";
        double count = db.getSingleValue(sql, username, currentUserId);
        return count > 0;
    }

    private boolean isEmailTaken(String email, int currentUserId) {
        String sql = "SELECT COUNT(*) as count FROM tbl_users WHERE user_email = ? AND user_id != ?";
        double count = db.getSingleValue(sql, email, currentUserId);
        return count > 0;
    }

    private void saveChanges() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();

        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please fill in all required fields!", 
                "Incomplete Information", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid email address.", 
                "Invalid Email", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check if username is already taken by another user
        if (!username.equals(session.getUsername()) && isUsernameTaken(username, adminId)) {
            JOptionPane.showMessageDialog(this, 
                "The username '" + username + "' is already taken.\nPlease choose a different username.", 
                "Username Unavailable", 
                JOptionPane.WARNING_MESSAGE);
            usernameField.requestFocus();
            return;
        }

        // Check if email is already taken by another user
        if (!email.equals(session.getEmail()) && isEmailTaken(email, adminId)) {
            JOptionPane.showMessageDialog(this, 
                "The email address '" + email + "' is already registered.\nPlease use a different email address.", 
                "Email Already Registered", 
                JOptionPane.WARNING_MESSAGE);
            emailField.requestFocus();
            return;
        }

        String sql = "UPDATE tbl_users SET user_fullname = ?, user_username = ?, user_email = ? WHERE user_id = ?";
        
        try {
            db.updateRecord(sql, fullName, username, email, adminId);

            // Update session data
            session.setFullName(fullName);
            session.setUsername(username);
            session.setEmail(email);
            session.refreshData();
            
            JOptionPane.showMessageDialog(this, 
                "Profile updated successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error updating profile: " + e.getMessage(), 
                "Update Failed", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}