<%-- 
    Document   : viewRevenue
    Created on : Jun 24, 2025, 6:08:49 PM
    Author     : Hanne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<%@page import="java.util.*, com.google.gson.Gson"%>
<%@page import="dto.RevenueLineDTO, dto.ProductSoldDTO"%>

<h1 style="text-align:center;">📈 Revenue Overview</h1>
<!-- Filter Form -->
<form action="${pageContext.request.contextPath}/ViewRevenueController" method="get">
    <label>Month:
        <select name="monthValue">
            <% 
                String selectedMonth = (String) request.getAttribute("selectedMonth");
                out.print("<option value='all'" + ("all".equals(selectedMonth) ? " selected" : "") + ">All</option>");
                for (int i = 1; i <= 12; i++) {
                    String val = String.valueOf(i);
                    String sel = val.equals(selectedMonth) ? " selected" : "";
                    out.print("<option value='" + val + "'" + sel + ">Month " + val + "</option>");
                }
            %>
        </select>
    </label>

    <label>Year:
        <select name="yearValue">
            <% 
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                String selectedYear = (String) request.getAttribute("selectedYear");
                out.print("<option value='all'" + ("all".equals(selectedYear) ? " selected" : "") + ">All</option>");
                for (int y = currentYear; y >= currentYear - 5; y--) {
                    String val = String.valueOf(y);
                    String sel = val.equals(selectedYear) ? " selected" : "";
                    out.print("<option value='" + val + "'" + sel + ">" + val + "</option>");
                }
            %>
        </select>
    </label>

    <label>Category:
        <select name="category">
            <% 
                List categoryList = (List) request.getAttribute("categoryList");
                String selectedCategory = (String) request.getAttribute("selectedCategory");
                out.print("<option value='all'" + ("all".equals(selectedCategory) ? " selected" : "") + ">All</option>");
                if (categoryList != null) {
                    for (Object obj : categoryList) {
                        dto.CategoryDTO cate = (dto.CategoryDTO) obj;
                        if ("all".equalsIgnoreCase(cate.getCateName())) continue;
                        String selected = cate.getCateName().equals(selectedCategory) ? "selected" : "";
                        out.print("<option value='" + cate.getCateName() + "' " + selected + ">" + cate.getCateName() + "</option>");
                    }
                }
            %>
        </select>
    </label>

    <button type="submit">Apply Filters</button>
</form>

<!-- Tổng doanh thu -->
<%
    List lineData = (List) request.getAttribute("lineData");
    double totalRevenue = 0;
    if (lineData != null) {
        for (Object obj : lineData) {
            dto.RevenueLineDTO dto = (dto.RevenueLineDTO) obj;
            totalRevenue += dto.getTotalRevenue();
        }
    }
%>
<div style="display:flex; justify-content:center; gap:30px; margin-bottom:30px;">
    <div style="background:#fff; border-radius:10px; box-shadow:0 2px 8px #eee; padding:20px 30px; min-width:180px; text-align:center;">
        <div style="font-size:2em; color:#195181; font-weight:bold;"> <%= String.format("%,.0f", totalRevenue) %> </div>
        <div style="color:#888;">Total Revenue (VND)</div>
    </div>
    <div style="background:#fff; border-radius:10px; box-shadow:0 2px 8px #eee; padding:20px 30px; min-width:180px; text-align:center;">
        <div style="font-size:2em; color:#234C45; font-weight:bold;"> <%= request.getAttribute("totalOrders") %> </div>
        <div style="color:#888;">Total Orders</div>
    </div>
    <div style="background:#fff; border-radius:10px; box-shadow:0 2px 8px #eee; padding:20px 30px; min-width:180px; text-align:center;">
        <div style="font-size:2em; color:#28a745; font-weight:bold;"> <%= request.getAttribute("totalProducts") %> </div>
        <div style="color:#888;">Products Sold</div>
    </div>
    <div style="background:#fff; border-radius:10px; box-shadow:0 2px 8px #eee; padding:20px 30px; min-width:180px; text-align:center;">
        <div style="font-size:2em; color:#007bff; font-weight:bold;"> <%= request.getAttribute("deliveredOrders") %> </div>
        <div style="color:#888;">Delivered Orders</div>
    </div>
    <div style="background:#fff; border-radius:10px; box-shadow:0 2px 8px #eee; padding:20px 30px; min-width:180px; text-align:center;">
        <div style="font-size:2em; color:#dc3545; font-weight:bold;"> <%= request.getAttribute("cancelledOrders") %> </div>
        <div style="color:#888;">Cancelled Orders</div>
    </div>
