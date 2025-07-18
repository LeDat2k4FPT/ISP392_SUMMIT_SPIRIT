/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.ProductSoldDTO;
import dto.RevenueLineDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

/**
 *
 * @author Hanne
 */
public class RevenueDAO {

    private void appendFilterConditions(StringBuilder sql, String orderStatus, String category, String day, String month, String year, String filterType) {
        if (orderStatus != null && !"All".equalsIgnoreCase(orderStatus)) {
            sql.append("AND o.Status = ? ");
        }
        if (category != null && !"All".equalsIgnoreCase(category)) {
            sql.append("AND c.CateName = ? ");
        }
        if ("day".equalsIgnoreCase(filterType) && day != null && !"all".equalsIgnoreCase(day)) {
            sql.append("AND CAST(o.OrderDate AS DATE) = ? ");
        } else if ("month".equalsIgnoreCase(filterType) && month != null && !"all".equalsIgnoreCase(month)) {
            sql.append("AND FORMAT(o.OrderDate, 'yyyy-MM') = ? ");
        } else if ("year".equalsIgnoreCase(filterType) && year != null && !"all".equalsIgnoreCase(year)) {
            sql.append("AND YEAR(o.OrderDate) = ? ");
        }
    }

    private int setFilterParameters(PreparedStatement ps, String orderStatus, String category, String day, String month, String year, String filterType) throws SQLException {
        int paramIndex = 1;
        if (orderStatus != null && !"All".equalsIgnoreCase(orderStatus)) {
            ps.setString(paramIndex++, orderStatus);
        }
        if (category != null && !"All".equalsIgnoreCase(category)) {
            ps.setString(paramIndex++, category);
        }
        if ("day".equalsIgnoreCase(filterType) && day != null && !"all".equalsIgnoreCase(day)) {
            ps.setString(paramIndex++, day);
        } else if ("month".equalsIgnoreCase(filterType) && month != null && !"all".equalsIgnoreCase(month)) {
            ps.setString(paramIndex++, month);
        } else if ("year".equalsIgnoreCase(filterType) && year != null && !"all".equalsIgnoreCase(year)) {
            ps.setInt(paramIndex++, Integer.parseInt(year));
        }
        return paramIndex;
    }

