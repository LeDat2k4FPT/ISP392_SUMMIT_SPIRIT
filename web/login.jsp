<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SUMMIT SPIRIT Page</title>
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
                <h2 class="login-title">Login</h2>
                <%-- THÔNG BÁO LỖI --%>
                <%
                    String message = (String) request.getAttribute("MESSAGE");
                    if (message != null && !message.isEmpty()) {
                %>
                <p style="color:red; text-align:center;"><%= message %></p>
                <%
                    }
                %>
                <form id="login-form" action="MainController" method="POST">
                    <div class="mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="email" class="form-control" id="email" name="email" required>
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">Password</label>
                        <input type="password" class="form-control" id="password" name="password" required>
                    </div>
                    <div class="g-recaptcha" data-sitekey="6LeToWArAAAAABMEYYO1tLmV18YTlfhk7C8Zt2GW"></div>
                    <div style="color: red" id="error"></div>
                    <div class="d-flex justify-content-between">
                        <button type="submit" name="action" value="Login" class="btn btn-primary">Login</button>
                        <button type="reset" class="btn btn-secondary">Reset</button>
                    </div>
                    <div class="register-link text-center mt-3">
                        <p>Don't have an account? <a href="createUser.jsp">Register here</a></p>
                        <p><a href="forgotPassword.jsp">Forgot Password</a></p>
                    </div>
                </form>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://www.google.com/recaptcha/api.js" async defer></script>
        <script type="text/javascript">
            document.getElementById("login-form").addEventListener("submit", function (e) {
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
