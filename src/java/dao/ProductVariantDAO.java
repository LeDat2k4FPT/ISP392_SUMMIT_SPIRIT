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
import java.util.HashMap;
import java.util.Map;

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
        // Láº¥y AttributeID má»›i báº±ng cÃ¡ch láº¥y MAX hiá»‡n táº¡i + 1
        // Náº¿u báº£ng chÆ°a cÃ³ báº£n ghi nÃ o, dÃ¹ng 1 lÃ m ID Ä‘áº§u tiÃªn
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
    public List<String> getAvailableColorsByProductId(int productId) throws SQLException, ClassNotFoundException {
    List<String> colors = new ArrayList<>();
    String sql = "SELECT DISTINCT c.ColorName " +
                 "FROM ProductVariant pv " +
                 "JOIN Color c ON pv.ColorID = c.ColorID " +
                 "WHERE pv.ProductID = ?";
    try (Connection conn = DBUtils.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, productId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                colors.add(rs.getString("ColorName"));
            }
        }
    }
    return colors;
}

    // ThÃªm phÆ°Æ¡ng thá»©c láº¥y táº¥t cáº£ variant, nhÃ³m theo ProductID
    public Map<Integer, List<ProductVariantDTO>> getAllVariantsGroupedByProduct() throws SQLException, ClassNotFoundException {
        Map<Integer, List<ProductVariantDTO>> map = new HashMap<>();
        String sql = "SELECT pv.AttributeID, pv.ProductID, pv.ColorID, pv.SizeID, pv.Price, pv.Quantity, c.ColorName, s.SizeName " +
                "FROM ProductVariant pv " +
                "LEFT JOIN Color c ON pv.ColorID = c.ColorID " +
                "LEFT JOIN Size s ON pv.SizeID = s.SizeID";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ProductVariantDTO variant = new ProductVariantDTO();
                variant.setAttributeID(rs.getInt("AttributeID"));
                variant.setProductID(rs.getInt("ProductID"));
                variant.setColorID(rs.getInt("ColorID"));
                variant.setSizeID(rs.getInt("SizeID"));
                variant.setPrice(rs.getDouble("Price"));
                variant.setQuantity(rs.getInt("Quantity"));
                variant.setColorName(rs.getString("ColorName"));
                variant.setSizeName(rs.getString("SizeName"));
                int productId = variant.getProductID();
                map.computeIfAbsent(productId, k -> new ArrayList<>()).add(variant);
            }
        }
        return map;
    }

    public List<ProductVariantDTO> getVariantsByProductId(int productId) throws SQLException, ClassNotFoundException {
        List<ProductVariantDTO> list = new ArrayList<>();
        String sql = "SELECT pv.AttributeID, pv.ProductID, pv.ColorID, pv.SizeID, pv.Price, pv.Quantity, c.ColorName, s.SizeName " +
                "FROM ProductVariant pv " +
                "LEFT JOIN Color c ON pv.ColorID = c.ColorID " +
                "LEFT JOIN Size s ON pv.SizeID = s.SizeID " +
                "WHERE pv.ProductID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProductVariantDTO variant = new ProductVariantDTO();
                    variant.setAttributeID(rs.getInt("AttributeID"));
                    variant.setProductID(rs.getInt("ProductID"));
                    variant.setColorID(rs.getInt("ColorID"));
                    variant.setSizeID(rs.getInt("SizeID"));
                    variant.setPrice(rs.getDouble("Price"));
                    variant.setQuantity(rs.getInt("Quantity"));
                    variant.setColorName(rs.getString("ColorName"));
                    variant.setSizeName(rs.getString("SizeName"));
                    list.add(variant);
                }
            }
        }
        return list;
    }


    public void deleteVariantByID(int attributeID) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM ProductVariant WHERE AttributeID=?";
        try (Connection conn = DBUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, attributeID);
            ps.executeUpdate();
        }
    }

    public void updateVariant(ProductVariantDTO variant) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE ProductVariant SET ColorID = ?, SizeID = ?, Price = ?, Quantity = ? WHERE AttributeID = ? AND ProductID = ?";
        try (Connection conn = DBUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, variant.getColorID());
            ps.setInt(2, variant.getSizeID());
            ps.setDouble(3, variant.getPrice());
            ps.setInt(4, variant.getQuantity());
            ps.setInt(5, variant.getAttributeID());
            ps.setInt(6, variant.getProductID());
            int affected = ps.executeUpdate();
            if (affected == 0) {
                // Náº¿u khÃ´ng cÃ³ dÃ²ng nÃ o bá»‹ update, thÃ¬ insert má»›i
                insertVariant(variant);
            }
        }
    }

}
