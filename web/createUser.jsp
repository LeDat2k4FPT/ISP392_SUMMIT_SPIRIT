<%-- 
    Document   : createUser
    Created on : May 28, 2025, 5:04:58 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create User - SUMMIT SPIRIT</title>
    </head>
    <body>
        <h2>Create New User Account</h2>
        <form action="MainController" method="POST">
            User ID: <input type="text" name="userID" required=""/>
            </br>Password: <input type="password" name="password" required=""/>
            </br>Confirm Password: <input type="password" name="confirm" required=""/>
            </br>Full Name: <input type="text" name="fullName" required=""/>
            </br>Role ID: <input type="text" name="roleID" value="US" readonly=""/>
            </br><input type="submit" name="action" value="Create"/>
            <input type="reset" value="Reset"/>
        </form>
        </br><a href="login.jsp">Back to Login</a>
        <% 
            String message = (String)request.getAttribute("MESSAGE");
            if(message == null) message = "";
        %>
        <%= message %>
    </body>
</html> 