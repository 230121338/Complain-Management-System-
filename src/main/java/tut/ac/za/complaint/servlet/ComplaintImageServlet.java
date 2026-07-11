package tut.ac.za.complaint.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tut.ac.za.complaint.dao.ComplaintDAO;
import tut.ac.za.complaint.model.ImageData;

/**
 * Streams a complaint's attached picture. When the complaint has no picture the
 * request is redirected to the default placeholder image, so pages can always
 * render an {@code <img>} without special-casing missing pictures.
 */
public class ComplaintImageServlet extends HttpServlet {

    private final ComplaintDAO complaintDAO = new ComplaintDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ImageData image = null;
        try {
            int complaintId = Integer.parseInt(request.getParameter("id"));
            image = complaintDAO.findImage(complaintId);
        } catch (NumberFormatException e) {
            // fall through to the default image
        } catch (SQLException e) {
            throw new ServletException("Unable to load complaint picture", e);
        }

        if (image == null) {
            response.sendRedirect(request.getContextPath() + "/images/default-complaint.svg");
            return;
        }

        response.setContentType(image.getContentType() == null ? "image/jpeg" : image.getContentType());
        response.setContentLength(image.getData().length);
        response.getOutputStream().write(image.getData());
    }
}
