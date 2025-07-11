<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="dto.OrderDTO"%>
<%@page import="dao.OrderDAO"%>
<!DOCTYPE html>
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

            <form class="search-form" action="<%=request.getContextPath()%>/staff/orderlist.jsp" method="get">
                <input type="text" name="keyword" value="<%= request.getParameter("keyword") != null ? request.getParameter("keyword") : "" %>"
                       placeholder="Search by customer name, email, status, or ID"/>
                <button type="submit">Search</button>
            </form>

            <div class="order-table-wrapper">
                <table class="order-table">
                    <thead>
                        <tr>
                            <th>ID</th>
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

                            if (orders != null && !orders.isEmpty()) {
                                for (OrderDTO o : orders) {
                        %>
                        <tr>
                            <td><%= o.getOrderID() %></td>
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
                                <form action="<%=request.getContextPath()%>/staff/orderDetail.jsp" method="get" style="display:inline;">
                                    <input type="hidden" name="orderID" value="<%= o.getOrderID() %>"/>
                                    <button type="submit">Details</button>
                                </form>
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
            <div class="back-btn-container">
                <a class="btn" href="<%= request.getContextPath() %>/staffDashboard.jsp?page=staff/orderlist.jsp">Order List</a>
            </div>
        </div>

    </body>
</html>
