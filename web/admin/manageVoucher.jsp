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
    <form method="post" action="ManageVoucherController">
        <input type="hidden" name="action" value="add">
        <div class="row g-2">
            <div class="col-md-2"><input class="form-control" name="voucherID" placeholder="ID" type="number" required></div>
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
    <hr>
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
                <form method="post" action="ManageVoucherController">
                    <input type="hidden" name="voucherID" value="<%=v.getVoucherID()%>">
                    <input type="hidden" name="action" value="update">
                    <td><input class="form-control" name="voucherID" value="<%=v.getVoucherID()%>" type="number" readonly></td>
                    <td><input class="form-control" name="voucherCode" value="<%=v.getVoucherCode()%>" required></td>
                    <td><input class="form-control" name="discountValue" value="<%=v.getDiscountValue()%>" type="number" step="0.01" required></td>
                    <td><input class="form-control" name="expiryDate" value="<%=v.getExpiryDate()%>" type="date" required></td>
                    <td>
                        <select class="form-control" name="status">
                            <option value="Active" <%=v.getStatus().equals("Active") ? "selected" : ""%>>Active</option>
                            <option value="Inactive" <%=v.getStatus().equals("Inactive") ? "selected" : ""%>>Inactive</option>
                        </select>
                    </td>
                    <td>
                        <button class="btn btn-primary btn-sm" type="submit">Update</button>
                </form>
                <form method="post" action="ManageVoucherController" style="display:inline;">
                    <input type="hidden" name="voucherID" value="<%=v.getVoucherID()%>">
                    <input type="hidden" name="action" value="delete">
                    <button class="btn btn-danger btn-sm" type="submit" onclick="return confirm('Delete this voucher?')">Delete</button>
                </form>
                    </td>
            </tr>
        <% } %>
        </tbody>
    </table>
</div>
</body>
</html> 