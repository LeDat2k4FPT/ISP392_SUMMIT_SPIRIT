<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="dto.UserDTO, dao.CategoryDAO, dto.CategoryDTO" %>
<%
    UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
    if (user == null || !"Staff".equals(user.getRole())) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    CategoryDAO categoryDAO = new CategoryDAO();
    java.util.List<CategoryDTO> categories = categoryDAO.getAllCategories();
%>

<link rel="stylesheet" href="<%= request.getContextPath() %>/css/addProduct.css">


<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h4><i class="bi bi-plus-circle"></i> Add New Product</h4>
        <a href="<%= request.getContextPath() %>/ProductListController" class="btn btn-outline-secondary">
            <i class="bi bi-arrow-left-circle"></i> Back to Product List
        </a>
    </div>

    <% String error = (String) request.getAttribute("error");
       if (error != null) { %>
    <div class="alert alert-danger"><%= error %></div>
    <% } %>

    <form action="AddProductController" method="post" enctype="multipart/form-data" class="row g-3">
        <div class="col-md-6">
            <label class="form-label">Product Name</label>
            <input type="text" name="productName" class="form-control" required>
        </div>

        <div class="col-md-6">
            <label class="form-label">Category</label>
            <select name="cateID" class="form-select" required>
                <% for (CategoryDTO c : categories) { %>
                <option value="<%= c.getCateID() %>"><%= c.getCateName() %></option>
                <% } %>
            </select>
        </div>

        <div class="col-12">
            <label class="form-label">Description</label>
            <textarea name="description" class="form-control" rows="3" required></textarea>
        </div>

        <div class="col-md-6">
            <label class="form-label">Color</label>
            <select name="color" class="form-select">
                <option value="">-- No Color --</option>
                <option>Red</option>
                <option>Black</option>
                <option>Blue</option>
            </select>
        </div>

        <div class="col-md-6">
            <label class="form-label">Size</label>
            <select name="size" class="form-select">
                <option value="">-- No Size --</option>
                <option>S</option>
                <option>M</option>
                <option>L</option>
                <option>Small</option>
                <option>Big</option>
            </select>
        </div>

        <div class="col-md-6">
            <label class="form-label">Price</label>
            <input type="text" name="price" class="form-control" required
                   oninput="this.value = this.value.replace(/[^0-9.]/g, '')">
        </div>

        <div class="col-md-6">
            <label class="form-label">Stock</label>
            <input type="number" name="stock" class="form-control" required>
        </div>

        <input type="hidden" name="status" value="Active">

        <div class="col-md-6">
            <label class="form-label">Product Image (URL)</label>
            <input type="text" name="imageURL" class="form-control" placeholder="Or enter image URL...">
        </div>

        <div class="col-12 text-end">
            <button type="submit" class="btn btn-success">
                <i class="bi bi-plus-circle"></i> Add Product
            </button>
        </div>
    </form>
</div>
