<%--
    Document   : resetPassword
    Created on : Jun 15, 2025, 6:59:41 PM
    Author     : gmt
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%--<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>--%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Reset Password</title>
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
        <div class="form-container">
            <h2>Reset Your Password</h2>
            <%
                   String message = (String) request.getAttribute("MESSAGE");
                   if (message != null && !message.isEmpty()) {
            %>
            <p style="color:red; text-align:center;"><%= message %></p>
            <%
                }
            %>
            <form action="MainController" method="POST">
                <input type="hidden" name="action" value="ResetPassword" />
                <label>New Password:</label>
                <input type="password" name="newPassword" required />
                <label>Confirm New Password:</label>
                <input type="password" name="confirmNewPassword" required />
                <input type="submit" value="Update Password" />
                <div class="text-center mt-3">
                    <a href="profile.jsp" class="back-link">Back to User Information Page</a>
                </div>
            </form>
        </div>
    </body>
</html>
