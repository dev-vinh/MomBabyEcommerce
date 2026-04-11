package hcmuaf.fit.mombabyecommerce.controller.auth;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.service.OtpService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/auth/verify-otp")
public class OTPVerificationController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        String enteredOtp = request.getParameter("otp");
        String sessionOtp = (String) request.getSession().getAttribute("otp");

        if (enteredOtp != null && enteredOtp.equals(sessionOtp)) {
            request.getSession().setAttribute("otpVerified", true);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("success");

        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Mã OTP không chính xác");

        }
    }
}
