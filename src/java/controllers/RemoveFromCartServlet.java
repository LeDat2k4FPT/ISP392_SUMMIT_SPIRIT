package controllers;

import dto.CartDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/RemoveFromCartServlet")
public class RemoveFromCartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int productID = Integer.parseInt(request.getParameter("productID"));
            String size = request.getParameter("size");
            String color = request.getParameter("color");

            // Làm sạch dữ liệu
            if (size != null) {
                size = size.trim();
            }
            if (color != null) {
                color = color.trim();
            }

            HttpSession session = request.getSession();
            CartDTO cart = (CartDTO) session.getAttribute("CART");

            if (cart != null) {
                cart.removeFromCart(productID, size, color); // luôn dùng key chuẩn
                session.setAttribute("CART", cart);
            }

            response.sendRedirect("user/cart.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("user/error.jsp");
        }
    }
}
