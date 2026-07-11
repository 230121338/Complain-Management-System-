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
    if (user == null || !"Student".equals(user.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }
    List<Complaint> complaints = new ComplaintDAO().findByStudent(user.getUserId());
%>
<!DOCTYPE html>
<html>
<head>
    <title>My Complaints</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="topbar">
        <h1>Complaint System &mdash; Student</h1>
        <div class="topbar-user">
            <img class="avatar" src="ProfileImageServlet?id=<%= user.getUserId() %>" alt="Profile picture">
            <span class="topbar-name"><%= user.getFullname() %></span>
            <a href="LogoutServlet">Logout</a>
        </div>
    </div>
    <div class="container">
        <div class="card">
            <h2>My Complaints</h2>
            <div class="menu">
                <a class="btn" href="complaint.jsp">New Complaint</a>
                <a class="btn" href="studentHome.jsp">Home</a>
            </div>

            <% if (complaints.isEmpty()) { %>
                <p class="empty">You have not submitted any complaints yet.</p>
            <% } else { %>
                <table>
                    <tr>
                        <th>Complaint</th>
                        <th>Picture</th>
                        <th>Block</th>
                        <th>Room</th>
                        <th>Status</th>
                        <th>Date Reported</th>
                    </tr>
                    <% for (Complaint c : complaints) { %>
                        <tr>
                            <td><%= c.getComplaint() %></td>
                            <td>
                                <a href="ComplaintImageServlet?id=<%= c.getComplaintId() %>" target="_blank">
                                    <img class="complaint-thumb" src="ComplaintImageServlet?id=<%= c.getComplaintId() %>" alt="Complaint picture">
                                </a>
                            </td>
                            <td><%= c.getBlock() %></td>
                            <td><%= c.getRoom() %></td>
                            <td><span class="<%= badge(c.getStatus()) %>"><%= c.getStatus() %></span></td>
                            <td><%= c.getDateReported() %></td>
                        </tr>
                    <% } %>
                </table>
            <% } %>
        </div>
    </div>
</body>
</html>
