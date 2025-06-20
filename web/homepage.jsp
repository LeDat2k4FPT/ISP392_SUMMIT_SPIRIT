<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, dao.ProductDAO, dto.ProductDTO, dto.UserDTO" %>

<%
    UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
    if (loginUser == null || !"User".equals(loginUser.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }

    List<ProductDTO> products = (List<ProductDTO>) request.getAttribute("PRODUCT_LIST");
    if (products == null || products.isEmpty()) {
        try {
            ProductDAO dao = new ProductDAO();
            products = dao.getTopNProductsByIDs(new int[]{1, 2, 3});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    List<ProductDTO> topSales = null;
    try {
        ProductDAO dao = new ProductDAO();
        List<ProductDTO> allProducts = dao.getTop3Products();
        Collections.shuffle(allProducts);
        topSales = allProducts.size() > 4 ? allProducts.subList(0, 4) : allProducts;
    } catch (Exception e) {
        e.printStackTrace();
    }

    List<ProductDTO> selectedProducts = new ArrayList<>();
    try {
        ProductDAO dao = new ProductDAO();
        int[] categoryIDs = {1, 2, 3}; // Shirts, Pants, Backpacks
        for (int cateID : categoryIDs) {
            List<ProductDTO> list = dao.getProductsByCategorySorted(cateID, "asc");
            if (!list.isEmpty()) {
                selectedProducts.add(list.get(0));
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Homepage - Summit Spirit</title>
    <link href="https://fonts.googleapis.com/css2?family=Kumbh+Sans&display=swap" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            font-family: 'Kumbh Sans', sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f9f9f9;
        }

        .header {
            padding: 10px 40px;
            background-color: #ffffff;
            display: flex;
            align-items: center;
            justify-content: space-between;
            border-bottom: 1px solid #e0e0e0;
        }

        .header img {
            height: 60px;
        }

        .nav-links {
            display: flex;
            align-items: center;
            gap: 20px;
        }

        .nav-links a {
            text-decoration: none;
            color: #333;
            font-weight: 500;
        }

        .user-dropdown {
            position: relative;
        }

        .user-name {
            cursor: pointer;
            font-weight: bold;
            padding: 10px;
        }

        .dropdown-menu {
            display: none;
            position: absolute;
            top: 100%;
            right: 0;
            background-color: white;
            box-shadow: 0 8px 16px rgba(0,0,0,0.2);
            border-radius: 4px;
            z-index: 1000;
            min-width: 150px;
        }

        .dropdown-menu a {
            display: block;
            padding: 10px 16px;
            text-decoration: none;
            color: black;
        }

        .dropdown-menu a:hover {
            background-color: #f1f1f1;
        }

        .hero {
            position: relative;
            width: 100%;
            height: 400px;
            overflow: hidden;
        }

        .hero img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .hero-text {
            position: absolute;
            top: 40px;
            left: 40px;
            color: white;
            text-align: left;
        }

        .hero-text h1 {
            font-size: 48px;
            margin: 0;
        }

        .hero-text p {
            font-size: 20px;
        }

        .categories {
            display: flex;
            justify-content: space-between;
            background-color: #fff;
            padding: 20px 10%;
            border-top: 1px solid #eee;
            border-bottom: 1px solid #eee;
            flex-wrap: wrap;
        }

        .categories a {
            text-decoration: none;
            font-size: 16px;
            font-weight: bold;
            color: #333;
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 5px;
            flex: 1 1 100px;
        }

        .categories img,
        .categories i {
            width: 40px;
            height: 40px;
            font-size: 32px;
            display: block;
        }

        .main {
            padding: 40px;
        }

        h2 {
            font-size: 26px;
            color: #333;
            text-align: left;
            margin-left: 10px;
        }

        .product-section {
            margin-bottom: 60px;
            border-top: 2px solid #eee;
            padding-top: 30px;
        }

        .thin-divider {
            width: 90%;
            height: 1px;
            background-color: #ccc;
            margin: 40px auto;
        }

        .product-section h2 {
            margin-left: 0;
            padding-left: 10px;
            border-left: 4px solid #ff6a00;
        }

        .product-list {
            display: flex;
            justify-content: center;
            flex-wrap: wrap;
            gap: 30px;
            margin-top: 30px;
        }

        .product {
            background-color: white;
            width: 300px;
            padding: 20px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            transition: transform 0.2s;
        }

        .product:hover {
            transform: translateY(-5px);
        }

        .product img {
            width: 100%;
            height: 200px;
            object-fit: contain;
            margin-bottom: 10px;
        }

        .product h4 {
            font-size: 16px;
            margin: 10px 0 5px;
        }

        .product h4 a {
            text-decoration: none;
            color: #3c006d;
        }

        .product h4 a:hover {
            color: #007bff;
        }

        .product p {
            font-size: 14px;
            color: #666;
        }

        .product strong {
            display: block;
            margin-top: 10px;
            font-size: 16px;
            color: #000;
        }
    </style>
</head>
<body>
<!-- Header -->
<div class="header">
    <img src="image/summit_logo.png" alt="Logo">
    <div class="nav-links">
        <a href="homepage.jsp">Home</a>
        <a href="cart.jsp">Cart</a>
        <div class="user-dropdown">
            <div class="user-name" onclick="toggleMenu()"><%= loginUser.getFullName() %></div>
            <div id="dropdown" class="dropdown-menu">
                <a href="profile.jsp">User Profile</a>
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

<!-- Hero Banner -->
<div class="hero">
    <img src="image/hero_banner.png" alt="Hero Banner">
    <div class="hero-text">
        <h1>SUMMIT SPIRIT</h1>
        <p>Climb Higher, Live Brighter</p>
    </div>
</div>

<!-- Categories -->
<div class="categories">
    <a href="category.jsp?category=ao" data-category="Shirts">
        <img src="image/logo_ao.png" alt="Shirts">
        Shirts
    </a>
    <a href="category.jsp?category=quan" data-category="Pants">
        <img src="image/logo_quan.png" alt="Pants">
        Pants
    </a>
    <a href="category.jsp?category=balo" data-category="Backpacks">
        <img src="image/logo_balo.png" alt="Backpacks">
        Backpacks
    </a>
    <a href="category.jsp?category=dungcu" data-category="Camping Tools">
        <img src="image/logo_tool.png" alt="Camping Tools">
        Camping Tools
    </a>
    <a href="category.jsp?category=trai" data-category="Tents">
        <img src="image/logo_leu.png" alt="Tents">
        Tents
    </a>
    <a href="category.jsp?category=mu" data-category="Hats">
        <i class="fa-solid fa-hat-cowboy"></i>
        Hats
    </a>
    <a href="category.jsp?category=camping" data-category="Camping Stove">
        <i class="fa-solid fa-fire"></i>
        Camping Stove
    </a>
</div>

<!-- Main Content -->
<div class="main">
    <h2>🔥 Top Sales</h2>
    <div class="product-list">
        <% if (topSales != null && !topSales.isEmpty()) {
            for (ProductDTO p : topSales) { %>
        <div class="product">
            <img src="<%= p.getProductImage() %>" alt="Product Image">
            <h4><a href="productDetail.jsp?id=<%= p.getProductID() %>"><%= p.getProductName() %></a></h4>
            <p><%= p.getDescription() %></p>
            <strong><%= String.format("%,.0f", p.getPrice()) %> VND</strong>
        </div>
        <% }} else { %>
        <p>No products found in Top Sales.</p>
        <% } %>
    </div>
<!-- Divider between sections -->
<div class="thin-divider"></div>

    <h2>Featured Picks</h2>
    <div class="product-list">
        <% for (ProductDTO p : selectedProducts) { %>
        <div class="product">
            <img src="<%= p.getProductImage() %>" alt="Product Image">
            <h4><a href="productDetail.jsp?id=<%= p.getProductID() %>"><%= p.getProductName() %></a></h4>
            <p><%= p.getDescription() %></p>
            <strong><%= String.format("%,.0f", p.getPrice()) %> VND</strong>
        </div>
        <% } %>
    </div>
</div>

<%
    String message = (String) request.getAttribute("MESSAGE");
    if (message == null) message = "";
%>
<%= message %>

</body>
</html>
