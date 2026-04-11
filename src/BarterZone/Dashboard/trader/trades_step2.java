package BarterZone.Dashboard.trader;

import database.config.config;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.util.List;
import java.util.Map;

public class trades_step2 {
    
    private int tradeId;
    private int traderId;
    private int otherTraderId;
    private String otherTraderName;
    private String exchangeMethod;
    private config db;
    private JFrame parent;
    private Runnable onStateChanged;
    private JButton proceedButton;
    
    private JPanel mainContainer;
    private JPanel myDetailsPanel;
    private JPanel otherDetailsPanel;
    
    private JTextArea myDetailsArea;
    private JTextArea otherDetailsArea;
    private JScrollPane myDetailsScroll;
    private JScrollPane otherDetailsScroll;
    
    private JLabel myDetailsStatusLabel;
    private JLabel otherDetailsStatusLabel;
    private JLabel myAgreementStatusLabel;
    private JLabel otherAgreementStatusLabel;
    private JLabel agreementStatusLabel;
    
    private JButton submitDetailsButton;
    private JButton editDetailsButton;
    private JButton agreeToOtherDetailsButton;
    
    // For current trader
    private int myMeetupId = -1;
    private int myDeliveryId = -1;
    private boolean myDetailsSubmitted = false;
    private boolean myAgreed = false;
    
    // For other trader
    private int otherMeetupId = -1;
    private int otherDeliveryId = -1;
    private boolean otherDetailsSubmitted = false;
    private boolean otherAgreed = false;
    
    private step2_submit step2Handler;
    
    // Professional colors
    private Color primaryColor = new Color(0, 102, 102);
    private Color successColor = new Color(46, 125, 50);
    private Color warningColor = new Color(255, 153, 0);
    private Color errorColor = new Color(204, 0, 0);
    private Color infoColor = new Color(33, 150, 243);
    private Color borderColor = new Color(200, 200, 200);
    private Color bgColor = new Color(250, 250, 250);
    private Color textColor = new Color(80, 80, 80);
    
    public trades_step2(int tradeId, int traderId, int otherTraderId, String otherTraderName, String exchangeMethod,
                        config db, JFrame parent, Runnable onStateChanged, JButton proceedButton) {
        this.tradeId = tradeId;
        this.traderId = traderId;
        this.otherTraderId = otherTraderId;
        this.otherTraderName = otherTraderName;
        this.exchangeMethod = exchangeMethod;
        this.db = db;
        this.parent = parent;
        this.onStateChanged = onStateChanged;
        this.proceedButton = proceedButton;
        
        initComponents();
    }
    
