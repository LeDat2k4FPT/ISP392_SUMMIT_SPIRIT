/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import dao.ProductDAO;
import dto.ProductDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchController", urlPatterns = {"/SearchProducts"})
public class SearchController extends HttpServlet {

    private static final String SUCCESS = "productList.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String search = request.getParameter("search");
            if (search == null) {
                search = "";
            }
            ProductDAO dao = new ProductDAO();
            List<ProductDTO> list = dao.searchProducts(search);
            request.setAttribute("PRODUCT_LIST", list);
            request.setAttribute("SEARCH_VALUE", search);
        } catch (Exception e) {
            log("Error at SearchController: " + e.toString());
            request.setAttribute("MESSAGE", "Lỗi khi tìm kiếm sản phẩm");
        }
        request.getRequestDispatcher(SUCCESS).forward(request, response);
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

