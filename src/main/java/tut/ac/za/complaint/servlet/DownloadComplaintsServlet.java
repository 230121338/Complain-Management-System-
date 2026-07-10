package tut.ac.za.complaint.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tut.ac.za.complaint.dao.ComplaintDAO;
import tut.ac.za.complaint.model.Complaint;
import tut.ac.za.complaint.model.User;

public class DownloadComplaintsServlet extends HttpServlet {

    private final ComplaintDAO complaintDAO = new ComplaintDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");
        if (user == null || !"Receptionist".equals(user.getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<Complaint> complaints;
        try {
            complaints = complaintDAO.findAll();
        } catch (SQLException e) {
            throw new ServletException("Unable to load complaints", e);
        }

        response.setContentType("text/csv;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"complaints.csv\"");

        PrintWriter out = response.getWriter();
        out.println("Student,Complaint,Block,Room,Status,Date Reported,Viewed Date,Fixed Date");
        for (Complaint c : complaints) {
            out.println(String.join(",",
                    csv(c.getStudentName()),
                    csv(c.getComplaint()),
                    csv(c.getFloor()),
                    csv(c.getRoom()),
                    csv(c.getStatus()),
                    csv(c.getDateReported() == null ? "" : c.getDateReported().toString()),
                    csv(c.getViewedDate() == null ? "" : c.getViewedDate().toString()),
                    csv(c.getFixedDate() == null ? "" : c.getFixedDate().toString())));
        }
        out.flush();
    }

    /** Quotes a CSV field and escapes any embedded quotes. */
    private String csv(String value) {
        String v = value == null ? "" : value;
        return "\"" + v.replace("\"", "\"\"") + "\"";
    }
}
