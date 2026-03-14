package hcmuaf.fit.mombabyecommerce.controller.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.service.AuthService;
import hcmuaf.fit.mombabyecommerce.util.ResponseWrapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

@WebServlet("/change-password")
public class ChangePasswordController extends HttpServlet {
    private final AuthService authService = new AuthService(DBConnection.getJdbi());
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
            }
            String jsonString = jsonBuilder.toString();
            Map<String, String> jsonData = objectMapper.readValue(jsonString, new TypeReference<Map<String, String>>() {});

            String currentPassword = jsonData.get("currentPassword");
            String newPassword = jsonData.get("newPassword");
            String confirmPassword = jsonData.get("confirmPassword");

            HttpSession session = request.getSession();
            Integer userId = (Integer) session.getAttribute("userId");

            if (newPassword == null || newPassword.isEmpty()) {
                response.getWriter().write(objectMapper.writeValueAsString(new ResponseWrapper<>(
                        400, "error", "New password cannot be null or empty", null)));
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                response.getWriter().write(objectMapper.writeValueAsString(new ResponseWrapper<>(
                        400, "error", "New password and confirmation do not match", null)));
                return;
            }

            boolean isPasswordChanged = authService.changePassword(userId, currentPassword, newPassword, true);
            if (isPasswordChanged) {
                session.invalidate();
                response.getWriter().write(objectMapper.writeValueAsString(new ResponseWrapper<>(
                        200, "success", "Password changed successfully", null)));
            } else {
                response.getWriter().write(objectMapper.writeValueAsString(new ResponseWrapper<>(
                        400, "error", "Current password is incorrect", null)));
            }

        } catch (Exception e) {
            response.getWriter().write(objectMapper.writeValueAsString(new ResponseWrapper<>(
                    500, "error", "An error occurred: " + e.getMessage(), null)));
        }
    }
}
