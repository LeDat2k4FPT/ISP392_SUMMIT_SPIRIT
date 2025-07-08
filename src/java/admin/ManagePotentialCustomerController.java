package admin;

import dao.UserDAO;
import dao.OrderDAO;
import dao.ReviewDAO;
import dto.UserDTO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ManagePotentialCustomerController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            UserDAO userDAO = new UserDAO();
            OrderDAO orderDAO = new OrderDAO();
            ReviewDAO reviewDAO = new ReviewDAO();
            List<UserDTO> users = userDAO.getAllUsers();
            List<Map<String, Object>> potentialList = new ArrayList<>();
            for (UserDTO user : users) {
                if (user.getRole().equalsIgnoreCase("User")) {
                    int userID = user.getUserID();
                    double totalSpent = orderDAO.getTotalSpentByUser(userID);
                    int orderCount = orderDAO.getOrderCountByUser(userID);
                    int reviewCount = reviewDAO.getReviewCountByUser(userID);
                    Map<String, Object> info = new HashMap<>();
                    info.put("user", user);
                    info.put("totalSpent", totalSpent);
                    info.put("orderCount", orderCount);
                    info.put("reviewCount", reviewCount);
                    potentialList.add(info);
                }
            }
            // Sắp xếp theo tổng chi tiêu giảm dần
            potentialList.sort((a, b) -> Double.compare((double) b.get("totalSpent"), (double) a.get("totalSpent")));
            request.setAttribute("potentialList", potentialList);
            request.getRequestDispatcher("admin/potentialCustomer.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }
} 