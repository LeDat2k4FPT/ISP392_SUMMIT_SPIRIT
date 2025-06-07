package dao;

import dto.OrderDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class OrderDAO {

    private static final String GET_ALL_ORDERS = "SELECT * FROM Orders";
    private static final String GET_ORDER_BY_ID = "SELECT * FROM Orders WHERE OrderID = ?";
    private static final String GET_ORDERS_BY_USER = "SELECT * FROM Orders WHERE userID = ? ORDER BY orderDate DESC";
    private static final String CREATE_ORDER = "INSERT INTO Orders (userID, orderDate, total, status) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_ORDER_STATUS = "UPDATE Orders SET status = ? WHERE orderID = ?";
    private static final String DELETE_ORDER = "DELETE FROM Orders WHERE OrderID = ?";

    public List<OrderDTO> getAllOrders() throws SQLException {
        List<OrderDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_ALL_ORDERS);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    list.add(new OrderDTO(
                            rs.getInt("OrderID"),
                            rs.getInt("UserID"),
                            rs.getDate("OrderDate"),
                            rs.getDouble("Total"),
                            rs.getString("Status"),
                            rs.getString("PaymentStatus"),
                            rs.getTimestamp("PaymentDate")
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
        return list;
    }

    public OrderDTO getOrderById(int orderID) throws SQLException {
        OrderDTO order = null;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_ORDER_BY_ID);
                ptm.setInt(1, orderID);
                rs = ptm.executeQuery();
                if (rs.next()) {
                    order = new OrderDTO(
                            rs.getInt("OrderID"),
                            rs.getInt("UserID"),
                            rs.getDate("OrderDate"),
                            rs.getDouble("Total"),
                            rs.getString("Status")
                    );
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

    public boolean createOrder(OrderDTO order) throws SQLException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(CREATE_ORDER);
                ptm.setInt(1, order.getUserID());
                ptm.setDate(2, order.getOrderDate());
                ptm.setDouble(3, order.getTotal());
                ptm.setString(4, order.getStatus());
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
}
