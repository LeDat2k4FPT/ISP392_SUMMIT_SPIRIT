<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="dto.ShippingDTO" %>
<%@ page import="dto.UserDTO" %>
<%
    UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
    if (loginUser == null || !"Shipper".equals(loginUser.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }
    List<ShippingDTO> deliveredList = (List<ShippingDTO>) request.getAttribute("deliveredList");
%>
<link href="https://fonts.googleapis.com/css2?family=Kumbh+Sans&display=swap" rel="stylesheet">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/historyShipping.css" />

<div class="history-container">
    <h2>📜 Delivery History - Giao hàng thành công</h2>

    <% if (deliveredList != null && !deliveredList.isEmpty()) { %>
        <div class="history-grid">
            <% for (ShippingDTO item : deliveredList) { %>
                <div class="history-card">
                    <h4>🚚 Order ID: <%= item.getOrderID() %></h4>
                    <p><strong>Delivered At:</strong> <%= item.getDeliveryTime() %></p>
                    <p><strong>Note:</strong> <%= item.getNote() != null ? item.getNote() : "Không có" %></p>

                    <% if (item.getDeliveryImageURL() != null) { %>
                        <img src="<%= request.getContextPath() + "/" + item.getDeliveryImageURL() %>"
                             alt="Delivery Image"
                             class="delivery-image" />
                    <% } else { %>
                        <p class="no-image">⚠️ No delivery photos</p>
                    <% } %>
                </div>
            <% } %>
        </div>
    <% } else { %>
        <p class="empty-message">No orders have been delivered successfully.</p>
    <% } %>
</div>
