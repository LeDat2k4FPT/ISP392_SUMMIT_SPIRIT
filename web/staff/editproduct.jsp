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
    StringBuilder sizeOptions = new StringBuilder();
    for (String s : allSizes) {
        sizeOptions.append("<option value=\"" + s + "\">" + s + "</option>");
    }
    StringBuilder colorOptions = new StringBuilder();
    for (String c : allColors) {
        colorOptions.append("<option value=\"" + c + "\">" + c + "</option>");
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

        <div class="section-box">
            <h2 style="margin-bottom: 20px;">Edit Product - ID: <%= product.getProductID() %></h2>

            <form action="<%= request.getContextPath() %>/UpdateProductController" method="post" enctype="multipart/form-data">
                <input type="hidden" name="productID" value="<%= product.getProductID() %>"/>

                <div class="form-group">
                    <label>Product Name</label>
                    <input type="text" name="productName" value="<%= product.getProductName() %>" required/>
                </div>

                <div class="form-group">
                    <label>Description</label>
                    <textarea name="description"><%= product.getDescription() %></textarea>
                </div>

                <div class="form-group">
                    <label>Status</label>
                    <%
                        String status = product.getStatus() != null ? product.getStatus() : "Active";
                        String color = "Active".equalsIgnoreCase(status) ? "green" : "red";
                    %>
                    <span style="display:block; margin-top:6px; margin-bottom:15px; color:<%= color %>; font-weight:bold;">
                        <%= status %>
                    </span>
                </div>

                <div class="form-group">
                    <label>Category</label>
                    <select name="cateID">
                        <% for (CategoryDTO c : categories) { %>
                        <option value="<%= c.getCateID() %>" <%= product.getCateID() == c.getCateID() ? "selected" : "" %>>
                            <%= c.getCateName() %>
                        </option>
                        <% } %>
                    </select>
                </div>

                <div class="form-group">
                    <label>Image (Upload file hoặc nhập URL)</label>
                    <input type="file" name="image"/>
                    <input type="text" name="imageUrl" placeholder="Hoặc nhập URL ảnh..."/>
                </div>

                <div class="form-group">
                    <label>Variants</label>
                    <table id="variantTable" border="1" style="width:100%; margin-bottom:15px;">
                        <thead>
                            <tr>
                                <th>Size</th>
                                <th>Color</th>
                                <th>Quantity</th>
                                <th>Price</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% int idx = 0;
                            for (ProductVariantDTO v : variants) {
                                String sizeName = sizeDAO.getSizeNameById(v.getSizeID());
                                String colorName = colorDAO.getColorNameById(v.getColorID());
                            %>
                            <tr>
                                <td>
                                    <input type="hidden" name="variantId_<%= idx %>" value="<%= v.getAttributeID() %>"/>
                                    <select name="size_<%= idx %>">
                                        <% for (String s : allSizes) { %>
                                        <option value="<%= s %>" <%= s.equals(sizeName) ? "selected" : "" %>><%= s %></option>
                                        <% } %>
                                    </select>
                                </td>
                                <td>
                                    <select name="color_<%= idx %>">
                                        <% for (String c : allColors) { %>
                                        <option value="<%= c %>" <%= c.equals(colorName) ? "selected" : "" %>><%= c %></option>
                                        <% } %>
                                    </select>
                                </td>
                                <td><input type="number" name="quantity_<%= idx %>" value="<%= v.getQuantity() %>" required/></td>
                                <td><input type="number" step="0.01" name="price_<%= idx %>" value="<%= v.getPrice() %>" required/></td>
                            </tr>
                            <% idx++; } %>
                        </tbody>
                    </table>
                </div>

                <button type="submit">Update Product</button>
            </form>

            <a class="back-button styled-button" href="<%= request.getContextPath() %>/ProductListController">⬅ Back to Product List</a>
        </div>

    </body>

</html>
