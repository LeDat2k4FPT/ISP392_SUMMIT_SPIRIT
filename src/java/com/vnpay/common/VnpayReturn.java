/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.vnpay.common;

import dao.OrderDAO;
import dao.OrderDetailDAO;
import dao.PaymentDAO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import dto.PaymentDTO;
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
    private final PaymentDAO paymentDao = new PaymentDAO();

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
                request.setAttribute("paymentResult", "invalid");
                request.getRequestDispatcher("paymentResult.jsp").forward(request, response);
                request.setAttribute("retryLink", "checkout.jsp?retry=true");
                return;
            }

            String responseCode = request.getParameter("vnp_ResponseCode");
            String fullTxnRef = request.getParameter("vnp_TxnRef");
            String transactionNo = request.getParameter("vnp_TransactionNo");
            long amount = Long.parseLong(request.getParameter("vnp_Amount")) / 100;
            String cardType = request.getParameter("vnp_CardType");

            int orderId;
            try {
                String[] parts = fullTxnRef.split("-");
                orderId = Integer.parseInt(parts[0]); // tách orderId từ txnRef
                System.out.println("Parsed orderId from vnp_TxnRef: " + orderId);
            } catch (Exception ex) {
                request.setAttribute("paymentResult", "error");
                request.getRequestDispatcher("paymentResult.jsp").forward(request, response);
                request.setAttribute("retryLink", "checkout.jsp?retry=true");
                return;
            }

            HttpSession session = request.getSession();
            Object sessionObj = session.getAttribute("PENDING_ORDER");
            if (!(sessionObj instanceof Map)) {
                request.setAttribute("paymentResult", "error");
                request.getRequestDispatcher("paymentResult.jsp").forward(request, response);
                request.setAttribute("retryLink", "checkout.jsp?retry=true");
                return;
            }
            Map<String, Object> orderSessionData = (Map<String, Object>) sessionObj;

            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setOrderID(orderId);
            paymentDTO.setPaymentMethod(cardType);
            String paymentStatus = null;
            if ("00".equals(responseCode)) {
                paymentStatus = "Paid";
            } else if ("24".equals(responseCode)) {
                paymentStatus = "Unpaid";
            } else {
                paymentStatus = "Unpaid";
            }
            paymentDTO.setPaymentStatus(paymentStatus);
            paymentDTO.setPaymentDate(new java.sql.Date(System.currentTimeMillis()));
            paymentDTO.setTransactionCode(transactionNo);
            paymentDao.addPayment(paymentDTO);

            String checkoutType = (String) orderSessionData.get("checkoutType");
            if ("00".equals(responseCode) && orderSessionData != null) {
                // Cập nhật trạng thái đơn hàng
                OrderDTO order = new OrderDTO();
                order.setStatus("Processing");
                order.setOrderID(orderId);
                order.setNote("");
                boolean updated = orderDao.updateOrderAfterPayment(order);

                if (updated) {
                    if ("BUY_NOW".equals(checkoutType)) {
                        int productId = Integer.parseInt(String.valueOf(orderSessionData.get("productId")));
                        int quantity = Integer.parseInt(String.valueOf(orderSessionData.get("quantity")));
                        String size = (String) orderSessionData.get("size");
                        String color = (String) orderSessionData.get("color");
                        boolean fromSaleOff = Boolean.parseBoolean(String.valueOf(orderSessionData.get("fromSaleOff")));
                        int attributeId = 0;
                        if ((size == null || size.isEmpty()) && (color == null || color.isEmpty())) {
                            attributeId = detailDao.getAttributeIdByProduct(productId);
                        } else if (size == null || size.isEmpty()) {
                            attributeId = detailDao.getAttributeIdByProductColor(productId, color);
                        } else if (color == null || color.isEmpty()) {
                            attributeId = detailDao.getAttributeIdByProductSize(productId, size);
                        } else {
                            attributeId = detailDao.getAttributeIdByProductSizeColor(productId, color, size);
                        }
                        double unitPrice = detailDao.getUnitPriceByAttributeId(attributeId);
                        OrderDetailDTO detail = new OrderDetailDTO(orderId, attributeId, quantity, unitPrice);
                        detailDao.addOrderDetail(detail);
                        detailDao.updateProductVariantQuantity(attributeId, quantity);
                    } else if ("CART".equals(checkoutType)) {
                        // Xử lý giỏ hàng
                        Object cartObj = session.getAttribute("CART");
                        if (cartObj != null && cartObj instanceof dto.CartDTO) {
                            dto.CartDTO cart = (dto.CartDTO) cartObj;
                            for (dto.CartItemDTO item : cart.getCartItems()) {
                                dto.ProductDTO product = item.getProduct();
                                int attributeId = detailDao.getAttributeIdByProductSizeColor(
                                        product.getProductID(), product.getColor(), product.getSize());
                                double unitPrice = detailDao.getUnitPriceByAttributeId(attributeId);
                                OrderDetailDTO detail = new OrderDetailDTO(orderId, attributeId, item.getQuantity(), unitPrice);
                                detailDao.addOrderDetail(detail);
                                detailDao.updateProductVariantQuantity(attributeId, item.getQuantity());
                            }
                            // Sau khi xử lý xong, có thể xóa giỏ hàng
                            session.removeAttribute("CART");
                        }
                    }

                    // Xóa session tạm
                    session.removeAttribute("PENDING_ORDER");
                    session.removeAttribute("TEMP_ORDER_ID");
                    request.setAttribute("paymentResult", "success");
                    request.setAttribute("transactionId", request.getParameter("vnp_TransactionNo"));
                    request.setAttribute("amount", String.format("%,.0f", (double) amount));
                    request.setAttribute("orderInfo", request.getParameter("vnp_OrderInfo"));
                } else {
                    request.setAttribute("paymentResult", "error");
                    request.setAttribute("retryLink", "checkout.jsp?retry=true");
                }
            } else {
                // Giao dịch thất bại hoặc không có session
                OrderDTO order = new OrderDTO();
                order.setOrderID(orderId);
                order.setStatus("Failed");
                order.setNote("Order payment failed");
                boolean updated = orderDao.updateOrderStatusAndNote(order);
                if (updated) {
                    request.setAttribute("paymentResult", "failed");
                    request.setAttribute("retryLink", "checkout.jsp?retry=true");
                }
            }
        } catch (Exception e) {
            Logger.getLogger(VnpayReturn.class.getName()).log(Level.SEVERE, null, e);
            request.setAttribute("paymentResult", "error");
            request.setAttribute("retryLink", "checkout.jsp?retry=true");
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
