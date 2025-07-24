<%-- 
    Document   : shipHome
    Created on : Jul 24, 2025, 1:53:14 AM
    Author     : Hanne
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dto.UserDTO" %>

<%
    // Kiểm tra session người dùng
    UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
    if (loginUser == null || !"Shipper".equalsIgnoreCase(loginUser.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Lấy page cần hiển thị
    String currentPage = (String) request.getAttribute("page");
    if (currentPage == null || currentPage.isEmpty()) {
        currentPage = request.getParameter("page");
    }
    if (currentPage == null || currentPage.isEmpty()) {
        currentPage = "shipHome.jsp"; // mặc định
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Shipper Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/ship.css">
    <!-- Có thể thêm Bootstrap hoặc icon font ở đây -->
</head>
<body>

<div class="topbar">
    Hello Shipper, <%= loginUser.getFullName() %>
</div>

<div class="container-fluid">
    <div class="row">
        <!-- SIDEBAR -->
        <nav class="col-md-2 sidebar-custom">
            <ul class="nav flex-column">
                <li class="nav-item">
                    <a class="nav-link <%= "shipHome.jsp".equals(currentPage) ? "active" : "" %>" 
                       href="${pageContext.request.contextPath}/ship/shipDashboard.jsp?page=shipHome.jsp">
                       🏠 Home
                    </a>
                </li>

                <li class="nav-item">
                    <a class="nav-link <%= "shippingList.jsp".equals(currentPage) ? "active" : "" %>" 
                       href="${pageContext.request.contextPath}/ShippingListController">
                       📦 Orders to Ship
                    </a>
                </li>

                <li class="nav-item">
                    <a class="nav-link <%= "deliveryProof.jsp".equals(currentPage) ? "active" : "" %>" 
                       href="${pageContext.request.contextPath}/DeliveryListController">
                       📸 Delivery Proof
                    </a>
                </li>
                <li class="nav-item">
    <a class="nav-link <%= "historyShipping.jsp".equals(currentPage) ? "active" : "" %>" 
       href="${pageContext.request.contextPath}/DeliveryHistoryController">
       🧾 Delivery History
    </a>
</li>

            </ul>

            <a href="${pageContext.request.contextPath}/LogoutController" class="logout-btn">Logout</a>
        </nav>

        <!-- MAIN CONTENT -->
        <main class="col-md-10 p-4">
            <jsp:include page="<%= currentPage %>" />
        </main>
    </div>
</div>

</body>
</html>
