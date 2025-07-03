package controllers;

import dao.ProductDAO;
import dto.CartDTO;
import dto.ProductDTO;
import dto.UserDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/AddToCartServlet")
public class AddToCartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int productID = Integer.parseInt(request.getParameter("productID"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            double price = Double.parseDouble(request.getParameter("price")); // ✅ Dùng giá từ form
            String size = request.getParameter("size");
            String color = request.getParameter("color");
            boolean fromSaleOff = "true".equals(request.getParameter("fromSaleOff")); // ✅ Optional

            if (quantity <= 0) {
                response.sendRedirect("productDetail.jsp?id=" + productID + "&error=invalid_quantity");
                return;
            }

            ProductDAO dao = new ProductDAO();
            ProductDTO product = dao.getFullProductByID(productID);

            if (product == null) {
                response.sendRedirect("error.jsp");
                return;
            }

            product.setSize(size);
            product.setColor(color);
            product.setPrice(price); // ✅ Gán giá đã được xử lý từ form
            product.setFromSaleOff(fromSaleOff); // ✅ Gán cờ SaleOff vào sản phẩm

            int stock = dao.getStockByProductID(productID);

            HttpSession session = request.getSession();
            UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");

            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            int userID = user.getUserID();

            CartDTO cart = (CartDTO) session.getAttribute("CART");
            if (cart == null || cart.getUserID() != userID) {
                cart = new CartDTO(userID);
            }

            int existingQuantity = 0;
            if (cart.getCartItem(productID, size, color) != null) {
                existingQuantity = cart.getCartItem(productID, size, color).getQuantity();
            }

            if (quantity + existingQuantity > stock) {
                int available = stock - existingQuantity;
                response.sendRedirect("productDetail.jsp?id=" + productID + "&error=stock&available=" + available);
                return;
            }

            cart.addToCart(product, quantity);
            session.setAttribute("CART", cart);

            response.sendRedirect("productDetail.jsp?id=" + productID + (fromSaleOff ? "&fromSaleOff=true" : ""));

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
