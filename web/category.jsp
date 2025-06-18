<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, dao.ProductDAO, dto.ProductDTO" %>

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
%>

<!DOCTYPE html>
<html>
<head>
    <title>Category - <%= categoryName %></title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 0; }
        .header {
            padding: 20px;
            background-color: #f4f4f4;
            display: flex;
            align-items: center;
        }
        .logo { font-size: 24px; font-weight: bold; color: #1a1a1a; }
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
        .search-form {
            margin-left: auto;
            display: flex;
            gap: 10px;
        }
        .search-form input {
            padding: 6px;
            border-radius: 4px;
            border: 1px solid #ccc;
            width: 200px;
        }
        .search-form button {
            padding: 6px 12px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
    </style>
</head>
<body>

<div class="header">
    <div class="logo">SUMMIT SPIRIT</div>
    <form method="get" action="category.jsp" class="search-form">
        <input type="hidden" name="category" value="<%= category %>"/>
        <input type="text" name="keyword" placeholder="Search..." value="<%= keyword != null ? keyword : "" %>" />
        <button type="submit">Search</button>
    </form>
</div>

<div class="main">

    <!-- BREADCRUMB -->
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
