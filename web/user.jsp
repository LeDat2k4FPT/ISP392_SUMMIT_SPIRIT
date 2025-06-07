<%@ page import="dto.UserDTO" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User Page</title>
    </head>
    <body>
        <%
            UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
            if (loginUser == null || !"User".equals(loginUser.getRole())) {
                response.sendRedirect("login.jsp");
                return;
            }
        %>

        <h2>User Information:</h2>
        User ID: <%= loginUser.getUserID() %><br/>
        Full Name: <%= loginUser.getFullName() %><br/>
        Role: <%= loginUser.getRole() %><br/>
        Password: <%= loginUser.getPassword() %><br/>

<!--        <br/><a href="shopping.jsp">Shopping</a>-->
    </body>
</html>
