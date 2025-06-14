<%@ page import="dto.UserDTO" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User Page</title>
        <style>
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
        <div>
            <a href="user.jsp" class="btn btn-primary me-2">Home</a>
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
        <%
            String message = (String) request.getAttribute("MESSAGE");
            if (message == null) {
                message = "";
            }
        %>
        <%= message%>
    </body>
</html>
