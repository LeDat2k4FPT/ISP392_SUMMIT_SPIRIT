<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, dto.ProductDTO, dto.CartDTO, dto.UserDTO"%>
<%
    List<ProductDTO> saleProducts = (List<ProductDTO>) request.getAttribute("SALE_PRODUCTS");
    UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
    CartDTO cart = (CartDTO) session.getAttribute("CART");
    int cartItemCount = (cart != null) ? cart.getCartItems().size() : 0;
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sale Off - Summit Spirit</title>
    <link href="https://fonts.googleapis.com/css2?family=Kumbh+Sans&display=swap" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            font-family: 'Kumbh Sans', sans-serif;
            background: #f0f0f0;
            margin: 0;
            padding: 0;
        }
        .header {
            background-color: #1e5787;
            padding: 10px 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .header img {
            height: 60px;
        }
        .nav-links {
            display: flex;
            align-items: center;
            gap: 16px;
        }
        .nav-links a {
            color: white;
            font-size: 20px;
            text-decoration: none;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .cart-badge {
            background: red;
            color: white;
            border-radius: 50%;
            padding: 2px 6px;
            font-size: 12px;
            vertical-align: top;
            margin-left: 4px;
        }
        .user-dropdown {
            position: relative;
        }
        .user-name {
            cursor: pointer;
        }
        .dropdown-menu {
            display: none;
            position: absolute;
            right: 0;
            background: white;
            border: 1px solid #ccc;
            padding: 10px;
            z-index: 999;
        }
        .dropdown-menu a {
            display: block;
            text-decoration: none;
            color: black;
            margin: 5px 0;
        }

        h1 {
            text-align: center;
            padding: 20px 0;
            background-color: #fff;
            margin: 0;
        }
        .product-grid {
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
            gap: 30px;
            padding: 30px;
        }
        .product {
            background: white;
            padding: 15px;
            border-radius: 10px;
            width: 220px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            text-align: center;
        }
        .product img {
            width: 100%;
            height: 180px;
            object-fit: cover;
            border-radius: 8px;
        }
        .original-price {
            text-decoration: line-through;
            color: gray;
        }
        .discount-price {
            color: #e53935;
            font-weight: bold;
            font-size: 18px;
        }
        .button {
            display: inline-block;
            margin-top: 10px;
            padding: 5px 10px;
            background: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }
    </style>
</head>
<body>
    <!-- Header -->
    <div class="header">
        <a href="homepage.jsp">
            <img src="image/summit_logo.png" alt="Logo">
        </a>
        <div class="nav-links">
            <a href="homepage.jsp"><i class="fas fa-home"></i></a>
            <a href="cart.jsp" class="cart-icon">
                <i class="fas fa-shopping-cart"></i>
                <% if (cartItemCount > 0) { %>
                <span class="cart-badge"><%= cartItemCount %></span>
                <% } %>
            </a>
            <div class="user-dropdown">
                <div class="user-name" onclick="toggleMenu()"><i class="fas fa-user"></i></div>
                <div id="dropdown" class="dropdown-menu">
                    <a href="profile.jsp"><%= loginUser != null ? loginUser.getFullName() : "Account" %></a>
                    <a href="MainController?action=Logout">Logout</a>
                </div>
            </div>
        </div>
    </div>
    <script>
        function toggleMenu() {
            const menu = document.getElementById("dropdown");
            menu.style.display = menu.style.display === "block" ? "none" : "block";
        }
        document.addEventListener("click", function (event) {
            const dropdown = document.getElementById("dropdown");
            const userBtn = document.querySelector(".user-name");
            if (!dropdown.contains(event.target) && !userBtn.contains(event.target)) {
                dropdown.style.display = "none";
            }
        });
    </script>

    <!-- Main -->
    <h1>🎉 20% Discounted Products 🎉</h1>

    <% if (saleProducts != null && !saleProducts.isEmpty()) { %>
        <div class="product-grid">
        <% for (ProductDTO p : saleProducts) {
            double originalPrice = p.getPrice(); // giá từ DB
            double finalPrice = Math.round(originalPrice * 0.8); // giá đã giảm 20%
        %>
            <div class="product">
                <img src="<%= p.getProductImage() %>" alt="Product Image">
                <h3><%= p.getProductName() %></h3>
                <p class="original-price"><%= String.format("%,.0f", originalPrice) %> VND</p>
                <p class="discount-price"><%= String.format("%,.0f", finalPrice) %> VND (−20%)</p>
                <a class="button" href="productDetail.jsp?id=<%= p.getProductID() %>&fromSaleOff=true">View Details</a>

            </div>
        <% } %>
        </div>
    <% } else { %>
        <p style="text-align:center;">No discounted products available.</p>
    <% } %>
</body>
</html>
