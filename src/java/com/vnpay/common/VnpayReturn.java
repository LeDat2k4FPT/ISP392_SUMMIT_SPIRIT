/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.vnpay.common;

import dao.OrderDAO;
import dao.OrderDetailDAO;
import dao.PaymentLogDAO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import dto.PaymentLogDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gmt
 */
public class VnpayReturn extends HttpServlet {

    private final OrderDAO orderDao = new OrderDAO();
    private final OrderDetailDAO detailDao = new OrderDetailDAO();
    private final PaymentLogDAO logDao = new PaymentLogDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Log đầu vào
        System.out.println("==== VNPay Return ====");
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            System.out.println(param + "=" + request.getParameter(param));
        }
        try {
            Map<String, String> fields = new HashMap<>();
            for (Enumeration<String> params2 = request.getParameterNames(); params2.hasMoreElements();) {
                String fieldName = (String) params2.nextElement();
                String fieldValue = request.getParameter(fieldName);
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    fields.put(fieldName, fieldValue);
                }
            }
            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            fields.remove("vnp_SecureHashType");
            fields.remove("vnp_SecureHash");

            String signValue = Config.hashAllFields(fields);
            if (!signValue.equals(vnp_SecureHash)) {
                System.out.println("Chữ ký không hợp lệ!");
                request.setAttribute("paymentResult", "invalid");
                request.setAttribute("message", "❌ Chữ ký không hợp lệ! Dữ liệu có thể đã bị thay đổi.");
                request.getRequestDispatcher("paymentResult.jsp").forward(request, response);
                return;
            }

            String responseCode = request.getParameter("vnp_ResponseCode");
            String fullTxnRef = request.getParameter("vnp_TxnRef");
            String transactionNo = request.getParameter("vnp_TransactionNo");
            long amount = Long.parseLong(request.getParameter("vnp_Amount")) / 100;

            int orderId;
            try {
                String[] parts = fullTxnRef.split("-");
                orderId = Integer.parseInt(parts[0]); // tách orderId từ txnRef
                System.out.println("Parsed orderId from vnp_TxnRef: " + orderId);
            } catch (Exception ex) {
                System.out.println("Không thể lấy orderId từ mã giao dịch: " + fullTxnRef);
                request.setAttribute("paymentResult", "error");
                request.setAttribute("message", "❌ Không thể lấy orderId từ mã giao dịch.");
                request.getRequestDispatcher("paymentResult.jsp").forward(request, response);
                return;
            }

            HttpSession session = request.getSession();
            Object sessionObj = session.getAttribute("PENDING_ORDER");
            if (!(sessionObj instanceof Map)) {
                System.out.println("Thiếu dữ liệu đơn hàng trong session.");
                request.setAttribute("paymentResult", "error");
                request.setAttribute("message", "❌ Thiếu dữ liệu đơn hàng trong session.");
                request.getRequestDispatcher("paymentResult.jsp").forward(request, response);
                return;
            }
            Map<String, Object> orderSessionData = (Map<String, Object>) sessionObj;

            PaymentLogDTO log = new PaymentLogDTO();
            log.setOrderID(orderId);
            log.setTxnRef(fullTxnRef);
            log.setTransactionNo(transactionNo);
            log.setAmount((double) amount);
            log.setResponseCode(responseCode);
            log.setStatus("00".equals(responseCode) ? "SUCCESS" : "FAILED");
            log.setCreatedAt(new java.sql.Date(System.currentTimeMillis()));
            logDao.addPaymentLog(log);

            if ("00".equals(responseCode) && orderSessionData != null) {
                // Thanh toán thành công
                int userId = (int) orderSessionData.get("userID");
                double totalAmount = (double) orderSessionData.get("totalAmount");

                String discountCode = (String) orderSessionData.getOrDefault("discountCode", "");
                double discountPercent = 0;
                try {
                    discountPercent = Double.parseDouble(orderSessionData.getOrDefault("discountPercent", "0").toString());
                } catch (Exception e) {
                    discountPercent = 0;
                }

                String country = (String) orderSessionData.get("country");
                String fullName = (String) orderSessionData.get("fullName");
                String phone = (String) orderSessionData.get("phone");
                String email = (String) orderSessionData.get("email");
                String address = (String) orderSessionData.get("address");
                String district = (String) orderSessionData.get("district");
                String city = (String) orderSessionData.get("city");

                // Cập nhật đơn hàng
                OrderDTO order = new OrderDTO();
                order.setStatus("Preparing");
                order.setOrderID(orderId);

                System.out.println("Order info before update: " + order);
                boolean updated = orderDao.updateOrderAfterPayment(order);
                System.out.println("Order update result: " + updated);
                if (updated) {
                    // Thêm chi tiết đơn hàng
                    int productId = Integer.parseInt(String.valueOf(orderSessionData.get("productId")));
                    int quantity = Integer.parseInt(String.valueOf(orderSessionData.get("quantity")));
                    String size = (String) orderSessionData.get("size");
                    String color = (String) orderSessionData.get("color");
                    boolean fromSaleOff = Boolean.parseBoolean(String.valueOf(orderSessionData.get("fromSaleOff")));
                    int attributeId = detailDao.getAttributeIdByProductSizeColor(productId, color, size);
                    double unitPrice = detailDao.getUnitPriceByAttributeId(attributeId);

                    OrderDetailDTO detail = new OrderDetailDTO();
                    detail.setOrderID(orderId);
                    detail.setAttributeID(attributeId);
                    detail.setQuantity(quantity);
                    detail.setUnitPrice(unitPrice);

                    detailDao.addOrderDetail(detail);
                    // Xóa session tạm
                    session.removeAttribute("PENDING_ORDER");
                    session.removeAttribute("TEMP_ORDER_ID");
                    request.setAttribute("paymentResult", "success");
                    request.setAttribute("message", "✅ Thanh toán thành công!");
                    request.setAttribute("transactionId", request.getParameter("vnp_TransactionNo"));
                    request.setAttribute("amount", String.format("%,.0f", (double) amount));
                    request.setAttribute("orderInfo", request.getParameter("vnp_OrderInfo"));
                } else {
                    System.out.println("Không thể cập nhật đơn hàng với orderId=" + orderId);
                    request.setAttribute("paymentResult", "error");
                    request.setAttribute("message", "❌ Không thể cập nhật đơn hàng.");
                }
            } else {
                // Giao dịch thất bại hoặc không có session
                System.out.println("Thanh toán thất bại hoặc không có session. orderId=" + orderId + ", responseCode=" + responseCode);
                OrderDTO order = new OrderDTO();
                order.setOrderID(orderId);
                order.setStatus("Failed");
                orderDao.updateOrderStatus(order);
                request.setAttribute("paymentResult", "failed");
                request.setAttribute("message", "❌ Thanh toán thất bại! Mã lỗi: " + responseCode);
            }
        } catch (Exception e) {
            Logger.getLogger(VnpayReturn.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Lỗi xử lý giao dịch: " + e.getMessage());
            request.setAttribute("paymentResult", "error");
            request.setAttribute("message", "❌ Lỗi xử lý giao dịch: " + e.getMessage());
        }
        // Hiển thị trang kết quả
        request.getRequestDispatcher("paymentResult.jsp").forward(request, response);
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Handles VNPay return and creates order";
    }// </editor-fold>

}
