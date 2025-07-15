package controllers;

import dao.OrderDAO;
import dao.ReviewDAO;
import dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/SubmitReview")
public class SubmitReviewController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy người dùng từ session
            UserDTO user = (UserDTO) request.getSession().getAttribute("LOGIN_USER");
            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            int userId = user.getUserID();
            int productId = Integer.parseInt(request.getParameter("productId"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String comment = request.getParameter("comment");

            // ✅ Kiểm tra đã từng mua & đơn đã Delivered
            OrderDAO orderDAO = new OrderDAO();
            boolean isEligible = orderDAO.hasUserPurchasedProduct(userId, productId);

            if (!isEligible) {
                // ❌ Không đủ điều kiện đánh giá
                response.sendRedirect("productDetail.jsp?id=" + productId + "&error=unauthorized");
                return;
            }

            // ✅ Tiếp tục nếu hợp lệ
            ReviewDAO reviewDAO = new ReviewDAO();
            reviewDAO.deleteExistingReview(userId, productId); // Optional
            reviewDAO.insertReview(userId, productId, rating, comment);

            response.sendRedirect("productDetail.jsp?id=" + productId + "&review=success");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
