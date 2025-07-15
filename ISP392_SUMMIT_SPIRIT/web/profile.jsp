<%--
    Document   : profile
    Created on : Jun 15, 2025, 12:19:33 AM
    Author     : gmt
--%>

<%@page import="dto.UserDTO" %>
<%@page import="user.UserError" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%--<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>--%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User Profile</title>
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
            .user-dropdown {
                position: relative;
                display: inline-block;
                font-family: Arial;
            }
            .user-name {
                cursor: pointer;
                font-weight: bold;
                padding: 10px;
            }
            .dropdown-menu {
                display: none;
                position: absolute;
                top: 100%;
                left: 0;
                background-color: white;
                min-width: 160px;
                box-shadow: 0 8px 16px rgba(0,0,0,0.2);
                border-radius: 4px;
                z-index: 1000;
            }
            .dropdown-menu a {
                padding: 12px 16px;
                display: block;
                text-decoration: none;
                color: black;
            }
            .dropdown-menu a:hover {
                background-color: #f1f1f1;
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
        <div class="header">
            <div class="logo">SUMMIT SPIRIT</div>
            <a href="homepage.jsp" class="btn btn-primary me-2">Home</a>
            <a href="cart.jsp" class="btn btn-secondary me-2">Cart</a>
            <div class="user-dropdown">
                <div class="user-name" onclick="toggleMenu()">
                    <%= loginUser.getFullName()%>
                </div>
                <div id="dropdown" class="dropdown-menu">
                    <a href="profile.jsp">User Profile</a>
                    <a href="MainController?action=Logout">Logout</a>
                </div>
            </div>
            <script>
                function toggleMenu() {
                    const menu = document.getElementById("dropdown");
                    menu.style.display = menu.style.display === "block" ? "none" : "block";
                }
                document.addEventListener("click", function (event) {
                    const dropdown = document.getElementById("dropdown");
                    const userBtn = document.querySelector(".user-name");
                    if (!dropdown.contains(event.target) && !userBtn.contains(event.target)) {
                        dropdown.style.display = "none";
                    }
                });
            </script>
        </div>
        <div class="container">
            <div class="login-container">
                <h2 class="login-title">Your information</h2>
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
                    if (userError == null) {
                        userError = new UserError();
                    }
                %>
                <form action="MainController" method="POST">
                    <input type="hidden" name="action" value="EditProfile">
                    <input type="hidden" name="userID" value="<%= loginUser.getUserID()%>">
                    <div class="mb-3">
                        <label for="fullName" class="form-label">Full Name</label>
                        <input type="text" class="form-control" name="fullName" value="<%= loginUser.getFullName()%>">
                    </div>
                    <div class="mb-3">
                        <label for="address" class="form-label">Address</label>
                        <input type="text" class="form-control" name="address" value="<%= loginUser.getAddress()%>">
                    </div>
                    <div class="mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="text" class="form-control" name="email" value="<%= loginUser.getEmail()%>">
                    </div>
                    <div class="text-danger">
                        <%= userError.getEmail() %>
                    </div>
                    <div class="mb-3">
                        <label for="phone" class="form-label">Phone Number</label>
                        <input type="text" class="form-control" name="phone" value="<%= loginUser.getPhone()%>" minlength="10" maxlength="11">
                    </div>
                    <div class="text-danger">
                        <%= userError.getPhone() %>
                    </div>
                    <input type="hidden" class="form-control" name="role" value="<%= loginUser.getRole()%>">
                    <div class="d-flex justify-content-between">
                        <button type="submit" class="btn btn-primary">Edit Information</button>
                    </div>
                </form>
                <form action="changePassword.jsp" method="GET">
                    <button type="submit" name="action" value="ChangePassword" class="btn btn-secondary">Change Password</button>
                </form>
                <div class="text-danger">
                    <%= userError.getErrorMessage()%>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
