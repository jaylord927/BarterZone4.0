package BarterZone.Dashboard.trader;

import BarterZone.resources.IconManager;
import database.config.config;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class reports extends javax.swing.JFrame {

    private int traderId;
    private String traderName;
    private config db;
    private IconManager iconManager;

    private DefaultTableModel myReportsTableModel;
    private javax.swing.JTable myReportsTable;
    private JScrollPane myReportsScrollPane;

    private javax.swing.JComboBox<String> traderComboBox;
    private javax.swing.JComboBox<String> reasonComboBox;
    private javax.swing.JTextArea descriptionArea;
    private JScrollPane descriptionScrollPane;
    private javax.swing.JButton submitReportButton;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton cancelReportButton;
    private javax.swing.JLabel selectedReportLabel;
    private javax.swing.JTextArea adminReplyArea;
    private JScrollPane replyScrollPane;

    private int selectedReportId = -1;
    private String selectedReportStatus = "";
    private int lastSelectedRow = -1;

    private Color hoverColor = new Color(70, 210, 235);
    private Color defaultColor = new Color(12, 192, 223);
    private Color activeColor = new Color(0, 150, 180);
    private JPanel activePanel = null;

    public reports(int traderId, String traderName) {
        this.traderId = traderId;
        this.traderName = traderName;
        this.db = new config();
        this.iconManager = IconManager.getInstance();
        initComponents();

        loadAndResizeIcons();

        setActivePanel(panelreports);

        setupCustomComponents();
        loadMyReports();
        loadTraders();

        setupSidebarHoverEffects();

        setTitle("Reports - " + traderName);
        setSize(800, 500);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void loadAndResizeIcons() {
        setIconSafely(dashboardicon, iconManager.getSideMenuIcon("dashboard"));
        setIconSafely(myitemsicon, iconManager.getSideMenuIcon("myitems"));
        setIconSafely(finditemsicon, iconManager.getSideMenuIcon("finditems"));
        setIconSafely(tradesicon, iconManager.getSideMenuIcon("trade"));
        setIconSafely(messagesicon, iconManager.getSideMenuIcon("messages"));
        setIconSafely(reportsicon, iconManager.getSideMenuIcon("report"));
        setIconSafely(profileicon, iconManager.getSideMenuIcon("profile"));
        setIconSafely(logouticon, iconManager.getSideMenuIcon("logout"));

        setIconSafely(barterzonelogo, iconManager.getLogoIcon());
    }

    private void setIconSafely(javax.swing.JLabel label, ImageIcon icon) {
        if (icon != null) {
            label.setIcon(icon);
            label.setText("");
        }
    }

    private void setupSidebarHoverEffects() {
        applyHoverEffectToPanelAndLabel(paneldashboard, dashboard);
        applyHoverEffectToPanelAndLabel(panelmyitems, myitems);
        applyHoverEffectToPanelAndLabel(panelfinditems, finditems);
        applyHoverEffectToPanelAndLabel(paneltrades, trades);
        applyHoverEffectToPanelAndLabel(panelmessages, messages);
        applyHoverEffectToPanelAndLabel(panelreports, Reports);
        applyHoverEffectToPanelAndLabel(panelprofile, Profile);
        applyHoverEffectToPanelAndLabel(panellogout, logout);
    }

    private void applyHoverEffectToPanelAndLabel(JPanel panel, javax.swing.JLabel label) {
        java.awt.event.MouseAdapter adapter = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setHover(panel);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setDefault(panel);
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (panel == paneldashboard) {
                    openDashboard();
                } else if (panel == panelmyitems) {
                    openMyItems();
                } else if (panel == panelfinditems) {
                    openFindItems();
                } else if (panel == paneltrades) {
                    openTrades();
                } else if (panel == panelmessages) {
                    openMessages();
                } else if (panel == panelprofile) {
                    openProfile();
                } else if (panel == panellogout) {
                    logout();
                }
            }
        };

        panel.addMouseListener(adapter);
        label.addMouseListener(adapter);
    }

    private void setActivePanel(JPanel panel) {
        if (activePanel != null) {
            activePanel.setBackground(defaultColor);
        }
        activePanel = panel;
        activePanel.setBackground(activeColor);
    }

    private void setHover(JPanel panel) {
        if (panel != activePanel) {
            panel.setBackground(hoverColor);
        }
    }

    private void setDefault(JPanel panel) {
        if (panel != activePanel) {
            panel.setBackground(defaultColor);
        }
    }

    private void setupCustomComponents() {
        username.setText(traderName);
        if (traderName != null && traderName.length() > 0) {
            avatarletter.setText(String.valueOf(traderName.charAt(0)).toUpperCase());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        CurrentDate.setText(sdf.format(new Date()));

        jPanel2.removeAll();
        jPanel2.setLayout(null);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBounds(0, 0, 620, 450);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(null);
        leftPanel.setBackground(new Color(245, 245, 245));
        leftPanel.setBorder(new LineBorder(new Color(12, 192, 223), 2));
        leftPanel.setBounds(10, 10, 250, 430);

        javax.swing.JLabel myReportsTitle = new javax.swing.JLabel("My Reports");
        myReportsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        myReportsTitle.setForeground(new Color(0, 102, 102));
        myReportsTitle.setBounds(10, 10, 200, 25);
        leftPanel.add(myReportsTitle);

        setupMyReportsTable();
        myReportsScrollPane = new JScrollPane(myReportsTable);
        myReportsScrollPane.setBounds(10, 40, 230, 380);
        myReportsScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        myReportsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        myReportsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        leftPanel.add(myReportsScrollPane);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(null);
        rightPanel.setBackground(new Color(245, 245, 245));
        rightPanel.setBorder(new LineBorder(new Color(12, 192, 223), 2));
        rightPanel.setBounds(270, 10, 340, 430);

        javax.swing.JLabel submitTitle = new javax.swing.JLabel("Submit New Report");
        submitTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        submitTitle.setForeground(new Color(0, 102, 102));
        submitTitle.setBounds(10, 10, 200, 25);
        rightPanel.add(submitTitle);

        javax.swing.JLabel traderLabel = new javax.swing.JLabel("Report Trader:");
        traderLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        traderLabel.setBounds(10, 45, 100, 20);
        rightPanel.add(traderLabel);

        traderComboBox = new javax.swing.JComboBox<>();
        traderComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        traderComboBox.setBounds(10, 65, 320, 30);
        traderComboBox.setBackground(Color.WHITE);
        traderComboBox.setBorder(new LineBorder(new Color(12, 192, 223)));
        rightPanel.add(traderComboBox);

        javax.swing.JLabel reasonLabel = new javax.swing.JLabel("Reason for Report:");
        reasonLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        reasonLabel.setBounds(10, 105, 150, 20);
        rightPanel.add(reasonLabel);

        String[] reportReasons = {
            "Select a reason",
            "Scamming / Fraud",
            "Fake Account",
            "Fake Item Listing",
            "Harassment / Bullying",
            "Inappropriate Behavior",
            "Misleading Description",
            "Trade Cancellation Abuse",
            "No Show for Meetup",
            "Suspicious Activity",
            "Multiple Accounts",
            "Violation of Terms",
            "Other"
        };
        
        reasonComboBox = new javax.swing.JComboBox<>(reportReasons);
        reasonComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        reasonComboBox.setBounds(10, 125, 320, 30);
        reasonComboBox.setBackground(Color.WHITE);
        reasonComboBox.setBorder(new LineBorder(new Color(12, 192, 223)));
        rightPanel.add(reasonComboBox);

        javax.swing.JLabel descriptionLabel = new javax.swing.JLabel("Description:");
        descriptionLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        descriptionLabel.setBounds(10, 165, 100, 20);
        rightPanel.add(descriptionLabel);

        descriptionArea = new javax.swing.JTextArea();
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionScrollPane = new JScrollPane(descriptionArea);
        descriptionScrollPane.setBounds(10, 185, 320, 60);
        descriptionScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        descriptionScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightPanel.add(descriptionScrollPane);

        submitReportButton = new javax.swing.JButton("SUBMIT REPORT");
        submitReportButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        submitReportButton.setBackground(new Color(204, 0, 0));
        submitReportButton.setForeground(Color.WHITE);
        submitReportButton.setBounds(20, 255, 140, 30);
        submitReportButton.setBorder(null);
        submitReportButton.setFocusPainted(false);
        submitReportButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitReportButton.addActionListener(e -> submitReport());
        rightPanel.add(submitReportButton);

        cancelReportButton = new javax.swing.JButton("CANCEL REPORT");
        cancelReportButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cancelReportButton.setBackground(new Color(102, 102, 102));
        cancelReportButton.setForeground(Color.WHITE);
        cancelReportButton.setBounds(170, 255, 140, 30);
        cancelReportButton.setBorder(null);
        cancelReportButton.setFocusPainted(false);
        cancelReportButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelReportButton.setEnabled(false);
        cancelReportButton.addActionListener(e -> cancelReport());
        rightPanel.add(cancelReportButton);

        refreshButton = new javax.swing.JButton("REFRESH");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshButton.setBackground(new Color(0, 102, 102));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBounds(100, 295, 130, 30);
        refreshButton.setBorder(null);
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> refreshReports());
        rightPanel.add(refreshButton);

        javax.swing.JLabel statusLabel = new javax.swing.JLabel("Admin Reply:");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setBounds(10, 335, 100, 20);
        rightPanel.add(statusLabel);

        selectedReportLabel = new javax.swing.JLabel("Select a report");
        selectedReportLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        selectedReportLabel.setForeground(new Color(102, 102, 102));
        selectedReportLabel.setBounds(10, 350, 320, 15);
        rightPanel.add(selectedReportLabel);

        adminReplyArea = new javax.swing.JTextArea();
        adminReplyArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        adminReplyArea.setLineWrap(true);
        adminReplyArea.setWrapStyleWord(true);
        adminReplyArea.setEditable(false);
        adminReplyArea.setBackground(new Color(245, 245, 245));
        replyScrollPane = new JScrollPane(adminReplyArea);
        replyScrollPane.setBounds(10, 365, 320, 55);
        replyScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        replyScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        replyScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        rightPanel.add(replyScrollPane);

        contentPanel.add(leftPanel);
        contentPanel.add(rightPanel);

        jPanel2.add(contentPanel);
        contentPanel.setBounds(0, 0, 620, 450);

        jPanel2.revalidate();
        jPanel2.repaint();
    }

    private void setupMyReportsTable() {
        String[] columns = {"Report ID", "Date", "Reason", "Status", "Reported Trader"};
        myReportsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        myReportsTable = new javax.swing.JTable(myReportsTableModel);
        myReportsTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        myReportsTable.setRowHeight(30);
        myReportsTable.setShowGrid(true);
        myReportsTable.setGridColor(new Color(12, 192, 223));
        myReportsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        myReportsTable.getTableHeader().setBackground(new Color(0, 102, 102));
        myReportsTable.getTableHeader().setForeground(Color.WHITE);
        myReportsTable.setSelectionBackground(new Color(184, 239, 255));
        myReportsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        myReportsTable.getColumnModel().getColumn(0).setMinWidth(0);
        myReportsTable.getColumnModel().getColumn(0).setMaxWidth(0);
        myReportsTable.getColumnModel().getColumn(0).setWidth(0);

        myReportsTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        myReportsTable.getColumnModel().getColumn(2).setPreferredWidth(70);
        myReportsTable.getColumnModel().getColumn(3).setPreferredWidth(50);
        myReportsTable.getColumnModel().getColumn(4).setPreferredWidth(50);

        myReportsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = myReportsTable.getSelectedRow();
                    
                    if (selectedRow == lastSelectedRow && selectedRow != -1) {
                        myReportsTable.clearSelection();
                        clearReportDetails();
                        lastSelectedRow = -1;
                    } else if (selectedRow != -1) {
                        int modelRow = myReportsTable.convertRowIndexToModel(selectedRow);
                        displayReportDetails(modelRow);
                        lastSelectedRow = selectedRow;
                    }
                }
            }
        });

        myReportsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    myReportsTable.clearSelection();
                    clearReportDetails();
                    lastSelectedRow = -1;
                }
            }
        });
    }

    private void loadMyReports() {
        myReportsTableModel.setRowCount(0);

        String sql = "SELECT r.report_id, r.report_date, r.report_reason, r.report_status, "
                + "u.user_fullname as reported_trader, r.admin_notes "
                + "FROM tbl_reports r "
                + "JOIN tbl_users u ON r.reported_trader_id = u.user_id "
                + "WHERE r.reporter_id = ? "
                + "ORDER BY r.report_date DESC";

        List<Map<String, Object>> reports = db.fetchRecords(sql, traderId);

        for (Map<String, Object> report : reports) {
            String status = report.get("report_status") != null ? report.get("report_status").toString() : "pending";
            String displayStatus = "";
            
            switch (status.toLowerCase()) {
                case "resolved":
                    displayStatus = "Resolved";
                    break;
                case "under_review":
                    displayStatus = "Under Review";
                    break;
                case "pending":
                default:
                    displayStatus = "Pending";
                    break;
            }

            myReportsTableModel.addRow(new Object[]{
                report.get("report_id"),
                formatDate(report.get("report_date")),
                report.get("report_reason"),
                displayStatus,
                report.get("reported_trader")
            });
        }
    }

    private void loadTraders() {
        traderComboBox.removeAllItems();
        traderComboBox.addItem("-- Select Trader to Report --");

        String sql = "SELECT user_id, user_fullname FROM tbl_users WHERE user_id != ? AND user_type = 'trader' AND user_status = 'active' ORDER BY user_fullname";
        List<Map<String, Object>> traders = db.fetchRecords(sql, traderId);

        for (Map<String, Object> trader : traders) {
            String displayName = trader.get("user_fullname").toString();
            traderComboBox.addItem(displayName);
        }
    }

    private void displayReportDetails(int modelRow) {
        selectedReportId = Integer.parseInt(myReportsTableModel.getValueAt(modelRow, 0).toString());
        String status = myReportsTableModel.getValueAt(modelRow, 3).toString();
        selectedReportStatus = status;

        String sql = "SELECT admin_notes, report_status FROM tbl_reports WHERE report_id = ?";
        List<Map<String, Object>> report = db.fetchRecords(sql, selectedReportId);

        if (!report.isEmpty()) {
            String adminNotes = report.get(0).get("admin_notes") != null ? 
                report.get(0).get("admin_notes").toString() : "No admin reply yet.";
            String reportStatus = report.get(0).get("report_status") != null ? 
                report.get(0).get("report_status").toString() : "pending";

            String statusDisplay = "";
            switch (reportStatus.toLowerCase()) {
                case "resolved":
                    statusDisplay = "RESOLVED";
                    cancelReportButton.setEnabled(false);
                    break;
                case "under_review":
                    statusDisplay = "UNDER REVIEW";
                    cancelReportButton.setEnabled(true);
                    break;
                case "pending":
                default:
                    statusDisplay = "PENDING";
                    cancelReportButton.setEnabled(true);
                    break;
            }

            selectedReportLabel.setText("Report #" + selectedReportId + " - " + statusDisplay);
            adminReplyArea.setText(adminNotes);
            adminReplyArea.setCaretPosition(0);
        }
    }

    private void clearReportDetails() {
        selectedReportId = -1;
        selectedReportStatus = "";
        selectedReportLabel.setText("Select a report");
        adminReplyArea.setText("");
        cancelReportButton.setEnabled(false);
    }

    private void submitReport() {
        int selectedTraderIndex = traderComboBox.getSelectedIndex();
        if (selectedTraderIndex == 0) {
            JOptionPane.showMessageDialog(this, "Please select a trader to report.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int selectedReasonIndex = reasonComboBox.getSelectedIndex();
        if (selectedReasonIndex == 0) {
            JOptionPane.showMessageDialog(this, "Please select a reason for the report.", "No Reason", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String description = descriptionArea.getText().trim();
        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please provide a description of the issue.", "No Description", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedTraderName = traderComboBox.getSelectedItem().toString();
        
        String getTraderIdSql = "SELECT user_id FROM tbl_users WHERE user_fullname = ? AND user_type = 'trader'";
        List<Map<String, Object>> traders = db.fetchRecords(getTraderIdSql, selectedTraderName);

        if (traders.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Error: Trader not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int reportedTraderId = Integer.parseInt(traders.get(0).get("user_id").toString());
        String reason = reasonComboBox.getSelectedItem().toString();

        String checkSql = "SELECT COUNT(*) as count FROM tbl_reports WHERE reporter_id = ? AND reported_trader_id = ? AND report_status IN ('pending', 'under_review')";
        double pendingCount = db.getSingleValue(checkSql, traderId, reportedTraderId);

        if (pendingCount > 0) {
            JOptionPane.showMessageDialog(this, 
                "You already have a pending report against this trader.\nPlease wait for admin resolution.",
                "Duplicate Report", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String insertSql = "INSERT INTO tbl_reports (reporter_id, reported_trader_id, report_reason, report_description, report_date, report_status) "
                + "VALUES (?, ?, ?, ?, datetime('now'), 'pending')";

        try {
            db.addRecord(insertSql, traderId, reportedTraderId, reason, description);
            
            JOptionPane.showMessageDialog(this, 
                "Report submitted successfully!\n\n"
                + "Reported Trader: " + selectedTraderName + "\n"
                + "Reason: " + reason + "\n\n"
                + "An admin will review your report.",
                "Success", JOptionPane.INFORMATION_MESSAGE);

            traderComboBox.setSelectedIndex(0);
            reasonComboBox.setSelectedIndex(0);
            descriptionArea.setText("");
            
            loadMyReports();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to submit report: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelReport() {
        if (selectedReportId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a report to cancel.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!selectedReportStatus.equalsIgnoreCase("Pending") && !selectedReportStatus.equalsIgnoreCase("Under Review")) {
            JOptionPane.showMessageDialog(this, 
                "This report cannot be cancelled as it is already " + selectedReportStatus + ".\n\n"
                + "It will be removed from your view only.",
                "Cannot Cancel", JOptionPane.INFORMATION_MESSAGE);
            
            removeFromDisplay();
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel this report?\n\n"
                + "Report ID: " + selectedReportId + "\n"
                + "Status: " + selectedReportStatus,
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM tbl_reports WHERE report_id = ? AND reporter_id = ? AND report_status IN ('pending', 'under_review')";
                db.deleteRecord(sql, selectedReportId, traderId);
                
                JOptionPane.showMessageDialog(this, 
                    "Report cancelled successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                loadMyReports();
                clearReportDetails();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to cancel report: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removeFromDisplay() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Remove this report from your view?\n\n"
                + "Report ID: " + selectedReportId + "\n"
                + "Status: " + selectedReportStatus + "\n\n"
                + "Note: This will only hide it from your list. The report will still be in the database.",
                "Confirm Remove",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            int currentRow = myReportsTable.getSelectedRow();
            if (currentRow != -1) {
                int modelRow = myReportsTable.convertRowIndexToModel(currentRow);
                myReportsTableModel.removeRow(modelRow);
                clearReportDetails();
                
                JOptionPane.showMessageDialog(this, 
                    "Report removed from display.",
                    "Removed", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void refreshReports() {
        loadMyReports();
        loadTraders();
        clearReportDetails();
        JOptionPane.showMessageDialog(this, "Reports refreshed!", "Refresh", JOptionPane.INFORMATION_MESSAGE);
    }

    private String formatDate(Object dateObj) {
        if (dateObj == null) return "-";
        try {
            String dateStr = dateObj.toString();
            if (dateStr.length() >= 10) {
                return dateStr.substring(0, 10);
            }
            return dateStr;
        } catch (Exception e) {
            return "-";
        }
    }

    private void openDashboard() {
        trader_dashboard dashboard = new trader_dashboard(traderId, traderName);
        dashboard.setVisible(true);
        dashboard.setLocationRelativeTo(null);
        this.dispose();
    }

    private void openMyItems() {
        myitems myItemsFrame = new myitems(traderId, traderName);
        myItemsFrame.setVisible(true);
        myItemsFrame.setLocationRelativeTo(null);
        this.dispose();
    }

    private void openFindItems() {
        finditems findItemsFrame = new finditems(traderId, traderName);
        findItemsFrame.setVisible(true);
        findItemsFrame.setLocationRelativeTo(null);
        this.dispose();
    }

    private void openTrades() {
        trades tradesFrame = new trades(traderId, traderName);
        tradesFrame.setVisible(true);
        tradesFrame.setLocationRelativeTo(null);
        this.dispose();
    }

    private void openMessages() {
        messages messagesFrame = new messages(traderId, traderName);
        messagesFrame.setVisible(true);
        messagesFrame.setLocationRelativeTo(null);
        this.dispose();
    }

    private void openProfile() {
        profile profileFrame = new profile();
        profileFrame.setVisible(true);
        profileFrame.setLocationRelativeTo(null);
        this.dispose();
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            landing.landing landingFrame = new landing.landing();
            landingFrame.setVisible(true);
            landingFrame.setLocationRelativeTo(null);
            this.dispose();
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        tradermenu1 = new javax.swing.JPanel();
        logotext = new javax.swing.JLabel();
        avatar = new javax.swing.JPanel();
        avatarletter = new javax.swing.JLabel();
        username = new javax.swing.JLabel();
        barterzonelogo = new javax.swing.JLabel();
        paneldashboard = new javax.swing.JPanel();
        dashboardicon = new javax.swing.JLabel();
        dashboard = new javax.swing.JLabel();
        panelmyitems = new javax.swing.JPanel();
        myitemsicon = new javax.swing.JLabel();
        myitems = new javax.swing.JLabel();
        panelfinditems = new javax.swing.JPanel();
        finditemsicon = new javax.swing.JLabel();
        finditems = new javax.swing.JLabel();
        paneltrades = new javax.swing.JPanel();
        tradesicon = new javax.swing.JLabel();
        trades = new javax.swing.JLabel();
        panelmessages = new javax.swing.JPanel();
        messagesicon = new javax.swing.JLabel();
        messages = new javax.swing.JLabel();
        panellogout = new javax.swing.JPanel();
        logout = new javax.swing.JLabel();
        logouticon = new javax.swing.JLabel();
        panelreports = new javax.swing.JPanel();
        Reports = new javax.swing.JLabel();
        reportsicon = new javax.swing.JLabel();
        panelprofile = new javax.swing.JPanel();
        profileicon = new javax.swing.JLabel();
        Profile = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        CurrentDate = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        myitemstable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 500));
        setPreferredSize(new java.awt.Dimension(800, 500));
        setResizable(false);

        tradermenu1.setBackground(new java.awt.Color(12, 192, 223));
        tradermenu1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(8, 150, 175), 1, true));
        tradermenu1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logotext.setFont(new java.awt.Font("Segoe UI", 1, 22));
        logotext.setForeground(new java.awt.Color(255, 255, 255));
        logotext.setText("BarterZone");
        logotext.setAlignmentX(0.5F);
        tradermenu1.add(logotext, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 0, -1, 40));

        avatar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3));
        avatar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        avatarletter.setFont(new java.awt.Font("Arial", 1, 36));
        avatarletter.setForeground(new java.awt.Color(12, 192, 223));
        avatarletter.setText("T");
        avatar.add(avatarletter, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 50, 30));

        username.setBackground(new java.awt.Color(255, 255, 255));
        username.setFont(new java.awt.Font("Segoe UI", 1, 14));
        username.setForeground(new java.awt.Color(255, 255, 255));
        avatar.add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 110, 30));

        tradermenu1.add(avatar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 110, 60));

        barterzonelogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/logo.png")));
        tradermenu1.add(barterzonelogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        paneldashboard.setBackground(new java.awt.Color(12, 192, 223));

        dashboardicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/dashboard.png")));

        dashboard.setFont(new java.awt.Font("Segoe UI", 1, 14));
        dashboard.setForeground(new java.awt.Color(255, 255, 255));
        dashboard.setText("Dashboard");

        javax.swing.GroupLayout paneldashboardLayout = new javax.swing.GroupLayout(paneldashboard);
        paneldashboard.setLayout(paneldashboardLayout);
        paneldashboardLayout.setHorizontalGroup(
            paneldashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneldashboardLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dashboardicon, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        paneldashboardLayout.setVerticalGroup(
            paneldashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dashboardicon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(paneldashboardLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dashboard)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tradermenu1.add(paneldashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 130, 40));

        panelmyitems.setBackground(new java.awt.Color(12, 192, 223));

        myitemsicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/myitems.png")));

        myitems.setFont(new java.awt.Font("Segoe UI", 1, 14));
        myitems.setForeground(new java.awt.Color(255, 255, 255));
        myitems.setText("My Items");

        javax.swing.GroupLayout panelmyitemsLayout = new javax.swing.GroupLayout(panelmyitems);
        panelmyitems.setLayout(panelmyitemsLayout);
        panelmyitemsLayout.setHorizontalGroup(
            panelmyitemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelmyitemsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(myitemsicon, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(myitems)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        panelmyitemsLayout.setVerticalGroup(
            panelmyitemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelmyitemsLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelmyitemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(myitems)
                    .addComponent(myitemsicon, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );

        tradermenu1.add(panelmyitems, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 130, 40));

        panelfinditems.setBackground(new java.awt.Color(12, 192, 223));

        finditemsicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/finditems.png")));

        finditems.setFont(new java.awt.Font("Segoe UI", 1, 14));
        finditems.setForeground(new java.awt.Color(255, 255, 255));
        finditems.setText("Find Items");

        javax.swing.GroupLayout panelfinditemsLayout = new javax.swing.GroupLayout(panelfinditems);
        panelfinditems.setLayout(panelfinditemsLayout);
        panelfinditemsLayout.setHorizontalGroup(
            panelfinditemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelfinditemsLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(finditemsicon, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(finditems)
                .addGap(18, 18, 18))
        );
        panelfinditemsLayout.setVerticalGroup(
            panelfinditemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelfinditemsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelfinditemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(finditems)
                    .addComponent(finditemsicon, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tradermenu1.add(panelfinditems, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, 130, 40));

        paneltrades.setBackground(new java.awt.Color(12, 192, 223));

        tradesicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/trade.png")));

        trades.setFont(new java.awt.Font("Segoe UI", 1, 14));
        trades.setForeground(new java.awt.Color(255, 255, 255));
        trades.setText("Trades");

        javax.swing.GroupLayout paneltradesLayout = new javax.swing.GroupLayout(paneltrades);
        paneltrades.setLayout(paneltradesLayout);
        paneltradesLayout.setHorizontalGroup(
            paneltradesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneltradesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tradesicon, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(trades)
                .addContainerGap(34, Short.MAX_VALUE))
        );
        paneltradesLayout.setVerticalGroup(
            paneltradesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneltradesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(paneltradesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(trades)
                    .addComponent(tradesicon, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );

        tradermenu1.add(paneltrades, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, 130, 40));

        panelmessages.setBackground(new java.awt.Color(12, 192, 223));

        messagesicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/messages.png")));

        messages.setFont(new java.awt.Font("Segoe UI", 1, 14));
        messages.setForeground(new java.awt.Color(255, 255, 255));
        messages.setText("Messages");

        javax.swing.GroupLayout panelmessagesLayout = new javax.swing.GroupLayout(panelmessages);
        panelmessages.setLayout(panelmessagesLayout);
        panelmessagesLayout.setHorizontalGroup(
            panelmessagesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelmessagesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(messagesicon, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(messages)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        panelmessagesLayout.setVerticalGroup(
            panelmessagesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelmessagesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelmessagesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(messages)
                    .addComponent(messagesicon, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );

        tradermenu1.add(panelmessages, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, 130, 40));

        panellogout.setBackground(new java.awt.Color(12, 192, 223));

        logout.setFont(new java.awt.Font("Segoe UI", 1, 14));
        logout.setForeground(new java.awt.Color(255, 255, 255));
        logout.setText("Logout");

        logouticon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/logout.png")));

        javax.swing.GroupLayout panellogoutLayout = new javax.swing.GroupLayout(panellogout);
        panellogout.setLayout(panellogoutLayout);
        panellogoutLayout.setHorizontalGroup(
            panellogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panellogoutLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logouticon, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(logout)
                .addContainerGap(34, Short.MAX_VALUE))
        );
        panellogoutLayout.setVerticalGroup(
            panellogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panellogoutLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panellogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(logout)
                    .addComponent(logouticon, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );

        tradermenu1.add(panellogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 400, 130, 40));

        panelreports.setBackground(new java.awt.Color(12, 192, 223));

        Reports.setFont(new java.awt.Font("Segoe UI", 1, 14));
        Reports.setForeground(new java.awt.Color(255, 255, 255));
        Reports.setText("Reports");

        reportsicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/report.png")));

        javax.swing.GroupLayout panelreportsLayout = new javax.swing.GroupLayout(panelreports);
        panelreports.setLayout(panelreportsLayout);
        panelreportsLayout.setHorizontalGroup(
            panelreportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelreportsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(reportsicon, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Reports)
                .addContainerGap(27, Short.MAX_VALUE))
        );
        panelreportsLayout.setVerticalGroup(
            panelreportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelreportsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelreportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(reportsicon, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(Reports, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tradermenu1.add(panelreports, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 130, 40));

        panelprofile.setBackground(new java.awt.Color(12, 192, 223));

        profileicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BarterZone/resources/icon/profile.png")));

        Profile.setFont(new java.awt.Font("Segoe UI", 1, 14));
        Profile.setForeground(new java.awt.Color(255, 255, 255));
        Profile.setText("Profile");

        javax.swing.GroupLayout panelprofileLayout = new javax.swing.GroupLayout(panelprofile);
        panelprofile.setLayout(panelprofileLayout);
        panelprofileLayout.setHorizontalGroup(
            panelprofileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelprofileLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(profileicon, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Profile)
                .addContainerGap(34, Short.MAX_VALUE))
        );
        panelprofileLayout.setVerticalGroup(
            panelprofileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelprofileLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelprofileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Profile)
                    .addComponent(profileicon, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tradermenu1.add(panelprofile, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, 130, 40));

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 2));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30));
        jLabel1.setText("Reports");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, 30));

        CurrentDate.setFont(new java.awt.Font("Segoe UI", 0, 14));
        CurrentDate.setForeground(new java.awt.Color(102, 102, 102));
        CurrentDate.setText("jLabel2");
        jPanel1.add(CurrentDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 10, 200, 30));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        myitemstable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(myitemstable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tradermenu1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tradermenu1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0))
        );

        pack();
    }

    private javax.swing.JLabel CurrentDate;
    private javax.swing.JLabel Profile;
    private javax.swing.JLabel Reports;
    private javax.swing.JPanel avatar;
    private javax.swing.JLabel avatarletter;
    private javax.swing.JLabel barterzonelogo;
    private javax.swing.JLabel dashboard;
    private javax.swing.JLabel dashboardicon;
    private javax.swing.JLabel finditems;
    private javax.swing.JLabel finditemsicon;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel logotext;
    private javax.swing.JLabel logout;
    private javax.swing.JLabel logouticon;
    private javax.swing.JLabel messages;
    private javax.swing.JLabel messagesicon;
    private javax.swing.JLabel myitems;
    private javax.swing.JLabel myitemsicon;
    private javax.swing.JTable myitemstable;
    private javax.swing.JPanel paneldashboard;
    private javax.swing.JPanel panelfinditems;
    private javax.swing.JPanel panellogout;
    private javax.swing.JPanel panelmessages;
    private javax.swing.JPanel panelmyitems;
    private javax.swing.JPanel panelprofile;
    private javax.swing.JPanel panelreports;
    private javax.swing.JPanel paneltrades;
    private javax.swing.JLabel profileicon;
    private javax.swing.JLabel reportsicon;
    private javax.swing.JPanel tradermenu1;
    private javax.swing.JLabel trades;
    private javax.swing.JLabel tradesicon;
    private javax.swing.JLabel username;
}