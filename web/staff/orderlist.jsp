<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="dto.OrderDTO"%>
<%@page import="dao.OrderDAO"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Order List</title>
    </head>
    <body>
        <h2>Order List</h2>
        <table border="1" cellpadding="5" cellspacing="0">
            <tr>
                <th>ID</th>
                <th>Customer</th>
                <th>Date</th>
                <th>Total</th>
                <th>Status</th>
                <th>View</th>
            </tr>
            <%
                OrderDAO orderDAO = new OrderDAO();
                List<OrderDTO> orders = orderDAO.getAllOrders();

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
                    <form action="staff/orderDetail.jsp" method="get" style="display:inline;">
                        <input type="hidden" name="orderID" value="<%= o.getOrderID() %>"/>
                        <button type="submit">Details</button>
                    </form>
                </td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr><td colspan="6">No orders found.</td></tr>
            <%
                }
            %>
        </table>

    </body>
</html>
