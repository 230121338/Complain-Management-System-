package tut.ac.za.complaint.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import tut.ac.za.complaint.dao.ComplaintDAO;
import tut.ac.za.complaint.model.ImageData;
import tut.ac.za.complaint.model.User;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,      // 1 MB
        maxFileSize = 5 * 1024 * 1024,        // 5 MB per picture
        maxRequestSize = 6 * 1024 * 1024      // 6 MB total request
)
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
        String floor = request.getParameter("floor");
        String room = request.getParameter("room");

        if (complaint == null || complaint.trim().isEmpty()) {
            request.setAttribute("error", "Please describe your complaint.");
            request.getRequestDispatcher("complaint.jsp").forward(request, response);
            return;
        }

        ImageData image = readImagePart(request, "image");

        try {
            complaintDAO.insert(user.getUserId(), complaint.trim(), floor, room, image);
        } catch (SQLException e) {
            throw new ServletException("Unable to save complaint", e);
        }

        response.sendRedirect("myComplaints.jsp");
    }

    /** Reads an uploaded image part, returning {@code null} when no file was sent. */
    static ImageData readImagePart(HttpServletRequest request, String field)
            throws ServletException, IOException {
        Part part = request.getPart(field);
        if (part == null || part.getSize() == 0) {
            return null;
        }
        String contentType = part.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return null;
        }
        try (InputStream in = part.getInputStream()) {
            return new ImageData(in.readAllBytes(), contentType);
        }
    }
}
