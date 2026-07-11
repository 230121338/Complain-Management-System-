package tut.ac.za.complaint.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tut.ac.za.complaint.model.Complaint;
import tut.ac.za.complaint.model.ImageData;

public class ComplaintDAO {

    public static final String STATUS_NOT_FIXED = "Not Fixed";
    public static final String STATUS_VIEWED = "Viewed";
    public static final String STATUS_FIXED = "Fixed";

    /** Inserts a new complaint with status "Not Fixed" and today's report date. */
    public void insert(int studentId, String complaint, String block, String room) throws SQLException {
        insert(studentId, complaint, block, room, null);
    }

    /**
     * Inserts a new complaint with an optional attached picture. Pass {@code null}
     * for {@code image} to store a complaint without a picture.
     */
    public void insert(int studentId, String complaint, String block, String room, ImageData image)
            throws SQLException {
        String sql = "INSERT INTO complaints (studentId, complaint, block, room, status, dateReported, image, imageType) "
                + "VALUES (?, ?, ?, ?, ?, CURDATE(), ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setString(2, complaint);
            ps.setString(3, block);
            ps.setString(4, room);
            ps.setString(5, STATUS_NOT_FIXED);
            if (image != null && image.getData() != null && image.getData().length > 0) {
                ps.setBytes(6, image.getData());
                ps.setString(7, image.getContentType());
            } else {
                ps.setNull(6, java.sql.Types.BLOB);
                ps.setNull(7, java.sql.Types.VARCHAR);
            }
            ps.executeUpdate();
        }
    }

    /** Returns the picture attached to a complaint, or {@code null} if none exists. */
    public ImageData findImage(int complaintId) throws SQLException {
        String sql = "SELECT image, imageType FROM complaints WHERE complaintId = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, complaintId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    byte[] data = rs.getBytes("image");
                    if (data != null && data.length > 0) {
                        return new ImageData(data, rs.getString("imageType"));
                    }
                }
            }
        }
        return null;
    }

    /** Complaints belonging to a single student, newest first. */
    public List<Complaint> findByStudent(int studentId) throws SQLException {
        String sql = baseSelect() + " WHERE c.studentId = ? ORDER BY c.complaintId DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                return mapList(rs);
            }
        }
    }

    /** Every complaint, newest first. */
    public List<Complaint> findAll() throws SQLException {
        String sql = baseSelect() + " ORDER BY c.complaintId DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return mapList(rs);
        }
    }

    /** Complaints whose student name matches the query (case-insensitive), newest first. */
    public List<Complaint> searchByStudentName(String query) throws SQLException {
        String sql = baseSelect() + " WHERE u.fullname LIKE ? ORDER BY c.complaintId DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + query + "%");
            try (ResultSet rs = ps.executeQuery()) {
                return mapList(rs);
            }
        }
    }

    /** Marks a complaint as Viewed and stamps today's date. */
    public void markViewed(int complaintId) throws SQLException {
        String sql = "UPDATE complaints SET status = ?, viewedDate = CURDATE() WHERE complaintId = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, STATUS_VIEWED);
            ps.setInt(2, complaintId);
            ps.executeUpdate();
        }
    }

    /** Marks a complaint as Fixed and stamps today's date. */
    public void markFixed(int complaintId) throws SQLException {
        String sql = "UPDATE complaints SET status = ?, fixedDate = CURDATE() WHERE complaintId = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, STATUS_FIXED);
            ps.setInt(2, complaintId);
            ps.executeUpdate();
        }
    }

    private String baseSelect() {
        return "SELECT c.complaintId, c.studentId, u.fullname AS studentName, c.complaint, "
                + "c.block, c.room, c.status, c.dateReported, c.viewedDate, c.fixedDate, "
                + "(c.image IS NOT NULL) AS hasImage "
                + "FROM complaints c LEFT JOIN users u ON c.studentId = u.userId";
    }

    private List<Complaint> mapList(ResultSet rs) throws SQLException {
        List<Complaint> list = new ArrayList<>();
        while (rs.next()) {
            list.add(map(rs));
        }
        return list;
    }

    private Complaint map(ResultSet rs) throws SQLException {
        Complaint c = new Complaint();
        c.setComplaintId(rs.getInt("complaintId"));
        c.setStudentId(rs.getInt("studentId"));
        c.setStudentName(rs.getString("studentName"));
        c.setComplaint(rs.getString("complaint"));
        c.setBlock(rs.getString("block"));
        c.setRoom(rs.getString("room"));
        c.setStatus(rs.getString("status"));
        c.setDateReported(rs.getDate("dateReported"));
        c.setViewedDate(rs.getDate("viewedDate"));
        c.setFixedDate(rs.getDate("fixedDate"));
        c.setHasImage(rs.getBoolean("hasImage"));
        return c;
    }
}
