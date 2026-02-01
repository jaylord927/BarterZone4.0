package Barterzone.ui;

import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Transition {
    
    public static void slideLeftToRight(JFrame currentFrame, JFrame nextFrame) {
        if (currentFrame == null || nextFrame == null) return;
        
        // Get current position
        int currentX = currentFrame.getX();
        int currentY = currentFrame.getY();
        int width = currentFrame.getWidth();
        
        // Position next frame off-screen to the left
        nextFrame.setLocation(currentX - width, currentY);
        nextFrame.setVisible(true);
        nextFrame.pack();
        
        // Animation timer
        Timer timer = new Timer(10, new ActionListener() {
            int step = 0;
            int totalSteps = 30;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                step++;
                if (step > totalSteps) {
                    ((Timer) e.getSource()).stop();
                    currentFrame.dispose();
                    return;
                }
                
                float progress = (float) step / totalSteps;
                
                // Move current frame to the right
                int currentNewX = (int) (currentX + (width * progress));
                currentFrame.setLocation(currentNewX, currentY);
                
                // Move next frame from left to center
                int nextNewX = (int) (currentX - (width * (1 - progress)));
                nextFrame.setLocation(nextNewX, currentY);
            }
        });
        timer.start();
    }
    
    public static void slideRightToLeft(JFrame currentFrame, JFrame nextFrame) {
        if (currentFrame == null || nextFrame == null) return;
        
        // Get current position
        int currentX = currentFrame.getX();
        int currentY = currentFrame.getY();
        int width = currentFrame.getWidth();
        
        // Position next frame off-screen to the right
        nextFrame.setLocation(currentX + width, currentY);
        nextFrame.setVisible(true);
        nextFrame.pack();
        
        // Animation timer
        Timer timer = new Timer(10, new ActionListener() {
            int step = 0;
            int totalSteps = 30;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                step++;
                if (step > totalSteps) {
                    ((Timer) e.getSource()).stop();
                    currentFrame.dispose();
                    return;
                }
                
                float progress = (float) step / totalSteps;
                
                // Move current frame to the left
                int currentNewX = (int) (currentX - (width * progress));
                currentFrame.setLocation(currentNewX, currentY);
                
                // Move next frame from right to center
                int nextNewX = (int) (currentX + (width * (1 - progress)));
                nextFrame.setLocation(nextNewX, currentY);
            }
        });
        timer.start();
    }
}