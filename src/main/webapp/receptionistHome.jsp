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
    String query = request.getParameter("q");
    boolean searching = query != null && !query.trim().isEmpty();
    List<Complaint> complaints = searching
            ? new ComplaintDAO().searchByStudentName(query.trim())
            : new ComplaintDAO().findAll();
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
        <div class="topbar-user">
            <img class="avatar" src="ProfileImageServlet?id=<%= user.getUserId() %>" alt="Profile picture">
            <span class="topbar-name"><%= user.getFullname() %></span>
            <a href="LogoutServlet">Logout</a>
        </div>
    </div>
    <div class="container">
        <div class="card">
            <h2>All Complaints (View Only)</h2>

            <div class="toolbar">
                <form class="search-form" action="receptionistHome.jsp" method="get">
                    <input type="text" name="q" placeholder="Search by student name"
                           value="<%= searching ? query.trim().replace("\"", "&quot;") : "" %>">
                    <button type="submit">Search</button>
                    <% if (searching) { %>
                        <a class="btn" href="receptionistHome.jsp">Clear</a>
                    <% } %>
                </form>
                <a class="btn btn-green" href="DownloadComplaintsServlet<%= searching ? "?q=" + java.net.URLEncoder.encode(query.trim(), "UTF-8") : "" %>">Download PDF</a>
            </div>

            <% if (complaints.isEmpty()) { %>
                <p class="empty"><%= searching ? "No complaints match that student name." : "There are no complaints." %></p>
            <% } else { %>
                <table>
                    <tr>
                        <th>Student</th>
                        <th>Complaint</th>
                        <th>Picture</th>
                        <th>Block</th>
                        <th>Room</th>
                        <th>Status</th>
                        <th>Viewed Date</th>
                        <th>Fixed Date</th>
                        <th>Action</th>
                    </tr>
                    <% for (Complaint c : complaints) { %>
                        <tr>
                            <td><%= c.getStudentName() %></td>
                            <td><%= c.getComplaint() %></td>
                            <td>
                                <a href="ComplaintImageServlet?id=<%= c.getComplaintId() %>" target="_blank">
                                    <img class="complaint-thumb" src="ComplaintImageServlet?id=<%= c.getComplaintId() %>" alt="Complaint picture">
                                </a>
                            </td>
                            <td><%= c.getBlock() %></td>
                            <td><%= c.getRoom() %></td>
                            <td><span class="<%= badge(c.getStatus()) %>"><%= c.getStatus() %></span></td>
                            <td><%= c.getViewedDate() == null ? "-" : c.getViewedDate() %></td>
                            <td><%= c.getFixedDate() == null ? "-" : c.getFixedDate() %></td>
                            <td>
                                <form class="inline-form" action="DeleteComplaintServlet" method="post"
                                      onsubmit="return confirm('Delete this complaint permanently?');">
                                    <input type="hidden" name="complaintId" value="<%= c.getComplaintId() %>">
                                    <button type="submit" class="btn-red">Delete</button>
                                </form>
                            </td>
                        </tr>
                    <% } %>
                </table>
            <% } %>
        </div>
    </div>
</body>
</html>
