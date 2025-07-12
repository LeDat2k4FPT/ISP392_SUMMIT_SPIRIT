
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="dto.OrderDTO" %>
<%@ page import="dto.OrderDetailDTO" %>
<%@ page import="dao.OrderDAO" %>
<%@ page import="java.text.NumberFormat" %>

<%
    String paramOrderID = request.getParameter("orderID");
    int orderID = 0;
    if (paramOrderID != null) {
        orderID = Integer.parseInt(paramOrderID);
    }

    OrderDAO dao = new OrderDAO();
    OrderDTO order = dao.getOrderById(orderID);
    List<OrderDetailDTO> orderDetails = dao.getOrderDetails(orderID);

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
                    <div><span></span> <strong>Customer:</strong> <%= order.getFullName() %></div>
                    <div><span></span> <strong>Email:</strong> <%= order.getEmail() %></div>
                    <div><span></span> <strong>Phone:</strong> <%= order.getPhoneNumber() %></div>
                    <div><span></span> <strong>Date:</strong> <%= order.getOrderDate() %></div>
                    <div>
                        <span></span> <strong>Status:</strong> 
                        <%
                            String status = order.getStatus();
                            String badgeClass = "";
                            if ("Shipped".equalsIgnoreCase(status)) {
                                badgeClass = "status-badge status-shipped";
                            } else if ("Processing".equalsIgnoreCase(status)) {
                                badgeClass = "status-badge status-processing";
                            } else if ("Cancelled".equalsIgnoreCase(status)) {
                                badgeClass = "status-badge status-canceled";
                            } else if ("Delivered".equalsIgnoreCase(status)) {
                                badgeClass = "status-badge status-delivered";
                            } else {
                                badgeClass = "status-badge";
                            }
                        %>
                        <span class="<%= badgeClass %>"><%= status %></span>
                    </div>

                    <div><span></span> <strong>Total:</strong> <%= nf.format(order.getTotalAmount()) %> ₫</div>
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



