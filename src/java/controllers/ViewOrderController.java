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

<<<<<<< HEAD
@WebServlet(name = "ViewOrderController", urlPatterns = {"/ViewOrders"})
public class ViewOrderController extends HttpServlet {

=======
@WebServlet(name = "ViewOrderController", urlPatterns = {"/ViewOrderController"})
public class ViewOrderController extends HttpServlet {
>>>>>>> dd4dc50dad166f64a8d749f7f7c88852de362a43
    private static final String ERROR = "error.jsp";
    private static final String ORDER_PAGE = "orderHistory.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
<<<<<<< HEAD
        String url = ERROR;
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
                if (loginUser != null) {
                    OrderDAO orderDAO = new OrderDAO();
                    List<OrderDTO> orders = orderDAO.getOrdersByUser(loginUser.getUserID());
                    request.setAttribute("ORDER_LIST", orders);
                    url = ORDER_PAGE;
                } else {
                    request.setAttribute("MESSAGE", "Bạn chưa đăng nhập.");
                    url = "login.jsp";
                }
            } else {
                request.setAttribute("MESSAGE", "Phiên làm việc hết hạn, vui lòng đăng nhập lại.");
                url = "login.jsp";
            }
        } catch (Exception e) {
            log("Error at ViewOrderController: " + e.toString());
            request.setAttribute("MESSAGE", "Lỗi khi tải danh sách đơn hàng.");
        }
        request.getRequestDispatcher(url).forward(request, response);
=======
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
>>>>>>> dd4dc50dad166f64a8d749f7f7c88852de362a43
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
<<<<<<< HEAD
}
=======

    @Override
    public String getServletInfo() {
        return "Short description";
    }
} 
>>>>>>> dd4dc50dad166f64a8d749f7f7c88852de362a43
