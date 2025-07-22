/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package staff.management.product;

import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;

/**
 *
 * @author Admin
 */
@WebServlet(name = "DeleteProductController", urlPatterns = {"/DeleteProductController"})
public class DeleteProductController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int productID = Integer.parseInt(request.getParameter("productID"));
            ProductDAO dao = new ProductDAO();

            boolean deleted = dao.deleteProductByID(productID);

            String msg, type;
            if (deleted) {
                msg = "Delete product successfully !";
                type = "success";
            } else {
                msg = "Failed to delete product.";
                type = "danger";
            }

            // Redirect về ProductListController kèm thông báo
            response.sendRedirect(request.getContextPath() + "/ProductListController?msg="
                    + URLEncoder.encode(msg, "UTF-8") + "&type=" + type);

        } catch (Exception e) {
            log("Error at DeleteProductController: " + e.getMessage());

            // Lỗi nghiêm trọng → redirect kèm lỗi
            String msg = "Lỗi: " + e.getMessage();
            response.sendRedirect(request.getContextPath() + "/ProductListController?msg="
                    + URLEncoder.encode(msg, "UTF-8") + "&type=danger");
        }
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
