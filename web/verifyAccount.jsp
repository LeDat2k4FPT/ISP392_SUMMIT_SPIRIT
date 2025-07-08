<%--
    Document   : verifyAccount
    Created on : Jun 17, 2025, 1:55:13 PM
    Author     : gmt
--%>

<%@page import="dto.UserDTO" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%--<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>--%>
<%
    UserDTO pendingUser = (UserDTO) session.getAttribute("PENDING_USER");
    if (pendingUser == null || !"User".equals(pendingUser.getRole())) {
        response.sendRedirect("createUser.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Verify Account</title>
        <link href="https://fonts.googleapis.com/css2?family=Kumbh+Sans&display=swap" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="css/verifyaccount.css">
    </head>
    <body>
        <div class="otp-box">
            <h2 class="otp-title">Verify Account</h2>
            <p class="text-muted">Enter the code sent to email <%= pendingUser.getEmail()%> to verify account</p>
            <form action="MainController" method="POST">
                <div class="mb-3 text-start">
                    <label for="otp" class="form-label">OTP Code</label>
                    <input type="text" class="form-control" id="otp" name="otp" required>
                </div>
                <button type="submit" name="action" value="VerifyAccount" class="btn btn-verify w-100">Verify</button>
                <div class="text-center mt-3">
                    <a href="createUser.jsp" class="back-link">Back to Create Account Page</a>
                </div>
                <%
                    String message = (String) request.getAttribute("MESSAGE");
                    if (message != null && !message.isEmpty()) {
                %>
                <p class="text-danger text-center mt-2"><%= message %></p>
                <%
                    }
                %>
            </form>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
