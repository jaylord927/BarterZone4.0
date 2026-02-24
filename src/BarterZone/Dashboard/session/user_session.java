package BarterZone.Dashboard.session;

import database.config.config;
import java.util.HashMap;
import java.util.Map;

public class user_session {
    private static user_session instance;
    private int userId;
    private String fullName;
    private String username;
    private String email;
    private String userType;
    private String status;
    private String profilePicture;
    private config db;
    
    private user_session() {
        this.db = new config();
    }
    
    public static user_session getInstance() {
        if (instance == null) {
            instance = new user_session();
        }
        return instance;
    }
    
    public boolean login(int userId, String userType, String fullName) {
        this.userId = userId;
        this.userType = userType;
        this.fullName = fullName;
        return loadUserData();
    }
    
    private boolean loadUserData() {
        String sql = "SELECT user_fullname, user_username, user_email, user_type, user_status, user_profile_picture "
                + "FROM tbl_users WHERE user_id = ?";
        
        java.util.List<Map<String, Object>> users = db.fetchRecords(sql, userId);
        
        if (!users.isEmpty()) {
            Map<String, Object> user = users.get(0);
            this.fullName = (String) user.get("user_fullname");
            this.username = (String) user.get("user_username");
            this.email = (String) user.get("user_email");
            this.userType = (String) user.get("user_type");
            this.status = (String) user.get("user_status");
            this.profilePicture = (String) user.get("user_profile_picture");
            return true;
        }
        return false;
    }
    
    public void refreshData() {
        loadUserData();
    }
    
    public void logout() {
        this.userId = -1;
        this.fullName = null;
        this.username = null;
        this.email = null;
        this.userType = null;
        this.status = null;
        this.profilePicture = null;
    }
    
    public boolean isLoggedIn() {
        return userId != -1;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getUserType() {
        return userType;
    }
    
    public String getStatus() {
        return status;
    }
    
    public String getProfilePicture() {
        return profilePicture;
    }
    
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
        String sql = "UPDATE tbl_users SET user_profile_picture = ? WHERE user_id = ?";
        db.updateRecord(sql, profilePicture, userId);
    }
    
    public Map<String, Object> getAllUserData() {
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", userId);
        data.put("user_fullname", fullName);
        data.put("user_username", username);
        data.put("user_email", email);
        data.put("user_type", userType);
        data.put("user_status", status);
        data.put("user_profile_picture", profilePicture);
        return data;
    }
}