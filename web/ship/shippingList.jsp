<%-- 
    Document   : shippingList
    Created on : Jul 24, 2025, 2:12:37 AM
    Author     : Hanne
--%>

<%@ page import="java.util.List" %>
<%@ page import="dto.OrderDTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<OrderDTO> packedOrders = (List<OrderDTO>) request.getAttribute("packedOrders");
%>

<h2>📦 Orders Waiting for Shipping</h2>

<% if (packedOrders != null && !packedOrders.isEmpty()) { %>
    <table border="1" cellpadding="10">
        <tr><th>OrderID</th><th>UserID</th><th>Order Date</th><th>Total</th><th>Action</th></tr>
        <% for (OrderDTO order : packedOrders) { %>
            <tr>
                <td><%= order.getOrderID() %></td>
                <td><%= order.getUserID() %></td>
                <td><%= order.getOrderDate() %></td>
                <td><%= order.getTotalAmount() %></td>
                <form method="post" action="AcceptShippingController" >
                    <input type="hidden" name="orderID" value="<%= order.getOrderID() %>"/>
                    <button type="submit">Accept</button>
                </form>

            </tr>
        <% } %>
    </table>
<% } else { %>
    <p>No packed orders available.</p>
<% } %>

