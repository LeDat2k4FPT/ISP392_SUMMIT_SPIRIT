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
    <title>Homepage</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 0; }
        .header {
            padding: 20px 40px;
            background-color: #f4f4f4;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }
        .logo { font-size: 28px; font-weight: bold; color: #1a1a1a; }
        .categories {
            display: flex;
            justify-content: center;
            background-color: #fff;
            border-top: 1px solid #eee;
            border-bottom: 1px solid #eee;
            padding: 15px 0;
            gap: 40px;
        }
        .categories a {
            text-decoration: none;
            font-size: 16px;
            font-weight: bold;
            color: #333;
        }
        .categories a:hover { color: #007bff; }
        .main { padding: 40px; }
        h2 { margin-top: 0; font-size: 26px; color: #333; }
        .product-list {
            display: flex;
            gap: 40px;
            flex-wrap: wrap;
            margin-top: 30px;
        }
        .product {
            width: 250px;
            border: 1px solid #ddd;
            border-radius: 10px;
            padding: 20px;
            text-align: center;
            box-shadow: 2px 2px 8px rgba(0,0,0,0.1);
        }
        .product img {
            width: 200px;
            height: 200px;
            object-fit: contain;
            margin-bottom: 10px;
        }
        .product h4 {
            margin: 10px 0 5px;
            font-size: 16px;
        }
        .product h4 a {
            text-decoration: underline;
            color: #3c006d;
            font-weight: bold;
        }
        .product h4 a:hover {
            color: #007bff;
        }
        .product p { font-size: 14px; color: #555; }
        .product strong {
            display: block;
            margin-top: 10px;
            font-size: 16px;
            color: #000;
        }
        .user-dropdown {
            position: relative;
            display: inline-block;
            font-family: Arial;
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
            left: 0;
            background-color: white;
            min-width: 160px;
            box-shadow: 0 8px 16px rgba(0,0,0,0.2);
            border-radius: 4px;
            z-index: 1000;
        }
        .dropdown-menu a {
            padding: 12px 16px;
            display: block;
            text-decoration: none;
            color: black;
        }
        .dropdown-menu a:hover { background-color: #f1f1f1; }
    </style>
</head>
<body>

<!-- Header -->
<div class="header">
    <div class="logo">SUMMIT SPIRIT</div>
    <a href="homepage.jsp" class="btn btn-primary me-2">Home</a>
    <a href="cart.jsp" class="btn btn-secondary me-2">Cart</a>
    <div class="user-dropdown">
        <div class="user-name" onclick="toggleMenu()"><%= loginUser.getFullName() %></div>
        <div id="dropdown" class="dropdown-menu">
            <a href="profile.jsp">User Profile</a>
            <a href="MainController?action=Logout">Logout</a>
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

<!-- Categories -->
<div class="categories">
    <a href="category.jsp?category=ao">Shirts</a>
    <a href="category.jsp?category=quan">Pants</a>
    <a href="category.jsp?category=balo">Backpacks</a>
    <a href="category.jsp?category=dungcu">Gears</a>
    <a href="category.jsp?category=trai">Tents</a>
    <a href="category.jsp?category=mu">Hats</a>
    <a href="category.jsp?category=camping">Camping Stove</a>
</div>

<!-- Main Content -->
<div class="main">

    <!-- Top Sales Section -->
    <h2>🔥 Top Sales</h2>
    <div class="product-list">
        <%
            if (topSales != null && !topSales.isEmpty()) {
                for (ProductDTO p : topSales) {
        %>
        <div class="product">
            <img src="<%= p.getProductImage() %>" alt="Product Image" />
            <h4><a href="productDetail.jsp?id=<%= p.getProductID() %>"><%= p.getProductName() %></a></h4>
            <p><%= p.getDescription() %></p>
            <strong><%= String.format("%,.0f", p.getPrice()) %> VND</strong>
        </div>
        <%
                }
            } else {
        %>
        <p>No products found in Top Sales.</p>
        <%
            }
        %>
    </div>

    <!-- Featured Picks Section -->
    <h2>Featured Picks</h2>
    <div class="product-list">
        <%
            for (ProductDTO p : selectedProducts) {
        %>
        <div class="product">
            <img src="<%= p.getProductImage() %>" alt="Product Image" />
            <h4><a href="productDetail.jsp?id=<%= p.getProductID() %>"><%= p.getProductName() %></a></h4>
            <p><%= p.getDescription() %></p>
            <strong><%= String.format("%,.0f", p.getPrice()) %> VND</strong>
        </div>
        <%
            }
        %>
    </div>

</div>

<%
    String message = (String) request.getAttribute("MESSAGE");
    if (message == null) message = "";
%>
<%= message %>

</body>
</html>
