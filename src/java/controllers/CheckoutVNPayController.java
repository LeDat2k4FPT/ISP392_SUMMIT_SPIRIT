package controllers;

import com.vnpay.common.Config;
import com.vnpay.common.VNPayUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import dto.UserDTO;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author gmt
 */
@WebServlet(name = "CheckoutVNPayController", urlPatterns = {"/CheckoutVNPayController"})
public class CheckoutVNPayController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Kiểm tra đăng nhập
        HttpSession session = request.getSession();
        UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
        if (loginUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Nhận dữ liệu từ form
        String amountParam = request.getParameter("amount");
        int amount = 100000;
        try {
            amount = Integer.parseInt(amountParam);
        } catch (Exception e) {
        }

        // Lưu thông tin đơn hàng tạm vào session
        Map<String, Object> pendingOrder = new HashMap<>();
        pendingOrder.put("userID", loginUser.getUserID());
        pendingOrder.put("totalAmount", amount);
        pendingOrder.put("fullName", loginUser.getFullName());
        pendingOrder.put("email", loginUser.getEmail());
        pendingOrder.put("phone", loginUser.getPhone());

        session.setAttribute("PENDING_ORDER", pendingOrder);

        // Tạo mã giao dịch tạm (UUID)
        String vnp_TxnRef = UUID.randomUUID().toString();
        session.setAttribute("PENDING_ORDER_TXNREF", vnp_TxnRef);

        // Chuẩn bị thông tin thanh toán VNPay
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TmnCode = Config.vnp_TmnCode;
        String vnp_ReturnUrl = Config.vnp_ReturnUrl;
        String vnp_IpAddress = Config.getIpAddress(request);
        String vnp_OrderType = "other";
        String vnp_Locale = "vn";
        String vnp_OrderInfo = "Thanh toán đơn hàng: " + vnp_TxnRef;
        long vnp_Amount = (long) amount * 100;

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = sdf.format(calendar.getTime());
        calendar.add(Calendar.MINUTE, 15); //Hết hạn sau 15 phút
        String vnp_ExpireDate = sdf.format(calendar.getTime());

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
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames); // Sắp xếp alphabet

        StringBuilder hashData = new StringBuilder();
        for (int i = 0; i < fieldNames.size(); i++) {
            String field = fieldNames.get(i);
            String value = vnp_Params.get(field);
            if (value != null && !value.isEmpty()) {
                hashData.append(field).append('=').append(value);
                if (i < fieldNames.size() - 1) {
                    hashData.append('&');
                }
            }
        }

        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        vnp_Params.put("vnp_SecureHash", vnp_SecureHash);

        String queryUrl = VNPayUtil.builQueryString(vnp_Params);
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;
        response.sendRedirect(paymentUrl);
    }
}
