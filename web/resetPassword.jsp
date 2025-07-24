<%--
    Document   : resetPassword
    Created on : Jun 15, 2025, 6:59:41 PM
    Author     : gmt
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="dto.UserDTO"%>
<%
    UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
    String backLink = "login.jsp";
    if (loginUser != null) {
        if ("Admin".equalsIgnoreCase(loginUser.getRole())) {
            backLink = "admin/admin.jsp?page=adminProfile.jsp";
        } else if ("User".equalsIgnoreCase(loginUser.getRole())) {
            backLink = "profile.jsp";
        } else if ("Staff".equalsIgnoreCase(loginUser.getRole())) {
            backLink = "staffProfile.jsp"; // nếu có
        }
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Reset Password</title>
        <link rel="stylesheet" type="text/css" href="css/resetpassword.css">
    </head>
    <body>
        <div class="form-container">
            <h2>Reset Your Password</h2>
            <%
               String message = (String) request.getAttribute("MESSAGE");
               if (message != null && !message.isEmpty()) {
            %>
            <p class="error text-center"><%= message %></p>
            <%
                }
            %>
            <form action="MainController" method="POST">
                <input type="hidden" name="action" value="ResetPassword" />

                <label>Current Password:</label>
                <input type="password" name="currentPassword" required />

                <label>New Password:</label>
                <input type="password" name="newPassword" required />

                <label>Confirm New Password:</label>
                <input type="password" name="confirmNewPassword" required />

                <input type="submit" value="Update Password" />
                
                <div class="text-center mt-3">
                    <a href="<%= backLink %>" class="back-link">Back to Your Profile</a>
                </div>
            </form>
        </div>
    </body>
</html>
