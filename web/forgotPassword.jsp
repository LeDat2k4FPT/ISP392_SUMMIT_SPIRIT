<%--
    Document   : forgotPassword
    Created on : Jun 14, 2025, 5:16:04 PM
    Author     : gmt
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Forgot Password</title>
    <link href="https://fonts.googleapis.com/css2?family=Kumbh+Sans&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="css/forgotpassword.css">
</head>
<body>
    <div class="logo" >  
            <a href="homepage.jsp">
                <img src="image/summit_logo.png" alt="Logo">
            </a>
        </div>  

    <div class="forgot-box">
        <h2 class="forgot-title">Forgot Password</h2>
        <p class="text-muted">Enter your email and we’ll send you the OTP code</p>

        <form action="MainController" method="POST">
            <%
                String message = (String) request.getAttribute("MESSAGE");
                if (message != null && !message.isEmpty()) {
            %>
            <p class="text-danger text-center"><%= message %></p>
            <%
                }
            %>

            <div class="mb-3">
                <label for="email" class="form-label">Email</label>
                <input type="email" class="form-control" id="email" name="email" required>
            </div>

            <button type="submit" name="action" value="ForgotPassword" class="btn btn-forgot w-100">Send OTP</button>

            <div class="text-center mt-3">
                <a href="login.jsp" class="back-link">Back to Login</a>
            </div>
        </form>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
