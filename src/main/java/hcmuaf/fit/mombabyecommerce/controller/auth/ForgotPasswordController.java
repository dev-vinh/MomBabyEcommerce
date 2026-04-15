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
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Locale;

@WebServlet("/auth/forgot-password")
public class ForgotPasswordController extends HttpServlet {
    private final AuthService authService = new AuthService(DBConnection.getJdbi());
    private final EmailService emailService = new EmailService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("backToEmail".equals(action)) {
            request.getSession().removeAttribute("userEmail");
            request.getSession().removeAttribute("otp");
            request.getSession().removeAttribute("otpExpiry");
            request.getSession().removeAttribute("otpVerified");
        } else if ("backToOtp".equals(action)) {
            request.getSession().removeAttribute("otpVerified");
        }

        request.getRequestDispatcher("/auth/forgotpassword.jsp").forward(request, response);
    }

    // Xử lý quên mật khẩu
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");


        String email = request.getParameter("email");
        if(email == null || email.trim().isEmpty()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Vui lòng nhập email");
            return;
        }

        email = email.trim().toLowerCase();

        HttpSession session = request.getSession();

        Long lastSentAt = (Long) session.getAttribute("otpSentAt");
        if (lastSentAt != null) {
            long elapsed = System.currentTimeMillis() - lastSentAt;
            if (elapsed < 60_000L) {
                long remaining = (60_000L - elapsed) / 1000;
                response.setStatus(429); // Too Many Requests
                response.getWriter().write("Vui lòng chờ " + remaining + " giây trước khi gửi lại.");
                return;
            }
        }
        // Kiểm tra email có tồn tại trong hệ thống hay không
        User user = authService.getUserByEmail(email);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Email không tồn tại trong hệ thống");
            return;
        } else {
            try{
                String otp = generateOTP();
                long now = System.currentTimeMillis();
                emailService.sendEmailWithOTP(user.getEmail(), otp);


                session.setAttribute("otp", otp);
                session.setAttribute("otpSentAt", now);
                session.setAttribute("otpExpiry", now + 60_000L);
                session.setAttribute("userEmail", user.getEmail());
                session.removeAttribute("otpVerified");

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("success");

            }catch (Exception e){
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Không thể gửi email. Vui lòng thử lại.");
            }

        }
    }

    private String generateOTP() {
        EmailService emailService = new EmailService();
        return emailService.generateOTP();
    }
}