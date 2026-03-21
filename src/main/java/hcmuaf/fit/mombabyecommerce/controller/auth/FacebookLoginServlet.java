package hcmuaf.fit.mombabyecommerce.controller.auth;

import hcmuaf.fit.mombabyecommerce.config.ConfigLoader;
import hcmuaf.fit.mombabyecommerce.config.EnvConfig;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;

public class FacebookLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final String FACEBOOK_APP_ID = EnvConfig.get("FACEBOOK_APP_ID");

    private String redirectUri;

    @Override
    public void init() throws ServletException {
        String hostProduct = ConfigLoader.get("host.dev");
        this.redirectUri = hostProduct + "/facebook-callback";
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mode = request.getParameter("mode");
        if(mode == null){
            mode = "login";
        }
        String state = UUID.randomUUID().toString();
        HttpSession session = request.getSession(true);
        System.out.println("Setting session state: " + state);
        System.out.println("Setting fbAuthMode: " + mode);
        session.setAttribute("facebook_state", state);
        session.setAttribute("fbAuthMode", mode);

        System.out.println("Verify session state: " + session.getAttribute("facebook_state"));
        System.out.println("Verify fbAuthMode: " + session.getAttribute("fbAuthMode"));
        System.out.println("Session ID: " + session.getId());

        String authUrl = "https://www.facebook.com/v18.0/dialog/oauth" +
                "?client_id=" + FACEBOOK_APP_ID +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, "UTF-8") +
                "&state=" + state +
                "&scope=email,public_profile";
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        response.sendRedirect(authUrl);
        }
}
