<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*, java.text.DecimalFormat, dto.CartDTO, dto.CartItemDTO, dto.ProductDTO, dto.UserDTO" %>
<%@ page import="utils.DBUtils" %>

<%
    CartDTO cart = (CartDTO) session.getAttribute("CART");
    UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");

    double discountPercent = 0.0;
    String discountCode = "";
    String discountError = null;

    String country = request.getParameter("country") != null ? request.getParameter("country") : "";
    String fullname = request.getParameter("fullname") != null ? request.getParameter("fullname") : "";
    String phone = request.getParameter("phone") != null ? request.getParameter("phone") : "";
    String email = request.getParameter("email") != null ? request.getParameter("email") : "";
    String address = request.getParameter("address") != null ? request.getParameter("address") : "";
    String district = request.getParameter("district") != null ? request.getParameter("district") : "";
    String city = request.getParameter("city") != null ? request.getParameter("city") : "";

    if ("POST".equalsIgnoreCase(request.getMethod()) && "apply".equals(request.getParameter("action"))) {
        discountCode = request.getParameter("discountCode");
        if (discountCode != null && !discountCode.trim().isEmpty()) {
            try {
                Connection conn = DBUtils.getConnection();
                String sql = "SELECT DiscountValue FROM Voucher WHERE VoucherCode = ? AND Status = 'Active' AND ExpiryDate >= GETDATE()";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, discountCode.trim());
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    discountPercent = rs.getDouble("DiscountValue");
                    session.setAttribute("DISCOUNT_PERCENT", discountPercent);
                    session.setAttribute("DISCOUNT_CODE", discountCode);
                    session.removeAttribute("DISCOUNT_ERROR");
                } else {
                    discountError = "Code not available.";
                    session.removeAttribute("DISCOUNT_PERCENT");
                    session.removeAttribute("DISCOUNT_CODE");
                    session.setAttribute("DISCOUNT_ERROR", discountError);
                }

                rs.close();
                ps.close();
                conn.close();
            } catch (Exception e) {
                discountError = "Error checking code: " + e.getMessage();
                session.removeAttribute("DISCOUNT_PERCENT");
                session.removeAttribute("DISCOUNT_CODE");
                session.setAttribute("DISCOUNT_ERROR", discountError);
            }
        }
    }

    if (session.getAttribute("DISCOUNT_PERCENT") != null) {
        discountPercent = (double) session.getAttribute("DISCOUNT_PERCENT");
    }
    if (session.getAttribute("DISCOUNT_CODE") != null) {
        discountCode = (String) session.getAttribute("DISCOUNT_CODE");
    }
    if (session.getAttribute("DISCOUNT_ERROR") != null) {
        discountError = (String) session.getAttribute("DISCOUNT_ERROR");
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Shipping Address - Summit Spirit</title>
    <link href="https://fonts.googleapis.com/css2?family=Kumbh+Sans&display=swap" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        body { font-family: 'Kumbh Sans', sans-serif; margin: 0; background-color: #f9f9f9; }
        .header { background-color: #004080; padding: 15px 30px; display: flex; justify-content: space-between; align-items: center; color: white; }
        .header .logo { font-size: 24px; font-weight: bold; color: white; text-decoration: none; }
        .nav-links a { color: white; margin: 0 10px; text-decoration: none; font-size: 16px; }
        .container { display: flex; padding: 40px; gap: 40px; }
        .form-section { flex: 1; background: #fff; padding: 30px; border-radius: 10px; }
        .form-section input { width: 100%; padding: 10px; margin-bottom: 15px; border-radius: 4px; border: 1px solid #ccc; font-size: 14px; }
        .form-section .row { display: flex; gap: 10px; }
        .form-section button { background-color: #2f4f4f; color: white; padding: 10px 20px; border: none; border-radius: 4px; cursor: pointer; }
        .cart-preview { flex: 1; background: #f4f4f4; padding: 20px; border-radius: 10px; }
        .cart-item { display: flex; gap: 15px; padding: 15px 0; border-bottom: 1px solid #ccc; }
        .cart-item img { width: 80px; height: auto; }
        .cart-info h4 { margin: 0; font-size: 16px; }
        .cart-info p { margin: 4px 0; font-size: 14px; }
        .quantity-box { display: flex; gap: 10px; margin-top: 8px; align-items: center; }
        .summary-line { display: flex; justify-content: space-between; margin: 10px 0; font-size: 15px; }
        .total-line { font-weight: bold; font-size: 18px; margin-top: 10px; }
        .footer-buttons { display: flex; justify-content: space-between; margin-top: 20px; }
        .footer-buttons a, .footer-buttons button { padding: 10px 20px; font-size: 14px; border: none; border-radius: 4px; text-decoration: none; color: white; }
        .back-btn { background-color: gray; }
        .pay-btn { background-color: #2f4f4f; }
        .error-message { color: red; margin-top: -10px; margin-bottom: 10px; font-size: 14px; }
        .success-message { color: green; margin-top: -10px; margin-bottom: 10px; font-size: 14px; }
    </style>
</head>
<body>
<div class="header">
    <a href="homepage.jsp" class="logo">🏕 Summit Spirit</a>
    <div class="nav-links">
        <a href="homepage.jsp">Home</a>
        <a href="cart.jsp">Cart</a>
        <a href="profile.jsp"><i class="fa fa-user"></i></a>
    </div>
</div>

<form method="post" action="checkout.jsp" onsubmit="return validateForm()">
<div class="container">
    <div class="form-section">
        <h3>Shipping Address</h3>
        <input type="text" name="country" placeholder="Country/Region" value="<%= country %>" required>
        <div class="row">
            <input type="text" name="fullname" placeholder="Full Name" value="<%= fullname %>" required>
            <input type="text" name="phone" placeholder="Phone" value="<%= phone %>" required>
        </div>
        <input type="email" name="email" placeholder="Email" value="<%= email %>" required>
        <input type="text" name="address" placeholder="Address" value="<%= address %>" required>
        <div class="row">
            <input type="text" name="district" placeholder="District" value="<%= district %>" required>
            <input type="text" name="city" placeholder="City" value="<%= city %>" required>
        </div>
        <div class="row">
            <input type="text" name="discountCode" placeholder="Discount code" value="<%= discountCode %>">
            <button type="submit" name="action" value="apply">Apply</button>
        </div>

        <% if (discountError != null) { %>
            <div class="error-message"><%= discountError %></div>
        <% } else if (discountCode != null && !discountCode.trim().isEmpty() && discountPercent > 0) { %>
            <div class="success-message">Discount code has been applied!</div>
        <% } %>

        <div class="footer-buttons">
            <a href="cart.jsp" class="back-btn">← Return To Cart</a>
            <!-- ✅ Đây là button gửi form và sẽ bị chặn nếu chưa hợp lệ -->
            <button type="submit" class="pay-btn">Continue To Pay</button>
        </div>
    </div>

    <% if (cart != null && !cart.isEmpty()) { %>
    <div class="cart-preview">
        <h3>Your Cart</h3>
        <% double total = 0;
           for (CartItemDTO item : cart.getCartItems()) {
               ProductDTO p = item.getProduct();
               int quantity = item.getQuantity();
               double lineTotal = p.getPrice() * quantity;
               total += lineTotal;
        %>
        <div class="cart-item">
            <img src="<%= p.getProductImage() %>" alt="">
            <div class="cart-info">
                <h4><%= p.getProductName() %></h4>
                <% if (p.getSize() != null && !p.getSize().isEmpty()) { %>
                    <p>Size: <%= p.getSize() %></p>
                <% } %>
                <div class="quantity-box"><span><%= quantity %></span></div>
            </div>
            <div><%= String.format("%,.0f", lineTotal) %></div>
        </div>
        <% }
           double shipFee = 30000;
           double discountAmount = total * discountPercent / 100;
           double grandTotal = total + shipFee - discountAmount;
        %>
        <div class="summary-line">Subtotal: <span><%= String.format("%,.0f", total) %></span></div>
        <div class="summary-line">Ship: <span><%= String.format("%,.0f", shipFee) %></span></div>
        <div class="summary-line">Voucher: <span><%= discountPercent > 0 ? "-" + String.format("%,.0f", discountAmount) : "0" %></span></div>
        <div class="summary-line total-line">Total: <span><%= String.format("%,.0f", grandTotal) %></span></div>
    </div>
    <% } else { %>
        <p>No items in cart.</p>
    <% } %>
</div>
</form>

<script>
function validateForm() {
    const phone = document.querySelector('input[name="phone"]').value.trim();
    const phonePattern = /^\d{10,12}$/;

    if (!phonePattern.test(phone)) {
        alert("Phone number must be 10 to 12 digits.");
        return false;
    }

    return true;
}
</script>

</body>
</html>
