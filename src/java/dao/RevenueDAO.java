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
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

/**
 *
 * @author Hanne
 */
public class RevenueDAO {

    /**
     * Retrieves line chart data based on the selected filter (day, month, year)
     * and optional selected values.
     */
    public List<RevenueLineDTO> getLineChartData(String filterType, String selectedDay, String selectedMonth, String selectedYear, String selectedCategory, String orderStatus) {
        List<RevenueLineDTO> list = new ArrayList<>();
        String groupBy;
        String selectField;

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

        if (orderStatus != null && !"All".equalsIgnoreCase(orderStatus)) {
            sql.append("AND o.Status = ? ");
        }

        // Add filter condition
        if (selectedCategory != null && !"All".equalsIgnoreCase(selectedCategory)) {
            sql.append("AND c.CateName = ? ");
        }
        if ("day".equals(filterType) && selectedDay != null && !"all".equalsIgnoreCase(selectedDay)) {
            sql.append("AND CAST(o.OrderDate AS DATE) = ? ");
        } else if ("month".equals(filterType) && selectedMonth != null && !"all".equalsIgnoreCase(selectedMonth)) {
            sql.append("AND FORMAT(o.OrderDate, 'yyyy-MM') = ? ");
        } else if ("year".equals(filterType) && selectedYear != null && !"all".equalsIgnoreCase(selectedYear)) {
            sql.append("AND YEAR(o.OrderDate) = ? ");
        }

        sql.append("GROUP BY c.CateName, ").append(groupBy).append(" ")
           .append("ORDER BY c.CateName, DatePart");

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (orderStatus != null && !"All".equalsIgnoreCase(orderStatus)) {
                ps.setString(paramIndex++, orderStatus);
            }
            if (selectedCategory != null && !"All".equalsIgnoreCase(selectedCategory)) {
                ps.setString(paramIndex++, selectedCategory);
            }
            if ("day".equals(filterType) && selectedDay != null && !"all".equalsIgnoreCase(selectedDay)) {
                ps.setString(paramIndex++, selectedDay); // Format: yyyy-MM-dd
            } else if ("month".equals(filterType) && selectedMonth != null && !"all".equalsIgnoreCase(selectedMonth)) {
                ps.setString(paramIndex++, selectedMonth); // Format: yyyy-MM
            } else if ("year".equals(filterType) && selectedYear != null && !"all".equalsIgnoreCase(selectedYear)) {
                ps.setInt(paramIndex++, Integer.parseInt(selectedYear));
            }

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

    /**
     * Retrieves pie chart data: quantity sold by product and color in a given category.
     */
    public List<ProductSoldDTO> getPieChartData(String categoryName, String filterType, String selectedDay, String selectedMonth, String selectedYear, String orderStatus) {
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

        if (orderStatus != null && !"All".equalsIgnoreCase(orderStatus)) {
            sql.append("AND o.Status = ? ");
        }

        if (categoryName != null && !"All".equalsIgnoreCase(categoryName)) {
            sql.append("AND c.CateName = ? ");
        }
        if ("day".equals(filterType) && selectedDay != null && !"all".equalsIgnoreCase(selectedDay)) {
            sql.append("AND CAST(o.OrderDate AS DATE) = ? ");
        } else if ("month".equals(filterType) && selectedMonth != null && !"all".equalsIgnoreCase(selectedMonth)) {
            sql.append("AND FORMAT(o.OrderDate, 'yyyy-MM') = ? ");
        } else if ("year".equals(filterType) && selectedYear != null && !"all".equalsIgnoreCase(selectedYear)) {
            sql.append("AND YEAR(o.OrderDate) = ? ");
        }

        sql.append("GROUP BY p.ProductName, cl.ColorName ")
           .append("ORDER BY TotalSold DESC");

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (orderStatus != null && !"All".equalsIgnoreCase(orderStatus)) {
                ps.setString(paramIndex++, orderStatus);
            }
            if (categoryName != null && !"All".equalsIgnoreCase(categoryName)) {
                ps.setString(paramIndex++, categoryName);
            }
            if ("day".equals(filterType) && selectedDay != null && !"all".equalsIgnoreCase(selectedDay)) {
                ps.setString(paramIndex++, selectedDay);
            } else if ("month".equals(filterType) && selectedMonth != null && !"all".equalsIgnoreCase(selectedMonth)) {
                ps.setString(paramIndex++, selectedMonth);
            } else if ("year".equals(filterType) && selectedYear != null && !"all".equalsIgnoreCase(selectedYear)) {
                ps.setInt(paramIndex++, Integer.parseInt(selectedYear));
            }

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

    public int countOrders(String selectedDay, String selectedMonth, String selectedYear, String selectedCategory, String orderStatus) {
        int count = 0;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(DISTINCT o.OrderID) FROM Orders o ")
           .append("JOIN OrderDetail od ON o.OrderID = od.OrderID ")
           .append("JOIN ProductVariant pv ON od.AttributeID = pv.AttributeID ")
           .append("JOIN Product p ON pv.ProductID = p.ProductID ")
           .append("JOIN Category c ON p.CateID = c.CateID WHERE 1=1 ");
        if (orderStatus != null && !"All".equalsIgnoreCase(orderStatus)) {
            sql.append("AND o.Status = ? ");
        }
        if (selectedCategory != null && !"All".equalsIgnoreCase(selectedCategory)) {
            sql.append("AND c.CateName = ? ");
        }
        if (selectedDay != null && !"all".equalsIgnoreCase(selectedDay)) {
            sql.append("AND CAST(o.OrderDate AS DATE) = ? ");
        } else if (selectedMonth != null && !"all".equalsIgnoreCase(selectedMonth)) {
            sql.append("AND FORMAT(o.OrderDate, 'yyyy-MM') = ? ");
        } else if (selectedYear != null && !"all".equalsIgnoreCase(selectedYear)) {
            sql.append("AND YEAR(o.OrderDate) = ? ");
        }
        try (Connection conn = DBUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (orderStatus != null && !"All".equalsIgnoreCase(orderStatus)) {
                ps.setString(paramIndex++, orderStatus);
            }
            if (selectedCategory != null && !"All".equalsIgnoreCase(selectedCategory)) {
                ps.setString(paramIndex++, selectedCategory);
            }
            if (selectedDay != null && !"all".equalsIgnoreCase(selectedDay)) {
                ps.setString(paramIndex++, selectedDay);
            } else if (selectedMonth != null && !"all".equalsIgnoreCase(selectedMonth)) {
                ps.setString(paramIndex++, selectedMonth);
            } else if (selectedYear != null && !"all".equalsIgnoreCase(selectedYear)) {
                ps.setInt(paramIndex++, Integer.parseInt(selectedYear));
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return count;
    }

    public int countProductsSold(String selectedDay, String selectedMonth, String selectedYear, String selectedCategory, String orderStatus) {
        int count = 0;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT SUM(od.Quantity) FROM Orders o ")
           .append("JOIN OrderDetail od ON o.OrderID = od.OrderID ")
           .append("JOIN ProductVariant pv ON od.AttributeID = pv.AttributeID ")
           .append("JOIN Product p ON pv.ProductID = p.ProductID ")
           .append("JOIN Category c ON p.CateID = c.CateID WHERE 1=1 ");
        if (orderStatus != null && !"All".equalsIgnoreCase(orderStatus)) {
            sql.append("AND o.Status = ? ");
        }
        if (selectedCategory != null && !"All".equalsIgnoreCase(selectedCategory)) {
            sql.append("AND c.CateName = ? ");
        }
        if (selectedDay != null && !"all".equalsIgnoreCase(selectedDay)) {
            sql.append("AND CAST(o.OrderDate AS DATE) = ? ");
        } else if (selectedMonth != null && !"all".equalsIgnoreCase(selectedMonth)) {
            sql.append("AND FORMAT(o.OrderDate, 'yyyy-MM') = ? ");
        } else if (selectedYear != null && !"all".equalsIgnoreCase(selectedYear)) {
            sql.append("AND YEAR(o.OrderDate) = ? ");
        }
        try (Connection conn = DBUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (orderStatus != null && !"All".equalsIgnoreCase(orderStatus)) {
                ps.setString(paramIndex++, orderStatus);
            }
            if (selectedCategory != null && !"All".equalsIgnoreCase(selectedCategory)) {
                ps.setString(paramIndex++, selectedCategory);
            }
            if (selectedDay != null && !"all".equalsIgnoreCase(selectedDay)) {
                ps.setString(paramIndex++, selectedDay);
            } else if (selectedMonth != null && !"all".equalsIgnoreCase(selectedMonth)) {
                ps.setString(paramIndex++, selectedMonth);
            } else if (selectedYear != null && !"all".equalsIgnoreCase(selectedYear)) {
                ps.setInt(paramIndex++, Integer.parseInt(selectedYear));
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return count;
    }

    public List<ProductSoldDTO> getTopSellingProducts(String selectedDay, String selectedMonth, String selectedYear, String selectedCategory, String orderStatus, int limit) {
        List<ProductSoldDTO> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT TOP " + limit + " p.ProductName, SUM(od.Quantity) AS TotalSold, SUM(od.Quantity * od.UnitPrice) AS TotalRevenue ")
           .append("FROM Orders o ")
           .append("JOIN OrderDetail od ON o.OrderID = od.OrderID ")
           .append("JOIN ProductVariant pv ON od.AttributeID = pv.AttributeID ")
           .append("JOIN Product p ON pv.ProductID = p.ProductID ")
           .append("JOIN Category c ON p.CateID = c.CateID WHERE 1=1 ");
        if (orderStatus != null && !"All".equalsIgnoreCase(orderStatus)) {
            sql.append("AND o.Status = ? ");
        }
        if (selectedCategory != null && !"All".equalsIgnoreCase(selectedCategory)) {
            sql.append("AND c.CateName = ? ");
        }
        if (selectedDay != null && !"all".equalsIgnoreCase(selectedDay)) {
            sql.append("AND CAST(o.OrderDate AS DATE) = ? ");
        } else if (selectedMonth != null && !"all".equalsIgnoreCase(selectedMonth)) {
            sql.append("AND FORMAT(o.OrderDate, 'yyyy-MM') = ? ");
        } else if (selectedYear != null && !"all".equalsIgnoreCase(selectedYear)) {
            sql.append("AND YEAR(o.OrderDate) = ? ");
        }
        sql.append("GROUP BY p.ProductName ")
           .append("ORDER BY TotalSold DESC");
        try (Connection conn = DBUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (orderStatus != null && !"All".equalsIgnoreCase(orderStatus)) {
                ps.setString(paramIndex++, orderStatus);
            }
            if (selectedCategory != null && !"All".equalsIgnoreCase(selectedCategory)) {
                ps.setString(paramIndex++, selectedCategory);
            }
            if (selectedDay != null && !"all".equalsIgnoreCase(selectedDay)) {
                ps.setString(paramIndex++, selectedDay);
            } else if (selectedMonth != null && !"all".equalsIgnoreCase(selectedMonth)) {
                ps.setString(paramIndex++, selectedMonth);
            } else if (selectedYear != null && !"all".equalsIgnoreCase(selectedYear)) {
                ps.setInt(paramIndex++, Integer.parseInt(selectedYear));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProductSoldDTO dto = new ProductSoldDTO();
                dto.setProductName(rs.getString("ProductName"));
                dto.setQuantitySold(rs.getInt("TotalSold"));
                dto.setTotalRevenue(rs.getDouble("TotalRevenue"));
                list.add(dto);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}
