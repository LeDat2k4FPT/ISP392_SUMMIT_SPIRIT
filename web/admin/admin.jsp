<%-- 
    Document   : hanne
    Created on : May 28, 2025, 5:48:17 PM
    Author     : Hanne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="dto.UserDTO"%>

<%
    // Kiểm tra session, chặn truy cập
    UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
    if (loginUser == null || !"Admin".equalsIgnoreCase(loginUser.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }

    String currentPage = (String) request.getAttribute("page");
    if (currentPage == null) {
        currentPage = request.getParameter("page") != null ? request.getParameter("page") : "adminHome.jsp";
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Panel</title>
    <!-- Bootstrap + Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">

    <style>
        .sidebar-custom {
            background: #fff;
            min-height: 100vh;
            color: #234C45;
            border-right: 1px solid #ddd;
            padding-top: 20px;
        }
        .sidebar-custom .nav-link {
            color: #234C45;
            font-weight: 500;
            padding: 10px 15px;
        }
        .sidebar-custom .nav-link.active, .sidebar-custom .nav-link:hover {
            background: #f0f0f0;
            color: #234C45;
        }
        .top-bar-custom {
            background: #39504A;
            color: #fff;
            padding: 1rem 2rem;
            font-size: 18px;
            font-weight: bold;
        }
        .logout-btn {
            background: #39504A;
            color: #fff;
            width: 90%;
            margin: 20px auto;
            display: block;
            text-align: center;
            border-radius: 5px;
            padding: 10px;
            border: none;
            font-weight: bold;
        }
        .logout-btn:hover {
            background: #234C45;
        }
    </style>
</head>
<body>

<!-- TOP BAR -->
<div class="top-bar-custom d-flex justify-content-between align-items-center">
    <span>Hello, <%= loginUser.getFullName() %></span>
    <form action="${pageContext.request.contextPath}/LogoutController" method="post" class="m-0">
        <button class="logout-btn btn"><i class="bi bi-box-arrow-right"></i> Logout</button>
    </form>
</div>

<div class="container-fluid">
    <div class="row">
        <!-- SIDEBAR -->
        <nav class="col-md-2 sidebar-custom">
            <ul class="nav flex-column">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/admin.jsp?page=adminHome.jsp"><i class="bi bi-house"></i> Home</a>
                </li>
                </li>
    <li class="nav-item">
        <a class="nav-link <%= "viewRevenue.jsp".equals(currentPage) ? "active" : "" %>" 
           href="${pageContext.request.contextPath}/ViewRevenueController">
           <i class="bi bi-bar-chart"></i> View Revenue
        </a>
    </li>
    <li class="nav-item">
        <a class="nav-link <%= "adminManageUser.jsp".equals(currentPage) ? "active" : "" %>"
           href="${pageContext.request.contextPath}/ManageUserAccountController">
           <i class="bi bi-people"></i> Manage Users
        </a>
    </li>
    <li class="nav-item">
        <a class="nav-link <%= "manageVoucher.jsp".equals(currentPage) ? "active" : "" %>"
           href="${pageContext.request.contextPath}/ManageVoucherController">
           <i class="bi bi-ticket"></i> Manage Vouchers
        </a>
    </li>
    <li class="nav-item">
        <a class="nav-link <%= "potentialCustomer.jsp".equals(currentPage) ? "active" : "" %>"
           href="${pageContext.request.contextPath}/ManagePotentialCustomerController">
           <i class="bi bi-star"></i> Potential Customers
        </a>
    </li>
<!--    <li class="nav-item">
        <a class="nav-link <%= "updateHistory.jsp".equals(currentPage) ? "active" : "" %>"
           href="${pageContext.request.contextPath}/ManageUpdateHistoryController">
           <i class="bi bi-clock-history"></i> Update History
        </a>
    </li>-->
            </ul>
        </nav>

        <!-- MAIN CONTENT -->
        <main class="col-md-10 p-4">
            <jsp:include page="<%= currentPage %>"/>
        </main>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
