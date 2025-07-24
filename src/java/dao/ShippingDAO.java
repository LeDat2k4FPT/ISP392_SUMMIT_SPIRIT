/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Hanne
 */


import dto.ShippingDTO;
import utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShippingDAO {

    // Gán shipper khi Accept đơn
    public boolean assignOrderToShipper(int orderID, int shipperID) throws SQLException {
        String sql = "INSERT INTO Shipping (OrderID, UserID) VALUES (?, ?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderID);
            ps.setInt(2, shipperID);
            return ps.executeUpdate() > 0;
        }
    }

    // Cập nhật ảnh, thời gian và ghi chú khi giao thành công
    public boolean markAsDelivered(int orderID, String imageUrl, String note) throws SQLException {
        String sql = "UPDATE Shipping SET DeliveryTime = GETDATE(), DeliveryImageURL = ?, Note = ? WHERE OrderID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, imageUrl);
            ps.setString(2, note);
            ps.setInt(3, orderID);
            return ps.executeUpdate() > 0;
        }
    }

    // Truy xuất lịch sử giao hàng của shipper (cho historyShipping.jsp)
    public List<ShippingDTO> getDeliveredByShipper(int shipperID) throws SQLException {
    List<ShippingDTO> list = new ArrayList<>();
    String sql = "SELECT * FROM Shipping WHERE UserID = ? AND DeliveryTime IS NOT NULL";
    try (Connection conn = DBUtils.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, shipperID);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ShippingDTO dto = new ShippingDTO();
                dto.setShippingID(rs.getInt("ShippingID"));
                dto.setOrderID(rs.getInt("OrderID"));
                dto.setUserID(rs.getInt("UserID"));
                dto.setDeliveryTime(rs.getTimestamp("DeliveryTime"));
                dto.setDeliveryImageURL(rs.getString("DeliveryImageURL"));
                dto.setNote(rs.getString("Note"));
                list.add(dto);
            }
        }
    }
    return list;
}


}


