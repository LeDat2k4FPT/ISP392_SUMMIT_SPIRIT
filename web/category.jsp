<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, dao.ProductDAO, dto.ProductDTO, dto.UserDTO" %>

<%
    UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
    if (loginUser == null || !"User".equals(loginUser.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }

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
        case "dungcu": cateID = 4; categoryName = "Camping Tools"; break;
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
        <meta charset="UTF-8">
        <title>Category - <%= categoryName %></title>
        <link href="https://fonts.googleapis.com/css2?family=Kumbh+Sans&display=swap" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="css/category.css">
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


        <div class="layout">
            <div class="categories">
                <h3 class="menu-title">Category</h3>
                <a href="category.jsp?category=ao" class="<%= "ao".equals(category) ? "active" : "" %>">Shirts</a>
                <a href="category.jsp?category=quan" class="<%= "quan".equals(category) ? "active" : "" %>">Pants</a>
                <a href="category.jsp?category=balo" class="<%= "balo".equals(category) ? "active" : "" %>">Backpacks</a>
                <a href="category.jsp?category=dungcu" class="<%= "dungcu".equals(category) ? "active" : "" %>">Camping Tools</a>
                <a href="category.jsp?category=trai" class="<%= "trai".equals(category) ? "active" : "" %>">Tents</a>
                <a href="category.jsp?category=mu" class="<%= "mu".equals(category) ? "active" : "" %>">Hats</a>
                <a href="category.jsp?category=camping" class="<%= "camping".equals(category) ? "active" : "" %>">Camping Stove</a>
            </div>
            <div class="main">
                <form class="search-bar" action="category.jsp" method="get">
                    <input type="hidden" name="category" value="<%= category %>" />
                    <input type="text" name="keyword" placeholder="Search for products..." value="<%= keyword != null ? keyword : "" %>">
                    <button type="submit"><i class="fa fa-search"></i></button>
                </form>

                <div class="sort-options">
                    <a href="category.jsp?category=<%= category %>&sort=asc<%= keyword != null ? "&keyword=" + keyword : "" %>">Sort by Price: Low to High</a>
                    <a href="category.jsp?category=<%= category %>&sort=desc<%= keyword != null ? "&keyword=" + keyword : "" %>">Sort by Price: High to Low</a>
                </div>

                <h2>Category: <%= categoryName %></h2>

                <div class="product-list">
                    <%
                        if (products != null && !products.isEmpty()) {
                            for (ProductDTO p : products) {
                    %>
                    <div class="product">
                        <a href="productDetail.jsp?id=<%= p.getProductID() %>">
                            <img src="<%= p.getProductImage() %>" alt="Product Image">
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
        </div>
    </body>
</html>
