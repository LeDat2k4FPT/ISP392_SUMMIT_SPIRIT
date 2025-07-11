/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.PaymentLogDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import utils.DBUtils;

/**
 *
 * @author gmt
 */
public class PaymentLogDAO {

    private static final String ADD_PAYMENT = "INSERT INTO PaymentLog (OrderID, txnRef, transactionNo, amount, responseCode, status, createdAt) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

    public boolean addPaymentLog(PaymentLogDTO log) throws SQLException, ClassNotFoundException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(ADD_PAYMENT);
                ptm.setInt(1, log.getOrderID());
                ptm.setString(2, log.getTxnRef());
                ptm.setString(3, log.getTransactionNo());
                ptm.setDouble(4, log.getAmount());
                ptm.setString(5, log.getResponseCode());
                ptm.setString(6, log.getStatus());
                ptm.setDate(7, new java.sql.Date(log.getCreatedAt().getTime()));
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
