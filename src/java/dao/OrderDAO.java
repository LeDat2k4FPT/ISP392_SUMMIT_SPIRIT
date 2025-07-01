package dao;

import dto.OrderDTO;
import dto.OrderDetailDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class OrderDAO {

    private static final String GET_ALL_ORDERS = "SELECT OrderID, UserID, OrderDate, TotalAmount, Status FROM Orders";
    private static final String GET_ORDER_BY_ID = "SELECT * FROM Orders WHERE OrderID = ?";
    private static final String GET_ORDERS_BY_USER = "SELECT * FROM Orders WHERE userID = ? ORDER BY orderDate DESC";
    private static final String CREATE_ORDER = "INSERT INTO Orders (userID, orderDate, total, status) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_ORDER_STATUS = "UPDATE Orders SET status = ? WHERE orderID = ?";
    private static final String DELETE_ORDER = "DELETE FROM Orders WHERE OrderID = ?";

    public List<OrderDTO> getAllOrders() throws SQLException, ClassNotFoundException {
        List<OrderDTO> list = new ArrayList<>();
        String sql = "SELECT o.OrderID, o.OrderDate, o.TotalAmount, o.Status, a.FullName "
                + "FROM Orders o JOIN Account a ON o.UserID = a.UserID "
                + "ORDER BY o.OrderDate DESC";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                OrderDTO order = new OrderDTO();
                order.setOrderID(rs.getInt("OrderID"));
                order.setOrderDate(rs.getDate("OrderDate"));
                order.setTotalAmount(rs.getDouble("TotalAmount"));
                order.setStatus(rs.getString("Status"));
                order.setFullName(rs.getString("FullName"));
                list.add(order);
            }
        }
        return list;
    }

    public OrderDTO getOrderById(int orderID) throws SQLException, ClassNotFoundException {
        OrderDTO order = null;
        String sql = "SELECT o.OrderID, o.UserID, o.OrderDate, o.TotalAmount, o.Status, "
                + "a.FullName, a.Email, a.Phone "
                + "FROM Orders o "
                + "JOIN Account a ON o.UserID = a.UserID "
                + "WHERE o.OrderID = ?";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderID);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    order = new OrderDTO(
                            rs.getInt("OrderID"),
                            rs.getInt("UserID"),
                            rs.getDate("OrderDate"),
                            rs.getDouble("TotalAmount"),
                            rs.getString("Status")
                    );
                    order.setFullName(rs.getString("FullName"));
                    order.setEmail(rs.getString("Email"));
                    order.setPhoneNumber(rs.getString("Phone"));

                    System.out.println("DEBUG: Found orderID = " + order.getOrderID());
                } else {
                    System.out.println("DEBUG: No order found for orderID = " + orderID);
                }
            }
        }
        return order;
    }

    public List<OrderDTO> getOrdersByUser(int userID) throws SQLException {
        List<OrderDTO> orders = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_ORDERS_BY_USER);
                ptm.setInt(1, userID);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    orders.add(new OrderDTO(
                            rs.getInt("OrderID"),
                            rs.getInt("UserID"),
                            rs.getDate("OrderDate"),
                            rs.getDouble("Total"),
                            rs.getString("Status")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return orders;
    }

//    public boolean createOrder(OrderDTO order) throws SQLException {
//        boolean check = false;
//        Connection conn = null;
//        PreparedStatement ptm = null;
//        try {
//            conn = DBUtils.getConnection();
//            if (conn != null) {
//                ptm = conn.prepareStatement(CREATE_ORDER);
//                ptm.setInt(1, order.getUserID());
//                ptm.setDate(2, order.getOrderDate());
//                ptm.setDouble(3, order.getTotal());
//                ptm.setString(4, order.getStatus());
//                check = ptm.executeUpdate() > 0;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (ptm != null) {
//                ptm.close();
//            }
//            if (conn != null) {
//                conn.close();
//            }
//        }
//        return check;
//    }
    public boolean updateOrderStatus(int orderID, String status) throws SQLException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(UPDATE_ORDER_STATUS);
                ptm.setString(1, status);
                ptm.setInt(2, orderID);
                check = ptm.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return check;
    }

    public boolean deleteOrder(int orderID) throws SQLException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(DELETE_ORDER);
                ptm.setInt(1, orderID);
                check = ptm.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return check;
    }

    public List<OrderDetailDTO> getOrderDetails(int orderID) throws SQLException, ClassNotFoundException {
        List<OrderDetailDTO> list = new ArrayList<>();
        String sql = "SELECT od.Quantity, od.UnitPrice, "
                + "pv.ProductID, p.ProductName, s.SizeName, c.ColorName "
                + "FROM OrderDetail od "
                + "JOIN ProductVariant pv ON od.AttributeID = pv.AttributeID "
                + "JOIN Product p ON pv.ProductID = p.ProductID "
                + "JOIN Size s ON pv.SizeID = s.SizeID "
                + "JOIN Color c ON pv.ColorID = c.ColorID "
                + "WHERE od.OrderID = ?";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderID);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderDetailDTO od = new OrderDetailDTO();
                    od.setProductID(rs.getInt("ProductID"));
                    od.setProductName(rs.getString("ProductName"));
                    od.setSizeName(rs.getString("SizeName"));
                    od.setColorName(rs.getString("ColorName"));
                    od.setQuantity(rs.getInt("Quantity"));
                    od.setUnitPrice(rs.getDouble("UnitPrice"));
                    list.add(od);
                }
            }
        }
        return list;
    }

}
