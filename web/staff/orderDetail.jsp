<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="dto.OrderDTO" %>
<%@ page import="dto.OrderDetailDTO" %>
<%@ page import="dto.UserAddressDTO" %>
<%@ page import="dao.OrderDAO" %>
<%@ page import="java.text.NumberFormat" %>

<%
    int orderID = 0;
    try {
        String paramOrderID = request.getParameter("orderID");
        if (paramOrderID != null) {
            orderID = Integer.parseInt(paramOrderID);
        }
    } catch (NumberFormatException e) {
        orderID = 0;
    }

    OrderDAO dao = new OrderDAO();
    OrderDTO order = dao.getOrderById(orderID);
    List<OrderDetailDTO> orderDetails = dao.getOrderDetails(orderID);
    UserAddressDTO userInfo = dao.getUserAddressInfoByOrderId(orderID);

    NumberFormat nf = NumberFormat.getInstance();
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Order Details</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/orderDetail.css">
</head>
<body>
<div class="container">
    <% if (order == null) { %>
        <div class="alert">
            <h2>🚫 Order Not Found!</h2>
            <p>No order exists with ID: <%= orderID %></p>
            <button class="btn" onclick="history.back()">← Back</button>
        </div>
    <% } else { %>
        <div class="order-card">
            <h2>Order #<%= order.getOrderID() %></h2>
            <div class="order-info">
                <% if (userInfo != null) { %>
                    <div><strong>Customer:</strong> <%= userInfo.getFullName() %></div>
                    <div><strong>Email:</strong> <%= userInfo.getEmail() %></div>
                    <div><strong>Phone:</strong> <%= userInfo.getPhone() %></div>
                    <div><strong>Address:</strong> <%= userInfo.getAddress() %>, <%= userInfo.getDistrict() %>, 
                         <%= userInfo.getCity() %>, <%= userInfo.getCountry() %></div>
                <% } else { %>
                    <div><strong>Customer:</strong> <i>Not provided yet</i></div>
                    <div><strong>Email:</strong> <i>Not provided yet</i></div>
                    <div><strong>Phone:</strong> <i>Not provided yet</i></div>
                    <div><strong>Address:</strong> <i>Not provided yet</i></div>
                <% } %>
                <div><strong>Date:</strong> <%= order.getOrderDate() %></div>
                <div>
                    <strong>Status:</strong>
                    <%
                        String status = order.getStatus();
                        String badgeClass = "status-badge";
                        if ("Shipped".equalsIgnoreCase(status)) badgeClass += " status-shipped";
                        else if ("Processing".equalsIgnoreCase(status)) badgeClass += " status-processing";
                        else if ("Cancelled".equalsIgnoreCase(status)) badgeClass += " status-canceled";
                        else if ("Delivered".equalsIgnoreCase(status)) badgeClass += " status-delivered";
                    %>
                    <span class="<%= badgeClass %>"><%= status %></span>
                </div>
                <div><strong>Total:</strong> <%= nf.format(order.getTotalAmount()) %> ₫</div>
            </div>
        </div>

        <h3>📦 Products</h3>
        <div class="products-list">
            <% for (OrderDetailDTO od : orderDetails) { %>
                <div class="product-box">
                    <p><strong>Product ID:</strong> <%= od.getProductID() %></p>
                    <p><strong>Name:</strong> <%= od.getProductName() %></p>
                    <p><strong>Size:</strong> <%= od.getSizeName() %></p>
                    <p><strong>Color:</strong> <%= od.getColorName() %></p>
                    <p><strong>Quantity:</strong> <%= od.getQuantity() %></p>
                    <p><strong>Unit Price:</strong> <%= nf.format(od.getUnitPrice()) %> ₫</p>
                </div>
            <% } %>
        </div>
        <button class="btn" onclick="history.back()">← Back to Order List</button>
    <% } %>
</div>
</body>
</html>
