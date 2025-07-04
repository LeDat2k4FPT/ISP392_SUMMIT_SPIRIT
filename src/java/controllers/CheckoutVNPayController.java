package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import dto.OrderDTO;
import dao.OrderDAO;
import dto.UserDTO;

/**
 *
 * @author gmt
 */
@WebServlet(name = "CheckoutVNPayController", urlPatterns = {"/CheckoutVNPayController"})
public class CheckoutVNPayController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Kiểm tra đăng nhập
        jakarta.servlet.http.HttpSession session = request.getSession();
        UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
        if (loginUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Nhận dữ liệu từ form
        String amountParam = request.getParameter("amount");
        int amount;
        if (amountParam != null) {
            try {
                amount = Integer.parseInt(amountParam);
            } catch (NumberFormatException e) {
                amount = 100000; // giá trị mặc định nếu lỗi
            }
        } else {
            amount = 100000; // giá trị mặc định nếu không có param
        }
        String productName = request.getParameter("productName");
        String size = request.getParameter("size");
        String color = request.getParameter("color");

        // Lưu đơn hàng vào DB
        int orderID = -1;
        try {
            OrderDTO order = new OrderDTO();
            order.setUserID(loginUser.getUserID());
            order.setTotalAmount(amount);
            order.setStatus("Pending");
            order.setFullName(loginUser.getFullName());
            order.setEmail(loginUser.getEmail());
            order.setPhoneNumber(loginUser.getPhone());
            orderID = new OrderDAO().insertOrder(order);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.getWriter().write("Lỗi: Không tạo được đơn hàng.");
            return;
        }
        if (orderID < 1) {
            response.getWriter().write("Lỗi: Không tạo được đơn hàng.");
            return;
        }

        // Chuẩn bị thông tin thanh toán VNPay
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TmnCode = vnpay.Config.vnp_TmnCode;
        String vnp_HashSecret = vnpay.Config.secretKey;
        String vnp_ReturnUrl = vnpay.Config.vnp_ReturnUrl;
        String vnp_IpAddress = vnpay.Config.getIpAddress(request);
        String vnp_TxnRef = String.valueOf(orderID); // Sử dụng orderID làm mã giao dịch
        String vnp_OrderType = "other";
        String vnp_Locale = "vn";
        String vnp_OrderInfo = "Thanh toán đơn hàng: " + productName + " (Size: " + size + ", Màu: " + color + ")";
        long vnp_Amount = (long) amount * 100;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(vnp_Amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", vnp_OrderType);
        vnp_Params.put("vnp_Locale", vnp_Locale);
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddress", vnp_IpAddress);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = sdf.format(calendar.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (int i = 0; i < fieldNames.size(); i++) {
            String fieldName = fieldNames.get(i);
            String value = vnp_Params.get(fieldName);
            if (value != null && !value.isEmpty()) {
                hashData.append(fieldName).append('=').append(value);
                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8.toString()))
                        .append('=')
                        .append(URLEncoder.encode(value, StandardCharsets.UTF_8.toString()));
                if (i < fieldNames.size() - 1) {
                    hashData.append('&');
                    query.append('&');
                }
            }
        }
        String vnp_SecureHash = vnpay.Config.hmacSHA512(vnp_HashSecret, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);
        String paymentUrl = vnpay.Config.vnp_PayUrl + "?" + query.toString();
        response.sendRedirect(paymentUrl);
    }
}
