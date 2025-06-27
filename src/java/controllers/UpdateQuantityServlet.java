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
                response.sendRedirect("login.jsp");
                return;
            }

            int productID = Integer.parseInt(request.getParameter("productID"));
            int newQuantity = Integer.parseInt(request.getParameter("quantity"));

            CartDTO cart = (CartDTO) session.getAttribute("CART");

            if (cart == null || cart.getCartItem(productID) == null) {
                response.sendRedirect("cart.jsp?error=not_found");
                return;
            }

            // ❌ Remove if quantity <= 0
            if (newQuantity <= 0) {
                cart.removeFromCart(productID);
                session.setAttribute("CART", cart);
                response.sendRedirect("cart.jsp?removed=true");
                return;
            }

            // ✅ Check stock
            ProductDAO dao = new ProductDAO();
            int stock = dao.getStockByProductID(productID);
            if (newQuantity > stock) {
                // Pass productID as parameter for error display
                response.sendRedirect("cart.jsp?error=overstock&pid=" + productID);
                return;
            }

            // ✅ Update quantity
            CartItemDTO item = cart.getCartItem(productID);
            item.setQuantity(newQuantity);
            session.setAttribute("CART", cart);
            response.sendRedirect("cart.jsp?updated=true");

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("cart.jsp?error=invalid_input");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
