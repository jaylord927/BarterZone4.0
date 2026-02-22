package BarterZone.Dashboard.trader;

import database.config.config;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Cursor;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class edit_items extends javax.swing.JFrame {

    private int traderId;
    private String traderName;
    private int itemId;
    private config db;
    private String selectedImagePath = "";
    private String selectedImageFileName = "";
    private String currentImagePath = "";

    private javax.swing.JTextField txtItemName;
    private javax.swing.JTextField txtBrand;
    private javax.swing.JComboBox<String> cmbCondition;
    private javax.swing.JTextField txtDateBought;
    private javax.swing.JTextArea txtDescription;
    private javax.swing.JLabel lblPhotoPreview;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnBrowseImage;

    private static final String IMAGE_STORAGE_PATH = "src/BarterZone/resources/images/items/";

    public edit_items(int traderId, String traderName, int itemId) {
        this.traderId = traderId;
        this.traderName = traderName;
        this.itemId = itemId;
        this.db = new config();
        initComponents();
        setupDialog();
        loadItemData();
        createImageDirectory();
    }

    private void createImageDirectory() {
        File directory = new File(IMAGE_STORAGE_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private void setupDialog() {
        setTitle("Edit Item");
        setSize(500, 550);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void loadItemData() {
        String sql = "SELECT * FROM tbl_items WHERE items_id = ? AND trader_id = ?";
        List<Map<String, Object>> items = db.fetchRecords(sql, itemId, traderId);

        if (!items.isEmpty()) {
            Map<String, Object> item = items.get(0);

            txtItemName.setText(item.get("item_Name") != null ? item.get("item_Name").toString() : "");
            txtBrand.setText(item.get("item_Brand") != null ? item.get("item_Brand").toString() : "");

            String condition = item.get("item_Condition") != null ? item.get("item_Condition").toString() : "New";
            cmbCondition.setSelectedItem(condition);

            txtDateBought.setText(item.get("item_Date") != null ? item.get("item_Date").toString() : "");
            txtDescription.setText(item.get("item_Description") != null ? item.get("item_Description").toString() : "");

            String photoPath = item.get("item_picture") != null ? item.get("item_picture").toString() : "";
            currentImagePath = photoPath;
            
            if (!photoPath.isEmpty()) {
                String fullPath = "src/" + photoPath.replace(".", "/");
                File imageFile = new File(fullPath);
                if (imageFile.exists()) {
                    selectedImagePath = fullPath;
                    selectedImageFileName = imageFile.getName();
                    displayImagePreview(fullPath);
                } else {
                    selectedImagePath = "";
                    selectedImageFileName = "";
                    lblPhotoPreview.setIcon(null);
                    lblPhotoPreview.setText("No Image");
                }
            } else {
                selectedImagePath = "";
                selectedImageFileName = "";
                lblPhotoPreview.setIcon(null);
                lblPhotoPreview.setText("No Image");
            }
        }
    }

    private void initComponents() {

        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        int y = 20;
        int labelWidth = 100;
        int fieldWidth = 300;
        int fieldX = 120;

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(12, 192, 223));
        titlePanel.setBounds(0, 0, 500, 40);
        titlePanel.setLayout(null);

        javax.swing.JLabel titleLabel = new javax.swing.JLabel("EDIT ITEM");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(20, 5, 200, 30);
        titlePanel.add(titleLabel);

        getContentPane().add(titlePanel);
        y = 60;

        javax.swing.JLabel lblItemName = new javax.swing.JLabel("Item Name:");
        lblItemName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblItemName.setBounds(20, y, labelWidth, 25);
        getContentPane().add(lblItemName);

        txtItemName = new javax.swing.JTextField();
        txtItemName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtItemName.setBounds(fieldX, y, fieldWidth, 30);
        txtItemName.setBorder(new LineBorder(new Color(200, 200, 200)));
        getContentPane().add(txtItemName);
        y += 45;

        javax.swing.JLabel lblBrand = new javax.swing.JLabel("Brand:");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblBrand.setBounds(20, y, labelWidth, 25);
        getContentPane().add(lblBrand);

        txtBrand = new javax.swing.JTextField();
        txtBrand.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBrand.setBounds(fieldX, y, fieldWidth, 30);
        txtBrand.setBorder(new LineBorder(new Color(200, 200, 200)));
        getContentPane().add(txtBrand);
        y += 45;

        javax.swing.JLabel lblCondition = new javax.swing.JLabel("Condition:");
        lblCondition.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCondition.setBounds(20, y, labelWidth, 25);
        getContentPane().add(lblCondition);

        cmbCondition = new javax.swing.JComboBox<>(new String[]{"New", "Like New", "Good", "Fair", "Poor"});
        cmbCondition.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbCondition.setBounds(fieldX, y, fieldWidth, 30);
        cmbCondition.setBackground(Color.WHITE);
        getContentPane().add(cmbCondition);
        y += 45;

        javax.swing.JLabel lblDate = new javax.swing.JLabel("Date Bought:");
        lblDate.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDate.setBounds(20, y, labelWidth, 25);
        getContentPane().add(lblDate);

        txtDateBought = new javax.swing.JTextField();
        txtDateBought.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDateBought.setBounds(fieldX, y, fieldWidth, 30);
        txtDateBought.setBorder(new LineBorder(new Color(200, 200, 200)));
        getContentPane().add(txtDateBought);
        y += 45;

        javax.swing.JLabel lblDescription = new javax.swing.JLabel("Description:");
        lblDescription.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDescription.setBounds(20, y, labelWidth, 25);
        getContentPane().add(lblDescription);

        txtDescription = new javax.swing.JTextArea();
        txtDescription.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(txtDescription);
        scrollPane.setBounds(fieldX, y, fieldWidth, 80);
        scrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        getContentPane().add(scrollPane);
        y += 95;

        javax.swing.JLabel lblPhoto = new javax.swing.JLabel("Photo:");
        lblPhoto.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPhoto.setBounds(20, y, labelWidth, 25);
        getContentPane().add(lblPhoto);

        btnBrowseImage = new javax.swing.JButton("Browse Image");
        btnBrowseImage.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBrowseImage.setBackground(new Color(12, 192, 223));
        btnBrowseImage.setForeground(Color.WHITE);
        btnBrowseImage.setBounds(fieldX, y, 120, 30);
        btnBrowseImage.setBorder(null);
        btnBrowseImage.setFocusPainted(false);
        btnBrowseImage.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBrowseImage.addActionListener(e -> browseImage());
        getContentPane().add(btnBrowseImage);

        lblPhotoPreview = new javax.swing.JLabel();
        lblPhotoPreview.setBounds(fieldX + 130, y - 50, 150, 150);
        lblPhotoPreview.setBorder(new LineBorder(new Color(200, 200, 200)));
        lblPhotoPreview.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPhotoPreview.setText("No Image");
        lblPhotoPreview.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        getContentPane().add(lblPhotoPreview);
        y += 120;

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBounds(0, y, 500, 60);

        btnSave = new javax.swing.JButton("UPDATE ITEM");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(new Color(0, 102, 102));
        btnSave.setForeground(Color.WHITE);
        btnSave.setBounds(150, 15, 120, 35);
        btnSave.setBorder(null);
        btnSave.setFocusPainted(false);
        btnSave.addActionListener(e -> updateItem());
        buttonPanel.add(btnSave);

        btnCancel = new javax.swing.JButton("CANCEL");
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setBackground(new Color(204, 0, 0));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setBounds(280, 15, 100, 35);
        btnCancel.setBorder(null);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> {
            myitems myItemsFrame = new myitems(traderId, traderName);
            myItemsFrame.setVisible(true);
            myItemsFrame.setLocationRelativeTo(null);
            this.dispose();
        });
        buttonPanel.add(btnCancel);

        getContentPane().add(buttonPanel);

        pack();
    }

    private void browseImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif", "bmp"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedImagePath = selectedFile.getAbsolutePath();
            selectedImageFileName = selectedFile.getName();
            displayImagePreview(selectedImagePath);
        }
    }

    private void displayImagePreview(String imagePath) {
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            Image image = icon.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
            lblPhotoPreview.setIcon(new ImageIcon(image));
            lblPhotoPreview.setText("");
        } catch (Exception e) {
            lblPhotoPreview.setIcon(null);
            lblPhotoPreview.setText("Preview Error");
        }
    }

    private String saveImageToApp(String sourcePath) {
        if (sourcePath == null || sourcePath.isEmpty()) {
            return "";
        }

        try {
            File directory = new File(IMAGE_STORAGE_PATH);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String destinationPath = IMAGE_STORAGE_PATH + selectedImageFileName;

            File destFile = new File(destinationPath);
            if (destFile.exists()) {
                String nameWithoutExt = selectedImageFileName.substring(0, selectedImageFileName.lastIndexOf("."));
                String extension = selectedImageFileName.substring(selectedImageFileName.lastIndexOf("."));
                int counter = 1;
                while (destFile.exists()) {
                    String newFileName = nameWithoutExt + "_" + counter + extension;
                    destinationPath = IMAGE_STORAGE_PATH + newFileName;
                    destFile = new File(destinationPath);
                    counter++;
                }
            }

            Files.copy(Paths.get(sourcePath), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);

            File savedFile = new File(destinationPath);
            return "BarterZone.resources.images.items." + savedFile.getName();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return "";
        }
    }

    private void updateItem() {
        if (txtItemName.getText().trim().isEmpty() || txtBrand.getText().trim().isEmpty() || txtDescription.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String savedImagePath;
        if (!selectedImagePath.isEmpty() && !selectedImagePath.contains("BarterZone.resources.images.items")) {
            savedImagePath = saveImageToApp(selectedImagePath);
        } else {
            savedImagePath = currentImagePath;
        }

        String sql = "UPDATE tbl_items SET item_Name = ?, item_Brand = ?, item_Condition = ?, "
                + "item_Date = ?, item_Description = ?, item_picture = ? WHERE items_id = ? AND trader_id = ?";

        db.updateRecord(sql,
                txtItemName.getText().trim(),
                txtBrand.getText().trim(),
                cmbCondition.getSelectedItem().toString(),
                txtDateBought.getText().trim(),
                txtDescription.getText().trim(),
                savedImagePath,
                itemId,
                traderId
        );

        JOptionPane.showMessageDialog(this, "Item updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        
        myitems myItemsFrame = new myitems(traderId, traderName);
        myItemsFrame.setVisible(true);
        myItemsFrame.setLocationRelativeTo(null);
        this.dispose();
    }
}