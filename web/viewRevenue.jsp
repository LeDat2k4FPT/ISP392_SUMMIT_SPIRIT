<%-- 
    Document   : viewRevenue
    Created on : Jun 24, 2025, 6:08:49 PM
    Author     : Hanne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, com.google.gson.Gson"%>
<%@page import="dto.RevenueLineDTO, dto.ProductSoldDTO"%>
<!DOCTYPE html>
<html>
<head>
    <title>View Revenue</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background: #f7f7f7; }
        .chart-container { width: 80%; margin: auto; padding-bottom: 50px; }
        form { text-align: center; margin-bottom: 30px; }
        select, input, button { margin: 5px; padding: 5px; }
    </style>
</head>
<body>
    <h1 style="text-align:center;">📈 Revenue Overview</h1>

    <!-- Filter Form -->
    <form method="get" action="ViewRevenueController">
        <label>View by:
            <select name="filter">
                <option value="day" <%= "day".equals(request.getAttribute("filterType")) ? "selected" : "" %>>Day</option>
                <option value="month" <%= "month".equals(request.getAttribute("filterType")) ? "selected" : "" %>>Month</option>
                <option value="year" <%= "year".equals(request.getAttribute("filterType")) ? "selected" : "" %>>Year</option>
            </select>
        </label>

        <label>Date:
            <select name="dateValue">
                <option value="all">All</option>
                <% for (int i = 1; i <= 31; i++) { %>
                    <option value="<%= i %>"><%= i %></option>
                <% } %>
            </select>
        </label>

        <label>Month:
            <select name="monthValue">
                <option value="all">All</option>
                <% for (int i = 1; i <= 12; i++) { %>
                    <option value="<%= i %>">Month <%= i %></option>
                <% } %>
            </select>
        </label>

        <label>Year:
            <select name="yearValue">
                <option value="all">All</option>
                <% 
                    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                    for (int y = currentYear; y >= currentYear - 5; y--) {
                %>
                    <option value="<%= y %>"><%= y %></option>
                <% } %>
            </select>
        </label>

        <label>Category:
            <select name="category">
                <option value="All" <%= "All".equals(request.getAttribute("selectedCategory")) ? "selected" : "" %>>All</option>
                <option value="Clothing" <%= "Clothing".equals(request.getAttribute("selectedCategory")) ? "selected" : "" %>>Clothing</option>
                <option value="Bags" <%= "Bags".equals(request.getAttribute("selectedCategory")) ? "selected" : "" %>>Bags</option>
                <option value="Shoes" <%= "Shoes".equals(request.getAttribute("selectedCategory")) ? "selected" : "" %>>Shoes</option>
                <!-- Add more categories as needed -->
            </select>
        </label>

        <button type="submit">Apply Filters</button>
    </form>

    <!-- Line Chart Section -->
    <div class="chart-container">
        <h3>Line Chart - Sales Quantity by <%= request.getAttribute("filterType") %></h3>
        <canvas id="lineChart"></canvas>
    </div>

    <!-- Pie Chart Section -->
    <div class="chart-container">
        <h3>Pie Chart - Product Distribution in "<%= request.getAttribute("selectedCategory") %>"</h3>
        <canvas id="pieChart"></canvas>
    </div>

    <script>
        // Line Chart Data
        const lineRaw = <%
            List<RevenueLineDTO> lineData = (List<RevenueLineDTO>) request.getAttribute("lineData");
            if (lineData == null) lineData = new ArrayList<>();

            Map<String, Map<String, Integer>> lineMap = new LinkedHashMap<>();
            for (RevenueLineDTO dto : lineData) {
                lineMap.putIfAbsent(dto.getCategory(), new LinkedHashMap<>());
                lineMap.get(dto.getCategory()).put(dto.getTimeLabel(), dto.getTotalQuantity());
            }
            out.print(new Gson().toJson(lineMap));
        %>;

        // Pie Chart Data
        const pieRaw = <%
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

            out.print(new Gson().toJson(pieMap));
        %>;

        // Render Line Chart
        const ctxLine = document.getElementById('lineChart').getContext('2d');
        const labelSet = new Set();
        Object.values(lineRaw).forEach(obj => Object.keys(obj).forEach(key => labelSet.add(key)));
        const lineLabels = Array.from(labelSet).sort();

        const lineDatasets = Object.keys(lineRaw).map(cat => ({
            label: cat,
            data: lineLabels.map(l => lineRaw[cat][l] || 0),
            borderColor: `hsl(${Math.random()*360}, 70%, 50%)`,
            borderWidth: 2,
            fill: false
        }));

        new Chart(ctxLine, {
            type: 'line',
            data: {
                labels: lineLabels,
                datasets: lineDatasets
            },
            options: {
                responsive: true,
                plugins: {
                    title: { display: true, text: 'Sales Quantity' },
                    legend: { position: 'bottom' }
                }
            }
        });

        // Render Pie Chart
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
    </script>
</body>
</html>


