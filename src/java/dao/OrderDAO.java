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
    private static final String GET_ORDERS_BY_USER = "SELECT OrderID, OrderDate, Status, TotalAmount FROM Orders WHERE UserID = ? ORDER BY OrderDate DESC";
    private static final String CREATE_ORDER = "INSERT INTO Orders (userID, orderDate, total, status) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_ORDER_STATUS = "UPDATE Orders SET status = ? WHERE orderID = ?";
    private static final String DELETE_ORDER = "DELETE FROM Orders WHERE OrderID = ?";
    private static final String INSERT_ORDER = "INSERT INTO Orders (UserID, OrderDate, TotalAmount, Status) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_STATUS = "UPDATE Orders SET Status = ? WHERE OrderID = ?";

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
                    // Kiểm tra null trước khi set
                    String fullName = rs.getString("FullName");
                    if (fullName != null) {
                        order.setFullName(fullName);
                    }
                    String email = rs.getString("Email");
                    if (email != null) {
                        order.setEmail(email);
                    }
                    String phone = rs.getString("Phone");
                    if (phone != null) {
                        order.setPhoneNumber(phone);
                    }

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
                            userID, // vì bạn không select UserID, gán thẳng
                            rs.getDate("OrderDate"),
                            rs.getDouble("TotalAmount"),
                            rs.getString("Status")
                    ));
                    System.out.println("DEBUG: Found orderID = " + rs.getInt("OrderID"));
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

//    public List<OrderDTO> getOrdersByUserID(int userID) {
//    List<OrderDTO> list = new ArrayList<>();
//    try {
//        String sql = "SELECT OrderID, OrderDate, Status, TotalAmount FROM Orders WHERE UserID=? ORDER BY OrderDate DESC";
//        PreparedStatement ps = connection.prepareStatement(sql);
//        ps.setInt(1, userID);
//        ResultSet rs = ps.executeQuery();
//        while (rs.next()) {
//            OrderDTO o = new OrderDTO();
//            o.setOrderID(rs.getInt("OrderID"));
//            o.setOrderDate(rs.getDate("OrderDate"));
//            o.setStatus(rs.getString("Status"));
//            o.setTotalAmount(rs.getDouble("TotalAmount"));
//            list.add(o);
//        }
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//    return list;
//}
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
//    public boolean updateOrderStatus(int orderID, String status) throws SQLException {
//        boolean check = false;
//        Connection conn = null;
//        PreparedStatement ptm = null;
//        try {
//            conn = DBUtils.getConnection();
//            if (conn != null) {
//                ptm = conn.prepareStatement(UPDATE_ORDER_STATUS);
//                ptm.setString(1, status);
//                ptm.setInt(2, orderID);
//                check = ptm.executeUpdate() > 0;
//                System.out.println("Executing update: orderID=" + orderID + ", status=" + status);
//                int rows = ptm.executeUpdate();
//                System.out.println("Updated rows: " + rows);
//                check = rows > 0;
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

        if (orderID <= 0 || status == null || status.isEmpty()) {
            System.err.println("[updateOrderStatus] Lỗi: orderID hoặc status không hợp lệ. orderID=" + orderID + ", status=" + status);
            return false;
        }
        boolean check = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                System.out.println("[DAO] Preparing to update orderID=" + orderID + " to status=" + status);
                ptm = conn.prepareStatement("UPDATE Orders SET status = ? WHERE orderID = ?");
                ptm.setString(1, status);
                ptm.setInt(2, orderID);
                int count = ptm.executeUpdate();
                System.out.println("[DAO] Updated rows = " + count);
                check = count > 0;
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

