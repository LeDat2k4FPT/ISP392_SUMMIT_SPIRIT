
<!-- Phần đầu trang giữ nguyên -->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="dto.CartDTO, dto.CartItemDTO, dto.ProductDTO, dto.UserDTO" %>
<%
    CartDTO cart = (CartDTO) session.getAttribute("CART");
    UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
    if (loginUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    double discountPercent = 0.0;
    if (session.getAttribute("DISCOUNT_PERCENT") != null) {
        discountPercent = (double) session.getAttribute("DISCOUNT_PERCENT");
    }
    String discountCode = (String) session.getAttribute("DISCOUNT_CODE");
    String discountError = (String) session.getAttribute("DISCOUNT_ERROR");
    int cartItemCount = (cart != null) ? cart.getCartItems().size() : 0;
%>
<!DOCTYPE html>
<html>
<head>
    <title>Cart</title>
    <link href="https://fonts.googleapis.com/css2?family=Kumbh+Sans&display=swap" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="css/cart.css">
</head>
<body>


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
<style>
    .header {
                background-color: #004080;
                padding: 15px 30px;
                display: flex;
                justify-content: space-between;
                align-items: center;
                color: white;
            }
            .header .logo {
                font-size: 24px;
                font-weight: bold;
                color: white;
                text-decoration: none;
            }
            .nav-links a {
                color: white;
                margin: 0 10px;
                text-decoration: none;
                font-size: 16px;
            }
            .user-dropdown {
                position: relative;
                display: inline-block;
            }
            .user-name {
                cursor: pointer;
                font-weight: bold;
            }
            .dropdown-menu {
                display: none;
                position: absolute;
                background-color: white;
                box-shadow: 0px 8px 16px rgba(0,0,0,0.2);
                z-index: 1;
                right: 0;
            }
            .dropdown-menu a {
                display: block;
                padding: 10px;
                text-decoration: none;
                color: #333;
            }
            .dropdown-menu a:hover {
                background-color: #eee;
            }
            .main-wrapper {
                display: flex;
                justify-content: space-between;
                padding: 40px;
                gap: 20px;
            }
            .cart-section {
                flex: 3;
            }
            .summary-section {
                flex: 1;
                background: #fff;
                padding: 30px;
                border-radius: 10px;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
                height: fit-content;
            }
            .cart-item {
                display: flex;
                gap: 20px;
                align-items: center;
                border-bottom: 1px solid #ddd;
                padding: 20px 0;
                background-color: #fff;
                border-radius: 10px;
                margin-bottom: 20px;
            }
            .cart-item img {
                width: 100px;
            }
            .cart-info {
                flex: 1;
            }
            .cart-info h3 {
                margin: 0;
                font-size: 18px;
            }
            .price {
                font-weight: bold;
                min-width: 120px;
                text-align: right;
            }
            .quantity-box {
                display: flex;
                align-items: center;
                gap: 8px;
                margin-top: 10px;
            }
            .quantity-box button {
                width: 30px;
                height: 30px;
                font-size: 16px;
                border: none;
                border-radius: 6px;
                background-color: #f0f0f0;
                cursor: pointer;
            }
            .quantity-box input {
                width: 40px;
                text-align: center;
                border: 1px solid #ccc;
            }
            .delete-link {
                font-size: 18px;
                color: #888;
                text-decoration: none;
                margin-left: 10px;
            }
            .summary-line {
                display: flex;
                justify-content: space-between;
                margin-bottom: 12px;
                font-size: 16px;
            }
            .total-line {
                font-weight: bold;
                font-size: 18px;
                color: #222;
                border-top: 1px solid #ccc;
                padding-top: 10px;
            }
            .btn-continue {
                width: 100%;
                padding: 15px;
                background-color: #849e98;
                color: white;
                border: none;
                font-size: 16px;
                border-radius: 5px;
                cursor: pointer;
                margin-top: 20px;
            }
            .empty-cart {
                text-align: center;
                font-size: 20px;
                padding: 80px 0;
            }
</style>        
    </head>
    <body>
        <div class="header">
            <a href="homepage.jsp" class="logo"> Summit Spirit</a>
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

            function updateQuantityOnServer(productKey, quantity) {
                const parts = productKey.split('_');
                const productID = parts[0];
                const size = parts[1];
                const color = parts[2];

                fetch('UpdateQuantityServlet', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                    body: 'productID=' + encodeURIComponent(productID) +
                            '&size=' + encodeURIComponent(size) +
                            '&color=' + encodeURIComponent(color) +
                            '&quantity=' + encodeURIComponent(quantity)
                }).catch(err => console.error("Error updating quantity:", err));
            }

            function increase(id, stock) {
                const input = document.getElementById("qty_" + id);
                let value = parseInt(input.value);
                if (value >= stock) {
                    alert("Product quantity exceeds stock");
                    input.value = stock;
                    return;
                }
                input.value = value + 1;
                updateQuantityOnServer(id, input.value);
                updateTotal();
            }

            function decrease(id) {
                const input = document.getElementById("qty_" + id);
                let value = parseInt(input.value);
                if (value <= 1) {
                    if (confirm("Quantity is 0. Remove this item from cart?")) {
                        const parts = id.split('_');
                        const productID = parts[0];
                        const size = parts[1];
                        const color = parts[2];
                        window.location.href = "RemoveFromCartServlet?productID=" + productID + "&size=" + size + "&color=" + color;
                        return;
                    } else {
                        input.value = 1;
                    }
                } else {
                    input.value = value - 1;
                    updateQuantityOnServer(id, input.value);
                    updateTotal();
                }
            }

            function handleManualInput(id, stock) {
                const input = document.getElementById("qty_" + id);
                let value = parseInt(input.value);
                if (value > stock) {
                    alert("Product quantity exceeds stock");
                    input.value = stock;
                } else if (value < 1 || isNaN(value)) {
                    if (confirm("Quantity is 0. Remove this item from cart?")) {
                        const parts = id.split('_');
                        const productID = parts[0];
                        const size = parts[1];
                        const color = parts[2];
                        window.location.href = "RemoveFromCartServlet?productID=" + productID + "&size=" + size + "&color=" + color;
                        return;
                    } else {
                        input.value = 1;
                    }
                }
                updateQuantityOnServer(id, input.value);
                updateTotal();
            }

            function updateTotal() {
                let cartItems = document.querySelectorAll(".cart-item");
                let subTotal = 0;
                cartItems.forEach(item => {
                    const pid = item.getAttribute("data-id");
                    const price = parseFloat(item.getAttribute("data-price"));
                    const qty = parseInt(document.getElementById("qty_" + pid).value);
                    subTotal += price * qty;
                });

                const discountPercent = <%= discountPercent %>;
                const discountAmount = subTotal * discountPercent / 100;
                const ship = 30000;
                const grandTotal = subTotal + ship - discountAmount;

                document.getElementById("subtotal").innerText = subTotal.toLocaleString();
                document.getElementById("ship").innerText = ship.toLocaleString();
                if (discountPercent > 0) {
                    document.getElementById("discount").innerText = "-" + discountAmount.toLocaleString();
                }
                document.getElementById("grandTotal").innerText = grandTotal.toLocaleString() + " VND";
            }

            document.addEventListener("DOMContentLoaded", () => {
                document.querySelector('.btn-continue').addEventListener('click', function () {
                    document.querySelectorAll('.cart-item').forEach(item => {
                        const pid = item.getAttribute("data-id");
                        const currentQty = document.getElementById("qty_" + pid).value;
                        const hiddenInput = document.getElementById("hidden_qty_" + pid);
                        if (hiddenInput) {
                            hiddenInput.value = currentQty;
                        }
                    });
                });
            });
        </script>

        <% if (cart == null || cart.isEmpty()) { %>
        <div class="empty-cart">Your cart is empty.</div>
        <% } else { double total = 0; %>
        <div class="main-wrapper">
            <div class="cart-section">
                <h2>Shopping cart</h2>
                <p>You have <%= cart.getTotalQuantity() %> item<%= cart.getTotalQuantity() > 1 ? "s" : "" %> in your cart</p>
                <% for (CartItemDTO item : cart.getCartItems()) {
                    ProductDTO p = item.getProduct();
                    String sizeStr = (p.getSize() != null) ? p.getSize().trim() : "";
                    String colorStr = (p.getColor() != null) ? p.getColor().trim() : "";
                    String uniqueKey = p.getProductID() + "_" + sizeStr + "_" + colorStr;
                    int quantity = item.getQuantity();
                    int stock = p.getStock();

                    double originalPrice = p.getPrice();
        double price = originalPrice;
        boolean isDiscounted = p.isFromSaleOff() ;

        if (isDiscounted) {
            price = Math.round(originalPrice * 0.8);
        }


                    double lineTotal = price * quantity;
                    total += lineTotal;
                %>
                <div class="cart-item" data-id="<%= uniqueKey %>" data-price="<%= price %>" data-stock="<%= stock %>">
                    <img src="<%= p.getProductImage() %>" alt="">
                    <div class="cart-info">
                        <h3><%= p.getProductName() %></h3>
                        <% if (!sizeStr.isEmpty()) { %>
                        <p>Size: <%= sizeStr %></p>
                        <% } %>
                        <% if (!colorStr.isEmpty()) { %>
                        <p>Color: <%= colorStr %></p>
                        <% } %>
                        <div class="quantity-box">
                            <button type="button" onclick="decrease('<%= uniqueKey %>')">−</button>
                            <input type="number" name="quantity_<%= uniqueKey %>" id="qty_<%= uniqueKey %>" value="<%= quantity %>" min="1" max="<%= stock %>" onchange="handleManualInput('<%= uniqueKey %>', <%= stock %>)">
                            <button type="button" onclick="increase('<%= uniqueKey %>', <%= stock %>)">+</button>
                            <a href="RemoveFromCartServlet?productID=<%= p.getProductID() %>&size=<%= p.getSize() %>&color=<%= p.getColor() %>" class="delete-link"><i class="fa fa-trash"></i></a>
                        </div>
                    </div>
                    <div class="price">
                        <% if (isDiscounted) {

                            double displayedOriginal = Math.round(originalPrice * 1.25);
                        %>
                        <span style="text-decoration: line-through; color: gray;">
                            <%= String.format("%,.0f", displayedOriginal * quantity) %> VND
                        </span><br>
                        <span style="color: red; font-weight: bold;">
                            <%= String.format("%,.0f", originalPrice * quantity) %> VND
                        </span>
                        <span style="color: red;"> (-20%)</span>
                        <% } else { %>
                        <span style="font-weight: bold;">
                            <%= String.format("%,.0f", originalPrice * quantity) %> VND
                        </span>
                        <% } %>
                    </div>
                </div>
                <% } %>
            </div>

            <div class="summary-section">
                <h3>ORDER SUMMARY</h3>
                <% double shipFee = 30000;
                   double discountAmount = total * discountPercent / 100;
                   double grandTotal = total + shipFee - discountAmount; %>
                <div class="summary-line">Subtotal: <span id="subtotal"><%= String.format("%,.0f", total) %></span></div>
                <div class="summary-line">Shipping: <span id="ship"><%= String.format("%,.0f", shipFee) %></span></div>
                    <% if (discountPercent > 0) { %>
                <div class="summary-line" id="discount-line">Discount (<%= discountPercent %>%): <span id="discount">-<%= String.format("%,.0f", discountAmount) %></span></div>
                <% } %>
                <div class="summary-line total-line">TOTAL <span id="grandTotal"><%= String.format("%,.0f", grandTotal) %> VND</span></div>

                <form action="GoToShippingServlet" method="post">
                    <% for (CartItemDTO item : cart.getCartItems()) {
                        ProductDTO p = item.getProduct();
                        String uniqueKey = p.getProductID() + "_" + p.getSize() + "_" + p.getColor();
                        int quantity = item.getQuantity();
                    %>
                    <input type="hidden" name="quantity_<%= uniqueKey %>" value="<%= quantity %>" id="hidden_qty_<%= uniqueKey %>">
                    <% } %>
                    <button type="submit" class="btn-continue">CONTINUE</button>
                </form>
            </div>
        </div>

        <% } %>
    </body>
</html>
