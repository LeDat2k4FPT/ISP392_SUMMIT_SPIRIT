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

@WebServlet(name = "UpdateProfileController", urlPatterns = {"/UpdateProfile"})
public class UpdateProfileController extends HttpServlet {

    private static final String ERROR_PAGE = "error.jsp";
    private static final String PROFILE_PAGE = "profile.jsp";
    private static final String USER_PAGE = "user.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String url = ERROR_PAGE;
        try {
            HttpSession session = request.getSession(false);
            if (session == null) {
                request.setAttribute("MESSAGE", "Phiên làm việc hết hạn, vui lòng đăng nhập lại.");
                url = "login.jsp";
            } else {
                UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
                if (loginUser == null) {
                    request.setAttribute("MESSAGE", "Bạn chưa đăng nhập.");
                    url = "login.jsp";
                } else {
                    String action = request.getParameter("action");
                    UserDAO dao = new UserDAO();

                    if ("update".equals(action)) {
                        
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
                            request.setAttribute("MESSAGE", "Cập nhật thông tin thành công!");
                            url = USER_PAGE;
                        } else {
                            request.setAttribute("MESSAGE", "Cập nhật thông tin thất bại!");
                            url = PROFILE_PAGE;
                        }
                    } else if ("changePassword".equals(action)) {
                        
                        String oldPassword = request.getParameter("oldPassword");
                        String newPassword = request.getParameter("newPassword");
                        String confirmPassword = request.getParameter("confirmPassword");

                        if (!oldPassword.equals(loginUser.getPassword())) {
                            request.setAttribute("ERROR", "Mật khẩu cũ không đúng!");
                            url = PROFILE_PAGE;
                        } else if (!newPassword.equals(confirmPassword)) {
                            request.setAttribute("ERROR", "Mật khẩu mới và xác nhận không khớp!");
                            url = PROFILE_PAGE;
                        } else {
                            loginUser.setPassword(newPassword);
                            boolean check = dao.updatePassword(loginUser);
                            if (check) {
                                session.setAttribute("LOGIN_USER", loginUser);
                                request.setAttribute("MESSAGE", "Đổi mật khẩu thành công!");
                                url = USER_PAGE;
                            } else {
                                request.setAttribute("ERROR", "Đổi mật khẩu thất bại!");
                                url = PROFILE_PAGE;
                            }
                        }
                    } else {
                        
                        url = PROFILE_PAGE;
                    }
                }
            }
        } catch (Exception e) {
            log("Error at UpdateProfileController: " + e.toString());
            request.setAttribute("MESSAGE", "Lỗi hệ thống, vui lòng thử lại!");
        }
        request.getRequestDispatcher(url).forward(request, response);
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
