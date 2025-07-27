package controllers;

import dao.OrderDAO;
import dao.ReviewDAO;
import dao.UserDAO;
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
            // ✅ Lấy thông tin cơ bản từ form
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String orderDate = request.getParameter("orderDate");
            String status = request.getParameter("status");

            // ✅ Lấy người dùng từ session hoặc từ email
            UserDTO user = (UserDTO) request.getSession().getAttribute("LOGIN_USER");
            if (user == null && email != null) {
                UserDAO userDAO = new UserDAO();
                user = userDAO.findByEmail(email);
            }

            if (user == null) {
                response.sendRedirect("user/login.jsp");
                return;
            }

            int userId = user.getUserID();
            int productId = Integer.parseInt(request.getParameter("productId"));
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String comment = request.getParameter("comment");

            // ✅ Kiểm tra đã mua hàng và đơn đã Delivered
            OrderDAO orderDAO = new OrderDAO();
            boolean isEligible = orderDAO.hasUserPurchasedProduct(userId, productId);

            if (!isEligible) {
                response.sendRedirect("user/productDetail.jsp?id=" + productId + "&error=unauthorized");
                return;
            }

            // ✅ Thêm hoặc cập nhật đánh giá
            ReviewDAO reviewDAO = new ReviewDAO();
            reviewDAO.upsertReview(userId, productId, rating, comment);

            // ✅ Redirect trở lại trang productDetail.jsp sau khi submit review thành công
            response.sendRedirect("user/productDetail.jsp?id=" + productId + "&review=success");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("user/error.jsp");
        }
    }
}
