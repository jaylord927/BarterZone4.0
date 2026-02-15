//package Barterzone.ui;
//
//import javax.swing.*;
//import java.awt.*;
//
//public class Transition {
//
//    public static void swap(JFrame current, JFrame next) {
//
//        next.setLocation(current.getLocation());
//        next.setVisible(true);
//
//        int width = current.getWidth();
//        int height = current.getHeight();
//
//        next.setLocation(current.getX() + width, current.getY());
//
//        Timer timer = new Timer(5, null);
//
//        timer.addActionListener(e -> {
//
//            int x = next.getX();
//            int target = current.getX();
//
//            if (x <= target) {
//                next.setLocation(target, current.getY());
//                timer.stop();
//                current.dispose();
//            } else {
//                next.setLocation(x - 20, current.getY());
//                current.setLocation(x - width - 20, current.getY());
//            }
//
//        });
//
//        timer.start();
//
//    }
//}
