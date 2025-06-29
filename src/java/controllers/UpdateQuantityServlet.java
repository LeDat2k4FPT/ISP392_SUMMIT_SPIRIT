package controllers;

import dao.ProductDAO;
import dto.CartDTO;
import dto.CartItemDTO;
import dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/UpdateQuantityServlet")
public class UpdateQuantityServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession();

            // ✅ Check login
            UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
            if (user == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // ✅ Lấy tham số productID, size, quantity từ AJAX
            String productIDStr = request.getParameter("productID");
            String size = request.getParameter("size");
            String quantityStr = request.getParameter("quantity");

            if (productIDStr == null || size == null || quantityStr == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            int productID = Integer.parseInt(productIDStr);
            int newQuantity = Integer.parseInt(quantityStr);

            CartDTO cart = (CartDTO) session.getAttribute("CART");

            if (cart == null || cart.getCartItem(productID, size) == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // ✅ Nếu số lượng <= 0 → xóa khỏi giỏ hàng
            if (newQuantity <= 0) {
                cart.removeFromCart(productID, size);
                session.setAttribute("CART", cart);
                return;
            }

            // ✅ Kiểm tra tồn kho
            ProductDAO dao = new ProductDAO();
            int stock = dao.getStockByProductID(productID);
            if (newQuantity > stock) {
                response.sendError(HttpServletResponse.SC_CONFLICT, "Quantity exceeds stock");
                return;
            }

            // ✅ Cập nhật số lượng
            cart.updateQuantity(productID, size, newQuantity);
            session.setAttribute("CART", cart);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid number format");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
