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
    <title>Student Home</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="topbar">
        <h1>Complaint System &mdash; Student</h1>
        <nav class="nav">
            <a class="active" href="studentHome.jsp">Home</a>
            <a href="complaint.jsp">New Complaint</a>
            <a href="myComplaints.jsp">My Complaints</a>
            <a href="LogoutServlet">Logout</a>
        </nav>
    </div>
    <div class="container">
        <div class="card">
            <h2>Welcome, <%= user.getFullname() %></h2>
            <p>What would you like to do?</p>
            <div class="menu">
                <a class="btn" href="complaint.jsp">New Complaint</a>
                <a class="btn" href="myComplaints.jsp">My Complaints</a>
            </div>
        </div>
    </div>
</body>
</html>
