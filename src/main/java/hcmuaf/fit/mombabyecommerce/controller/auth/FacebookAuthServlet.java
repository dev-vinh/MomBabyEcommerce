package hcmuaf.fit.mombabyecommerce.controller.auth;

import hcmuaf.fit.mombabyecommerce.config.ConfigLoader;
import hcmuaf.fit.mombabyecommerce.config.EnvConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@WebServlet("/facebook-auth")
public class FacebookAuthServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final String FACEBOOK_APP_ID = EnvConfig.get("FACEBOOK_APP_ID");
    private String redirectUri;

    @Override
    public void init() throws ServletException {
        String host = ConfigLoader.get("host.dev");
        this.redirectUri = host + "/facebook-callback";
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//lấy mode
        String mode = request.getParameter("mode");

        if (mode == null || (!mode.equals("login") && !mode.equals("register"))) {
            mode = "login";
        }
        String state = UUID.randomUUID().toString();

        HttpSession session = request.getSession(true);

        session.setAttribute("facebook_state", state);
        session.setAttribute("fbAuthMode", mode);

        System.out.println("FacebookAuth START:");
        System.out.println("Mode = " + mode);
        System.out.println("State = " + state);
        System.out.println("Session ID = " + session.getId());

        String authUrl = "https://www.facebook.com/v22.0/dialog/oauth"
                + "?client_id=" + FACEBOOK_APP_ID
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
                + "&state=" + state
                + "&scope=public_profile";

        System.out.println("Redirect URL: " + authUrl);

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        response.sendRedirect(authUrl);
    }
}