    private void initComponents() {
        // My Details Panel
        myDetailsPanel = new JPanel();
        myDetailsPanel.setLayout(null);
        myDetailsPanel.setBackground(Color.WHITE);
        myDetailsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(primaryColor, 2), 
            "MY EXCHANGE DETAILS", 
            TitledBorder.LEFT, TitledBorder.TOP, 
            new Font("Segoe UI", Font.BOLD, 14), primaryColor));
        
        // Other Details Panel
        otherDetailsPanel = new JPanel();
        otherDetailsPanel.setLayout(null);
        otherDetailsPanel.setBackground(Color.WHITE);
        otherDetailsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(primaryColor, 2), 
            otherTraderName.toUpperCase() + "'S EXCHANGE DETAILS", 
            TitledBorder.LEFT, TitledBorder.TOP, 
            new Font("Segoe UI", Font.BOLD, 14), primaryColor));
        
        // Text areas for displaying details
        myDetailsArea = new JTextArea();
        myDetailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        myDetailsArea.setEditable(false);
        myDetailsArea.setLineWrap(true);
        myDetailsArea.setWrapStyleWord(true);
        myDetailsArea.setBackground(bgColor);
        myDetailsArea.setForeground(textColor);
        myDetailsScroll = new JScrollPane(myDetailsArea);
        myDetailsScroll.setBorder(new LineBorder(borderColor, 1));
        
        otherDetailsArea = new JTextArea();
        otherDetailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        otherDetailsArea.setEditable(false);
        otherDetailsArea.setLineWrap(true);
        otherDetailsArea.setWrapStyleWord(true);
        otherDetailsArea.setBackground(bgColor);
        otherDetailsArea.setForeground(textColor);
        otherDetailsScroll = new JScrollPane(otherDetailsArea);
        otherDetailsScroll.setBorder(new LineBorder(borderColor, 1));
        
        // Status labels
        myDetailsStatusLabel = new JLabel();
        myDetailsStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        otherDetailsStatusLabel = new JLabel();
        otherDetailsStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        myAgreementStatusLabel = new JLabel();
        myAgreementStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        otherAgreementStatusLabel = new JLabel();
        otherAgreementStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        agreementStatusLabel = new JLabel();
        agreementStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        agreementStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Buttons
        submitDetailsButton = new JButton("SUBMIT MY DETAILS");
        submitDetailsButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        submitDetailsButton.setBackground(primaryColor);
        submitDetailsButton.setForeground(Color.WHITE);
        submitDetailsButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        submitDetailsButton.setFocusPainted(false);
        submitDetailsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitDetailsButton.addActionListener(e -> showDetailsInputDialog());
        
        editDetailsButton = new JButton("EDIT MY DETAILS");
        editDetailsButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        editDetailsButton.setBackground(infoColor);
        editDetailsButton.setForeground(Color.WHITE);
        editDetailsButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        editDetailsButton.setFocusPainted(false);
        editDetailsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editDetailsButton.setVisible(false);
        editDetailsButton.addActionListener(e -> editDetails());
        
        agreeToOtherDetailsButton = new JButton("AGREE TO DETAILS");
        agreeToOtherDetailsButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        agreeToOtherDetailsButton.setBackground(successColor);
        agreeToOtherDetailsButton.setForeground(Color.WHITE);
        agreeToOtherDetailsButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        agreeToOtherDetailsButton.setFocusPainted(false);
        agreeToOtherDetailsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        agreeToOtherDetailsButton.setEnabled(false);
        agreeToOtherDetailsButton.addActionListener(e -> agreeToOtherDetails());
    }
    
    public void loadState(boolean myDetailsSubmitted, boolean myAgreed, int myMeetupId, int myDeliveryId) {
        this.myDetailsSubmitted = myDetailsSubmitted;
        this.myAgreed = myAgreed;
        this.myMeetupId = myMeetupId;
        this.myDeliveryId = myDeliveryId;
        
        // Load current trader's own details
        loadMyOwnDetails();
        
        // Load other trader's details
        loadOtherTradersDetails();
    }
    
    // Load the current logged-in trader's own details
    private void loadMyOwnDetails() {
        if (myDetailsSubmitted) {
            myDetailsStatusLabel.setText("[OK] Your details have been submitted");
            myDetailsStatusLabel.setForeground(successColor);
            
            // Load and display current trader's submitted details
            boolean detailsLoaded = false;
            
            if (exchangeMethod != null && exchangeMethod.equals("meetup") && myMeetupId != -1) {
                detailsLoaded = displayMyMeetupDetails(myMeetupId);
                if (!detailsLoaded) {
                    myDetailsArea.setText("Your meetup details were submitted but cannot be displayed. Please contact support.");
                }
            } else if (exchangeMethod != null && exchangeMethod.equals("delivery") && myDeliveryId != -1) {
                detailsLoaded = displayMyDeliveryDetails(myDeliveryId);
                if (!detailsLoaded) {
                    myDetailsArea.setText("Your delivery details were submitted but cannot be displayed. Please contact support.");
                }
            } else if (exchangeMethod != null && exchangeMethod.equals("meetup")) {
                myDetailsArea.setText("Your meetup details have been submitted but the record could not be found.");
            } else if (exchangeMethod != null && exchangeMethod.equals("delivery")) {
                myDetailsArea.setText("Your delivery details have been submitted but the record could not be found.");
            } else {
                myDetailsArea.setText("Your details have been submitted but exchange method is not set.");
            }
            
            // Check if edit button should be shown
            boolean canEdit = (!myAgreed) || (myAgreed && !otherAgreed);
            editDetailsButton.setVisible(canEdit);
            submitDetailsButton.setVisible(false);
            
        } else {
            myDetailsStatusLabel.setText("[!] Your details have NOT been submitted yet");
            myDetailsStatusLabel.setForeground(warningColor);
            
            if (exchangeMethod != null && exchangeMethod.equals("meetup")) {
                myDetailsArea.setText("Click SUBMIT MY DETAILS to provide your meetup information.\n\nRequired information:\n- Meetup location\n- Date and time\n- Contact person and number");
            } else if (exchangeMethod != null && exchangeMethod.equals("delivery")) {
                myDetailsArea.setText("Click SUBMIT MY DETAILS to provide your delivery information.\n\nRequired information:\n- Delivery address\n- Courier service\n- Expected delivery date");
            } else {
                myDetailsArea.setText("Exchange method not set. Please complete Step 1 first.");
            }
            
            editDetailsButton.setVisible(false);
            submitDetailsButton.setVisible(true);
        }
        
        // Display current trader's agreement status
        if (myAgreed) {
            myAgreementStatusLabel.setText("[OK] You have agreed to " + otherTraderName + "'s details");
            myAgreementStatusLabel.setForeground(successColor);
        } else {
            myAgreementStatusLabel.setText("[ ] You have NOT agreed yet");
            myAgreementStatusLabel.setForeground(textColor);
        }
    }
    
    private boolean displayMyMeetupDetails(int meetupId) {
        String sql = "SELECT * FROM tbl_meetup_details WHERE meetup_id = ?";
        List<Map<String, Object>> details = db.fetchRecords(sql, meetupId);
        
        if (!details.isEmpty()) {
            Map<String, Object> d = details.get(0);
            StringBuilder sb = new StringBuilder();
            sb.append("EXCHANGE METHOD: MEETUP\n");
            sb.append("----------------------------------------\n\n");
            sb.append("LOCATION:\n");
            sb.append("   ").append(d.get("location") != null ? d.get("location") : "Not provided").append("\n\n");
            
            if (d.get("google_maps_link") != null && !d.get("google_maps_link").toString().isEmpty()) {
                sb.append("GOOGLE MAPS LINK:\n");
                sb.append("   ").append(d.get("google_maps_link")).append("\n\n");
            }
            
            sb.append("DATE:\n");
            sb.append("   ").append(d.get("date") != null ? d.get("date") : "Not provided").append("\n\n");
            
            sb.append("TIME:\n");
            sb.append("   ").append(d.get("time") != null ? d.get("time") : "Not provided").append("\n\n");
            
            sb.append("CONTACT PERSON:\n");
            sb.append("   ").append(d.get("contact_person") != null ? d.get("contact_person") : "Not provided").append("\n\n");
            
            sb.append("CONTACT NUMBER:\n");
            sb.append("   ").append(d.get("contact_number") != null ? d.get("contact_number") : "Not provided").append("\n\n");
            
            if (d.get("instructions") != null && !d.get("instructions").toString().isEmpty()) {
                sb.append("SPECIAL INSTRUCTIONS:\n");
                sb.append("   ").append(d.get("instructions"));
            }
            
            myDetailsArea.setText(sb.toString());
            myDetailsArea.setCaretPosition(0);
            return true;
        }
        return false;
    }
    
    private boolean displayMyDeliveryDetails(int deliveryId) {
        String sql = "SELECT * FROM tbl_delivery_details WHERE delivery_id = ?";
        List<Map<String, Object>> details = db.fetchRecords(sql, deliveryId);
        
        if (!details.isEmpty()) {
            Map<String, Object> d = details.get(0);
            StringBuilder sb = new StringBuilder();
            sb.append("EXCHANGE METHOD: DELIVERY\n");
            sb.append("----------------------------------------\n\n");
            sb.append("DELIVERY ADDRESS:\n");
            sb.append("   ").append(d.get("address") != null ? d.get("address") : "Not provided").append("\n\n");
            
            sb.append("COURIER SERVICE:\n");
            sb.append("   ").append(d.get("courier") != null ? d.get("courier") : "Not provided").append("\n\n");
            
            sb.append("EXPECTED DATE:\n");
            sb.append("   ").append(d.get("expected_date") != null ? d.get("expected_date") : "Not provided").append("\n\n");
            
            if (d.get("tracking_number") != null && !d.get("tracking_number").toString().isEmpty()) {
                sb.append("TRACKING NUMBER:\n");
                sb.append("   ").append(d.get("tracking_number")).append("\n\n");
            }
            
            if (d.get("instructions") != null && !d.get("instructions").toString().isEmpty()) {
                sb.append("SPECIAL INSTRUCTIONS:\n");
                sb.append("   ").append(d.get("instructions"));
            }
            
            myDetailsArea.setText(sb.toString());
            myDetailsArea.setCaretPosition(0);
            return true;
        }
        return false;
    }
    
    // Load the other trader's details
    private void loadOtherTradersDetails() {
        // Query the other trader's row from tbl_trade_details
        String sql = "SELECT * FROM tbl_trade_details WHERE trade_id = ? AND trader_id = ?";
        List<Map<String, Object>> otherTraderRecord = db.fetchRecords(sql, tradeId, otherTraderId);
        
        if (!otherTraderRecord.isEmpty()) {
            Map<String, Object> other = otherTraderRecord.get(0);
            
            // Check if other trader has submitted their details (my_details_submitted in THEIR row)
            boolean otherHasSubmitted = other.get("my_details_submitted") != null && 
                ((Number) other.get("my_details_submitted")).intValue() == 1;
            
            otherDetailsSubmitted = otherHasSubmitted;
            
            if (otherHasSubmitted) {
                otherDetailsStatusLabel.setText("[OK] " + otherTraderName + " has submitted details");
                otherDetailsStatusLabel.setForeground(successColor);
                
                // Load the other trader's meetup or delivery details
                boolean otherDetailsLoaded = false;
                
                if (other.get("meetup_id") != null && ((Number) other.get("meetup_id")).intValue() > 0) {
                    otherMeetupId = ((Number) other.get("meetup_id")).intValue();
                    otherDetailsLoaded = displayOtherMeetupDetails(otherMeetupId);
                } else if (other.get("delivery_id") != null && ((Number) other.get("delivery_id")).intValue() > 0) {
                    otherDeliveryId = ((Number) other.get("delivery_id")).intValue();
                    otherDetailsLoaded = displayOtherDeliveryDetails(otherDeliveryId);
                }
                
                if (!otherDetailsLoaded) {
                    otherDetailsArea.setText("The other trader's details could not be loaded.");
                }
                
                // Get other trader's agreement status
                boolean otherHasAgreed = other.get("my_agreed") != null && 
                    ((Number) other.get("my_agreed")).intValue() == 1;
                this.otherAgreed = otherHasAgreed;
                
                // Agree button appears ONLY if other trader has submitted AND current trader hasn't agreed yet
                boolean showAgreeButton = otherHasSubmitted && (!myAgreed);
                
                if (showAgreeButton) {
                    String buttonText = (exchangeMethod != null && exchangeMethod.equals("meetup")) ? 
                        "AGREE TO THESE DETAILS" : 
                        "AGREE TO THESE DETAILS";
                    agreeToOtherDetailsButton.setText(buttonText);
                    agreeToOtherDetailsButton.setEnabled(true);
                } else {
                    agreeToOtherDetailsButton.setEnabled(false);
                }
                
                if (otherHasAgreed) {
                    otherAgreementStatusLabel.setText("[OK] " + otherTraderName + " has agreed to your details");
                    otherAgreementStatusLabel.setForeground(successColor);
                } else if (myAgreed && exchangeMethod != null && exchangeMethod.equals("meetup")) {
                    otherAgreementStatusLabel.setText("[OK] You have agreed to use their meetup details");
                    otherAgreementStatusLabel.setForeground(successColor);
                } else {
                    otherAgreementStatusLabel.setText("[ ] " + otherTraderName + " has not agreed yet");
                    otherAgreementStatusLabel.setForeground(textColor);
                }
                
            } else {
                otherDetailsStatusLabel.setText("[!] Waiting for " + otherTraderName + " to submit details");
                otherDetailsStatusLabel.setForeground(warningColor);
                
                if (exchangeMethod != null && exchangeMethod.equals("meetup")) {
                    otherDetailsArea.setText("No meetup details submitted yet by " + otherTraderName + ".\n\nPlease wait for them to provide their meetup information.");
                } else {
                    otherDetailsArea.setText("No delivery details submitted yet by " + otherTraderName + ".\n\nPlease wait for them to provide their delivery information.");
                }
                agreeToOtherDetailsButton.setEnabled(false);
                otherAgreementStatusLabel.setText("");
            }
        } else {
            otherDetailsStatusLabel.setText("[!] Waiting for " + otherTraderName + " to submit details");
            otherDetailsStatusLabel.setForeground(warningColor);
            
            if (exchangeMethod != null && exchangeMethod.equals("meetup")) {
                otherDetailsArea.setText("No meetup details submitted yet by " + otherTraderName + ".\n\nPlease wait for them to provide their meetup information.");
            } else {
                otherDetailsArea.setText("No delivery details submitted yet by " + otherTraderName + ".\n\nPlease wait for them to provide their delivery information.");
            }
            agreeToOtherDetailsButton.setEnabled(false);
            otherAgreementStatusLabel.setText("");
        }
    }
    
    private boolean displayOtherMeetupDetails(int meetupId) {
        String sql = "SELECT * FROM tbl_meetup_details WHERE meetup_id = ?";
        List<Map<String, Object>> details = db.fetchRecords(sql, meetupId);
        
        if (!details.isEmpty()) {
            Map<String, Object> d = details.get(0);
            StringBuilder sb = new StringBuilder();
            sb.append("EXCHANGE METHOD: MEETUP\n");
            sb.append("----------------------------------------\n\n");
            sb.append("LOCATION:\n");
            sb.append("   ").append(d.get("location") != null ? d.get("location") : "Not provided").append("\n\n");
            
            if (d.get("google_maps_link") != null && !d.get("google_maps_link").toString().isEmpty()) {
                sb.append("GOOGLE MAPS LINK:\n");
                sb.append("   ").append(d.get("google_maps_link")).append("\n\n");
            }
            
            sb.append("DATE:\n");
            sb.append("   ").append(d.get("date") != null ? d.get("date") : "Not provided").append("\n\n");
            
            sb.append("TIME:\n");
            sb.append("   ").append(d.get("time") != null ? d.get("time") : "Not provided").append("\n\n");
            
            sb.append("CONTACT PERSON:\n");
            sb.append("   ").append(d.get("contact_person") != null ? d.get("contact_person") : "Not provided").append("\n\n");
            
            sb.append("CONTACT NUMBER:\n");
            sb.append("   ").append(d.get("contact_number") != null ? d.get("contact_number") : "Not provided").append("\n\n");
            
            if (d.get("instructions") != null && !d.get("instructions").toString().isEmpty()) {
                sb.append("SPECIAL INSTRUCTIONS:\n");
                sb.append("   ").append(d.get("instructions"));
            }
            
            otherDetailsArea.setText(sb.toString());
            otherDetailsArea.setCaretPosition(0);
            return true;
        }
        return false;
    }
    
    private boolean displayOtherDeliveryDetails(int deliveryId) {
        String sql = "SELECT * FROM tbl_delivery_details WHERE delivery_id = ?";
        List<Map<String, Object>> details = db.fetchRecords(sql, deliveryId);
        
        if (!details.isEmpty()) {
            Map<String, Object> d = details.get(0);
            StringBuilder sb = new StringBuilder();
            sb.append("EXCHANGE METHOD: DELIVERY\n");
            sb.append("----------------------------------------\n\n");
            sb.append("DELIVERY ADDRESS:\n");
            sb.append("   ").append(d.get("address") != null ? d.get("address") : "Not provided").append("\n\n");
            
            sb.append("COURIER SERVICE:\n");
            sb.append("   ").append(d.get("courier") != null ? d.get("courier") : "Not provided").append("\n\n");
            
            sb.append("EXPECTED DATE:\n");
            sb.append("   ").append(d.get("expected_date") != null ? d.get("expected_date") : "Not provided").append("\n\n");
            
            if (d.get("tracking_number") != null && !d.get("tracking_number").toString().isEmpty()) {
                sb.append("TRACKING NUMBER:\n");
                sb.append("   ").append(d.get("tracking_number")).append("\n\n");
            }
            
            if (d.get("instructions") != null && !d.get("instructions").toString().isEmpty()) {
                sb.append("SPECIAL INSTRUCTIONS:\n");
                sb.append("   ").append(d.get("instructions"));
            }
            
            otherDetailsArea.setText(sb.toString());
            otherDetailsArea.setCaretPosition(0);
            return true;
        }
        return false;
    }
    
    public JPanel buildPanel() {
        mainContainer = new JPanel();
        mainContainer.setLayout(null);
        mainContainer.setBackground(new Color(245, 245, 250));
        mainContainer.setBounds(0, 0, 940, 650);
        
        int panelWidth = 440;
        int panelHeight = 520;
        int leftX = 25;
        int rightX = 475;
        int topY = 20;
        
        // My Details Panel
        myDetailsPanel.setBounds(leftX, topY, panelWidth, panelHeight);
        buildMyDetailsPanel();
        mainContainer.add(myDetailsPanel);
        
        // Other Details Panel
        otherDetailsPanel.setBounds(rightX, topY, panelWidth, panelHeight);
        buildOtherDetailsPanel();
        mainContainer.add(otherDetailsPanel);
        
        // Bottom status panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(null);
        bottomPanel.setBackground(new Color(245, 245, 250));
        bottomPanel.setBounds(25, 555, 890, 60);
        
        agreementStatusLabel.setBounds(10, 15, 870, 30);
        bottomPanel.add(agreementStatusLabel);
        
        mainContainer.add(bottomPanel);
        
        // Update agreement status based on current state
        updateAgreementStatus();
        
        return mainContainer;
    }
    
    private void buildMyDetailsPanel() {
        myDetailsPanel.removeAll();
        
        int y = 25;
        
        // Status label
        myDetailsStatusLabel.setBounds(15, y, 400, 25);
        myDetailsPanel.add(myDetailsStatusLabel);
        y += 35;
        
        // Details text area
        myDetailsScroll.setBounds(15, y, 410, 280);
        myDetailsPanel.add(myDetailsScroll);
        y += 295;
        
        // Submit button or Edit button
        if (submitDetailsButton.isVisible()) {
            submitDetailsButton.setBounds(130, y, 180, 40);
            myDetailsPanel.add(submitDetailsButton);
            y += 55;
        } else if (editDetailsButton.isVisible()) {
            editDetailsButton.setBounds(130, y, 180, 40);
            myDetailsPanel.add(editDetailsButton);
            y += 55;
        } else {
            JLabel submittedLabel = new JLabel("[OK] Details submitted successfully");
            submittedLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            submittedLabel.setForeground(successColor);
            submittedLabel.setBounds(120, y, 250, 30);
            myDetailsPanel.add(submittedLabel);
            y += 45;
        }
        
        // Agreement status
        JLabel agreementTitle = new JLabel("Agreement Status:");
        agreementTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        agreementTitle.setForeground(primaryColor);
        agreementTitle.setBounds(15, y, 150, 25);
        myDetailsPanel.add(agreementTitle);
        y += 30;
        
        myAgreementStatusLabel.setBounds(15, y, 400, 25);
        myDetailsPanel.add(myAgreementStatusLabel);
    }
    
    private void buildOtherDetailsPanel() {
        otherDetailsPanel.removeAll();
        
        int y = 25;
        
        // Status label
        otherDetailsStatusLabel.setBounds(15, y, 400, 25);
        otherDetailsPanel.add(otherDetailsStatusLabel);
        y += 35;
        
        // Details text area
        otherDetailsScroll.setBounds(15, y, 410, 280);
        otherDetailsPanel.add(otherDetailsScroll);
        y += 295;
        
        // Agree button
        agreeToOtherDetailsButton.setBounds(120, y, 200, 40);
        otherDetailsPanel.add(agreeToOtherDetailsButton);
        y += 55;
        
        // Agreement status
        JLabel agreementTitle = new JLabel("Agreement Status:");
        agreementTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        agreementTitle.setForeground(primaryColor);
        agreementTitle.setBounds(15, y, 150, 25);
        otherDetailsPanel.add(agreementTitle);
        y += 30;
        
        otherAgreementStatusLabel.setBounds(15, y, 400, 25);
        otherDetailsPanel.add(otherAgreementStatusLabel);
    }
    
    private void updateAgreementStatus() {
        if (myDetailsSubmitted && otherDetailsSubmitted) {
            boolean canProceed = false;
            String agreementMessage = "";
            
            if (exchangeMethod != null && exchangeMethod.equals("delivery")) {
                // Delivery: BOTH must agree
                canProceed = (myAgreed && otherAgreed);
                if (myAgreed && otherAgreed) {
                    agreementMessage = "[OK] BOTH TRADERS HAVE AGREED! Click PROCEED to continue to payment.";
                    agreementStatusLabel.setForeground(successColor);
                } else if (myAgreed) {
                    agreementMessage = "[!] You have agreed. Waiting for " + otherTraderName + " to agree.";
                    agreementStatusLabel.setForeground(warningColor);
                } else if (otherAgreed) {
                    agreementMessage = "[!] " + otherTraderName + " has agreed. Click AGREE on your side to confirm.";
                    agreementStatusLabel.setForeground(warningColor);
                } else {
                    agreementMessage = "[!] Both traders have submitted details. Click AGREE to confirm the other trader's details.";
                    agreementStatusLabel.setForeground(warningColor);
                }
            } else if (exchangeMethod != null && exchangeMethod.equals("meetup")) {
                // Meetup: ONE trader needs to agree
                canProceed = (myAgreed || otherAgreed);
                if (myAgreed) {
                    agreementMessage = "[OK] You have agreed to " + otherTraderName + "'s meetup details! Click PROCEED to continue.";
                    agreementStatusLabel.setForeground(successColor);
                } else if (otherAgreed) {
                    agreementMessage = "[OK] " + otherTraderName + " has agreed to your meetup details! Click PROCEED to continue.";
                    agreementStatusLabel.setForeground(successColor);
                } else {
                    agreementMessage = "[!] Both traders have submitted details. Click AGREE to use the other trader's meetup details.";
                    agreementStatusLabel.setForeground(warningColor);
                }
            } else {
                agreementMessage = "[!] Exchange method not set. Please complete Step 1 first.";
                agreementStatusLabel.setForeground(errorColor);
                canProceed = false;
            }
            
            agreementStatusLabel.setText(agreementMessage);
            proceedButton.setEnabled(canProceed);
            
            if (canProceed) {
                proceedButton.setText("PROCEED TO PAYMENT");
            }
            
        } else if (myDetailsSubmitted) {
            agreementStatusLabel.setText("[!] You have submitted your details. Waiting for " + otherTraderName + " to submit.");
            agreementStatusLabel.setForeground(warningColor);
            proceedButton.setEnabled(false);
        } else if (otherDetailsSubmitted) {
            agreementStatusLabel.setText("[!] " + otherTraderName + " has submitted details. Please submit your details and then agree.");
            agreementStatusLabel.setForeground(warningColor);
            proceedButton.setEnabled(false);
        } else {
            agreementStatusLabel.setText("[!] Please fill in your details and click SUBMIT MY DETAILS.");
            agreementStatusLabel.setForeground(textColor);
            proceedButton.setEnabled(false);
        }
    }
    
    private void showDetailsInputDialog() {
        if (exchangeMethod == null) {
            JOptionPane.showMessageDialog(parent, "Exchange method not set. Please complete Step 1 first.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        step2Handler = new step2_submit(tradeId, traderId, otherTraderId, exchangeMethod, parent);
        step2Handler.showDialog();
        
        if (onStateChanged != null) onStateChanged.run();
    }
    
    private void editDetails() {
        if (exchangeMethod == null) {
            JOptionPane.showMessageDialog(parent, "Exchange method not set. Please complete Step 1 first.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Check if editing is allowed
        if (myAgreed && otherAgreed) {
            JOptionPane.showMessageDialog(parent, "Both traders have already agreed. Details cannot be edited.", "Cannot Edit", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (myAgreed && !otherAgreed) {
            JOptionPane.showMessageDialog(parent, "You have already agreed. If you edit your details, the agreement will be reset.\n\nThe other trader will need to agree again.", "Warning", JOptionPane.WARNING_MESSAGE);
            
            int confirm = JOptionPane.showConfirmDialog(parent,
                "Do you want to proceed with editing your details?\n\nThis will reset both traders' agreement status.",
                "Confirm Edit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            
            String resetMySql = "UPDATE tbl_trade_details SET my_agreed = 0 WHERE trade_id = ? AND trader_id = ?";
            db.updateRecord(resetMySql, tradeId, traderId);
            
            String resetOtherSql = "UPDATE tbl_trade_details SET my_agreed = 0 WHERE trade_id = ? AND trader_id = ?";
            db.updateRecord(resetOtherSql, tradeId, otherTraderId);
        }
        
        step2Handler = new step2_submit(tradeId, traderId, otherTraderId, exchangeMethod, parent);
        step2Handler.showDialog();
        
        if (onStateChanged != null) onStateChanged.run();
    }
    
    private void agreeToOtherDetails() {
        int confirm = JOptionPane.showConfirmDialog(parent,
            "Confirm that you agree with " + otherTraderName + "'s exchange details?\n\n" +
            "This means you have reviewed and confirmed that their exchange details are acceptable.\n\n" +
            ((exchangeMethod != null && exchangeMethod.equals("delivery")) ? 
                "Both traders must agree to proceed to payment." : 
                "Once you agree, the trade will proceed to payment using their meetup details."),
            "Confirm Agreement",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Update current trader's my_agreed = 1
            String sql = "UPDATE tbl_trade_details SET my_agreed = 1 WHERE trade_id = ? AND trader_id = ?";
            db.updateRecord(sql, tradeId, traderId);
            
            if (exchangeMethod != null && exchangeMethod.equals("delivery")) {
                // For delivery, check if both have agreed
                String checkMySql = "SELECT my_agreed FROM tbl_trade_details WHERE trade_id = ? AND trader_id = ?";
                List<Map<String, Object>> myResult = db.fetchRecords(checkMySql, tradeId, traderId);
                boolean myAgreedVal = ((Number) myResult.get(0).get("my_agreed")).intValue() == 1;
                
                String checkOtherSql = "SELECT my_agreed FROM tbl_trade_details WHERE trade_id = ? AND trader_id = ?";
                List<Map<String, Object>> otherResult = db.fetchRecords(checkOtherSql, tradeId, otherTraderId);
                boolean otherAgreedVal = ((Number) otherResult.get(0).get("my_agreed")).intValue() == 1;
                
                if (myAgreedVal && otherAgreedVal) {
                    String updateTradeSql = "UPDATE tbl_trade SET trade_status = 'arrangements_confirmed' WHERE trade_id = ?";
                    db.updateRecord(updateTradeSql, tradeId);
                    JOptionPane.showMessageDialog(parent,
                        "Both traders have agreed! You can now proceed to payment.",
                        "Agreement Complete", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(parent,
                        "You have agreed to " + otherTraderName + "'s details.\n\nWaiting for " + otherTraderName + " to agree as well.",
                        "Agreement Recorded", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                String updateTradeSql = "UPDATE tbl_trade SET trade_status = 'arrangements_confirmed' WHERE trade_id = ?";
                db.updateRecord(updateTradeSql, tradeId);
                JOptionPane.showMessageDialog(parent,
                    "You have agreed to " + otherTraderName + "'s meetup details!\n\nThe trade will now proceed to payment using their meetup information.",
                    "Agreement Complete - Proceeding to Payment", JOptionPane.INFORMATION_MESSAGE);
            }
            
            if (onStateChanged != null) onStateChanged.run();
        }
    }
}