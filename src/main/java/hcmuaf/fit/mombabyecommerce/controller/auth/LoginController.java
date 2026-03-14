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
    private static final String SECRET_KEY =  EnvConfig.get("RECAPTCHA_SECRET_KEY");
    private final UserService userService = new UserService(DBConnection.getJdbi());

    private boolean verifyRecaptcha(String token) {
        try {
            URL url = new URL("https://www.google.com/recaptcha/api/siteverify");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            String postData = "secret=" + SECRET_KEY + "&response=" + token;
            conn.getOutputStream().write(postData.getBytes(StandardCharsets.UTF_8));

            try (InputStream input = conn.getInputStream();
                 InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
                 JsonReader jsonReader = new JsonReader(reader)) {

                JsonObject jsonObject = com.google.gson.JsonParser.parseReader(jsonReader).getAsJsonObject();
                return jsonObject.get("success").getAsBoolean();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try{
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
            }
            String jsonString = jsonBuilder.toString();

            Map<String, String> jsonData = objectMapper.readValue(jsonString, Map.class);
            String email = jsonData.get("email");
            String password = jsonData.get("password");
            String recaptchaToken = jsonData.get("recaptcha");
// input
            if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
                ResponseWrapper<Object> responseWrapper = new ResponseWrapper<>(
                        400, "error", "Email và mật khẩu không được để trống", null);
                response.getWriter().write(objectMapper.writeValueAsString(responseWrapper));
                return;
            }
            if (recaptchaToken == null || !verifyRecaptcha(recaptchaToken)) {
                ResponseWrapper<Object> responseWrapper = new ResponseWrapper<>(
                        400, "error", "Xác thực reCAPTCHA không thành công.", null);
                response.getWriter().write(objectMapper.writeValueAsString(responseWrapper));
                return;
            }
            User user = authService.login(email, password);
            if (user != null) {
                // Lấy danh sách permissions cho role của user
                List<Permission> permissions = authService.getPermissionsByRoleId(user.getRole().getId());
                List<String> permissionTypes = permissions.stream()
                        .map(permission -> permission.getType().toString())
                        .collect(Collectors.toList());
                HttpSession session = request.getSession();
                session.setAttribute("userId", user.getId());
                session.setAttribute("roleType", user.getRole().getRoleType());
                session.setAttribute("roleId", user.getRole().getId());
                session.setAttribute("permissions", permissionTypes);

                if (user.getNeedRefresh()){
                    userService.updateNeedRefresh(user.getId(), false);
                }
                Map<String, Object> userData = new HashMap<>();
                userData.put("id", String.valueOf(user.getId()));
                userData.put("fullName", user.getFullName());
                userData.put("displayName", user.getDisplayName());
                userData.put("email", user.getEmail());
                userData.put("roleType", user.getRole().getRoleType().toString());
                userData.put("roleId", String.valueOf(user.getRole().getId()));
                userData.put("status", user.getStatus());
                userData.put("sessionId", session.getId());
                userData.put("permissions", permissionTypes);

                ResponseWrapper<Map<String, Object>> responseWrapper = new ResponseWrapper<>(
                        200, "success", "Đăng nhập thành công", userData);
                response.getWriter().write(objectMapper.writeValueAsString(responseWrapper));


            } else {
                ResponseWrapper<Object> responseWrapper = new ResponseWrapper<>(
                        401, "error", "Có lỗi khi đăng nhập! Vui lòng kiểm tra lại.", null);
                response.getWriter().write(objectMapper.writeValueAsString(responseWrapper));
            }
        } catch (RuntimeException e) {
            ResponseWrapper<Object> responseWrapper = new ResponseWrapper<>(
                    401, "error", e.getMessage(), null);
            response.getWriter().write(objectMapper.writeValueAsString(responseWrapper));
        } catch (Exception e) {
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

