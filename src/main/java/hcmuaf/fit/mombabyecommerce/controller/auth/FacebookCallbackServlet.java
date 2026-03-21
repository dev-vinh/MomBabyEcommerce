package hcmuaf.fit.mombabyecommerce.controller.auth;

import hcmuaf.fit.mombabyecommerce.Dao.UserDao;
import hcmuaf.fit.mombabyecommerce.config.ConfigLoader;
import hcmuaf.fit.mombabyecommerce.config.EnvConfig;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Permission;
import hcmuaf.fit.mombabyecommerce.model.User;
import hcmuaf.fit.mombabyecommerce.service.AuthService;
import hcmuaf.fit.mombabyecommerce.util.FacebookUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jdbi.v3.core.Jdbi;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FacebookCallbackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final String FACEBOOK_APP_ID = EnvConfig.get("FACEBOOK_APP_ID");
    private final String FACEBOOK_APP_SECRET = EnvConfig.get("FACEBOOK_APP_SECRET");
    private String redirectUri;
    private Jdbi jdbi;
    private AuthService authService;

    @Override
    public void init() throws ServletException {
        String hostProduct = ConfigLoader.get("host.dev");
        this.redirectUri = hostProduct + "/facebook-callback";
        super.init();
        jdbi = DBConnection.getJdbi();
        authService = new AuthService(DBConnection.getJdbi());

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String code = request.getParameter("code");
            String state = request.getParameter("state");
            String sessionState = (String) request.getSession().getAttribute("facebook_state");
            String fbAction = (String) request.getSession().getAttribute("fbAuthMode");

            // Debug logging
            System.out.println("Request state: " + state);
            System.out.println("Session state: " + sessionState);
            System.out.println("Facebook action: " + fbAction);

            // Kiểm tra state parameter
            if (state == null || !state.equals(sessionState)) {
                System.out.println("State mismatch - Request: " + state + ", Session: " + sessionState);
                response.sendRedirect("https://modernhome.property" + "/login?error=Invalid state parameter");
                return;
            }

            if (code == null || code.isEmpty()) {
                response.sendRedirect("https://modernhome.property" + "/login?error=No authorization code received");
                return;
            }

            String accessToken = FacebookUtil.getAccessToken(
                    FACEBOOK_APP_ID, FACEBOOK_APP_SECRET, redirectUri, code);

            JSONObject fbUserData = FacebookUtil.getUserData(accessToken);

            String facebookId = fbUserData.getString("id");
            String name = fbUserData.getString("name");
            String email = fbUserData.optString("email", null);

            System.out.println("Facebook User Data - Name: " + name + ", Email: " + email + ", Action: " + fbAction);

            HttpSession session = request.getSession();

            boolean isRegistration = "register".equals(fbAction);
            System.out.println("Is Registration Mode: " + isRegistration);

            User user = jdbi.withExtension(UserDao.class, dao -> {
                if (email != null && !email.isEmpty()) {
                    User existingUser = dao.getUserByEmail(email);
                    if (existingUser != null) {
                        if (isRegistration) {
                            return null;
                        }
                        return existingUser;
                    }
                }

                if (isRegistration || email != null) {
                    String randomPassword = UUID.randomUUID().toString();
                    authService.registerWithFacebookActive(name, name, email, randomPassword,facebookId );
                }

                return null;
            });

            User newUser = authService.getUserByEmail(email);

            if (newUser == null) {
                if (isRegistration) {
                    response.sendRedirect("https://modernhome.property" + "/login?error=Không thể tạo tài khoản Facebook. Vui lòng thử lại.");
                } else {
                    response.sendRedirect("https://modernhome.property" + "/login?error=Tài khoản Facebook này chưa được đăng ký.");
                }
                return;
            }

            System.out.println("User before setting session - ID: " + newUser.getId() + ", Role: " + newUser.getRole());

            session.setAttribute("loggedInUser", newUser);
            session.setAttribute("isLoggedIn", true);
            session.setAttribute("userRole", newUser.getRole());
            session.setAttribute("userId", newUser.getId());
            session.setMaxInactiveInterval(30 * 60);

            System.out.println("Session after setting - UserRole: " + session.getAttribute("userRole"));
            System.out.println("Session after setting - UserId: " + session.getAttribute("userId"));

            List<Permission> permissions = authService.getPermissionsByRoleId(newUser.getRole().getId());
            List<String> permissionTypes = permissions.stream()
                    .map(permission -> permission.getType().toString())
                    .collect(Collectors.toList());

            // JavaScript: set sessionStorage và redirect
            String script = "<script>" +
                    "sessionStorage.setItem('roleType', '" + newUser.getRole().getRoleType() + "');" +
                    "sessionStorage.setItem('sessionId', '" + session.getId() + "');" +
                    "sessionStorage.setItem('userId', '" + newUser.getId() + "');" +
                    "sessionStorage.setItem('roleId', '" + newUser.getRole().getId() + "');" +
                    "sessionStorage.setItem('permission', '" + permissionTypes + "');" +
                    "if (sessionStorage.getItem('role') === 'ADMIN') {" +
                    "   window.location.href = '" + "https://modernhome.property" + "/admin/dashboard';" +
                    "} else {" +
                    "alert('Chúc mừng! Bạn đã đăng ký tài khoản thành công');" +
                    "   window.location.href = '" + "https://modernhome.property" + "/home?success=Login successful';" +
                    "}" +
                    "</script>";

            response.setContentType("text/html");
            response.getWriter().write(script);

            // Xóa state
            request.getSession().removeAttribute("facebook_state");
            request.getSession().removeAttribute("fbAuthMode");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("https://modernhome.property" + "/login?error=Facebook login failed: " + e.getMessage());
        }
    }
}
