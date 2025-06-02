package controllers;

import dao.OrderDAO;
import dto.OrderDTO;
import dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ViewOrderController", urlPatterns = {"/ViewOrderController"})
public class ViewOrderController extends HttpServlet {
    private static final String ERROR = "error.jsp";
    private static final String ORDER_PAGE = "orderHistory.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR;
        try {
            HttpSession session = request.getSession();
            UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
            if (loginUser != null) {
                OrderDAO orderDAO = new OrderDAO();
                List<OrderDTO> orders = orderDAO.getOrdersByUser(loginUser.getUserID());
                request.setAttribute("ORDER_LIST", orders);
                url = ORDER_PAGE;
            }
        } catch (Exception e) {
            log("Error at ViewOrderController: " + e.toString());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
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

    @Override
    public String getServletInfo() {
        return "Short description";
    }
} 