package hcmuaf.fit.mombabyecommerce.controller.admin.vouchers;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Voucher;
import hcmuaf.fit.mombabyecommerce.service.VoucherService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.jdbi.v3.core.Jdbi;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@WebServlet("/admin/vouchers/add")
public class AddVoucherController extends HttpServlet {
private VoucherService voucherService = new VoucherService(DBConnection.getJdbi());
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/admin/addVoucher.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//lấy dữ liệu
        String code = request.getParameter("code");
        Integer discountPercent = Integer.parseInt(request.getParameter("discountPercent"));

        Integer quantity = Integer.parseInt(request.getParameter("quantity"));

        Double minOrderAmount = Double.parseDouble(request.getParameter("minOrderAmount"));

        Double maxDiscount = Double.parseDouble(request.getParameter("maxDiscount"));

        String startDateStr = request.getParameter("startDate");
        String description = request.getParameter("description");
        String endDateStr = request.getParameter("endDate");

        LocalDate startDate = LocalDate.parse(startDateStr);

        LocalDate endDate = LocalDate.parse(endDateStr);

        LocalDateTime start = startDate.atStartOfDay();

        LocalDateTime end = endDate.atTime(23, 59, 59);

        boolean active = request.getParameter("active") != null;
// tạo voucher
        Voucher voucher = new Voucher();

        voucher.setCode(code);

        voucher.setDiscountPercent(discountPercent);

        voucher.setQuantity(quantity);

        voucher.setMinOrderAmount(minOrderAmount);

        voucher.setMaxDiscount(maxDiscount);

        voucher.setStartDate(start);

        voucher.setEndDate(end);
        voucher.setDescription(description);
        voucher.setActive(active);

        voucherService.inserVoucher(voucher);

        response.sendRedirect(request.getContextPath() + "/admin/vouchers");
    }

}