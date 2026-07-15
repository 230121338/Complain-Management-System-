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

public class ComplaintServlet extends HttpServlet {

    private final ComplaintDAO complaintDAO = new ComplaintDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");
        if (user == null || !"Student".equals(user.getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }

        String complaint = request.getParameter("complaint");
        String block = request.getParameter("block");
        String room = request.getParameter("room");

        if (complaint == null || complaint.trim().isEmpty()) {
            request.setAttribute("error", "Please describe your complaint.");
            request.getRequestDispatcher("complaint.jsp").forward(request, response);
            return;
        }

        if (block == null || block.trim().isEmpty()) {
            request.setAttribute("error", "Please enter the block.");
            request.getRequestDispatcher("complaint.jsp").forward(request, response);
            return;
        }

        try {
            complaintDAO.insert(user.getUserId(), complaint.trim(), block.trim(), room);
        } catch (SQLException e) {
            throw new ServletException("Unable to save complaint", e);
        }

        response.sendRedirect("myComplaints.jsp");
    }
}
