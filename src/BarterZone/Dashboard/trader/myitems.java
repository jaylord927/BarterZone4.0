package BarterZone.Dashboard.trader;

import BarterZone.resources.IconManager;
import database.config.config;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;

public class myitems extends javax.swing.JFrame {

    private int traderId;
    private String traderName;
    private config db;
    private DefaultTableModel tableModel;
    private TableRowSorter<TableModel> rowSorter;
    private String selectedImagePath = "";
    private int selectedItemId = -1;
    private IconManager iconManager;

    // UI Components
    private javax.swing.JTextField searchField;
    private javax.swing.JButton addButton;
    private javax.swing.JButton editButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton uploadPhotoButton;
    private javax.swing.JDialog itemDialog;
    private javax.swing.JTextField txtItemName;
    private javax.swing.JTextField txtBrand;
    private javax.swing.JComboBox<String> cmbCondition;
    private javax.swing.JTextField txtDateBought;
    private javax.swing.JTextArea txtDescription;
    private javax.swing.JLabel lblPhotoPreview;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnBrowseImage;

    private Color hoverColor = new Color(70, 210, 235);
    private Color defaultColor = new Color(12, 192, 223);
    private Color activeColor = new Color(0, 150, 180);
    private JPanel activePanel = null;

    // Path for storing images
    private static final String IMAGE_STORAGE_PATH = "src/BarterZone/resources/images/items/";

    public myitems(int traderId, String traderName) {
        this.traderId = traderId;
        this.traderName = traderName;
        this.db = new config();
        this.iconManager = IconManager.getInstance();
        initComponents();

        // Load icons for side panel
        loadAndResizeIcons();

        // Set as active panel
        setActivePanel(panelmyitems);

        setupCustomComponents();
        loadItems();
        setupLiveSearch();

        // Set title and properties
        setTitle("My Items - " + traderName);
        setSize(800, 500);
        setResizable(false);
        setLocationRelativeTo(null);

        // Create image directory if it doesn't exist
        createImageDirectory();
    }

