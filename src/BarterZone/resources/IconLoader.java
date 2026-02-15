package BarterZone.resources;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public interface IconLoader {
    default void setSideMenuIcon(JLabel iconLabel, String iconName) {
        ImageIcon icon = IconManager.getInstance().getSideMenuIcon(iconName);
        if (icon != null) {
            iconLabel.setIcon(icon);
            iconLabel.setText(""); 
        }
    }
    
    default void setActionButtonIcon(JLabel iconLabel, String iconName) {
        ImageIcon icon = IconManager.getInstance().getActionButtonIcon(iconName);
        if (icon != null) {
            iconLabel.setIcon(icon);
            iconLabel.setText("");
        }
    }
    
    default void setStatsIcon(JLabel iconLabel, String iconName) {
        ImageIcon icon = IconManager.getInstance().getStatsIcon(iconName);
        if (icon != null) {
            iconLabel.setIcon(icon);
            iconLabel.setText("");
        }
    }
}