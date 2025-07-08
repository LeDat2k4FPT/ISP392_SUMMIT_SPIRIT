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
import java.util.Random;
import utils.EmailUtils;

/**
 *
 * @author gmt
 */
@WebServlet(name = "ForgotPasswordController", urlPatterns = {"/ForgotPasswordController"})
public class ForgotPasswordController extends HttpServlet {

    private static final String ERROR = "forgotPassword.jsp";
    private static final String SUCCESS = "verifyOtp.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR;
        boolean checkValidation = true;
        try {
            String email = request.getParameter("email");
            String otp = String.valueOf(new Random().nextInt(900000) + 100000);
            if (checkValidation) {
                UserDAO dao = new UserDAO();
                HttpSession session = request.getSession();
                UserDTO user = dao.findByEmail(email);
                if (user != null) {
                    session.setAttribute("OTP", otp);
                    session.setAttribute("email", email);
                    //Gửi OTP qua email
                    String htmlBody = "<p>Hello <strong>" + user.getFullName() + "</strong>,</p>"
                            + "<p>We have received a request to reset the password for your account on <strong>SUMMIT SPIRIT</strong>.</p>"
                            + "<p><strong>Your OTP code: <span style='font-size: 20px; color: #007bff;'>" + otp + "</span></strong></p>"
                            + "<p>If you did not request a password reset, please ignore this email.</p>"
                            + "<p>Best regards,<br><em>SUMMIT SPIRIT</em></p>";
                    EmailUtils.sendEmail(email, "Password Reset Request", htmlBody);
                    url = SUCCESS;
                } else {
                    request.setAttribute("MESSAGE", "Email doesn't exist!");
                    url = ERROR;
                }
            }
        } catch (Exception e) {
            log("Error at ForgotPasswordController: " + e.toString());
        } finally {
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
