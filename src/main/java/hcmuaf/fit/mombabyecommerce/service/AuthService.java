package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.PermissionDao;
import hcmuaf.fit.mombabyecommerce.Dao.UserDao;
import hcmuaf.fit.mombabyecommerce.contant.ERole;
import hcmuaf.fit.mombabyecommerce.model.Permission;
import hcmuaf.fit.mombabyecommerce.model.Role;
import hcmuaf.fit.mombabyecommerce.model.User;
import hcmuaf.fit.mombabyecommerce.util.HashUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.UUID;

public class AuthService {
    private UserDao userDAO;
    private PermissionDao permissionDAO;
    public AuthService(Jdbi jdbi) {

        this.userDAO = jdbi.onDemand(UserDao.class);
        this.permissionDAO = jdbi.onDemand(PermissionDao.class);
    }

    public String  register(String fullName, String displayName, String email, String password) {
        if (userDAO.getUserByEmail(email) != null) {
            return null;
        }
        String confirmationToken  = UUID.randomUUID().toString();
        String salt = HashUtils.generateSalt();
        String hashedPassword = HashUtils.hashWithSalt(password, salt);
        int userId = userDAO.createUser(fullName, displayName, email, hashedPassword, salt,confirmationToken );
        if (userId > 0) {
            userDAO.assignRole(userId, ERole.USER.name());
        }
        return confirmationToken;
    }


    public User login(String email, String password) {
        User user = userDAO.getUserByEmail(email);

        if (user != null) {
            String storedSalt = user.getSalt();
            String storedHashedPassword = user.getPasswordUsername();
            String hashedPassword = HashUtils.hashWithSalt(password, storedSalt);

            if (hashedPassword.equals(storedHashedPassword)
                    && "ACTIVE".equalsIgnoreCase(user.getStatus())) {

                String roleStr = userDAO.getHighestRole(user.getId());

                if (roleStr != null) {
                    Role role = new Role();
                    role.setRoleType(ERole.valueOf(roleStr.toUpperCase()));
                    user.setRole(role);
                }

                return user;
            }
        }

        return null;
    }


    public boolean changePassword(Integer userId, String oldPassword, String newPassword, boolean verifyOldPassword) {
        User user = userDAO.getPasswordByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        if (verifyOldPassword) {
            String storedSalt = user.getSalt();
            String storedHashedPassword = user.getPasswordUsername();

            String hashedPassword = HashUtils.hashWithSalt(oldPassword, storedSalt);

            if (!hashedPassword.equals(storedHashedPassword)) {
                throw new IllegalArgumentException("Current password is incorrect");
            }
        }

        String newSalt = HashUtils.generateSalt();
        String hashedNewPassword = HashUtils.hashWithSalt(newPassword, newSalt);

        return userDAO.updatePassword(userId, hashedNewPassword, newSalt) > 0;
    }

    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

    public User getUserById(Integer userId) {
        return userDAO.getUserById(userId);
    }

    public boolean verifySession(HttpServletRequest request, String sessionId) {
        HttpSession session = request.getSession(false);  // Lấy session hiện tại, nếu không có thì trả về null
        if (session != null) {
            String storedSessionId = (String) session.getAttribute("sessionId");
            return storedSessionId != null && storedSessionId.equals(sessionId);
        }
        return false;
    }
    public void activateUserAccount(Integer userId) {
        userDAO.updateUserStatus(userId, "ACTIVE");
        }


    public boolean confirmAccount(String token) {

        User user = userDAO.getUserByConfirmationToken(token);
        if (user != null && "PENDING".equals(user.getStatus())) {
            int updated = userDAO.updateUserStatusByToken(token, "ACTIVE");
            return updated > 0;
        }
        return false;
    }
    public void saveSessionId(HttpServletRequest request, String email, String sessionId) {
        HttpSession session = request.getSession();
        session.setAttribute("sessionId", sessionId);
        session.setAttribute("email", email);  // Lưu email vào session nếu cần thiết
    }

// new code
public boolean registerWithGoogleActive(String fullName, String displayName, String email, String googleId) {
    User existingUser = userDAO.getUserByEmail(email);
    if (existingUser != null) {
        userDAO.linkGoogleAccount(email, googleId);
        String roleStr = userDAO.getHighestRole(existingUser.getId());

        if (roleStr == null) {
            userDAO.assignRole(existingUser.getId(), ERole.USER.name());
        }
        return true;
    }
    try {
        String dummyPass = "GOOGLE_USER_" + UUID.randomUUID().toString().substring(0, 10);
        int newUserId = userDAO.createUserGoogle(fullName, displayName, email, googleId, dummyPass);

        if (newUserId > 0) {
            userDAO.assignRole(newUserId, ERole.USER.name());
            return true;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return false;
}
    public List<Permission> getPermissionsByRoleId(Integer roleId) {
        return permissionDAO.getPermissionsByRoleId(roleId);
    }
    public User enrichUserWithRole(User user) {
        if (user == null) return null;

        List<Role> roles = userDAO.getUserRoleObjects(user.getId());
        if (roles != null && !roles.isEmpty()) {
            user.setRole(roles.get(0));
        }
        return user;
    }
}
