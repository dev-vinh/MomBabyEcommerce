package hcmuaf.fit.mombabyecommerce.controller.auth;

import hcmuaf.fit.mombabyecommerce.config.ConfigLoader;
import hcmuaf.fit.mombabyecommerce.config.EnvConfig;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/login-google")
public class GoogleLogin extends HttpServlet {
    String clientId = EnvConfig.get("GOOGLE_CLIENT_ID");
    String clientSecret = EnvConfig.get("GOOGLE_CLIENT_SECRET");
    private String redirectUri;
    private static final String SCOPE = "email profile";
    private static final String AUTH_ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth";

    @Override
    public void init() throws ServletException {
        try {
            String hostProduct = ConfigLoader.get("host.dev");
            this.redirectUri = hostProduct + "/MomBabyEcommerce_war/google-callback";
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
    try{
        if (clientId == null || clientId.trim().isEmpty()) {
            throw new ServletException("Google Client ID is not configured");
        }
        String state = UUID.randomUUID().toString();
        request.getSession().setAttribute("google_state", state);
        request.getSession().setAttribute("google_action", "login");

        String authUrl = AUTH_ENDPOINT +
                "?client_id=" + java.net.URLEncoder.encode(clientId, "UTF-8") +
                "&redirect_uri=" + java.net.URLEncoder.encode(redirectUri, "UTF-8") +
                "&response_type=code" +
                "&scope=" + java.net.URLEncoder.encode(SCOPE, "UTF-8") +
                "&state=" + state +
                "&access_type=offline" +
                "&prompt=consent";

        System.out.println("Redirecting to Google OAuth URL: " + authUrl);
        response.sendRedirect(authUrl);
    }catch(Exception e){
        System.err.println("Error in Google Login: " + e.getMessage());
        e.printStackTrace();
        response.sendRedirect(request.getContextPath() + "/auth/auth.jsp?error=" +
                java.net.URLEncoder.encode("Error initializing Google Login: " + e.getMessage(), "UTF-8"));
    }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
