<%-- 
    Document   : mnproduct
    Created on : Jun 14, 2025, 10:48:16 AM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="dto.UserDTO" %>
<!DOCTYPE html>
<html>
<head>
    <title>Add Product</title>
    <style>
        body { font-family: Arial; margin: 20px; }
        input, select, textarea { display: block; margin: 10px 0; width: 100%; padding: 8px; }
        button { padding: 10px 20px; }
        .top-bar { text-align: right; margin-bottom: 20px; }
    </style>
</head>
<body>
<%
    UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
    if (user == null || !"Staff".equals(user.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<div class="top-bar">
    Welcome, <%= user.getFullName() %> |
    <form action="LogoutController" method="post" style="display:inline;">
        <button type="submit">Logout</button>
    </form>
</div>

<h2>Add New Product</h2>

<% String error = (String) request.getAttribute("error");
   if (error != null) { %>
    <p style="color:red;"><%= error %></p>
<% } %>

<form action="AddProductController" method="post">
    <label>Product Name:</label>
    <input type="text" name="productName" required>

    <label>Description:</label>
    <textarea name="description" required></textarea>

    <label>Size:</label>
    <input type="text" name="size" required>

    <label>Stock Quantity:</label>
    <input type="number" name="stock" required>

    <label>Price:</label>
    <input type="text" name="price" required oninput="this.value = this.value.replace(/[^0-9.]/g, '')">

    <label>Category:</label>
    <select name="cateID" required>
        <option value="1">Clothing</option>
        <option value="2">Shirt</option>
        <option value="3">Tents</option>
        <option value="4">Cooking Equipment</option>
        <option value="5">Hats</option>
        <option value="6">Backpacks</option>
        <option value="7">Camping Tools</option>
    </select>

    <label>Status:</label>
    <select name="status" required>
        <option value="Active">Active</option>
        <option value="Inactive">Inactive</option>
    </select>

    <label>Product Image URLs:</label>
    <input type="text" name="productImage" placeholder="Image URL 1">
    <input type="text" name="productImage" placeholder="Image URL 2">

    <button type="submit">Add Product</button>
</form>

</body>
</html>
