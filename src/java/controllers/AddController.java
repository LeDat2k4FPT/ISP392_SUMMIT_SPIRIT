/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

<<<<<<< HEAD
import dao.ProductDAO;
import dto.ProductDTO;
=======
>>>>>>> dd4dc50dad166f64a8d749f7f7c88852de362a43
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
<<<<<<< HEAD

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

=======
import java.io.PrintWriter;


@WebServlet(name = "AddController", urlPatterns = {"/AddController"})
public class AddController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    //vinh
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
//   try {
//            String productID = request.getParameter("productID");
//            String productName = request.getParameter("productName");
//            double price = Double.parseDouble(request.getParameter("price"));
//            int quantity = Integer.parseInt(request.getParameter("quantity"));
//
//            HttpSession session = request.getSession();
//            Cart cart = (Cart) session.getAttribute("CART");
//            if (cart == null) {
//                cart = new Cart();
//            }
//            boolean check = cart.add(new Clothes(productID, productName, quantity, price));
//            if (check) {
//                session.setAttribute("CART", cart);
//                request.setAttribute("MESSAGE", "Đã thêm " + " : " + quantity + " thành công");
//                url = SUCCESS;
//            }
//        } catch (Exception e) {
//            log("Error at AddController: " + e.toString());
//        } finally {
//            request.getRequestDispatcher(url).forward(request, response);
//        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
>>>>>>> dd4dc50dad166f64a8d749f7f7c88852de362a43
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
<<<<<<< HEAD
=======

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

>>>>>>> dd4dc50dad166f64a8d749f7f7c88852de362a43
}
