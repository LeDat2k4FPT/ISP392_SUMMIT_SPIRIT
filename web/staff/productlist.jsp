<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Map" %>
<%@ page import="dto.ProductDTO, dto.ProductVariantDTO" %>
<%@ page import="dao.ProductDAO, dao.ProductVariantDAO" %>
<%@ page import="java.sql.SQLException" %>

<%
    ProductDAO dao = new ProductDAO();
    ProductVariantDAO variantDAO = new ProductVariantDAO();

    List<ProductDTO> productList = dao.getAllProducts();
    Map<Integer, List<ProductVariantDTO>> variantMap = variantDAO.getAllVariantsGroupedByProduct();

    String msg = (String) request.getAttribute("message");
%>

<% if (msg != null) { %>
    <div class="success-box"><%= msg %></div>
<% } %>

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
                    <th>Variants</th>
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
                    <td>
                        <table style="font-size: 12px; border-collapse: collapse; width: 100%;">
                            <thead>
                                <tr>
                                    <th>Size</th>
                                    <th>Color</th>
                                    <th>Qty</th>
                                </tr>
                            </thead>
                            <tbody>
                            <%
                                List<ProductVariantDTO> variants = variantMap.get(p.getProductID());
                                if (variants != null) {
                                    for (ProductVariantDTO v : variants) {
                            %>
                                <tr>
                                    <td><%= v.getSizeName() %></td>
                                    <td><%= v.getColorName() %></td>
                                    <td>
                                        <% if (v.getQuantity() == 0) { %>
                                            <span style="color:red;">Out of stock</span>
                                        <% } else { %>
                                            <%= v.getQuantity() %>
                                        <% } %>
                                    </td>
                                </tr>
                            <%
                                    }
                                }
                            %>
                            </tbody>
                        </table>
                    </td>
                    <td class="actions">
                        <a href="<%= request.getContextPath()%>/EditProductController?productID=<%= p.getProductID() %>" title="Edit">🖋️</a>
                        <a href="<%= request.getContextPath()%>/DeleteProductController?productID=<%= p.getProductID() %>" title="Delete"
                           onclick="return confirm('Are you sure?')">🗑️</a>
                    </td>
                </tr>
            <%
                    }
                } else {
            %>
                <tr><td colspan="7" class="no-data">No products found.</td></tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>
