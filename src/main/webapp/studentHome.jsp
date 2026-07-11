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
        <div class="topbar-user">
            <img class="avatar" src="ProfileImageServlet?id=<%= user.getUserId() %>" alt="Profile picture">
            <span class="topbar-name"><%= user.getFullname() %></span>
            <a href="LogoutServlet">Logout</a>
        </div>
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
        <div class="card">
            <h2>Profile Picture</h2>
            <p>Add or change the picture shown in your navigation bar.</p>
            <img class="avatar-large" src="ProfileImageServlet?id=<%= user.getUserId() %>" alt="Current profile picture">
            <form action="ProfilePictureServlet" method="post" enctype="multipart/form-data">
                <label for="profileImage">Choose a picture</label>
                <input type="file" id="profileImage" name="profileImage" accept="image/*" required>
                <button type="submit">Upload</button>
            </form>
        </div>
    </div>
</body>
</html>
