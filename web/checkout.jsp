<%--
    Document   : checkout
    Created on : Jul 3, 2025, 9:11:24 PM
    Author     : gmt
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="dto.UserDTO, dao.ProductDAO, dto.ProductDTO"%>
<%@page import="java.util.List, java.util.ArrayList" %>
<%@page import="dao.ProductVariantDAO" %>
<%@page import="dto.VoucherDTO, dao.VoucherDAO" %>
<%--<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>--%>

<%
    UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");

    double discountPercent = 0.0;
    String discountCode = "";
    String discountError = null;

    Double appliedDiscountPercent = (Double) request.getAttribute("DISCOUNT_PERCENT");
    String appliedDiscountCode = (String) request.getAttribute("DISCOUNT_CODE");
    String discountErrorMsg = (String) request.getAttribute("DISCOUNT_ERROR");

    if (appliedDiscountPercent != null) discountPercent = appliedDiscountPercent;
    if (appliedDiscountCode != null) discountCode = appliedDiscountCode;
    if (discountErrorMsg != null) discountError = discountErrorMsg;

    List<String> sizeList = new ArrayList<>();
    List<String> colorList = new ArrayList<>();

    // Lấy thông tin sản phẩm từ request
    String paramProductId = request.getParameter("productId");
    String paramSize = request.getParameter("size");
    String paramColor = request.getParameter("color");
    String paramQuantity = request.getParameter("quantity");
    String code = request.getParameter("discountCode");
    VoucherDAO dao = new VoucherDAO();
    VoucherDTO voucher = dao.getVoucherByCode(code);

    if (voucher != null) {
        request.setAttribute("DISCOUNT_CODE", voucher.getVoucherCode());
        request.setAttribute("DISCOUNT_PERCENT", voucher.getDiscountValue());
        request.setAttribute("VOUCHER_ID", voucher.getVoucherID());
    } else {
        request.setAttribute("DISCOUNT_ERROR", "Invalid or expired code!");
    }

    if (paramProductId != null) {
        int productId = Integer.parseInt(paramProductId);
        ProductVariantDAO variantDAO = new ProductVariantDAO();
        try {
            sizeList = variantDAO.getAvailableSizesByProductId(productId);
            colorList = variantDAO.getAvailableColorsByProductId(productId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ProductDTO product = null;
    int quantity = 1;
    boolean hasSaleOffProduct = false;

    if (paramProductId != null && paramQuantity != null) {
        int productId = Integer.parseInt(paramProductId);
        quantity = Integer.parseInt(paramQuantity);

        ProductDAO productDAO = new ProductDAO();
        product = productDAO.getProductByID(productId);

        if (product != null) {
            if (paramSize != null) product.setSize(paramSize);
            if (paramColor != null) product.setColor(paramColor);
            product.setQuantity(quantity);
            boolean fromSaleOff = "true".equals(request.getParameter("fromSaleOff"));
            product.setFromSaleOff(fromSaleOff); // ✅ Đánh dấu giảm giá
            session.setAttribute("BUY_NOW_PRODUCT", product); // ✅ Lưu session để servlet dùng
            session.setAttribute("FROM_BUY_NOW", true);
            hasSaleOffProduct = fromSaleOff;
        }
    }

    String country = request.getParameter("country") != null ? request.getParameter("country") : "";
    String fullname = request.getParameter("fullname") != null ? request.getParameter("fullname") : "";
    String phone = request.getParameter("phone") != null ? request.getParameter("phone") : "";
    String email = request.getParameter("email") != null ? request.getParameter("email") : "";
    String address = request.getParameter("address") != null ? request.getParameter("address") : "";
    String district = request.getParameter("district") != null ? request.getParameter("district") : "";
    String city = request.getParameter("city") != null ? request.getParameter("city") : "";
    Integer voucherID = (Integer) request.getAttribute("VOUCHER_ID");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Checkout Page</title>
        <link href="https://fonts.googleapis.com/css2?family=Kumbh+Sans&display=swap" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
        <style>
            body {
                font-family: 'Kumbh Sans', sans-serif;
                margin: 0;
                background-color: #f9f9f9;
            }
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
            .container {
                display: flex;
                padding: 40px;
                gap: 40px;
            }
            .form-section {
                flex: 1;
                background: #fff;
                padding: 30px;
                border-radius: 10px;
            }
            .form-section input {
                width: 100%;
                padding: 10px;
                margin-bottom: 15px;
                border-radius: 4px;
                border: 1px solid #ccc;
                font-size: 14px;
            }
            .form-section .row {
                display: flex;
                gap: 10px;
            }
            .form-section button {
                background-color: #2f4f4f;
                color: white;
                padding: 10px 20px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
            }
            .cart-preview {
                flex: 1;
                background: #f4f4f4;
                padding: 20px;
                border-radius: 10px;
            }
            .cart-item {
                display: flex;
                gap: 15px;
                padding: 15px 0;
                border-bottom: 1px solid #ccc;
            }
            .cart-item img {
                width: 80px;
                height: auto;
            }
            .cart-info h4 {
                margin: 0;
                font-size: 16px;
            }
            .cart-info p {
                margin: 4px 0;
                font-size: 14px;
            }
            .quantity-box {
                display: flex;
                gap: 10px;
                margin-top: 8px;
                align-items: center;
            }
            .summary-line {
                display: flex;
                justify-content: space-between;
                margin: 10px 0;
                font-size: 15px;
            }
            .total-line {
                font-weight: bold;
                font-size: 18px;
                margin-top: 10px;
            }
            .footer-buttons {
                display: flex;
                justify-content: space-between;
                margin-top: 20px;
            }
            .footer-buttons a, .footer-buttons button {
                padding: 10px 20px;
                font-size: 14px;
                border: none;
                border-radius: 4px;
                text-decoration: none;
                color: white;
            }
            .back-btn {
                background-color: gray;
            }
            .pay-btn {
                background-color: #2f4f4f;
            }
            .error-message {
                color: red;
                margin-top: -10px;
                margin-bottom: 10px;
                font-size: 14px;
            }
            .success-message {
                color: green;
                margin-top: -10px;
                margin-bottom: 10px;
                font-size: 14px;
            }
        </style>
    </head>
    <body>
        <div class="header">
            <a href="homepage.jsp" class="logo"> Summit Spirit</a>
            <div class="nav-links">
                <a href="homepage.jsp">Home</a>
                <a href="cart.jsp">Cart</a>
                <a href="profile.jsp"><i class="fa fa-user"></i></a>
            </div>
        </div>
        <form id="checkoutForm" action="payment" method="POST" onsubmit="return validateForm()">
            <div class="container">
                <div class="form-section">
                    <h3>Shipping Address</h3>
                    <input type="text" name="country" placeholder="Country/Region" value="<%= country %>" required>
                    <div class="row">
                        <input type="text" name="fullname" placeholder="Full Name" value="<%= fullname %>" required>
                        <input type="text" name="phone" placeholder="Phone" value="<%= phone %>" required>
                        <div class="error-message" id="phoneError"></div>
                    </div>
                    <input type="email" name="email" placeholder="Email" value="<%= email %>" required>
                    <div class="error-message" id="emailError"></div>
                    <input type="text" name="address" placeholder="Address" value="<%= address %>" required>
                    <div class="row">
                        <input type="text" name="district" placeholder="District" value="<%= district %>" required>
                        <input type="text" name="city" placeholder="City" value="<%= city %>" required>
                    </div>

                    <% if (!hasSaleOffProduct) { %>
                    <div class="row">
                        <input type="hidden" name="sourcePage" value="checkout.jsp">
                        <input type="hidden" name="productId" value="<%= paramProductId %>">
                        <input type="hidden" name="quantity" value="<%= paramQuantity %>">
                        <input type="text" name="discountCode" placeholder="Discount code" value="<%= discountCode %>">
                        <button type="submit" name="action" value="ApplyDiscount" onclick="setFormAction('MainController')">Apply</button>
                    </div>
                    <% if (discountError != null) { %>
                    <div class="error-message"><%= discountError %></div>
                    <% } else if (!discountCode.trim().isEmpty() && discountPercent > 0) { %>
                    <div class="success-message">Discount code has been applied!</div>
                    <% } %>
                    <% } else { %>
                    <div class="error-message">You cannot apply the discount code because the product is already discounted.</div>
                    <% } %>

                    <div class="footer-buttons">
                        <% if (product != null) {
                            double total = product.getPrice() * quantity;
                            double shipFee = 30000;
                            double discountAmount = total * discountPercent / 100;
                            double grandTotal = total + shipFee - discountAmount;
                        %>
                        <input type="hidden" name="amount" value="<%= (int) grandTotal %>">
                        <input type="hidden" name="productName" value="<%= product.getProductName() %>">
                        <input type="hidden" name="size" value="<%= product.getSize() != null ? product.getSize() : "N/A" %>">
                        <input type="hidden" name="color" value="<%= product.getColor() != null ? product.getColor() : "N/A" %>">
                        <input type="hidden" name="productId" value="<%= paramProductId %>">
                        <input type="hidden" name="quantity" value="<%= paramQuantity %>">
                        <input type="hidden" name="fromSaleOff" value="<%= hasSaleOffProduct %>">
                        <input type="hidden" name="discountCode" value="<%= discountCode != null ? discountCode : "" %>">
                        <input type="hidden" name="discountPercent" value="<%= discountPercent %>">
                        <% if (voucherID != null) { %>
                        <input type="hidden" name="voucherID" value="<%= voucherID %>">
                        <% } %>
                        <% } %>
                        <button type="submit" name="action" value="CheckoutVNPay" class="pay-btn" onclick="setFormAction('payment')">Continue To Pay</button>
                    </div>
                </div>

                <div>
                    <h3>Your Order</h3>
                    <% if (product != null) {
                            double total = product.getPrice() * quantity;
                            double shipFee = 30000;
                            double discountAmount = total * discountPercent / 100;
                            double grandTotal = total + shipFee - discountAmount;
                    %>
                    <div class="cart-item">
                        <img src="<%= product.getProductImage() %>" alt="">
                        <div class="cart-info">
                            <h4><%= product.getProductName() %></h4>
                            <% if (product.getSize() != null && !product.getSize().isEmpty()) { %>
                            <p>Size: <%= product.getSize() %></p>
                            <% } %>
                            <% if (product.getColor() != null && !product.getColor().isEmpty()) { %>
                            <p>Color: <%= product.getColor() %></p>
                            <% } %>
                            <div class="quantity-box"><span><%= quantity %></span></div>
                        </div>
                        <div><%= String.format("%,.0f", product.getPrice()) %></div>
                    </div>
                    <div class="summary-line">Subtotal: <span><%= String.format("%,.0f", total) %></span></div>
                    <div class="summary-line">Ship: <span><%= String.format("%,.0f", shipFee) %></span></div>
                    <div class="summary-line">Voucher: <span><%= discountPercent > 0 ? "-" + String.format("%,.0f", discountAmount) : "0" %></span></div>
                    <div class="summary-line total-line">Total: <span><%= String.format("%,.0f", grandTotal) %></span></div>
                    <% } %>
                </div>
            </div>
        </form>

        <script>
            function setFormAction(actionUrl) {
                document.getElementById('checkoutForm').action = actionUrl;
            }

            function validateForm() {
                let valid = true;
                document.getElementById('phoneError').innerText = "";
                document.getElementById('emailError').innerText = "";

                const phone = document.querySelector('input[name="phone"]').value.trim();
                const phonePattern = /^0\d{9}$/;
                const email = document.querySelector('input[name="email"]').value.trim();
                const emailPattern = /^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,6}$/;
                if (!phonePattern.test(phone)) {
                    document.getElementById('phoneError').innerText = "Invalid phone number format!";
                    valid = false;
                }
                if (!emailPattern.test(email)) {
                    document.getElementById('emailError').innerText = "Invalid email format!";
                    valid = false;
                }
                return valid;
            }
        </script>
    </body>
</html>

