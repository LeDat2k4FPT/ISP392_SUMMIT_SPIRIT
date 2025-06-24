<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="dto.UserDTO" %>
<%
    UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
    if (user == null || !"Staff".equals(user.getRole())) {
        response.sendRedirect("../login.jsp");
        return;
    }
%>
<div class="product-form-wrapper">
    <div class="product-header">
        <h2>Add new product</h2>
        <a href="#" onclick="loadContent('staff/productlist.jsp')" class="btn-add">🔙 Back to Product List</a>
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
                <select name="color" required>
                    <option value="Black">Black</option>
                    <option value="Red">Red</option>
                    <option value="Blue">Blue</option>
                </select>
            </div>
            <div>
                <label>Size:</label>
                <select name="size" required>
                    <option value="S">S</option>
                    <option value="M">M</option>
                    <option value="L">L</option>
                    <option value="small">small</option>
                    <option value="big">big</option>
                </select>
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
        <select name="cateID" required>
            <option value="1">Paints</option>
            <option value="2">Shirts</option>
            <option value="3">Backpacks</option>
            <option value="4">Gears</option>
            <option value="5">Tents</option>
            <option value="6">Hats</option>
            <option value="7">Camping Tools</option>
        </select>

        <label>Status:</label>
        <select name="status" required>
            <option value="Active">Active</option>
            <option value="Inactive">Inactive</option>
        </select>

        <label>Product Image URL:</label>
        <input type="text" name="imageURL" placeholder="e.g., https://example.com/image.jpg">

        <button type="submit" class="submit-btn">Add new product</button>
    </form>
</div>
