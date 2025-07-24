<%-- 
    Document   : adminProfile
    Created on : Jul 25, 2025
    Author     : Admin
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="dto.UserDTO" %>

<%
    UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
    if (loginUser == null || !"Admin".equalsIgnoreCase(loginUser.getRole())) {
        response.sendRedirect("../login.jsp");
        return;
    }
%>

<div class="container">
    <h2 class="mb-4"><i class="bi bi-person-circle"></i> Admin Profile</h2>
    <table class="table table-bordered" style="max-width: 600px;">
        <tr>
            <th>Full Name</th>
            <td><%= loginUser.getFullName() %></td>
        </tr>
        <tr>
            <th>Email</th>
            <td><%= loginUser.getEmail() %></td>
        </tr>
        <tr>
            <th>Phone</th>
            <td><%= loginUser.getPhone() %></td>
        </tr>
        <tr>
            <th>Role</th>
            <td><%= loginUser.getRole() %></td>
        </tr>
    </table>

    <!-- Action buttons -->
    <div class="mt-3 d-flex gap-2">
        <a href="#" class="btn btn-outline-primary">
            <i class="bi bi-pencil-square"></i> Edit Profile
        </a>
        <a href="${pageContext.request.contextPath}/resetPassword.jsp" class="btn btn-outline-danger">
            <i class="bi bi-shield-lock"></i> Change Password
        </a>
    </div>
</div>
