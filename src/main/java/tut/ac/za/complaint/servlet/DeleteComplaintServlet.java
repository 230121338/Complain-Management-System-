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

public class DeleteComplaintServlet extends HttpServlet {

    private final ComplaintDAO complaintDAO = new ComplaintDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");
        if (user == null || !"Receptionist".equals(user.getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }

        String idParam = request.getParameter("complaintId");
        if (idParam != null && !idParam.isBlank()) {
            try {
                complaintDAO.delete(Integer.parseInt(idParam));
            } catch (NumberFormatException e) {
                // ignore an invalid id and fall through to the redirect
            } catch (SQLException e) {
                throw new ServletException("Unable to delete complaint", e);
            }
        }

        String q = request.getParameter("q");
        if (q != null && !q.isBlank()) {
            response.sendRedirect("receptionistHome.jsp?q="
                    + java.net.URLEncoder.encode(q, "UTF-8"));
        } else {
            response.sendRedirect("receptionistHome.jsp");
        }
    }
}
