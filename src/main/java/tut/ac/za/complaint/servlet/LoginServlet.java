package tut.ac.za.complaint.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tut.ac.za.complaint.dao.UserDAO;
import tut.ac.za.complaint.model.User;

public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user;
        try {
            user = userDAO.authenticate(username, password);
        } catch (SQLException e) {
            throw new ServletException("Unable to verify login", e);
        }

        if (user == null) {
            request.setAttribute("error", "Invalid username or password.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("user", user);

        switch (user.getRole()) {
            case "Student":
                response.sendRedirect("studentHome.jsp");
                break;
            case "Employee":
                response.sendRedirect("employeeHome.jsp");
                break;
            case "Receptionist":
                response.sendRedirect("receptionistHome.jsp");
                break;
            default:
                request.setAttribute("error", "Unknown role: " + user.getRole());
                request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }
}
