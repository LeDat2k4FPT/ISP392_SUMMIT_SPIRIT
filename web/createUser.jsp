<%--
    Document   : createUser
    Created on : May 28, 2025, 5:04:58 PM
    Author     : Admin
--%>

<%@page import="user.UserError"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register Account</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                background-color: #f8f9fa;
            }
            .login-container {
                max-width: 400px;
                margin: 100px auto;
                padding: 20px;
                background-color: white;
                border-radius: 10px;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
            }
            .login-title {
                text-align: center;
                margin-bottom: 20px;
                color: #333;
            }
            .form-control {
                margin-bottom: 15px;
            }
            .btn-login {
                width: 100%;
                background-color: #007bff;
                border: none;
            }
            .btn-login:hover {
                background-color: #0056b3;
            }
            .register-link {
                text-align: center;
                margin-top: 15px;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="login-container">
                <h2 class="login-title">Welcome to SUMMIT SPIRIT</h2>
                <%
                    String message = (String) request.getAttribute("MESSAGE");
                    if (message != null && !message.isEmpty()) {
                %>
                <p style="color:red; text-align:center;"><%= message %></p>
                <%
                    }
                %>
                <%
                    UserError userError = (UserError) request.getAttribute("USER_ERROR");
                    if (userError != null) {
                %>
                <div style="color:red"><%= userError.getErrorMessage() %></div>
                <%
                    } else {
                        userError = new UserError();
                    }
                %>
                <form id="create-form" action="MainController" method="POST">
                    <div class="mb-3">
                        <label for="fullName" class="form-label">Full Name</label>
                        <input type="text" class="form-control" id="fullName" name="fullName" value="<%= request.getAttribute("fullName") != null ? request.getAttribute("fullName") : "" %>" required>
                    </div>
                    <div class="mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="email" class="form-control" id="email" name="email" value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>" required>
                    </div>
                    <div class="text-danger">
                        <%= userError.getEmail() %>
                    </div>
                    <div class="mb-3">
                        <label for="address" class="form-label">Address</label>
                        <input type="address" class="form-control" id="address" name="address" value="<%= request.getAttribute("address") != null ? request.getAttribute("address") : "" %>" required>
                    </div>
                    <div class="mb-3">
                        <label for="phone" class="form-label">Phone Number</label>
                        <input type="text" class="form-control" id="phone" name="phone"  value="<%= request.getAttribute("phone") != null ? request.getAttribute("phone") : "" %>" minlength="10" maxlength="11" required>
                    </div>
                    <div class="text-danger">
                        <%= userError.getPhone() %>
                    </div>
                    <input type="hidden" name="role" value="User">
                    <div class="mb-3">
                        <label for="password" class="form-label">Password</label>
                        <input type="password" class="form-control" id="password" name="password"  value="<%= request.getAttribute("password") != null ? request.getAttribute("password") : "" %>" required>
                    </div>
                    <div class="text-danger">
                        <%= userError.getPassword() %>
                    </div>
                    <div class="mb-3">
                        <label for="confirm" class="form-label">Confirm Password</label>
                        <input type="password" class="form-control" id="confirm" name="confirm"  value="<%= request.getAttribute("confirm") != null ? request.getAttribute("confirm") : "" %>" required>
                    </div>
                    <div class="text-danger">
                        <%= userError.getConfirm() %>
                    </div>
                    <div class="g-recaptcha" data-sitekey="6LeToWArAAAAABMEYYO1tLmV18YTlfhk7C8Zt2GW"></div>
                    <div style="color: red" id="error"></div>
                    <div class="d-flex justify-content-between">
                        <button type="submit" name="action" value="CreateUser" class="btn btn-primary">Create Account</button>
                    </div>
                    <div class="register-link text-center mt-3">
                        <p>Already have an account? <a href="login.jsp">Login</a></p>
                    </div>
                </form>
                <div class="text-danger">
                    <%= userError.getErrorMessage()%>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://www.google.com/recaptcha/api.js" async defer></script>
        <script type="text/javascript">
            document.getElementById("create-form").addEventListener("submit", function (e) {
                var error = document.getElementById("error");
                var response = grecaptcha.getResponse();
                if (response.length === 0) {
                    e.preventDefault();
                    error.textContent = "Please verify that you are not a robot.";
                } else {
                    error.textContent = "";
                }
            });
        </script>
    </body>
</html>