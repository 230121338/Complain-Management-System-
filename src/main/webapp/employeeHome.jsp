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
    if (user == null || !"Employee".equals(user.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }
    List<Complaint> complaints = new ComplaintDAO().findAll();
%>
<!DOCTYPE html>
<html>
<head>
    <title>Employee Home</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="topbar">
        <h1>Complaint System &mdash; Employee</h1>
        <nav class="nav">
            <a class="active" href="employeeHome.jsp">All Complaints</a>
            <a href="LogoutServlet">Logout</a>
        </nav>
    </div>
    <div class="container">
        <div class="card">
            <h2>All Complaints</h2>

            <% if (complaints.isEmpty()) { %>
                <p class="empty">There are no complaints.</p>
            <% } else { %>
                <table>
                    <tr>
                        <th>Student</th>
                        <th>Complaint</th>
                        <th>Block</th>
                        <th>Room</th>
                        <th>Status</th>
                        <th>Action</th>
                    </tr>
                    <% for (Complaint c : complaints) { %>
                        <tr>
                            <td><%= c.getStudentName() %></td>
                            <td><%= c.getComplaint() %></td>
                            <td><%= c.getFloor() %></td>
                            <td><%= c.getRoom() %></td>
                            <td><span class="<%= badge(c.getStatus()) %>"><%= c.getStatus() %></span></td>
                            <td>
                                <% if ("Not Fixed".equals(c.getStatus())) { %>
                                    <form class="inline-form" action="ViewComplaintServlet" method="post">
                                        <input type="hidden" name="complaintId" value="<%= c.getComplaintId() %>">
                                        <button type="submit">View</button>
                                    </form>
                                <% } %>
                                <% if (!"Fixed".equals(c.getStatus())) { %>
                                    <form class="inline-form" action="FixComplaintServlet" method="post">
                                        <input type="hidden" name="complaintId" value="<%= c.getComplaintId() %>">
                                        <button type="submit" class="btn-green">Fix</button>
                                    </form>
                                <% } else { %>
                                    &mdash;
                                <% } %>
                            </td>
                        </tr>
                    <% } %>
                </table>
            <% } %>
        </div>
    </div>
</body>
</html>
