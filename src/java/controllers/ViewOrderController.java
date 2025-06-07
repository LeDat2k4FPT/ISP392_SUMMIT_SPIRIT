package controllers;

import dao.OrderDAO;
import dto.OrderDTO;
import dto.UserDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ViewOrderController", urlPatterns = {"/ViewOrderController"})
public class ViewOrderController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Lấy session người dùng
        HttpSession session = request.getSession();
        UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");

        // Kiểm tra nếu người dùng chưa đăng nhập
        if (loginUser == null) {
            // Nếu không có người dùng đăng nhập, chuyển hướng tới trang đăng nhập
            response.sendRedirect("login.jsp");
            return;
        }

        int userID = loginUser.getUserID();
        OrderDAO orderDAO = new OrderDAO();

        try {
            // Lấy danh sách đơn hàng của người dùng
            List<OrderDTO> orderList = orderDAO.getOrdersByUser(userID);
            
            // Nếu danh sách đơn hàng không rỗng
            if (orderList != null && !orderList.isEmpty()) {
                request.setAttribute("ORDER_LIST", orderList);
            } else {
                request.setAttribute("MESSAGE", "Bạn chưa có đơn hàng nào.");
            }
        } catch (Exception e) {
            // Log lỗi chi tiết
            e.printStackTrace();
            request.setAttribute("ERROR", "Đã xảy ra lỗi khi lấy thông tin đơn hàng. Vui lòng thử lại sau.");
        }

        // Forward đến trang lịch sử đơn hàng
        request.getRequestDispatcher("orderHistory.jsp").forward(request, response);
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
