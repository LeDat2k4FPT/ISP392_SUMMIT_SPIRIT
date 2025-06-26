/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import dao.RevenueDAO;
import dto.ProductSoldDTO;
import dto.RevenueLineDTO;
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

            // Set default values if not provided
            if (selectedDay == null) selectedDay = "all";
            if (selectedMonth == null) selectedMonth = "all";
            if (selectedYear == null) selectedYear = "all";
            if (selectedCategory == null || selectedCategory.isEmpty()) {
                selectedCategory = "Clothing"; // Default category
            }

            // 3. Retrieve data using DAO
            RevenueDAO dao = new RevenueDAO();

            // Line chart: sales quantity by time
            List<RevenueLineDTO> lineData = dao.getLineChartData(filterType, selectedDay, selectedMonth, selectedYear);

            // Pie chart: sales quantity by product & color for a category
            List<ProductSoldDTO> pieData = dao.getPieChartData(selectedCategory);

            // 4. Set data attributes for JSP
            request.setAttribute("lineData", lineData);
            request.setAttribute("pieData", pieData);
            request.setAttribute("filterType", filterType);
            request.setAttribute("selectedCategory", selectedCategory);
            request.setAttribute("selectedDay", selectedDay);
            request.setAttribute("selectedMonth", selectedMonth);
            request.setAttribute("selectedYear", selectedYear);

            // 5. Forward to JSP page
            request.getRequestDispatcher("viewRevenue.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while loading revenue data.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}