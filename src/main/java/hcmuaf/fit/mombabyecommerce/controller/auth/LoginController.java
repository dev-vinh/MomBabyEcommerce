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
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            Map<String, String> jsonData = objectMapper.readValue(request.getInputStream(), Map.class);
            String email = jsonData.get("email");
            String password = jsonData.get("password");
            String recaptchaToken = jsonData.get("recaptcha");

            if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
                ResponseWrapper<Object> responseWrapper = new ResponseWrapper<>(
                        400, "error", "Email và mật khẩu không được để trống", null);
                response.getWriter().write(objectMapper.writeValueAsString(responseWrapper));
                return;
            }
            User user = authService.login(email, password);
            if (user != null) {
                List<Permission> permissions = authService.getPermissionsByRoleId(user.getRole().getId());
                List<String> permissionTypes = permissions.stream()
                        .map(p -> p.getType().toString())
                        .collect(Collectors.toList());

                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getId());
                session.setAttribute("roleType", user.getRole().getRoleType());
                session.setAttribute("permissions", permissionTypes);

                if (Boolean.TRUE.equals(user.getNeedRefresh())){
                    userService.updateNeedRefresh(user.getId(), false);
                }

                Map<String, Object> userData = new HashMap<>();
                userData.put("id", user.getId());
                userData.put("fullName", user.getFullName());
                userData.put("displayName", user.getDisplayName());
                userData.put("email", user.getEmail());
                userData.put("roleType", user.getRole().getRoleType());
                userData.put("status", user.getStatus());
                userData.put("permissions", permissionTypes);
                userData.put("sessionId", session.getId());

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(objectMapper.writeValueAsString(
                        new ResponseWrapper<>(200, "success", "Đăng nhập thành công", userData)));

            } else {
                sendError(response, objectMapper, 401, "Email hoặc mật khẩu không chính xác.");
            }
        } catch (RuntimeException e) {
            sendError(response, objectMapper, 401, e.getMessage());
        } catch (Exception e) {
            sendError(response, objectMapper, 500, "Lỗi hệ thống: " + e.getMessage());
        }
    }
    private void sendError(HttpServletResponse response, ObjectMapper mapper, int status, String message) throws IOException {
        response.setStatus(status);
        response.getWriter().write(mapper.writeValueAsString(new ResponseWrapper<>(status, "error", message, null)));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/auth/auth.jsp").forward(request, response);
    }

}