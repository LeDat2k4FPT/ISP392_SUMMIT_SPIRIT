/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package admin;

import dao.RevenueDAO;
import dao.CategoryDAO;
import dto.ProductSoldDTO;
import dto.RevenueDTO;
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

        String monthRaw = request.getParameter("monthValue");
        String yearRaw = request.getParameter("yearValue");
        String category = request.getParameter("category");

        // Gán mặc định nếu không có giá trị
        if (monthRaw == null || monthRaw.isEmpty()) monthRaw = "all";
        if (yearRaw == null || yearRaw.isEmpty()) yearRaw = "all";
        if (category == null || category.isEmpty()) category = "all";

        int month = "all".equalsIgnoreCase(monthRaw) ? 0 : Integer.parseInt(monthRaw);
        int year = "all".equalsIgnoreCase(yearRaw) ? 0 : Integer.parseInt(yearRaw);
        String selectedCategory = "all".equalsIgnoreCase(category) ? null : category;

        try {
            RevenueDAO revenueDAO = new RevenueDAO();
            CategoryDAO categoryDAO = new CategoryDAO();

            // Lấy dữ liệu biểu đồ và bảng
            List<RevenueDTO> revenueList = revenueDAO.getRevenueByMonthYearCategory(month, year, selectedCategory);
            List<RevenueDTO> topProducts = revenueDAO.getTopSellingProductsByCategory(
                    selectedCategory != null ? selectedCategory : "Clothing");
            List<RevenueDTO> colorStats = revenueDAO.getProductColorStatsByCategory(
                    selectedCategory != null ? selectedCategory : "Clothing");

            // Lấy danh sách danh mục để hiển thị dropdown filter
            List<CategoryDTO> categoryList = categoryDAO.getAllCategories();

            // Set attribute để hiển thị dữ liệu
            request.setAttribute("revenueList", revenueList);
            request.setAttribute("topProducts", topProducts);
            request.setAttribute("colorStats", colorStats);
            request.setAttribute("categoryList", categoryList);

            // Truyền lại giá trị đã chọn cho filter
            request.setAttribute("selectedMonth", monthRaw);
            request.setAttribute("selectedYear", yearRaw);
            request.setAttribute("selectedCategory", category);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading revenue data.");
        }

        // Nhúng trang con vào admin.jsp
        request.setAttribute("page", "viewRevenue.jsp");
        request.getRequestDispatcher("/admin/admin.jsp").forward(request, response);
    }
}