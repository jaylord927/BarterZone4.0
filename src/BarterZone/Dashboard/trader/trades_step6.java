package BarterZone.Dashboard.trader;

import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;

public class trades_step6 {
    
    private JFrame parent;
    private Runnable onClose;
    
    private Color themeColor = new Color(12, 192, 223);
    private Color successColor = new Color(46, 125, 50);
    
    public trades_step6(JFrame parent, Runnable onClose) {
        this.parent = parent;
        this.onClose = onClose;
    }
    
    public JPanel buildPanel(JButton proceedButton, JButton backStepButton, JButton cancelTradeButton) {
        JPanel completePanel = new JPanel();
        completePanel.setLayout(null);
        completePanel.setBackground(Color.WHITE);
        completePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(successColor), "Trade Complete"));
        completePanel.setBounds(20, 100, 900, 250);
        
        JLabel completedLabel = new JLabel(
            "<html><h2 style='color:#2E7D32;'>TRADE COMPLETED</h2>"
            + "<p>This trade has been successfully completed.</p>"
            + "<p>Thank you for using BarterZone!</p></html>");
        completedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        completedLabel.setBounds(20, 30, 600, 120);
        completePanel.add(completedLabel);
        
        JButton closeButton = new JButton("CLOSE");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeButton.setBackground(themeColor);
        closeButton.setForeground(Color.WHITE);
        closeButton.setBounds(320, 160, 150, 40);
        closeButton.setBorder(null);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> {
            if (onClose != null) onClose.run();
        });
        completePanel.add(closeButton);
        
        proceedButton.setEnabled(false);
        backStepButton.setEnabled(false);
        cancelTradeButton.setEnabled(false);
        
        return completePanel;
    }
}