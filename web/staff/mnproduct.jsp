<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dto.UserDTO" %>
<%@ page import="dao.CategoryDAO" %>
<%@ page import="dto.CategoryDTO" %>
<%
    UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
    if (user == null || !"Staff".equals(user.getRole())) {
        // Bảo đảm redirect đúng context path
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // Debug session
    System.out.println("[DEBUG] Staff user: " + user.getUserID() + ", Role: " + user.getRole());

    CategoryDAO categoryDAO = new CategoryDAO();
    java.util.List<CategoryDTO> categories = categoryDAO.getAllCategories();
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Add New Product</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                padding: 20px;
            }
            .product-form-wrapper {
                max-width: 600px;
                margin: auto;
            }
            .product-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
            }
            .product-header h2 {
                margin: 0;
            }
            .btn-add {
                text-decoration: none;
                color: #007BFF;
            }
            form {
                margin-top: 20px;
            }
            label {
                display: block;
                margin-top: 10px;
            }
            input, textarea, select, button {
                width: 100%;
                padding: 8px;
                margin-top: 5px;
            }
            .form-row {
                display: flex;
                gap: 10px;
            }
            .form-row > div {
                flex: 1;
            }
            .submit-btn {
                background-color: #28a745;
                color: white;
                border: none;
                cursor: pointer;
                margin-top: 15px;
            }
            .submit-btn:hover {
                background-color: #218838;
            }
            .error-msg {
                color: red;
                margin-top: 10px;
            }
        </style>
    </head>
    <body>
        <div class="product-form-wrapper">
            <div class="product-header">
                <h2>Add new product</h2>
                <a href="<%= request.getContextPath() %>/ProductListController" class="btn-add">🔙 Back to Product List</a>
            </div>

            <% String error = (String) request.getAttribute("error");
       if (error != null) { %>
            <p class="error-msg"><%= error %></p>
            <% } %>

            <form action="<%= request.getContextPath() %>/AddProductController" method="post" enctype="multipart/form-data">
                <label>Product Name:</label>
                <input type="text" name="productName" required>

                <label>Description:</label>
                <textarea name="description" required></textarea>

                <div class="form-row">
                    <div>
                        <label>Color:</label>
                        <select name="color">
                            <option value="">-- No Color --</option>
                            <option value="Red">Red</option>
                            <option value="Black">Black</option>
                            <option value="Blue">Blue</option>
                        </select>
                    </div>
                    <div>
                        <label>Size:</label>
                        <select name="size">
                            <option value="">-- No Size --</option>
                            <option value="S">S</option>
                            <option value="M">M</option>
                            <option value="L">L</option>
                            <option value="Small">Small</option>
                            <option value="Big">Big</option>
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
                <select name="cateID" id="cateID" required>
                    <% for (CategoryDTO c : categories) { %>
                    <option value="<%= c.getCateID() %>"><%= c.getCateName() %></option>
                    <% } %>
                </select>

                <label>Status:</label>
                <select name="status" required>
                    <option value="Active">Active</option>
                </select>

                <label>Product Image:</label>
                <input type="text" name="imageURL" placeholder="Or enter image URL...">
                <button type="submit" class="submit-btn">Add new product</button>
            </form>
        </div>
    </body>
</html>
