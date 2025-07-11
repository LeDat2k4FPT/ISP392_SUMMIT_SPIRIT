package admin;

import dao.VoucherDAO;
import dto.VoucherDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ManageVoucherController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            VoucherDAO dao = new VoucherDAO();
            List<VoucherDTO> vouchers = dao.getActiveVouchers();
            request.setAttribute("vouchers", vouchers);
            request.getRequestDispatcher("admin/manageVoucher.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        VoucherDAO dao = new VoucherDAO();
        try {
            if ("add".equals(action)) {
                VoucherDTO v = new VoucherDTO(
                    0,
                    request.getParameter("voucherCode"),
                    Double.parseDouble(request.getParameter("discountValue")),
                    java.sql.Date.valueOf(request.getParameter("expiryDate")),
                    request.getParameter("status")
                );
                dao.addVoucher(v);
            } else if ("update".equals(action)) {
                int voucherID = Integer.parseInt(request.getParameter("voucherID"));
                String status = request.getParameter("status");
                dao.updateVoucherStatus(voucherID, status);
            } else if ("delete".equals(action)) {
                int voucherID = Integer.parseInt(request.getParameter("voucherID"));
                dao.updateVoucherStatus(voucherID, "Inactive");
                response.sendRedirect("ManageVoucherController?msg=Voucher+deleted+successfully&type=success");
                return;
            }
            response.sendRedirect("ManageVoucherController");
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }
} 