    private void createImageDirectory() {
        File directory = new File(IMAGE_STORAGE_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private void loadAndResizeIcons() {
        setIconSafely(dashboardicon, iconManager.getSideMenuIcon("dashboard"));
        setIconSafely(myitemsicon, iconManager.getSideMenuIcon("myitems"));
        setIconSafely(finditemsicon, iconManager.getSideMenuIcon("finditems"));
        setIconSafely(tradesicon, iconManager.getSideMenuIcon("trade"));
        setIconSafely(messagesicon, iconManager.getSideMenuIcon("messages"));
        setIconSafely(reportsicon, iconManager.getSideMenuIcon("report"));
        setIconSafely(profileicon, iconManager.getSideMenuIcon("profile"));
        setIconSafely(logouticon, iconManager.getSideMenuIcon("logout"));

        setIconSafely(barterzonelogo, iconManager.getLogoIcon());
    }

    private void setIconSafely(javax.swing.JLabel label, ImageIcon icon) {
        if (icon != null) {
            label.setIcon(icon);
            label.setText("");
        }
    }

    private void setActivePanel(JPanel panel) {
        if (activePanel != null) {
            activePanel.setBackground(defaultColor);
        }
        activePanel = panel;
        activePanel.setBackground(activeColor);
    }

    private void setHover(JPanel panel) {
        if (panel != activePanel) {
            panel.setBackground(hoverColor);
        }
    }

    private void setDefault(JPanel panel) {
        if (panel != activePanel) {
            panel.setBackground(defaultColor);
        }
    }

    private void setupCustomComponents() {
        // Set username and avatar
        username.setText(traderName);
        if (traderName != null && traderName.length() > 0) {
            avatarletter.setText(String.valueOf(traderName.charAt(0)).toUpperCase());
        }

        // Set current date
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        CurrentDate.setText(sdf.format(new Date()));

        // Create main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBounds(0, 0, 620, 450);

        // Search Section
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(null);
        searchPanel.setBackground(new Color(245, 245, 245));
        searchPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        searchPanel.setBounds(10, 10, 600, 50);

        javax.swing.JLabel searchLabel = new javax.swing.JLabel("Search Items:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchLabel.setBounds(10, 15, 100, 20);
        searchPanel.add(searchLabel);

        searchField = new javax.swing.JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBounds(120, 12, 200, 26);
        searchField.setBorder(new LineBorder(new Color(200, 200, 200)));
        searchPanel.add(searchField);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        buttonPanel.setBounds(10, 70, 600, 50);

        addButton = new javax.swing.JButton("Add Item");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addButton.setBackground(new Color(0, 102, 102));
        addButton.setForeground(Color.WHITE);
        addButton.setBounds(10, 10, 100, 30);
        addButton.setBorder(null);
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> showAddItemDialog());
        buttonPanel.add(addButton);

        editButton = new javax.swing.JButton("Edit");
        editButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        editButton.setBackground(new Color(255, 153, 0));
        editButton.setForeground(Color.WHITE);
        editButton.setBounds(120, 10, 80, 30);
        editButton.setBorder(null);
        editButton.setFocusPainted(false);
        editButton.addActionListener(e -> showEditItemDialog());
        buttonPanel.add(editButton);

        deleteButton = new javax.swing.JButton("Delete");
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        deleteButton.setBackground(new Color(204, 0, 0));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setBounds(210, 10, 80, 30);
        deleteButton.setBorder(null);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(e -> deleteSelectedItem());
        buttonPanel.add(deleteButton);

        uploadPhotoButton = new javax.swing.JButton("Upload Photo");
        uploadPhotoButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        uploadPhotoButton.setBackground(new Color(12, 192, 223));
        uploadPhotoButton.setForeground(Color.WHITE);
        uploadPhotoButton.setBounds(300, 10, 120, 30);
        uploadPhotoButton.setBorder(null);
        uploadPhotoButton.setFocusPainted(false);
        uploadPhotoButton.addActionListener(e -> uploadPhotoForSelected());
        buttonPanel.add(uploadPhotoButton);

        // Table Panel
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(null);
        tablePanel.setBackground(new Color(245, 245, 245));
        tablePanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        tablePanel.setBounds(10, 130, 600, 310);

        // Setup table
        setupTable();

        jScrollPane1.setBounds(10, 10, 580, 290);
        jScrollPane1.setBorder(null);
        tablePanel.add(jScrollPane1);

        // Add all panels to content panel
        contentPanel.add(searchPanel);
        contentPanel.add(buttonPanel);
        contentPanel.add(tablePanel);

        // Clear jPanel2 and add content panel
        jPanel2.removeAll();
        jPanel2.setLayout(null);
        jPanel2.add(contentPanel);
        contentPanel.setBounds(0, 0, 620, 450);

        jPanel2.revalidate();
        jPanel2.repaint();
    }

    private void setupTable() {
        String[] columns = {"ID", "Item Name", "Brand", "Condition", "Date Bought", "Description", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        myitemstable.setModel(tableModel);
        myitemstable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        myitemstable.setRowHeight(25);
        myitemstable.setShowGrid(true);
        myitemstable.setGridColor(new Color(230, 230, 230));
        myitemstable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        myitemstable.getTableHeader().setBackground(new Color(12, 192, 223));
        myitemstable.getTableHeader().setForeground(Color.WHITE);
        myitemstable.getTableHeader().setBorder(null);
        myitemstable.setSelectionBackground(new Color(184, 239, 255));

        // Hide the ID column
        myitemstable.getColumnModel().getColumn(0).setMinWidth(0);
        myitemstable.getColumnModel().getColumn(0).setMaxWidth(0);
        myitemstable.getColumnModel().getColumn(0).setWidth(0);

        // Set column widths
        myitemstable.getColumnModel().getColumn(1).setPreferredWidth(150);
        myitemstable.getColumnModel().getColumn(2).setPreferredWidth(100);
        myitemstable.getColumnModel().getColumn(3).setPreferredWidth(80);
        myitemstable.getColumnModel().getColumn(4).setPreferredWidth(100);
        myitemstable.getColumnModel().getColumn(5).setPreferredWidth(200);
        myitemstable.getColumnModel().getColumn(6).setPreferredWidth(80);

        // Add selection listener
        myitemstable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && myitemstable.getSelectedRow() >= 0) {
                int modelRow = myitemstable.convertRowIndexToModel(myitemstable.getSelectedRow());
                selectedItemId = Integer.parseInt(tableModel.getValueAt(modelRow, 0).toString());
            }
        });
    }

