package staff.management.order;

import dao.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "UpdateOrderStatus", urlPatterns = {"/UpdateOrderStatus"})
public class UpdateOrderStatus extends HttpServlet {
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        HttpSession session = request.getSession(false);
//        if (session == null || session.getAttribute("LOGIN_USER") == null) {
//            response.sendRedirect("login.jsp");
//            return;
//        }
//
//        try {
//            int orderID = Integer.parseInt(request.getParameter("orderID"));
//            String status = request.getParameter("status");
//
//            System.out.println("[SERVLET] orderID=" + orderID + ", status=" + status);
//
//            OrderDAO dao = new OrderDAO();
//            boolean updated = dao.updateOrderStatus(orderID, status);
//
//            if (updated) {
//                System.out.println("[SERVLET] Update successful.");
//            } else {
//                System.out.println("[SERVLET] Update failed.");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        response.sendRedirect("staff/orderlist.jsp"); // quay lại list sau khi update
//    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("LOGIN_USER") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    try {
        int orderID = Integer.parseInt(request.getParameter("orderID"));
        String status = request.getParameter("status");

        System.out.println("[SERVLET] orderID=" + orderID + ", status=" + status);

        OrderDAO dao = new OrderDAO();
        boolean updated = dao.updateOrderStatus(orderID, status);

        if (updated) {
            System.out.println("[SERVLET] Update successful.");
        } else {
            System.out.println("[SERVLET] Update failed.");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    // ✅ Quay lại trang chính để load lại
//request.getRequestDispatcher("staff/orderlist.jsp").forward(request, response);
//response.sendRedirect(request.getContextPath() + "/staffDashboard.jsp");
response.sendRedirect(request.getContextPath() + "/staffDashboard.jsp?staff/orderlist.jsp");
    
    }

}


