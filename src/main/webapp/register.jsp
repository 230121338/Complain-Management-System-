<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Complaint Management System - Register</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="card login-card">
        <h2>Create Account</h2>
        <p>Register as a student, employee or receptionist.</p>

        <% String error = (String) request.getAttribute("error");
           if (error != null) { %>
            <div class="error"><%= error %></div>
        <% } %>

        <form action="RegisterServlet" method="post">
            <label for="fullname">Full Name</label>
            <input type="text" id="fullname" name="fullname" required autofocus>

            <label for="username">Username</label>
            <input type="text" id="username" name="username" required>

            <label for="password">Password</label>
            <input type="password" id="password" name="password" required>

            <label for="role">Role</label>
            <select id="role" name="role" required>
                <option value="Student">Student</option>
                <option value="Employee">Employee</option>
                <option value="Receptionist">Receptionist</option>
            </select>

            <button type="submit">Register</button>
        </form>

        <p class="form-footer">Already have an account? <a href="login.jsp">Sign in</a></p>
    </div>
</body>
</html>
