<%-- 
    Document   : admin
    Created on : May 28, 2025, 5:48:17 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="dto.UserDTO"%>
<%
    UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
    if (loginUser == null || !"Admin".equals(loginUser.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }
    String name = loginUser.getFullName();
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Admin Dashboard</title>
        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
        <link href="https://fonts.googleapis.com/css2?family=Kumbh+Sans&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="../css/admin.css">
        <style>
            .sidebar-custom {
                background: #fff;
                min-height: 100vh;
                color: #234C45;
                display: flex;
                flex-direction: column;
                justify-content: space-between;
                padding: 0;
            }
            .sidebar-custom .nav {
                background: transparent;
                box-shadow: none;
                border-radius: 0;
                padding: 32px 0 0 0;
            }
            .sidebar-custom .nav-link {
                color: #234C45;
                background: transparent;
                border-radius: 0;
                margin: 0 0 16px 0;
                font-weight: 500;
                font-size: 18px;
                padding: 8px 24px;
                transition: background 0.2s, color 0.2s;
            }
            .sidebar-custom .nav-link.active, .sidebar-custom .nav-link:hover {
                background: #f0f0f0;
                color: #234C45 !important;
            }
            .top-bar-custom {
                background: #39504A;
                color: #fff;
                padding: 1.2rem 2rem;
                font-weight: bold;
                font-size: 20px;
            }
            .logout-btn {
                background: #39504A;
                color: #fff;
                border: none;
                width: 90%;
                margin: 24px auto 16px auto;
                padding: 12px 0;
                border-radius: 6px;
                font-weight: bold;
                display: block;
                font-size: 16px;
            }
            .logout-btn:hover {
                background: #234C45;
            }
            body {
                background-color: #f7f7f7;
            }
            .main-content {
                background: #f7f7f7;
                min-height: 100vh;
                padding: 48px 48px 0 48px;
            }
        </style>
        <script>
            function loadContent(page, msg, type = 'success') {
                // Lấy context path từ URL
                var pathArr = window.location.pathname.split('/');
                var contextPath = pathArr.length > 1 ? '/' + pathArr[1] : '';
                // Nếu page đã bắt đầu bằng '/' thì không thêm, nếu không thì thêm '/'
                var url = page.startsWith('/') ? contextPath + page : contextPath + '/' + page;
                fetch(url)
                    .then(res => res.text())
                    .then(html => {
                        const mainContent = document.getElementById("main-content");
                        let alertBox = "";
                        if (msg) {
                            alertBox = '<div class="alert alert-' + type + ' mt-2">' + msg + '</div>';
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
    <body style="background-color: #f0f0f0;">
        <div class="top-bar-custom d-flex justify-content-between align-items-center">
            <span>Hello, <%= loginUser.getFullName() %></span>
        </div>
        <div class="container-fluid">
            <div class="row flex-nowrap">
                <nav class="col-auto col-md-3 col-xl-2 px-sm-2 px-0 sidebar-custom d-flex flex-column align-items-stretch py-3">
                    <ul class="nav nav-pills flex-column mb-auto gap-2">
                        <li class="nav-item">
                            <button class="nav-link text-start w-100" onclick="loadContent('adminHome.jsp')"><i class="bi bi-house"></i> Home</button>
                        </li>
                        <li>
                            <button class="nav-link text-start w-100" onclick="loadContent('viewRevenue.jsp')"><i class="bi bi-bar-chart"></i> View Revenue</button>
                        </li>
                        <li>
                            <button class="nav-link text-start w-100" onclick="loadContent('MainController?action=ManageUserAccount')"><i class="bi bi-people"></i> Manage User Account</button>
                        </li>
                        <li>
                            <button class="nav-link text-start w-100" onclick="loadContent('ManageVoucherController')"><i class="bi bi-ticket"></i> Manage Vouchers</button>
                        </li>
                        <li>
                            <button class="nav-link text-start w-100" onclick="loadContent('ManagePotentialCustomerController')"><i class="bi bi-star"></i> Potential Customers</button>
                        </li>
                        <li>
                            <button class="nav-link text-start w-100" onclick="loadContent('ManageUpdateHistoryController')"><i class="bi bi-clock-history"></i> Update History</button>
                        </li>
                    </ul>
                    <form action="LogoutController" method="post" class="mt-auto">
                        <button class="logout-btn mt-3"><i class="bi bi-box-arrow-right"></i> Logout</button>
                    </form>
                </nav>
                <main id="main-content" class="col py-4 main-content">
                    <h2>Welcome to Admin Dashboard</h2>
                    <p>Select a menu on the left to manage the system.</p>
                </main>
            </div>
        </div>
        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
