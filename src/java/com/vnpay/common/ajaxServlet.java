/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vnpay.common;

import dao.OrderDAO;
import dto.OrderDTO;
import dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gmt
 */
public class ajaxServlet extends HttpServlet {

    OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
        if (loginUser == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        // Nhận dữ liệu từ form
        String amountParam = req.getParameter("amount");
        double amountDouble;
        try {
            amountDouble = Double.parseDouble(amountParam);
            if (amountDouble <= 0) {
                resp.sendRedirect("checkout.jsp");
                return;
            }
        } catch (Exception e) {
            resp.sendRedirect("checkout.jsp");
            return;
        }

        String voucherIDParam = req.getParameter("voucherID");
        Integer voucherID = null;
        if (voucherIDParam != null && !voucherIDParam.equals("null") && !voucherIDParam.isEmpty()) {
            try {
                voucherID = Integer.parseInt(voucherIDParam);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        String bankCode = req.getParameter("bankCode");

        int orderId_new = -1;
        // Tạo đơn hàng tạm và thêm vào DB trước khi chuyển sang VNPay
        OrderDTO order = new OrderDTO();
        order.setUserID(loginUser.getUserID());
        order.setOrderDate(new java.sql.Date(System.currentTimeMillis()));
        order.setStatus("Pending");
        order.setTotalAmount(amountDouble);
        order.setShipFee(30000);
        order.setVoucherID(voucherID);

        Integer tempOrderId = (Integer) session.getAttribute("TEMP_ORDER_ID");
        if (tempOrderId != null) {
            OrderDTO existingOrder = null;
            try {
                existingOrder = orderDAO.getOrderById(tempOrderId);
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(ajaxServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (existingOrder != null && "Pending".equals(existingOrder.getStatus())) {
                orderId_new = tempOrderId;
            } else {
                // Nếu đơn hàng đã được xử lý rồi, xóa khỏi session và tạo đơn mới
                session.removeAttribute("TEMP_ORDER_ID");
                try {
                    orderId_new = orderDAO.addOrder(order);
                } catch (SQLException ex) {
                    Logger.getLogger(ajaxServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                session.setAttribute("TEMP_ORDER_ID", orderId_new);
            }
        } else {
            try {
                // Chưa có thì tạo mới
                orderId_new = orderDAO.addOrder(order);
            } catch (SQLException ex) {
                Logger.getLogger(ajaxServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            session.setAttribute("TEMP_ORDER_ID", orderId_new);
        }
        if (orderId_new < 0) {
            resp.sendRedirect("error.jsp");
            return;
        }
        String vnp_TxnRef = orderId_new + "-" + System.currentTimeMillis();

        // Lưu vào session để sử dụng sau khi thanh toán thành công
        Map<String, Object> pendingOrder = new HashMap<>();
        pendingOrder.put("orderId", orderId_new);
        pendingOrder.put("userID", loginUser.getUserID());
        pendingOrder.put("totalAmount", amountDouble);
        pendingOrder.put("fullName", loginUser.getFullName());
        pendingOrder.put("email", loginUser.getEmail());
        pendingOrder.put("phone", loginUser.getPhone());
        pendingOrder.put("txnRef", vnp_TxnRef);
        pendingOrder.put("country", req.getParameter("country"));
        pendingOrder.put("address", req.getParameter("address"));
        pendingOrder.put("district", req.getParameter("district"));
        pendingOrder.put("city", req.getParameter("city"));
        pendingOrder.put("discountCode", req.getParameter("discountCode"));
        pendingOrder.put("discountPercent", req.getParameter("discountPercent"));
        pendingOrder.put("productId", req.getParameter("productId"));
        pendingOrder.put("quantity", req.getParameter("quantity"));
        pendingOrder.put("size", req.getParameter("size"));
        pendingOrder.put("color", req.getParameter("color"));
        pendingOrder.put("fromSaleOff", req.getParameter("fromSaleOff"));
        session.setAttribute("PENDING_ORDER", pendingOrder);
        session.setAttribute("TEMP_ORDER_ID", orderId_new);

        // Chuẩn bị redirect sang VNPay
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TmnCode = Config.vnp_TmnCode;
        String orderType = "other";
        long amount = (long) (amountDouble * 100);
        String vnp_IpAddr = Config.getIpAddress(req);

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = req.getParameter("language");
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (int i = 0; i < fieldNames.size(); i++) {
            String fieldName = fieldNames.get(i);
            String fieldValue = vnp_Params.get(fieldName);
            hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
            query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8.toString()))
                    .append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));

            if (i < fieldNames.size() - 1) {
                hashData.append('&');
                query.append('&');
            }
        }

        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);

        String paymentUrl = Config.vnp_PayUrl + "?" + query;
        resp.sendRedirect(paymentUrl);
    }
}
