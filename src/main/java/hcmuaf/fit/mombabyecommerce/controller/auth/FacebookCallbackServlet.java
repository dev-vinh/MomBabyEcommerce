package hcmuaf.fit.mombabyecommerce.controller.auth;

import hcmuaf.fit.mombabyecommerce.Dao.UserDao;
import hcmuaf.fit.mombabyecommerce.config.ConfigLoader;
import hcmuaf.fit.mombabyecommerce.config.EnvConfig;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.contant.ERole;
import hcmuaf.fit.mombabyecommerce.model.Permission;
import hcmuaf.fit.mombabyecommerce.model.Role;
import hcmuaf.fit.mombabyecommerce.model.User;
import hcmuaf.fit.mombabyecommerce.service.AuthService;
import hcmuaf.fit.mombabyecommerce.service.UserService;
import hcmuaf.fit.mombabyecommerce.util.FacebookUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import org.jdbi.v3.core.Jdbi;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@WebServlet("/facebook-callback")
public class FacebookCallbackServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final String FACEBOOK_APP_ID = EnvConfig.get("FACEBOOK_APP_ID");
    private final String FACEBOOK_APP_SECRET = EnvConfig.get("FACEBOOK_APP_SECRET");

    private String redirectUri;
    private Jdbi jdbi;
    private AuthService authService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        String host = ConfigLoader.get("host.dev");
        this.redirectUri = host + "/facebook-callback";

        jdbi = DBConnection.getJdbi();
        authService = new AuthService(jdbi);
        userService = new UserService(jdbi);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession();

            String code = request.getParameter("code");
            String state = request.getParameter("state");

            String sessionState = (String) session.getAttribute("facebook_state");
            String fbAction = (String) session.getAttribute("fbAuthMode");

            if (state == null || !state.equals(sessionState)) {
                response.sendRedirect(request.getContextPath() + "/login?error=Invalid state parameter");
                return;
            }

            if (code == null || code.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/login?error=No authorization code");
                return;
            }

            String accessToken = FacebookUtil.getAccessToken(FACEBOOK_APP_ID, FACEBOOK_APP_SECRET, redirectUri, code);
            JSONObject fbUser = FacebookUtil.getUserData(accessToken);

            String facebookId = fbUser.getString("id");
            String name = fbUser.getString("name");

            boolean isRegister = "register".equals(fbAction);

            User user = jdbi.withExtension(UserDao.class, dao -> {
                User existing = dao.getUserByFacebookId(facebookId);

                // nếu login
//                if (!isRegister) {
//                    return existing;
//                }

                // trường hợp đăng ký
                if (existing != null) {
                    return existing;
                }

                // Tạo tài khoản mới ảo qua facebook
                String generatedEmail = facebookId + "@facebook.local";
                String randomPassword = UUID.randomUUID().toString();

                authService.registerWithFacebookActive(name, name, generatedEmail, randomPassword, facebookId);
                return dao.getUserByFacebookId(facebookId);
            });
            // kiểm tra user
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login?error=" +
                        URLEncoder.encode("Tài khoản Facebook chưa tồn tại", StandardCharsets.UTF_8));
                return;
            }

            Role role = user.getRole();
            if (role == null) {
                String roleType = "USER";
                Role existingRole = jdbi.withExtension(UserDao.class, dao -> dao.getRoleByUserId(user.getId()));

                if (existingRole != null) {
                    role = existingRole;
                } else {
                    Role defaultRole = jdbi.withExtension(UserDao.class, dao -> dao.getRoleByUserType(roleType));
                    if (defaultRole == null) {
                        response.sendRedirect(request.getContextPath() + "/login?error=Role USER not found in database");
                        return;
                    }
                    jdbi.useExtension(UserDao.class, dao -> dao.assignRole(user.getId(), roleType));
                    role = defaultRole;
                }
                user.setRole(role);
            }

            List<Permission> permissions = authService.getPermissionsByRoleId(user.getRole().getId());
            List<String> permissionTypes = permissions.stream()
                    .map(p -> p.getType().toString())
                    .collect(Collectors.toList());

            // lưu thông tin người dùng vào session
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("roleType", user.getRole().getRoleType().name());
            session.setAttribute("permissions", permissionTypes); // Thêm quyền vào session
            session.setMaxInactiveInterval(30 * 60);

            // 3. Xử lý refresh flag nếu cần
            if (Boolean.TRUE.equals(user.getNeedRefresh())) {
                userService.updateNeedRefresh(user.getId(), false);
            }
            // 8. CLEAN TEMP DATA
            session.removeAttribute("facebook_state");
            session.removeAttribute("fbAuthMode");

            String roleName = user.getRole().getRoleType().name();
            if (roleName.equals(ERole.SUPER_ADMIN.name())) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/home");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/login?error=Facebook login failed: " + e.getMessage());
        }
    }
}