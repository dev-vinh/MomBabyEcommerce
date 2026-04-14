package hcmuaf.fit.mombabyecommerce.controller.auth;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.User;
import hcmuaf.fit.mombabyecommerce.service.AuthService;
import hcmuaf.fit.mombabyecommerce.service.EmailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
@WebServlet("/auth/forgot-password")
public class ForgotPasswordController extends HttpServlet {
    private final AuthService authService = new AuthService(DBConnection.getJdbi());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/auth/forgotpassword.jsp").forward(request, response);
    }

    // Xử lý quên mật khẩu
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        String email = request.getParameter("email");

        // Kiểm tra email có tồn tại trong hệ thống hay không
        User user = authService.getUserByEmail(email);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Email không tồn tại trong hệ thống");
        } else {
            String otp = generateOTP();
            sendEmailWithOTP(user.getEmail(), otp);

            // Lưu OTP vào session để xác minh sau
            request.getSession().setAttribute("otp", otp);
            request.getSession().setAttribute("userEmail", user.getEmail());

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("success");

        }
    }

    private String generateOTP() {
        EmailService emailService = new EmailService();
        return emailService.generateOTP();
    }

    private void sendEmailWithOTP(String email, String otp) {
        EmailService emailService = new EmailService();
        try {
            emailService.sendEmailWithOTP(email, otp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}