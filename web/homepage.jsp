<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, dao.ProductDAO, dto.ProductDTO, dto.UserDTO" %>

<%
    UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
    if (user == null) {
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

    String currentCategory = (String) request.getAttribute("CURRENT_CATEGORY");
    String sortOrder = (String) request.getAttribute("SORT_ORDER");
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
        .logo {
            font-size: 28px;
            font-weight: bold;
            color: #1a1a1a;
        }

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
        .categories a:hover {
            color: #007bff;
        }

        .main {
            padding: 40px;
        }

        h2 {
            margin-top: 0;
            font-size: 26px;
            color: #333;
        }

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

        .product p {
            font-size: 14px;
            color: #555;
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
    <div class="logo">SUMMIT SPIRIT</div>
</div>

<!-- Categories Horizontal -->
<div class="categories">
    <a href="ViewProductController?category=ao">Shirts</a>
    <a href="ViewProductController?category=quan">Pants</a>
    <a href="ViewProductController?category=balo">Backpacks</a>
    <a href="ViewProductController?category=dungcu">Gears</a>
    <a href="ViewProductController?category=trai">Tents</a>
</div>

<!-- Main Content -->
<div class="main">
    <h2>Top Sales</h2>

    <div class="product-list">
        <%
            if (products != null && !products.isEmpty()) {
                for (ProductDTO p : products) {
        %>
        <div class="product">
            <img src="<%= p.getProductImage() %>" alt="Product Image" />
            <h4><%= p.getProductName() %></h4>
            <p><%= p.getDescription() %></p>
            <strong><%= String.format("%,.0f", p.getPrice()) %> VND</strong>
        </div>
        <%
                }
            } else {
        %>
        <p>No products available to display.</p>
        <%
            }
        %>
    </div>
</div>

</body>
</html>
