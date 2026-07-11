package tut.ac.za.complaint.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import tut.ac.za.complaint.model.ImageData;
import tut.ac.za.complaint.model.User;

public class UserDAO {

    /**
     * Returns the matching user when the username/password pair is valid,
     * otherwise null.
     */
    public User authenticate(String username, String password) throws SQLException {
        String sql = "SELECT userId, fullname, username, password, role "
                + "FROM users WHERE username = ? AND password = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        }
        return null;
    }

    /** Returns true when a username is already taken. */
    public boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /** Creates a new user account and returns the generated user id. */
    public int create(String fullname, String username, String password, String role) throws SQLException {
        String sql = "INSERT INTO users (fullname, username, password, role) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, fullname);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setString(4, role);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    /** Stores (or replaces) the profile picture for a user. */
    public void updateProfileImage(int userId, ImageData image) throws SQLException {
        String sql = "UPDATE users SET profileImage = ?, profileType = ? WHERE userId = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBytes(1, image.getData());
            ps.setString(2, image.getContentType());
            ps.setInt(3, userId);
            ps.executeUpdate();
        }
    }

    /** Returns a user's profile picture, or {@code null} if none has been set. */
    public ImageData findProfileImage(int userId) throws SQLException {
        String sql = "SELECT profileImage, profileType FROM users WHERE userId = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    byte[] data = rs.getBytes("profileImage");
                    if (data != null && data.length > 0) {
                        return new ImageData(data, rs.getString("profileType"));
                    }
                }
            }
        }
        return null;
    }

    private User map(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("userId"));
        user.setFullname(rs.getString("fullname"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        return user;
    }
}
