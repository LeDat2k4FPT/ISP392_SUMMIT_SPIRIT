<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="dto.VoucherDTO"%>
<%
    List<VoucherDTO> vouchers = (List<VoucherDTO>) request.getAttribute("vouchers");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Vouchers</title>
    <link rel="stylesheet" href="../css/admin.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-4">
    <h2>Manage Vouchers</h2>
    <hr>
    <form method="post" action="ManageVoucherController">
        <input type="hidden" name="action" value="add">
        <div class="row g-2">
            <div class="col-md-2"><input class="form-control" name="voucherCode" placeholder="Code" required></div>
            <div class="col-md-2"><input class="form-control" name="discountValue" placeholder="Discount" type="number" step="0.01" required></div>
            <div class="col-md-3"><input class="form-control" name="expiryDate" type="date" required></div>
            <div class="col-md-2">
                <select class="form-control" name="status">
                    <option value="Active">Active</option>
                    <option value="Inactive">Inactive</option>
                </select>
            </div>
            <div class="col-md-1">
                <button class="btn btn-success" type="submit">Add</button>
            </div>
        </div>
    </form>
    <table class="table table-bordered mt-3">
        <thead>
        <tr>
            <th>ID</th>
            <th>Code</th>
            <th>Discount</th>
            <th>Expiry Date</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <% if (vouchers != null) for (VoucherDTO v : vouchers) { %>
            <tr>
                <td><%=v.getVoucherID()%></td>
                <td><%=v.getVoucherCode()%></td>
                <td><%=v.getDiscountValue()%></td>
                <td><%=v.getExpiryDate()%></td>
                <td><%=v.getStatus()%></td>
                <td>
                    <form method="post" action="ManageVoucherController" style="display:inline;" onsubmit="return confirm('Are you sure you want to delete (deactivate) this voucher?');">
                        <input type="hidden" name="voucherID" value="<%=v.getVoucherID()%>">
                        <input type="hidden" name="action" value="delete">
                        <button class="btn btn-danger btn-sm" type="submit">Delete</button>
                    </form>
                </td>
            </tr>
        <% } %>
        </tbody>
    </table>
</div>
</body>
</html> 