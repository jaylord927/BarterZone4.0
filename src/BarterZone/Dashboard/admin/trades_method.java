package BarterZone.Dashboard.admin;

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
import javax.swing.table.DefaultTableModel;

public class trades_method {
    
    private config db;
    private JFrame parentFrame;
    private int adminId;
    private String adminName;
    
    private Color successColor = new Color(46, 125, 50);
    private Color warningColor = new Color(255, 153, 0);
    private Color errorColor = new Color(204, 0, 0);
    private Color sideBarColor = new Color(8, 78, 128);
    
    private static final String QR_CODE_PATH = "src/BarterZone/resources/images/admin_qrcodes/";
    
    public trades_method(config db, JFrame parentFrame, int adminId, String adminName) {
        this.db = db;
        this.parentFrame = parentFrame;
        this.adminId = adminId;
        this.adminName = adminName;
        createDirectories();
    }
    
    private void createDirectories() {
        new File(QR_CODE_PATH).mkdirs();
    }
    
    public void loadPaymentMethods(DefaultTableModel methodsTableModel) {
        methodsTableModel.setRowCount(0);
        String sql = "SELECT method_id, method_name, account_number, account_name, is_active FROM tbl_payment_methods WHERE admin_id = ? ORDER BY method_id DESC";
        List<Map<String, Object>> methods = db.fetchRecords(sql, adminId);
        
        for (Map<String, Object> method : methods) {
            int isActive = Integer.parseInt(method.get("is_active").toString());
            String status = isActive == 1 ? "Active" : "Inactive";
            methodsTableModel.addRow(new Object[]{
                method.get("method_id"),
                method.get("method_name"),
                method.get("account_number"),
                method.get("account_name") != null ? method.get("account_name") : "",
                status
            });
        }
    }
    
    public String uploadQRCode(JFrame parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));
        
        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return saveQRCodeImage(selectedFile.getAbsolutePath(), selectedFile.getName());
        }
        return "";
    }
    
    private String saveQRCodeImage(String sourcePath, String originalFileName) {
        try {
            File directory = new File(QR_CODE_PATH);
            if (!directory.exists()) directory.mkdirs();
            
            String extension = "";
            String nameWithoutExt = originalFileName;
            int dotIndex = originalFileName.lastIndexOf(".");
            if (dotIndex > 0) {
                nameWithoutExt = originalFileName.substring(0, dotIndex);
                extension = originalFileName.substring(dotIndex);
            }
            
            String destinationPath = QR_CODE_PATH + originalFileName;
            File destFile = new File(destinationPath);
            int counter = 1;
            
            while (destFile.exists()) {
                String newFileName = nameWithoutExt + "_" + counter + extension;
                destinationPath = QR_CODE_PATH + newFileName;
                destFile = new File(destinationPath);
                counter++;
            }
            
            Files.copy(Paths.get(sourcePath), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
            return "BarterZone.resources.images.admin_qrcodes." + destFile.getName();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    public void addPaymentMethod(String methodName, String accountNumber, String accountName, String qrPath, 
                                  JTextField methodNameField, JTextField accountNumberField, JTextField accountNameField,
                                  JLabel qrFileNameLabel, Runnable onComplete) {
        if (methodName.isEmpty() || accountNumber.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "Method Name and Account Number are required!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String sql = "INSERT INTO tbl_payment_methods (admin_id, method_name, account_number, account_name, qr_code_path, is_active, created_date) "
                + "VALUES (?, ?, ?, ?, ?, 1, datetime('now'))";
        db.addRecord(sql, adminId, methodName, accountNumber, accountName, qrPath);
        
        JOptionPane.showMessageDialog(parentFrame, "Payment method added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        
        if (onComplete != null) {
            onComplete.run();
        }
    }
    
    public void updatePaymentMethod(int methodId, String methodName, String accountNumber, String accountName, String qrPath,
                                     Runnable onComplete) {
        if (methodId == -1) return;
        
        if (methodName.isEmpty() || accountNumber.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "Method Name and Account Number are required!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String sql = "UPDATE tbl_payment_methods SET method_name = ?, account_number = ?, account_name = ?, qr_code_path = ?, updated_date = datetime('now') "
                + "WHERE method_id = ?";
        db.updateRecord(sql, methodName, accountNumber, accountName, qrPath, methodId);
        
        JOptionPane.showMessageDialog(parentFrame, "Payment method updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        
        if (onComplete != null) {
            onComplete.run();
        }
    }
    
    public void activatePaymentMethod(int methodId, Runnable onComplete) {
        if (methodId == -1) return;
        
        String sql = "UPDATE tbl_payment_methods SET is_active = 1, updated_date = datetime('now') WHERE method_id = ?";
        db.updateRecord(sql, methodId);
        
        JOptionPane.showMessageDialog(parentFrame, "Payment method activated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        
        if (onComplete != null) {
            onComplete.run();
        }
    }
    
    public void deactivatePaymentMethod(int methodId, Runnable onComplete) {
        if (methodId == -1) return;
        
        int confirm = JOptionPane.showConfirmDialog(parentFrame, 
            "Deactivate this payment method?\n\nIt will not be visible to traders.", 
            "Confirm Deactivate", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_payment_methods SET is_active = 0, updated_date = datetime('now') WHERE method_id = ?";
            db.updateRecord(sql, methodId);
            
            JOptionPane.showMessageDialog(parentFrame, "Payment method deactivated!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            if (onComplete != null) {
                onComplete.run();
            }
        }
    }
    
    public void loadPaymentMethodsForCombo(JComboBox<String> paymentMethodCombo) {
        paymentMethodCombo.removeAllItems();
        String sql = "SELECT method_id, method_name FROM tbl_payment_methods WHERE admin_id = ? AND is_active = 1";
        List<Map<String, Object>> methods = db.fetchRecords(sql, adminId);
        
        paymentMethodCombo.addItem("-- Select Payment Method --");
        for (Map<String, Object> method : methods) {
            paymentMethodCombo.addItem(method.get("method_id") + " - " + method.get("method_name"));
        }
    }
}