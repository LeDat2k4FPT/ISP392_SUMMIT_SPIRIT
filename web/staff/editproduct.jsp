<%-- 
    Document   : editproduct
    Created on : Jun 18, 2025, 7:32:57 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="dto.ProductDTO"%>
<%
    ProductDTO product = (ProductDTO) request.getAttribute("PRODUCT");
    if (product == null) {
        response.sendRedirect("staff/productlist.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Product</title>
    <style>
        body {
            font-family: Arial;
            padding: 20px;
        }
        form {
            max-width: 500px;
            margin: auto;
        }
        input, textarea, select, button {
            width: 100%;
            padding: 8px;
            margin-top: 10px;
            margin-bottom: 15px;
        }
        button {
            background-color: #28a745;
            color: white;
            border: none;
        }
        a {
            display: block;
            margin-top: 20px;
            color: blue;
        }
    </style>
</head>
<body>

    <h2>Edit Product - ID: <%= product.getProductID() %></h2>

    <form action="<%= request.getContextPath() %>/UpdateProductController" method="post">
        <input type="hidden" name="productID" value="<%= product.getProductID() %>"/>

        <label>Product Name</label>
        <input type="text" name="productName" value="<%= product.getProductName() %>" required/>

        <label>Description</label>
        <textarea name="description"><%= product.getDescription() %></textarea>

        <label>Status</label>
        <select name="status">
            <option value="Active" <%= "Active".equals(product.getStatus()) ? "selected" : "" %>>Active</option>
            <option value="Inactive" <%= "Inactive".equals(product.getStatus()) ? "selected" : "" %>>Inactive</option>
        </select>

        <label>Category ID</label>
        <input type="number" name="cateID" value="<%= product.getCateID() %>" required/>

        <button type="submit">Update Product</button>
    </form>

    <a href="productlist.jsp">⬅ Back to Product List</a>

</body>
</html>

