package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ResizableFrame {
    
    public static void setupResizableFrame(JFrame frame) {
        frame.setMinimumSize(new Dimension(800, 500));
        
        frame.setResizable(true);
        
        frame.setLocationRelativeTo(null);
        
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                enforceMinimumSize(frame);
            }
            
            @Override
            public void componentMoved(ComponentEvent e) {
            }
        });
    }
    
    private static void enforceMinimumSize(JFrame frame) {
        Dimension size = frame.getSize();
        Dimension minSize = frame.getMinimumSize();
        
        if (size.width < minSize.width || size.height < minSize.height) {
            int newWidth = Math.max(size.width, minSize.width);
            int newHeight = Math.max(size.height, minSize.height);
            frame.setSize(newWidth, newHeight);
        }
    }
    
    public static void showFullScreen(JFrame frame) {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        
        if (device.isFullScreenSupported()) {
            device.setFullScreenWindow(frame);
        } else {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    }
}