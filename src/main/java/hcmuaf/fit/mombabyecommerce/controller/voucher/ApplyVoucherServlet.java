package hcmuaf.fit.mombabyecommerce.controller.voucher;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Voucher;
import hcmuaf.fit.mombabyecommerce.service.VoucherService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/apply-voucher")
public class ApplyVoucherServlet extends HttpServlet {
     private final VoucherService voucherService = new VoucherService(DBConnection.getJdbi());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String code = request.getParameter("code");
        System.out.println("code: " + code);
        if(code == null || code.isBlank()){
            response.getWriter().write("""
        {"success":false,
        "message":"Vui lòng nhập mã giảm giá!"} 
        """);
            return;
        }
        Voucher voucher = voucherService.findByCode(code);
        System.out.println(voucher);
        if(voucher == null){
            response.getWriter().write("""
       {
            "success": false,
            "message": "Voucher không tồn tại"
        }""");
            return;
        }
        if(!voucher.getActive()){

            response.getWriter().write("""
        {
            "success": false,
            "message": "Voucher đã bị khóa"
        }
        """);

            return;
        }

        if(voucher.getQuantity() <= 0){

            response.getWriter().write("""
        {
            "success": false,
            "message": "Voucher đã hết lượt sử dụng"
        }
        """);

            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("voucher", voucher);

        response.getWriter().write("""
{
    "success": true,
    "message": "Áp dụng voucher thành công",
    "discountPercent": %d,
    "maxDiscount": %.0f
}
""".formatted(voucher.getDiscountPercent(),
                voucher.getMaxDiscount()));
    }
}