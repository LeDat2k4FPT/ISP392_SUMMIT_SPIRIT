///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package dao;
//
//import dto.RevenueLineDTO;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.util.ArrayList;
//import java.util.List;
//import utils.DBUtils;
//
///**
// *
// * @author Hanne
// */
//public class RevenueDAO {
//
//    // Lấy dữ liệu biểu đồ đường theo ngày / tháng / năm
//    public List<RevenueLineDTO> getLineChartData(String filterType) {
//        List<RevenueLineDTO> list = new ArrayList<>();
//        String groupBy;
//        String selectField;
//
//        switch (filterType.toLowerCase()) {
//            case "day":
//                groupBy = "CAST(o.OrderDate AS DATE)";
//                selectField = "CAST(o.OrderDate AS DATE) AS DatePart";
//                break;
//            case "month":
//                groupBy = "YEAR(o.OrderDate), MONTH(o.OrderDate)";
//                selectField = "FORMAT(o.OrderDate, 'yyyy-MM') AS DatePart";
//                break;
//            case "year":
//                groupBy = "YEAR(o.OrderDate)";
//                selectField = "YEAR(o.OrderDate) AS DatePart";
//                break;
//            default:
//                throw new IllegalArgumentException("Invalid filter type: " + filterType);
//        }
//
//        String sql = "SELECT c.CategoryName, " + selectField + ", SUM(od.Quantity) AS TotalQuantity " +
//                     "FROM [Order] o " +
//                     "JOIN OrderDetails od ON o.OrderID = od.OrderID " +
//                     "JOIN ProductAttribute pa ON od.AttributeID = pa.AttributeID " +
//                     "JOIN Product p ON pa.ProductID = p.ProductID " +
//                     "JOIN Category c ON p.CateID = c.CateID " +
//                     "WHERE o.OrderStatus = 'Delivered' " +
//                     "GROUP BY c.CategoryName, " + groupBy + " " +
//                     "ORDER BY c.CategoryName, DatePart";
//
//        try (Connection conn = DBUtils.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//
//            while (rs.next()) {
//                RevenueLineDTO dto = new RevenueLineDTO();
//                dto.setCategory(rs.getString("CategoryName"));
//                dto.setTimeLabel(rs.getString("DatePart"));
//                dto.setTotalQuantity(rs.getInt("TotalQuantity"));
//                list.add(dto);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return list;
//    }
//
//    // Lấy dữ liệu biểu đồ tròn theo danh mục (category)
//    public List<ProductSoldDTO> getPieChartData(String categoryName) {
//        List<ProductSoldDTO> list = new ArrayList<>();
//
//        String sql = "SELECT p.ProductName, pa.Color, SUM(od.Quantity) AS TotalSold " +
//                     "FROM [Order] o " +
//                     "JOIN OrderDetails od ON o.OrderID = od.OrderID " +
//                     "JOIN ProductAttribute pa ON od.AttributeID = pa.AttributeID " +
//                     "JOIN Product p ON pa.ProductID = p.ProductID " +
//                     "JOIN Category c ON p.CateID = c.CateID " +
//                     "WHERE o.OrderStatus = 'Delivered' AND c.CategoryName = ? " +
//                     "GROUP BY p.ProductName, pa.Color " +
//                     "ORDER BY TotalSold DESC";
//
//        try (Connection conn = DBUtils.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setString(1, categoryName);
//            ResultSet rs = ps.executeQuery();
//
//            while (rs.next()) {
//                ProductSoldDTO dto = new ProductSoldDTO();
//                dto.setProductName(rs.getString("ProductName"));
//                dto.setColor(rs.getString("Color"));
//                dto.setQuantitySold(rs.getInt("TotalSold"));
//                list.add(dto);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return list;
//    }
//}
