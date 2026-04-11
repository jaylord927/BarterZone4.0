package BarterZone.Dashboard.trader;

import database.config.config;
import java.awt.Color;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class trades_utils {
    
    // Colors
    public static final Color THEME_COLOR = new Color(12, 192, 223);
    public static final Color HOVER_COLOR = new Color(70, 210, 235);
    public static final Color ACTIVE_COLOR = new Color(0, 150, 180);
    public static final Color HEADER_BG_COLOR = new Color(245, 245, 245);
    public static final Color TEXT_COLOR = new Color(80, 80, 80);
    public static final Color ACCENT_COLOR = new Color(0, 102, 102);
    public static final Color SUCCESS_COLOR = new Color(46, 125, 50);
    public static final Color WARNING_COLOR = new Color(255, 153, 0);
    public static final Color ERROR_COLOR = new Color(204, 0, 0);
    public static final Color INFO_COLOR = new Color(33, 150, 243);
    
    // Paths
    public static final String PROOF_IMAGE_PATH = "src/BarterZone/resources/images/payment_proofs/";
    public static final String REFUND_QR_PATH = "src/BarterZone/resources/images/refund_qrcodes/";
    public static final String REFUND_PROOF_PATH = "src/BarterZone/resources/images/refund_proofs/";
    
    public static void createDirectories() {
        new File(PROOF_IMAGE_PATH).mkdirs();
        new File(REFUND_QR_PATH).mkdirs();
        new File(REFUND_PROOF_PATH).mkdirs();
    }
    
    public static String formatDateTime(Object dateObj) {
        if (dateObj == null) return "-";
        try {
            String dateStr = dateObj.toString();
            if (dateStr.length() >= 16) {
                return dateStr.substring(0, 16).replace("T", " ");
            }
            return dateStr;
        } catch (Exception e) {
            return "-";
        }
    }
    
    public static void showMessage(java.awt.Component parent, Object message, String title, int type) {
        JOptionPane.showMessageDialog(parent, message, title, type);
    }
    
    public static int showConfirm(java.awt.Component parent, String message, String title, int optionType, int messageType) {
        return JOptionPane.showConfirmDialog(parent, message, title, optionType, messageType);
    }
}