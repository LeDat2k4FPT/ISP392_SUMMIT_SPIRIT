/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import utils.DBUtils;

/**
 *
 * @author Admin
 */
public class SizeDAO {

    public int getOrInsertSize(String sizeName) throws SQLException, ClassNotFoundException {
        String selectSql = "SELECT SizeID FROM Size WHERE SizeName = ?";
        String insertSql = "INSERT INTO Size (SizeName) VALUES (?)";

        try (Connection conn = DBUtils.getConnection()) {
            // Kiểm tra đã tồn tại chưa
            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.setString(1, sizeName);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("SizeID");
                }
            }

            // Nếu chưa có → chèn mới
            try (PreparedStatement ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, sizeName);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return -1;
    }
}
