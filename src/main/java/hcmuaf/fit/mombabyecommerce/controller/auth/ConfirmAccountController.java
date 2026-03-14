package hcmuaf.fit.mombabyecommerce.controller.auth;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/confirm")
public class ConfirmAccountController extends HttpServlet {
    private final AuthService authService = new AuthService(DBConnection.getJdbi());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");
        if (token == null || token.isEmpty()) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<script>alert('Token không hợp lệ'); window.location.href='login';</script>");
            return;
        }

        try {
            boolean success = authService.confirmAccount(token);
            response.setContentType("text/html;charset=UTF-8");
            if (success) {
                response.getWriter().write("<script>alert('Chào mừng bạn! Tài khoản của bạn đã được xác nhận thành công. Vui lòng đăng nhập.'); window.location.href='login';</script>");
            } else {
                response.getWriter().write("<script>alert('Token không hợp lệ hoặc tài khoản đã được xác nhận'); window.location.href='login';</script>");
            }
        } catch (Exception e) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<script>alert('Đã xảy ra lỗi: " + e.getMessage() + "'); window.location.href='login';</script>");
        }
    }
}
