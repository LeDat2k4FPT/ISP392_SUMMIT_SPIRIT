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

@WebServlet(name = "UpdateProfileController", urlPatterns = {"/UpdateProfileController"})
public class UpdateProfileController extends HttpServlet {
    private static final String ERROR = "error.jsp";
    private static final String SUCCESS = "user.jsp";
    private static final String PROFILE_PAGE = "profile.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR;
        try {
            String action = request.getParameter("action");
            HttpSession session = request.getSession();
            UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
            UserDAO dao = new UserDAO();

            if ("update".equals(action)) {
                // Cập nhật thông tin cá nhân
                String fullName = request.getParameter("fullName");
                String address = request.getParameter("address");
                String phone = request.getParameter("phone");
                String email = request.getParameter("email");

                loginUser.setFullName(fullName);
                loginUser.setAddress(address);
                loginUser.setPhone(phone);
                loginUser.setEmail(email);

                boolean check = dao.updateUser(loginUser);
                if (check) {
                    session.setAttribute("LOGIN_USER", loginUser);
                    request.setAttribute("MESSAGE", "Update successful!");
                    url = SUCCESS;
                }
            } else if ("changePassword".equals(action)) {
                // Đổi mật khẩu
                String oldPassword = request.getParameter("oldPassword");
                String newPassword = request.getParameter("newPassword");
                String confirmPassword = request.getParameter("confirmPassword");

                if (!oldPassword.equals(loginUser.getPassword())) {
                    request.setAttribute("ERROR", "Old password is incorrect!");
                    url = PROFILE_PAGE;
                } else if (!newPassword.equals(confirmPassword)) {
                    request.setAttribute("ERROR", "New password and confirm password do not match!");
                    url = PROFILE_PAGE;
                } else {
                    loginUser.setPassword(newPassword);
                    boolean check = dao.updatePassword(loginUser);
                    if (check) {
                        session.setAttribute("LOGIN_USER", loginUser);
                        request.setAttribute("MESSAGE", "Password changed successfully!");
                        url = SUCCESS;
                    }
                }
            }
        } catch (Exception e) {
            log("Error at UpdateProfileController: " + e.toString());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
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

    @Override
    public String getServletInfo() {
        return "Short description";
    }
} 