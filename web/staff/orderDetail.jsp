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

<% if (order == null) { %>
<h2 style="color:red;">Order not found!</h2>
<% } else { %>
<h2>🧾 Order Details - ID: <%= order.getOrderID() %></h2>
<p><strong>Customer:</strong> <%= order.getFullName() %></p>
<p><strong>Email:</strong> <%= order.getEmail() %></p>
<p><strong>Phone:</strong> <%= order.getPhoneNumber() %></p>
<p><strong>Order Date:</strong> <%= order.getOrderDate() %></p>
<p><strong>Status:</strong> <%= order.getStatus() %></p>
<p><strong>Total:</strong> <%= nf.format(order.getTotalAmount()) %></p>

<h3>📦 Order Items</h3>
<table border="1" cellspacing="0" cellpadding="5">
    <tr>
        <th>Product ID</th>
        <th>Product Name</th>
        <th>Size</th>
        <th>Color</th>
        <th>Quantity</th>
        <th>Unit Price</th>
    </tr>
    <% for (OrderDetailDTO od : orderDetails) { %>
    <tr>
        <td><%= od.getProductID() %></td>
        <td><%= od.getProductName() %></td>
        <td><%= od.getSizeName() %></td>
        <td><%= od.getColorName() %></td>
        <td><%= od.getQuantity() %></td>
        <td><%= nf.format(od.getUnitPrice()) %></td>
    </tr>
    <% } %>
</table>
<button class="btn btn-secondary" onclick="history.back()">Back</button>
<% } %>
