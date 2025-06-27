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
            // Lấy productID và size từ URL
            int productID = Integer.parseInt(request.getParameter("id"));
            String size = request.getParameter("size");

            HttpSession session = request.getSession();
            CartDTO cart = (CartDTO) session.getAttribute("CART");

            if (cart != null) {
                if (size != null && !size.isEmpty()) {
                    // ✅ Xoá đúng một biến thể (theo size cụ thể)
                    cart.removeFromCart(productID, size);
                } else {
                    // 🔁 Trường hợp không truyền size → xoá toàn bộ biến thể
                    cart.removeFromCart(productID);
                }

                session.setAttribute("CART", cart);
            }

            response.sendRedirect("cart.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
