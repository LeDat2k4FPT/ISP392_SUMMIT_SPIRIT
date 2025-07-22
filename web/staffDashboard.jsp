<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="dto.UserDTO" %>
<%
    UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
    if (loginUser == null || !"Staff".equals(loginUser.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }

    String currentPage = (String) request.getAttribute("page");
    if (currentPage == null) {
        currentPage = request.getParameter("page") != null ? request.getParameter("page") : "staff/home.jsp";
    }

    String productIDParam = request.getParameter("productID");
    if (productIDParam == null) productIDParam = "";
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Staff Panel</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/staff.css">
</head>
<body>

    <!-- TOP BAR -->
    <div class="top-bar-custom d-flex justify-content-between align-items-center">
        <span>Hello, <%= loginUser.getFullName() %></span>
    </div>

    <!-- MAIN LAYOUT -->
    <div class="main-layout">
        <!-- SIDEBAR -->
        <nav class="sidebar-custom">
            <ul class="nav flex-column">
                <li class="nav-item">
                    <a class="nav-link <%= "staff/home.jsp".equals(currentPage) ? "active" : "" %>" 
                       href="staffDashboard.jsp?page=staff/home.jsp">
                        <i class="bi bi-house"></i> Home
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link <%= "staff/orderlist.jsp".equals(currentPage) ? "active" : "" %>" 
                       href="staffDashboard.jsp?page=staff/orderlist.jsp">
                        <i class="bi bi-list-check"></i> Orders
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link <%= "staff/productlist.jsp".equals(currentPage) ? "active" : "" %>" 
                       href="ProductListController">
                        <i class="bi bi-list-check"></i> Product
                    </a> 
                </li>
            </ul>
            <div class="logout-container">
                <form action="LogoutController" method="post" class="m-0">
                    <button class="logout-btn btn"><i class="bi bi-power"></i> Logout</button>
                </form>
            </div>
        </nav>

        <!-- CONTENT AREA -->
        <main class="main-content">
            <jsp:include page="<%= currentPage %>">
                <jsp:param name="productID" value="<%= productIDParam %>" />
            </jsp:include>
        </main>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
