package tut.ac.za.complaint.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tut.ac.za.complaint.dao.UserDAO;

public class RegisterServlet extends HttpServlet {

    private static final List<String> ALLOWED_ROLES =
            Arrays.asList("Student", "Employee", "Receptionist");

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fullname = trim(request.getParameter("fullname"));
        String username = trim(request.getParameter("username"));
        String password = request.getParameter("password");
        String role = trim(request.getParameter("role"));

        if (fullname.isEmpty() || username.isEmpty()
                || password == null || password.isEmpty() || role.isEmpty()) {
            fail(request, response, "All fields are required.");
            return;
        }
        if (!ALLOWED_ROLES.contains(role)) {
            fail(request, response, "Please choose a valid role.");
            return;
        }

        try {
            if (userDAO.usernameExists(username)) {
                fail(request, response, "That username is already taken.");
                return;
            }
            userDAO.create(fullname, username, password, role);
        } catch (SQLException e) {
            throw new ServletException("Unable to create account", e);
        }

        request.setAttribute("success", "Account created. You can now sign in.");
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    private void fail(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        request.setAttribute("error", message);
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
