package tut.ac.za.complaint.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tut.ac.za.complaint.dao.UserDAO;
import tut.ac.za.complaint.model.ImageData;
import tut.ac.za.complaint.model.User;

/** Lets a signed-in student or employee upload a navigation-bar profile picture. */
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 6 * 1024 * 1024
)
public class ProfilePictureServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");
        if (user == null || !("Student".equals(user.getRole()) || "Employee".equals(user.getRole()))) {
            response.sendRedirect("login.jsp");
            return;
        }

        ImageData image = ComplaintServlet.readImagePart(request, "profileImage");
        if (image != null) {
            try {
                userDAO.updateProfileImage(user.getUserId(), image);
            } catch (SQLException e) {
                throw new ServletException("Unable to save profile picture", e);
            }
        }

        response.sendRedirect("Employee".equals(user.getRole()) ? "employeeHome.jsp" : "studentHome.jsp");
    }
}
