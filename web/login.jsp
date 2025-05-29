<%-- 
    Document   : login
    Created on : May 28, 2025, 5:04:58 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SUMMIT SPIRIT Page</title>
    </head>
    <body>
        
        <form action="MainController" method="POST">
            User ID: <input type="text" name="userID" required=""/>
            </br>Password: <input type="password" name="password" required=""/>
            </br><input type="submit" name="action" value="Login"/>
            <input type="reset" value="Reset"/>
        </form>      
        </br><a href="createUser.jsp">Create user</a>
        <% 
            String message= (String)request.getAttribute("MESSAGE");
            if(message== null) message="";
            
        %>
        <%= message %>
    
       
    </body>
</html>