</div>

<!-- Line Chart Section -->
<div class="chart-container">
    <h3>Line Chart - <span id="lineChartTitle">Sales Quantity</span> by <%= request.getAttribute("filterType") %></h3>
    <div style="text-align:center; margin-bottom:10px;">
        <button type="button" id="toggleChartBtn" style="padding:6px 18px; border-radius:6px; background:#195181; color:#fff; border:none; cursor:pointer;">Toggle Quantity/Revenue</button>
    </div>
    <canvas id="lineChart"></canvas>
</div>

<!-- Pie Chart Section -->
<div class="chart-container">
    <h3>Pie Chart - Product Distribution in "<%= request.getAttribute("selectedCategory") %>"</h3>
    <canvas id="pieChart"></canvas>
</div>

<!-- Bảng chi tiết doanh thu -->
<div class="chart-container" style="margin-bottom: 30px;">
    <h3 style="text-align:center;">Revenue Details</h3>
    <table style="width:100%; border-collapse:collapse; background:#fff; box-shadow:0 2px 8px #eee;">
        <thead>
            <tr style="background:#195181; color:#fff;">
                <th style="padding:10px;">Time</th>
                <th style="padding:10px;">Category</th>
                <th style="padding:10px;">Quantity Sold</th>
                <th style="padding:10px;">Revenue (VND)</th>
            </tr>
        </thead>
        <tbody>
            <% if (lineData != null && !lineData.isEmpty()) {
                for (Object obj : lineData) {
                    dto.RevenueLineDTO dto = (dto.RevenueLineDTO) obj;
            %>
            <tr>
                <td style="padding:8px; text-align:center;"><%= dto.getTimeLabel() %></td>
                <td style="padding:8px; text-align:center;"><%= dto.getCategory() %></td>
                <td style="padding:8px; text-align:right;"><%= dto.getTotalQuantity() %></td>
                <td style="padding:8px; text-align:right;"><%= String.format("%,.0f", dto.getTotalRevenue()) %></td>
            </tr>
            <%  } 
               } else { %>
            <tr><td colspan="4" style="text-align:center; color:#888; padding:15px;">No data available.</td></tr>
            <% } %>
        </tbody>
    </table>
</div>

<!-- Bar Chart Top Products -->
<div class="chart-container" style="margin-bottom: 30px;">
    <h3 style="text-align:center;">Top 5 Best-Selling Products</h3>
    <canvas id="barChart"></canvas>
    <table style="width:100%; border-collapse:collapse; background:#fff; box-shadow:0 2px 8px #eee; margin-top:20px;">
        <thead>
            <tr style="background:#195181; color:#fff;">
                <th style="padding:10px;">Product</th>
                <th style="padding:10px;">Quantity Sold</th>
                <th style="padding:10px;">Revenue (VND)</th>
            </tr>
        </thead>
        <tbody>
            <% 
                List topProducts = (List) request.getAttribute("topProducts");
                if (topProducts != null && !topProducts.isEmpty()) {
                    for (Object obj : topProducts) {
                        dto.ProductSoldDTO dto = (dto.ProductSoldDTO) obj;
            %>
            <tr>
                <td style="padding:8px; text-align:center;"><%= dto.getProductName() %></td>
                <td style="padding:8px; text-align:right;"><%= dto.getQuantitySold() %></td>
                <td style="padding:8px; text-align:right;"><%= String.format("%,.0f", dto.getTotalRevenue()) %></td>
            </tr>
            <%  } 
               } else { %>
            <tr><td colspan="3" style="text-align:center; color:#888; padding:15px;">No data available.</td></tr>
            <% } %>
        </tbody>
    </table>
</div>

