 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package admin;

import dao.RevenueDAO;
import dao.CategoryDAO;
import dto.ProductSoldDTO;
import dto.RevenueLineDTO;
import dto.CategoryDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


/**
 *
 * @author Hanne
 */

/**
 * Controller to handle displaying revenue visualizations.
 */
@WebServlet(name = "ViewRevenueController", urlPatterns = {"/ViewRevenueController"})
public class ViewRevenueController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 1. Get filter type: day / month / year
            String filterType = request.getParameter("filter");
            if (filterType == null || filterType.isEmpty()) {
                filterType = "month"; // Default to month
            }

            // 2. Get filter values from request
            String selectedDay = request.getParameter("dateValue");
            String selectedMonth = request.getParameter("monthValue");
            String selectedYear = request.getParameter("yearValue");
            String selectedCategory = request.getParameter("category");
            String selectedStatus = request.getParameter("orderStatus");
            if (selectedStatus == null || selectedStatus.isEmpty()) selectedStatus = "All";

            // Set default values if not provided
            if (selectedDay == null) selectedDay = "all";
            if (selectedMonth == null) selectedMonth = "all";
            if (selectedYear == null) selectedYear = "all";
            if (selectedCategory == null || selectedCategory.isEmpty()) {
                selectedCategory = "all";
            }

            // Định dạng lại ngày/tháng/năm cho truy vấn SQL
            if ("day".equals(filterType) && !"all".equals(selectedDay) && !"all".equals(selectedMonth) && !"all".equals(selectedYear)) {
                // Định dạng yyyy-MM-dd
                selectedDay = String.format("%s-%02d-%02d", selectedYear, Integer.parseInt(selectedMonth), Integer.parseInt(selectedDay));
            } else if ("month".equals(filterType) && !"all".equals(selectedMonth) && !"all".equals(selectedYear)) {
                // Định dạng yyyy-MM
                selectedMonth = String.format("%s-%02d", selectedYear, Integer.parseInt(selectedMonth));
            }

            // 3. Retrieve data using DAO
            RevenueDAO dao = new RevenueDAO();
            CategoryDAO cateDao = new CategoryDAO();
            List<CategoryDTO> categoryList = cateDao.getAllCategories();
            CategoryDTO allCate = new CategoryDTO();
            allCate.setCateName("all");
            categoryList.add(0, allCate);

            // Line chart: sales quantity by time
            List<RevenueLineDTO> lineData = dao.getLineChartData(filterType, selectedDay, selectedMonth, selectedYear, selectedCategory, selectedStatus);

            // Pie chart: sales quantity by product & color for a category
            List<ProductSoldDTO> pieData = dao.getPieChartData(selectedCategory, filterType, selectedDay, selectedMonth, selectedYear, selectedStatus);

            // Tổng số đơn hàng, tổng sản phẩm bán ra, số đơn đã giao, số đơn bị hủy
            int totalOrders = dao.countOrders(selectedDay, selectedMonth, selectedYear, selectedCategory, selectedStatus);
            int totalProducts = dao.countProductsSold(selectedDay, selectedMonth, selectedYear, selectedCategory, selectedStatus);
            int deliveredOrders = dao.countOrders(selectedDay, selectedMonth, selectedYear, selectedCategory, "Delivered");
            int cancelledOrders = dao.countOrders(selectedDay, selectedMonth, selectedYear, selectedCategory, "Cancelled");

            // Top products
            List<ProductSoldDTO> topProducts = dao.getTopSellingProducts(selectedDay, selectedMonth, selectedYear, selectedCategory, selectedStatus, 5);

            // 4. Set data attributes for JSP
            request.setAttribute("lineData", lineData);
            request.setAttribute("pieData", pieData);
            request.setAttribute("filterType", filterType);
            request.setAttribute("selectedCategory", selectedCategory);
            request.setAttribute("selectedDay", selectedDay);
            request.setAttribute("selectedMonth", selectedMonth);
            request.setAttribute("selectedYear", selectedYear);
            request.setAttribute("categoryList", categoryList);
            request.setAttribute("selectedStatus", selectedStatus);
            request.setAttribute("totalOrders", totalOrders);
            request.setAttribute("totalProducts", totalProducts);
            request.setAttribute("deliveredOrders", deliveredOrders);
            request.setAttribute("cancelledOrders", cancelledOrders);
            request.setAttribute("topProducts", topProducts);

            // 5. Forward to JSP page
            request.getRequestDispatcher("viewRevenue.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while loading revenue data.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}