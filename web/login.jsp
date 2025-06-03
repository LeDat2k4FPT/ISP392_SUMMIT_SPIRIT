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
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 6a22f44c9917dddd110f1771c211d30f0c0fea21
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                background-color: #f8f9fa;
            }
            .login-container {
                max-width: 400px;
                margin: 100px auto;
                padding: 20px;
                background-color: white;
                border-radius: 10px;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
            }
            .login-title {
                text-align: center;
                margin-bottom: 30px;
                color: #333;
            }
            .form-control {
                margin-bottom: 15px;
            }
            .btn-login {
                width: 100%;
                background-color: #007bff;
                border: none;
            }
            .btn-login:hover {
                background-color: #0056b3;
            }
            .register-link {
                text-align: center;
                margin-top: 15px;
            }
        </style>
<<<<<<< HEAD
    </head>
    <body>
        <div class="container">
            <div class="login-container">
                <h2 class="login-title">Login</h2>
                <form action="LoginController" method="POST">
                    <div class="mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="email" class="form-control" id="email" name="email" required>
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">Password</label>
                        <input type="password" class="form-control" id="password" name="password" required>
                    </div>
                    <button type="submit" class="btn btn-primary btn-login">Login</button>
                    <div class="register-link">
                        <p>Don't have an account? <a href="createUser.jsp">Register here</a></p>
                    </div>
                </form>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
=======
    </head>
    <body>
        <div class="container">
            <div class="login-container">
                <h2 class="login-title">Login</h2>
                <form action="LoginController" method="POST">
                    <div class="mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="email" class="form-control" id="email" name="email" required>
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">Password</label>
                        <input type="password" class="form-control" id="password" name="password" required>
                    </div>
                    <button type="submit" class="btn btn-primary btn-login">Login</button>
                    <div class="register-link">
                        <p>Don't have an account? <a href="createUser.jsp">Register here</a></p>
                    </div>
                </form>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
=======
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
    
       
>>>>>>> 608a15763e5e09c5cb5d6b029a0d33fd43000f9f
>>>>>>> 6a22f44c9917dddd110f1771c211d30f0c0fea21
    </body>
</html>
