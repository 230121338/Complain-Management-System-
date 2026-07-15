package tut.ac.za.complaint.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tut.ac.za.complaint.model.Complaint;

public class ComplaintDAO {

    public static final String STATUS_NOT_FIXED = "Not Fixed";
    public static final String STATUS_VIEWED = "Viewed";
    public static final String STATUS_FIXED = "Fixed";

    /** Inserts a new complaint with status "Not Fixed" and today's report date. */
    public void insert(int studentId, String complaint, String block, String room) throws SQLException {
        String sql = "INSERT INTO complaints (studentId, complaint, block, room, status, dateReported) "
                + "VALUES (?, ?, ?, ?, ?, CURDATE())";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setString(2, complaint);
            ps.setString(3, block);
            ps.setString(4, room);
            ps.setString(5, STATUS_NOT_FIXED);
            ps.executeUpdate();
        }
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
                + "c.block, c.room, c.status, c.dateReported, c.viewedDate, c.fixedDate "
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
        return c;
    }
}
