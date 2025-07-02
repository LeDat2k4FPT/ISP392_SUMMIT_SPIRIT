package controllers;

import dao.CartDAO;
import dto.CartDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "ApplyDiscountServlet", urlPatterns = {"/ApplyDiscountServlet"})
public class ApplyDiscountServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String discountCode = request.getParameter("discountCode");
        HttpSession session = request.getSession();

        // ✅ Bước 1: Kiểm tra người dùng đã đăng nhập chưa
        Object loginUser = session.getAttribute("LOGIN_USER");
        if (loginUser == null) {
            session.setAttribute("DISCOUNT_ERROR", "Bạn cần đăng nhập để sử dụng mã giảm giá.");
            response.sendRedirect("login.jsp");
            return;
        }

        // ✅ Bước 2: Kiểm tra giỏ hàng
        CartDTO cart = (CartDTO) session.getAttribute("CART");
        if (cart == null || cart.isEmpty()) {
            session.setAttribute("DISCOUNT_ERROR", "Không thể áp dụng mã khi giỏ hàng trống.");
            response.sendRedirect("cart.jsp");
            return;
        }

        double totalCartAmount = cart.getTotalPrice();

        // ✅ Bước 3: Kiểm tra mã giảm giá
        CartDAO dao = new CartDAO();
        Optional<Double> discountOpt = dao.validateDiscountCode(discountCode, totalCartAmount);

        if (discountOpt.isPresent()) {
            double discountValue = discountOpt.get();
            double maxAllowedDiscount = Math.min(discountValue, 100);
            double discountAmount = totalCartAmount * maxAllowedDiscount / 100;

            if (discountAmount >= totalCartAmount) {
                session.setAttribute("DISCOUNT_ERROR", "Chiết khấu vượt quá tổng tiền giỏ hàng.");
                session.removeAttribute("DISCOUNT_PERCENT");
                session.removeAttribute("DISCOUNT_CODE");
            } else {
                session.setAttribute("DISCOUNT_PERCENT", maxAllowedDiscount);
                session.setAttribute("DISCOUNT_CODE", discountCode);
                session.removeAttribute("DISCOUNT_ERROR");
            }

        } else {
            session.removeAttribute("DISCOUNT_PERCENT");
            session.removeAttribute("DISCOUNT_CODE");
            session.setAttribute("DISCOUNT_ERROR", "Mã giảm giá không hợp lệ, hết hạn hoặc không đủ điều kiện.");
        }

        // ✅ Ghi nhớ lại thông tin form đã nhập
        session.setAttribute("SHIPPING_COUNTRY", request.getParameter("country"));
        session.setAttribute("SHIPPING_FULLNAME", request.getParameter("fullname"));
        session.setAttribute("SHIPPING_PHONE", request.getParameter("phone"));
        session.setAttribute("SHIPPING_EMAIL", request.getParameter("email"));
        session.setAttribute("SHIPPING_ADDRESS", request.getParameter("address"));
        session.setAttribute("SHIPPING_DISTRICT", request.getParameter("district"));
        session.setAttribute("SHIPPING_CITY", request.getParameter("city"));

        // ✅ Quay lại trang shipping.jsp
        response.sendRedirect("shipping.jsp");
    }
}
