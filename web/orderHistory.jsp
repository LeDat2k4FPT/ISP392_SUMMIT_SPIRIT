<%@page import="java.util.List"%>
<%@page import="dto.OrderDTO"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<%
    List<OrderDTO> orders = (List<OrderDTO>) request.getAttribute("orders");
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Order History</title>
        <style>
            #cancelModal, #successModal {
                display: none;
                position: fixed;
                top: 30%;
                left: 35%;
                background: #fff;
                border: 1px solid #333;
                padding: 20px;
                z-index: 1000;
                box-shadow: 0 0 10px rgba(0,0,0,0.3);
                width: 30%;
                border-radius: 8px;
                text-align: center;
            }

            #modalOverlay {
                display: none;
                position: fixed;
                top: 0;
                left: 0;
                right: 0;
                bottom: 0;
                background: rgba(0,0,0,0.5);
                z-index: 900;
            }

            button {
                margin-top: 10px;
                padding: 6px 12px;
            }

            table {
                border-collapse: collapse;
                width: 80%;
            }

            th, td {
                padding: 10px;
                border: 1px solid #ddd;
                text-align: center;
            }

            .status-cancelled {
                color: red;
                font-weight: bold;
            }
            .status-cancelling {
                color: orange;
                font-weight: bold;
            }
            .status-processing {
                color: blue;
            }
            .status-shipped {
                color: green;
            }
            .status-pending {
                color: gray;
            }
        </style>
    </head>
    <body>
        <h2>My Order History</h2>
        <table border="1">
            <tr>
                <th>Order ID</th>
                <th>Date</th>
                <th>Status</th>
                <th>Total</th>
                <th>Detail</th>
                <th></th>
            </tr>
            <% if (orders != null && !orders.isEmpty()) {
                  for (OrderDTO o : orders) {
                      String status = o.getStatus() != null ? o.getStatus() : "";
                String statusClass = "";
                if ("Cancelled".equalsIgnoreCase(status)) statusClass = "status-cancelled";
                else if ("Cancelling".equalsIgnoreCase(status)) statusClass = "status-cancelling";
                else if ("Processing".equalsIgnoreCase(status)) statusClass = "status-processing";
                else if ("Shipped".equalsIgnoreCase(status)) statusClass = "status-shipped";
                else if ("Pending".equalsIgnoreCase(status)) statusClass = "status-pending";
            %>
            <tr>
                <td><%= o.getOrderID() %></td>
                <td><%= o.getOrderDate() %></td>
                <td class="<%= statusClass %>"><%= o.getStatus() %></td>
                <td><%= String.format("%,.0f", o.getTotalAmount()) %></td>
                <td><a href="OrderDetailController?orderID=<%= o.getOrderID() %>">View</a></td>
                <td>
                    <% if (!"Cancelled".equalsIgnoreCase(status)
                        && !"Delivered".equalsIgnoreCase(status)
                        && !"Cancelling".equalsIgnoreCase(status)) { %>
                    <button onclick="openCancelModal(<%= o.getOrderID() %>)">Cancel Order</button>
                    <% } else { %>
                    &nbsp;
                    <% } %>
                </td>
            </tr>
            <% }
               } else { %>
            <tr>
                <td colspan="6">No orders found.</td>
            </tr>
            <% } %>
        </table>

        <br>
        <form action="profile.jsp" method="get">
            <button type="submit">Back to Profile</button>
        </form>

        <div id="modalOverlay"></div>

        <div id="cancelModal">
            <h3>Cancel Order</h3>
            <form action="CancelOrderController" method="post">
                <input type="hidden" name="orderID" id="modalOrderID" />
                <label>Reason for cancellation:</label><br/>
                <textarea name="cancelReason" rows="4" cols="30" required></textarea><br/><br/>
                <button type="submit">Confirm Cancel</button>
                <button type="button" onclick="closeCancelModal()">Close</button>
            </form>
        </div>

        <div id="successModal">
            <p>We have received your cancellation.<br/>
                Please wait for our email or phone number to confirm and receive your payment back.</p>
            <button onclick="closeSuccessModal()">Close</button>
        </div>

        <script>
            function openCancelModal(orderID, status) {
                // Hiển thị modal yêu cầu nhập lý do
                document.getElementById("modalOrderID").value = orderID;
                document.getElementById("modalOverlay").style.display = "block";
                document.getElementById("cancelModal").style.display = "block";
            }

            function closeCancelModal() {
                document.getElementById("modalOverlay").style.display = "none";
                document.getElementById("cancelModal").style.display = "none";
            }

            function closeSuccessModal() {
                document.getElementById("modalOverlay").style.display = "none";
                document.getElementById("successModal").style.display = "none";
            }
        </script>

        <%-- Thông báo khi huỷ đơn hàng loại cần xác nhận --%>
        <%
            Boolean cancelSuccess = (Boolean) session.getAttribute("cancelSuccess");
            if (cancelSuccess != null && cancelSuccess) {
                session.removeAttribute("cancelSuccess");
        %>
        <script>
            document.getElementById("modalOverlay").style.display = "block";
            document.getElementById("successModal").style.display = "block";
        </script>
        <%
            }
        %>
    </body>
</html>