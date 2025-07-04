<%@page import="java.util.List"%>
<%@page import="dto.OrderDTO"%>
<%
    List<OrderDTO> orders = (List<OrderDTO>) request.getAttribute("orders");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Order History</title>
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
        </tr>
        <% if (orders != null) { 
               for (OrderDTO o : orders) { %>
        <tr>
            <td><%= o.getOrderID() %></td>
            <td><%= o.getOrderDate() %></td>
            <td><%= o.getStatus() %></td>
            <td><%= String.format("%,.0f", o.getTotalAmount()) %></td>
            <td><a href="OrderDetailController?orderID=<%= o.getOrderID() %>">View</a></td>
        </tr>
        <%    }
           } else { %>
        <tr>
            <td colspan="5">No orders found.</td>
        </tr>
        <% } %>
    </table>

    <br>
    <form action="profile.jsp" method="get">
        <button type="submit">Back to Profile</button>
    </form>
</body>
</html>
