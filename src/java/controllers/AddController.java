package controllers;

import dao.ProductDAO;
import dto.ProductDTO;
import dto.CartItemDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "AddController", urlPatterns = {"/AddController"})
public class AddController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Map<Integer, CartItemDTO> cart = (Map<Integer, CartItemDTO>) session.getAttribute("CART");

        if (cart == null) {
            cart = new HashMap<>();
        }

        String productIDStr = request.getParameter("productID");
        String quantityStr = request.getParameter("quantity");

        if (productIDStr != null && quantityStr != null) {
            try {
                int productID = Integer.parseInt(productIDStr);
                int quantity = Integer.parseInt(quantityStr);

                ProductDAO productDAO = new ProductDAO();
                ProductDTO product = productDAO.getProductByID(productID);

                if (product != null) {
                    CartItemDTO cartItem = cart.get(productID);
                    if (cartItem == null) {
                        cart.put(productID, new CartItemDTO(product, quantity));
                    } else {
                        cartItem.setQuantity(cartItem.getQuantity() + quantity);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        session.setAttribute("CART", cart);
        response.sendRedirect("cart.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
