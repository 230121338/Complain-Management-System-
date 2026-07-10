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
        <nav class="nav">
            <a href="studentHome.jsp">Home</a>
            <a href="complaint.jsp">New Complaint</a>
            <a class="active" href="myComplaints.jsp">My Complaints</a>
            <a href="LogoutServlet">Logout</a>
        </nav>
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
                        <th>Block</th>
                        <th>Room</th>
                        <th>Status</th>
                        <th>Date Reported</th>
                    </tr>
                    <% for (Complaint c : complaints) { %>
                        <tr>
                            <td><%= c.getComplaint() %></td>
                            <td><%= c.getFloor() %></td>
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
