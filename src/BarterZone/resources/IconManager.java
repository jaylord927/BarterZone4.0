package BarterZone.resources;

import javax.swing.ImageIcon;
import java.awt.Image;

public class IconManager {
    private static IconManager instance;
    
    public static final int SIDE_MENU_SIZE = 25;
    public static final int ACTION_BUTTON_SIZE = 20;
    public static final int STATS_ICON_SIZE = 30;
    public static final int LOGO_SIZE = 32;
    
    private IconManager() {}
    
    public static IconManager getInstance() {
        if (instance == null) {
            instance = new IconManager();
        }
        return instance;
    }
    
    public ImageIcon resizeIcon(String path, int width, int height) {
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource(path));
            Image scaledImage = originalIcon.getImage().getScaledInstance(
                width, height, Image.SCALE_SMOOTH
            );
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.err.println("Could not load icon: " + path);
            return null;
        }
    }
    
    public ImageIcon getSideMenuIcon(String iconName) {
        return resizeIcon("/BarterZone/resources/icon/" + iconName + ".png", 
                         SIDE_MENU_SIZE, SIDE_MENU_SIZE);
    }
    
    public ImageIcon getActionButtonIcon(String iconName) {
        return resizeIcon("/BarterZone/resources/icon/" + iconName + ".png", 
                         ACTION_BUTTON_SIZE, ACTION_BUTTON_SIZE);
    }
    
    public ImageIcon getStatsIcon(String iconName) {
        return resizeIcon("/BarterZone/resources/icon/" + iconName + ".png", 
                         STATS_ICON_SIZE, STATS_ICON_SIZE);
    }
    
    public ImageIcon getLogoIcon() {
        return resizeIcon("/BarterZone/resources/icon/logo.png", 
                         LOGO_SIZE, LOGO_SIZE);
    }
    
    public ImageIcon getIcon(String iconName, int width, int height) {
        return resizeIcon("/BarterZone/resources/icon/" + iconName + ".png", width, height);
    }
}