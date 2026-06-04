package hcmuaf.fit.mombabyecommerce.controller.admin.vouchers;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Voucher;
import hcmuaf.fit.mombabyecommerce.service.VoucherService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/vouchers")
public class VoucherController extends HttpServlet {
    VoucherService voucherService = new VoucherService(DBConnection.getJdbi());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Voucher> vouchers = voucherService.getAllVouchers();

        long activeVoucher = vouchers.stream().filter(v -> "Active".equals(v.getStatus())).count();

        long expiredVoucher = vouchers.stream().filter(v -> "Expired".equals(v.getStatus())).count();

        long scheduledVoucher = vouchers.stream().filter(v -> "Scheduled".equals(v.getStatus())).count();

        request.setAttribute("activeVoucher", activeVoucher);
        request.setAttribute("expiredVoucher", expiredVoucher);
        request.setAttribute("scheduledVoucher", scheduledVoucher);
        request.setAttribute("vouchers", vouchers);
        request.setAttribute("totalVoucher", voucherService.countAll());
        request.getRequestDispatcher("/admin/vouchers.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}