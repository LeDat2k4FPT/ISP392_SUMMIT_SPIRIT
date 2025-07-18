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
            // 1. Get filter values
            String selectedMonth = request.getParameter("monthValue");
            String selectedYear = request.getParameter("yearValue");
            String selectedCategory = request.getParameter("category");

            // Set defaults
            if (selectedMonth == null) selectedMonth = "all";
            if (selectedYear == null) selectedYear = "all";
            if (selectedCategory == null || selectedCategory.isEmpty()) {
                selectedCategory = "all";
            }

            // Format lại tháng yyyy-MM
            if (!"all".equals(selectedMonth) && !"all".equals(selectedYear)) {
                selectedMonth = String.format("%s-%02d", selectedYear, Integer.parseInt(selectedMonth));
            }

            // 2. Load data
            RevenueDAO dao = new RevenueDAO();
            CategoryDAO cateDao = new CategoryDAO();
            List<CategoryDTO> categoryList = cateDao.getAllCategories();
            CategoryDTO allCate = new CategoryDTO();
            allCate.setCateName("all");
            categoryList.add(0, allCate);

            List<RevenueLineDTO> lineData = dao.getLineChartData("month", null, selectedMonth, selectedYear, selectedCategory, "All");
            List<ProductSoldDTO> pieData = dao.getPieChartData(selectedCategory, "month", null, selectedMonth, selectedYear, "All");

            int totalOrders = dao.countOrders(null, selectedMonth, selectedYear, selectedCategory, "All");
            int totalProducts = dao.countProductsSold(null, selectedMonth, selectedYear, selectedCategory, "All");
            int deliveredOrders = dao.countOrders(null, selectedMonth, selectedYear, selectedCategory, "Delivered");
            int cancelledOrders = dao.countOrders(null, selectedMonth, selectedYear, selectedCategory, "Cancelled");

            List<ProductSoldDTO> topProducts = dao.getTopSellingProducts(null, selectedMonth, selectedYear, selectedCategory, "All", 5);

            // 3. Set attributes
            request.setAttribute("lineData", lineData);
            request.setAttribute("pieData", pieData);
            request.setAttribute("selectedCategory", selectedCategory);
            request.setAttribute("selectedMonth", selectedMonth);
            request.setAttribute("selectedYear", selectedYear);
            request.setAttribute("categoryList", categoryList);
            request.setAttribute("totalOrders", totalOrders);
            request.setAttribute("totalProducts", totalProducts);
            request.setAttribute("deliveredOrders", deliveredOrders);
            request.setAttribute("cancelledOrders", cancelledOrders);
            request.setAttribute("topProducts", topProducts);
            request.setAttribute("page", "viewRevenue.jsp");

            request.getRequestDispatcher("admin/admin.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while loading revenue data.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}