package BarterZone.Dashboard.trader;

import database.config.config;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class step3_submit {
    
    private int tradeId;
    private int traderId;
    private config db;
    private JFrame parentFrame;
    
    // Payment details
    private int selectedAdminId = -1;
    private String selectedAdminName = "";
    private String paymentMethod = "";
    private String paymentAccountNumber = "";
    private String paymentAccountName = "";
    private String paymentQrCode = "";
    private boolean paymentSubmitted = false;
    private boolean paymentVerified = false;
    private double adminFee = 15;
    private double totalAmount = 215;
    private int myPaymentId = -1;
    
    // UI Components
    private JComboBox<String> adminComboBox;
    private JComboBox<String> paymentMethodCombo;
    private JTextField accountNumberField;
    private JTextField accountNameField;
    private JButton uploadQrButton;
    private JLabel qrFileNameLabel;
    private String uploadedQrPath = "";
    
    private static final String QR_CODE_PATH = "src/BarterZone/resources/images/qrcodes/";
    private Color successColor = new Color(46, 125, 50);
    private Color themeColor = new Color(12, 192, 223);
    
    public step3_submit(int tradeId, int traderId, JFrame parentFrame) {
        this.tradeId = tradeId;
        this.traderId = traderId;
        this.db = new config();
        this.parentFrame = parentFrame;
        
        createDirectories();
        loadPaymentDetails();
    }
    
    private void createDirectories() {
        File directory = new File(QR_CODE_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
    
    private void loadPaymentDetails() {
        String sql = "SELECT * FROM tbl_payment_details WHERE trade_id = ? AND trader_id = ?";
        List<Map<String, Object>> details = db.fetchRecords(sql, tradeId, traderId);
        
        if (!details.isEmpty()) {
            Map<String, Object> d = details.get(0);
            myPaymentId = Integer.parseInt(d.get("payment_id").toString());
            selectedAdminId = d.get("admin_id") != null ? Integer.parseInt(d.get("admin_id").toString()) : -1;
            paymentMethod = d.get("payment_method") != null ? d.get("payment_method").toString() : "";
            paymentAccountNumber = d.get("account_number") != null ? d.get("account_number").toString() : "";
            paymentAccountName = d.get("account_name") != null ? d.get("account_name").toString() : "";
            paymentQrCode = d.get("qr_code_path") != null ? d.get("qr_code_path").toString() : "";
            paymentSubmitted = d.get("payment_submitted") != null && Integer.parseInt(d.get("payment_submitted").toString()) == 1;
            paymentVerified = d.get("payment_verified") != null && Integer.parseInt(d.get("payment_verified").toString()) == 1;
            
            if (selectedAdminId != -1) {
                loadAdminName();
            }
        }
    }
    
    private void loadAdminName() {
        String sql = "SELECT user_fullname FROM tbl_users WHERE user_id = ?";
        List<Map<String, Object>> result = db.fetchRecords(sql, selectedAdminId);
        if (!result.isEmpty()) {
            selectedAdminName = result.get(0).get("user_fullname").toString();
        }
    }
    
    private void loadAdmins() {
        String sql = "SELECT user_id, user_fullname FROM tbl_users WHERE user_type = 'admin' AND user_status = 'active'";
        List<Map<String, Object>> admins = db.fetchRecords(sql);
        
        adminComboBox.removeAllItems();
        adminComboBox.addItem("-- Select Admin --");
        for (Map<String, Object> admin : admins) {
            adminComboBox.addItem(admin.get("user_fullname").toString());
        }
        
        if (selectedAdminId != -1 && !selectedAdminName.isEmpty()) {
            adminComboBox.setSelectedItem(selectedAdminName);
        }
    }
    
    public JPanel createPaymentPanel() {
        JPanel paymentPanel = new JPanel();
        paymentPanel.setLayout(null);
        paymentPanel.setBackground(Color.WHITE);
        paymentPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0, 102, 102)), "Payment Information"));
        
        loadAdmins();
        
        int py = 25;
        
        JLabel adminLabel = new JLabel("Select Admin (Middleman):");
        adminLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        adminLabel.setBounds(20, py, 200, 25);
        paymentPanel.add(adminLabel);
        
        adminComboBox = new JComboBox<>();
        adminComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        adminComboBox.setBounds(230, py, 250, 30);
        paymentPanel.add(adminComboBox);
        py += 45;
        
        JLabel feeLabel = new JLabel("Admin Fee: ₱" + String.format("%.2f", adminFee));
        feeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        feeLabel.setBounds(20, py, 200, 25);
        paymentPanel.add(feeLabel);
        
        JLabel totalLabel = new JLabel("Total Amount: ₱" + String.format("%.2f", totalAmount));
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        totalLabel.setForeground(successColor);
        totalLabel.setBounds(230, py, 200, 25);
        paymentPanel.add(totalLabel);
        py += 45;
        
        JLabel detailsTitle = new JLabel("PAYMENT DETAILS");
        detailsTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        detailsTitle.setForeground(new Color(0, 102, 102));
        detailsTitle.setBounds(20, py, 200, 25);
        paymentPanel.add(detailsTitle);
        py += 35;
        
        String[] paymentMethods = {"Select Method", "GCash", "PayMaya", "Bank Transfer"};
        paymentMethodCombo = new JComboBox<>(paymentMethods);
        paymentMethodCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JLabel methodLabel = new JLabel("Payment Method:*");
        methodLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        methodLabel.setBounds(20, py, 120, 25);
        paymentPanel.add(methodLabel);
        paymentMethodCombo.setBounds(150, py, 200, 30);
        paymentPanel.add(paymentMethodCombo);
        py += 40;
        
        accountNumberField = new JTextField();
        accountNumberField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JLabel numberLabel = new JLabel("Account Number:*");
        numberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        numberLabel.setBounds(20, py, 120, 25);
        paymentPanel.add(numberLabel);
        accountNumberField.setBounds(150, py, 250, 30);
        paymentPanel.add(accountNumberField);
        py += 40;
        
        accountNameField = new JTextField();
        accountNameField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JLabel nameLabel = new JLabel("Account Name:*");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nameLabel.setBounds(20, py, 120, 25);
        paymentPanel.add(nameLabel);
        accountNameField.setBounds(150, py, 250, 30);
        paymentPanel.add(accountNameField);
        py += 45;
        
        uploadQrButton = new JButton("Upload QR Code");
        uploadQrButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        uploadQrButton.setBackground(themeColor);
        uploadQrButton.setForeground(Color.WHITE);
        uploadQrButton.setBounds(20, py, 150, 35);
        uploadQrButton.setBorder(null);
        uploadQrButton.setFocusPainted(false);
        uploadQrButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        uploadQrButton.addActionListener(e -> uploadQRCode());
        paymentPanel.add(uploadQrButton);
        
        qrFileNameLabel = new JLabel();
        qrFileNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        qrFileNameLabel.setBounds(180, py, 300, 35);
        paymentPanel.add(qrFileNameLabel);
        py += 50;
        
        if (!paymentAccountNumber.isEmpty()) {
            accountNumberField.setText(paymentAccountNumber);
            accountNameField.setText(paymentAccountName);
            if (!paymentMethod.isEmpty()) {
                paymentMethodCombo.setSelectedItem(paymentMethod);
            }
            if (!paymentQrCode.isEmpty()) {
                qrFileNameLabel.setText("QR Code uploaded");
            }
        }
        
        return paymentPanel;
    }
    
    private void uploadQRCode() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));

        if (fileChooser.showOpenDialog(parentFrame) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String savedPath = saveQRCode(selectedFile.getAbsolutePath());
            uploadedQrPath = savedPath;
            qrFileNameLabel.setText(selectedFile.getName());
            JOptionPane.showMessageDialog(parentFrame,
                "QR Code uploaded successfully!",
                "Upload Complete",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private String saveQRCode(String sourcePath) {
        try {
            File directory = new File(QR_CODE_PATH);
            if (!directory.exists()) directory.mkdirs();
            
            File sourceFile = new File(sourcePath);
            String fileName = "qr_" + tradeId + "_" + traderId + "_" + System.currentTimeMillis() 
                + sourceFile.getName().substring(sourceFile.getName().lastIndexOf("."));
            String destPath = QR_CODE_PATH + fileName;
            
            Files.copy(Paths.get(sourcePath), Paths.get(destPath), StandardCopyOption.REPLACE_EXISTING);
            return "BarterZone.resources.images.qrcodes." + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    public boolean submitPayment() {
        if (adminComboBox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(parentFrame, "Please select an admin.", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (paymentMethodCombo.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(parentFrame, "Please select a payment method.", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        String number = accountNumberField.getText().trim();
        if (number.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "Please enter account number.", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        String accName = accountNameField.getText().trim();
        if (accName.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "Please enter account name.", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        String selectedAdmin = adminComboBox.getSelectedItem().toString();
        String getAdminIdSql = "SELECT user_id FROM tbl_users WHERE user_fullname = ? AND user_type = 'admin'";
        List<Map<String, Object>> admins = db.fetchRecords(getAdminIdSql, selectedAdmin);
        int adminId = -1;
        if (!admins.isEmpty()) {
            adminId = Integer.parseInt(admins.get(0).get("user_id").toString());
        }
        
        int confirm = JOptionPane.showConfirmDialog(parentFrame,
            "Submit payment details?\n\n"
            + "Admin: " + selectedAdmin + "\n"
            + "Method: " + paymentMethodCombo.getSelectedItem() + "\n"
            + "Number: " + number + "\n"
            + "Name: " + accName + "\n"
            + "Amount: ₱" + String.format("%.2f", totalAmount) + "\n\n"
            + "Admin will verify your payment.",
            "Confirm Payment",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (myPaymentId == -1) {
                String sql = "INSERT INTO tbl_payment_details (trade_id, trader_id, admin_id, payment_method, "
                        + "account_number, account_name, qr_code_path, amount_paid, admin_fee, total_amount, payment_submitted) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1)";
                db.addRecord(sql, tradeId, traderId, adminId, paymentMethodCombo.getSelectedItem().toString(),
                    number, accName, uploadedQrPath, totalAmount, adminFee, totalAmount);
                
                String getIdSql = "SELECT last_insert_rowid() as id";
                List<Map<String, Object>> result = db.fetchRecords(getIdSql);
                if (!result.isEmpty()) {
                    myPaymentId = Integer.parseInt(result.get(0).get("id").toString());
                }
            } else {
                String sql = "UPDATE tbl_payment_details SET admin_id = ?, payment_method = ?, "
                        + "account_number = ?, account_name = ?, qr_code_path = ?, payment_submitted = 1, updated_date = datetime('now') "
                        + "WHERE payment_id = ?";
                db.updateRecord(sql, adminId, paymentMethodCombo.getSelectedItem().toString(),
                    number, accName, uploadedQrPath, myPaymentId);
            }
            
            String updateDetailSql = "UPDATE tbl_trade_details SET payment_id = ? WHERE trade_id = ? AND trader_id = ?";
            db.updateRecord(updateDetailSql, myPaymentId, tradeId, traderId);
            
            JOptionPane.showMessageDialog(parentFrame,
                "Payment submitted successfully!\n\n"
                + "Admin will verify your payment.\n"
                + "You will be notified once verified.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            return true;
        }
        
        return false;
    }
    
    public boolean isPaymentVerified() {
        loadPaymentDetails();
        return paymentVerified;
    }
    
    public boolean isPaymentSubmitted() {
        loadPaymentDetails();
        return paymentSubmitted;
    }
    
    public boolean hasSelectedAdmin() {
        return selectedAdminId != -1;
    }
}