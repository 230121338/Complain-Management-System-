package tut.ac.za.complaint.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tut.ac.za.complaint.dao.UserDAO;
import tut.ac.za.complaint.model.ImageData;

/**
 * Streams a user's profile picture for the navigation bar. Falls back to the
 * default avatar when the user has not uploaded one.
 */
public class ProfileImageServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ImageData image = null;
        try {
            int userId = Integer.parseInt(request.getParameter("id"));
            image = userDAO.findProfileImage(userId);
        } catch (NumberFormatException e) {
            // fall through to the default avatar
        } catch (SQLException e) {
            throw new ServletException("Unable to load profile picture", e);
        }

        if (image == null) {
            response.sendRedirect(request.getContextPath() + "/images/default-avatar.svg");
            return;
        }

        response.setContentType(image.getContentType() == null ? "image/jpeg" : image.getContentType());
        response.setContentLength(image.getData().length);
        response.getOutputStream().write(image.getData());
    }
}
