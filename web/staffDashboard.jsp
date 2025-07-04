<%-- 
    Document   : staffDashboard
    Created on : Jun 18, 2025
    Author     : Admin
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dto.UserDTO" %>
<%
    UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
    if (user == null || !"Staff".equals(user.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Staff Dashboard</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <link href="https://fonts.googleapis.com/css2?family=Kumbh+Sans&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/staff.css">
    <script>
        function loadContent(page, msg, type = 'success') {
            fetch(page)
                .then(res => res.text())
                .then(html => {
                    const mainContent = document.getElementById("main-content");
                    let alertBox = "";
                    if (msg) {
                        alertBox = '<div class="alert alert-' + type + '">' + msg + '</div>';
                    }
                    mainContent.innerHTML = alertBox + html;

                    setTimeout(() => {
                        const alert = document.querySelector(".alert");
                        if (alert)
                            alert.remove();
                    }, 3000);
                });
        }

        window.addEventListener("DOMContentLoaded", () => {
            const urlParams = new URLSearchParams(window.location.search);
            const page = urlParams.get("page");
            const msg = urlParams.get("msg");
            const type = urlParams.get("type") || "success";
            if (page) {
                loadContent(page, msg, type);
            }
        });
    </script>
</head>
<body>
    <div class="top-bar">
        <span>Hello, <%= user.getFullName() %></span>         
    </div>
    <div class="layout">
        <aside class="sidebar">
            <nav class="menu">
                <button onclick="loadContent('staff/home.jsp')">Home</button>
                <button onclick="loadContent('ProductListController')">Product</button>
                <button onclick="loadContent('staff/orderlist.jsp')">Order</button>
            </nav>
            <form action="LogoutController" method="post">
                <button class="logout-btn">Logout</button>
            </form>
        </aside>
        <main id="main-content" class="main-content">
            <h2>Welcome to Staff Dashboard</h2>
            <p>Select a menu on the left to manage the system.</p>
        </main>
    </div>
</body>
</html>
