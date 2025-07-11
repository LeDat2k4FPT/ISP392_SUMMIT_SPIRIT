/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.sql.Date;

/**
 *
 * @author gmt
 */
public class PaymentLogDTO {

    private int logID;
    private int orderID;
    private String txnRef;
    private String transactionNo;
    private double amount;
    private String responseCode;
    private String status;
    private Date createdAt;

    public PaymentLogDTO() {
    }

    public PaymentLogDTO(int logID, int orderID, String txnRef, String transactionNo, double amount, String responseCode, String status, Date createdAt) {
        this.logID = logID;
        this.orderID = orderID;
        this.txnRef = txnRef;
        this.transactionNo = transactionNo;
        this.amount = amount;
        this.responseCode = responseCode;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getLogID() {
        return logID;
    }

    public void setLogID(int logID) {
        this.logID = logID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getTxnRef() {
        return txnRef;
    }

    public void setTxnRef(String txnRef) {
        this.txnRef = txnRef;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
