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

public class trades_payment {
    
    private config db;
    private JFrame parentFrame;
    private int adminId;
    private String adminName;
    
    // Colors
    private Color successColor = new Color(46, 125, 50);
    private Color warningColor = new Color(255, 153, 0);
    private Color errorColor = new Color(204, 0, 0);
    private Color accentColor = new Color(255, 215, 0);
    private Color sideBarColor = new Color(8, 78, 128);
    
    private static final String PROOF_PATH = "src/BarterZone/resources/images/payment_proofs/";
    
    public trades_payment(config db, JFrame parentFrame, int adminId, String adminName) {
        this.db = db;
        this.parentFrame = parentFrame;
        this.adminId = adminId;
        this.adminName = adminName;
        createDirectories();
    }
    
    private void createDirectories() {
        new File(PROOF_PATH).mkdirs();
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
    
    public void loadPaymentMethodDetails(int methodId, JLabel paymentMethodDetailLabel, JLabel accountNumberLabel, 
                                          JLabel accountNameLabel, JLabel qrCodeLabel) {
        String sql = "SELECT method_name, account_number, account_name, qr_code_path FROM tbl_payment_methods WHERE method_id = ? AND is_active = 1";
        List<Map<String, Object>> result = db.fetchRecords(sql, methodId);
        
        if (!result.isEmpty()) {
            Map<String, Object> method = result.get(0);
            paymentMethodDetailLabel.setText("Method: " + method.get("method_name"));
            accountNumberLabel.setText("Account Number: " + method.get("account_number"));
            if (method.get("account_name") != null && !method.get("account_name").toString().isEmpty()) {
                accountNameLabel.setText("Account Name: " + method.get("account_name"));
            } else {
                accountNameLabel.setText("");
            }
            
            if (method.get("qr_code_path") != null && !method.get("qr_code_path").toString().isEmpty()) {
                String qrPath = method.get("qr_code_path").toString();
                String fullPath = "src/" + qrPath;
                File qrFile = new File(fullPath);
                if (qrFile.exists()) {
                    try {
                        ImageIcon qrIcon = new ImageIcon(fullPath);
                        java.awt.Image scaledImage = qrIcon.getImage().getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
                        qrCodeLabel.setIcon(new ImageIcon(scaledImage));
                        qrCodeLabel.setText("");
                    } catch (Exception e) {
                        qrCodeLabel.setIcon(null);
                        qrCodeLabel.setText("QR Code Error");
                    }
                } else {
                    qrCodeLabel.setIcon(null);
                    qrCodeLabel.setText("QR Code Not Found");
                }
            } else {
                qrCodeLabel.setIcon(null);
                qrCodeLabel.setText("No QR Code");
            }
        }
    }
    
    public void loadTradePaymentInfo(int tradeId, int traderId, JLabel serviceFeeLabel, JLabel totalAmountLabel,
                                      JComboBox<String> paymentMethodCombo) {
        String feeSql = "SELECT service_fee, total_amount, method_id FROM tbl_payment_details WHERE trade_id = ? AND trader_id = ? LIMIT 1";
        List<Map<String, Object>> feeResult = db.fetchRecords(feeSql, tradeId, traderId);
        
        if (!feeResult.isEmpty()) {
            Map<String, Object> feeData = feeResult.get(0);
            double serviceFee = feeData.get("service_fee") != null ? Double.parseDouble(feeData.get("service_fee").toString()) : 15.00;
            double totalAmount = feeData.get("total_amount") != null ? Double.parseDouble(feeData.get("total_amount").toString()) : 215.00;
            
            serviceFeeLabel.setText("Service Fee: ₱" + String.format("%.2f", serviceFee));
            totalAmountLabel.setText("Total Amount: ₱" + String.format("%.2f", totalAmount));
            
            if (feeData.get("method_id") != null) {
                int methodId = Integer.parseInt(feeData.get("method_id").toString());
                for (int i = 0; i < paymentMethodCombo.getItemCount(); i++) {
                    String item = paymentMethodCombo.getItemAt(i);
                    if (item.startsWith(String.valueOf(methodId))) {
                        paymentMethodCombo.setSelectedIndex(i);
                        break;
                    }
                }
            }
        } else {
            serviceFeeLabel.setText("Service Fee: ₱15.00");
            totalAmountLabel.setText("Total Amount: ₱215.00");
        }
    }
    
    public void saveTradePaymentSettings(int selectedTradeId, int selectedOfferTraderId, int selectedTargetTraderId,
                                          JComboBox<String> paymentMethodCombo, JTextField serviceFeeField, 
                                          JTextField totalAmountField, JFrame parent) {
        if (selectedTradeId == -1) {
            JOptionPane.showMessageDialog(parent, "Please select a trade first.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (paymentMethodCombo.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(parent, "Please select a payment method.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            double serviceFee = Double.parseDouble(serviceFeeField.getText().trim());
            double totalAmount = Double.parseDouble(totalAmountField.getText().trim());
            String selectedMethod = paymentMethodCombo.getSelectedItem().toString();
            int methodId = Integer.parseInt(selectedMethod.substring(0, selectedMethod.indexOf(" -")));
            
            for (int traderId : new int[]{selectedOfferTraderId, selectedTargetTraderId}) {
                String checkSql = "SELECT COUNT(*) as count FROM tbl_payment_details WHERE trade_id = ? AND trader_id = ?";
                double count = db.getSingleValue(checkSql, selectedTradeId, traderId);
                
                if (count == 0) {
                    String insertSql = "INSERT INTO tbl_payment_details (trade_id, trader_id, method_id, service_fee, total_amount, created_date) "
                            + "VALUES (?, ?, ?, ?, ?, datetime('now'))";
                    db.addRecord(insertSql, selectedTradeId, traderId, methodId, serviceFee, totalAmount);
                } else {
                    String updateSql = "UPDATE tbl_payment_details SET method_id = ?, service_fee = ?, total_amount = ?, updated_date = datetime('now') "
                            + "WHERE trade_id = ? AND trader_id = ?";
                    db.updateRecord(updateSql, methodId, serviceFee, totalAmount, selectedTradeId, traderId);
                }
            }
            
            JOptionPane.showMessageDialog(parent, "Trade payment settings saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(parent, "Please enter valid numbers for fee and amount.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public void loadPaymentVerificationData(int tradeId, int traderId, JLabel paymentNumberLabel, 
                                             JLabel accountNameLabel, JLabel statusLabel, JButton viewButton, 
                                             JButton markButton, int[] paymentIdHolder) {
        String sql = "SELECT payment_id, my_number, acc_name, payment_proof, payment_submitted, payment_verified FROM tbl_payment_details WHERE trade_id = ? AND trader_id = ?";
        List<Map<String, Object>> result = db.fetchRecords(sql, tradeId, traderId);
        
        if (!result.isEmpty()) {
            Map<String, Object> payment = result.get(0);
            
            // Safe parsing with null checks
            int paymentId = -1;
            if (payment.get("payment_id") != null) {
                try {
                    paymentId = Integer.parseInt(payment.get("payment_id").toString());
                } catch (NumberFormatException e) {
                    paymentId = -1;
                }
            }
            
            String myNumber = payment.get("my_number") != null ? payment.get("my_number").toString() : "";
            String accName = payment.get("acc_name") != null ? payment.get("acc_name").toString() : "";
            
            int paymentSubmitted = 0;
            if (payment.get("payment_submitted") != null) {
                try {
                    paymentSubmitted = Integer.parseInt(payment.get("payment_submitted").toString());
                } catch (NumberFormatException e) {
                    paymentSubmitted = 0;
                }
            }
            
            int paymentVerified = 0;
            if (payment.get("payment_verified") != null) {
                try {
                    paymentVerified = Integer.parseInt(payment.get("payment_verified").toString());
                } catch (NumberFormatException e) {
                    paymentVerified = 0;
                }
            }
            
            String proofPath = payment.get("payment_proof") != null ? payment.get("payment_proof").toString() : "";
            
            if (paymentIdHolder != null && paymentIdHolder.length > 0) {
                paymentIdHolder[0] = paymentId;
            }
            
            paymentNumberLabel.setText(myNumber.isEmpty() ? "Not submitted" : myNumber);
            accountNameLabel.setText(accName.isEmpty() ? "Not submitted" : accName);
            
            if (paymentVerified == 1) {
                statusLabel.setText("✓ VERIFIED");
                statusLabel.setForeground(successColor);
                viewButton.setEnabled(!proofPath.isEmpty());
                markButton.setEnabled(false);
                markButton.setText("Verified");
                markButton.setBackground(new Color(150, 150, 150));
            } else if (paymentSubmitted == 1) {
                statusLabel.setText("⏳ PENDING VERIFICATION");
                statusLabel.setForeground(warningColor);
                viewButton.setEnabled(!proofPath.isEmpty());
                markButton.setEnabled(true);
                markButton.setText("Mark as Paid");
                markButton.setBackground(successColor);
            } else {
                statusLabel.setText("❌ NOT SUBMITTED");
                statusLabel.setForeground(errorColor);
                viewButton.setEnabled(false);
                markButton.setEnabled(false);
                markButton.setText("Mark as Paid");
                markButton.setBackground(successColor);
            }
        } else {
            paymentNumberLabel.setText("Not submitted");
            accountNameLabel.setText("Not submitted");
            statusLabel.setText("❌ NO RECORD");
            statusLabel.setForeground(errorColor);
            viewButton.setEnabled(false);
            markButton.setEnabled(false);
            if (paymentIdHolder != null && paymentIdHolder.length > 0) {
                paymentIdHolder[0] = -1;
            }
        }
    }
    
    public void viewPaymentProof(int paymentId) {
        if (paymentId == -1) {
            JOptionPane.showMessageDialog(parentFrame, "No payment record found.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String sql = "SELECT payment_proof FROM tbl_payment_details WHERE payment_id = ?";
        List<Map<String, Object>> result = db.fetchRecords(sql, paymentId);
        
        if (!result.isEmpty() && result.get(0).get("payment_proof") != null) {
            String proofPath = result.get(0).get("payment_proof").toString();
            String fullPath = "src/" + proofPath;
            File imgFile = new File(fullPath);
            
            if (imgFile.exists()) {
                try {
                    ImageIcon icon = new ImageIcon(fullPath);
                    java.awt.Image img = icon.getImage().getScaledInstance(500, 500, java.awt.Image.SCALE_SMOOTH);
                    JOptionPane.showMessageDialog(parentFrame, new JLabel(new ImageIcon(img)), "Payment Proof", JOptionPane.PLAIN_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(parentFrame, "Error loading image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(parentFrame, "Proof image not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(parentFrame, "No payment proof uploaded yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void markPaymentAsPaid(int paymentId, String traderName, int tradeId, JFrame parent, Runnable onComplete) {
        if (paymentId == -1) {
            JOptionPane.showMessageDialog(parent, "No payment record found.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new java.awt.Dimension(400, 120));
        
        JLabel notesLabel = new JLabel("Admin Notes (Optional):");
        notesLabel.setBounds(20, 20, 150, 25);
        panel.add(notesLabel);
        
        JTextArea notesArea = new JTextArea();
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(notesArea);
        scrollPane.setBounds(20, 50, 360, 50);
        panel.add(scrollPane);
        
        int confirm = JOptionPane.showConfirmDialog(parent, panel,
            "Verify Payment for " + traderName,
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.OK_OPTION) {
            String adminNotes = notesArea.getText().trim();
            
            String sql = "UPDATE tbl_payment_details SET payment_verified = 1, payment_verified_date = datetime('now'), admin_notes = ? WHERE payment_id = ?";
            db.updateRecord(sql, adminNotes.isEmpty() ? null : adminNotes, paymentId);
            
            String checkSql = "SELECT COUNT(*) as verified_count FROM tbl_payment_details WHERE trade_id = ? AND payment_verified = 1";
            double verifiedCount = db.getSingleValue(checkSql, tradeId);
            
            if (verifiedCount == 2) {
                String updateTradeSql = "UPDATE tbl_trade SET payment_verified = 1, trade_status = 'payment_verified' WHERE trade_id = ?";
                db.updateRecord(updateTradeSql, tradeId);
                
                JOptionPane.showMessageDialog(parent,
                    "Both traders' payments have been verified!\n\n"
                    + "The trade can now proceed to Step 4.",
                    "Payments Verified",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parent,
                    traderName + "'s payment has been verified!\n\n"
                    + "Waiting for the other trader's payment verification.",
                    "Payment Verified",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
            if (onComplete != null) {
                onComplete.run();
            }
        }
    }
    
    public void checkOverallVerificationStatus(int tradeId, JLabel overallStatusLabel) {
        String sql = "SELECT COUNT(*) as verified_count FROM tbl_payment_details WHERE trade_id = ? AND payment_verified = 1";
        double verifiedCount = db.getSingleValue(sql, tradeId);
        
        if (verifiedCount == 2) {
            overallStatusLabel.setText("✓ BOTH TRADERS HAVE BEEN VERIFIED! Ready to proceed to Step 4.");
            overallStatusLabel.setForeground(successColor);
        } else if (verifiedCount == 1) {
            overallStatusLabel.setText("⏳ One trader verified. Waiting for the other trader's payment verification.");
            overallStatusLabel.setForeground(warningColor);
        } else {
            overallStatusLabel.setText("❌ No payments verified yet. Please verify each trader's payment.");
            overallStatusLabel.setForeground(errorColor);
        }
    }
}