/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Admin
 */

import dto.ProductImageDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import utils.DBUtils;

public class ProductImageDAO {

    /**
     * Thêm ảnh mới vào bảng ProductImages
     *
     * @param image ProductImageDTO gồm: imageURL, productID
     * @throws SQLException
     */
 public void insertImage(ProductImageDTO image) throws SQLException, ClassNotFoundException {
    String sql = "INSERT INTO ProductImages (ImageID, ImageURL, ProductID) VALUES (?, ?, ?)";

    try (Connection conn = DBUtils.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        int imageID = getNextImageID(); // tự động lấy ID tiếp theo

        ps.setInt(1, imageID);
        ps.setString(2, image.getImageURL());
        ps.setInt(3, image.getProductID());

        ps.executeUpdate();
    }
}

    //hàm tự động lấy max ID hiện tại + 1 trong ProductImageDAO khi insert
    public int getNextImageID() throws SQLException, ClassNotFoundException {
    String sql = "SELECT ISNULL(MAX(ImageID), 0) + 1 FROM ProductImages";
    try (Connection conn = DBUtils.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
            return rs.getInt(1);
        }
    }
    return 1;
}

    // Các hàm bổ sung nếu cần sau này:
    // - getImagesByProductID(int productId)
    // - deleteImage(int imageID)
    // - updateImage(int imageID, String newURL)
}


