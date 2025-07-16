<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Product Review</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="css/rating.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <div class="container">
        <div class="review-left">
            <a href="javascript:history.back()" class="back-link">← Back</a>
            <h2>PRODUCT REVIEW</h2>
            <form action="SubmitReview" method="post">
                <label>Full Name</label>
                <input type="text" name="fullName" required>

                <label>Phone Number</label>
                <input type="text" name="phone" required>

                <label>Email</label>
                <input type="email" name="email" required>

                <label>Rating</label>
                <div class="rating-stars">
                    <% for (int i = 1; i <= 5; i++) { %>
                        <input type="radio" id="star<%=i%>" name="rating" value="<%=i%>">
                        <label for="star<%=i%>"><i class="fa fa-star"></i></label>
                    <% } %>
                </div>

                <label>Review Summary</label>
                <textarea name="comment" rows="4" required></textarea>

                <button type="submit">SEND</button>
            </form>
        </div>

        <div class="review-right">
            <img src="image/your-product.jpg" alt="Product Image">
            <h3>Naturehike NH16Y065-Q 65L <br> Mountaineering Backpack</h3>
        </div>
    </div>
</body>
</html>
