/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package ship;

import dao.OrderDAO;
import dao.ShippingDAO;
import dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Hanne
 */
@WebServlet(name = "AcceptShippingController", urlPatterns = {"/AcceptShippingController"})
public class AcceptShippingController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int orderID = Integer.parseInt(request.getParameter("orderID"));
            UserDTO user = (UserDTO) request.getSession().getAttribute("LOGIN_USER");

            OrderDAO orderDAO = new OrderDAO();
            ShippingDAO shippingDAO = new ShippingDAO();

            // Cập nhật trạng thái đơn
            orderDAO.updateOrderStatus(orderID, "Shipped");
            shippingDAO.assignOrderToShipper(orderID, user.getUserID());

            response.sendRedirect("ShippingListController");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Error accepting order");
        }
    }
    
}
