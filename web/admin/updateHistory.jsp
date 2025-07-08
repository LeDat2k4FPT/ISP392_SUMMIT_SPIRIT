<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="dto.UpdateHistoryDTO"%>
<%
    List<UpdateHistoryDTO> logs = (List<UpdateHistoryDTO>) request.getAttribute("logs");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Update History</title>
    <link rel="stylesheet" href="../css/admin.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-4">
    <h2>Update History</h2>
    <table class="table table-bordered mt-3">
        <thead>
        <tr>
            <th>LogID</th>
            <th>Action</th>
            <th>Table</th>
            <th>RecordID</th>
            <th>Old Value</th>
            <th>New Value</th>
            <th>By</th>
            <th>At</th>
        </tr>
        </thead>
        <tbody>
        <% if (logs != null) for (UpdateHistoryDTO log : logs) { %>
            <tr>
                <td><%= log.getLogID() %></td>
                <td><%= log.getActionType() %></td>
                <td><%= log.getTableName() %></td>
                <td><%= log.getRecordID() %></td>
                <td><%= log.getOldValue() %></td>
                <td><%= log.getNewValue() %></td>
                <td><%= log.getPerformedBy() %></td>
                <td><%= log.getPerformedAt() %></td>
            </tr>
        <% } %>
        </tbody>
    </table>
</div>
</body>
</html> 