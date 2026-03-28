package landing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

public class pp extends JFrame {

    private JPanel mainPanel;
    private JPanel headerPanel;
    private JLabel titleLabel;
    private JButton backButton;
    private JTextArea contentArea;
    private JScrollPane scrollPane;

    private Color accentColor = new Color(12, 192, 223);

    public pp() {
        initComponents();
        setTitle("BarterZone");
        setIconImage(new ImageIcon(getClass().getResource(
                "/BarterZone/resources/icon/logo.png")).getImage());
        setSize(600, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Header Panel
        headerPanel = new JPanel();
        headerPanel.setLayout(null);
        headerPanel.setBackground(accentColor);
        headerPanel.setBounds(0, 0, 600, 60);
        headerPanel.setBorder(new LineBorder(new Color(8, 150, 175), 1, true));
        add(headerPanel);

        titleLabel = new JLabel("Privacy Policy");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(20, 15, 300, 30);
        headerPanel.add(titleLabel);

        backButton = new JButton("← Back");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        backButton.setBackground(new Color(0, 102, 102));
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(500, 15, 80, 30);
        backButton.setBorder(null);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> dispose());
        headerPanel.add(backButton);

        // Content Area
        contentArea = new JTextArea();
        contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setText(getPrivacyPolicyText());

        scrollPane = new JScrollPane(contentArea);
        scrollPane.setBounds(20, 80, 560, 380);
        scrollPane.setBorder(new LineBorder(accentColor, 1));
        add(scrollPane);
    }

    private String getPrivacyPolicyText() {
        return "PRIVACY POLICY\n"
                + "================\n\n"
                + "Last Updated: March 15, 2026\n\n"
                + "1. INFORMATION WE COLLECT\n"
                + "-------------------------\n"
                + "We collect information you provide directly to us, such as when you create an account, "
                + "list an item, or communicate with other users. This may include:\n"
                + "• Name and contact information\n"
                + "• Profile information\n"
                + "• Item listings and trade history\n"
                + "• Communications with other users\n\n"
                + "2. HOW WE USE YOUR INFORMATION\n"
                + "------------------------------\n"
                + "We use the information we collect to:\n"
                + "• Provide, maintain, and improve our services\n"
                + "• Facilitate trades between users\n"
                + "• Communicate with you about our services\n"
                + "• Protect against fraud and unauthorized activity\n\n"
                + "3. INFORMATION SHARING\n"
                + "----------------------\n"
                + "We do not sell your personal information. We may share information:\n"
                + "• With other users as necessary to complete a trade\n"
                + "• With service providers who help us operate our platform\n"
                + "• When required by law or to protect rights and safety\n\n"
                + "4. DATA SECURITY\n"
                + "----------------\n"
                + "We implement reasonable security measures to protect your information. "
                + "However, no method of transmission over the Internet is 100% secure.\n\n"
                + "5. YOUR CHOICES\n"
                + "---------------\n"
                + "You can access and update your account information at any time. "
                + "You may also contact us to delete your account.\n\n"
                + "6. CONTACT US\n"
                + "-------------\n"
                + "If you have questions about this Privacy Policy, please contact us at:\n"
                + "Email: privacy@barterzone.com\n\n"
                + "© 2026 BarterZone. All rights reserved.";
    }
}
