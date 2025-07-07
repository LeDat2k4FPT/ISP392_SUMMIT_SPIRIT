package controllers;

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
            // Lấy thông tin từ session
            UserDTO user = (UserDTO) request.getSession().getAttribute("LOGIN_USER");
            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            int userId = user.getUserID();
            int productId = Integer.parseInt(request.getParameter("productId"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String comment = request.getParameter("comment");

            ReviewDAO reviewDAO = new ReviewDAO();

            // Kiểm tra nếu user đã đánh giá sản phẩm này trước đó → xóa để chỉ cho 1 lần đánh giá (nếu cần)
            reviewDAO.deleteExistingReview(userId, productId);

            // Lưu review mới
            reviewDAO.insertReview(userId, productId, rating, comment);

            // Quay lại trang sản phẩm
            response.sendRedirect("productDetail.jsp?id=" + productId);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
