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
        <title>Admin Dashboard</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
        <link href="https://fonts.googleapis.com/css2?family=Kumbh+Sans&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="css/admin.css"/>
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
        <script>
            function confirmLogout() {
                if (confirm("Are you sure you want to logout?")) {
                    window.location.href = "MainController?action=Logout";
                }
            }
        </script>
    </head>
    <body>
        <div class="top-bar">
            <span>Hello, <%= name %></span>
        </div>

        <div class="layout">
            <aside class="sidebar">
                <nav class="menu">
                    <button onclick="loadContent('manageUser')">Manage User Accounts</button>
                    <button onclick="loadContent('report.jsp')" >Revenues</button>                   
                </nav>  
                <button onclick="confirmLogout()" class="logout-btn">Logout</button>
            </aside>      
            <main id="main-content" class="main-content">
                <h2>Welcome to Admin Dashboard</h2>
                <p>Select a menu on the left to manage the system.</p>
            </main>
        </div>
    </body>
</html>
