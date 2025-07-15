package admin;

import dao.UpdateHistoryDAO;
import dto.UpdateHistoryDTO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ManageUpdateHistoryController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            UpdateHistoryDAO dao = new UpdateHistoryDAO();
            List<UpdateHistoryDTO> logs = dao.getAllLogs();
            request.setAttribute("logs", logs);
            request.setAttribute("page", "updateHistory.jsp");
            request.getRequestDispatcher("admin/admin.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }
} 