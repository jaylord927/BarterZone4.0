package BarterZone.Dashboard.trader;

import database.config.config;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
    private String traderName;
    private int otherTraderId;
    private config db;
    private JFrame parentFrame;
    
    // UI Components
    private JDialog paymentDialog;
    private JTextField paymentNumberField;
    private JTextField accountNameField;
    private JButton uploadProofButton;
    private JLabel proofFileNameLabel;
    private JButton submitButton;
    private JButton cancelButton;
    
    private String selectedImagePath = "";
    private String selectedImageFileName = "";
    
    private static final String PROOF_IMAGE_PATH = "src/BarterZone/resources/images/payment_proofs/";
    
    private Color themeColor = new Color(12, 192, 223);
    private Color successColor = new Color(46, 125, 50);
    private Color errorColor = new Color(204, 0, 0);
    private Color accentColor = new Color(0, 102, 102);
    
    public step3_submit(int tradeId, int traderId, String traderName, int otherTraderId, JFrame parentFrame) {
        this.tradeId = tradeId;
        this.traderId = traderId;
        this.traderName = traderName;
        this.otherTraderId = otherTraderId;
        this.db = new config();
        this.parentFrame = parentFrame;
        
        createDirectories();
    }
    
    private void createDirectories() {
        File directory = new File(PROOF_IMAGE_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
    
    public void showDialog() {
        paymentDialog = new JDialog(parentFrame, "Add Payment Proof", true);
        paymentDialog.setSize(500, 450);
        paymentDialog.setLayout(null);
        paymentDialog.setLocationRelativeTo(parentFrame);
        paymentDialog.getContentPane().setBackground(Color.WHITE);
        paymentDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        paymentDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                paymentDialog.dispose();
            }
        });
        
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(themeColor);
        titlePanel.setBounds(0, 0, 500, 45);
        titlePanel.setLayout(null);
        
        JLabel titleLabel = new JLabel("ADD PAYMENT PROOF");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(20, 8, 300, 30);
        titlePanel.add(titleLabel);
        paymentDialog.add(titlePanel);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBounds(10, 55, 480, 340);
        paymentDialog.add(contentPanel);
        
        int y = 20;
        int labelWidth = 120;
        int fieldWidth = 300;
        int fieldX = 150;
        
        // Payment Number Field
        JLabel numberLabel = new JLabel("Payment Number:*");
        numberLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        numberLabel.setBounds(20, y, labelWidth, 30);
        contentPanel.add(numberLabel);
        
        paymentNumberField = new JTextField();
        paymentNumberField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        paymentNumberField.setBounds(fieldX, y, fieldWidth, 35);
        paymentNumberField.setBorder(new LineBorder(new Color(200, 200, 200)));
        contentPanel.add(paymentNumberField);
        y += 55;
        
        // Account Name Field
        JLabel nameLabel = new JLabel("Account Name:*");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLabel.setBounds(20, y, labelWidth, 30);
        contentPanel.add(nameLabel);
        
        accountNameField = new JTextField();
        accountNameField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        accountNameField.setBounds(fieldX, y, fieldWidth, 35);
        accountNameField.setBorder(new LineBorder(new Color(200, 200, 200)));
        contentPanel.add(accountNameField);
        y += 55;
        
        // Upload Screenshot
        JLabel proofLabel = new JLabel("Payment Screenshot:*");
        proofLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        proofLabel.setBounds(20, y, labelWidth, 30);
        contentPanel.add(proofLabel);
        
        uploadProofButton = new JButton("Choose File");
        uploadProofButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        uploadProofButton.setBackground(themeColor);
        uploadProofButton.setForeground(Color.WHITE);
        uploadProofButton.setBounds(fieldX, y, 120, 35);
        uploadProofButton.setBorder(null);
        uploadProofButton.setFocusPainted(false);
        uploadProofButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        uploadProofButton.addActionListener(e -> uploadProofImage());
        contentPanel.add(uploadProofButton);
        
        proofFileNameLabel = new JLabel("No file chosen");
        proofFileNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        proofFileNameLabel.setForeground(new Color(102, 102, 102));
        proofFileNameLabel.setBounds(fieldX + 130, y, 200, 35);
        contentPanel.add(proofFileNameLabel);
        y += 60;
        
        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(null);
        infoPanel.setBackground(new Color(255, 245, 220));
        infoPanel.setBorder(new LineBorder(new Color(255, 153, 0), 1));
        infoPanel.setBounds(20, y, 440, 80);
        
        JLabel infoLabel = new JLabel(
            "<html><b>Note:</b> Please upload a clear screenshot of your payment.<br>"
            + "Supported formats: JPG, PNG, GIF. Max size: 5MB.</html>");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        infoLabel.setBounds(10, 10, 420, 60);
        infoPanel.add(infoLabel);
        
        contentPanel.add(infoPanel);
        y += 95;
        
        // Buttons
        submitButton = new JButton("SUBMIT PAYMENT");
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitButton.setBackground(successColor);
        submitButton.setForeground(Color.WHITE);
        submitButton.setBounds(100, y, 150, 40);
        submitButton.setBorder(null);
        submitButton.setFocusPainted(false);
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.addActionListener(e -> submitPaymentProof());
        contentPanel.add(submitButton);
        
        cancelButton = new JButton("CANCEL");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setBackground(errorColor);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBounds(270, y, 100, 40);
        cancelButton.setBorder(null);
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> paymentDialog.dispose());
        contentPanel.add(cancelButton);
        
        paymentDialog.setVisible(true);
    }
    
    private void uploadProofImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif", "bmp"));
        
        if (fileChooser.showOpenDialog(paymentDialog) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedImagePath = selectedFile.getAbsolutePath();
            selectedImageFileName = selectedFile.getName();
            proofFileNameLabel.setText(selectedImageFileName);
            
            // Validate file size (5MB max)
            long fileSize = selectedFile.length();
            if (fileSize > 5 * 1024 * 1024) {
                JOptionPane.showMessageDialog(paymentDialog, 
                    "File size exceeds 5MB. Please choose a smaller file.", 
                    "File Too Large", 
                    JOptionPane.WARNING_MESSAGE);
                selectedImagePath = "";
                selectedImageFileName = "";
                proofFileNameLabel.setText("No file chosen");
            }
        }
    }
    
    private String saveProofImage() {
        if (selectedImagePath == null || selectedImagePath.isEmpty()) {
            return "";
        }
        
        try {
            File directory = new File(PROOF_IMAGE_PATH);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            // Get the original filename
            String originalFileName = selectedImageFileName;
            
            // Check if file exists and generate unique name if needed
            String fileNameWithoutExt = originalFileName;
            String extension = "";
            int dotIndex = originalFileName.lastIndexOf(".");
            if (dotIndex > 0) {
                fileNameWithoutExt = originalFileName.substring(0, dotIndex);
                extension = originalFileName.substring(dotIndex);
            }
            
            String destinationPath = PROOF_IMAGE_PATH + originalFileName;
            File destFile = new File(destinationPath);
            int counter = 1;
            
            // Handle duplicate filenames
            while (destFile.exists()) {
                String newFileName = fileNameWithoutExt + "_" + counter + extension;
                destinationPath = PROOF_IMAGE_PATH + newFileName;
                destFile = new File(destinationPath);
                counter++;
            }
            
            // Copy file to destination
            Files.copy(Paths.get(selectedImagePath), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
            
            // Return correct path format: BarterZone/resources/images/payment_proofs/filename
            return "BarterZone/resources/images/payment_proofs/" + destFile.getName();
            
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(paymentDialog, "Error saving image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return "";
        }
    }
    
    private void submitPaymentProof() {
        String paymentNumber = paymentNumberField.getText().trim();
        String accountName = accountNameField.getText().trim();
        
        if (paymentNumber.isEmpty()) {
            JOptionPane.showMessageDialog(paymentDialog, "Please enter payment number.", "Required Field", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (selectedImagePath.isEmpty()) {
            JOptionPane.showMessageDialog(paymentDialog, "Please upload payment screenshot.", "Required Field", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Save the image
        String savedImagePath = saveProofImage();
        if (savedImagePath.isEmpty()) {
            JOptionPane.showMessageDialog(paymentDialog, "Failed to save image. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if payment record already exists
        String checkSql = "SELECT COUNT(*) as count FROM tbl_payment_details WHERE trade_id = ? AND trader_id = ?";
        double count = db.getSingleValue(checkSql, tradeId, traderId);
        
        boolean success = false;
        
        if (count == 0) {
            // Insert new record
            String sql = "INSERT INTO tbl_payment_details (trade_id, trader_id, my_number, acc_name, payment_proof, payment_submitted, payment_submitted_date, created_date) "
                    + "VALUES (?, ?, ?, ?, ?, 1, datetime('now'), datetime('now'))";
            db.addRecord(sql, tradeId, traderId, paymentNumber, accountName, savedImagePath);
            success = true;
        } else {
            // Update existing record
            String sql = "UPDATE tbl_payment_details SET my_number = ?, acc_name = ?, payment_proof = ?, payment_submitted = 1, payment_submitted_date = datetime('now'), updated_date = datetime('now') "
                    + "WHERE trade_id = ? AND trader_id = ?";
            db.updateRecord(sql, paymentNumber, accountName, savedImagePath, tradeId, traderId);
            success = true;
        }
        
        if (success) {
            JOptionPane.showMessageDialog(paymentDialog, 
                "Payment proof submitted successfully!\n\n"
                + "Payment Number: " + paymentNumber + "\n"
                + "Account Name: " + accountName + "\n\n"
                + "Admin will verify your payment.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            paymentDialog.dispose();
        } else {
            JOptionPane.showMessageDialog(paymentDialog, "Failed to submit payment. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}