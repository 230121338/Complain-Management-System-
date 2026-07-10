package tut.ac.za.complaint.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tut.ac.za.complaint.dao.ComplaintDAO;
import tut.ac.za.complaint.model.User;

public class FixComplaintServlet extends HttpServlet {

    private final ComplaintDAO complaintDAO = new ComplaintDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");
        if (user == null || !"Employee".equals(user.getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int complaintId = Integer.parseInt(request.getParameter("complaintId"));
            complaintDAO.markFixed(complaintId);
        } catch (NumberFormatException e) {
            // Ignore malformed id and simply return to the list.
        } catch (SQLException e) {
            throw new ServletException("Unable to update complaint", e);
        }

        response.sendRedirect("employeeHome.jsp");
    }
}
