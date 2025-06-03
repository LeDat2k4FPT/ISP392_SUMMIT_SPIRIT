/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
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

@WebServlet(name = "AddController", urlPatterns = {"/AddProduct"})
public class AddController extends HttpServlet {

    private static final String SUCCESS = "productList.jsp"; 
    private static final String ERROR = "addProduct.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = ERROR;
        try {
            
            String productName = request.getParameter("productName");
            String productImage = request.getParameter("productImage");
            String description = request.getParameter("description");
            String size = request.getParameter("size");
            double price = Double.parseDouble(request.getParameter("price"));
            String status = request.getParameter("status");
            int stock = Integer.parseInt(request.getParameter("stock"));
            int cateID = Integer.parseInt(request.getParameter("cateID"));

            ProductDTO product = new ProductDTO(0, productName, productImage, description, size, price, status, stock, cateID);
            ProductDAO dao = new ProductDAO();

            boolean check = dao.createProduct(product);
            if (check) {
                url = SUCCESS;
                request.setAttribute("MESSAGE", "Thêm sản phẩm thành công");
            } else {
                request.setAttribute("MESSAGE", "Thêm sản phẩm thất bại");
            }
        } catch (Exception e) {
            log("Error at AddController: " + e.toString());
            request.setAttribute("MESSAGE", "Lỗi khi thêm sản phẩm");
        }
        request.getRequestDispatcher(url).forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getRequestDispatcher("addProduct.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
