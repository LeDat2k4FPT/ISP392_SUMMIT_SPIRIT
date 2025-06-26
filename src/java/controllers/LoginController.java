package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import dao.UserDAO;
import dto.UserDTO;

@WebServlet(name = "LoginController", urlPatterns = {"/LoginController"})
public class LoginController extends HttpServlet {

    private static final String ERROR = "login.jsp";
    private static final String AD = "Admin";
    private static final String ADMIN_PAGE = "admin.jsp";
    private static final String US = "User";
    private static final String USER_PAGE = "homepage.jsp";
    private static final String ST = "Staff";
    private static final String STAFF_PAGE = "staffDashboard.jsp";
    private static final String INCORRECT_MESSAGE = "Incorrect Email or Password!";
    private static final String UNSUPPORT_MESSAGE = "Your role is not supported yet!";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR;
        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            UserDAO dao = new UserDAO();
            UserDTO loginUser = dao.checkLogin(email, password);
            if (loginUser != null) {
                loginUser.setPassword(password);
                HttpSession session = request.getSession();
                session.setAttribute("LOGIN_USER", loginUser);

                // ✅ Gán USER_ID vào session để liên kết với giỏ hàng
                session.setAttribute("USER_ID", loginUser.getUserID());

                String role = loginUser.getRole();
                if (AD.equals(role)) {
                    url = ADMIN_PAGE;
                } else if (US.equals(role)) {
                    url = USER_PAGE;
                } else if (ST.equals(role)) {
                    url = STAFF_PAGE;
                } else {
                    request.setAttribute("MESSAGE", UNSUPPORT_MESSAGE);
                }
            } else {
                request.setAttribute("MESSAGE", INCORRECT_MESSAGE);
            }
        } catch (Exception e) {
            log("Error at LoginController: " + e.toString());
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
        return "LoginController handles user login and stores session attributes";
    }
}
