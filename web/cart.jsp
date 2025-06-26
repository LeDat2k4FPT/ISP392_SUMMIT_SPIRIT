<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dto.CartDTO, dto.CartItemDTO, dto.ProductDTO" %>
<%
    CartDTO cart = (CartDTO) session.getAttribute("CART");
    double discountPercent = 0.0;
    if (session.getAttribute("DISCOUNT_PERCENT") != null) {
        discountPercent = (double) session.getAttribute("DISCOUNT_PERCENT");
    }
    String discountCode = (String) session.getAttribute("DISCOUNT_CODE");
    String discountError = (String) session.getAttribute("DISCOUNT_ERROR");
    String error = request.getParameter("error");
    String pidError = request.getParameter("pid");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Cart</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; background-color: #f9f9f9; }
        .header { background-color: #004080; padding: 15px 30px; display: flex; justify-content: space-between; align-items: center; color: white; }
        .header .logo { font-size: 24px; font-weight: bold; color: white; text-decoration: none; }
        .back-btn { padding: 15px 30px; }
        .back-btn button { padding: 10px 20px; font-size: 14px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; }
        .main-container { display: flex; padding: 40px; gap: 40px; }
        .left-panel, .right-panel { background: #fff; padding: 30px; border-radius: 10px; }
        .left-panel { flex: 1; }
        .right-panel { flex: 1; }
        .form-group { margin-bottom: 15px; }
        .form-group input { width: 100%; padding: 10px; font-size: 14px; border-radius: 4px; border: 1px solid #ccc; }
        .discount-area { display: flex; gap: 10px; margin: 20px 0; }
        .discount-area input { flex: 1; }
        .discount-area button { background-color: #333; color: white; padding: 10px 20px; border: none; border-radius: 4px; }
        .cart-item { display: flex; gap: 20px; align-items: center; border-bottom: 1px solid #ddd; padding: 20px 0; }
        .cart-item img { width: 100px; }
        .cart-info { flex: 1; }
        .cart-info h3 { margin: 0; }
        .price { font-weight: bold; }
        .quantity-box { display: flex; align-items: center; gap: 8px; margin-top: 10px; }
        .quantity-box button { width: 30px; height: 30px; font-size: 16px; border: none; border-radius: 50%; background-color: #f0f0f0; cursor: pointer; }
        .quantity-box input { width: 40px; text-align: center; border: 1px solid #ccc; }
        .delete-link { font-size: 20px; color: red; text-decoration: none; }
        .summary-line { display: flex; justify-content: space-between; margin-bottom: 12px; font-size: 16px; }
        .total-line { font-weight: bold; font-size: 18px; color: green; }
        .btn-continue { width: 100%; padding: 15px; background-color: #28a745; color: white; border: none; font-size: 16px; border-radius: 5px; cursor: pointer; }
        .empty-cart { text-align: center; font-size: 20px; padding: 80px 0; }
    </style>
</head>
<body>
<div class="header">
    <a href="homepage.jsp" class="logo">🏕 Summit Spirit</a>
</div>
<div class="back-btn">
    <button onclick="history.back()">← Return to previous page</button>
</div>
<% if (cart == null || cart.isEmpty()) { %>
<div class="empty-cart">Your cart is empty.</div>
<% } else { double total = 0; %>
<div class="main-container">
    <div class="left-panel">
        <h2>Shipping Address</h2>
        <form action="checkout.jsp" method="post" id="cartForm">
            <div class="form-group"><input type="text" placeholder="Country/Region"></div>
            <div class="form-group"><input type="text" placeholder="Full Name"></div>
            <div class="form-group"><input type="text" placeholder="Phone"></div>
            <div class="form-group"><input type="email" placeholder="Email"></div>
            <div class="form-group"><input type="text" placeholder="Address"></div>
            <div class="form-group"><input type="text" placeholder="District"></div>
            <div class="form-group"><input type="text" placeholder="City"></div>
            <div class="discount-area">
                <form action="ApplyDiscountServlet" method="post">
                    <input type="text" name="discountCode" placeholder="Discount code..." value="<%= discountCode != null ? discountCode : "" %>">
                    <button type="submit">Apply</button>
                </form>
            </div>
            <% if (discountError != null) { %>
                <p style="color: red;"><%= discountError %></p>
                <% session.removeAttribute("DISCOUNT_ERROR"); %>
            <% } %>
            <button type="submit" class="btn-continue">Continue To Pay</button>
        </form>
    </div>
    <div class="right-panel">
        <h2>Your Cart</h2>
        <% for (CartItemDTO item : cart.getCartItems()) {
            ProductDTO p = item.getProduct();
            String uniqueKey = p.getProductID() + "_" + p.getSize();
            int quantity = item.getQuantity();
            int stock = p.getStock();
            double price = p.getPrice();
            double lineTotal = price * quantity;
            total += lineTotal;
        %>
        <div class="cart-item" data-id="<%= uniqueKey %>" data-price="<%= price %>" data-stock="<%= stock %>">
            <img src="<%= p.getProductImage() %>" alt="">
            <div class="cart-info">
                <h3><%= p.getProductName() %></h3>
                <p>Size: <%= p.getSize() %></p>
                <div class="quantity-box">
                    <button type="button" onclick="decrease('<%= uniqueKey %>')">−</button>
                    <input type="number" name="quantity_<%= uniqueKey %>" id="qty_<%= uniqueKey %>" value="<%= quantity %>" min="1" max="<%= stock %>" onchange="handleManualInput('<%= uniqueKey %>', <%= stock %>)">
                    <button type="button" onclick="increase('<%= uniqueKey %>', <%= stock %>)">+</button>
                </div>
                <% if ("overstock".equals(error) && pidError != null && pidError.equals(uniqueKey)) { %>
                    <p style="color: red; font-size: 14px; margin-top: 5px;">Sản phẩm đã vượt quá tồn kho</p>
                <% } %>
            </div>
            <div class="price"><%= String.format("%,.0f", lineTotal) %>₫</div>
            <a href="RemoveFromCartServlet?productID=<%= p.getProductID() %>&size=<%= p.getSize() %>" class="delete-link">×</a>
        </div>
        <% } %>
        <% double shipFee = 30000;
           double discountAmount = total * discountPercent / 100;
           double grandTotal = total + shipFee - discountAmount; %>
        <div class="summary-line">Subtotal: <span><%= String.format("%,.0f", total) %>₫</span></div>
        <div class="summary-line">Shipping: <span><%= String.format("%,.0f", shipFee) %>₫</span></div>
        <% if (discountPercent > 0) { %>
            <div class="summary-line">Discount (<%= discountPercent %>%): <span>-<%= String.format("%,.0f", discountAmount) %>₫</span></div>
        <% } %>
        <div class="summary-line total-line">Total: <span id="grandTotal"><%= String.format("%,.0f", grandTotal) %>₫</span></div>
    </div>
</div>
<script>
    function increase(id, stock) {
        const input = document.getElementById("qty_" + id);
        let value = parseInt(input.value);
        if (value >= stock) {
            alert("Sản phẩm đã vượt quá tồn kho");
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
                window.location.href = "RemoveFromCartServlet?key=" + id;
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
            alert("Sản phẩm đã vượt quá tồn kho");
            input.value = stock;
        } else if (value < 1 || isNaN(value)) {
            input.value = 1;
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
        document.getElementById("grandTotal").innerText = grandTotal.toLocaleString() + "₫";
    }
    function updateQuantityOnServer(productKey, quantity) {
        fetch('UpdateQuantityServlet', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: 'key=' + productKey + '&quantity=' + quantity
        }).catch(err => console.error("Error updating quantity:", err));
    }
</script>
<% } %>
</body>
</html>
