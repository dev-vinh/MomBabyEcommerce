package hcmuaf.fit.mombabyecommerce.controller.auth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;
import hcmuaf.fit.mombabyecommerce.config.ConfigLoader;
import hcmuaf.fit.mombabyecommerce.config.EnvConfig;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.contant.ERole;
import hcmuaf.fit.mombabyecommerce.model.Permission;
import hcmuaf.fit.mombabyecommerce.model.User;
import hcmuaf.fit.mombabyecommerce.service.AuthService;
import hcmuaf.fit.mombabyecommerce.service.EmailService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@WebServlet("/google-callback")
public class GoogleCallbackServlet extends HttpServlet {
    private String clientId ;
    private String clientSecret ;
    private String redirectUri;
    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo";
    private AuthService authService;
    private EmailService emailService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.clientId = EnvConfig.get("GOOGLE_CLIENT_ID");
        this.clientSecret = EnvConfig.get("GOOGLE_CLIENT_SECRET");
        String hostProduct = ConfigLoader.get("host.dev");
        this.redirectUri = hostProduct + "/google-callback";
        if (clientId == null || clientSecret == null) {
            throw new ServletException("Google OAuth credentials not found in application.properties");
        }
        authService = new AuthService(DBConnection.getJdbi());
        emailService = new EmailService();

        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) -> {
                    if (src == null) {
                        return null;
                    }
                    return context.serialize(src.format(DateTimeFormatter.ISO_LOCAL_DATE));
                })
                .create();


    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try {
            String code = request.getParameter("code");
            String state = request.getParameter("state");
            String sessionState = (String) request.getSession().getAttribute("google_state");

            if (state == null || !state.equals(sessionState)) {
                response.sendRedirect(request.getContextPath() + "/auth/auth.jsp?error=Invalid%20state%20parameter");
                return;
            }

            String postData = String.format(
                    "client_id=%s&client_secret=%s&redirect_uri=%s&code=%s&grant_type=authorization_code",
                    URLEncoder.encode(clientId, "UTF-8"),
                    URLEncoder.encode(clientSecret, "UTF-8"),
                    URLEncoder.encode(redirectUri, "UTF-8"),
                    URLEncoder.encode(code, "UTF-8")
            );

            String responseToken = makePostRequest(TOKEN_URL, postData);
            JsonObject jobj = gson.fromJson(responseToken, JsonObject.class);
            String accessToken = jobj.get("access_token").getAsString();

            String userInfoResponse = makeGetRequest(USER_INFO_URL, accessToken);
            JsonObject userInfo = gson.fromJson(userInfoResponse, JsonObject.class);
            String email = userInfo.get("email").getAsString();
            String name = userInfo.get("name").getAsString();

            User user = authService.getUserByEmail(email);
            user = authService.enrichUserWithRole(user);

            if (user == null) {
                String randomPass = UUID.randomUUID().toString();
                boolean success = authService.registerWithGoogleActive(name, name, email, randomPass);
                if (success) {
                    user = authService.getUserByEmail(email);
                    user = authService.enrichUserWithRole(user);
                    System.out.println("Auto-registered Google user: " + email);
                } else {
                    throw new Exception("Không thể tự động đăng ký tài khoản Google.");
                }
            }

            if ("BANNED".equals(user.getStatus()) || "DEACTIVE".equals(user.getStatus())) {
                response.sendRedirect(request.getContextPath() + "/login?error=Account%20is%20disabled");
                return;
            }
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            try {
                emailService.sendLoginNotification(user.getEmail(), user.getFullName());
            } catch (Exception e) {
                System.err.println("Email notification failed: " + e.getMessage());
            }
            if (user.getRole() == null) {
                throw new RuntimeException("User chưa có role");
            }
            List<Permission> permissions = authService.getPermissionsByRoleId(user.getRole().getId());
            List<String> permissionTypes = permissions.stream()
                    .map(p -> p.getType().toString())
                    .collect(Collectors.toList());
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("roleType", user.getRole().getRoleType().name());
            session.setAttribute("permissions", permissionTypes);

            String roleType = user.getRole().getRoleType().name();
            String redirectUrl;
            if (roleType.equals(ERole.SUPER_ADMIN.name())) {
                redirectUrl = request.getContextPath() + "/admin/dashboard";
            } else {
                redirectUrl = request.getContextPath() + "/home";
            }

            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/login?error=Google%20Login%20Failed");
        }
    }
    
    private String makePostRequest(String url, String postData) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        try (OutputStream os = conn.getOutputStream()) {
            os.write(postData.getBytes(StandardCharsets.UTF_8));
        }
        return readResponse(conn);
    }

    private String makeGetRequest(String url, String accessToken) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        return readResponse(conn);
    }

    private String readResponse(HttpURLConnection conn) throws IOException {
        int responseCode = conn.getResponseCode();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                (responseCode == 200) ? conn.getInputStream() : conn.getErrorStream(), StandardCharsets.UTF_8))) {
            return br.lines().collect(Collectors.joining());
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}