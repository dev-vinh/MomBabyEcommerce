package hcmuaf.fit.mombabyecommerce.controller.admin.vouchers;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.service.VoucherService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/admin/vouchers/delete")
public class DeleteVoucherController extends HttpServlet {

    VoucherService voucherService = new VoucherService(DBConnection.getJdbi());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Integer id = Integer.parseInt(request.getParameter("id"));

        voucherService.disableVoucher(id);

        response.sendRedirect(request.getContextPath() + "/admin/vouchers");
    }
}