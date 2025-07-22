<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="dto.OrderDTO"%>
<%@page import="dao.OrderDAO"%>
<%@ page import="dto.UserDTO" %>
<!DOCTYPE html>
<%
    UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
    if (user == null || !"Staff".equals(user.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<%
    String msg = request.getParameter("msg");
    String type = request.getParameter("type"); // success, danger, etc.
    if (msg != null && type != null) {
%>
<div class="alert alert-<%= type %>" role="alert" style="margin-top: 10px;">
    <%= msg %>
</div>
<%
    }
%>
<html>
    <head>
        <title>Order List</title>
        <link href="https://fonts.googleapis.com/css2?family=Kumbh+Sans&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="<%= request.getContextPath() %>/css/orderlist.css">
    </head>
    <body>

        <div class="order-section">
            <div class="order-header">
                <h2>Order List</h2>
            </div>

            <form class="search-form" action="<%= request.getContextPath() %>/staffDashboard.jsp" method="get">
                <input type="hidden" name="page" value="staff/orderlist.jsp" />
                <input type="text" name="keyword"
                       value="<%= request.getParameter("keyword") != null ? request.getParameter("keyword") : "" %>"
                       placeholder="Search by customer name, email, status, or ID" />
                <button type="submit">Search</button>
            </form>

            <div class="order-table-wrapper">
                <table class="order-table">
                    <thead>
                        <tr>
                            <th>No</th> 
                            <th>Customer</th>
                            <th>Date</th>
                            <th>Total</th>
                            <th>Status</th>
                            <th>View</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            String keyword = request.getParameter("keyword");
                            OrderDAO orderDAO = new OrderDAO();
                            List<OrderDTO> orders;

                            if (keyword != null && !keyword.trim().isEmpty()) {
                                orders = orderDAO.searchOrders(keyword.trim());
                            } else {
                                orders = orderDAO.getAllOrders();
                            }

                            int index = 1; 

                            if (orders != null && !orders.isEmpty()) {
                                for (OrderDTO o : orders) {
                        %>
                        <tr>
                            <td><%= index++ %></td> 
                            <td><%= o.getFullName() %></td>
                            <td><%= o.getOrderDate() %></td>
                            <td><%= String.format("%,.0f", o.getTotalAmount()) %></td>
                            <td>
                                <form action="<%=request.getContextPath()%>/UpdateOrderStatus" method="post" style="display:inline;">
                                    <input type="hidden" name="orderID" value="<%= o.getOrderID() %>"/>
                                    <select name="status">
                                        <option value="Processing" <%= "Processing".equals(o.getStatus()) ? "selected":"" %>>Processing</option>
                                        <option value="Shipped" <%= "Shipped".equals(o.getStatus()) ? "selected":"" %>>Shipped</option>
                                        <option value="Delivered" <%= "Delivered".equals(o.getStatus()) ? "selected":"" %>>Delivered</option>
                                        <option value="Cancelled" <%= "Cancelled".equals(o.getStatus()) ? "selected":"" %>>Cancelled</option>
                                    </select>
                                    <button type="submit">Update</button>
                                </form>
                            </td>
                            <td>
                                <a href="<%= request.getContextPath() %>/OrderDetailController?orderID=<%=o.getOrderID()%>" class="view-btn">View Detail</a>
                            </td>

                        </tr>
                        <%
                                }
                            } else {
                        %>
                        <tr><td colspan="6" style="text-align:center; color:#999;">No orders found.</td></tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>      
            </div>
        </div>
        <script>
            // Tự động ẩn alert sau 3 giây
            setTimeout(function () {
                const alertBox = document.querySelector('.alert');
                if (alertBox) {
                    alertBox.style.transition = "opacity 0.5s ease-out";
                    alertBox.style.opacity = "0";
                    setTimeout(() => alertBox.remove(), 500); // Xoá khỏi DOM sau khi ẩn
                }
            }, 3000);
        </script>
    </body>
</html>