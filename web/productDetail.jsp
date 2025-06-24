<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dao.ProductDAO, dto.ProductDTO, dto.CartDTO, dto.UserDTO" %>
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
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Product Detail</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            background-color: #f9f9f9;
        }
        .header {
            display: flex;
            justify-content: flex-end;
            gap: 30px;
            align-items: center;
            padding: 20px;
            font-family: Arial, sans-serif;
        }
        .header a {
            text-decoration: none;
            color: black;
            font-size: 16px;
        }
        .header .cart-link span {
            background: red;
            color: white;
            border-radius: 50%;
            padding: 2px 6px;
            font-size: 12px;
            margin-left: 5px;
        }
        .breadcrumb {
            margin-bottom: 20px;
            font-size: 14px;
        }
        .breadcrumb a {
            text-decoration: none;
            color: #007bff;
        }
        .breadcrumb span {
            color: #555;
        }
        .product-container {
            display: flex;
            gap: 40px;
        }
        .product-image {
            flex: 1;
        }
        .product-info {
            flex: 1.2;
            background: #fff;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .product-info h1 {
            margin-top: 0;
        }
        .price {
            font-size: 24px;
            font-weight: bold;
            margin: 10px 0;
            color: #e53935;
        }
        .select-group {
            margin: 15px 0;
        }
        .select-group label {
            display: block;
            margin-bottom: 5px;
        }
        .select-group select {
            width: 120px;
            padding: 8px;
            font-size: 16px;
        }
        .add-to-cart {
            margin-top: 20px;
        }
        .add-to-cart button {
            padding: 12px 30px;
            font-size: 16px;
            background-color: #28a745;
            color: #fff;
            border: none;
            cursor: pointer;
            border-radius: 5px;
        }
        .tabs {
            margin-top: 50px;
        }
        .tab-buttons {
            display: flex;
            gap: 20px;
            margin-bottom: 20px;
        }
        .tab-buttons button {
            background: #eee;
            padding: 10px 20px;
            border: none;
            cursor: pointer;
            border-radius: 5px;
        }
        .tab-buttons button.active {
            background-color: #007bff;
            color: #fff;
        }
        .tab-content {
            display: none;
        }
        .tab-content.active {
            display: block;
            background: #fff;
            padding: 20px;
            border-radius: 5px;
        }
    </style>
    <script>
        function showTab(tabId) {
            const tabs = document.querySelectorAll('.tab-content');
            tabs.forEach(tab => tab.classList.remove('active'));
            const buttons = document.querySelectorAll('.tab-buttons button');
            buttons.forEach(btn => btn.classList.remove('active'));
            document.getElementById(tabId).classList.add('active');
            document.getElementById(tabId + '-btn').classList.add('active');
        }

        window.onload = () => {
            showTab('description');
        };

        function decreaseQuantity() {
            const input = document.getElementById("quantity");
            let current = parseInt(input.value);
            if (current > 1) {
                input.value = current - 1;
            }
        }

        function increaseQuantity(maxAvailable) {
            const input = document.getElementById("quantity");
            let current = parseInt(input.value);
            if (current < maxAvailable) {
                input.value = current + 1;
            } else {
                alert("Số lượng đã vượt quá tồn kho còn lại.");
            }
        }

        function validateBeforeSubmit(maxAvailable) {
            const input = document.getElementById("quantity");
            let current = parseInt(input.value);
            if (current > maxAvailable) {
                alert("Số lượng đặt vượt quá tồn kho còn lại.");
                return false;
            }
            return true;
        }
    </script>
</head>
<body>

<!-- Header -->
<div class="header">
    <a href="homepage.jsp">Home</a>
    <a href="cart.jsp" class="cart-link">
        Cart
        <span><%= totalQuantity %></span>
    </a>
    <% if (loginUser != null) { %>
        <span style="font-weight: bold; font-size: 16px;"><%= loginUser.getFullName() %></span>
    <% } %>
</div>

<% if (product != null) {
    int availableStock = product.getStock() - alreadyInCart;
    if (availableStock < 0) availableStock = 0;
%>
<div class="breadcrumb">
    <a href="homepage.jsp">Home</a> /
    <a href="category.jsp?category=<%= categoryParam %>"><%= categoryName %></a> /
    <span><%= product.getProductName() %></span>
</div>

<div class="product-container">
    <div class="product-image">
        <img src="<%= product.getProductImage() %>" alt="Product Image" style="width: 100%; max-width: 400px;">
    </div>
    <div class="product-info">
        <h1><%= product.getProductName() %></h1>
        <div class="price"><%= String.format("%,.0f", product.getPrice()) %> VND</div>
        <form action="AddToCartServlet" method="post" onsubmit="return validateBeforeSubmit(<%= availableStock %>)">
            <input type="hidden" name="productID" value="<%= product.getProductID() %>">
            <input type="hidden" name="productName" value="<%= product.getProductName() %>">
            <input type="hidden" name="productImage" value="<%= product.getProductImage() %>">
            <input type="hidden" name="price" value="<%= product.getPrice() %>">
            <div class="select-group">
                <label for="size">Size:</label>
                <select name="size" id="size">
                    <option value="M">M</option>
                    <option value="L">L</option>
                </select>
            </div>
            <div class="select-group">
                <label for="quantity">Quantity:</label>
                <div style="display: flex; align-items: center; gap: 10px;">
                    <button type="button" onclick="decreaseQuantity()" style="width: 30px; height: 30px;">−</button>
                    <input type="number" name="quantity" id="quantity" value="1" min="1" max="<%= availableStock %>" style="width: 50px; text-align: center;">
                    <button type="button" onclick="increaseQuantity(<%= availableStock %>)" style="width: 30px; height: 30px;">+</button>
                </div>
            </div>
            <div class="add-to-cart">
                <button type="submit">Add to Cart</button>
            </div>
        </form>
        <%
            String error = request.getParameter("error");
            String availableStr = request.getParameter("available");
            if (error != null) {
        %>
        <div style="color: red; margin-top: 10px;">
            <%
                if ("stock".equals(error)) {
                    int available = 0;
                    try { available = Integer.parseInt(availableStr); } catch (Exception ignore) {}
                    out.print(available <= 0 ? "This product is out of stock." : "Only " + available + " items are available in stock.");
                } else if ("invalid_quantity".equals(error)) {
                    out.print("Quantity must be greater than 0.");
                } else {
                    out.print("An error occurred. Please try again.");
                }
            %>
        </div>
        <% } %>
    </div>
</div>

<div class="tabs">
    <div class="tab-buttons">
        <button id="description-btn" onclick="showTab('description')">Description</button>
        <button id="reviews-btn" onclick="showTab('reviews')">Customer Reviews</button>
    </div>
    <div id="description" class="tab-content">
        <h3>Description</h3>
        <p><%= product.getDescription() %></p>
    </div>
    <div id="reviews" class="tab-content">
        <h3>Customer Reviews</h3>
        <p>No reviews yet.</p>
    </div>
</div>
<% } else { %>
<p>Product not found.</p>
<% } %>

</body>
</html>
