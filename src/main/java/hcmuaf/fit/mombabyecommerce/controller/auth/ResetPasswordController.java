package hcmuaf.fit.mombabyecommerce.controller.auth;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.User;
import hcmuaf.fit.mombabyecommerce.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
@WebServlet("/auth/reset-password")
public class ResetPasswordController extends HttpServlet {
    private final AuthService authService = new AuthService(DBConnection.getJdbi());

    // ít nhất 8 ký tự, 1 ký tự viết hoa, 1 số, 1 ký tự đặc biệt
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        String email = (String) request.getSession().getAttribute("userEmail");
        Boolean otpVerified = (Boolean) request.getSession().getAttribute("otpVerified");

        // Kiểm tra email và OTP đã được xác thực chưa
        if (email == null || otpVerified == null || !otpVerified) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Phiên làm việc đã hết hạn. Vui lòng thử lại.");
            return;
        }

        if (newPassword == null || newPassword.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Vui lòng nhập mật khẩu mới.");
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Mật khẩu không khớp.");
            return;
        }
        // Validate mật khẩu mạnh
        if (!newPassword.matches(PASSWORD_PATTERN)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Mật khẩu phải có ít nhất 8 ký tự, bao gồm một ký tự viết hoa, một ký tự số và một ký tự đặc biệt.");
            return;
        }


        User user = authService.getUserByEmail(email);
        if (user != null) {
            try {
                authService.changePassword(user.getId(), null, newPassword, false);
                // Xóa session sau khi đổi mật khẩu thành công
                request.getSession().removeAttribute("otp");
                request.getSession().removeAttribute("userEmail");
                request.getSession().removeAttribute("otpVerified");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("success");
            } catch (IllegalArgumentException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(e.getMessage());
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Không tìm thấy người dùng.");
        }
    }

}

