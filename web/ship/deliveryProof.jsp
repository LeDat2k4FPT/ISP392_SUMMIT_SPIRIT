<%-- 
    Document   : deliveryProof
    Created on : Jul 24, 2025, 2:12:48 AM
    Author     : Hanne
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="dto.OrderDTO" %>

<%
    List<OrderDTO> shippedOrders = (List<OrderDTO>) request.getAttribute("shippedOrders");
%>

<h2>📸 Delivery Proof - Shipped Orders</h2>

<% if (shippedOrders != null && !shippedOrders.isEmpty()) { %>
    <% for (OrderDTO order : shippedOrders) { %>
        <div style="border: 1px solid #ccc; padding: 15px; margin-bottom: 30px;">
            <h3>Order ID: <%= order.getOrderID() %></h3>
            <p>User ID: <%= order.getUserID() %></p>
            <p>Order Date: <%= order.getOrderDate() %></p>
            <p>Total Amount: <%= order.getTotalAmount() %> VND</p>

            <!-- Camera preview -->
            <video id="video-<%= order.getOrderID() %>" width="320" height="240" autoplay style="border: 1px solid #000;"></video>
            <canvas id="canvas-<%= order.getOrderID() %>" width="320" height="240" style="display: none;"></canvas>
            <br>
            <button type="button" onclick="capture(<%= order.getOrderID() %>)">📸 Chụp ảnh</button>
            <br><br>
            <img id="preview-<%= order.getOrderID() %>" src="" style="max-width: 320px; display: none; border: 1px solid #aaa;" />

            <!-- Submit Form -->
            <form action="${pageContext.request.contextPath}/MarkDeliveredController" method="post">
                <input type="hidden" name="orderID" value="<%= order.getOrderID() %>">
                <input type="hidden" name="imageData" id="imageData-<%= order.getOrderID() %>">
                <label for="note">Ghi chú giao hàng:</label>
                <input type="text" name="note" placeholder="VD: Khách nhận hàng" required>
                <br><br>
                <button type="submit">✅ Mark as Delivered</button>
            </form>
        </div>
    <% } %>
<% } else { %>
    <p>Không có đơn hàng đang giao.</p>
<% } %>

<script>
    // Bắt đầu mở camera khi trang load
    window.addEventListener("DOMContentLoaded", () => {
        <% for (OrderDTO order : shippedOrders) { %>
            const video<%= order.getOrderID() %> = document.getElementById("video-<%= order.getOrderID() %>");
            navigator.mediaDevices.getUserMedia({ video: true })
                .then(stream => {
                    video<%= order.getOrderID() %>.srcObject = stream;
                })
                .catch(err => {
                    alert("Không thể mở camera: " + err);
                });
        <% } %>
    });

    // Hàm chụp ảnh và hiển thị preview
    function capture(orderID) {
        const video = document.getElementById("video-" + orderID);
        const canvas = document.getElementById("canvas-" + orderID);
        const preview = document.getElementById("preview-" + orderID);
        const imageDataField = document.getElementById("imageData-" + orderID);

        const context = canvas.getContext("2d");
        context.drawImage(video, 0, 0, canvas.width, canvas.height);

        const imageData = canvas.toDataURL("image/png");
        preview.src = imageData;
        preview.style.display = "block";
        imageDataField.value = imageData;
    }
</script>
