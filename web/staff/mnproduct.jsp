<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="dto.UserDTO" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Add Product</title>
        <style>
            body {
                font-family: Arial;
                margin: 20px;
            }
            input, select, textarea {
                display: block;
                margin: 10px 0;
                width: 100%;
                padding: 8px;
            }
            button {
                padding: 10px 20px;
            }
            .top-bar {
                text-align: right;
                margin-bottom: 20px;
            }
            .back-btn {
                display: inline-block;
                background-color: #6c757d;
                color: white;
                padding: 6px 12px;
                text-decoration: none;
                border-radius: 5px;
                margin-bottom: 20px;
            }
            .back-btn:hover {
                background-color: #5a6268;
            }
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
            <form action="<%= request.getContextPath() %>/LogoutController" method="post" style="display:inline;">
            <button type="submit" class="btn logout-btn">🚪 Logout</button>
        </form>
        </div>

        <a href="staffDashboard.jsp" class="back-btn">⬅ Back to Staff Menu</a>

        <h2>Add New Product</h2>

        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
        <p style="color:red;"><%= error %></p>
        <%
            }
        %>

        <form action="<%= request.getContextPath() %>/AddProductController" method="post">
            <label>Product Name:</label>
            <input type="text" name="productName" required>

            <label>Description:</label>
            <textarea name="description" required></textarea>
            
            <label>Color:</label>
            <select name="color" required>
                <option value="Black">Black</option>
                <option value="Red">Red</option>
                <option value="Blue">Blue</option>
            </select>


            <label>Size:</label>
            <select name="size" required>
                <option value="S">S</option>
                <option value="M">M</option>
                <option value="L">L</option>
                <option value="small">small</option>
                <option value="big">big</option>
            </select>


            <label>Stock Quantity:</label>
            <input type="number" name="stock" required>

            <label>Price:</label>
            <input type="text" name="price" required oninput="this.value = this.value.replace(/[^0-9.]/g, '')">

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

            <button type="submit">Add Product</button>
        </form>

    </body>
</html>
