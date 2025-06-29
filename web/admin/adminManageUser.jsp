<%-- 
    Document   : adminManageUser
    Created on : Jun 15, 2025, 3:08:04 AM
    Author     : Hanne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="dto.UserDTO" %>
<%
    List<UserDTO> users = (List<UserDTO>) request.getAttribute("users");
    String keyword = request.getAttribute("keyword") != null ? (String) request.getAttribute("keyword") : "";
%>

<div class="container-fluid px-0">
    <h2 class="mb-4" style="color:#234C45;font-weight:600;">Manage User Accounts</h2>
    <form method="get" action="manageUser" class="row g-2 align-items-center mb-3">
        <div class="col-md-4">
            <input type="text" class="form-control" name="keyword" value="<%= keyword %>" placeholder="Search by name, email, address, or role...">
        </div>
        <div class="col-auto">
            <button type="submit" class="btn btn-success" style="background:#234C45;">Search</button>
        </div>
        <div class="col-auto">
            <button type="button" class="btn btn-outline-success" style="color:#234C45;border-color:#234C45;" onclick="loadContent('admin/createUserAccount.jsp')">Create New Account</button>
        </div>
    </form>
    <div class="table-responsive">
        <table class="table table-bordered table-hover align-middle bg-white">
            <thead class="table-light">
                <tr>
                    <th>UserID</th>
                    <th>Full Name</th>
                    <th>Email</th>
                    <th>Address</th>
                    <th>Role</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
            <% if (users != null) { 
               for (UserDTO u : users) { %>
                <tr>
                    <td><%= u.getUserID() %></td>
                    <td><%= u.getFullName() %></td>
                    <td><%= u.getEmail() %></td>
                    <td><%= u.getAddress() %></td>
                    <td><%= u.getRole() %></td>
                    <td>
                        <form action="ManageUserActionController" method="post" style="display:inline;" onsubmit="return confirm('Are you sure to change role?');">
                            <input type="hidden" name="userID" value="<%= u.getUserID() %>">
                            <input type="hidden" name="action" value="editRole">
                            <button type="submit" class="btn btn-sm btn-outline-primary">Change Role</button>
                        </form>
                        <form action="ManageUserActionController" method="post" style="display:inline;" onsubmit="return confirm('Are you sure you want to permanently delete user <%= u.getFullName() %>?');">
                            <input type="hidden" name="userID" value="<%= u.getUserID() %>">
                            <input type="hidden" name="action" value="delete">
                            <button type="submit" class="btn btn-sm btn-outline-danger">Delete</button>
                        </form>
                    </td>
                </tr>
            <%     } 
               } else { %>
                <tr><td colspan="6" class="text-center text-muted">No users found or error loading users.</td></tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>
