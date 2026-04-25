package hcmuaf.fit.mombabyecommerce.controller.auth;

import hcmuaf.fit.mombabyecommerce.config.ConfigLoader;
import hcmuaf.fit.mombabyecommerce.config.EnvConfig;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/register-google")
public class GoogleRegisterServlet extends HttpServlet {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private static final String AUTH_ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String SCOPE = "email profile";

    @Override
    public void init() throws ServletException {
        try {
            this.clientId = EnvConfig.get("GOOGLE_CLIENT_ID");
            this.clientSecret = EnvConfig.get("GOOGLE_CLIENT_SECRET");
            String hostProduct = ConfigLoader.get("host.dev");
            this.redirectUri = hostProduct + "/google-callback";
            if (clientId == null || clientId.trim().isEmpty()) {
                throw new ServletException("Google Client ID is not configured properly");
            }
            System.out.println("GoogleLogin initialized with Client ID: " + clientId);
        } catch (Exception e) {
            throw new ServletException("Error initializing Google Login: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            if (clientId == null || clientId.trim().isEmpty()) {
                throw new ServletException("Google Client ID is not configured");
            }

            String state = UUID.randomUUID().toString();
            HttpSession session = request.getSession();
            session.setAttribute("google_state", state);
            session.setAttribute("google_action", "register");

            String url = String.format("%s?client_id=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s&access_type=offline&prompt=consent",
                    AUTH_ENDPOINT,
                    java.net.URLEncoder.encode(clientId, "UTF-8"),
                    java.net.URLEncoder.encode(redirectUri, "UTF-8"),
                    java.net.URLEncoder.encode(SCOPE, "UTF-8"),
                    state
            );

            System.out.println("Redirecting to Google OAuth URL for registration: " + url);
            response.sendRedirect(url);
        } catch (Exception e) {
            System.err.println("Error in Google Register: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/auth?error=" +
                    java.net.URLEncoder.encode("Error initializing Google Register: " + e.getMessage(), "UTF-8"));
        }
    }

}
