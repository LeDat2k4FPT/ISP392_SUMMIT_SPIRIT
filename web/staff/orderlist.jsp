<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="dto.OrderDTO" %>
<%@ page import="dao.OrderDAO" %>

<%
    OrderDAO orderDAO = new OrderDAO();
    List<OrderDTO> orders = orderDAO.getAllOrders();
%>

<h2>📦 Orders</h2>
<table border="1" cellspacing="0" cellpadding="5">
    <tr>
        <th>ID</th>
        <th>Customer</th> <!-- 👈 đổi User ID thành Customer -->
        <th>Order Date</th>
        <th>Total</th>
        <th>Status</th>
        <th>Actions</th>
    </tr>
    <%
        for (OrderDTO o : orders) {
    %>
    <tr>
        <td><%= o.getOrderID() %></td>
        <td><%= o.getFullName() %></td> <!-- 👈 dùng FullName -->
        <td><%= o.getOrderDate() %></td>
        <td><%= String.format("%,.0f", o.getTotalAmount()) %></td>
        <td><%= o.getStatus() %></td>
        <td>
            <a href="staff/orderDetail.jsp?orderID=<%= o.getOrderID() %>">Details</a> |
            <a href="updateOrderStatus.jsp?orderID=<%= o.getOrderID() %>">Update</a>
        </td>
    </tr>
    <%
        }
    %>
</table>
