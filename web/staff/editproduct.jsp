<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="dto.ProductDTO"%>
<%@ page import="dao.ProductVariantDAO" %>
<%@ page import="dao.ColorDAO" %>
<%@ page import="dao.SizeDAO" %>
<%@ page import="dto.ProductVariantDTO" %>
<%@ page import="dao.CategoryDAO" %>
<%@ page import="dto.CategoryDTO" %>
<%
    ProductDTO product = (ProductDTO) request.getAttribute("PRODUCT");
    if (product == null) {
        response.sendRedirect("ProductListController");
        return;
    }
    ProductVariantDAO variantDAO = new ProductVariantDAO();
    ColorDAO colorDAO = new ColorDAO();
    SizeDAO sizeDAO = new SizeDAO();
    CategoryDAO categoryDAO = new CategoryDAO();
    java.util.List<ProductVariantDTO> variants = variantDAO.getVariantsByProductId(product.getProductID());
    java.util.List<CategoryDTO> categories = categoryDAO.getAllCategories();
    java.util.List<String> allSizes = sizeDAO.getAllSizes();
    java.util.List<String> allColors = colorDAO.getAllColors();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Product</title>
    <style>
        body { font-family: Arial; padding: 20px; }
        form { max-width: 600px; margin: auto; }
        input, textarea, select, button { width: 100%; padding: 8px; margin-top: 10px; margin-bottom: 15px; }
        button { background-color: #28a745; color: white; border: none; }
        a { display: block; margin-top: 20px; color: blue; }
        table { width:100%; border-collapse: collapse; margin-bottom: 15px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: center; }
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
    <input type="text" name="status" value="Active" readonly/>

    <label>Category</label>
    <select name="cateID">
        <% for (CategoryDTO c : categories) { %>
        <option value="<%= c.getCateID() %>" <%= product.getCateID() == c.getCateID() ? "selected" : "" %>><%= c.getCateName() %></option>
        <% } %>
    </select>

    <label>Image (Upload file hoặc nhập URL)</label>
    <input type="text" name="imageUrl" placeholder="Hoặc nhập URL ảnh..."/>

    <label>Variants</label>
    <table>
        <thead>
            <tr>
                <% boolean showSize = false, showColor = false;
                   if (!variants.isEmpty()) {
                       ProductVariantDTO sample = variants.get(0);
                       showSize = sample.getSizeID() != 0;
                       showColor = sample.getColorID() != 0;
                   }
                %>
                <% if (showSize) { %><th>Size</th><% } %>
                <% if (showColor) { %><th>Color</th><% } %>
                <th>Quantity</th>
                <th>Price</th>
            </tr>
        </thead>
        <tbody>
            <% int idx = 0;
            for (ProductVariantDTO v : variants) {
                String sizeName = (v.getSizeID() != 0) ? sizeDAO.getSizeNameById(v.getSizeID()) : null;
                String colorName = (v.getColorID() != 0) ? colorDAO.getColorNameById(v.getColorID()) : null;
            %>
            <tr>
                <input type="hidden" name="variantId_<%= idx %>" value="<%= v.getAttributeID() %>"/>
                <% if (showSize) { %>
                <td>
                    <select name="size_<%= idx %>">
                        <% for (String s : allSizes) { %>
                        <option value="<%= s %>" <%= s.equals(sizeName) ? "selected" : "" %>><%= s %></option>
                        <% } %>
                    </select>
                </td>
                <% } %>
                <% if (showColor) { %>
                <td>
                    <select name="color_<%= idx %>">
                        <% for (String c : allColors) { %>
                        <option value="<%= c %>" <%= c.equals(colorName) ? "selected" : "" %>><%= c %></option>
                        <% } %>
                    </select>
                </td>
                <% } %>
                <td><input type="number" name="quantity_<%= idx %>" value="<%= v.getQuantity() %>" required/></td>
                <td><input type="number" step="0.01" name="price_<%= idx %>" value="<%= v.getPrice() %>" required/></td>
            </tr>
            <% idx++; } %>
        </tbody>
    </table>
    <input type="hidden" name="variantCount" value="<%= variants.size() %>"/>

    <button type="submit">Update Product</button>
</form>

<a href="<%= request.getContextPath() %>/ProductListController">⬅ Back to Product List</a>

</body>
</html>
