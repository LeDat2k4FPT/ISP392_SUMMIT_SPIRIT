<%-- 
    Document   : productlist
    Created on : Jun 16, 2025, 4:34:30 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="dto.ProductDTO" %>
<%@ page import="dao.ProductDAO" %>
<%@ page import="dto.UserDTO" %>
<%
    UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
    if (user == null || !"Staff".equals(user.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }

    ProductDAO dao = new ProductDAO();
    List<ProductDTO> productList = dao.getAllProducts(); // Cần có phương thức này trong DAO
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Product List</title>
    <style>
        body { font-family: Arial; padding: 20px; }
        table { border-collapse: collapse; width: 100%; margin-top: 20px; }
        th, td { border: 1px solid #ccc; padding: 10px; text-align: left; }
        th { background-color: #f2f2f2; }
        .btn {
            padding: 6px 12px;
            text-decoration: none;
            border-radius: 5px;
            font-size: 14px;
        }
        .edit-btn { background-color: #28a745; color: white; }
        .delete-btn { background-color: #dc3545; color: white; }
        .add-btn { background-color: #007bff; color: white; margin-bottom: 10px; display: inline-block; }
        .logout-btn { background-color: #6c757d; color: white; float: right; }
    </style>
</head>
<body>

<h2>Product List</h2>

<a href="mnproduct.jsp" class="btn add-btn">➕ Add New Product</a>
<a href="LogoutController" class="btn logout-btn">🚪 Logout</a>

<table>
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Price</th>
        <th>Status</th>
        <th>Category ID</th>
        <th>Action</th>
    </tr>
    <%
        if (productList != null && !productList.isEmpty()) {
            for (ProductDTO p : productList) {
    %>
    <tr>
        <td><%= p.getProductID() %></td>
        <td><%= p.getProductName() %></td>
        <td><%= p.getPrice() %></td>
        <td><%= p.getStatus() %></td>
        <td><%= p.getCateID() %></td>
        <td>
            <a href="EditProductController?productID=<%= p.getProductID() %>" class="btn edit-btn">Edit</a>
            <a href="DeleteProductController?productID=<%= p.getProductID() %>" class="btn delete-btn" onclick="return confirm('Are you sure?')">Delete</a>
        </td>
    </tr>
    <%
            }
        } else {
    %>
    <tr>
        <td colspan="6">No products found.</td>
    </tr>
    <% } %>
</table>

</body>
</html>
