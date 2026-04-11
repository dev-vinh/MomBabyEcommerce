package hcmuaf.fit.mombabyecommerce.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import hcmuaf.fit.mombabyecommerce.config.EnvConfig;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Permission;
import hcmuaf.fit.mombabyecommerce.model.User;
import hcmuaf.fit.mombabyecommerce.service.AuthService;
import hcmuaf.fit.mombabyecommerce.service.UserService;
import hcmuaf.fit.mombabyecommerce.util.ResponseWrapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/login")
public class LoginController extends HttpServlet {

    private final AuthService authService = new AuthService(DBConnection.getJdbi());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            Map<String, String> jsonData = objectMapper.readValue(jsonString, Map.class);
            String email = jsonData.get("email");
            String password = jsonData.get("password");

            // Kiểm tra thông tin đầu vào
            if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
                ResponseWrapper<Object> responseWrapper = new ResponseWrapper<>(
                        400, "error", "Email và mật khẩu không được để trống", null);
                response.getWriter().write(objectMapper.writeValueAsString(responseWrapper));
                return;
            }

            // Xử lý đăng nhập
            User user = authService.login(email, password);
            if (user != null) {
                // Lưu thông tin người dùng vào session
                HttpSession session = request.getSession();
                session.setAttribute("userId", user.getId());
                session.setAttribute("role", user.getRole()); // Lưu thông tin role (nếu có)

                // Trả về thông tin người dùng
                Map<String, String> userData = Map.of(
                        "id", String.valueOf(user.getId()),
                        "fullName", user.getFullName(),
                        "displayName", user.getDisplayName(),
                        "email", user.getEmail(),
                        "role", user.getRole(),
                        "sessionId", session.getId()
                );


                ResponseWrapper<Map<String, String>> responseWrapper = new ResponseWrapper<>(
                        200, "success", "Đăng nhập thành công", userData);
                response.getWriter().write(objectMapper.writeValueAsString(responseWrapper));
            } else {
                // Trả về lỗi nếu thông tin đăng nhập không hợp lệ
                ResponseWrapper<Object> responseWrapper = new ResponseWrapper<>(
                        401, "error", "Email hoặc mật khẩu không chính xác", null);
                response.getWriter().write(objectMapper.writeValueAsString(responseWrapper));
            }
        } catch (Exception e) {
            // Xử lý lỗi hệ thống
            ResponseWrapper<Object> responseWrapper = new ResponseWrapper<>(
                    500, "error", "Đã xảy ra lỗi: " + e.getMessage(), null);
            response.getWriter().write(objectMapper.writeValueAsString(responseWrapper));
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/auth/auth.jsp").forward(request, response);
    }

}