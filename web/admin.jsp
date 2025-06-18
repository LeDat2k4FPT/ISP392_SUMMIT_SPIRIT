<%-- 
    Document   : admin
    Created on : May 28, 2025, 5:48:17 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="dto.UserDTO"%>
<%
    UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
    String name = loginUser != null ? loginUser.getFullName() : "Guest";
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <div class="header">
            Hello, <%= name %>
        </div>
        <h1>Admin Tools:</h1>
        <script>
            function confirmLogout() {
                if (confirm("Are you sure you want to logout?")) {
                    window.location.href = "MainController?action=Logout";
                }
            }
        </script>
        <ul>
            <li><a href="manageUser">Manage User Accounts</a></li>
            <!-- có thể thêm các liên kết khác nếu có nhiều công cụ admin -->
            <li><a href="#" onclick="confirmLogout()">Logout</a></li>
        </ul>
    </body>
</html>
