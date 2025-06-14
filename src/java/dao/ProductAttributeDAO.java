/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.ProductAttributeDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import utils.DBUtils;

/**
 *
 * @author Admin
 */
public class ProductAttributeDAO {

    public int getNextAttributeID() throws SQLException, ClassNotFoundException {
        String sql = "SELECT ISNULL(MAX(AttributeID), 0) + 1 FROM ProductAttribute";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 1;
    }

    public void insertAttribute(ProductAttributeDTO attr) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO ProductAttribute (AttributeID, ProductID, Color, Size, Stock) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int id = getNextAttributeID();
            ps.setInt(1, id);
            ps.setInt(2, attr.getProductID());
            ps.setString(3, attr.getColor());
            ps.setString(4, attr.getSize());
            ps.setInt(5, attr.getStock());
            ps.executeUpdate();
        }
    }
}

