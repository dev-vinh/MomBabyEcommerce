package hcmuaf.fit.mombabyecommerce.controller.auth;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.service.OtpService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/auth/verify-otp")
public class OTPVerificationController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        String enteredOtp = request.getParameter("otp");
        String storedOtp = (String) session.getAttribute("otp");
        Long   otpExpiry = (Long)   session.getAttribute("otpExpiry");



        if (storedOtp == null || otpExpiry == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Không tìm thấy OTP trong session");
            return;
        }
        if (System.currentTimeMillis() > otpExpiry) {
            session.removeAttribute("otp");
            session.removeAttribute("otpExpiry");
            session.removeAttribute("otpSentAt");
            response.setStatus(410);
            response.getWriter().write("OTP đã hết hạn.");
            return;
        }

        if (!storedOtp.equals(enteredOtp)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Mã OTP không chính xác.");
            return;
        }

        session.setAttribute("otpVerified", true);
        session.removeAttribute("otp");
        session.removeAttribute("otpExpiry");
        session.removeAttribute("otpSentAt");

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("success");
    }
}
