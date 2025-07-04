<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="dto.UserDTO"%>
<%
    List<Map<String, Object>> potentialList = (List<Map<String, Object>>) request.getAttribute("potentialList");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Potential Customers</title>
    <link rel="stylesheet" href="../css/admin.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-4">
    <h2>Potential Customers</h2>
    <table class="table table-bordered mt-3">
        <thead>
        <tr>
            <th>#</th>
            <th>Name</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Total Orders</th>
            <th>Total Spent</th>
            <th>Reviews</th>
        </tr>
        </thead>
        <tbody>
        <% if (potentialList != null) {
            int idx = 1;
            for (Map<String, Object> info : potentialList) {
                UserDTO user = (UserDTO) info.get("user");
        %>
            <tr>
                <td><%= idx++ %></td>
                <td><%= user.getFullName() %></td>
                <td><%= user.getEmail() %></td>
                <td><%= user.getPhone() %></td>
                <td><%= info.get("orderCount") %></td>
                <td><%= info.get("totalSpent") %></td>
                <td><%= info.get("reviewCount") %></td>
            </tr>
        <%   }
           } %>
        </tbody>
    </table>
</div>
</body>
</html> 