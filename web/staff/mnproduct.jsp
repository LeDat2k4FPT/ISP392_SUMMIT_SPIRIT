<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="dto.UserDTO" %>
<%@ page import="dao.CategoryDAO" %>
<%@ page import="dto.CategoryDTO" %>
<%
    UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
    if (user == null || !"Staff".equals(user.getRole())) {
        response.sendRedirect("../login.jsp");
        return;
    }

    CategoryDAO categoryDAO = new CategoryDAO();
    java.util.List<CategoryDTO> categories = categoryDAO.getAllCategories();
%>
<div class="product-form-wrapper">
    <div class="product-header">
        <h2>Add new product</h2>
        <a href="#" onclick="loadContent('ProductListController')" class="btn-add">🔙 Back to Product List</a>
    </div>

    <% String error = (String) request.getAttribute("error");
       if (error != null) { %>
        <p class="error-msg"><%= error %></p>
    <% } %>

    <form action="<%= request.getContextPath() %>/AddProductController" method="post" class="add-product-form">
        <label>Product Name:</label>
        <input type="text" name="productName" required>

        <label>Description:</label>
        <textarea name="description" required></textarea>

        <div class="form-row">
            <div>
                <label>Color:</label>
                <input type="text" name="color" required>
            </div>
            <div>
                <label>Size:</label>
                <input type="text" name="size" required>
            </div>
        </div>
        <div class="form-row">
            <div>
                <label>Price:</label>
                <input type="text" name="price" required oninput="this.value = this.value.replace(/[^0-9.]/g, '')">
            </div>
            <div>
                <label>Stock:</label>
                <input type="number" name="stock" required>
            </div>
        </div>

        <label>Category:</label>
        <select name="cateID" id="cateID" required>
            <% for (CategoryDTO c : categories) { %>
                <option value="<%= c.getCateID() %>"><%= c.getCateName() %></option>
            <% } %>
        </select>

        <label>Status:</label>
        <select name="status" required>
            <option value="Active">Active</option>
            <option value="Inactive">Inactive</option>
        </select>

        <label>Product Image:</label>
        <input type="file" name="imageFile" accept="image/*">
        <input type="text" name="imageURL" placeholder="Or enter image URL...">

        <button type="submit" class="submit-btn">Add new product</button>
    </form>