package BarterZone.Dashboard.trader;

import database.config.config;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import java.awt.FlowLayout;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.util.List;
import java.util.Map;

public class trades_step6 {
    
    private JFrame parent;
    private Runnable onClose;
    private config db;
    private int tradeId;
    private int traderId;
    private String traderName;
    
    private JRadioButton[] starRadios;
    private ButtonGroup starGroup;
    private JTextArea feedbackArea;
    private JButton submitRatingButton;
    private JLabel ratingStatusLabel;
    
    private JTextArea suggestionArea;
    private JButton submitSuggestionButton;
    private JLabel suggestionStatusLabel;
    
    private Color themeColor = new Color(12, 192, 223);
    private Color successColor = new Color(46, 125, 50);
    private Color reminderColor = new Color(255, 153, 0);
    private Color infoColor = new Color(33, 150, 243);
    private Color textColor = new Color(80, 80, 80);
    private Color bgColor = new Color(250, 250, 250);
    private Color statusTextColor = new Color(33, 150, 243);
    
    public trades_step6(JFrame parent, Runnable onClose) {
        this.parent = parent;
        this.onClose = onClose;
        this.db = new config();
        
        BarterZone.Dashboard.session.user_session session = BarterZone.Dashboard.session.user_session.getInstance();
        if (session.isLoggedIn()) {
            this.traderId = session.getUserId();
            this.traderName = session.getFullName();
        }
    }
    
    public void setTradeInfo(int tradeId) {
        this.tradeId = tradeId;
    }
    
