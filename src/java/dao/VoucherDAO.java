package dao;

import dto.VoucherDTO;
import java.sql.*;
import java.util.*;
import utils.DBUtils;

public class VoucherDAO {
    public List<VoucherDTO> getAllVouchers() throws SQLException, ClassNotFoundException {
        List<VoucherDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Voucher";
        try (Connection con = DBUtils.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new VoucherDTO(
                    rs.getInt("VoucherID"),
                    rs.getString("VoucherCode"),
                    rs.getDouble("DiscountValue"),
                    rs.getDate("ExpiryDate"),
                    rs.getString("Status")
                ));
            }
        }
        return list;
    }
    public boolean addVoucher(VoucherDTO v) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Voucher(VoucherID, VoucherCode, DiscountValue, ExpiryDate, Status) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBUtils.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, v.getVoucherID());
            ps.setString(2, v.getVoucherCode());
            ps.setDouble(3, v.getDiscountValue());
            ps.setDate(4, v.getExpiryDate());
            ps.setString(5, v.getStatus());
            return ps.executeUpdate() > 0;
        }
    }
    public boolean updateVoucher(VoucherDTO v) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Voucher SET VoucherCode=?, DiscountValue=?, ExpiryDate=?, Status=? WHERE VoucherID=?";
        try (Connection con = DBUtils.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, v.getVoucherCode());
            ps.setDouble(2, v.getDiscountValue());
            ps.setDate(3, v.getExpiryDate());
            ps.setString(4, v.getStatus());
            ps.setInt(5, v.getVoucherID());
            return ps.executeUpdate() > 0;
        }
    }
    public boolean deleteVoucher(int voucherID) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Voucher WHERE VoucherID=?";
        try (Connection con = DBUtils.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, voucherID);
            return ps.executeUpdate() > 0;
        }
    }
    public VoucherDTO getVoucherById(int voucherID) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Voucher WHERE VoucherID=?";
        try (Connection con = DBUtils.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, voucherID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new VoucherDTO(
                        rs.getInt("VoucherID"),
                        rs.getString("VoucherCode"),
                        rs.getDouble("DiscountValue"),
                        rs.getDate("ExpiryDate"),
                        rs.getString("Status")
                    );
                }
            }
        }
        return null;
    }
} 