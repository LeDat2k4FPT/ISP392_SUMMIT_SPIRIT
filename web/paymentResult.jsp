<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>VNPay Payment Result</title>
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
                String retryLink = (String) request.getAttribute("retryLink");
            %>

            <% if ("success".equals(paymentResult)) { %>
            <div>
                <h3 style="font-weight: bold; color: #28a745;">
                    Your transaction has been successful!
                    <i class="fas fa-check-circle"></i>
                </h3>
                <%
                    if (transactionId != null) {
                %>
                <p>Transaction code: <b><%= transactionId %></b></p>
                <%
                    }
                    if (amount != null) {
                %>
                <p>Amount: <b><%= amount %></b></p>
                <%
                    }
                    if (orderInfo != null) {
                %>
                <p>Order information: <b><%= orderInfo %></b></p>
                <%
                    }
                %>
                <div class="text-center mt-3">
                    <a href="UserOrderHistoryController" class="back-link">View Order History</a>
                </div>
            </div>
            <% } else if ("failed".equals(paymentResult)) { %>
            <div>
                <h3 style="font-weight: bold; color: #dc3545;">
                    Transaction order failed!
                </h3>
                <a href="<%= retryLink != null ? retryLink : "checkout.jsp" %>" class="btn-retry">🔁 Back to payment</a>
            </div>
            <% } else if ("invalid".equals(paymentResult)) { %>
            <div>
                <h3 style="font-weight: bold; color: #ffc107;">
                    Invalid signature! Data may have been modified.
                </h3>
                <a href="<%= retryLink != null ? retryLink : "checkout.jsp" %>" class="btn-retry">🔁 Back to payment</a>
            </div>
            <% } else if ("error".equals(paymentResult)) { %>
            <div>
                <h3 style="font-weight: bold; color: #dc3545;">
                    An error occurred while processing the transaction!
                </h3>
                <a href="<%= retryLink != null ? retryLink : "checkout.jsp" %>" class="btn-retry">🔁 Back to payment</a>
            </div>
            <% } else { %>
            <div>
                <h3 style="font-weight: bold; color: #ffc107;">
                    We have received your order, please wait for processing!
                </h3>
            </div>
            <% } %>
        </section>
    </body>
</html>