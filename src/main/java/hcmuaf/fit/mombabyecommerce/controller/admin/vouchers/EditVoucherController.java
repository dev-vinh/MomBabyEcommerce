package hcmuaf.fit.mombabyecommerce.controller.admin.vouchers;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Voucher;
import hcmuaf.fit.mombabyecommerce.service.VoucherService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet(name = "EditVoucherController", value = "/admin/vouchers/edit")
public class EditVoucherController extends HttpServlet {

    VoucherService voucherService = new VoucherService(DBConnection.getJdbi());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));

        Voucher voucher = voucherService.findById(id);

        request.setAttribute("voucher", voucher);
        request.getRequestDispatcher("/admin/addVoucher.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/admin/vouchers");
            return;
        }
        Integer id = Integer.parseInt(request.getParameter("id"));
        Voucher voucher = voucherService.findById(id);

        if (voucher == null) {
            response.sendRedirect(request.getContextPath() + "/admin/vouchers");
            return;
        }
        if (id == null) {
            response.sendRedirect(request.getContextPath() + "/admin/vouchers");
            return;
        }
        voucher.setCode(request.getParameter("code"));
        voucher.setDiscountPercent(Integer.parseInt(request.getParameter("discountPercent")));
        voucher.setQuantity(Integer.parseInt(request.getParameter("quantity")));
        voucher.setMinOrderAmount(Double.parseDouble(request.getParameter("minOrderAmount")));
        voucher.setMaxDiscount(Double.parseDouble(request.getParameter("maxDiscount")));
        voucher.setDescription(request.getParameter("description"));

        LocalDate start = LocalDate.parse(request.getParameter("startDate"));
        LocalDate end = LocalDate.parse(request.getParameter("endDate"));

        voucher.setStartDate(start.atStartOfDay());
        voucher.setEndDate(end.atTime(23, 59, 59));

        voucher.setActive(request.getParameter("active") != null);

        voucherService.updateVoucher(voucher);

        response.sendRedirect(request.getContextPath() + "/admin/vouchers");
    }
}