    public List<RevenueLineDTO> getLineChartData(String filterType, String day, String month, String year, String category, String status) {
        List<RevenueLineDTO> list = new ArrayList<>();
        String groupBy, selectField;

        switch (filterType.toLowerCase()) {
            case "day":
                groupBy = "CAST(o.OrderDate AS DATE)";
                selectField = "CAST(o.OrderDate AS DATE) AS DatePart";
                break;
            case "month":
                groupBy = "YEAR(o.OrderDate), MONTH(o.OrderDate)";
                selectField = "FORMAT(o.OrderDate, 'yyyy-MM') AS DatePart";
                break;
            case "year":
                groupBy = "YEAR(o.OrderDate)";
                selectField = "YEAR(o.OrderDate) AS DatePart";
                break;
            default:
                throw new IllegalArgumentException("Invalid filter type: " + filterType);
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT c.CateName, ").append(selectField).append(", SUM(od.Quantity) AS TotalQuantity, SUM(od.Quantity * od.UnitPrice) AS TotalRevenue ")
           .append("FROM Orders o ")
           .append("JOIN OrderDetail od ON o.OrderID = od.OrderID ")
           .append("JOIN ProductVariant pv ON od.AttributeID = pv.AttributeID ")
           .append("JOIN Product p ON pv.ProductID = p.ProductID ")
           .append("JOIN Category c ON p.CateID = c.CateID ")
           .append("WHERE 1=1 ");
        appendFilterConditions(sql, status, category, day, month, year, filterType);
        sql.append("GROUP BY c.CateName, ").append(groupBy).append(" ORDER BY c.CateName, DatePart");

        try (Connection conn = DBUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            setFilterParameters(ps, status, category, day, month, year, filterType);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RevenueLineDTO dto = new RevenueLineDTO();
                dto.setCategory(rs.getString("CateName"));
                dto.setTimeLabel(rs.getString("DatePart"));
                dto.setTotalQuantity(rs.getInt("TotalQuantity"));
                dto.setTotalRevenue(rs.getDouble("TotalRevenue"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ProductSoldDTO> getPieChartData(String category, String filterType, String day, String month, String year, String status) {
        List<ProductSoldDTO> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.ProductName, cl.ColorName, SUM(od.Quantity) AS TotalSold ")
           .append("FROM Orders o ")
           .append("JOIN OrderDetail od ON o.OrderID = od.OrderID ")
           .append("JOIN ProductVariant pv ON od.AttributeID = pv.AttributeID ")
           .append("JOIN Product p ON pv.ProductID = p.ProductID ")
           .append("JOIN Category c ON p.CateID = c.CateID ")
           .append("JOIN Color cl ON pv.ColorID = cl.ColorID ")
           .append("WHERE 1=1 ");
        appendFilterConditions(sql, status, category, day, month, year, filterType);
        sql.append("GROUP BY p.ProductName, cl.ColorName ORDER BY TotalSold DESC");

        try (Connection conn = DBUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            setFilterParameters(ps, status, category, day, month, year, filterType);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProductSoldDTO dto = new ProductSoldDTO();
                dto.setProductName(rs.getString("ProductName"));
                dto.setColor(rs.getString("ColorName"));
                dto.setQuantitySold(rs.getInt("TotalSold"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countOrders(String day, String month, String year, String category, String status) {
        int count = 0;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(DISTINCT o.OrderID) FROM Orders o ")
           .append("JOIN OrderDetail od ON o.OrderID = od.OrderID ")
           .append("JOIN ProductVariant pv ON od.AttributeID = pv.AttributeID ")
           .append("JOIN Product p ON pv.ProductID = p.ProductID ")
           .append("JOIN Category c ON p.CateID = c.CateID WHERE 1=1 ");
        appendFilterConditions(sql, status, category, day, month, year, day != null ? "day" : month != null ? "month" : "year");

        try (Connection conn = DBUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            setFilterParameters(ps, status, category, day, month, year, day != null ? "day" : month != null ? "month" : "year");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) count = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public int countProductsSold(String day, String month, String year, String category, String status) {
        int count = 0;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT SUM(od.Quantity) FROM Orders o ")
           .append("JOIN OrderDetail od ON o.OrderID = od.OrderID ")
           .append("JOIN ProductVariant pv ON od.AttributeID = pv.AttributeID ")
           .append("JOIN Product p ON pv.ProductID = p.ProductID ")
           .append("JOIN Category c ON p.CateID = c.CateID WHERE 1=1 ");
        appendFilterConditions(sql, status, category, day, month, year, day != null ? "day" : month != null ? "month" : "year");

        try (Connection conn = DBUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            setFilterParameters(ps, status, category, day, month, year, day != null ? "day" : month != null ? "month" : "year");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) count = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public List<ProductSoldDTO> getTopSellingProducts(String day, String month, String year, String category, String status, int limit) {
        List<ProductSoldDTO> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT TOP ").append(limit).append(" p.ProductName, SUM(od.Quantity) AS TotalSold, SUM(od.Quantity * od.UnitPrice) AS TotalRevenue ")
           .append("FROM Orders o ")
           .append("JOIN OrderDetail od ON o.OrderID = od.OrderID ")
           .append("JOIN ProductVariant pv ON od.AttributeID = pv.AttributeID ")
           .append("JOIN Product p ON pv.ProductID = p.ProductID ")
           .append("JOIN Category c ON p.CateID = c.CateID WHERE 1=1 ");
        appendFilterConditions(sql, status, category, day, month, year, day != null ? "day" : month != null ? "month" : "year");
        sql.append("GROUP BY p.ProductName ORDER BY TotalSold DESC");

        try (Connection conn = DBUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            setFilterParameters(ps, status, category, day, month, year, day != null ? "day" : month != null ? "month" : "year");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProductSoldDTO dto = new ProductSoldDTO();
                dto.setProductName(rs.getString("ProductName"));
                dto.setQuantitySold(rs.getInt("TotalSold"));
                dto.setTotalRevenue(rs.getDouble("TotalRevenue"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
