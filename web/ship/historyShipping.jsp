<%-- 
    Document   : historyShipping
    Created on : Jul 24, 2025, 5:02:09 PM
    Author     : Hanne
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="dto.ShippingDTO" %>

<%
    List<ShippingDTO> deliveredList = (List<ShippingDTO>) request.getAttribute("deliveredList");
%>

<h2>📜 Delivery History - Giao hàng thành công</h2>

<% if (deliveredList != null && !deliveredList.isEmpty()) { %>
    <div style="display: flex; flex-wrap: wrap; gap: 20px;">
        <% for (ShippingDTO item : deliveredList) { %>
            <div style="border: 1px solid #ccc; padding: 15px; width: 320px;">
                <h4>🚚 Order ID: <%= item.getOrderID() %></h4>
<!--                <p><strong>Shipper ID: </strong> <%= item.getUserID() %></p>-->
                <p><strong>Delivered At:</strong> <%= item.getDeliveryTime() %></p>
                <p><strong>Note:</strong> <%= item.getNote() != null ? item.getNote() : "Không có" %></p>
                <% if (item.getDeliveryImageURL() != null) { %>
                    <img src="<%= request.getContextPath() + "/" + item.getDeliveryImageURL() %>" alt="Delivery Image" style="max-width: 100%; border: 1px solid #999;" />
                <% } else { %>
                    <p style="color: red;">⚠️ Không có ảnh giao hàng</p>
                <% } %>
            </div>
        <% } %>
    </div>
<% } else { %>
    <p>Không có đơn hàng nào đã giao thành công.</p>
<% } %>