//        boolean check = false;
//        Connection conn = null;
//        PreparedStatement ptm = null;
//        try {
//            conn = DBUtils.getConnection();
//            if (conn != null) {
//                System.out.println("[DAO] Preparing to update orderID=" + orderID + " to status=" + status);
//                ptm = conn.prepareStatement("UPDATE Orders SET status = ? WHERE orderID = ?");
//                ptm.setString(1, status);
//                ptm.setInt(2, orderID);
//                int count = ptm.executeUpdate();
//                System.out.println("[DAO] Updated rows = " + count);
//                check = count > 0;
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

    public int insertOrder(OrderDTO order) throws SQLException, ClassNotFoundException {
        int orderId = -1;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            // Kiểm tra dữ liệu đầu vào
            if (order == null || order.getUserID() <= 0 || order.getTotalAmount() <= 0 || order.getStatus() == null || order.getStatus().isEmpty()) {
                System.err.println("[insertOrder] Lỗi: Dữ liệu đầu vào không hợp lệ. userID=" + (order != null ? order.getUserID() : "null") + ", totalAmount=" + (order != null ? order.getTotalAmount() : "null") + ", status=" + (order != null ? order.getStatus() : "null"));
                return -1;
            }
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(INSERT_ORDER, PreparedStatement.RETURN_GENERATED_KEYS);
                ptm.setInt(1, order.getUserID());
                ptm.setTimestamp(2, order.getOrderDate() != null
                        ? new java.sql.Timestamp(order.getOrderDate().getTime())
                        : new java.sql.Timestamp(System.currentTimeMillis()));
                ptm.setDouble(3, order.getTotalAmount());
                ptm.setString(4, order.getStatus() != null ? order.getStatus() : "Pending");
                int affectedRows = ptm.executeUpdate();
                if (affectedRows > 0) {
                    rs = ptm.getGeneratedKeys();
                    if (rs.next()) {
                        orderId = rs.getInt(1);
                    }
                } else {
                    System.err.println("[insertOrder] Không insert được order vào DB. affectedRows=0");
                }
            } else {
                System.err.println("[insertOrder] Không kết nối được DB.");
            }
        } catch (Exception ex) {
            System.err.println("[insertOrder] Exception: " + ex.getMessage());
            ex.printStackTrace();
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
        return orderId;
    }

    public boolean updateOrderStatus(OrderDTO order) throws SQLException, ClassNotFoundException {
        if (order == null || order.getOrderID() <= 0 || order.getStatus() == null || order.getStatus().isEmpty()) {
            System.err.println("[updateOrderStatus] Lỗi: order hoặc trường dữ liệu không hợp lệ. orderID=" + (order != null ? order.getOrderID() : "null") + ", status=" + (order != null ? order.getStatus() : "null"));
            return false;
        }
        boolean check = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(UPDATE_STATUS);
            ptm.setString(1, order.getStatus());
            ptm.setInt(2, order.getOrderID());
            int rowsAffected = ptm.executeUpdate();
            check = rowsAffected > 0;
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

    public List<OrderDTO> searchOrders(String keyword) throws SQLException, ClassNotFoundException {
        List<OrderDTO> list = new ArrayList<>();
        String sql = "SELECT o.OrderID, o.OrderDate, o.TotalAmount, o.Status, a.FullName "
                + "FROM Orders o "
                + "JOIN Account a ON o.UserID = a.UserID "
                + "WHERE o.OrderID LIKE ? OR o.Status LIKE ? OR a.FullName LIKE ? OR a.Email LIKE ? "
                + "ORDER BY o.OrderDate DESC";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);
            ps.setString(4, kw);
            try ( ResultSet rs = ps.executeQuery()) {
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
        }
        return list;
    }

    public List<OrderDetailDTO> getOrderDetailsByOrderID(int orderID) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public double getTotalSpentByUser(int userID) throws SQLException, ClassNotFoundException {
        String sql = "SELECT SUM(TotalAmount) FROM Orders WHERE UserID = ? AND Status = 'Delivered'";
        try (Connection con = DBUtils.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        }
        return 0;
    }

    public int getOrderCountByUser(int userID) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM Orders WHERE UserID = ? AND Status = 'Delivered'";
        try (Connection con = DBUtils.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

}
