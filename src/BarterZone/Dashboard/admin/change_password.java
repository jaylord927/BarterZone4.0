package BarterZone.Dashboard.admin;

import BarterZone.Dashboard.session.user_session;
import database.config.config;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.border.LineBorder;

public class change_password extends JDialog {

    private user_session session;
    private config db;
    private int adminId;
    private String adminName;

    private JTextField emailField;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JCheckBox showPasswordCheck;
    private JButton changeButton;
    private JButton cancelButton;

    public change_password(int adminId, String adminName) {
        super();
        this.session = user_session.getInstance();
        this.db = new config();
        this.adminId = adminId;
        this.adminName = adminName;

        initComponents();

        setTitle("BarterZone - Change Password");
        setIconImage(new ImageIcon(getClass().getResource(
                "/BarterZone/resources/icon/logo.png")).getImage());
        setSize(450, 400);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
    }

    private void initComponents() {
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(8, 78, 128));
        titlePanel.setBounds(0, 0, 450, 50);
        titlePanel.setLayout(null);

        JLabel titleLabel = new JLabel("CHANGE PASSWORD");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(20, 10, 250, 30);
        titlePanel.add(titleLabel);
        getContentPane().add(titlePanel);

        int startY = 70;
        int labelWidth = 120;
        int fieldWidth = 230;
        int labelX = 40;
        int fieldX = 170;

        JLabel emailLabel = new JLabel("Email Address:");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        emailLabel.setBounds(labelX, startY, labelWidth, 30);
        getContentPane().add(emailLabel);

        emailField = new JTextField();
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        emailField.setBounds(fieldX, startY, fieldWidth, 35);
        emailField.setBorder(new LineBorder(new Color(200, 200, 200)));
        getContentPane().add(emailField);

        JLabel currentPassLabel = new JLabel("Current Password:");
        currentPassLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        currentPassLabel.setBounds(labelX, startY + 55, labelWidth, 30);
        getContentPane().add(currentPassLabel);

        currentPasswordField = new JPasswordField();
        currentPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        currentPasswordField.setBounds(fieldX, startY + 55, fieldWidth, 35);
        currentPasswordField.setBorder(new LineBorder(new Color(200, 200, 200)));
        getContentPane().add(currentPasswordField);

        JLabel newPassLabel = new JLabel("New Password:");
        newPassLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        newPassLabel.setBounds(labelX, startY + 110, labelWidth, 30);
        getContentPane().add(newPassLabel);

        newPasswordField = new JPasswordField();
        newPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        newPasswordField.setBounds(fieldX, startY + 110, fieldWidth, 35);
        newPasswordField.setBorder(new LineBorder(new Color(200, 200, 200)));
        getContentPane().add(newPasswordField);

        JLabel confirmPassLabel = new JLabel("Confirm Password:");
        confirmPassLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        confirmPassLabel.setBounds(labelX, startY + 165, labelWidth, 30);
        getContentPane().add(confirmPassLabel);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        confirmPasswordField.setBounds(fieldX, startY + 165, fieldWidth, 35);
        confirmPasswordField.setBorder(new LineBorder(new Color(200, 200, 200)));
        getContentPane().add(confirmPasswordField);

        showPasswordCheck = new JCheckBox("Show Password");
        showPasswordCheck.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        showPasswordCheck.setBackground(Color.WHITE);
        showPasswordCheck.setBounds(fieldX, startY + 210, 150, 25);
        showPasswordCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                togglePasswordVisibility();
            }
        });
        getContentPane().add(showPasswordCheck);

        changeButton = new JButton("CHANGE PASSWORD");
        changeButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        changeButton.setBackground(new Color(46, 125, 50));
        changeButton.setForeground(Color.WHITE);
        changeButton.setBounds(100, 310, 150, 40);
        changeButton.setBorder(null);
        changeButton.setFocusPainted(false);
        changeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        changeButton.addActionListener(e -> changePassword());
        getContentPane().add(changeButton);

        cancelButton = new JButton("CANCEL");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cancelButton.setBackground(new Color(204, 0, 0));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBounds(270, 310, 100, 40);
        cancelButton.setBorder(null);
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> dispose());
        getContentPane().add(cancelButton);

        // Pre-fill email field with current user's email
        emailField.setText(session.getEmail());
        emailField.setEditable(false);
        emailField.setBackground(new Color(240, 240, 240));
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

    private void changePassword() {
        String email = emailField.getText().trim();
        String currentPass = new String(currentPasswordField.getPassword());
        String newPass = new String(newPasswordField.getPassword());
        String confirmPass = new String(confirmPasswordField.getPassword());

        // Validate email
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Email address is required.",
                    "Email Required",
                    JOptionPane.WARNING_MESSAGE);
            emailField.requestFocus();
            return;
        }

        // Verify email matches the logged-in user
        if (!email.equals(session.getEmail())) {
            JOptionPane.showMessageDialog(this,
                    "Email address does not match your account.\nPlease enter the correct email address.",
                    "Email Mismatch",
                    JOptionPane.ERROR_MESSAGE);
            emailField.requestFocus();
            return;
        }

        // Validate current password
        if (currentPass.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter your current password.",
                    "Current Password Required",
                    JOptionPane.WARNING_MESSAGE);
            currentPasswordField.requestFocus();
            return;
        }

        // Verify current password
        String sql = "SELECT user_pass FROM tbl_users WHERE user_id = ?";
        List<Map<String, Object>> result = db.fetchRecords(sql, adminId);

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

        // Validate new password
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
                    "Password must be at least 6 characters long.",
                    "Password Too Short",
                    JOptionPane.WARNING_MESSAGE);
            newPasswordField.requestFocus();
            return;
        }

        // Check if new password matches confirm password
        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this,
                    "The new passwords you entered don't match.\nPlease make sure both passwords are identical.",
                    "Passwords Don't Match",
                    JOptionPane.WARNING_MESSAGE);
            confirmPasswordField.requestFocus();
            return;
        }

        // Check if new password is same as current password
        String hashedNewPass = db.hashPassword(newPass);
        String hashedCurrent = db.hashPassword(currentPass);
        if (hashedNewPass.equals(hashedCurrent)) {
            JOptionPane.showMessageDialog(this,
                    "New password cannot be the same as your current password.\nPlease choose a different password.",
                    "Same Password",
                    JOptionPane.WARNING_MESSAGE);
            newPasswordField.requestFocus();
            return;
        }

        // Update password
        String updateSql = "UPDATE tbl_users SET user_pass = ? WHERE user_id = ?";

        try {
            db.updateRecord(updateSql, hashedNewPass, adminId);

            JOptionPane.showMessageDialog(this,
                    "Password changed successfully!\n\nPlease login again with your new password.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();

            // Logout and redirect to login
            session.logout();
            BarterZone.loginandsignup.login loginFrame = new BarterZone.loginandsignup.login();
            loginFrame.setVisible(true);
            loginFrame.setLocationRelativeTo(null);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error changing password: " + e.getMessage(),
                    "Update Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
