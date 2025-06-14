package controllers;

import dao.UserDAO;
import dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import user.UserError;

/**
 *
 * @author gmt
 */
@WebServlet(name = "CreateUserController", urlPatterns = {"/CreateUserController"})
public class CreateUserController extends HttpServlet {

    private static final String ERROR = "createUser.jsp";
    private static final String SUCCESS = "user.jsp";
    private static final String UNKNOW_MESSAGE = "Unknow error!";
    private static final String NOT_MATCH = "Please make sure the password and confirm password match!";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR;
        String fullName = "";
        String address = "";
        String email = "";
        String phone = "";
        String password = "";
        String confirm = "";
        UserError userError = new UserError();
        boolean checkValidation = true;
        try {
            fullName = request.getParameter("fullName");
            address = request.getParameter("address");
            email = request.getParameter("email");
            phone = request.getParameter("phone");
            password = request.getParameter("password");
            confirm = request.getParameter("confirm");
            UserDAO dao = new UserDAO();
            if (!confirm.equals(password)) {
                userError.setConfirm(NOT_MATCH);
                checkValidation = false;
            }
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
                userError.setEmail("Invalid email format!");
                checkValidation = false;
            }
            if (!phone.matches("^\\d{10,11}$")) {
                userError.setPhone("Invalid phone number format!");
                checkValidation = false;
            }
            if (checkValidation) {
                UserDTO tempUser = new UserDTO(0, fullName, address, password, phone, email, "User");
                boolean checkCreate = dao.create(tempUser);
                if (checkCreate) {
                    UserDTO user = dao.checkLogin(email, password);
                    HttpSession session = request.getSession();
                    session.setAttribute("LOGIN_USER", user);
                    url = SUCCESS;
                } else {
                    userError.setErrorMessage(UNKNOW_MESSAGE);
                }
            } else {
                request.setAttribute("USER_ERROR", userError);
                url = ERROR;
            }
        } catch (Exception e) {
            log("Error at CreateUserController: " + e.toString());
        } finally {
            request.setAttribute("fullName", fullName);
            request.setAttribute("address", address);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.setAttribute("password", password);
            request.setAttribute("confirm", confirm);
            request.getRequestDispatcher(url).forward(request, response);
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        processRequest(request, response);
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
