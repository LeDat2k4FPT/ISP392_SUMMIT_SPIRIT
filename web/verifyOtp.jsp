<%--
    Document   : verifyOtp
    Created on : Jun 15, 2025, 6:46:08 PM
    Author     : gmt
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%--<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>--%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Verify OTP</title>
    </head>
    <body>
        <h2>Enter OTP sent to your email</h2>
        <form action="MainController" method="POST">
            <input type="text" name="otp" required />
            <button type="submit" name="action" value="VerifyOtp">Verify</button>
        </form>
        <%
            String message = (String) request.getAttribute("MESSAGE");
            if (message != null && !message.isEmpty()) {
        %>
        <p style="color:red; text-align:center;"><%= message %></p>
        <%
            }
        %>
    </body>
</html>
