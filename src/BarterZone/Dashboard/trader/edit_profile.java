package BarterZone.Dashboard.trader;

import BarterZone.Dashboard.session.user_session;
import database.config.config;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class edit_profile extends javax.swing.JFrame {

    private user_session session;
    private config db;
    
    private javax.swing.JTextField fullNameField;
    private javax.swing.JTextField usernameField;
    private javax.swing.JTextField emailField;
    private javax.swing.JPasswordField currentPasswordField;
    private javax.swing.JPasswordField newPasswordField;
    private javax.swing.JPasswordField confirmPasswordField;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JCheckBox showPasswordCheck;

    public edit_profile() {
        this.session = user_session.getInstance();
        this.db = new config();
        
        initComponents();
        loadUserData();
        
        setTitle("Edit Profile");
        setSize(500, 550);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void loadUserData() {
        fullNameField.setText(session.getFullName());
        usernameField.setText(session.getUsername());
        emailField.setText(session.getEmail());
    }

    private void initComponents() {

        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(12, 192, 223));
        titlePanel.setBounds(0, 0, 500, 50);
        titlePanel.setLayout(null);

        javax.swing.JLabel titleLabel = new javax.swing.JLabel("EDIT PROFILE");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(20, 10, 200, 30);
        titlePanel.add(titleLabel);
        getContentPane().add(titlePanel);

        int startY = 70;
        int labelWidth = 120;
        int fieldWidth = 280;
        int labelX = 50;
        int fieldX = 180;

        javax.swing.JLabel fullNameLabel = new javax.swing.JLabel("Full Name:");
        fullNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        fullNameLabel.setBounds(labelX, startY, labelWidth, 25);
        getContentPane().add(fullNameLabel);

        fullNameField = new javax.swing.JTextField();
        fullNameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        fullNameField.setBounds(fieldX, startY, fieldWidth, 35);
        fullNameField.setBorder(new LineBorder(new Color(200, 200, 200)));
        getContentPane().add(fullNameField);

        javax.swing.JLabel usernameLabel = new javax.swing.JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        usernameLabel.setBounds(labelX, startY + 50, labelWidth, 25);
        getContentPane().add(usernameLabel);

        usernameField = new javax.swing.JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBounds(fieldX, startY + 50, fieldWidth, 35);
        usernameField.setBorder(new LineBorder(new Color(200, 200, 200)));
        getContentPane().add(usernameField);

        javax.swing.JLabel emailLabel = new javax.swing.JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        emailLabel.setBounds(labelX, startY + 100, labelWidth, 25);
        getContentPane().add(emailLabel);

        emailField = new javax.swing.JTextField();
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setBounds(fieldX, startY + 100, fieldWidth, 35);
        emailField.setBorder(new LineBorder(new Color(200, 200, 200)));
        getContentPane().add(emailField);

        javax.swing.JLabel currentPassLabel = new javax.swing.JLabel("Current Password:");
        currentPassLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        currentPassLabel.setBounds(labelX, startY + 150, labelWidth, 25);
        getContentPane().add(currentPassLabel);

        currentPasswordField = new javax.swing.JPasswordField();
        currentPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        currentPasswordField.setBounds(fieldX, startY + 150, fieldWidth, 35);
        currentPasswordField.setBorder(new LineBorder(new Color(200, 200, 200)));
        getContentPane().add(currentPasswordField);

        javax.swing.JLabel newPassLabel = new javax.swing.JLabel("New Password:");
        newPassLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        newPassLabel.setBounds(labelX, startY + 200, labelWidth, 25);
        getContentPane().add(newPassLabel);

        newPasswordField = new javax.swing.JPasswordField();
        newPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        newPasswordField.setBounds(fieldX, startY + 200, fieldWidth, 35);
        newPasswordField.setBorder(new LineBorder(new Color(200, 200, 200)));
        getContentPane().add(newPasswordField);

        javax.swing.JLabel confirmPassLabel = new javax.swing.JLabel("Confirm Password:");
        confirmPassLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        confirmPassLabel.setBounds(labelX, startY + 250, labelWidth, 25);
        getContentPane().add(confirmPassLabel);

        confirmPasswordField = new javax.swing.JPasswordField();
        confirmPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        confirmPasswordField.setBounds(fieldX, startY + 250, fieldWidth, 35);
        confirmPasswordField.setBorder(new LineBorder(new Color(200, 200, 200)));
        getContentPane().add(confirmPasswordField);

        showPasswordCheck = new javax.swing.JCheckBox("Show Password");
        showPasswordCheck.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        showPasswordCheck.setBackground(Color.WHITE);
        showPasswordCheck.setBounds(fieldX, startY + 295, 150, 25);
        showPasswordCheck.addActionListener(e -> togglePasswordVisibility());
        getContentPane().add(showPasswordCheck);

        saveButton = new javax.swing.JButton("SAVE CHANGES");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.setBackground(new Color(0, 102, 102));
        saveButton.setForeground(Color.WHITE);
        saveButton.setBounds(120, 460, 150, 40);
        saveButton.setBorder(null);
        saveButton.setFocusPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(e -> saveChanges());
        getContentPane().add(saveButton);

        cancelButton = new javax.swing.JButton("CANCEL");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setBackground(new Color(204, 0, 0));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBounds(280, 460, 100, 40);
        cancelButton.setBorder(null);
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> {
            profile profileFrame = new profile();
            profileFrame.setVisible(true);
            profileFrame.setLocationRelativeTo(null);
            this.dispose();
        });
        getContentPane().add(cancelButton);

        pack();
    }

    private void togglePasswordVisibility() {
        if (showPasswordCheck.isSelected()) {
            currentPasswordField.setEchoChar((char) 0);
            newPasswordField.setEchoChar((char) 0);
            confirmPasswordField.setEchoChar((char) 0);
        } else {
            currentPasswordField.setEchoChar('•');
            newPasswordField.setEchoChar('•');
            confirmPasswordField.setEchoChar('•');
        }
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
        String currentPass = new String(currentPasswordField.getPassword());
        String newPass = new String(newPasswordField.getPassword());
        String confirmPass = new String(confirmPasswordField.getPassword());

        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please fill in all required fields!", 
                "Incomplete Information", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, 
                "Hmm, that email doesn't look right. Please enter a valid email address (e.g., name@example.com).", 
                "Invalid Email", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check if username is already taken by another user
        if (!username.equals(session.getUsername()) && isUsernameTaken(username, session.getUserId())) {
            JOptionPane.showMessageDialog(this, 
                "The username '" + username + "' is already taken.\nPlease choose a different username.", 
                "Username Unavailable", 
                JOptionPane.WARNING_MESSAGE);
            usernameField.requestFocus();
            return;
        }

        // Check if email is already taken by another user
        if (!email.equals(session.getEmail()) && isEmailTaken(email, session.getUserId())) {
            JOptionPane.showMessageDialog(this, 
                "The email address '" + email + "' is already registered.\nPlease use a different email address.", 
                "Email Already Registered", 
                JOptionPane.WARNING_MESSAGE);
            emailField.requestFocus();
            return;
        }

        // Password change logic
        if (!currentPass.isEmpty() || !newPass.isEmpty() || !confirmPass.isEmpty()) {
            if (currentPass.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "To change your password, please enter your current password.", 
                    "Current Password Required", 
                    JOptionPane.WARNING_MESSAGE);
                currentPasswordField.requestFocus();
                return;
            }

            String sql = "SELECT user_pass FROM tbl_users WHERE user_id = ?";
            java.util.List<Map<String, Object>> result = db.fetchRecords(sql, session.getUserId());
            
            if (!result.isEmpty()) {
                String storedPass = (String) result.get(0).get("user_pass");
                String hashedCurrent = db.hashPassword(currentPass);
                
                if (!hashedCurrent.equals(storedPass)) {
                    JOptionPane.showMessageDialog(this, 
                        "The current password you entered is incorrect.\nPlease try again.", 
                        "Incorrect Password", 
                        JOptionPane.ERROR_MESSAGE);
                    currentPasswordField.requestFocus();
                    return;
                }
            }

            if (newPass.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter a new password.", 
                    "New Password Required", 
                    JOptionPane.WARNING_MESSAGE);
                newPasswordField.requestFocus();
                return;
            }

            if (newPass.length() < 6) {
                JOptionPane.showMessageDialog(this, 
                    "For security reasons, your password must be at least 6 characters long.", 
                    "Password Too Short", 
                    JOptionPane.WARNING_MESSAGE);
                newPasswordField.requestFocus();
                return;
            }

            if (!newPass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(this, 
                    "The new passwords you entered don't match.\nPlease make sure both passwords are identical.", 
                    "Passwords Don't Match", 
                    JOptionPane.WARNING_MESSAGE);
                confirmPasswordField.requestFocus();
                return;
            }
        }

        StringBuilder updateSql = new StringBuilder("UPDATE tbl_users SET user_fullname = ?, user_username = ?, user_email = ?");
        
        if (!newPass.isEmpty()) {
            updateSql.append(", user_pass = ?");
        }
        updateSql.append(" WHERE user_id = ?");

        try {
            if (!newPass.isEmpty()) {
                String hashedNewPass = db.hashPassword(newPass);
                db.updateRecord(updateSql.toString(), fullName, username, email, hashedNewPass, session.getUserId());
            } else {
                db.updateRecord(updateSql.toString(), fullName, username, email, session.getUserId());
            }

            session.setFullName(fullName);
            session.setUsername(username);
            session.setEmail(email);
            session.refreshData();
            
            JOptionPane.showMessageDialog(this, 
                "Great! Your profile has been updated successfully.", 
                "Profile Updated", 
                JOptionPane.INFORMATION_MESSAGE);
            
            profile profileFrame = new profile();
            profileFrame.setVisible(true);
            profileFrame.setLocationRelativeTo(null);
            this.dispose();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Sorry, we couldn't update your profile at this time.\nError: " + e.getMessage(), 
                "Update Failed", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}