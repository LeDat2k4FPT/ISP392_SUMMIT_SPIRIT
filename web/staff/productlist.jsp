<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="dto.ProductDTO" %>
<%@ page import="dto.ProductVariantDTO" %>
<%@ page import="java.util.Comparator" %>
<%
    String keyword = request.getParameter("keyword");
    if (keyword == null) keyword = "";
%>
<%
    List<ProductDTO> productList = (List<ProductDTO>) request.getAttribute("productList");
    Map<Integer, List<ProductVariantDTO>> variantMap = (Map<Integer, List<ProductVariantDTO>>) request.getAttribute("variantMap");
    if (productList != null) {
        productList.sort(java.util.Comparator.comparingInt(dto.ProductDTO::getProductID));
    }
%>
<%
    String msg = (String) request.getAttribute("message");
    if (msg != null) {
%>

    <div class="success-box"><%= msg %></div>
<%
    }
%>
<div class="search-bar" style="margin-bottom:15px;">
    <form method="get" action="<%= request.getContextPath() %>/staff/productlist.jsp">
        <input type="text" name="keyword" value="<%= keyword %>" placeholder="🔍 Search product name..." style="padding:5px; width:200px;">
        <button type="submit">Search</button>
    </form>
</div>


<div class="product-section">
    <div class="product-header">
        <h2>📦 Product</h2>
        <button onclick="loadContent('staff/mnproduct.jsp')" class="btn-add"> ➕ Add product</button>
    </div>

    <div class="product-table-wrapper">
        <table class="product-table">
            <thead>
                <tr>
                    <th rowspan="2">ID</th>
                    <th rowspan="2">Product name</th>
                    <th rowspan="2">Category</th>
                    <th colspan="4" style="text-align:center;">Variant</th>
                    <th rowspan="2">Stock</th>
                    <th rowspan="2">Status</th>
                    <th rowspan="2">Actions</th>
                </tr>
                <tr>
                    <th style="text-align:center;">Size</th>
                    <th style="text-align:center;">Color</th>
                    <th style="text-align:center;">Quantity</th>
                    <th style="text-align:center;">Price</th>
                </tr>
            </thead>
            <tbody>
            <%
                if (productList != null && !productList.isEmpty()) {
                    for (ProductDTO p : productList) {
                        List<ProductVariantDTO> variants = variantMap != null ? variantMap.get(p.getProductID()) : null;
                        if (variants != null) {
                            variants.sort(Comparator.comparing(ProductVariantDTO::getSizeName, Comparator.nullsLast(String::compareTo))
                                .thenComparing(ProductVariantDTO::getColorName, Comparator.nullsLast(String::compareTo)));
                        }
                        int variantCount = (variants != null) ? variants.size() : 0;
                        boolean firstRow = true;
                        if (variantCount > 0) {
                            for (ProductVariantDTO v : variants) {
            %>
                <tr>
                    <% if (firstRow) { %>
                        <td rowspan="<%= variantCount %>"><%= p.getProductID() %></td>
                        <td rowspan="<%= variantCount %>"><%= p.getProductName() %></td>
                        <td rowspan="<%= variantCount %>"><%= p.getCateName() %></td>
                    <% } %>
                    <td style="text-align:center;"><%= v.getSizeName() != null ? v.getSizeName() : "" %></td>
                    <td style="text-align:center;"><%= v.getColorName() != null ? v.getColorName() : "" %></td>
                    <td style="text-align:center;"><%= v.getQuantity() != 0 ? v.getQuantity() : "" %></td>
                    <td style="text-align:center;"><%= v.getPrice() != 0 ? String.format("%,.0f", v.getPrice()) : "" %></td>
                    <% if (firstRow) { %>
                        <td rowspan="<%= variantCount %>"><%= p.getStock() %></td>
                        <td rowspan="<%= variantCount %>"><%= p.getStatus() != null ? p.getStatus() : "active" %></td>
                        <td rowspan="<%= variantCount %>" class="actions">
                            <a href="<%= request.getContextPath()%>/EditProductController?productID=<%= p.getProductID() %>" title="Edit">🖋️</a>
                            <a href="<%= request.getContextPath()%>/DeleteProductController?productID=<%= p.getProductID() %>" title="Delete" onclick="return confirm('Are you sure?')">🗑️</a>
                        </td>
                    <% } %>
                </tr>
            <%
                                firstRow = false;
                            }
                        } else {
            %>
                <tr>
                    <td><%= p.getProductID() %></td>
                    <td><%= p.getProductName() %></td>
                    <td><%= p.getCateName() %></td>
                    <td colspan="4" style="text-align:center;">No variants</td>
                    <td><%= p.getStock() %></td>
                    <td><%= p.getStatus() != null ? p.getStatus() : "active" %></td>
                    <td class="actions">
                        <a href="<%= request.getContextPath()%>/EditProductController?productID=<%= p.getProductID() %>" title="Edit">🖋️</a>
                        <a href="<%= request.getContextPath()%>/DeleteProductController?productID=<%= p.getProductID() %>" title="Delete" onclick="return confirm('Are you sure?')">🗑️</a>
                    </td>
                </tr>
            <%
                        }
                    }
                } else {
            %>
                <tr><td colspan="10" class="no-data">No products found.</td></tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>
