/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import utils.DBUtils;
import dto.ProductVariantDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class ProductVariantDAO {

    public void insertVariant(ProductVariantDTO variant) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO ProductVariant (AttributeID, ProductID, ColorID, SizeID, Price, Quantity) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            int nextID = getNextAttributeID();

            ps.setInt(1, nextID);
            ps.setInt(2, variant.getProductID());
            ps.setInt(3, variant.getColorID());
            ps.setInt(4, variant.getSizeID());
            ps.setDouble(5, variant.getPrice());
            ps.setInt(6, variant.getQuantity());

            ps.executeUpdate();
        }
    }

    public int getNextAttributeID() throws SQLException, ClassNotFoundException {
        // Lấy AttributeID mới bằng cách lấy MAX hiện tại + 1
        // Nếu bảng chưa có bản ghi nào, dùng 1 làm ID đầu tiên
        String sql = "SELECT ISNULL(MAX(AttributeID), 0) + 1 FROM ProductVariant";
        try (Connection conn = DBUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 1;
    }

    public void deleteByProductID(int productID) throws Exception {
        String sql = "DELETE FROM ProductVariant WHERE ProductID = ?";
        try (Connection con = DBUtils.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, productID);
            ps.executeUpdate();
        }
    }

    public List<String> getAvailableSizesByProductId(int productId) throws SQLException, ClassNotFoundException {
        List<String> sizes = new ArrayList<>();
        String sql = "SELECT DISTINCT s.SizeName " +
                     "FROM ProductVariant pv " +
                     "JOIN Size s ON pv.SizeID = s.SizeID " +
                     "WHERE pv.ProductID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sizes.add(rs.getString("SizeName"));
                }
            }
        }
        return sizes;
    }
}
