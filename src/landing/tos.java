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

public class tos extends JFrame {

    private JPanel mainPanel;
    private JPanel headerPanel;
    private JLabel titleLabel;
    private JButton backButton;
    private JTextArea contentArea;
    private JScrollPane scrollPane;

    private Color accentColor = new Color(12, 192, 223);

    public tos() {
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

        titleLabel = new JLabel("Terms of Policy");
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
        contentArea.setText(getTermsText());

        scrollPane = new JScrollPane(contentArea);
        scrollPane.setBounds(20, 80, 560, 380);
        scrollPane.setBorder(new LineBorder(accentColor, 1));
        add(scrollPane);
    }

    private String getTermsText() {
        return "TERMS OF SERVICE\n"
                + "================\n\n"
                + "Last Updated: March 15, 2026\n\n"
                + "1. ACCEPTANCE OF TERMS\n"
                + "----------------------\n"
                + "By accessing or using BarterZone, you agree to be bound by these Terms. "
                + "If you do not agree, please do not use our services.\n\n"
                + "2. ELIGIBILITY\n"
                + "--------------\n"
                + "You must be at least 18 years old to use BarterZone. "
                + "By using our services, you represent that you meet this requirement.\n\n"
                + "3. ACCOUNT REGISTRATION\n"
                + "-----------------------\n"
                + "You are responsible for maintaining the security of your account. "
                + "You must provide accurate and complete information when creating an account.\n\n"
                + "4. TRADING RULES\n"
                + "----------------\n"
                + "• All trades are between users. BarterZone facilitates but does not guarantee trades.\n"
                + "• Users must provide accurate descriptions of items.\n"
                + "• Fraudulent activity will result in account termination.\n"
                + "• Users are responsible for shipping costs and arrangements.\n\n"
                + "5. PROHIBITED ITEMS\n"
                + "-------------------\n"
                + "The following items may not be listed on BarterZone:\n"
                + "• Illegal items or substances\n"
                + "• Weapons or explosives\n"
                + "• Counterfeit goods\n"
                + "• Stolen property\n"
                + "• Items that infringe on intellectual property rights\n\n"
                + "6. FEES\n"
                + "-------\n"
                + "BarterZone charges service fees for completed trades. "
                + "Fees are clearly displayed before you confirm a trade.\n\n"
                + "7. DISPUTE RESOLUTION\n"
                + "---------------------\n"
                + "If a dispute arises, users should first attempt to resolve it directly. "
                + "If unable to resolve, BarterZone administrators may mediate.\n\n"
                + "8. TERMINATION\n"
                + "--------------\n"
                + "We reserve the right to suspend or terminate accounts that violate these terms "
                + "or engage in harmful behavior.\n\n"
                + "9. LIMITATION OF LIABILITY\n"
                + "--------------------------\n"
                + "BarterZone is not liable for any damages arising from trades between users. "
                + "We provide the platform \"as is\" without warranties.\n\n"
                + "10. CONTACT US\n"
                + "--------------\n"
                + "For questions about these Terms, contact us at:\n"
                + "Email: legal@barterzone.com\n\n"
                + "© 2026 BarterZone. All rights reserved.";
    }
}
