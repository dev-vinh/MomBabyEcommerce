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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

@WebServlet("/register")
public class RegisterController extends HttpServlet {
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

            String fullName = jsonData.get("fullName");
            String displayName = jsonData.get("displayName");
            String email = jsonData.get("email");
            String inputPassword = jsonData.get("password");
            String confirmPassword = jsonData.get("confirmPassword");

            if (inputPassword == null || inputPassword.isEmpty()) {
                ResponseWrapper<Object> responseWrapper = new ResponseWrapper<>(400, "error", "Password cannot be null or empty", null);
                response.getWriter().write(objectMapper.writeValueAsString(responseWrapper));
                return;
            }

            if (!inputPassword.equals(confirmPassword)) {
                ResponseWrapper<Object> responseWrapper = new ResponseWrapper<>(400, "error", "Passwords do not match", null);
                response.getWriter().write(objectMapper.writeValueAsString(responseWrapper));
                return;
            }
            if (authService.register(fullName, displayName, email, inputPassword)) {
                String sessionId = request.getSession().getId();
                authService.saveSessionId(request, email, sessionId);
                Map<String, String> userData = Map.of(
                        "fullName", fullName,
                        "displayName", displayName,
                        "email", email
                );

                ResponseWrapper<Map<String, String>> responseWrapper = new ResponseWrapper<>(201, "success", "Registration successful. Please check your email to confirm your account.", userData);
                response.getWriter().write(objectMapper.writeValueAsString(responseWrapper));
            } else {
                ResponseWrapper<Object> responseWrapper = new ResponseWrapper<>(409, "error", "Email already exists", null);
                response.getWriter().write(objectMapper.writeValueAsString(responseWrapper));
            }

        } catch (Exception e) {
            ResponseWrapper<Object> responseWrapper = new ResponseWrapper<>(500, "error", "An error occurred: " + e.getMessage(), null);
            response.getWriter().write(objectMapper.writeValueAsString(responseWrapper));
        }
    }
}
