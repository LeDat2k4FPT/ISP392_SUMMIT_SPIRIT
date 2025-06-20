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

    <form action="<%= request.getContextPath() %>/UpdateProductController" method="post" enctype="multipart/form-data">
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

    <label>Category</label>
    <select name="cateID">
        <option value="1" <%= product.getCateID() == 1 ? "selected" : "" %>>Clothing</option>
        <option value="2" <%= product.getCateID() == 2 ? "selected" : "" %>>Backpack</option>
        <!-- thêm nếu có -->
    </select>

    <label>Price</label>
    <input type="number" step="0.01" name="price" value="<%= product.getPrice() %>" required/>

    <label>Color</label>
    <select name="colorID">
        <option value="1">Red</option>
        <option value="2">Blue</option>
        <!-- thêm nếu cần -->
    </select>

    <label>Size</label>
    <select name="sizeID">
        <option value="1">S</option>
        <option value="2">M</option>
        <option value="3">L</option>
        <!-- thêm nếu cần -->
    </select>

    <label>Quantity</label>
    <input type="number" name="quantity" value="10" required/>

    <label>Image</label>
    <input type="file" name="image"/>

    <button type="submit">Update Product</button>
</form>

    <a href="<%= request.getContextPath() %>/staff/productlist.jsp">⬅ Back to Product List</a>


</body>
</html>