    public JPanel buildPanel(JButton proceedButton, JButton backStepButton, JButton cancelTradeButton) {
        JPanel container = new JPanel();
        container.setLayout(null);
        container.setBackground(bgColor);
        container.setBounds(0, 0, 940, 800);
        
        int y = 20;
        
        JPanel thankYouPanel = new JPanel();
        thankYouPanel.setLayout(null);
        thankYouPanel.setBackground(Color.WHITE);
        thankYouPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(successColor, 2),
            "TRADE COMPLETED",
            TitledBorder.CENTER, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 18), successColor));
        thankYouPanel.setBounds(20, y, 900, 140);
        container.add(thankYouPanel);
        
        JLabel mainThanksLabel = new JLabel("Thank you for using BarterZone!");
        mainThanksLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        mainThanksLabel.setForeground(successColor);
        mainThanksLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainThanksLabel.setBounds(20, 25, 860, 35);
        thankYouPanel.add(mainThanksLabel);
        
        JLabel subMessage1 = new JLabel("We appreciate your trust in our platform.");
        subMessage1.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subMessage1.setForeground(textColor);
        subMessage1.setHorizontalAlignment(SwingConstants.CENTER);
        subMessage1.setBounds(20, 65, 860, 25);
        thankYouPanel.add(subMessage1);
        
        JLabel subMessage2 = new JLabel("Your successful trade helps build a better community.");
        subMessage2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subMessage2.setForeground(textColor);
        subMessage2.setHorizontalAlignment(SwingConstants.CENTER);
        subMessage2.setBounds(20, 95, 860, 25);
        thankYouPanel.add(subMessage2);
        
        y += 160;
        
        JPanel remindersPanel = new JPanel();
        remindersPanel.setLayout(null);
        remindersPanel.setBackground(Color.WHITE);
        remindersPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(reminderColor, 2),
            "IMPORTANT REMINDERS",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), reminderColor));
        remindersPanel.setBounds(20, y, 900, 150);
        container.add(remindersPanel);
        
        String[] reminders = {
            "Always be aware of scammers and suspicious activities",
            "Take care when dealing with transactions and payments",
            "Verify all details before confirming any action",
            "Stay safe and trade responsibly at all times",
            "Be happy and keep working hard toward your goals"
        };
        
        int remY = 25;
        for (int i = 0; i < reminders.length; i++) {
            JLabel reminderLabel = new JLabel(reminders[i]);
            reminderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            reminderLabel.setForeground(textColor);
            reminderLabel.setBounds(25, remY, 860, 25);
            remindersPanel.add(reminderLabel);
            remY += 25;
        }
        
        y += 170;
        
        int labelY = 25;
        int textAreaY = 110;
        int buttonY = 225;
        int statusY = 275;
        
        JPanel ratingPanel = new JPanel();
        ratingPanel.setLayout(null);
        ratingPanel.setBackground(Color.WHITE);
        ratingPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(themeColor, 2),
            "RATE THIS APPLICATION",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), themeColor));
        ratingPanel.setBounds(20, y, 440, 360);
        container.add(ratingPanel);
        
        JLabel starsLabel = new JLabel("Select Your Rating:");
        starsLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        starsLabel.setBounds(20, labelY, 150, 30);
        ratingPanel.add(starsLabel);
        
        JPanel starsPanel = new JPanel();
        starsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        starsPanel.setBackground(Color.WHITE);
        starsPanel.setBounds(20, labelY + 35, 400, 40);
        ratingPanel.add(starsPanel);
        
        starRadios = new JRadioButton[5];
        starGroup = new ButtonGroup();
        String[] starTexts = {"1 Star", "2 Stars", "3 Stars", "4 Stars", "5 Stars"};
        
        for (int i = 0; i < 5; i++) {
            starRadios[i] = new JRadioButton(starTexts[i]);
            starRadios[i].setFont(new Font("Segoe UI", Font.PLAIN, 12));
            starRadios[i].setBackground(Color.WHITE);
            starRadios[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            starGroup.add(starRadios[i]);
            starsPanel.add(starRadios[i]);
        }
        
        JLabel feedbackLabel = new JLabel("Your Feedback (Optional):");
        feedbackLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        feedbackLabel.setBounds(20, labelY + 85, 200, 25);
        ratingPanel.add(feedbackLabel);
        
        feedbackArea = new JTextArea();
        feedbackArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        feedbackArea.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        JScrollPane feedbackScroll = new JScrollPane(feedbackArea);
        feedbackScroll.setBounds(20, textAreaY, 400, 100);
        feedbackScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        ratingPanel.add(feedbackScroll);
        
        submitRatingButton = new JButton("SUBMIT RATING");
        submitRatingButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        submitRatingButton.setBackground(themeColor);
        submitRatingButton.setForeground(Color.WHITE);
        submitRatingButton.setBounds(130, buttonY, 180, 35);
        submitRatingButton.setBorder(null);
        submitRatingButton.setFocusPainted(false);
        submitRatingButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitRatingButton.addActionListener(e -> submitRating());
        ratingPanel.add(submitRatingButton);
        
        ratingStatusLabel = new JLabel("");
        ratingStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        ratingStatusLabel.setBounds(20, statusY, 400, 50);
        ratingStatusLabel.setVerticalAlignment(SwingConstants.TOP);
        ratingPanel.add(ratingStatusLabel);
        
        checkExistingRating();
        
        JPanel improvePanel = new JPanel();
        improvePanel.setLayout(null);
        improvePanel.setBackground(Color.WHITE);
        improvePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(infoColor, 2),
            "HELP US IMPROVE THIS APPLICATION",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), infoColor));
        improvePanel.setBounds(480, y, 440, 360);
        container.add(improvePanel);
        
        JLabel suggestionsLabel = new JLabel("Your Suggestions / Feature Requests:");
        suggestionsLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        suggestionsLabel.setBounds(20, labelY, 250, 30);
        improvePanel.add(suggestionsLabel);
        
        suggestionArea = new JTextArea();
        suggestionArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        suggestionArea.setLineWrap(true);
        suggestionArea.setWrapStyleWord(true);
        suggestionArea.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        suggestionArea.setText("Enter your suggestions or feature requests...");
        suggestionArea.setForeground(Color.GRAY);
        
        suggestionArea.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (suggestionArea.getText().equals("Enter your suggestions or feature requests...")) {
                    suggestionArea.setText("");
                    suggestionArea.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (suggestionArea.getText().trim().isEmpty()) {
                    suggestionArea.setText("Enter your suggestions or feature requests...");
                    suggestionArea.setForeground(Color.GRAY);
                }
            }
        });
        
        JScrollPane suggestionScroll = new JScrollPane(suggestionArea);
        suggestionScroll.setBounds(20, textAreaY, 400, 100);
        suggestionScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        improvePanel.add(suggestionScroll);
        
        submitSuggestionButton = new JButton("SUBMIT SUGGESTION");
        submitSuggestionButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        submitSuggestionButton.setBackground(infoColor);
        submitSuggestionButton.setForeground(Color.WHITE);
        submitSuggestionButton.setBounds(130, buttonY, 180, 35);
        submitSuggestionButton.setBorder(null);
        submitSuggestionButton.setFocusPainted(false);
        submitSuggestionButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        improvePanel.add(submitSuggestionButton);
        
        suggestionStatusLabel = new JLabel("");
        suggestionStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        suggestionStatusLabel.setBounds(20, statusY, 400, 50);
        suggestionStatusLabel.setVerticalAlignment(SwingConstants.TOP);
        improvePanel.add(suggestionStatusLabel);
        
        submitSuggestionButton.addActionListener(e -> submitSuggestion());
        
        y += 380;
        
        JPanel closePanel = new JPanel();
        closePanel.setLayout(null);
        closePanel.setBackground(Color.WHITE);
        closePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(successColor, 2),
            "COMPLETE",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), successColor));
        closePanel.setBounds(20, y, 900, 80);
        container.add(closePanel);
        
        JLabel finalMessage = new JLabel("Your trade has been successfully completed. Thank you for trading with us!");
        finalMessage.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        finalMessage.setForeground(textColor);
        finalMessage.setBounds(20, 20, 700, 25);
        closePanel.add(finalMessage);
        
        JButton closeButton = new JButton("CLOSE");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeButton.setBackground(successColor);
        closeButton.setForeground(Color.WHITE);
        closeButton.setBounds(740, 20, 140, 40);
        closeButton.setBorder(null);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> {
            if (onClose != null) onClose.run();
        });
        closePanel.add(closeButton);
        
        proceedButton.setEnabled(false);
        backStepButton.setEnabled(false);
        cancelTradeButton.setEnabled(false);
        
        return container;
    }
    
    private int getSelectedRating() {
        for (int i = 0; i < starRadios.length; i++) {
            if (starRadios[i].isSelected()) {
                return i + 1;
            }
        }
        return 0;
    }
    
    private void checkExistingRating() {
        try {
            String sql = "SELECT rating_id FROM tbl_ratings WHERE trader_id = ? AND trade_id = ?";
            List<Map<String, Object>> result = db.fetchRecords(sql, traderId, tradeId);
            
            if (!result.isEmpty()) {
                ratingStatusLabel.setText("<html>You have already rated this trade.<br>Thank you for your feedback!</html>");
                ratingStatusLabel.setForeground(infoColor);
                submitRatingButton.setEnabled(false);
                
                for (JRadioButton radio : starRadios) {
                    radio.setEnabled(false);
                }
            }
        } catch (Exception e) {
            System.out.println("Error checking existing rating: " + e.getMessage());
        }
    }
    
    private void submitRating() {
        int selectedRating = getSelectedRating();
        
        if (selectedRating == 0) {
            ratingStatusLabel.setText("<html>Please select a rating (1 to 5 stars) before submitting.</html>");
            ratingStatusLabel.setForeground(reminderColor);
            return;
        }
        
        String feedback = feedbackArea.getText().trim();
        
        try {
            String checkSql = "SELECT COUNT(*) as count FROM tbl_ratings WHERE trader_id = ? AND trade_id = ?";
            double count = db.getSingleValue(checkSql, traderId, tradeId);
            
            if (count > 0) {
                ratingStatusLabel.setText("<html>You have already rated this trade.<br>Thank you for your feedback!</html>");
                ratingStatusLabel.setForeground(infoColor);
                return;
            }
            
            String sql = "INSERT INTO tbl_ratings (trade_id, trader_id, trader_name, rating_value, feedback, date_submitted) "
                    + "VALUES (?, ?, ?, ?, ?, datetime('now'))";
            db.addRecord(sql, tradeId, traderId, traderName, selectedRating, feedback);
            
            ratingStatusLabel.setText("<html>Thank you for your rating!<br>Your feedback helps us improve BarterZone.</html>");
            ratingStatusLabel.setForeground(infoColor);
            submitRatingButton.setEnabled(false);
            
            for (JRadioButton radio : starRadios) {
                radio.setEnabled(false);
            }
            
        } catch (Exception e) {
            ratingStatusLabel.setText("<html>Error submitting rating.<br>Please try again.</html>");
            ratingStatusLabel.setForeground(reminderColor);
            System.out.println("Error submitting rating: " + e.getMessage());
        }
    }
    
    private void submitSuggestion() {
        String suggestion = suggestionArea.getText().trim();
        
        if (suggestion.isEmpty() || suggestion.equals("Enter your suggestions or feature requests...")) {
            suggestionStatusLabel.setText("<html>Please enter a suggestion before submitting.</html>");
            suggestionStatusLabel.setForeground(reminderColor);
            return;
        }
        
        try {
            String sql = "INSERT INTO tbl_suggestions (trade_id, trader_id, trader_name, suggestion, date_submitted, status) "
                    + "VALUES (?, ?, ?, ?, datetime('now'), 'pending')";
            db.addRecord(sql, tradeId, traderId, traderName, suggestion);
            
            suggestionStatusLabel.setText("<html>Thank you! Your suggestion has been submitted successfully.<br>We appreciate your input to improve BarterZone.</html>");
            suggestionStatusLabel.setForeground(infoColor);
            
            suggestionArea.setText("Enter your suggestions or feature requests...");
            suggestionArea.setForeground(Color.GRAY);
            
            submitSuggestionButton.setEnabled(false);
            submitSuggestionButton.setText("SUBMITTED");
            submitSuggestionButton.setBackground(new Color(150, 150, 150));
            
        } catch (Exception ex) {
            suggestionStatusLabel.setText("<html>Error submitting suggestion.<br>Please try again.</html>");
            suggestionStatusLabel.setForeground(reminderColor);
            System.out.println("Error submitting suggestion: " + ex.getMessage());
        }
    }
}