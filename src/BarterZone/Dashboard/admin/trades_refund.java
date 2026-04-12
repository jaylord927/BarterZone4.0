package BarterZone.Dashboard.admin;

import database.config.config;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class trades_refund {

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
    private Color infoColor = new Color(33, 150, 243);
    private Color bgColor = new Color(250, 250, 250);

    private static final String REFUND_PROOF_PATH = "src/BarterZone/resources/images/refund_proofs/";

    public trades_refund(config db, JFrame parentFrame, int adminId, String adminName) {
        this.db = db;
        this.parentFrame = parentFrame;
        this.adminId = adminId;
        this.adminName = adminName;
        createDirectories();
    }

    private void createDirectories() {
        new File(REFUND_PROOF_PATH).mkdirs();
    }

    public void loadTradesForRefundDropdown(JComboBox<String> refundTradeComboBox) {
        refundTradeComboBox.removeAllItems();
        String sql = "SELECT t.trade_id, 'Trade #' || t.trade_id || ' - ' || u1.user_fullname || ' ↔ ' || u2.user_fullname as display "
                + "FROM tbl_trade t "
                + "LEFT JOIN tbl_users u1 ON t.offer_trader_id = u1.user_id "
                + "LEFT JOIN tbl_users u2 ON t.target_trader_id = u2.user_id "
                + "WHERE (t.my_item_received = 1 AND t.other_item_received = 1) "
                + "AND t.trade_status NOT IN ('completed', 'refund_pending') "
                + "ORDER BY t.trade_id DESC";
        List<Map<String, Object>> trades = db.fetchRecords(sql);

        for (Map<String, Object> trade : trades) {
            refundTradeComboBox.addItem(trade.get("display").toString());
        }
    }

    public void loadTraderRefundData(int tradeId, int traderId, String traderName,
            JLabel accountNumberLabel, JLabel accountNameLabel,
            JLabel statusLabel, JButton uploadButton, JButton markButton,
            JTextArea messageArea, JLabel proofStatusLabel,
            int[] refundIdHolder) {
        String sql = "SELECT refund_id, account_number, account_name, qr_code_path, refund_proof, refund_message, is_refunded FROM tbl_refund WHERE trade_id = ? AND user_id = ?";
        List<Map<String, Object>> result = db.fetchRecords(sql, tradeId, traderId);

        if (!result.isEmpty()) {
            Map<String, Object> refund = result.get(0);
            int refundId = Integer.parseInt(refund.get("refund_id").toString());
            String accountNumber = refund.get("account_number").toString();
            String accountName = refund.get("account_name").toString();
            String refundProof = refund.get("refund_proof") != null ? refund.get("refund_proof").toString() : "";
            String refundMessage = refund.get("refund_message") != null ? refund.get("refund_message").toString() : "";
            int isRefunded = Integer.parseInt(refund.get("is_refunded").toString());

            if (refundIdHolder != null && refundIdHolder.length > 0) {
                refundIdHolder[0] = refundId;
            }

            accountNumberLabel.setText(accountNumber);
            accountNameLabel.setText(accountName);

            if (messageArea != null) {
                messageArea.setText(refundMessage);
                messageArea.setCaretPosition(0);
            }

            if (proofStatusLabel != null) {
                if (!refundProof.isEmpty()) {
                    proofStatusLabel.setText("Proof uploaded");
                    proofStatusLabel.setForeground(successColor);
                } else {
                    proofStatusLabel.setText("No proof uploaded");
                    proofStatusLabel.setForeground(warningColor);
                }
            }

            if (isRefunded == 1) {
                statusLabel.setText("REFUND COMPLETED");
                statusLabel.setForeground(successColor);
                if (uploadButton != null) {
                    uploadButton.setEnabled(false);
                }
                if (markButton != null) {
                    markButton.setEnabled(false);
                }
            } else if (!refundProof.isEmpty()) {
                statusLabel.setText("REFUND PROOF UPLOADED - Ready to mark");
                statusLabel.setForeground(warningColor);
                if (uploadButton != null) {
                    uploadButton.setEnabled(false);
                }
                if (markButton != null) {
                    markButton.setEnabled(true);
                }
            } else {
                statusLabel.setText("PENDING - Add refund proof");
                statusLabel.setForeground(errorColor);
                if (uploadButton != null) {
                    uploadButton.setEnabled(true);
                }
                if (markButton != null) {
                    markButton.setEnabled(false);
                }
            }
        } else {
            accountNumberLabel.setText("Not submitted");
            accountNameLabel.setText("Not submitted");
            statusLabel.setText("REFUND DETAILS NOT SUBMITTED");
            statusLabel.setForeground(errorColor);
            if (uploadButton != null) {
                uploadButton.setEnabled(false);
            }
            if (markButton != null) {
                markButton.setEnabled(false);
            }
            if (messageArea != null) {
                messageArea.setText("");
            }
            if (proofStatusLabel != null) {
                proofStatusLabel.setText("No proof");
            }
        }
    }

    public void showAddRefundProofDialog(int refundId, String traderName, int tradeId, Runnable onComplete) {
        if (refundId == -1) {
            JOptionPane.showMessageDialog(parentFrame, "No refund record found for this trader.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog refundDialog = new JDialog(parentFrame, "Add Refund Proof for " + traderName, true);
        refundDialog.setSize(550, 500);
        refundDialog.setLayout(null);
        refundDialog.setLocationRelativeTo(parentFrame);
        refundDialog.getContentPane().setBackground(Color.WHITE);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(sideBarColor);
        titlePanel.setBounds(0, 0, 550, 50);
        titlePanel.setLayout(null);

        JLabel titleLabel = new JLabel("ADD REFUND PROOF");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(20, 10, 300, 30);
        titlePanel.add(titleLabel);
        refundDialog.add(titlePanel);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBounds(10, 60, 530, 390);
        refundDialog.add(contentPanel);

        int y = 20;
        int labelWidth = 130;
        int fieldWidth = 350;
        int fieldX = 150;

        // Refund Message
        JLabel messageLabel = new JLabel("Refund Message:");
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        messageLabel.setBounds(20, y, labelWidth, 30);
        contentPanel.add(messageLabel);

        JTextArea messageArea = new JTextArea();
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane messageScroll = new JScrollPane(messageArea);
        messageScroll.setBounds(fieldX, y, fieldWidth, 80);
        messageScroll.setBorder(new LineBorder(new Color(200, 200, 200)));
        contentPanel.add(messageScroll);
        y += 95;

        // Upload Proof Image
        JLabel proofLabel = new JLabel("Refund Proof Image:");
        proofLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        proofLabel.setBounds(20, y, labelWidth, 30);
        contentPanel.add(proofLabel);

        JButton uploadButton = new JButton("CHOOSE FILE");
        uploadButton.setBounds(fieldX, y, 140, 35);
        uploadButton.setBackground(sideBarColor);
        uploadButton.setForeground(Color.WHITE);
        uploadButton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        uploadButton.setFocusPainted(false);
        uploadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPanel.add(uploadButton);

        JLabel fileNameLabel = new JLabel("No file chosen");
        fileNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        fileNameLabel.setForeground(new Color(102, 102, 102));
        fileNameLabel.setBounds(fieldX + 155, y, 200, 35);
        contentPanel.add(fileNameLabel);
        y += 50;

        // Image Preview
        JLabel previewLabel = new JLabel();
        previewLabel.setBounds(fieldX, y, 150, 150);
        previewLabel.setBorder(new LineBorder(new Color(200, 200, 200)));
        previewLabel.setHorizontalAlignment(JLabel.CENTER);
        previewLabel.setVerticalAlignment(JLabel.CENTER);
        previewLabel.setText("Preview");
        previewLabel.setBackground(Color.WHITE);
        previewLabel.setOpaque(true);
        contentPanel.add(previewLabel);
        y += 165;

        final String[] uploadedImagePath = {""};

        uploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));
            if (fileChooser.showOpenDialog(refundDialog) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String savedPath = saveRefundProofImage(selectedFile.getAbsolutePath(), selectedFile.getName());
                uploadedImagePath[0] = savedPath;
                fileNameLabel.setText(selectedFile.getName());

                try {
                    ImageIcon previewIcon = new ImageIcon(selectedFile.getAbsolutePath());
                    Image scaledImage = previewIcon.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
                    previewLabel.setIcon(new ImageIcon(scaledImage));
                    previewLabel.setText("");
                } catch (Exception ex) {
                    previewLabel.setText("Error");
                }
            }
        });

        JButton saveButton = new JButton("SAVE REFUND PROOF");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.setBackground(successColor);
        saveButton.setForeground(Color.WHITE);
        saveButton.setBounds(150, y, 250, 45);
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveButton.setFocusPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(e -> {
            String message = messageArea.getText().trim();
            String imagePath = uploadedImagePath[0];

            if (imagePath.isEmpty()) {
                JOptionPane.showMessageDialog(refundDialog, "Please select a refund proof image.", "Required", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String sql = "UPDATE tbl_refund SET refund_proof = ?, refund_message = ?, updated_date = datetime('now') WHERE refund_id = ?";
            db.updateRecord(sql, imagePath, message.isEmpty() ? null : message, refundId);

            JOptionPane.showMessageDialog(refundDialog, "Refund proof saved successfully for " + traderName + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
            refundDialog.dispose();

            if (onComplete != null) {
                onComplete.run();
            }
        });
        contentPanel.add(saveButton);

        JButton cancelButton = new JButton("CANCEL");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setBackground(errorColor);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBounds(420, y, 100, 45);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> refundDialog.dispose());
        contentPanel.add(cancelButton);

        refundDialog.setVisible(true);
    }

    public void viewProofImage(String proofPath, String title) {
        if (proofPath == null || proofPath.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "No proof image available.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            String fullPath = "src/" + proofPath;
            File imgFile = new File(fullPath);
            if (imgFile.exists()) {
                ImageIcon icon = new ImageIcon(fullPath);
                Image scaledImage = icon.getImage().getScaledInstance(600, 600, Image.SCALE_SMOOTH);
                JOptionPane.showMessageDialog(parentFrame, new JLabel(new ImageIcon(scaledImage)), title, JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parentFrame, "Proof image not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parentFrame, "Error loading image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String saveRefundProofImage(String sourcePath, String originalFileName) {
        try {
            File directory = new File(REFUND_PROOF_PATH);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String extension = "";
            String nameWithoutExt = originalFileName;
            int dotIndex = originalFileName.lastIndexOf(".");
            if (dotIndex > 0) {
                nameWithoutExt = originalFileName.substring(0, dotIndex);
                extension = originalFileName.substring(dotIndex);
            }

            String destinationPath = REFUND_PROOF_PATH + originalFileName;
            File destFile = new File(destinationPath);
            int counter = 1;

            while (destFile.exists()) {
                String newFileName = nameWithoutExt + "_" + counter + extension;
                destinationPath = REFUND_PROOF_PATH + newFileName;
                destFile = new File(destinationPath);
                counter++;
            }

            Files.copy(Paths.get(sourcePath), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
            return "BarterZone.resources.images.refund_proofs." + destFile.getName();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void markRefundAsProcessed(int refundId, String traderName, int tradeId, JFrame parent, Runnable onComplete) {
        if (refundId == -1) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(parent,
                "Mark refund as processed for " + traderName + "?\n\n"
                + "This will notify the trader that their refund has been sent.",
                "Confirm Refund",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE tbl_refund SET is_refunded = 1, refund_confirmed_date = datetime('now'), updated_date = datetime('now') WHERE refund_id = ?";
            db.updateRecord(sql, refundId);

            JOptionPane.showMessageDialog(parent, "Refund marked as processed for " + traderName + "!", "Success", JOptionPane.INFORMATION_MESSAGE);

            if (onComplete != null) {
                onComplete.run();
            }
        }
    }

    public void checkOverallRefundStatus(int tradeId, JLabel refundOverallLabel, JFrame parent, Runnable onComplete) {
        String sql = "SELECT COUNT(*) as refunded_count FROM tbl_refund WHERE trade_id = ? AND is_refunded = 1";
        double refundedCount = db.getSingleValue(sql, tradeId);

        if (refundedCount == 2) {
            refundOverallLabel.setText("BOTH REFUNDS HAVE BEEN PROCESSED! Ready to complete the trade.");
            refundOverallLabel.setForeground(successColor);

            String updateSql = "UPDATE tbl_trade SET trade_status = 'ready_for_completion' WHERE trade_id = ?";
            db.updateRecord(updateSql, tradeId);

            int completeConfirm = JOptionPane.showConfirmDialog(parent,
                    "Both refunds have been processed!\n\n"
                    + "Do you want to mark this trade as COMPLETED now?\n"
                    + "This will move the trade to history.",
                    "Complete Trade",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (completeConfirm == JOptionPane.YES_OPTION && onComplete != null) {
                onComplete.run();
            }
        } else if (refundedCount == 1) {
            refundOverallLabel.setText("One refund processed. Waiting for the other trader's refund confirmation.");
            refundOverallLabel.setForeground(warningColor);
        } else {
            refundOverallLabel.setText("No refunds processed yet. Add refund proof and mark as refunded for each trader.");
            refundOverallLabel.setForeground(errorColor);
        }
    }

    public void completeTrade(int tradeId, Runnable onComplete) {
        String getSql = "SELECT * FROM tbl_trade WHERE trade_id = ?";
        List<Map<String, Object>> trade = db.fetchRecords(getSql, tradeId);

        if (!trade.isEmpty()) {
            Map<String, Object> t = trade.get(0);

            String historySql = "INSERT INTO tbl_trade_history "
                    + "(trade_id, offer_trader_id, target_trader_id, offer_item_id, "
                    + "target_item_id, trade_status, trade_DateRequest, trade_DateCompleted) "
                    + "VALUES (?, ?, ?, ?, ?, 'completed', ?, datetime('now'))";

            db.addRecord(historySql,
                    tradeId,
                    t.get("offer_trader_id"),
                    t.get("target_trader_id"),
                    t.get("offer_item_id"),
                    t.get("target_item_id"),
                    t.get("trade_DateRequest"));

            String deleteSql = "DELETE FROM tbl_trade WHERE trade_id = ?";
            db.deleteRecord(deleteSql, tradeId);

            JOptionPane.showMessageDialog(parentFrame,
                    "TRADE COMPLETED SUCCESSFULLY!\n\n"
                    + "The trade has been moved to history.",
                    "Trade Complete",
                    JOptionPane.INFORMATION_MESSAGE);

            if (onComplete != null) {
                onComplete.run();
            }
        }
    }
}
