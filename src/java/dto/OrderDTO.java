package dto;

import java.sql.Date;
import java.sql.Timestamp;

public class OrderDTO {
    private int orderID;
    private int userID;
    private Date orderDate;
    private double total;
    private String status;
    private String paymentStatus;
    private Timestamp paymentDate;

    public OrderDTO() {
    }

    public OrderDTO(int orderID, int userID, Date orderDate, double total, String status) {
        this.orderID = orderID;
        this.userID = userID;
        this.orderDate = orderDate;
        this.total = total;
        this.status = status;
    }

    public OrderDTO(int orderID, int userID, Date orderDate, double total, String status, String paymentStatus, Timestamp paymentDate) {
        this.orderID = orderID;
        this.userID = userID;
        this.orderDate = orderDate;
        this.total = total;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Timestamp paymentDate) {
        this.paymentDate = paymentDate;
    }
}

   