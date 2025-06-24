<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="dto.ProductDTO" %>
<%@ page import="dao.ProductDAO" %>
<%@ page import="java.sql.SQLException" %>

<%
    ProductDAO dao = new ProductDAO();
    List<ProductDTO> productList = dao.getAllProducts();
%>
<%
    String msg = (String) request.getAttribute("message");
    if (msg != null) {
%>

    <div class="success-box"><%= msg %></div>
<%
    }
%>
<div class="product-section">
    <div class="product-header">
        <h2>📦 Product</h2>
        <button onclick="loadContent('staff/mnproduct.jsp')" class="btn-add"> ➕ Add product</button>
    </div>

    <div class="product-table-wrapper">
        <table class="product-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Product Name</th>
                    <th>Category</th>
                    <th>Price</th>
                    <th>Stock</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
            <%
                if (productList != null && !productList.isEmpty()) {
                    int row = 0;
                    for (ProductDTO p : productList) {
                        String rowClass = (row++ % 2 == 0) ? "row-even" : "row-odd";
            %>
                <tr class="<%= rowClass %>">
                    <td><%= p.getProductID() %></td>
                    <td><%= p.getProductName() %></td>
                    <td><%= p.getCateName() %></td>
                    <td><%= String.format("%,.0f", p.getPrice())%></td>
                    <td><%= p.getStock() %></td>
                    <td class="actions">
                        <a href="<%= request.getContextPath()%>/EditProductController?productID=<%= p.getProductID() %>" title="Edit">🖋️</a>
                        <a href="<%= request.getContextPath()%>/DeleteProductController?productID=<%= p.getProductID() %>" title="Delete" onclick="return confirm('Are you sure?')">🗑️</a>
                    </td>
                </tr>
            <%
                    }
                } else {
            %>
                <tr><td colspan="6" class="no-data">No products found.</td></tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>
