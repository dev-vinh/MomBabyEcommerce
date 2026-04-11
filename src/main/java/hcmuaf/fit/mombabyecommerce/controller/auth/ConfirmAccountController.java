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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sessionId = request.getParameter("sessionId");

        if (sessionId != null) {
            if (authService.verifySession(request, sessionId)) {
                authService.activateUserAccount(request, sessionId);

                request.setAttribute("message", "Tài khoản của bạn đã được xác nhận. Vui lòng đăng nhập.");
                request.getRequestDispatcher("/auth/auth.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Mã xác nhận không hợp lệ hoặc đã hết hạn.");
            }
        } else {
            request.setAttribute("error", "Mã xác nhận không hợp lệ.");
        }
    }
}

