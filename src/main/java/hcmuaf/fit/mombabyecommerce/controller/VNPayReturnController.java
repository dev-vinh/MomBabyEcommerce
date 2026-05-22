package hcmuaf.fit.mombabyecommerce.controller;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.contant.PaymentStatus;
import hcmuaf.fit.mombabyecommerce.service.OrderService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/vnpay_return")
public class VNPayReturnController extends HttpServlet {

    OrderService orderService =
            new OrderService(DBConnection.getJdbi());

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        String responseCode =
                request.getParameter("vnp_ResponseCode");

        String txnRef =
                request.getParameter("vnp_TxnRef");

        if (txnRef == null) {
            response.sendRedirect("fail");
            return;
        }

        Integer orderId = Integer.parseInt(txnRef);
// 00 là thành công
        if ("00".equals(responseCode)) {
            orderService.updatePaymentStatus(orderId, PaymentStatus.PAID);
            response.sendRedirect("success");
        } else {
            response.sendRedirect("fail");
        }
    }
}