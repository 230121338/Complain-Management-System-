<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="tut.ac.za.complaint.model.User" %>
<%
    User user = (session == null) ? null : (User) session.getAttribute("user");
    if (user == null || !"Student".equals(user.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>New Complaint</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="topbar">
        <h1>Complaint System &mdash; Student</h1>
        <nav class="nav">
            <a href="studentHome.jsp">Home</a>
            <a class="active" href="complaint.jsp">New Complaint</a>
            <a href="myComplaints.jsp">My Complaints</a>
            <a href="LogoutServlet">Logout</a>
        </nav>
    </div>
    <div class="container">
        <div class="card">
            <h2>New Complaint</h2>

            <% String error = (String) request.getAttribute("error");
               if (error != null) { %>
                <div class="error"><%= error %></div>
            <% } %>

            <form action="ComplaintServlet" method="post">
                <label for="complaint">Complaint</label>
                <textarea id="complaint" name="complaint" required></textarea>

                <label for="floor">Block</label>
                <input type="text" id="floor" name="floor" required>

                <label for="room">Room</label>
                <input type="text" id="room" name="room" required>

                <button type="submit">Submit</button>
                <a class="btn" href="studentHome.jsp">Back</a>
            </form>
        </div>
    </div>
</body>
</html>
