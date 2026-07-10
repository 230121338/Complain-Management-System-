<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="tut.ac.za.complaint.model.User" %>
<%@ page import="tut.ac.za.complaint.model.Complaint" %>
<%@ page import="tut.ac.za.complaint.dao.ComplaintDAO" %>
<%!
    String badge(String status) {
        if ("Fixed".equals(status)) return "status status-fixed";
        if ("Viewed".equals(status)) return "status status-viewed";
        return "status status-notfixed";
    }
%>
<%
    User user = (session == null) ? null : (User) session.getAttribute("user");
    if (user == null || !"Receptionist".equals(user.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }
    List<Complaint> complaints = new ComplaintDAO().findAll();
%>
<!DOCTYPE html>
<html>
<head>
    <title>Receptionist Home</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="topbar">
        <h1>Complaint System &mdash; Receptionist</h1>
        <a href="LogoutServlet">Logout</a>
    </div>
    <div class="container">
        <div class="card">
            <h2>All Complaints (View Only)</h2>

            <% if (complaints.isEmpty()) { %>
                <p class="empty">There are no complaints.</p>
            <% } else { %>
                <table>
                    <tr>
                        <th>Student</th>
                        <th>Complaint</th>
                        <th>Floor</th>
                        <th>Room</th>
                        <th>Status</th>
                        <th>Viewed Date</th>
                        <th>Fixed Date</th>
                    </tr>
                    <% for (Complaint c : complaints) { %>
                        <tr>
                            <td><%= c.getStudentName() %></td>
                            <td><%= c.getComplaint() %></td>
                            <td><%= c.getFloor() %></td>
                            <td><%= c.getRoom() %></td>
                            <td><span class="<%= badge(c.getStatus()) %>"><%= c.getStatus() %></span></td>
                            <td><%= c.getViewedDate() == null ? "-" : c.getViewedDate() %></td>
                            <td><%= c.getFixedDate() == null ? "-" : c.getFixedDate() %></td>
                        </tr>
                    <% } %>
                </table>
            <% } %>
        </div>
    </div>
</body>
</html>
