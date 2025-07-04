<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Payment Result Page</title>
    </head>
    <body>
        <h1>Kết quả thanh toán VNPay</h1>

        <p><strong>Trạng thái:</strong> <%= request.getAttribute("paymentResult") != null ? request.getAttribute("paymentResult") : "" %></p>
        <p><strong>Thông báo:</strong> <%= request.getAttribute("message") != null ? request.getAttribute("message") : "" %></p>

        <%
            String result = (String) request.getAttribute("paymentResult");
            if ("success".equals(result)) {
        %>
        <p><strong>Mã giao dịch:</strong> <%= request.getAttribute("transactionId") %></p>
        <p><strong>Số tiền:</strong> <%= request.getAttribute("amount") %> VNĐ</p>
        <p><strong>Chi tiết đơn hàng:</strong> <%= request.getAttribute("orderInfo") %></p>
        <% } else if ("failed".equals(result)) { %>
        <p><strong>Thanh toán thất bại. Vui lòng thử lại hoặc liên hệ hỗ trợ.</strong></p>
        <% } else if ("invalid".equals(result)) { %>
        <p><strong>Chữ ký không hợp lệ! Dữ liệu có thể đã bị thay đổi.</strong></p>
        <% } %>
    </body>
</html>