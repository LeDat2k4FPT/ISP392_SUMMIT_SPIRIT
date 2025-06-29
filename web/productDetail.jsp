
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dao.ProductDAO, dto.ProductDTO, dto.CartDTO, dto.UserDTO" %>
<%@ page import="dao.ProductVariantDAO, dao.ReviewDAO" %>
<%@ page import="dto.ReviewDTO" %>
<%@ page import="java.util.List" %>
<%
    int productID = Integer.parseInt(request.getParameter("id"));
    ProductDTO product = null;
    String categoryName = "";
    String categoryParam = "";

    UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
    CartDTO cart = (CartDTO) session.getAttribute("CART");
    int totalQuantity = (cart != null) ? cart.getTotalQuantity() : 0;

    int alreadyInCart = 0;
    if (cart != null && cart.getCartItem(productID) != null) {
        alreadyInCart = cart.getCartItem(productID).getQuantity();
    }

    List<String> sizeList = new java.util.ArrayList<>();
    List<ReviewDTO> reviews = new java.util.ArrayList<>();

    try {
        ProductDAO dao = new ProductDAO();
        product = dao.getFullProductByID(productID);
        if (product != null) {
            int cateID = product.getCateID();
            switch (cateID) {
                case 1: categoryName = "Pants"; categoryParam = "quan"; break;
                case 2: categoryName = "Shirts"; categoryParam = "ao"; break;
                case 3: categoryName = "Backpacks"; categoryParam = "balo"; break;
                case 4: categoryName = "Gears"; categoryParam = "dungcu"; break;
                case 5: categoryName = "Tents"; categoryParam = "trai"; break;
                case 6: categoryName = "Hats"; categoryParam = "mu"; break;
                case 7: categoryName = "Camping Stove"; categoryParam = "camping"; break;
                default: categoryName = "Unknown"; categoryParam = "#"; break;
            }

            ProductVariantDAO variantDAO = new ProductVariantDAO();
            sizeList = variantDAO.getAvailableSizesByProductId(productID);

            ReviewDAO reviewDAO = new ReviewDAO();
            reviews = reviewDAO.getReviewsByProductID(productID);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= product.getProductName() %></title>
    <link href="https://fonts.googleapis.com/css2?family=Kumbh+Sans&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/productDetail.css">
</head>
<body>
<div class="header">
    <img src="image/summit_logo.png" alt="Logo">
    <div class="nav-links">
        <a href="homepage.jsp">Home</a>
        <a href="cart.jsp">Cart <span class="cart-count"><%= totalQuantity %></span></a>
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

<% if (product != null) {
    int availableStock = product.getStock() - alreadyInCart;
    if (availableStock < 0) availableStock = 0;
%>
<div class="layout">
    <div class="main">
        <div class="breadcrumb">
            <a class="back-button styled-button" href="javascript:history.back()">← Back</a>
        </div>

        <div class="product-container">
            <div class="product-image">
                <img src="<%= product.getProductImage() %>" alt="Product Image">
            </div>
            <div class="product-info">
                <h1><%= product.getProductName() %></h1>
                <div class="price"><%= String.format("%,.0f", product.getPrice()) %> VND</div>
                <form action="AddToCartServlet" method="post" onsubmit="return validateBeforeSubmit(<%= availableStock %>)">
                    <input type="hidden" name="productID" value="<%= product.getProductID() %>">
                    <input type="hidden" name="productName" value="<%= product.getProductName() %>">
                    <input type="hidden" name="productImage" value="<%= product.getProductImage() %>">
                    <input type="hidden" name="price" value="<%= product.getPrice() %>">

                    <% if (sizeList != null && !sizeList.isEmpty()) { %>
                    <div class="select-group">
                        <label for="size">Size:</label>
                        <select name="size" id="size">
                            <% for (String size : sizeList) { %>
                                <option value="<%= size %>"><%= size %></option>
                            <% } %>
                        </select>
                    </div>
                    <% } else { %>
                        <input type="hidden" name="size" value="">
                    <% } %>

                    <div class="select-group">
                        <label for="quantity">Quantity:</label>
                        <div class="quantity-controls">
                            <button type="button" onclick="decreaseQuantity()">−</button>
                            <input type="number" name="quantity" id="quantity" value="1" min="1" max="<%= availableStock %>">
                            <button type="button" onclick="increaseQuantity(<%= availableStock %>)">+</button>
                        </div>
                    </div>
                    <div class="add-to-cart">
                        <button type="submit">Add to Cart</button>
                    </div>
                </form>
            </div>
        </div>

        <div class="tabs">
            <div class="tab-buttons">
                <button id="description-btn" onclick="showTab('description')">Description</button>
                <button id="reviews-btn" onclick="showTab('reviews')">Customer Reviews</button>
            </div>
            <div id="description" class="tab-content active">
                <h3>Description</h3>
                <p><%= product.getDescription() %></p>
            </div>
            <div id="reviews" class="tab-content">
                <h3>Customer Reviews</h3>
                <% if (reviews != null && !reviews.isEmpty()) {
                    for (ReviewDTO review : reviews) { %>
                        <div style="margin-bottom: 15px; padding: 10px; border-bottom: 1px solid #ccc;">
                            <strong>Rating:</strong> <%= review.getRating() %> / 5<br>
                            <strong>Comment:</strong> <%= review.getComment() %><br>
                            <small><%= review.getReviewDate() %></small>
                        </div>
                <% } } else { %>
                    <p>No reviews yet.</p>
                <% } %>
            </div>
        </div>
    </div>
</div>
<% } else { %>
    <p>Product not found.</p>
<% } %>

<script>
    function showTab(tabId) {
        document.querySelectorAll('.tab-content').forEach(tab => tab.classList.remove('active'));
        document.querySelectorAll('.tab-buttons button').forEach(btn => btn.classList.remove('active'));
        document.getElementById(tabId).classList.add('active');
        document.getElementById(tabId + '-btn').classList.add('active');
    }
    function decreaseQuantity() {
        const input = document.getElementById("quantity");
        if (parseInt(input.value) > 1) input.value = parseInt(input.value) - 1;
    }
    function increaseQuantity(maxAvailable) {
        const input = document.getElementById("quantity");
        if (parseInt(input.value) < maxAvailable) input.value = parseInt(input.value) + 1;
        else alert("Số lượng đã vượt quá tồn kho còn lại.");
    }
    function validateBeforeSubmit(maxAvailable) {
        const input = document.getElementById("quantity");
        return parseInt(input.value) <= maxAvailable;
    }
    window.onload = () => showTab('description');
</script>
</body>
</html>
