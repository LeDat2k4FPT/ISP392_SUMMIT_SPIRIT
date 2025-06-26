/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import dao.UserDAO;
import dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Hanne
 */
@WebServlet(name = "ManageUserAccountController", urlPatterns = {"/ManageUserAccountController"})
public class ManageUserAccountController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("keyword");

        UserDAO userDAO = new UserDAO();
        List<UserDTO> userList;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            userList = userDAO.searchUsers(keyword);
        } else {
            userList = userDAO.getAllUsers();
        }

        request.setAttribute("users", userList);
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("admin/adminManageUser.jsp").forward(request, response);
    }
}