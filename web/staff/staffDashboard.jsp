<%-- 
    Document   : staffDashboard
    Created on : Jun 18, 2025, 10:40:50 AM
    Author     : Admin
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dto.UserDTO" %>
<%
    UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
    if (user == null || !"Staff".equals(user.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Staff Dashboard</title>
        <style>
            body {
                font-family: Arial;
                margin: 50px;
                background-color: #f9f9f9;
            }
            h2 {
                text-align: center;
            }
            .menu {
                display: flex;
                justify-content: center;
                gap: 40px;
                margin-top: 50px;
            }
            .menu a {
                text-decoration: none;
                background-color: #007bff;
                color: white;
                padding: 20px 30px;
                border-radius: 10px;
                font-size: 18px;
                box-shadow: 0 4px 8px rgba(0,0,0,0.1);
                transition: background-color 0.2s;
            }
            .menu a:hover {
                background-color: #0056b3;
            }
            .top-bar {
                text-align: right;
                margin-bottom: 30px;
            }
        </style>
    </head>
    <body>

        <div class="top-bar">
            Welcome, <%= user.getFullName() %> |
            <form action="<%= request.getContextPath() %>/LogoutController" method="post" style="display:inline;">
                <button type="submit">Logout</button>
            </form>
        </div>

        <h2>Staff Menu</h2>

        <div class="menu">
            <a href="${pageContext.request.contextPath}/staff/mnproduct.jsp">➕ Add Product</a>
            <a href="${pageContext.request.contextPath}/staff/productlist.jsp">📦 View Product List</a>

        </div>

    </body>
</html>
