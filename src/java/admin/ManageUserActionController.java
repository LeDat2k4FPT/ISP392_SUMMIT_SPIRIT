/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package admin;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 *
 * @author Hanne
 */
@WebServlet(name = "ManageUserActionController", urlPatterns = {"/ManageUserActionController"})
public class ManageUserActionController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet requestaa
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ManageUserActionController</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ManageUserActionController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
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
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userID = Integer.parseInt(request.getParameter("userID"));
        String action = request.getParameter("action");
         UserDAO dao = new UserDAO();

        try {
            switch (action) {
                case "editRole":
                    dao.toggleRole(userID);
                    break;
                case "delete":
                dao.deleteUser(userID); // Gọi phương thức mới trong DAO
                break;
                default:
                    request.setAttribute("ERROR", "Unknown action: " + action);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("ERROR", "Failed to perform action: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/admin.jsp?page=ManageUserAccountController"); // Reload the list with layer

//        UserDAO dao = new UserDAO();
//
//        if ("editRole".equals(action)) {
//            // Get current role from DAO
//            String currentRole = dao.getUserRole(userID);
//            String newRole = currentRole.equalsIgnoreCase("user") ? "staff" : "user";
//            dao.updateUserRole(userID, newRole);
//        } else if ("deactivate".equals(action)) {
//            dao.deactivateUser(userID);  // cập nhật role thành 'deactivated' hoặc status 'inactive'
//        }
//
//        // Redirect lại trang quản lý
//        response.sendRedirect("manageUser");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
 