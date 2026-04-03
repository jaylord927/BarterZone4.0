package BarterZone.Dashboard.trader;

import database.config.config;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class step2_submit {
    
    private int tradeId;
    private int traderId;
    private int otherTraderId;
    private String exchangeMethod;
    private config db;
    private JFrame parentFrame;
    
    // UI Components
    private JDialog detailsDialog;
    private JTextField meetupLocationField;
    private JTextField meetupDateField;
    private JTextField meetupTimeField;
    private JTextField contactPersonField;
    private JTextField contactNumberField;
    private JTextField googleMapsLinkField;
    private JTextArea meetupInstructionsArea;
    
    private JTextField deliveryAddressField;
    private JTextField courierField;
    private JTextField expectedDateField;
    private JTextField trackingField;
    private JTextArea deliveryInstructionsArea;
    
    private int myMeetupId = -1;
    private int myDeliveryId = -1;
    private int myDetailId = -1;
    private boolean myDetailsSubmitted = false;
    
    private Color themeColor = new Color(12, 192, 223);
    private Color successColor = new Color(46, 125, 50);
    private Color errorColor = new Color(204, 0, 0);
    
    public step2_submit(int tradeId, int traderId, int otherTraderId, String exchangeMethod, JFrame parentFrame) {
        this.tradeId = tradeId;
        this.traderId = traderId;
        this.otherTraderId = otherTraderId;
        this.exchangeMethod = exchangeMethod;
        this.db = new config();
        this.parentFrame = parentFrame;
        
        loadExistingDetails();
    }
    
    private void loadExistingDetails() {
        // Load my trade details from tbl_trade_details
        String sql = "SELECT * FROM tbl_trade_details WHERE trade_id = ? AND trader_id = ?";
        List<Map<String, Object>> details = db.fetchRecords(sql, tradeId, traderId);
        
        if (!details.isEmpty()) {
            Map<String, Object> d = details.get(0);
            myDetailId = Integer.parseInt(d.get("detail_id").toString());
            myDetailsSubmitted = d.get("my_details_submitted") != null && 
                Integer.parseInt(d.get("my_details_submitted").toString()) == 1;
            
            if (d.get("meetup_id") != null && Integer.parseInt(d.get("meetup_id").toString()) > 0) {
                myMeetupId = Integer.parseInt(d.get("meetup_id").toString());
            }
            if (d.get("delivery_id") != null && Integer.parseInt(d.get("delivery_id").toString()) > 0) {
                myDeliveryId = Integer.parseInt(d.get("delivery_id").toString());
            }
        }
    }
    
    public void showDialog() {
        detailsDialog = new JDialog(parentFrame, "Enter Your Details", true);
        detailsDialog.setSize(550, 550);
        detailsDialog.setLayout(null);
        detailsDialog.setLocationRelativeTo(parentFrame);
        detailsDialog.getContentPane().setBackground(Color.WHITE);
        detailsDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        detailsDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                detailsDialog.dispose();
            }
        });
        
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(themeColor);
        titlePanel.setBounds(0, 0, 550, 45);
        titlePanel.setLayout(null);
        
        JLabel titleLabel = new JLabel("ENTER EXCHANGE DETAILS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(20, 8, 300, 30);
        titlePanel.add(titleLabel);
        detailsDialog.add(titlePanel);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBounds(10, 55, 530, 440);
        detailsDialog.add(contentPanel);
        
        if (exchangeMethod != null && exchangeMethod.equals("meetup")) {
            buildMeetupPanel(contentPanel);
            loadMeetupDetails();
        } else if (exchangeMethod != null && exchangeMethod.equals("delivery")) {
            buildDeliveryPanel(contentPanel);
            loadDeliveryDetails();
        }
        
        JButton cancelButton = new JButton("CANCEL");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setBackground(errorColor);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBounds(350, 460, 100, 35);
        cancelButton.setBorder(null);
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> detailsDialog.dispose());
        contentPanel.add(cancelButton);
        
        detailsDialog.setVisible(true);
    }
    
    private void loadMeetupDetails() {
        if (myMeetupId != -1) {
            String sql = "SELECT * FROM tbl_meetup_details WHERE meetup_id = ?";
            List<Map<String, Object>> details = db.fetchRecords(sql, myMeetupId);
            
            if (!details.isEmpty()) {
                Map<String, Object> d = details.get(0);
                meetupLocationField.setText(d.get("location") != null ? d.get("location").toString() : "");
                meetupDateField.setText(d.get("date") != null ? d.get("date").toString() : "");
                meetupTimeField.setText(d.get("time") != null ? d.get("time").toString() : "");
                contactPersonField.setText(d.get("contact_person") != null ? d.get("contact_person").toString() : "");
                contactNumberField.setText(d.get("contact_number") != null ? d.get("contact_number").toString() : "");
                meetupInstructionsArea.setText(d.get("instructions") != null ? d.get("instructions").toString() : "");
                googleMapsLinkField.setText(d.get("google_maps_link") != null ? d.get("google_maps_link").toString() : "");
            }
        }
    }
    
    private void loadDeliveryDetails() {
        if (myDeliveryId != -1) {
            String sql = "SELECT * FROM tbl_delivery_details WHERE delivery_id = ?";
            List<Map<String, Object>> details = db.fetchRecords(sql, myDeliveryId);
            
            if (!details.isEmpty()) {
                Map<String, Object> d = details.get(0);
                deliveryAddressField.setText(d.get("address") != null ? d.get("address").toString() : "");
                courierField.setText(d.get("courier") != null ? d.get("courier").toString() : "");
                expectedDateField.setText(d.get("expected_date") != null ? d.get("expected_date").toString() : "");
                trackingField.setText(d.get("tracking_number") != null ? d.get("tracking_number").toString() : "");
                deliveryInstructionsArea.setText(d.get("instructions") != null ? d.get("instructions").toString() : "");
            }
        }
    }
    
    private void buildMeetupPanel(JPanel contentPanel) {
        int y = 20;
        int labelWidth = 120;
        int fieldWidth = 360;
        int fieldX = 140;
        
        meetupLocationField = new JTextField();
        meetupLocationField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel locLabel = createLabel("Meetup Location:*", 10, y, labelWidth);
        contentPanel.add(locLabel);
        meetupLocationField = addTextField(contentPanel, meetupLocationField, fieldX, y, fieldWidth);
        y += 40;
        
        googleMapsLinkField = new JTextField();
        googleMapsLinkField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel mapsLabel = createLabel("Google Maps Link:", 10, y, labelWidth);
        contentPanel.add(mapsLabel);
        googleMapsLinkField = addTextField(contentPanel, googleMapsLinkField, fieldX, y, fieldWidth);
        y += 40;
        
        meetupDateField = new JTextField();
        meetupDateField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel dateLabel = createLabel("Date (YYYY-MM-DD):*", 10, y, labelWidth);
        contentPanel.add(dateLabel);
        meetupDateField = addTextField(contentPanel, meetupDateField, fieldX, y, fieldWidth);
        y += 40;
        
        meetupTimeField = new JTextField();
        meetupTimeField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel timeLabel = createLabel("Time (HH:MM):*", 10, y, labelWidth);
        contentPanel.add(timeLabel);
        meetupTimeField = addTextField(contentPanel, meetupTimeField, fieldX, y, fieldWidth);
        y += 40;
        
        contactPersonField = new JTextField();
        contactPersonField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel personLabel = createLabel("Contact Person:*", 10, y, labelWidth);
        contentPanel.add(personLabel);
        contactPersonField = addTextField(contentPanel, contactPersonField, fieldX, y, fieldWidth);
        y += 40;
        
        contactNumberField = new JTextField();
        contactNumberField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel numberLabel = createLabel("Contact Number:*", 10, y, labelWidth);
        contentPanel.add(numberLabel);
        contactNumberField = addTextField(contentPanel, contactNumberField, fieldX, y, fieldWidth);
        y += 40;
        
        meetupInstructionsArea = new JTextArea();
        meetupInstructionsArea.setLineWrap(true);
        meetupInstructionsArea.setWrapStyleWord(true);
        meetupInstructionsArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel instLabel = createLabel("Special Instructions:", 10, y, labelWidth);
        contentPanel.add(instLabel);
        JScrollPane instScroll = new JScrollPane(meetupInstructionsArea);
        instScroll.setBounds(fieldX, y, fieldWidth, 80);
        instScroll.setBorder(new LineBorder(new Color(200, 200, 200)));
        contentPanel.add(instScroll);
        y += 90;
        
        JButton saveButton = createSaveButton("SAVE DETAILS", 180, y);
        saveButton.addActionListener(e -> saveMeetupDetails());
        contentPanel.add(saveButton);
    }
    
    private void buildDeliveryPanel(JPanel contentPanel) {
        int y = 20;
        int labelWidth = 120;
        int fieldWidth = 360;
        int fieldX = 140;
        
        deliveryAddressField = new JTextField();
        deliveryAddressField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel addrLabel = createLabel("Delivery Address:*", 10, y, labelWidth);
        contentPanel.add(addrLabel);
        deliveryAddressField = addTextField(contentPanel, deliveryAddressField, fieldX, y, fieldWidth);
        y += 40;
        
        courierField = new JTextField();
        courierField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel courierLabel = createLabel("Courier Service:*", 10, y, labelWidth);
        contentPanel.add(courierLabel);
        courierField = addTextField(contentPanel, courierField, fieldX, y, fieldWidth);
        y += 40;
        
        expectedDateField = new JTextField();
        expectedDateField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel dateLabel = createLabel("Expected Date:*", 10, y, labelWidth);
        contentPanel.add(dateLabel);
        expectedDateField = addTextField(contentPanel, expectedDateField, fieldX, y, fieldWidth);
        y += 40;
        
        trackingField = new JTextField();
        trackingField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel trackLabel = createLabel("Tracking Number:", 10, y, labelWidth);
        contentPanel.add(trackLabel);
        trackingField = addTextField(contentPanel, trackingField, fieldX, y, fieldWidth);
        y += 40;
        
        deliveryInstructionsArea = new JTextArea();
        deliveryInstructionsArea.setLineWrap(true);
        deliveryInstructionsArea.setWrapStyleWord(true);
        deliveryInstructionsArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel instLabel = createLabel("Special Instructions:", 10, y, labelWidth);
        contentPanel.add(instLabel);
        JScrollPane instScroll = new JScrollPane(deliveryInstructionsArea);
        instScroll.setBounds(fieldX, y, fieldWidth, 80);
        instScroll.setBorder(new LineBorder(new Color(200, 200, 200)));
        contentPanel.add(instScroll);
        y += 90;
        
        JButton saveButton = createSaveButton("SAVE DETAILS", 180, y);
        saveButton.addActionListener(e -> saveDeliveryDetails());
        contentPanel.add(saveButton);
    }
    
    private JLabel createLabel(String text, int x, int y, int width) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setBounds(x, y, width, 25);
        return label;
    }
    
    private JTextField addTextField(JPanel panel, JTextField field, int x, int y, int width) {
        field.setBounds(x, y, width, 30);
        field.setBorder(new LineBorder(new Color(200, 200, 200)));
        panel.add(field);
        return field;
    }
    
    private JButton createSaveButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(successColor);
        button.setForeground(Color.WHITE);
        button.setBounds(x, y, 150, 35);
        button.setBorder(null);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void saveMeetupDetails() {
        try {
            // Validate required fields
            if (meetupLocationField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(detailsDialog, "Please enter meetup location.", "Incomplete", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (meetupDateField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(detailsDialog, "Please enter meetup date.", "Incomplete", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (meetupTimeField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(detailsDialog, "Please enter meetup time.", "Incomplete", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (contactPersonField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(detailsDialog, "Please enter contact person.", "Incomplete", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (contactNumberField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(detailsDialog, "Please enter contact number.", "Incomplete", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String location = meetupLocationField.getText().trim();
            String date = meetupDateField.getText().trim();
            String time = meetupTimeField.getText().trim();
            String contactPerson = contactPersonField.getText().trim();
            String contactNumber = contactNumberField.getText().trim();
            String instructions = meetupInstructionsArea.getText().trim();
            String mapsLink = googleMapsLinkField.getText().trim();
            
            // Check if we have an existing meetup record
            if (myMeetupId == -1) {
                String checkSql = "SELECT meetup_id FROM tbl_meetup_details WHERE trade_id = ? AND trader_id = ?";
                List<Map<String, Object>> existing = db.fetchRecords(checkSql, tradeId, traderId);
                if (!existing.isEmpty()) {
                    myMeetupId = Integer.parseInt(existing.get(0).get("meetup_id").toString());
                }
            }
            
            if (myMeetupId == -1) {
                // Insert new record - using direct SQL with proper null handling
                String insertMeetup = "INSERT INTO tbl_meetup_details (trade_id, trader_id, location, date, time, "
                        + "contact_person, contact_number, instructions, google_maps_link) VALUES ("
                        + tradeId + ", " + traderId + ", '" + escapeString(location) + "', '" 
                        + escapeString(date) + "', '" + escapeString(time) + "', '" 
                        + escapeString(contactPerson) + "', '" + escapeString(contactNumber) + "', "
                        + (instructions.isEmpty() ? "NULL" : "'" + escapeString(instructions) + "'") + ", "
                        + (mapsLink.isEmpty() ? "NULL" : "'" + escapeString(mapsLink) + "'") + ")";
                
                db.updateRecord(insertMeetup);
                
                // Get the generated ID
                String getIdSql = "SELECT last_insert_rowid() as id";
                List<Map<String, Object>> result = db.fetchRecords(getIdSql);
                if (!result.isEmpty()) {
                    myMeetupId = Integer.parseInt(result.get(0).get("id").toString());
                }
            } else {
                // Update existing record
                String updateMeetup = "UPDATE tbl_meetup_details SET location = '" + escapeString(location) 
                        + "', date = '" + escapeString(date) + "', time = '" + escapeString(time) 
                        + "', contact_person = '" + escapeString(contactPerson) + "', contact_number = '" 
                        + escapeString(contactNumber) + "', instructions = " 
                        + (instructions.isEmpty() ? "NULL" : "'" + escapeString(instructions) + "'") 
                        + ", google_maps_link = " + (mapsLink.isEmpty() ? "NULL" : "'" + escapeString(mapsLink) + "'") 
                        + " WHERE meetup_id = " + myMeetupId;
                
                db.updateRecord(updateMeetup);
            }
            
            saveOrUpdateTradeDetails(myMeetupId, -1);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(detailsDialog, "Error saving details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void saveDeliveryDetails() {
        try {
            // Validate required fields
            if (deliveryAddressField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(detailsDialog, "Please enter delivery address.", "Incomplete", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (courierField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(detailsDialog, "Please enter courier service.", "Incomplete", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (expectedDateField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(detailsDialog, "Please enter expected delivery date.", "Incomplete", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String address = deliveryAddressField.getText().trim();
            String courier = courierField.getText().trim();
            String expectedDate = expectedDateField.getText().trim();
            String tracking = trackingField.getText().trim();
            String instructions = deliveryInstructionsArea.getText().trim();
            
            // Check if we have an existing delivery record
            if (myDeliveryId == -1) {
                String checkSql = "SELECT delivery_id FROM tbl_delivery_details WHERE trade_id = ? AND trader_id = ?";
                List<Map<String, Object>> existing = db.fetchRecords(checkSql, tradeId, traderId);
                if (!existing.isEmpty()) {
                    myDeliveryId = Integer.parseInt(existing.get(0).get("delivery_id").toString());
                }
            }
            
            if (myDeliveryId == -1) {
                // Insert new record
                String insertDelivery = "INSERT INTO tbl_delivery_details (trade_id, trader_id, address, courier, "
                        + "expected_date, tracking_number, instructions) VALUES ("
                        + tradeId + ", " + traderId + ", '" + escapeString(address) + "', '" 
                        + escapeString(courier) + "', '" + escapeString(expectedDate) + "', "
                        + (tracking.isEmpty() ? "NULL" : "'" + escapeString(tracking) + "'") + ", "
                        + (instructions.isEmpty() ? "NULL" : "'" + escapeString(instructions) + "'") + ")";
                
                db.updateRecord(insertDelivery);
                
                // Get the generated ID
                String getIdSql = "SELECT last_insert_rowid() as id";
                List<Map<String, Object>> result = db.fetchRecords(getIdSql);
                if (!result.isEmpty()) {
                    myDeliveryId = Integer.parseInt(result.get(0).get("id").toString());
                }
            } else {
                // Update existing record
                String updateDelivery = "UPDATE tbl_delivery_details SET address = '" + escapeString(address) 
                        + "', courier = '" + escapeString(courier) + "', expected_date = '" + escapeString(expectedDate) 
                        + "', tracking_number = " + (tracking.isEmpty() ? "NULL" : "'" + escapeString(tracking) + "'") 
                        + ", instructions = " + (instructions.isEmpty() ? "NULL" : "'" + escapeString(instructions) + "'") 
                        + " WHERE delivery_id = " + myDeliveryId;
                
                db.updateRecord(updateDelivery);
            }
            
            saveOrUpdateTradeDetails(-1, myDeliveryId);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(detailsDialog, "Error saving details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String escapeString(String input) {
        if (input == null) return "";
        return input.replace("'", "''");
    }
    
    private void saveOrUpdateTradeDetails(int meetupId, int deliveryId) {
        try {
            // First, check if a trade_details record already exists for this trade and trader
            if (myDetailId == -1) {
                String checkSql = "SELECT detail_id FROM tbl_trade_details WHERE trade_id = ? AND trader_id = ?";
                List<Map<String, Object>> existing = db.fetchRecords(checkSql, tradeId, traderId);
                if (!existing.isEmpty()) {
                    myDetailId = Integer.parseInt(existing.get(0).get("detail_id").toString());
                }
            }
            
            String meetupValue = (meetupId != -1) ? String.valueOf(meetupId) : "NULL";
            String deliveryValue = (deliveryId != -1) ? String.valueOf(deliveryId) : "NULL";
            
            if (myDetailId == -1) {
                // Insert new record
                String insertDetail = "INSERT INTO tbl_trade_details (trade_id, trader_id, exchange_method, meetup_id, delivery_id, my_details_submitted, created_date) "
                        + "VALUES (" + tradeId + ", " + traderId + ", '" + exchangeMethod + "', " 
                        + meetupValue + ", " + deliveryValue + ", 1, datetime('now'))";
                
                db.updateRecord(insertDetail);
                
                // Get the generated ID
                String getIdSql = "SELECT last_insert_rowid() as id";
                List<Map<String, Object>> result = db.fetchRecords(getIdSql);
                if (!result.isEmpty()) {
                    myDetailId = Integer.parseInt(result.get(0).get("id").toString());
                }
            } else {
                // Update existing record
                String updateDetail = "UPDATE tbl_trade_details SET meetup_id = " + meetupValue 
                        + ", delivery_id = " + deliveryValue 
                        + ", my_details_submitted = 1, updated_date = datetime('now') "
                        + "WHERE detail_id = " + myDetailId;
                
                db.updateRecord(updateDetail);
            }
            
            myDetailsSubmitted = true;
            
            JOptionPane.showMessageDialog(detailsDialog, "Details saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            detailsDialog.dispose();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(detailsDialog, "Error saving trade details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isDetailsSubmitted() {
        return myDetailsSubmitted;
    }
    
    public void refreshData() {
        loadExistingDetails();
    }
}