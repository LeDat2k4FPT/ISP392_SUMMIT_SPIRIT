<%-- 
    Document   : adminManageUser
    Created on : Jun 15, 2025, 3:08:04 AM
    Author     : Hanne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ page import="java.util.List" %>
<%@ page import="dto.UserDTO" %>
<%
    List<UserDTO> users = (List<UserDTO>) request.getAttribute("users");
    String keyword = request.getAttribute("keyword") != null ? (String) request.getAttribute("keyword") : "";
%>
<html>
<head>
    <title>Manage User Accounts</title>
    <script>
        function confirmChangeRole(fullname, currentRole) {
            return confirm(`Are you sure to change role?`);
    }        
        function confirmLogout() {
            if (confirm("Do you really want to logout?")) {
                window.location.href = 'login.jsp';
            }
        }
    </script>
</head>
<body>
<h2>Manage User Accounts</h2>
<form method="get" action="manageUser">
    <input type="text" name="keyword" value="<%= keyword %>" placeholder="Search by name, email, address, or role...">
    <input type="submit" value="Search">
    <button type="button" onclick="window.location.href='createUserAccount.jsp'">Create New Account</button>
    <button type="button" onclick="confirmLogout()">Logout</button>
    <button type="button" onclick="window.location.href='admin.jsp'">Back</button>
    
</form>
<br>
<table border="1">
    <tr>
        <th>UserID</th>
        <th>Full Name</th>
        <th>Email</th>
        <th>Address</th>
        <th>Role</th>
        <th>Action</th>
    </tr>
    <% for (UserDTO u : users) { %>
        <tr>
            <td><%= u.getUserID() %></td>
            <td><%= u.getFullName() %></td>
            <td><%= u.getEmail() %></td>
            <td><%= u.getAddress() %></td>
            <td><%= u.getRole() %></td>
            <td>
                <!-- Change Role Button -->
                <form action="ManageUserActionController" method="post" style="display:inline;" onsubmit="return confirmChangeRole('<%= u.getFullName() %>', '<%= u.getRole() %>');">
                    <input type="hidden" name="userID" value="<%= u.getUserID() %>">
                    <input type="hidden" name="action" value="editRole">
                    <input type="submit" value="Change Role">
                </form>

                <!-- Delete Button with confirm -->
                <form action="ManageUserActionController" method="post" style="display:inline;"
                      onsubmit="return confirm('Are you sure you want to permanently delete user <%= u.getFullName() %>?');">
                    <input type="hidden" name="userID" value="<%= u.getUserID() %>">
                    <input type="hidden" name="action" value="delete">
                    <input type="submit" value="Delete">
                </form>

            </td>

        </tr>
    <% } %>
</table>
</body>
</html>