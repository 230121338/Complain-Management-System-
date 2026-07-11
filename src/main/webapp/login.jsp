<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Complaint Management System - Login</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="card login-card">
        <h2>Complaint System</h2>
        <p>Please sign in to continue.</p>

        <% String message = (String) request.getAttribute("message");
           if (message != null) { %>
            <div class="notice"><%= message %></div>
        <% } %>

        <% String error = (String) request.getAttribute("error");
           if (error != null) { %>
            <div class="error"><%= error %></div>
        <% } %>

        <form action="LoginServlet" method="post">
            <label for="username">Username</label>
            <input type="text" id="username" name="username" required autofocus>

            <label for="password">Password</label>
            <input type="password" id="password" name="password" required>

            <button type="submit">Login</button>
        </form>

        <p class="form-footer">Don't have an account? <a href="register.jsp">Create one</a></p>
    </div>
</body>
</html>
