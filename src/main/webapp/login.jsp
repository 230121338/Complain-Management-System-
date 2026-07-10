<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Complaint Management System - Login</title>
    <link rel="stylesheet" href="style.css">
</head>
<body class="auth-page">
    <div class="card login-card">
        <h2>Complaint System</h2>
        <p class="subtitle">Please sign in to continue.</p>

        <% String success = (String) request.getAttribute("success");
           if (success != null) { %>
            <div class="success"><%= success %></div>
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

        <p class="auth-switch">Don't have an account? <a href="register.jsp">Create account</a></p>
    </div>
</body>
</html>
