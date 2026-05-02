package hcmuaf.fit.mombabyecommerce.controller.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.service.AuthService;
import hcmuaf.fit.mombabyecommerce.service.EmailService;
import hcmuaf.fit.mombabyecommerce.util.ResponseWrapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

@WebServlet("/register")
public class RegisterController extends HttpServlet {
    private final AuthService authService = new AuthService(DBConnection.getJdbi());
    private final EmailService emailService = new EmailService(); // Khởi tạo EmailService

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Đọc nội dung JSON từ body request
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
            }
            String jsonString = jsonBuilder.toString();

            // Parse JSON để lấy dữ liệu
            Map<String, String> jsonData = objectMapper.readValue(jsonString, new TypeReference<Map<String, String>>() {
            });

            String fullName = jsonData.get("fullName");
            String displayName = jsonData.get("displayName");
            String email = jsonData.get("email");
            String inputPassword = jsonData.get("password");
            String confirmPassword = jsonData.get("confirmPassword");

            if (fullName != null) fullName = fullName.trim();
            if (displayName != null) displayName = displayName.trim();
       
            if (fullName == null || fullName.isEmpty()) {
                returnError(response, objectMapper, "Tên đầy đủ không được để trống");
                return;
            }

            if (fullName.length() > 50) {
                returnError(response, objectMapper, "Tên đầy đủ tối đa 50 ký tự");
                return;
            }

            if (displayName == null || displayName.isEmpty()) {
                returnError(response, objectMapper, "Tên hiển thị không được để trống");
                return;
            }

            if (displayName.length() > 20) {
                returnError(response, objectMapper, "Tên hiển thị tối đa 20 ký tự");
                return;
            }
            // Kiểm tra mật khẩu
            if (inputPassword == null || inputPassword.isEmpty()) {
                ResponseWrapper<Object> responseWrapper = new ResponseWrapper<>(400, "error",
                        "Password cannot be null or empty", null);
                response.getWriter().write(objectMapper.writeValueAsString(responseWrapper));
                return;
            }

            if (!inputPassword.equals(confirmPassword)) {
                ResponseWrapper<Object> responseWrapper = new ResponseWrapper<>(400, "error", "Passwords do not match",
                        null);
                response.getWriter().write(objectMapper.writeValueAsString(responseWrapper));
                return;
            }

            // Đăng ký người dùng
            String confirmationToken = authService.register(fullName, displayName, email, inputPassword);
            if (confirmationToken != null) {
                // Tạo sessionId cho người dùng mới đăng ký
                String sessionId = request.getSession().getId();

                // Lưu thông tin session vào cơ sở dữ liệu nếu cần
                authService.saveSessionId(request, email, sessionId);

                // Gửi email xác nhận
                String contextPath = request.getContextPath();
                String message = "Registration successful. Please check your email to confirm your account.";
                try {
                    emailService.sendConfirmationEmail(email, confirmationToken, contextPath);
                } catch (Exception e) {
                    e.printStackTrace();
                    message = "Registration successful, but failed to send confirmation email. Please contact support.";
                }

                // Chuẩn bị thông tin người dùng để trả về
                Map<String, String> userData = Map.of(
                        "fullName", fullName,
                        "displayName", displayName,
                        "email", email);

                ResponseWrapper<Map<String, String>> responseWrapper = new ResponseWrapper<>(201, "success", message,
                        userData);
                response.getWriter().write(objectMapper.writeValueAsString(responseWrapper));
            } else {
                response.setStatus(409);
                ResponseWrapper<Object> responseWrapper = new ResponseWrapper<>(409, "error", "Địa chỉ email này đã được đăng ký",
                        null);
                response.getWriter().write(objectMapper.writeValueAsString(responseWrapper));
            }

        } catch (Exception e) {
            ResponseWrapper<Object> responseWrapper = new ResponseWrapper<>(500, "error",
                    "An error occurred: " + e.getMessage(), null);
            response.getWriter().write(objectMapper.writeValueAsString(responseWrapper));
        }
    }

    private void returnError(HttpServletResponse response, ObjectMapper mapper, String message) throws IOException {
        response.setStatus(400);
        ResponseWrapper<Object> res = new ResponseWrapper<>(400, "error", message, null);
        response.getWriter().write(mapper.writeValueAsString(res));
    }
}
