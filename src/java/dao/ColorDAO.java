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
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

/**
 *
 * @author Admin
 */
public class ColorDAO {

    public int getOrInsertColor(String colorName) throws SQLException, ClassNotFoundException {
        String selectSql = "SELECT ColorID FROM Color WHERE ColorName = ?";
        String insertSql = "INSERT INTO Color (ColorName) VALUES (?)";

        try (Connection conn = DBUtils.getConnection()) {
            // Kiểm tra đã tồn tại chưa
            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.setString(1, colorName);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("ColorID");
                }
            }

            // Nếu chưa có → chèn mới
            try (PreparedStatement ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, colorName);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return -1;
    }

    public String getColorNameById(int colorID) throws SQLException, ClassNotFoundException {
        String sql = "SELECT ColorName FROM Color WHERE ColorID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, colorID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("ColorName");
                }
            }
        }
        return null;
    }

    public List<String> getAllColors() throws SQLException, ClassNotFoundException {
        List<String> colors = new ArrayList<>();
        String sql = "SELECT ColorName FROM Color";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                colors.add(rs.getString("ColorName"));
            }
        }
        return colors;
    }
}

