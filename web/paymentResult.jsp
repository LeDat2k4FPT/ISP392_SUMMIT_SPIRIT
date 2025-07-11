<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Kết quả thanh toán VNPay</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css"
              integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA=="
              crossorigin="anonymous" referrerpolicy="no-referrer" />
    </head>
    <body style="background-color: #f4f4f4; font-family: Arial, sans-serif; margin: 0; padding: 20px;">
        <section style="margin-top: 50px; text-align: center;">
            <div>
                <img src="https://cdn2.cellphones.com.vn/insecure/rs:fill:150:0/q:90/plain/https://cellphones.com.vn/media/wysiwyg/Review-empty.png"
                     alt="Transaction Status"
                     style="width: 120px; height: 120px; margin-bottom: 20px;">
            </div>

            <%
                String paymentResult = (String) request.getAttribute("paymentResult");
                String message = (String) request.getAttribute("message");
                String transactionId = (String) request.getAttribute("transactionId");
                String amount = (String) request.getAttribute("amount");
                String orderInfo = (String) request.getAttribute("orderInfo");
            %>

            <% if ("success".equals(paymentResult)) { %>
            <div>
                <h3 style="font-weight: bold; color: #28a745;">
                    Bạn đã giao dịch thành công!
                    <i class="fas fa-check-circle"></i>
                </h3>
                <% if (message != null) { %>
                <p style="font-size: 18px; margin-top: 15px;"><%= message %></p>
                <% } %>
                <%
                    if (transactionId != null) {
                %>
                <p>Mã giao dịch: <b><%= transactionId %></b></p>
                <%
                    }
                    if (amount != null) {
                %>
                <p>Số tiền: <b><%= amount %></b></p>
                <%
                    }
                    if (orderInfo != null) {
                %>
                <p>Thông tin đơn hàng: <b><%= orderInfo %></b></p>
                <%
                    }
                %>
            </div>
            <% } else if ("failed".equals(paymentResult)) { %>
            <div>
                <h3 style="font-weight: bold; color: #dc3545;">
                    Đơn hàng giao dịch thất bại!
                </h3>
                <% if (message != null) { %>
                <p style="font-size: 18px; margin-top: 15px;"><%= message %></p>
                <% } %>
            </div>
            <% } else if ("invalid".equals(paymentResult)) { %>
            <div>
                <h3 style="font-weight: bold; color: #ffc107;">
                    Chữ ký không hợp lệ! Dữ liệu có thể đã bị thay đổi.
                </h3>
                <% if (message != null) { %>
                <p style="font-size: 18px; margin-top: 15px;"><%= message %></p>
                <% } %>
            </div>
            <% } else if ("error".equals(paymentResult)) { %>
            <div>
                <h3 style="font-weight: bold; color: #dc3545;">
                    Đã xảy ra lỗi trong quá trình xử lý giao dịch!
                </h3>
                <% if (message != null) { %>
                <p style="font-size: 18px; margin-top: 15px;"><%= message %></p>
                <% } %>
            </div>
            <% } else { %>
            <div>
                <h3 style="font-weight: bold; color: #ffc107;">
                    Chúng tôi đã tiếp nhận đơn hàng, xin chờ quá trình xử lý!
                </h3>
            </div>
            <% } %>
        </section>
    </body>
</html>