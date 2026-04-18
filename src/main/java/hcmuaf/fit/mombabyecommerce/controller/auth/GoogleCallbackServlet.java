package hcmuaf.fit.mombabyecommerce.controller.auth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;
import hcmuaf.fit.mombabyecommerce.config.ConfigLoader;
import hcmuaf.fit.mombabyecommerce.config.EnvConfig;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
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
    private String makePostRequest(String url, String postData) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = postData.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                StringBuilder errorResponse = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    errorResponse.append(line);
                }
                throw new IOException("HTTP error code: " + responseCode + ", Response: " + errorResponse.toString());
            }
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        return response.toString();
    }

    private String makeGetRequest(String url, String accessToken) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                StringBuilder errorResponse = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    errorResponse.append(line);
                }
                throw new IOException("HTTP error code: " + responseCode + ", Response: " + errorResponse.toString());
            }
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        return response.toString();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String code = request.getParameter("code");
            String state = request.getParameter("state");
            String sessionState = (String) request.getSession().getAttribute("google_state");
            String googleAction = (String) request.getSession().getAttribute("google_action");

            if (state == null || !state.equals(sessionState)) {
                response.sendRedirect(request.getContextPath() + "/auth/auth.jsp?error=Invalid state parameter");
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

            // Get user info using the new endpoint
            String userInfoResponse = makeGetRequest(USER_INFO_URL, accessToken);
            JsonObject userInfo = gson.fromJson(userInfoResponse, JsonObject.class);
            String email = userInfo.get("email").getAsString();
            String name = userInfo.get("name").getAsString();

            User user = authService.getUserByEmail(email);

            if (user == null) {
                if ("register".equals(googleAction)) {
                    String randomPassword = UUID.randomUUID().toString();

                    boolean registrationSuccess = authService.registerWithGoogleActive(name, name, email, randomPassword);
                    if (registrationSuccess) {
                        user = authService.getUserByEmail(email);
                        EmailService.sendRegistrationEmail(email, randomPassword);
                        if (user != null) {
                            HttpSession session = request.getSession();
                            session.setAttribute("user", user);
                            List<Permission> permissions = authService.getPermissionsByRoleId(user.getRole().getId());
                            List<String> permissionTypes = permissions.stream()
                                    .map(permission -> permission.getType().toString())
                                    .collect(Collectors.toList());

                            // Create a script to store user info in sessionStorage and redirect with success alert
                            String script = "<script>" +
                                    "sessionStorage.setItem('roleType', '" + user.getRole().getRoleType() + "');" +
                                    "sessionStorage.setItem('sessionId', '" + session.getId() + "');" +
                                    "sessionStorage.setItem('userId', '" + user.getId() + "');" +
                                    "sessionStorage.setItem('roleId', '" + user.getRole().getId() + "');" +
                                    "sessionStorage.setItem('permission', '" + permissionTypes + "');" +
                                    "if (sessionStorage.getItem('roleType') === 'ADMIN') {" +
                                    "window.location.href = '" + request.getContextPath() + "/admin/dashboard';" +
                                    "} else {" +
                                    "alert('Chúc mừng! Bạn đã đăng ký tài khoản thành công');" +
                                    "window.location.href = '" + request.getContextPath() + "/home?success=Registration and login successful';" +
                                    "}" +
                                    "</script>";

                            response.setContentType("text/html");
                            response.getWriter().write(script);
                        } else {
                            response.sendRedirect(request.getContextPath() + "/login?message=Registration successful. Please login.");
                        }
                    } else {
                        response.sendRedirect(request.getContextPath() + "/login?error=Registration failed");
                    }
                } else {
                    // User doesn't exist and not registering
                    response.sendRedirect(request.getContextPath() + "/login?error=Account does not exist. Please register first.");
                }
            } else {
                if ("register".equals(googleAction)) {
                    // User already exists and trying to register
                    response.sendRedirect(request.getContextPath() + "/login?error=Account already exists. Please login.");
                } else {
                    // User exists, check status and login
                    if ("PENDING".equals(user.getStatus())) {
                        // Activate the account for Google users
                        authService.activateUserAccount(user.getId());
                        // Refresh user data
                        user = authService.getUserByEmail(email);
                    }

                    if ("BANNED".equals(user.getStatus())) {
                        response.sendRedirect(request.getContextPath() + "/login?error=Account is banned.");
                        return;
                    }

                    if ("DEACTIVE".equals(user.getStatus())) {
                        response.sendRedirect(request.getContextPath() + "/login?error=Account is deactivated.");
                        return;
                    }

                    // Login successful
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    List<Permission> permissions = authService.getPermissionsByRoleId(user.getRole().getId());
                    List<String> permissionTypes = permissions.stream()
                            .map(permission -> permission.getType().toString())
                            .collect(Collectors.toList());
                    // Create a script to store basic user info in sessionStorage - FIXED
                    String script = "<script>" +
                            "sessionStorage.setItem('roleType', '" + user.getRole().getRoleType() + "');" +
                            "sessionStorage.setItem('sessionId', '" + session.getId() + "');" +
                            "sessionStorage.setItem('userId', '" + user.getId() + "');" +
                            "sessionStorage.setItem('roleId', '" + user.getRole().getId() + "');" +
                            "sessionStorage.setItem('permission', '" + permissionTypes + "');" +
                            "if (sessionStorage.getItem('roleType') === 'ADMIN') {" +
                            "window.location.href = '" + request.getContextPath() + "/admin/dashboard';" +
                            "} else {" +
                            "window.location.href = '" + request.getContextPath() + "/home?success=Login successful';" +
                            "}" +
                            "</script>";

                    response.setContentType("text/html");
                    response.getWriter().write(script);
                }
            }
            request.getSession().removeAttribute("google_state");
            request.getSession().removeAttribute("google_action");

        } catch (Exception e) {
            System.err.println("Error in Google callback: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/login?error=Google login failed");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}