<script>
<%
    // Chuẩn bị dữ liệu cho line chart
    if (lineData == null) lineData = new ArrayList<>();
    Map<String, Map<String, Integer>> lineMap = new LinkedHashMap<>();
    Map<String, Map<String, Double>> revenueMap = new LinkedHashMap<>();
    for (Object obj : lineData) {
        dto.RevenueLineDTO dto = (dto.RevenueLineDTO) obj;
        lineMap.putIfAbsent(dto.getCategory(), new LinkedHashMap<>());
        lineMap.get(dto.getCategory()).put(dto.getTimeLabel(), dto.getTotalQuantity());
        revenueMap.putIfAbsent(dto.getCategory(), new LinkedHashMap<>());
        revenueMap.get(dto.getCategory()).put(dto.getTimeLabel(), dto.getTotalRevenue());
    }
    String lineMapJson = new com.google.gson.Gson().toJson(lineMap);
    String revenueMapJson = new com.google.gson.Gson().toJson(revenueMap);

    // Bar chart data
    if (topProducts == null) topProducts = new ArrayList();
    List<String> prodNames = new ArrayList<>();
    List<Integer> prodQty = new ArrayList<>();
    for (Object obj : topProducts) {
        dto.ProductSoldDTO dto = (dto.ProductSoldDTO) obj;
        prodNames.add(dto.getProductName());
        prodQty.add(dto.getQuantitySold());
    }
    String prodNamesJson = new com.google.gson.Gson().toJson(prodNames);
    String prodQtyJson = new com.google.gson.Gson().toJson(prodQty);

    // Pie chart data
    List<ProductSoldDTO> pieData = (List<ProductSoldDTO>) request.getAttribute("pieData");
    if (pieData == null) pieData = new ArrayList<>();
    List<String> labels = new ArrayList<>();
    List<Integer> values = new ArrayList<>();
    List<String> colors = new ArrayList<>();
    for (ProductSoldDTO dto : pieData) {
        labels.add(dto.getProductName() + " (" + dto.getColor() + ")");
        values.add(dto.getQuantitySold());
        colors.add("rgb(" + (int)(Math.random()*255) + "," + (int)(Math.random()*255) + "," + (int)(Math.random()*255) + ")");
    }
    Map<String, Object> pieMap = new HashMap<>();
    pieMap.put("labels", labels);
    pieMap.put("values", values);
    pieMap.put("colors", colors);
    String pieMapJson = new com.google.gson.Gson().toJson(pieMap);
%>
    const lineRaw = <%= lineMapJson %>;
const revenueRaw = <%= revenueMapJson %>;
console.log('✅ Dữ liệu lineRaw:', lineRaw);

    let showingRevenue = false;

    const ctxLine = document.getElementById('lineChart').getContext('2d');
    const labelSet = new Set();
    Object.values(lineRaw).forEach(obj => Object.keys(obj).forEach(key => labelSet.add(key)));
    const lineLabels = Array.from(labelSet).sort();

    const colorPalette = ['#195181', '#28a745', '#dc3545', '#007bff', '#ffc107', '#6f42c1', '#fd7e14', '#20c997', '#343a40', '#e83e8c'];

    function getDatasets(dataMap) {
        return Object.keys(dataMap).map((cat, idx) => ({
            label: cat,
            data: lineLabels.map(l => dataMap[cat][l] || 0),
            borderColor: colorPalette[idx % colorPalette.length],
            borderWidth: 2,
            fill: false
        }));
    }

    let lineChart = new Chart(ctxLine, {
        type: 'line',
        data: {
            labels: lineLabels,
            datasets: getDatasets(lineRaw)
        },
        options: {
            responsive: true,
            plugins: {
                title: { display: true, text: 'Sales Quantity' },
                legend: { position: 'bottom' }
            }
        }
    });

    document.getElementById('toggleChartBtn').onclick = function () {
        showingRevenue = !showingRevenue;
        lineChart.data.datasets = getDatasets(showingRevenue ? revenueRaw : lineRaw);
        document.getElementById('lineChartTitle').innerText = showingRevenue ? 'Revenue' : 'Sales Quantity';
        lineChart.options.plugins.title.text = showingRevenue ? 'Revenue' : 'Sales Quantity';
        lineChart.update();
    };

    // Pie Chart
    const pieRaw = <%= pieMapJson %>;
    const ctxPie = document.getElementById('pieChart').getContext('2d');
    new Chart(ctxPie, {
        type: 'pie',
        data: {
            labels: pieRaw.labels,
            datasets: [{
                data: pieRaw.values,
                backgroundColor: pieRaw.colors
            }]
        },
        options: {
            responsive: true,
            plugins: {
                title: { display: true, text: 'Sold Products by Color' },
                legend: { position: 'right' }
            }
        }
    });

    // Bar Chart - Top Products
    const topProducts = <%= prodNamesJson %>;
    const topQty = <%= prodQtyJson %>;
    const ctxBar = document.getElementById('barChart').getContext('2d');
    new Chart(ctxBar, {
        type: 'bar',
        data: {
            labels: topProducts,
            datasets: [{
                label: 'Quantity Sold',
                data: topQty,
                backgroundColor: '#195181',
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { display: false },
                title: { display: true, text: 'Top 5 Best-Selling Products' }
            },
            scales: {
                y: { beginAtZero: true }
            }
        }
    });
</script>

