<%--
    Document   : changePassword
    Created on : Jun 15, 2025, 4:33:25 PM
    Author     : gmt
--%>

<%@page import="dto.UserDTO" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%--<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>--%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Change Password</title>
        <style>
            .form-container {
                max-width: 400px;
                margin: 50px auto;
                font-family: Arial;
            }
            input[type=password], input[type=submit] {
                width: 100%;
                padding: 10px;
                margin: 8px 0;
            }
            .error {
                color: red;
            }
            .success {
                color: green;
            }
        </style>
    </head>
    <body>
        <%
            UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
            if (loginUser == null || !"User".equals(loginUser.getRole())) {
                response.sendRedirect("login.jsp");
                return;
            }
        %>
        <div class="form-container">
            <h2>Change Password</h2>
            <%
                String message = (String) request.getAttribute("MESSAGE");
                if (message != null && !message.isEmpty()) {
            %>
            <p style="color:red; text-align:center;"><%= message %></p>
            <%
                }
            %>
            <form action="MainController" method="POST">
                <input type="hidden" name="action" value="ChangePassword" />
                <label>Current Password:</label>
                <input type="password" name="currentPassword" required />
                <label>New Password:</label>
                <input type="password" name="newPassword" required />
                <label>Confirm New Password:</label>
                <input type="password" name="confirmNewPassword" required />
                <input type="submit" value="Update Password" />
            </form>
        </div>
    </body>
</html>
