/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import dao.ProductDAO;
import dto.ProductDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "FilterProductServlet", urlPatterns = {"/FilterProductServlet"})
public class FilterProductServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String cateIDStr = request.getParameter("cateID");
        String minPriceStr = request.getParameter("minPrice");
        String maxPriceStr = request.getParameter("maxPrice");

        int cateID = 0;
        double minPrice = 0.0;
        double maxPrice = Double.MAX_VALUE;

        try {
            if (cateIDStr != null && !cateIDStr.trim().isEmpty()) {
                cateID = Integer.parseInt(cateIDStr.trim());
                if (cateID < 0) cateID = 0;
            }
            if (minPriceStr != null && !minPriceStr.trim().isEmpty()) {
                minPrice = Double.parseDouble(minPriceStr.trim());
                if (minPrice < 0) minPrice = 0.0;
            }
            if (maxPriceStr != null && !maxPriceStr.trim().isEmpty()) {
                maxPrice = Double.parseDouble(maxPriceStr.trim());
                if (maxPrice < 0) maxPrice = Double.MAX_VALUE;
            }

            if (minPrice > maxPrice) {
                double tmp = minPrice;
                minPrice = maxPrice;
                maxPrice = tmp;
            }
        } catch (NumberFormatException e) {
            log("FilterProductServlet: Invalid number format for filter params", e);
            request.setAttribute("ERROR_MESSAGE", "Invalid filter input format.");
            request.setAttribute("PRODUCT_LIST", Collections.emptyList());
            request.getRequestDispatcher("productList.jsp").forward(request, response);
            return;
        }

        ProductDAO productDAO = new ProductDAO();
        List<ProductDTO> products;

        try {
            if (cateID > 0) {
                products = productDAO.getProductsByCategory(cateID);
            } else {
                products = productDAO.getAllProducts();
            }
            if (products == null) products = Collections.emptyList();

            // Khai báo biến final để tránh lỗi lambda với biến không final
            final double min = minPrice;
            final double max = maxPrice;

            List<ProductDTO> filtered = products.stream()
                .filter(p -> p != null && p.getPrice() >= min && p.getPrice() <= max)
                .collect(Collectors.toList());

            request.setAttribute("PRODUCT_LIST", filtered);

        } catch (Exception e) {
            log("FilterProductServlet: Error loading or filtering products", e);
            request.setAttribute("ERROR_MESSAGE", "Error processing request. Please try again later.");
            request.setAttribute("PRODUCT_LIST", Collections.emptyList());
        }

        request.getRequestDispatcher("productList.jsp").forward(request, response);
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


