<%-- 
    Document   : createUserAccount
    Created on : Jun 17, 2025, 3:02:08 PM
    Author     : Hanne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Create New User Account</title>
    </head>
    <body>

<h2>Create New User Account</h2>

<% String msg = (String) request.getAttribute("message"); %>
<% if (msg != null && !msg.isEmpty()) { %>
    <p class="error"><%= msg %></p>
<% } %>

<form action="AdminCreateUserController" method="post">
    <label>Full Name:</label>
    <input type="text" name="fullName" value="<%= request.getAttribute("fullName") != null ? request.getAttribute("fullName") : "" %>" required>

    <label>Email:</label>
    <input type="email" name="email" value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>" required>

    <label>Address:</label>
    <input type="text" name="address" value="<%= request.getAttribute("address") != null ? request.getAttribute("address") : "" %>" required>

    <label>Phone:</label>
    <input type="text" name="phone" value="<%= request.getAttribute("phone") != null ? request.getAttribute("phone") : "" %>" required>

    <label>Password:</label>
    <input type="password" name="password" value="<%= request.getAttribute("password") != null ? request.getAttribute("password") : "" %>" required>

    <label>Confirm Password:</label>
    <input type="password" name="confirm" value="<%= request.getAttribute("confirm") != null ? request.getAttribute("confirm") : "" %>" required>

    <label>Role:</label>
    <select name="role">
        <option value="User" <%= "User".equals(request.getAttribute("role")) ? "selected" : "" %>>User</option>
        <option value="Staff" <%= "Staff".equals(request.getAttribute("role")) ? "selected" : "" %>>Staff</option>
        <option value="Admin" <%= "Admin".equals(request.getAttribute("role")) ? "selected" : "" %>>Admin</option>
    </select>

    <div class="btns">
        <input type="submit" value="Create">
        <button type="button" onclick="window.location.href='manageUser'">Cancel</button>
    </div>
</form>

</body>
</html>