    private void setupLiveSearch() {
        rowSorter = new TableRowSorter<>(myitemstable.getModel());
        myitemstable.setRowSorter(rowSorter);

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }
        });
    }

    private void filterTable() {
        String text = searchField.getText();
        if (text.trim().length() == 0) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 1, 2, 3, 5));
        }
    }

    private void loadItems() {
        tableModel.setRowCount(0);

        String sql = "SELECT items_id, item_Name, item_Brand, item_Condition, "
                + "item_Date, item_Description, is_active "
                + "FROM tbl_items WHERE trader_id = ? ORDER BY items_id DESC";

        List<Map<String, Object>> items = db.fetchRecords(sql, traderId);

        for (Map<String, Object> item : items) {
            Object status = item.get("is_active");
            String statusText = "Active";
            if (status instanceof Boolean) {
                statusText = (Boolean) status ? "Active" : "Inactive";
            } else if (status instanceof Integer) {
                statusText = ((Integer) status == 1) ? "Active" : "Inactive";
            }

            tableModel.addRow(new Object[]{
                item.get("items_id"),
                item.get("item_Name"),
                item.get("item_Brand"),
                item.get("item_Condition"),
                item.get("item_Date"),
                item.get("item_Description"),
                statusText
            });
        }
    }

    private void refreshItems() {
        loadItems();
        JOptionPane.showMessageDialog(this, "Items refreshed!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAddItemDialog() {
        createItemDialog("Add New Item", true);
    }

    private void showEditItemDialog() {
        if (selectedItemId == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get the most recent data from the database
        String sql = "SELECT * FROM tbl_items WHERE items_id = ? AND trader_id = ?";
        List<Map<String, Object>> items = db.fetchRecords(sql, selectedItemId, traderId);

        if (!items.isEmpty()) {
            Map<String, Object> item = items.get(0);
            createItemDialog("Edit Item", false);

            // Populate with the most recent data
            txtItemName.setText(item.get("item_Name") != null ? item.get("item_Name").toString() : "");
            txtBrand.setText(item.get("item_Brand") != null ? item.get("item_Brand").toString() : "");

            String condition = item.get("item_Condition") != null ? item.get("item_Condition").toString() : "New";
            cmbCondition.setSelectedItem(condition);

            txtDateBought.setText(item.get("item_Date") != null ? item.get("item_Date").toString() : "");
            txtDescription.setText(item.get("item_Description") != null ? item.get("item_Description").toString() : "");

            // Load and display the existing image if available
            String photoPath = item.get("item_picture") != null ? item.get("item_picture").toString() : "";
            if (!photoPath.isEmpty()) {
                // Construct the full path to the image
                String fullPath = "src/" + photoPath.replace(".", "/");
                File imageFile = new File(fullPath);
                if (imageFile.exists()) {
                    selectedImagePath = fullPath;
                    displayImagePreview(fullPath);
                } else {
                    selectedImagePath = "";
                    lblPhotoPreview.setIcon(null);
                    lblPhotoPreview.setText("No Image");
                }
            } else {
                selectedImagePath = "";
                lblPhotoPreview.setIcon(null);
                lblPhotoPreview.setText("No Image");
            }
        }
    }

    private void createItemDialog(String title, boolean isAdd) {
        itemDialog = new javax.swing.JDialog(this, title, true);
        itemDialog.setSize(500, 550);
        itemDialog.setLayout(null);
        itemDialog.setLocationRelativeTo(this);
        itemDialog.getContentPane().setBackground(Color.WHITE);

        int y = 20;
        int labelWidth = 100;
        int fieldWidth = 300;
        int fieldX = 120;

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(12, 192, 223));
        titlePanel.setBounds(0, 0, 500, 40);
        titlePanel.setLayout(null);

        javax.swing.JLabel titleLabel = new javax.swing.JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(20, 5, 200, 30);
        titlePanel.add(titleLabel);

        itemDialog.add(titlePanel);
        y = 60;

        // Item Name
        javax.swing.JLabel lblItemName = new javax.swing.JLabel("Item Name:");
        lblItemName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblItemName.setBounds(20, y, labelWidth, 25);
        itemDialog.add(lblItemName);

        txtItemName = new javax.swing.JTextField();
        txtItemName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtItemName.setBounds(fieldX, y, fieldWidth, 30);
        txtItemName.setBorder(new LineBorder(new Color(200, 200, 200)));
        itemDialog.add(txtItemName);
        y += 45;

        // Brand
        javax.swing.JLabel lblBrand = new javax.swing.JLabel("Brand:");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblBrand.setBounds(20, y, labelWidth, 25);
        itemDialog.add(lblBrand);

        txtBrand = new javax.swing.JTextField();
        txtBrand.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBrand.setBounds(fieldX, y, fieldWidth, 30);
        txtBrand.setBorder(new LineBorder(new Color(200, 200, 200)));
        itemDialog.add(txtBrand);
        y += 45;

        // Condition
        javax.swing.JLabel lblCondition = new javax.swing.JLabel("Condition:");
        lblCondition.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCondition.setBounds(20, y, labelWidth, 25);
        itemDialog.add(lblCondition);

        cmbCondition = new javax.swing.JComboBox<>(new String[]{"New", "Like New", "Good", "Fair", "Poor"});
        cmbCondition.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbCondition.setBounds(fieldX, y, fieldWidth, 30);
        cmbCondition.setBackground(Color.WHITE);
        itemDialog.add(cmbCondition);
        y += 45;

        // Date Bought
        javax.swing.JLabel lblDate = new javax.swing.JLabel("Date Bought:");
        lblDate.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDate.setBounds(20, y, labelWidth, 25);
        itemDialog.add(lblDate);

        txtDateBought = new javax.swing.JTextField();
        txtDateBought.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDateBought.setBounds(fieldX, y, fieldWidth, 30);
        txtDateBought.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        txtDateBought.setBorder(new LineBorder(new Color(200, 200, 200)));
        itemDialog.add(txtDateBought);
        y += 45;

        // Description
        javax.swing.JLabel lblDescription = new javax.swing.JLabel("Description:");
        lblDescription.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDescription.setBounds(20, y, labelWidth, 25);
        itemDialog.add(lblDescription);

        txtDescription = new javax.swing.JTextArea();
        txtDescription.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setBorder(new LineBorder(new Color(200, 200, 200)));
        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(txtDescription);
        scrollPane.setBounds(fieldX, y, fieldWidth, 80);
        scrollPane.setBorder(null);
        itemDialog.add(scrollPane);
        y += 95;

        // Photo Upload
        javax.swing.JLabel lblPhoto = new javax.swing.JLabel("Photo:");
        lblPhoto.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPhoto.setBounds(20, y, labelWidth, 25);
        itemDialog.add(lblPhoto);

        btnBrowseImage = new javax.swing.JButton("Browse Image");
        btnBrowseImage.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBrowseImage.setBackground(new Color(12, 192, 223));
        btnBrowseImage.setForeground(Color.WHITE);
        btnBrowseImage.setBounds(fieldX, y, 120, 30);
        btnBrowseImage.setBorder(null);
        btnBrowseImage.setFocusPainted(false);
        btnBrowseImage.addActionListener(e -> browseImage());
        itemDialog.add(btnBrowseImage);

        lblPhotoPreview = new javax.swing.JLabel();
        lblPhotoPreview.setBounds(fieldX + 130, y - 50, 150, 150);
        lblPhotoPreview.setBorder(new LineBorder(new Color(200, 200, 200)));
        lblPhotoPreview.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPhotoPreview.setText("No Image");
        lblPhotoPreview.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        itemDialog.add(lblPhotoPreview);
        y += 120;

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBounds(0, y, 500, 60);

        btnSave = new javax.swing.JButton(isAdd ? "Add Item" : "Update Item");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(new Color(0, 102, 102));
        btnSave.setForeground(Color.WHITE);
        btnSave.setBounds(150, 15, 120, 35);
        btnSave.setBorder(null);
        btnSave.setFocusPainted(false);
        btnSave.addActionListener(e -> {
            if (isAdd) {
                saveItem();
            } else {
                updateItem();
            }
        });
        buttonPanel.add(btnSave);

        btnCancel = new javax.swing.JButton("Cancel");
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setBackground(new Color(204, 0, 0));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setBounds(280, 15, 100, 35);
        btnCancel.setBorder(null);
        btnCancel.setFocusPainted(false);
        btnCancel.addActionListener(e -> itemDialog.dispose());
        buttonPanel.add(btnCancel);

        itemDialog.add(buttonPanel);

        itemDialog.setVisible(true);
    }

    private void browseImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif", "bmp"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedImagePath = selectedFile.getAbsolutePath();
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
            // Create directory if it doesn't exist
            File directory = new File(IMAGE_STORAGE_PATH);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Generate unique filename
            String extension = sourcePath.substring(sourcePath.lastIndexOf("."));
            String fileName = "item_" + System.currentTimeMillis() + extension;
            String destinationPath = IMAGE_STORAGE_PATH + fileName;

            // Copy file
            Files.copy(Paths.get(sourcePath), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);

            // Return the package-style path for database storage
            return "BarterZone.resources.images.items." + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return "";
        }
    }

    private void saveItem() {
        if (txtItemName.getText().trim().isEmpty() || txtBrand.getText().trim().isEmpty() || txtDescription.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(itemDialog, "Please fill in all required fields!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String savedImagePath = saveImageToApp(selectedImagePath);

        String sql = "INSERT INTO tbl_items (item_Name, item_Brand, item_Condition, item_Date, "
                + "item_Description, item_picture, trader_id, created_date, is_active) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, datetime('now'), 1)";

        db.addRecord(sql,
                txtItemName.getText().trim(),
                txtBrand.getText().trim(),
                cmbCondition.getSelectedItem().toString(),
                txtDateBought.getText().trim(),
                txtDescription.getText().trim(),
                savedImagePath,
                traderId
        );

        JOptionPane.showMessageDialog(itemDialog, "Item added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        itemDialog.dispose();
        selectedImagePath = "";
        loadItems();
    }

    private void updateItem() {
        if (txtItemName.getText().trim().isEmpty() || txtBrand.getText().trim().isEmpty() || txtDescription.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(itemDialog, "Please fill in all required fields!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String savedImagePath;
        if (!selectedImagePath.isEmpty() && !selectedImagePath.contains("BarterZone.resources.images.items")) {
            // New image selected
            savedImagePath = saveImageToApp(selectedImagePath);
        } else {
            // Keep existing image path
            savedImagePath = selectedImagePath;
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
                selectedItemId,
                traderId
        );

        JOptionPane.showMessageDialog(itemDialog, "Item updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        itemDialog.dispose();
        selectedImagePath = "";
        selectedItemId = -1;
        loadItems();
    }

    private void deleteSelectedItem() {
        if (selectedItemId == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this item?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM tbl_items WHERE items_id = ? AND trader_id = ?";
            db.deleteRecord(sql, selectedItemId, traderId);

            JOptionPane.showMessageDialog(this, "Item deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            selectedItemId = -1;
            loadItems();
        }
    }

    private void uploadPhotoForSelected() {
        if (selectedItemId == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to upload photo for.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif", "bmp"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String imagePath = fileChooser.getSelectedFile().getAbsolutePath();
            String savedImagePath = saveImageToApp(imagePath);

            String sql = "UPDATE tbl_items SET item_picture = ? WHERE items_id = ? AND trader_id = ?";
            db.updateRecord(sql, savedImagePath, selectedItemId, traderId);

            JOptionPane.showMessageDialog(this, "Photo uploaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadItems();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        tradermenu1 = new javax.swing.JPanel();
        logotext = new javax.swing.JLabel();
        avatar = new javax.swing.JPanel();
        avatarletter = new javax.swing.JLabel();
        username = new javax.swing.JLabel();
        barterzonelogo = new javax.swing.JLabel();
        paneldashboard = new javax.swing.JPanel();
        dashboardicon = new javax.swing.JLabel();
        dashboard = new javax.swing.JLabel();
        panelmyitems = new javax.swing.JPanel();
        myitemsicon = new javax.swing.JLabel();
        myitems = new javax.swing.JLabel();
        panelfinditems = new javax.swing.JPanel();
        finditemsicon = new javax.swing.JLabel();
        finditems = new javax.swing.JLabel();
        paneltrades = new javax.swing.JPanel();
        tradesicon = new javax.swing.JLabel();
        trades = new javax.swing.JLabel();
        panelmessages = new javax.swing.JPanel();
        messagesicon = new javax.swing.JLabel();
        messages = new javax.swing.JLabel();
        panellogout = new javax.swing.JPanel();
        logout = new javax.swing.JLabel();
        logouticon = new javax.swing.JLabel();
        panelreports = new javax.swing.JPanel();
        Reports = new javax.swing.JLabel();
        reportsicon = new javax.swing.JLabel();
        panelprofile = new javax.swing.JPanel();
        profileicon = new javax.swing.JLabel();
        Profile = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        CurrentDate = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        myitemstable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 500));
        setPreferredSize(new java.awt.Dimension(800, 500));
        setResizable(false);

        tradermenu1.setBackground(new java.awt.Color(12, 192, 223));
        tradermenu1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(8, 150, 175), 1, true));
        tradermenu1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logotext.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        logotext.setForeground(new java.awt.Color(255, 255, 255));
        logotext.setText("BarterZone");
        logotext.setAlignmentX(0.5F);
        tradermenu1.add(logotext, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 0, -1, 40));

        avatar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3));
        avatar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        avatarletter.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        avatarletter.setForeground(new java.awt.Color(12, 192, 223));
        avatarletter.setText("T");
        avatar.add(avatarletter, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 50, 30));

        username.setBackground(new java.awt.Color(255, 255, 255));
        username.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        username.setForeground(new java.awt.Color(255, 255, 255));
        avatar.add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 110, 30));

        tradermenu1.add(avatar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 110, 60));

        barterzonelogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/logo.png"))); // NOI18N
        tradermenu1.add(barterzonelogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        paneldashboard.setBackground(new java.awt.Color(12, 192, 223));
        paneldashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                paneldashboardMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                paneldashboardMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                paneldashboardMouseExited(evt);
            }
        });

        dashboardicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/dashboard.png"))); // NOI18N

        dashboard.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        dashboard.setForeground(new java.awt.Color(255, 255, 255));
        dashboard.setText("Dashboard");

        javax.swing.GroupLayout paneldashboardLayout = new javax.swing.GroupLayout(paneldashboard);
        paneldashboard.setLayout(paneldashboardLayout);
        paneldashboardLayout.setHorizontalGroup(
                paneldashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneldashboardLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(dashboardicon, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14))
        );
        paneldashboardLayout.setVerticalGroup(
                paneldashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(dashboardicon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGroup(paneldashboardLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(dashboard)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tradermenu1.add(paneldashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 130, 40));

        panelmyitems.setBackground(new java.awt.Color(12, 192, 223));
        panelmyitems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelmyitemsMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelmyitemsMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelmyitemsMouseExited(evt);
            }
        });

        myitemsicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/myitems.png"))); // NOI18N

        myitems.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        myitems.setForeground(new java.awt.Color(255, 255, 255));
        myitems.setText("My Items");

        javax.swing.GroupLayout panelmyitemsLayout = new javax.swing.GroupLayout(panelmyitems);
        panelmyitems.setLayout(panelmyitemsLayout);
        panelmyitemsLayout.setHorizontalGroup(
                panelmyitemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelmyitemsLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(myitemsicon, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(myitems)
                                .addContainerGap(17, Short.MAX_VALUE))
        );
        panelmyitemsLayout.setVerticalGroup(
                panelmyitemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelmyitemsLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(panelmyitemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(myitems)
                                        .addComponent(myitemsicon, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(21, 21, 21))
        );

        tradermenu1.add(panelmyitems, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 130, 40));

        panelfinditems.setBackground(new java.awt.Color(12, 192, 223));
        panelfinditems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelfinditemsMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelfinditemsMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelfinditemsMouseExited(evt);
            }
        });

        finditemsicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/finditems.png"))); // NOI18N

        finditems.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        finditems.setForeground(new java.awt.Color(255, 255, 255));
        finditems.setText("Find Items");

        javax.swing.GroupLayout panelfinditemsLayout = new javax.swing.GroupLayout(panelfinditems);
        panelfinditems.setLayout(panelfinditemsLayout);
        panelfinditemsLayout.setHorizontalGroup(
                panelfinditemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelfinditemsLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(finditemsicon, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(finditems)
                                .addGap(18, 18, 18))
        );
        panelfinditemsLayout.setVerticalGroup(
                panelfinditemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelfinditemsLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelfinditemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(finditems)
                                        .addComponent(finditemsicon, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tradermenu1.add(panelfinditems, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, 130, 40));

        paneltrades.setBackground(new java.awt.Color(12, 192, 223));
        paneltrades.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                paneltradesMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                paneltradesMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                paneltradesMouseExited(evt);
            }
        });

        tradesicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/trade.png"))); // NOI18N

        trades.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        trades.setForeground(new java.awt.Color(255, 255, 255));
        trades.setText("Trades");

        javax.swing.GroupLayout paneltradesLayout = new javax.swing.GroupLayout(paneltrades);
        paneltrades.setLayout(paneltradesLayout);
        paneltradesLayout.setHorizontalGroup(
                paneltradesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneltradesLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(tradesicon, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(trades)
                                .addContainerGap(34, Short.MAX_VALUE))
        );
        paneltradesLayout.setVerticalGroup(
                paneltradesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneltradesLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(paneltradesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(trades)
                                        .addComponent(tradesicon, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(21, 21, 21))
        );

        tradermenu1.add(paneltrades, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, 130, 40));

        panelmessages.setBackground(new java.awt.Color(12, 192, 223));
        panelmessages.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelmessagesMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelmessagesMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelmessagesMouseExited(evt);
            }
        });

        messagesicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/messages.png"))); // NOI18N

        messages.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        messages.setForeground(new java.awt.Color(255, 255, 255));
        messages.setText("Messages");

        javax.swing.GroupLayout panelmessagesLayout = new javax.swing.GroupLayout(panelmessages);
        panelmessages.setLayout(panelmessagesLayout);
        panelmessagesLayout.setHorizontalGroup(
                panelmessagesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelmessagesLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(messagesicon, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(messages)
                                .addContainerGap(14, Short.MAX_VALUE))
        );
        panelmessagesLayout.setVerticalGroup(
                panelmessagesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelmessagesLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(panelmessagesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(messages)
                                        .addComponent(messagesicon, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22))
        );

        tradermenu1.add(panelmessages, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, 130, 40));

        panellogout.setBackground(new java.awt.Color(12, 192, 223));
        panellogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panellogoutMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panellogoutMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                panellogoutMouseExited(evt);
            }
        });

        logout.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        logout.setForeground(new java.awt.Color(255, 255, 255));
        logout.setText("Logout");

        logouticon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/logout.png"))); // NOI18N

        javax.swing.GroupLayout panellogoutLayout = new javax.swing.GroupLayout(panellogout);
        panellogout.setLayout(panellogoutLayout);
        panellogoutLayout.setHorizontalGroup(
                panellogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panellogoutLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(logouticon, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(16, 16, 16)
                                .addComponent(logout)
                                .addContainerGap(34, Short.MAX_VALUE))
        );
        panellogoutLayout.setVerticalGroup(
                panellogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panellogoutLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(panellogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(logout)
                                        .addComponent(logouticon, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22))
        );

        tradermenu1.add(panellogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 400, 130, 40));

        panelreports.setBackground(new java.awt.Color(12, 192, 223));
        panelreports.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelreportsMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelreportsMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelreportsMouseExited(evt);
            }
        });

        Reports.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Reports.setForeground(new java.awt.Color(255, 255, 255));
        Reports.setText("Reports");

        reportsicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/report.png"))); // NOI18N

        javax.swing.GroupLayout panelreportsLayout = new javax.swing.GroupLayout(panelreports);
        panelreports.setLayout(panelreportsLayout);
        panelreportsLayout.setHorizontalGroup(
                panelreportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelreportsLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(reportsicon, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Reports)
                                .addContainerGap(27, Short.MAX_VALUE))
        );
        panelreportsLayout.setVerticalGroup(
                panelreportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelreportsLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelreportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(reportsicon, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                        .addComponent(Reports, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tradermenu1.add(panelreports, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 130, 40));

        panelprofile.setBackground(new java.awt.Color(12, 192, 223));
        panelprofile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelprofileMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelprofileMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelprofileMouseExited(evt);
            }
        });

        profileicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/profile.png"))); // NOI18N

        Profile.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Profile.setForeground(new java.awt.Color(255, 255, 255));
        Profile.setText("Profile");

        javax.swing.GroupLayout panelprofileLayout = new javax.swing.GroupLayout(panelprofile);
        panelprofile.setLayout(panelprofileLayout);
        panelprofileLayout.setHorizontalGroup(
                panelprofileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelprofileLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(profileicon, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Profile)
                                .addContainerGap(34, Short.MAX_VALUE))
        );
        panelprofileLayout.setVerticalGroup(
                panelprofileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelprofileLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelprofileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(Profile)
                                        .addComponent(profileicon, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tradermenu1.add(panelprofile, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, 130, 40));

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 2));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setText("My Items");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, 30));

        CurrentDate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        CurrentDate.setForeground(new java.awt.Color(102, 102, 102));
        CurrentDate.setText("jLabel2");
        jPanel1.add(CurrentDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 10, 200, 30));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        myitemstable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null}
                },
                new String[]{
                    "Item name", "Brand", "Condition", "Date bought", "Description", "Status", "Picture"
                }
        ));
        myitemstable.setEditingColumn(1);
        myitemstable.setEditingRow(1);
        jScrollPane1.setViewportView(myitemstable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(tradermenu1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(tradermenu1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, 0)
                                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>                        

    // Sidebar Navigation Methods
    private void paneldashboardMouseClicked(java.awt.event.MouseEvent evt) {
        openDashboard();
    }

    private void panelfinditemsMouseClicked(java.awt.event.MouseEvent evt) {
        finditems findItemsFrame = new finditems(traderId, traderName);
        findItemsFrame.setVisible(true);
        findItemsFrame.setLocationRelativeTo(null);
        this.dispose();
    }

    private void paneltradesMouseClicked(java.awt.event.MouseEvent evt) {
        trades tradesFrame = new trades(traderId, traderName);
        tradesFrame.setVisible(true);
        tradesFrame.setLocationRelativeTo(null);
        this.dispose();
    }

    private void panelmessagesMouseClicked(java.awt.event.MouseEvent evt) {
        openMessages();
    }

    private void panelreportsMouseClicked(java.awt.event.MouseEvent evt) {
        openReports();
    }

    private void panelprofileMouseClicked(java.awt.event.MouseEvent evt) {
        openProfile();
    }

    private void panellogoutMouseClicked(java.awt.event.MouseEvent evt) {
        logout();
    }

    private void panelmyitemsMouseClicked(java.awt.event.MouseEvent evt) {
        refreshItems();
    }

    // Hover Effects
    private void paneldashboardMouseEntered(java.awt.event.MouseEvent evt) {
        setHover(paneldashboard);
    }

    private void paneldashboardMouseExited(java.awt.event.MouseEvent evt) {
        setDefault(paneldashboard);
    }

    private void panelmyitemsMouseEntered(java.awt.event.MouseEvent evt) {
        setHover(panelmyitems);
    }

    private void panelmyitemsMouseExited(java.awt.event.MouseEvent evt) {
        setDefault(panelmyitems);
    }

    private void panelfinditemsMouseEntered(java.awt.event.MouseEvent evt) {
        setHover(panelfinditems);
    }

    private void panelfinditemsMouseExited(java.awt.event.MouseEvent evt) {
        setDefault(panelfinditems);
    }

    private void paneltradesMouseEntered(java.awt.event.MouseEvent evt) {
        setHover(paneltrades);
    }

    private void paneltradesMouseExited(java.awt.event.MouseEvent evt) {
        setDefault(paneltrades);
    }

    private void panelmessagesMouseEntered(java.awt.event.MouseEvent evt) {
        setHover(panelmessages);
    }

    private void panelmessagesMouseExited(java.awt.event.MouseEvent evt) {
        setDefault(panelmessages);
    }

    private void panelreportsMouseEntered(java.awt.event.MouseEvent evt) {
        setHover(panelreports);
    }

    private void panelreportsMouseExited(java.awt.event.MouseEvent evt) {
        setDefault(panelreports);
    }

    private void panelprofileMouseEntered(java.awt.event.MouseEvent evt) {
        setHover(panelprofile);
    }

    private void panelprofileMouseExited(java.awt.event.MouseEvent evt) {
        setDefault(panelprofile);
    }

    private void panellogoutMouseEntered(java.awt.event.MouseEvent evt) {
        setHover(panellogout);
    }

    private void panellogoutMouseExited(java.awt.event.MouseEvent evt) {
        setDefault(panellogout);
    }

    // Navigation Methods
    private void openDashboard() {
        trader_dashboard dashboard = new trader_dashboard(traderId, traderName);
        dashboard.setVisible(true);
        dashboard.setLocationRelativeTo(null);
        this.dispose();
    }

    private void openFindItems() {
        JOptionPane.showMessageDialog(this, "Find Items feature coming soon!", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openTrades() {
        JOptionPane.showMessageDialog(this, "Trades feature coming soon!", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openMessages() {
        JOptionPane.showMessageDialog(this, "Messages feature coming soon!", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openReports() {
        JOptionPane.showMessageDialog(this, "Reports feature coming soon!", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openProfile() {
        profile profileFrame = new profile(traderId, traderName);
        profileFrame.setVisible(true);
        profileFrame.setLocationRelativeTo(null);
        this.dispose();
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            landing.landing landingFrame = new landing.landing();
            landingFrame.setVisible(true);
            landingFrame.setLocationRelativeTo(null);
            this.dispose();
        }
    }

    // Variables declaration - do not modify                     
    private javax.swing.JLabel CurrentDate;
    private javax.swing.JLabel Profile;
    javax.swing.JLabel Reports;
    javax.swing.JPanel avatar;
    javax.swing.JLabel avatarletter;
    javax.swing.JLabel barterzonelogo;
    javax.swing.JLabel dashboard;
    javax.swing.JLabel dashboardicon;
    javax.swing.JLabel finditems;
    javax.swing.JLabel finditemsicon;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    javax.swing.JPanel jPanel2;
    javax.swing.JScrollPane jScrollPane1;
    javax.swing.JLabel logotext;
    javax.swing.JLabel logout;
    javax.swing.JLabel logouticon;
    javax.swing.JLabel messages;
    javax.swing.JLabel messagesicon;
    javax.swing.JLabel myitems;
    javax.swing.JLabel myitemsicon;
    javax.swing.JTable myitemstable;
    javax.swing.JPanel paneldashboard;
    javax.swing.JPanel panelfinditems;
    javax.swing.JPanel panellogout;
    javax.swing.JPanel panelmessages;
    javax.swing.JPanel panelmyitems;
    javax.swing.JPanel panelprofile;
    javax.swing.JPanel panelreports;
    javax.swing.JPanel paneltrades;
    javax.swing.JLabel profileicon;
    javax.swing.JLabel reportsicon;
    javax.swing.JPanel tradermenu1;
    javax.swing.JLabel trades;
    javax.swing.JLabel tradesicon;
    javax.swing.JLabel username;
    // End of variables declaration                   
}
