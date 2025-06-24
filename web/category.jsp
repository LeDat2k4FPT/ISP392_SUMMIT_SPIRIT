<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, dao.ProductDAO, dto.ProductDTO, dto.CartDTO, dto.UserDTO" %>

<%
    String category = request.getParameter("category");
    String sort = request.getParameter("sort");
    String keyword = request.getParameter("keyword");
    if (sort == null) sort = "asc";

    List<ProductDTO> products = new ArrayList<>();
    int cateID = 0;
    String categoryName = "";

    switch (category) {
        case "quan": cateID = 1; categoryName = "Pants"; break;
        case "ao": cateID = 2; categoryName = "Shirts"; break;
        case "balo": cateID = 3; categoryName = "Backpacks"; break;
        case "dungcu": cateID = 4; categoryName = "Gears"; break;
        case "trai": cateID = 5; categoryName = "Tents"; break;
        case "mu": cateID = 6; categoryName = "Hats"; break;
        case "camping": cateID = 7; categoryName = "Camping Stove"; break;
        default: categoryName = "Unknown"; break;
    }

    try {
        ProductDAO dao = new ProductDAO();
        List<ProductDTO> all = dao.getProductsByCategorySorted(cateID, sort);
        if (keyword != null && !keyword.trim().isEmpty()) {
            for (ProductDTO p : all) {
                if (p.getProductName().toLowerCase().contains(keyword.toLowerCase())) {
                    products.add(p);
                }
            }
        } else {
            products = all;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
    CartDTO cart = (CartDTO) session.getAttribute("CART");
    int totalQuantity = (cart != null) ? cart.getTotalQuantity() : 0;
%>

<!DOCTYPE html>
<html>
<head>
    <title>Category - <%= categoryName %></title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 0; }

        /* Header Layout */
        .header {
            padding: 20px;
            background-color: #f4f4f4;
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 30px;
        }

        .search-form-left {
            display: flex;
            gap: 10px;
        }
        .search-form-left input {
            padding: 6px;
            border-radius: 4px;
            border: 1px solid #ccc;
            width: 200px;
        }
        .search-form-left button {
            padding: 6px 12px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .header-right {
            display: flex;
            gap: 30px;
            align-items: center;
        }
        .header-right a {
            text-decoration: none;
            color: black;
            font-size: 16px;
        }
        .header-right span {
            font-weight: bold;
            font-size: 16px;
        }
        .cart-count {
            background: red;
            color: white;
            border-radius: 50%;
            padding: 2px 6px;
            font-size: 12px;
            margin-left: 4px;
        }

        .main { padding: 40px; }
        h2 { font-size: 28px; margin-bottom: 20px; }
        .sort-options {
            margin-bottom: 30px;
            font-size: 14px;
        }
        .sort-options a {
            margin-right: 20px;
            text-decoration: none;
            color: #555;
        }
        .sort-options a:hover {
            text-decoration: underline;
            color: #007bff;
        }

        .product-list {
            display: flex;
            gap: 40px;
            flex-wrap: wrap;
        }
        .product {
            width: 250px;
            border: 1px solid #ccc;
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
            font-size: 18px;
        }
        .product h4 a {
            text-decoration: underline;
            color: #000;
        }
        .product h4 a:hover {
            color: #007bff;
        }
        .product p { font-size: 14px; color: #666; }
        .product strong { display: block; margin-top: 10px; font-size: 16px; color: #000; }

        /* Category Navigation */
        .categories {
            display: flex;
            justify-content: center;
            gap: 30px;
            padding: 15px 0;
            background-color: #f9f9f9;
            border-bottom: 1px solid #ccc;
        }
        .categories a {
            text-decoration: none;
            font-size: 16px;
            color: #333;
            padding: 8px 12px;
            border-radius: 5px;
        }
        .categories a:hover,
        .categories a.active {
            background-color: #007bff;
            color: white;
        }
    </style>
</head>
<body>

<!-- Header -->
<div class="header">
    <!-- SEARCH BÊN TRÁI -->
    <form method="get" action="category.jsp" class="search-form-left">
        <input type="hidden" name="category" value="<%= category %>"/>
        <input type="text" name="keyword" placeholder="Search..." value="<%= keyword != null ? keyword : "" %>" />
        <button type="submit">Search</button>
    </form>

    <!-- HOME | CART | USER BÊN PHẢI -->
    <div class="header-right">
        <a href="homepage.jsp">Home</a>
        <a href="cart.jsp">
            Cart
            <span class="cart-count"><%= totalQuantity %></span>
        </a>
        <% if (loginUser != null) { %>
            <span><%= loginUser.getFullName() %></span>
        <% } %>
    </div>
</div>

<!-- Category Navigation -->
<div class="categories">
    <a href="category.jsp?category=ao" class="<%= "ao".equals(category) ? "active" : "" %>">Shirts</a>
    <a href="category.jsp?category=quan" class="<%= "quan".equals(category) ? "active" : "" %>">Pants</a>
    <a href="category.jsp?category=balo" class="<%= "balo".equals(category) ? "active" : "" %>">Backpacks</a>
    <a href="category.jsp?category=dungcu" class="<%= "dungcu".equals(category) ? "active" : "" %>">Camping Tools</a>
    <a href="category.jsp?category=trai" class="<%= "trai".equals(category) ? "active" : "" %>">Tents</a>
    <a href="category.jsp?category=mu" class="<%= "mu".equals(category) ? "active" : "" %>">Hats</a>
    <a href="category.jsp?category=camping" class="<%= "camping".equals(category) ? "active" : "" %>">Camping Stove</a>
</div>

<div class="main">

    <!-- Breadcrumb -->
    <div style="font-size: 14px; margin-bottom: 10px; color: #555;">
        <a href="homepage.jsp" style="text-decoration: none; color: #888;">Home</a>
        <span style="margin: 0 5px;">/</span>
        <strong style="color: #000;"><%= categoryName.toUpperCase() %></strong>
    </div>

    <h2>Category: <%= categoryName %></h2>

    <div class="sort-options">
        <a href="category.jsp?category=<%= category %>&sort=asc<%= keyword != null ? "&keyword=" + keyword : "" %>">Sort by Price: Low to High</a>
        <a href="category.jsp?category=<%= category %>&sort=desc<%= keyword != null ? "&keyword=" + keyword : "" %>">Sort by Price: High to Low</a>
    </div>

    <div class="product-list">
        <%
            if (products != null && !products.isEmpty()) {
                for (ProductDTO p : products) {
        %>
        <div class="product">
            <a href="productDetail.jsp?id=<%= p.getProductID() %>">
                <img src="<%= p.getProductImage() %>" alt="Product Image" />
            </a>
            <h4><a href="productDetail.jsp?id=<%= p.getProductID() %>"><%= p.getProductName() %></a></h4>
            <p><%= p.getDescription() %></p>
            <strong><%= String.format("%,.0f", p.getPrice()) %> VND</strong>
        </div>
        <%
                }
            } else {
        %>
        <p>No products found in this category.</p>
        <%
            }
        %>
    </div>
</div>

</body>
</html>
